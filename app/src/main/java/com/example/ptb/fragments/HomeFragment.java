package com.example.ptb.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.ptb.ListActivity;
import com.example.ptb.R;
import com.example.ptb.SharePreferenceManager;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private SharePreferenceManager spManager;
    private TextView tvUserName, tvLocation;
    private ImageView ivFoto;
    private String fotoProfil = "http://shyntadarmawan.000webhostapp.com/assets/user.png";
    private CardView btn24Jam;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        spManager = new SharePreferenceManager(getContext());
        tvUserName = view.findViewById(R.id.tvUserLoginHome);
        tvLocation = view.findViewById(R.id.tvLocationHome);
        ivFoto = view.findViewById(R.id.ivFotoProfilHome);
        String usernamelogin = spManager.getSPString(SharePreferenceManager.SP_Username,"");
        String username = usernamelogin!="" ?usernamelogin:"---not yet login---";
        tvUserName.setText(username);
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.logo_tambal_ban).error(R.drawable.logoputih);
        Glide.with(getContext()).load(fotoProfil).apply(requestOptions).into(ivFoto);
        tvLocation.setText(spManager.getSPString(SharePreferenceManager.SP_Address,""));
//        tvLocation.setText("lat "+spManager.getSPDouble(SharePreferenceManager.SP_Lat,0)+", lng "+spManager.getSPDouble(SharePreferenceManager.SP_Lng,0));
        btn24Jam = view.findViewById(R.id.btn24Jam);
        btn24Jam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ListActivity.class));
                getActivity().finish();
            }
        });

    }
}
