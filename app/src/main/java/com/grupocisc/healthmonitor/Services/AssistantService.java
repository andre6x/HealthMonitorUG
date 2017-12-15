package com.grupocisc.healthmonitor.Services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.grupocisc.healthmonitor.HealthMonitorApplicattion;
import com.grupocisc.healthmonitor.Utils.Constantes;
import com.grupocisc.healthmonitor.Utils.NotificationHelper;
import com.grupocisc.healthmonitor.Utils.Utils;
import com.grupocisc.healthmonitor.entities.IPulse;
import com.grupocisc.healthmonitor.entities.IWeight;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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

        if(Utils.getEmailFromPreference(getApplicationContext()) != null){
            //TODO Poner un timerTask para mostrar la notificación cada 2 horas siempre y cuando
            // la fecha del último registro sea diferente sea >=24 horas
            try {
                IWeight data = Utils.getLastRecordWithDate(HealthMonitorApplicattion.getApplication().getWeightDao(), Constantes.TABLA_WEIGHT);
                if(data!=null)
                {
                    String dateString = data.getFecha()!=null ? data.getFecha():"";
                    //SimpleDateFormat formater = new SimpleDateFormat()
                    //Date date =
                    Log.i(TAG,"La fecha no es nula, la fecha es "+dateString);
                }
                else {
                    //TODO PRESENTAR LA NOTIFICACION
                    NotificationHelper.Create(getApplicationContext(),Constantes.TABLA_WEIGHT,"no ha ingresado su peso en varios días","001");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

        return START_STICKY;
    }

        @Override
    public void onDestroy() {
        super.onDestroy();
    }
}