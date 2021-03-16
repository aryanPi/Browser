package com.example.browser.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class HomePageBookmark extends RealmObject {

    @PrimaryKey
    int id;
    String url;
    String title;
    String uri;

    public HomePageBookmark(){}

    public HomePageBookmark(int id, String url, String title, String uri) {
        this.id = id;
        this.url = url;
        this.title = title;
        this.uri = uri;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
