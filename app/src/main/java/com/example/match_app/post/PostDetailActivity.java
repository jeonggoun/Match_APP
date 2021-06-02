package com.example.match_app.post;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.example.match_app.ChattingActivity;
import com.example.match_app.R;
import com.example.match_app.dto.ChattingDTO;
import com.example.match_app.dto.MemberDTO;
import com.example.match_app.dto.MetaDTO;
import com.example.match_app.dto.PostDTO;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PostDetailActivity extends AppCompatActivity {
    private static final String TAG = "main:PostDetailActivity";

    MemberDTO user;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference;
    DatabaseReference databaseReference2;
    ImageView imageView;
    PostDTO dto;

    public final static String path = "matchapp/ChatMeta";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        imageView = findViewById(R.id.imageView);

        //사진 불러오려고 고군분투중인것..
//        Intent intent = getIntent();
//        dto = (PostDTO) intent.getSerializableExtra("post");
//
//        String imagePath = "https://" + dto.getImgPath().replace("content://", "");
//        Log.d(TAG, "onCreate: " + imagePath);
//
//        Glide.with(this).load(imagePath).into(imageView);
        //imageView.setImageURI(Uri.parse(dto.getImgPath()));

//https://firebasestorage.googleapis.com/v0/b/match-app-b8c4a.appspot.com/o/matchapp%2FpostImg%2F
// 040ccb4d-ac06-4cf2-80d9-eb744a63ea28.jpg
// ?alt=media
        String ss = "https://firebasestorage.googleapis.com/v0/b/match-app-b8c4a.appspot.com/o/matchapp%2FpostImg%2F040ccb4d-ac06-4cf2-80d9-eb744a63ea28.jpg?alt=media";
        Glide.with(this).load(ss).into(imageView);

    }
    private void startChatting(PostDTO postDTO){
        MetaDTO meta = new MetaDTO();
        meta.setTitle(postDTO.getTitle());
        meta.setDate(postDTO.getTime());
        meta.setGame(postDTO.getGame());
        ChattingDTO chattingDTO = new ChattingDTO();
        meta.setRecent(chattingDTO);
        databaseReference = firebaseDatabase.getReference(path+"/"+user.getIdToken());  // user가 존재하지 않아 동작하지 않음
        databaseReference.child(postDTO.getWriterToken()).setValue(meta);
        databaseReference2 = firebaseDatabase.getReference(path+"/"+postDTO.getWriterToken());
        databaseReference2.child(user.getIdToken()).setValue(meta);

        Intent intent = new Intent(this , ChattingActivity.class);
        intent.putExtra("userName", user.getEmailId());  // user nickName으로 수정해야함
        intent.putExtra("chatName", postDTO.getWriter());
        startActivity(intent);
    }
}

