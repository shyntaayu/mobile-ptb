package com.example.ptb;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.example.ptb.model.Penilaian;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Random;

public class PenilaianActivity extends AppCompatActivity {

    private RatingBar rbPenilaian;
    private EditText etKomen;
    private Button btnNilai;
    private SharePreferenceManager spManager;
    private Penilaian model;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_penilaian);
        spManager = new SharePreferenceManager(this);
        model = new Penilaian();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        Toolbar mToolbar = findViewById(R.id.toolbar1);
        mToolbar.setNavigationIcon(R.drawable.ic_baseline_keyboard_arrow_left_blue);

        mToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        rbPenilaian = findViewById(R.id.rbPenilaian);
        btnNilai = findViewById(R.id.btnNilai);
        etKomen = findViewById(R.id.etKomen);

        btnNilai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
            }
        });
    }
    private void saveData(){
        spManager.saveSPString("tambalID", "-MA5g0d6Hhb7F3dmm0Am");
        String tambalID = spManager.getSPString("tambalID", "");
        String DATA = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijkklmnopqrstuvwxyz";
        Random RANDOM = new Random();
        StringBuilder sb = new StringBuilder(20);

        for (int i = 0; i < 20; i++) {
            sb.append(DATA.charAt(RANDOM.nextInt(DATA.length())));
        }
        String key = sb.toString();
        model = new Penilaian();
        //model.setKey(key);
        model.setUserID("-MA5LbhWZ0Dc66nB40N8");
        model.setTblID(tambalID);
        //model.setCreatedtime(Long.valueOf(Calendar.getInstance().getTime().toString()));
        model.setRating(String.valueOf(rbPenilaian.getRating()));
        mDatabase.child("ratings").child(key)
                .setValue(model).addOnSuccessListener(this, new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                rbPenilaian.setIsIndicator(true);
            }
        });
        model.setRating(null);
        model.setKomen(etKomen.getText().toString());
        mDatabase.child("komentars").child(key)
                .setValue(model).addOnSuccessListener(this, new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                etKomen.setText("");
                Toast.makeText(PenilaianActivity.this, "Penilaian Tersimpan", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}