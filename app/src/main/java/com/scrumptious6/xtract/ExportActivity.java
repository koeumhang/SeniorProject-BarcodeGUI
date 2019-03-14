package com.scrumptious6.xtract;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ExportActivity extends AppCompatActivity {

    DatabaseHandler DBob;
    private Button exportButton;

    private Context mContext;
    private static final int REQUEST = 112;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export);
        mContext= ExportActivity.this;

        if (Build.VERSION.SDK_INT >= 23) {
            String[] PERMISSIONS = {android.Manifest.permission.READ_EXTERNAL_STORAGE,android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
            if (!hasPermissions(mContext, PERMISSIONS)) {
                ActivityCompat.requestPermissions((Activity) mContext, PERMISSIONS, REQUEST );
            }
        }
        DBob = new DatabaseHandler(this);
        exportButton = (Button) findViewById(R.id.exButton);
        exportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try
                {
                    new ExportDatabaseCSVTask().execute("");
                }
                catch(Exception ex)
                {
                    Log.e("Error in ExportActivity",ex.toString());
                }
            }
        });
    }
    private class ExportDatabaseCSVTask extends AsyncTask<String, Void, Boolean>
    {
        Context ctx = ExportActivity.this;
        private final ProgressDialog dialog = new ProgressDialog(ctx);
        // can use UI thread here
        @Override
        protected void onPreExecute()
        {
            this.dialog.setMessage("Exporting database...");
            this.dialog.show();
        }
        // automatically done on worker thread (separate from UI thread)
        protected Boolean doInBackground(final String... args)
        {
            File dbFile=getDatabasePath("Inventory.db");
            File exportDir = new File(Environment.getExternalStorageDirectory(), "");
            if (!exportDir.exists())
            {
                exportDir.mkdirs();
            }
            //File file = new File("data/data/com.scrumptious6.xtract/", "excelDB.csv");
            File file = new File(exportDir, "excelDB.csv");
            try
            {
                file.createNewFile();
                CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
                SQLiteDatabase db = DBob.getReadableDatabase();
                String selectQuery = "SELECT * FROM " + DatabaseHandler.DATABASE_TEMP_TABLE;
                final Cursor cursor = db.rawQuery(selectQuery, null);
                Cursor curCSV = db.rawQuery(selectQuery, null);
                csvWrite.writeNext(curCSV.getColumnNames());
                while(curCSV.moveToNext()) {
                    String outlet_barcode = curCSV.getString(curCSV.getColumnIndex("BARCODE"));
                    int outlet_atp = curCSV.getInt(curCSV.getColumnIndex("SCANLIST_ITEM_ATP"));
                    String outlet_storage_bin = curCSV.getString(curCSV.getColumnIndex("SCANLIST_ITEM_STORAGE_BIN"));
                    String outlet_plant = curCSV.getString(curCSV.getColumnIndex("SCANLIST_ITEM_PLANT"));
                    String outlet_safety_stock = curCSV.getString(curCSV.getColumnIndex("SCANLIST_SAFETY_STOCK"));
                    String[] colText = {outlet_barcode, outlet_atp + "", outlet_storage_bin, outlet_plant, outlet_safety_stock};
                    csvWrite.writeNext(colText);
                }
                csvWrite.close();
                curCSV.close();
                return true;
            }
            catch(SQLException sqlEx)
            {
                Log.e("MainActivity", sqlEx.getMessage(), sqlEx);
                return false;
            }
            catch (IOException e)
            {
                Log.e("MainActivity", e.getMessage(), e);
                return false;
            }
        }
        // can use UI thread here
        @Override
        protected void onPostExecute(final Boolean success)
        {
            if (this.dialog.isShowing())
            {
                this.dialog.dismiss();
            }
            if (success)
            {
                Toast.makeText(ctx, "Export successful!", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(ctx, "Export failed", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //do here
                } else {
                    Toast.makeText(mContext, "The app was not allowed to read your store.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}


