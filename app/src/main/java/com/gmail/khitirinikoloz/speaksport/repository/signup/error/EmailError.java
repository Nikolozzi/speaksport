package com.gmail.khitirinikoloz.speaksport.repository.signup.error;

public class EmailError {
    public static final String NO_EMAIL_ERROR = "no email error";
    private String emailError;
    private String errorBody;
    private int status;

    public EmailError(String emailError) {
        this.emailError = emailError;
    }

    public void setErrorBody(String errorBody) {
        this.errorBody = errorBody;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getEmailError() {
        return emailError;
    }

    public String getErrorBody() {
        return errorBody;
    }

    public int getStatus() {
        return status;
    }
}
