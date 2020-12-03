package com.example.health_e_checkerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

public class LaunchActivity extends AppCompatActivity {
    private static final String TAG = "LaunchActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        Log.i(TAG,"onCreate() method called");
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.i(TAG,"onResume() method called");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG,"moving to StartActivity");
                startActivity(new Intent(LaunchActivity.this,MainActivity.class));
                finish();
            }
        },4000);
    }
}