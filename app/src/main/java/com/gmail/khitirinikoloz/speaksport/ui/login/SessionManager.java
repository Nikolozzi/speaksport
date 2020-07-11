package com.gmail.khitirinikoloz.speaksport.ui.login;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

public final class SessionManager {
    private final SharedPreferences preferences;
    private final SharedPreferences.Editor editor;

    private static final String PREF_NAME = "Pref";
    private static final String IS_LOGIN = "isLoggedIn";
    private static final String KEY_ID = "id";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_TOKEN = "token";
    private static final String KEY_IMAGE_PATH = "image_path";

    @SuppressLint("CommitPrefEdits")
    public SessionManager(@NonNull final Context context) {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public void createLoginSession(@NonNull final LoggedInUser loginResponse) {
        editor.putBoolean(IS_LOGIN, true);
        editor.putLong(KEY_ID, loginResponse.getUserId());
        editor.putString(KEY_USERNAME, loginResponse.getUsername());
        editor.putString(KEY_EMAIL, loginResponse.getEmail());
        editor.putString(KEY_TOKEN, loginResponse.getToken());

        editor.apply();
    }

    public void saveProfileImage(@NonNull final String path) {
        editor.putString(KEY_IMAGE_PATH, path);
        editor.apply();
    }

    public LoggedInUser getLoggedInUser() {
        final long id = preferences.getLong(KEY_ID, -1);
        final String username = preferences.getString(KEY_USERNAME, null);
        final String email = preferences.getString(KEY_EMAIL, null);
        final String token = preferences.getString(KEY_TOKEN, null);
        final String imagePath = preferences.getString(KEY_IMAGE_PATH, null);

        final LoggedInUser loggedInUser = new LoggedInUser(id, username, email, token);
        loggedInUser.setImagePath(imagePath);
        return loggedInUser;
    }

    public void logOutUser() {
        editor.remove(IS_LOGIN);
        editor.remove(KEY_EMAIL);
        editor.remove(KEY_USERNAME);
        editor.remove(KEY_ID);
        editor.remove(KEY_TOKEN);
        editor.apply();
    }

    public boolean isUserLoggedIn() {
        return preferences.getBoolean(IS_LOGIN, false);
    }
}
