package com.example.match_app.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.match_app.R;
import com.example.match_app.dto.FavoriteDTO;
import com.example.match_app.dto.MemberDTO;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.UUID;

import static com.example.match_app.Common.CommonMethod.favoriteDTO;
import static com.example.match_app.Common.CommonMethod.memberDTO;
import static com.example.match_app.MainActivity.user;

public class Login04Activity extends AppCompatActivity {
    private final int GET_GALLERY_IMAGE = 200;

    private DatabaseReference mDatabaseRef;
    private FirebaseAuth firebaseAuth;
    private StorageReference storageRef;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); // 로그인한 유저의 정보 가져오기
    String uid = user != null ? user.getUid() : null; // 로그인한 유저의 고유 uid 가져오기

    private String filename;  //ex) profile1.jpg 로그인하는 사람에 따라 그에 식별값에 맞는 프로필 사진 가져오기
    private String filepath;
    private Uri file;

    private ImageView iv_back;

    private ConstraintLayout[] cblays;
    private CheckBox[] cbs;
    private TextView[] tvs;

    private TextView auth_finish;
    private EditText et_01;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login04);

        cblays = new ConstraintLayout[24];
        cbs = new CheckBox[24];
        tvs = new TextView[24];


        int[] cblaysId = {R.id.cblay_01, R.id.cblay_02, R.id.cblay_03,R.id.cblay_04,R.id.cblay_05,R.id.cblay_06,R.id.cblay_07,R.id.cblay_08,R.id.cblay_09,R.id.cblay_10,
                        R.id.cblay_11,R.id.cblay_12,R.id.cblay_13,R.id.cblay_14,R.id.cblay_15,R.id.cblay_16,R.id.cblay_17,R.id.cblay_18,R.id.cblay_19,R.id.cblay_20,
                        R.id.cblay_21,R.id.cblay_22,R.id.cblay_23,R.id.cblay_24};

        int[] cbsId = {R.id.cb01, R.id.cb02, R.id.cb03,R.id.cb04,R.id.cb05,R.id.cb06,R.id.cb07,R.id.cb08,R.id.cb09,R.id.cb10,
                        R.id.cb11,R.id.cb12,R.id.cb13,R.id.cb14,R.id.cb15,R.id.cb16,R.id.cb17,R.id.cb18,R.id.cb19,R.id.cb20,
                        R.id.cb21,R.id.cb22,R.id.cb23,R.id.cb24};

        int[] tvsId = {R.id.tv01, R.id.tv02, R.id.tv03,R.id.tv04,R.id.tv05,R.id.tv06,R.id.tv07,R.id.tv08,R.id.tv09,R.id.tv10,
                R.id.tv11,R.id.tv12,R.id.tv13,R.id.tv14,R.id.tv15,R.id.tv16,R.id.tv17,R.id.tv18,R.id.tv19,R.id.tv20,
                R.id.tv21,R.id.tv22,R.id.tv23,R.id.tv24};

        String[] sports = new String[]{ "육상", "야구", "배구", "테니스", "볼링", "배드민턴", "육상", "체조", "헬스", "수영",
                                        "유도", "레슬링", "복싱", "사격", "사이클", "스쿼시", "승마", "카누", "e스포츠", "스케이팅",
                                        "익스트림", "레이싱", "등산", "전체"};

        for(int i = 0; i < 24; i++){
            this.cblays[i] = findViewById(cblaysId[i]);
            this.cbs[i] = findViewById(cbsId[i]);
            this.tvs[i] = findViewById(tvsId[i]);

            if (sports[i].length()>3) { tvs[i].setText(sports[i]); tvs[i].setTextSize(13); }
            else tvs[i].setText(sports[i]);

            this.cblays[i].setOnClickListener(layoutListener);
        }

        cbs[23].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { checkedListener(); }
        });


        et_01 = findViewById(R.id.et_01);
        auth_finish = findViewById(R.id.auth_finish);
        firebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("matchapp");
        storageRef = FirebaseStorage.getInstance().getReference("matchapp/profileImg");
//-------------------------------------------------------------------------------------------------
        auth_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nickName = et_01.getText().toString();
                if (nickName.trim().length() < 1) {
                    Toast.makeText(Login04Activity.this, "닉네임은 필수 항목입니다", Toast.LENGTH_SHORT).show();

                } else {
                    memberDTO.setNickName(nickName);

                    if (file != null) {
                        filename = UUID.randomUUID().toString() + ".jpg";
                        memberDTO.setFileName(filename);
                        StorageReference riversRef = storageRef.child(filename);
                        UploadTask uploadTask = riversRef.putFile(file);
                    }//file 있을 때

                    StringBuilder sports = new StringBuilder("");
                    for(int i = 0; i < 23; i++) if (cbs[i].isChecked() == true)  sports.append(tvs[i].getText()+" ");
                    memberDTO.setSports(sports.toString());
                    sendToNext();
                }
            }
        });

        iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextIntent = new Intent(Login04Activity.this, Login03Activity.class);
                startActivity(nextIntent);
                finish();
            }
        });
    }

    //-------------------------------------------------------------------------------------------------

    // layout 클릭리스너
    private View.OnClickListener layoutListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            for(int i = 0; i < 24; i++){
                if(v.getId() == cblays[i].getId()) {
                    if (cbs[i].isChecked() == false) cbs[i].setChecked(true);
                    else cbs[i].setChecked(false);
                }
            }
        }
    };

    private void checkedListener() {
        if (cbs[23].isChecked() == true) for (int i = 0; i < 23; i++) cbs[i].setChecked(true);
        else for (int i = 0; i < 23; i++) cbs[i].setChecked(false);
    };

    private void sendToNext() {
        Intent nextIntent = new Intent(Login04Activity.this, Login02Activity.class);
        startActivity(nextIntent);
        finish();
    }


}