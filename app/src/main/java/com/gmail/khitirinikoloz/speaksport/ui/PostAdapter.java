package com.gmail.khitirinikoloz.speaksport.ui;

import android.content.Context;
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
import com.gmail.khitirinikoloz.speaksport.data.login.SessionManager;
import com.gmail.khitirinikoloz.speaksport.entity.EventPost;
import com.gmail.khitirinikoloz.speaksport.model.Post;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    private final List<Post> posts;
    private final Context context;
    private final SessionManager sessionManager;

    public PostAdapter(Context context, List<Post> names) {
        posts = new ArrayList<>(names);
        this.context = context;
        sessionManager = new SessionManager(context);
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View postItem = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.post_item, parent, false);
        return new PostViewHolder(postItem);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post currentPost = posts.get(position);

        //for all posts, including regular ones
        holder.authorView.setText(R.string.username_default);
        holder.titleView.setText(currentPost.getTitle());
        holder.topicView.setText(currentPost.getTopic());
        //load icons with glide for better memory efficiency
        Glide.with(context).load(R.drawable.avatar).into(holder.avatarImg);
        Glide.with(context).load(R.drawable.topic).into(holder.topicImg);

        if (currentPost instanceof EventPost) {
            EventPost eventPost = (EventPost) currentPost;

            Glide.with(context).load(R.drawable.event).into(holder.eventImg);
            Glide.with(context).load(R.drawable.world).into(holder.locationImg);

            DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
            String formattedDateTime = dateFormat.format(eventPost.getStartTime().getTime()) + " - " +
                    dateFormat.format(eventPost.getEndTime().getTime());
            holder.dateView.setText(formattedDateTime);

            holder.eventLayout.setVisibility(View.VISIBLE);
            holder.goingLayout.setVisibility(View.VISIBLE);
            if (!sessionManager.isUserLoggedIn()) {
                holder.goingLayout.setEnabled(false);
            }
        }

        holder.moreButton.setOnClickListener(this::showPostMenu);
        holder.goingLayout.setOnClickListener(v -> increaseEventSubscribers(holder));
    }

    private void increaseEventSubscribers(@NonNull PostViewHolder holder) {
        //add the current user to the event, change the goingImg tint to green and increment the subscriber number.
        //If the api response returns the user is already subscribed, unsubscribe, change the
        // image tint back to default black and decrement the number.

        if (!holder.isSubscribed) {
            holder.goingImg.setColorFilter(ContextCompat.getColor(context, R.color.subscribed_img_tint));
            int currentSubscribers = Integer.parseInt(holder.eventSubscribers.getText().toString());
            holder.eventSubscribers.setText(String.valueOf(currentSubscribers + 1));

            holder.isSubscribed = true;
        } else {
            holder.goingImg.clearColorFilter();
            int currentSubscribers = Integer.parseInt(holder.eventSubscribers.getText().toString());
            holder.eventSubscribers.setText(String.valueOf(currentSubscribers - 1));

            holder.isSubscribed = false;
        }
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public void addPost(Post newPost) {
        this.posts.add(newPost);
        notifyDataSetChanged();
    }

    private void showPostMenu(View view) {
        final PopupMenu postMenu = new PopupMenu(view.getContext(), view);
        postMenu.getMenuInflater().inflate(R.menu.post_menu, postMenu.getMenu());
        postMenu.show();
    }

    static class PostViewHolder extends RecyclerView.ViewHolder {
        private final ImageView eventImg;
        private final ImageView locationImg;
        private final ImageView avatarImg;
        private final ImageView topicImg;
        private final ImageView goingImg;

        private final TextView authorView;
        private final TextView titleView;
        private final TextView topicView;
        private final TextView dateView;
        private final TextView eventSubscribers;
        //view container that holds event ImageView and TextView
        private final RelativeLayout eventLayout;
        private final RelativeLayout goingLayout;
        private final ImageButton moreButton;

        private boolean isSubscribed;

        PostViewHolder(@NonNull View itemView) {
            super(itemView);
            locationImg = itemView.findViewById(R.id.event_location_pic);
            eventImg = itemView.findViewById(R.id.event_pic);
            avatarImg = itemView.findViewById(R.id.avatar);
            topicImg = itemView.findViewById(R.id.topic_pic);
            goingImg = itemView.findViewById(R.id.subscriber_pic);

            authorView = itemView.findViewById(R.id.username);
            titleView = itemView.findViewById(R.id.main_text);
            topicView = itemView.findViewById(R.id.topic);
            dateView = itemView.findViewById(R.id.event_date);
            eventSubscribers = itemView.findViewById(R.id.subscriber_number);
            moreButton = itemView.findViewById(R.id.icon_more);
            eventLayout = itemView.findViewById(R.id.event_container);
            goingLayout = itemView.findViewById(R.id.event_subscribers_container);
        }
    }
}
