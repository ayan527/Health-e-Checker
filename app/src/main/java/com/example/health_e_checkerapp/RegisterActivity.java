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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "RegisterActivity";

    private EditText registerEmailIdEditText;
    private EditText registerPasswordEditText;

    private Button registerButton;

    private ProgressBar registerProgressBar;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Log.i(TAG,"onCreate() method called");

        registerEmailIdEditText = (EditText) findViewById(R.id.registerEmailIdEditText);
        registerPasswordEditText = (EditText) findViewById(R.id.registerPasswordEditText);

        registerProgressBar = (ProgressBar) findViewById(R.id.registerProgressBar);

        registerButton = (Button) findViewById(R.id.registerButton);
        registerButton.setOnClickListener(this);

        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View v) {
        String emailId = registerEmailIdEditText.getText().toString().trim();
        String password = registerPasswordEditText.getText().toString().trim();

        if (TextUtils.isEmpty(emailId)) {
            registerEmailIdEditText.setError("Email-Id is empty");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            registerPasswordEditText.setError("Password is empty");
            return;
        }

        if (password.length() < 6) {
            registerPasswordEditText.setError("Minimum 6 characters required");
            return;
        }

        registerProgressBar.setVisibility(View.VISIBLE);

        firebaseAuth.createUserWithEmailAndPassword(emailId,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                            assert firebaseUser != null;
                            firebaseUser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(RegisterActivity.this, "Verification Email has been sent.", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(RegisterActivity.this, "Verification Email has'nt been Sent.", Toast.LENGTH_SHORT).show();
                                    Log.d(TAG, "onFailure: Email not sent " + e.getMessage());
                                }
                            });

                            Toast.makeText(RegisterActivity.this, "User Created.", Toast.LENGTH_SHORT).show();

                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                            finish();
                        } else {
                            Toast.makeText(RegisterActivity.this, "Error ! ", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "Error : " + Objects.requireNonNull(task.getException()).getMessage());
                            registerProgressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }
}