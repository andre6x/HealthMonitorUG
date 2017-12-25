package com.grupocisc.healthmonitor.Services;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * Created by alex on 12/8/17.
 */

public class NetworkStateReceiver extends BroadcastReceiver {

    ConnectivityManager _connectivityManager;
    Context _ctx;
    final String TAG="NetworkStateReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        int connectionType=0;
        _ctx=context;
        Intent assistantService = new Intent(context, AssistantService.class);
        //_connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//        if(_connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState()== NetworkInfo.State.CONNECTED)
//        {
            //Log.i(TAG,"Conexión por wifi");
            //connectionType = ConnectivityManager.TYPE_WIFI;
            //assistantService.putExtra("connectionType",connectionType);
            if(!isServiceRunning(AssistantService.class))
            {
                Log.i(TAG,"Iniciando el servicio de asistencia");
                context.startService(assistantService);
            }
            else
            {
                Log.i(TAG,"El servicio de asistencia ya está en ejecución");
                Log.i(TAG,"Se va a detener el servicio");
                context.stopService(assistantService);
                Log.i(TAG,"Se va a reiniciar el servicio");

                context.startService(assistantService);
            }
//        }
//        else
//        {
            /*Log.i("NetworkStateReceiver","Otro tipo de conexión");
            connectionType = ConnectivityManager.TYPE_MOBILE | ConnectivityManager.TYPE_MOBILE_DUN | ConnectivityManager.TYPE_VPN | ConnectivityManager.TYPE_WIMAX;
            assistantService.putExtra("connectionType", connectionType);*/
        //}
    }


    private boolean isServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) _ctx.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
