package com.gmail.khitirinikoloz.speaksport.entity;

import androidx.annotation.NonNull;

public class Comment {
    private final String username;
    private final String comment;


    public Comment(@NonNull String username, @NonNull String comment) {
        this.username = username;
        this.comment = comment;
    }

    public String getUsername() {
        return username;
    }

    public String getComment() {
        return comment;
    }
}
