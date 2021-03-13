package com.kadenhellewellcouponcalendar.phoneapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.kadenhellewellcouponcalendar.api.Verify;

// This is the launcher activity
// If the user is signed in, it will redirect to HomeActivity

public class SignInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        Verify.verifyPhoneApp();
    }
}