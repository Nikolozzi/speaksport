package com.gmail.khitirinikoloz.speaksport.ui.bookmarks;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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

import static com.gmail.khitirinikoloz.speaksport.repository.Constants.DEFAULT_PAGE_NUM;
import static com.gmail.khitirinikoloz.speaksport.repository.Constants.SUCCESS;

public class BookmarksFragment extends Fragment {

    private static final String LOG_TAG = BookmarksFragment.class.getSimpleName();
    public static final String NAME = LOG_TAG;
    private EndlessRecyclerViewScrollListener scrollListener;
    private SwipeRefreshLayout swipeRefreshLayout;

    private long userId;
    private TextView noBookmarksView;
    private ProgressBar progressBarMain;
    private ProgressBar progressBar;

    private PostAdapter postAdapter;
    private BookmarksViewModel bookmarksViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_bookmarks, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bookmarksViewModel = new ViewModelProvider(requireActivity(), new BookmarkViewModelFactory())
                .get(BookmarksViewModel.class);

        progressBarMain = view.findViewById(R.id.progress_load_posts_main);
        progressBar = view.findViewById(R.id.progress_load_posts);
        noBookmarksView = view.findViewById(R.id.no_bookmarks);

        final SessionManager sessionManager = new SessionManager(requireContext());
        if (sessionManager.isUserLoggedIn()) {
            userId = sessionManager.getLoggedInUser().getUserId();
            progressBarMain.setVisibility(View.VISIBLE);
            bookmarksViewModel.getBookmarks(userId, DEFAULT_PAGE_NUM);
        } else
            return;

        final RecyclerView recyclerView = view.findViewById(R.id.posts_list);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        postAdapter = new PostAdapter(getContext(), BookmarksFragment.class);
        postAdapter.setHasStableIds(true);
        recyclerView.setAdapter(postAdapter);

        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                getBookmarkedPosts(userId, page);
            }
        };

        recyclerView.addOnScrollListener(scrollListener);

        swipeRefreshLayout = view.findViewById(R.id.refresh_posts);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            scrollListener.resetState();
            postAdapter.clearData();
            bookmarksViewModel.getBookmarks(userId, DEFAULT_PAGE_NUM);
            noBookmarksView.setVisibility(View.GONE);
        });

        this.observeBookmarkedPosts();
        this.observeDeletedBookmark();
    }

    private void getBookmarkedPosts(final long userId, final int page) {
        progressBar.setVisibility(View.VISIBLE);
        progressBarMain.setVisibility(View.GONE);
        bookmarksViewModel.getBookmarks(userId, page);
    }

    private void observeBookmarkedPosts() {
        bookmarksViewModel.getBookmarksResponse().observe(getViewLifecycleOwner(), postResponses -> {
            if (postResponses != null) {
                if (postResponses.isEmpty())
                    noBookmarksView.setVisibility(View.VISIBLE);

                postAdapter.setPosts(postResponses);
                progressBar.setVisibility(View.GONE);
                progressBarMain.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);
            }
        });

    }

    private void observeDeletedBookmark() {
        bookmarksViewModel.getDeletedBookmarkResponse().observe(getViewLifecycleOwner(), responseCode -> {
            if (responseCode == SUCCESS) {
                postAdapter.deletePost(bookmarksViewModel.getDeletedPostId());
                Toast.makeText(requireContext(), "Successfully removed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
