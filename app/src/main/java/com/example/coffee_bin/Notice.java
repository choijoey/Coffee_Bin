package com.example.coffee_bin;

public class Notice {
    String notice;
    String size;
    String date;

    public Notice(String notice, String size, String date) {
        this.notice = notice;
        this.size = size;
        this.date = date;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
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
