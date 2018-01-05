package com.grupocisc.healthmonitor.Utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.grupocisc.healthmonitor.Complementary.activities.ComplActivity;
import com.grupocisc.healthmonitor.R;

import java.util.Random;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by alex on 12/14/17.
 */

public final class NotificationHelper {
    public static void ShowNotification(Context ctx, String title, String message, String channelId, Class<?> activity, int icon)
    {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(ctx,channelId)
                        .setSmallIcon(icon)
                        .setContentTitle(title)
                        .setContentText(message)
                        .setDefaults(Notification.DEFAULT_SOUND)
                        .setAutoCancel(true);

        Intent resultIntent = new Intent(ctx, activity);

        PendingIntent resultPendingIntent = PendingIntent.getActivity(ctx,0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder.setContentIntent(resultPendingIntent);

        Random randomNumber = new Random();
        int mNotificationId = randomNumber.nextInt(50)+1;

        NotificationManager mNotifyMgr = (NotificationManager) ctx.getSystemService(NOTIFICATION_SERVICE);

        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }
}
