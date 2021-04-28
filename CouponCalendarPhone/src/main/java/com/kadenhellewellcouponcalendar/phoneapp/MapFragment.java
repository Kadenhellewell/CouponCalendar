package com.kadenhellewellcouponcalendar.phoneapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableList;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.kadenhellewellcouponcalendar.api.models.Coupon;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.LocationComponentOptions;
import com.mapbox.mapboxsdk.location.OnCameraTrackingChangedListener;
import com.mapbox.mapboxsdk.location.OnLocationCameraTransitionListener;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;

import com.mapbox.mapboxsdk.plugins.markerview.MarkerView;
import com.mapbox.mapboxsdk.plugins.markerview.MarkerViewManager;

import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.search.MapboxSearchSdk;
import com.mapbox.search.ResponseInfo;
import com.mapbox.search.SearchEngine;
import com.mapbox.search.SearchOptions;
import com.mapbox.search.SearchRequestTask;
import com.mapbox.search.SearchSelectionCallback;
import com.mapbox.search.result.SearchResult;
import com.mapbox.search.result.SearchSuggestion;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class MapFragment extends Fragment {
    MapView mapView;
    MarkerView userMarker;
    //TODO make observable array list
    ObservableArrayList<Point> couponLocs = new ObservableArrayList<>();
    HomeActivity activity;

    //Various mapbox things
    public SearchEngine searchEngine;
    public SearchRequestTask searchRequestTask;

    public final SearchSelectionCallback searchCallback = new SearchSelectionCallback() {

        @Override
        public void onSuggestions(
                @NonNull List<? extends SearchSuggestion> suggestions,
                @NonNull ResponseInfo responseInfo
        ) {
            if (suggestions.isEmpty()) {
                Log.i("SearchApiExample", "No suggestions found");
            } else {
                Log.i("SearchApiExample", "Search suggestions: " + suggestions + "\nSelecting first...");
                searchRequestTask = searchEngine.select(suggestions.get(0), this);
            }
        }

        @Override
        public void onResult(
                @NonNull SearchSuggestion searchSuggestion,
                @NonNull SearchResult result,
                @NonNull ResponseInfo responseInfo
        ) {
            Log.i("SearchApiExample", "Search result: " + result);
            // result.getCoordinate();
            // result.getEtaMinutes();
            // TODO check time or distance when we get to that
            if (result.getCoordinate() != null)
            {
                System.out.println("Non null coordinate\n");
                couponLocs.add(result.getCoordinate());
                System.out.println(result.getCoordinate().toString());
            }
        }

        @Override
        public void onCategoryResult(
                @NonNull SearchSuggestion searchSuggestion,
                @NonNull List<? extends SearchResult> results,
                @NonNull ResponseInfo responseInfo
        ) {
            Log.i("SearchApiExample", "Category search results: " + results);
        }

        @Override
        public void onError(@NonNull Exception e) {
            Log.i("SearchApiExample", "Search error: ", e);
        }
    };



    public MapFragment() {
        super(R.layout.fragment_map);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        activity = (HomeActivity)getActivity();
        searchEngine = MapboxSearchSdk.createSearchEngine();


        final SearchOptions options = new SearchOptions.Builder()
                .build();



        mapView = view.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull MapboxMap mapboxMap) {
                mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        LocationManager manager = (LocationManager)activity.getSystemService(Context.LOCATION_SERVICE);
                        if (activity.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            activity.requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                        }

                        for(Coupon coupon : activity.couponViewModel.getCoupons())
                        {
                            searchRequestTask = searchEngine.search(coupon.address, options, searchCallback);
                        }

                        MarkerViewManager markerViewManager = new MarkerViewManager(mapView, mapboxMap);
                        // create markerview for each coordinate in couponLocs
                        couponLocs.addOnListChangedCallback(new ObservableList.OnListChangedCallback<ObservableList<Point>>() {
                            @Override
                            public void onChanged(ObservableList<Point> sender) {
                                for(Point point : sender)
                                {
                                    markerViewManager.addMarker(createMakrerView(point));
                                    System.out.println(couponLocs);
                                }
                            }

                            @Override
                            public void onItemRangeChanged(ObservableList<Point> sender, int positionStart, int itemCount) {

                            }

                            @Override
                            public void onItemRangeInserted(ObservableList<Point> sender, int positionStart, int itemCount) {

                            }

                            @Override
                            public void onItemRangeMoved(ObservableList<Point> sender, int fromPosition, int toPosition, int itemCount) {

                            }

                            @Override
                            public void onItemRangeRemoved(ObservableList<Point> sender, int positionStart, int itemCount) {

                            }
                        });


                        GeoJsonSource source = new GeoJsonSource("points",
                                FeatureCollection.fromFeatures(new Feature[]{
                                        Feature.fromGeometry(LineString.fromLngLats(couponLocs))
                                })
                        );

                        style.addSource(source);

                        style.addLayer(new LineLayer("line-layer", "points").withProperties(
                                PropertyFactory.lineDasharray(new Float[] {0.01f, 2f}),
                                PropertyFactory.lineCap(Property.LINE_CAP_ROUND),
                                PropertyFactory.lineJoin(Property.LINE_JOIN_ROUND),
                                PropertyFactory.lineWidth(5f),
                                PropertyFactory.lineColor(Color.parseColor("#e55e5e"))
                        ));


                        LocationComponent locationComponent = mapboxMap.getLocationComponent();

                        locationComponent.activateLocationComponent(
                                LocationComponentActivationOptions.builder(activity, style)
                                        .useSpecializedLocationLayer(true)
                                        .build()
                        );

                        locationComponent.setLocationComponentEnabled(true);

                        locationComponent.setCameraMode(CameraMode.TRACKING_GPS_NORTH, new OnLocationCameraTransitionListener() {
                            @Override
                            public void onLocationCameraTransitionFinished(int cameraMode) {
                                locationComponent.zoomWhileTracking(17, 100);
                            }

                            @Override
                            public void onLocationCameraTransitionCanceled(int cameraMode) {

                            }
                        });
                    }
                });
            }
        });
    }

    MarkerView createMakrerView(Point point)
    {
        TextView view = new TextView(activity);
        view.setText("Marker on Map");
        view.setTextSize(24);
        return new MarkerView(new LatLng(point.latitude(), point.longitude()), view);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mapView.onDestroy();
    }

}