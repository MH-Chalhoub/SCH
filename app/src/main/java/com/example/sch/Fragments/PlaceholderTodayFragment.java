package com.example.sch.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sch.Adaptars.SubsAdaptar;
import com.example.sch.Interfaces.OnItemClickListener;
import com.example.sch.MainActivity;
import com.example.sch.R;
import com.example.sch.Substance;

import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderTodayFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String ARG_SECTION_DATE = "section_date";
    private static Date date;

    ArrayList<Substance> subs;

    long startTime = 0;
    Timer timer;
    TimerTask timerTask;

    //we are going to use a handler to be able to run in our TimerTask
    final Handler handler = new Handler();

    float ALbumineValue;

    SubsAdaptar adapter;
    RecyclerView rvsubs;
    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static PlaceholderTodayFragment newInstance(int sectionNumber,String date) {
        PlaceholderTodayFragment fragment = new PlaceholderTodayFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        args.putString(ARG_SECTION_DATE, date);
        fragment.setArguments(args);
        return fragment;
    }

    public PlaceholderTodayFragment() {
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        stoptimertask();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        System.out.println("onAttach PlaceholderTodayFragment");
        startTimer();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        //final View rootView1 = inflater.inflate(R.layout.activity_main, container, false);
        TextView textView = (TextView) rootView.findViewById(R.id.section_label);
        //TextView day = (TextView) rootView1.findViewById(R.id.day);
        textView.setText("Today");
        //day.setText("Today");

        rvsubs = (RecyclerView) rootView.findViewById(R.id.items_view);

        // Initialize contacts
        subs = Substance.createContactsList(2);
        // Create adapter passing in the sample user data
        adapter = new SubsAdaptar(rootView.getContext(), subs);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                //Toast.makeText(getContext(),"onClickListener", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onItemLongClick(int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                LayoutInflater inflater = getLayoutInflater();
                View dialogLayout = inflater.inflate(R.layout.info_alert_dialog, null);
                TextView alertInfo = (TextView) dialogLayout.findViewById(R.id.alertInfo);
                ImageView alertImage = (ImageView) dialogLayout.findViewById(R.id.alertImage);
                alertInfo.setText(subs.get(position).getSub_info());
                alertImage.setImageResource(subs.get(position).getImage());
                builder.setCancelable(true);
                builder.setView(dialogLayout);
                builder.show();
            }
        });
        // Attach the adapter to the recyclerview to populate items
        rvsubs.setAdapter(adapter);
        // Set layout manager to position the items
        rvsubs.setLayoutManager(new LinearLayoutManager(rootView.getContext()));
        // That's all!

        System.out.println("On Create !!!!!!!!!!!!!!!!!!!!!!");

        return rootView;
    }
    public void startTimer() {
        //set a new Timer
        timer = new Timer();

        //initialize the TimerTask's job
        initializeTimerTask();

        //schedule the timer, after the first 5000ms the TimerTask will run every 10000ms
        timer.schedule(timerTask, 0,250);
    }

    public void stoptimertask() {
        //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public void initializeTimerTask() {

        timerTask = new TimerTask() {
            public void run() {

                //use a handler to run a toast that shows the current timestamp
                handler.post(new Runnable() {
                    public void run() {
                        //get the current timeStamp
                        ALbumineValue = GenerateALbumineValue();
                        //Toast.makeText( getContext() , "albumine value :"+String.valueOf(ALbumineValue) ,Toast.LENGTH_SHORT).show();
                        subs.get(0).setValue(ALbumineValue);
                        subs.get(0).setLatestValues(ALbumineValue);
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        };
    }
    //fonction to generate random concetration of albumine in patient blood
    private float GenerateALbumineValue(){
        return (float) (2.5+(float)Math.round(Math.random() * (7.5 - 2.5)));
    }

}
