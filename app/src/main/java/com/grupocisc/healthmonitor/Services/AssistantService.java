package com.grupocisc.healthmonitor.Services;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.Log;

import com.grupocisc.healthmonitor.Complementary.activities.ComplActivity;
import com.grupocisc.healthmonitor.Complementary.activities.ComplCholesterolRegistyActivity;
import com.grupocisc.healthmonitor.Complementary.activities.ComplHba1cRegistyActivity;
import com.grupocisc.healthmonitor.Glucose.activities.GlucoseActivity;
import com.grupocisc.healthmonitor.HealthMonitorApplicattion;
import com.grupocisc.healthmonitor.Pulse.activities.PulseActivity;
import com.grupocisc.healthmonitor.R;
import com.grupocisc.healthmonitor.State.activities.StateActivity;
import com.grupocisc.healthmonitor.Utils.Constantes;
import com.grupocisc.healthmonitor.Utils.NotificationHelper;
import com.grupocisc.healthmonitor.Utils.ServiceChecker;
import com.grupocisc.healthmonitor.Utils.Utils;
import com.grupocisc.healthmonitor.Weight.activities.WeightActivity;
import com.grupocisc.healthmonitor.entities.IColesterol;
import com.grupocisc.healthmonitor.entities.IGlucose;
import com.grupocisc.healthmonitor.entities.IHba1c;
import com.grupocisc.healthmonitor.entities.IPressure;
import com.grupocisc.healthmonitor.entities.IPulse;
import com.grupocisc.healthmonitor.entities.IState;
import com.grupocisc.healthmonitor.entities.IV2Cholesterol;
import com.grupocisc.healthmonitor.entities.IWeight;
import com.j256.ormlite.dao.Dao;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import butterknife.ButterKnife;

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

    final String TAG = "AssistantService";

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        if(!ServiceChecker.Current.isServiceRunning(getApplicationContext(),AssistantService.class))
        {
            Log.i(TAG,"Iniciando el servicio de asistencia");
            Intent restartServiceIntent = new Intent(getApplicationContext(),this.getClass());
            restartServiceIntent.setPackage(getPackageName());
            startService(restartServiceIntent);
        }
        else
        {
            Log.i(TAG,"El servicio de asistencia ya está en ejecución");
        }

        super.onTaskRemoved(rootIntent);
    }

    /*private boolean isServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }*/

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        onTaskRemoved(intent);

        Log.i(TAG, "AssistantService has been started");

        Timer _timer = new Timer();
        _timerTask = new TimerTask() {
            @Override
            public void run() {
                Log.i(TAG,"Executing timer task");
                RunService();
            }
        };

        //El servicio se ejecutará cada hora
        //_timer.scheduleAtFixedRate(_timerTask,0,4000*60*60); //se ejecuta cada 4 horas
        _timer.scheduleAtFixedRate(_timerTask,0,1000*60); //se ejecuta cada 1 segundo

        return START_STICKY;
    }

    void RunService(){
        //Permite saber si el usuario ha iniciado sesión o no
        if(Utils.getEmailFromPreference(getApplicationContext()) != null)
        {
            int currentHour = getHours();
            if(currentHour <8 || currentHour>18) // se hace esto para que no se ejecute en horas de la noche y la madrugada
            {
                Log.i(TAG,"The service is not available, it's "+currentHour+" hours");
            }
            else {
                Action<Void> weightAction = new ActionImplement(x->checkWeightTable(),0);
                weightAction.invoke(null);

                Action<Void> pulseAction = new ActionImplement(x->checkPulseTable(),2000);
                pulseAction.invoke(null);

                Action<Void> cholesterolAction = new ActionImplement(x->checkCholesterol(),4000);
                cholesterolAction.invoke(null);

                Action<Void> hba1cAction = new ActionImplement(x->checkHBA1C(),6000);
                hba1cAction.invoke(null);

                Action<Void> glucoseAction = new ActionImplement(x->checkGlucose(),8000);
                glucoseAction.invoke(null);

                Action<Void> stateAction = new ActionImplement(x->checkState(),10000);
                stateAction.invoke(null);
            }
        }
        else {
            Log.i(TAG,"User didn't log in.");
        }
    }

    interface Action<T>{
        void invoke(T args);
    }

    class ActionImplement<T> implements Action<T>{
        Action<T> action;
        int delay;
        public ActionImplement(Action<T> action, int delay) {
            this.action = action;
            this.delay = delay;
        }

        @Override
        public void invoke(T args) {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(new Runnable() {
                public void run() {
                    action.invoke(null);
                }
            }, this.delay);
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

    int getHours(){
        Date today = new Date();
        int hours =  today.getHours();
        return  hours;
    }

    void checkWeightTable()
    {
        try {
            IWeight data = Utils.getLastRecordWithDate(HealthMonitorApplicattion.getApplication().getWeightDao(), Constantes.TABLA_WEIGHT);
            if(data!=null)
            {
                String dateString = data.getFecha()!=null ? data.getFecha():"";
                int days = getDays(dateString);

                if(days<3){
                    Log.i(TAG,"Last record on: "+dateString);
                }
                else {
                    NotificationHelper.ShowNotification(getApplicationContext(),Constantes.WEIGHT_NOTIFICATION_TITLE,"no ha ingresado su peso en varios días","001", WeightActivity.class, R.mipmap.icon_peso);
                }
            }
            else {
                NotificationHelper.ShowNotification(getApplicationContext(),Constantes.WEIGHT_NOTIFICATION_TITLE,"no ha ingresado su peso en varios días","001", WeightActivity.class, R.mipmap.icon_peso);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    void checkPulseTable()
    {
        try {
            IPulse data = Utils.getLastRecordWithDate(HealthMonitorApplicattion.getApplication().getPulseDao(), Constantes.TABLA_PULSE);
            if(data!=null)
            {
                String dateString = data.getFecha()!=null ? data.getFecha():"";
                int days = getDays(dateString);

                if(days<2){
                    Log.i(TAG,"Last record on: "+dateString);
                }
                else {
                    NotificationHelper.ShowNotification(getApplicationContext(),Constantes.PULSE_NOTIFICATION_TITLE,"no ha ingresado su pulso en varios días","002", PulseActivity.class, R.mipmap.icon_pulse);
                }
            }
            else {
                NotificationHelper.ShowNotification(getApplicationContext(),Constantes.PULSE_NOTIFICATION_TITLE,"no ha ingresado su pulso en varios días","002", PulseActivity.class, R.mipmap.icon_pulse);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    void checkPressureTable()
    {
        // la fecha del último registro sea diferente sea >=24 horas
        try {
            IPressure data = Utils.getLastRecordWithDate(HealthMonitorApplicattion.getApplication().getPressureDao(), Constantes.TABLA_PRESSURE);
            if(data!=null)
            {
                String dateString = data.getFecha()!=null ? data.getFecha():"";
                int days = getDays(dateString);

                if(days<2){
                    Log.i(TAG,"Last record on: "+dateString);
                }
                else {
                    NotificationHelper.ShowNotification(getApplicationContext(),Constantes.PRESSURE_NOTIFICATION_TITLE,"no ha ingresado su presión en varios días","002", PulseActivity.class, R.mipmap.icon_pulse);
                }
            }
            else {
                NotificationHelper.ShowNotification(getApplicationContext(),Constantes.PRESSURE_NOTIFICATION_TITLE,"no ha ingresado su presión en varios días","002", PulseActivity.class, R.mipmap.icon_pulse);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    void checkGlucose()
    {
        // la fecha del último registro sea diferente sea >=24 horas
        try {
            IGlucose data = Utils.getLastRecordWithDate(HealthMonitorApplicattion.getApplication().getGlucoseDao(), Constantes.TABLA_GLUCOSA);
            if(data!=null)
            {
                String dateString = data.getFecha()!=null ? data.getFecha():"";
                int days = getDays(dateString);

                if(days<1){
                    Log.i(TAG,"Last record on: "+dateString);
                }
                else {
                    NotificationHelper.ShowNotification(getApplicationContext(),Constantes.GLUCOSE_NOTIFICATION_TITLE,"no ha ingresado su glucosa en varios días","003", GlucoseActivity.class, R.mipmap.icon_blood);
                }
            }
            else {
                NotificationHelper.ShowNotification(getApplicationContext(),Constantes.GLUCOSE_NOTIFICATION_TITLE,"no ha ingresado su glucosa en varios días","003", GlucoseActivity.class, R.mipmap.icon_blood);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    void checkCholesterol()
    {
        // la fecha del último registro sea diferente sea >=24 horas
        try {
            IColesterol data = Utils.getLastRecordWithDate(HealthMonitorApplicattion.getApplication().getColesterolDao(), Constantes.TABLA_COLESTEROL);
            if(data!=null)
            {
                String dateString = data.getFecha()!=null ? data.getFecha():"";
                int days = getDays(dateString);

                if(days<4){
                    Log.i(TAG,"Last record on: "+dateString);
                }
                else {
                    NotificationHelper.ShowNotification(getApplicationContext(),Constantes.CHOLESTEROL_NOTIFICATION_TITLE,"no ha ingresado su colesterol en varios días","004", ComplCholesterolRegistyActivity.class, R.mipmap.icon_coleste);
                }
            }
            else {
                NotificationHelper.ShowNotification(getApplicationContext(),Constantes.CHOLESTEROL_NOTIFICATION_TITLE,"no ha ingresado su colesterol en varios días","004", ComplCholesterolRegistyActivity.class, R.mipmap.icon_coleste);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    void checkHBA1C()
    {
        try {
            IHba1c data = Utils.getLastRecordWithDate(HealthMonitorApplicattion.getApplication().getHba1cDao(), Constantes.TABLA_HBA1C);
            if(data!=null)
            {
                String dateString = data.getFecha()!=null ? data.getFecha():"";
                int days = getDays(dateString);

                if(days<4){
                    Log.i(TAG,"Last record on: "+dateString);
                }
                else {
                    NotificationHelper.ShowNotification(getApplicationContext(),Constantes.HBA1C_NOTIFICATION_TITLE,"no ha ingresado su HBA1C en varios días","005", ComplHba1cRegistyActivity.class, R.mipmap.icon_hba1c);
                }
            }
            else {
                NotificationHelper.ShowNotification(getApplicationContext(),Constantes.HBA1C_NOTIFICATION_TITLE,"no ha ingresado su HBA1C en varios días","005", ComplHba1cRegistyActivity.class, R.mipmap.icon_hba1c);
            }
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
    }


    void checkState()
    {
        // la fecha del último registro sea diferente sea >=24 horas
        try {
            IState data = Utils.getLastRecordWithDate(HealthMonitorApplicattion.getApplication().getStateDao(), Constantes.TABLA_STATE);
            if(data!=null)
            {
                String dateString = data.getFecha()!=null ? data.getFecha():"";
                int days = getDays(dateString);

                if(days<1){
                    Log.i(TAG,"Last record on: "+dateString);
                }
                else {
                    NotificationHelper.ShowNotification(getApplicationContext(),Constantes.STATE_NOTIFICATION_TITLE,"no ha ingresado su estado de ánimo en varios días","006", StateActivity.class, R.drawable.estado_feliz_con);
                }
            }
            else {
                NotificationHelper.ShowNotification(getApplicationContext(),Constantes.STATE_NOTIFICATION_TITLE,"no ha ingresado su estado de ánimo varios días","006", StateActivity.class, R.drawable.estado_feliz_con);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

        @Override
    public void onDestroy() {
        super.onDestroy();
    }
}