package com.example.match_app.etc;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.match_app.R;
import com.example.match_app.adapter.AnswerAdapter;
import com.example.match_app.adapter.CategoryAdapter;
import com.example.match_app.dto.PublicPostDTO;

import java.util.ArrayList;

import static com.example.match_app.Common.CommonMethod.memberDTO;
import static com.example.match_app.Common.MyService.qaDTO;

public class Btn06 extends AppCompatActivity {

    RecyclerView rv_answer, rv_category;
    EditText et_keyword;
    CategoryAdapter adapter1;
    AnswerAdapter adapter2;
    ArrayList<PublicPostDTO> dtos;
    String[] category;
    String searchText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_btn06);

        et_keyword = findViewById(R.id.et_keyword);
        et_keyword.setHint(memberDTO.getNickName()+"님 무엇을 도와드릴까요?");

        dtos = new ArrayList<PublicPostDTO>();

        Intent intent = getIntent();
        String question_gps = intent.getStringExtra("question_gps");
        if (question_gps != null) {
            et_keyword.setText(question_gps);
        }

        findViewById(R.id.iv_back2).setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) { finish(); }});

        category = new String[]{"운영정책","계정/인증","이벤트/초대", "이용 제재", "기타", "모모 채팅", "모모 종목", "모모 매너", "모모 광고"};
        rv_category = findViewById(R.id.rv_category);
        rv_category.setLayoutManager(new GridLayoutManager(this,3));
        adapter1 = new CategoryAdapter(this, category);
        rv_category.setAdapter(adapter1);

        rv_answer = findViewById(R.id.rv_answer);
        rv_answer.setLayoutManager(new LinearLayoutManager(this));
        adapter2 = new AnswerAdapter(this, qaDTO);
        rv_answer.setAdapter(adapter2);
        adapter2.notifyDataSetChanged();

        et_keyword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable editable) {

                searchText = et_keyword.getText().toString();
                searchFilter(searchText);
            }
        });

        adapter1.setOnItemClickListener(new CategoryAdapter.OnitemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                dtos.clear();
                for (int i = 0; i < qaDTO.size(); i++) {
                    if (qaDTO.get(i).getCategory().equals(category[position])) {
                        dtos.add(qaDTO.get(i));
                    }
                }
                adapter2.filterList(dtos);
            }
        });

    }

    public void searchFilter(String searchText) {
        dtos.clear();
        for (int i = 0; i < qaDTO.size(); i++) {
            if (qaDTO.get(i).getTitle().toLowerCase().contains(searchText.toLowerCase())) {
                dtos.add(qaDTO.get(i));
            }
        }
        if (dtos!=null) adapter2.filterList(dtos);
    }
}