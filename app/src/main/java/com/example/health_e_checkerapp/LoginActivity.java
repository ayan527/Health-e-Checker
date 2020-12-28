package com.example.health_e_checkerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
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

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "LoginActivity";

    private Animation button_click;

    private EditText loginEmailIdEditText;
    private EditText loginPasswordEditText;
    private TextView forgotPasswordTextView;
    private TextView registerTextView;

    private Button loginButton;

    private ProgressBar loginProgressBar;

    private FirebaseAuth firebaseAuth;

    private Boolean isEmailVerified = false;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Log.i(TAG,"onCreate() method called");

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        firebaseAuth = FirebaseAuth.getInstance();

        button_click = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.button_click);

        forgotPasswordTextView = (TextView) findViewById(R.id.forgotPasswordTextView);
        SpannableString ss2 = new SpannableString("Forgot Password?");
        ClickableSpan clickableSpan2 = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
            }
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };
        ss2.setSpan(clickableSpan2, 0,16, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        forgotPasswordTextView.setText(ss2);
        forgotPasswordTextView.setMovementMethod(LinkMovementMethod.getInstance());
        forgotPasswordTextView.setHighlightColor(Color.TRANSPARENT);
        forgotPasswordTextView.setLinkTextColor(getResources().getColor(R.color.text_field));

        registerTextView = (TextView) findViewById(R.id.registerTextView);
        SpannableString ss1 = new SpannableString("No account? Register Now");
        ClickableSpan clickableSpan1 = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };
        ss1.setSpan(clickableSpan1, 12, 20, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        registerTextView.setText(ss1);
        registerTextView.setMovementMethod(LinkMovementMethod.getInstance());
        registerTextView.setHighlightColor(Color.TRANSPARENT);
        registerTextView.setLinkTextColor(getResources().getColor(R.color.text_field));

        loginEmailIdEditText = (EditText) findViewById(R.id.loginEmailIdEditText);
        loginPasswordEditText = (EditText) findViewById(R.id.loginPasswordEditText);

        loginProgressBar = (ProgressBar) findViewById(R.id.loginProgressBar);

        loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(this);
    }

    private void verifyEmailAddress(String emailId) {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        assert currentUser != null;

        isEmailVerified = currentUser.isEmailVerified();

        if (! isEmailVerified) {
            Toast.makeText(getApplicationContext(),"Please go to your email & verify your account !",Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
            loginProgressBar.setVisibility(View.GONE);
            startActivity(new Intent(getApplicationContext(),LoginActivity.class));
            finish();
        } else {
            Toast.makeText(getApplicationContext(),"Logged in Successfully !",Toast.LENGTH_SHORT).show();
            databaseReference = FirebaseDatabase.getInstance().getReference();
            databaseReference.child("nurseDetails").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        NurseDetails nurse = snapshot.getValue(NurseDetails.class);
                        if (nurse.getEmailId().equals(emailId)) {
                            loginProgressBar.setVisibility(View.GONE);
                            startActivity(new Intent(getApplicationContext(),MainActivity.class).putExtra("nurse_id",nurse.getId())
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
        }
    }

    @Override
    public void onClick(View v) {

        loginButton.startAnimation(button_click);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
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

                if (password.length() < 8) {
                    loginPasswordEditText.setError("Minimum 8 characters required");
                    return;
                }

                loginProgressBar.setVisibility(View.VISIBLE);

                firebaseAuth.signInWithEmailAndPassword(emailId,password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    verifyEmailAddress(emailId);
                                } else {
                                    Toast.makeText(LoginActivity.this, "Wrong Email-Id or Password!", Toast.LENGTH_SHORT).show();
                                    resetAllFields();
                                    Log.d(TAG, "Error : " + Objects.requireNonNull(task.getException()).getMessage());
                                    loginProgressBar.setVisibility(View.GONE);
                                }
                            }
                        });
            }
        },100);
    }

    private void resetAllFields() {
        loginEmailIdEditText.setText("");
        loginPasswordEditText.setText("");
    }
}