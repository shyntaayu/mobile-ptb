package com.example.ptb;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {
    private EditText inputEmail, inputPassword;
    private FirebaseAuth auth;
    private FirebaseDatabase mDatabase;
    private Button btnLogin;
    private SharePreferenceManager spManager;
    private ProgressBar progressBar;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        auth = FirebaseAuth.getInstance();
        spManager = new SharePreferenceManager(getContext());
        mDatabase = FirebaseDatabase.getInstance();

        inputEmail = view.findViewById(R.id.et_email);
        inputPassword = view.findViewById(R.id.et_password);
        btnLogin = view.findViewById(R.id.btn_login);
        progressBar = view.findViewById(R.id.progressBar);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = inputEmail.getText().toString();
                final String password = inputPassword.getText().toString();

                if (TextUtils.isEmpty(email)) {
//                    Toast.makeText(getContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    inputEmail.setError("Enter email address!");
                    return;
                }

                if (TextUtils.isEmpty(password)) {
//                    Toast.makeText(getContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    inputPassword.setError("Enter password!");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                //authenticate user
                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener((Activity) getContext(), new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                if (!task.isSuccessful()) {
                                    // there was an error
                                    if (password.length() < 6) {
                                        inputPassword.setError(getString(R.string.minimum_password));
                                    } else {
                                        Toast.makeText(getContext(), getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    spManager.saveSPString(SharePreferenceManager.SP_ID, task.getResult().getUser().getUid());
                                    spManager.saveSPString(SharePreferenceManager.SP_Username, task.getResult().getUser().getEmail());
                                    spManager.saveSPBoolean(SharePreferenceManager.SP_SUDAH_LOGIN, true);

                                    mDatabase.getReference().child("users").orderByChild("email").equalTo(spManager.getSP_Username()).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                                                Log.d("userID", snapshot.getKey());
                                                spManager.saveSPString(SharePreferenceManager.SP_ID, snapshot.getKey());
                                                spManager.saveSPString("username", snapshot.child("username").getValue().toString());
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });

                                    startActivity(new Intent(getContext(), MainActivity.class)
                                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                                }
                            }
                        });
            }
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }
}
