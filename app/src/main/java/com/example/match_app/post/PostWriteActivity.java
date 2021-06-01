package com.example.match_app.post;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.match_app.R;
import com.example.match_app.dto.PostDTO;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;
import java.util.Date;

public class PostWriteActivity extends AppCompatActivity {
    private static final String TAG = "로그 PostWriteActivity";

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference;
    Button cancel, next;
    private PostDTO dto;
    TextView etTitle, etFee, etContent;
    DatePicker datePicker;
    String date = "";

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);
        databaseReference = firebaseDatabase.getReference("matchapp/Post");

        datePicker = (DatePicker) findViewById(R.id.datePicker);

        Date tempDate = new Date();
        date = new DecimalFormat("0000").format(tempDate.getYear()) + "/" +
                new DecimalFormat("00").format(tempDate.getMonth() + 1)
                + "/" + new DecimalFormat("00").format(tempDate.getDay());

        datePicker.init(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(),
                new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker datePicker, int year, int month, int day) {
                        date = new DecimalFormat("0000").format(year) + "/" +
                                new DecimalFormat("00").format(month + 1)
                                + "/" + new DecimalFormat("00").format(day);
                    }
                });

        dto = new PostDTO();

        //버튼 찾기
        cancel = findViewById(R.id.cancel);
        next = findViewById(R.id.next);
        etTitle = findViewById(R.id.etTitle);
        etFee = findViewById(R.id.etFee);
        etContent = findViewById(R.id.etContent);

        //취소 버튼 누르면 write액티비티 끈다
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: 여기까지 옴");
                dto.setGame("모름");
                dto.setFee(etFee.getText().toString());
                dto.setTitle(etTitle.getText().toString());
                dto.setTime(new Date().toString());
                //calendar.getDate();
                dto.setPlace("모름");
                dto.setContent(etContent.getText().toString());
                dto.setImgPath("모름");

                databaseReference.push().setValue(dto);

                finish();

//        ListItemDTO dto0 = new ListItemDTO(0, "테니스", "테니스 치실 분", "2021/5/26", "농성테니스장", "무료", "#");
//        adapter.addDto(dto0);    //어디 DTO 받아올 건지 물어볼 예정
//        databaseReference.push().setValue(dto0);
            }
        });


    }
}