package com.example.sch;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.sch.Databases.DBHandler;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TableActivity extends AppCompatActivity {

    private TableLayout tableLayout;
    private int currentRowNumber = 0;

    private DBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);

        dbHandler = new DBHandler(this);
        tableLayout = (TableLayout)findViewById(R.id.tableLayout);
        ShowAll();
    }


    public void ShowAll()
    {
        tableLayout.removeAllViews();

        DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy");
        String date = df.format(Calendar.getInstance().getTime());
        List<Substance> substanceList = dbHandler.getAllStatus(date);

        int i = 0;
        for(; i < substanceList.size(); i++)
        {
            final TableRow tableRow = new TableRow(this);

            TextView textView = new TextView(this);
            textView.setText(substanceList.get(i).getSub_name());
            textView.setPadding(10, 15, 10, 10);
            textView.setTextSize(18);
            TextView textView1 = new TextView(this);
            textView1.setText(substanceList.get(i).getSub_name());
            textView1.setPadding(10, 15, 10, 10);
            textView1.setTextSize(18);

            tableRow.addView(textView);
            tableRow.addView(textView1);

            if(i%2 == 0)
                tableRow.setBackgroundColor(Color.rgb(186, 210, 224));

            tableLayout.addView(tableRow);
        }

        currentRowNumber = i;
    }

}
