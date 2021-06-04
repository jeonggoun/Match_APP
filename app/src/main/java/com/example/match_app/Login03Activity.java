package com.example.match_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ListActivity;
import android.app.blob.BlobStoreManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.CarrierConfigManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.match_app.dto.MemberDTO;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.crypto.spec.GCMParameterSpec;


public class Login03Activity extends AppCompatActivity {
    private TextView button1;
    private ListView addr_list;
    double longitude = 0, latitude = 0;
    List<Address> address = null;
    ArrayList<String> addrList = null;
    String addr;
    private DatabaseReference mDatabaseRef;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login03);
        checkDangerousPermissions();
        button1 = findViewById(R.id.button1);
        addr_list = findViewById(R.id.addr_list);
        addrList = new ArrayList<>();

        firebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("matchapp");

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addrList.clear();
                startLocationService();
                getAddress();
                getListView();
            }
        });
    }
    private void getListView() {
        final ArrayAdapter<String> adapter
                = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, addrList);
        addr_list.setAdapter(adapter);
        addr_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                addr = (String) parent.getItemAtPosition(position);
                sendToNext();
            }
        });
    }
    private void startLocationService() {
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        GPSListener gpsListener = new GPSListener();
        long minTime = 1000;
        float minDistance = 100;

        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }

            manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance, gpsListener);
            Location lastLocation=manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if(lastLocation != null){
                latitude = lastLocation.getLatitude();  // 위도
                longitude = lastLocation.getLongitude();  // 경도
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void sendToNext() {
        Intent nextIntent = new Intent(Login03Activity.this, Login04Activity.class);

        MemberDTO dto = new MemberDTO();
        dto.setLatitude(latitude);
        dto.setLongitude(longitude);
        dto.setAddress(addr);
        dto.setEmailId("das");

        nextIntent.putExtra("dto", dto);
        startActivity(nextIntent);
        finish();
    }
    private void getAddress() {
        Geocoder geocoder = new Geocoder(this);
        try {
            address = geocoder.getFromLocation(latitude, longitude, 1);
            if (address.get(0).getSubLocality() == null) {
                String msg = address.get(0).getAdminArea() + " " + address.get(0).getThoroughfare();
                addrList.add(msg.toString());
            } else {
                String msg = address.get(0).getAdminArea() + " " + address.get(0).getSubLocality() + " " + address.get(0).getThoroughfare();
                addrList.add(msg.toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        for (int i=0; i<7; i++) {
            for (int j=0; j<7; j++) {
                try {
/*                 address = geocoder.getFromLocation(latitude, longitude, 1);
                 String msg = address.get(0).getAdminArea()+" "+address.get(0).getSubLocality()+" "+address.get(0).getThoroughfare();
                 txtResult.append(msg);*/
                    double latitudeA = latitude;
                    double longitudeA = longitude;
                    latitudeA = (latitudeA - 0.01*(3-i));
                    longitudeA = (longitudeA - 0.01*(3-j));

                    try {
                    address = geocoder.getFromLocation(latitudeA, longitudeA, 1);
                        int state = 0;
                        if (address.get(0).getSubLocality() == null) {
                            String msg = address.get(0).getAdminArea() + " " + address.get(0).getThoroughfare();
                            for (String addr : addrList) {
                                if (addr.equals(msg.toString())) {
                                    state = 1;
                                    break;
                                }
                            }
                            if(state == 0){
                                addrList.add(msg.toString());
                            }
                        } else {
                            String msg = address.get(0).getAdminArea() + " " + address.get(0).getSubLocality() + " " + address.get(0).getThoroughfare();
                            for (String addr : addrList) {
                                if (addr.equals(msg.toString())) {
                                    state = 1;
                                    break;
                                }
                            }
                            if(state == 0){
                                addrList.add(msg.toString());
                            }
                        }
                    } catch (Exception e) {
                     e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    } //getAddress()
    private class GPSListener implements LocationListener{

        @Override
        public void onLocationChanged(@NonNull Location location) {
            Double latitude = location.getLatitude();  // 위도
            Double longitude = location.getLongitude();  // 경도
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
            Toast.makeText(this, "권한 설정 필요함", Toast.LENGTH_LONG).show();
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


