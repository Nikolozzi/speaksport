package com.gmail.khitirinikoloz.speaksport.repository.login.response;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class LoginResponse implements Serializable {
    private int userId;
    private String username;
    private String email;
    private String fullName;
    private String token;
    private String fileObject;
    private boolean failedRequest;
    private Integer responseCode;

    public LoginResponse(boolean failedRequest, Integer responseCode) {
        this.failedRequest = failedRequest;
        this.responseCode = responseCode;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getFileObject() {
        return fileObject;
    }

    public void setFileObject(String fileObject) {
        this.fileObject = fileObject;
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

    @NonNull
    @Override
    public String toString() {
        return "LoginResponse{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", fullName='" + fullName + '\'' +
                ", token='" + token + '\'' +
                ", fileObject='" + fileObject + '\'' +
                ", failedRequest=" + failedRequest +
                ", responseCode=" + responseCode +
                '}';
    }
}
