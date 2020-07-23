package com.gmail.khitirinikoloz.speaksport.ui.bookmarks;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.gmail.khitirinikoloz.speaksport.repository.bookmark.BookmarkRepository;

public class BookmarkViewModelFactory implements ViewModelProvider.Factory {
    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(BookmarksViewModel.class)) {
            return (T) new BookmarksViewModel(BookmarkRepository.getInstance());
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
