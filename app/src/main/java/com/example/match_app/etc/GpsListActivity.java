package com.example.match_app.etc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Image;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.match_app.R;
import com.example.match_app.login.Login03Activity;
import com.example.match_app.post.PostUpdateActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import static com.example.match_app.Common.CommonMethod.memberDTO;

public class GpsListActivity extends AppCompatActivity implements OnMapReadyCallback {
    private double longitude=0, latitude = 0;
    private DatabaseReference mDatabaseRef;
    private FirebaseAuth firebaseAuth;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); // 로그인한 유저의 정보 가져오기
    String uid = user != null ? user.getUid() : null; // 로그인한 유저의 고유 uid 가져오기

    private List<Address> address = null;
    TextView tv_message, tv_message2, finish, finish2, auth_status;
    String msg;
    ImageView auth_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps_list);

        tv_message = findViewById(R.id.tv_message);
        tv_message2 = findViewById(R.id.tv_message2);
        finish = findViewById(R.id.finish);
        finish2 = findViewById(R.id.finish2);
        checkDangerousPermissions();
        startLocationService();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getView().setVisibility(View.VISIBLE);

        firebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("matchapp");

        mapFragment.getMapAsync(GpsListActivity.this::onMapReady);
        getAddress();
        getNeighbor();

        tv_message.setText(Html.fromHtml("<B>동네 인증을 하는 이유?</B>"));
        tv_message.setTextColor(Color.BLACK);
        tv_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GpsListActivity.this, Btn06.class);
                intent.putExtra("question_gps", "동네 인증을 하는 이유");
                startActivity(intent);
            }
        });
        auth_img = findViewById(R.id.auth_img);
        auth_status = findViewById(R.id.auth_status);
        if (memberDTO.isAddrAuth()==true) {
            auth_status.setText("인증된 회원");
            auth_status.setTextColor(Color.BLACK);
            auth_img.setImageResource(R.drawable.icon_successed);
        }else if (memberDTO.isAddrAuth()!=true){
            auth_status.setText("미인증 회원");
            auth_status.setTextColor(Color.BLACK);
            auth_img.setImageResource(R.drawable.icon_failed);
        }
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(GpsListActivity.this, "동네인증 실패!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        finish2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                memberDTO.setAddrAuth(true);
                mDatabaseRef.child("UserAccount").child(uid).setValue(memberDTO);
                Toast.makeText(GpsListActivity.this, "동네인증 성공!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void getAddress() {
        Geocoder geocoder = new Geocoder(this);
        try {
            address = geocoder.getFromLocation(latitude, longitude, 1);
            if (address.get(0).getSubLocality() == null) {
                msg = address.get(0).getAdminArea() + " " + address.get(0).getThoroughfare();
            } else {
                String msg = address.get(0).getAdminArea() + " " + address.get(0).getSubLocality() + " " + address.get(0).getThoroughfare();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    } //getAddress()

    private void getNeighbor() {
        if (memberDTO.getLatitude()-0.03 < latitude && memberDTO.getLatitude()+0.03 > latitude &&
                memberDTO.getLongitude()-0.03 < longitude && memberDTO.getLongitude()+0.03 > longitude) {
            tv_message2.setText(Html.fromHtml("현재 위치가 동네로 설정한 "+"<B>'"+memberDTO.getAddress()+"</B>"+"' 근처에 있어요."));
            finish.setVisibility(View.GONE);
            finish2.setVisibility(View.VISIBLE);
        }else {
            tv_message2.setText(Html.fromHtml("현재 위치가 동네로 설정한 "+"<B>'"+memberDTO.getAddress()+"'</B>"+" 에서 멀리 떨어져 있어요."));
        }

    }

    private void startLocationService() {
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        GpsListActivity.GPSListener gpsListener = new GpsListActivity.GPSListener();
        long minTime = 1000;
        float minDistance = 100;

        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }

            manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance, gpsListener);
            Location lastLocation = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if (lastLocation != null) {
                latitude = lastLocation.getLatitude();  // 위도
                longitude = lastLocation.getLongitude();  // 경도
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng location = new LatLng(latitude, longitude);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.title("내 위치");
        markerOptions.position(location);
        googleMap.addMarker(markerOptions);
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 14));
    }

    private class GPSListener implements LocationListener {
        @Override
        public void onLocationChanged(@NonNull Location location) {
            latitude = location.getLatitude();  // 위도
            longitude = location.getLongitude();  // 경도
        }
    }

    private void checkDangerousPermissions() {
        String[] permissions = {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_WIFI_STATE
        };

        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        for (int i = 0; i < permissions.length; i++) {
            permissionCheck = ContextCompat.checkSelfPermission(this, permissions[i]);
            if (permissionCheck == PackageManager.PERMISSION_DENIED) {
                break;
            }
        }

        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
        } else {
            /*Toast.makeText(this, "권한 설정 필요함", Toast.LENGTH_LONG).show();*/
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0])) {
                /*Toast.makeText(this, "권한 설명 필요함.", Toast.LENGTH_LONG).show();*/
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
                    /*Toast.makeText(this, permissions[i] + " 권한이 승인됨.", Toast.LENGTH_LONG).show();*/
                } else {
                    /*Toast.makeText(this, permissions[i] + " 권한이 승인되지 않음.", Toast.LENGTH_LONG).show();*/
                }
            }
        }
    }


}