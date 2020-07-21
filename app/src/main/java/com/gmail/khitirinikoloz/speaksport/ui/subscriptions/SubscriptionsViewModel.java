package com.gmail.khitirinikoloz.speaksport.ui.subscriptions;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.gmail.khitirinikoloz.speaksport.model.User;
import com.gmail.khitirinikoloz.speaksport.repository.post.PostResponse;
import com.gmail.khitirinikoloz.speaksport.repository.subscription.SubscriptionRepository;

import java.util.List;

public class SubscriptionsViewModel extends ViewModel {
    private final SubscriptionRepository subscriptionRepository;
    private final MutableLiveData<Integer> subscriptionResponseData = new MutableLiveData<>();
    private final MutableLiveData<Integer> unSubscriptionResponseData = new MutableLiveData<>();
    private final MutableLiveData<List<PostResponse>> subscribedPostsResponseData = new MutableLiveData<>();

    public SubscriptionsViewModel(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    public void subscribeUser(final User user, final long postId) {
        subscriptionRepository.subscribeUser(user, postId, subscriptionResponseData);
    }

    public void unSubscribeUser(final User user, final long postId) {
        subscriptionRepository.unSubscribeUser(user, postId, unSubscriptionResponseData);
    }

    public void getSubscribedPosts(final long userId, final int page) {
        subscriptionRepository.getSubscribedPosts(userId, page, subscribedPostsResponseData);
    }

    public LiveData<Integer> getSubscriptionResponseData() {
        return subscriptionResponseData;
    }

    public LiveData<Integer> getUnSubscriptionResponseData() {
        return unSubscriptionResponseData;
    }

    public LiveData<List<PostResponse>> getSubscribedPostsResponseData() {
        return subscribedPostsResponseData;
    }
}
