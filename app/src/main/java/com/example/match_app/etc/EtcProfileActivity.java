package com.example.match_app.etc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.match_app.R;
import com.example.match_app.fragment.EtcFragment;
import com.example.match_app.fragment.HomeFragment;
import com.example.match_app.login.Login04Activity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.match_app.Common.CommonMethod.memberDTO;

public class EtcProfileActivity extends AppCompatActivity {

    private static final int GET_GALLERY_IMAGE = 200;
    private DatabaseReference mDatabaseRef;
    private FirebaseAuth firebaseAuth;
    private StorageReference storageRef;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); // 로그인한 유저의 정보 가져오기
    String uid = user != null ? user.getUid() : null; // 로그인한 유저의 고유 uid 가져오기

    String filename;  //ex) profile1.jpg 로그인하는 사람에 따라 그에 식별값에 맞는 프로필 사진 가져오기
    String filepath;
    Uri file;

    EditText et_nick2;
    TextView auth_finish;
    ImageView iv_back2, profilePic;
    FrameLayout image_logo;
    private File storageDir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_etc_profile);

        firebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("matchapp");
        storageRef = FirebaseStorage.getInstance().getReference("matchapp/profileImg");
        getFireBaseProfileImage();

        et_nick2 = findViewById(R.id.et_nick2);
        et_nick2.setHint(memberDTO.getNickName());

        iv_back2 = findViewById(R.id.iv_back2);
        iv_back2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        profilePic = findViewById(R.id.profilePic);
        image_logo = findViewById(R.id.image_logo);
        image_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoAlbum();
            }
        });

        auth_finish = findViewById(R.id.auth_finish);
        auth_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nickName = et_nick2.getText().toString();
                if (nickName.trim().length() < 1) {
                    if (file != null) {
                        int changed = memberDTO.getChanged() != 1 ? 1 : 0;
                        memberDTO.setChanged(changed);
                        filename = uid + ".jpg";
                        memberDTO.setFileName(filename);
                        StorageReference riversRef = storageRef.child(filename);
                        UploadTask uploadTask = riversRef.putFile(file);

                        mDatabaseRef.child("UserAccount").child(uid).setValue(memberDTO);
                    }
                    setResult(1);
                    finish();

                } else {
                    memberDTO.setNickName(nickName);

                    if (file != null) {
                        int changed = memberDTO.getChanged() != 1 ? 1 : 0;
                        memberDTO.setChanged(changed);
                        filename = uid + ".jpg";
                        memberDTO.setFileName(filename);
                        StorageReference riversRef = storageRef.child(filename);
                        UploadTask uploadTask = riversRef.putFile(file);

                        mDatabaseRef.child("UserAccount").child(uid).setValue(memberDTO);
                    }
                    setResult(RESULT_OK);
                    finish();
                }
            }
        });

    }

    private void getFireBaseProfileImage() {
        downloadImg();
    }

/*    private void downloadImg2() {
        //다운로드할 파일을 가르키는 참조 만들기
        StorageReference pathReference = storageRef.child(memberDTO.getFileName());
        File momo = getExternalFilesDir(Environment.DIRECTORY_PICTURES+"/momo_img");
        if (!momo.isDirectory()) momo.mkdir();
        try {
            final File localFile = File.createTempFile(uid, "jpg", momo);
            pathReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getApplicationContext(), "파일 저장 성공", Toast.LENGTH_SHORT).show();
                    Glide.with(EtcProfileActivity.this).load(localFile).into(profilePic);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), "파일 저장 실패", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    private void downloadImg() {
        storageRef = FirebaseStorage.getInstance().getReference("matchapp/profileImg");
        if (memberDTO.getFileName() != null) storageRef.child(memberDTO.getFileName()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(EtcProfileActivity.this).load(uri).into(profilePic);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void gotoAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
        startActivityForResult(intent, GET_GALLERY_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 200 && resultCode == RESULT_OK && data.getData() != null) {
            if(data != null) {
                file = data.getData();
                filepath = String.valueOf(file);
                memberDTO.setFilePath(filepath);
                profilePic.setImageURI(file);
            }
        }
    }
}