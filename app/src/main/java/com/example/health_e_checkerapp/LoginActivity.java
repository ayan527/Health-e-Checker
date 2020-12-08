package com.example.health_e_checkerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "LoginActivity";

    private EditText loginEmailIdEditText;
    private EditText loginPasswordEditText;

    private Button loginButton;

    private ProgressBar loginProgressBar;

    private FirebaseAuth firebaseAuth;

    private Boolean isEmailVerified = false;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Log.i(TAG,"onCreate() method called");

        firebaseAuth = FirebaseAuth.getInstance();

        loginEmailIdEditText = (EditText) findViewById(R.id.loginEmailIdEditText);
        loginPasswordEditText = (EditText) findViewById(R.id.loginPasswordEditText);

        loginProgressBar = (ProgressBar) findViewById(R.id.loginProgressBar);

        loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(this);
    }

    private void verifyEmailAddress(String emailId) {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        assert currentUser != null;

        isEmailVerified = currentUser.isEmailVerified();

        if (! isEmailVerified) {
            Toast.makeText(getApplicationContext(),"Please go to your email & verify your account...",Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
            startActivity(new Intent(getApplicationContext(),LoginActivity.class));
            finish();
        } else {
            Toast.makeText(getApplicationContext(),"Logged in Successfully !",Toast.LENGTH_SHORT).show();
            databaseReference = FirebaseDatabase.getInstance().getReference();
            databaseReference.child("nurseDetails").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        NurseDetails nurse = snapshot.getValue(NurseDetails.class);
                        if (nurse.getEmailId().equals(emailId)) {
                            startActivity(new Intent(getApplicationContext(),MainActivity.class).putExtra("nurse_id",nurse.getId()).putExtra("nurse_fullName",nurse.getFirstName() + " " +nurse.getLastName()));
                            finish();
                            break;
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        String emailId = loginEmailIdEditText.getText().toString().trim();
        String password = loginPasswordEditText.getText().toString().trim();

        if (TextUtils.isEmpty(emailId)) {
            loginEmailIdEditText.setError("Email-Id is empty");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            loginPasswordEditText.setError("Password is empty");
            return;
        }

        if (password.length() < 8) {
            loginPasswordEditText.setError("Minimum 8 characters required");
            return;
        }

        loginProgressBar.setVisibility(View.VISIBLE);

        firebaseAuth.signInWithEmailAndPassword(emailId,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            verifyEmailAddress(emailId);
                        } else {
                            Toast.makeText(LoginActivity.this, "Wrong Email-Id or Password!", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "Error : " + Objects.requireNonNull(task.getException()).getMessage());
                            loginProgressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }
}