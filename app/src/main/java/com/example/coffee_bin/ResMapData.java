package com.example.coffee_bin;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import androidx.annotation.NonNull;

// api/login 응답 데이터
public class ResMapData {


    @SerializedName("latitude")
    String latitude;
    @SerializedName("longitude")
    String longitude;

    public ResMapData(String latitude, String longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
    public String getLatitude() {
        return latitude;
    }
    public String getLongitude() {
        return longitude;
    }

}

