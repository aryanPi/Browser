package com.example.browser.model;

public class searchSuggestionItemModel {
    String title;
    String url;
    Integer from; // 0 = history , 1 = bookmark , 2 = search

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getFrom() {
        return from;
    }

    public void setFrom(Integer from) {
        this.from = from;
    }

    public searchSuggestionItemModel(String title, String url, Integer from) {
        this.title = title;
        this.url = url;
        this.from = from;
    }
}
