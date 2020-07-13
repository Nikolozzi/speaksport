package com.gmail.khitirinikoloz.speaksport.api;

import com.gmail.khitirinikoloz.speaksport.model.Post;
import com.gmail.khitirinikoloz.speaksport.repository.post.PostResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface PostApi {
    @POST("/posts/")
    Call<Post> addPost(@Body final Post post);

    @GET("/posts/")
    Call<List<PostResponse>> getAllPosts();
}
