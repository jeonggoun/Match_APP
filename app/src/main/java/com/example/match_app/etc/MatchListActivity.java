package com.example.match_app.etc;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.match_app.IntroActivity;
import com.example.match_app.R;
import com.example.match_app.adapter.MyPostAdapter;
import com.example.match_app.dto.PostDTO;
import com.example.match_app.dto.SportsDTO;
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

    private ListView listView1;
    private MyPostAdapter adapter1, adapter2;
    private PostDTO selected;
    private TextView tv_ing, tv_end, tv_null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_list);
        listView1 = findViewById(R.id.listView1);
        tv_ing = findViewById(R.id.tv_ing);
        tv_end = findViewById(R.id.tv_end);
        tv_null = findViewById(R.id.tv_null);



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
        ArrayList<PostDTO> dto1 = new ArrayList<>();
        ArrayList<PostDTO> dto2 = new ArrayList<>();

        ArrayList<SportsDTO> dtox = new ArrayList<>();

        Query query = FirebaseDatabase.getInstance().getReference().child("matchapp/SportsClass").child(uid).orderByChild("checked").equalTo(true);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren())
                    dtox.add(ds.getValue(SportsDTO.class));       // 쿼리 데이터 dto 주입

                if (dtox.size() != 0) {
                    IntroActivity.items = new String[dtox.size() + 1];
                    IntroActivity.items[0] = "전체";                                          // 선호 종목들 넣기
                    for (int i = 1; i < dtox.size() + 1; i++)
                        IntroActivity.items[i] = dtox.get(i - 1).getSports();
                } else {
                    IntroActivity.items = new String[]{"전체", "축구", "야구", "농구", "배구"};
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Query myPost = mDatabaseRefPost.orderByChild("writerToken").equalTo(uid);
        myPost.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()){
                    // ds.getValue() → 컬럼, ds.getKey() → 키값
                    //String title = ds.child("title").getValue(String.class);
                    dto.add(ds.getValue(PostDTO.class));
                }

                dto1.clear(); dto2.clear();

                for (int i=0; i<dto.size(); i++) {
                    if (dto.get(i).getMatchConfirm().equals("enable")) {
                        dto1.add(dto.get(i));
                    } else {
                        dto2.add(dto.get(i));
                    }
                }

                adapter1 = new MyPostAdapter(dto1, getApplicationContext());
                adapter2 = new MyPostAdapter(dto2, getApplicationContext());

                listView1.setAdapter(adapter1); listView1.setVisibility(View.VISIBLE);
                if (dto.size()==0) {
                    tv_null.setVisibility(View.VISIBLE);
                } else {
                    tv_null.setVisibility(View.GONE);
                }

                tv_ing.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tv_ing.setPaintFlags(tv_ing.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG | Paint.FAKE_BOLD_TEXT_FLAG);
                        tv_end.setPaintFlags(0);
                        listView1.setAdapter(adapter1);

                        if (dto1.size() == 0) {
                            tv_null.setVisibility(View.VISIBLE);
                            tv_null.setText("회원님의 진행중인 게시글이 없습니다.");
                        }
                        else tv_null.setVisibility(View.GONE);
                    }
                });

                tv_end.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tv_end.setPaintFlags(tv_ing.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG | Paint.FAKE_BOLD_TEXT_FLAG);
                        tv_ing.setPaintFlags(0);
                        listView1.setAdapter(adapter2);

                        if (dto2.size() == 0) {
                            tv_null.setVisibility(View.VISIBLE);
                            tv_null.setText("회원님의 완료된 게시글이 없습니다.");
                        }
                        else tv_null.setVisibility(View.GONE);
                    }
                });

                listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                        selected = new PostDTO();

                        if (dto1.size()!=0) {
                            selected = (PostDTO) dto1.get(i);
                        }else {
                            selected = (PostDTO) dto2.get(i);
                        }
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