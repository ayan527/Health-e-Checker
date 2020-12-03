package com.example.health_e_checkerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileActivity extends AppCompatActivity {
    private static final String TAG = "ProfileActivity";

    private EditText viewPatientIdEditText;
    private EditText viewFirstNameEditText, viewLastNameEditText;
    private EditText viewSexEditText, viewAgeEditText, viewWeightEditText, viewHeightEditText;

    private Button updatePatientButton;

    private FirebaseDataOperations firebaseDataOperations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Log.i(TAG,"onCreate() method created");

        Intent getIntent = getIntent();
        String patient_id = getIntent.getStringExtra("patient_id");

        firebaseDataOperations = new FirebaseDataOperations();
        getDataFromFirebase(patient_id);

        /*Intent getIntent = getIntent();
        String patient_id = getIntent.getStringExtra("patient_id");

        profileTextView = (TextView) findViewById(R.id.profileTextView);
        profileTextView.setText("Old Patient ID is : " +patient_id);*/
    }

    private void getDataFromFirebase(String patient_id) {
        firebaseDataOperations.getPatientDetails(patient_id, new GetDataListener() {
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
        viewPatientIdEditText = (EditText) findViewById(R.id.viewPatientIdEditText);
        viewPatientIdEditText.setText(patientDetails.getId());

        viewFirstNameEditText = (EditText) findViewById(R.id.viewFirstNameEditText);
        viewFirstNameEditText.setText(patientDetails.getFirstName());

        viewLastNameEditText = (EditText) findViewById(R.id.viewLastNameEditText);
        viewLastNameEditText.setText(patientDetails.getLastName());

        viewSexEditText = (EditText) findViewById(R.id.viewSexEditText);
        viewSexEditText.setText(patientDetails.getSex());

        viewAgeEditText = (EditText) findViewById(R.id.viewAgeEditText);
        viewAgeEditText.setText(patientDetails.getAge().toString());

        viewWeightEditText = (EditText) findViewById(R.id.viewWeightEditText);
        viewWeightEditText.setText(patientDetails.getWeight().toString());

        viewHeightEditText = (EditText) findViewById(R.id.viewHeightEditText);
        viewHeightEditText.setText(patientDetails.getHeight().toString());

        updatePatientButton = (Button) findViewById(R.id.updatePatientButton);
        updatePatientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PatientDetails patientDetails = new PatientDetails();
                patientDetails.setId(viewPatientIdEditText.getText().toString());
                patientDetails.setFirstName(viewFirstNameEditText.getText().toString());
                patientDetails.setLastName(viewLastNameEditText.getText().toString());
                patientDetails.setSex(viewSexEditText.getText().toString());
                patientDetails.setAge(Integer.parseInt(viewAgeEditText.getText().toString()));
                patientDetails.setWeight(Integer.parseInt(viewWeightEditText.getText().toString()));
                patientDetails.setHeight(Double.parseDouble(viewHeightEditText.getText().toString()));

                firebaseDataOperations.updateRecord(patientDetails);

                Toast.makeText(getApplicationContext(),"Record Updated Successfully!",Toast.LENGTH_SHORT).show();

                startActivity(new Intent(ProfileActivity.this,ScannerActivity.class));
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Log.i(TAG,"going to ScannerActivity");

        startActivity(new Intent(ProfileActivity.this,ScannerActivity.class));
        finish();
    }
}