package com.example.health_e_checkerapp;

import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FirebaseDataOperations {
    private static final String TAG = "FirebaseDataOperations";

    private DatabaseReference databaseReference;

    public FirebaseDataOperations() {
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    public void insertPatientDetails(PatientDetails patientDetails) {
        databaseReference.child("patientDetails").child(patientDetails.getId()).setValue(patientDetails);
    }

    public void getPatientDetails(final String id, final GetDataListener getDataListener) {
        DatabaseReference patientDetailsRef = databaseReference.child("patientDetails");
        patientDetailsRef.orderByChild(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i(TAG,"onDataChange() called");

                boolean found = false;
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    PatientDetails patientDetails = snapshot.getValue(PatientDetails.class);
                    if(patientDetails.getId().equals(id)) {
                        found = true;
                        getDataListener.onSuccess(patientDetails);
                    }
                }
                if(!found) {
                    getDataListener.onFailure();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void updateRecord(PatientDetails patientDetails) {
        databaseReference.child("patientDetails").child(patientDetails.getId()).setValue(patientDetails);
    }
}
