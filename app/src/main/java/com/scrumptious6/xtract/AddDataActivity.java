package com.scrumptious6.xtract;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

public class AddDataActivity extends AppCompatActivity {


    DatabaseHandler db;
    EditText bar, quan, storage;
    //Button add;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_data);
        db = new DatabaseHandler(this);
        bar = (EditText) findViewById(R.id.barcode);
        quan = (EditText) findViewById(R.id.atp);
        storage = (EditText) findViewById(R.id.storage);
       // add = (Button)findViewById(R.id.addButton);
        /*
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean insert = db.insert(bar.getText().toString()
                        ,quan.getText().toString()
                        ,storage.getText().toString());
                if(insert){
                   // Toast.makeText(AddDataActivity,null,"")
                }
            }
        });*/

    }
    public void addButtonClicked(View v){
        /*
        DataClass data = new DataClass(bar.getText().toString()
                ,Integer.parseInt(quan.getText().toString())
                ,storage.getText().toString());
        db.insert(data);*/
        DataClass data = new DataClass();
        data.setBarcode(bar.getText().toString());
        data.setAtp(Integer.parseInt(quan.getText().toString()));
        data.setStorage(storage.getText().toString());
        db.insert(data);


    }/*
    public void printDatabase(){
        String dbString = db.databaseToString();
        bar.setText(dbString);
        myInput.setText("");
    }*/

}
