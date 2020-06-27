package com.gmail.khitirinikoloz.speaksport.repository.signup.response;

import java.io.Serializable;

public class UserResponse implements Serializable {
    private Long id;

    private String username;

    private String email;

    private String password;

    private String fullName;

    private String description;

    private Object photo;

    private boolean failedRequest;

    private Integer responseCode;

    public UserResponse(boolean failedRequest, Integer responseCode) {
        this.failedRequest = failedRequest;
        this.responseCode = responseCode;
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

    public Object getPhoto() {
        return photo;
    }

    public void setPhoto(Object photo) {
        this.photo = photo;
    }

    public boolean isFailedRequest() {
        return failedRequest;
    }

    public void setFailedRequest(boolean failedRequest) {
        this.failedRequest = failedRequest;
    }

    public Integer getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(Integer responseCode) {
        this.responseCode = responseCode;
    }

    @Override
    public String toString() {
        return "UserResponse{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", fullName='" + fullName + '\'' +
                ", description='" + description + '\'' +
                ", photo=" + photo +
                ", failedRequest=" + failedRequest +
                ", responseCode=" + responseCode +
                '}';
    }
}
