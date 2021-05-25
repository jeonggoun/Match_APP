package com.example.match_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.example.match_app.fragment.ChatListFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "IntroActivity MAIN : ";
    private Button button;
    private FirebaseAuth mAuth;
    ChatListFragment chatListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                startActivity(new Intent(MainActivity.this, IntroActivity.class));
                finish();
            }

        });
    }

    // 현재 사용자 계정정보가 있는가? if =null 이면 로그인 액티비티로 가시오
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        Log.d(TAG, "onStart: currentUser : "+currentUser);
        if (currentUser == null) {
            startActivity(new Intent(MainActivity.this, Login02Activity.class));
            finish();
        }
    }
}