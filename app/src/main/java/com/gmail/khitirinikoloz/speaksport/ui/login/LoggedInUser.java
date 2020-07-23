package com.gmail.khitirinikoloz.speaksport.ui.login;

import androidx.annotation.NonNull;

public class LoggedInUser {
    private final long userId;
    private String username;
    private String email;
    private final String token;
    private String imagePath;

    public LoggedInUser(long userId, String username, String email, String token, String imagePath) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.token = token;
        this.imagePath = imagePath;
    }

    public long getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    @NonNull
    @Override
    public String toString() {
        return "LoggedInUser{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", token='" + token + '\'' +
                ", imagePath='" + imagePath + '\'' +
                '}';
    }
}
