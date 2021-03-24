package com.kadenhellewellcouponcalendar.phoneapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.kadenhellewellcouponcalendar.api.viewmodels.UserViewModel;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        UserViewModel userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        toolbar.setNavigationOnClickListener(view -> {
            drawer.open();
        });

        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(menuItem -> {
            menuItem.setChecked(true);
            //TODO switch statement on menuItem.getItemId()
            switch (menuItem.getItemId())
            {
                case R.id.home_item:
                    //TODO redirect to home fragment
                    break;
                case R.id.sign_out_item:
                    userViewModel.signOut();
                    Intent intent = new Intent(this, SignInUpActivity.class);
                    startActivity(intent);
                    finish();
                    break;
            }
            drawer.close();
            return true;
        });
    }
}