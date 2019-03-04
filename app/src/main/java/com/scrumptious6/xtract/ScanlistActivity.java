package com.scrumptious6.xtract;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
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

    private ImageButton addB;
    DatabaseHandler db;
    String bar, quan, storage;
    List<DataClass> inventoryList;
    DataClass data = new DataClass();
    InventoryList list;
    Cursor cs;
    SQLiteDatabase sd;
    String GetSQliteQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbh = new DatabaseHandler(this);
        setContentView(R.layout.activity_scanlist);
        db = new DatabaseHandler(this);
        //bar = (EditText) findViewById(R.id.barcode);
        //quan = (EditText) findViewById(R.id.atp);
        //storage = (EditText) findViewById(R.id.storage);
        addB = (ImageButton) findViewById(R.id.addB);
        //inventoryList = new ArrayList<>();
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
                            dialog.dismiss();
                            Intent scanlistActivity = new Intent(ScanlistActivity.this, ScanlistActivity.class);
                            startActivity(scanlistActivity);
                        }else{
                            Toast.makeText(ScanlistActivity.this, "Error", Toast.LENGTH_SHORT).show();

                        }

                    }
                });
            }
        });
        final TableLayout tableLayout = (TableLayout) findViewById(R.id.tablelayout);
        // Add header row
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
                            //System.out.println("-------------------------------------------------------------------------------------------------------");

                            List<DataClass> contacts = getAllContacts();
                            index = tableLayout.indexOfChild(v);
                            index--;
                            //index = Math.min(Math.max(index, 0), contacts.size() - 1);
                            DataClass cn = contacts.get(index);

                            //for ( cn : contacts) {
                                String log = "Barcode: "+cn.getBarcode()+" ,ATP: " + cn.getAtp() + " ,Storage: " + cn.getStorage();
                                barcodeIn.setText(cn.getBarcode());
                                atpIn.setText(String.valueOf(cn.getAtp()));
                                storageIn.setText(cn.getStorage());
                            //}
                            builder.setView(view);
                            //reload();
                            /*barcodeIn.setText(data.getBarcode());
                            atpIn.setText(String.valueOf(data.getAtp()));
                            storageIn.setText(data.getStorage());*/
                            //System.out.println(data.getBarcode());
                            /*
                            builder.setView(view);
                            bar = barcodeIn.getText().toString();
                            quan = atpIn.getText().toString();
                            storage = storageIn.getText().toString();
                            */
                            /*
                            data.setBarcode(bar);
                            data.setAtp(Integer.parseInt(quan));
                            data.setStorage(storage);
                            */
                            /*Cursor cursor = this.sd.query(SQLiteHelper.TABLE_NAME_STUDENT, new String[]{SQLiteHelper._ID, SQLiteHelper.NAME, SQLiteHelper.AGE}, null, null, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();
                            }
                            return cursor;*/


                            /*
                            GetSQliteQuery = "SELECT * FROM DATABASE_TEMP_TABLE" ;
                            sd = openOrCreateDatabase("Scanlist_Table", Context.MODE_PRIVATE, null);
                            cs = sd.rawQuery(GetSQliteQuery, null);
                            cursor.moveToFirst();
                            */
                           // barcodeIn.setText(cursor.getString(0).toString());

                            //atpIn.setText(cursor.getString(1));

                            //storageIn.setText(cursor.getString(2).toString());

                            //SubJect.setText(cursor.getString(3).toString());

                                //barcodeIn.setText(data.getBarcode());
                                //atpIn.setText(String.valueOf(data.getAtp()));
                                //storageIn.setText(data.getStorage());
                            /*
                            String selectQuery = "SELECT * FROM " + DatabaseHandler.DATABASE_TEMP_TABLE;
                            Cursor cursor = sd.rawQuery(selectQuery, null);
                            //Cursor data = sd.rawQuery("SELECT * FROM DATABASE_TEMP_TABLE", null);
                            barcodeIn.setText(cursor.getString(0).toString());
                            atpIn.setText(cursor.getString(1));
                            storageIn.setText(cursor.getString(2).toString());
                            cursor.moveToFirst();*/

                            final AlertDialog dialog = builder.create();
                            dialog.show();
                            //update button

                            update.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    bar = barcodeIn.getText().toString();
                                    quan = atpIn.getText().toString();
                                    storage = storageIn.getText().toString();

                                    if(!barcodeIn.getText().toString().isEmpty()  && !atpIn.getText().toString().isEmpty()&& !storageIn.getText().toString().isEmpty()) {
                                        dbh.update(bar,quan,storage);
                                        Toast.makeText(ScanlistActivity.this, "Item is updated", Toast.LENGTH_SHORT).show();




                                    }else {
                                        Toast.makeText(ScanlistActivity.this, "Error", Toast.LENGTH_SHORT).show();

                                    }

                                }
                            });
                            cancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                    Intent scanlistActivity = new Intent(ScanlistActivity.this, ScanlistActivity.class);
                                    startActivity(scanlistActivity);
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

        {
            if (cursor.moveToFirst()) {
                do {
                    DataClass contact = new DataClass();
                    contact.setBarcode(cursor.getString(0));
                    contact.setAtp(Integer.parseInt(cursor.getString(1)));
                    contact.setStorage(cursor.getString(2));
                    contactList.add(contact);
                }while(cursor.moveToNext());
            }
           // cursor.moveToFirst();
            cursor.close();
        }
        //System.out.print("-------------------------------------------------------------------------------------------------------------");
        return contactList;
    }


    /*
    private void reload() {
        //SQLiteDatabase db;// = db.getReadableDatabase();
       // String selectQuery = "SELECT * FROM " + DatabaseHandler.DATABASE_TEMP_TABLE;
        //Cursor cursor = sd.rawQuery(selectQuery, null);

        sd = dbh.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + DatabaseHandler.DATABASE_TEMP_TABLE;
        final Cursor cs = sd.rawQuery(selectQuery, null);
        //DatabaseHandler d = new DatabaseHandler(this);
        //Cursor cs = sd.rawQuery("SELECT * FROM DATABASE_TEMP_TABLE", null);
        if (cs.moveToFirst()) {

            inventoryList.clear();
            do {
                inventoryList.add(new DataClass(
                        cs.getString(0),
                        cs.getInt(1),
                        cs.getString(2)
                        //cursorEmployees.getString(3),
                        //cursorEmployees.getDouble(4)
                ));
            } while (cs.moveToNext());
        }
        //cs.moveToFirst();
        System.out.println(list);

        // barcodeIn.setText(cursor.getString(0).toString());

        //atpIn.setText(cursor.getString(1));

        //storageIn.setText(cursor.getString(2).toString());

        //SubJect.setText(cursor.getString(3).toString());

        //cursorEmployees.close();
        //notifyDataSetChanged();

    }*/

}
