package com.gmail.khitirinikoloz.speaksport.repository.signup.error;

public class UsernameError {
    public static final String NO_USERNAME_ERROR = "no username error";
    private String usernameError;
    private String errorBody;
    private int status;

    public UsernameError(String usernameError) {
        this.usernameError = usernameError;
    }

    public void setErrorBody(String errorBody) {
        this.errorBody = errorBody;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUsernameError() {
        return usernameError;
    }

    public String getErrorBody() {
        return errorBody;
    }

    public int getStatus() {
        return status;
    }
}
