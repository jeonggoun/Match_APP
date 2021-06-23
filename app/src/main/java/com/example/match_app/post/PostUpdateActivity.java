package com.example.match_app.post;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.match_app.MainActivity;
import com.example.match_app.R;
import com.example.match_app.asynctask.post.PostDetail;
import com.example.match_app.dto.MemberDTO;
import com.example.match_app.dto.PostDTO;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.UUID;

import static com.example.match_app.MainActivity.user;
import static com.example.match_app.fragment.SearchFragment.items;

public class PostUpdateActivity extends AppCompatActivity {
    private static final String TAG = "main:PostUpdateActivity";

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference;

    FirebaseStorage storage = FirebaseStorage.getInstance(); //스토리지 인스턴스를 만들고,
    StorageReference storageRef = storage.getReference("matchapp/postImg");//스토리지를 참조한다
    private MemberDTO memberDTO;

    String filename;  //ex) profile1.jpg 로그인하는 사람에 따라 그에 식별값에 맞는 프로필 사진 가져오기

    Button cancel, next, selectDateTime, selectPlace;

    PostDTO dto, update;

    TextView etTitle, etFee, etContent, etPlace, txtResult, alertTitle;
    Uri file;
    String imagePath;
    /*맵
    Double longitude=0.0;
    Double latitude=0.0;*/

    //스피너
    Spinner spinnerGame;
    String selectGame , result="";

    //이미지뷰
    ImageView postImage;
    private final int GET_GALLERY_IMAGE = 200;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_update);
        databaseReference = firebaseDatabase.getReference("matchapp/Post");

        //dto = new PostDTO();

        //데이터 가져오기
        Intent intent = getIntent();
        dto = (PostDTO) intent.getSerializableExtra("post");
//        Log.d(TAG, "디티오: " + dto.getGame());

        //버튼 찾기
        cancel = findViewById(R.id.cancel);
        next = findViewById(R.id.next);
        etTitle = findViewById(R.id.etTitle);
        etFee = findViewById(R.id.etFee);
        etContent = findViewById(R.id.etContent);
        etPlace = findViewById(R.id.etPlace);

        postImage = findViewById(R.id.postImage);

        //일시 선택 버튼
        selectDateTime = findViewById(R.id.selectDateTime);
        txtResult = findViewById(R.id.txtResult);

        /*맵
        selectPlace = findViewById(R.id.selectPlace);*/

        //경고창
        alertTitle = findViewById(R.id.alertTitle);


        //사진 불러올 수 있게 하기
        postImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
                startActivityForResult(intent, GET_GALLERY_IMAGE);
            }
        });

        // 기존 글 제목 불러오기
        etTitle.setText(dto.getTitle());
        etFee.setText(dto.getFee());
        etContent.setText(dto.getContent());
        etPlace.setText(dto.getPlace());
        txtResult.setText(dto.getTime());

        if(dto.getImgPath() != null) {
            String filePath = "https://firebasestorage.googleapis.com/v0/b/match-app-b8c4a.appspot.com/o/matchapp%2FpostImg%2F" + dto.getImgPath() + "?alt=media";
            Glide.with(this).load(filePath).into(postImage);
        }

        //취소 버튼 누르면 update액티비티 끈다
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //수정 버튼 누르면 파이어베이스에 보낸다
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etTitle.getText().toString().trim().length() < 1){
                    alertTitle.setVisibility(View.VISIBLE);
                    alertTitle.setText("제목을 입력해주세요");
                    return;
                }else if(selectGame.equals("전체")){
                    alertTitle.setVisibility(View.VISIBLE);
                    alertTitle.setText("종목을 선택해주세요");
                    return;
                }else if(txtResult.toString().trim().length() < 1){
                    alertTitle.setVisibility(View.VISIBLE);
                    alertTitle.setText("일시를 선택해주세요");
                    return;
                }else if(etPlace.getText().toString().trim().length() < 1){
                    alertTitle.setVisibility(View.VISIBLE);
                    alertTitle.setText("장소를 입력해주세요");
                    return;
                }else if(etContent.getText().toString().trim().length() < 1){
                    alertTitle.setVisibility(View.VISIBLE);
                    alertTitle.setText("내용을 입력해주세요");
                    return;
                }else{
                    alertTitle.setVisibility(View.GONE);
                    if(etFee.getText().toString().trim().length() < 1 ||
                            Integer.parseInt(etFee.getText().toString()) < 1){
                        etFee.setText("0");
                    }
                    dto.setGame(selectGame);
                    dto.setFee(etFee.getText().toString());
                    dto.setTitle(etTitle.getText().toString());
                    dto.setTime(txtResult.getText().toString());
                    //calendar.getDate();
                    dto.setPlace(etPlace.getText().toString());
                    dto.setContent(etContent.getText().toString());
                    /*맵
                    dto.setLatitude(latitude.toString());
                    dto.setLongitude(longitude.toString());*/
                    dto.setWriter(user.getNickName());
                    dto.setWriterToken(user.getIdToken());

                    if(file != null) {
                        filename = UUID.randomUUID().toString() + ".jpg";
                        dto.setImgPath(filename);
                        StorageReference riversRef = storageRef.child(filename);
                        UploadTask uploadTask = riversRef.putFile(file);
                    }//file 있을 때

//                    Log.d(TAG, "onClick: dto.getPostKey()" + dto.getPostKey());
                }

                firebaseDatabase.getReference("matchapp/Post/" + dto.getPostKey()).setValue(dto).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(PostUpdateActivity.this, "수정 성공", Toast.LENGTH_SHORT).show();
//                        Log.d(TAG, "onSuccess: 수정 성공" + dto.getTitle());

                        Intent mainIntent = new Intent(PostUpdateActivity.this, MainActivity.class);
                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        mainIntent.putExtra("requestCode", 100);
                        startActivity(mainIntent);
                        finish();


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NotNull Exception e) {
//                        Log.d(TAG, "onFailure: " + e.getMessage());
                        Toast.makeText(PostUpdateActivity.this, "수정 실패", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        //스피너 찾아주기
        spinnerGame = findViewById(R.id.spinnerGame);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, items);
        arrayAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);

        //스피너에 어댑터 설정
        spinnerGame.setAdapter(arrayAdapter);

        int dtoPosition = arrayAdapter.getPosition(dto.getGame());
//        Log.d(TAG, "onCreate: dtoPos => " + dtoPosition);
        spinnerGame.setSelection(dtoPosition);

        spinnerGame.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectGame = items[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        selectDateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PostDateActivity.class);
                intent.putExtra("data", "Test Popup");
                startActivityForResult(intent, 1);
            }
        });

        /*맵
        selectPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PostMapActivity.class);
                intent.putExtra("latitude", latitude);
                intent.putExtra("longitude", longitude);
                startActivityForResult(intent, 2);
            }
        });

        if(latitude != 0.0 || longitude !=0.0) {
            mapResult.setVisibility(View.VISIBLE);
            mapResult.setText("장소가 입력되었습니다");
        }*/
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK) {
            if (requestCode == 1) {
                if (resultCode == RESULT_OK) {
                    //데이터 받기
                    result = data.getStringExtra("result");
                    txtResult.setText(result);
                }
            }
/*            맵
            else if (requestCode == 2) {
                longitude = data.getDoubleExtra("long", 0);
                latitude = data.getDoubleExtra("lati", 0);

            }*/

            if (requestCode == 200 && resultCode == RESULT_OK && data.getData() != null) {
//                onActivityResult(requestCode, resultCode, data);
                if(data != null) {
                    file = data.getData();
                    postImage.setImageURI(file);
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();


    }
}