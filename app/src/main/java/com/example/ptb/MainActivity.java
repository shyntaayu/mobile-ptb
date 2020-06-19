package com.example.ptb;

import android.Manifest;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.ptb.fragments.HomeFragment;
import com.example.ptb.fragments.ListFragment;
import com.example.ptb.fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    boolean doubleBackToExitPressedOnce = false;
    private SharePreferenceManager spManager;
    private Button btnLogout;
    public double lat, lng;
    private final static String TAG = "MainActivity";

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadFragment(new HomeFragment());
        BottomNavigationView bottomNavigationView = findViewById(R.id.bn_main);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        btnLogout = findViewById(R.id.btn_logout);
        spManager = new SharePreferenceManager(this);
//        if(spManager.getSpId()==""){
//            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
//            startActivity(intent);
//            finish();
//        }
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},123 );
        getLatLng();
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spManager.saveSPString(SharePreferenceManager.SP_Username, "");
                spManager.saveSPString(SharePreferenceManager.SP_ID, "");
                spManager.saveSPBoolean(SharePreferenceManager.SP_SUDAH_LOGIN, false);
                spManager.saveSPDouble(SharePreferenceManager.SP_Lat, 0);
                spManager.saveSPDouble(SharePreferenceManager.SP_Lng, 0);
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }

    protected void getLatLng() {
        GPSTracker gpsTracker = new GPSTracker(getApplicationContext());
        Log.d(TAG, gpsTracker.toString());
        Location location = gpsTracker.getLocation();
        Log.d(TAG+"tag", location.toString());
        Log.d(TAG+"tag", String.valueOf(location.getLatitude()));
        Log.d(TAG+"tag", String.valueOf(location.getLongitude()));
        if (location != null) {
            lat = location.getLatitude();
            lng = location.getLongitude();
            Log.d("tag",location.getProvider());

            spManager.saveSPDouble(SharePreferenceManager.SP_Lat, location.getLatitude());
            spManager.saveSPDouble(SharePreferenceManager.SP_Lng, location.getLongitude());
        }

        Log.d(TAG, location.toString());
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
