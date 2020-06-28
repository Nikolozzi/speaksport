package com.gmail.khitirinikoloz.speaksport.repository.signup.error;

public class UsernameError {
    public static final String NO_USERNAME_ERROR = "no username error";
    private String usernameResponse;
    private boolean failedRequest;

    public UsernameError(String usernameResponse, boolean failedRequest) {
        this.usernameResponse = usernameResponse;
        this.failedRequest = failedRequest;
    }

    public String getUsernameResponse() {
        return usernameResponse;
    }

    public boolean isFailedRequest() {
        return failedRequest;
    }
}
