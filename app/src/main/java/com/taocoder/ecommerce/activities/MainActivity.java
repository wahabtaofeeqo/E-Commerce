package com.taocoder.ecommerce.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.taocoder.ecommerce.R;
import com.taocoder.ecommerce.data.SessionManager;
import com.taocoder.ecommerce.utils.Utils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Utils.toastMessage(this, SessionManager.getInstance(this).getToken());
    }
}