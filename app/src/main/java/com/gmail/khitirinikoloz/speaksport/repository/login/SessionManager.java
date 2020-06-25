package com.gmail.khitirinikoloz.speaksport.repository.login;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SessionManager {
    private final SharedPreferences preferences;
    private final SharedPreferences.Editor editor;
    private final Context context;

    private static final String PREF_NAME = "Pref";
    private static final String IS_LOGIN = "isLoggedIn";
    private static final String KEY_USERNAME = "username";

    @SuppressLint("CommitPrefEdits")
    public SessionManager(@NonNull final Context context) {
        this.context = context;
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public void createLoginSession(@NonNull final String username) {
        Objects.requireNonNull(username);

        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_USERNAME, username);

        editor.apply();
    }

    public Map<String, String> getUserDetails() {
        final Map<String, String> user = new HashMap<>();
        user.put(KEY_USERNAME, preferences.getString(KEY_USERNAME, null));

        return user;
    }

    public void logoutUser() {
        editor.clear();
        editor.apply();
    }

    public boolean isUserLoggedIn() {
        return preferences.getBoolean(IS_LOGIN, false);
    }
}
