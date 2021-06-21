package com.example.match_app.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.match_app.R;
import com.example.match_app.dto.MemberDTO;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Login04Activity extends AppCompatActivity {
    TextView auth_finish;
    EditText et_01;
    private DatabaseReference mDatabaseRef;
    private FirebaseAuth firebaseAuth;
    MemberDTO dto = new MemberDTO();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login04);
        dto = (MemberDTO) getIntent().getSerializableExtra("dto");
        et_01 = findViewById(R.id.et_01);
        auth_finish = findViewById(R.id.auth_finish);
        firebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("matchapp");



        auth_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nickName = et_01.getText().toString();
                dto.setNickName(nickName);
                sendToNext();
            }
        });
    }

    private void sendToNext() {
        Intent nextIntent = new Intent(Login04Activity.this, Login02Activity.class);
        nextIntent.putExtra("dto", dto);
        startActivity(nextIntent);
        finish();
    }
}