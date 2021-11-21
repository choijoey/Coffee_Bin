package com.example.coffee_bin;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Frag1 extends Fragment{
    private View view;
    //private int[] trash_volum= new int[3];


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,  Bundle savedInstanceState) {
       view=inflater.inflate(R.layout.frag1,container,false);

        String[] ListviewTitle = new String[]{
                "99","20","66"
        };
        String[] ListviewDescription = new String[]{
                "99","20","66"
        };
        Bundle bundle=getArguments();
        if(bundle!= null){
            ListviewTitle = getArguments().getStringArray("name");
            ListviewDescription = getArguments().getStringArray("plastic_amount");
        }




        int[] ListviewImages= new int[]{
                R.drawable.plastic_volume1,R.drawable.plastic_volume2,R.drawable.plastic_volume3
        };
        List <HashMap<String,String>> aList = new ArrayList<HashMap<String,String>>();

        String [] from={
                "ListImages","ListTitle","ListDescription"
        };
        int[] to = {
                R.id.listview_images,R.id.Title,R.id.Description
        };

        SimpleAdapter simpleAdapter = new SimpleAdapter(getActivity(),aList,R.layout.listview_items,from,to);
        ListView simpleListview=(ListView)view.findViewById(R.id.frag1_list_view);
        simpleListview.setAdapter(simpleAdapter);
        for (int x=0;x<3;x++){
            HashMap<String,String> hm = new HashMap<String,String>();
            hm.put("ListTitle",ListviewTitle[x]);
            hm.put("ListDescription",Integer.toString((int)(((35.0-Double.parseDouble(ListviewDescription[x]))/35)*100))+"%");

            if(((int)(((35.0-Double.parseDouble(ListviewDescription[x]))/35)*100))<33){
                hm.put("ListImages",Integer.toString(ListviewImages[0]));
            }else if(((int)(((35.0-Double.parseDouble(ListviewDescription[x]))/35)*100))<66){
                hm.put("ListImages",Integer.toString(ListviewImages[1]));
            }
            else{
                hm.put("ListImages",Integer.toString(ListviewImages[2]));
            }
            aList.add(hm);

        }
        simpleAdapter.notifyDataSetChanged();


        return view;
    }
}





