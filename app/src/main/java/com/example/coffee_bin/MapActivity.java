package com.example.coffee_bin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


import android.Manifest;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;

import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private FragmentManager fragmentManager;
    private MapFragment mapFragment;
    public GoogleMap mMap;
    private String token;

    ApiInterfaceMap api;

    // 받을 위도 경도값
    double[] pointX = {0, 0, 0};
    double[] pointY = {0, 0, 0};
    LatLng curLatLng;

    public List<LatLng> points = new ArrayList<LatLng>();

    private int zoomFlag = 1;


    FusedLocationProviderClient client;
    LocationRequest locationRequest;


    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            if (locationResult == null) {
                return;
            }

            for (Location location : locationResult.getLocations()) {


                mapFragment.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap googleMap) {

                        mMap = googleMap;


                        //경도 위도 초기화
                        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                        curLatLng = latLng;
                        //마커 만들기
                        MarkerOptions options = new MarkerOptions().position(latLng).title("현재 위치");

                        //한번만 지도 줌
                        if (zoomFlag == 1) {
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));//가까이 보고싶으면 숫자 올리면되고 멀리 보고싶으면 숫자 내리면됨

                        }
                        //
                        googleMap.clear();

                        googleMap.addMarker(options);
                        int x = 0;
                        for (LatLng point : points) {
                            options = new MarkerOptions().position(point).title("쓰레기통" + (x + 1));
                            options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                            googleMap.addMarker(options);
                            x++;
                        }

                    }
                });
                Log.d("TAG", "onLocationResult: " + location.toString());
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        token = intent.getStringExtra("token");
        api = HttpClient.getRetrofit().create(ApiInterfaceMap.class);
        requestPost(token);


        setContentView(R.layout.activity_map);

        fragmentManager = getFragmentManager();
        mapFragment = (MapFragment) fragmentManager.findFragmentById(R.id.googleMap);
        mapFragment.getMapAsync(this);


        locationRequest = LocationRequest.create();
        locationRequest.setInterval(4000);
        locationRequest.setFastestInterval(2000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);


        //처음 위치
        client = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(MapActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //위치 허락 받았다면
            //getCurrentLocation();

            checkSettingAndStartLocationUpdates();
        }


    }

    // POST 통신요청
    public void requestPost(String token) {

        ReqTokenData reqTokenData = new ReqTokenData(token);
        Call<List<ResMapData>> call = api.requestPostLogin(reqTokenData);

        call.enqueue(new Callback<List<ResMapData>>() {
            // 통신성공 후 텍스트뷰에 결과값 출력

            @Override
            public void onResponse(Call<List<ResMapData>> call, Response<List<ResMapData>> response) {
                List<ResMapData> resMapDatas = response.body();

                int idx = 0;
                for (ResMapData resMapData : resMapDatas) {
                    String x = resMapData.getLatitude();
                    String y = resMapData.getLongitude();
                    pointX[idx] = Double.parseDouble(x);
                    pointY[idx] = Double.parseDouble(y);
                    idx++;
                    System.out.println("dddddddddddddd" + x + "   " + y);

                }
                for (int i = 0; i < pointX.length; i++) {
                    points.add(new LatLng(pointX[i], pointY[i]));
                }
                ;
                //tv_email.setText( response.body().toString() );    // body() - API 결과값을 객체에 맵핑
                Log.d("TEST", "onResponse 후");
            }

            @Override
            public void onFailure(Call<List<ResMapData>> call, Throwable t) {
                //tv_email.setText( "onFailure" );
                Log.d("TEST", "로그찍어봄", t);
            }
        });
    }

    //옵션 메뉴를 구성하기 위해 호출되는 메서드
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.map_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    //메뉴 클릭 시 수행
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //사용자가 선택한 메뉴의 id값 추출
        int id = item.getItemId();
        mMap.stopAnimation();

        switch (id) {

            case R.id.item1:

                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(pointX[0], pointY[0]), 18));
                zoomFlag = 0;
                break;
            case R.id.item2:
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(pointX[1], pointY[1]), 18));
                zoomFlag = 0;
                break;
            case R.id.item3:

                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(pointX[2], pointY[2]), 18));
                zoomFlag = 0;
                break;
            case R.id.item4:
                zoomFlag=1;
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(curLatLng, 18));
                break;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopLocationUpdates();
    }

    private void checkSettingAndStartLocationUpdates() {
        LocationSettingsRequest request = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest).build();
        SettingsClient client = LocationServices.getSettingsClient(this);

        Task<LocationSettingsResponse> locationSettingsResponseTask = client.checkLocationSettings(request);
        locationSettingsResponseTask.addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                //디바이스 세팅 완료 위치 업데이트 시작
                startLocationUpdates();
            }
        });
        locationSettingsResponseTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                //유저에게 세팅하라고 알림
                if (e instanceof ResolvableApiException) {
                    ResolvableApiException apiException = (ResolvableApiException) e;
                    try {
                        apiException.startResolutionForResult(MapActivity.this, 1001);
                    } catch (IntentSender.SendIntentException ex) {
                        ex.printStackTrace();
                    }

                }
            }
        });

    }


    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(MapActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            client.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());


            return;
        }

    }

    private void stopLocationUpdates() {
        client.removeLocationUpdates(locationCallback);
    }

    private void getCurrentLocation() {


        if (ActivityCompat.checkSelfPermission(MapActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Task<Location> mCurrentLocation = client.getLastLocation();
            mCurrentLocation.addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    //성공했을 때
                    if (location != null) {
                        //맵 동기화
                        mapFragment.getMapAsync(new OnMapReadyCallback() {
                            @Override
                            public void onMapReady(GoogleMap googleMap) {
                                //경도 위도 초기화
                                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

                                //마커 만들기
                                MarkerOptions options = new MarkerOptions().position(latLng).title("현재 위치");
                                //지도 줌
                                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));//가까이 보고싶으면 숫자 올리면되고 멀리 보고싶으면 숫자 내리면됨
                                //
                                googleMap.addMarker(options);
                            }
                        });
                    }
                }
            });
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
//        LatLng location = new LatLng(37.391460, 126.648040);//위도 경도 값
//        MarkerOptions markerOptions =new MarkerOptions();
//        markerOptions.title("우리 집");
//        markerOptions.snippet("우리집 상세 설명");
//        markerOptions.position(location);
//        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
//        googleMap.addMarker(markerOptions);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            mMap.setOnMyLocationButtonClickListener((GoogleMap.OnMyLocationButtonClickListener) this);
            mMap.setOnMyLocationClickListener((GoogleMap.OnMyLocationClickListener) this);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            return;
        }


    }


}

