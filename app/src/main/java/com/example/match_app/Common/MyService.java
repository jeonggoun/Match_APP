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
import com.example.match_app.MainActivity;
import com.example.match_app.R;
import com.example.match_app.adapter.PostAdapter;
import com.example.match_app.dto.MemberDTO;
import com.example.match_app.dto.NotiDataDTO;
import com.example.match_app.dto.PostDTO;
import com.example.match_app.dto.PublicPostDTO;
import com.example.match_app.etc.Btn05;
import com.example.match_app.etc.Btn06;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import static com.example.match_app.Common.CommonMethod.keywords;
import static com.example.match_app.Common.CommonMethod.memberDTO;
import static com.example.match_app.Common.CommonMethod.notiDataDTO;
import static com.example.match_app.Common.CommonMethod.optionDTO;

public class MyService extends Service {
    private static final String TAG = "main:MyService";
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); // 로그인한 유저의 정보 가져오기
    String uid = user != null ? user.getUid() : null; // 로그인한 유저의 고유 uid 가져오기

    public static ArrayList<PublicPostDTO> publicPostDTO = null;
    public static ArrayList<PublicPostDTO> qaDTO = null;
    public static ArrayList<NotiDataDTO> notiDTO = null;
    public static ArrayList<PostDTO> postsDTO = null;
    PostDTO dto;

    public MyService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

        publicPostDTO = new ArrayList<>();
        qaDTO = new ArrayList<>();
        notiDTO = new ArrayList<>();
        postsDTO = new ArrayList<>();

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
                public void run() { processCommand(intent); }
            });
            thread.start();
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void processCommand(Intent intent) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        createNotificationChannel();

        database.getReference("matchapp/public_post").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                PublicPostDTO dto = snapshot.getValue(PublicPostDTO.class);
                if (dto.isRead()==false) {
                    dto.setRead(true);
                    database.getReference("matchapp/public_post").child(snapshot.getKey()).setValue(dto);
                    showNotiNewPublicPost(dto.getTitle(), dto.getContent());

                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) { }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });


        database.getReference("matchapp/Post").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull  DataSnapshot snapshot, @Nullable String previousChildName) {
                dto = snapshot.getValue(PostDTO.class);
                postsDTO.add(dto);
                if (dto.isRead()==false) {
                    for (int i = 0; i < keywords.length; i++) {
                        if (dto.getTitle().contains(keywords[i]) || dto.getContent().contains(keywords[i])) {
                            if (dto.getWriterToken() != uid) {
                                dto.setRead(true);
                                database.getReference("matchapp/Post").child(snapshot.getKey()).setValue(dto);
                                showNotiNewPost("키워드 알림!", "설정한 키워드가 포함된 새글이 작성되었습니다");
                            }
                        }
                    }
                }
            }
            @Override
            public void onChildChanged(@NonNull  DataSnapshot snapshot, @Nullable String previousChildName) { }
            @Override
            public void onChildRemoved(@NonNull  DataSnapshot snapshot) { }
            @Override
            public void onChildMoved(@NonNull  DataSnapshot snapshot, @Nullable String previousChildName) { }
            @Override
            public void onCancelled(@NonNull  DatabaseError error) { }
        });

        database.getReference("matchapp/public_post").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull  DataSnapshot snapshot, @Nullable String previousChildName) {
                publicPostDTO.add(snapshot.getValue(PublicPostDTO.class));
            }
            @Override
            public void onChildChanged(@NonNull  DataSnapshot snapshot, @Nullable String previousChildName) { }
            @Override
            public void onChildRemoved(@NonNull  DataSnapshot snapshot) { }
            @Override
            public void onChildMoved(@NonNull  DataSnapshot snapshot, @Nullable String previousChildName) { }
            @Override
            public void onCancelled(@NonNull  DatabaseError error) { }
        });

        database.getReference("matchapp/qna").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull  DataSnapshot snapshot, @Nullable String previousChildName) {
                qaDTO.add(snapshot.getValue(PublicPostDTO.class));
            }
            @Override
            public void onChildChanged(@NonNull  DataSnapshot snapshot, @Nullable String previousChildName) { }
            @Override
            public void onChildRemoved(@NonNull  DataSnapshot snapshot) { }
            @Override
            public void onChildMoved(@NonNull  DataSnapshot snapshot, @Nullable String previousChildName) { }
            @Override
            public void onCancelled(@NonNull  DatabaseError error) { }
        });

        database.getReference("matchapp/UsertAccount/"+uid).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull  DataSnapshot snapshot, @Nullable String previousChildName) {
                MemberDTO account = snapshot.getValue(MemberDTO.class);
                if (uid == account.getIdToken()){
                    memberDTO = account;
                }
            }
            @Override
            public void onChildChanged(@NonNull  DataSnapshot snapshot, @Nullable String previousChildName) {
                MemberDTO account = snapshot.getValue(MemberDTO.class);
                if (uid == account.getIdToken()){
                    memberDTO = account;
                    keywords = new String[memberDTO.getKeyWord().trim().split(" ").length];
                    keywords = memberDTO.getKeyWord().trim().split(" ");
                }
            }
            @Override
            public void onChildRemoved(@NonNull  DataSnapshot snapshot) { }
            @Override
            public void onChildMoved(@NonNull  DataSnapshot snapshot, @Nullable String previousChildName) { }
            @Override
            public void onCancelled(@NonNull  DatabaseError error) { }
        });

        database.getReference("matchapp/NotiData").child(uid).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                notiDTO.add(snapshot.getValue(NotiDataDTO.class));
            }
            @Override
            public void onChildChanged(@NonNull  DataSnapshot snapshot, @Nullable String previousChildName) {
                NotiDataDTO dto = snapshot.getValue(NotiDataDTO.class);

                for (int i=0; i<notiDTO.size(); i++) {
                    if (notiDTO.get(i).getPostToken().equals(dto.getPostToken())) {
                        notiDTO.remove(i);
                        notiDTO.add(dto);
                    }
                }

            }
            @Override
            public void onChildRemoved(@NonNull  DataSnapshot snapshot) { }
            @Override
            public void onChildMoved(@NonNull  DataSnapshot snapshot, @Nullable String previousChildName) { }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChannel() {
        NotificationChannel notificationChannel = new NotificationChannel("momo", "test momo channel", NotificationManager.IMPORTANCE_HIGH);
        notificationChannel.setDescription("My test notification channel");

        NotificationChannel notificationChannel2 = new NotificationChannel("momo2", "test momo channel2", NotificationManager.IMPORTANCE_HIGH);
        notificationChannel2.setDescription("My test notification channel2");

        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(notificationChannel);
    }

    public void showNotiNewPost(String title, String body) {
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("data", "data");

        if (optionDTO.isSound() == true) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "momo")
                    .setContentTitle(title)
                    .setContentText(body)
                    .setSmallIcon(R.drawable.noti_smallicon)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.noti_largeicon2))
                    .setAutoCancel(true)
                    .setContentIntent(PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT));
            notificationManager.notify(1, builder.build());
        }else {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "momo2")
                    .setContentTitle(title)
                    .setContentText(body)
                    .setSmallIcon(R.drawable.noti_smallicon)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.noti_largeicon2))
                    .setAutoCancel(true)
                    .setContentIntent(PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT));
        }
    }

    public void showNotiNewPublicPost(String title, String body) {
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent(this, Btn05.class);
        if (optionDTO.isVib() == true) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "momo")
                    .setContentTitle(title)
                    .setContentText(body)
                    .setSmallIcon(R.drawable.noti_smallicon)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.noti_largeicon))
                    .setAutoCancel(true)
                    .setContentIntent(PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT));
            notificationManager.notify(1, builder.build());
        }else {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "momo2")
                    .setContentTitle(title)
                    .setContentText(body)
                    .setSmallIcon(R.drawable.noti_smallicon)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.noti_largeicon))
                    .setAutoCancel(true)
                    .setContentIntent(PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT));
            notificationManager.notify(1, builder.build());
        }
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