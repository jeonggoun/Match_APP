package com.example.match_app;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.match_app.adapter.ChatAdpter;
import com.example.match_app.dto.ChattingDTO;
import com.example.match_app.dto.MetaDTO;
import com.example.match_app.dto.PostDTO;
import com.example.match_app.post.PostDetailActivity;
import com.example.match_app.post.PostUpdateActivity;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.match_app.MainActivity.user;
public class ChattingActivity extends AppCompatActivity {
    private static final String TAG = "Main: ChattingActivity";
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<ChattingDTO> ChattingDTOList;
    private String userName ,userToken;
    private String chat_name , chatToken;
    private MetaDTO chatMeta;
    private String path = "matchapp/ChatList";
    private String metaPath = "matchapp/ChatMeta";
    private EditText edt_chat;
    private Button btn_send, matchConfirm, matchCancel;
    private Context context = this;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef;
    private DatabaseReference toRef;
    private DatabaseReference toRefMeta, userMeta, post;
    private TextView tvChatPostTitle, tvChatGame;
    private LinearLayout chat_match_confirm_layout;
    int counter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chating);


        checkDangerousPermissions();
        userToken = user.getIdToken();
        Intent intent = getIntent();
        chatMeta = (MetaDTO) intent.getSerializableExtra("meta");
        chatToken = chatMeta.getChatToken();
        userName = user.getNickName();
        chat_name = chatMeta.getRecent().getNickname();

        btn_send = findViewById(R.id.btn_send);
        edt_chat = findViewById(R.id.edt_chat);
        tvChatPostTitle = findViewById(R.id.tvChatPostTitle);
        tvChatGame = findViewById(R.id.tvChatGame);

        matchConfirm = findViewById(R.id.chat_activity_match_confirm);
        chat_match_confirm_layout = findViewById(R.id.chat_match_confirm_layout);

        //dto.writerToken == chatMeta.chatToken 이거로 하면 됨 글쓴이만 경기 확정 할 수 있게
        Log.d(TAG, "user.getIdToken: " + user.getIdToken());
        Log.d(TAG, "chatMeta.getChatToken: " + chatMeta.getChatToken());
        //todo 경기 확정은 글을 쓴 사람만 할 수 있게 바꾸기 → 대충 된 거 같음
