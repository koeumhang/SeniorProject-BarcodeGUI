package com.scrumptious6.xtract;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class DatabaseActivity extends AppCompatActivity {

    DatabaseHandler db;
    ArrayList<String> listitem;
    ListView userlist;
    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database);

        db = new DatabaseHandler(this);
        setContentView(R.layout.activity_database);
        listitem = new ArrayList<>();
        userlist  = findViewById(R.id.users_list);
        viewItem();
    }

    private void viewItem() {
        Cursor cursor = db.viewDatabase();

        if (cursor == null){
            Toast.makeText(this, "No data to show", Toast.LENGTH_SHORT).show();
        }else{
            while (cursor.moveToNext()){
                listitem.add(cursor.getString(0)); // index 1 is name, 0 is Id
            }
            adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,listitem);
            userlist.setAdapter(adapter);
        }
    }
}
