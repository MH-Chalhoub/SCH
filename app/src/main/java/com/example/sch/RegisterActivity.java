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

import java.util.ArrayList;
import java.util.Arrays;

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

        blood_grps = new ArrayList<>(Arrays.asList("a+", "b+", "ab+", "o+", "a-", "b-", "ab-", "o-"));

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

                if(uname.isEmpty()){
                    nameWrapper.setError("Enter Name");
                    nameWrapper.requestFocus();
                    return;
                }
                if(ubday.isEmpty()){
                    birthWrapper.setError("Enter age");
                    birthWrapper.requestFocus();
                    return;
                }
                if(ubloodgrp.isEmpty()){
                    bloodgpWrapper.setError("Enter blood groupe");
                    bloodgpWrapper.requestFocus();
                    return;
                }
                if(!blood_grps.contains(ubloodgrp)){
                    bloodgpWrapper.setError("Wrong blood group choose from {a,b,ab,o}{+,-}");
                    bloodgpWrapper.requestFocus();
                    return;
                }
                patient = new Patient(uname, Integer.parseInt(ubday), ubloodgrp, genre, 0);
                helper.addPatient(patient);

                helper.changeButtonStatus("Disable", "Enable");

                register.setEnabled(false);

                finish();
            }
        });
    }
}
