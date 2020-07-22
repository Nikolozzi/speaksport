package com.gmail.khitirinikoloz.speaksport.ui.post.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.gmail.khitirinikoloz.speaksport.repository.post.PostRepository;
import com.gmail.khitirinikoloz.speaksport.repository.post.PostResponse;

import java.util.List;

public class RegularPostViewModel extends ViewModel {
    private PostRepository postRepository;
    private MutableLiveData<List<PostResponse>> regularPosts = new MutableLiveData<>();
    private MutableLiveData<Integer> deletedPostResponse = new MutableLiveData<>();
    private long deletedPostId;

    public RegularPostViewModel(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public void getRegularPosts(final long userId, final int page) {
        postRepository.getRegularPosts(userId, page, regularPosts);
    }

    public void deletePost(final long postId) {
        postRepository.deletePost(postId, deletedPostResponse);
    }

    public LiveData<List<PostResponse>> getRegularPosts() {
        return regularPosts;
    }

    public LiveData<Integer> getDeletedPostResponse() {
        return deletedPostResponse;
    }

    public long getDeletedPostId() {
        return deletedPostId;
    }

    public void setDeletedPostId(long deletedPostId) {
        this.deletedPostId = deletedPostId;
    }
}
