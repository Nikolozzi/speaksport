package com.gmail.khitirinikoloz.speaksport.model;

import androidx.annotation.NonNull;

import com.gmail.khitirinikoloz.speaksport.repository.signup.response.UserResponse;
import com.gmail.khitirinikoloz.speaksport.ui.login.LoggedInUser;

import java.io.Serializable;

public class User implements Serializable {
    private Long id;

    private String username;

    private String email;

    private String password;

    private String fullName;

    private String description;

    private Photo photo;

    public User(@NonNull final String username, @NonNull final String email, @NonNull final String password) {
        setUsername(username);
        setEmail(email);
        setPassword(password);
    }

    public User(@NonNull final LoggedInUser loggedInUser) {
        setId(loggedInUser.getUserId());
        setEmail(loggedInUser.getEmail());
        setUsername(loggedInUser.getUsername());
    }

    public User(@NonNull final UserResponse userResponse) {
        setId(userResponse.getId());
        setUsername(userResponse.getUsername());
        setEmail(userResponse.getEmail());
        setPassword(userResponse.getPassword());
        setFullName(userResponse.getFullName());
        setDescription(userResponse.getDescription());
        setPhoto(userResponse.getPhoto());
    }

    public User() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Photo getPhoto() {
        return photo;
    }

    public void setPhoto(Photo photo) {
        this.photo = photo;
    }

    @androidx.annotation.NonNull
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", fullName='" + fullName + '\'' +
                ", description='" + description + '\'' +
                ", photo=" + photo +
                '}';
    }
}
