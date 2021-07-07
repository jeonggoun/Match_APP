package com.example.match_app;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.match_app.Common.GetKeyHash;
import com.example.match_app.dto.FavoriteDTO;
import com.example.match_app.dto.MemberDTO;
import com.example.match_app.dto.OptionDTO;
import com.example.match_app.login.Login00Activity;
import com.github.ybq.android.spinkit.style.Wave;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;

import static com.example.match_app.Common.CommonMethod.favoriteDTO;
import static com.example.match_app.Common.CommonMethod.keywords;
import static com.example.match_app.Common.CommonMethod.memberDTO;
import static com.example.match_app.Common.CommonMethod.optionDTO;
import static com.example.match_app.MainActivity.user;
public class IntroActivity extends AppCompatActivity {
    private static final String TAG = "IntroActivity MAIN : ";
    private FirebaseAuth mAuth;
    private GetKeyHash CommonFunction;
    private ImageView iv_momo;
    public static String[] items = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        favoriteDTO = new FavoriteDTO();

        mAuth = FirebaseAuth.getInstance();
        Log.e("GR_KeyHash",CommonFunction.getKeyHash(this));

        ProgressBar progressBar = (ProgressBar) findViewById(R.id.spin_kit);
        Wave wave = new Wave();
        progressBar.setIndeterminateDrawable(wave);
        progressBar.bringToFront();
        iv_momo = findViewById(R.id.iv_momo);

        optionDTO = new OptionDTO();
        optionDTO.setSound(true);
        optionDTO.setVib(true);
        optionDTO.setChat(false);
        optionDTO.setPublic_post(false);

        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                        // Get deep link from result (may be null if no link is found)
                        Uri deepLink = null;
                        if (pendingDynamicLinkData != null) {
                            deepLink = pendingDynamicLinkData.getLink();
                        }

                        // Handle the deep link. For example, open the linked
                        // content, or apply promotional credit to the user's
                        // account.
                        // ...

                        // ...
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "getDynamicLink:onFailure", e);
                    }
                });

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
            /*Toast.makeText(this, "권한 있음", Toast.LENGTH_LONG).show();*/
        } else {
            /*Toast.makeText(this, "권한 없음", Toast.LENGTH_LONG).show();*/

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0])) {
                /*Toast.makeText(this, "권한 설명 필요함.", Toast.LENGTH_LONG).show();*/
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
                    /*Toast.makeText(this, permissions[i] + " 권한이 승인됨.", Toast.LENGTH_LONG).show();*/
                } else {
                    /*Toast.makeText(this, permissions[i] + " 권한이 승인되지 않음.", Toast.LENGTH_LONG).show();*/
                }
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            //Toast.makeText(this, "사용자 정보 없음", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(IntroActivity.this, Login00Activity.class));
            finish();
        } else {
            //Toast.makeText(this, "사용자 정보 있음", Toast.LENGTH_SHORT).show();

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
                        memberDTO = dataSnapshot.getValue(MemberDTO.class);
                        if (memberDTO.getKeyWord() != null) {
                            keywords = new String[memberDTO.getKeyWord().trim().split(" ").length];
                            keywords = memberDTO.getKeyWord().trim().split(" ");
                        }

                        if (memberDTO.getChecked1() != null) {
                            ArrayList<Boolean> chked1 = new ArrayList<>();
                            String[] checked1 = memberDTO.getChecked1().trim().split(" ");
                            for (int i = 0; i < checked1.length; i++) {
                                chked1.add(Boolean.parseBoolean(checked1[i]));
                            }
                            favoriteDTO.setChked1(chked1);

                            ArrayList<Boolean> chked2 = new ArrayList<>();
                            String[] checked2 = memberDTO.getChecked2().trim().split(" ");
                            for (int i = 0; i < checked2.length; i++) {
                                chked2.add(Boolean.parseBoolean(checked2[i]));
                            }
                            favoriteDTO.setChked2(chked2);

                            ArrayList<Boolean> chked3 = new ArrayList<>();
                            String[] checked3 = memberDTO.getChecked3().trim().split(" ");
                            for (int i = 0; i < checked3.length; i++) {
                                chked3.add(Boolean.parseBoolean(checked3[i]));
                            }
                            favoriteDTO.setChked3(chked3);
                        }

                        if (memberDTO.getKeyWord() != null) {
                            ArrayList<String> keyword = new ArrayList<>();
                            String[] keyw = memberDTO.getKeyWord().trim().split(" ");
                            for (int i = 0; i < keyw.length; i++) {
                                keyword.add(keyw[i]);
                            }
                            favoriteDTO.setKeyword(keyword);
                        }


                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent(IntroActivity.this, MainActivity.class));
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
        }
    }
}