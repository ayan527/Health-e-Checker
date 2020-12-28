package com.example.health_e_checkerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {
    private static final String TAG = "ProfileActivity";
    private String patient_id;

    private EditText patientIdEditText, patientFullNameEditText, patientMobileNoEditText, patientGenderEditText;

    private AutoCompleteTextView ageACTV;
    private AutoCompleteTextView weightACTV;
    private AutoCompleteTextView heightACTV;
    private AutoCompleteTextView tempACTV;
    private AutoCompleteTextView pressureACTV;
    private AutoCompleteTextView heartRateACTV;

    private ArrayList<String> numberList= new ArrayList<String>();

    private Button updatePatientButton;

    private FirebaseDataOperations firebaseDataOperations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Log.i(TAG,"onCreate() method created");

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        Intent getIntent = getIntent();
        patient_id = getIntent.getStringExtra("patient_id");
        patientIdEditText = (EditText) findViewById(R.id.patientIdEditText);
        patientIdEditText.setText(patient_id);

        patientFullNameEditText = (EditText) findViewById(R.id.patientFullNameEditText);
        patientMobileNoEditText = (EditText) findViewById(R.id.patientMobileNoEditText);
        patientGenderEditText = (EditText) findViewById(R.id.patientGenderEditText);

        generateArrayList();

        ageACTV = (AutoCompleteTextView) findViewById(R.id.ageACTV);
        generateAgeArray();

        heartRateACTV = (AutoCompleteTextView) findViewById(R.id.heartRateACTV);
        generateHeartRateArray();

        weightACTV = (AutoCompleteTextView) findViewById(R.id.weightACTV);
        generateWeightArray();

        heightACTV = (AutoCompleteTextView) findViewById(R.id.heightACTV);
        generateHeightArray();

        tempACTV = (AutoCompleteTextView) findViewById(R.id.tempACTV);
        generateTempArray();

        pressureACTV = (AutoCompleteTextView) findViewById(R.id.pressureACTV);
        generatePressureArray();

        firebaseDataOperations = new FirebaseDataOperations();
        getDataFromFirebase(patient_id);
    }

    private void generatePressureArray() {
        ArrayList<String> pressureList = new ArrayList<String>();
        for (String i : numberList.subList(0,401)) {
            for (String j : numberList.subList(0,401)) {
                pressureList.add(i+ "/" +j);
            }
        }
        pressureACTV.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line,pressureList));
        pressureACTV.setThreshold(1);
        pressureACTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pressureACTV.showDropDown();
            }
        });
    }

    private void generateHeartRateArray() {
        heartRateACTV.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line,numberList.subList(0,501)));
        heartRateACTV.setThreshold(1);
        heartRateACTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                heartRateACTV.showDropDown();
            }
        });
    }

    private void generateTempArray() {
        ArrayList<String> tempList = new ArrayList<>();
        for (int i = 95; i <= 114; i++) {
            for (int j = 0; j <= 9; j++) {
                tempList.add(Integer.toString(i)+ "." +Integer.toString(j));
            }
        }
        tempList.add("115.0");
        tempACTV.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line,tempList));
        tempACTV.setThreshold(1);
        tempACTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tempACTV.showDropDown();
            }
        });
    }

    private void generateArrayList() {
        for (int i = 0; i <= 650; i++) {
            numberList.add(Integer.toString(i));
        }
    }

    private void generateHeightArray() {
        heightACTV.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line,numberList.subList(0,276)));
        heightACTV.setThreshold(1);
        heightACTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                heightACTV.showDropDown();
            }
        });
    }

    private void generateWeightArray() {
        weightACTV.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line,numberList));
        weightACTV.setThreshold(1);
        weightACTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                weightACTV.showDropDown();
            }
        });
    }

    private void generateAgeArray() {
        ageACTV.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line,numberList.subList(0,151)));
        ageACTV.setThreshold(1);
        ageACTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ageACTV.showDropDown();
            }
        });
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
        patientIdEditText.setText(patientDetails.getId().toString());
        patientFullNameEditText.setText(patientDetails.getFullName().toString());
        patientMobileNoEditText.setText(patientDetails.getMobileNo());
        patientGenderEditText.setText(patientDetails.getGender());

        ageACTV.setText(patientDetails.getAge().toString());
        weightACTV.setText(patientDetails.getWeight().toString());
        heightACTV.setText(patientDetails.getHeight().toString());
        tempACTV.setText(patientDetails.getTemperature().toString());
        pressureACTV.setText(patientDetails.getBloodPressure().toString());
        heartRateACTV.setText(patientDetails.getPulseRate().toString());

        updatePatientButton = (Button) findViewById(R.id.updatePatientButton);
        updatePatientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullName = patientFullNameEditText.getText().toString().trim();
                String mobileNo = patientFullNameEditText.getText().toString().trim();
                String gender = patientGenderEditText.getText().toString().trim();

                String age = ageACTV.getText().toString().trim();
                String weight = weightACTV.getText().toString().trim();
                String height = heightACTV.getText().toString().trim();
                String temperature = tempACTV.getText().toString().trim();
                String heartRate = heartRateACTV.getText().toString().trim();
                String pressure = pressureACTV.getText().toString().trim();

                if (TextUtils.isEmpty(fullName)) {
                    patientFullNameEditText.setError("Full Name is empty");
                    return;
                }

                if (TextUtils.isEmpty(mobileNo)) {
                    patientMobileNoEditText.setError("Mobile No is empty");
                    return;
                }

                if (TextUtils.isEmpty(gender)) {
                    patientGenderEditText.setError("Mobile No is empty");
                    return;
                }

                if (TextUtils.isEmpty(age)) {
                    ageACTV.setError("Full Name is empty");
                    return;
                }

                if (TextUtils.isEmpty(weight)) {
                    weightACTV.setError("Full Name is empty");
                    return;
                }

                if (TextUtils.isEmpty(height)) {
                    heightACTV.setError("Full Name is empty");
                    return;
                }

                if (TextUtils.isEmpty(temperature)) {
                    tempACTV.setError("Full Name is empty");
                    return;
                }

                if (TextUtils.isEmpty(pressure)) {
                    pressureACTV.setError("Full Name is empty");
                    return;
                }

                if (TextUtils.isEmpty(heartRate)) {
                    heartRateACTV.setError("Full Name is empty");
                    return;
                }

                String nurse_id = getIntent().getStringExtra("nurse_id");

                PatientDetails patientDetails = new PatientDetails();
                patientDetails.setId(patient_id);
                patientDetails.setFullName(fullName);
                patientDetails.setMobileNo(mobileNo);
                patientDetails.setGender(gender);
                patientDetails.setAge(age);
                patientDetails.setWeight(weight);
                patientDetails.setHeight(height);
                patientDetails.setTemperature(temperature);
                patientDetails.setPulseRate(heartRate);
                patientDetails.setBloodPressure(pressure);
                patientDetails.setLastNurseId(nurse_id);

                firebaseDataOperations.updateRecord(patientDetails);

                Toast.makeText(getApplicationContext(),"Record Updated Successfully!",Toast.LENGTH_SHORT).show();

                startActivity(new Intent(ProfileActivity.this,PatientActivity.class)
                                .putExtra("patient_id",patient_id)
                                .putExtra("nurse_id",nurse_id));
                finish();
            }
        });
    }
}