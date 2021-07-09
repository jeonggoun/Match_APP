package com.example.match_app.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.match_app.IntroActivity;
import com.example.match_app.MainActivity;
import com.example.match_app.R;
import com.example.match_app.adapter.PostAdapter;
import com.example.match_app.dto.PostDTO;
import com.example.match_app.dto.SportsDTO;
import com.example.match_app.post.PostWriteActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.example.match_app.Common.CommonMethod.memberDTO;
import static com.example.match_app.Common.MyService.notiDTO;

public class SearchFragment extends Fragment {
    private static final String TAG = "main: SearchFragment";
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); // 로그인한 유저의 정보 가져오기
    String uid = user != null ? user.getUid() : null; // 로그인한 유저의 고유 uid 가져오기

    MainActivity activity;

    //ListItem용
    RecyclerView recyclerView;
    PostAdapter adapter;
    ArrayList<PostDTO> dtos;
    PostAdapter postAdapter;
    Button btnWrite;
    EditText tvSearch;
    //콤보박스용 items
    //String itemString = "전체";

    Spinner spinner;
    String item = "전체";
    Context context;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_search, container, false);
        context = container.getContext();

        ArrayList<SportsDTO> dtox = new ArrayList<>();

        Query query = FirebaseDatabase.getInstance().getReference().child("matchapp/SportsClass").child(uid).orderByChild("checked").equalTo(true);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()) dtox.add(ds.getValue(SportsDTO.class));       // 쿼리 데이터 dto 주입

                if (dtox != null) {
                    IntroActivity.items = new String[dtox.size()+1];
                    IntroActivity.items[0] = "전체";                                          // 선호 종목들 넣기
                    for (int i=1; i<dtox.size()+1; i++) IntroActivity.items[i] = dtox.get(i-1).getSports();
                }else {
                    IntroActivity.items = new String[]{"전체", "축구", "야구", "농구", "배구"};
                }
                //spinner();
                //스피너 찾아주기
                spinner = viewGroup.findViewById(R.id.spinner);

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, IntroActivity.items);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                //스피너에 어댑터 설정
                spinner.setAdapter(arrayAdapter);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                        // 받은 어댑터에서 야구만 있는 어댑터를 만들어서 그어댑터를 setAdapter ?!
                        if(!item.equals(IntroActivity.items[position])) {
                            item = IntroActivity.items[position];
//                    FragmentTransaction ft = getFragmentManager().beginTransaction();
//                    ft.detach(SearchFragment.this).attach(SearchFragment.this).commit();
                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        databaseReference = firebaseDatabase.getReference("matchapp/Post");
//        databaseReference2 = firebaseDatabase.getReference("matchapp/Game");
        //itemString = "전체";
        activity = (MainActivity) getActivity();
        ////////ListItem용
        // 반드시 생성해서 어댑터에 넘겨야 함
        dtos = new ArrayList<>();

        recyclerView = viewGroup.findViewById(R.id.recyclerView);
        tvSearch = viewGroup.findViewById(R.id.tvSearch);
        // 리사이클러뷰에서 반드시 초기화 시켜야함
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                activity, RecyclerView.VERTICAL, false
        );
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);

        // 어댑터 객체를 생성한다
        adapter = new PostAdapter(dtos, getContext());

        //글작성 버튼 클릭시 화면전환
        btnWrite = viewGroup.findViewById(R.id.btnWrite);
            btnWrite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (memberDTO.isAddrAuth() == true) {
                        Intent intent = new Intent(getActivity(), PostWriteActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(activity, "동네인증을 먼저 해주세요!", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        viewGroup.findViewById(R.id.btnSearch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.detach(SearchFragment.this).attach(SearchFragment.this).commit();
            }
        });

        showPostList();
        return viewGroup;
    }

    @Override
    public void onStart() {
        super.onStart();
        addItemsOnSpinner();
    }

    public void addItemsOnSpinner() {

    }

    private void showPostList() {
//     리스트 어댑터 생성 및 세팅
        postAdapter = new PostAdapter(dtos, getContext());
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);

        // 데이터 받아오기 및 어댑터 데이터 추가 및 삭제 등..리스너 관리
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                PostDTO dto = dataSnapshot.getValue(PostDTO.class);
                dto.setPostKey(dataSnapshot.getKey());

                boolean check = true;
                if(!item.isEmpty() && !item.equals("전체")) {
                    if (!item.equals(dto.getGame()))
                        check = false;
                }
                if(!tvSearch.getText().toString().equals("")) {
                    String ss = dto.getTitle();
                    if(!ss.contains(tvSearch.getText().toString().trim())){
                        check = false;
                    }
                }
                if(check) { adapter.addDto(dto); }
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

    /* 글 삭제 후 새로고침 */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 100){
            getFragmentManager().beginTransaction().detach(SearchFragment.this).attach(SearchFragment.this).commit();
        }
    }

    public  void onNewIntent(int requestCode){
        if(requestCode == 100){
            getFragmentManager().beginTransaction().detach(SearchFragment.this).attach(SearchFragment.this).commit();
        }
    }

    /*@Override
    public void onResume() {
        super.onResume();

        getFragmentManager().beginTransaction().detach(this).attach(this).commit();
    }
*/
    //
//    private void spinner(){
//
//        // 데이터 받아오기 및 어댑터 데이터 추가 및 삭제 등..리스너 관리
//        databaseReference2.addChildEventListener(new ChildEventListener() {
//            @RequiresApi(api = Build.VERSION_CODES.N)
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                itemString += ","+dataSnapshot.getValue().toString();
//                items = itemString.split(",");
//
//                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
//                        getContext(), android.R.layout.simple_spinner_item, items);
//                arrayAdapter.setDropDownViewResource(
//                        android.R.layout.simple_spinner_dropdown_item);
//                //스피너에 어댑터 설정
//                spinner.setAdapter(arrayAdapter);
//            }
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {            }
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {            }
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {            }
//            @Override
//            public void onCancelled(DatabaseError databaseError) {            }
//        });
//    }
}