package com.example.mapandlocation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.mapandlocation.databinding.ActivityMainBinding;
import com.example.mapandlocation.utils.MyLocationProvider;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private ActivityMainBinding binding;
    private static final Object LOCATION_PERMISSION_REQUEST_CODE = 100;
    private static final int LOCATION_REQUEST_CODE = 10;
    MyLocationProvider myLocationProvider;
    Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.mapView.getMapAsync(this);
        binding.mapView.onCreate(savedInstanceState);

        myLocationProvider = new MyLocationProvider(this);

            if (isLocationPermissionAllowed()) {
                location = myLocationProvider.getCurrentLocation(null);
                Log.d("dddd","onCreate: "+location);
            }
            else{
                    requestLocationPermission();
                }
            }

    @Override
    protected void onStart() {
        super.onStart();
        binding.mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        binding.mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        binding.mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        binding.mapView.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding.mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        binding.mapView.onLowMemory();
    }

    private void requestLocationPermission() {


        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            showUIMessage ("need your location");
        }
        else {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    (Integer) LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    private void showUIMessage(String message) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage(message);
        alertDialog.setTitle("Location Service");
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                //request permission
                ActivityCompat.requestPermissions( MainActivity.this, new String[] {
                        Manifest.permission.ACCESS_FINE_LOCATION },
                        (Integer) LOCATION_PERMISSION_REQUEST_CODE);

            }
        });

    }

    private boolean isLocationPermissionAllowed() {

            if(ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED)
                return true;
            return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        switch (requestCode) {
            case LOCATION_REQUEST_CODE:
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    location = myLocationProvider.getCurrentLocation(null);
                    Log.d("dddd","onCreate: "+location);
                }
                else
                    Toast.makeText(this, "feature not available", Toast.LENGTH_SHORT).show();
                return;
        }
    }
    GoogleMap myGoogleMap;

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        myGoogleMap = googleMap;
        addUserMarker();

    }
    Marker userMarker;

    protected void addUserMarker() {

        if (location == null || myGoogleMap == null) {
            Toast.makeText(this,"Something wrong", Toast.LENGTH_SHORT).show();
            return;
        }
        userMarker= myGoogleMap.addMarker(new MarkerOptions()
                        .title("yes")
                        .position(new LatLng(location.getLatitude(), location.getLongitude()))
        );
        //make camera focus on marker
        myGoogleMap.animateCamera (CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(),
                location.getLongitude()),18));
    }
}
