package com.gmail.khitirinikoloz.speaksport.ui.login;

import java.util.Objects;

public class LoggedInUser {
    private final int userId;
    private final String username;
    private final String token;

    public LoggedInUser(int userId, String username, String token) {
        this.userId = Objects.requireNonNull(userId);
        this.username = Objects.requireNonNull(username);
        this.token = Objects.requireNonNull(token);
    }

    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getToken() {
        return token;
    }

    @Override
    public String toString() {
        return "LoggedInUser{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
