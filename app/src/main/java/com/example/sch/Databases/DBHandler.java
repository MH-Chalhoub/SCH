package com.example.sch.Databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.sch.Entities.Patient;

import java.util.ArrayList;
import java.util.List;

public class DBHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 2;

    // Database Name
    private static final String DATABASE_NAME = "hemodialyseManager";

    // Contacts table name
    private static final String TABLE_PATIENTS = "PATIENT";
    private static final String TABLE_SENSORS = "PATIENT";
    private static final String TABLE_GONFLEMENT = "GONFLEMENT";
    private static final String TABLE_BUTTONS = "BUTTONS";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_AGE = "age";
    private static final String KEY_NAME = "name";
    private static final String KEY_PH_NO = "phone_number";
    private static final String KEY_BL_GR = "blood_groupe";
    private static final String KEY_GENDER = "gender";

    public DBHandler(Context c) {

        super(c, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        String CREATE_PATIENTS_TABLE = "CREATE TABLE " + TABLE_PATIENTS + "("+ KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_AGE + " INTEGER," + KEY_NAME + " TEXT," + KEY_PH_NO + " TEXT," + KEY_BL_GR + " TEXT," + KEY_GENDER + " TEXT"+")";
        db.execSQL(CREATE_PATIENTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public long addPatient(Patient c) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_AGE, c.getAge());
        values.put(KEY_NAME, c.getName());
        values.put(KEY_PH_NO, c.getPhone_number());
        values.put(KEY_BL_GR, c.getBlood_groupe());
        values.put(KEY_GENDER, c.getGender());

        // Inserting Row
        long i = db.insert(TABLE_PATIENTS, null, values);
        System.out.println("inserted contact with id "  + i);
        db.close(); // Closing database connection
        return i;
    }


    // Getting single contact
    public Patient getPatient(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_PATIENTS, new String[] { KEY_ID, KEY_AGE, KEY_NAME, KEY_PH_NO, KEY_BL_GR, KEY_GENDER },
                KEY_ID + "=?", new String[] { String.valueOf(id) }, null, null, null, null);

        if(cursor == null)
            return null;

        cursor.moveToFirst();

        Patient patient = new Patient(Integer.parseInt(cursor.getString(0)), Integer.parseInt(cursor.getString(1)),
                cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5));

        db.close();

        return patient;
    }

    // Getting single contact
    public List<Patient> getAllPatientS() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query="SELECT * FROM "+TABLE_PATIENTS;
        Cursor cursor = db.rawQuery(query, null);

        if(cursor == null)
            return null;

        List<Patient> patients = new ArrayList<Patient>();

        if(cursor.moveToFirst()){
            do{
                Patient p = new Patient(Integer.parseInt(cursor.getString(0)), Integer.parseInt(cursor.getString(1)),
                        cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5));
                patients.add(p);
            }while(cursor.moveToNext());
        }

        db.close();

        return patients;
    }

    public void delete(int id)
    {
        SQLiteDatabase db = getWritableDatabase();

        db.delete(TABLE_PATIENTS, KEY_ID + "=?", new String[]{""+id});

        db.close();
    }
}