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

import com.example.ptb.model.Penilaian;
import com.example.ptb.model.Tambalban;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DetailTambalBanActivity extends AppCompatActivity {
    private static final String TAG = "DetailTambalBanActivity";
    private Button btnPenilaian;
    private RecyclerView rvPenilaian;
    private RecyclerView.Adapter adapter;
    private ArrayList<Penilaian> listPenilaian;
    private ArrayList<Penilaian> listPenilaianKom;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseReference myRef;
    private FirebaseDatabase mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_tambal_ban);

        Toolbar mToolbar = findViewById(R.id.toolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_baseline_keyboard_arrow_left_white);

        mToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnPenilaian = findViewById(R.id.btnPenilaian);
        rvPenilaian = findViewById(R.id.rvListPenilaian);
        rvPenilaian.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        rvPenilaian.setLayoutManager(layoutManager);

        mDatabase = FirebaseDatabase.getInstance();
        myRef = mDatabase.getReference().child("ratings");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                listPenilaianKom = new ArrayList<>();
                for(DataSnapshot penilaianDataSnapshot : dataSnapshot.getChildren()){
                    final Penilaian penilaian = penilaianDataSnapshot.getValue(Penilaian.class);
                    mDatabase.getReference().child("komentars").child(penilaianDataSnapshot.getKey()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Penilaian komen = dataSnapshot.getValue(Penilaian.class);
                            if(komen != null){
                                penilaian.setKomen(komen.getKomen());
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    Log.d("user", penilaian.getUserID());
                    mDatabase.getReference().child("users").child(penilaian.getUserID()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Penilaian user = dataSnapshot.getValue(Penilaian.class);
                            if(user != null){
                                Log.d("user", user.getUser() == null ? "kosong" : user.getUser());
                                penilaian.setUser(user.getUser());
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    listPenilaianKom.add(penilaian);
                }

                adapter = new PenilaianAdapter(listPenilaianKom,DetailTambalBanActivity.this);
                rvPenilaian.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

//        myRef = mDatabase.getReference().child("komentars");
//        myRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                // This method is called once with the initial value and again
//                // whenever data at this location is updated.
//                listPenilaianKom = new ArrayList<>();
//                for(DataSnapshot penilaianDataSnapshot : dataSnapshot.getChildren()){
//                    Penilaian penilaian = penilaianDataSnapshot.getValue(Penilaian.class);
//                    //penilaianDataSnapshot.setKey(tbdataSnapshot.getKey());
//
//                    listPenilaianKom.add(penilaian);
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//                // Failed to read value
//                Log.w(TAG, "Failed to read value.", error.toException());
//            }
//        });

//        for(Penilaian penilaian : listPenilaianKom){
//            if(penilaian.getKomen() == null){
//                Penilaian a = new Penilaian();
//                a.setTblID(penilaian.getTblID());
//                listPenilaianKom.indexOf(a);
//                Log.d("list", listPenilaianKom.toString());
//            }
//        }


        btnPenilaian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PenilaianActivity.class);
                startActivity(intent);
            }
        });
    }
}