package com.example.match_app.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.match_app.ChattingActivity;
import com.example.match_app.MainActivity;
import com.example.match_app.R;
import com.example.match_app.dto.MemberDTO;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChatListFragment extends Fragment {
    private ListView chat_list;
    private String user ;
    private MemberDTO member;
    public final static String path = "matchapp/ChatList";
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference;// = firebaseDatabase.getReference(path);
    MainActivity mainActivity;
    Context context;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_chat, container, false);
        context = container.getContext();
        member = new MemberDTO("aaa@naver.com");
        member.setIdToken("1234");
        mainActivity = (MainActivity) getActivity();
        user = member.getIdToken();
        databaseReference = firebaseDatabase.getReference(path+"/"+user);
        chat_list = viewGroup.findViewById(R.id.chat_list);
        showChatList();
        return viewGroup;
    }



    private void showChatList() {
        // 리스트 어댑터 생성 및 세팅
        final ArrayAdapter<String> adapter

                = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, android.R.id.text1);
        chat_list.setAdapter(adapter);
        adapter.add("친구1");
        adapter.add("12345");
        Toast.makeText(context,"유저명", Toast.LENGTH_SHORT).show();
        chat_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ChattingActivity.class);
                if ( user ==null || user.isEmpty()||user.trim().length() < 1){
                    Toast.makeText(getContext(),"유저명 확인 불가"+user, Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(getContext(),"유저명 확인", Toast.LENGTH_SHORT).show();
                intent.putExtra("chatName", adapter.getItem(position));
                intent.putExtra("userName", user);
                startActivity(intent);

            }
        });

        // 데이터 받아오기 및 어댑터 데이터 추가 및 삭제 등..리스너 관리
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {adapter.add(dataSnapshot.getKey());}
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