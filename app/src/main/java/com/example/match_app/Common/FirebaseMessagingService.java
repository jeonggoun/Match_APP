package com.example.match_app.Common;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.example.match_app.IntroActivity;
import com.example.match_app.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    private static final String TAG = "FirebaseMsgService";
    private String msg, title;

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        Log.d(TAG, "Token : "+s);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d(TAG, "Notification "+remoteMessage.getFrom());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel();
        }
        if (remoteMessage.getNotification()!=null){
            String title = remoteMessage.getNotification().getTitle();
            String body = remoteMessage.getNotification().getBody();
            Log.d(TAG, "Notification Title"+title+" - Body:"+ body);

            showNotification(title, body);
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChannel() {
        NotificationChannel notificationChannel = new NotificationChannel("momo", "test momo channel",NotificationManager.IMPORTANCE_HIGH);
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

    //    @Override
//    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
//        //super.onMessageReceived(remoteMessage);
//
//        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
//            @Override
//            public void onComplete(@NonNull Task<String> task) {
//                if(!task.isSuccessful()) return;
//                String token = task.getResult();
//                Log.d(TAG, "msgToken"+token);
//            }
//        });
//
//        Log.e(TAG, "onMessageReceived");
//        title = remoteMessage.getNotification().getTitle();
//        msg = remoteMessage.getNotification().getBody();
//
//        Intent intent = new Intent(this, IntroActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // 찾아보기
//
//        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, IntroActivity.class),0);
//
//        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this).setSmallIcon(R.mipmap.ic_launcher)
//                .setContentTitle(title).setContentText(msg).setAutoCancel(true).setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
//                .setVibrate(new long[]{1, 1000});
//
//        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        notificationManager.notify(0,mBuilder.build());
//
//        mBuilder.setContentIntent(contentIntent);
//    }


}
