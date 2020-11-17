package com.example.health_e_checkerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class StartActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "StartActivity";

    private Button startLoginButton;
    private Button startRegisterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        Log.i(TAG,"onCreate() method called");

        startLoginButton = (Button) findViewById(R.id.startLoginButton);
        startRegisterButton = (Button) findViewById(R.id.startRegisterButton);

        startLoginButton.setOnClickListener(this);
        startRegisterButton.setOnClickListener(this);
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
    }

}