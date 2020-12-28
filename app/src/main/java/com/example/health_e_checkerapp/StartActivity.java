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

public class StartActivity extends AppCompatActivity{
    private static final String TAG = "StartActivity";

    private ProgressBar startProgressBar;

    private FirebaseAuth firebaseAuth;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        Log.i(TAG,"onCreate() method called");

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
                            startProgressBar.setVisibility(View.GONE);
                            startActivity(new Intent(getApplicationContext(),MainActivity.class)
                                    .putExtra("nurse_id",nurse.getId())
                                    .putExtra("nurse_fullName",nurse.getFullName())
                                    .putExtra("nurse_emailId",nurse.getEmailId())
                                    .putExtra("nurse_gender",nurse.getGender())
                                    .putExtra("nurse_views",Integer.toString(nurse.getViews()))
                                    .putExtra("nurse_mobileNo",nurse.getMobileNo()));
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
            startProgressBar.setVisibility(View.GONE);
            startActivity(new Intent(getApplicationContext(),LoginActivity.class));
            finish();
        }
    }
}