package com.scrumptious6.xtract;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class DatabaseActivity extends AppCompatActivity {

    DatabaseHandler dbh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database);
        dbh = new DatabaseHandler(this);
        dbh = new DatabaseHandler(this);
        setContentView(R.layout.activity_database);

        TableLayout tableLayout = (TableLayout) findViewById(R.id.tablelayout);
        // Add header row
        TableRow rowHeader = new TableRow(this);
        rowHeader.setBackgroundColor(Color.parseColor("#c0c0c0"));
        rowHeader.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT));
        String[] headerText = {"Material No", "Plant", "Storage Bin", "ATP", "Safety Stock"};
        for (String c : headerText) {
            TextView tv = new TextView(this);
            tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            tv.setGravity(Gravity.CENTER);
            tv.setTextSize(14);
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
            String selectQuery = "SELECT * FROM " + DatabaseHandler.DATABASE_TABLE;
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    // Read columns data
                    String outlet_material_no = cursor.getString(cursor.getColumnIndex("MATERIAL_NUM"));
                    String outlet_plant = cursor.getString(cursor.getColumnIndex("MATERIAL_PLANT"));
                    String outlet_bin = cursor.getString(cursor.getColumnIndex("STORAGE_BIN"));
                    int outlet_atp = cursor.getInt(cursor.getColumnIndex("MATERIAL_ATP"));
                    int outlet_stock = cursor.getInt(cursor.getColumnIndex("SAFETY_STOCK"));
                    //String outlet_type = cursor.getString(cursor.getColumnIndex("outlet_type"));

                    // dara rows
                    TableRow row = new TableRow(this);
                    row.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                            TableLayout.LayoutParams.WRAP_CONTENT));
                    //String[] colText = {outlet_id + "", outlet_name, outlet_type};
                    String[] colText = {outlet_material_no, outlet_plant, outlet_bin,  outlet_atp + "", outlet_stock + ""};
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
                            Toast.makeText(DatabaseActivity.this, "Click item ", Toast.LENGTH_SHORT).show();
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
}






