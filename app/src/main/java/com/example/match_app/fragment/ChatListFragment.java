package com.example.match_app.fragment;

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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChatListFragment extends Fragment {
    private ListView chat_list;
    private String user ;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    MainActivity mainActivity;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_chat, container, false);
        mainActivity = (MainActivity) getActivity();
    //    user = intent.getStringExtra("name");/////////////////////////////////////////////////////////////
        databaseReference = firebaseDatabase.getReference(user);
        chat_list = mainActivity.findViewById(R.id.chat_list);
        showChatList();
        return viewGroup;
    }



    private void showChatList() {
        // 리스트 어댑터 생성 및 세팅
        final ArrayAdapter<String> adapter

                = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, android.R.id.text1);
        chat_list.setAdapter(adapter);

        chat_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext() , ChattingActivity.class);
                if (user.trim().length() < 1 || user.isEmpty()){
                    Toast.makeText(getContext(),"유저명 확인 불가", Toast.LENGTH_SHORT).show();
                    return;
                }
                intent.putExtra("chatName", adapter.getItem(position).toString());
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