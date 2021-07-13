package com.example.match_app.login;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.match_app.Common.TimerView;
import com.example.match_app.IntroActivity;
import com.example.match_app.R;
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

import static com.example.match_app.Common.CommonMethod.memberDTO;

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
                            .setTimeout(30L, TimeUnit.SECONDS)
                            .setActivity(Login02Activity.this)
                            .setCallbacks(mCallBacks)
                            .build();

                    PhoneAuthProvider.verifyPhoneNumber(options);

                    timerView.start(30000);
                    timerView.setVisibility(View.VISIBLE);

                    if (timerView.isCertification() == false) {
                        auth_request.setEnabled(true);
                        tv_auth05.setText("시간이 만료되어 인증번호를 재요청 하세요.");
                        tv_auth05.setTextColor(Color.RED);
                        tv_auth05.setVisibility(View.VISIBLE);
                    }
                }else {
                    tv_auth05.setText("전화번호를 입력해주세요");
                    tv_auth05.setTextColor(Color.RED);
                    tv_auth05.setVisibility(View.VISIBLE);
                }
            }
        });

        mCallBacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);

                // 코드가 자동 입력이 안될 시 사용자가 수동으로 코드 입력해야함
                tv_auth05.setText("코드가 발송됐습니다.");
                tv_auth05.setVisibility(View.VISIBLE);
                otp = s;

/*                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                    }
                }, 10000);*/

                auth_finish.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String auth_enter = et_auth_enter.getText().toString();
                        if(!auth_enter.isEmpty()){
                            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(otp, auth_enter);
                            singIn(credential);
                        } else {
                            tv_auth05.setText("인증번호를 입력해주세요.");
                            tv_auth05.setTextColor(Color.RED);
                            tv_auth05.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }

            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                String code = phoneAuthCredential.getSmsCode();
                if (code!=null) {
                    tv_auth05.setText("가입내역이 있는 번호");
                    tv_auth05.setTextColor(Color.RED);
                    tv_auth05.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                tv_auth05.setText("status code 17093 null : 짧은 시간 요청으로 인증 블럭처리됨");
                tv_auth05.setTextColor(Color.RED);
                tv_auth05.setVisibility(View.VISIBLE);
            }
        };
    }

    // 시작시 유저정보가 있으면 메인으로 건너가기
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser firebaseUser = auth.getCurrentUser();
        if (firebaseUser != null) {
            sendToNext();
        }
    }

    private void singIn(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser firebaseUser = auth.getCurrentUser();
                    memberDTO.setIdToken(firebaseUser.getUid());
                    memberDTO.setPhoneNumber(phoneNumber);
                    memberDTO.setAddrAuth(false);

                    // setValue : database에 update(수정) 행위
                    mDatabaseRef.child("UserAccount").child(firebaseUser.getUid()).setValue(memberDTO);
                    sendToNext();
                } else {
                    tv_auth05.setText(task.getException().getMessage());
                    tv_auth05.setTextColor(Color.RED);
                    tv_auth05.setVisibility(View.VISIBLE);
                }
            }
        });
    }
    private void sendToNext() {
        Intent nextIntent = new Intent(Login02Activity.this, IntroActivity.class);
        startActivity(nextIntent);
        finish();
    }
}