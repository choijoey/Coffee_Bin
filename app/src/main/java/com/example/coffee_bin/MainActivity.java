package com.example.coffee_bin;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import android.provider.Settings.Secure;

public class MainActivity extends AppCompatActivity {

    private String strEmail;
    private String phoneNum;
    private String nickName;
    private String serial;

    TextView tv_email;
    //TextView tv_serialNum;
    ApiInterface api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        //TelephonyManager tm =
         //       (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        //serial = Secure.getString(this.getContentResolver(), Secure.ANDROID_ID);
        //https://stackoverflow.com/questions/4799394/is-secure-android-id-unique-for-each-device

        //액션바 숨기기
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        //api 생성
        api = HttpClient.getRetrofit().create( ApiInterface.class );


        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        strEmail=intent.getStringExtra("email");
        nickName=intent.getStringExtra("name");
        phoneNum= intent.getStringExtra("phoneNum");


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

        // 서버에 데이터 전송 ,토큰 값 받아오기
        requestPost();


        ImageButton mapBtn = findViewById(R.id.map_btn);
        ImageButton volumeBtn = findViewById(R.id.volume_btn);
        ImageButton pointCheckBtn = findViewById(R.id.point_check_btn);



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

    // POST 통신요청
    public void requestPost() {

        ReqLoginData reqLoginData = new ReqLoginData( phone_format(phoneNum),strEmail);

        Call<ResLoginData> call = api.requestPostLogin( reqLoginData );

        Log.d("TEST","requestPost 전");
        call.enqueue( new Callback<ResLoginData>() {
            // 통신성공 후 텍스트뷰에 결과값 출력

            @Override
            public void onResponse(Call<ResLoginData> call, Response<ResLoginData> response) {
                Log.d("TEST","onResponse 전");
                System.out.println("dddddddddddddd"+response.body());
                //tv_email.setText( response.body().toString() );    // body() - API 결과값을 객체에 맵핑
                Log.d("TEST","onResponse 후");
            }

            @Override
            public void onFailure(Call<ResLoginData> call, Throwable t) {
                //tv_email.setText( "onFailure" );
                Log.d("TEST","로그찍어봄",t);
            }
        } );
    }

    public String phone_format(String number) {
        String regEx = "(\\d{3})(\\d{3,4})(\\d{4})"; return number.replaceAll(regEx, "$1-$2-$3"); }


}