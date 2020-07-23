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
import com.gmail.khitirinikoloz.speaksport.ui.post.viewmodel.EventPostViewModel;
import com.gmail.khitirinikoloz.speaksport.ui.post.viewmodel.EventPostViewModelFactory;

import static com.gmail.khitirinikoloz.speaksport.repository.Constants.DEFAULT_PAGE_NUM;
import static com.gmail.khitirinikoloz.speaksport.repository.Constants.SUCCESS;

public class EventPostActivity extends AppCompatActivity {

    private static final String LOG_TAG = EventPostActivity.class.getSimpleName();
    public static final String NAME = LOG_TAG;

    private long userId;
    private TextView noEventsView;
    private ProgressBar progressBarMain;
    private ProgressBar progressBar;

    private PostAdapter postAdapter;
    private EventPostViewModel eventPostViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_post);
        setTitle(getString(R.string.events_title));

        eventPostViewModel = new ViewModelProvider(this, new EventPostViewModelFactory())
                .get(EventPostViewModel.class);

        progressBarMain = findViewById(R.id.progress_load_posts_main);
        progressBar = findViewById(R.id.progress_load_posts);
        noEventsView = findViewById(R.id.no_events);

        final SessionManager sessionManager = new SessionManager(this);
        userId = sessionManager.getLoggedInUser().getUserId();
        getEventPosts(userId, DEFAULT_PAGE_NUM);
        progressBarMain.setVisibility(View.VISIBLE);

        final RecyclerView recyclerView = findViewById(R.id.posts_list);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        postAdapter = new PostAdapter(this, EventPostActivity.class);
        postAdapter.setHasStableIds(true);
        recyclerView.setAdapter(postAdapter);

        EndlessRecyclerViewScrollListener scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                getEventPosts(userId, page);
            }
        };

        recyclerView.addOnScrollListener(scrollListener);
        this.observeEventPosts();
        this.observeDeletedEvent();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getEventPosts(final long userId, final int page) {
        progressBar.setVisibility(View.VISIBLE);
        progressBarMain.setVisibility(View.GONE);
        eventPostViewModel.getEvents(userId, page);
    }

    private void observeEventPosts() {
        eventPostViewModel.getEventPosts().observe(this, postResponses -> {
            if (postResponses != null) {
                if (postResponses.isEmpty())
                    noEventsView.setVisibility(View.VISIBLE);

                postAdapter.setPosts(postResponses);
                progressBar.setVisibility(View.GONE);
                progressBarMain.setVisibility(View.GONE);
            }
        });
    }

    private void observeDeletedEvent() {
        eventPostViewModel.getDeletedEventResponse().observe(this, responseCode -> {
            if (responseCode == SUCCESS) {
                postAdapter.deletePost(eventPostViewModel.getDeletedEventId());
                Toast.makeText(this, "Successfully removed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
