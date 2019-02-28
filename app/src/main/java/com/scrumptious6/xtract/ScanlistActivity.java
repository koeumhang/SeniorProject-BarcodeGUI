package com.scrumptious6.xtract;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class ScanlistActivity extends AppCompatActivity
{
    DatabaseHandler dbh;
    EditText barCode, location, quan;
    private ImageButton addB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbh = new DatabaseHandler(this);
        setContentView(R.layout.activity_scanlist);
        TableLayout tableLayout = (TableLayout) findViewById(R.id.tablelayout);
        // Add header row
        TableRow rowHeader = new TableRow(this);
        rowHeader.setBackgroundColor(Color.parseColor("#c0c0c0"));
        rowHeader.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT));
        String[] headerText = {"BARCODE", "ATP", "STORAGE_BIN"};
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
            Cursor cursor = db.rawQuery(selectQuery, null);
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
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(ScanlistActivity.this, "Click item ", Toast.LENGTH_SHORT).show();

                        }
                    });
                    tableLayout.addView(row);
                }

            }
            db.setTransactionSuccessful();
            //barCode = (EditText) findViewById(R.id.Barcode);
            //location = (EditText) findViewById(R.id.ATP);
            //quan = (EditText) findViewById(R.id.Quantity);
            //addB = (ImageButton) findViewById(R.id.addB);
            //    public String onClick(View v){
             //       String s1 = barCode.getText().toString();
               //     String s2 = location.getText().toString();
               //     String s3 = quan.getText().toString();

                //}

        } catch (SQLiteException e) {
            e.printStackTrace();

        } finally {
            db.endTransaction();
            // End the transaction.
            db.close();
            // Close database
        }
    }
}
