package com.gmail.khitirinikoloz.speaksport.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.gmail.khitirinikoloz.speaksport.R;

public class FullScreenPostFragment extends Fragment {

    public static final String LOG_TAG = FullScreenPostFragment.class.getSimpleName();

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

        ImageView eventImg = view.findViewById(R.id.event_pic);
        ImageView locationImg = view.findViewById(R.id.event_location_pic);
        ImageView topicImg = view.findViewById(R.id.topic_pic);
        ImageView avatarImg = view.findViewById(R.id.avatar);
        Glide.with(getContext()).load(R.drawable.event).into(eventImg);
        Glide.with(getContext()).load(R.drawable.world).into(locationImg);
        Glide.with(getContext()).load(R.drawable.topic).into(topicImg);
        Glide.with(getContext()).load(R.drawable.avatar).into(avatarImg);

        TextView authorView = view.findViewById(R.id.username);
        TextView titleView = view.findViewById(R.id.main_text);
        TextView descriptionView = view.findViewById(R.id.description);
        LinearLayout descriptionContainer = view.findViewById(R.id.description_container);
        TextView topicView = view.findViewById(R.id.topic);
        TextView eventView = view.findViewById(R.id.event_date);
        TextView locationView = view.findViewById(R.id.event_location);

        RelativeLayout eventContainer = view.findViewById(R.id.event_container);
        RelativeLayout locationContainer = view.findViewById(R.id.location_container);
        RelativeLayout subscribersContainer = view.findViewById(R.id.event_subscribers_container);

        locationView.setOnClickListener(this::showMap);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            String username = bundle.getString(PostAdapter.USERNAME_KEY);
            String title = bundle.getString(PostAdapter.TITLE_KEY);
            String topic = bundle.getString(PostAdapter.TOPIC_KEY);

            authorView.setText(username);
            titleView.setText(title);
            topicView.setText(topic);

            String description = bundle.getString(PostAdapter.DESCRIPTION_KEY);
            if (TextUtils.getTrimmedLength(description) > 0) {
                descriptionContainer.setVisibility(View.VISIBLE);
                descriptionView.setText(description);
            }

            String eventDate = bundle.getString(PostAdapter.DATE_KEY);
            String location = bundle.getString(PostAdapter.LOCATION_KEY);
            String subscribers = bundle.getString(PostAdapter.SUBSCRIBERS_KEY);
            if (!TextUtils.isEmpty(eventDate) && !TextUtils.isEmpty(location)) {
                eventContainer.setVisibility(View.VISIBLE);
                eventView.setText(eventDate);
                locationContainer.setVisibility(View.VISIBLE);
                locationView.setText(location);
                subscribersContainer.setVisibility(View.VISIBLE);
            }
        }
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
}
