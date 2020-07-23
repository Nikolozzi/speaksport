package com.gmail.khitirinikoloz.speaksport.ui.post;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gmail.khitirinikoloz.speaksport.R;
import com.gmail.khitirinikoloz.speaksport.ui.login.SessionManager;
import com.gmail.khitirinikoloz.speaksport.ui.post.adapter.PostAdapter;
import com.gmail.khitirinikoloz.speaksport.ui.post.util.EndlessRecyclerViewScrollListener;
import com.gmail.khitirinikoloz.speaksport.ui.post.viewmodel.RegularPostViewModel;
import com.gmail.khitirinikoloz.speaksport.ui.post.viewmodel.RegularPostViewModelFactory;

import static com.gmail.khitirinikoloz.speaksport.repository.Constants.DEFAULT_PAGE_NUM;
import static com.gmail.khitirinikoloz.speaksport.repository.Constants.SUCCESS;

public class RegularPostActivity extends AppCompatActivity {
    private static final String LOG_TAG = RegularPostActivity.class.getSimpleName();
    public static final String NAME = LOG_TAG;

    private long userId;
    private TextView noPostsView;
    private ProgressBar progressBarMain;
    private ProgressBar progressBar;

    private PostAdapter postAdapter;
    private RegularPostViewModel regularPostViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regular_post);
        setTitle(getString(R.string.title_post));

        regularPostViewModel = new ViewModelProvider(this, new RegularPostViewModelFactory())
                .get(RegularPostViewModel.class);

        progressBarMain = findViewById(R.id.progress_load_posts_main);
        progressBar = findViewById(R.id.progress_load_posts);
        noPostsView = findViewById(R.id.no_reg_posts);

        final SessionManager sessionManager = new SessionManager(this);
        userId = sessionManager.getLoggedInUser().getUserId();
        getRegularPosts(userId, DEFAULT_PAGE_NUM);
        progressBarMain.setVisibility(View.VISIBLE);

        final RecyclerView recyclerView = findViewById(R.id.posts_list);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        postAdapter = new PostAdapter(this, RegularPostActivity.class);
        postAdapter.setHasStableIds(true);
        recyclerView.setAdapter(postAdapter);

        EndlessRecyclerViewScrollListener scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                getRegularPosts(userId, page);
            }
        };

        recyclerView.addOnScrollListener(scrollListener);
        this.observeEventPosts();
        this.observeDeletedPost();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getRegularPosts(final long userId, final int page) {
        progressBar.setVisibility(View.VISIBLE);
        progressBarMain.setVisibility(View.GONE);
        regularPostViewModel.getRegularPosts(userId, page);
    }

    private void observeEventPosts() {
        regularPostViewModel.getRegularPosts().observe(this, postResponses -> {
            if (postResponses != null) {
                if (postResponses.isEmpty())
                    noPostsView.setVisibility(View.VISIBLE);

                postAdapter.setPosts(postResponses);
                progressBar.setVisibility(View.GONE);
                progressBarMain.setVisibility(View.GONE);
            }
        });
    }

    private void observeDeletedPost() {
        regularPostViewModel.getDeletedPostResponse().observe(this, responseCode -> {
            if (responseCode == SUCCESS) {
                postAdapter.deletePost(regularPostViewModel.getDeletedPostId());
                Toast.makeText(this, "Successfully removed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
