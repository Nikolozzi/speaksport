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
    private static final String KEY_TOKEN = "token";

    @SuppressLint("CommitPrefEdits")
    public SessionManager(@NonNull final Context context) {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public void createLoginSession(@NonNull final LoggedInUser loginResponse) {
        editor.putBoolean(IS_LOGIN, true);
        editor.putInt(KEY_ID, loginResponse.getUserId());
        editor.putString(KEY_USERNAME, loginResponse.getUsername());
        editor.putString(KEY_TOKEN, loginResponse.getToken());

        editor.apply();
    }

    public LoggedInUser getLoggedInUser() {
        final int id = preferences.getInt(KEY_ID, -1);
        final String username = preferences.getString(KEY_USERNAME, null);
        final String token = preferences.getString(KEY_TOKEN, null);

        return new LoggedInUser(id, username, token);
    }

    public void logOutUser() {
        editor.clear();
        editor.apply();
    }

    public boolean isUserLoggedIn() {
        return preferences.getBoolean(IS_LOGIN, false);
    }
}
