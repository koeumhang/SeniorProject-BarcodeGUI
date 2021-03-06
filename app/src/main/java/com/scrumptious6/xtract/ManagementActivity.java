package com.scrumptious6.xtract;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ManagementActivity extends AppCompatActivity {

    private Button databaseButton, scanlistButton;

    ///Define all the buttoms from the Management class///
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_management);

        //Manage Screen to Scanlist Screen
        scanlistButton = (Button) findViewById(R.id.scanlistButton);
        scanlistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent scanlistActivity = new Intent(ManagementActivity.this, ScanlistActivity.class);
                startActivity(scanlistActivity);
            }
        });

        databaseButton = (Button) findViewById(R.id.databaseButton);
        databaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dataActivity = new Intent(ManagementActivity.this, DatabaseActivity.class);
                startActivity(dataActivity);
            }
        });
    }
}
