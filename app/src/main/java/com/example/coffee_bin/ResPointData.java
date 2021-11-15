package com.example.coffee_bin;

import com.google.gson.annotations.SerializedName;

public class ResPointData {
    @SerializedName("size")
    String size;
    @SerializedName("date")
    String date;
    @SerializedName("curPoint")
    Integer curPoint;

    public ResPointData(String size, String date, Integer curPoint) {
        this.size = size;
        this.date = date;
        this.curPoint = curPoint;
    }

    public String getSize() {
        return size;
    }

    public String getDate() {
        return date;
    }

    public Integer getCurPoint() {
        return curPoint;
    }
}
