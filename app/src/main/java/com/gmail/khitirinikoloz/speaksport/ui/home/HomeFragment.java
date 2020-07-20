package com.gmail.khitirinikoloz.speaksport.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.gmail.khitirinikoloz.speaksport.R;
import com.gmail.khitirinikoloz.speaksport.ui.MainActivity;
import com.gmail.khitirinikoloz.speaksport.ui.post.adapter.PostAdapter;
import com.gmail.khitirinikoloz.speaksport.ui.post.util.EndlessRecyclerViewScrollListener;

public class HomeFragment extends Fragment {
    private static final String LOG_TAG = HomeFragment.class.getSimpleName();
    private PostAdapter postAdapter;
    private HomeViewModel homeViewModel;
    private MainActivity mainActivity;
    private ProgressBar progressBarMain;
    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefreshLayout;
    private EndlessRecyclerViewScrollListener scrollListener;
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
        swipeRefreshLayout = view.findViewById(R.id.refresh_posts);

        final RecyclerView recyclerView = view.findViewById(R.id.posts_list);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        postAdapter = new PostAdapter(getContext());
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
    }

    private void getPosts(int page){
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
