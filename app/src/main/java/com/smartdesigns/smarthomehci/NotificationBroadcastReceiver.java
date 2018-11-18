package com.smartdesigns.smarthomehci;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.TaskStackBuilder;

public class NotificationBroadcastReceiver extends BroadcastReceiver {
    public static final String ACTION_NEW_MESSAGE = "com.smartdesigns.smarthomehci.ACTION_NEW_MESSAGE";

    @Override
    public void onReceive(Context context, Intent intent) {

        int messageId = intent.getIntExtra(Devices.MESSAGE_ID, 0);

        Intent notificationIntent = new Intent(context, Devices.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(Devices.class);
        stackBuilder.addNextIntent(notificationIntent);

        final PendingIntent contentIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new Notification.Builder(context)
                .setContentTitle("Blinds has changed status")
                .setContentText("Blinds are up")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                .setAutoCancel(true)
                .setContentIntent(contentIntent)
                .build();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(messageId, notification);
    }

}
