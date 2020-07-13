package com.gmail.khitirinikoloz.speaksport.ui.home;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.gmail.khitirinikoloz.speaksport.repository.post.PostRepository;
import com.gmail.khitirinikoloz.speaksport.repository.post.PostResponse;

import java.util.List;

final class HomeViewModel extends ViewModel {

    private PostRepository postRepository;
    private MutableLiveData<List<PostResponse>> postsResponse = new MutableLiveData<>();

    HomeViewModel(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    void getPosts() {
        postRepository.getAllPosts(postsResponse);
    }

    MutableLiveData<List<PostResponse>> getPostsResponse() {
        return postsResponse;
    }
}