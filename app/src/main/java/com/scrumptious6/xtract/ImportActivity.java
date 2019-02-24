package com.scrumptious6.xtract;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ImportActivity extends AppCompatActivity {
    DatabaseHandler db;
    private int mProgress = 0;
    private static final int READ_REQUEST_CODE = 42;
    MyTask myTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import);

        db = new DatabaseHandler(this);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Warning this will overwrite previous database!");
        alertDialogBuilder.setPositiveButton("Continue",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.addCategory(Intent.CATEGORY_OPENABLE);
                        intent.setType("*/*");
                        startActivityForResult(intent, READ_REQUEST_CODE);
                    }
                });
        alertDialogBuilder.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        StringBuilder stringBuilder = new StringBuilder();
        String line= "";
        stringBuilder.append(line);
        String str1 = "INSERT INTO Inventory_Table values('";
        Uri uri = data.getData();

        if(uri != null){
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                line = reader.readLine();
                db.clearDatabase();
                while ((line = reader.readLine()) != null) {
                    stringBuilder.setLength(0);
                    String[] tokens = line.split(",");
                    stringBuilder.append(str1 + tokens[0] + "','" + tokens[1] + "','" +
                            tokens[2] + "','" + tokens[3] + "','" + tokens[4] + "');");
                    db.importDatabase(stringBuilder);
                }
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        finish();
    }

    class MyTask extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String... strings) {
            //TODO: Implement an Async task to display progress
            int bytes = 0;
            while(bytes <= 100){

            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);

        }
    }
}
