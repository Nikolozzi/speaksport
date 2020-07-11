package com.gmail.khitirinikoloz.speaksport.ui.post.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.gmail.khitirinikoloz.speaksport.model.Post;
import com.gmail.khitirinikoloz.speaksport.repository.post.PostRepository;

public class NewPostViewModel extends ViewModel {
    private final PostRepository postRepository;
    private MutableLiveData<Post> eventResponseLiveData = new MutableLiveData<>();
    private MutableLiveData<Post> postResponseLiveData = new MutableLiveData<>();

    NewPostViewModel(final PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public void addPost(final Post post) {
        postRepository.addPost(post, eventResponseLiveData, postResponseLiveData);
    }

    public LiveData<Post> getEventResponseLiveData() {
        return eventResponseLiveData;
    }

    public MutableLiveData<Post> getPostResponseLiveData() {
        return postResponseLiveData;
    }
}
