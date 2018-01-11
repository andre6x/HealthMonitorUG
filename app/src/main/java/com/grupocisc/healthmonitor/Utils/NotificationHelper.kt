package com.grupocisc.healthmonitor.Utils

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.support.v4.app.NotificationCompat
import com.grupocisc.healthmonitor.Home.activities.MainActivity
import com.itextpdf.text.xml.xmp.DublinCoreProperties.setDescription
import android.app.NotificationChannel
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v4.app.TaskStackBuilder
import com.grupocisc.healthmonitor.R


/**
 * Created by alex on 1/3/18.
 */
class NotificationHelper {
    companion object Current{

        fun <T> showNotification(ctx:Context, activity:Class<T>, notificationId:Int, icon:Int, channelId:String, title:String, message:String){

            val notificationManager = ctx.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

                val importance = NotificationManager.IMPORTANCE_HIGH
                val mChannel = NotificationChannel(channelId, activity.name, importance)
                mChannel.description = message
                mChannel.enableLights(true)
                mChannel.lightColor = Color.RED
                mChannel.enableVibration(true)
                mChannel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
                mChannel.setShowBadge(false)
                notificationManager.createNotificationChannel(mChannel)
            }

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

            notificationManager.notify(notificationId,notificationBuilder.build())
        }

        fun showNotification(ctx: Context, notificationId: Int, icon:Int, channelId: String, title: String, message: String) {

            val notificationManager = ctx.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

                val importance = NotificationManager.IMPORTANCE_HIGH
                val mChannel = NotificationChannel(channelId, "Noti", importance)
                mChannel.description = message
                mChannel.enableLights(true)
                mChannel.lightColor = Color.RED
                mChannel.enableVibration(true)
                mChannel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
                mChannel.setShowBadge(false)
                notificationManager.createNotificationChannel(mChannel)
            }

            val notificationBuilder = NotificationCompat.Builder(ctx, channelId)
                    .setSmallIcon(icon)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setDefaults(Notification.DEFAULT_SOUND)
                    .setAutoCancel(true)
                    .setVibrate(longArrayOf(300, 300, 300, 300, 300))
                    .setLights(Color.RED, 1500, 1500)

            notificationManager.notify(notificationId, notificationBuilder.build())
        }
    }
}