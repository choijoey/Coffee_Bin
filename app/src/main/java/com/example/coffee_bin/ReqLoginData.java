package com.example.coffee_bin;

import androidx.annotation.NonNull;

public class ReqLoginData {
    String phoneNum;
    String email;

    public ReqLoginData( String phoneNum,String email) {
        this.phoneNum = phoneNum;
        this.email = email;
    }

    @Override
    public String toString() {
            return "[ReqLoginData] =" + phoneNum+" "+email ;

    }


}
