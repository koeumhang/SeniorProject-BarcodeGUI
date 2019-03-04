package com.scrumptious6.xtract;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class InventoryList extends ArrayAdapter<DataClass> {
    Context context;
    int layout;
    List<DataClass> inventoryList;
    SQLiteDatabase sd;

    public InventoryList(Context context, int layout, List<DataClass> inventoryList, SQLiteDatabase sd) {
        super(context, layout, inventoryList);
        this.context = context;
        this.layout = layout;
        this.inventoryList = inventoryList;
        this.sd = sd;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layout, null);

        //getting employee of the specified position
        DataClass data = inventoryList.get(position);

        //getting views
        TextView textViewName = view.findViewById(R.id.barcode);
        TextView textViewDept = view.findViewById(R.id.atp);
        TextView textViewSalary = view.findViewById(R.id.storage);
        //TextView textViewJoiningDate = view.findViewById(R.id.textViewJoiningDate);

        //adding data to views
        textViewName.setText(data.getBarcode());
        textViewDept.setText(data.getAtp());
        textViewSalary.setText(String.valueOf(data.getStorage()));
        //textViewJoiningDate.setText(data.getJoiningDate());

        //we will use these buttons later for update and delete operation
        //Button buttonDelete = view.findViewById(R.id.buttonDeleteEmployee);
        //Button buttonEdit = view.findViewById(R.id.buttonEditEmployee);

        return view;
    }

}
