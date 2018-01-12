/**
 * Copyright 2015 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.grupocisc.healthmonitor.gcmClasses;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.view.View;

import com.google.android.gms.gcm.GcmListenerService;
import com.grupocisc.healthmonitor.HealthMonitorApplicattion;
import com.grupocisc.healthmonitor.Home.activities.MainActivity;
import com.grupocisc.healthmonitor.R;
import com.grupocisc.healthmonitor.Utils.Utils;

import java.sql.SQLException;
import java.util.Calendar;


public class MyGcmListenerService extends GcmListenerService {

    private static final String TAG = "MyGcmListenerService";
    // [START receive_message]
    @Override
    public void onMessageReceived(String from, Bundle data) {
        String message = data.getString("mensaje");
        String tipo    = data.getString("tipo");
        String seccion = data.getString("seccion");
        String titulo  = data.getString("titulo");

        Log.e(TAG, "From: " + from);
        Log.e(TAG, "Message: " + message);

        if (from.startsWith("/topics/")) {
            // message received from some topic.
        } else {
            // normal downstream message.
        }

        // [START_EXCLUDE]
        int idSeccion = 0;
        if(seccion!=null && !seccion.equals(""))
            idSeccion = Integer.parseInt(seccion);
        if(tipo==null)
            tipo="";
        if(titulo==null)
            titulo="";
        if(message==null)
            message="";
        sendNotification(message, tipo, idSeccion, titulo);
        saveDataNotifcationsMedicalDB(message, tipo, "", "");
    }
    // [END receive_message]


    private void sendNotification(String message, String tipo, int idSeccion, String titulo) {
        PendingIntent pendingIntent;
        Intent intent;
        //int numeroNotificacion = SharedPreferencesManager.getValorEsperadoInt(getApplicationContext(),"NotificationCount","NotificationNumber");
        //if(numeroNotificacion>100)
        //    numeroNotificacion=0;
        Log.e("tipo", "->" + tipo);
        Log.e("idSeccion", "->" + idSeccion);

        Double d = Math.random() * 1024;
        Integer numeroNotificacion = d.intValue();

            intent = new Intent(this, MainActivity.class);
            intent.putExtra("idSeccion", idSeccion);
            intent.putExtra("tipo", tipo);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            TaskStackBuilder taskStackBuilder  = TaskStackBuilder.create(this).addParentStack(MainActivity.class).addNextIntent(intent);
            pendingIntent = taskStackBuilder.getPendingIntent(numeroNotificacion, PendingIntent.FLAG_UPDATE_CURRENT);
            //PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);


        Uri urlsound = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.notificacion_campana);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                //.setSmallIcon(R.drawable.icon_vivo)
                .setSmallIcon(R.mipmap.ic_notification)
                .setContentTitle(titulo)
                .setContentText(message)
                .setSound(urlsound)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(numeroNotificacion , notificationBuilder.build());
        //numeroNotificacion++;
        //SharedPreferencesManager.setValorInt(getApplicationContext(),"NotificationCount",numeroNotificacion,"NotificationNumber");
    }

    //GUARDOS DATOS EN LA TABLA BD
    public  void saveDataNotifcationsMedicalDB(String message, String tipo, String fecha, String hora){
          try {
              String fechaOS = inicializarFecha();
              String horaOS = inicializarHora();

            //setear datos al objeto y guardar y BD
            Utils.DbsaveNotificationsMedicalFromDatabase(message,
                    tipo,
                    fechaOS,
                    horaOS,
                    HealthMonitorApplicattion.getApplication().getNotifcationsMedicalDao());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

     private static String  inicializarFecha(){
        int year, month, day;
        //obtener fechay hroa de Calendar
        Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);


        //setear fecha
         String date = ""+day+"/"+  seteaMes(month+1)  +"/"+year;
        return date;
    }

    private static String seteaMes(int m){
         String mes = String.valueOf(m);
         if(mes.length()==1){
             mes = "0" + mes;
         }
         return mes;
    }

    private static String inicializarHora(){
        int  hour, minute;
        //obtener fechay hroa de Calendar
        Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);
        //setear hora
        String hourString = hour < 10 ? "0"+hour : ""+hour;
        String minuteString = minute < 10 ? "0"+minute : ""+minute;
        String time = ""+hourString+":"+minuteString;
        return  time;
    }

}
