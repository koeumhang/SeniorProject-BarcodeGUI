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
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
                finish();
            } else {
                Log.d("MainActivity", "Scanned");
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();

                // Retrieve information on the scanned item
                retreiveScannedInformation("ScanList_Table", "BARCODE", result);
            }
        } else {
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void retreiveScannedInformation(final String dB_Name, final String columnName, final IntentResult result) {
        LayoutInflater layoutInflater = LayoutInflater.from(BarcodeActivity.this);
        View promptView = layoutInflater.inflate(R.layout.scanlist_popup_dialog, null);

        //create the text for scanned item dialog
        final TextView barcode = promptView.findViewById(R.id.barcodeValue);
        final TextView atp = promptView.findViewById(R.id.atpValue);
        final TextView storageBin = promptView.findViewById(R.id.storageValue);
        final TextView plant = promptView.findViewById(R.id.plantValue);
        final TextView safetyStock = promptView.findViewById(R.id.stockValue);

        //create buttons for quantity addition/subtraction on dialog box
        Button addButton = promptView.findViewById(R.id.addButton);
        Button subButton = promptView.findViewById(R.id.subButton);

        //assign a DatabaseHandler and
        db = new DatabaseHandler(this);
        final SQLiteDatabase db_scannedlist = db.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + dB_Name +
                " WHERE " + columnName + " = '" + result.getContents() + "';";
        final Cursor cursor_SL = db_scannedlist.rawQuery(selectQuery, null);
        if (cursor_SL.getCount() <= 0) {
            if (dB_Name.equals("Inventory_Table")) {
                Toast.makeText(this, "No record found!\nAdd to Scan List", Toast.LENGTH_LONG).show();
            } else {
                retreiveScannedInformation("Inventory_Table", "MATERIAL_NUM", result);
            }
        }

        //Assign text values to the popup items
        if (cursor_SL.getCount() <= 0) {
            barcode.setText("Barcode:   " + result.getContents());
        } else {
            cursor_SL.moveToFirst();
            barcode.setText("Barcode:   " + cursor_SL.getString(0));
            atp.setText("ATP:   " + String.valueOf(cursor_SL.getString(1)));
            storageBin.setText("Storage Bin:    " + cursor_SL.getString(2));
            plant.setText("Plant:       S095");
            safetyStock.setText("Safety Stock:       0");
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
                                String selectQuery = "SELECT * FROM " + dB_Name +
                                        " WHERE BARCODE = '" + result.getContents() + "';";
                                final Cursor cursor_SL = db_scannedlist.rawQuery(selectQuery, null);

                                if (cursor_SL.getCount() <= 0) {
                                    if (dB_Name.equals("Inventory_Table")) {
                                        db.insertScannedItem(cursor_SL.getString(0));
                                    }
                                }
                                else {
                                    db.insertScannedItem(result.getContents());
                                }
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
                    final Cursor cursor_SL_update = db_scannedlist.rawQuery(selectQuery, null);
                    final SQLiteDatabase db_updateScanList;

                    cursor_SL_update.moveToFirst();
                    db_updateScanList = db.getWritableDatabase();
                    int atp_current = Integer.parseInt(cursor_SL.getString(1));
                    int atp_update = Integer.parseInt(cursor_SL_update.getString(1)) + 1;

                    ContentValues contentValues = new ContentValues();
                    contentValues.put("SCANLIST_ITEM_ATP", atp_update);
                    contentValues.put("SCANLIST_ITEM_STORAGE_BIN", cursor_SL.getString(2));
                    db_updateScanList.update("Scanlist_Table", contentValues, "BARCODE=?", new String[]{cursor_SL_update.getString(0)});
                    atp.setText("ATP:   " + String.valueOf(atp_current) + " -> " + String.valueOf(atp_update));
                } else {
                    Toast.makeText(BarcodeActivity.this, "Item found in the application's database\nCannot modify quantity", Toast.LENGTH_LONG).show();
                }
            }
        });

        //Subtract Button
        subButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String selectQuery = "SELECT * FROM " + DatabaseHandler.DATABASE_TEMP_TABLE +
                        " WHERE BARCODE = '" + result.getContents() + "';";
                final Cursor cursor_SL_update = db_scannedlist.rawQuery(selectQuery, null);
                final SQLiteDatabase db_updateScanList;


                cursor_SL_update.moveToFirst();
                db_updateScanList = db.getWritableDatabase();
                int atp_current = Integer.parseInt(cursor_SL.getString(1));
                int atp_update = Integer.parseInt(cursor_SL_update.getString(1)) - 1;

                ContentValues contentValues = new ContentValues();
                contentValues.put("SCANLIST_ITEM_ATP", atp_update);
                contentValues.put("SCANLIST_ITEM_STORAGE_BIN", cursor_SL.getString(2));
                db_updateScanList.update("Scanlist_Table", contentValues, "BARCODE=?", new String[]{cursor_SL_update.getString(0)});
                atp.setText("ATP:   " + String.valueOf(atp_current) + " -> " + String.valueOf(atp_update));
            }
        });
    }
}

