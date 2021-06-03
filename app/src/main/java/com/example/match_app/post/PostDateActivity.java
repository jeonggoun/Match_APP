package com.example.match_app.post;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.match_app.R;

import java.text.DecimalFormat;
import java.util.Date;

public class PostDateActivity extends AppCompatActivity {
    DatePicker datePicker;
    TimePicker timePicker;
    String date = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_date);

        //UI 객체생성
        datePicker = (DatePicker) findViewById(R.id.datePicker);
        timePicker = (TimePicker) findViewById(R.id.timePicker);

        //데이터 가져오기
        Intent intent = getIntent();
        String data = intent.getStringExtra("data");

        //datePicker.setText(data);



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
    }

    //확인 버튼 클릭
    public void mOnClose(View v){
        //데이터 전달하기
        Intent intent = new Intent();
        intent.putExtra("result", "Close Popup");
        setResult(RESULT_OK, intent);

        //액티비티(팝업) 닫기
        finish();
    }
}

