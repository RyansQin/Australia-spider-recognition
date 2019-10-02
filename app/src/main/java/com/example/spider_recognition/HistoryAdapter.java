package com.example.spider_recognition;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HistoryAdapter extends ArrayAdapter {
    static class ViewHolder{
        @BindView(R.id.list_spider_image)
        ImageView image;

        @BindView(R.id.list_spider_category)
        TextView category;

        @BindView(R.id.list_spider_date)
        TextView date;

        ViewHolder(View view){
            ButterKnife.bind(this, view);
        }
    }

    private int resourceID;

    public HistoryAdapter(Context context, int resource, List<History> Objects) {
        super(context, resource, Objects);
        resourceID = resource;
    }

    public View getView(int position, View input_view, ViewGroup group){
        History history = (History)getItem(position);
        View view;
        HistoryAdapter.ViewHolder holder;
        if (input_view == null){
            view = LayoutInflater.from(getContext()).inflate(resourceID, null);
            holder = new HistoryAdapter.ViewHolder(view);
            view.setTag(holder);
        }
        else{
            view = input_view;
            holder = (HistoryAdapter.ViewHolder)view.getTag();
        }

        holder.category.setText(history.getCategory());
        holder.date.setText(history.getDate());
        holder.image.setImageBitmap(BitmapFactory.decodeFile(history.getImagePath()));
        return view;
    }
}
