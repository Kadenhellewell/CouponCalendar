package com.kadenhellewellcouponcalendar.phoneapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import com.kadenhellewellcouponcalendar.api.viewmodels.UserViewModel;

// This is the launcher activity
// If the user is signed in, it will redirect to HomeActivity

public class SignInUpActivity extends AppCompatActivity {
    UserViewModel viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_up);
        viewModel = new ViewModelProvider(this).get(UserViewModel.class);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, SignInFragment.class, null)
                    .setReorderingAllowed(true)
                    .commit();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        viewModel.getUser().observe(this, (user) -> {
            if (user != null) {
                Intent intent = new Intent(this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}