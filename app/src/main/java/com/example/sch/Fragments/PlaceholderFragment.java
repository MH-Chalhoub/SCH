package com.example.sch.Fragments;


import android.os.Bundle;
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
import com.example.sch.Databases.DBHandler;
import com.example.sch.Interfaces.OnItemClickListener;
import com.example.sch.R;
import com.example.sch.Substance;

import java.util.ArrayList;
import java.util.Date;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String ARG_SECTION_DATE = "section_date";
    private static Date date;

    ArrayList<Substance> subs;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static PlaceholderFragment newInstance(int sectionNumber,String date) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        args.putString(ARG_SECTION_DATE, date);
        fragment.setArguments(args);
        return fragment;
    }

    public PlaceholderFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        //final View rootView1 = inflater.inflate(R.layout.activity_main, container, false);
        TextView textView = (TextView) rootView.findViewById(R.id.section_label);
        //TextView day = (TextView) rootView1.findViewById(R.id.day);
        textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
        //day.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));

        RecyclerView rvsubs = (RecyclerView) rootView.findViewById(R.id.items_view);

        // Initialize contacts
        DBHandler helper = new DBHandler(getContext());
        subs = helper.getAllStatus(getArguments().getString(ARG_SECTION_DATE));
        // Create adapter passing in the sample user data
        SubsAdaptar adapter = new SubsAdaptar(rootView.getContext(), subs);
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

        System.out.println("On Create !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1");


        textView.setText(getArguments().getString(ARG_SECTION_DATE));
        return rootView;
    }
}
