package com.gmail.khitirinikoloz.speaksport.api;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface FileApi {
    @Multipart
    @POST("/file/user/{userId}")
    Call<Void> uploadUserImage(@Path("userId") final long userId, @Part final MultipartBody.Part file);

    @GET("/file/image")
    Call<ResponseBody> getUserImage(@Query("path") final String path);
}
