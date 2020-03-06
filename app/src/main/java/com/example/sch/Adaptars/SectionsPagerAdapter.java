package com.example.sch.Adaptars;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.widget.Toast;

import com.example.sch.Fragments.PlaceholderFragment;
import com.example.sch.Fragments.PlaceholderTodayFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {
    private int  diff = 0;
    private Context context;
    public SectionsPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        diff = position - Integer.MAX_VALUE/2;
        //Toast.makeText(context, "position = " + position + "diff = " + diff,
        //        Toast.LENGTH_LONG).show();
        Date dt = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(dt);
        c.add(Calendar.DATE, diff);
        dt = c.getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String strDate= formatter.format(dt);
        //Toast.makeText(context, "diff = " + diff,Toast.LENGTH_LONG).show();
        if(diff == 0)
            return PlaceholderTodayFragment.newInstance(position + 1,strDate);
        else
            return PlaceholderFragment.newInstance(position + 1,strDate);
    }

    @Override
    public int getCount() {
        // Show Integer.MAX_VALUE total pages.
        return Integer.MAX_VALUE;
    }

}
