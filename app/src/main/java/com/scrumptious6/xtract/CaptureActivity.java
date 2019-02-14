//package com.journeyapps.barcodescanner;
package com.scrumptious6.xtract;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.zxing.client.android.R;
import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.CompoundBarcodeView;
import com.scrumptious6.xtract.DatabaseHandler;

import android.widget.Toast;

/**
 *
 */
public class CaptureActivity extends AppCompatActivity {
    private CaptureManager capture;
    private CompoundBarcodeView barcodeScannerView;
    private Button manual;

    DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.scrumptious6.xtract.R.layout.zxing_capture);
        // components from main.xml
        manual = (Button) this.findViewById(com.scrumptious6.xtract.R.id.manual);
        // resultText = (TextView) findViewById(R.id.result);
        manual.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Hello Javatpoint",Toast.LENGTH_SHORT).show();
                showInputDialog();
            }
        });

        barcodeScannerView = initializeContent();

        capture = new CaptureManager(this, barcodeScannerView);
        capture.initializeFromIntent(getIntent(), savedInstanceState);
        capture.decode();

        //db = new DatabaseHandler(this);
        //db.insertScannedItem();


    }

        protected void showInputDialog() {

            // get prompts.xml view
            LayoutInflater layoutInflater = LayoutInflater.from(CaptureActivity.this);
            View promptView = layoutInflater.inflate(com.scrumptious6.xtract.R.layout.input_dialog, null);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(CaptureActivity.this);
            alertDialogBuilder.setView(promptView);

            final EditText editText = (EditText) promptView.findViewById(com.scrumptious6.xtract.R.id.edittext);
            // setup a dialog window
            alertDialogBuilder.setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                           // resultText.setText("Hello, " + editText.getText());
                        }
                    })
                    .setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

            // create an alert dialog
            AlertDialog alert = alertDialogBuilder.create();
            alert.show();
        }



    /**
     * Override to use a different layout.
     *
     * @return the CompoundBarcodeView
     */
    protected CompoundBarcodeView initializeContent() {
        setContentView(R.layout.zxing_capture);
        return (CompoundBarcodeView)findViewById(R.id.zxing_barcode_scanner);
    }

    @Override
    protected void onResume() {
        super.onResume();
        capture.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        capture.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        capture.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        capture.onSaveInstanceState(outState);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        capture.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return barcodeScannerView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }
}
