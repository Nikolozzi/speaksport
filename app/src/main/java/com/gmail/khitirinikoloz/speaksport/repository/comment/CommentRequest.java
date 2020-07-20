package com.gmail.khitirinikoloz.speaksport.repository.comment;

import com.gmail.khitirinikoloz.speaksport.model.Post;
import com.gmail.khitirinikoloz.speaksport.model.User;

public class CommentRequest {
    private String text;
    private User user;
    private Post post;

    public CommentRequest() {
    }

    public CommentRequest(String text, User user, Post post) {
        this.text = text;
        this.user = user;
        this.post = post;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    @Override
    public String toString() {
        return "CommentRequest{" +
                "text='" + text + '\'' +
                ", user=" + user +
                ", post=" + post +
                '}';
    }
}
