package com.gmail.khitirinikoloz.speaksport.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gmail.khitirinikoloz.speaksport.R;
import com.gmail.khitirinikoloz.speaksport.ui.MainActivity;
import com.gmail.khitirinikoloz.speaksport.ui.post.adapter.PostAdapter;

public class HomeFragment extends Fragment {
    private static final String LOG_TAG = HomeFragment.class.getSimpleName();
    private PostAdapter postAdapter;
    private HomeViewModel homeViewModel;
    private MainActivity mainActivity;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mainActivity = (MainActivity) requireActivity();
        homeViewModel = new ViewModelProvider(mainActivity, new HomeViewModelFactory())
                .get(HomeViewModel.class);

        final RecyclerView recyclerView = view.findViewById(R.id.posts_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(mainActivity));

        postAdapter = new PostAdapter(getContext());
        recyclerView.setAdapter(postAdapter);

        @SuppressWarnings("ConstantConditions")
        // getSupportActionBar - it's set in MainActivity
        final TextView actionBarText = ((MainActivity) requireActivity()).getSupportActionBar()
                .getCustomView().findViewById(R.id.action_bar_title);
        actionBarText.setText(R.string.title_home);

        homeViewModel.getPosts();
        this.observePosts();
    }

    private void observePosts() {
        homeViewModel.getPostsResponse().observe(getViewLifecycleOwner(), postResponses -> {
            if (postResponses != null) {
                postAdapter.setPosts(postResponses);
            }
        });
    }
}
