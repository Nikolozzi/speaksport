package com.gmail.khitirinikoloz.speaksport.api;

import com.gmail.khitirinikoloz.speaksport.model.User;
import com.gmail.khitirinikoloz.speaksport.repository.post.PostRepository;
import com.gmail.khitirinikoloz.speaksport.repository.post.PostResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface SubscriptionApi {
    @POST("/subscriptions/{postId}")
    Call<Void> subscribeUser(@Body final User user, @Path("postId") final long postId);

    @PUT("/subscriptions/{postId}")
    Call<Void> unSubscribeUser(@Body final User user, @Path("postId") final long postId);

    @GET("/subscriptions/user/{userId}")
    Call<List<PostResponse>> getSubscribedPosts(@Path("userId") final long userId, @Query("page") final int page);
}
