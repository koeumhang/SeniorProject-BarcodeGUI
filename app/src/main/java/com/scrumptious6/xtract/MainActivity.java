package com.scrumptious6.xtract;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

/*
    This class defines the main screen of the application.
 */
//
public class MainActivity extends AppCompatActivity {

    DatabaseHandler db;
    private ImageButton scanButton;
    private ImageButton manageButton;
    private ImageButton importButton;
    private ImageButton exportButton;

    ///Define of all buttons from the home page///
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scanButton = (ImageButton) findViewById(R.id.scanButton);
        manageButton = (ImageButton) findViewById(R.id.manageButton);
        importButton = (ImageButton) findViewById(R.id.importButton);
        exportButton = (ImageButton) findViewById(R.id.exportButton);

        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BarcodeActivity.class);
                startActivity(intent);
            }
        });
        manageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent manageActivity = new Intent(MainActivity.this, ManagementActivity.class);
                startActivity(manageActivity);
            }
        });
        importButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent importActivity = new Intent(MainActivity.this, ImportActivity.class);
                startActivity(importActivity);
            }
        });
        exportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ExportActivity.class);
                startActivity(intent);
            }
        });
    }
} // End of MainActivity class