package com.example.ptb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.ptb.model.Penilaian;
import com.example.ptb.model.Tambalban;
import com.example.ptb.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;

public class DetailTambalBanActivity extends AppCompatActivity {
    private static final String TAG = "DetailTambalBanActivity";
    private Button btnPenilaian;
    private TextView tvNamaTb, tvAlamat, tvJamBuka;
    private RatingBar ratingBar;
    private RecyclerView rvPenilaian;
    private RecyclerView.Adapter adapter;
    private ArrayList<Penilaian> listPenilaian, listPenilaianRat, listPenilaianKom, listPenilaianUser;
    private float ratingAll;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseReference myRef;
    private FirebaseDatabase mDatabase;
    private SharePreferenceManager spManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_tambal_ban);

        spManager = new SharePreferenceManager(this);
        Toolbar mToolbar = findViewById(R.id.toolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_baseline_keyboard_arrow_left_white);

        mToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnPenilaian = findViewById(R.id.btnPenilaian);
        tvNamaTb = findViewById(R.id.txtNamaTB);
        tvAlamat = findViewById(R.id.txtAlamat);
        tvJamBuka = findViewById(R.id.txtJadwalBuka);
        ratingBar = findViewById(R.id.ratingBar);
        rvPenilaian = findViewById(R.id.rvListPenilaian);
        rvPenilaian.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        rvPenilaian.setLayoutManager(layoutManager);

        String tambalID = spManager.getSPString("tambalID", "");
        mDatabase = FirebaseDatabase.getInstance();

        myRef = mDatabase.getReference().child("ratings");
        myRef.orderByChild("tbID").equalTo(tambalID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
//                Log.d("tambalID", tambalID);
                listPenilaianRat= new ArrayList<>();
                for (DataSnapshot penilaianDataSnapshot : dataSnapshot.getChildren()) {

                    final Penilaian penilaian = penilaianDataSnapshot.getValue(Penilaian.class);
                    penilaian.setKey(penilaianDataSnapshot.getKey());
                    Log.d("key", penilaian.getKey());
                    listPenilaianRat.add(penilaian);
                    float rating = Float.parseFloat(penilaian.getRating());
                    Log.d("rating", String.valueOf(rating));
                    ratingAll += rating;
                }
                ratingAll = ratingAll/listPenilaianRat.size();
                Log.d("ratingAll", String.valueOf(ratingAll));
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        mDatabase.getReference().child("tambalban").child(spManager.getSPString("tambalID", "")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Tambalban tambalban = dataSnapshot.getValue(Tambalban.class);
                tvNamaTb.setText(tambalban.getNama());
                tvAlamat.setText(tambalban.getAlamat());
                tvJamBuka.setText("Pukul " + tambalban.getJambuka() + " - " + tambalban.getJamtutup() + " WIB");
                Log.d("ratingAll", String.valueOf(ratingAll));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mDatabase.getReference().child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listPenilaianUser = new ArrayList<>();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Penilaian penilaian = new Penilaian();
                    String userID = snapshot.getKey();
                    String username = snapshot.child("username").getValue().toString();
                    penilaian.setUserID(userID);
                    penilaian.setUser(username);
                    listPenilaianUser.add(penilaian);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mDatabase.getReference().child("komentars").orderByChild("tbID").equalTo(tambalID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
//                Log.d("tambalID", tambalID);
                listPenilaianKom= new ArrayList<>();
                listPenilaian= new ArrayList<>();
                for (DataSnapshot penilaianDataSnapshot : dataSnapshot.getChildren()) {

                    Penilaian penilaian = penilaianDataSnapshot.getValue(Penilaian.class);
                    penilaian.setKey(penilaianDataSnapshot.getKey());
                    Log.d("key", penilaian.getKey());
                    listPenilaianKom.add(penilaian);
                }
                //listPenilaian.addAll(listPenilaianKom);
                for(int i=0; i<listPenilaianRat.size(); i++){
                    Penilaian rating = listPenilaianRat.get(i);

                    Comparator<Penilaian> c = new Comparator<Penilaian>() {
                        public int compare(Penilaian p1, Penilaian p2) {
                            return p1.getKey().compareTo(p2.getKey());
                        }
                    };

                    Comparator<Penilaian> cu = new Comparator<Penilaian>() {
                        public int compare(Penilaian p1, Penilaian p2) {
                            return p1.getUserID().compareTo(p2.getUserID());
                        }
                    };

                    int indexKomen = Collections.binarySearch(listPenilaianKom, rating, c);
                    Log.d("indexKomen", String.valueOf(indexKomen));
                    Penilaian komen = listPenilaianKom.get(indexKomen);

                    int indexUser = Collections.binarySearch(listPenilaianUser, rating , cu);
                    Log.d("indexUser", String.valueOf(indexKomen));
                    Penilaian user = listPenilaianUser.get(indexUser);

                    Log.d("indexKomen", user.getUser());
                    Log.d("indexKomen", rating.getRating());
                    Log.d("indexKomen", komen.getKomentar());
                    Penilaian penilaian = new Penilaian();
                    penilaian.setUser(user.getUser());
                    penilaian.setRating(rating.getRating());
                    penilaian.setKomentar(komen.getKomentar());

                    listPenilaian.add(penilaian);
                }
                adapter = new PenilaianAdapter(listPenilaian, DetailTambalBanActivity.this);
                rvPenilaian.setAdapter(adapter);
                ratingBar.setRating(ratingAll);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });


        btnPenilaian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PenilaianActivity.class);
                startActivity(intent);
            }
        });
    }
}