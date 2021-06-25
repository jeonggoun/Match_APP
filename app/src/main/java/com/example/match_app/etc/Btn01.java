package com.example.match_app.etc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.match_app.R;
import com.example.match_app.dto.IconDTO;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class Btn01 extends AppCompatActivity {
    private DatabaseReference mDatabaseRef;
    private FirebaseAuth firebaseAuth;
    private StorageReference storageRef;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); // 로그인한 유저의 정보 가져오기
    String uid = user != null ? user.getUid() : null; // 로그인한 유저의 고유 uid 가져오기

    Button versus01, versus02, versus03;
    GridView gv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_btn01);

        gv = findViewById(R.id.gridView);



        String[] sports01 = new String[]{ "권투", "검도", "유도", "펜싱", "테니스", "배드민턴", "탁구", "스쿼시", "당구", "볼링", "체스", "바둑", "장기" };

        String[] sports02 = new String[]{ };

        String[] sports03 = new String[]{ };

        findViewById(R.id.iv_back2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        versus01 = findViewById(R.id.versus01);
        versus01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                versus01.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.versus11,0,0);
                versus02.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.versus02,0,0);
                versus03.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.versus03,0,0);
            }
        });
        versus02 = findViewById(R.id.versus02);
        versus02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                versus01.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.versus01,0,0);
                versus02.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.versus12,0,0);
                versus03.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.versus03,0,0);
            }
        });
        versus03 = findViewById(R.id.versus03);
        versus03.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                versus01.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.versus01,0,0);
                versus02.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.versus02,0,0);
                versus03.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.versus13,0,0);
            }
        });

    }
}