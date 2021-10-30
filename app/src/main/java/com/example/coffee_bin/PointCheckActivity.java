package com.example.coffee_bin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class PointCheckActivity extends AppCompatActivity {

    private ListView noticeListView;
    private NoticeListAdapter adapter;
    private List<Notice> noticeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point_check);

        noticeListView = (ListView) findViewById((R.id.point_listView));
        noticeList=new ArrayList<Notice>();
        noticeList.add(new Notice("적립 내역 안내","+30","2021-10-30"));
        noticeList.add(new Notice("적립 내역 안내","+30","2021-10-30"));
        noticeList.add(new Notice("적립 내역 안내","+30","2021-10-30"));
        noticeList.add(new Notice("적립 내역 안내","+30","2021-10-30"));
        noticeList.add(new Notice("적립 내역 안내","+30","2021-10-30"));
        noticeList.add(new Notice("적립 내역 안내","+30","2021-10-30"));
        noticeList.add(new Notice("적립 내역 안내","+30","2021-10-30"));
        adapter = new NoticeListAdapter(getApplicationContext(),noticeList);
        noticeListView.setAdapter(adapter);




    }
}