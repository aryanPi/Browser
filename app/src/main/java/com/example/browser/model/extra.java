package com.example.browser.model;

import java.util.ArrayList;

import io.realm.RealmObject;

public class extra extends RealmObject {

    public extra() {
    }

    //Settings
    String privateFolderPass = "";
    String downloadLocation = "";
    String defaultSearchEngine = "https://www.google.com/search?q=";
    String nameOfDefaultSearchEngine="Google";
    long historyStack = 0;
    boolean downloadOnlyWtithWifi = false;
    boolean downloadCompleteRingtone = true;
    boolean downloadCompleteVibration = true;
    boolean incognitoMode = false;
    boolean downloadWith = true; // true = inbuild download manager, false = Device Download manager
    boolean defaultBrowser = false;



    public String getPrivateFolderPass() {
        return privateFolderPass;
    }

    public void setPrivateFolderPass(String privateFolderPass) {
        this.privateFolderPass = privateFolderPass;
    }

    public String getNameOfDefaultSearchEngine() {
        return nameOfDefaultSearchEngine;
    }

    public void setNameOfDefaultSearchEngine(String nameOfDefaultSearchEngine) {
        this.nameOfDefaultSearchEngine = nameOfDefaultSearchEngine;
    }

    public String getDefaultSearchEngine() {
        return defaultSearchEngine;
    }

    public void setDefaultSearchEngine(String defaultSearchEngine) {
        this.defaultSearchEngine = defaultSearchEngine;
    }

    public boolean isDefaultBrowser() {
        return defaultBrowser;
    }

    public void setDefaultBrowser(boolean defaultBrowser) {
        this.defaultBrowser = defaultBrowser;
    }

    public boolean isDownloadWith() {
        return downloadWith;
    }

    public void setDownloadWith(boolean downloadWith) {
        this.downloadWith = downloadWith;
    }

    public extra(String downloadLocation, long historyStack, boolean downloadOnlyWtithWifi, boolean downloadCompleteRingtone, boolean downloadCompleteVibration, boolean incognitoMode, boolean premium) {
        this.downloadLocation = downloadLocation;
        this.historyStack = historyStack;
        this.downloadOnlyWtithWifi = downloadOnlyWtithWifi;
        this.downloadCompleteRingtone = downloadCompleteRingtone;
        this.downloadCompleteVibration = downloadCompleteVibration;
        this.incognitoMode = incognitoMode;
        this.premium = premium;
    }

    boolean premium = false;

    public long getHistoryStack() {
        return historyStack;
    }

    public void setHistoryStack(long historyStack) {
        this.historyStack = historyStack;
    }

    public String getDownloadLocation() {
        return downloadLocation;
    }

    public void setDownloadLocation(String downloadLocation) {
        this.downloadLocation = downloadLocation;
    }

    public boolean isDownloadOnlyWtithWifi() {
        return downloadOnlyWtithWifi;
    }

    public void setDownloadOnlyWtithWifi(boolean downloadOnlyWtithWifi) {
        this.downloadOnlyWtithWifi = downloadOnlyWtithWifi;
    }

    public boolean isDownloadCompleteRingtone() {
        return downloadCompleteRingtone;
    }

    public void setDownloadCompleteRingtone(boolean downloadCompleteRingtone) {
        this.downloadCompleteRingtone = downloadCompleteRingtone;
    }

    public boolean isDownloadCompleteVibration() {
        return downloadCompleteVibration;
    }

    public void setDownloadCompleteVibration(boolean downloadCompleteVibration) {
        this.downloadCompleteVibration = downloadCompleteVibration;
    }

    public boolean isIncognitoMode() {
        return incognitoMode;
    }

    public void setIncognitoMode(boolean incognitoMode) {
        this.incognitoMode = incognitoMode;
    }

    public boolean isPremium() {
        return premium;
    }

    public void setPremium(boolean premium) {
        this.premium = premium;
    }
}
