package com.example.health_e_checkerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ForgotPasswordActivity extends AppCompatActivity {
    private static final String TAG = "ForgotPasswordActivity";

    private Animation button_click;

    private EditText forgotNurseIdEditText, forgotEmailIdEditText;
    private Button verifyButton;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    private boolean isUserPresent = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        Log.i(TAG,"onCreate() method called");

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        button_click = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.button_click);

        forgotNurseIdEditText = (EditText) findViewById(R.id.forgotNurseIdEditText);
        forgotEmailIdEditText = (EditText) findViewById(R.id.forgotEmailIdEditText);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();

        verifyButton = (Button) findViewById(R.id.verifyButton);
        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyButton.startAnimation(button_click);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String nurseId = forgotNurseIdEditText.getText().toString().trim();
                        String emailId = forgotEmailIdEditText.getText().toString().trim();

                        if (TextUtils.isEmpty(nurseId)) {
                            forgotNurseIdEditText.setError("Nurse-Id is empty");
                            return;
                        }

                        if (TextUtils.isEmpty(emailId)) {
                            forgotEmailIdEditText.setError("Email-Id is empty");
                            return;
                        }

                        checkEmailOrIdPresent(databaseReference,nurseId,emailId);
                    }
                },100);
            }
        });
    }

    private void checkEmailOrIdPresent(DatabaseReference databaseReference, String nurseId, String emailId) {
        databaseReference.child("nurseDetails").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.i(TAG,"onDataChange() called");

                if (!isUserPresent) {
                    boolean found = false;
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        NurseDetails nurse = snapshot.getValue(NurseDetails.class);
                        if (nurse.getId().equals(nurseId) && nurse.getEmailId().equals(emailId)) {
                            found = true;
                            break;
                        }
                    }

                    if (found) {
                        isUserPresent = !isUserPresent;
                        Log.i(TAG, "Calling verification method");
                        sendVerificationEmail(nurseId, emailId);
                    } else {
                        Toast.makeText(getApplicationContext(),"Incorrect nurse-id or email-id !",Toast.LENGTH_SHORT).show();
                        resetAllFields();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void resetAllFields() {
        forgotEmailIdEditText.setText("");
        forgotNurseIdEditText.setText("");
    }

    private void sendVerificationEmail(String nurseId, String emailId) {
        firebaseAuth.sendPasswordResetEmail(emailId)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(),"Verification email has been sent !",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ForgotPasswordActivity.this,LoginActivity.class));
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),"Error in sending verification email !",Toast.LENGTH_SHORT).show();
                        Log.i(TAG,"Error : " + e.getMessage());
                        resetAllFields();
                    }
                });
    }
}