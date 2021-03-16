package com.example.browser.model;

import android.net.Uri;

import java.io.Serializable;

public class ModelAudio {

    long id;
    Uri data;
    String title, duration;
    int albumIndex;

    public ModelAudio(long id, Uri data, String title, String duration, int albumIndex) {
        this.id = id;
        this.data = data;
        this.title = title;
        this.duration = duration;
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

    public int getAlbumIndex() {
        return albumIndex;
    }

    public void setAlbumIndex(int albumIndex) {
        this.albumIndex = albumIndex;
    }
}
