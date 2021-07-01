package com.example.match_app.etc;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.LongDef;
import androidx.appcompat.app.AppCompatActivity;

import com.example.match_app.R;
import com.example.match_app.dto.MemberDTO;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import static com.example.match_app.Common.CommonMethod.favoriteDTO;
import static com.example.match_app.Common.CommonMethod.memberDTO;

public class Btn02 extends AppCompatActivity {
    private DatabaseReference mDatabaseRef;
    private FirebaseAuth firebaseAuth;
    private StorageReference storageRef;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); // 로그인한 유저의 정보 가져오기
    String uid = user != null ? user.getUid() : null; // 로그인한 유저의 고유 uid 가져오기

    private static final String TAG = "Btn02";
    EditText et_keyword;
    TextView tv_addKeyword;
    ChipGroup chipGroup;

    String[] keywords = new String[20];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_btn02);
        firebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("matchapp");
        et_keyword = findViewById(R.id.et_keyword);
        tv_addKeyword = findViewById(R.id.tv_addKeyword);
        chipGroup = findViewById(R.id.chipGroup);
        StringBuilder sB1 = new StringBuilder();
        ArrayList<String> keyw = new ArrayList<>();

        if(favoriteDTO.getKeyword() != null) {
            addChip();
        }

        tv_addKeyword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_keyword.getText().length()<1) Toast.makeText(Btn02.this, "키워드를 입력해주세요. (예: 쥐불놀이)", Toast.LENGTH_SHORT).show();
                else if (et_keyword.getText().length()>10) Toast.makeText(Btn02.this, "키워드 길이는 10자를 초과할 수 없습니다", Toast.LENGTH_SHORT).show();
                else if (et_keyword.getText().toString() != "") {
                    keyw.add(et_keyword.getText().toString());
                    Chip chip = new Chip(Btn02.this);
                    chip.setText(et_keyword.getText().toString());
                    chip.setCloseIconVisible(true);
                    chip.setCloseIconResource(R.drawable.clear);
                    chip.setCheckable(true);
                    chip.setChecked(true);
                    chip.setCheckedIconVisible(false);
                    chip.setOnCloseIconClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                chipGroup.removeView(chip);
                            }
                    });
                    chipGroup.addView(chip);
                    chipGroup.setVisibility(View.VISIBLE);
                    et_keyword.setText("");
                }
            }
        });


        findViewById(R.id.iv_back2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.finish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Integer> ids = chipGroup.getCheckedChipIds();
                ArrayList<String> keyw = new ArrayList<>();
                for (Integer id:ids) {
                    Chip chip2 = chipGroup.findViewById(id);
                    keyw.add(chip2.getText().toString());
                }

                favoriteDTO.setKeyword(keyw);
                for (int i=0; i<favoriteDTO.getKeyword().size(); i++) {
                    sB1.append(favoriteDTO.getKeyword().get(i)+" ");
                }
                memberDTO.setKeyWord(sB1.toString());
                mDatabaseRef.child("UserAccount").child(uid).setValue(memberDTO);

                finish();
            }
        });
    }

    private void addChip() {
            for (int i = 0; i < favoriteDTO.getKeyword().size(); i++) {
                Chip chip = new Chip(Btn02.this);
                chip.setText(favoriteDTO.getKeyword().get(i));
                chip.setCloseIconVisible(true);
                chip.setCloseIconResource(R.drawable.clear);
                chip.setCheckable(true);
                chip.setChecked(true);
                chip.setCheckedIconVisible(false);
                chip.setOnCloseIconClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        chipGroup.removeView(chip);
                    }
                });
                chipGroup.addView(chip);
                chipGroup.setVisibility(View.VISIBLE);
            }
    }
}