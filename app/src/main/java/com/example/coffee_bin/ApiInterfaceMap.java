package com.example.coffee_bin;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterfaceMap {
    // base_url + "api/login" 으로 POST 통신
    @POST("map/")
    Call<List<ResMapData>> requestPostLogin(@Body ReqTokenData reqTokenData );   // @Body : request 파라미터


}
