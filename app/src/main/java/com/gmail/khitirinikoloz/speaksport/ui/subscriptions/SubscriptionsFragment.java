package com.gmail.khitirinikoloz.speaksport.ui.subscriptions;

import android.os.Bundle;
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
import com.gmail.khitirinikoloz.speaksport.ui.login.SessionManager;
import com.gmail.khitirinikoloz.speaksport.ui.post.adapter.PostAdapter;
import com.gmail.khitirinikoloz.speaksport.ui.post.util.EndlessRecyclerViewScrollListener;

public class SubscriptionsFragment extends Fragment {

    private static final String LOG_TAG = SubscriptionsFragment.class.getSimpleName();
    public static final String NAME = LOG_TAG;
    private static final int DEFAULT_PAGE_NUM = 0;
    private SubscriptionsViewModel subscriptionsViewModel;
    private SessionManager sessionManager;
    private long userId;
    private PostAdapter postAdapter;
    private EndlessRecyclerViewScrollListener scrollListener;
    private SwipeRefreshLayout swipeRefreshLayout;

    private TextView noSubscriptionsView;
    private ProgressBar progressBarMain;
    private ProgressBar progressBar;

    public SubscriptionsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_subscriptions, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        subscriptionsViewModel = new ViewModelProvider(this,
                new SubscriptionsViewModelFactory()).get(SubscriptionsViewModel.class);
        sessionManager = new SessionManager(requireContext());

        noSubscriptionsView = view.findViewById(R.id.no_subscriptions);
        progressBarMain = view.findViewById(R.id.progress_load_posts_main);
        progressBar = view.findViewById(R.id.progress_load_posts);

        if (sessionManager.isUserLoggedIn()) {
            userId = sessionManager.getLoggedInUser().getUserId();
            progressBarMain.setVisibility(View.VISIBLE);
            subscriptionsViewModel.getSubscribedPosts(userId, DEFAULT_PAGE_NUM);
        } else
            return;

        final RecyclerView recyclerView = view.findViewById(R.id.posts_list);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        postAdapter = new PostAdapter(getContext(), SubscriptionsFragment.class);
        postAdapter.setHasStableIds(true);
        recyclerView.setAdapter(postAdapter);

        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                getSubscribedPosts(userId, page);
            }
        };

        recyclerView.addOnScrollListener(scrollListener);

        swipeRefreshLayout = view.findViewById(R.id.refresh_posts);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            scrollListener.resetState();
            postAdapter.clearData();
            subscriptionsViewModel.getSubscribedPosts(userId, DEFAULT_PAGE_NUM);
            noSubscriptionsView.setVisibility(View.GONE);
        });

        this.observeSubscribedPosts();
    }


    private void getSubscribedPosts(final long userId, final int page) {
        progressBar.setVisibility(View.VISIBLE);
        progressBarMain.setVisibility(View.GONE);
        subscriptionsViewModel.getSubscribedPosts(userId, page);
    }

    private void observeSubscribedPosts() {
        subscriptionsViewModel.getSubscribedPostsResponseData().observe(getViewLifecycleOwner(), postResponses -> {
            if (postResponses != null) {
                if (postResponses.isEmpty())
                    noSubscriptionsView.setVisibility(View.VISIBLE);

                postAdapter.setPosts(postResponses);
                progressBar.setVisibility(View.GONE);
                progressBarMain.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }
}
