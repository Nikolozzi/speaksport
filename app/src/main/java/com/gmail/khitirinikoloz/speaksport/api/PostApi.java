package com.gmail.khitirinikoloz.speaksport.api;

import com.gmail.khitirinikoloz.speaksport.model.Post;
import com.gmail.khitirinikoloz.speaksport.repository.post.PostResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PostApi {
    @POST("/posts/")
    Call<Post> addPost(@Body final Post post);

    @GET("/posts/")
    Call<List<PostResponse>> getAllPosts(@Query("page") int page);

    @GET("/posts/{id}")
    Call<PostResponse> getPost(@Path("id") final long id);

    @GET("/posts/event/user/{userId}")
    Call<List<PostResponse>> getUserEvents(@Path("userId") final long userId, @Query("page") int page);

    @GET("/posts/regular/user/{userId}")
    Call<List<PostResponse>> getUserRegularPosts(@Path("userId") final long userId, @Query("page") int page);

    @DELETE("/posts/{id}")
    Call<Void> deletePost(@Path("id") final long id);
}
