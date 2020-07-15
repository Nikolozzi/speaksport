package com.gmail.khitirinikoloz.speaksport.api;

import com.gmail.khitirinikoloz.speaksport.model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface SubscriptionApi {
    @POST("/subscriptions/{postId}")
    Call<Void> subscribeUser(@Body final User user, @Path("postId") final long postId);

    @PUT("/subscriptions/{postId}")
    Call<Void> unSubscribeUser(@Body final User user, @Path("postId") final long postId);
}
