package com.grupocisc.healthmonitor.Services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.grupocisc.healthmonitor.HealthMonitorApplicattion;
import com.grupocisc.healthmonitor.Utils.Utils;

import java.sql.SQLException;

/**
 * Created by alex on 12/8/17.
 */

public class AssistantService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private static final String sendServer = "false";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        String TAG = "AssitantService";
        Log.i(TAG, "AssistantService has benn started");
        try {
            if (Utils.GetPulseFromDataBase(HealthMonitorApplicattion.getApplication().getPulseDao()).size() > 0) {
                //TODO
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return START_STICKY;
    }

        @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
