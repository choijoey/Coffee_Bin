package com.example.coffee_bin;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInterfaceLogin {

    // base_url + "api/login" 으로 POST 통신
    @POST("kakaoLogin/")
    Call<ResLoginData> requestPostLogin(@Body ReqLoginData reqLoginData );   // @Body : request 파라미터

}


