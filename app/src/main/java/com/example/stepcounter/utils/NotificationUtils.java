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
    public static final String GOAL_ACCOMPLISHED_CHANNEL_ID = "goal_accomplished_channel";

    public static void goalNotification(Context context, String titleText) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel channel = new NotificationChannel(GOAL_ACCOMPLISHED_CHANNEL_ID,
                context.getString(R.string.goal_accomplished_channel_name), NotificationManager.IMPORTANCE_HIGH);
        notificationManager.createNotificationChannel(channel);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,GOAL_ACCOMPLISHED_CHANNEL_ID)
                .setColor(ContextCompat.getColor(context,R.color.colorPrimary)).setSmallIcon(R.drawable.ic_directions_walk_24px)
                .setLargeIcon(largeIcon(context))
                .setContentTitle(titleText)
                .setContentText(context.getString(R.string.goal_half_reached_notification_body))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(context.getString(R.string.goal_reached_notification_body)))
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setContentIntent(contentIntent(context))
                .setAutoCancel(true);

        notificationManager.notify(GOAL_ACCOMPLISHED_ID, builder.build());
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
