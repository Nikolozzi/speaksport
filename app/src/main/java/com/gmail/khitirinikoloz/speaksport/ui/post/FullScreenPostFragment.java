package com.gmail.khitirinikoloz.speaksport.ui.post;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.gmail.khitirinikoloz.speaksport.R;
import com.gmail.khitirinikoloz.speaksport.model.Post;
import com.gmail.khitirinikoloz.speaksport.model.User;
import com.gmail.khitirinikoloz.speaksport.repository.comment.CommentRequest;
import com.gmail.khitirinikoloz.speaksport.repository.post.PostResponse;
import com.gmail.khitirinikoloz.speaksport.repository.signup.response.UserResponse;
import com.gmail.khitirinikoloz.speaksport.ui.CommentFragment;
import com.gmail.khitirinikoloz.speaksport.ui.MainActivity;
import com.gmail.khitirinikoloz.speaksport.ui.login.LoggedInUser;
import com.gmail.khitirinikoloz.speaksport.ui.login.SessionManager;
import com.gmail.khitirinikoloz.speaksport.ui.post.adapter.CommentAdapter;
import com.gmail.khitirinikoloz.speaksport.ui.post.util.EndlessRecyclerViewScrollListener;
import com.gmail.khitirinikoloz.speaksport.ui.post.util.PostHelper;
import com.gmail.khitirinikoloz.speaksport.ui.post.viewmodel.FullScreenPostViewModel;
import com.gmail.khitirinikoloz.speaksport.ui.post.viewmodel.FullScreenPostViewModelFactory;
import com.gmail.khitirinikoloz.speaksport.ui.profile.util.ImageUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DateFormat;
import java.util.Set;

public class FullScreenPostFragment extends Fragment implements MainActivity.OnDispatchCommentCallBack, OnMapReadyCallback {

    private static final String LOG_TAG = FullScreenPostFragment.class.getSimpleName();
    private static final int REQUEST_MAP_PERMISSIONS = 3;
    private static final int DEFAULT_PAGE_NUM = 0;
    public static final String FRAGMENT_TAG = "FullScreenPostFragment";
    private FullScreenPostViewModel fullScreenPostViewModel;
    private MainActivity mainActivity;
    private User currentUser;
    private long postId;
    private PostResponse currentPostResponse;
    private UserResponse currentUserResponse;

    private ImageView avatarImg;
    private ImageView topicImg;
    private ImageView eventImg;
    private ImageView locationImg;
    private ImageView subscribersImg;

    private RelativeLayout eventContainer;
    private RelativeLayout locationContainer;
    private RelativeLayout commentsSubscribersContainer;
    private LinearLayout descriptionContainer;
    private LinearLayout mapContainer;
    private TextView authorView;
    private TextView titleView;
    private TextView descriptionView;
    private TextView topicView;
    private TextView locationView;
    private TextView dateView;
    private TextView commentView;
    private TextView subscribersView;
    private TextView publicationTime;

    private ProgressBar progressBarMain;
    private ProgressBar progressBar;

    private MapView mapView;

    //comments
    private CommentAdapter commentAdapter;

    public FullScreenPostFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_full_screen_post, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final View hiderView = view.findViewById(R.id.hider);

        final ImageView commentAvatarImg = view.findViewById(R.id.comment_avatar);
        avatarImg = view.findViewById(R.id.avatar);
        eventImg = view.findViewById(R.id.event_pic);
        locationImg = view.findViewById(R.id.event_location_pic);
        topicImg = view.findViewById(R.id.topic_pic);
        subscribersImg = view.findViewById(R.id.subscriber_pic);

        eventContainer = view.findViewById(R.id.event_container);
        locationContainer = view.findViewById(R.id.location_container);
        commentsSubscribersContainer = view.findViewById(R.id.event_subscribers_container);
        mapContainer = view.findViewById(R.id.map_container);
        progressBarMain = view.findViewById(R.id.progress_load_post);
        progressBar = view.findViewById(R.id.progress_load_comments);
        progressBarMain.setVisibility(View.VISIBLE);

        eventContainer.setVisibility(View.GONE);
        locationContainer.setVisibility(View.GONE);
        commentsSubscribersContainer.setVisibility(View.GONE);

