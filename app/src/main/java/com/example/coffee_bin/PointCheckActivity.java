package com.example.coffee_bin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PointCheckActivity extends AppCompatActivity {

    private ListView noticeListView;
    private NoticeListAdapter adapter;
    private List<Notice> noticeList;
    private String token;
    TextView coin_text;
    ApiInterfacePoint api;
    Integer cur_point =null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point_check);

        Intent intent = getIntent();
        token = intent.getStringExtra("token");
        api = HttpClient.getRetrofit().create(ApiInterfacePoint.class);
        requestPost(token);


        noticeListView = (ListView) findViewById((R.id.point_listView));
        noticeList=new ArrayList<Notice>();
        adapter = new NoticeListAdapter(getApplicationContext(),noticeList);
        noticeListView.setAdapter(adapter);


        coin_text=findViewById(R.id.coin_text);



    }

    // POST 통신요청
    public void requestPost(String token) {

        ReqTokenData reqTokenData = new ReqTokenData(token);
        Call<List<ResPointData>> call = api.requestPostLogin(reqTokenData);

        call.enqueue(new Callback<List<ResPointData>>() {
            // 통신성공 후 텍스트뷰에 결과값 출력

            @Override
            public void onResponse(Call<List<ResPointData>> call, Response<List<ResPointData>> response) {
                List<ResPointData> resPointDatas = response.body();

                int idx = 0;

                
                System.out.println("제대로 함 ");
                for (ResPointData resPointData : resPointDatas) {
                    String x = resPointData.getSize();
                    String y = resPointData.getDate();
                    cur_point = resPointData.getCurPoint();

                    String si="+"+x+"        / 잔여 포인트: "+ cur_point.toString();
                    noticeList.add(new Notice(y,si));

                }

                runOnUiThread(new Runnable() { // runOnUiThread()를 호출하여 실시간 갱신한다.
                    @Override
                    public void run() {
                        // 갱신된 데이터 내역을 어댑터에 알려줌
                        coin_text.setText(cur_point.toString());
                        adapter.notifyDataSetChanged();

                    }
                });
                Log.d("TEST", "onResponse 후");

            }

            @Override
            public void onFailure(Call<List<ResPointData>> call, Throwable t) {
                //tv_email.setText( "onFailure" );
                Log.d("TEST", "로그찍어봄", t);
            }
        });
    }
}