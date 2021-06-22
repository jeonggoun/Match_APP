package com.example.match_app.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import com.example.match_app.R;
import com.example.match_app.dto.MemberDTO;
import com.example.match_app.etc.EtcProfileActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EtcFragment extends Fragment {
    private static final String TAG = "EtcFragment :";
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); // 로그인한 유저의 정보 가져오기
    String uid = user != null ? user.getUid() : null; // 로그인한 유저의 고유 uid 가져오기
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference;
    MemberDTO dto;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        dto = new MemberDTO();
        databaseReference = FirebaseDatabase.getInstance().getReference("matchapp/UserAccount");
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_etc, container, false);
        TextView tv_nick = viewGroup.findViewById(R.id.tv_nick);
        TextView tv_local = viewGroup.findViewById(R.id.tv_local);
        TextView btn_profile = viewGroup.findViewById(R.id.btn_profile);
        Button lL_matchlist = viewGroup.findViewById(R.id.lL_matchlist);
        Button lL_locationAuth = viewGroup.findViewById(R.id.lL_locationAuth);
        Button lL_favoritelist = viewGroup.findViewById(R.id.lL_favoritelist);

/*        databaseReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    dto.setAddress(task.getResult().child("address").getValue().toString());
                    dto.setNickName(task.getResult().child("nickName").getValue().toString());
                    dto.setEmailId(task.getResult().child("emailId").getValue().toString());
                    dto.setPhoneNumber(task.getResult().child("phoneNumber").getValue().toString());
                    dto.setIdToken(task.getResult().child("idToken").getValue().toString());
                    dto.setLongitude((double) task.getResult().child("longitude").getValue());
                    dto.setLatitude((double) task.getResult().child("latitude").getValue());

                    tv_nick.setText(dto.getNickName());
                    tv_local.setText(dto.getAddress());
                }
            }
        });*/

        databaseReference.orderByKey().equalTo(uid).addChildEventListener(new ChildEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                dto = dataSnapshot.getValue(MemberDTO.class);
                tv_nick.setText(dto.getNickName());
                tv_local.setText(dto.getAddress());
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

        btn_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToNext();
            }
        });

        return  viewGroup;
    }

    private void sendToNext() {
        Intent nextIntent = new Intent(getActivity(), EtcProfileActivity.class);
        nextIntent.putExtra("dto", dto);
        startActivity(nextIntent);
    }
}