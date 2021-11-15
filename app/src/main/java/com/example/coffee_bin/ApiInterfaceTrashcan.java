package com.example.coffee_bin;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterfaceTrashcan {
    // base_url + "api/login" 으로 POST 통신
    @POST("amount/")
    Call<List<ResTrashcanData>> requestPostLogin(@Body ReqTokenData reqTokenData );   // @Body : request 파라미터
}
