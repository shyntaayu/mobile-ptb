package com.example.ptb.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.ptb.ListAdapter;
import com.example.ptb.R;
import com.example.ptb.SharePreferenceManager;
import com.example.ptb.model.Rating;
import com.example.ptb.model.Tambalban;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final String TAG = "ListFragment";
    private DatabaseReference myRef;
    private FirebaseDatabase mDatabase;
    private RecyclerView rvList;
    private RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    private ArrayList<Tambalban> listTb;
    private SearchView searchView;
    private TextView tvSearchresult;
    private String valueDB;
    private String refinedData;
    private ProgressBar progressBar;
    private SharePreferenceManager spManager;
    private TextView tvUserName, tvLocation;
    private ImageView ivFoto;
    private String fotoProfil = "http://shyntadarmawan.000webhostapp.com/assets/user.png";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListFragment newInstance(String param1, String param2) {
        ListFragment fragment = new ListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getData(view);
        setHeader(view);
    }

    public void setHeader(View view) {
        spManager = new SharePreferenceManager(getContext());
        tvUserName = view.findViewById(R.id.tvUserLoginList);
        ivFoto = view.findViewById(R.id.ivFotoProfilList);
        tvLocation = view.findViewById(R.id.tvLocationList);
        String usernamelogin = spManager.getSPString(SharePreferenceManager.SP_Username, "");
        String username = usernamelogin != "" ? usernamelogin : "---not yet login---";
        tvUserName.setText(username);
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.logo_tambal_ban).error(R.drawable.logoputih);
        Glide.with(getContext()).load(fotoProfil).apply(requestOptions).into(ivFoto);
        tvLocation.setText(spManager.getSPString(SharePreferenceManager.SP_Address, ""));
//        tvLocation.setText("lat " + spManager.getSPDouble(SharePreferenceManager.SP_Lat, 0) + ", lng " + spManager.getSPDouble(SharePreferenceManager.SP_Lng, 0));

    }

    public void getData(final View view) {
        searchView = view.findViewById(R.id.svTbList);
        tvSearchresult = view.findViewById(R.id.tvSearchResultList);
        rvList = view.findViewById(R.id.rvListTbList);
        progressBar = view.findViewById(R.id.progressBar);
        rvList.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        rvList.setLayoutManager(layoutManager);
        // [START write_message]
        // Write a message to the database
        mDatabase = FirebaseDatabase.getInstance();
        myRef = mDatabase.getReference();

        // [END write_message]

        // [START read_message]
        // Read from the database
        progressBar.setVisibility(View.VISIBLE);
        listTb = new ArrayList<>();
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
                                    listTb.add(tambalban);

                                } else {
                                    Log.d(TAG, "Data not found");
                                }

                                adapter = new ListAdapter(listTb, getContext());
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
                    Toast.makeText(getContext(), "Data not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                int SearchIndex = refinedData.indexOf(query);
                if (SearchIndex != -1) {
                    String SearchResult = refinedData.substring(SearchIndex);
                    String SearchSplit[] = SearchResult.split(",");
                    tvSearchresult.setText("Result : " + SearchSplit[0]);
                    getSearch(true, SearchSplit[0]);
                } else {
                    tvSearchresult.setText("Result : Data not found");
                    Toast.makeText(getContext(), "Data not found", Toast.LENGTH_SHORT).show();
                    getSearch(false, null);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                int SearchIndex = refinedData.indexOf(newText);
                if (SearchIndex != -1) {
                    String SearchResult = refinedData.substring(SearchIndex);
                    String SearchSplit[] = SearchResult.split(",");
                    Log.d(TAG, SearchSplit.toString());
                    tvSearchresult.setText("Result : " + SearchSplit[0]);
                    getSearch(true, SearchSplit[0]);
                } else {
                    tvSearchresult.setText("Result : Data not found");
                    Toast.makeText(getContext(), "Data not found", Toast.LENGTH_SHORT).show();
                    getSearch(false, null);
                }
                return false;
            }
        });
        // [END read_message]
    }

    public void getSearch(boolean mustSearch, String a) {
        if (mustSearch) {
            myRef.orderByChild("nama").startAt(a).endAt("~").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() != null) {
                        Log.d(TAG + "aaaa", dataSnapshot.getValue().toString());
                        listTb = new ArrayList<>();
                        for (DataSnapshot tbdataSnapshot : dataSnapshot.getChildren()) {
                            Tambalban tambalban = tbdataSnapshot.getValue(Tambalban.class);
                            tambalban.setKey(tbdataSnapshot.getKey());

                            listTb.add(tambalban);
                        }
                        Log.d(TAG, listTb.size() + "");
                        adapter = new ListAdapter(listTb, getContext());
                        rvList.setAdapter(adapter);
                    } else {
                        Toast.makeText(getContext(), "Data not found", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.w(TAG, "Failed to read value.", databaseError.toException());
                }
            });
        } else {
            listTb = new ArrayList<>();
            adapter = new ListAdapter(listTb, getContext());
            rvList.setAdapter(adapter);
        }

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
