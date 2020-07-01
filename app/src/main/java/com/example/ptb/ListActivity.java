package com.example.ptb;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.ptb.model.Rating;
import com.example.ptb.model.Tambalban;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {

    private static final String TAG = "ListActivity";
    private DatabaseReference myRef;
    private FirebaseDatabase mDatabase;
    private RecyclerView rvList;
    private RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    private ArrayList<Tambalban> listTb;
    private SearchView searchView;
    private TextView tvSearchresult, tvUserName, tvLocation, tvTitleList;
    private ImageView ivFoto;
    private String valueDB;
    private String refinedData;
    private ProgressBar progressBar;
    private SharePreferenceManager spManager;
    private String fotoProfil = "http://shyntadarmawan.000webhostapp.com/assets/user.png";
    private ConstraintLayout constraintLayout;
    private boolean is24jam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        basicReadWrite();
    }

    public void getSearchFullTime(boolean mustSearch, final String searchValue) {
        if (mustSearch && searchValue!=null) {
            listTb = new ArrayList<>();
            myRef.child("tambalban").orderByChild("fulltime").equalTo(true).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() != null) {
                        valueDB = dataSnapshot.getValue().toString();
                        refinedData = valueDB.substring(1, valueDB.length() - 1);
                        Log.d(TAG + "refined", refinedData);
                        for (DataSnapshot tbdataSnapshot : dataSnapshot.getChildren()) {
                            final Tambalban tambalban = tbdataSnapshot.getValue(Tambalban.class);
                            tambalban.setKey(tbdataSnapshot.getKey());
                            myRef.child("ratings").orderByChild("tbID").equalTo(tbdataSnapshot.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    Log.d(TAG, "kesini");
                                    double a = 0;
                                    if (dataSnapshot.getValue() != null) {
                                        Log.d(TAG + "kerating", dataSnapshot.getValue().toString());
                                        for (DataSnapshot tbdataSnapshot : dataSnapshot.getChildren()) {
                                            Rating rating = tbdataSnapshot.getValue(Rating.class);
                                            a += Double.parseDouble(rating.getRating());
                                        }

                                        tambalban.setRating(a / dataSnapshot.getChildrenCount());
                                        Log.d(TAG + "per", a / dataSnapshot.getChildrenCount() + "");
                                        Log.d("listtb", listTb.indexOf(tambalban) + "");
                                        int index = listTb.indexOf(tambalban);
                                        if (index != -1) {
                                            listTb.remove(tambalban);
                                        }
                                        String latS = tambalban.getLatitude().replace(",", "");
                                        String lngS = tambalban.getLongitude().replace(",", "");
                                        Log.d("list", latS);
                                        Log.d("list", lngS);
                                        double lat = Double.parseDouble(latS);
                                        double lng = Double.parseDouble(lngS);
                                        double jarak = getHarvesine(lat, lng);
                                        String jarakS = String.format("%.2f", jarak);
                                        tambalban.setJarak(jarakS);
                                        if (tambalban.getNama().contains(searchValue)) {
                                            listTb.add(tambalban);
                                        }
                                    } else {
                                        Log.d(TAG, "Data not found");
                                    }

                                    adapter = new ListAdapter(listTb, ListActivity.this);
                                    rvList.setAdapter(adapter);
                                    progressBar.setVisibility(View.GONE);
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    Log.w(TAG, "Failed to read value.", databaseError.toException());
                                }
                            });
                            String latS = tambalban.getLatitude().replace(",", "");
                            String lngS = tambalban.getLongitude().replace(",", "");
                            Log.d("list", latS);
                            Log.d("list", lngS);
                            double lat = Double.parseDouble(latS);
                            double lng = Double.parseDouble(lngS);
                            double jarak = getHarvesine(lat, lng);
                            String jarakS = String.format("%.2f", jarak);
                            tambalban.setJarak(jarakS);
                            if (tambalban.getNama().contains(searchValue)) {
                                listTb.add(tambalban);
                            }
                        }
                    } else {
                        Toast.makeText(ListActivity.this, "Data not found", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w(TAG, "Failed to read value.", error.toException());
                }
            });
        } else {
            adapter = new ListAdapter(listTb, ListActivity.this);
            rvList.setAdapter(adapter);
        }

    }

    public void getSearchTerdekat(boolean mustSearch, final String searchValue) {
        if (mustSearch&& searchValue!=null) {
            listTb = new ArrayList<>();
            myRef.child("tambalban").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() != null) {
                        valueDB = dataSnapshot.getValue().toString();
                        refinedData = valueDB.substring(1, valueDB.length() - 1);
                        Log.d(TAG + "refined", refinedData);
                        for (DataSnapshot tbdataSnapshot : dataSnapshot.getChildren()) {
                            final Tambalban tambalban = tbdataSnapshot.getValue(Tambalban.class);
                            tambalban.setKey(tbdataSnapshot.getKey());
                            myRef.child("ratings").orderByChild("tbID").equalTo(tbdataSnapshot.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    Log.d(TAG, "kesini");
                                    double a = 0;
                                    if (dataSnapshot.getValue() != null) {
                                        Log.d(TAG + "kerating", dataSnapshot.getValue().toString());
                                        for (DataSnapshot tbdataSnapshot : dataSnapshot.getChildren()) {
                                            Rating rating = tbdataSnapshot.getValue(Rating.class);
                                            a += Double.parseDouble(rating.getRating());
                                        }

                                        tambalban.setRating(a / dataSnapshot.getChildrenCount());
                                        Log.d(TAG + "per", a / dataSnapshot.getChildrenCount() + "");
                                        Log.d("listtb", listTb.indexOf(tambalban) + "");
                                        int index = listTb.indexOf(tambalban);
                                        if (index != -1) {
                                            listTb.remove(tambalban);
                                        }
                                        String latS = tambalban.getLatitude().replace(",", "");
                                        String lngS = tambalban.getLongitude().replace(",", "");
                                        Log.d("list", latS);
                                        Log.d("list", lngS);
                                        double lat = Double.parseDouble(latS);
                                        double lng = Double.parseDouble(lngS);
                                        double jarak = getHarvesine(lat, lng);
                                        String jarakS = String.format("%.2f", jarak);
                                        tambalban.setJarak(jarakS);
                                        if (tambalban.getNama().contains(searchValue) && jarak < 2) {
                                            listTb.add(tambalban);
                                        }
                                    } else {
                                        Log.d(TAG, "Data not found");
                                    }

                                    adapter = new ListAdapter(listTb, ListActivity.this);
                                    rvList.setAdapter(adapter);
                                    progressBar.setVisibility(View.GONE);
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    Log.w(TAG, "Failed to read value.", databaseError.toException());
                                }
                            });
                            String latS = tambalban.getLatitude().replace(",", "");
                            String lngS = tambalban.getLongitude().replace(",", "");
                            Log.d("list", latS);
                            Log.d("list", lngS);
                            double lat = Double.parseDouble(latS);
                            double lng = Double.parseDouble(lngS);
                            double jarak = getHarvesine(lat, lng);
                            String jarakS = String.format("%.2f", jarak);
                            tambalban.setJarak(jarakS);
                            if (tambalban.getNama().contains(searchValue) && jarak < 2) {
                                listTb.add(tambalban);
                            }
                        }
                    } else {
                        Toast.makeText(ListActivity.this, "Data not found", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w(TAG, "Failed to read value.", error.toException());
                }
            });
        } else {
            adapter = new ListAdapter(listTb, ListActivity.this);
            rvList.setAdapter(adapter);
        }

    }

    public static Intent getActIntent(Activity activity) {
        return new Intent(activity, ListActivity.class);
    }

    public void basicReadWrite() {
        Bundle b = getIntent().getExtras();
        if (b != null)
            is24jam = b.getInt("key") == 1 ? true : false;
        constraintLayout = findViewById(R.id.constraintBack);
        searchView = findViewById(R.id.svTb);
        tvSearchresult = findViewById(R.id.tvSearchResult);
        rvList = findViewById(R.id.rvListTb);
        tvLocation = findViewById(R.id.tvJarak);
        tvUserName = findViewById(R.id.tvUserName);
        tvTitleList = findViewById(R.id.tvTitleList);
        ivFoto = findViewById(R.id.ivFotoProfilList);
        rvList.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        rvList.setLayoutManager(layoutManager);
        progressBar = findViewById(R.id.progressBar);
        // [START write_message]
        // Write a message to the database
        mDatabase = FirebaseDatabase.getInstance();
        myRef = mDatabase.getReference();
        spManager = new SharePreferenceManager(this);
        String usernamelogin = spManager.getSPString(SharePreferenceManager.SP_Username, "");
        String username = usernamelogin != "" ? usernamelogin : "---not yet login---";
        tvUserName.setText(username);
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.logo_tambal_ban).error(R.drawable.logoputih);
        Glide.with(this).load(fotoProfil).apply(requestOptions).into(ivFoto);
        tvTitleList.setText(is24jam ? " 24 Jam" : " Terdekat");
        tvLocation.setText(spManager.getSPString(SharePreferenceManager.SP_Address, ""));
