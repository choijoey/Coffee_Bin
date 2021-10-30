package com.example.coffee_bin;

import androidx.activity.result.IntentSenderRequest;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;


import android.Manifest;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;

import android.nfc.Tag;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private FragmentManager fragmentManager;
    private MapFragment mapFragment;
    public GoogleMap mMap;
    // 받을 위도 경도값
    double pointX[]={37.39180718331199,37.39338852171473, 37.38935401540017};
    double pointY[]={126.65054524412689,126.64819762346761,126.65118318449554};
    public static final CameraPosition SYDNEY =
            new CameraPosition.Builder().target(new LatLng(-33.87365, 151.20689))
                    .zoom(15.5f)
                    .bearing(0)
                    .tilt(25)
                    .build();
    public List<LatLng> points=new ArrayList<LatLng>();

    private int mCurrentAnimationIndex = -1;

    FusedLocationProviderClient client;
    LocationRequest locationRequest;

    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            if(locationResult == null){
                return;
            }

            for(Location location: locationResult.getLocations()){


                mapFragment.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap googleMap) {
                        mMap=googleMap;
                        //경도 위도 초기화
                        LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
                        //마커 만들기
                        MarkerOptions options = new MarkerOptions().position(latLng).title("현재 위치");
                        //지도 줌
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,18));//가까이 보고싶으면 숫자 올리면되고 멀리 보고싶으면 숫자 내리면됨
                        //
                        googleMap.clear();

                        googleMap.addMarker(options);
                        int x=0;
                        for(LatLng point : points){
                            options = new MarkerOptions().position(point).title("쓰레기통"+(x+1));
                            options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                            googleMap.addMarker(options);
                            x++;
                        }

                    }
                });
                Log.d("TAG","onLocationResult: "+location.toString());
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        for (int i = 0 ; i < pointX.length; i++){
            points.add(new LatLng(pointX[i],pointY[i]));
        };


        setContentView(R.layout.activity_map);

        fragmentManager = getFragmentManager();
        mapFragment = (MapFragment) fragmentManager.findFragmentById(R.id.googleMap);
        mapFragment.getMapAsync(this);


        locationRequest = LocationRequest.create();
        locationRequest.setInterval(8000);
        locationRequest.setFastestInterval(4000);
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

    //옵션 메뉴를 구성하기 위해 호출되는 메서드
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.map_menu,menu);

        return super.onCreateOptionsMenu(menu);
    }
    //메뉴 클릭 시 수행
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //사용자가 선택한 메뉴의 id값 추출
        int id = item.getItemId();
        mMap.stopAnimation();

        switch (id){

            case R.id.item1:

                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(pointX[0],pointY[0]),18));
                break;
            case R.id.item2:
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(pointX[1],pointY[1]),18));
                break;
            case R.id.item3:

                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(pointX[2],pointY[2]),18));
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
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            client.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());



            return;
        }

    }
    private void stopLocationUpdates(){
        client.removeLocationUpdates(locationCallback);
    }

    private void getCurrentLocation(){




        if(ActivityCompat.checkSelfPermission(MapActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
        Task<Location> mCurrentLocation = client.getLastLocation();
            mCurrentLocation.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                //성공했을 때
                if(location != null){
                    //맵 동기화
                    mapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap googleMap) {
                            //경도 위도 초기화
                            LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());

                            //마커 만들기
                            MarkerOptions options = new MarkerOptions().position(latLng).title("현재 위치");
                            //지도 줌
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,16));//가까이 보고싶으면 숫자 올리면되고 멀리 보고싶으면 숫자 내리면됨
                            //
                            googleMap.addMarker(options);
                        }
                    });
                }
            }
        });
    }}
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        LatLng location = new LatLng(37.391460, 126.648040);//위도 경도 값
        MarkerOptions markerOptions =new MarkerOptions();
        markerOptions.title("우리 집");
        markerOptions.snippet("우리집 상세 설명");
        markerOptions.position(location);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        googleMap.addMarker(markerOptions);


    }


}

