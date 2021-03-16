package com.example.browser.model;

import androidx.room.PrimaryKey;

import io.realm.RealmObject;

public class BookmarksModel extends RealmObject {

    @PrimaryKey
    String url;
    String title;
    String logo;


    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public BookmarksModel(String url, String title, String logo) {
        this.url = url;
        this.title = title;
        this.logo = logo;
    }

    public BookmarksModel(){}

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
