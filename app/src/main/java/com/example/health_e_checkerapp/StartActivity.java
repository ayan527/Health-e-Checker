package com.example.health_e_checkerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class StartActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "StartActivity";

    private ProgressBar startProgressBar;

    private Button startLoginButton;
    private Button startRegisterButton;

    private FirebaseAuth firebaseAuth;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        Log.i(TAG,"onCreate() method called");

        startLoginButton = (Button) findViewById(R.id.startLoginButton);
        startRegisterButton = (Button) findViewById(R.id.startRegisterButton);

        startLoginButton.setOnClickListener(this);
        startRegisterButton.setOnClickListener(this);

        startProgressBar = (ProgressBar) findViewById(R.id.startProgressBar);
        startProgressBar.setVisibility(View.VISIBLE);

        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() != null) {
            databaseReference = FirebaseDatabase.getInstance().getReference();
            String emailId = firebaseAuth.getCurrentUser().getEmail();
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
        } else {
            startProgressBar.setVisibility(View.INVISIBLE);
            startLoginButton.setVisibility(View.VISIBLE);
            startRegisterButton.setVisibility(View.VISIBLE);
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.startLoginButton:
                startNewActivity(LoginActivity.class);
                break;
            case R.id.startRegisterButton:
                startNewActivity(RegisterActivity.class);
                break;
        }
    }

    private void startNewActivity(Class<?> activityClass) {
        startActivity(new Intent(StartActivity.this,activityClass));
        finish();
    }

}