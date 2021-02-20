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
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "RegisterActivity";

    private Animation button_click;

    private EditText registerFullNameEditText;
    private EditText registerMobileNoEditText;
    private EditText registerNurseIdEditText;
    private EditText registerEmailIdEditText;
    private EditText registerPasswordEditText;
    private EditText registerConfirmPasswordEditText;

    private RadioGroup registerGenderRadioGroup;
    private RadioButton registerGenderRadioButton;

    private Button registerButton;

    private ProgressBar registerProgressBar;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    private boolean isUserCreated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Log.i(TAG,"onCreate() method called");

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        button_click = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.button_click);

        registerNurseIdEditText = (EditText) findViewById(R.id.registerNurseIdEditText);
        registerFullNameEditText = (EditText) findViewById(R.id.registerFullNameEditText);
        registerMobileNoEditText = (EditText) findViewById(R.id.registerMobileNoEditText);
        registerEmailIdEditText = (EditText) findViewById(R.id.registerEmailIdEditText);
        registerPasswordEditText = (EditText) findViewById(R.id.registerPasswordEditText);
        registerConfirmPasswordEditText = (EditText) findViewById(R.id.registerConfirmPasswordEditText);

        registerGenderRadioGroup = (RadioGroup) findViewById(R.id.registerGenderRadioGroup);

        registerProgressBar = (ProgressBar) findViewById(R.id.registerProgressBar);

        registerButton = (Button) findViewById(R.id.registerButton);
        registerButton.setOnClickListener(this);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public void onClick(View v) {
        registerButton.startAnimation(button_click);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String id = registerNurseIdEditText.getText().toString().trim();
                String fullName = registerFullNameEditText.getText().toString().trim();
                String mobileNo = registerMobileNoEditText.getText().toString().trim();
                String emailId = registerEmailIdEditText.getText().toString().trim();
                String password = registerPasswordEditText.getText().toString().trim();
                String confirmPassword = registerConfirmPasswordEditText.getText().toString().trim();

                int radioButtonId = registerGenderRadioGroup.getCheckedRadioButtonId();
                if (radioButtonId == -1) {
                    Toast.makeText(RegisterActivity.this,"Gender is not selected",Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    registerGenderRadioButton = (RadioButton) findViewById(radioButtonId);
                }
                String gender = registerGenderRadioButton.getText().toString().trim();

                if (TextUtils.isEmpty(id)) {
                    registerNurseIdEditText.setError("Nurse-Id is empty");
                    return;
                }

                if (TextUtils.isEmpty(fullName)) {
                    registerFullNameEditText.setError("Full Name is empty");
                    return;
                }

                if (TextUtils.isEmpty(mobileNo)) {
                    registerMobileNoEditText.setError("Mobile No is empty");
                    return;
                }

                if (TextUtils.isEmpty(emailId)) {
                    registerEmailIdEditText.setError("Email-Id is empty");
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    registerPasswordEditText.setError("Password is empty");
                    return;
                }

                if (TextUtils.isEmpty(confirmPassword)) {
                    registerConfirmPasswordEditText.setError("Confirm Password is empty");
                    return;
                }

                if (password.length() < 8) {
                    registerPasswordEditText.setError("Minimum 8 characters required");
                    return;
                }

                if (! confirmPassword.equals(password)) {
                    registerConfirmPasswordEditText.setError("Password is'nt matching");
                    return;
                }

                registerProgressBar.setVisibility(View.VISIBLE);

                checkEmailOrIdPresent(databaseReference,id,fullName,mobileNo,gender,emailId,password);
            }
        },100);
    }

    private void addNurseDetails(String id, String fullName, String mobileNo, String gender, String emailId) {
        NurseDetails nurseDetails = new NurseDetails();

        nurseDetails.setId(id);
        nurseDetails.setFullName(fullName);
        nurseDetails.setMobileNo(mobileNo);
        nurseDetails.setGender(gender);
        nurseDetails.setEmailId(emailId);

        databaseReference.child("nurseDetails").child(nurseDetails.getId()).setValue(nurseDetails);
    }

    private void checkEmailOrIdPresent(DatabaseReference databaseReference, String id, String fullName, String mobileNo, String gender, String emailId, String password) {
        databaseReference.child("nurseDetails").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.i(TAG,"onDataChange() called");

                if (!isUserCreated) {
                    boolean found = false;
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        NurseDetails nurse = snapshot.getValue(NurseDetails.class);
                        if (nurse.getId().equals(id)) {
                            found = true;
                            Toast.makeText(getApplicationContext(), "Nurse-Id already exists!", Toast.LENGTH_SHORT).show();
                            break;
                        } else if (nurse.getEmailId().equals(emailId)) {
                            found = true;
                            Toast.makeText(getApplicationContext(), "Email-Id already exists!", Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }


                    if (!found) {
                        isUserCreated = !isUserCreated;
                        Log.i(TAG, "Calling create method");
                        createNurseUser(id, fullName, mobileNo, gender, emailId, password);
                    } else {
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
        registerNurseIdEditText.setText("");
        registerFullNameEditText.setText("");
        registerMobileNoEditText.setText("");
        registerGenderRadioGroup.clearCheck();
        registerEmailIdEditText.setText("");
        registerPasswordEditText.setText("");
        registerConfirmPasswordEditText.setText("");

        registerProgressBar.setVisibility(View.GONE);
    }

    private void createNurseUser(String id, String fullName, String mobileNo, String gender, String emailId, String password) {
        Log.i(TAG,"Creating User");

        firebaseAuth.createUserWithEmailAndPassword(emailId,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            addNurseDetails(id,fullName,mobileNo,gender,emailId);

                            FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                            assert currentUser != null;
                            currentUser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(RegisterActivity.this, "Verification Email has been sent.", Toast.LENGTH_SHORT).show();
                                    firebaseAuth.signOut();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(RegisterActivity.this, "Verification Email has'nt been Sent.", Toast.LENGTH_SHORT).show();
                                    Log.d(TAG, "onFailure: Email not sent " + e.getMessage());
                                }
                            });

                            //Toast.makeText(RegisterActivity.this, "User Created.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(RegisterActivity.this, "Registration not Successful! ", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "Error : " + Objects.requireNonNull(task.getException()).getMessage());
                            registerProgressBar.setVisibility(View.GONE);
                        }

                        startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                        finish();
                    }
                });
    }
}