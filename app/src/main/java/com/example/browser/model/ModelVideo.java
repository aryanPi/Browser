package com.example.browser.model;

import android.net.Uri;

public class ModelVideo {

    long id;
    Uri data;
    String title, duration;
    int albumIndex;

    public ModelVideo(long id, Uri data, String title, String duration,int i) {
        this.id = id;
        this.data = data;
        this.title = title;
        this.duration = duration;
        this.albumIndex = i;
    }


    public int getAlbumIndex() {
        return albumIndex;
    }

    public void setAlbumIndex(int albumIndex) {
        this.albumIndex = albumIndex;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Uri getData() {
        return data;
    }

    public void setData(Uri data) {
        this.data = data;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
