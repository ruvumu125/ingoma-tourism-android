package com.ingoma.tourism.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.ingoma.tourism.model.User;

public class LoginPreferencesManager {

    private static final String PREF_NAME = "MyAppPrefs";     // Preferences file name
    private static final String KEY_JWT_TOKEN = "jwt_token";  // Key for the JWT token
    private static final String KEY_USER = "user"; // Key for User object

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Gson gson;

    public LoginPreferencesManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        gson = new Gson();
    }

    // Save JWT Token
    public void saveToken(String token) {
        editor.putString(KEY_JWT_TOKEN, token);
        editor.apply();
    }

    // Get JWT Token
    public String getToken() {
        return sharedPreferences.getString(KEY_JWT_TOKEN, null);
    }

    // Check if JWT Token is available
    public boolean isLoggedIn() {
        return getToken() != null;
    }

    // Save User object
    public void saveUser(User user) {
        String json = gson.toJson(user);
        editor.putString(KEY_USER, json);
        editor.apply();
    }

    // Get User object
    public User getUser() {
        String json = sharedPreferences.getString(KEY_USER, null);
        if (json != null) {
            return gson.fromJson(json, User.class);
        }
        return null;
    }

    // Clear JWT Token, User, and Logout
    public void logout() {
        editor.remove(KEY_JWT_TOKEN);
        editor.remove(KEY_USER); // Remove the user as well
        editor.apply();
    }
}
