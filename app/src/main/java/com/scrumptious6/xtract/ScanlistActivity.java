package com.scrumptious6.xtract;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ScanlistActivity extends AppCompatActivity
{
    DatabaseHandler dbh;
    private ImageButton addB;
    DatabaseHandler db;
    String bar, quan, storage;
    TableLayout tableLayout;
    DataClass data = new DataClass();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbh = new DatabaseHandler(this);
        setContentView(R.layout.activity_scanlist);
        db = new DatabaseHandler(this);
        displayTable();
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

                builder.setView(view);
                final AlertDialog dialog = builder.create();
                dialog.show();

                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if(!barcodeIn.getText().toString().isEmpty()  && !atpIn.getText().toString().isEmpty()&& !storageIn.getText().toString().isEmpty()){
                            bar = barcodeIn.getText().toString();
                            quan = atpIn.getText().toString();
                            storage = storageIn.getText().toString();

                            data.setBarcode(bar);
                            data.setAtp(Integer.parseInt(quan));
                            data.setStorage(storage);
                            db.insert(data);
                            Toast.makeText(ScanlistActivity.this, "Item is added", Toast.LENGTH_SHORT).show();
                            tableLayout.removeAllViewsInLayout();
                            displayTable();
                            dialog.dismiss();
                        }else{
                            Toast.makeText(ScanlistActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        }

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
                            final EditText barcodeIn = view.findViewById(R.id.barcode);
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

                                    bar = barcodeIn.getText().toString();
                                    quan = atpIn.getText().toString();
                                    storage = storageIn.getText().toString();
                                    data.setBarcode(bar);
                                    data.setAtp(Integer.parseInt(quan));
                                    data.setStorage(storage);

                                    if(!barcodeIn.getText().toString().isEmpty()  && !atpIn.getText().toString().isEmpty()&& !storageIn.getText().toString().isEmpty()) {
                                        //dbh.update(bar, quan, storage);
                                        dbh.update(data);
                                        tableLayout.removeAllViewsInLayout();
                                        displayTable();
                                        Toast.makeText(ScanlistActivity.this, "Item is updated", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();

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
                DataClass contact = new DataClass();
                contact.setBarcode(cursor.getString(0));
                contact.setAtp(Integer.parseInt(cursor.getString(1)));
                contact.setStorage(cursor.getString(2));
                contactList.add(contact);
            }while(cursor.moveToNext());
        }
        cursor.close();
        return contactList;
    }
}