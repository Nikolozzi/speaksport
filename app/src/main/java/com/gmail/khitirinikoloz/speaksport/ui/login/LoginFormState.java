package com.gmail.khitirinikoloz.speaksport.ui.login;

/**
 * Data validation state of the login form.
 */
class LoginFormState {
    private boolean isDataValid;

    LoginFormState(boolean isDataValid) {
        this.isDataValid = isDataValid;
    }

    boolean isDataValid() {
        return isDataValid;
    }
}
