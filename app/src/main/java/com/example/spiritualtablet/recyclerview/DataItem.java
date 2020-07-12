package com.example.spiritualtablet.recyclerview;

public class DataItem {

    private String title;
    private String category;
    private int thumbnail;

    public DataItem(){}

    public DataItem(String title, String category, int thumbnail) {
        this.title = title;
        this.category = category;
        this.thumbnail = thumbnail;
    }

    String getTitle() {
        return title;
    }

    String getCategory() {
        return category;
    }

    int getThumbnail() {
        return thumbnail;
    }
}
