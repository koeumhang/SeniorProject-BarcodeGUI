package com.scrumptious6.xtract;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Inventory.db";
    public static final String DATABASE_TABLE = "Inventory_Table";
    public static final String DATABASE_TEMP_TABLE = "Scanlist_Table";

    //Columns for Inventory Table
    private static final String MATERIAL_NUM = "MATERIAL_NUM";
    private static final String MATERIAL_PLANT = "MATERIAL_PLANT";
    private static final String STORAGE_BIN = "STORAGE_BIN";
    private static final String MATERIAL_ATP = "MATERIAL_ATP";
    private static final String SAFETY_STOCK = "SAFETY_STOCK";

    //Column for Scanlist Table
    //private static final String ID = "ID";
    private static final String BARCODE = "BARCODE";
    private static final String SCANLIST_ITEM_ATP = "SCANLIST_ITEM_ATP";
    private SQLiteDatabase dbh;

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        ///The Scanlist table is created in the inventory database.
        String CREATE_SCANLIST_TABLE = "CREATE TABLE " + DATABASE_TEMP_TABLE + "("
                + BARCODE + " TEXT PRIMARY KEY," + SCANLIST_ITEM_ATP + " INTEGER" + ")";
        db.execSQL(CREATE_SCANLIST_TABLE);

        ///The Inventory table is created in the inventory database
        String CREATE_ITEMS_TABLE = "CREATE TABLE " + DATABASE_TABLE + "("
                + MATERIAL_NUM + " VARCHAR(25)," + MATERIAL_PLANT + " VARCHAR(25),"
                + STORAGE_BIN + " VARCHAR(25)," + MATERIAL_ATP + " INTEGER,"
                + SAFETY_STOCK + " INTEGER"
                + ")";
        db.execSQL(CREATE_ITEMS_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        //db.execSQL("DROP TABLE IF EXISTS "+ DATABASE_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+ DATABASE_TEMP_TABLE);
        onCreate(db);
    }
    //To insert Scanned Items into the table///
    public boolean insertScannedItem(String name){
        SQLiteDatabase idb = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(BARCODE, name);
        long result = idb.insert(DATABASE_TEMP_TABLE, null, contentValues);
        //long result = idb.insertWithOnConflict(DATABASE_TEMP_TABLE, null, contentValues, SQLiteDatabase.CONFLICT_IGNORE);
        return result != -1; //if result = -1 data absent insert
    }

    public void clearDatabase(){
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete(DATABASE_TABLE, null, null);
    }

    public boolean importDatabase(StringBuilder statement) throws IOException {
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            db.execSQL(statement.toString());
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return true;
    }
}
