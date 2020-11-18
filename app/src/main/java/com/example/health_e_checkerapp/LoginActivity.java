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

import java.util.Objects;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "LoginActivity";

    private EditText loginEmailIdEditText;
    private EditText loginPasswordEditText;

    private Button loginButton;

    private ProgressBar loginProgressBar;

    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Log.i(TAG,"onCreate() method called");

        loginEmailIdEditText = (EditText) findViewById(R.id.loginEmailIdEditText);
        loginPasswordEditText = (EditText) findViewById(R.id.loginPasswordEditText);

        loginProgressBar = (ProgressBar) findViewById(R.id.loginProgressBar);

        loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(this);

        firebaseAuth = FirebaseAuth.getInstance();
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

        if (password.length() < 6) {
            loginPasswordEditText.setError("Minimum 6 characters required");
            return;
        }

        loginProgressBar.setVisibility(View.VISIBLE);

        firebaseAuth.signInWithEmailAndPassword(emailId,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(),"Logged in Successfully !",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "Error !", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "Error : " + Objects.requireNonNull(task.getException()).getMessage());
                            loginProgressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }
}