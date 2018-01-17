package com.grupocisc.healthmonitor.Utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.opengl.Visibility
import android.os.Build
import android.os.Bundle
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.RemoteViews
import com.grupocisc.healthmonitor.Asthma.activities.AsthmaRegistry
import com.grupocisc.healthmonitor.Asthma.activities.PickFlowRegistry
import com.grupocisc.healthmonitor.Complementary.activities.ComplCholesterolRegistyActivity
import com.grupocisc.healthmonitor.Complementary.activities.ComplHba1cRegistyActivity
import com.grupocisc.healthmonitor.Disease.activities.DiseaseActivity
import com.grupocisc.healthmonitor.Doctor.activities.DoctorRegistre
import com.grupocisc.healthmonitor.Glucose.activities.GlucoseRegistyActivity
import com.grupocisc.healthmonitor.Insulin.activities.InsulinRegistry
import com.grupocisc.healthmonitor.Medicines.activities.MedicineRegistry
import com.grupocisc.healthmonitor.NotificationsMedical.activities.NotificationsMedicalActivity
import com.grupocisc.healthmonitor.Pulse.activities.PulseActivity
import com.grupocisc.healthmonitor.R
import com.grupocisc.healthmonitor.State.activities.StateRegistyActivity
import com.grupocisc.healthmonitor.Weight.activities.WeightRegistyActivity


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

            val drawable = ctx.applicationInfo.loadIcon(ctx.packageManager)
            val bitmap = (drawable as BitmapDrawable).bitmap

            val resultPendingIntent = PendingIntent.getActivity(ctx,0,notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT)

            val notificationBuilder = NotificationCompat.Builder(ctx,channelId)
                    .setLargeIcon(bitmap)
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

            if (Build.VERSION.SDK_INT >= 23) {
                notificationBuilder.color = ContextCompat.getColor(ctx, R.color.colorPrimary)
            } else {
                notificationBuilder.color=ctx.resources.getColor(R.color.colorPrimary)
            }

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

            val drawable = ctx.applicationInfo.loadIcon(ctx.packageManager)
            val bitmap = (drawable as BitmapDrawable).bitmap

            val notificationBuilder = NotificationCompat.Builder(ctx,channelId)
                    .setLargeIcon(bitmap)
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

            if (Build.VERSION.SDK_INT >= 23) {
                notificationBuilder.color = ContextCompat.getColor(ctx, R.color.colorPrimary)
            } else {
                notificationBuilder.color=ctx.resources.getColor(R.color.colorPrimary)
            }

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

        fun cancelNotificationFromActivity(ctx:Context,bundle: Bundle?){

            if(bundle!=null){
                if(bundle.containsKey("notificationId")){
                    val notificationId = bundle.getInt("notificationId")
                    if (notificationId > 0) {
                        NotificationManagerCompat.from(ctx).cancel(notificationId)
                    }
                }
            }
        }

        fun cancelAllNotifications(ctx:Context){
            NotificationManagerCompat.from(ctx).cancelAll()
        }

