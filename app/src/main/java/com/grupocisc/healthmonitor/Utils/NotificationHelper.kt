package com.grupocisc.healthmonitor.Utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
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

            val notificationIntent =Intent(ctx,activity)
            val options:Bundle = if(notificationIntent.extras !=null) (notificationIntent.extras) else Bundle()
            options.putInt("notificationId",notificationId)
            notificationIntent.putExtras(options)

            //notificationIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            notificationIntent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP




            val resultPendingIntent = PendingIntent.getActivity(ctx,0,notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT)

            val notificationBuilder = NotificationCompat.Builder(ctx,channelId)
                    .setSmallIcon(icon)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setDefaults(Notification.DEFAULT_SOUND)
                    .setVibrate(longArrayOf(300,300,300,300,300))
                    .setLights(Color.RED,1500,1500)
                    .setStyle(NotificationCompat.BigTextStyle().bigText(message))
                    .addAction(R.drawable.notification_template_icon_low_bg,"Registrar",resultPendingIntent)
                    .setContentIntent(resultPendingIntent)
                    .setAutoCancel(true)

            notificationManager.notify(notificationId,notificationBuilder.build())
        }

        fun <T,K>showNotification(ctx:Context, activity1:Class<T>,activity2:Class<K> ,notificationId:Int, icon:Int, channelId:String, title:String, message:String,action1:String,action2:String){

            val notificationManager = ctx.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

                val importance = NotificationManager.IMPORTANCE_HIGH
                val mChannel = NotificationChannel(channelId, "Notification", importance)
                mChannel.description = message
                mChannel.enableLights(true)
                mChannel.lightColor = Color.RED
                mChannel.enableVibration(true)
                mChannel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
                mChannel.setShowBadge(false)
                notificationManager.createNotificationChannel(mChannel)
            }

            val action1Intent =Intent(ctx,activity1)
            val options:Bundle = if(action1Intent.extras !=null) (action1Intent.extras) else Bundle()
            options.putInt("notificationId",notificationId)
            action1Intent.putExtras(options)
            //notificationIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            action1Intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP

            val action1PendingIntent = PendingIntent.getActivity(ctx,0,action1Intent,PendingIntent.FLAG_UPDATE_CURRENT)


            val action2Intent =Intent(ctx,activity2)
            val options2:Bundle = if(action2Intent.extras !=null) (action2Intent.extras) else Bundle()
            options2.putInt("notificationId",notificationId)
            action2Intent.putExtras(options2)
            //notificationIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            action2Intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP

            val action2PendingIntent = PendingIntent.getActivity(ctx,0,action2Intent,PendingIntent.FLAG_UPDATE_CURRENT)

            val notificationBuilder = NotificationCompat.Builder(ctx,channelId)
                    .setSmallIcon(icon)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setDefaults(Notification.DEFAULT_SOUND)
                    .setVibrate(longArrayOf(300,300,300,300,300))
                    .setLights(Color.RED,1500,1500)
                    .setStyle(NotificationCompat.BigTextStyle().bigText(message))
                    .addAction(R.drawable.notification_template_icon_low_bg,action1,action1PendingIntent)
                    .addAction(R.drawable.notification_template_icon_low_bg,action2,action2PendingIntent)
                    //.setContentIntent(resultPendingIntent)
                    .setAutoCancel(true)

            notificationManager.notify(notificationId,notificationBuilder.build())
        }

        fun showNotification(ctx: Context, notificationId: Int, icon:Int, channelId: String, title: String, message: String) {

            val notificationManager = ctx.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

                val importance = NotificationManager.IMPORTANCE_HIGH
                val mChannel = NotificationChannel(channelId, "Notificacion", importance)
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
                    .setStyle(NotificationCompat.BigTextStyle().bigText(message))

            notificationManager.notify(notificationId, notificationBuilder.build())
        }

        fun cancelNotificationFromActivity(ctx:Context,bundle: Bundle?):Unit{

            if(bundle!=null){
                if(bundle.containsKey("notificationId")){
                    val notificationId = bundle.getInt("notificationId")
                    if (notificationId > 0) {
                        NotificationManagerCompat.from(ctx).cancel(notificationId)
                    }
                }
            }
        }

        fun cancelAllNotifications(ctx:Context):Unit{
            NotificationManagerCompat.from(ctx).cancelAll()
        }
    }
}