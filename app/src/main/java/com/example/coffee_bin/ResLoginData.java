package com.example.coffee_bin;

import com.google.gson.annotations.Expose;

import androidx.annotation.NonNull;

// api/login 응답 데이터
public class ResLoginData {

    @Expose
    String phoneNum;
    String serialNum;


    @NonNull
    @Override
    public String toString() {
        return "[ResLoginData] =" + phoneNum+" "+serialNum ;}
}

