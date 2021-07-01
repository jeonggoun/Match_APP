package com.example.match_app.etc;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.match_app.R;
import com.example.match_app.adapter.MyPostAdapter;
import com.example.match_app.dto.PostDTO;
import com.example.match_app.post.PostDetailActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MatchListActivity extends AppCompatActivity {
    private static final String TAG = "MatchListActivity : ";
    private DatabaseReference mDatabaseRefAccount, mDatabaseRefPost;
    private FirebaseAuth firebaseAuth;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); // 로그인한 유저의 정보 가져오기
    String uid = user != null ? user.getUid() : null; // 로그인한 유저의 고유 uid 가져오기

    private ListView listView;
    private MyPostAdapter adapter;
    private PostDTO selected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_list);
        listView = findViewById(R.id.listView);

        findViewById(R.id.iv_back2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRefAccount = FirebaseDatabase.getInstance().getReference("matchapp/UserAccount");
        mDatabaseRefPost = FirebaseDatabase.getInstance().getReference("matchapp/Post");

        ArrayList<PostDTO> dto = new ArrayList<>();

        Query myPost = mDatabaseRefPost.orderByChild("writerToken").equalTo(uid);
        myPost.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()){
                    // ds.getValue() → 컬럼, ds.getKey() → 키값
                    //String title = ds.child("title").getValue(String.class);
                    dto.add(ds.getValue(PostDTO.class));
                }

                adapter = new MyPostAdapter(dto, getApplicationContext());
                listView.setAdapter(adapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                        selected = new PostDTO();
                        selected = (PostDTO) dto.get(i);
                        Intent intent = new Intent(MatchListActivity.this, PostDetailActivity.class);
                        intent.putExtra("post", selected);
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, error.getMessage()+"");
            }
        });
    }
}