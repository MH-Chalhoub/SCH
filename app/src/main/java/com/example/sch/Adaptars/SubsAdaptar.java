package com.example.sch.Adaptars;


import android.content.Context;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.sch.Interfaces.OnItemClickListener;
import com.example.sch.MainActivity;
import com.example.sch.R;
import com.example.sch.Substance;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SubsAdaptar extends RecyclerView.Adapter<SubsAdaptar.ImageViewHolder>  {

    private Context mContext;
    private List<Substance> mItems;
    private OnItemClickListener mListener;

    public SubsAdaptar(Context context, List<Substance> Items)
    {
        mContext = context;
        mItems = Items;
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.sub_item, viewGroup, false);
        return new ImageViewHolder(v, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder imageViewHolder, int i) {
        Substance itemCur = mItems.get(i);

        imageViewHolder.addText.setText(itemCur.getAdd_text());
        imageViewHolder.subName.setText(itemCur.getSub_name());
        imageViewHolder.subValue.setText(Math.ceil(itemCur.getValue()) + "");
        imageViewHolder.progress.setProgress((int)itemCur.getValue());
        imageViewHolder.itemImage.setImageResource(itemCur.getLogo());
        imageViewHolder.chartWrapperitem.setVisibility(View.GONE);
        renderData(imageViewHolder.mChart);
        imageViewHolder.mChart.setTouchEnabled(false);
        imageViewHolder.mChart.setPinchZoom(false);
        imageViewHolder.chartDescription.setText("This is " + itemCur.getSub_name().toUpperCase() + " values during this day ");
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }


    public static class ImageViewHolder extends RecyclerView.ViewHolder{
        public ImageView itemImage;
        public TextView addText, subName, subValue, chartDescription;
        public LinearLayout iconWrapperitem, chartWrapperitem;
        public ProgressBar progress;
        private LineChart mChart;
        public ImageViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            itemImage = itemView.findViewById(R.id.itemImage);
            addText = itemView.findViewById(R.id.addText);
            subName = itemView.findViewById(R.id.subName);
            subValue = itemView.findViewById(R.id.subAvg);
            iconWrapperitem = itemView.findViewById(R.id.iconWrapperitem);
            chartWrapperitem = itemView.findViewById(R.id.chartWrapperitem);
            progress = itemView.findViewById(R.id.progressBar);
            mChart = itemView.findViewById(R.id.chart);
            chartDescription = itemView.findViewById(R.id.chartDescription);

            iconWrapperitem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //prod_favorite.setImageResource(R.drawable.btn_favorite_black);
                    if(listener != null){
                        int postion = getAdapterPosition();
                        if(postion != RecyclerView.NO_POSITION){
                            listener.onItemClick(postion);
                        }
                    }
                    if (chartWrapperitem.getVisibility() == View.VISIBLE) {
                        chartWrapperitem.setVisibility(View.GONE);
                    } else {
                        chartWrapperitem.setVisibility(View.VISIBLE);
                    }
                }
            });
            iconWrapperitem.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if(listener != null) {
                        int postion = getAdapterPosition();
                        if (postion != RecyclerView.NO_POSITION) {
                            listener.onItemLongClick(postion);
                        }
                    }
                    return false;
                }
            });
        }
    }



    public void renderData(LineChart mChart) {

        XAxis xAxis = mChart.getXAxis();
        xAxis.enableGridDashedLine(10f, 10f, 0f);
        xAxis.setAxisMaximum(10f);
        xAxis.setAxisMinimum(0f);
        xAxis.setDrawLimitLinesBehindData(true);

        LimitLine ll1 = new LimitLine(215f, "Maximum Limit");
        ll1.setLineWidth(4f);
        ll1.enableDashedLine(10f, 10f, 0f);
        ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        ll1.setTextSize(10f);

        LimitLine ll2 = new LimitLine(70f, "Minimum Limit");
        ll2.setLineWidth(4f);
        ll2.enableDashedLine(10f, 10f, 0f);
        ll2.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        ll2.setTextSize(10f);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.removeAllLimitLines();
        leftAxis.addLimitLine(ll1);
        leftAxis.addLimitLine(ll2);
        leftAxis.setAxisMaximum(350f);
        leftAxis.setAxisMinimum(0f);
        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        leftAxis.setDrawZeroLine(false);
        leftAxis.setDrawLimitLinesBehindData(false);

        mChart.getAxisRight().setEnabled(false);
        setData(mChart);
    }

    private void setData(LineChart mChart) {

        ArrayList<Entry> values = new ArrayList<>();
        Random rand = new Random();
        values.add(new Entry(1, (float)rand.nextInt(250)));
        values.add(new Entry(2, (float)rand.nextInt(250)));
        values.add(new Entry(3, (float)rand.nextInt(250)));
        values.add(new Entry(4, (float)rand.nextInt(250)));
        values.add(new Entry(5, (float)rand.nextInt(250)));
        values.add(new Entry(7, (float)rand.nextInt(250)));
        values.add(new Entry(8, (float)rand.nextInt(250)));
        values.add(new Entry(9, (float)rand.nextInt(250)));

        LineDataSet set1;
        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) mChart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            set1 = new LineDataSet(values, "Sample Data");
            set1.setDrawIcons(false);
            set1.enableDashedLine(10f, 5f, 0f);
            set1.enableDashedHighlightLine(10f, 5f, 0f);
            set1.setColor(Color.DKGRAY);
            set1.setCircleColor(Color.DKGRAY);
            set1.setLineWidth(1f);
            set1.setCircleRadius(3f);
            set1.setDrawCircleHole(false);
            set1.setValueTextSize(9f);
            set1.setDrawFilled(true);
            set1.setFormLineWidth(1f);
            set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
            set1.setFormSize(15.f);

            if (Utils.getSDKInt() >= 18) {
                Drawable drawable = ContextCompat.getDrawable(mContext, R.drawable.fade_blue);
                set1.setFillDrawable(drawable);
            } else {
                set1.setFillColor(Color.DKGRAY);
            }
            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);
            LineData data = new LineData(dataSets);
            mChart.setData(data);
        }
    }
}


