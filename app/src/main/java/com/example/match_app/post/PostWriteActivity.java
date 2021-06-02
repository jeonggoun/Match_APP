package com.example.match_app.post;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.match_app.Common.CommonMethod;
import com.example.match_app.MainActivity;
import com.example.match_app.R;
import com.example.match_app.dto.MemberDTO;
import com.example.match_app.dto.PostDTO;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.UUID;

public class PostWriteActivity extends AppCompatActivity {
    private static final String TAG = "로그 PostWriteActivity";

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference;

    //storage 객체 만들고 참조 => 이미지 저장하려고
    FirebaseStorage storage = FirebaseStorage.getInstance(); //스토리지 인스턴스를 만들고,
    StorageReference storageRef = storage.getReference("matchapp/postImg");//스토리지를 참조한다
    private MemberDTO memberDTO = new MemberDTO();

    String filename;  //ex) profile1.jpg 로그인하는 사람에 따라 그에 식별값에 맞는 프로필 사진 가져오기

    Button cancel, next;

    private PostDTO dto;
    TextView etTitle, etFee, etContent;
    DatePicker datePicker;
    String date = "";
    Uri file;
    String imagePath;

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




        datePicker = (DatePicker) findViewById(R.id.datePicker);

        Date tempDate = new Date();
        date = new DecimalFormat("0000").format(tempDate.getYear()) + "/" +
                new DecimalFormat("00").format(tempDate.getMonth() + 1)
                + "/" + new DecimalFormat("00").format(tempDate.getDay());

        datePicker.init(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(),
                new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker datePicker, int year, int month, int day) {
                        date = new DecimalFormat("0000").format(year) + "/" +
                                new DecimalFormat("00").format(month + 1)
                                + "/" + new DecimalFormat("00").format(day);
                    }
                });

        dto = new PostDTO();

        //버튼 찾기
        cancel = findViewById(R.id.cancel);
        next = findViewById(R.id.next);
        etTitle = findViewById(R.id.etTitle);
        etFee = findViewById(R.id.etFee);
        etContent = findViewById(R.id.etContent);

        postImage = findViewById(R.id.postImage);

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
                dto.setGame("모름");
                dto.setFee(etFee.getText().toString());
                dto.setTitle(etTitle.getText().toString());
                dto.setTime(new Date().toString());
                //calendar.getDate();
                dto.setPlace("모름");
                dto.setContent(etContent.getText().toString());
                filename = UUID.randomUUID().toString() + ".jpg";
                dto.setImgPath(filename);

                //Log.d("유알", String.valueOf(file));

                StorageReference riversRef = storageRef.child(filename);
                UploadTask uploadTask = riversRef.putFile(file);

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

                finish();

//        ListItemDTO dto0 = new ListItemDTO(0, "테니스", "테니스 치실 분", "2021/5/26", "농성테니스장", "무료", "#");
//        adapter.addDto(dto0);    //어디 DTO 받아올 건지 물어볼 예정
//        databaseReference.push().setValue(dto0);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            Log.d(TAG, "onClick: ");
            if(data != null) {
                file = data.getData();
                postImage.setImageURI(file);
            }
    }
}