package com.example.sch.Databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.sch.Entities.Patient;
import com.example.sch.R;
import com.example.sch.Substance;

import java.util.ArrayList;
import java.util.List;

public class DBHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 2;

    // Database Name
    private static final String DATABASE_NAME = "hemodialyseManager";

    // Tables names
    private static final String TABLE_PATIENTS = "PATIENT";
    private static final String TABLE_STATUS = "STATUS";
    private static final String TABLE_GONFLEMENT = "gonflement";
    private static final String TABLE_SUBSTANCE = "SUBSTANCE";
    private static final String TABLE_BUTTONS = "BUTTONS";

    // PATIENT Table Columns names
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
        /*String CREATE_PATIENTS_TABLE = "CREATE TABLE " + TABLE_PATIENTS + "("+ KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_AGE + " INTEGER," + KEY_NAME + " TEXT," + KEY_PH_NO + " TEXT," + KEY_BL_GR + " TEXT," + KEY_GENDER + " TEXT"+")";*/
        System.out.println("table created");
        String CREATE_PATIENTS_TABLE = "CREATE TABLE PATIENT(id INTEGER  PRIMARY KEY  , name TEXT, age INTEGER, blood_grp TEXT,gender TEXT, deleted INTEGER);";
        db.execSQL(CREATE_PATIENTS_TABLE);

        String CREATE_STATUS_TABLE = "CREATE TABLE STATUS(id INTEGER PRIMARY KEY   ,idpatient INTEGER, albumineDose INTEGER, creatinineDose INTEGER,potassiumDose INTEGER,gonflement TEXT,dialysatDose INTEGER, date TEXT);";
        db.execSQL(CREATE_STATUS_TABLE);

        String CREATE_GONFLEMENT_TABLE = "CREATE TABLE gonflement (id INTEGER PRIMARY KEY, indice INTEGER,  niveau TEXT);";
        db.execSQL(CREATE_GONFLEMENT_TABLE);

        String CREATE_SUBSTANCES_TABLE = "CREATE TABLE SUBSTANCE ( subst TEXT PRIMARY KEY,dosemin INTEGER,dosemax INTEGER );";
        db.execSQL(CREATE_SUBSTANCES_TABLE);

        String CREATE_BUTTONS_TABLE = "CREATE TABLE BUTTONS (id  INTEGER PRIMARY KEY , registerstatus TEXT , resetstatus TEXT );";
        db.execSQL(CREATE_BUTTONS_TABLE);
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
        System.out.println("inserted contact with id " + i);
        db.close(); // Closing database connection
        return i;
    }


    // Getting single contact
    public Patient getPatient(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_PATIENTS, new String[]{KEY_ID, KEY_AGE, KEY_NAME, KEY_PH_NO, KEY_BL_GR, KEY_GENDER},
                KEY_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor == null)
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
        String query = "SELECT * FROM " + TABLE_PATIENTS;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor == null)
            return null;

        List<Patient> patients = new ArrayList<Patient>();

        if (cursor.moveToFirst()) {
            do {
                Patient p = new Patient(Integer.parseInt(cursor.getString(0)), Integer.parseInt(cursor.getString(1)),
                        cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5));
                patients.add(p);
            } while (cursor.moveToNext());
        }

        db.close();

        return patients;
    }

    public void delete(int id) {
        SQLiteDatabase db = getWritableDatabase();

        db.delete(TABLE_PATIENTS, KEY_ID + "=?", new String[]{"" + id});

        db.close();
    }

    // Getting single contact
    public ArrayList<Substance> getAllStatus(String date) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_STATUS + " WHERE date LIKE '" + date + "%'";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor == null){
            System.out.println("ArrayList null null null");
            return null;
        }

        ArrayList<Substance> substances = new ArrayList<Substance>();

        String[] subs = {"albumine", "creatinine", "potassium"};
        String[] subsinfo = {
                "Albumin is a family of globular proteins, the most common of which are the serum albumins. All the proteins of the albumin family are water-soluble, moderately soluble in concentrated salt solutions, and experience heat denaturation.",
                "Creatinine is a breakdown product of creatine phosphate from muscle and protein metabolism. It is released at a constant rate by the body (depending on muscle mass).",
                "Potassium is a chemical element with the symbol K (from Neo-Latin kalium) and atomic number 19. Potassium is a silvery-white metal that is soft enough to be cut with a knife with little force."
        };
        int[] subsImage = {R.drawable.albumineimage, R.drawable.creatinineimage, R.drawable.potassiumimage};
        int[] bLogos = {R.drawable.albumine, R.drawable.creatinine, R.drawable.potassium};

        ArrayList<Double> albuminevalues = new ArrayList<>();
        ArrayList<Double> creatininevalues = new ArrayList<>();
        ArrayList<Double> potassiumvalues = new ArrayList<>();
        //System.out.println("getAllStatus" + date);
        if (cursor.moveToFirst()) {
            do {
                //System.out.println("ArrayList notnull notnull notnull " + cursor.getString(2) + ", " + cursor.getString(3) + ", " + cursor.getString(4));
                albuminevalues.add(Double.parseDouble(cursor.getString(2)));
                creatininevalues.add(Double.parseDouble(cursor.getString(3)));
                potassiumvalues.add(Double.parseDouble(cursor.getString(4)));
            } while (cursor.moveToNext());
        }
        int lastItem;
        lastItem = albuminevalues.size() != 0 ? albuminevalues.size()-1 : 0;
        Substance albumine;
        Substance creatinine;
        Substance potassium;
        if(lastItem !=0){
            albumine = new Substance(subs[0], subs[0], albuminevalues.get(lastItem), bLogos[0], subsinfo[0], subsImage[0], albuminevalues);
            creatinine = new Substance(subs[1], subs[1], creatininevalues.get(lastItem), bLogos[1], subsinfo[1], subsImage[1], creatininevalues);
            potassium = new Substance(subs[2], subs[2], potassiumvalues.get(lastItem), bLogos[2], subsinfo[2], subsImage[2], potassiumvalues);
        }
        else {
            //If there is no substance at the specific date
            albumine = new Substance(subs[0], subs[0], -1, bLogos[0], subsinfo[0], subsImage[0], new ArrayList<Double>());
            creatinine = new Substance(subs[1], subs[1], -1, bLogos[1], subsinfo[1], subsImage[1], new ArrayList<Double>());
            potassium = new Substance(subs[2], subs[2], -1, bLogos[2], subsinfo[2], subsImage[2], new ArrayList<Double>());
        }
        substances.add(albumine);
        substances.add(creatinine);
        substances.add(potassium);

        db.close();

        return substances;
    }
}