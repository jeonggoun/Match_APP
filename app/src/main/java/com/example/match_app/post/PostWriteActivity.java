package com.example.match_app.post;

//todo 글 수정 시 가지고 있는 종목값 스피너가 나타나게 하기
//todo 글쓴 사람에게만 수정/삭제 나오도록 하기
//todo map 첨부되면 지도가 첨부되었습니다 나오게 하기
//todo map 검색 안 되는 경우 오류처리
//todo map, 장소 따로 입력해야 하는 상황 ▶ 어떻게 하면 좋을지 팀원들 조언 구하기

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.match_app.R;
import com.example.match_app.asynctask.post.PostWrite;
import com.example.match_app.dto.MemberDTO;
import com.example.match_app.dto.PostDTO;
import com.example.match_app.fragment.SearchFragment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

import static com.example.match_app.fragment.SearchFragment.items;
import static com.example.match_app.MainActivity.user;
public class PostWriteActivity extends AppCompatActivity {
    private static final String TAG = "로그 PostWriteActivity";

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference;

    //storage 객체 만들고 참조 => 이미지 저장하려고
    FirebaseStorage storage = FirebaseStorage.getInstance(); //스토리지 인스턴스를 만들고,
    StorageReference storageRef = storage.getReference("matchapp/postImg");//스토리지를 참조한다
    private MemberDTO memberDTO = new MemberDTO();

    String filename;  //ex) profile1.jpg 로그인하는 사람에 따라 그에 식별값에 맞는 프로필 사진 가져오기

    Button cancel, next, selectDateTime, selectPlace;

    private PostDTO dto;
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);
        databaseReference = firebaseDatabase.getReference("matchapp/Post");

        //memberDTO.setIdToken("test1");

        dto = new PostDTO();

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

        //취소 버튼 누르면 write액티비티 끈다
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
                // 새로운 프로필 이미지 저장
                // Register observers to listen for when the download is done or if it fails
//                uploadTask.addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception exception) {
//                    }
//                });
//                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                        //Toast.makeText(getParent(), "프로필 이미지가 변경되었습니다.", Toast.LENGTH_SHORT).show();
//                    }
//                });
                databaseReference.push().setValue(dto);
//                databaseReference.child(dto.getPostKey()).setValue(dto);  //글 수정시
//                databaseReference.child(dto.getPostKey()).removeValue();  //글 삭제시

                finish();

//        ListItemDTO dto0 = new ListItemDTO(0, "테니스", "테니스 치실 분", "2021/5/26", "농성테니스장", "무료", "#");
//        adapter.addDto(dto0);    //어디 DTO 받아올 건지 물어볼 예정
//        databaseReference.push().setValue(dto0);
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