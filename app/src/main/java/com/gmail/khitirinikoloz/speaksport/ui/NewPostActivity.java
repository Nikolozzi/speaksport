package com.gmail.khitirinikoloz.speaksport.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GestureDetectorCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.gmail.khitirinikoloz.speaksport.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class NewPostActivity extends AppCompatActivity {
    private static final String LOG_TAG = NewPostActivity.class.getSimpleName();
    private static final int NUM_PAGES = 2;
    private ViewPager2 viewPager;
    private GestureDetectorCompat detector;
    private static List<String> sportsNames;
    private View popUpWindowView;
    private PopupWindow popupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_title);

        ((TextView) getSupportActionBar().getCustomView().findViewById(R.id.action_bar_title))
                .setText(R.string.new_post_title);

        detector = new GestureDetectorCompat(this, new MyGestureListener());
        viewPager = findViewById(R.id.post_pager);
        final FragmentStateAdapter pagerAdapter = new NewPostPagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tab_layout);

        new TabLayoutMediator(tabLayout, viewPager, (tab, pos) -> {
            if (pos == 0) {
                tab.setIcon(R.drawable.event);
                tab.setText("Event");
            } else {
                tab.setIcon(R.drawable.reg_post);
                tab.setText("Post");
            }
        }).attach();

        this.preparePopupWindow();
    }

    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() == 0)
            super.onBackPressed();
        else
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        detector.onTouchEvent(ev);
        return super.dispatchTouchEvent(ev);
    }

    private static class NewPostPagerAdapter extends FragmentStateAdapter {
        NewPostPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            if (position == 0)
                return new EventPostFragment();

            return new RegularPostFragment();
        }

        @Override
        public int getItemCount() {
            return NUM_PAGES;
        }
    }

    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            float sensitivity = 30;

            if (Math.abs(e1.getY() - e2.getY()) > sensitivity) {
                hideKeyboard();
                return true;
            }

            return super.onFling(e1, e2, velocityX, velocityY);
        }
    }

    private void hideKeyboard() {
        final View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void preparePopupWindow() {
        final View rootView = findViewById(R.id.post_activity_layout);
        popUpWindowView = LayoutInflater.from(this)
                .inflate(R.layout.sports_list_window, (ViewGroup) rootView, false);

        popupWindow = new PopupWindow(popUpWindowView,
                WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        popupWindow.setAnimationStyle(R.style.popup_window_animation);

        final ImageView closeView = popUpWindowView.findViewById(R.id.close_window_button);
        closeView.setOnClickListener(v -> popupWindow.dismiss());

        sportsNames = new ArrayList<>(Arrays.asList(getResources()
                .getStringArray(R.array.sports_names_list)));
        Collections.sort(sportsNames);
    }

    protected void setUpPopupWindow(final TextInputEditText topicEditText) {
        final RecyclerView recyclerView = popUpWindowView.findViewById(R.id.sports_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        final SportsAdapter sportsAdapter = new SportsAdapter(sportsNames, popupWindow, topicEditText);
        recyclerView.setAdapter(sportsAdapter);
    }

    protected View getPopUpWindowView() {
        return popUpWindowView;
    }

    protected PopupWindow getPopupWindow() {
        return popupWindow;
    }
}
