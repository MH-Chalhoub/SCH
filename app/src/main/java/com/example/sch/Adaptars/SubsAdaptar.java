package com.example.sch.Adaptars;


import android.content.Context;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
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

        if(itemCur.getValue() != -1){
            imageViewHolder.addText.setText(itemCur.getAdd_text());
            imageViewHolder.subName.setText(itemCur.getSub_name());
            imageViewHolder.subValue.setText(itemCur.getValue() + "");
            imageViewHolder.progress.setProgress((int)itemCur.getValue());
            imageViewHolder.itemImage.setImageResource(itemCur.getLogo());
            //imageViewHolder.chartWrapperitem.setVisibility(View.GONE);
            renderData(imageViewHolder.mChart, itemCur.getValues(), itemCur.getSub_name().toUpperCase());
            imageViewHolder.mChart.setTouchEnabled(false);
            imageViewHolder.mChart.setPinchZoom(false);
            imageViewHolder.mChart.getDescription().setText(itemCur.getSub_name() + " Levels");
            imageViewHolder.chartDescription.setText("This is " + itemCur.getSub_name().toUpperCase() + " values during this day ");
            imageViewHolder.mChart.getDescription().setText(itemCur.getSub_name().toUpperCase() + " Levels");
            if(itemCur.getValue() < 3 || itemCur.getValue() > 9){
                imageViewHolder.insideCard.setBackgroundColor(mContext.getResources().getColor(R.color.transparentRed));
            }
            else {
                imageViewHolder.insideCard.setBackgroundColor(Color.WHITE);
            }
        }
        else {
            imageViewHolder.addText.setText(itemCur.getAdd_text());
            imageViewHolder.subName.setText(itemCur.getSub_name());
            imageViewHolder.subValue.setText("No Data");
            imageViewHolder.progress.setProgress(0);
            imageViewHolder.itemImage.setImageResource(itemCur.getLogo());
            imageViewHolder.chartWrapperitem.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }


    public static class ImageViewHolder extends RecyclerView.ViewHolder{
        public ImageView itemImage;
        public TextView addText, subName, subValue, chartDescription;
        public LinearLayout iconWrapperitem, chartWrapperitem, insideCard;
        public ProgressBar progress;
        private LineChart mChart;
        private CardView cardView;
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
            cardView = itemView.findViewById(R.id.card_view);
            chartDescription = itemView.findViewById(R.id.chartDescription);
            insideCard = itemView.findViewById(R.id.insideCard);

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
                    if(!subValue.getText().equals("No Data")){
                        if (chartWrapperitem.getVisibility() == View.VISIBLE) {
                            chartWrapperitem.setVisibility(View.GONE);
                        } else {
                            chartWrapperitem.setVisibility(View.VISIBLE);
                        }
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



    public void renderData(LineChart mChart, ArrayList<Double> sub_values, String sub_name) {

        XAxis xAxis = mChart.getXAxis();
        xAxis.enableGridDashedLine(10f, 10f, 0f);
        xAxis.setAxisMaximum(sub_values.size());
        xAxis.setAxisMinimum(sub_values.size()-10);
        xAxis.setDrawLimitLinesBehindData(true);

        LimitLine ll1 = new LimitLine(9f, "Maximum Limit");
        ll1.setLineWidth(4f);
        ll1.enableDashedLine(10f, 10f, 0f);
        ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        ll1.setTextSize(10f);

        LimitLine ll2 = new LimitLine(3f, "Minimum Limit");
        ll2.setLineWidth(4f);
        ll2.enableDashedLine(10f, 10f, 0f);
        ll2.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        ll2.setTextSize(10f);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.removeAllLimitLines();
        leftAxis.addLimitLine(ll1);
        leftAxis.addLimitLine(ll2);
        leftAxis.setAxisMaximum(20f);
        leftAxis.setAxisMinimum(0f);
        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        leftAxis.setDrawZeroLine(false);
        leftAxis.setDrawLimitLinesBehindData(false);

        mChart.getAxisRight().setEnabled(false);
        setData(mChart, sub_values, sub_name);
    }

    private void setData(LineChart mChart, ArrayList<Double> sub_values, String sub_name) {

        ArrayList<Entry> values = new ArrayList<>();
        Random rand = new Random();
        for(int i=0; i<sub_values.size() ; i++){
            values.add(new Entry((float)i, (float)((double)sub_values.get(i))));
        }

        LineDataSet set1;
        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) mChart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
            //System.out.println("if (mChart.getData())");
        } else {
            //System.out.println("elsef (mChart.getData())");
            set1 = new LineDataSet(values, sub_name);
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


