package com.example.dogwalkerandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.dogwalkerandroid.utils.PermissionUtils;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

public class AfterSplash extends AppCompatActivity {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 123;
    private String TAG = AfterSplash.class.getSimpleName();
    private FusedLocationProviderClient fusedLocationClient;
    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;


    private void stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(mLocationCallback);
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Log.e(TAG, "startLocationUpdates: " );
        fusedLocationClient.requestLocationUpdates(mLocationRequest,
                mLocationCallback,
                Looper.getMainLooper());
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

//    private void reqlocateionUpdates(){
//         mLocationRequest = LocationRequest.create();
//        mLocationRequest.setInterval(60000);
//        mLocationRequest.setFastestInterval(5000);
//        mLocationRequest.setNumUpdates(1);
//        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//         mLocationCallback = new LocationCallback() {
//            @Override
//            public void onLocationResult(LocationResult locationResult) {
//                Log.e(TAG, "onLocationResult: may be null");
//
//                if (locationResult == null) {
//                    Log.e(TAG, "onLocationResult: "+locationResult.getLastLocation());
//                    return;
//                }
//                for (Location location : locationResult.getLocations()) {
//                    if (location != null) {
//                        Log.e(TAG, "onLocationResult: "+locationResult.getLastLocation());
//
//                        //TODO: UI updates.
//                    }
//                }
//            }
//        };
//    }






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_splash);
        SharedPref.init(AfterSplash.this);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);


        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(10000);
        mLocationRequest.setNumUpdates(1);
        mLocationRequest.setFastestInterval(10000 / 2);


        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                Log.e(TAG, "onLocationResult: maybe null" );
                if (locationResult == null) {
                    Log.e(TAG, "onLocationResult: "+locationResult.getLastLocation().getLatitude() );

                    double lat = locationResult.getLastLocation().getLatitude();
                   double lng =  locationResult.getLastLocation().getLongitude();
                   SharedPref.putFloat("lat",(float)lat);
                    SharedPref.putFloat("lng",(float)lng);

                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    Log.e(TAG, "onLocationResult: "+locationResult.getLastLocation().getLatitude() );

                    double lat = locationResult.getLastLocation().getLatitude();
                    double lng =  locationResult.getLastLocation().getLongitude();
                    SharedPref.putFloat("lat",(float)lat);
                    SharedPref.putFloat("lng",(float)lng);
                    // Update UI with location data
                    // ...
                }
            }
        };


        findViewById(R.id.dogowner).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPref.putString(SharedPref.USER_STATE,SharedPref.DOGOWNER);
                navigateToLogin();
            }
        });

        findViewById(R.id.dogwalker).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPref.putString(SharedPref.USER_STATE,SharedPref.DOGWALKER);
                navigateToLogin();
            }
        });

        findViewById(R.id.admin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPref.putString(SharedPref.USER_STATE,SharedPref.ADMIN);
                navigateToLogin();

            }
        });


        getLocationper();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        stopLocationUpdates();
    }

    private void navigateToLogin(){
        startActivity(new Intent(AfterSplash.this,Login.class));

    }

    private void getLocationper(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
//            map.setMyLocationEnabled(true);
            Log.e(TAG, "Allready have permission ");
//            fusedLocationClient.getLastLocation()
//                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
//                        @Override
//                        public void onSuccess(Location location) {
//                            // Got last known location. In some rare situations this can be null.
//                            Log.e(TAG, "location null: " );
//                            if (location != null) {
//                                Log.e(TAG, "onSuccess: "+location.getLatitude() );
//
//                                // Logic to handle location object
//                            }
//                        }
//                    }).addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            Log.e(TAG, "onFailure: "+e.getMessage() );
//                        }
//                    });


startLocationUpdates();

        } else {
            PermissionUtils.requestLocationPermissions(this, LOCATION_PERMISSION_REQUEST_CODE, true);

        }

        // 2. Otherwise, request location permissions from the user.
    }


    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION) || PermissionUtils
                .isPermissionGranted(permissions, grantResults,
                        Manifest.permission.ACCESS_COARSE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            Log.e(TAG, "Permission requested: " );
startLocationUpdates();

//            fusedLocationClient.getLastLocation()
//                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
//                        @Override
//                        public void onSuccess(Location location) {
//                            // Got last known location. In some rare situations this can be null.
//                            if (location != null) {
//                                Log.e(TAG, "onSuccess: "+location.getLatitude() );
//                                // Logic to handle location object
//                            }
//                        }
//                    });

        } else {
            // Permission was denied. Display an error message
            // Display the missing permission error dialog when the fragments resume.
//            permissionDenied = true;
            Log.e(TAG, "Permission denied: " );

        }
    }
}