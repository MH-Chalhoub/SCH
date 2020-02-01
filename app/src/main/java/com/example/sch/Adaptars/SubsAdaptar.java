package com.example.sch.Adaptars;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sch.Interfaces.OnItemClickListener;
import com.example.sch.R;
import com.example.sch.Substance;

import java.util.List;

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
        imageViewHolder.itemImage.setImageResource(R.drawable.ic_menu_report_image);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }


    public static class ImageViewHolder extends RecyclerView.ViewHolder{
        public ImageView itemImage;
        public TextView addText, subName;
        public LinearLayout iconWrapperitem;
        public ImageViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            itemImage = itemView.findViewById(R.id.itemImage);
            addText = itemView.findViewById(R.id.addText);
            subName = itemView.findViewById(R.id.subName);
            iconWrapperitem = itemView.findViewById(R.id.iconWrapperitem);

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
}


