package com.example.health_e_checkerapp;

public interface GetDataListener {
    public void onStart();
    public void onSuccess(PatientDetails patientDetails);
    public void onFailure();
}
