package com.grupocisc.healthmonitor.Services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.grupocisc.healthmonitor.Utils.ServiceChecker;

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