        mainActivity = (MainActivity) requireActivity();
        fullScreenPostViewModel = new ViewModelProvider(this,
                new FullScreenPostViewModelFactory()).get(FullScreenPostViewModel.class);

        if (getArguments() != null) {
            postId = getArguments().getLong("postId");
            fullScreenPostViewModel.getPost(postId);
            fullScreenPostViewModel.getComments(postId, DEFAULT_PAGE_NUM);
            progressBar.setVisibility(View.VISIBLE);
        }
        this.observePost(hiderView);
        this.observeComments();
        this.observeComment();

        final SessionManager sessionManager = new SessionManager(mainActivity);
        final LoggedInUser loggedInUser = sessionManager.getLoggedInUser();
        currentUser = new User(sessionManager.getLoggedInUser());
        if (loggedInUser.getImagePath() != null) {
            Bitmap bitmap = ImageUtil.loadImageFromStorage(loggedInUser.getImagePath());
            Glide.with(requireContext()).load(bitmap).into(commentAvatarImg);
        } else
            Glide.with(requireContext()).load(R.drawable.avatar).into(commentAvatarImg);

        authorView = view.findViewById(R.id.username);
        titleView = view.findViewById(R.id.main_text);
        descriptionView = view.findViewById(R.id.description);
        descriptionContainer = view.findViewById(R.id.description_container);
        topicView = view.findViewById(R.id.topic);
        locationView = view.findViewById(R.id.event_location);
        dateView = view.findViewById(R.id.event_date);
        commentView = view.findViewById(R.id.comment_number);
        subscribersView = view.findViewById(R.id.subscriber_number);
        publicationTime = view.findViewById(R.id.time);
        EditText commentEditText = view.findViewById(R.id.comment);
        commentEditText.setOnClickListener(v -> openCommentWindow());

        locationView.setOnClickListener(this::showMap);

        final RecyclerView recyclerView = view.findViewById(R.id.comments_list);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        commentAdapter = new CommentAdapter(getContext());
        commentAdapter.setHasStableIds(true);
        recyclerView.setAdapter(commentAdapter);

