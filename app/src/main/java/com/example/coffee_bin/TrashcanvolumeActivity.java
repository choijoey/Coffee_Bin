package com.example.coffee_bin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.List;

public class TrashcanvolumeActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private FragmentManager fm;
    private FragmentTransaction ft;
    private Frag1 frag1;
    private Frag2 frag2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trashcan_volume);





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
        setFrag(0); //첫 화면은 frag1
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
}