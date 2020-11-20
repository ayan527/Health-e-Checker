package com.example.health_e_checkerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.DialogInterface;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata;
import com.google.mlkit.vision.barcode.Barcode;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.common.InputImage;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.otaliastudios.cameraview.CameraView;
import com.otaliastudios.cameraview.frame.Frame;
import com.otaliastudios.cameraview.frame.FrameProcessor;

import java.util.List;

public class ScannerActivity extends AppCompatActivity {
    private static final String TAG = "ScannerActivity";

    private CameraView scannerCameraView;
    private Button scanButton;

    private boolean isDetected;

    private BarcodeScannerOptions scannerOptions;
//    private BarcodeDetector scannerDetector;
    private BarcodeScanner scanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        Log.i(TAG,"onCreate() method called");

        Dexter.withContext(ScannerActivity.this)
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        setupScannerCamera();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        Toast.makeText(ScannerActivity.this,"Accept Camera Permission !",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

                    }
                }).check();
    }

    private void setupScannerCamera() {
        scanButton = (Button) findViewById(R.id.scanButton);
        scanButton.setEnabled(isDetected);
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isDetected = !isDetected;
                scanButton.setEnabled(isDetected);
            }
        });

        scannerOptions = new BarcodeScannerOptions.Builder()
                .setBarcodeFormats(
                        Barcode.FORMAT_QR_CODE,
                        Barcode.FORMAT_AZTEC)
                .build();
        scanner = BarcodeScanning.getClient(scannerOptions);

        scannerCameraView = (CameraView) findViewById(R.id.scannerCameraView);
        scannerCameraView.setLifecycleOwner(this);
        scannerCameraView.addFrameProcessor(new FrameProcessor() {
            @Override
            public void process(@NonNull Frame frame) {
                processImage(getVisionImageFromFrame(frame));
            }
        });
    }

    private void processImage(InputImage scannedImage) {
        if (!isDetected) {
            Task<List<Barcode>> result = scanner.process(scannedImage)
                    .addOnSuccessListener(new OnSuccessListener<List<Barcode>>() {
                        @Override
                        public void onSuccess(List<Barcode> barcodes) {
                            if (barcodes.size() > 0) {
                                isDetected = true;
                                scanButton.setEnabled(isDetected);

                                for (Barcode barcode: barcodes) {
                                   /* Rect bounds = barcode.getBoundingBox();
                                    Point[] corners = barcode.getCornerPoints();

                                    String rawValue = barcode.getRawValue();*/

                                    int valueType = barcode.getValueType();

                                    switch (valueType) {
                                        case Barcode.TYPE_TEXT :
                                            AlertDialog.Builder builder = new AlertDialog.Builder(ScannerActivity.this);
                                            builder.setMessage(barcode.getRawValue())
                                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            dialog.dismiss();
                                                        }
                                                    });
                                            AlertDialog alertDialog = builder.create();
                                            alertDialog.show();
                                            break;
                                    }
                                }
                            }

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ScannerActivity.this,"Error ! : " +e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private InputImage getVisionImageFromFrame(Frame frame) {
        byte[] frameData = frame.getData();
        return InputImage.fromByteArray(frameData,frame.getSize().getWidth(),frame.getSize().getHeight(),frame.getRotation(),
                InputImage.IMAGE_FORMAT_NV21);
    }
}