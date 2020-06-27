package com.gmail.khitirinikoloz.speaksport.api;

import com.gmail.khitirinikoloz.speaksport.repository.login.request.LoginRequest;
import com.gmail.khitirinikoloz.speaksport.repository.login.response.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface LoginApi {
    @POST("/security/login")
    Call<LoginResponse> login(@Body final LoginRequest loginRequest);
}
