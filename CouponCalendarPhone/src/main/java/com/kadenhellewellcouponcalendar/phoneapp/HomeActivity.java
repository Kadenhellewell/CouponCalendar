package com.kadenhellewellcouponcalendar.phoneapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.kadenhellewellcouponcalendar.api.viewmodels.CouponViewModel;
import com.kadenhellewellcouponcalendar.api.viewmodels.GiftCardViewModel;
import com.kadenhellewellcouponcalendar.api.viewmodels.UserViewModel;


import java.util.List;

public class HomeActivity extends AppCompatActivity {
    public MaterialToolbar toolbar;
    public CouponViewModel couponViewModel;
    public GiftCardViewModel giftCardViewModel;
    public UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }

        //Set up various mapbox things


        setContentView(R.layout.activity_home);

        //View models
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        couponViewModel = new ViewModelProvider(this).get(CouponViewModel.class);
        couponViewModel.setUser(userViewModel.getUser());
        giftCardViewModel = new ViewModelProvider(this).get(GiftCardViewModel.class);
        giftCardViewModel.setUser(userViewModel.getUser());
        //View Models

        //Navigation drawer thing
        toolbar = findViewById(R.id.topAppBar);
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
                    redirectToFragment(HomeFragment.class);
                    break;
                case R.id.coupons_item:
                    redirectToFragment(CouponsFragment.class);
                    break;
                case R.id.giftcards_item:
                    redirectToFragment(GiftCardsFragment.class);
                    break;
                case R.id.map_item:
                    redirectToFragment(MapFragment.class);
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
        //Navigation drawer thing

        //Floating action button
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            redirectToFragment(NewCouponFragment.class);
        });
        //Floating action button

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, HomeFragment.class, null)
                    .setReorderingAllowed(true)
                    .commit();
        }
    }



    public void redirectToFragment(Class fragment)
    {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment, null)
                .setReorderingAllowed(true)
                .addToBackStack(null)
                .commit();
    }
}