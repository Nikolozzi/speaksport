package com.gmail.khitirinikoloz.speaksport.ui.bookmarks;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.gmail.khitirinikoloz.speaksport.model.User;
import com.gmail.khitirinikoloz.speaksport.repository.bookmark.BookmarkRepository;
import com.gmail.khitirinikoloz.speaksport.repository.post.PostResponse;

import java.util.List;

public class BookmarksViewModel extends ViewModel {
    private BookmarkRepository bookmarkRepository;
    private MutableLiveData<Integer> bookmarkResponse = new MutableLiveData<>();
    private MutableLiveData<List<PostResponse>> bookmarksResponse = new MutableLiveData<>();
    private MutableLiveData<Integer> deletedBookmarkResponse = new MutableLiveData<>();
    private long deletedBookmarkId;

    public BookmarksViewModel(BookmarkRepository bookmarkRepository) {
        this.bookmarkRepository = bookmarkRepository;
    }

    public void addBookmark(final User user, final long postId) {
        bookmarkRepository.addBookmark(user, postId, bookmarkResponse);
    }

    void getBookmarks(final long userId, final int page) {
        bookmarkRepository.getBookmarks(userId, page, bookmarksResponse);
    }

    public void deleteBookmark(final User user, final long postId) {
        bookmarkRepository.deleteBookmark(user, postId, deletedBookmarkResponse);
    }

    public LiveData<Integer> getBookmarkResponse() {
        return bookmarkResponse;
    }

    LiveData<List<PostResponse>> getBookmarksResponse() {
        return bookmarksResponse;
    }

    LiveData<Integer> getDeletedBookmarkResponse() {
        return deletedBookmarkResponse;
    }

    public void setDeletedBookmarkId(final long id) {
        deletedBookmarkId = id;
    }

    long getDeletedBookmarkId() {
        return deletedBookmarkId;
    }
}