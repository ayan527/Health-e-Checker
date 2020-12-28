package com.example.health_e_checkerapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.database.DatabaseReference;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private Animation button_click;

    private ImageView nurseImageView;

    private TextView nurseIdTextView;
    private TextView nurseFullNameTextView;
    private TextView nurseMobileNoTextView;
    private TextView nurseEmailIdTextView;
    private TextView nurseGenderTextView;
    private TextView nurseViewsTextView;

    private Button scannerButton;
    private Button logoutButton;

    //private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i(TAG,"onCreate() method created");

        button_click = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.button_click);

        String nurse_id = getIntent().getStringExtra("nurse_id");
        String nurse_fullName = getIntent().getStringExtra("nurse_fullName");
        String nurse_gender = getIntent().getStringExtra("nurse_gender");
        String nurse_emailId = getIntent().getStringExtra("nurse_emailId");
        String nurse_mobileNo = getIntent().getStringExtra("nurse_mobileNo");
        String nurse_views = getIntent().getStringExtra("nurse_views");

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
        nurseImageView = (ImageView) findViewById(R.id.nurseImageView);
        if (nurse_gender.equals("Female")) {
            nurseImageView.setImageResource(R.drawable.nurse);
        } else if (nurse_gender.equals("Male")) {
            nurseImageView.setImageResource(R.drawable.male_nurse);
        }

        nurseFullNameTextView = (TextView) findViewById(R.id.nurseFullNameTextView);
        nurseFullNameTextView.setText(nurse_fullName);

        nurseIdTextView = (TextView) findViewById(R.id.nurseIdTextView);
        nurseIdTextView.setText(nurse_id);

        nurseGenderTextView = (TextView) findViewById(R.id.nurseGenderTextView);
        nurseGenderTextView.setText(nurse_gender);

        nurseViewsTextView = (TextView) findViewById(R.id.nurseViewsTextView);
        nurseViewsTextView.setText(nurse_views);

        nurseEmailIdTextView = (TextView) findViewById(R.id.nurseEmailIdTextView);
        nurseEmailIdTextView.setText(nurse_emailId);

        nurseMobileNoTextView = (TextView) findViewById(R.id.nurseMobileNoTextView);
        nurseMobileNoTextView.setText(nurse_mobileNo);

        scannerButton = (Button) findViewById(R.id.scannerButton);
        scannerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scannerButton.startAnimation(button_click);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(getApplicationContext(),ScannerActivity.class).putExtra("nurse_id",nurse_id));
                    }
                },100);
            }
        });

        logoutButton = (Button) findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutButton.startAnimation(button_click);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                        finish();
                    }
                },100);
            }
        });
    }
}