        EndlessRecyclerViewScrollListener scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                getComments(postId, page);
            }
        };

        recyclerView.addOnScrollListener(scrollListener);

        BottomNavigationView navigationView = requireActivity().findViewById(R.id.nav_view);
        navigationView.setVisibility(View.GONE);
        FloatingActionButton floatingActionButton = requireActivity().findViewById(R.id.add_fab);
        floatingActionButton.setVisibility(View.GONE);

        final LinearLayout commentContainer = view.findViewById(R.id.add_comment_container);
        if (!sessionManager.isUserLoggedIn())
            commentContainer.setVisibility(View.GONE);
        else
            commentContainer.setVisibility(View.VISIBLE);

        view.findViewById(R.id.icon_more).setVisibility(View.GONE);
        setHasOptionsMenu(true);

        mapView = view.findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);
    }

    private void getComments(final long postId, final int page) {
        progressBar.setVisibility(View.VISIBLE);
        fullScreenPostViewModel.getComments(postId, page);
    }

    private void observePost(final View hiderView) {
        fullScreenPostViewModel.getPostResponseLiveData().observe(getViewLifecycleOwner(), postResponse -> {
            if (postResponse != null) {
                this.currentPostResponse = postResponse;
                this.currentUserResponse = postResponse.getUser();

                PostHelper.loadUserImage(getContext(), avatarImg, currentUserResponse);
                Glide.with(mainActivity).load(R.drawable.topic).into(topicImg);

                authorView.setText(currentUserResponse.getUsername());
                titleView.setText(postResponse.getTitle());
                topicView.setText(postResponse.getTopic());
                commentView.setText(String.valueOf(postResponse.getCommentsNumber()));
                publicationTime.setText(PostHelper.getPublicationTime(postResponse.getPostedAt()));

                if (!TextUtils.isEmpty(postResponse.getDescription())) {
                    descriptionContainer.setVisibility(View.VISIBLE);
                    descriptionView.setText(postResponse.getDescription());
                }

                if (postResponse.isEvent()) {
                    mapView.getMapAsync(this);
                    Glide.with(mainActivity).load(R.drawable.event).into(eventImg);
                    Glide.with(mainActivity).load(R.drawable.world).into(locationImg);
                    eventContainer.setVisibility(View.VISIBLE);
                    locationContainer.setVisibility(View.VISIBLE);
                    commentsSubscribersContainer.setVisibility(View.VISIBLE);
                    mapContainer.setVisibility(View.VISIBLE);

                    DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
                    String formattedDateTime = dateFormat.format(postResponse.getStartTime()) + " - " +
                            dateFormat.format(postResponse.getEndTime());
                    dateView.setText(formattedDateTime);
                    locationView.setText(postResponse.getLocation());
                    this.manageSubscriptions(postResponse);
                }

                hiderView.setVisibility(View.GONE);
                progressBarMain.setVisibility(View.GONE);
            }
        });
    }

    private void observeComments() {
        fullScreenPostViewModel.getCommentsResponseLiveData().observe(getViewLifecycleOwner(), comments -> {
            if (comments != null) {
                progressBar.setVisibility(View.GONE);
                commentAdapter.setComments(comments);
            }
        });
    }

    private void observeComment() {
        fullScreenPostViewModel.getCommentResponseLiveData().observe(getViewLifecycleOwner(), comment -> {
            if (comment != null) {
                commentAdapter.setComment(comment);
            }
        });
    }

    private void observeNewComment() {
        fullScreenPostViewModel.getNewCommentResponseLiveData().observe(getViewLifecycleOwner(), newComment -> {
            if (newComment != null) {
                fullScreenPostViewModel.getComment(newComment.getId());
            }
        });
    }

    private void manageSubscriptions(final PostResponse postResponse) {
        Set<UserResponse> subscribedUsers = postResponse.getSubscribedUsers();
        subscribersView.setText(String.valueOf(subscribedUsers.size()));
        for (UserResponse user : subscribedUsers) {
            if (user.getId().equals(currentUser.getId())) {
                subscribersImg.setColorFilter(ContextCompat.getColor(mainActivity, R.color.subscribed_img_tint));
            }
        }
    }

    private void openCommentWindow() {
        final CommentFragment commentFragment = new CommentFragment();
        requireActivity().getSupportFragmentManager().beginTransaction()
                .add(R.id.fullscreen_container, commentFragment, null)
                .addToBackStack(null)
                .commit();
    }

    private void showMap(View v) {
        String locationText = ((TextView) v).getText().toString();
        String geoLocation = "geo:0,0?q=" + Uri.encode(locationText);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(geoLocation));

        if (intent.resolveActivity(requireActivity().getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        mapView.onLowMemory();
        super.onLowMemory();
    }

    @Override
    public void onDispatchComment(final String comment) {
        final Post post;
        if (currentPostResponse.isEvent()) {
            post = new Post.PostBuilder(currentPostResponse.isEvent(),
                    currentPostResponse.getTitle(),
                    currentPostResponse.getTopic(),
                    new User(currentPostResponse.getUser()))
                    .id(currentPostResponse.getId())
                    .description(currentPostResponse.getDescription())
                    .startTime(currentPostResponse.getStartTime())
                    .endTime(currentPostResponse.getEndTime())
                    .location(currentPostResponse.getLocation())
                    .latitude(currentPostResponse.getLatitude())
                    .longitude(currentPostResponse.getLongitude())
                    .build();
        } else {
            post = new Post.PostBuilder(currentPostResponse.isEvent(),
                    currentPostResponse.getTitle(),
                    currentPostResponse.getTopic(),
                    new User(currentPostResponse.getUser()))
                    .id(currentPostResponse.getId())
                    .description(currentPostResponse.getDescription()).build();
        }

        final CommentRequest newComment = new CommentRequest(comment, currentUser, post);
        fullScreenPostViewModel.addComment(newComment);
        this.observeNewComment();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (ContextCompat.checkSelfPermission(mainActivity, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(mainActivity, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
            googleMap.setMyLocationEnabled(false);
            googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        } else {
            ActivityCompat.requestPermissions(mainActivity, new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_MAP_PERMISSIONS);
        }

        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        if (currentPostResponse != null) {
            final LatLng latLng = new LatLng(currentPostResponse.getLatitude(), currentPostResponse.getLongitude());
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(latLng.latitude, latLng.longitude), 8);
            googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(latLng.latitude, latLng.longitude))
                    .title("Marker"));
            googleMap.moveCamera(cameraUpdate);
        }
    }
}
