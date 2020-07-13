package com.gmail.khitirinikoloz.speaksport.ui.post.adapter;

import android.content.Context;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.gmail.khitirinikoloz.speaksport.R;
import com.gmail.khitirinikoloz.speaksport.repository.post.PostResponse;
import com.gmail.khitirinikoloz.speaksport.repository.signup.response.UserResponse;
import com.gmail.khitirinikoloz.speaksport.ui.MainActivity;
import com.gmail.khitirinikoloz.speaksport.ui.login.SessionManager;
import com.gmail.khitirinikoloz.speaksport.ui.post.FullScreenPostFragment;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    private List<PostResponse> posts;
    private final Context context;
    private final SessionManager sessionManager;
    public static final String USERNAME_KEY = "username";
    public static final String TITLE_KEY = "title";
    public static final String DESCRIPTION_KEY = "description";
    public static final String DATE_KEY = "date";
    public static final String LOCATION_KEY = "location";
    public static final String TOPIC_KEY = "topic";
    public static final String COMMENTS_KEY = "comments";
    public static final String SUBSCRIBERS_KEY = "subscribers";

    public PostAdapter(Context context) {
        posts = new ArrayList<>();
        this.context = context;
        sessionManager = new SessionManager(context);
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View postItem = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.post_item, parent, false);
        return new PostViewHolder(postItem, context);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        final PostResponse postResponse = posts.get(position);
        final UserResponse userResponse = postResponse.getUser();

        //for all posts, including regular ones
        holder.authorView.setText(userResponse.getUsername());
        holder.titleView.setText(postResponse.getTitle());
        holder.description = postResponse.getDescription();
        holder.topicView.setText(postResponse.getTopic());

        //set post publication time
        this.setPostTime(holder, postResponse.getPostedAt());
        //load icons with glide for better memory efficiency
        this.loadUserImage(holder, userResponse);

        Glide.with(context).load(R.drawable.topic).into(holder.topicImg);

        if (postResponse.isEvent()) {
            Glide.with(context).load(R.drawable.event).into(holder.eventImg);
            Glide.with(context).load(R.drawable.world).into(holder.locationImg);

            DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
            String formattedDateTime = dateFormat.format(postResponse.getStartTime()) + " - " +
                    dateFormat.format(postResponse.getEndTime());
            holder.dateView.setText(formattedDateTime);
            holder.locationView.setText(postResponse.getLocation());
            holder.eventSubscribersView.setText(String.valueOf(postResponse.getSubscribersNumber()));
            holder.commentView.setText(String.valueOf(postResponse.getCommentsNumber()));

            holder.eventLayout.setVisibility(View.VISIBLE);
            holder.goingLayout.setVisibility(View.VISIBLE);
            if (!sessionManager.isUserLoggedIn()) {
                holder.goingLayout.setEnabled(false);
            }
        }

        holder.moreButton.setOnClickListener(this::showPostMenu);
        holder.goingLayout.setOnClickListener(v -> increaseEventSubscribers(holder));
    }

    private void setPostTime(final PostViewHolder holder, final Date postedAt) {
        final long diff = Calendar.getInstance().getTime().getTime() -
                postedAt.getTime();
        long minutes = TimeUnit.MILLISECONDS.toMinutes(diff);
        String timeText = minutes + "m ago";
        if (minutes >= 60) {
            long hours = TimeUnit.MINUTES.toHours(minutes);
            timeText = hours + "h ago";
            if (hours >= 24) {
                long days = TimeUnit.HOURS.toDays(hours);
                timeText = days + "d ago";
            }
        }

        holder.publicationTime.setText(timeText);
    }

    private void loadUserImage(final PostViewHolder holder, final UserResponse userResponse) {
        if (userResponse.getPhoto() != null) {
            final String fileAsString = userResponse.getPhoto().getFileObject();
            if (fileAsString != null) {
                byte[] fileAsBytes = android.util.Base64.decode(fileAsString, Base64.DEFAULT);
                Glide.with(context).load(fileAsBytes).into(holder.avatarImg);
            } else
                Glide.with(context).load(R.drawable.avatar).into(holder.avatarImg);
        }
    }

    private void increaseEventSubscribers(@NonNull PostViewHolder holder) {
        //add the current user to the event, change the goingImg tint to green and increment the subscriber number.
        //If the api response returns the user is already subscribed, unsubscribe, change the
        // image tint back to default black and decrement the number.

        if (!holder.isSubscribed) {
            holder.goingImg.setColorFilter(ContextCompat.getColor(context, R.color.subscribed_img_tint));
            int currentSubscribers = Integer.parseInt(holder.eventSubscribersView.getText().toString());
            holder.eventSubscribersView.setText(String.valueOf(currentSubscribers + 1));

            holder.isSubscribed = true;
        } else {
            holder.goingImg.clearColorFilter();
            int currentSubscribers = Integer.parseInt(holder.eventSubscribersView.getText().toString());
            holder.eventSubscribersView.setText(String.valueOf(currentSubscribers - 1));

            holder.isSubscribed = false;
        }
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public void setPosts(List<PostResponse> posts) {
        this.posts = posts;
        notifyDataSetChanged();
    }

    private void showPostMenu(View view) {
        final PopupMenu postMenu = new PopupMenu(view.getContext(), view);
        postMenu.getMenuInflater().inflate(R.menu.post_menu, postMenu.getMenu());
        postMenu.show();
    }

    static class PostViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ImageView eventImg;
        private final ImageView locationImg;
        private final ImageView avatarImg;
        private final ImageView topicImg;
        private final ImageView goingImg;

        private final TextView authorView;
        private final TextView publicationTime;
        private final TextView titleView;
        private final TextView topicView;
        private final TextView dateView;
        private final TextView locationView;
        private final TextView commentView;
        private final TextView eventSubscribersView;
        //view container that holds event ImageView and TextView
        private final RelativeLayout eventLayout;
        private final RelativeLayout goingLayout;
        private final ImageButton moreButton;

        private boolean isSubscribed;
        private String description;
        private final Context context;

        PostViewHolder(@NonNull View itemView, @NonNull Context context) {
            super(itemView);
            this.context = context;
            locationImg = itemView.findViewById(R.id.event_location_pic);
            eventImg = itemView.findViewById(R.id.event_pic);
            avatarImg = itemView.findViewById(R.id.avatar);
            topicImg = itemView.findViewById(R.id.topic_pic);
            goingImg = itemView.findViewById(R.id.subscriber_pic);

            authorView = itemView.findViewById(R.id.username);
            publicationTime = itemView.findViewById(R.id.time);
            titleView = itemView.findViewById(R.id.main_text);
            topicView = itemView.findViewById(R.id.topic);
            dateView = itemView.findViewById(R.id.event_date);
            locationView = itemView.findViewById(R.id.event_location);
            commentView = itemView.findViewById(R.id.comment_number);
            eventSubscribersView = itemView.findViewById(R.id.subscriber_number);

            moreButton = itemView.findViewById(R.id.icon_more);
            eventLayout = itemView.findViewById(R.id.event_container);
            goingLayout = itemView.findViewById(R.id.event_subscribers_container);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            //make a call to the api to fetch this post's fields before opening the fullscreen view
            //temporary implementation
            Bundle bundle = new Bundle();
            bundle.putString(USERNAME_KEY, authorView.getText().toString());
            bundle.putString(TITLE_KEY, titleView.getText().toString());
            bundle.putString(DESCRIPTION_KEY, description);
            bundle.putString(DATE_KEY, dateView.getText().toString());
            bundle.putString(LOCATION_KEY, locationView.getText().toString());
            bundle.putString(TOPIC_KEY, topicView.getText().toString());
            bundle.putString(COMMENTS_KEY, commentView.getText().toString());
            bundle.putString(SUBSCRIBERS_KEY, eventSubscribersView.getText().toString());

            FullScreenPostFragment fullScreenPostFragment = new FullScreenPostFragment();
            fullScreenPostFragment.setArguments(bundle);
            MainActivity activity = (MainActivity) context;
            activity.getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right,
                            R.anim.enter_from_right, R.anim.exit_to_right)
                    .add(R.id.home_container, fullScreenPostFragment, null)
                    .addToBackStack(null)
                    .commit();
        }
    }
}
