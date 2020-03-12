package com.example.sch;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.sch.Databases.DBHandler;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TableActivity extends AppCompatActivity {

    private TableLayout tableLayout;
    TextView eText;
    private int currentRowNumber = 0;

    private DBHandler dbHandler;

    DatePickerDialog picker;
    List<Substance> substanceList;

    int lastItem;

    private LineChart mChart;

    Calendar calendar = Calendar.getInstance();
    DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);

        dbHandler = new DBHandler(this);
        tableLayout = (TableLayout) findViewById(R.id.tableLayout);
        eText = (TextView) findViewById(R.id.textDate);

        mChart = findViewById(R.id.chart);
        mChart.setTouchEnabled(true);
        mChart.setPinchZoom(true);
        mChart.setDescription(new Description());
        mChart.getDescription().setText("ZOOM IN OR SCROLL FOR A BETTER VIEW");

        eText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(TableActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                calendar.set(year, monthOfYear, dayOfMonth);
                                String formatedDate = df.format(calendar.getTime());
                                eText.setText(formatedDate);
                                substanceList.clear();
                                substanceList = dbHandler.getAllStatus(formatedDate);
                                lastItem = substanceList.get(0).getValues().size() != 0 ? substanceList.get(0).getValues().size() - 1 : 0;
                                if (lastItem != 0) {
                                    renderData(substanceList);
                                    ShowAll(substanceList);
                                } else {
                                    tableLayout.removeAllViews();
                                    mChart.clear();
                                }
                            }
                        }, year, month, day);
                picker.show();
            }
        });


        String date = df.format(calendar.getTime());
        substanceList = dbHandler.getAllStatus(date);
        lastItem = substanceList.get(0).getValues().size() != 0 ? substanceList.get(0).getValues().size() - 1 : 0;
        if (lastItem != 0) {
            renderData(substanceList);
            ShowAll(substanceList);
        }
        eText.setText(date);

        ImageView action_forword = (ImageView) findViewById(R.id.date_forword);
        action_forword.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                calendar.add(Calendar.DATE, +1);
                String formatedDate = df.format(calendar.getTime());
                eText.setText(formatedDate);
                substanceList.clear();
                substanceList = dbHandler.getAllStatus(formatedDate);
                lastItem = substanceList.get(0).getValues().size() != 0 ? substanceList.get(0).getValues().size() - 1 : 0;
                if (lastItem != 0) {
                    renderData(substanceList);
                    ShowAll(substanceList);
                } else {
                    tableLayout.removeAllViews();
                    mChart.clear();
                }
            }
        });

        ImageView action_backword = (ImageView) findViewById(R.id.date_backword);
        action_backword.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                calendar.add(Calendar.DATE, -1);
                String formatedDate = df.format(calendar.getTime());
                eText.setText(formatedDate);
                substanceList.clear();
                substanceList = dbHandler.getAllStatus(formatedDate);
                lastItem = substanceList.get(0).getValues().size() != 0 ? substanceList.get(0).getValues().size() - 1 : 0;
                if (lastItem != 0) {
                    renderData(substanceList);
                    ShowAll(substanceList);
                } else {
                    tableLayout.removeAllViews();
                    mChart.clear();
                }
            }
        });
    }


    public void ShowAll(List<Substance> substanceList) {
        tableLayout.removeAllViews();

        setTitle();

        int i = 0;
        int row = 0;
        for (; i < substanceList.size(); i++) {


            for (int j = 1; j <= substanceList.get(i).getDates().size(); j++) {
                final TableRow tableRow = new TableRow(this);
                TextView rowNb = new TextView(this);
                TextView subDate = new TextView(this);
                TextView subName = new TextView(this);
                TextView subValue = new TextView(this);

                rowNb.setPadding(10, 15, 10, 10);
                rowNb.setTextSize(12);
                rowNb.setBackgroundColor(Color.rgb(237, 140, 119));

                subDate.setPadding(10, 15, 10, 10);
                subDate.setTextSize(12);

                subName.setPadding(10, 15, 10, 10);
                subName.setTextSize(12);

                subValue.setPadding(10, 15, 10, 10);
                subValue.setTextSize(12);

                rowNb.setText(row + "");
                subDate.setText(substanceList.get(i).getDates().get(j - 1));
                subName.setText(substanceList.get(i).getSub_name());
                subValue.setText(substanceList.get(i).getValues().get(j - 1) + "");
                tableRow.removeAllViews();
                if (j % 2 == 0)
                    rowNb.setBackgroundColor(Color.rgb(240, 116, 89));
                tableRow.addView(rowNb);
                tableRow.addView(subDate);
                tableRow.addView(subName);
                tableRow.addView(subValue);
                if (j % 2 == 0)
                    tableRow.setBackgroundColor(Color.rgb(186, 210, 224));
                tableLayout.addView(tableRow);
                row++;
            }


        }

        currentRowNumber = i;
    }

    public void setTitle() {

        TableRow tableRow = new TableRow(this);
        TextView rowNb = new TextView(this);
        TextView subDate = new TextView(this);
        TextView subName = new TextView(this);
        TextView subValue = new TextView(this);

        rowNb.setPadding(10, 15, 10, 10);
        rowNb.setTextSize(12);
        rowNb.setBackgroundColor(Color.rgb(186, 235, 255));

        subDate.setPadding(10, 15, 10, 10);
        subDate.setTextSize(12);
        subDate.setBackgroundColor(Color.rgb(200, 240, 230));

        subName.setPadding(10, 15, 10, 10);
        subName.setTextSize(12);
        subName.setBackgroundColor(Color.rgb(186, 235, 255));

        subValue.setPadding(10, 15, 10, 10);
        subValue.setTextSize(12);
        subValue.setBackgroundColor(Color.rgb(200, 240, 230));

        rowNb.setText("Row");
        subDate.setText("Date");
        subName.setText("Substance");
        subValue.setText("Value (g/dL)");
        tableRow.removeAllViews();
        tableRow.addView(rowNb);
        tableRow.addView(subDate);
        tableRow.addView(subName);
        tableRow.addView(subValue);
        tableRow.setBackgroundColor(Color.rgb(200, 240, 230));
        //tableRow.setBackgroundColor(Color.rgb(186, 235, 255));
        tableLayout.addView(tableRow);

    }

    public void renderData(List<Substance> substanceList) {

        XAxis xAxis = mChart.getXAxis();
        xAxis.enableGridDashedLine(10f, 10f, 0f);
        xAxis.setAxisMaximum(substanceList.get(0).getDates().size());
        xAxis.setAxisMinimum(0);
        xAxis.setDrawLimitLinesBehindData(true);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.removeAllLimitLines();
        leftAxis.setAxisMaximum(20f);
        leftAxis.setAxisMinimum(0f);
        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        leftAxis.setDrawZeroLine(false);
        leftAxis.setDrawLimitLinesBehindData(false);

        mChart.getAxisRight().setEnabled(false);
        setData(substanceList);
    }

    private void setData(List<Substance> substanceList) {

        ArrayList<Entry> values = new ArrayList<>();
        for (int i = 0; i < substanceList.get(0).getValues().size(); i++) {
            values.add(new Entry((float) i, (float) ((double) substanceList.get(0).getValues().get(i))));
        }

        ArrayList<Entry> dataset2 = new ArrayList<>();
        for (int i = 0; i < substanceList.get(1).getValues().size(); i++) {
            dataset2.add(new Entry((float) i, (float) ((double) substanceList.get(1).getValues().get(i))));
        }

        ArrayList<Entry> dataset3 = new ArrayList<>();
        for (int i = 0; i < substanceList.get(2).getValues().size(); i++) {
            dataset3.add(new Entry((float) i, (float) ((double) substanceList.get(2).getValues().get(i))));
        }

        LineDataSet set1, set2, set3;
        set1 = new LineDataSet(values, "albumine");
        set1.setDrawIcons(false);
        set1.enableDashedLine(10f, 5f, 0f);
        set1.enableDashedHighlightLine(10f, 5f, 0f);
        set1.setColor(Color.DKGRAY);
        set1.setCircleColor(Color.DKGRAY);
        set1.setLineWidth(1f);
        set1.setCircleRadius(3f);
        set1.setDrawCircleHole(false);
        set1.setValueTextSize(9f);
        set1.setDrawFilled(false);
        set1.setFormLineWidth(1f);
        set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
        set1.setFormSize(15.f);

        set2 = new LineDataSet(dataset2, "creatinine");
        set2.setDrawIcons(false);
        set2.enableDashedLine(10f, 5f, 0f);
        set2.enableDashedHighlightLine(10f, 5f, 0f);
        set2.setColor(Color.RED);
        set2.setCircleColor(Color.RED);
        set2.setLineWidth(1f);
        set2.setCircleRadius(3f);
        set2.setDrawCircleHole(false);
        set2.setValueTextSize(9f);
        set2.setDrawFilled(false);
        set2.setFormLineWidth(1f);
        set2.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
        set2.setFormSize(15.f);

        set3 = new LineDataSet(dataset3, "potassium");
        set3.setDrawIcons(false);
        set3.enableDashedLine(10f, 5f, 0f);
        set3.enableDashedHighlightLine(10f, 5f, 0f);
        set3.setColor(Color.GREEN);
        set3.setCircleColor(Color.GREEN);
        set3.setLineWidth(1f);
        set3.setCircleRadius(3f);
        set3.setDrawCircleHole(false);
        set3.setValueTextSize(9f);
        set3.setDrawFilled(false);
        set3.setFormLineWidth(1f);
        set3.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
        set3.setFormSize(15.f);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);
        dataSets.add(set2);
        dataSets.add(set3);
        LineData data = new LineData(dataSets);
        mChart.setData(data);
        mChart.setVisibleXRangeMaximum(20);
        mChart.moveViewToX(0);
    }

}
