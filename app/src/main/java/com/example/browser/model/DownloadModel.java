package com.example.browser.model;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class DownloadModel extends RealmObject implements Serializable {

    @PrimaryKey
    Long id;
    Long downloadManagerId;
    String url;
    String title;
    String file_path;
    int progress;
    int status;
    Long file_size;
    String uri;
    String mimeType;
    Long total_file_Size;
    boolean downloadingWith; //true = with url inbuild , false = with download manager

    public DownloadModel(Long id, String url, String title, String file_path, int progress, int status, Long file_size, String uri, String mimeType, Long total_file_Size, boolean downloadingWith) {
        this.id = id;
        this.url = url;
        this.title = title;
        this.file_path = file_path;
        this.progress = progress;
        this.status = status;
        this.file_size = file_size;
        this.uri = uri;
        this.mimeType = mimeType;
        this.total_file_Size = total_file_Size;
        this.downloadingWith = downloadingWith;
    }


    public Long getDownloadManagerId() {
        return downloadManagerId;
    }

    public void setDownloadManagerId(Long downloadManagerId) {
        this.downloadManagerId = downloadManagerId;
    }

    public DownloadModel(){

    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public boolean isDownloadingWith() {
        return downloadingWith;
    }

    public void setDownloadingWith(boolean downloadingWith) {
        this.downloadingWith = downloadingWith;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getTotal_file_Size() {
        return total_file_Size;
    }

    public void setTotal_file_Size(Long total_file_Size) {
        this.total_file_Size = total_file_Size;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFile_path() {
        return file_path;
    }

    public void setFile_path(String file_path) {
        this.file_path = file_path;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Long getFile_size() {
        return file_size;
    }

    public void setFile_size(Long file_size) {
        this.file_size = file_size;
    }

}
