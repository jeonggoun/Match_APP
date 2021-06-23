package com.example.match_app.fragment;

import android.app.Activity;
import android.app.ListActivity;
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
import com.example.match_app.etc.Btn01;
import com.example.match_app.etc.Btn02;
import com.example.match_app.etc.Btn03;
import com.example.match_app.etc.Btn04;
import com.example.match_app.etc.Btn05;
import com.example.match_app.etc.Btn06;
import com.example.match_app.etc.Btn07;
import com.example.match_app.etc.EtcProfileActivity;
import com.example.match_app.etc.FavoriteListActivity;
import com.example.match_app.etc.GpsListActivity;
import com.example.match_app.etc.MatchListActivity;
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
    Button lL_matchlist, lL_locationAuth, lL_favoritelist, btn_01, btn_02, btn_03, btn_04, btn_05, btn_06, btn_07;
    TextView tv_nick, tv_local, btn_profile;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("matchapp/UserAccount");
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_etc, container, false);
        iv_profile = viewGroup.findViewById(R.id.iv_profile);

        tv_nick = viewGroup.findViewById(R.id.tv_nick);
        tv_local = viewGroup.findViewById(R.id.tv_local);
        tv_nick.setText(memberDTO.getNickName());
        tv_local.setText(memberDTO.getAddress());

        btn_profile = viewGroup.findViewById(R.id.btn_profile);
        btn_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToProfile();
            }
        });
        lL_matchlist = viewGroup.findViewById(R.id.lL_matchlist);
        lL_matchlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextIntent = new Intent(getActivity(), MatchListActivity.class);
                startActivity(nextIntent);
            }
        });
        lL_favoritelist = viewGroup.findViewById(R.id.lL_favoritelist);
        lL_favoritelist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextIntent = new Intent(getActivity(), FavoriteListActivity.class);
                startActivity(nextIntent);
            }
        });
        lL_locationAuth = viewGroup.findViewById(R.id.lL_locationAuth);
        lL_locationAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextIntent = new Intent(getActivity(), GpsListActivity.class);
                startActivity(nextIntent);
            }
        });
        btn_01 = viewGroup.findViewById(R.id.btn01);
        btn_01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextIntent = new Intent(getActivity(), Btn01.class);
                startActivity(nextIntent);
            }
        });
        btn_02 = viewGroup.findViewById(R.id.btn02);
        btn_02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextIntent = new Intent(getActivity(), Btn02.class);
                startActivity(nextIntent);
            }
        });
        btn_03 = viewGroup.findViewById(R.id.btn03);
        btn_03.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextIntent = new Intent(getActivity(), Btn03.class);
                startActivity(nextIntent);
            }
        });
        btn_04 = viewGroup.findViewById(R.id.btn04);
        btn_04.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextIntent = new Intent(getActivity(), Btn04.class);
                startActivity(nextIntent);
            }
        });
        btn_05 = viewGroup.findViewById(R.id.btn05);
        btn_05.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextIntent = new Intent(getActivity(), Btn05.class);
                startActivity(nextIntent);
            }
        });
        btn_06 = viewGroup.findViewById(R.id.btn06);
        btn_06.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextIntent = new Intent(getActivity(), Btn06.class);
                startActivity(nextIntent);
            }
        });
        btn_07 = viewGroup.findViewById(R.id.btn07);
        btn_07.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextIntent = new Intent(getActivity(), Btn07.class);
                startActivity(nextIntent);
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