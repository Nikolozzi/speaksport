package com.gmail.khitirinikoloz.speaksport.repository.login.request;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class LoginRequest implements Serializable {
    private final String username;
    private final String password;

    public LoginRequest(@NonNull String username, @NonNull String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
