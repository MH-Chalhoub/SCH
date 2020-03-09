package com.example.sch;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sch.Databases.DBHandler;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class HomepageActivity extends AppCompatActivity {

    SQLiteDatabase dbReadable;
    ImageButton dosage;
    ImageButton regstration, reset;
    TextView patient;
    String register_status, reset_status;
    TextClock textClock1,textClock2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        regstration = findViewById(R.id.register);
        reset = findViewById(R.id.reset);
        patient = findViewById(R.id.user);
        dosage = findViewById(R.id.dosage);
        textClock1 = findViewById(R.id.textClock1);
        textClock2 = findViewById(R.id.textClock2);

        reset.setEnabled(false);
        Typeface type = Typeface.createFromAsset(getAssets(),"alarmclock.ttf");
        textClock1.setTypeface(type);
        textClock2.setTypeface(type);

        String output = null;
        DBHandler helper = new DBHandler(this);
        dbReadable = helper.getReadableDatabase();
        SQLiteDatabase dbWritable = helper.getWritableDatabase();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabhome);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                initialise();
            }
        });
        //initialiser les valeurs fixés

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Cursor cursor = dbReadable.rawQuery("select * from PATIENT where deleted=0 ", null);
        if (cursor.moveToFirst()) {
            do {
                patient.setText("Hello " + String.valueOf(cursor.getString(cursor.getColumnIndex("name"))).toUpperCase());
            } while (cursor.moveToNext());
        }


        // buttons listener
        dosage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(v.getContext(), MainActivity.class);
                startActivity(i);
            }
        });
        regstration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), RegisterActivity.class);

                startActivity(i);
            }
        });
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), RegisterActivity.class);

                startActivity(i);
            }
        });
    }

    public void initialise() {
        DBHandler helper = new DBHandler(this);
        SQLiteDatabase dbWritable = helper.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("id", 1);
        values.put("indice", 3);
        values.put("niveau", "normal");
        long result = result = dbWritable.insert("gonflement", null, values);
        values.put("id", 2);
        values.put("indice", 2);
        values.put("niveau", "inflated");
        result = dbWritable.insert("gonflement", null, values);
        values.put("id", 3);
        values.put("indice", 1);
        values.put("niveau", "severly inflated");

        result = dbWritable.insert("gonflement", null, values);

        /*if(result<0){
            Toast.makeText(this,"value is"+String.valueOf(result),Toast.LENGTH_LONG).show();

        }
        else  Toast.makeText(this,"insert succes. "+String.valueOf(result),Toast.LENGTH_LONG).show();*/
        ContentValues values2 = new ContentValues();

        values2.put("subst", "Albumine");
        values2.put("dosemin", 3);
        values2.put("dosemax", 5);
        result = dbWritable.insert("SUBSTANCE", null, values2);
        values2.put("subst", "Creatinine");
        values2.put("dosemin", 4);
        values2.put("dosemax", 10);
        result = dbWritable.insert("SUBSTANCE", null, values2);
        values2.put("subst", "Potassium");
        values2.put("dosemin", 3);
        values2.put("dosemax", 5);
        result = dbWritable.insert("SUBSTANCE", null, values2);

        /*if(result<0){
            Toast.makeText(this,"no substance inserted",Toast.LENGTH_LONG).show();

        }
        else
            Toast.makeText(this,"insert succes. "+String.valueOf(result),Toast.LENGTH_LONG).show();*/

        //checking if any patient is registered
        ButtonStatus();
    }

    private void ButtonStatus() {
        DBHandler helper = new DBHandler(this);
        SQLiteDatabase dbWritable = helper.getWritableDatabase();
        //buttons stats
        ContentValues values = new ContentValues();
        values.put("id", 1);
        values.put("registerstatus", "Enable");
        values.put("resetstatus", "Diasble");
        long result = dbWritable.insert("BUTTONS", null, values);

        Cursor cursor = dbReadable.rawQuery("select * from BUTTONS where id=1", null);
        if (cursor.moveToFirst()) {
            do {
                //Toast.makeText(this,"ouput : "+cursor.getString(cursor.getColumnIndex("registerstatus")),Toast.LENGTH_LONG).show();
                register_status = String.valueOf(cursor.getString(cursor.getColumnIndex("registerstatus")));
                reset_status = String.valueOf(cursor.getString(cursor.getColumnIndex("resetstatus")));
//            register_status="Enable";
            } while (cursor.moveToNext());
        } else
            Toast.makeText(this, "5onfooshari :", Toast.LENGTH_LONG).show();
        if (reset_status.equals("Enable"))
            reset.setEnabled(true);
        else
            reset.setEnabled(false);
        if (register_status.equals("Enable"))
            regstration.setEnabled(true);
        else
            regstration.setEnabled(false);
    }
}
