package com.gmail.khitirinikoloz.speaksport.ui.post.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.gmail.khitirinikoloz.speaksport.repository.comment.CommentRepository;
import com.gmail.khitirinikoloz.speaksport.repository.post.PostRepository;

public class FullScreenPostViewModelFactory implements ViewModelProvider.Factory {
    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(FullScreenPostViewModel.class)) {
            return (T) new FullScreenPostViewModel(PostRepository.getInstance(), CommentRepository.getInstance());
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
