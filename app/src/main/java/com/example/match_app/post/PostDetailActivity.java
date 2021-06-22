package com.example.match_app.post;



import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.example.match_app.ChattingActivity;
import com.example.match_app.MainActivity;
import com.example.match_app.R;
import com.example.match_app.dto.ChattingDTO;
import com.example.match_app.dto.MemberDTO;
import com.example.match_app.dto.MetaDTO;
import com.example.match_app.dto.PostDTO;
import com.example.match_app.fragment.SearchFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.NotNull;

import static com.example.match_app.MainActivity.user;
public class PostDetailActivity extends AppCompatActivity {
    private static final String TAG = "main:PostDetailActivity";

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference;
    DatabaseReference databaseReference2;
    ImageView ivDetailImage, ivDetailBack;
    PostDTO dto;
    TextView tvDetailNickname, tvDetailTitle, tvDetailGame, tvDetailPlace, tvDetailTime, tvDetailContent, tvDetailFee;
    SupportMapFragment mapFragment;
    GoogleMap map;
    MarkerOptions myMarker;
    LatLng myLoc;
    FragmentTransaction detailMap = getSupportFragmentManager().beginTransaction();

    FrameLayout frameMap;

    public final static String path = "matchapp/ChatMeta";

    public static Context postDetailActivityContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        postDetailActivityContext = this;

        ivDetailImage = findViewById(R.id.ivDetailImage);
        ivDetailBack = findViewById(R.id.ivDetailBack);

        tvDetailNickname = findViewById(R.id.tvDetailNickname);
        tvDetailTitle = findViewById(R.id.tvDetailTitle);
        tvDetailGame = findViewById(R.id.tvDetailGame);
        tvDetailPlace = findViewById(R.id.tvDetailPlace);
        tvDetailTime = findViewById(R.id.tvDetailTime);
        tvDetailContent = findViewById(R.id.tvDetailContent);
        tvDetailFee = findViewById(R.id.tvDetailFee);

        frameMap = findViewById(R.id.frameMap);

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
        tvDetailPlace.setText("모임장소 : " + dto.getPlace());
        tvDetailTime.setText("모임일시 : " + dto.getTime());

        if(dto.getFee().equals("0"))
            tvDetailFee.setText("참가비 없음");
        else
            tvDetailFee.setText("참가비 : " + dto.getFee() + "원");

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
        if(dto.getImgPath() == null) {
            ivDetailImage.setVisibility(View.GONE);
        }

        // 선택한 좌표 없으면 프래그먼트 숨기기
        Log.d(TAG, "onCreate: " + dto.getLatitude());
        Log.d(TAG, "onCreate: " + dto.getLongitude());
        if(dto.getLatitude().equals("0.0") || dto.getLongitude().equals("0.0")){
            frameMap.setVisibility(View.GONE);
        }

        // back 버튼
        ivDetailBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent65=new Intent(this.getIntent());
    }

    public void onPopupButtonClick(View button) {
        //PopupMenu 객체 생성.
        PopupMenu popup = new PopupMenu(this, button);

        //설정한 popup XML을 inflate.
        popup.getMenuInflater().inflate(R.menu.menu_post, popup.getMenu());

        //팝업메뉴 클릭 시 이벤트
        // xml의 onClick 이벤트로 연결해 두었음
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    /*글 수정*/
                    case R.id.update:
                        Log.d(TAG, "수정 클릭");
                        /* update를 선택했을 때 이벤트 실행 코드 작성 */
                        Intent intent = new Intent(getApplicationContext(), PostUpdateActivity.class);
                        intent.putExtra("post", dto);
                        //intent.putExtra("postkey", dto.getPostKey());
                        startActivityForResult(intent, 1);
                        break;

                    /*글 삭제*/
                    case R.id.delete:
                            AlertDialog.Builder builder = new AlertDialog.Builder(PostDetailActivity.this);
                            builder.setTitle("정말 삭제하시겠습니까?")        // 제목 설정
                                    .setCancelable(false)        // 뒤로 버튼 클릭시 취소 가능 설정
                                    .setPositiveButton("확인", new DialogInterface.OnClickListener(){
                                        public void onClick(DialogInterface dialog, int whichButton){

                                            Log.d(TAG, "onClick: " + firebaseDatabase.getReference().child(dto.getPostKey()));

                                            //선생님1     databaseReference = firebaseDatabase.getReference("matchapp/Post");
                                            //                                            databaseReference.child(dto.getPostKey()).removeValue().

                                            //선생님2     firebaseDatabase.getReference("matchapp/Post/" + dto.getPostKey() ).removeValue().

                                            firebaseDatabase.getReference("matchapp/Post/" + dto.getPostKey() ).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Toast.makeText(PostDetailActivity.this, "삭제 성공", Toast.LENGTH_SHORT).show();
                                                    finish();
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(PostDetailActivity.this, "삭제 실패", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                    })
                                    .setNegativeButton("취소", new DialogInterface.OnClickListener(){
                                        public void onClick(DialogInterface dialog, int whichButton){

                                        }
                                    });

                            AlertDialog dialog = builder.create();    // 알림창 객체 생성
                            dialog.show();    // 알림창 띄우기
                        break;
                }
                return true;
            }
        });
        popup.show();
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

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);


    }

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 65) {
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }
    }*/
}

