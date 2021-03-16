package com.example.browser.model;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class DownloadsBottomSheetModel {

    Bitmap image;
    String title;
    ArrayList<String> url;
    ArrayList<String> size;
    ArrayList<String> qualities;


    public DownloadsBottomSheetModel(Bitmap image, String title, ArrayList<String> url, ArrayList<String> size, ArrayList<String> qualities) {
        this.image = image;
        this.title = title;
        this.url = url;
        this.size = size;
        this.qualities = qualities;
    }


    public ArrayList<String> getQualities() {
        return qualities;
    }

    public void setQualities(ArrayList<String> qualities) {
        this.qualities = qualities;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<String> getUrl() {
        return url;
    }

    public void setUrl(ArrayList<String> url) {
        this.url = url;
    }

    public ArrayList<String> getSize() {
        return size;
    }

    public void setSize(ArrayList<String> size) {
        this.size = size;
    }

}
