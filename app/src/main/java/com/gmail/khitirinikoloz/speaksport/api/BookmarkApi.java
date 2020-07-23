package com.gmail.khitirinikoloz.speaksport.api;

import com.gmail.khitirinikoloz.speaksport.model.User;
import com.gmail.khitirinikoloz.speaksport.repository.post.PostResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface BookmarkApi {

    @POST("/bookmarks/{postId}")
    Call<Void> addBookmark(@Body User user, @Path("postId") final long postId);

    @GET("/bookmarks/user/{userId}")
    Call<List<PostResponse>> getBookmarks(@Path("userId") final long userId, @Query("page") final int page);

    @PUT("/bookmarks/{postId}")
    Call<Void> deleteBookmark(@Body User user, @Path("postId") final long postId);
}
