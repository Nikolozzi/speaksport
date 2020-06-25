package com.gmail.khitirinikoloz.speaksport.api;

import com.gmail.khitirinikoloz.speaksport.entity.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserApi {

    @GET("users/")
    Call<List<User>> getAllUsers();

    @GET("users/email/{email}")
    Call<User> getUserByEmail(@Path("email") final String email);

    @GET("users/username/{username}")
    Call<User> getUserByUsername(@Path("username") final String username);

    @POST("users/registration")
    Call<User> addUser(@Body final User user);
}
