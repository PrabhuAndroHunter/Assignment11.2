package com.assignment;

import android.content.ContentValues;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.assignment.database.DBHelper;
import com.assignment.utils.CommonUtilities;
import com.assignment.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private final String TAG = MainActivity.class.toString();
    List <String> dataList = new ArrayList <String>();

    DBHelper dbHelper;
    String[] product_names = new String[]{
            "Microprocessor", "Motherboard", "Memory", "Case", "Hard Drive", "CD-ROM", "Floppy Drive", "Read/Write CD-ROM", "Video Card", "Sound Card", "Keyboard", "Mouse", "Monitor", "Modem"
    };

    String[] ids = new String[]{"1234", "2345", "3456", "4567", "5678", "6789", "7890", "8901", "9012", "1010", "1212", "2121", "3434", "4343"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // init layout
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: ");
        // get DATABASE Helper instance
        dbHelper = CommonUtilities.getDBObject(this);
        int count = dbHelper.getFullCount(Constants.PRODUCT_RECORD, null);
        if (count == 0) {
            insertProductRecord(); // If there is not records in database insert new records
        }
        // get all data related to product
        dataList = dbHelper.getAllProduct();
        ArrayAdapter <String> adapter = new ArrayAdapter <String>
                (this, android.R.layout.select_dialog_item, dataList);
        //Getting the instance of AutoCompleteTextView
        AutoCompleteTextView actv = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
        actv.setThreshold(1);//will start working from first character
        actv.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
        actv.setTextColor(Color.BLACK);
    }

    // This will insert all records
    private void insertProductRecord() {
        Log.d(TAG, "insertProductRecord: ");
        for (int i = 0; i < product_names.length; i++) {
            ContentValues vals = new ContentValues();
            vals.put(Constants.ID, ids[i]);
            vals.put(Constants.PRODUCT_NAME, product_names[i]);
            dbHelper.insertContentVals(Constants.PRODUCT_RECORD, vals);
        }
    }
}