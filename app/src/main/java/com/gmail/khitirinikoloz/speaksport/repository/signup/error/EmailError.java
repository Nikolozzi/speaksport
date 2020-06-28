package com.gmail.khitirinikoloz.speaksport.repository.signup.error;

public class EmailError {
    public static final String NO_EMAIL_ERROR = "no email error";
    private String emailResponse;
    private boolean failedRequest;

    public EmailError(String emailResponse, boolean failedRequest) {
        this.emailResponse = emailResponse;
        this.failedRequest = failedRequest;
    }

    public String getEmailResponse() {
        return emailResponse;
    }

    public boolean isFailedRequest() {
        return failedRequest;
    }
}
