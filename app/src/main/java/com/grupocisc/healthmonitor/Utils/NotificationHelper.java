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

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by alex on 12/14/17.
 */

public final class NotificationHelper {
    public static void Create(Context ctx, String title, String message, String channelId)
    {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(ctx,channelId)
                        .setSmallIcon(R.drawable.heart_pulse)
                        .setContentTitle(title)
                        .setContentText(message);

        Intent resultIntent = new Intent(ctx, ComplActivity.class);

        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        ctx,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        mBuilder.setContentIntent(resultPendingIntent);

        int mNotificationId = 001;

        NotificationManager mNotifyMgr = (NotificationManager) ctx.getSystemService(NOTIFICATION_SERVICE);

        mNotifyMgr.notify(mNotificationId, mBuilder.build());
        mNotifyMgr.cancel(mNotificationId);
    }
}
