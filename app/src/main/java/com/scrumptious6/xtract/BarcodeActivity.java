package com.scrumptious6.xtract;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceView;

public class BarcodeActivity extends AppCompatActivity {

    private SurfaceView cameraPreview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode);

        cameraPreview = (SurfaceView) findViewById(R.id.camera_preview);
    }
}
