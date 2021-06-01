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
import com.example.match_app.dto.MemberDTO;
import com.example.match_app.dto.MetaDTO;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ChatListFragment extends Fragment {
    private ListView chat_list;
    private String user , temp;
    private MemberDTO member;
    public final static String path = "matchapp/ChatMeta";
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference;// = firebaseDatabase.getReference(path);
    private DatabaseReference databaseReference2;
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

        member = new MemberDTO("aaa@naver.com");
        member.setIdToken("user2");
        mainActivity = (MainActivity) getActivity();
        user = member.getIdToken();
        databaseReference = firebaseDatabase.getReference(path+"/"+user);
        chat_list = viewGroup.findViewById(R.id.chat_list);


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
//// 12356
//        MetaDTO meta = new MetaDTO();
//        meta.setTitle("축구 모집");
//        meta.setGame("축구");
//        meta.setDate("11월");
//        ChattingDTO chat = new ChattingDTO();
//        chat.setNickname("user2");
//        meta.setRecent(chat);
//        dtos.add(meta);
//        databaseReference.child(meta.getRecent().getNickname()).setValue(meta);
//        databaseReference2 = firebaseDatabase.getReference(path+"/"+meta.getRecent().getNickname());
//        databaseReference2.child(user).setValue(meta);
        showChatList();
        return viewGroup;
    }



    private void showChatList() {
        // 리스트 어댑터 생성 및 세팅
        ChatListAdapter adapter = new ChatListAdapter(dtos, context, user);
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