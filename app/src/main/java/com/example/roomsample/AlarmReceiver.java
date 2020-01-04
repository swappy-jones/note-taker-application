package com.example.roomsample;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationHeler notificationHeler = new NotificationHeler(context);
        NotificationCompat.Builder builder = notificationHeler.getChannelNotification(intent.getStringExtra(ExtraUtil.TITLE),intent.getStringExtra(ExtraUtil.DESCRIPTION));
        notificationHeler.getNotificationManager().notify(1,builder.build());
    }

}
