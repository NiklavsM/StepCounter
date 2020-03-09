package com.example.stepcounter.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.example.stepcounter.MainActivity;
import com.example.stepcounter.R;

public class NotificationUtils {

    private static final int GOAL_ACCOMPLISHED_ID = 1000;
    private static final String GOAL_ACCOMPLISHED_CHANNEL_ID = "goal_accomplished_channel";
    private static final String RUNNING_IN_BACKGROUND_CHANNEL_ID = "RUNNING_IN_BACKGROUND_CHANNEL_ID";

    public static void goalNotification(Context context, String titleText, String bodyText) {
        addChannel(context, GOAL_ACCOMPLISHED_CHANNEL_ID, context.getString(R.string.goal_accomplished_channel_name), NotificationManager.IMPORTANCE_HIGH);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, GOAL_ACCOMPLISHED_CHANNEL_ID)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setSmallIcon(R.drawable.ic_directions_walk_24px)
                .setLargeIcon(largeIcon(context))
                .setContentTitle(titleText)
                .setContentText(bodyText)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setContentIntent(contentIntent(context))
                .setAutoCancel(true);

        notificationManager.notify(GOAL_ACCOMPLISHED_ID, builder.build());
    }


    public static Notification getRunningInBackgroundNotification(Context context) {
        addChannel(context, RUNNING_IN_BACKGROUND_CHANNEL_ID, context.getString(R.string.running_in_background), NotificationManager.IMPORTANCE_LOW);
        Intent notificationIntent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
        Notification notification = new NotificationCompat
                .Builder(context, RUNNING_IN_BACKGROUND_CHANNEL_ID)
                .setContentTitle(context.getString(R.string.running_in_background))
                .setLargeIcon(largeIcon(context))
                .setSmallIcon(R.drawable.ic_directions_walk_24px)
                .setContentIntent(pendingIntent).build();
        return notification;
    }

    private static void addChannel(Context context, String id, String name, int importance) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel channel = new NotificationChannel(id, name, importance);
        notificationManager.createNotificationChannel(channel);
    }

    private static PendingIntent contentIntent(Context context) {
        Intent startActivityIntent = new Intent(context, MainActivity.class);
        return PendingIntent.getActivity(context, GOAL_ACCOMPLISHED_ID, startActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private static Bitmap largeIcon(Context context) {
        Resources res = context.getResources();
        return BitmapFactory.decodeResource(res, R.drawable.ic_directions_walk_24px);
    }
}
