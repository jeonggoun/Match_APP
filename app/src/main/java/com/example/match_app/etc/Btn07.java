package com.example.match_app.etc;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.match_app.IntroActivity;
import com.example.match_app.R;
import com.example.match_app.dto.OptionDTO;
import com.example.match_app.login.Login00Activity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.example.match_app.Common.CommonMethod.memberDTO;
import static com.example.match_app.Common.CommonMethod.optionDTO;

public class Btn07 extends AppCompatActivity {

    private DatabaseReference mDatabaseRefAccount, mDatabaseRefPost;
    private FirebaseAuth firebaseAuth;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); // 로그인한 유저의 정보 가져오기
    String uid = user != null ? user.getUid() : null; // 로그인한 유저의 고유 uid 가져오기

    ToggleButton toggle_sound, toggle_vib, toggle_keyword, toggle_public, toggle_chat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_btn07);

        firebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRefAccount = FirebaseDatabase.getInstance().getReference("matchapp");

        TextView tv_number = findViewById(R.id.tv_number);
        TextView tv_email = findViewById(R.id.tv_email);

        String phone =memberDTO.getPhoneNumber().toString().replace("+82","0");

        tv_email.setText(memberDTO.getEmailId());
        tv_number.setText(phone);

        toggle_sound = findViewById(R.id.toggle_sound);
        toggle_vib = findViewById(R.id.toggle_vib);
        toggle_keyword = findViewById(R.id.toggle_keyword);
        toggle_public = findViewById(R.id.toggle_public);
        toggle_chat = findViewById(R.id.toggle_chat);

        Vibrator vibrator = (Vibrator)getSystemService(VIBRATOR_SERVICE);
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone rt = RingtoneManager.getRingtone(getApplicationContext(),notification);

        findViewById(R.id.btn00).setVisibility(View.GONE);
        findViewById(R.id.btn01).setVisibility(View.GONE);
        findViewById(R.id.btn04).setVisibility(View.GONE);
        toggle_vib.setVisibility(View.GONE);
        toggle_sound.setVisibility(View.GONE);
        toggle_chat.setVisibility(View.GONE);

        toggle_public.setChecked(optionDTO.isVib());
        toggle_public.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (toggle_public.isChecked()) {
                    optionDTO.setVib(true);
                    rt.play();
                    vibrator.vibrate(1000);
                    Toast.makeText(Btn07.this, "공지 알림 on!", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(Btn07.this, "공지 알림 off!", Toast.LENGTH_SHORT).show();
                    optionDTO.setVib(false);
                    vibrator.cancel();
                };
            }
        });
        toggle_keyword.setChecked(optionDTO.isSound());
        toggle_keyword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (toggle_keyword.isChecked()) {
                    optionDTO.setSound(true);
                    rt.play();
                    vibrator.vibrate(1000);
                    Toast.makeText(Btn07.this, "키워드 알림 on!", Toast.LENGTH_SHORT).show();
                } else {
                    optionDTO.setSound(false);
                    rt.stop();
                    Toast.makeText(Btn07.this, "키워드 알림 off!", Toast.LENGTH_SHORT).show();
                };
            }
        });
        toggle_chat.setChecked(optionDTO.isChat());
        toggle_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (toggle_chat.isChecked()) {
                    optionDTO.setChat(true);
                    Toast.makeText(Btn07.this, "채팅 알림 on!", Toast.LENGTH_SHORT).show();
                }else {
                    optionDTO.setChat(false);
                    Toast.makeText(Btn07.this, "채팅 알림 off!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        findViewById(R.id.email_reg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        findViewById(R.id.iv_back2).setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) { finish(); }});
        findViewById(R.id.btn_logOut).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder dialog = new AlertDialog.Builder(Btn07.this, android.R.style.Theme_DeviceDefault_Light_Dialog_Alert);
                dialog.setMessage("로그아웃 하시겠습니까?")
                        .setPositiveButton("아니오", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getApplicationContext(), "취소했습니다.", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNeutralButton("예", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    firebaseAuth.signOut();
                                    Intent intent = new Intent(Btn07.this, IntroActivity.class);
                                    startActivity(intent);
                                    ActivityCompat.finishAffinity(Btn07.this);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        })
                        .setCancelable(false)
                        .show();
            }
        });

        findViewById(R.id.btn_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder dialog = new AlertDialog.Builder(Btn07.this, android.R.style.Theme_DeviceDefault_Light_Dialog_Alert);
                dialog.setMessage("회원탈퇴 하시겠습니까?")
                        .setPositiveButton("아니오", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getApplicationContext(), "취소했습니다.", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNeutralButton("예", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    firebaseAuth.getCurrentUser().delete();
                                    Intent intent = new Intent(Btn07.this, IntroActivity.class);
                                    startActivity(intent);
                                    finish();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        })
                        .setCancelable(false)
                        .show();
            }
        });

    }
}

