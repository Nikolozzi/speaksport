package com.gmail.khitirinikoloz.speaksport.api;

import com.gmail.khitirinikoloz.speaksport.model.Post;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface PostApi {
    @POST("/posts/")
    Call<Post> addPost(@Body final Post post);
}
