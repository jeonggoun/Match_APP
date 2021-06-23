package com.example.match_app.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.example.match_app.IntroActivity;
import com.example.match_app.MainActivity;
import com.example.match_app.R;
import com.example.match_app.dto.MemberDTO;
import com.example.match_app.etc.EtcProfileActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.net.URI;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.match_app.Common.CommonMethod.memberDTO;

public class EtcFragment extends Fragment {
    private final int GET_GALLERY_IMAGE = 200;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); // 로그인한 유저의 정보 가져오기
    String uid = user != null ? user.getUid() : null; // 로그인한 유저의 고유 uid 가져오기

    private DatabaseReference mDatabaseRef;
    private FirebaseAuth firebaseAuth;
    private StorageReference storageRef;
    CircleImageView iv_profile;
    Button lL_matchlist, lL_locationAuth, lL_favoritelist;
    TextView tv_nick, tv_local, btn_profile;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("matchapp/UserAccount");
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_etc, container, false);
        iv_profile = viewGroup.findViewById(R.id.iv_profile);
        tv_nick = viewGroup.findViewById(R.id.tv_nick);
        tv_local = viewGroup.findViewById(R.id.tv_local);
        btn_profile = viewGroup.findViewById(R.id.btn_profile);
        lL_matchlist = viewGroup.findViewById(R.id.lL_matchlist);
        lL_locationAuth = viewGroup.findViewById(R.id.lL_locationAuth);
        lL_favoritelist = viewGroup.findViewById(R.id.lL_favoritelist);

        tv_nick.setText(memberDTO.getNickName());
        tv_local.setText(memberDTO.getAddress());

        btn_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToProfile();
            }
        });

        mDatabaseRef.orderByKey().equalTo(uid).addChildEventListener(new ChildEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                getFireBaseProfileImage();
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                getFireBaseProfileImage();
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {            }
            @Override
            public void onCancelled(DatabaseError databaseError) {            }
        });

        return  viewGroup;
    }

    private void getFireBaseProfileImage() {
        downloadImg();
    }

    /*private void downloadImg2() {
        Glide.with(getContext()).load(Environment.DIRECTORY_PICTURES + "/momo_img/" +uid+ ".jpg").into(iv_profile);
    }*/

    private void downloadImg() {
        storageRef = FirebaseStorage.getInstance().getReference("matchapp/profileImg");
        if (memberDTO.getFileName() != null) storageRef.child(memberDTO.getFileName()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(getContext()).load(uri).into(iv_profile);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void refresh() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
    }

    private void sendToProfile() {
        Intent nextIntent = new Intent(getActivity(), EtcProfileActivity.class);
        startActivityForResult(nextIntent,1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    refresh();
                }
            },1000);

        }
    }
}