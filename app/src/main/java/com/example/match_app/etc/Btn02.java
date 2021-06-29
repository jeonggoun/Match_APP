package com.example.match_app.etc;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.match_app.R;

public class Btn02 extends AppCompatActivity {
    TextView tv_keyword;
    EditText et_keyword;
    Button btn_keywordSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_btn02);
        tv_keyword = findViewById(R.id.tv_keyword);
        et_keyword = findViewById(R.id.et_keyword);
        btn_keywordSave = findViewById(R.id.btn_keywordSave);

        findViewById(R.id.iv_back2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_keywordSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_keyword.append(et_keyword.getText()+",");
                et_keyword.setText("");
            }
        });
    }
}