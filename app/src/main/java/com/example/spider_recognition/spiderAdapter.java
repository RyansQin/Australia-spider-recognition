package com.example.spider_recognition;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class spiderAdapter extends ArrayAdapter {
    static class ViewHolder{
        @BindView(R.id.list_example_image)
        ImageView image;

        @BindView(R.id.list_example_text)
        TextView text;

        ViewHolder(View view){
            ButterKnife.bind(this, view);
        }
    }

    private int resourceID;

    public spiderAdapter(Context context, int resource, List<spider> Objects){
        super(context, resource, Objects);
        resourceID = resource;
    }

    public View getView(int position, View input_view, ViewGroup group){
        spider au_spider = (spider)getItem(position);
        View view;
        ViewHolder holder;
        if (input_view == null){
            view = LayoutInflater.from(getContext()).inflate(resourceID, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }
        else{
            view = input_view;
            holder = (ViewHolder)view.getTag();
        }

        holder.image.setImageResource(au_spider.getSpider_image());
        holder.text.setText(au_spider.getSpider_name());
        return view;
    }





}
