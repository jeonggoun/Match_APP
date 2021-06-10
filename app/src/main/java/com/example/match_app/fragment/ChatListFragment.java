package com.example.match_app.fragment;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.match_app.MainActivity;
import com.example.match_app.R;
import com.example.match_app.adapter.ChatListAdapter;
import com.example.match_app.dto.ChattingDTO;
import com.example.match_app.dto.MemberDTO;
import com.example.match_app.dto.MetaDTO;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import static com.example.match_app.MainActivity.user;
public class ChatListFragment extends Fragment {
    private String userIdToken;
    public final static String path = "matchapp/ChatMeta";
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference;// = firebaseDatabase.getReference(path);

    MainActivity mainActivity;
    Context context;

    MainActivity activity;
    RecyclerView recyclerView;
    ArrayList<MetaDTO> dtos;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_chat, container, false);
        context = container.getContext();

        userIdToken = user.getIdToken();
        mainActivity = (MainActivity) getActivity();
        databaseReference = firebaseDatabase.getReference(path+"/"+ userIdToken);


        activity = (MainActivity) getActivity();
        ////////ListItem용
        // 반드시 생성해서 어댑터에 넘겨야 함
        dtos = new ArrayList<>();

        recyclerView = viewGroup.findViewById(R.id.chat_recyclerView);
//
//        // 리사이클러뷰에서 반드시 초기화 시켜야함
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                activity, RecyclerView.VERTICAL, false
        );
        recyclerView.setLayoutManager(layoutManager);
//// 채팅창 테스트 생성
//        private DatabaseReference databaseReference2;
//        MetaDTO meta = new MetaDTO();
//        meta.setTitle("테니스 모집");
//        meta.setGame("축구");       //경기종류
//        meta.setDate("11월");
//        ChattingDTO chat = new ChattingDTO();
//        chat.setNickname("화순오리불고기");      //닉네임
//        meta.setRecent(chat);
//        meta.setChatToken("JMGLHcdmxuTph9bEXk8klROt8lq1"); // 토큰값
//        meta.setPostToken("-MbFvpBs-cydJ-Soa8Og");
//        dtos.add(meta);
//        databaseReference.child(meta.getChatToken()).setValue(meta);  //유저의 채팅목록창에 방을 추가
//        databaseReference2 = firebaseDatabase.getReference(path+"/"+meta.getChatToken());  //상대의 채팅목록을 가져옴
//        meta.setChatToken(userIdToken);
//        databaseReference2.child(user.getIdToken()).setValue(meta);   //상대의 채팅목록창에 방을 추가
        showChatList();
        return viewGroup;
    }



    private void showChatList() {
        // 리스트 어댑터 생성 및 세팅
        ChatListAdapter adapter = new ChatListAdapter(dtos, context);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);

        // 데이터 받아오기 및 어댑터 데이터 추가 및 삭제 등..리스너 관리
        databaseReference.addChildEventListener(new ChildEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                MetaDTO dto = dataSnapshot.getValue(MetaDTO.class);
                adapter.addDto(dto);
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {            }
            @Override
            public void onCancelled(DatabaseError databaseError) {            }
        });
    }
}