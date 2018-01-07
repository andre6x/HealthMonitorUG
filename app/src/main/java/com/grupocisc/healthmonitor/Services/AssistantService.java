package com.grupocisc.healthmonitor.Services;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.Log;

import com.grupocisc.healthmonitor.Asthma.activities.AsthmaRegistry;
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
import com.grupocisc.healthmonitor.Utils.WakeLocker;
import com.grupocisc.healthmonitor.Weight.activities.WeightActivity;
import com.grupocisc.healthmonitor.entities.IAsthma;
import com.grupocisc.healthmonitor.entities.IColesterol;
import com.grupocisc.healthmonitor.entities.IGlucose;
import com.grupocisc.healthmonitor.entities.IHba1c;
import com.grupocisc.healthmonitor.entities.IPressure;
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

    @Override
    public void onCreate() {
        super.onCreate();
        WakeLocker.Current.acquire(getApplicationContext());

        Timer _timer = new Timer();
        _timerTask = new TimerTask() {
            @Override
            public void run() {
                Log.i(TAG,"Executing timer task");
                RunService();
            }
        };

        _timer.scheduleAtFixedRate(_timerTask,0,2000*60*60); //se ejecuta cada 2 horas
        //_timer.scheduleAtFixedRate(_timerTask,0,1000*60); //se ejecuta cada 1 segundo
        WakeLocker.Current.release();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "AssistantService has been started");
        onTaskRemoved(intent);

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

                Action<Void> pulseAction = new ActionImplement(x->checkPulseTable(),4000);
                pulseAction.invoke(null);

                Action<Void> cholesterolAction = new ActionImplement(x->checkCholesterol(),8000);
                cholesterolAction.invoke(null);

                Action<Void> hba1cAction = new ActionImplement(x->checkHBA1C(),12000);
                hba1cAction.invoke(null);

                Action<Void> glucoseAction = new ActionImplement(x->checkGlucose(),16000);
                glucoseAction.invoke(null);

                Action<Void> stateAction = new ActionImplement(x->checkState(),20000);
                stateAction.invoke(null);

                Action<Void> peakFlowAction = new ActionImplement(x->checkPeakFlowTable(),22000);
                peakFlowAction.invoke(null);
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

                if(days<3){
                    Log.i(TAG,"Last record on: "+dateString);
                }
                else {
                    //NotificationHelper.ShowNotification(getApplicationContext(),Constantes.WEIGHT_NOTIFICATION_TITLE,"no ha ingresado su peso en varios días","001", WeightActivity.class, R.mipmap.icon_peso);
                    NotificationHelper.Current.showNotification(getApplicationContext(), WeightActivity.class, WEIGHT_NOTIFICATION_ID,R.mipmap.icon_peso, WEIGHT_NOTIFICATION_CHANNEL_ID,Constantes.WEIGHT_NOTIFICATION_TITLE,"No ha ingresado su peso en "+days+" "+getCorrectWord(days));
                }
            }
            else {
                //NotificationHelper.ShowNotification(getApplicationContext(),Constantes.WEIGHT_NOTIFICATION_TITLE,"no ha ingresado su peso en varios días","001", WeightActivity.class, R.mipmap.icon_peso);
                NotificationHelper.Current.showNotification(getApplicationContext(), WeightActivity.class, WEIGHT_NOTIFICATION_ID,R.mipmap.icon_peso, WEIGHT_NOTIFICATION_CHANNEL_ID,Constantes.WEIGHT_NOTIFICATION_TITLE,"Aún no ha ingresado su peso");
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

                if(days<2){
                    Log.i(TAG,"Last record on: "+dateString);
                }
                else {
                    //NotificationHelper.ShowNotification(getApplicationContext(),Constantes.PULSE_NOTIFICATION_TITLE,"no ha ingresado su pulso en varios días","002", PulseActivity.class, R.mipmap.icon_pulse);
                    NotificationHelper.Current.showNotification(getApplicationContext(), PulseActivity.class, PULSE_NOTIFICATION_ID,R.mipmap.icon_pulse, PULSE_NOTIFICATION_CHANNEL_ID,Constantes.PULSE_NOTIFICATION_TITLE,"No ha ingresado su pulso en "+days+" "+getCorrectWord(days));
                }
            }
            else {
                //NotificationHelper.ShowNotification(getApplicationContext(),Constantes.PULSE_NOTIFICATION_TITLE,"no ha ingresado su pulso en varios días","002", PulseActivity.class, R.mipmap.icon_pulse);
                NotificationHelper.Current.showNotification(getApplicationContext(), PulseActivity.class, PULSE_NOTIFICATION_ID,R.mipmap.icon_pulse, PULSE_NOTIFICATION_CHANNEL_ID,Constantes.PULSE_NOTIFICATION_TITLE,"Aún no ha ingresado su pulso");
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
                    //NotificationHelper.ShowNotification(getApplicationContext(),Constantes.PRESSURE_NOTIFICATION_TITLE,"no ha ingresado su presión en varios días","002", PulseActivity.class, R.mipmap.icon_pulse);
                }
            }
            else {
                //NotificationHelper.ShowNotification(getApplicationContext(),Constantes.PRESSURE_NOTIFICATION_TITLE,"no ha ingresado su presión en varios días","002", PulseActivity.class, R.mipmap.icon_pulse);
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
            int GLUCOSE_NOTIFICATION_ID = 1003;
            String GLUCOSE_NOTIFICATION_CHANNEL_ID = "003";
            if(data!=null)
            {
                String dateString = data.getFecha()!=null ? data.getFecha():"";
                int days = getDays(dateString);

                if(days<1){
                    Log.i(TAG,"Last record on: "+dateString);
                }
                else {
                    //NotificationHelper.ShowNotification(getApplicationContext(),Constantes.GLUCOSE_NOTIFICATION_TITLE,"no ha ingresado su glucosa en varios días","003", GlucoseActivity.class, R.mipmap.icon_blood);
                    NotificationHelper.Current.showNotification(getApplicationContext(), GlucoseActivity.class, GLUCOSE_NOTIFICATION_ID,R.mipmap.icon_blood, GLUCOSE_NOTIFICATION_CHANNEL_ID,Constantes.GLUCOSE_NOTIFICATION_TITLE,"No ha ingresado su glucosa en "+days+" "+getCorrectWord(days));
                }
            }
            else {
                //NotificationHelper.ShowNotification(getApplicationContext(),Constantes.GLUCOSE_NOTIFICATION_TITLE,"no ha ingresado su glucosa en varios días","003", GlucoseActivity.class, R.mipmap.icon_blood);
                NotificationHelper.Current.showNotification(getApplicationContext(), GlucoseActivity.class, GLUCOSE_NOTIFICATION_ID,R.mipmap.icon_blood, GLUCOSE_NOTIFICATION_CHANNEL_ID,Constantes.GLUCOSE_NOTIFICATION_TITLE,"Aún no ha ingresado su glucosa");
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
            int CHOLESTEROL_NOTIFICATION_ID = 1005;
            String CHOLESTEROL_NOTIFICATION_CHANNEL_ID = "005";
            if(data!=null)
            {
                String dateString = data.getFecha()!=null ? data.getFecha():"";
                int days = getDays(dateString);

                if(days<4){
                    Log.i(TAG,"Last record on: "+dateString);
                }
                else {
                    //NotificationHelper.ShowNotification(getApplicationContext(),Constantes.CHOLESTEROL_NOTIFICATION_TITLE,"no ha ingresado su colesterol en varios días","004", ComplCholesterolRegistyActivity.class, R.mipmap.icon_coleste);
                    NotificationHelper.Current.showNotification(getApplicationContext(), ComplCholesterolRegistyActivity.class, CHOLESTEROL_NOTIFICATION_ID,R.mipmap.icon_coleste, CHOLESTEROL_NOTIFICATION_CHANNEL_ID,Constantes.CHOLESTEROL_NOTIFICATION_TITLE,"No ha ingresado su colesterol en "+days+" "+getCorrectWord(days));
                }
            }
            else {
                //NotificationHelper.ShowNotification(getApplicationContext(),Constantes.CHOLESTEROL_NOTIFICATION_TITLE,"no ha ingresado su colesterol en varios días","004", ComplCholesterolRegistyActivity.class, R.mipmap.icon_coleste);
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

                if(days<4){
                    Log.i(TAG,"Last record on: "+dateString);
                }
                else {
                    //NotificationHelper.ShowNotification(getApplicationContext(),Constantes.HBA1C_NOTIFICATION_TITLE,"no ha ingresado su HBA1C en varios días","005", ComplHba1cRegistyActivity.class, R.mipmap.icon_hba1c);
                    NotificationHelper.Current.showNotification(getApplicationContext(), ComplHba1cRegistyActivity.class, HBA1C_NOTIFICATION_ID,R.mipmap.icon_hba1c, HBA1C_NOTIFICATION_CHANNEL_ID,Constantes.HBA1C_NOTIFICATION_TITLE,"No ha ingresado su HBA1C en "+days+" "+getCorrectWord(days));
                }
            }
            else {
                //NotificationHelper.ShowNotification(getApplicationContext(),Constantes.HBA1C_NOTIFICATION_TITLE,"no ha ingresado su HBA1C en varios días","005", ComplHba1cRegistyActivity.class, R.mipmap.icon_hba1c);
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
        // la fecha del último registro sea diferente sea >=24 horas
        try {
            IState data = Utils.getLastRecordWithDate(HealthMonitorApplicattion.getApplication().getStateDao(), Constantes.TABLA_STATE);
            int STATE_NOTIFICATION_ID = 1004;
            String STATE_NOTIFICATION_CHANNEL_ID = "004";
            if(data!=null)
            {
                String dateString = data.getFecha()!=null ? data.getFecha():"";
                int days = getDays(dateString);

                if(days<1){
                    Log.i(TAG,"Last record on: "+dateString);
                }
                else {
                    //NotificationHelper.ShowNotification(getApplicationContext(),Constantes.STATE_NOTIFICATION_TITLE,"no ha ingresado su estado de ánimo en varios días","006", StateActivity.class, R.drawable.estado_feliz_con);
                    NotificationHelper.Current.showNotification(getApplicationContext(), StateActivity.class, STATE_NOTIFICATION_ID,R.drawable.estado_feliz_con, STATE_NOTIFICATION_CHANNEL_ID,Constantes.STATE_NOTIFICATION_TITLE,"No ha ingresado su estado de ánimo en "+days+" "+getCorrectWord(days));
                }
            }
            else {
                //NotificationHelper.ShowNotification(getApplicationContext(),Constantes.STATE_NOTIFICATION_TITLE,"no ha ingresado su estado de ánimo varios días","006", StateActivity.class, R.drawable.estado_feliz_con);
                NotificationHelper.Current.showNotification(getApplicationContext(), StateActivity.class, STATE_NOTIFICATION_ID,R.drawable.estado_feliz_con, STATE_NOTIFICATION_CHANNEL_ID,Constantes.STATE_NOTIFICATION_TITLE,"Aún no ha ingresado su estado de ánimo");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }



    void checkPeakFlowTable()
    {
        try {
            IAsthma data = Utils.getLastRecordWithDate(HealthMonitorApplicattion.getApplication().getAsthmaDao(), Constantes.TABLA_ASTHMA);
            int PEAK_NOTIFICATION_ID = 1007;
            String PEAK_NOTIFICATION_CHANNEL_ID = "007";
            if(data!=null)
            {
                String dateString = data.getFecha()!=null ? data.getFecha():"";
                int days = getDays(dateString);

                if(days<2){
                    Log.i(TAG,"Last record on: "+dateString);
                }
                else {
                    //NotificationHelper.ShowNotification(getApplicationContext(),Constantes.PULSE_NOTIFICATION_TITLE,"no ha ingresado su pulso en varios días","002", PulseActivity.class, R.mipmap.icon_pulse);
                    NotificationHelper.Current.showNotification(getApplicationContext(), AsthmaRegistry.class, PEAK_NOTIFICATION_ID,R.mipmap.header_asma, PEAK_NOTIFICATION_CHANNEL_ID,Constantes.PEAK_FLOW_NOTIFICATION_TITLE,"No ha ingresado su registro de flujo máximo en "+days+" "+getCorrectWord(days));
                }
            }
            else {
                //NotificationHelper.ShowNotification(getApplicationContext(),Constantes.PULSE_NOTIFICATION_TITLE,"no ha ingresado su pulso en varios días","002", PulseActivity.class, R.mipmap.icon_pulse);
                NotificationHelper.Current.showNotification(getApplicationContext(), AsthmaRegistry.class, PEAK_NOTIFICATION_ID,R.mipmap.header_asma, PEAK_NOTIFICATION_CHANNEL_ID,Constantes.PEAK_FLOW_NOTIFICATION_TITLE,"Aún no ha ingresado su registro de flujo máximo");
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
            sendBroadcast(new Intent("RestartService"));
            super.onDestroy();
        }
}