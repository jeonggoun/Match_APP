package com.example.match_app.post;



import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.example.match_app.ChattingActivity;
import com.example.match_app.R;
import com.example.match_app.dto.ChattingDTO;
import com.example.match_app.dto.MemberDTO;
import com.example.match_app.dto.MetaDTO;
import com.example.match_app.dto.PostDTO;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.example.match_app.MainActivity.user;
public class PostDetailActivity extends AppCompatActivity {
    private static final String TAG = "main:PostDetailActivity";

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference;
    DatabaseReference databaseReference2;
    ImageView ivDetailImage;
    PostDTO dto;
    TextView tvDetailNickname, tvDetailTitle, tvDetailGame, tvDetailPlace, tvDetailTime, tvDetailContent;
    SupportMapFragment mapFragment;
    GoogleMap map;
    MarkerOptions myMarker;
    LatLng myLoc;

    public final static String path = "matchapp/ChatMeta";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        ivDetailImage = findViewById(R.id.ivDetailImage);

        tvDetailNickname = findViewById(R.id.tvDetailNickname);
        tvDetailTitle = findViewById(R.id.tvDetailTitle);
        tvDetailGame = findViewById(R.id.tvDetailGame);
        tvDetailPlace = findViewById(R.id.tvDetailPlace);
        tvDetailTime = findViewById(R.id.tvDetailTime);
        tvDetailContent = findViewById(R.id.tvDetailContent);

        findViewById(R.id.btnChat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!user.getIdToken().equals(dto.getWriterToken())){
                    startChatting(dto);
                }
            }
        });

        Intent intent = getIntent();
        dto = (PostDTO) intent.getSerializableExtra("post");

        //https://firebasestorage.googleapis.com/v0/b/match-app-b8c4a.appspot.com/o/matchapp%2FpostImg%2F
        // 040ccb4d-ac06-4cf2-80d9-eb744a63ea28.jpg
        // ?alt=media

        String filePath = "https://firebasestorage.googleapis.com/v0/b/match-app-b8c4a.appspot.com/o/matchapp%2FpostImg%2F"+dto.getImgPath()+"?alt=media";
        Glide.with(this).load(filePath).into(ivDetailImage);

        tvDetailTitle.setText(dto.getTitle());
        tvDetailGame.setText(dto.getGame());
        tvDetailContent.setText(dto.getContent());
        tvDetailNickname.setText(dto.getWriter());
        tvDetailPlace.setText(dto.getPlace());
        tvDetailTime.setText(dto.getTime());

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.detailMap);
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
                Location targetLocation = new Location("");
                targetLocation.setLatitude(Double.parseDouble(dto.getLatitude()));
                targetLocation.setLongitude(Double.parseDouble(dto.getLongitude())); //이동하고 찍을 좌표 설정
                showMyLocationMarker(targetLocation);  //마커찍기
                showCurrentLocation(targetLocation);  //위치로 이동

            }
        });

        // 구글맵 초기화
        MapsInitializer.initialize(this);

        // 이미지 없으면
        if(filePath == null) {
            ivDetailImage.setVisibility(View.GONE);
        }


    }
    private void startChatting(PostDTO postDTO){
        MetaDTO meta = new MetaDTO();
        meta.setTitle(postDTO.getTitle());
        meta.setDate(postDTO.getTime());
        meta.setGame(postDTO.getGame());
        meta.setChatToken(dto.getWriterToken());
        meta.setPostToken(postDTO.getPostKey());
        ChattingDTO chattingDTO = new ChattingDTO();
        chattingDTO.setNickname(user.getNickName());
        meta.setRecent(chattingDTO);
        databaseReference2 = firebaseDatabase.getReference(path+"/"+postDTO.getWriterToken());
        databaseReference2.child(user.getIdToken()).setValue(meta);
        chattingDTO.setNickname(dto.getWriter());
        databaseReference = firebaseDatabase.getReference(path+"/"+user.getIdToken());  // user가 존재하지 않아 동작하지 않음
        databaseReference.child(postDTO.getWriterToken()).setValue(meta);

        Intent intent = new Intent(this , ChattingActivity.class);
        intent.putExtra("meta", meta);
        startActivity(intent);
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

