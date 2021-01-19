package com.taocoder.ecommerce.data;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private static final String NAME = "ecommerceSessionPreferences";
    private static SessionManager instance;

    //Fields
    private static final String token = "token";

    private SessionManager(Context context) {
        sharedPreferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public static SessionManager getInstance(Context context) {
        if (instance == null) {
            instance = new SessionManager(context);
        }
        return instance;
    }

    public String getToken() {
        return sharedPreferences.getString(token, "");
    }

    public void setToken(String value) {
        editor.putString(token, value);
        editor.commit();
    }
}
