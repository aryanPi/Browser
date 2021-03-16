package com.example.browser.model;

import android.net.Uri;

import java.io.Serializable;

public class ModelImage implements Serializable {

    String data;
    int indexOfAlbum;

    public ModelImage(String data,int indexOfAlbum) {
        this.data = data;
        this.indexOfAlbum = indexOfAlbum;
    }



    public int getIndexOfAlbum() {
        return indexOfAlbum;
    }

    public void setIndexOfAlbum(int indexOfAlbum) {
        this.indexOfAlbum = indexOfAlbum;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

}
