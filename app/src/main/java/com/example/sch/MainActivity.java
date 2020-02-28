package com.example.sch;

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

import com.example.sch.Adaptars.SubsAdaptar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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

    TextView day;
    Date date;
    SimpleDateFormat formatter;
    Calendar c;

    int lastPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mViewPager.setCurrentItem(Integer.MAX_VALUE/2);

        day = (TextView)findViewById(R.id.day);

        date = new Date();
        formatter = new SimpleDateFormat("dd/MM/yyyy");
        String strDate= formatter.format(date);

        day.setText(strDate);

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




    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        private int  diff = 0;
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            diff = position - Integer.MAX_VALUE/2;
            Toast.makeText(getApplicationContext(), "position = " + position + "diff = " + diff,
                    Toast.LENGTH_LONG).show();
            Date dt = new Date();
            Calendar c = Calendar.getInstance();
            c.setTime(dt);
            c.add(Calendar.DATE, diff);
            dt = c.getTime();
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            String strDate= formatter.format(dt);
            return PlaceholderFragment.newInstance(position + 1,strDate);
        }

        @Override
        public int getCount() {
            // Show 5 total pages.
            return Integer.MAX_VALUE;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
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
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            View rootView1 = inflater.inflate(R.layout.activity_main, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            TextView day = (TextView) rootView1.findViewById(R.id.day);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            day.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));

            RecyclerView rvsubs = (RecyclerView) rootView.findViewById(R.id.items_view);
            // Initialize contacts
            subs = Substance.createContactsList(4);
            // Create adapter passing in the sample user data
            SubsAdaptar adapter = new SubsAdaptar(rootView.getContext(), subs);
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
}
