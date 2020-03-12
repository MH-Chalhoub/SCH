package com.example.sch;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sch.Adaptars.SectionsPagerAdapter;
import com.example.sch.Adaptars.SubsAdaptar;
import com.example.sch.Databases.DBHandler;
import com.example.sch.Entities.Patient;
import com.example.sch.Fragments.PlaceholderFragment;
import com.example.sch.Interfaces.OnItemClickListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import static java.lang.Integer.MAX_VALUE;

public class MainActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private DBHandler dbHandler;

    TextView day;
    Date date;
    SimpleDateFormat formatter;
    Calendar c;

    long startTime = 0;
    Timer timer, time_to_check;
    TimerTask timerTask, timertocheckTask;
    double av1, av2, av3; //percentage of additional  substance

    //we are going to use a handler to be able to run in our TimerTask
    final Handler handler = new Handler();

    int lastPosition = 0;


    float albumine, creatinine, potassium, gonflement; //random values
    float min, max; //min and max for each substance % select from database
    float value, av4;    //value need in random fction , av4 is average of deviation of gonflement
    static float dialysat;   //dose of dialysat required for dialyse
    static int time_required;    //time of dialyse required
    int test = 0;     //test=1 if critical states
    float dbdialysat;
    int patientId;
    String status;

    SQLiteDatabase dbReadable;
    SQLiteDatabase dbWritable;
    Context context;
    ContentValues values;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        dbHandler = new DBHandler(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), getApplicationContext());
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "For more info about each substance long click on it", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mViewPager.setCurrentItem(MAX_VALUE/2);

        day = (TextView)findViewById(R.id.day);

        date = new Date();
        formatter = new SimpleDateFormat("dd/MM/yyyy");
        String strDate= formatter.format(date);

        day.setText("Today");

        c = Calendar.getInstance();
        c.setTime(date);

        lastPosition = mViewPager.getCurrentItem();

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                if (lastPosition > i) {
                    System.out.println("Left");
                    c.add(Calendar.DATE, -1);
                    date = c.getTime();
                    String strDate= formatter.format(date);
                    if(i == Integer.MAX_VALUE/2)
                        day.setText("Today");
                    else
                        day.setText(strDate);
                }else if (lastPosition < i) {
                    System.out.println("Right");
                    c.add(Calendar.DATE, 1);
                    date = c.getTime();
                    String strDate= formatter.format(date);
                    if(i == Integer.MAX_VALUE/2)
                        day.setText("Today");
                    else
                        day.setText(strDate);
                }
                lastPosition = i;
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        ImageView action_forword = (ImageView)findViewById(R.id.action_forword);
        action_forword.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                mViewPager.setCurrentItem(getItem(+1), true); //getItem(-1) for previous

            }
        });

        ImageView action_backword = (ImageView)findViewById(R.id.action_backword);
        action_backword.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                mViewPager.setCurrentItem(getItem(-1), true); //getItem(-1) for previous
            }
        });


        context = this.getApplicationContext();
        DBHandler helper = new DBHandler(this);
        dbReadable = helper.getReadableDatabase();
        dbWritable = helper.getWritableDatabase();
        values = new ContentValues();
        Cursor cursor = dbReadable.rawQuery("select * from PATIENT where deleted=0 ", null);
        if (cursor.moveToFirst()) {
            do {
                patientId = Integer.parseInt(cursor.getString(cursor.getColumnIndex("id")));
            } while (cursor.moveToNext());
        }
        startTimer();
    }

    private int getItem(int i) {
        return mViewPager.getCurrentItem() + i;
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        System.out.println("onAttachFragment");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //timer creation,initializing and listener
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        System.out.println("onPause");
    }
    @Override
    protected void onStop() {
        super.onStop();
        System.out.println("onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stoptimertask();
        System.out.println("onDestroy");
    }
    public void startTimer() {
        //set a new Timer

        timer = new Timer();
        time_to_check = new Timer();

        //initialize the TimerTask's job
        initializeTimerTask();

        //schedule the timer, after the first 5000ms the TimerTask will run every 10000ms
        timer.schedule(timerTask, 0, 1000);
        time_to_check.schedule(timertocheckTask, 0, 30000);

    }

    public void stoptimertask() {
        //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (time_to_check != null) {
            time_to_check.cancel();
            time_to_check = null;
        }
    }

    public void initializeTimerTask() {

        timerTask = new TimerTask() {
            public void run() {
                //use a handler to run a toast that shows the current timestamp
                handler.post(new Runnable() {
                    @TargetApi(Build.VERSION_CODES.O)
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    public void run() {
                        //get the current timeStamp
                        Testing();

                    }
                });

            }


        };
        timertocheckTask = new TimerTask() {
            public void run() {
                //use a handler to run a toast that shows the current timestamp
                handler.post(new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    public void run() {
                        //get the current timeStamp
                        value++;
                        notificationDialog();
                    }
                });

            }


        };
    }

    //fonction to generate random concetration of albumine in patient blood
    private float GenerateValue(Float max, Float min) {

        Random rand = new Random();
        value = (float) (min + (float) Math.round(Math.random() * (max - min))) + rand.nextFloat();

        return value;

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    void Testing() {
        Cursor cursor = dbReadable.rawQuery("select * from SUBSTANCE where subst='Albumine'", null);
        if (cursor.moveToFirst()) {
            do {
                min = cursor.getFloat(cursor.getColumnIndex("dosemin")) - 2;
                max = cursor.getFloat(cursor.getColumnIndex("dosemax")) + 10;
                albumine = GenerateValue(max, min);
                if (albumine > max - 10) {
                    test = 1;
                    //Toast.makeText(context, "albumine critical value", Toast.LENGTH_LONG).show();
                    av1 = (albumine - (max - 10)) * 100 / (max - 2);
                    //Toast.makeText(context, String.valueOf(av1), Toast.LENGTH_LONG).show();


                }

                //Toast.makeText(context, "albumine :" + albumine + "g/dl", Toast.LENGTH_LONG).show();
            } while (cursor.moveToNext());
        }
        values.put("idpatient", patientId);
        values.put("albumineDose", albumine);
        cursor = dbReadable.rawQuery("select * from SUBSTANCE where subst='Creatinine'", null);
        if (cursor.moveToFirst()) {
            do {
                min = cursor.getFloat(cursor.getColumnIndex("dosemin")) - 2;
                max = cursor.getFloat(cursor.getColumnIndex("dosemax")) + 10;
                creatinine = GenerateValue(max, min);
                if (creatinine > max - 10) {
                    test = 1;
                    av2 = (creatinine - (max - 10)) * 100 / (max - 10);
                    //Toast.makeText(context, "creatinine critical value", Toast.LENGTH_LONG).show();
                    //Toast.makeText(context, String.valueOf(av2), Toast.LENGTH_LONG).show();
                }
                //Toast.makeText(context, "creatinine :" + creatinine + "mg/l", Toast.LENGTH_LONG).show();
            } while (cursor.moveToNext());
        }
        values.put("creatinineDose", creatinine);
        cursor = dbReadable.rawQuery("select * from SUBSTANCE where subst='Potassium'", null);
        if (cursor.moveToFirst()) {
            do {
                min = cursor.getFloat(cursor.getColumnIndex("dosemin")) - 2;
                max = cursor.getFloat(cursor.getColumnIndex("dosemax")) + 10;
                potassium = GenerateValue(max, min);
                if (potassium > max - 10) {
                    test = 1;
                    av3 = (potassium - (max - 10)) * 100 / (max - 10);
                    //Toast.makeText(context, String.valueOf(av3), Toast.LENGTH_LONG).show();
                    //Toast.makeText(context, "potassium critical value", Toast.LENGTH_LONG).show();
                }
                //Toast.makeText(context, "potassium :" + potassium + "mmol/l", Toast.LENGTH_LONG).show();
            } while (cursor.moveToNext());
        }
        values.put("potassiumDose", potassium);

        min = (float) 0.5;
        max = 3;
        gonflement = GenerateValue(max, min);
        if (gonflement < 2) {
            test = 1;
            av4 = gonflement;
            if (gonflement > 1) {
                status = "inflated";
                //Toast.makeText(context, "Kidneys are inflated", Toast.LENGTH_LONG).show();
            } else {
                status = "severly inflated";
                //Toast.makeText(context, "Kidneys are severly inflated", Toast.LENGTH_LONG).show();
            }
        } else status = "normal";
        values.put("gonflement", status);
        //Toast.makeText(context, "gonflement :" + gonflement + "mm", Toast.LENGTH_LONG).show();
        if (test == 1) {
            dbdialysat = checkstatus(av1, av2, av3, av4);
            values.put("dialysatDose", dbdialysat);

        } else
            values.put("dialysatDose", "Null");
        DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm");
        String date = df.format(Calendar.getInstance().getTime());
        //System.out.println("MAIN : DATE" + date);
        values.put("date", date);
        long result = dbWritable.insert("STATUS", null, values);
        System.out.println("Insert sub for patient with id : " + patientId);
        //if (result < 0)
            //Toast.makeText(context, "No Inserted Values In Status !!!!!!!", Toast.LENGTH_LONG).show();


    }

    @TargetApi(Build.VERSION_CODES.O)
    @RequiresApi(api = Build.VERSION_CODES.O)
    float checkstatus(double albumine_avg, double creatinine_avg, double potassium_avg, float gonflement_avg) {
        time_required = 0;
        dialysat = 15;
        //Max dialysat dose : in case if the patient take dose above the Max dose it will affect his
        // kidney and in some cases he may die.
        float max_dialysat_dose=22;

        //Min dialysat dose : in case if the patient take dose below the Min dose it will affect the
        // operation of cleaning his blood and in some cases he may die.
        float min_dialysat_dose=15;

        //Max dialysat dose : in case if the patient take dose above the Max dose it will affect his
        // kidney and in some cases he may die.
        int max_time_required=20;

        //Min dialysat dose : in case if the patient take dose below the Min dose it will affect the
        // operation of cleaning his blood and in some cases he may die.
        int min_time_required=8;

        if (albumine_avg < 25) {
            dialysat += 2;
            time_required += 2;
        } else {
            dialysat += 5;
            time_required += 6;
        }
        if (creatinine_avg < 30) {
            dialysat += 3;
            time_required += 1;
        } else {
            dialysat += 10;
            time_required += 4;
        }

        if (potassium_avg < 10) {
            dialysat += 5;
            time_required += 4;
        } else {
            dialysat += 15;
            time_required += 12;
        }

        //in case if the dialysat is above the Max dialysat dose the dialysat is
        // reassigned to max_dialysat_dose
        if(dialysat>max_dialysat_dose)
            dialysat = max_dialysat_dose;
        //in case if the dialysat is below the Min dialysat dose the dialysat is
        // reassigned to min_dialysat_dose
        if(dialysat<min_dialysat_dose)
            dialysat = min_dialysat_dose;

        //in case if the time required is above the Max time required the time_required is
        // reassigned to max_time_required
        if(time_required<max_time_required)
            time_required = max_time_required;
        //in case if the time required is below the Min time required the time_required is
        // reassigned to min_time_required
        if(time_required<min_time_required)
            time_required = min_time_required;


        return dialysat;

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void notificationDialog() {
        NotificationManager notificationManager = (NotificationManager) getLayoutInflater().getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "tutorialspoint_01";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            @SuppressLint("WrongConstant") NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_MAX);
            // Configure the notification channel.
            notificationChannel.setDescription("Sample Channel description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.BLUE);
            notificationChannel.setVibrationPattern(new long[]{0, 50});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), NOTIFICATION_CHANNEL_ID);
        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.kidney_notification)
                .setTicker("Tutorialspoint")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("You have " + String.valueOf(time_required) + " Hour of Dialyse REQUIRED. And your Dialysat Quote = " + String.valueOf(dialysat) + " g/dL"))
                //.setPriority(Notification.PRIORITY_MAX)
                .setContentTitle("SCH Warning")
                .setContentInfo("Information");
        notificationManager.notify(1, notificationBuilder.build());
    }

}
