package com.example.coffee_bin;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class NoticeListAdapter extends BaseAdapter {

    public NoticeListAdapter(Context context, List<Notice> noticeList) {
        this.context = context;
        this.noticeList = noticeList;
    }

    private Context context;
    private List<Notice> noticeList;


    @Override
    public int getCount() {
        return noticeList.size();
    }

    @Override
    public Object getItem(int position) {
        return noticeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = View.inflate(context, R.layout.point,null);
        TextView pointText = (TextView) v.findViewById(R.id.pointTitle);
        TextView dateText = (TextView) v.findViewById(R.id.dateText);
        TextView pointSize = (TextView) v.findViewById(R.id.pointSize);

        pointText.setText(noticeList.get(position).getNotice());
        dateText.setText(noticeList.get(position).getDate());
        pointSize.setText(noticeList.get(position).getSize());

        v.setTag(noticeList.get(position).getNotice());
        return v;
    }
}
