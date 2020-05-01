package com.example.spiritualtablet;

public class DataItem {

    private String title;
    private String category;
    private int thumbnail;

    public DataItem(){}

    DataItem(String title, String category, int thumbnail) {
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
