package com.example.match_app;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.match_app.Common.MyService;
import com.example.match_app.dto.MemberDTO;
import com.example.match_app.fragment.ChatListFragment;
import com.example.match_app.fragment.EtcFragment;
import com.example.match_app.fragment.HomeFragment;
import com.example.match_app.fragment.SearchFragment;
import com.example.match_app.login.Login02Activity;
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

        checkDangerousPermissions();

        tv_fragTitle = findViewById(R.id.tv_fragTitle);
        dto = (MemberDTO) getIntent().getSerializableExtra("dto");
        mAuth = FirebaseAuth.getInstance();

        // 현재 사용자 계정정보가 있는가? if =null 이면 로그인 액티비티로 가시오
        FirebaseUser currentUser = mAuth.getCurrentUser();
        Log.d(TAG, "onStart: currentUser : "+currentUser);
        if (currentUser == null) {
            startActivity(new Intent(MainActivity.this, Login02Activity.class));
            finish();
        }

        fragment1 = new HomeFragment();
        fragment2 = new SearchFragment();
        fragment3 = new ChatListFragment();
        fragment4 = new EtcFragment();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.contain, fragment1).commit();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navi);
        Intent intent = new Intent(getApplicationContext(), MyService.class);
        Log.d(TAG, "onChildAdded: 쏼라 aabbccddd");
        startService(intent);
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
    /*@Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        Log.d(TAG, "onStart: currentUser : "+currentUser);
        if (currentUser == null) {
            startActivity(new Intent(MainActivity.this, Login02Activity.class));
            finish();
        }
    }*/

    // startActivityForResult 를 사용하여 requestCode를 보내고
    // 그 액티비티가 종료되고 그 넘어온 데이터를 받을때
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 100){
            fragment2.onActivityResult(requestCode, resultCode, data); /* 글 삭제 후 새로고침 */
        }else if(requestCode == 200 && resultCode == RESULT_OK && data.getData() != null){
            fragment4.onActivityResult(requestCode, resultCode, data);
        }

    }

    // 기존에 만들어진 액티비티를 재사용할때
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        int requestcode = intent.getIntExtra("requestCode", 0);
        if(requestcode == 100){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.contain, fragment2).commit();
            fragment2.onNewIntent(requestcode);
        }
    }

    private void checkDangerousPermissions() {
        String[] permissions = {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        };

        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        for (int i = 0; i < permissions.length; i++) {
            permissionCheck = ContextCompat.checkSelfPermission(this, permissions[i]);
            if (permissionCheck == PackageManager.PERMISSION_DENIED) {
                break;
            }
        }

        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "권한 있음", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "권한 없음", Toast.LENGTH_LONG).show();

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0])) {
                Toast.makeText(this, "권한 설명 필요함.", Toast.LENGTH_LONG).show();
            } else {
                ActivityCompat.requestPermissions(this, permissions, 1);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, permissions[i] + " 권한이 승인됨.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, permissions[i] + " 권한이 승인되지 않음.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

}