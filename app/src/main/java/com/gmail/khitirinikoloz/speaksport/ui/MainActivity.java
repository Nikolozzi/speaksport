package com.gmail.khitirinikoloz.speaksport.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.gmail.khitirinikoloz.speaksport.R;
import com.gmail.khitirinikoloz.speaksport.model.User;
import com.gmail.khitirinikoloz.speaksport.ui.bookmarks.BookmarkViewModelFactory;
import com.gmail.khitirinikoloz.speaksport.ui.bookmarks.BookmarksFragment;
import com.gmail.khitirinikoloz.speaksport.ui.bookmarks.BookmarksViewModel;
import com.gmail.khitirinikoloz.speaksport.ui.home.HomeFragment;
import com.gmail.khitirinikoloz.speaksport.ui.login.LoggedInUser;
import com.gmail.khitirinikoloz.speaksport.ui.login.LoginActivity;
import com.gmail.khitirinikoloz.speaksport.ui.login.SessionManager;
import com.gmail.khitirinikoloz.speaksport.ui.notifications.NotificationsFragment;
import com.gmail.khitirinikoloz.speaksport.ui.post.FullScreenPostFragment;
import com.gmail.khitirinikoloz.speaksport.ui.post.util.PostHelper;
import com.gmail.khitirinikoloz.speaksport.ui.profile.ProfileActivity;
import com.gmail.khitirinikoloz.speaksport.ui.profile.util.ImageUtil;
import com.gmail.khitirinikoloz.speaksport.ui.subscriptions.SubscriptionsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import static com.gmail.khitirinikoloz.speaksport.repository.Constants.CREATED;
import static com.gmail.khitirinikoloz.speaksport.ui.post.FullScreenPostFragment.FRAGMENT_TAG;
import static com.gmail.khitirinikoloz.speaksport.ui.profile.ProfileActivity.ACTION_USER_UPDATE_BROADCAST;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, FragmentManager.OnBackStackChangedListener,
        CommentFragment.OnPublishCommentCallBack {

    private static final int PROFILE_REQUEST_CODE = 1;
    private FragmentManager fragmentManager = getSupportFragmentManager();
    private HomeFragment homeFragment = new HomeFragment();
    private SubscriptionsFragment subscriptionsFragment = new SubscriptionsFragment();
    private BookmarksFragment bookmarksFragment = new BookmarksFragment();
    private NotificationsFragment notificationsFragment = new NotificationsFragment();
    private Fragment activeFragment = homeFragment;

    private SessionManager sessionManager;
    private NavigationView navigationView;
    private ImageView toolbarImg;
    private BottomNavigationView bottomNavigationView;
    private UpdatedUserReceiver updatedUserReceiver;
    private Toolbar toolbar;
    private TextView actionBarText;
    private FloatingActionButton floatingActionButton;
    private BookmarksViewModel bookmarksViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_title);
        actionBarText = getSupportActionBar().getCustomView().findViewById(R.id.action_bar_title);

        fragmentManager.beginTransaction().add(R.id.fragment_container, notificationsFragment,
                "notifications").hide(notificationsFragment).commit();
        fragmentManager.beginTransaction().add(R.id.fragment_container, bookmarksFragment,
                "bookmarks").hide(bookmarksFragment).commit();
        fragmentManager.beginTransaction().add(R.id.fragment_container, subscriptionsFragment,
                "subscribers").hide(subscriptionsFragment).commit();
        fragmentManager.beginTransaction().add(R.id.fragment_container, homeFragment, "home").commit();

        bottomNavigationView = findViewById(R.id.nav_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(this::selectFragment);

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        toolbarImg = findViewById(R.id.toolbar_img);
        toolbarImg.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));
        floatingActionButton = findViewById(R.id.add_fab);

        navigationView = findViewById(R.id.side_nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //open different navigation drawers depending on whether the user is logged in or not
        sessionManager = new SessionManager(this);
        if (!sessionManager.isUserLoggedIn()) {
            this.loadLoggedOutNavigation();
            floatingActionButton.setVisibility(View.GONE);
            bottomNavigationView.setVisibility(View.GONE);
            toolbarImg.setImageDrawable(ContextCompat.getDrawable(this,
                    R.drawable.ic_not_signed_user));
        } else
            this.setUpLoggedInView();

        invalidateOptionsMenu();

        fragmentManager.addOnBackStackChangedListener(this);

        updatedUserReceiver =
                new UpdatedUserReceiver();
        LocalBroadcastManager.getInstance(this).registerReceiver
                (updatedUserReceiver, new IntentFilter(ACTION_USER_UPDATE_BROADCAST));

        bookmarksViewModel = new ViewModelProvider(this, new BookmarkViewModelFactory())
                .get(BookmarksViewModel.class);
        this.observeBookmarkResponse();
    }

    private void observeBookmarkResponse() {
        bookmarksViewModel.getBookmarkResponse().observe(this, responseCode -> {
            if (responseCode != null && responseCode == CREATED) {
                final ViewGroup viewGroup = (ViewGroup) ((ViewGroup) this
                        .findViewById(android.R.id.content)).getChildAt(0);
                PostHelper.showSnackBarSuccess(viewGroup, R.id.snackbar_bookmark_success,
                        "Successfully bookmarked", ContextCompat.getColor(this, R.color.color_success));
            }
        });
    }

    private boolean selectFragment(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_home:
                fragmentManager.beginTransaction().hide(activeFragment).show(homeFragment).commit();
                activeFragment = homeFragment;
                actionBarText.setText(R.string.title_home);
                return true;

            case R.id.navigation_subscriptions:
                fragmentManager.beginTransaction().hide(activeFragment).show(subscriptionsFragment).commit();
                activeFragment = subscriptionsFragment;
                actionBarText.setText(R.string.title_subscriptions);
                return true;

            case R.id.navigation_bookmarks:
                fragmentManager.beginTransaction().hide(activeFragment).show(bookmarksFragment).commit();
                activeFragment = bookmarksFragment;
                actionBarText.setText(R.string.title_bookmarks);
                return true;
            case R.id.navigation_notifications:
                fragmentManager.beginTransaction().hide(activeFragment).show(notificationsFragment).commit();
                activeFragment = notificationsFragment;
                actionBarText.setText(R.string.title_notifications);
                return true;
        }
        return false;
    }

    private void setUpLoggedInView() {
        final LoggedInUser user = sessionManager.getLoggedInUser();
        this.loadLoggedInNavigation();
        toolbarImg.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.avatar));
        TextView navUsername = navigationView.getHeaderView(0).findViewById(R.id.username_nav);
        navUsername.setText(user.getUsername());
    }

    @Override
    public void onBackStackChanged() {
        if (fragmentManager.getBackStackEntryCount() == 0) {
            if (bottomNavigationView.getVisibility() == View.GONE && sessionManager.isUserLoggedIn())
                bottomNavigationView.setVisibility(View.VISIBLE);
            if (floatingActionButton.getVisibility() == View.GONE && sessionManager.isUserLoggedIn())
                floatingActionButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_menu, menu);

        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.user_sign_up: {
                Intent loginIntent = new Intent(this, LoginActivity.class);
                startActivity(loginIntent);
                break;
            }
            case R.id.user_profile: {
                Intent profileIntent = new Intent(this, ProfileActivity.class);
                startActivityForResult(profileIntent, PROFILE_REQUEST_CODE);
                break;
            }
            case R.id.logout: {
                sessionManager.logOutUser();
                this.loadLoggedOutNavigation();
                invalidateOptionsMenu();
                toolbarImg.setImageDrawable(ContextCompat.getDrawable(this,
                        R.drawable.ic_not_signed_user));
                finish();
                startActivity(getIntent());
                break;
            }
            case R.id.settings_nav:
                break;
            //
        }
        return false;
    }

    @Override
    protected void onResume() {
        if (sessionManager != null) {
            final String imagePath = sessionManager.getLoggedInUser().getImagePath();
            if (imagePath != null) {
                final Bitmap bitmap = ImageUtil.loadImageFromStorage(imagePath);

                ImageView navImg = navigationView.getHeaderView(0).findViewById(R.id.img_nav);
                ImageView toolbarImg = toolbar.findViewById(R.id.toolbar_img);
                if (navImg != null && toolbarImg != null) {
                    navImg.setImageBitmap(bitmap);
                    toolbarImg.setImageBitmap(bitmap);
                }
            }
        }
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        if (updatedUserReceiver != null)
            LocalBroadcastManager.getInstance(this).unregisterReceiver(updatedUserReceiver);
        super.onDestroy();
    }

    private void loadLoggedOutNavigation() {
        navigationView.getMenu().clear();
        navigationView.inflateMenu(R.menu.side_nav_menu);
        navigationView.removeHeaderView(navigationView.getHeaderView(0));
        navigationView.inflateHeaderView(R.layout.nav_header_main);
    }

    private void loadLoggedInNavigation() {
        navigationView.getMenu().clear();
        navigationView.inflateMenu(R.menu.side_nav_user_menu);
        navigationView.removeHeaderView(navigationView.getHeaderView(0));
        navigationView.inflateHeaderView(R.layout.nav_header_user_main);
    }

    @Override
    public void onPublishComment(final String comment) {
        OnDispatchCommentCallBack dispatchCommentCallBack =
                (FullScreenPostFragment) getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG);

        if (dispatchCommentCallBack != null)
            dispatchCommentCallBack.onDispatchComment(comment);
    }

    private class UpdatedUserReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() != null && intent.getAction().equals(ACTION_USER_UPDATE_BROADCAST)) {
                User updatedUser = (User) intent.getSerializableExtra("user");
                LoggedInUser loggedInUser = sessionManager.getLoggedInUser();
                if (updatedUser != null) {
                    if (updatedUser.getUsername() != null)
                        loggedInUser.setUsername(updatedUser.getUsername());
                    if (updatedUser.getEmail() != null)
                        loggedInUser.setEmail(updatedUser.getEmail());

                    //update current login credentials
                    sessionManager.createLoginSession(loggedInUser);
                    setUpLoggedInView();
                }
            }
        }
    }

    public interface OnDispatchCommentCallBack {
        void onDispatchComment(final String comment);
    }
}
