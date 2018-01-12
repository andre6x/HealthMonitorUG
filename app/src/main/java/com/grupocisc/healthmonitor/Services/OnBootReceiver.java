package com.grupocisc.healthmonitor.Services;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.hardware.Sensor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.AlarmManagerCompat;
import android.util.Log;

import com.grupocisc.healthmonitor.Utils.SensorChecker;
import com.grupocisc.healthmonitor.Utils.ServiceChecker;
import com.grupocisc.healthmonitor.Utils.Utils;

/**
 * Created by alex on 12/8/17.
 */

public class OnBootReceiver extends BroadcastReceiver {
    //Context _ctx;
    final String TAG="OnBootReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {

//        if(Utils.getEmailFromPreference(context.getApplicationContext()) != null){
//            if (SensorChecker.Current.isSupported(context.getApplicationContext(), Sensor.TYPE_PRESSURE)){
//                scheduler = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//
//                Intent barometerService = new Intent(context.getApplicationContext(),BarometerService.class);
//                PendingIntent pendingIntent = PendingIntent.getService(context.getApplicationContext(),0,barometerService,PendingIntent.FLAG_UPDATE_CURRENT);
//
//                scheduler.setInexactRepeating(AlarmManager.RTC_WAKEUP,System.currentTimeMillis(),AlarmManager.INTERVAL_HALF_HOUR,pendingIntent);
//            }
//            else {
//                Log.i(TAG,"El dispositivo no soporta el sensor de barómetro");
//            }
//        }

        Intent barometerService = new Intent(context, BarometerService.class);

        if(!ServiceChecker.Current.isServiceRunning(context,BarometerService.class)){
            Log.i(TAG, "Iniciando el servicio de lectura de temperatura");
            context.startService(barometerService);
        }
        else
        {
            Log.i(TAG, "El servicio de lectura de temperatura ya está en ejecución");
        }

        Intent assistantService = new Intent(context, AssistantService.class);

        if (!ServiceChecker.Current.isServiceRunning(context, AssistantService.class)) {
            Log.i(TAG, "Iniciando el servicio de asistencia");
            context.startService(assistantService);
        } else {
            Log.i(TAG, "El servicio de asistencia ya está en ejecución");
        }
    }
}