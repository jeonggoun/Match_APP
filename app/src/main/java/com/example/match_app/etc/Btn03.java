package com.example.match_app.etc;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.match_app.R;
import com.example.match_app.dto.MemberDTO;
import com.example.match_app.dto.PublicPostDTO;
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

import static com.example.match_app.Common.CommonMethod.memberDTO;

public class Btn03 extends AppCompatActivity {
    private DatabaseReference mDatabaseRef;
    private FirebaseAuth firebaseAuth;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); // 로그인한 유저의 정보 가져오기
    String uid = user != null ? user.getUid() : null; // 로그인한 유저의 고유 uid 가져오기
    int c;
    TextView tv_recount, tv_reward;
    Button versus01, versus02, versus03;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_btn03);
        versus01 = findViewById(R.id.versus01); versus02 = findViewById(R.id.versus02); versus03 = findViewById(R.id.versus03);
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("matchapp/UserAccount");
        tv_recount = findViewById(R.id.recount); tv_reward = findViewById(R.id.tv_reward);
        findViewById(R.id.iv_back2).setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) { finish(); }});
        ArrayList<MemberDTO> dto = new ArrayList<>();
        String nickName = memberDTO.getNickName();
        Query myPost = mDatabaseRef.orderByChild("recommend").equalTo(nickName);
        myPost.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    dto.add(ds.getValue(MemberDTO.class));
                }
                tv_recount.setText(dto.size()+"");

                int quotient = (dto.size()+1)/3;
                tv_reward.setText(quotient+"");
                int remainder = (dto.size()+1)%3;

                if (quotient!=0) {
                    switch (remainder) {
                        case 0:
                            versus01.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.no21, 0, 0);
                            versus02.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.no22, 0, 0);
                            versus03.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.no23, 0, 0);
                            break;
                        case 1:
                            versus01.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.no21, 0, 0);
                            versus02.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.no22, 0, 0);
                            break;
                        case 2:
                            versus01.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.no21, 0, 0);
                            break;
                    }
                }else {
                    switch (remainder) {
                        case 0:
                            break;
                        case 1:
                            versus01.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.no21, 0, 0);
                            break;
                        case 2:
                            versus01.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.no21, 0, 0);
                            versus02.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.no22, 0, 0);
                            break;
                    }
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}