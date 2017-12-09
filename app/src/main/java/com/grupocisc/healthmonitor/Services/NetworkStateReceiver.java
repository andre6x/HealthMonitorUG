package com.grupocisc.healthmonitor.Services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * Created by alex on 12/8/17.
 */

public class NetworkStateReceiver extends BroadcastReceiver {

    ConnectivityManager _connectivityManager;
    @Override
    public void onReceive(Context context, Intent intent) {
        _connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(_connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState()== NetworkInfo.State.CONNECTED)
        {
            Log.i("NetworkStateReceiver","Conexión por wifi");
            context.startService(new Intent(context, AssistantService.class));
        }
        else
        {
            Log.i("NetworkStateReceiver","Otro tipo de conexión");
        }
    }
}
