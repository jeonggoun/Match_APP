package com.example.match_app.Common;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.example.match_app.IntroActivity;
import com.example.match_app.R;
import com.example.match_app.dto.PostDTO;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MyService extends Service {
    private static final String TAG = "main:MyService";
    public static String[] keywords = {};
    public MyService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: 실행됨");
    }

    // 서비스가 실행되면 무조건 onStartCommand를 실행한다.
    @Override //        메인에서 넘겨준 인텐트, 플래그, 실행될때 보이는 숫자
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: " + flags + ", " + startId);
        // 비정상적으로 종료되었을때
        if(intent == null){
            return Service.START_STICKY; // 재시작해서 정상적으로 만든다
        }else {
            Thread thread = new Thread(new Runnable() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void run() {
                    Log.d(TAG, "onChildAdded: 쏼라 aabbccddd");
                    processCommand(intent);
                }
            });
            thread.start();
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void processCommand(Intent intent) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("matchapp/public_post");
        createNotificationChannel();
        Log.d(TAG, "onChildAdded: 쏼라 aabbccddd");
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                PostDTO dto = snapshot.getValue(PostDTO.class);
                showNotification(dto.getTitle(), dto.getContent());
                Log.d(TAG, "onChildAdded: 쏼라 aabbccddd");
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        DatabaseReference myRef2 = database.getReference("matchapp/Post");
        Log.d(TAG, "onChildAdded: 쏼라 aabbccdddee");
        myRef2.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull  DataSnapshot snapshot, @Nullable String previousChildName) {
                PostDTO dto = snapshot.getValue(PostDTO.class);
                for(int i = 0; i< keywords.length; i++){
                    if(dto.getTitle().contains(keywords[i])||dto.getContent().contains(keywords[i])){
                        showNotification("키워드 알림!", "등록한 키워드 새글이 작성되었습니다");
                    }
                }
            }
            @Override
            public void onChildChanged(@NonNull  DataSnapshot snapshot, @Nullable String previousChildName) {

            }
            @Override
            public void onChildRemoved(@NonNull  DataSnapshot snapshot) {

            }
            @Override
            public void onChildMoved(@NonNull  DataSnapshot snapshot, @Nullable String previousChildName) {

            }
            @Override
            public void onCancelled(@NonNull  DatabaseError error) {

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChannel() {
        NotificationChannel notificationChannel = new NotificationChannel("momo", "test momo channel", NotificationManager.IMPORTANCE_HIGH);
        notificationChannel.setDescription("My test notification channel");

        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(notificationChannel);
    }

    public void showNotification(String title, String body) {
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent(this, IntroActivity.class);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "momo")
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setAutoCancel(true)
                .setContentIntent(PendingIntent.getActivity(getApplicationContext(),0, intent, PendingIntent.FLAG_UPDATE_CURRENT));
        notificationManager.notify(1,builder.build());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}