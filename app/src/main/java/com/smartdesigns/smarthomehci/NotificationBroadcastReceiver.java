package com.smartdesigns.smarthomehci;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.TaskStackBuilder;

public class NotificationBroadcastReceiver extends BroadcastReceiver {
    public static final String ACTION_NEW_MESSAGE = "com.smartdesigns.smarthomehci.ACTION_NEW_MESSAGE";
    private int counter = 0;

    @Override
    public void onReceive(Context context, Intent intent) {

        int messageId = intent.getIntExtra("id",0);

        Intent notificationIntent = new Intent(context, Devices.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(Devices.class);
        stackBuilder.addNextIntent(notificationIntent);

        final PendingIntent contentIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        @SuppressLint("ResourceAsColor") Notification notification = new Notification.Builder(context)
                .setContentTitle(intent.getStringExtra("title"))
                .setContentText(intent.getStringExtra("subtitle"))
                .setSmallIcon(R.drawable.ic_smarthome)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), intent.getIntExtra("image", R.drawable.notfound)))
                .setColor(R.color.blue)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(contentIntent)
                .build();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(messageId, notification);
    }

}
