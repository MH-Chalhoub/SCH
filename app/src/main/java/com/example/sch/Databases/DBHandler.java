package com.example.sch.Databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.sch.Entities.Patient;
import com.example.sch.R;
import com.example.sch.Substance;

import java.io.Console;
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
    private static final String KEY_BL_GR = "blood_grp";
    private static final String KEY_GENDER = "gender";
    private static final String KEY_DELETED = "deleted";

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

    public long addPatient(Patient p) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, p.getName());
        values.put(KEY_AGE, p.getAge());
        values.put(KEY_BL_GR, p.getBlood_groupe());
        values.put(KEY_GENDER, p.getGender());
        values.put(KEY_DELETED, p.getDeleted());

        // Inserting Row
        long id = db.insert(TABLE_PATIENTS, null, values);
        System.out.println("Inserted Patient with id " + id);
        db.close(); // Closing database connection
        return id;
    }


    // Getting single contact
    public Patient getPatient(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_PATIENTS, new String[]{KEY_ID, KEY_NAME, KEY_AGE, KEY_BL_GR, KEY_GENDER, KEY_DELETED},
                KEY_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor == null)
            return null;

        cursor.moveToFirst();

        Patient patient = new Patient(Integer.parseInt(cursor.getString(0)), cursor.getString(1),
                Integer.parseInt(cursor.getString(2)), cursor.getString(3), cursor.getString(4), Integer.parseInt(cursor.getString(5)));

        db.close();

        return patient;
    }

    public int getCurrentPatientId() {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("select * from PATIENT where deleted=0 ", null);
        int patientId;

        if(cursor.moveToFirst()){
            patientId = Integer.parseInt(cursor.getString(cursor.getColumnIndex("id")));
        }
        else
            return -1;
        db.close();

        return patientId;
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
                Patient p = new Patient(Integer.parseInt(cursor.getString(0)), cursor.getString(1),
                        Integer.parseInt(cursor.getString(2)), cursor.getString(3), cursor.getString(4), Integer.parseInt(cursor.getString(5)));
                patients.add(p);
            } while (cursor.moveToNext());
        }

        db.close();

        return patients;
    }

    public void deletePatient(int id) {
        SQLiteDatabase db = getWritableDatabase();

        db.delete(TABLE_PATIENTS, KEY_ID + "=?", new String[]{"" + id});

        db.close();
    }

    // Getting all Status
    public ArrayList<Substance> getAllStatus(String date) {
        String query = "SELECT * FROM " + TABLE_STATUS + " WHERE date LIKE '" + date + "%' AND idpatient=" + getCurrentPatientId()+ ";";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor == null) {
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
        ArrayList<String> dates = new ArrayList<>();
        //System.out.println("getAllStatus" + date);
        if (cursor.moveToFirst()) {
            do {
                //System.out.println("ArrayList notnull notnull notnull " + cursor.getString(2) + ", " + cursor.getString(3) + ", " + cursor.getString(4));
                albuminevalues.add(Double.parseDouble(cursor.getString(2)));
                creatininevalues.add(Double.parseDouble(cursor.getString(3)));
                potassiumvalues.add(Double.parseDouble(cursor.getString(4)));
                dates.add(cursor.getString(7));
            } while (cursor.moveToNext());
        }
        int lastItem;
        lastItem = albuminevalues.size() != 0 ? albuminevalues.size() - 1 : 0;
        Substance albumine;
        Substance creatinine;
        Substance potassium;
        if (lastItem != 0) {
            albumine = new Substance(subs[0], subs[0], albuminevalues.get(lastItem), bLogos[0], subsinfo[0], subsImage[0], albuminevalues, dates);
            creatinine = new Substance(subs[1], subs[1], creatininevalues.get(lastItem), bLogos[1], subsinfo[1], subsImage[1], creatininevalues, dates);
            potassium = new Substance(subs[2], subs[2], potassiumvalues.get(lastItem), bLogos[2], subsinfo[2], subsImage[2], potassiumvalues, dates);
        } else {
            //If there is no substance at the specific date
            albumine = new Substance(subs[0], subs[0], -1, bLogos[0], subsinfo[0], subsImage[0], new ArrayList<Double>(), new ArrayList<String>());
            creatinine = new Substance(subs[1], subs[1], -1, bLogos[1], subsinfo[1], subsImage[1], new ArrayList<Double>(), new ArrayList<String>());
            potassium = new Substance(subs[2], subs[2], -1, bLogos[2], subsinfo[2], subsImage[2], new ArrayList<Double>(), new ArrayList<String>());
        }
        substances.add(albumine);
        substances.add(creatinine);
        substances.add(potassium);

        db.close();

        return substances;
    }

    public long changeButtonStatus(String rgister, String reset) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("registerstatus", rgister);
        values.put("resetstatus", reset);

        if(isButtonInitialized()){
            long id = db.update(TABLE_BUTTONS, values, "id=1", null);
            System.out.println("Updated BUTTONS with id " + id);
            db.close(); // Closing database connection
            return id;
        }
        else {
            long id = db.insert(TABLE_BUTTONS, null, values);
            System.out.println("Inserted BUTTONS with id " + id);
            db.close(); // Closing database connection
            return id;
        }
    }

    public boolean isButtonInitialized() {
        SQLiteDatabase db = this.getReadableDatabase();
        String DB_query = "SELECT * FROM " + TABLE_BUTTONS + " WHERE id=1;";
        Cursor DB_cursor = db.rawQuery(DB_query, null);

        if (DB_cursor.moveToFirst()) {
            DB_cursor.close();
            return true;
        } else {
            DB_cursor.close();
            return false;
        }
    }
    public boolean isRegisterEnabled(){
        SQLiteDatabase db = this.getReadableDatabase();
        String DB_query = "SELECT * FROM " + TABLE_BUTTONS + " WHERE id=1;";
        Cursor cursor = db.rawQuery(DB_query, null);
        String register_status = "Enable";
        if (cursor.moveToFirst()) {
            do {
                register_status = String.valueOf(cursor.getString(cursor.getColumnIndex("registerstatus")));
            } while (cursor.moveToNext());
        }
        if (register_status.equals("Enable"))
            return true;
        else
            return false;
    }
    public boolean isResetEnabled(){
        SQLiteDatabase db = this.getReadableDatabase();
        String DB_query = "SELECT * FROM " + TABLE_BUTTONS + " WHERE id=1;";
        Cursor cursor = db.rawQuery(DB_query, null);
        String reset_status = "Enable";
        if (cursor.moveToFirst()) {
            do {
                reset_status = String.valueOf(cursor.getString(cursor.getColumnIndex("resetstatus")));
            } while (cursor.moveToNext());
        }
        if (reset_status.equals("Enable"))
            return true;
        else
            return false;
    }
    public void disablePatientData(){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("deleted", 1);

        db.update("PATIENT", values, "deleted=0", null);

    }
    public long initialiseSubstance(String subName, int min, int max){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("subst", subName);
        values.put("dosemin", min);
        values.put("dosemax", max);

        if(!isSubstanceInitialized(subName)){
            long id = db.insert(TABLE_SUBSTANCE, null, values);
            System.out.println("Inserted SUBSTANCE : " + subName + " with id " + id);
            db.close(); // Closing database connection
            return id;
        }
        return -1;
    }
    public boolean isSubstanceInitialized(String sub) {
        SQLiteDatabase db = this.getReadableDatabase();
        String DB_query = "SELECT * FROM " + TABLE_SUBSTANCE + " WHERE subst='" + sub + "';";
        Cursor DB_cursor = db.rawQuery(DB_query, null);

        if (DB_cursor.moveToFirst()) {
            DB_cursor.close();
            return true;
        } else {
            DB_cursor.close();
            return false;
        }
    }

    public long initialiseGonflement(int id, int indice, String niveau){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("id", id);
        values.put("indice", indice);
        values.put("niveau", niveau);

        if(!isGonflementInitialized(id)){
            long i = db.insert(TABLE_GONFLEMENT, null, values);
            System.out.println("Inserted GONFLEMENT with id " + i);
            db.close(); // Closing database connection
            return i;
        }
        return -1;
    }
    public boolean isGonflementInitialized(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String DB_query = "SELECT * FROM " + TABLE_GONFLEMENT + " WHERE id=" + id + ";";
        Cursor DB_cursor = db.rawQuery(DB_query, null);

        if (DB_cursor.moveToFirst()) {
            DB_cursor.close();
            return true;
        } else {
            DB_cursor.close();
            return false;
        }
    }
}