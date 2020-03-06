package com.example.sch;

import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
    Timer timer;
    TimerTask timerTask;

    //we are going to use a handler to be able to run in our TimerTask
    final Handler handler = new Handler();

    int lastPosition = 0;

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
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                /*Patient p = new Patient(15, "Mohamad", "76123456", "o+", "Male");
                long i = dbHandler.addPatient(p);
                Patient p1 = dbHandler.getPatient((int) i);
                Toast.makeText(MainActivity.this, p1.toString(), Toast.LENGTH_LONG).show();*/
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
                if(i == Integer.MAX_VALUE/2){
                    System.out.println("Today");
                    day.setText("Today");
                }
                else if (lastPosition > i) {
                    System.out.println("Left");
                    c.add(Calendar.DATE, -1);
                    date = c.getTime();
                    String strDate= formatter.format(date);
                    day.setText(strDate);
                }else if (lastPosition < i) {
                    System.out.println("Right");
                    c.add(Calendar.DATE, 1);
                    date = c.getTime();
                    String strDate= formatter.format(date);
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
        System.out.println("onDestroy");
    }
}