//        tvLocation.setText("lat " + spManager.getSPDouble(SharePreferenceManager.SP_Lat, 0) + ", lng " + spManager.getSPDouble(SharePreferenceManager.SP_Lng, 0));

        constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(ListActivity.this, MainActivity.class));
                onBackPressed();
                finish();
            }
        });
        // [END write_message]

        // [START read_message]
        // Read from the database
        progressBar.setVisibility(View.VISIBLE);
        listTb = new ArrayList<>();
        if (is24jam) {
            myRef.child("tambalban").orderByChild("fulltime").equalTo(true).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() != null) {
                        valueDB = dataSnapshot.getValue().toString();
                        refinedData = valueDB.substring(1, valueDB.length() - 1);
                        Log.d(TAG + "refined", refinedData);
                        for (DataSnapshot tbdataSnapshot : dataSnapshot.getChildren()) {
                            final Tambalban tambalban = tbdataSnapshot.getValue(Tambalban.class);
                            tambalban.setKey(tbdataSnapshot.getKey());
                            myRef.child("ratings").orderByChild("tbID").equalTo(tbdataSnapshot.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    Log.d(TAG, "kesini");
                                    double a = 0;
                                    if (dataSnapshot.getValue() != null) {
                                        Log.d(TAG + "kerating", dataSnapshot.getValue().toString());
                                        for (DataSnapshot tbdataSnapshot : dataSnapshot.getChildren()) {
                                            Rating rating = tbdataSnapshot.getValue(Rating.class);
                                            a += Double.parseDouble(rating.getRating());
                                        }

                                        tambalban.setRating(a / dataSnapshot.getChildrenCount());
                                        Log.d(TAG + "per", a / dataSnapshot.getChildrenCount() + "");
                                        Log.d("listtb", listTb.indexOf(tambalban) + "");
                                        int index = listTb.indexOf(tambalban);
                                        if (index != -1) {
                                            listTb.remove(tambalban);
                                        }
                                        String latS = tambalban.getLatitude().replace(",", "");
                                        String lngS = tambalban.getLongitude().replace(",", "");
                                        Log.d("list", latS);
                                        Log.d("list", lngS);
                                        double lat = Double.parseDouble(latS);
                                        double lng = Double.parseDouble(lngS);
                                        double jarak = getHarvesine(lat, lng);
                                        String jarakS = String.format("%.2f", jarak);
                                        tambalban.setJarak(jarakS);
                                        listTb.add(tambalban);
                                    } else {
                                        Log.d(TAG, "Data not found");
                                    }

                                    adapter = new ListAdapter(listTb, ListActivity.this);
                                    rvList.setAdapter(adapter);
                                    progressBar.setVisibility(View.GONE);
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    Log.w(TAG, "Failed to read value.", databaseError.toException());
                                }
                            });
                            String latS = tambalban.getLatitude().replace(",", "");
                            String lngS = tambalban.getLongitude().replace(",", "");
                            Log.d("list", latS);
                            Log.d("list", lngS);
                            double lat = Double.parseDouble(latS);
                            double lng = Double.parseDouble(lngS);
                            double jarak = getHarvesine(lat, lng);
                            String jarakS = String.format("%.2f", jarak);
                            tambalban.setJarak(jarakS);
                            listTb.add(tambalban);
                        }
                    } else {
                        Toast.makeText(ListActivity.this, "Data not found", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w(TAG, "Failed to read value.", error.toException());
                }
            });
        } else {
            myRef.child("tambalban").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() != null) {
                        valueDB = dataSnapshot.getValue().toString();
                        refinedData = valueDB.substring(1, valueDB.length() - 1);
                        Log.d(TAG, refinedData);
                        for (DataSnapshot tbdataSnapshot : dataSnapshot.getChildren()) {
                            final Tambalban tambalban = tbdataSnapshot.getValue(Tambalban.class);
                            tambalban.setKey(tbdataSnapshot.getKey());
                            myRef.child("ratings").orderByChild("tbID").equalTo(tbdataSnapshot.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    Log.d(TAG, "kesini");
                                    double a = 0;
                                    if (dataSnapshot.getValue() != null) {
                                        Log.d(TAG + "kerating", dataSnapshot.getValue().toString());
                                        for (DataSnapshot tbdataSnapshot : dataSnapshot.getChildren()) {
                                            Rating rating = tbdataSnapshot.getValue(Rating.class);
                                            a += Double.parseDouble(rating.getRating());
                                        }

                                        tambalban.setRating(a / dataSnapshot.getChildrenCount());
                                        Log.d(TAG + "per", a / dataSnapshot.getChildrenCount() + "");
                                        Log.d("listtb", listTb.indexOf(tambalban) + "");
                                        int index = listTb.indexOf(tambalban);
                                        if (index != -1) {
                                            listTb.remove(tambalban);
                                        }
                                        String latS = tambalban.getLatitude().replace(",", "");
                                        String lngS = tambalban.getLongitude().replace(",", "");
                                        Log.d("list", latS);
                                        Log.d("list", lngS);
                                        double lat = Double.parseDouble(latS);
                                        double lng = Double.parseDouble(lngS);
                                        double jarak = getHarvesine(lat, lng);
                                        String jarakS = String.format("%.2f", jarak);
                                        tambalban.setJarak(jarakS);
                                        if (jarak < 2) {
                                            listTb.add(tambalban);
                                        }
                                    } else {
                                        Log.d(TAG, "Data not found");
                                    }

                                    adapter = new ListAdapter(listTb, ListActivity.this);
                                    rvList.setAdapter(adapter);
                                    progressBar.setVisibility(View.GONE);
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    Log.w(TAG, "Failed to read value.", databaseError.toException());
                                }
                            });
                            String latS = tambalban.getLatitude().replace(",", "");
                            String lngS = tambalban.getLongitude().replace(",", "");
                            Log.d("list", latS);
                            Log.d("list", lngS);
                            double lat = Double.parseDouble(latS);
                            double lng = Double.parseDouble(lngS);
                            double jarak = getHarvesine(lat, lng);
                            String jarakS = String.format("%.2f", jarak);
                            tambalban.setJarak(jarakS);
                            if (jarak < 2) {
                                listTb.add(tambalban);
                            }
                        }
                    } else {
                        Toast.makeText(ListActivity.this, "Data not found", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w(TAG, "Failed to read value.", error.toException());
                }
            });
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                int SearchIndex = refinedData.indexOf(query);
                Log.d(TAG + "refined", SearchIndex + "");
                if (SearchIndex != -1) {
                    String SearchResult = refinedData.substring(SearchIndex);
                    Log.d(TAG + "refined", SearchResult + "");
                    String SearchSplit[] = SearchResult.split(",");
                    Log.d(TAG + "refined", SearchSplit + "");
                    tvSearchresult.setText("Result : " + SearchSplit[0]);
                    if (is24jam) {
                        getSearchFullTime(true, SearchSplit[0]);
                    } else {
                        getSearchTerdekat(true, SearchSplit[0]);
                    }
                } else {
                    tvSearchresult.setText("Result : Data not found");
                    Toast.makeText(ListActivity.this, "Data not found", Toast.LENGTH_SHORT).show();
                    if (is24jam) {
                        getSearchFullTime(false, null);
                    } else {
                        getSearchTerdekat(false, null);
                    }
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
//                int SearchIndex = refinedData.indexOf(newText);
//                Log.d(TAG + "refined1", SearchIndex + "");
//                if (SearchIndex != -1) {
//                    String SearchResult = refinedData.substring(SearchIndex);
//                    Log.d(TAG + "refined2", SearchResult + "");
//                    String SearchSplit[] = SearchResult.split(",");
//                    Log.d(TAG + "refined3", SearchSplit + "");
//                    tvSearchresult.setText("Result : " + SearchSplit[0]);
//                    if (is24jam) {
//                        getSearchFullTime(true, SearchSplit[0]);
//                    } else {
//                        getSearchTerdekat(true, SearchSplit[0]);
//                    }
//                } else {
//                    tvSearchresult.setText("Result : Data not found");
//                    Toast.makeText(ListActivity.this, "Data not found", Toast.LENGTH_SHORT).show();
//                    if (is24jam) {
//                        getSearchFullTime(false, null);
//                    } else {
//                        getSearchTerdekat(false, null);
//                    }
//                }
                return false;
            }
        });
        // [END read_message]
    }

    public double getHarvesine(double lat, double lng) {
        final int R = 6371; // Radious of the earth
        double lat1 = spManager.getSPDouble(SharePreferenceManager.SP_Lat, 0);
        double lon1 = spManager.getSPDouble(SharePreferenceManager.SP_Lng, 0);
        double lat2 = lat;
        double lon2 = lng;
        double latDistance = toRad(lat2 - lat1);
        double lonDistance = toRad(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) +
                Math.cos(toRad(lat1)) * Math.cos(toRad(lat2)) *
                        Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c;
        return distance;
    }

    public static double toRad(double value) {
        return value * Math.PI / 180;
    }
}
