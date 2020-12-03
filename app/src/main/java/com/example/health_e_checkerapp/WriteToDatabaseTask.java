package com.example.health_e_checkerapp;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class WriteToDatabaseTask extends AsyncTask<PatientDetails,String,String> {
    private Context context;

    public WriteToDatabaseTask(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(PatientDetails... patientDetails) {
        FirebaseDataOperations firebaseDataOperations = new FirebaseDataOperations();
        firebaseDataOperations.insertPatientDetails(patientDetails[0]);
        return "SUCCESS";
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Toast.makeText(context, "Record Inserted Successfully!", Toast.LENGTH_SHORT).show();
    }
}
