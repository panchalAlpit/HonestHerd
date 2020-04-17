package com.mobilefirst.honestherd.HHFirebaseSerivce;

import android.app.NotificationManager;
import android.content.Context;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.mobilefirst.honestherd.R;
import com.google.firebase.messaging.RemoteMessage;
import com.hypertrack.sdk.HyperTrackMessagingService;

import java.util.Map;

public class HHMessaging_service extends HyperTrackMessagingService {


    @Override
    public void onNewToken(String newToken) {
        super.onNewToken(newToken);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "channel_id")
                .setContentTitle("Test")
                .setContentText("Body")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setStyle(new NotificationCompat.BigTextStyle())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }

    @Override
    public boolean onMessageReceived(@Nullable Map<String, String> notification) {
        return super.onMessageReceived(notification);
    }
}
