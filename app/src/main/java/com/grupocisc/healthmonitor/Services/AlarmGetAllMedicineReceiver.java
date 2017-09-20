package com.grupocisc.healthmonitor.Services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by mpolo on 08/09/2017.
 */

public class AlarmGetAllMedicineReceiver extends BroadcastReceiver {
    private static final String TAG = "[AlarmGetAllMedRec]";
    @Override
    public void onReceive(Context context, Intent intent) {
        String Method="[onReceive]";
        Log.i(TAG, Method + "Init...");
        Log.i(TAG, Method + "context="+context);
        Log.i(TAG, Method + "context.startService...");
        context.startService(new Intent(context, AlarmGetAllMedicineService.class));
        Log.i(TAG, Method + "End...");
    }
}
