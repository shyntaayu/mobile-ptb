package com.example.ptb;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    boolean doubleBackToExitPressedOnce = false;
    private SharePreferenceManager spManager;
    private Button btnLogout;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLogout = findViewById(R.id.btn_logout);
        spManager = new SharePreferenceManager(this);
//        if(spManager.getSpId()==""){
//            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
//            startActivity(intent);
//            finish();
//        }

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spManager.saveSPString(SharePreferenceManager.SP_Username, "");
                spManager.saveSPString(SharePreferenceManager.SP_ID, "");
                spManager.saveSPBoolean(SharePreferenceManager.SP_SUDAH_LOGIN, false);
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
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

}
