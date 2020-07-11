package com.gmail.khitirinikoloz.speaksport.repository.login.response;

import java.io.Serializable;

public class LoginResponse implements Serializable {
    private int userId;
    private String username;
    private String email;
    private String fullName;
    private String token;
    private boolean failedRequest;
    private Integer responseCode;

    public LoginResponse(boolean failedRequest, Integer responseCode) {
        this.failedRequest = failedRequest;
        this.responseCode = responseCode;
    }

    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getFullName() {
        return fullName;
    }

    public String getToken() {
        return token;
    }

    public boolean isFailedRequest() {
        return failedRequest;
    }

    public Integer getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(Integer responseCode) {
        this.responseCode = responseCode;
    }

    @Override
    public String toString() {
        return "LoginResponse{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", fullName='" + fullName + '\'' +
                ", token='" + token + '\'' +
                ", failedRequest=" + failedRequest +
                ", responseCode=" + responseCode +
                '}';
    }
}
