package com.gmail.khitirinikoloz.speaksport.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.gmail.khitirinikoloz.speaksport.R;
import com.gmail.khitirinikoloz.speaksport.repository.login.SessionManager;
import com.gmail.khitirinikoloz.speaksport.ui.login.LoginActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, FragmentManager.OnBackStackChangedListener {

    private SessionManager sessionManager;
    private NavigationView navigationView;
    private ImageView toolbarImg;
    private BottomNavigationView navView;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_title);

        navView = findViewById(R.id.nav_view);
        //navView.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_bookmarks, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        toolbarImg = findViewById(R.id.toolbar_img);
        toolbarImg.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));

        navigationView = findViewById(R.id.side_nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //open different navigation drawers depending on whether the user is logged in or not
        sessionManager = new SessionManager(this);
        if (!sessionManager.isUserLoggedIn()) {
            this.loadLoggedOutNavigation();
            toolbarImg.setImageDrawable(ContextCompat.getDrawable(this,
                    R.drawable.ic_not_signed_user));
        } else {
            this.loadLoggedInNavigation();
            toolbarImg.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.avatar));
        }

        invalidateOptionsMenu();

        fragmentManager = getSupportFragmentManager();
        fragmentManager.addOnBackStackChangedListener(this);
    }

    @Override
    public void onBackStackChanged() {
        if (fragmentManager.getBackStackEntryCount() == 0 && navView.getVisibility() == View.GONE)
            navView.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_menu, menu);
        MenuItem item = menu.findItem(R.id.action_add);
        if (!sessionManager.isUserLoggedIn())
            item.setVisible(false);
        else
            item.setVisible(true);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_add) {
            Intent intent = new Intent(this, NewPostActivity.class);
            startActivity(intent);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.user_sign_up: {
                Intent loginIntent = new Intent(this, LoginActivity.class);
                startActivity(loginIntent);
                break;
            }
            case R.id.logout: {
                sessionManager.logoutUser();
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
}
