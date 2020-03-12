package com.example.sch;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sch.Databases.DBHandler;
import com.example.sch.Entities.Patient;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class HomepageActivity extends AppCompatActivity {

    ImageButton dosage, repport;
    ImageButton regstration, reset;
    TextView patient;
    TextClock textClock1,textClock2;
    RelativeLayout registerLayout, resetLayout, dosageLayout, repportLayout;

    DBHandler helper;

    Patient p;
    int currentPatientId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        regstration = findViewById(R.id.register);
        reset = findViewById(R.id.reset);
        patient = findViewById(R.id.user);
        dosage = findViewById(R.id.dosage);
        repport = findViewById(R.id.repport);
        textClock1 = findViewById(R.id.textClock1);
        textClock2 = findViewById(R.id.textClock2);

        registerLayout = findViewById(R.id.registerLayout);
        resetLayout = findViewById(R.id.resetLayout);
        dosageLayout = findViewById(R.id.dosageLayout);
        repportLayout = findViewById(R.id.repportLayout);

        //reset.setEnabled(false);
        Typeface type = Typeface.createFromAsset(getAssets(),"alarmclock.ttf");
        textClock1.setTypeface(type);
        textClock2.setTypeface(type);

        helper = new DBHandler(this);
        if(!helper.isButtonInitialized())
            helper.changeButtonStatus("Enable", "Disable");

        initialise();

        currentPatientId = helper.getCurrentPatientId();
        if(currentPatientId != -1){
            p = helper.getPatient(currentPatientId);
            patient.setText("Hello " +p.getName() + "|Blood Gp (" + p.getBlood_groupe() + ")|Age : " + p.getAge());
        }

        // buttons listener
        dosage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), MainActivity.class);
                startActivity(i);
            }
        });
        repport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), TableActivity.class);
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
                new AlertDialog.Builder(v.getContext())
                        .setTitle("DELETE PATIENT")
                        .setMessage("Are you sure you want to delete this patient?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                helper.disablePatientData();

                                helper.changeButtonStatus("Enable", "Disable");
                                ButtonStatus();
                                setDosageReportVisibility();
                                patient.setText("Please Register First");
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });
        reset.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                new AlertDialog.Builder(v.getContext())
                        .setTitle("DELETE PATIENT STATUS")
                        .setMessage("Are you sure you want to delete this patient STATUS?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                helper.deleteAllStatus();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        ButtonStatus();
        currentPatientId = helper.getCurrentPatientId();
        if(currentPatientId != -1){
            p = helper.getPatient(currentPatientId);
            patient.setText("Hello " +p.getName() + "|Blood Gp (" + p.getBlood_groupe() + ")|Age : " + p.getAge());
        }
        setDosageReportVisibility();
    }
    public void setDosageReportVisibility(){
        int currentPatientId = helper.getCurrentPatientId();
        if(currentPatientId == -1){
            dosageLayout.setVisibility(View.GONE);
            repportLayout.setVisibility(View.GONE);
        }
        else{
            dosageLayout.setVisibility(View.VISIBLE);
            repportLayout.setVisibility(View.VISIBLE);
        }
    }

    public void initialise() {
        DBHandler helper = new DBHandler(this);

        helper.initialiseGonflement(1, 1, "normal");
        helper.initialiseGonflement(2, 2, "inflated");
        helper.initialiseGonflement(3, 3, "severly inflated");

        helper.initialiseSubstance("Albumine", 3, 5);
        helper.initialiseSubstance("Creatinine", 4, 10);
        helper.initialiseSubstance("Potassium", 3, 5);

        ButtonStatus();
    }

    private void ButtonStatus() {

        if (helper.isResetEnabled())
            resetLayout.setVisibility(View.VISIBLE);
        else
            resetLayout.setVisibility(View.GONE);
        if (helper.isRegisterEnabled())
            registerLayout.setVisibility(View.VISIBLE);
        else
            registerLayout.setVisibility(View.GONE);
    }
}
