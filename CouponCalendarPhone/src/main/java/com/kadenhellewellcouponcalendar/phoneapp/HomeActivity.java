package com.kadenhellewellcouponcalendar.phoneapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.kadenhellewellcouponcalendar.api.viewmodels.CouponViewModel;
import com.kadenhellewellcouponcalendar.api.viewmodels.UserViewModel;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.search.MapboxSearchSdk;
import com.mapbox.search.ResponseInfo;
import com.mapbox.search.SearchEngine;
import com.mapbox.search.SearchRequestTask;
import com.mapbox.search.SearchSelectionCallback;
import com.mapbox.search.result.SearchResult;
import com.mapbox.search.result.SearchSuggestion;

import java.util.List;

public class HomeActivity extends AppCompatActivity {
    public MaterialToolbar toolbar;
    CouponViewModel couponViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Set up various mapbox things
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));


        setContentView(R.layout.activity_home);
        UserViewModel userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        couponViewModel = new ViewModelProvider(this).get(CouponViewModel.class);
        couponViewModel.setUser(userViewModel.getUser());
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

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            redirectToFragment(NewCouponFragment.class);
        });

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