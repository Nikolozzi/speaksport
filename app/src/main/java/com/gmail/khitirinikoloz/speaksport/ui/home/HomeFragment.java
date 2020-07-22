package com.gmail.khitirinikoloz.speaksport.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.gmail.khitirinikoloz.speaksport.R;
import com.gmail.khitirinikoloz.speaksport.ui.MainActivity;
import com.gmail.khitirinikoloz.speaksport.ui.post.NewPostActivity;
import com.gmail.khitirinikoloz.speaksport.ui.post.adapter.PostAdapter;
import com.gmail.khitirinikoloz.speaksport.ui.post.util.EndlessRecyclerViewScrollListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class HomeFragment extends Fragment {
    private static final String LOG_TAG = HomeFragment.class.getSimpleName();
    public static final String NAME = LOG_TAG;
    private PostAdapter postAdapter;
    private HomeViewModel homeViewModel;
    private MainActivity mainActivity;
    private ProgressBar progressBarMain;
    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefreshLayout;
    private FloatingActionButton floatingActionButton;
    private EndlessRecyclerViewScrollListener scrollListener;
    private RecyclerView recyclerView;
    private int defaultPageNum = 0;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mainActivity = (MainActivity) requireActivity();
        homeViewModel = new ViewModelProvider(this, new HomeViewModelFactory())
                .get(HomeViewModel.class);
        homeViewModel.getPosts(defaultPageNum);
        progressBarMain = view.findViewById(R.id.progress_load_posts_main);
        progressBar = view.findViewById(R.id.progress_load_posts);
        progressBarMain.setVisibility(View.VISIBLE);
        floatingActionButton = mainActivity.findViewById(R.id.add_fab);
        swipeRefreshLayout = view.findViewById(R.id.refresh_posts);

        floatingActionButton.setOnClickListener(v -> openNewPostActivity());

        recyclerView = view.findViewById(R.id.posts_list);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        postAdapter = new PostAdapter(getContext(), HomeFragment.class);
        postAdapter.setHasStableIds(true);
        recyclerView.setAdapter(postAdapter);

        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                getPosts(page);
            }
        };

        recyclerView.addOnScrollListener(scrollListener);

        swipeRefreshLayout.setOnRefreshListener(() -> {
            scrollListener.resetState();
            postAdapter.clearData();
            homeViewModel.getPosts(defaultPageNum);
        });

        this.observePosts();

        @SuppressWarnings("ConstantConditions")
        // getSupportActionBar - it's set in MainActivity
        final TextView actionBarText = ((MainActivity) requireActivity()).getSupportActionBar()
                .getCustomView().findViewById(R.id.action_bar_title);
        actionBarText.setText(R.string.title_home);

        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.top_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);

        searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                recyclerView.clearOnScrollListeners();
                swipeRefreshLayout.setEnabled(false);
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                recyclerView.addOnScrollListener(scrollListener);
                swipeRefreshLayout.setEnabled(true);
                return true;
            }
        });

        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                postAdapter.getFilter().filter(newText);
                return false;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    private void openNewPostActivity() {
        final Intent intent = new Intent(mainActivity, NewPostActivity.class);
        startActivity(intent);
    }

    private void getPosts(int page) {
        progressBar.setVisibility(View.VISIBLE);
        homeViewModel.getPosts(page);
    }

    private void observePosts() {
        homeViewModel.getPostsResponse().observe(getViewLifecycleOwner(), postResponses -> {
            progressBarMain.setVisibility(View.GONE);
            if (postResponses != null) {
                postAdapter.setPosts(postResponses);
                progressBar.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }
}
