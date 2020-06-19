package com.example.ptb.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.ptb.LoginActivity;
import com.example.ptb.R;
import com.example.ptb.SharePreferenceManager;
import com.example.ptb.model.Komentar;
import com.example.ptb.model.Rating;
import com.example.ptb.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private SharePreferenceManager spManager;
    private DatabaseReference myRef;
    private FirebaseDatabase mDatabase;
    private final static String TAG = "ProfileFragment";
    private ArrayList<User> listUser;
    private ArrayList<Komentar> listKomentar;
    private ArrayList<Rating> listRating;
    private TextView tvUserName, tvEmail, tvFullName, tvComentar, tvRating;
    private ImageView ivFoto;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        spManager = new SharePreferenceManager(getContext());
        tvUserName = view.findViewById(R.id.tvUserName);
        ivFoto = view.findViewById(R.id.ivFotoProfilB);
        tvFullName = view.findViewById(R.id.tvFullName);
        tvEmail = view.findViewById(R.id.tvEmailProfile);
        tvComentar = view.findViewById(R.id.tvComentarCount);
        tvRating = view.findViewById(R.id.tvCountRating);

        init();
    }

    private void init() {
        spManager = new SharePreferenceManager(getContext());
        if (spManager.getSpId() == "") {
            Toast.makeText(getContext(), "Please login first, to access this profile", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getContext(), LoginActivity.class);
            startActivity(intent);
        } else {
            mDatabase = FirebaseDatabase.getInstance();
            myRef = mDatabase.getReference();

            myRef.child("users").orderByChild("email").equalTo(spManager.getSPString(SharePreferenceManager.SP_Username, "")).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.d(TAG, "kesini");
                    listUser = new ArrayList<>();
                    if (dataSnapshot.getValue() != null) {
                        Log.d(TAG + "aaaa", dataSnapshot.getValue().toString());
                        for (DataSnapshot tbdataSnapshot : dataSnapshot.getChildren()) {
                            User user = tbdataSnapshot.getValue(User.class);
                            user.setKey(tbdataSnapshot.getKey());
                            listUser.add(user);
                        }
                        Log.d(TAG, listUser.size() + "");
                        if(listUser.size()>0){
                            showData();
                        }
                    } else {
                        Toast.makeText(getContext(), "Data not found", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.w(TAG, "Failed to read value.", databaseError.toException());
                }
            });

        }
    }

    private void showData() {

        Log.d(TAG, listUser.size() + "");
        tvUserName.setText(listUser.get(0).getUsername());
        tvFullName.setText(listUser.get(0).getFullname());
        tvEmail.setText(listUser.get(0).getEmail());
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.logo_tambal_ban).error(R.drawable.logoputih);
        Glide.with(getContext()).load(listUser.get(0).getFoto()).apply(requestOptions).into(ivFoto);
        getComentarCount(listUser.get(0).getKey());
        getRatingCount(listUser.get(0).getKey());
    }

    private int getComentarCount(String id) {
        myRef.child("komentars").orderByChild("userID").equalTo(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "kesini");
                listKomentar = new ArrayList<>();
                if (dataSnapshot.getValue() != null) {
                    Log.d(TAG + "aaaa", dataSnapshot.getValue().toString());
                    for (DataSnapshot tbdataSnapshot : dataSnapshot.getChildren()) {
                        Komentar komentar = tbdataSnapshot.getValue(Komentar.class);
                        komentar.setKey(tbdataSnapshot.getKey());
                        listKomentar.add(komentar);
                    }
                    tvComentar.setText(dataSnapshot.getChildrenCount()+"");
                    Log.d(TAG, listKomentar.size() + "jml");
                } else {
                    Toast.makeText(getContext(), "Data not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "Failed to read value.", databaseError.toException());
            }
        });
        return 0;
    }

    private void getRatingCount(String id) {
        myRef.child("ratings").orderByChild("userID").equalTo(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "kesini");
                listRating = new ArrayList<>();
                if (dataSnapshot.getValue() != null) {
                    Log.d(TAG + "aaaa", dataSnapshot.getValue().toString());
                    for (DataSnapshot tbdataSnapshot : dataSnapshot.getChildren()) {
                        Rating rating = tbdataSnapshot.getValue(Rating.class);
                        rating.setKey(tbdataSnapshot.getKey());
                        listRating.add(rating);
                    }
                    tvRating.setText(dataSnapshot.getChildrenCount()+"");
                    Log.d(TAG, listRating.size() + "");
                } else {
                    Toast.makeText(getContext(), "Data not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "Failed to read value.", databaseError.toException());
            }
        });
    }
}
