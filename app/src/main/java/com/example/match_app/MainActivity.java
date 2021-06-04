package com.example.match_app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.match_app.dto.MemberDTO;
import com.example.match_app.fragment.ChatListFragment;
import com.example.match_app.fragment.EtcFragment;
import com.example.match_app.fragment.HomeFragment;
import com.example.match_app.fragment.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
public class MainActivity extends AppCompatActivity {

    public static MemberDTO user;
    private static final String TAG = "IntroActivity MAIN : ";
    private Button button;//
    private FirebaseAuth mAuth;
    MemberDTO dto;
    TextView tv_fragTitle;

    HomeFragment fragment1;
    SearchFragment fragment2;
    ChatListFragment fragment3;
    EtcFragment fragment4;
    public static Uri ImageUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_fragTitle = findViewById(R.id.tv_fragTitle);
        dto = (MemberDTO) getIntent().getSerializableExtra("dto");
        mAuth = FirebaseAuth.getInstance();

        fragment1 = new HomeFragment();
        fragment2 = new SearchFragment();
        fragment3 = new ChatListFragment();
        fragment4 = new EtcFragment();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.contain, fragment1).commit();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navi);

        //하단 네비게이션바
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.tab1:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.contain, fragment1).commit();
                        tv_fragTitle.setText("모모 홈");
                        break;

                    case R.id.tab2:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.contain, fragment2).commit();
                        tv_fragTitle.setText("모모 게임");
                        break;

                    case R.id.tab3:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.contain, fragment3).commit();
                        tv_fragTitle.setText("모모 채팅");
                        break;

                    case R.id.tab4:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.contain, fragment4).commit();
                        tv_fragTitle.setText("나의 모모");
                        break;
                }

                return true;
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 200 && resultCode == RESULT_OK && data.getData() != null){
            fragment4.onActivityResult(requestCode, resultCode, data);
        }

    }
}