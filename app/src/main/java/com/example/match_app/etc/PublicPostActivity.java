package com.example.match_app.etc;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.example.match_app.R;
import com.example.match_app.dto.PostDTO;
import com.example.match_app.dto.PublicPostDTO;

public class PublicPostActivity extends Activity {
    PublicPostDTO dto;
    TextView tv_content, tv_title, tv_time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature( Window.FEATURE_NO_TITLE );
        setContentView(R.layout.activity_public_post);

        Intent intent = getIntent();
        dto = (PublicPostDTO) intent.getSerializableExtra("dto");

        TextView close = findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tv_content = findViewById(R.id.tv_content);
        tv_content.setText(dto.getContent());

        tv_title = findViewById(R.id.tv_title);
        tv_title.setText(dto.getTitle());

/*        tv_time = findViewById(R.id.tv_time);
        tv_time.setText(dto.getDate());*/


    }
}