package com.example.health_e_checkerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class NewProfileActivity extends AppCompatActivity {
    private static final String TAG = "NewProfileActivity";

    private EditText patientIdEditText;
    private EditText firstNameEditText, lastNameEditText;
    private EditText ageEditText, weightEditText, heightEditText, sexEditText;
    private EditText bloodPressureEditText, temperatureEditText, pulseRateEditText;

    /*private Spinner sexSpinner;
    private String sex;*/

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

       /* sexSpinner = (Spinner) findViewById(R.id.sexSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.gender_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sexSpinner.setAdapter(adapter);
        sexSpinner.setSelection(0);*/

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
        bloodPressureEditText = (EditText) findViewById(R.id.bloodPressureEditText);
        temperatureEditText = (EditText) findViewById(R.id.temperatureEditText);
        pulseRateEditText = (EditText) findViewById(R.id.pulseRateEditText);

        sexEditText = (EditText) findViewById(R.id.sexEditText);

        /*sexSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sex = parent.getItemAtPosition(position).toString();
                Toast.makeText(getApplicationContext(),"Sex: " +sex,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/

        weightEditText = (EditText) findViewById(R.id.weightEditText);
        heightEditText = (EditText) findViewById(R.id.heightEditText);

        PatientDetails patientDetails = new PatientDetails();
        patientDetails.setId(patient_id);
        patientDetails.setFirstName(firstNameEditText.getText().toString());
        patientDetails.setLastName(lastNameEditText.getText().toString());
        patientDetails.setAge(Integer.parseInt(ageEditText.getText().toString()));
        patientDetails.setWeight(Integer.parseInt(weightEditText.getText().toString()));
        patientDetails.setHeight(Double.parseDouble(heightEditText.getText().toString()));
        patientDetails.setPulseRate(Integer.parseInt(pulseRateEditText.getText().toString()));
        patientDetails.setBloodPressure(bloodPressureEditText.getText().toString());
        patientDetails.setTemperature(Double.parseDouble(temperatureEditText.getText().toString()));
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