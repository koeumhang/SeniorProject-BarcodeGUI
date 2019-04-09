package com.scrumptious6.xtract;

public class DataClass {
    private String barcode, storage;
    private int atp;


    public DataClass(String s, int i, String  b){
        barcode = s;
        atp = i;
        storage = b;
    }

    public DataClass() {

    }

    public String getBarcode(){

        return barcode;
    }

    public void setBarcode(String barcode) {

        this.barcode = barcode;
    }
    public int getAtp(){

        return atp;
    }

    public void setAtp(int atp) {

        this.atp = atp;
    }
    public String getStorage(){

        return storage;
    }

    public void setStorage(String storage) {

        this.storage = storage;
    }

    //public void printDatabase(){
        //String dbString = dbHandler.databaseToString();
        //myText.setText(dbString);
        //myInput.setText("");


}

