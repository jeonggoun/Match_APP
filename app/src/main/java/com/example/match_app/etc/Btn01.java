package com.example.match_app.etc;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.match_app.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;

public class Btn01 extends AppCompatActivity {
    private DatabaseReference mDatabaseRef;
    private FirebaseAuth firebaseAuth;
    private StorageReference storageRef;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); // 로그인한 유저의 정보 가져오기
    String uid = user != null ? user.getUid() : null; // 로그인한 유저의 고유 uid 가져오기

    Button versus01, versus02, versus03;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_btn01);

        versus01 = findViewById(R.id.versus01);
        versus01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                versus01.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.versus01,0,0);
            }
        });        versus01 = findViewById(R.id.versus01);
        versus02 = findViewById(R.id.versus02);
        versus02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                versus02.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.versus02,0,0);
            }
        });        versus02 = findViewById(R.id.versus01);
        versus03 = findViewById(R.id.versus03);
        versus03.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                versus03.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.versus03,0,0);
            }
        });

    }
}