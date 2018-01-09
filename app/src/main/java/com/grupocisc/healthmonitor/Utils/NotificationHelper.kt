package com.grupocisc.healthmonitor.Utils

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.support.v4.app.NotificationCompat

/**
 * Created by alex on 1/3/18.
 */
class NotificationHelper {
    companion object Current{
        fun <T> showNotification(ctx:Context,activity:Class<T>,notificationId:Int,icon:Int,channelId:String,title:String,message:String){

            val resultIntent =Intent(ctx,activity)

            val resultPendingIntent = PendingIntent.getActivity(ctx,0,resultIntent,PendingIntent.FLAG_UPDATE_CURRENT)

            val notificationBuilder = NotificationCompat.Builder(ctx,channelId)
                    .setSmallIcon(icon)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setDefaults(Notification.DEFAULT_SOUND)
                    .setAutoCancel(true)
                    .setVibrate(longArrayOf(300,300,300,300,300))
                    .setLights(Color.RED,1500,1500)
                    .setContentIntent(resultPendingIntent)

            val notificationManager = ctx.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(notificationId,notificationBuilder.build())
        }

        fun showNotification(ctx: Context,notificationId: Int,icon:Int,channelId: String,title: String,message: String) {
            //val resultIntent =Intent(ctx,activity)

            //val resultPendingIntent = PendingIntent.getActivity(ctx,0,resultIntent,PendingIntent.FLAG_UPDATE_CURRENT)

            val notificationBuilder = NotificationCompat.Builder(ctx, channelId)
                    .setSmallIcon(icon)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setDefaults(Notification.DEFAULT_SOUND)
                    .setAutoCancel(true)
                    .setVibrate(longArrayOf(300, 300, 300, 300, 300))
                    .setLights(Color.RED, 1500, 1500)
            //.setContentIntent(resultPendingIntent)

            val notificationManager = ctx.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(notificationId, notificationBuilder.build())
        }
    }
}