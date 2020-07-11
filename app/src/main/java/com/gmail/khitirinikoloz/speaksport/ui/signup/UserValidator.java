package com.gmail.khitirinikoloz.speaksport.ui.signup;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserValidator {
    private static final int USERNAME_MIN_LENGTH = 3;
    private static final int PASSWORD_MIN_LENGTH = 6;
    private static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static boolean isEmailValid(final String email) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email);
        return matcher.find();
    }

    public static boolean isUsernameValid(final String username) {
        return username != null && username.trim().length() >= USERNAME_MIN_LENGTH;
    }

    public static boolean isPasswordValid(final String password) {
        return password != null && password.trim().length() >= PASSWORD_MIN_LENGTH;
    }
}
