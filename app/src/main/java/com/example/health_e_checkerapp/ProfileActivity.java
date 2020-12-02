package com.example.health_e_checkerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity {
    private static final String TAG = "ProfileActivity";

    private TextView profileTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Log.i(TAG,"onCreate() method created");

        Intent getIntent = getIntent();
        String patient_id = getIntent.getStringExtra("patient_id");

        profileTextView = (TextView) findViewById(R.id.profileTextView);
        profileTextView.setText("Patient ID is : " +patient_id);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Log.i(TAG,"going to ScannerActivity");

        startActivity(new Intent(ProfileActivity.this,ScannerActivity.class));
        finish();
    }
}