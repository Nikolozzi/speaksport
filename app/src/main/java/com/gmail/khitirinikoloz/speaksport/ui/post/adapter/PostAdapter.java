package com.gmail.khitirinikoloz.speaksport.ui.post.adapter;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.gmail.khitirinikoloz.speaksport.R;
import com.gmail.khitirinikoloz.speaksport.model.User;
import com.gmail.khitirinikoloz.speaksport.repository.post.PostResponse;
import com.gmail.khitirinikoloz.speaksport.repository.signup.response.UserResponse;
import com.gmail.khitirinikoloz.speaksport.ui.MainActivity;
import com.gmail.khitirinikoloz.speaksport.ui.bookmarks.BookmarkViewModelFactory;
import com.gmail.khitirinikoloz.speaksport.ui.bookmarks.BookmarksFragment;
import com.gmail.khitirinikoloz.speaksport.ui.bookmarks.BookmarksViewModel;
import com.gmail.khitirinikoloz.speaksport.ui.home.HomeFragment;
import com.gmail.khitirinikoloz.speaksport.ui.login.SessionManager;
import com.gmail.khitirinikoloz.speaksport.ui.post.EventPostActivity;
import com.gmail.khitirinikoloz.speaksport.ui.post.FullScreenPostFragment;
import com.gmail.khitirinikoloz.speaksport.ui.post.RegularPostActivity;
import com.gmail.khitirinikoloz.speaksport.ui.post.util.PostHelper;
import com.gmail.khitirinikoloz.speaksport.ui.post.viewmodel.EventPostViewModel;
import com.gmail.khitirinikoloz.speaksport.ui.post.viewmodel.EventPostViewModelFactory;
import com.gmail.khitirinikoloz.speaksport.ui.post.viewmodel.RegularPostViewModel;
import com.gmail.khitirinikoloz.speaksport.ui.post.viewmodel.RegularPostViewModelFactory;
import com.gmail.khitirinikoloz.speaksport.ui.subscriptions.SubscriptionsFragment;
import com.gmail.khitirinikoloz.speaksport.ui.subscriptions.SubscriptionsViewModel;
import com.gmail.khitirinikoloz.speaksport.ui.subscriptions.SubscriptionsViewModelFactory;

