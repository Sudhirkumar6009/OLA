package com.maps.trackola;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ola.mapsdk.camera.MapControlSettings;
import com.ola.mapsdk.interfaces.OlaMapCallback;
import com.ola.mapsdk.model.OlaLatLng;
import com.ola.mapsdk.model.OlaMarkerOptions;
import com.ola.mapsdk.view.Marker;
import com.ola.mapsdk.view.OlaMap;
import com.ola.mapsdk.view.OlaMapView;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity implements LocationListener {

    private OlaMap olaMap;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private Marker userMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton currentLocFab = findViewById(R.id.currentLocation);
        OlaMapView mapView = findViewById(R.id.mapView);

        MapControlSettings mapControlSettings = new MapControlSettings.Builder().build();

        mapView.getMap("CxGXMz8BWgt0Sl9aT53lriuPGuYYf9ohZotfvRdy", new OlaMapCallback() {
            @Override
            public void onMapReady(@NonNull OlaMap map) {
                olaMap = map;
                requestLocationPermission();
            }

            @Override
            public void onMapError(@NotNull String error) {
                Toast.makeText(MainActivity.this, "Map Error: " + error, Toast.LENGTH_LONG).show();
            }
        }, mapControlSettings);


        getCurrentLocation();

        currentLocFab.setOnClickListener(view -> {
            if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
                return;
            }
            getCurrentLocation();
        });

        currentLocFab.setImageTintList(ColorStateList.valueOf(Color.WHITE));

    }

    private void requestLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    private void getCurrentLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (locationManager != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, this, null);
            }
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        OlaLatLng userLocation = new OlaLatLng(latitude, longitude, 0.0);

        double zoomLevel = 15.0;

        olaMap.zoomToLocation(userLocation, zoomLevel);

        if (userMarker != null) {
            userMarker.setPosition(userLocation);
        } else {
            OlaMarkerOptions markerOptions = new OlaMarkerOptions.Builder()
                    .setMarkerId("user_location")
                    .setPosition(userLocation)
                    .setIsAnimationEnable(true)
                    .build();

            userMarker = olaMap.addMarker(markerOptions);
            olaMap.zoomToLocation(userLocation, zoomLevel);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }
}
