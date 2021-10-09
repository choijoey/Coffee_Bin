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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private FragmentManager fragmentManager;
    private MapFragment mapFragment;



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
                        //경도 위도 초기화
                        LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
                        //마커 만들기
                        MarkerOptions options = new MarkerOptions().position(latLng).title("현재 위치");
                        //지도 줌
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,16));//가까이 보고싶으면 숫자 올리면되고 멀리 보고싶으면 숫자 내리면됨
                        //
                        googleMap.clear();
                        googleMap.addMarker(options);

                    }
                });
                Log.d("TAG","onLocationResult: "+location.toString());
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

