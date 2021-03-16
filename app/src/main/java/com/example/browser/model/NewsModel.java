package com.example.browser.model;

public class NewsModel {
    String url;
    String title;
    String urlToImage;
    String author;

    public NewsModel(String url, String title, String urlToImage, String author) {
        this.url = url;
        this.title = title;
        this.urlToImage = urlToImage;
        this.author = author;
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

    public String getUrlToImage() {
        return urlToImage;
    }

    public void setUrlToImage(String urlToImage) {
        this.urlToImage = urlToImage;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
