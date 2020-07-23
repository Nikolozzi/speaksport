package com.gmail.khitirinikoloz.speaksport.ui.home;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.gmail.khitirinikoloz.speaksport.repository.post.PostRepository;
import com.gmail.khitirinikoloz.speaksport.repository.post.PostResponse;

import java.util.List;

public final class HomeViewModel extends ViewModel {

    private PostRepository postRepository;
    private MutableLiveData<List<PostResponse>> postsResponse = new MutableLiveData<>();

    public HomeViewModel(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public void getPosts(final int page) {
        postRepository.getAllPosts(page, postsResponse);
    }

    MutableLiveData<List<PostResponse>> getPostsResponse() {
        return postsResponse;
    }
}