//        if(user.getIdToken().equals(chatMeta.)){
//            chat_match_confirm_layout.setVisibility(View.VISIBLE);
//        }

        tvChatPostTitle.setText(chatMeta.getTitle());
        tvChatGame.setText(chatMeta.getGame());


        //todo
        //이동 버튼 누르면 해당하는 post detail 페이지로 이동
        findViewById(R.id.btnChatPost).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference databaseReference;
                databaseReference = database.getReference("matchapp/Post");
                databaseReference.addChildEventListener(new ChildEventListener() { //채팅 가져오기
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//                        PostDTO dto = null;
//                        switch (snapshot.getKey()){
//                            case "content": dto.setContent((String) snapshot.getValue());
//                        }
                        if(snapshot.getKey().equals(chatMeta.getPostToken())) {
                            PostDTO dto = snapshot.getValue(PostDTO.class);
                            Intent intent = new Intent(ChattingActivity.this, PostDetailActivity.class);
                            intent.putExtra("post", dto);
                            startActivity(intent);
                        }
                    }
                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {            }
                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {            }
                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {            }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {            }
                });


            }
        });

        matchCancel = findViewById(R.id.chat_activity_match_cancel);
        matchConfirm.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                buttonChange("disable");
                setPost("disable");
                Toast.makeText(ChattingActivity.this, "경기가 확정되었습니다", Toast.LENGTH_SHORT).show();
            }
        });

        matchCancel.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                //chatMeta.getChatToken()==userToken
                buttonChange("enable");
                setPost("enable");
                Toast.makeText(ChattingActivity.this, "경기가 취소되었습니다", Toast.LENGTH_SHORT).show();
            }
        });

        btn_send.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                String msg = edt_chat.getText().toString();

                if (msg != null){
                    ChattingDTO dto = new ChattingDTO();
                    dto.setNickname(userName);
                    dto.setMsg(msg);
                    long now = System.currentTimeMillis();
                    Date mDate = new Date(now);
                    SimpleDateFormat simpleDate = new SimpleDateFormat("hh:mm:aa");
                    String getTime = simpleDate.format(mDate);
                    dto.setDate(getTime);
                    dto.setWriterToken(user.getIdToken());
                    toRefMeta.child("recent").setValue(dto);
                    toRefMeta.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                            if(snapshot.getKey().equals("noty")){
                                counter = Integer.parseInt((String) snapshot.getValue());
                                ArrayMap<String,String> map = new ArrayMap<>();
                                map.put("noty" , Integer.toString(counter+1));
                                toRefMeta.updateChildren(Collections.unmodifiableMap(map));
                            }
                        }
                        @Override
                        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                            if(snapshot.getKey().equals("noty")){
                                counter = Integer.parseInt((String) snapshot.getValue());
                            }
                        }
                        @Override
                        public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                        }
                        @Override
                        public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    myRef.push().setValue(dto);
                    toRef.push().setValue(dto);
                    edt_chat.setText("");
                }
            }
        });

        mRecyclerView = findViewById(R.id.rcview);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        ChattingDTOList = new ArrayList<>();
        mAdapter = new ChatAdpter(ChattingDTOList , ChattingActivity.this , userName);
        mRecyclerView.setAdapter(mAdapter);


        myRef = database.getReference(path+"/"+userToken+"/"+chatToken+chatMeta.getPostToken());
        toRef = database.getReference(path+"/"+chatToken+"/"+userToken+chatMeta.getPostToken());
        toRefMeta = database.getReference(metaPath+"/"+chatToken+"/"+userToken+chatMeta.getPostToken());
        userMeta = database.getReference(metaPath+"/"+userToken+"/"+chatToken+chatMeta.getPostToken());
        //userMeta.removeValue(); 사용시 더이상 채팅방이 목록에서 나타나지 않음
        //상대는 채팅방 목록에 남아있으나, 말을 해도 더이상 상대에게 전달되지 않음

        post = database.getReference("matchapp/Post/"+chatMeta.getPostToken());
        myRef.addChildEventListener(new ChildEventListener() { //채팅 가져오기
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                ChattingDTO dto = snapshot.getValue(ChattingDTO.class);
                int num = 0;
                if(dto.getWriterToken().equals(user.getIdToken())){ num =1; }
                ((ChatAdpter) mAdapter).addChat(dto);
                if(num ==1) { mRecyclerView.scrollToPosition(mAdapter.getItemCount() - 1); }
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {            }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {            }
            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {            }
        });
        post.addChildEventListener(new ChildEventListener() {//경기확정 버튼 반응
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(snapshot.getKey().equals("writerToken")){
                    if(((String)snapshot.getValue()).equals(user.getIdToken())){
                        chat_match_confirm_layout.setVisibility(View.VISIBLE);
                    }
                }
                if(snapshot.getKey().equals("matchConfirm")){
                    buttonChange((String) snapshot.getValue());
                }

            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(snapshot.getKey().equals("matchConfirm")){
                    buttonChange((String) snapshot.getValue());
                }
            }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }
            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onStop() {
        super.onStop();
        ArrayMap<String,String> map = new ArrayMap<>();
        map.put("noty" , "0");
        userMeta.updateChildren(Collections.unmodifiableMap(map));
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void setPost(String set) {
        ArrayMap<String,String> map2 = new ArrayMap<>();
        map2.put("matchConfirm" , set);
        post.updateChildren(Collections.unmodifiableMap(map2));
    }

    private void buttonChange(String set){
        if (set.equals("enable")){
            matchConfirm.setVisibility(View.VISIBLE);
            matchCancel.setVisibility(View.GONE);
        }else if (set.equals("disable")){
            matchConfirm.setVisibility(View.GONE);
            matchCancel.setVisibility(View.VISIBLE);
        }
    }


    private void checkDangerousPermissions() {
        String[] permissions = {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
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
            /*Toast.makeText(this, "권한 없음", Toast.LENGTH_LONG).show();*/

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0])) {
                /*Toast.makeText(this, "권한 설명 필요함.", Toast.LENGTH_LONG).show();*/
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
                    /*Toast.makeText(this, permissions[i] + " 권한이 승인됨.", Toast.LENGTH_LONG).show();*/
                } else {
                    /*Toast.makeText(this, permissions[i] + " 권한이 승인되지 않음.", Toast.LENGTH_LONG).show();*/
                }
            }
        }
    }
}