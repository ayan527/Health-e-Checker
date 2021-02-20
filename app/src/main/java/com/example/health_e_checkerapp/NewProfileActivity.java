package com.example.health_e_checkerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class NewProfileActivity extends AppCompatActivity {
    private static final String TAG = "NewProfileActivity";

    private EditText newPatientIdEditText, newPatientFullNameEditText, newPatientMobileNoEditText;

    private RadioGroup newPatientGenderRadioGroup;
    private RadioButton newPatientGenderRadioButton;

    private AutoCompleteTextView newAgeACTV;
    private AutoCompleteTextView newWeightACTV;
    private AutoCompleteTextView newHeightACTV;
    private AutoCompleteTextView newTempACTV;
    private AutoCompleteTextView newPressureACTV;
    private AutoCompleteTextView newHeartRateACTV;

    private ArrayList<String> numberList= new ArrayList<String>();

    private Button addPatientButton;

    private String nurse_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_profile);

        Log.i(TAG,"onCreate() method called");

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        Intent getIntent = getIntent();
        String patient_id = getIntent.getStringExtra("patient_id");
        newPatientIdEditText = (EditText) findViewById(R.id.newPatientIdEditText);
        newPatientIdEditText.setText(patient_id);

        nurse_id = getIntent.getStringExtra("nurse_id");

        generateArrayList();

        newAgeACTV = (AutoCompleteTextView) findViewById(R.id.newAgeACTV);
        generateAgeArray();

        newHeartRateACTV = (AutoCompleteTextView) findViewById(R.id.newHeartRateACTV);
        generateHeartRateArray();

        newWeightACTV = (AutoCompleteTextView) findViewById(R.id.newWeightACTV);
        generateWeightArray();

        newHeightACTV = (AutoCompleteTextView) findViewById(R.id.newHeightACTV);
        generateHeightArray();

        newTempACTV = (AutoCompleteTextView) findViewById(R.id.newTempACTV);
        generateTempArray();

        newPressureACTV = (AutoCompleteTextView) findViewById(R.id.newPressureACTV);
        generatePressureArray();

        addPatientButton = (Button) findViewById(R.id.addPatientButton);
        addPatientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPatientDetails(patient_id);

                startActivity(new Intent(NewProfileActivity.this,PatientActivity.class)
                                .putExtra("patient_id",patient_id)
                                .putExtra("nurse_id",nurse_id));
                finish();
            }
        });
    }

    private void generatePressureArray() {
        ArrayList<String> pressureList = new ArrayList<String>();
        for (String i : numberList.subList(0,401)) {
            for (String j : numberList.subList(0,401)) {
                pressureList.add(i+ "/" +j);
            }
        }
        newPressureACTV.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line,pressureList));
        newPressureACTV.setThreshold(1);
        newPressureACTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newPressureACTV.showDropDown();
            }
        });
    }

    private void generateHeartRateArray() {
        newHeartRateACTV.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line,numberList.subList(0,501)));
        newHeartRateACTV.setThreshold(1);
        newHeartRateACTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newHeartRateACTV.showDropDown();
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
        newTempACTV.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line,tempList));
        newTempACTV.setThreshold(1);
        newTempACTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newTempACTV.showDropDown();
            }
        });
    }

    private void generateArrayList() {
        for (int i = 0; i <= 650; i++) {
            numberList.add(Integer.toString(i));
        }
    }

    private void generateHeightArray() {
        newHeightACTV.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line,numberList.subList(0,276)));
        newHeightACTV.setThreshold(1);
        newHeightACTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newHeightACTV.showDropDown();
            }
        });
    }

    private void generateWeightArray() {
        newWeightACTV.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line,numberList));
        newWeightACTV.setThreshold(1);
        newWeightACTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newWeightACTV.showDropDown();
            }
        });
    }

    private void generateAgeArray() {
        newAgeACTV.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line,numberList.subList(0,151)));
        newAgeACTV.setThreshold(1);
        newAgeACTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newAgeACTV.showDropDown();
            }
        });
    }

    private void addPatientDetails(String patient_id) {
        newPatientFullNameEditText = (EditText) findViewById(R.id.newPatientFullNameEditText);
        newPatientMobileNoEditText = (EditText) findViewById(R.id.newPatientMobileNoEditText);
        newPatientGenderRadioGroup = (RadioGroup) findViewById(R.id.newPatientGenderRadioGroup);

        int newPatientGenderId = newPatientGenderRadioGroup.getCheckedRadioButtonId();
        if (newPatientGenderId == -1) {
            Toast.makeText(NewProfileActivity.this,"Gender is not selected",Toast.LENGTH_SHORT).show();
            return;
        } else {
            newPatientGenderRadioButton = (RadioButton) findViewById(newPatientGenderId);
        }

        String fullName = newPatientFullNameEditText.getText().toString().trim();
        String mobileNo = newPatientMobileNoEditText.getText().toString().trim();
        String gender = newPatientGenderRadioButton.getText().toString().trim();

        String age = newAgeACTV.getText().toString().trim();
        String weight = newWeightACTV.getText().toString().trim();
        String height = newHeartRateACTV.getText().toString().trim();
        String temperature = newTempACTV.getText().toString().trim();
        String heartRate = newHeartRateACTV.getText().toString().trim();
        String pressure = newPressureACTV.getText().toString().trim();

        if (TextUtils.isEmpty(fullName)) {
            newPatientFullNameEditText.setError("Full Name is empty");
            return;
        }

        if (TextUtils.isEmpty(mobileNo)) {
            newPatientMobileNoEditText.setError("Mobile No is empty");
            return;
        }

        if (TextUtils.isEmpty(age)) {
            newAgeACTV.setError("Full Name is empty");
            return;
        }

        if (TextUtils.isEmpty(weight)) {
            newWeightACTV.setError("Full Name is empty");
            return;
        }

        if (TextUtils.isEmpty(height)) {
            newHeightACTV.setError("Full Name is empty");
            return;
        }

        if (TextUtils.isEmpty(temperature)) {
            newTempACTV.setError("Full Name is empty");
            return;
        }

        if (TextUtils.isEmpty(pressure)) {
            newPressureACTV.setError("Full Name is empty");
            return;
        }

        if (TextUtils.isEmpty(heartRate)) {
            newHeartRateACTV.setError("Full Name is empty");
            return;
        }

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
        patientDetails.setLastNurseId(getIntent().getStringExtra("nurse_id"));

        new WriteToDatabaseTask(getApplicationContext()).execute(patientDetails);

        Toast.makeText(getApplicationContext(),"Record Added Successfully!",Toast.LENGTH_SHORT).show();

    }
}