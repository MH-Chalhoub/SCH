package com.example.sch;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.sch.Databases.DBHandler;

public class RegisterActivity extends AppCompatActivity {

    ImageButton backhome;
    Button register, reset;
    EditText name, bloodgrp, bday;
    RadioGroup gender;
    RadioButton male, female;
    String genre, register_status, reset_status;

    SQLiteDatabase dbWritable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        DBHandler helper = new DBHandler(this);

        final SQLiteDatabase dbReadable = helper.getReadableDatabase();
        dbWritable = helper.getWritableDatabase();

        name = findViewById(R.id.name);
        bloodgrp = findViewById(R.id.bloodgp);
        bday = findViewById(R.id.birth);
        gender = findViewById(R.id.gender);

        register = findViewById(R.id.register);
        reset = findViewById(R.id.reset);
        female = findViewById(R.id.female);
        male = findViewById(R.id.male);

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
            register.setEnabled(true);
        else
            register.setEnabled(false);


        gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (male.isChecked())
                    genre = "male";
                else if (female.isChecked())
                    genre = "female";
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ContentValues values = new ContentValues();
                values.put("name", String.valueOf(name.getText()));
                values.put("age", Integer.parseInt(String.valueOf(bday.getText())));
                values.put("blood_grp", String.valueOf(bloodgrp.getText()));
                values.put("gender", genre);
                values.put("deleted", 0);
                long result = dbWritable.insert("PATIENT", null, values);
                values.clear();
                values.put("registerstatus", "Disable");
                values.put("resetstatus", "Enable");
                dbWritable.update("BUTTONS", values, "id=1", null);
                /*if (result>0)
                    Toast.makeText(v.getContext(),"register done",Toast.LENGTH_LONG).show();
                Toast.makeText(v.getContext(),"end registration  "+String.valueOf(result),Toast.LENGTH_LONG).show();*/

                register.setEnabled(false);
                reset.setEnabled(true);


                Intent i = new Intent(v.getContext(), HomepageActivity.class);
                i.putExtra("checking", "on");
                startActivity(i);
            }
        });
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(v.getContext())
                        .setTitle("Delete patient")
                        .setMessage("Are you sure you want to delete this patient?!")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ContentValues values = new ContentValues();
                                values.put("deleted", 1);
                                dbWritable.update("PATIENT", values, "deleted=0", null);
                                values.clear();
                                values.put("registerstatus", "Enable");
                                values.put("resetstatus", "Disable");
                                dbWritable.update("BUTTONS", values, "id=1", null);
                                Intent i = new Intent(getApplicationContext(), HomepageActivity.class);
                                i.putExtra("checking", "on");
                                startActivity(i);


                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });
    }
}
