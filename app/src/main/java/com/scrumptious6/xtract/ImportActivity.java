package com.scrumptious6.xtract;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


/*
    This class handles the import screen. The import activity allows a user to access
    a csv file and upload to the database.
 */
public class ImportActivity extends AppCompatActivity {
    DatabaseHandler dbHandler;
    SQLiteDatabase db;
    private static final int READ_REQUEST_CODE = 42;
    String[] colNames;
    private int exitCode = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import);

        colNames = new String[]{"MATERIAL_NUM", "MATERIAL_PLANT", "STORAGE_BIN", "MATERIAL_ATP", "SAFETY_STOCK"};
        dbHandler = new DatabaseHandler(this);
        db = dbHandler.getReadableDatabase();

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Warning this will overwrite previous database!");
        alertDialogBuilder.setPositiveButton("Continue",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        dbHandler.clearDatabase();
                        getFile();
                    }
                });
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "Canceled!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        alertDialogBuilder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                finish();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public void onResume(){
        super.onResume();
        if(exitCode == -1) {
            Toast.makeText(this, "Error! No file was selected.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    public void getFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        startActivityForResult(intent, READ_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Checks if the file has any data. If data is null then returns back to application control
        //and sets the flag to end activity with error.
        if (data == null) {
            exitCode = -1;
            return;
        }

        Uri uri = data.getData();

        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            String line = reader.readLine();

            db.beginTransaction();
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(",");
                ContentValues cv = new ContentValues(5);
                cv.put(colNames[0], tokens[0].trim());
                cv.put(colNames[1], tokens[1].trim());
                cv.put(colNames[2], tokens[2].trim());
                cv.put(colNames[3], tokens[3].trim());
                cv.put(colNames[4], tokens[4].trim());
                db.insert("Inventory_Table", null, cv);
            }
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        Toast.makeText(this, "Sucess! Database was imported.", Toast.LENGTH_SHORT).show();
        finish();
    }
}