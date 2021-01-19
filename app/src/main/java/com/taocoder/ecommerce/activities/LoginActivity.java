package com.taocoder.ecommerce.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;

import com.taocoder.ecommerce.OnFragmentChangeListener;
import com.taocoder.ecommerce.R;
import com.taocoder.ecommerce.data.SessionManager;
import com.taocoder.ecommerce.fragments.LoginFragment;

public class LoginActivity extends AppCompatActivity implements OnFragmentChangeListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//        SessionManager sm = SessionManager.getInstance(this);
//        if (!sm.getToken().isEmpty()) {
//            startActivity(new Intent(this, MainActivity.class));
//            finish();
//        }

        changeFragment(new LoginFragment(), false);
    }

    @Override
    public void onFragmentChange(Fragment fragment) {
        changeFragment(fragment, true);
    }

    private void changeFragment(Fragment fragment, boolean backTrack) {

        final FragmentManager fragmentManager = getSupportFragmentManager();
        final FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        transaction.replace(R.id.container, fragment);

        if (backTrack) {
            transaction.addToBackStack(null);
        }

        transaction.commit();
    }
}