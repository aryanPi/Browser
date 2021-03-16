package com.example.browser.model;

import java.util.ArrayList;

public class WebPageDataTempModel {
    int Id;

    String url;
    String title;
    String frvicon = "";
    boolean fbOrInsta;

    ArrayList<String> downloadLinks = new ArrayList<>();



    public WebPageDataTempModel(int id, String url, String title, String frvicon, boolean fbOrInsta, ArrayList<String> downloadLinks) {
        Id = id;
        this.url = url;
        this.title = title;
        this.frvicon = frvicon;
        this.fbOrInsta = fbOrInsta;
        this.downloadLinks = downloadLinks;
    }


    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
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

    public String getFrvicon() {
        return frvicon;
    }

    public void setFrvicon(String frvicon) {
        this.frvicon = frvicon;
    }

    public boolean isFbOrInsta() {
        return fbOrInsta;
    }

    public void customScriptSite(boolean fbOrInsta) {
        this.fbOrInsta = fbOrInsta;
    }

    public ArrayList<String> getDownloadLinks() {
        return downloadLinks;
    }

    public void setDownloadLinks(ArrayList<String> downloadLinks) {
        this.downloadLinks = downloadLinks;
    }
}