import java.lang.ref.WeakReference;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.gmail.khitirinikoloz.speaksport.repository.Constants.CREATED;
import static com.gmail.khitirinikoloz.speaksport.repository.Constants.SUCCESS;
import static com.gmail.khitirinikoloz.speaksport.ui.post.FullScreenPostFragment.FRAGMENT_TAG;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> implements Filterable {
    private static final String LOG_TAG = PostAdapter.class.getSimpleName();
    private List<PostResponse> posts;
    private List<PostResponse> postsForSearch;
    private final Context context;
    private final SessionManager sessionManager;
    private final SubscriptionsViewModel subscriptionsViewModel;
    private BookmarksViewModel bookmarksViewModel;
    private EventPostViewModel eventPostViewModel;
    private RegularPostViewModel regularPostViewModel;
    private final User currentUser;
    private final Class caller;
    private static WeakReference<MainActivity> mainActivity;

    public PostAdapter(Context context, Class caller) {
        this.caller = caller;
        posts = new ArrayList<>();
        postsForSearch = new ArrayList<>();
        this.context = context;
        sessionManager = new SessionManager(context);
        currentUser = new User(sessionManager.getLoggedInUser());
        subscriptionsViewModel = new ViewModelProvider((ViewModelStoreOwner) context,
                new SubscriptionsViewModelFactory())
                .get(SubscriptionsViewModel.class);
        if (!caller.getSimpleName().equals(SubscriptionsFragment.NAME)) {
            bookmarksViewModel = new ViewModelProvider((ViewModelStoreOwner) context,
                    new BookmarkViewModelFactory())
                    .get(BookmarksViewModel.class);
        }

        if (!caller.getSimpleName().equals(EventPostActivity.NAME) &&
                !caller.getSimpleName().equals(RegularPostActivity.NAME)) {
            mainActivity = new WeakReference<>((MainActivity) context);
        }
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View postItem = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.post_item, parent, false);
        return new PostViewHolder(postItem, context, caller);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        final PostResponse postResponse = posts.get(position);
        final UserResponse userResponse = postResponse.getUser();

        //for all posts, including regular ones
        holder.authorView.setText(userResponse.getUsername());
        holder.titleView.setText(postResponse.getTitle());
        holder.topicView.setText(postResponse.getTopic());
        holder.commentView.setText(String.valueOf(postResponse.getCommentsNumber()));
        holder.postId = postResponse.getId();

        //set post publication time
        //this.setPostTime(holder, postResponse.getPostedAt());
        holder.publicationTime.setText(PostHelper.getPublicationTime(postResponse.getPostedAt()));
        //load icons with glide for better memory efficiency
        PostHelper.loadUserImage(context, holder.avatarImg, userResponse);

        Glide.with(context).load(R.drawable.topic).into(holder.topicImg);

        if (postResponse.isEvent()) {
            Glide.with(context).load(R.drawable.event).into(holder.eventImg);
            Glide.with(context).load(R.drawable.world).into(holder.locationImg);

            DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
            String formattedDateTime = dateFormat.format(postResponse.getStartTime()) + " - " +
                    dateFormat.format(postResponse.getEndTime());
            holder.dateView.setText(formattedDateTime);
            holder.locationView.setText(postResponse.getLocation());
            this.manageUserSubscriptions(holder, postResponse);

            holder.eventLayout.setVisibility(View.VISIBLE);
            holder.goingLayout.setVisibility(View.VISIBLE);
            if (!sessionManager.isUserLoggedIn()) {
                holder.goingLayout.setEnabled(false);
            }
        }

        if (caller.getSimpleName().equals(SubscriptionsFragment.NAME))
            holder.moreButton.setVisibility(View.GONE);

        if (caller.getSimpleName().equals(HomeFragment.NAME))
            holder.moreButton.setOnClickListener(v -> showMainPostMenu(holder.moreButton, postResponse));
        else
            holder.moreButton.setOnClickListener(v -> showPostMenu(holder.moreButton, postResponse));

        holder.goingLayout.setOnClickListener(v -> updateEventSubscribers(holder, postResponse));
    }

    private void manageUserSubscriptions(final PostViewHolder holder, final PostResponse postResponse) {
        if (currentUser != null) {
            final long subscribers = postResponse.getSubscribedUsers().size();
            holder.eventSubscribersView.setText(String.valueOf(subscribers));

            holder.goingImg.clearColorFilter();
            if (isUserSubscribedToEvent(postResponse))
                holder.goingImg.setColorFilter(ContextCompat.getColor(context, R.color.subscribed_img_tint));
        }
    }

    private boolean isUserSubscribedToEvent(final PostResponse postResponse) {
        for (UserResponse userResponse : postResponse.getSubscribedUsers()) {
            if (userResponse.getId().equals(currentUser.getId()))
                return true;
        }
        return false;
    }

    private void updateEventSubscribers(@NonNull PostViewHolder holder, final PostResponse postResponse) {
        if (currentUser != null) {
            if (isUserSubscribedToEvent(postResponse)) {
                holder.goingImg.clearColorFilter();
                holder.eventSubscribersView.setText(String.valueOf(postResponse.getSubscribedUsers().size() - 1));
                subscriptionsViewModel.unSubscribeUser(currentUser, postResponse.getId());
                this.observeUnSubscriptionResponse(postResponse);
            } else {
                holder.goingImg.setColorFilter(ContextCompat.getColor(context, R.color.subscribed_img_tint));
                holder.eventSubscribersView.setText(String.valueOf(postResponse.getSubscribedUsers().size() + 1));
                subscriptionsViewModel.subscribeUser(currentUser, postResponse.getId());
                this.observeSubscriptionResponse(postResponse);
            }
        }
    }

    private void observeSubscriptionResponse(final PostResponse postResponse) {
        subscriptionsViewModel.getSubscriptionResponseData().observe((LifecycleOwner) context, code -> {
            if (code != null && code == CREATED) {
                //add current user to the subscribed user set locally
                postResponse.getSubscribedUsers().add(new UserResponse(currentUser));
            }
        });
    }

    private void observeUnSubscriptionResponse(final PostResponse postResponse) {
        subscriptionsViewModel.getUnSubscriptionResponseData().observe((LifecycleOwner) context, code -> {
            if (code != null && code == SUCCESS) {
                //remove current user from the subscribed users
                postResponse.getSubscribedUsers().remove(new UserResponse(currentUser));
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return posts.get(position).getId();
    }

    @Override
    public int getItemViewType(int position) {
        return (int) posts.get(position).getId();
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public void setPosts(List<PostResponse> posts) {
        this.posts.addAll(posts);
        this.postsForSearch.addAll(posts);
        int startPosition = this.posts.size() - posts.size();
        notifyItemRangeInserted(startPosition, posts.size());
    }

    public void clearData() {
        posts.clear();
        postsForSearch.clear();
        notifyDataSetChanged();
    }

    private void showMainPostMenu(View view, PostResponse postResponse) {
        final PopupMenu postMenu = new PopupMenu(view.getContext(), view);
        postMenu.getMenuInflater().inflate(R.menu.main_post_menu, postMenu.getMenu());

        postMenu.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.bookmark: {
                    bookmarksViewModel.addBookmark(currentUser, postResponse.getId());
                    return true;
                }
                case R.id.report: {

                }
                case R.id.share: {

                }
                default:
                    return false;
            }
        });
        postMenu.show();
    }

    private void showPostMenu(View view, PostResponse postResponse) {
        final PopupMenu postMenu = new PopupMenu(view.getContext(), view);
        postMenu.getMenuInflater().inflate(R.menu.post_menu, postMenu.getMenu());

        postMenu.setOnMenuItemClickListener(item -> {
            if (caller.getSimpleName().equals(BookmarksFragment.NAME)) {
                if (item.getItemId() == R.id.remove_post) {
                    bookmarksViewModel.deleteBookmark(currentUser, postResponse.getId());
                    bookmarksViewModel.setDeletedPostId(postResponse.getId());
                    return true;
                }
                return false;
            } else if (caller.getSimpleName().equals(EventPostActivity.NAME)) {
                eventPostViewModel = new ViewModelProvider((ViewModelStoreOwner) context,
                        new EventPostViewModelFactory()).get(EventPostViewModel.class);
                if (item.getItemId() == R.id.remove_post) {
                    eventPostViewModel.deleteEvent(postResponse.getId());
                    eventPostViewModel.setDeletedEventId(postResponse.getId());
                    return true;
                }
                return false;
            } else if (caller.getSimpleName().equals(RegularPostActivity.NAME)) {
                regularPostViewModel = new ViewModelProvider((ViewModelStoreOwner) context,
                        new RegularPostViewModelFactory()).get(RegularPostViewModel.class);
                if (item.getItemId() == R.id.remove_post) {
                    regularPostViewModel.deletePost(postResponse.getId());
                    regularPostViewModel.setDeletedPostId(postResponse.getId());
                    return true;
                }
            }

            return false;
        });

        postMenu.show();
    }

    public void deletePost(final long postId) {
        for (int i = 0; i < posts.size(); i++) {
            if (posts.get(i).getId() == postId) {
                posts.remove(i);
                notifyItemRemoved(i);
                break;
            }
        }
    }

    @Override
    public Filter getFilter() {
        return postsFilter;
    }

    private Filter postsFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            List<PostResponse> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(postsForSearch);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (PostResponse postResponse : postsForSearch) {
                    if (postResponse.getTopic().toLowerCase().contains(filterPattern)) {
                        filteredList.add(postResponse);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            posts.clear();

            @SuppressWarnings("unchecked")
            List<PostResponse> filtered = (List<PostResponse>) results.values;
            List<PostResponse> finalResults = filtered.stream().distinct().collect(Collectors.toList());
            posts.addAll(new ArrayList<>(finalResults));

            notifyDataSetChanged();
        }
    };

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

        private long postId;
        private final Context context;
        private final Class caller;

        PostViewHolder(@NonNull View itemView, @NonNull Context context, @NonNull Class caller) {
            super(itemView);
            this.context = context;
            this.caller = caller;
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
            Bundle bundle = new Bundle();
            bundle.putLong("postId", postId);
            FullScreenPostFragment fullScreenPostFragment = new FullScreenPostFragment();
            fullScreenPostFragment.setArguments(bundle);
            if (caller.getSimpleName().equals(SubscriptionsFragment.NAME)) {
                loadFullScreenPostFragment(fullScreenPostFragment, R.id.subscriptions_container);
            } else if (caller.getSimpleName().equals(BookmarksFragment.NAME)) {
                loadFullScreenPostFragment(fullScreenPostFragment, R.id.bookmarks_container);
            } else {
                loadFullScreenPostFragment(fullScreenPostFragment, R.id.home_container);
            }
        }

        private void loadFullScreenPostFragment(FullScreenPostFragment fullScreenPostFragment, int layout) {
            if (caller.getSimpleName().equals(EventPostActivity.NAME)) {
                ((DrawerLayout) mainActivity.get().findViewById(R.id.drawer_layout)).closeDrawer(GravityCompat.START);
                ((EventPostActivity) (context)).finish();
            } else if (caller.getSimpleName().equals(RegularPostActivity.NAME)) {
                ((DrawerLayout) mainActivity.get().findViewById(R.id.drawer_layout)).closeDrawer(GravityCompat.START);
                ((RegularPostActivity) (context)).finish();
            }

            mainActivity.get().getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right,
                            R.anim.enter_from_right, R.anim.exit_to_right)
                    .add(layout, fullScreenPostFragment, FRAGMENT_TAG)
                    .addToBackStack(null)
                    .commitAllowingStateLoss();
        }
    }
}
