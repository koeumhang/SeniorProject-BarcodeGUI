package com.scrumptious6.xtract;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class BarcodeActivity extends AppCompatActivity {
    DatabaseHandler db;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode);

        IntentIntegrator integrator = new IntentIntegrator(BarcodeActivity.this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scan");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(true);
        integrator.setBarcodeImageEnabled(false);
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Log.d("MainActivity", "Cancelled scan");
                finish();
            } else {
                Log.d("MainActivity", "Scanned");

                // Retrieve information on the scanned item
                retreiveScannedInformation(result);
            }
        } else {
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void retreiveScannedInformation(final IntentResult result) {
        //keep track of the database where item was found, if at all
        final String dB_Name;
        //keep track of the cursor containing the items information
        final Cursor cursor_final;
        LayoutInflater layoutInflater = LayoutInflater.from(BarcodeActivity.this);
        final View promptView = layoutInflater.inflate(R.layout.scanlist_popup_dialog, null);


        //create the text for scanned item dialog
        final TextView title = promptView.findViewById(R.id.titleValue);
        final TextView barcode = promptView.findViewById(R.id.barcodeValue);
        final TextView atp = promptView.findViewById(R.id.atpValue);
        final TextView storageBin = promptView.findViewById(R.id.storageValue);
        final TextView plant = promptView.findViewById(R.id.plantValue);
        final TextView safetyStock = promptView.findViewById(R.id.stockValue);
        //retrieve the EditText fields
        final EditText atp_editable = promptView.findViewById(R.id.atp_editable_value);
        final EditText bin_editable = promptView.findViewById(R.id.storage_editable_value);

        //create buttons for quantity addition/subtraction on dialog box
        final Button addButton = promptView.findViewById(R.id.addButton);
        final Button subButton = promptView.findViewById(R.id.subButton);

        //assign a DatabaseHandler and
        db = new DatabaseHandler(this);
        //Query the scanlist for the scanned item
        final SQLiteDatabase db_scannedlist = db.getReadableDatabase();
        String selectQuery = "SELECT * FROM ScanList_Table" +
                " WHERE BARCODE = '" + result.getContents() + "';";
        Cursor cursor = db_scannedlist.rawQuery(selectQuery, null);

        //Check if scanned item was NOT in scanlist
        if (cursor.getCount() <= 0) {
            //Query the application database for the scanned item
            selectQuery = "SELECT * FROM Inventory_Table" +
                    " WHERE MATERIAL_NUM = '" + result.getContents() + "';";
            cursor = db_scannedlist.rawQuery(selectQuery, null);

            //Check if scanned item was NOT in application database, display error and dialog
            if (cursor.getCount() <= 0) {
                title.setText("New Item Information");
                barcode.setText("Barcode:   " + result.getContents());
                //Assign empty to database name as neither scanlist nor the application database had the scanned item
                dB_Name = "Empty";
                //Assign null to final cursor
                //make the TextViews for ATP and Storage Bin invisible and EditTexts visible
                atp.setVisibility(View.GONE);
                atp_editable.setVisibility(View.VISIBLE);
                storageBin.setVisibility(View.GONE);
                bin_editable.setVisibility(View.VISIBLE);

                //Make buttons invisible
                addButton.setVisibility(View.GONE);
                subButton.setVisibility(View.GONE);

                cursor_final = null;
                Toast.makeText(this, "No record found: Add to scanlist", Toast.LENGTH_LONG).show();
            }
            //Item was found in the application database, display dialog
            else {
                //Assign Inventory_Table to database name as scanned item was found there
                dB_Name = "Inventory_Table";
                //Assign cursor to final cursor
                cursor_final = cursor;
                cursor.moveToFirst();
                //Assign text values to the dialog TextViews
                title.setText("Database Item Information");
                barcode.setText("Material Num: " + cursor.getString(0));
                atp.setText("ATP: " + String.valueOf(cursor.getString(3)));
                storageBin.setText("Storage Bin: " + cursor.getString(2));
                plant.setText("Plant: S095");
                safetyStock.setText("Safety Stock: 0");
            }
        }
        //Scanned item was found in the scanlist, display dialog
        else {
            //Assign ScanList_Table to database name as scanned item was found there
            dB_Name = "ScanList_Table";
            //Assign cursor to final cursor
            cursor_final = cursor;
            cursor.moveToFirst();
            //Assign text values to the dialog TextViews
            title.setText("Scanlist Item Information");
            barcode.setText("Barcode: " + cursor.getString(0));
            atp.setText("ATP: " + String.valueOf(cursor.getString(1)));
            storageBin.setText("Storage Bin: " + cursor.getString(2));
            plant.setText("Plant: S095");
            safetyStock.setText("Safety Stock: 0");
        }

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(BarcodeActivity.this);
        alertDialogBuilder.setView(promptView);

        // setup a dialog window
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("Continue",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                                IntentIntegrator integrator = new IntentIntegrator(BarcodeActivity.this);
                                integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                                integrator.setPrompt("Scan");
                                integrator.setCameraId(0);
                                integrator.setBeepEnabled(true);
                                integrator.setBarcodeImageEnabled(false);
                                integrator.initiateScan();
                            }
                        })
                .setNegativeButton("Close",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                finish();
                            }
                        })
                .setNeutralButton("Add",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //check if listed item is already in scanlist
                                if (dB_Name.equals("ScanList_Table")) {
                                    Toast.makeText(BarcodeActivity.this, "Item is already in the scanlist", Toast.LENGTH_LONG).show();
                                }
                                //check if listed item is a new entry, add into scanlist
                                else if (dB_Name.equals("Empty")) {
                                    if (!atp_editable.getText().toString().isEmpty()&& !bin_editable.getText().toString().isEmpty()) {
                                        String atp_input = atp_editable.getText().toString();
                                        try {
                                            Integer.parseInt(atp_input);
                                            db.insertScannedItem(result.getContents(), atp_editable.getText().toString(), bin_editable.getText().toString());
                                            Toast.makeText(BarcodeActivity.this, "Item has been added to the scanlist", Toast.LENGTH_LONG).show();
                                        } catch (NumberFormatException e) {
                                            atp_editable.setError(atp_input + " is not a number");
                                            Toast.makeText(BarcodeActivity.this, "Error: Invalid data\nScan item and try again", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                    else {
                                        Toast.makeText(BarcodeActivity.this, "Error: Scan item again, fill in missing values and select add", Toast.LENGTH_LONG).show();
                                    }
                                }
                                //item is in the application database
                                else {
                                    cursor_final.moveToFirst();
                                    //copy the information into the scanlist
                                    db.insertScannedItem(cursor_final.getString(0),
                                            cursor_final.getString(3),
                                            cursor_final.getString(2)
                                    );
                                    Toast.makeText(BarcodeActivity.this, "Item has been copied to the scanlist", Toast.LENGTH_LONG).show();
                                }
                                //relaunch the scanner
                                IntentIntegrator integrator = new IntentIntegrator(BarcodeActivity.this);
                                integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                                integrator.setPrompt("Scan");
                                integrator.setCameraId(0);
                                integrator.setBeepEnabled(true);
                                integrator.setBarcodeImageEnabled(false);
                                integrator.initiateScan();
                            }
                        });
        // create an alert dialog
        AlertDialog scanned_alert = alertDialogBuilder.create();
        scanned_alert.show();

        //Button functionality
        //Add Button
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dB_Name.equals("ScanList_Table")) {
                    String selectQuery = "SELECT * FROM " + DatabaseHandler.DATABASE_TEMP_TABLE +
                            " WHERE BARCODE = '" + result.getContents() + "';";
                    //create an cursor to track updated value, create a database to track changes in database
                    final Cursor cursor_update = db_scannedlist.rawQuery(selectQuery, null);
                    final SQLiteDatabase db_updateScanList;

                    cursor_update.moveToFirst();
                    db_updateScanList = db.getWritableDatabase();
                    int atp_current = Integer.parseInt(cursor_final.getString(1));
                    int atp_update = Integer.parseInt(cursor_update.getString(1)) + 1;

                    ContentValues contentValues = new ContentValues();
                    contentValues.put("SCANLIST_ITEM_ATP", atp_update);
                    contentValues.put("SCANLIST_ITEM_STORAGE_BIN", cursor_final.getString(2));
                    db_updateScanList.update("Scanlist_Table", contentValues, "BARCODE=?", new String[]{cursor_update.getString(0)});
                    atp.setText("ATP:   " + String.valueOf(atp_current) + " -> " + String.valueOf(atp_update));
                } else {
                    Toast.makeText(BarcodeActivity.this, "Scanned item was not in the scanlist\nCannot modify quantity", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Subtract Button
        subButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dB_Name.equals("ScanList_Table")) {
                    String selectQuery = "SELECT * FROM " + DatabaseHandler.DATABASE_TEMP_TABLE +
                            " WHERE BARCODE = '" + result.getContents() + "';";
                    final Cursor cursor_update = db_scannedlist.rawQuery(selectQuery, null);
                    final SQLiteDatabase db_updateScanList;


                    cursor_update.moveToFirst();
                    db_updateScanList = db.getWritableDatabase();
                    int atp_current = Integer.parseInt(cursor_final.getString(1));
                    int atp_update = Integer.parseInt(cursor_update.getString(1)) - 1;

                    ContentValues contentValues = new ContentValues();
                    contentValues.put("SCANLIST_ITEM_ATP", atp_update);
                    contentValues.put("SCANLIST_ITEM_STORAGE_BIN", cursor_final.getString(2));
                    db_updateScanList.update("Scanlist_Table", contentValues, "BARCODE=?", new String[]{cursor_update.getString(0)});
                    atp.setText("ATP:   " + String.valueOf(atp_current) + " -> " + String.valueOf(atp_update));
                }
                else {
                    Toast.makeText(BarcodeActivity.this, "Scanned item was not in the scanlist\nCannot modify quantity", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

