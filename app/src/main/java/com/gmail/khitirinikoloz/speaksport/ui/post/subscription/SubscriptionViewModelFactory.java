package com.gmail.khitirinikoloz.speaksport.ui.post.subscription;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.gmail.khitirinikoloz.speaksport.repository.post.subscription.SubscriptionRepository;

public class SubscriptionViewModelFactory implements ViewModelProvider.Factory {
    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(SubscriptionViewModel.class)) {
            return (T) new SubscriptionViewModel(SubscriptionRepository.getInstance());
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
