package com.example.spider_recognition;

import android.util.Log;

public class History {
    private String category;
    private String date;
    private String imagePath;

    public History(String category, String date, String imagePath){
        this.category = category;
        this.date = date;
        this.imagePath = imagePath;
        Log.d("History","add");
    }

    public String getCategory(){
        return category;
    }

    public void setCategory(String category){
        this.category = category;
    }

    public String getDate(){
        return date;
    }

    public void setDate(String date){
        this.date = date;
    }

    public String getImagePath(){ return imagePath; }

    public void setImagePath(String imagePath) { this.imagePath = imagePath; }
}
