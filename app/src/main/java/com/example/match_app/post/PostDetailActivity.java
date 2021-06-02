package com.example.match_app.post;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.match_app.ChattingActivity;
import com.example.match_app.R;
import com.example.match_app.dto.ChattingDTO;
import com.example.match_app.dto.MemberDTO;
import com.example.match_app.dto.MetaDTO;
import com.example.match_app.dto.PostDTO;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PostDetailActivity extends AppCompatActivity {

    MemberDTO user;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference;
    DatabaseReference databaseReference2;
    public final static String path = "matchapp/ChatMeta";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
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

