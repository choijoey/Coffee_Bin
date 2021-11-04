package com.example.coffee_bin;

import androidx.annotation.NonNull;

public class ReqLoginData {
    String phoneNum;
    String serialNum;

    public ReqLoginData( String phoneNum,String serialNum) {
        this.phoneNum = phoneNum;
        this.serialNum = serialNum;
    }

    @Override
    public String toString() {
            return "[ReqLoginData] =" + phoneNum+" "+serialNum ;

    }


}
