package com.example.sch;

import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.sch.Databases.DBHandler;
import com.example.sch.Entities.Patient;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Random;

public class RegisterActivity extends AppCompatActivity {

    Button register;
    EditText name, bloodgrp, bday;
    RadioGroup gender;
    RadioButton male, female;
    TextInputLayout nameWrapper, bloodgpWrapper, birthWrapper;

    DBHandler helper;

    Patient patient;
    String genre = "male";
    ArrayList<String> blood_grps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        helper = new DBHandler(this);

        name = findViewById(R.id.name);
        bloodgrp = findViewById(R.id.bloodgp);
        bday = findViewById(R.id.birth);
        gender = findViewById(R.id.gender);

        register = findViewById(R.id.register);
        female = findViewById(R.id.female);
        male = findViewById(R.id.male);

        nameWrapper = findViewById(R.id.nameWrapper);
        bloodgpWrapper = findViewById(R.id.bloodgpWrapper);
        birthWrapper = findViewById(R.id.birthWrapper);

        blood_grps = new ArrayList<>(Arrays.asList("a+", "b+", "ab+", "o+", "a-", "b-", "ab-", "o-",
                "A+", "B+", "AB+", "O+", "A-", "B-", "AB-", "O-"));

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

                final String uname = name.getText().toString().trim();
                final String ubday = bday.getText().toString().trim();
                final String ubloodgrp = bloodgrp.getText().toString().trim();

                if (uname.isEmpty()) {
                    nameWrapper.setError("Enter Name");
                    nameWrapper.requestFocus();
                    return;
                }
                if (ubday.isEmpty()) {
                    birthWrapper.setError("Enter age");
                    birthWrapper.requestFocus();
                    return;
                }
                if (ubloodgrp.isEmpty()) {
                    bloodgpWrapper.setError("Enter blood groupe");
                    bloodgpWrapper.requestFocus();
                    return;
                }
                if (!blood_grps.contains(ubloodgrp)) {
                    bloodgpWrapper.setError("Wrong blood group choose from {a,b,ab,o}{+,-}");
                    bloodgpWrapper.requestFocus();
                    return;
                }
                patient = new Patient(uname, Integer.parseInt(ubday), ubloodgrp, genre, 0);
                long id = helper.addPatient(patient);
                addRandomValuesToStatus(id);

                helper.changeButtonStatus("Disable", "Enable");

                register.setEnabled(false);

                finish();
            }
        });
    }

    public void addRandomValuesToStatus(long id) {
        Calendar calendar = Calendar.getInstance();
        DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy");

        for (int day = 0; day < 5; day++) {
            String formatedDate = df.format(calendar.getTime());
            for (int i = 0; i < 20; i++) {
                float albumin = GenerateValue((float)15,(float)1);
                float creatinine = GenerateValue((float)20,(float)2);
                float potassium = GenerateValue((float)15,(float)1);
                String gonflement = generateGonflement();
                float dialysat = GenerateValue((float)22,(float)15);
                helper.addStatus((int) id, albumin, creatinine, potassium, gonflement, dialysat, formatedDate);
            }
            calendar.add(Calendar.DATE, -1);
        }
    }

    //fonction to generate random concetration of albumine in patient blood
    private float GenerateValue(Float max, Float min) {

        Random rand = new Random();
        float value = (float) (min + (float) Math.round(Math.random() * (max - min))) + rand.nextFloat();

        return value;

    }
    private String generateGonflement(){
        float min = (float) 0.5;
        float max = 3;
        String status;
        float gonflement = GenerateValue(max, min);
        if (gonflement < 2) {
            if (gonflement > 1) {
                status = "inflated";
                //Toast.makeText(context, "Kidneys are inflated", Toast.LENGTH_LONG).show();
            } else {
                status = "severly inflated";
                //Toast.makeText(context, "Kidneys are severly inflated", Toast.LENGTH_LONG).show();
            }
        }
        else
            status = "normal";
        return status;
    }
}
