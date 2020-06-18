package com.example.ptb;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.ptb.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment {

    private FirebaseAuth auth;
    private EditText inputEmail, inputPassword, inputName, inputRePassword;
    private Button btnRegister;
    private final static String TAG = "RegisterFragment";
    private DatabaseReference myRef;
    private FirebaseDatabase mDatabase;
    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        auth = FirebaseAuth.getInstance();
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        auth = FirebaseAuth.getInstance();
        inputEmail = view.findViewById(R.id.et_email);
        inputPassword = view.findViewById(R.id.et_password);
        inputRePassword = view.findViewById(R.id.et_repassword);
        inputName = view.findViewById(R.id.et_name);
        btnRegister = view.findViewById(R.id.btn_register);

        mDatabase = FirebaseDatabase.getInstance();
        myRef = mDatabase.getReference().child("users");

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = inputEmail.getText().toString();
                final String password = inputPassword.getText().toString();
                final String name = inputName.getText().toString();
                String repassword = inputRePassword.getText().toString();

                if (TextUtils.isEmpty(name)) {
//                    Toast.makeText(getContext(), "Enter name!", Toast.LENGTH_SHORT).show();
                    inputName.setError("Enter Name!");
                    return;
                }
                if (TextUtils.isEmpty(email)) {
//                    Toast.makeText(getContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    inputEmail.setError("Enter email address!");
                    return;
                }

                if (email.length()<4) {
//                    Toast.makeText(getContext(), "Enter email address, min length 4!", Toast.LENGTH_SHORT).show();
                    inputEmail.setError("Enter email address, min length 4!");
                    return;
                }

                if (TextUtils.isEmpty(password)) {
//                    Toast.makeText(getContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    inputPassword.setError("Enter password!");
                    return;
                }

                if (TextUtils.isEmpty(repassword)) {
//                    Toast.makeText(getContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    inputRePassword.setError("Enter Re-password!");
                    return;
                }

                if (password.length()<6) {
//                    Toast.makeText(getContext(), "Enter password, min length 6!", Toast.LENGTH_SHORT).show();
                    inputPassword.setError("Enter password, min length 6!");
                    return;
                }

                if (!password.equals(repassword)) {
//                    Toast.makeText(getContext(), "Enter re-password!", Toast.LENGTH_SHORT).show();
                    inputPassword.setError("Password not match with re password!");
                    return;
                }

                if (!repassword.equals(password)) {
//                    Toast.makeText(getContext(), "Re-password not match with password!", Toast.LENGTH_SHORT).show();
                    inputRePassword.setError("Re-password not match with password!");
                    return;
                }

                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener((Activity) getContext(), new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "createUserWithEmail:success");
                                    FirebaseUser user = auth.getCurrentUser();
                                    User userModel = new User();
                                    userModel.setEmail(email);
                                    userModel.setFullname(name);
                                    userModel.setCreatedtime(System.currentTimeMillis());
                                    userModel.setFoto("http://shyntadarmawan.000webhostapp.com/assets/user.png");
                                    userModel.setPassword(password);
                                    userModel.setUsername(name);

                                    String key = myRef.push().getKey();
                                    myRef.child(key).setValue(userModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(getContext(), "New account successfully created.",
                                                        Toast.LENGTH_LONG).show();
                                                startActivity(new Intent(getContext(), MainActivity.class));
                                            }else{
                                                Log.w(TAG, "createUserWithEmail:failure,indb", task.getException());
                                                Toast.makeText(getContext(), "Failed create account.",
                                                        Toast.LENGTH_LONG).show();

                                            }
                                        }
                                    });
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(getContext(), "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }

                                // ...
                            }
                        });
            }
        });

    }

    public void toLogin(){
        LoginFragment loginFragment = new LoginFragment();
        FragmentTransaction transaction = getFragmentManager()
                .beginTransaction().replace(R.id.fl_container, loginFragment);
        transaction.commit();
    }
}
