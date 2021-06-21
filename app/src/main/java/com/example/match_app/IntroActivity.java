package com.example.match_app;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.match_app.Common.GetKeyHash;
import com.example.match_app.dto.MemberDTO;
import com.example.match_app.login.Login00Activity;
import com.github.ybq.android.spinkit.style.Wave;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import static com.example.match_app.MainActivity.user;
public class IntroActivity extends AppCompatActivity {
    private static final String TAG = "IntroActivity MAIN : ";
    private FirebaseAuth mAuth;
    private GetKeyHash CommonFunction;
    private ImageView iv_momo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        mAuth = FirebaseAuth.getInstance();
        Log.e("GR_KeyHash",CommonFunction.getKeyHash(this));

        ProgressBar progressBar = (ProgressBar) findViewById(R.id.spin_kit);
        Wave wave = new Wave();
        progressBar.setIndeterminateDrawable(wave);
        progressBar.bringToFront();
        iv_momo = findViewById(R.id.iv_momo);
    }

    private void checkDangerousPermissions() {
        String[] permissions = {
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.VIBRATE
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

    // 사용자 정보가 없으면 Login02로 바로 가게끔, 있으면 main으로 가게끔

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "사용자 정보 없음", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(IntroActivity.this, Login00Activity.class));
            finish();
        } else {
            Toast.makeText(this, "사용자 정보 있음", Toast.LENGTH_SHORT).show();

            String token = currentUser.getUid();
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = firebaseDatabase.getReference("matchapp/UserAccount");
            databaseReference.addChildEventListener(new ChildEventListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    MemberDTO dto = dataSnapshot.getValue(MemberDTO.class);
                    if(dto.getIdToken().equals(token)) {
                        user = dto;

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent(IntroActivity.this, MainActivity.class));
                                // 찾은 다음에 MainActivity를 띄워줌
                                finish();
                            }
                        },4000);
                    }
                }
                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {            }
                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {            }
                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {            }
                @Override
                public void onCancelled(DatabaseError databaseError) {            }
            });
//            startActivity(new Intent(IntroActivity.this, Login03Activity.class)); //원래 이동처

        }
    }
}