//        fun showAssistantPanel(ctx:Context, channelId:String):Unit{
//            try {
//                val notificationManager = ctx.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//
//                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//
//                    val importance = NotificationManager.IMPORTANCE_HIGH
//                    val mChannel = NotificationChannel(channelId, "Notificacion", importance)
//                    //mChannel.description = message
//                    mChannel.enableLights(true)
//                    mChannel.lightColor = Color.RED
//                    mChannel.enableVibration(true)
//                    mChannel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
//                    mChannel.setShowBadge(false)
//                    notificationManager.createNotificationChannel(mChannel)
//                }
//
//                val remoteView = RemoteViews(ctx.packageName,R.layout.control_panel)
//
//                setControlPanelClickListener(ctx,remoteView)
//
//                val notificationBuilder = NotificationCompat.Builder(ctx, channelId)
//                        .setSmallIcon(R.mipmap.ic_launcher)
//                        .setContentTitle("Panel de control Healthmonitor")
//                        .setCustomBigContentView(remoteView)
//                        .setStyle(NotificationCompat.BigTextStyle().bigText(""))
//                        .setShowWhen(false)
//                        .setOngoing(true)
//
//                notificationManager.notify(5800, notificationBuilder.build())
//            }
//            catch (ex:Exception){
//                Log.e("NotificationHelper",ex.message)
//            }
//        }

        private fun setControlPanelClickListener(ctx:Context,remoteView:RemoteViews){
            val animStateIntent = Intent(ctx,StateRegistyActivity::class.java)
            val animStatePendingIntent =PendingIntent.getActivity(ctx,0,animStateIntent,PendingIntent.FLAG_UPDATE_CURRENT)
            remoteView.setOnClickPendingIntent(R.id.opAnim,animStatePendingIntent)

            val weightIntent = Intent(ctx,WeightRegistyActivity::class.java)
            val weightPendingIntent =PendingIntent.getActivity(ctx,0,weightIntent,PendingIntent.FLAG_UPDATE_CURRENT)
            remoteView.setOnClickPendingIntent(R.id.opWeight,weightPendingIntent)

            val glucoseIntent = Intent(ctx,GlucoseRegistyActivity::class.java)
            val glucosePendingIntent =PendingIntent.getActivity(ctx,0,glucoseIntent,PendingIntent.FLAG_UPDATE_CURRENT)
            remoteView.setOnClickPendingIntent(R.id.opGlucose,glucosePendingIntent)

            val insulinIntent = Intent(ctx,InsulinRegistry::class.java)
            val insulinPendingIntent =PendingIntent.getActivity(ctx,0,insulinIntent,PendingIntent.FLAG_UPDATE_CURRENT)
            remoteView.setOnClickPendingIntent(R.id.opInsulin,insulinPendingIntent)

            val pulseIntent = Intent(ctx,PulseActivity::class.java)
            val pulsePendingIntent =PendingIntent.getActivity(ctx,0,pulseIntent,PendingIntent.FLAG_UPDATE_CURRENT)
            remoteView.setOnClickPendingIntent(R.id.opPulse,pulsePendingIntent)

            val asthmaIntent = Intent(ctx,PickFlowRegistry::class.java)
            val asthmaPendingIntent =PendingIntent.getActivity(ctx,0,asthmaIntent,PendingIntent.FLAG_UPDATE_CURRENT)
            remoteView.setOnClickPendingIntent(R.id.opAsthma,asthmaPendingIntent)

            val cholesterolIntent = Intent(ctx,ComplCholesterolRegistyActivity::class.java)
            val cholesterolPendingIntent =PendingIntent.getActivity(ctx,0,cholesterolIntent,PendingIntent.FLAG_UPDATE_CURRENT)
            remoteView.setOnClickPendingIntent(R.id.opCholesterol,cholesterolPendingIntent)

            val hba1cIntent = Intent(ctx,ComplHba1cRegistyActivity::class.java)
            val hba1cPendingIntent =PendingIntent.getActivity(ctx,0,hba1cIntent,PendingIntent.FLAG_UPDATE_CURRENT)
            remoteView.setOnClickPendingIntent(R.id.opHba1c,hba1cPendingIntent)

            val medicineIntent = Intent(ctx,MedicineRegistry::class.java)
            val medicinePendingIntent =PendingIntent.getActivity(ctx,0,medicineIntent,PendingIntent.FLAG_UPDATE_CURRENT)
            remoteView.setOnClickPendingIntent(R.id.opMedicine,medicinePendingIntent)

            val doctorIntent = Intent(ctx,DoctorRegistre::class.java)
            val doctorPendingIntent =PendingIntent.getActivity(ctx,0,doctorIntent,PendingIntent.FLAG_UPDATE_CURRENT)
            remoteView.setOnClickPendingIntent(R.id.opDoctor,doctorPendingIntent)

            val diseaseIntent = Intent(ctx,DiseaseActivity::class.java)
            val diseasePendingIntent =PendingIntent.getActivity(ctx,0,diseaseIntent,PendingIntent.FLAG_UPDATE_CURRENT)
            remoteView.setOnClickPendingIntent(R.id.opDisease,diseasePendingIntent)

            val recommendationsIntent = Intent(ctx,NotificationsMedicalActivity::class.java)
            val recommendationsPendingIntent =PendingIntent.getActivity(ctx,0,recommendationsIntent,PendingIntent.FLAG_UPDATE_CURRENT)
            remoteView.setOnClickPendingIntent(R.id.opRecommendations,recommendationsPendingIntent)

        }

        fun createAssistanNotification(ctx: Context,channelId:String):Notification{

            val notificationManager = ctx.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

                val importance = NotificationManager.IMPORTANCE_HIGH
                val mChannel = NotificationChannel(channelId, "Notificacion", importance)
                //mChannel.description = message
                mChannel.enableLights(true)
                mChannel.lightColor = Color.RED
                mChannel.enableVibration(true)
                mChannel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
                mChannel.setShowBadge(false)
                notificationManager.createNotificationChannel(mChannel)
            }

            val remoteView = RemoteViews(ctx.packageName,R.layout.control_panel)

            if(Utils.getAsmaFromPreference(ctx.applicationContext)!=null){
                var asma:String = Utils.getAsmaFromPreference(ctx.applicationContext)
                if(asma=="true"||asma=="1"||asma=="2"){
                    remoteView.setViewVisibility(R.id.opAsthma, View.VISIBLE)
                }
                else
                    remoteView.setViewVisibility(R.id.opAsthma, View.GONE)
            }
            else
                remoteView.setViewVisibility(R.id.opAsthma, View.GONE)

            setControlPanelClickListener(ctx,remoteView)

            val drawable = ctx.applicationInfo.loadIcon(ctx.packageManager)
            val bitmap = (drawable as BitmapDrawable).bitmap

            val notificationBuilder = NotificationCompat.Builder(ctx, channelId)
                    .setLargeIcon(bitmap)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("Panel de control Healthmonitor")
                    .setContentText("Expanda para acceder a las opciones")
                    .setCustomBigContentView(remoteView)
                    .setStyle(NotificationCompat.BigTextStyle().setBigContentTitle("Panel de control Healthmonitor"))
                    .setStyle(NotificationCompat.BigTextStyle().bigText("Presione sobre las opciones"))
                    .setShowWhen(false)
                    .setOngoing(true)

            if (Build.VERSION.SDK_INT >= 23) {
                notificationBuilder.color = ContextCompat.getColor(ctx, R.color.colorPrimary)
            } else {
                notificationBuilder.color=ctx.resources.getColor(R.color.colorPrimary)
            }

            return notificationBuilder.build()
        }
    }
}