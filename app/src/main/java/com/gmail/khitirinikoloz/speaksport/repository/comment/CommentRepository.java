package com.gmail.khitirinikoloz.speaksport.repository.comment;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.gmail.khitirinikoloz.speaksport.api.CommentApi;
import com.gmail.khitirinikoloz.speaksport.model.Comment;
import com.gmail.khitirinikoloz.speaksport.repository.Constants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CommentRepository {

    private static final String LOG_TAG = CommentRepository.class.getSimpleName();
    private static volatile CommentRepository instance;
    private CommentApi commentApi;

    private CommentRepository() {
        this.initializeCommentApi();
    }

    public static CommentRepository getInstance() {
        if (instance == null) {
            synchronized (CommentRepository.class) {
                if (instance == null)
                    instance = new CommentRepository();
            }
        }
        return instance;
    }

    public void getComments(final long postId, final int page,
                            @NonNull final MutableLiveData<List<Comment>> commentsLiveData) {
        Call<List<Comment>> call = commentApi.getComments(postId, page);
        call.enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(@NonNull Call<List<Comment>> call, @NonNull Response<List<Comment>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    commentsLiveData.postValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Comment>> call, @NonNull Throwable t) {
                commentsLiveData.postValue(null);
                Log.d(LOG_TAG, t.toString());
            }
        });
    }

    public void getComment(final long commentId, @NonNull final MutableLiveData<Comment> commentLiveData) {
        Call<Comment> call = commentApi.getComment(commentId);
        call.enqueue(new Callback<Comment>() {
            @Override
            public void onResponse(@NonNull Call<Comment> call, @NonNull Response<Comment> response) {
                if (response.isSuccessful() && response.body() != null) {
                    commentLiveData.postValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Comment> call, @NonNull Throwable t) {
                commentLiveData.postValue(null);
                Log.d(LOG_TAG, t.toString());
            }
        });
    }

    public void addComment(@NonNull final CommentRequest commentRequest,
                           @NonNull final MutableLiveData<Comment> newCommentLiveData) {
        Call<Comment> call = commentApi.addComment(commentRequest);
        call.enqueue(new Callback<Comment>() {
            @Override
            public void onResponse(@NonNull Call<Comment> call, @NonNull Response<Comment> response) {
                if (response.isSuccessful() && response.body() != null) {
                    newCommentLiveData.postValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Comment> call, @NonNull Throwable t) {
                newCommentLiveData.postValue(null);
                Log.d(LOG_TAG, t.toString());
            }
        });
    }


    private void initializeCommentApi() {
        final Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssX")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        commentApi = retrofit.create(CommentApi.class);
    }
}
