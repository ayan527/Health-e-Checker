package com.example.health_e_checkerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class StartActivity extends AppCompatActivity{
    private static final String TAG = "StartActivity";

    private static final int RC_HANDLE_CAMERA_PERM = 2;

    private ProgressBar startProgressBar;

    private FirebaseAuth firebaseAuth;

    private DatabaseReference databaseReference;

    //Permission-grant Checking
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode != RC_HANDLE_CAMERA_PERM) {
            //Camera Permission not requested
            Log.d(TAG, "Error in Camera-Permission: " + requestCode);

            //Call parent class menthod
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            return;
        }

        if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //Camera Permission is granted
            Log.d(TAG, "Camera-Permission is granted");

            //Create the camera-source
            startTheApp();
            return;
        }

        //Camera Permission is not granted
        Log.d(TAG, "Camera-Permission is not granted");

        //On "OK" application will request
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                ActivityCompat.requestPermissions(StartActivity.this, permissions, RC_HANDLE_CAMERA_PERM);
            }
        };

        //Creating an AlertDialog Box for informing the client
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Health-e-Checker Error")
                .setMessage("The app needs CAMERA permission")
                .setPositiveButton("OK", listener)
                .create()
                .show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        Log.i(TAG,"onCreate() method called");

        startProgressBar = (ProgressBar) findViewById(R.id.startProgressBar);
        startProgressBar.setVisibility(View.VISIBLE);
    }

    private void startTheApp() {
        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() != null) {
            databaseReference = FirebaseDatabase.getInstance().getReference();
            String emailId = firebaseAuth.getCurrentUser().getEmail();
            databaseReference.child("nurseDetails").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        NurseDetails nurse = snapshot.getValue(NurseDetails.class);
                        if (nurse.getEmailId().equals(emailId)) {
                            startProgressBar.setVisibility(View.GONE);
                            startActivity(new Intent(StartActivity.this,MainActivity.class)
                                    .putExtra("nurse_id",nurse.getId())
                                    .putExtra("nurse_fullName",nurse.getFullName())
                                    .putExtra("nurse_emailId",nurse.getEmailId())
                                    .putExtra("nurse_gender",nurse.getGender())
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
        } else {
            startProgressBar.setVisibility(View.GONE);
            startActivity(new Intent(StartActivity.this,LoginActivity.class));
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        int rc = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (rc == PackageManager.PERMISSION_GRANTED) {
            //Camera Permission is granted.
            startTheApp();
        } else {
            //Camera Permission is not granted. So make a request for Camera Permission
            requestCameraPermission();
        }
    }

    private void requestCameraPermission() {
        final String[] permissions = new String[]{Manifest.permission.CAMERA};

        if (!ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CAMERA)) {
            //Request has been sent to user for Camera permission.
            ActivityCompat.requestPermissions(StartActivity.this, permissions, RC_HANDLE_CAMERA_PERM);
        }
    }
}