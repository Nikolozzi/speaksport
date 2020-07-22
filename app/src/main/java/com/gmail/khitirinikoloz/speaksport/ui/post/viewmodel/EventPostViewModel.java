package com.gmail.khitirinikoloz.speaksport.ui.post.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.gmail.khitirinikoloz.speaksport.repository.post.PostRepository;
import com.gmail.khitirinikoloz.speaksport.repository.post.PostResponse;

import java.util.List;

public class EventPostViewModel extends ViewModel {
    private PostRepository postRepository;
    private MutableLiveData<List<PostResponse>> eventPosts = new MutableLiveData<>();
    private MutableLiveData<Integer> deletedEventResponse = new MutableLiveData<>();
    private long deletedEventId;

    public EventPostViewModel(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public void getEvents(final long userId, final int page) {
        postRepository.getEvents(userId, page, eventPosts);
    }

    public void deleteEvent(final long postId) {
        postRepository.deletePost(postId, deletedEventResponse);
    }

    public LiveData<List<PostResponse>> getEventPosts() {
        return eventPosts;
    }

    public LiveData<Integer> getDeletedEventResponse() {
        return deletedEventResponse;
    }

    public long getDeletedEventId() {
        return deletedEventId;
    }

    public void setDeletedEventId(long deletedEventId) {
        this.deletedEventId = deletedEventId;
    }
}
