package com.example.match_app.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.match_app.ChattingActivity;
import com.example.match_app.MainActivity;
import com.example.match_app.R;
import com.example.match_app.adapter.ListItemAdapter;
import com.example.match_app.asynctask.post.PostDetail;
import com.example.match_app.dto.ListItemDTO;
import com.example.match_app.dto.PostDTO;
import com.example.match_app.dto.SuperDTO;
import com.example.match_app.post.PostWriteActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class SearchFragment extends Fragment {

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference;
    private String path = "matchapp/post";
    MainActivity activity;

    //글쓰기 버튼
   Button btnWrite;

    //ListItem용
    RecyclerView recyclerView;
    ListItemAdapter adapter;
    ArrayList<ListItemDTO> dtos;





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_search, container, false);
        databaseReference = firebaseDatabase.getReference(path);

        //글쓰기 버튼 찾고 PostWriteActivity로 넘기기
        btnWrite = viewGroup.findViewById(R.id.btnWrite);
        btnWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PostWriteActivity.class); //this 대신 getActivity()
                startActivity(intent);
            }
        });

        activity = (MainActivity) getActivity();
        ////////ListItem용
        // 반드시 생성해서 어댑터에 넘겨야 함
        dtos = new ArrayList<>();

        recyclerView = viewGroup.findViewById(R.id.recyclerView);
//
//        // 리사이클러뷰에서 반드시 초기화 시켜야함
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                activity, RecyclerView.VERTICAL, false
        );
        recyclerView.setLayoutManager(layoutManager);
//
//        // 어댑터 객체를 생성한다
        adapter = new ListItemAdapter(dtos, getContext());
//
//        // 어댑터에 있는 ArrayList에 dto를 5개 추가한다
        adapter.addDto(new ListItemDTO(0, "테니스", "테니스 치실 분", "2021/5/26", "농성테니스장", "무료", "#"));    //어디 DTO 받아올 건지 물어볼 예정
//
//        // 만든 어댑터를 리스트뷰에 붙인다
//        recyclerView.setAdapter(adapter);
        ////////
        showPostList();
        return viewGroup;
    }


    private void showPostList() {
//     리스트 어댑터 생성 및 세팅
        final ListItemAdapter adapter
                = new ListItemAdapter(dtos, getContext());

        recyclerView.setAdapter(adapter);
//        recyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//            }
//        });

        // 데이터 받아오기 및 어댑터 데이터 추가 및 삭제 등..리스너 관리
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ListItemDTO dto = dataSnapshot.getValue(ListItemDTO.class);
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