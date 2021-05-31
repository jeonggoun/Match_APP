package com.example.match_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.match_app.Common.TimerView;
import com.example.match_app.dto.MemberDTO;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class Login02Activity extends AppCompatActivity {
    private static final String TAG = "Login02Activity MAIN : ";
    private ProgressBar progressBar;
    private TimerView timerView;
    private ImageView iv_back;
    private TextView auth_request, auth_retry, auth_finish, tv_auth05;
    private EditText et_phone, et_auth_enter;
    private FirebaseAuth auth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBacks;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference mDatabaseRef;
    private String otp;
    private String phoneNumber = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login02);

        tv_auth05 = findViewById(R.id.tv_auth05);
        auth_request = findViewById(R.id.auth_request);
        auth_retry = findViewById(R.id.auth_retry);
        auth_finish = findViewById(R.id.auth_finish);
        et_phone = findViewById(R.id.et_phone);
        et_auth_enter = findViewById(R.id.et_auth_enter);
        iv_back = findViewById(R.id.iv_back);
        progressBar = findViewById(R.id.progressBar);
        timerView = findViewById(R.id.timer);

        auth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("matchapp");

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Login02Activity.this, "가입단계 진행 중에는 뒤로 갈 수 없습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        auth_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = et_phone.getText().toString();
                if (!phone.isEmpty()) {
                    phoneNumber = "+82"+phone.substring(1,phone.length());
                    PhoneAuthOptions options = PhoneAuthOptions.newBuilder(auth)
                            .setPhoneNumber(phoneNumber)
                            .setTimeout(90L, TimeUnit.SECONDS)
                            .setActivity(Login02Activity.this)
                            .setCallbacks(mCallBacks)
                            .build();
                    PhoneAuthProvider.verifyPhoneNumber(options);

                    timerView.start(90000);


                }else {
                    tv_auth05.setText("전화번호를 입력해주세요");
                    tv_auth05.setTextColor(Color.RED);
                    tv_auth05.setVisibility(View.VISIBLE);
                }
            }
        });

        mCallBacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                singIn(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                tv_auth05.setText(e.getMessage());
                tv_auth05.setTextColor(Color.RED);
                tv_auth05.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);

                // 코드가 자동 입력이 안될 시 사용자가 수동으로 코드 입력해야함
                tv_auth05.setText("코드가 발송됐습니다.");
                tv_auth05.setVisibility(View.VISIBLE);
                otp = s;

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                    }
                }, 10000);

                auth_finish.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String auth_enter = et_auth_enter.getText().toString();
                        if(!auth_enter.isEmpty()){
                            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(otp, auth_enter);
                            singIn(credential);
                        } else {
                            Toast.makeText(Login02Activity.this, "인증번호를 다시 확인해주세요.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        };
    }

    // 시작시 유저정보가 있으면 메인으로 건너가기
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            sendToMain();
        }
    }

    private void sendToMain() {
        Intent mainIntent = new Intent(Login02Activity.this,MainActivity.class);
        startActivity(mainIntent);
        finish();
    }

    private void singIn(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser firebaseUser = auth.getCurrentUser();
                    MemberDTO account = new MemberDTO();
                    account.setIdToken(firebaseUser.getUid());      // 토큰정보 고유값
                    account.setPhoneNumber(phoneNumber);

                    Log.d(TAG, account.getEmailId()+account.getPassword()+account.getIdToken());
                    // setValue : database에 insert(삽입) 행위
                    mDatabaseRef.child("UserAccount").child(firebaseUser.getUid()).setValue(account);
                    sendToMain();
                } else {
                    tv_auth05.setText(task.getException().getMessage());
                    tv_auth05.setTextColor(Color.RED);
                    tv_auth05.setVisibility(View.VISIBLE);
                }
            }
        });
    }


}