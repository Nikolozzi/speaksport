package com.gmail.khitirinikoloz.speaksport.repository.bookmark;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.gmail.khitirinikoloz.speaksport.api.BookmarkApi;
import com.gmail.khitirinikoloz.speaksport.model.User;
import com.gmail.khitirinikoloz.speaksport.repository.Constants;
import com.gmail.khitirinikoloz.speaksport.repository.post.PostResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BookmarkRepository {
    private static final String LOG_TAG = BookmarkRepository.class.getSimpleName();
    private static volatile BookmarkRepository instance;
    private BookmarkApi bookmarkApi;

    private BookmarkRepository() {
        this.initializeBookmarkApi();
    }

    public static BookmarkRepository getInstance() {
        if (instance == null) {
            synchronized (BookmarkRepository.class) {
                if (instance == null) {
                    instance = new BookmarkRepository();
                }
            }
        }
        return instance;
    }

    public void addBookmark(@NonNull final User user, final long postId,
                            @NonNull MutableLiveData<Integer> bookmarkResponse) {
        Call<Void> call = bookmarkApi.addBookmark(user, postId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                bookmarkResponse.postValue(response.code());
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                bookmarkResponse.postValue(null);
                Log.d(LOG_TAG, t.toString());
            }
        });
    }

    public void getBookmarks(final long userId, final int page,
                             @NonNull final MutableLiveData<List<PostResponse>> bookmarksResponse) {
        Call<List<PostResponse>> call = bookmarkApi.getBookmarks(userId, page);
        call.enqueue(new Callback<List<PostResponse>>() {
            @Override
            public void onResponse(@NonNull Call<List<PostResponse>> call, @NonNull Response<List<PostResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    bookmarksResponse.postValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<PostResponse>> call, @NonNull Throwable t) {
                bookmarksResponse.postValue(null);
                Log.d(LOG_TAG, t.toString());
            }
        });

    }

    public void deleteBookmark(@NonNull final User user, final long postId,
                               @NonNull MutableLiveData<Integer> deletedBookmarkResponse) {
        Log.d(LOG_TAG, "user : " + user.toString());
        Log.d(LOG_TAG, "postId : " + postId);
        Call<Void> call = bookmarkApi.deleteBookmark(user, postId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                deletedBookmarkResponse.postValue(response.code());
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                deletedBookmarkResponse.postValue(null);
                Log.d(LOG_TAG, t.toString());
            }
        });
    }

    private void initializeBookmarkApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        bookmarkApi = retrofit.create(BookmarkApi.class);
    }
}
