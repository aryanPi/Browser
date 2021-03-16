package com.example.browser.model;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import androidx.room.PrimaryKey;

import io.realm.RealmObject;
import io.realm.annotations.RealmField;

public class TabModel  {

    int Id;
    String url;
    String title;


    public TabModel(int id, String title, String url) {
        this.Id = id;
        this.title = title;
        this.url = url;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        this.Id  = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
