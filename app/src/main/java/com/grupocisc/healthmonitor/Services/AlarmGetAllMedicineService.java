package com.grupocisc.healthmonitor.Services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.grupocisc.healthmonitor.HealthMonitorApplicattion;
import com.grupocisc.healthmonitor.Home.activities.MainActivity;
import com.grupocisc.healthmonitor.State.activities.StateRegistyActivity;
import com.grupocisc.healthmonitor.Utils.Utils;
import com.grupocisc.healthmonitor.entities.EAlarmDetails;
import com.grupocisc.healthmonitor.splashActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by mpolo on 08/09/2017.
 */

public class AlarmGetAllMedicineService extends Service {
    private static final String TAG = "[AlarmGetAllMedSer]";
    private String Email;
    TimerTask timerTask;
    Calendar calendar;
    AlarmManager alarm_manager ;
    PendingIntent pending_intent;
    Context context;
    public AlarmGetAllMedicineService(){
        calendar = Calendar.getInstance();
    }

    @Override
    public void onCreate() {
        context=this;
        String Method="[onCreate]";
        super.onCreate();
        Log.i(TAG, Method + "Init...");
        if (alarm_manager != null){
            Log.i(TAG, Method + "alarm_manager != null");
            if(pending_intent != null){
                Log.i(TAG, Method + "pending_intent != null");
                alarm_manager.cancel(pending_intent);
            }
        }

        getAllMedicineAlarmByDate();
        Log.i(TAG, Method + "End...");
    }

    @Override
    public void onDestroy(){
        String Method="[onDestroy]";
        Log.i(TAG, Method + "Init...");
        try{
            alarm_manager.cancel(pending_intent);
        }catch (Exception e){e.printStackTrace();}
        Log.i(TAG, Method + "End...");

    }

