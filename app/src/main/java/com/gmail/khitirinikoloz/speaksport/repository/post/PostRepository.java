package com.gmail.khitirinikoloz.speaksport.repository.post;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.gmail.khitirinikoloz.speaksport.api.PostApi;
import com.gmail.khitirinikoloz.speaksport.model.Post;
import com.gmail.khitirinikoloz.speaksport.repository.Constants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PostRepository {
    private static final String LOG_TAG = PostRepository.class.getSimpleName();
    private static volatile PostRepository instance;
    private PostApi postApi;

    private PostRepository() {
        this.initializePostApi();
    }

    public static PostRepository getInstance() {
        if (instance == null) {
            synchronized (PostRepository.class) {
                if (instance == null)
                    instance = new PostRepository();
            }
        }
        return instance;
    }

    public void addPost(@NonNull final Post post, @NonNull final MutableLiveData<Post> eventMutableLiveData,
                        @NonNull final MutableLiveData<Post> postMutableLiveData) {
        Call<Post> call = postApi.addPost(post);
        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(@NonNull Call<Post> call, @NonNull Response<Post> response) {
                if (response.isSuccessful() && response.body() != null) {
                    final Post postData = response.body();
                    if (postData.isEvent())
                        eventMutableLiveData.postValue(postData);
                    else
                        postMutableLiveData.postValue(postData);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Post> call, @NonNull Throwable t) {
                eventMutableLiveData.postValue(null);
                postMutableLiveData.postValue(null);
                Log.d(LOG_TAG, t.toString());
            }
        });
    }

    public void getAllPosts(final int page, @NonNull MutableLiveData<List<PostResponse>> postsLiveData) {
        Call<List<PostResponse>> call = postApi.getAllPosts(page);
        call.enqueue(new Callback<List<PostResponse>>() {
            @Override
            public void onResponse(@NonNull Call<List<PostResponse>> call, @NonNull Response<List<PostResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    postsLiveData.postValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<PostResponse>> call, @NonNull Throwable t) {
                postsLiveData.postValue(null);
                Log.d(LOG_TAG, t.toString());
            }
        });
    }

    public void getPost(final long postId, @NonNull final MutableLiveData<PostResponse> postResponseLiveData) {
        Call<PostResponse> call = postApi.getPost(postId);
        call.enqueue(new Callback<PostResponse>() {
            @Override
            public void onResponse(@NonNull Call<PostResponse> call, @NonNull Response<PostResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    postResponseLiveData.postValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<PostResponse> call, @NonNull Throwable t) {
                postResponseLiveData.postValue(null);
                Log.d(LOG_TAG, t.toString());
            }
        });
    }

    private void initializePostApi() {
        final Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssX")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        postApi = retrofit.create(PostApi.class);
    }
}
