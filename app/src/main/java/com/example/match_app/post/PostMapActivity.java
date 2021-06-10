package com.example.match_app.post;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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

    LatLng myLoc, markerLoc, result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_map);

        etAddr = findViewById(R.id.etAddr);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.postMap);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
//                Log.d(TAG, "onMapReady: Google Map is Ready !!!" );

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
                Intent intent = new Intent(PostMapActivity.this, PostWriteActivity.class);
                startActivity(intent);
                finish();
            }//todo
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

    private void requestMyLocation() {
        LocationManager manager =
                (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        try{
            long minTime = 0;
            float minDistance = 0;

            manager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    minTime,
                    minDistance,
                    new LocationListener() {
                        @Override
                        public void onLocationChanged(@NonNull Location location) {
                            showCurrentLocation(location);
                        }
                    }
            );//todo 반복되므로 해제해줘야함

            Location lastLocation =
                    manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(lastLocation != null){
                String msg = "Latitude : " + lastLocation.getLatitude()
                        + "\nLongitude : " + lastLocation.getLongitude();
            }

        }catch (SecurityException e){

        }

    }

    private void showCurrentLocation(Location location) {
        LatLng curPoint =
                new LatLng(location.getLatitude(), location.getLongitude());
        // 현재 내위치 전역변수에 넣음
        myLoc = curPoint;

        String msg = "Latitude : " + curPoint.latitude
                + "\nLongitude : " + curPoint.longitude;

        map.animateCamera(CameraUpdateFactory.newLatLngZoom(curPoint, 18));

    }
    private void showMyLocationMarker(Location location){

        if(myMarker == null){
            myMarker = new MarkerOptions();
            myMarker.position(
                    new LatLng(location.getLatitude(), location.getLongitude()));
            myMarker.title("위치\n");
            myMarker.icon
                    (BitmapDescriptorFactory.fromResource(R.drawable.mylocation));
            map.addMarker(myMarker);
            myMarker = null;
        }

    }
    private void checkDangerousPermissions() {
        String[] permissions = {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        };

        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        for (int i = 0; i < permissions.length; i++) {
            permissionCheck = ContextCompat.checkSelfPermission(this, permissions[i]);
            if (permissionCheck == PackageManager.PERMISSION_DENIED) {
                break;
            }
        }

        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "권한 있음", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "권한 없음", Toast.LENGTH_LONG).show();

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0])) {
                Toast.makeText(this, "권한 설명 필요함.", Toast.LENGTH_LONG).show();
            } else {
                ActivityCompat.requestPermissions(this, permissions, 1);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, permissions[i] + " 권한이 승인됨.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, permissions[i] + " 권한이 승인되지 않음.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

}