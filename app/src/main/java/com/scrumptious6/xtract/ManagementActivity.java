package com.scrumptious6.xtract;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.app.AlertDialog;
import android.content.DialogInterface;

/*
    This class
 */
public class ManagementActivity extends AppCompatActivity {

    private Button databaseButton, scanlistButton, clearScanlist;
    DatabaseHandler dbh;

    ///Define all the buttoms from the Management class///
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_management);
        dbh = new DatabaseHandler(this);
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

        clearScanlist = (Button) findViewById(R.id.clearScanlist);
        clearScanlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ManagementActivity.this);
                alertDialogBuilder.setMessage("Are you sure you want to clear Scanlist");
                alertDialogBuilder.setPositiveButton("Continue",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                dbh.clearScanlist();
                                Toast.makeText(ManagementActivity.this, "Scanlist cleared", Toast.LENGTH_SHORT).show();

                            }
                        });
                alertDialogBuilder.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
               // dbh.clearScanlist();
               // Toast.makeText(ManagementActivity.this, "Data are clear", Toast.LENGTH_SHORT).show();

                //Intent dataActivity = new Intent(ManagementActivity.this, DatabaseActivity.class);
                //startActivity(dataActivity);
            }
        });
    }
}
