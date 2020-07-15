package com.gmail.khitirinikoloz.speaksport.ui.post.subscription;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.gmail.khitirinikoloz.speaksport.model.User;
import com.gmail.khitirinikoloz.speaksport.repository.post.subscription.SubscriptionRepository;

public class SubscriptionViewModel extends ViewModel {
    private final SubscriptionRepository subscriptionRepository;
    private final MutableLiveData<Integer> subscriptionResponseData = new MutableLiveData<>();
    private final MutableLiveData<Integer> unSubscriptionResponseData = new MutableLiveData<>();

    public SubscriptionViewModel(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    public void subscribeUser(final User user, final long postId) {
        subscriptionRepository.subscribeUser(user, postId, subscriptionResponseData);
    }

    public void unSubscribeUser(final User user, final long postId) {
        subscriptionRepository.unSubscribeUser(user, postId, unSubscriptionResponseData);
    }

    public LiveData<Integer> getSubscriptionResponseData() {
        return subscriptionResponseData;
    }

    public LiveData<Integer> getUnSubscriptionResponseData() {
        return unSubscriptionResponseData;
    }
}
