package com.grupocisc.healthmonitor.Services;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.Log;

import com.grupocisc.healthmonitor.Asthma.activities.AsthmaRegistry;
import com.grupocisc.healthmonitor.Complementary.activities.ComplCholesterolRegistyActivity;
import com.grupocisc.healthmonitor.Complementary.activities.ComplHba1cRegistyActivity;
import com.grupocisc.healthmonitor.Glucose.activities.GlucoseRegistyActivity;
import com.grupocisc.healthmonitor.HealthMonitorApplicattion;
import com.grupocisc.healthmonitor.Insulin.activities.InsulinRegistry;
import com.grupocisc.healthmonitor.Pulse.activities.PulseActivityAuto;
import com.grupocisc.healthmonitor.Pulse.activities.PulseRegistyActivity;
import com.grupocisc.healthmonitor.R;
import com.grupocisc.healthmonitor.State.activities.StateRegistyActivity;
import com.grupocisc.healthmonitor.Utils.Constantes;
import com.grupocisc.healthmonitor.Utils.NotificationHelper;
import com.grupocisc.healthmonitor.Utils.ServiceChecker;
import com.grupocisc.healthmonitor.Utils.Utils;
import com.grupocisc.healthmonitor.Utils.WakeLocker;
import com.grupocisc.healthmonitor.Weight.activities.WeightRegistyActivity;
import com.grupocisc.healthmonitor.entities.EInsulin;
import com.grupocisc.healthmonitor.entities.IAsthma;
import com.grupocisc.healthmonitor.entities.IColesterol;
import com.grupocisc.healthmonitor.entities.IGlucose;
import com.grupocisc.healthmonitor.entities.IHba1c;
import com.grupocisc.healthmonitor.entities.IPulse;
import com.grupocisc.healthmonitor.entities.IState;
import com.grupocisc.healthmonitor.entities.IWeight;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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

    TimerTask _timerTask;

    final String TAG = "AssistantService";

    static AssistantService instance;

    @Override
    public void onCreate() {
        super.onCreate();
    }


    @Override
    public void onTaskRemoved(Intent rootIntent) {
        if(Utils.getEmailFromPreference(getApplicationContext())!=null){
            if(!ServiceChecker.Current.isServiceRunning(getApplicationContext(),AssistantService.class))
            {
                Log.i(TAG,"Iniciando el servicio de asistencia");
                Intent restartServiceIntent = new Intent(getApplicationContext(),this.getClass());
                startService(restartServiceIntent);
            }
            else
            {
                Log.i(TAG,"El servicio de asistencia ya está en ejecución");
            }
        }
        else {
            Log.i(TAG,"Couldn't restart the service, user didn't logged in");
        }

        super.onTaskRemoved(rootIntent);
    }
    Integer period = 2000*60*60;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "AssistantService has been started");
        onTaskRemoved(intent);

//        if(Utils.getEmailFromPreference(getApplicationContext())!=null){
//            NotificationHelper.Current.showAssistantPanel(getApplicationContext(),"1115");
//        }

        instance = this;

        WakeLocker.Current.acquire(getApplicationContext());

        Timer _timer = new Timer();

        _timerTask = new TimerTask() {
            @Override
            public void run() {
                Log.i(TAG,"Executing timer task");
                    RunService();
            }
        };

        _timer.scheduleAtFixedRate(_timerTask,0,period);
        //_timer.scheduleAtFixedRate(_timerTask,0,2000*60); //se ejecuta cada 2 minutos
        WakeLocker.Current.release();

//        Runnable task = new Runnable() {
//            @Override
//            public void run() {
                startForeground(101, NotificationHelper.Current.createAssistanNotification(getApplicationContext(),"5800"));
