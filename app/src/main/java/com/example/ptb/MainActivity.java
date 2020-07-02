package com.example.ptb;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.ResultReceiver;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.ptb.fragments.HomeFragment;
import com.example.ptb.fragments.ListFragment;
import com.example.ptb.fragments.ProfileFragment;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import static com.example.ptb.Contstants.LOCATION_DATA_EXTRA;
import static com.example.ptb.Contstants.RECEIVER;
import static com.example.ptb.Contstants.RESULT_DATA_KEY;
import static com.example.ptb.Contstants.SUCCESS_RESULT;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    boolean doubleBackToExitPressedOnce = false;
    private SharePreferenceManager spManager;
    private Button btnLogout;
    public double lat, lng;
    private final static String TAG = "MainActivity";
    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;

    private FirebaseAuth mAuth;
    private ResultReceiver resultReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadFragment(new HomeFragment());
        BottomNavigationView bottomNavigationView = findViewById(R.id.bn_main);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        btnLogout = findViewById(R.id.btn_logout);
        resultReceiver = new AddressResultReceiver(new Handler());
        spManager = new SharePreferenceManager(this);
//        if(spManager.getSpId()==""){
//            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
//            startActivity(intent);
//            finish();
//        }
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_LOCATION_PERMISSION);

        } else {
            getLatLng();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLatLng();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    protected void getLatLng() {
//        GPSTracker gpsTracker = new GPSTracker(getApplicationContext());
//        Log.d(TAG, gpsTracker.toString());
//        final Location location = gpsTracker.getLocation();
//        if (location != null) {
//            lat = location.getLatitude();
//            lng = location.getLongitude();
//            Log.d("tag",location.getProvider());
//
//            spManager.saveSPDouble(SharePreferenceManager.SP_Lat, location.getLatitude());
//            spManager.saveSPDouble(SharePreferenceManager.SP_Lng, location.getLongitude());
//        }

        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000).setFastestInterval(3000).setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationServices.getFusedLocationProviderClient(MainActivity.this)
                .requestLocationUpdates(locationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        LocationServices.getFusedLocationProviderClient(MainActivity.this)
                                .removeLocationUpdates(this);
                        if (locationResult != null && locationResult.getLocations().size() > 0) {
                            int latestLocationIndex = locationResult.getLocations().size() - 1;
                            double latitude = locationResult.getLocations().get(latestLocationIndex).getLatitude();
                            double longitude = locationResult.getLocations().get(latestLocationIndex).getLongitude();
                            Log.d("latlng", latitude + " - " + longitude);

                            spManager.saveSPDouble(SharePreferenceManager.SP_Lat, latitude);
                            spManager.saveSPDouble(SharePreferenceManager.SP_Lng, longitude);
                            Location location1 = new Location("providerNa");
                            location1.setLatitude(latitude);
                            location1.setLongitude(longitude);
                            fetchAddressFromLatLong(location1);
                        } else {

                        }
                    }
                }, Looper.getMainLooper());
    }

    private void fetchAddressFromLatLong(Location location) {
        Intent intent = new Intent(this, FetchAddressIntentService.class);
        intent.putExtra(RECEIVER, resultReceiver);
        intent.putExtra(LOCATION_DATA_EXTRA, location);
        startService(intent);
    }


    private class AddressResultReceiver extends ResultReceiver {

        /**
         * Create a new ResultReceive to receive results.  Your
         * {@link #onReceiveResult} method will be called from the thread running
         * <var>handler</var> if given, or from an arbitrary thread if null.
         *
         * @param handler
         */
        AddressResultReceiver(Handler handler) {
            super(handler);
        }

        String address;

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            super.onReceiveResult(resultCode, resultData);
            if (resultCode == SUCCESS_RESULT) {
                address = resultData.getString(RESULT_DATA_KEY);
                Log.d(TAG + "latlng", address);
                spManager.saveSPString(SharePreferenceManager.SP_Address, address);
            } else {
                Toast.makeText(MainActivity.this, resultData.getString(RESULT_DATA_KEY), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            this.finishAffinity();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Tekan lagi untuk keluar ", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fragment = null;
        switch (menuItem.getItemId()) {
            case R.id.home_menu:
                fragment = new HomeFragment();
                break;
            case R.id.list_menu:
                fragment = new ListFragment();
                break;
            case R.id.profile_menu:
                fragment = new ProfileFragment();

                break;
        }
        return loadFragment(fragment);
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fl_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }
}
