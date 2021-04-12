package com.kadenhellewellcouponcalendar.phoneapp;

import android.app.Application;

import com.mapbox.search.MapboxSearchSdk;
import com.mapbox.search.location.DefaultLocationProvider;

public class SearchApplication extends Application {
    @Override
    public void onCreate () {
        super.onCreate();
        MapboxSearchSdk.initialize(this, getString(R.string.mapbox_access_token), new DefaultLocationProvider(this));
    }
}
