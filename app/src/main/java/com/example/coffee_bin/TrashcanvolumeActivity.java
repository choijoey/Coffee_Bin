package com.example.coffee_bin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrashcanvolumeActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private FragmentManager fm;
    private FragmentTransaction ft;
    private Frag1 frag1;
    private Frag2 frag2;
    private String token;
    private Bundle bundle=new Bundle();

    ApiInterfaceTrashcan api;


    String[] name ={"","",""};
    String[] plastic_amount ={"","",""};
    String[] paper_amount ={"","",""};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trashcan_volume);

        Intent intent = getIntent();
        token = intent.getStringExtra("token");
        api = HttpClient.getRetrofit().create(ApiInterfaceTrashcan.class);

        requestPost(token);



        bottomNavigationView=findViewById((R.id.bottomNavi));
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull  MenuItem menuitem) {
                switch (menuitem.getItemId()) {
                    case R.id.action_plastic:
                        setFrag(0);
                        break;
                    case R.id.action_paper:
                        setFrag(1);
                        break;
                }
                return false;
        }
  });
        frag1= new Frag1();
        frag2= new Frag2();

    }

    private void setFrag(int n){ //fragement 교체
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        switch (n){

            case 0:
                ft.replace(R.id.main_frame, frag1);
                ft.commit();
                break;
            case 1:
                ft.replace(R.id.main_frame, frag2);
                ft.commit();
                break;
        }
    }



    // POST 통신요청
    public void requestPost(String token) {

        ReqTokenData reqTokenData = new ReqTokenData(token);
        Call<List<ResTrashcanData>> call = api.requestPostLogin(reqTokenData);

        call.enqueue(new Callback<List<ResTrashcanData>>() {
            // 통신성공 후 텍스트뷰에 결과값 출력

            @Override
            public void onResponse(Call<List<ResTrashcanData>> call, Response<List<ResTrashcanData>> response) {
                List<ResTrashcanData> resTrashcanDatas = response.body();


                int idx = 0;

                for (ResTrashcanData resTrashcanData : resTrashcanDatas) {
                    String x = resTrashcanData.getName();
                    String y = resTrashcanData.getPaper_amount();
                    String z = resTrashcanData.getPlastic_amount();

                    name[idx]=x;
                    plastic_amount[idx]=y;
                    paper_amount[idx]=z;

                    idx++;
                }
                bundle.putStringArray("name",name);
                bundle.putStringArray("plastic_amount",plastic_amount);
                bundle.putStringArray("paper_amount",paper_amount);
                frag1.setArguments(bundle);
                frag2.setArguments(bundle);
                setFrag(0);
                Log.d("TEST", "onResponse 후");
            }

            @Override
            public void onFailure(Call<List<ResTrashcanData>> call, Throwable t) {

                Log.d("TEST", "로그찍어봄", t);
            }
        });
    }
}