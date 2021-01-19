package com.taocoder.ecommerce.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

public class Utils {
    public static void toastMessage(Context context, String message) {
        Toast.makeText(context,  message, Toast.LENGTH_LONG).show();
    }

    public static void hideKeyboard(Context context, View view) {
        if (view == null) return;
        InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static boolean isNetworkAvailable(Context context) {

        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo[] networks = manager.getAllNetworkInfo();
        for (NetworkInfo info: networks) {
            if (info.isConnectedOrConnecting())
                return true;
        }
        return false;
    }
}
