package com.example.match_app.etc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.match_app.R;

import org.w3c.dom.Text;

import de.hdodenhof.circleimageview.CircleImageView;

public class EtcProfileActivity extends AppCompatActivity {

    TextView tv_auth, auth_finish;
    ImageView iv_back2, profilePic;
    FrameLayout image_logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_etc_profile);

        iv_back2 = findViewById(R.id.iv_back2);
        iv_back2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}