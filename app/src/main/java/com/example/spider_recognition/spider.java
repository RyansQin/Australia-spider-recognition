package com.example.spider_recognition;

// For each type of spider, this class contains the image and the name;

public class spider {
    private int spider_image;
    private String spider_name;

    public spider(int image, String name){
        this.spider_image = image;
        this.spider_name = name;
    }

    public int getSpider_image(){
        return spider_image;
    }

    public void setSpider_image(int image){
        this.spider_image = image;
    }

    public String getSpider_name(){
        return spider_name;
    }

    public void setSpider_name(String name){
        this.spider_name = name;
    }

}
