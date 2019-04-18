package com.scrumptious6.xtract;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.client.android.Intents;

import java.util.ArrayList;
import java.util.List;

public class ScanlistActivity extends AppCompatActivity
{
    DatabaseHandler dbh;
    private ImageButton addB,search;
    DatabaseHandler db;
    String bar,quan, storage;
    TableLayout tableLayout;
    DataClass data = new DataClass();
    //////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbh = new DatabaseHandler(this);
        setContentView(R.layout.activity_scanlist);
        db = new DatabaseHandler(this);
        displayTable();
        search = (ImageButton) findViewById(R.id.searchButton);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ScanlistActivity.this);
                View view = getLayoutInflater().inflate(R.layout.activity_search,null);
                final EditText searchIn = view.findViewById(R.id.searchIn);
                Button ok = view.findViewById(R.id.okay);
                builder.setView(view);
                final AlertDialog dialog = builder.create();
                dialog.show();

                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // View view = getLayoutInflater().inflate(R.layout.activity_search,null);
                        String dB_Name;// = "ScanList_Table";
                        //Cursor cursor_final;
                        //assign a DatabaseHandler and
                        //final EditText searchIn = view.findViewById(R.id.searchIn);
                        //db = new DatabaseHandler(ScanlistActivity.this);
                        //Query the scanlist for the scanned item
                        final SQLiteDatabase db_scannedlist = db.getReadableDatabase();
                        String selectQuery = "SELECT * FROM ScanList_Table" +
                                " WHERE BARCODE = '" + searchIn.getText().toString() + "';";
                        Cursor cursor = db_scannedlist.rawQuery(selectQuery, null);

                        //Check if scanned item was NOT in scanlist
                        if (cursor.getCount() <= 0) {
                            //Query the application database for the scanned item
                            selectQuery = "SELECT * FROM Inventory_Table" +
                                    " WHERE MATERIAL_NUM = '" + searchIn.getText().toString() + "';";
                            cursor = db_scannedlist.rawQuery(selectQuery, null);

                            //Check if scanned item was NOT in application database, display error and dialog
                            if (cursor.getCount() <= 0) {
                                dB_Name = "Empty";
                                if(!searchIn.getText().toString().isEmpty()){
                                    Toast.makeText(ScanlistActivity.this, "item is not found", Toast.LENGTH_SHORT).show();

                                }else{
                                    Toast.makeText(ScanlistActivity.this, "Error, can't be empty", Toast.LENGTH_SHORT).show();
                                }
                            }
                            //Item was found in the application database, display dialog
                            else {

                                dB_Name = "Inventory_Table";
                                cursor.moveToFirst();
                            }
                        }
                        //Scanned item was found in the scanlist, display dialog
                        else {
                            //Assign ScanList_Table to database name as scanned item was found there
                            dB_Name = "ScanList_Table";
                            //Assign cursor to final cursor
                            //cursor_final = cursor;
                            cursor.moveToFirst();
                        }
                        if (dB_Name.equals("ScanList_Table")) {

                            AlertDialog.Builder builder = new AlertDialog.Builder(ScanlistActivity.this);
                            View view = getLayoutInflater().inflate(R.layout.search_popup_dialog,null);
                            final TextView title = view.findViewById(R.id.titleValue);
                            final TextView barcode = view.findViewById(R.id.barcodeValue);
                            final TextView atp = view.findViewById(R.id.atpValue);
                            final TextView storageBin = view.findViewById(R.id.storageValue);
                            final TextView plant = view.findViewById(R.id.plantValue);
                            final TextView safetyStock = view.findViewById(R.id.stockValue);
                            title.setText("Search Item Information");
                            barcode.setText("Barcode: " + cursor.getString(0));
                            atp.setText("ATP: " + String.valueOf(cursor.getString(1)));
                            storageBin.setText("Storage Bin: " + cursor.getString(2));
                            plant.setText("Plant: S095");
                            safetyStock.setText("Safety Stock: 0");

                            builder.setView(view);
                            final AlertDialog dialog = builder.create();
                            dialog.show();
                            //Toast.makeText(ScanlistActivity.this, "Item is already in the scanlist", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });////

        addB = (ImageButton) findViewById(R.id.addB);
        addB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ScanlistActivity.this);
                View view = getLayoutInflater().inflate(R.layout.activity_add_data,null);
                final EditText barcodeIn = view.findViewById(R.id.barcode);
                final EditText atpIn = view.findViewById(R.id.atp);
                final EditText storageIn = view.findViewById(R.id.storage);
                Button add = view.findViewById(R.id.addButton);
                Button can = view.findViewById(R.id.canButton);
                builder.setView(view);
                final AlertDialog dialog = builder.create();
                dialog.show();

                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String dB_Name;// = "ScanList_Table";
                        //Cursor cursor_final;
                        //assign a DatabaseHandler and
                        db = new DatabaseHandler(ScanlistActivity.this);
                        //Query the scanlist for the scanned item
                        final SQLiteDatabase db_scannedlist = db.getReadableDatabase();
                        String selectQuery = "SELECT * FROM ScanList_Table" +
                                " WHERE BARCODE = '" + barcodeIn.getText().toString() + "';";
                        Cursor cursor = db_scannedlist.rawQuery(selectQuery, null);

                        //Check if scanned item was NOT in scanlist
                        if (cursor.getCount() <= 0) {
                            //Query the application database for the scanned item
                            selectQuery = "SELECT * FROM Inventory_Table" +
                                    " WHERE MATERIAL_NUM = '" + barcodeIn.getText().toString() + "';";
                            cursor = db_scannedlist.rawQuery(selectQuery, null);

                            //Check if scanned item was NOT in application database, display error and dialog
                            if (cursor.getCount() <= 0) {
                                dB_Name = "Empty";
                                if(!barcodeIn.getText().toString().isEmpty()  && !atpIn.getText().toString().isEmpty()&& !storageIn.getText().toString().isEmpty()){
                                    bar = barcodeIn.getText().toString();
                                    quan = atpIn.getText().toString();
                                    storage = storageIn.getText().toString();
                                    try {
                                        data.setBarcode(bar);
                                        data.setAtp(Integer.parseInt(quan));
                                        data.setStorage(storage);
                                        db.insert(data);
                                        Toast.makeText(ScanlistActivity.this, "Item is added", Toast.LENGTH_SHORT).show();
                                        tableLayout.removeAllViewsInLayout();
                                        displayTable();
                                        dialog.dismiss();
                                    } catch (NumberFormatException e) {
                                        atpIn.setError(quan+ " is not a number");
                                        //Log.i("",quan+" is not a number");
                                        //Toast.makeText(ScanlistActivity.this, quan+" is not a number", Toast.LENGTH_SHORT).show();
                                    }
                                }else{
                                    Toast.makeText(ScanlistActivity.this, "Error", Toast.LENGTH_SHORT).show();
                                }
                            }
                            //Item was found in the application database, display dialog
                            else {

                                dB_Name = "Inventory_Table";
                                cursor.moveToFirst();
                            }
                        }
                        //Scanned item was found in the scanlist, display dialog
                        else {
                            //Assign ScanList_Table to database name as scanned item was found there
                            dB_Name = "ScanList_Table";
                            //Assign cursor to final cursor
                            //cursor_final = cursor;
                            cursor.moveToFirst();
                        }
                        if (dB_Name.equals("ScanList_Table")) {
                            Toast.makeText(ScanlistActivity.this, "Item is already in the scanlist", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                can.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog.dismiss();
                    }
                });
            }
        });
    }
    public void displayTable()
    {
        // Add header row
        tableLayout = (TableLayout) findViewById(R.id.tablelayout);
        TableRow rowHeader = new TableRow(this);
        rowHeader.setBackgroundColor(Color.parseColor("#c0c0c0"));
        rowHeader.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT));
        String[] headerText = {"BARCODE", "ATP", "STORAGE BIN"};
        for (String c : headerText) {
            TextView tv = new TextView(this);
            tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            tv.setGravity(Gravity.CENTER);
            tv.setTextSize(18);
            tv.setPadding(5, 5, 5, 5);
            tv.setText(c);
            rowHeader.addView(tv);
        }
        tableLayout.addView(rowHeader);

        // Get data from sqlite database and add them to the table
        // Open the database for reading
        SQLiteDatabase db = dbh.getReadableDatabase();
        // Start the transaction.
        db.beginTransaction();

        try {
            String selectQuery = "SELECT * FROM " + DatabaseHandler.DATABASE_TEMP_TABLE;
            final Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    // Read columns data
                    String outlet_barcode = cursor.getString(cursor.getColumnIndex("BARCODE"));
                    int outlet_atp = cursor.getInt(cursor.getColumnIndex("SCANLIST_ITEM_ATP"));
                    String outlet_storage_bin = cursor.getString(cursor.getColumnIndex("SCANLIST_ITEM_STORAGE_BIN"));
                    //String outlet_type = cursor.getString(cursor.getColumnIndex("outlet_type"));

                    // dara rows
                    TableRow row = new TableRow(this);
                    row.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                            TableLayout.LayoutParams.WRAP_CONTENT));
                    //String[] colText = {outlet_id + "", outlet_name, outlet_type};
                    String[] colText = {outlet_barcode, outlet_atp + "", outlet_storage_bin};
                    for (String text : colText) {
                        TextView tv = new TextView(this);
                        tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                                TableRow.LayoutParams.WRAP_CONTENT));
                        tv.setGravity(Gravity.CENTER);
                        tv.setTextSize(16);
                        tv.setPadding(5, 5, 5, 5);
                        tv.setText(text);
                        row.addView(tv);
                    }

                    row.setOnClickListener(new View.OnClickListener() {
                        int index = 0;
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(ScanlistActivity.this, "Click item ", Toast.LENGTH_SHORT).show();
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                            AlertDialog.Builder builder = new AlertDialog.Builder(ScanlistActivity.this);

                            View view = getLayoutInflater().inflate(R.layout.update_delete,null);
                            final TextView barcodeIn = view.findViewById(R.id.barcode);
                            final EditText atpIn = view.findViewById(R.id.atp);
                            final EditText storageIn = view.findViewById(R.id.storage);
                            Button update = view.findViewById(R.id.updateButton);
                            Button delete = view.findViewById(R.id.deleteButton);
                            Button cancel = view.findViewById(R.id.cancelButton);

                            List<DataClass> contacts = getAllContacts();
                            index = tableLayout.indexOfChild(v);
                            index--;
                            DataClass cn = contacts.get(index);
                            //String log = "Barcode: "+cn.getBarcode()+" ,ATP: " + cn.getAtp() + " ,Storage: " + cn.getStorage();
                            barcodeIn.setText(cn.getBarcode());
                            atpIn.setText(String.valueOf(cn.getAtp()));
                            storageIn.setText(cn.getStorage());
                            builder.setView(view);

                            final AlertDialog dialog = builder.create();
                            dialog.show();

                            //update button
                            update.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if(!barcodeIn.getText().toString().isEmpty()  && !atpIn.getText().toString().isEmpty()&& !storageIn.getText().toString().isEmpty()) {
                                        //dbh.update(bar, quan, storage);

                                        bar = barcodeIn.getText().toString();
                                        quan = atpIn.getText().toString();
                                        storage = storageIn.getText().toString();
                                        try {
                                            data.setBarcode(bar);
                                            data.setAtp(Integer.parseInt(quan));
                                            data.setStorage(storage);
                                            dbh.update(data);
                                            Toast.makeText(ScanlistActivity.this, "Item is added", Toast.LENGTH_SHORT).show();
                                            tableLayout.removeAllViewsInLayout();
                                            displayTable();
                                            Toast.makeText(ScanlistActivity.this, "Item is updated", Toast.LENGTH_SHORT).show();
                                            dialog.dismiss();
                                        }
                                        catch (NumberFormatException e) {
                                            atpIn.setError(quan+ " is not a number");
                                            //atpIn.setBackgroundResource(R.drawable.edterr);
                                            //Log.i("",quan+" is not a number");
                                            //Toast.makeText(ScanlistActivity.this, quan+" is not a number", Toast.LENGTH_SHORT).show();
                                        }
                                    }else {
                                        Toast.makeText(ScanlistActivity.this, "Error", Toast.LENGTH_SHORT).show();
                                    }
                                }

                            });
                            delete.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    bar = barcodeIn.getText().toString();
                                    quan = atpIn.getText().toString();
                                    storage = storageIn.getText().toString();
                                    data.setBarcode(bar);
                                    data.setAtp(Integer.parseInt(quan));
                                    data.setStorage(storage);
                                    dbh.delete(data);
                                    tableLayout.removeAllViewsInLayout();
                                    displayTable();
                                    Toast.makeText(ScanlistActivity.this, "Item is deleted", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();

                                }
                            });
                            cancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });

                        }
                    });
                    tableLayout.addView(row);
                }

            }
            db.setTransactionSuccessful();

        } catch (SQLiteException e) {
            e.printStackTrace();

        } finally {
            db.endTransaction();
            // End the transaction.
            db.close();
            // Close database
        }
    }
    public List<DataClass> getAllContacts() {
        List<DataClass> contactList = new ArrayList<DataClass>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + DatabaseHandler.DATABASE_TEMP_TABLE;
        SQLiteDatabase db = dbh.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                DataClass data = new DataClass();
                data.setBarcode(cursor.getString(0));
                data.setAtp(Integer.parseInt(cursor.getString(1)));
                data.setStorage(cursor.getString(2));
                contactList.add(data);
            }while(cursor.moveToNext());
        }
        cursor.close();
        return contactList;
    }

}

