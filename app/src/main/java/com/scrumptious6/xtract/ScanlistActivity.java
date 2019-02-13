package com.scrumptious6.xtract;

//package com.scrumptious6.xtract;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.journeyapps.barcodescanner.CaptureManager;

import java.util.ArrayList;

public class ScanlistActivity extends AppCompatActivity
{
    DatabaseHandler db;
    ArrayList<String> listitem;
    ListView userlist;
    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DatabaseHandler(this);
        setContentView(R.layout.activity_scanlist);
        listitem = new ArrayList<>();
        userlist  = findViewById(R.id.users_list);
        viewItem();
    }
    private void viewItem() {
        Cursor cursor = db.viewScanList();

        if (cursor == null){
            Toast.makeText(this, "No data to show", Toast.LENGTH_SHORT).show();
        }else{
            while (cursor.moveToNext()){
                listitem.add(cursor.getString(1)); // index 1 is name, 0 is Id
            }
            adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,listitem);
            userlist.setAdapter(adapter);
        }
    }

}

/*
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

public class ScanlistActivity extends AppCompatActivity {
    DatabaseHandler db;
    ListView itemlist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanlist);
        db = new DatabaseHandler(this);
    }
}
*/