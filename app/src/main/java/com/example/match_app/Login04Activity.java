package com.example.match_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.match_app.dto.MemberDTO;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Login04Activity extends AppCompatActivity {
    Button btn01;
    EditText et_01;
    TextView textView;
    private DatabaseReference mDatabaseRef;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login04);

        textView = findViewById(R.id.textView);
        et_01 = findViewById(R.id.et_01);
        btn01 = findViewById(R.id.button);

        firebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("matchapp");

        btn01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nickName = et_01.getText().toString();
                mDatabaseRef.child("UserAccount").child(firebaseAuth.getUid()).child("address").setValue(nickName);

                sendToNext();
            }
        });
    }

    private void sendToNext() {
        MemberDTO dto = new MemberDTO();
        dto.setNickName(mDatabaseRef.child("UserAccount").child(firebaseAuth.getUid()).child("nickName").get().toString());
        dto.setPhoneNumber(mDatabaseRef.child("UserAccount").child(firebaseAuth.getUid()).child("phoneNumber").get().toString());
        dto.setAddress(mDatabaseRef.child("UserAccount").child(firebaseAuth.getUid()).child("address").get().toString());

        Intent mainIntent = new Intent(Login04Activity.this, MainActivity.class);
        mainIntent.putExtra("dto", dto);
        startActivity(mainIntent);

        finish();
    }
}