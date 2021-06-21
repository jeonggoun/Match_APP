package com.example.match_app.post;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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

import com.example.match_app.R;
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

import java.util.UUID;

import static com.example.match_app.MainActivity.user;
import static com.example.match_app.fragment.SearchFragment.items;

public class PostUpdateActivity extends AppCompatActivity {
    private static final String TAG = "PostUpdateActivity 로그";

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference;

    FirebaseStorage storage = FirebaseStorage.getInstance(); //스토리지 인스턴스를 만들고,
    StorageReference storageRef = storage.getReference("matchapp/postImg");//스토리지를 참조한다
    private MemberDTO memberDTO = new MemberDTO();

    String filename;  //ex) profile1.jpg 로그인하는 사람에 따라 그에 식별값에 맞는 프로필 사진 가져오기

    Button cancel, next, selectDateTime, selectPlace;

    PostDTO dto;

    TextView etTitle, etFee, etContent, etPlace, txtResult, alertTitle, mapResult;
    Uri file;
    String imagePath;
    Double longitude=0.0;
    Double latitude=0.0;
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

        Intent intent = getIntent();
        dto = (PostDTO) intent.getSerializableExtra("post");

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

        selectPlace = findViewById(R.id.selectPlace);
        //경고창
        alertTitle = findViewById(R.id.alertTitle);
        mapResult = findViewById(R.id.mapResult);

        //사진 불러올 수 있게 하기
        postImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
                startActivityForResult(intent, GET_GALLERY_IMAGE);
            }
        });

        //취소 버튼 누르면 update액티비티 끈다
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //완료 버튼 누르면 파이어베이스에 보낸다
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
                }else if(result.length() < 1){
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
                    dto.setLatitude(latitude.toString());
                    dto.setLongitude(longitude.toString());
                    dto.setWriter(user.getNickName());
                    dto.setWriterToken(user.getIdToken());

                    if(file != null) {
                        filename = UUID.randomUUID().toString() + ".jpg";
                        dto.setImgPath(filename);
                        StorageReference riversRef = storageRef.child(filename);
                        UploadTask uploadTask = riversRef.putFile(file);
                    }//file 있을 때
                }

                firebaseDatabase.getReference("matchapp/Post/" + dto.getPostKey() ).setValue(dto).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(PostUpdateActivity.this, "수정 성공", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NotNull Exception e) {
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

        selectPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PostMapActivity.class);
                intent.putExtra("latitude", latitude);
                intent.putExtra("longitude", longitude);
                startActivityForResult(intent, 2);
            }
        });

        Log.d(TAG, "/nlatitude: " + latitude);
        Log.d(TAG, "longitude: " + longitude);

        if(latitude != 0.0 || longitude !=0.0) {
            mapResult.setVisibility(View.VISIBLE);
            mapResult.setText("장소가 입력되었습니다");
        }

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
            } else if (requestCode == 2) {
                longitude = data.getDoubleExtra("long", 0);
                latitude = data.getDoubleExtra("lati", 0);

            }

            if (requestCode == 200 && resultCode == RESULT_OK && data.getData() != null) {
//                onActivityResult(requestCode, resultCode, data);
                if(data != null) {
                    file = data.getData();
                    postImage.setImageURI(file);
                }
            }
        }
    }
}