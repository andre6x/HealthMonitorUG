package com.grupocisc.healthmonitor.Services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.grupocisc.healthmonitor.Complementary.activities.ComplActivity;
import com.grupocisc.healthmonitor.HealthMonitorApplicattion;
import com.grupocisc.healthmonitor.R;
import com.grupocisc.healthmonitor.Utils.Constantes;
import com.grupocisc.healthmonitor.Utils.NotificationHelper;
import com.grupocisc.healthmonitor.Utils.Utils;
import com.grupocisc.healthmonitor.Weight.activities.WeightActivity;
import com.grupocisc.healthmonitor.entities.IPulse;
import com.grupocisc.healthmonitor.entities.IWeight;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * Created by alex on 12/8/17.
 */

public class AssistantService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    //private static final String sendServer = "false";
    TimerTask _timerTask;

    final String TAG = "AssitantService";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        Log.i(TAG, "AssistantService has benn started");

        Timer _timer = new Timer();
        _timerTask = new TimerTask() {
            @Override
            public void run() {
                Log.i(TAG,"Executing timer task");

                RunService();
            }
        };

        //El servicio se ejecutará cada hora
        _timer.scheduleAtFixedRate(_timerTask,0,1000*60*60);

        return START_STICKY;
    }

    void RunService(){
        //Permite saber si el usuario ha iniciado sesión o no
        if(Utils.getEmailFromPreference(getApplicationContext()) != null){
            //TODO Poner un timerTask para mostrar la notificación cada 2 horas siempre y cuando
            // la fecha del último registro sea diferente sea >=24 horas
            try {
                IWeight data = Utils.getLastRecordWithDate(HealthMonitorApplicattion.getApplication().getWeightDao(), Constantes.TABLA_WEIGHT);
                if(data!=null)
                {
                    String dateString = data.getFecha()!=null ? data.getFecha():"";
                    int days = getDays(dateString);

                    if(days<1){
                        Log.i(TAG,"Last record on: "+dateString);
                    }
                    else {
                        NotificationHelper.ShowNotification(getApplicationContext(),Constantes.TABLA_WEIGHT,"no ha ingresado su peso en varios días","001", WeightActivity.class, R.mipmap.menu_peso);
                    }
                }
                else {
                    NotificationHelper.ShowNotification(getApplicationContext(),Constantes.TABLA_WEIGHT,"no ha ingresado su peso en varios días","001", WeightActivity.class, R.mipmap.menu_peso);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }


    int getDays(String dateString) throws ParseException {
        SimpleDateFormat formater = new SimpleDateFormat("yyyy/MM/dd");
        Date date = formater.parse(dateString);
        Date today = new Date();

        long daysInMilliseconds = TimeUnit.DAYS.toDays(today.getTime() - date.getTime());
        int days= (int)(daysInMilliseconds/(1000*60*60*24));

        return  days;
    }

        @Override
    public void onDestroy() {
        super.onDestroy();
    }
}