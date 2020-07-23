package com.gmail.khitirinikoloz.speaksport.api;

import com.gmail.khitirinikoloz.speaksport.model.Comment;
import com.gmail.khitirinikoloz.speaksport.repository.comment.CommentRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CommentApi {
    @GET("/comments/post/{postId}")
    Call<List<Comment>> getComments(@Path("postId") final long postId, @Query("page") final int page);

    @GET("/comments/{id}")
    Call<Comment> getComment(@Path("id") final long id);

    @POST("/comments/")
    Call<Comment> addComment(@Body final CommentRequest commentRequest);
}
