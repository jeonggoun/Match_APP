package com.example.match_app.post;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.match_app.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class PostMapActivity extends AppCompatActivity {

    SupportMapFragment mapFragment;
    GoogleMap map;
    EditText etAddr;

    MarkerOptions myMarker;
    LatLng myLoc, result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_map);

        etAddr = findViewById(R.id.etAddr);

        Intent getIntent = getIntent();
        Double lat = getIntent.getDoubleExtra("latitude", 0), lon = getIntent.getDoubleExtra("longitude", 0);


        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.postMap);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @RequiresApi(api = Build.VERSION_CODES.R)
            @SuppressLint("MissingPermission")
            @Override
            public void onMapReady(GoogleMap googleMap) {

                map = googleMap;
                try{
                    map.setMyLocationEnabled(true);
                }catch (SecurityException e){

                }
                map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        result = latLng;
                        Location targetLocation = new Location("");
                        targetLocation.setLatitude(latLng.latitude);
                        targetLocation.setLongitude(latLng.longitude);
                        showMyLocationMarker(targetLocation);
                    }
                });

                if(lat !=0 && lon != 0){
                    result = new LatLng(lat, lon); // 찍어진좌표 보관

                    Location targetLocation = new Location("");
                    targetLocation.setLatitude(result.latitude);
                    targetLocation.setLongitude(result.longitude); //이동하고 찍을 좌표 설정
                    showMyLocationMarker(targetLocation);  //마커찍기
                    showCurrentLocation(targetLocation);  //위치로 이동
                }else{
                    LocationManager manager =
                            (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    showCurrentLocation(manager.getLastKnownLocation(LocationManager.GPS_PROVIDER));
                }
            }
        });

        // 구글맵 초기화
        MapsInitializer.initialize(this);

        // 한글주소를 지도로 보여주기
        findViewById(R.id.btnSearchMap).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etAddr.getText().toString().length() > 0){
                    Location location = getLocationFromAddress
                            (getApplicationContext(), etAddr.getText().toString());

                    showCurrentLocation(location);
                }
            }
        });

        findViewById(R.id.btnSubmitMap).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("lati", result.latitude);
                intent.putExtra("long", result.longitude);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

    }
    private Location getLocationFromAddress(Context context, String address) {
        Geocoder geocoder = new Geocoder(context);
        List<Address> addresses;
        Location resLocation = new Location("");

        try{
            addresses = geocoder.getFromLocationName(address, 5);
            if((addresses == null) && (addresses.size() == 0)){
                return null;
            }
            Address addressLoc = addresses.get(0);
            resLocation.setLatitude(addressLoc.getLatitude());
            resLocation.setLongitude(addressLoc.getLongitude());

        }catch (IOException e){

        }
        return resLocation;
    }

    private void showCurrentLocation(Location location) {
        LatLng curPoint =
                new LatLng(location.getLatitude(), location.getLongitude());
        // 현재 내위치 전역변수에 넣음
        myLoc = curPoint;
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(curPoint, 18));
    }
    private void showMyLocationMarker(Location location){

        if(myMarker == null){
            myMarker = new MarkerOptions();
            myMarker.position(
                    new LatLng(location.getLatitude(), location.getLongitude()));
            myMarker.title("위치");
            myMarker.icon
                    (BitmapDescriptorFactory.fromResource(R.drawable.mylocation));
            map.addMarker(myMarker);
        }else{
            myMarker.position(new LatLng(location.getLatitude(), location.getLongitude()));
            map.clear();
            map.addMarker(myMarker);
        }

    }

}