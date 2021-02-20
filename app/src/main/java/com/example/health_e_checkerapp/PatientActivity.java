package com.example.health_e_checkerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;

public class PatientActivity extends AppCompatActivity {
    private static final String TAG = "PatientActivity";

    private TextView patientIdTextView, patientFullNameTextView, patientMobileNoTextView, patientViewedTextView, patientGenderTextView;
    private TextView patientAgeTextView, patientWeightTextView, patientHeightTextView, patientTempTextView, patientHeartRateTextView, patientPressureTextView;

    private ImageButton updatePatientImageButton;

    private Animation button_click;

    private FirebaseDataOperations dataOperations;

    private String patient_id;
    private String nurse_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient);

        Log.i(TAG,"onCreate() method called");

        nurse_id = getIntent().getStringExtra("nurse_id");

        button_click = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.button_click);

        dataOperations = new FirebaseDataOperations();

        patientFullNameTextView = (TextView) findViewById(R.id.patientFullNameTextView);
        patientMobileNoTextView = (TextView) findViewById(R.id.patientMobileNoTextView);
        patientIdTextView = (TextView) findViewById(R.id.patientIdTextView);
        patientGenderTextView = (TextView) findViewById(R.id.patientGenderTextView);
        patientViewedTextView = (TextView) findViewById(R.id.patientViewedTextView);

        patientAgeTextView = (TextView) findViewById(R.id.patientAgeTextView);
        patientWeightTextView = (TextView) findViewById(R.id.patientWeightTextView);
        patientHeightTextView = (TextView) findViewById(R.id.patientHeightTextView);
        patientTempTextView = (TextView) findViewById(R.id.patientTempTextView);
        patientPressureTextView = (TextView) findViewById(R.id.patientPressureTextView);
        patientHeartRateTextView = (TextView) findViewById(R.id.patientHeartRateTextView);

        getAndSetPatientDetails();

        updatePatientImageButton = (ImageButton) findViewById(R.id.updatePatientImageButton);
    }

    private void getAndSetPatientDetails() {
        patient_id = getIntent().getStringExtra("patient_id");
        getDataFromFirebase(patient_id);
    }

    private void getDataFromFirebase(String patient_id) {
        dataOperations.getPatientDetails(patient_id, new GetDataListener() {
            @Override
            public void onStart() {
                Log.d("Health-e-Checker", "Firebase Data Fetch Started");
            }

            @Override
            public void onSuccess(PatientDetails patientDetails) {
                showDataOnUI(patientDetails);
            }

            @Override
            public void onFailure() {
                Log.e("Health-e-Checker", "Error fetching the data");
            }
        });
    }

    private void showDataOnUI(PatientDetails patientDetails) {
        String patient_fullName = patientDetails.getFullName().toString();
        String patient_mobileNo = patientDetails.getMobileNo().toString();
        String patient_gender = patientDetails.getGender().toString();
        String patient_viewed = patientDetails.getLastNurseId().toString();

        patientFullNameTextView.setText(patient_fullName);
        patientMobileNoTextView.setText("+91 " +patient_mobileNo);
        patientIdTextView.setText(patient_id);
        patientGenderTextView.setText(patient_gender);
        patientViewedTextView.setText(patient_viewed);

        String patient_age = patientDetails.getAge().toString();
        String patient_weight = patientDetails.getWeight().toString();
        String patient_height = patientDetails.getHeight().toString();
        String patient_temp = patientDetails.getTemperature().toString();
        String patient_heartRate = patientDetails.getPulseRate().toString();
        String patient_pressure = patientDetails.getBloodPressure().toString();

        patientAgeTextView.setText(patient_age);
        patientWeightTextView.setText(patient_weight+ " kg");
        patientHeightTextView.setText(patient_height+ " cm");
        patientTempTextView.setText(patient_temp+ " °F");
        patientHeartRateTextView.setText(patient_heartRate+ " bpm");
        patientPressureTextView.setText(patient_pressure+ " mmHg");

        updatePatientImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePatientImageButton.setAnimation(button_click);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(PatientActivity.this,ProfileActivity.class)
                                .putExtra("patient_id", patient_id)
                                .putExtra("nurse_id", nurse_id));
                        finish();
                    }
                },100);
            }
        });
    }
}