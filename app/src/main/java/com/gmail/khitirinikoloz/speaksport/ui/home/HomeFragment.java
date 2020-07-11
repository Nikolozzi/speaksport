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
import com.gmail.khitirinikoloz.speaksport.model.Post;
import com.gmail.khitirinikoloz.speaksport.ui.MainActivity;
import com.gmail.khitirinikoloz.speaksport.ui.post.adapter.PostAdapter;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private PostAdapter postAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final HomeViewModel viewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);

        final RecyclerView recyclerView = view.findViewById(R.id.posts_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //sample data
        final List<Post> samplePosts = new ArrayList<>(SampleDataGenerator.getSamplePosts());

        postAdapter = new PostAdapter(getContext(), samplePosts);
        recyclerView.setAdapter(postAdapter);

        viewModel.getPost().observe(getViewLifecycleOwner(), post -> {
            postAdapter.addPost(post);
        });

        @SuppressWarnings("ConstantConditions") // suppressing possible NPE on
        // getSupportActionBar - it's set in MainActivity
        final TextView actionBarText = ((MainActivity) requireActivity()).getSupportActionBar()
                .getCustomView().findViewById(R.id.action_bar_title);
        actionBarText.setText(R.string.title_home);
    }
}
