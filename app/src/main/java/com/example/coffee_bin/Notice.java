package com.example.coffee_bin;

public class Notice {
    String size;
    String date;

    public Notice(String size, String date) {
        this.size = size;
        this.date = date;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
