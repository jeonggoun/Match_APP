package com.example.match_app.post;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.match_app.R;

import java.text.DecimalFormat;
import java.util.Date;

public class PostDateActivity extends AppCompatActivity {
    private static final String TAG = "main: PostDateActivity";
    DatePicker datePicker;
    TimePicker timePicker;
    String date = "", time = "";


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_date);

        //UI 객체생성
        datePicker = (DatePicker) findViewById(R.id.datePicker);
        timePicker = (TimePicker) findViewById(R.id.timePicker);

        //데이터 가져오기
//        Intent intent = getIntent();
//        String data = intent.getStringExtra("data");

        //현재 시간 이전은 선택할 수 없도록 지정
        datePicker.setMinDate(System.currentTimeMillis());

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
        time = new DecimalFormat("00").format(timePicker.getHour())+":"+
                new DecimalFormat("00").format(timePicker.getMinute());
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                time = new DecimalFormat("00").format(hourOfDay) + ":" +
                        new DecimalFormat("00").format(minute);
            }
        });

        findViewById(R.id.dateRes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    //확인 버튼 클릭
    public void mOnClose(View v){
        //데이터 전달하기
        Intent intent = new Intent();
        intent.putExtra("result", date+" "+time);
        setResult(RESULT_OK, intent);
        Log.d(TAG, "mOnClose: " + date);
        //액티비티(팝업) 닫기
        finish();
    }



    @Override
    public void onBackPressed() {
        //안드로이드 백버튼 막기
        return;
    }
}

