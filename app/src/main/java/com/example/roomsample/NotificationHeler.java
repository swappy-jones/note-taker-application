package com.example.roomsample;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

public class NotificationHeler extends ContextWrapper {
    public static final String channelID = "NoteChannelID";
    public static final String channelName = "NoteChannelName";
    private NotificationManager notificationManager;

    public NotificationHeler(Context base) {
        super(base);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            createChannel();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createChannel() {
        NotificationChannel notificationChannel = new NotificationChannel(channelID,channelName, NotificationManager.IMPORTANCE_HIGH);
        notificationChannel.enableLights(true);
        notificationChannel.enableVibration(true);
        notificationChannel.setLightColor(R.color.colorPrimary);
        notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);

        getNotificationManager().createNotificationChannel(notificationChannel);
    }

    public NotificationManager getNotificationManager(){
        if (notificationManager == null){
            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        }
        return notificationManager;
    }

    public NotificationCompat.Builder getChannelNotification(String title, String message){
        return new NotificationCompat.Builder(getApplicationContext(),channelID)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.notification_icon)
                .setColor(getResources().getColor(R.color.colorAccent));
    }
}
