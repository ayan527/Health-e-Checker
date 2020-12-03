package com.example.health_e_checkerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class NewProfileActivity extends AppCompatActivity {
    private static final String TAG = "NewProfileActivity";

    private EditText patientIdEditText;
    private EditText firstNameEditText, lastNameEditText;
    private EditText sexEditText, ageEditText, weightEditText, heightEditText;

    private Button addPatientButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_profile);

        Log.i(TAG,"onCreate() method called");

        Intent getIntent = getIntent();
        String patient_id = getIntent.getStringExtra("patient_id");
        patientIdEditText = (EditText) findViewById(R.id.patientIdEditText);
        patientIdEditText.setText(patient_id);

        addPatientButton = (Button) findViewById(R.id.addPatientButton);
        addPatientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPatientDetails(patient_id);

                startActivity(new Intent(NewProfileActivity.this,ScannerActivity.class));
                finish();
            }
        });
    }

    private void addPatientDetails(String patient_id) {
        firstNameEditText = (EditText) findViewById(R.id.firstNameEditText);
        lastNameEditText = (EditText) findViewById(R.id.lastNameEditText);
        ageEditText = (EditText) findViewById(R.id.ageEditText);
        sexEditText = (EditText) findViewById(R.id.sexEditText);
        weightEditText = (EditText) findViewById(R.id.weightEditText);
        heightEditText = (EditText) findViewById(R.id.heightEditText);

        PatientDetails patientDetails = new PatientDetails();
        patientDetails.setId(patient_id);
        patientDetails.setFirstName(firstNameEditText.getText().toString());
        patientDetails.setLastName(lastNameEditText.getText().toString());
        patientDetails.setAge(Integer.parseInt(ageEditText.getText().toString()));
        patientDetails.setWeight(Integer.parseInt(weightEditText.getText().toString()));
        patientDetails.setHeight(Double.parseDouble(heightEditText.getText().toString()));
        patientDetails.setSex(sexEditText.getText().toString());

        new WriteToDatabaseTask(getApplicationContext()).execute(patientDetails);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Log.i(TAG,"going to ScannerActivity");

        startActivity(new Intent(NewProfileActivity.this,ScannerActivity.class));
        finish();
    }
}