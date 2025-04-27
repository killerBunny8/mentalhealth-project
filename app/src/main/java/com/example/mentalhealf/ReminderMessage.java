package com.example.mentalhealf;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class ReminderMessage extends BroadcastReceiver {
    private static final String CHANNEL_ID = "mental_health_reminder_channel";
    private static int notificationId = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        //get title and message from intent
        String title = intent.getStringExtra("title");
        String message = intent.getStringExtra("message");

        createNotificationChannel(context);
        //Build notification
        Notification notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.mipmap.mindfullness)
                .setContentTitle(title != null ? title : "Mental Health Reminder")
                .setContentText(message != null ? message : "Time to check in with yourself!")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)//Remove when clicked
                .build();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(++notificationId, notification); // Unique ID for each notif
    }
    //creates the notificaction channel for newer android versions
    private void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Mental Health Reminders";
            String description = "Channel for mental health reminder notifications";
            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            channel.enableVibration(true);

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}