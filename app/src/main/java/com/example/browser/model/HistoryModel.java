package com.example.browser.model;

import java.util.ArrayList;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;

public class HistoryModel extends RealmObject {


    int webviewID;
    Long stack;
    String url;
    String title;
    String logo;
    Date date;
    boolean stateOfPage;


    public HistoryModel() {
    }

    public HistoryModel(Long stack, String url, String title, String logo, boolean stateOfPage, int webviewID,Date date) {
        this.stack = stack;
        this.url = url;
        this.title = title;
        this.logo = logo;
        this.stateOfPage = stateOfPage;
        this.webviewID = webviewID;
        this.date = date;
    }


    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getWebviewID() {
        return webviewID;
    }

    public void setWebviewID(int webviewID) {
        this.webviewID = webviewID;
    }

    public boolean isStateOfPage() {
        return stateOfPage;
    }

    public void setStateOfPage(boolean stateOfPage) {
        this.stateOfPage = stateOfPage;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public Long getStack() {
        return stack;
    }

    public void setStack(Long stack) {
        this.stack = stack;
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
}
