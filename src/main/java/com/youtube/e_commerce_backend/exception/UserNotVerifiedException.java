package com.youtube.e_commerce_backend.exception;

public class UserNotVerifiedException extends Exception{

    private Boolean newEmailSent;

    public UserNotVerifiedException(boolean newEmailSent) {
        this.newEmailSent = newEmailSent;
    }

    public boolean isNewEmailSent() {
        return newEmailSent;
    }
}
