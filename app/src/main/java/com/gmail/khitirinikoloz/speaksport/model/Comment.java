package com.gmail.khitirinikoloz.speaksport.model;

import androidx.annotation.NonNull;

import com.gmail.khitirinikoloz.speaksport.repository.post.PostResponse;
import com.gmail.khitirinikoloz.speaksport.repository.signup.response.UserResponse;

import java.util.Date;

public class Comment {
    private long id;
    private String text;
    private Date commentedAt;
    private UserResponse user;
    private PostResponse post;

    public Comment() {
    }

    public Comment(String text, UserResponse user, PostResponse post) {
        this.text = text;
        this.user = user;
        this.post = post;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getCommentedAt() {
        return commentedAt;
    }

    public void setCommentedAt(Date commentedAt) {
        this.commentedAt = commentedAt;
    }

    public UserResponse getUser() {
        return user;
    }

    public void setUser(UserResponse user) {
        this.user = user;
    }

    public PostResponse getPost() {
        return post;
    }

    public void setPost(PostResponse post) {
        this.post = post;
    }

    @NonNull
    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", commentedAt=" + commentedAt +
                ", user=" + user +
                ", post=" + post +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Comment comment = (Comment) o;

        return id == comment.id &&
                text.equals(comment.text) &&
                commentedAt.equals(comment.commentedAt);
    }

    @Override
    public int hashCode() {
        return (int) (17 * id + commentedAt.hashCode() + text.hashCode());
    }
}
