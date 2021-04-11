package com.kadenhellewellcouponcalendar.phoneapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

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

import java.util.ArrayList;


public class MapFragment extends Fragment {
    MapView mapView;
    MarkerView userMarker;
    ArrayList<Point> points = new ArrayList<>(); //TODO have this be an array of coupon locations
    HomeActivity activity;


    public MapFragment() {
        super(R.layout.fragment_map);
        activity = (HomeActivity)getActivity();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        //TODO this where map stuff goes

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
                            requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                        }


                        //MarkerViewManager markerViewManager = new MarkerViewManager(mapView, mapboxMap);

                        GeoJsonSource source = new GeoJsonSource("points",
                                FeatureCollection.fromFeatures(new Feature[]{
                                        Feature.fromGeometry(
                                                LineString.fromLngLats(points)
                                        )
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



                        LocationComponentOptions options = locationComponent.getLocationComponentOptions().toBuilder()
                                .trackingGesturesManagement(true)
                                .trackingInitialMoveThreshold(500)
                                .build();

                        locationComponent.applyStyle(options);



                        locationComponent.addOnCameraTrackingChangedListener(new OnCameraTrackingChangedListener() {
                            @Override
                            public void onCameraTrackingDismissed() {
                                System.out.println("DONE TRACKING");
                                new Thread(() -> {
                                    try {
                                        Thread.sleep(5000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    activity.runOnUiThread(() -> {
                                        locationComponent.setCameraMode(CameraMode.TRACKING_GPS_NORTH);
                                    });
                                }).start();
                            }

                            @Override
                            public void onCameraTrackingChanged(int currentMode) {
                                System.out.println("TRACKING CHANGED " + currentMode);
                            }
                        });




                        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, new LocationListener() {
                            @Override
                            public void onLocationChanged(@NonNull Location location) {
                                points.add(Point.fromLngLat(location.getLongitude(), location.getLatitude()));
                                source.setGeoJson(
                                        FeatureCollection.fromFeatures(new Feature[]{
                                                        Feature.fromGeometry(
                                                                LineString.fromLngLats(points)
                                                        )
                                                }
                                        )
                                );
                            }
                        });
                    }
                });
            }
        });
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
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

}