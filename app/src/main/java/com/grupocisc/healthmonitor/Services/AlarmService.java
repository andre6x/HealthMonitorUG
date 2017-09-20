package com.grupocisc.healthmonitor.Services;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Vibrator;
import android.support.annotation.Nullable;
//import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.grupocisc.healthmonitor.Medicines.activities.MedicineTakeActivity;
import com.grupocisc.healthmonitor.R;
//import com.grupocisc.healthmonitor.entities.EAlarmDetails;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mpolo on 08/09/2017.
 */

public class AlarmService extends Service {
    private static final String TAG = "AlarmService";
    MediaPlayer mediaPlayer;
    Context context =this;
    String objAlarm="";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    //@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String Method="[onStartCommand]";
        Log.e(TAG, Method + "Init...");
        Log.i(TAG, Method + "Received startId=" + startId + ": " + intent );
        //EAlarmDetails alarmDetails = new EAlarmDetails();
        try {
            Log.i(TAG,Method + "Trying getExtras...");
            //alarmDetails = (EAlarmDetails) intent.getExtras().getSerializable("alarmDetails");
            objAlarm = intent.getExtras().getString("objAlarmString");
        }catch (Exception e){e.printStackTrace();}

        if (objAlarm != null){
            Log.i(TAG, Method + "Received Objects=" + objAlarm );
        }else {Log.i(TAG,Method + "getExtras is null");}

        mediaPlayer = MediaPlayer.create(context, R.raw.notificacion_campana);
        if (mediaPlayer != null)
            mediaPlayer.start();
        Vibrator v = (Vibrator) this.context.getSystemService(Context.VIBRATOR_SERVICE);
        if (v != null ){
            v.vibrate(500);
        }

//        if ( activityIsRunning() ){
////            ((MedicineTakeActivity) this.context).getLstAlarmDetails().add(alarmDetails);
////            ((MedicineTakeActivity) this.context).setUpdateList(true);
//        }

        Intent intent_take = new Intent(context,  MedicineTakeActivity.class );
        Bundle bundle = new Bundle();
        bundle.putString("objAlarmString",objAlarm);
        intent_take.putExtras( bundle ) ;
        intent_take.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent_take);

        Log.i(TAG, Method + "End...");
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy(){
        String Method="[onDestroy]";
        Log.e(TAG, Method + "Init...");
        Log.e(TAG, Method + "on Destroy called...");
        //Toast.makeText(this,"on Destroy called",Toast.LENGTH_SHORT).show();
        Log.e(TAG, Method + "End...");

    }

    private boolean activityIsRunning(){
        boolean activityIsRunning = false;
        ArrayList<String> runningActivities = new ArrayList<String>();
        ActivityManager activityManager = (ActivityManager) getBaseContext().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> services = activityManager.getRunningTasks(Integer.MAX_VALUE);
        for(int i=0 ;i< services.size();i++){
            System.out.println("activityIsRunning ----------->" + services.get(i).topActivity.toShortString());
            runningActivities.add(0,services.get(i).topActivity.toShortString());
        }
        if (runningActivities.contains("{com.grupocisc.healthmonitor/com.grupocisc.healthmonitor.Medicines.activities.MedicineTakeActivity}")){
            activityIsRunning=true;
        }

        return  activityIsRunning;
    }

}
