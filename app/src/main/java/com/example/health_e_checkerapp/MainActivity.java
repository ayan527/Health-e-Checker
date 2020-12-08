package com.example.health_e_checkerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private TextView welcomeTextView;

    private Button scannerButton;
    private Button logoutButton;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i(TAG,"onCreate() method created");

        welcomeTextView = (TextView) findViewById(R.id.welcomeTextView);

        String nurse_id = getIntent().getStringExtra("nurse_id");
        String nurse_fullName = getIntent().getStringExtra("nurse_fullName");

        welcomeTextView.setText("Welcome, " + nurse_id + " : " +nurse_fullName);
        /*databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("nurseDetails").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    NurseDetails nurse = snapshot.getValue(NurseDetails.class);
                    if (nurse.getEmailId().equals(nurse_emailId)) {
                        welcomeTextView.setText("Welcome, " + nurse.getId() + " : " +nurse.getFirstName()+ " " +nurse.getLastName());
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/

        scannerButton = (Button) findViewById(R.id.scannerButton);
        scannerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),ScannerActivity.class));
            }
        });

        logoutButton = (Button) findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(),StartActivity.class));
                finish();
            }
        });
    }
}