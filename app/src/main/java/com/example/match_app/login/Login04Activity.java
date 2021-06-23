package com.example.match_app.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.match_app.R;
import com.example.match_app.dto.MemberDTO;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

import static com.example.match_app.Common.CommonMethod.memberDTO;
import static com.example.match_app.MainActivity.user;

public class Login04Activity extends AppCompatActivity {
    private final int GET_GALLERY_IMAGE = 200;

    private DatabaseReference mDatabaseRef;
    private FirebaseAuth firebaseAuth;
    private StorageReference storageRef;

    String filename;  //ex) profile1.jpg 로그인하는 사람에 따라 그에 식별값에 맞는 프로필 사진 가져오기
    String filepath;
    Uri file;

    ImageView profilePic, iv_camera;

    private TextView auth_finish;
    private EditText et_01;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login04);

        et_01 = findViewById(R.id.et_01);
        auth_finish = findViewById(R.id.auth_finish);
        profilePic = findViewById(R.id.profilePic);
        iv_camera = findViewById(R.id.iv_camera);
        firebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("matchapp");
        storageRef = FirebaseStorage.getInstance().getReference("matchapp/profileImg");


//-------------------------------------------------------------------------------------------------

        auth_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nickName = et_01.getText().toString();
                if (nickName.trim().length() < 1) {
                    Toast.makeText(Login04Activity.this, "닉네임은 필수 항목입니다", Toast.LENGTH_SHORT).show();

                } else {
                    memberDTO.setNickName(nickName);

                    if (file != null) {
                        filename = UUID.randomUUID().toString() + ".jpg";
                        memberDTO.setFileName(filename);
                        StorageReference riversRef = storageRef.child(filename);
                        UploadTask uploadTask = riversRef.putFile(file);
                    }//file 있을 때

                    sendToNext();
                }
            }
        });
    }

    private void sendToNext() {
        Intent nextIntent = new Intent(Login04Activity.this, Login02Activity.class);
        startActivity(nextIntent);
        finish();
    }

}