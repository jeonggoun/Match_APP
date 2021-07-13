package com.example.match_app.etc;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;

import com.example.match_app.R;
import com.example.match_app.adapter.SportsAdapter1;
import com.example.match_app.dto.FavoriteDTO;
import com.example.match_app.dto.SportsDTO;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import static com.example.match_app.Common.CommonMethod.favoriteDTO;
import static com.example.match_app.Common.CommonMethod.memberDTO;

public class Btn01 extends AppCompatActivity {
    private DatabaseReference mDatabaseRef;
    private FirebaseAuth firebaseAuth;
    private StorageReference storageRef;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); // 로그인한 유저의 정보 가져오기
    String uid = user != null ? user.getUid() : null; // 로그인한 유저의 고유 uid 가져오기

    Button versus01, versus02, versus03;
    GridView gv1, gv2, gv3;
    ArrayList<SportsDTO> items1, items2, items3, items0;
    ArrayList<Boolean> chked01, chked02, chked03;
    SportsAdapter1 adapter1, adapter2, adapter3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_btn01);

        findViewById(R.id.iv_back2).setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) { finish(); }});

        firebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("matchapp");

        gv1 = findViewById(R.id.gridView1);
        gv2 = findViewById(R.id.gridView2);
        gv3 = findViewById(R.id.gridView3);

        String[] sports01 = new String[]{ "태권도", "씨름", "유도", "권투", "검도", "유도", "펜싱", "테니스", "배드민턴", "탁구", "스쿼시", "당구", "골프", "볼링", "체스", "바둑", "장기" };
        String[] sports02 = new String[]{ "축구", "농구", "배구", "핸드볼", "야구", "하키", "럭비", "족구" };
        String[] sports03 = new String[]{ "놀이공원", "곤충채집", "공부", "등산", "익스트림", "번지점프", "승마", "피겨", "쇼트트랙", "마라톤", "경보", "수영", "헬스"};


        chked01 = new ArrayList<>();
        chked02 = new ArrayList<>();
        chked03 = new ArrayList<>();

        items0 = new ArrayList<>();
        items1 = new ArrayList<>();
        items2 = new ArrayList<>();
        items3 = new ArrayList<>();

        if (favoriteDTO.getChked1() != null) {
            for (int i = 0; i < sports01.length; i++)
                items1.add(new SportsDTO(sports01[i], favoriteDTO.getChked1().get(i)));
            for (int i = 0; i < sports02.length; i++)
                items2.add(new SportsDTO(sports02[i], favoriteDTO.getChked2().get(i)));
            for (int i = 0; i < sports03.length; i++)
                items3.add(new SportsDTO(sports03[i], favoriteDTO.getChked3().get(i)));
        } else {
            for (int i = 0; i < sports01.length; i++)
                items1.add(new SportsDTO(sports01[i], false));
            for (int i = 0; i < sports02.length; i++)
                items2.add(new SportsDTO(sports02[i], false));
            for (int i = 0; i < sports03.length; i++)
                items3.add(new SportsDTO(sports03[i], false));
        }

        adapter1 = new SportsAdapter1(items1, getApplicationContext());
        adapter2 = new SportsAdapter1(items2, getApplicationContext());
        adapter3 = new SportsAdapter1(items3, getApplicationContext());

        gv1.setAdapter(adapter1); gv1.setVisibility(View.GONE);
        gv2.setAdapter(adapter2); gv2.setVisibility(View.GONE);
        gv3.setAdapter(adapter3); gv3.setVisibility(View.GONE);

        gv1.setVisibility(View.VISIBLE);

        findViewById(R.id.finish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                chked01 = new ArrayList<>();
                chked02 = new ArrayList<>();
                chked03 = new ArrayList<>();

                for (int i=0; i<adapter1.getCount(); i++) {
                    chked01.add(((SportsDTO) adapter1.getItem(i)).isChecked());
                }
                for (int i=0; i<adapter2.getCount(); i++) {
                    chked02.add(((SportsDTO) adapter2.getItem(i)).isChecked() );
                }
                for (int i=0; i<adapter3.getCount(); i++) {
                    chked03.add(((SportsDTO) adapter3.getItem(i)).isChecked() );
                }

                favoriteDTO.setChked1(chked01);
                favoriteDTO.setChked2(chked02);
                favoriteDTO.setChked3(chked03);

                StringBuilder sB1 = new StringBuilder();
                StringBuilder sB2 = new StringBuilder();
                StringBuilder sB3 = new StringBuilder();

                for (int i=0; i<sports01.length; i++) memberDTO.setChecked1((sB1.append(favoriteDTO.getChked1().get(i).toString()+" ")).toString());
                for (int i=0; i<sports02.length; i++) memberDTO.setChecked2((sB2.append(favoriteDTO.getChked2().get(i).toString()+" ")).toString());
                for (int i=0; i<sports03.length; i++) memberDTO.setChecked3((sB3.append(favoriteDTO.getChked3().get(i).toString()+" ")).toString());

                mDatabaseRef.child("UserAccount").child(uid).setValue(memberDTO);

                for (int i=0; i<items1.size(); i++) items0.add(items1.get(i));
                for (int i=0; i<items2.size(); i++) items0.add(items2.get(i));
                for (int i=0; i<items3.size(); i++) items0.add(items3.get(i));

                mDatabaseRef.child("SportsClass").child(uid).setValue(items0);

                finish();
            }

        });



        versus01 = findViewById(R.id.versus01);
        versus01.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.versus11,0,0);

        versus01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                versus01.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.versus11,0,0);
                versus02.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.versus02,0,0);
                versus03.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.versus03,0,0);
                gv1.setVisibility(View.VISIBLE);
                gv2.setVisibility(View.GONE);
                gv3.setVisibility(View.GONE);
            }
        });
        versus02 = findViewById(R.id.versus02);
        versus02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                versus01.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.versus01,0,0);
                versus02.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.versus12,0,0);
                versus03.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.versus03,0,0);
                gv1.setVisibility(View.GONE);
                gv2.setVisibility(View.VISIBLE);
                gv3.setVisibility(View.GONE);
            }
        });
        versus03 = findViewById(R.id.versus03);
        versus03.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                versus01.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.versus01,0,0);
                versus02.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.versus02,0,0);
                versus03.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.versus13,0,0);
                gv1.setVisibility(View.GONE);
                gv2.setVisibility(View.GONE);
                gv3.setVisibility(View.VISIBLE);
            }
        });

    }
}