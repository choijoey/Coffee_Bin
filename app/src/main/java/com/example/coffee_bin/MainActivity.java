package com.example.coffee_bin;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;

public class MainActivity extends AppCompatActivity {

    private String strEmail;
    private String phoneNum;
    private String nickName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        strEmail=intent.getStringExtra("email");
        nickName=intent.getStringExtra("name");
        phoneNum= intent.getStringExtra("phoneNum");
        TextView tv_email=findViewById(R.id.tv_email);
        TextView tv_phoneNum=findViewById(R.id.tv_phoneNum);
        TextView tv_nickname=findViewById(R.id.tv_nickname);

        tv_email.setText(strEmail);
        tv_phoneNum.setText(phoneNum);
        tv_nickname.setText(nickName);

        findViewById(R.id.btn_logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
                    @Override
                    public void onCompleteLogout() {
                        // 로그아웃 성공 시 수행하는 지점
                        finish();

                    }
                });
            }
        });


        Button mapBtn = findViewById(R.id.map_btn);
        Button volumeBtn = findViewById(R.id.volume_btn);
        Button pointCheckBtn = findViewById(R.id.point_check_btn);



        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Toast.makeText(MainActivity.this, "권한 허가", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Toast.makeText(MainActivity.this, "권한 거부\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }

        };

        TedPermission.with(this)
                .setPermissionListener(permissionlistener)
                .setRationaleMessage("구글 로그인을 하기 위해서는 위치 접근 권한이 필요합니다")

                .setDeniedMessage("거부하면 어플을 사용할수 없습니다\n하지만 [설정] > [권한] 에서 권한을 허용할 수 있습니다.")
                .setPermissions(android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                .check();


        //지도 화면으로 전환
        mapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MapActivity.class);
                startActivity(intent);
            }
        });
        //쓰레기통 용량 화면으로 전환
        volumeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, TrashcanvolumeActivity.class);
                startActivity(intent);
            }
        });

        //포인트 내역 확인 화면으로 전환
        pointCheckBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, PointCheckActivity.class);
                startActivity(intent);
            }
        });





    }


}