//            }
//        };
//
//        if(Utils.getEmailFromPreference(getApplicationContext())!=null) {
//            task.run();
//        }

        return START_STICKY;
    }

    public static void stopService(Context ctx){
        if(ServiceChecker.Current.isServiceRunning(ctx.getApplicationContext(),AssistantService.class)){
            instance.stopForeground(true);
            instance.stopSelf();
            Log.i(instance.TAG,"AssistantService is stopped");
        }
    }

    void RunService(){
        //Permite saber si el usuario ha iniciado sesión o no
        if(Utils.getEmailFromPreference(getApplicationContext()) != null)
        {
            int currentHour = getHours();
            if(currentHour <8 || currentHour>18)
            {
                Log.i(TAG,"The service is not available, it's "+currentHour+" hours");
            }
            else {
                Action<Void> weightAction = new ActionImplement(x -> checkWeightTable(), 0);
                weightAction.invoke(null);

                Action<Void> pulseAction = new ActionImplement(x -> checkPulseTable(), 2500);
                pulseAction.invoke(null);

                Action<Void> cholesterolAction = new ActionImplement(x -> checkCholesterol(), 5000);
                cholesterolAction.invoke(null);

                Action<Void> hba1cAction = new ActionImplement(x -> checkHBA1C(), 7500);
                hba1cAction.invoke(null);

                Action<Void> glucoseAction = new ActionImplement(x -> checkGlucose(), 10000);
                glucoseAction.invoke(null);

                Action<Void> stateAction = new ActionImplement(x -> checkState(), 12500);
                stateAction.invoke(null);

                Action<Void> peakFlowAction = new ActionImplement(x -> checkPeakFlowTable(), 15000);
                peakFlowAction.invoke(null);


                Action<Void> insulinAction = new ActionImplement(x->checkInsulinTable(),17500);
                insulinAction.invoke(null);
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

    String getCorrectWord(int days){
        String dayString;
        if(days>1)
            dayString="días";
        else
            dayString="día";

        return  dayString;
    }

    void checkWeightTable()
    {
        try {
            IWeight data = Utils.getLastRecordWithDate(HealthMonitorApplicattion.getApplication().getWeightDao(), Constantes.TABLA_WEIGHT);
            int WEIGHT_NOTIFICATION_ID = 1001;
            String WEIGHT_NOTIFICATION_CHANNEL_ID = "001";
            if(data!=null)
            {
                String dateString = data.getFecha()!=null ? data.getFecha():"";
                int days = getDays(dateString);

                if(days<=3){
                    Log.i(TAG,Constantes.TABLA_WEIGHT+" Last record on: "+dateString);
                }
                else {
                    NotificationHelper.Current.showNotification(getApplicationContext(), WeightRegistyActivity.class, WEIGHT_NOTIFICATION_ID,R.mipmap.icon_peso, WEIGHT_NOTIFICATION_CHANNEL_ID,Constantes.WEIGHT_NOTIFICATION_TITLE,"No ha ingresado su peso en "+days+" "+getCorrectWord(days));
                }
            }
            else {
                NotificationHelper.Current.showNotification(getApplicationContext(), WeightRegistyActivity.class, WEIGHT_NOTIFICATION_ID,R.mipmap.icon_peso, WEIGHT_NOTIFICATION_CHANNEL_ID,Constantes.WEIGHT_NOTIFICATION_TITLE,"Aún no ha ingresado su peso");
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
            int PULSE_NOTIFICATION_ID = 1002;
            String PULSE_NOTIFICATION_CHANNEL_ID = "002";
            if(data!=null)
            {
                String dateString = data.getFecha()!=null ? data.getFecha():"";
                int days = getDays(dateString);

                if(days<=2){
                    Log.i(TAG,Constantes.TABLA_PULSE+" Last record on: "+dateString);
                }
                else {
                    //if(SensorChecker.Current.isSupported(getApplicationContext(),Sensor.TYPE_HEART_BEAT)){
                        NotificationHelper.Current.showNotification(getApplicationContext(), PulseRegistyActivity.class, PulseActivityAuto.class, PULSE_NOTIFICATION_ID,R.mipmap.icon_pulse, PULSE_NOTIFICATION_CHANNEL_ID,Constantes.PULSE_NOTIFICATION_TITLE,"No ha ingresado su pulso en "+days+" "+getCorrectWord(days),"Registro manual","Registro automático");
                    //}
                    //else {
                    //    NotificationHelper.Current.showNotification(getApplicationContext(), PulseRegistyActivity.class, PULSE_NOTIFICATION_ID,R.mipmap.icon_pulse, PULSE_NOTIFICATION_CHANNEL_ID,Constantes.PULSE_NOTIFICATION_TITLE,"No ha ingresado su pulso en "+days+" "+getCorrectWord(days));
                    //}
                }
            }
            else {
                //if(SensorChecker.Current.isSupported(getApplicationContext(),Sensor.TYPE_HEART_BEAT)){
                    NotificationHelper.Current.showNotification(getApplicationContext(), PulseRegistyActivity.class, PulseActivityAuto.class,PULSE_NOTIFICATION_ID,R.mipmap.icon_pulse, PULSE_NOTIFICATION_CHANNEL_ID, Constantes.PULSE_NOTIFICATION_TITLE,"Aún no ha ingresado su pulso","Registro manual","Registro automático");
                //}
                //else {
                //    NotificationHelper.Current.showNotification(getApplicationContext(), PulseRegistyActivity.class,PULSE_NOTIFICATION_ID,R.mipmap.icon_pulse, PULSE_NOTIFICATION_CHANNEL_ID, Constantes.PULSE_NOTIFICATION_TITLE,"Aún no ha ingresado su pulso");
                //}
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    void checkGlucose()
    {
        try {
            IGlucose data = Utils.getLastRecordWithDate(HealthMonitorApplicattion.getApplication().getGlucoseDao(), Constantes.TABLA_GLUCOSA);
            int GLUCOSE_NOTIFICATION_ID = 1003;
            String GLUCOSE_NOTIFICATION_CHANNEL_ID = "003";
            if(data!=null)
            {
                String dateString = data.getFecha()!=null ? data.getFecha():"";
                int days = getDays(dateString);

                if(days<=1){
                    Log.i(TAG,Constantes.TABLA_GLUCOSA+" Last record on: "+dateString);
                }
                else {
                    NotificationHelper.Current.showNotification(getApplicationContext(), GlucoseRegistyActivity.class, GLUCOSE_NOTIFICATION_ID,R.mipmap.icon_blood, GLUCOSE_NOTIFICATION_CHANNEL_ID,Constantes.GLUCOSE_NOTIFICATION_TITLE,"No ha ingresado su glucosa en "+days+" "+getCorrectWord(days));
                }
            }
            else {
                NotificationHelper.Current.showNotification(getApplicationContext(), GlucoseRegistyActivity.class, GLUCOSE_NOTIFICATION_ID,R.mipmap.icon_blood, GLUCOSE_NOTIFICATION_CHANNEL_ID,Constantes.GLUCOSE_NOTIFICATION_TITLE,"Aún no ha ingresado su glucosa");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    void checkCholesterol()
    {
        try {
            IColesterol data = Utils.getLastRecordWithDate(HealthMonitorApplicattion.getApplication().getColesterolDao(), Constantes.TABLA_COLESTEROL);
            int CHOLESTEROL_NOTIFICATION_ID = 1005;
            String CHOLESTEROL_NOTIFICATION_CHANNEL_ID = "005";
            if(data!=null)
            {
                String dateString = data.getFecha()!=null ? data.getFecha():"";
                int days = getDays(dateString);

                if(days<=1){
                    Log.i(TAG,Constantes.TABLA_COLESTEROL+" Last record on: "+dateString);
                }
                else {
                    NotificationHelper.Current.showNotification(getApplicationContext(), ComplCholesterolRegistyActivity.class, CHOLESTEROL_NOTIFICATION_ID,R.mipmap.icon_coleste, CHOLESTEROL_NOTIFICATION_CHANNEL_ID,Constantes.CHOLESTEROL_NOTIFICATION_TITLE,"No ha ingresado su colesterol en "+days+" "+getCorrectWord(days));
                }
            }
            else {
                NotificationHelper.Current.showNotification(getApplicationContext(), ComplCholesterolRegistyActivity.class, CHOLESTEROL_NOTIFICATION_ID,R.mipmap.icon_coleste, CHOLESTEROL_NOTIFICATION_CHANNEL_ID,Constantes.CHOLESTEROL_NOTIFICATION_TITLE,"Aún no ha ingresado su colesterol");
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
            int HBA1C_NOTIFICATION_ID = 1006;
            String HBA1C_NOTIFICATION_CHANNEL_ID = "006";
            if(data!=null)
            {
                String dateString = data.getFecha()!=null ? data.getFecha():"";
                int days = getDays(dateString);

                if(days<=2){
                    Log.i(TAG,Constantes.TABLA_HBA1C+" Last record on: "+dateString);
                }
                else {
                    NotificationHelper.Current.showNotification(getApplicationContext(), ComplHba1cRegistyActivity.class, HBA1C_NOTIFICATION_ID,R.mipmap.icon_hba1c, HBA1C_NOTIFICATION_CHANNEL_ID,Constantes.HBA1C_NOTIFICATION_TITLE,"No ha ingresado su HBA1C en "+days+" "+getCorrectWord(days));
                }
            }
            else {
                NotificationHelper.Current.showNotification(getApplicationContext(), ComplHba1cRegistyActivity.class, HBA1C_NOTIFICATION_ID,R.mipmap.icon_hba1c, HBA1C_NOTIFICATION_CHANNEL_ID,Constantes.HBA1C_NOTIFICATION_TITLE,"Aún no ha ingresado su HBA1C");
            }
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
    }

    void checkState()
    {
        try {
            IState data = Utils.getLastRecordWithDate(HealthMonitorApplicattion.getApplication().getStateDao(), Constantes.TABLA_STATE);
            int STATE_NOTIFICATION_ID = 1004;
            String STATE_NOTIFICATION_CHANNEL_ID = "004";
            if(data!=null)
            {
                String dateString = data.getFecha()!=null ? data.getFecha():"";
                int days = getDays(dateString);

                if(days<=1){
                    Log.i(TAG,Constantes.TABLA_STATE+" Last record on: "+dateString);
                }
                else {
                    NotificationHelper.Current.showNotification(getApplicationContext(), StateRegistyActivity.class, STATE_NOTIFICATION_ID,R.drawable.estado_feliz_con, STATE_NOTIFICATION_CHANNEL_ID,Constantes.STATE_NOTIFICATION_TITLE,"No ha ingresado su estado de ánimo en "+days+" "+getCorrectWord(days));
                }
            }
            else {
                NotificationHelper.Current.showNotification(getApplicationContext(), StateRegistyActivity.class, STATE_NOTIFICATION_ID,R.drawable.estado_feliz_con, STATE_NOTIFICATION_CHANNEL_ID,Constantes.STATE_NOTIFICATION_TITLE,"Aún no ha ingresado su estado de ánimo");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    void checkPeakFlowTable()
    {
        if(Utils.getAsmaFromPreference(getApplicationContext())!=null) {
            String asma = Utils.getAsmaFromPreference(getApplicationContext());
            if (asma.equals("true") || asma.equals("1")||asma.equals("2")) {
                try {
                    IAsthma data = Utils.getLastRecordWithDate(HealthMonitorApplicattion.getApplication().getAsthmaDao(), Constantes.TABLA_ASTHMA);
                    int PEAK_NOTIFICATION_ID = 1007;
                    String PEAK_NOTIFICATION_CHANNEL_ID = "007";
                    if(data!=null)
                    {
                        String dateString = data.getFecha()!=null ? data.getFecha():"";
                        int days = getDays(dateString);

                        if(days<=2){
                            Log.i(TAG,Constantes.TABLA_ASTHMA+" Last record on: "+dateString);
                        }
                        else {
                            NotificationHelper.Current.showNotification(getApplicationContext(), AsthmaRegistry.class, PEAK_NOTIFICATION_ID,R.mipmap.icon_inhalator, PEAK_NOTIFICATION_CHANNEL_ID,Constantes.PEAK_FLOW_NOTIFICATION_TITLE,"No ha ingresado su registro de flujo máximo en "+days+" "+getCorrectWord(days));
                        }
                    }
                    else {
                        NotificationHelper.Current.showNotification(getApplicationContext(), AsthmaRegistry.class, PEAK_NOTIFICATION_ID,R.mipmap.icon_inhalator, PEAK_NOTIFICATION_CHANNEL_ID,Constantes.PEAK_FLOW_NOTIFICATION_TITLE,"Aún no ha ingresado su registro de flujo máximo");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    void checkInsulinTable()
    {
        try {
            EInsulin data = Utils.getLastRecordWithDate(HealthMonitorApplicattion.getApplication().getInsulinDao(), Constantes.TABLA_INSULIN);
            int INSULIN_NOTIFICATION_ID = 1008;
            String INSULIN_NOTIFICATION_CHANNEL_ID = "008";
            if(data!=null)
            {
                String dateString = data.getFecha()!=null ? data.getFecha():"";
                int days = getDays(dateString);

                if(days<=1){
                    Log.i(TAG,Constantes.TABLA_INSULIN+" Last record on: "+dateString);
                }
                else {
                    NotificationHelper.Current.showNotification(getApplicationContext(), InsulinRegistry.class, INSULIN_NOTIFICATION_ID,R.mipmap.icon_insulina, INSULIN_NOTIFICATION_CHANNEL_ID,Constantes.INSULIN_NOTIFICATION_TITLE,"No ha ingresado su registro de insulina en "+days+" "+getCorrectWord(days));
                }
            }
            else {
                NotificationHelper.Current.showNotification(getApplicationContext(), InsulinRegistry.class, INSULIN_NOTIFICATION_ID,R.mipmap.icon_insulina, INSULIN_NOTIFICATION_CHANNEL_ID,Constantes.INSULIN_NOTIFICATION_TITLE,"Aún no ha ingresado su registro de insulina");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


        @Override
    public void onDestroy() {
            Log.i(TAG, "Stopping service");
            _timerTask.cancel();
            //sendBroadcast(new Intent("RestartService"));
            super.onDestroy();
        }
}