    private void getAllMedicineAlarmByDate(){
        String Method="[getAllMedicineAlarmByDate]";
        Log.i(TAG, Method + "Init...");
        Timer timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                getMedicineAlarm(new Date());
            }
        };
        long period =   60000 * 5 ;//* 24  ;
        timer.scheduleAtFixedRate(timerTask, 0,period  );//60000 = 1 mint

        Log.i(TAG, Method + "End...");
    }

    private void getMedicineAlarm(Date today) {
        String Method="[getMedicineAlarm]";
        Log.i(TAG, Method + "Init...");
        selectMedicineAlarmDB(today);
        Log.i(TAG, Method + "End...");

    }

    private void selectMedicineAlarmDB(Date today) {
        String Method="[selectMedicineAlarmDB]";
        Log.i(TAG, Method + "Init...");
        List<EAlarmDetails> lst;
        try {

            String todayInString = new SimpleDateFormat("yyyy-MM-dd").format(today) ;
            Log.i(TAG, Method + "Init getting List of Alarms to Set With Date values [todayInString=" +todayInString +"][today=" + today + "]" );
            lst = Utils.getEAlarmDetailsFromDataBaseLocal(HealthMonitorApplicattion.getApplication().getEAlarmDetailsDao(),0,0,todayInString,todayInString,"","");
            Log.i(TAG, Method + "End getting List of Alarms to Set With Date =" + today );
            if (lst != null){
                Log.i(TAG,Method + "Registers Obtained = " + lst.size() );
                if (lst.size() > 0 ){
//                    for (EAlarmDetails alarmDetail : lst) {
//                        Log.i(TAG,Method + "EAlarmDetails = " + alarmDetail.toString() );
//                    }
                    setMedicineAlarm(lst);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        Log.i(TAG, Method + "End...");
    }

    private void setMedicineAlarm(List<EAlarmDetails> lst) {
        String Method="[setMedicineAlarm]";
        Log.i(TAG,Method + "Init...");

        Date now = new Date();
        Date alarmDate = new Date();
        for (EAlarmDetails alarmDetails : lst) {
            Log.i(TAG,Method + "EAlarmDetails = " + alarmDetails.toString() );
            try {
                alarmDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse( new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + " " + alarmDetails.getAlarmDetailHour() ) ;
                //Log.i(TAG,Method + "Init Setting Alarm to = " + alarmDetail.getAlarmDetailHour() );
                if ( alarmDate.after(now) ){
                    Log.i(TAG,Method + "Init Setting Alarm to = " + alarmDetails.getAlarmDetailHour() );
                    Log.i(TAG,Method + "Init Setting Alarm to alarmDate = " + alarmDate + "*********************************************");
                    calendar = Calendar.getInstance();
                    //HH:mm:ss
                    calendar.set(Calendar.HOUR_OF_DAY , Integer.parseInt(alarmDetails.getAlarmDetailHour().substring(0,2)) );
                    calendar.set(Calendar.MINUTE , Integer.parseInt(alarmDetails.getAlarmDetailHour().substring(3,5)) );
                    calendar.set(Calendar.SECOND , Integer.parseInt(alarmDetails.getAlarmDetailHour().substring(6,8)) );
                    Log.i(TAG,Method + "calendar = " + calendar.getTime() );
                    alarm_manager = (AlarmManager) getSystemService(ALARM_SERVICE);
                    Intent my_intent = new Intent( context ,AlarmReceiver.class );
                    Bundle bundle = new Bundle();
                    //bundle.putSerializable("alarmDetails",alarmDetails);
                    bundle.putString("objAlarmString",alarmDetails.toString());
//                    bundle.putString("alarmDetailId",alarmDetails.getAlarmDetailId()+"");
//                    bundle.putString("registeredMedicinesId",alarmDetails.getRegisteredMedicinesId()+"" );
//                    bundle.putString("alarmDetailHour",alarmDetails.getAlarmDetailHour());
//                    bundle.putString("alarmDetailStatus",alarmDetails.getAlarmDetailStatus() );
//                    bundle.putString("alarmDetailCreateDate",alarmDetails.getAlarmDetailCreateDate());
//                    bundle.putString("medicineCode",alarmDetails.getMedicineCode()+"");
//                    bundle.putString("medicineDescription",alarmDetails.getMedicineDescription());
//                    bundle.putString("email",alarmDetails.getEmail());
//                    bundle.putString("idUsuario",alarmDetails.getIdUsuario() );
//                    bundle.putString("sentWs",alarmDetails.getSentWs() );
//                    bundle.putString("operationDb",alarmDetails.getOperationDb()  );
//                    bundle.putString("idServerDb",alarmDetails.toString() +"");

                    my_intent.putExtras(bundle);
                    final int _id = alarmDetails.getAlarmDetailId(); //int) System.currentTimeMillis();
                    pending_intent = PendingIntent.getBroadcast(context,_id,my_intent,PendingIntent.FLAG_UPDATE_CURRENT);
                    try{
                        Log.i(TAG,Method + "Tryig to cancel existing alarm with id = " + _id );
                        alarm_manager.cancel(pending_intent);
                        Log.i(TAG,Method + "Success cancenl existing alarm with id = " + _id );
                    }catch (Exception e){e.printStackTrace();}

                    if (Build.VERSION.SDK_INT >= 23 ) {
                        Log.i(TAG, Method + "Setting alarm using alarm_manager.setExactAndAllowWhileIdle()");
                        alarm_manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis() ,pending_intent);
                    }else if (Build.VERSION.SDK_INT >= 19 ) {
                        Log.i(TAG, Method + "Setting alarm using alarm_manager.setExact()");
                        alarm_manager.setExact(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis() ,pending_intent);
                        }
                        else{Log.i(TAG, Method + "Setting alarm using alarm_manager.set()");
                            alarm_manager.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis() ,pending_intent);
                        }

//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                        Log.i(TAG, Method + "Setting alarm using alarm_manager.setExact()");
//                        alarm_manager.setExact(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis() ,pending_intent);
//                    }else{Log.i(TAG, Method + "Setting alarm using alarm_manager.set()");
//                        alarm_manager.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis() ,pending_intent);}

                    Log.i(TAG,Method + "End Setting Alarm to alarmDate = " + alarmDate + "*********************************************");
                }
            }catch (Exception e){e.printStackTrace();}
        }
        Log.i(TAG,Method + "End...");
    }

    public void dataPreference(){
        //OBTENER DATA DE PREFERENCIA
        if (Utils.getEmailFromPreference(context) != null)
            Email = Utils.getEmailFromPreference(context);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String Method="[onStartCommand]";
        Log.i(TAG, Method + "Init ...");
        Log.i(TAG, Method + "End...");
        return START_STICKY;
    }



}
