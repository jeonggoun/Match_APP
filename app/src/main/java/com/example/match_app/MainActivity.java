package com.example.match_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.FrameLayout;

import com.example.match_app.fragment.ChatListFragment;

public class MainActivity extends AppCompatActivity {
//홈화면
     ChatListFragment chatListFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        chatListFragment = (ChatListFragment) getSupportFragmentManager().findFragmentById(R.id.contain);
    }
}