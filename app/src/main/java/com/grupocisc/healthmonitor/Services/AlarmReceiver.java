package com.grupocisc.healthmonitor.Services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

//import com.grupocisc.healthmonitor.entities.EAlarmDetails;

//import com.grupocisc.healthmonitor.Alarm.adapters.AlarmListTakeMedicineCardAdapter;

/**
 * Created by mpolo on 08/09/2017.
 */

public class AlarmReceiver extends BroadcastReceiver {
    private static final String TAG = "[AlarmReceiver]";

    @Override
    public void onReceive(Context context, Intent intent) {
        String Method="[onReceive]";
        //EAlarmDetails alarmDetails = null ;//= new EAlarmDetails();
        String objAlarm="";
        String alarmDetailId="";            //      ,alarmDetails.getAlarmDetailId()+"");
        String registeredMedicinesId="";    //",alarmDetails.getRegisteredMedicinesId()+"" );
        String alarmDetailHour="";          //",alarmDetails.getAlarmDetailHour());
        String alarmDetailStatus="";        //",alarmDetails.getAlarmDetailStatus() );
        String alarmDetailCreateDate="";    //",alarmDetails.getAlarmDetailCreateDate());
        String medicineCode="";             //",alarmDetails.getMedicineCode()+"");
        String medicineDescription="";      //",alarmDetails.getMedicineDescription());
        String email="";                    //",alarmDetails.getEmail());
        String idUsuario="";                //",alarmDetails.getIdUsuario() );
        String sentWs="";                   //",alarmDetails.getSentWs() );
        String operationDb="";              //",alarmDetails.getOperationDb()  );
        String idServerDb="";               //",alarmDetails.toString() +"");

        Log.i(TAG,Method + "Init...");
        try {
            Log.i(TAG,Method + "Trying getExtras...");
            //alarmDetails = (EAlarmDetails) intent.getExtras().getSerializable("alarmDetails");
            objAlarm = intent.getExtras().getString("objAlarmString");
//            alarmDetailId=          intent.getExtras().getString("alarmDetailId");
//            registeredMedicinesId=  intent.getExtras().getString("registeredMedicinesId");
//            alarmDetailHour=        intent.getExtras().getString("alarmDetailHour");
//            alarmDetailStatus=      intent.getExtras().getString("alarmDetailStatus");
//            alarmDetailCreateDate=  intent.getExtras().getString("alarmDetailCreateDate");
//            medicineCode=           intent.getExtras().getString("medicineCode");
//            medicineDescription=    intent.getExtras().getString("medicineDescription");
//            email=                  intent.getExtras().getString("email");
//            idUsuario=              intent.getExtras().getString("idUsuario");
//            sentWs=                 intent.getExtras().getString("sentWs");
//            operationDb=            intent.getExtras().getString("operationDb");
//            idServerDb=             intent.getExtras().getString("idServerDb");

            Log.i(TAG,Method + "Execution Alarm from intent.getExtras().getString = " + objAlarm );
            //Log.i(TAG,Method + "Execution Alarm from intent.getSerializableExtra = " + ((EAlarmDetails) intent.getSerializableExtra("alarmDetails")).toString() );
            //}
        }catch (Exception e){e.printStackTrace();}
        //Log.i(TAG,Method + "Execution Alarm from objAlarm = " + objAlarm );
        if (objAlarm != null) {
            Log.i(TAG,Method + "Execution Alarm OK = " +  objAlarm );
            Intent service_intent = new Intent(context, AlarmService.class);
            Bundle bundle = new Bundle();
            bundle.putString("objAlarmString",objAlarm);
            service_intent.putExtras(bundle);
            context.startService(service_intent);
        }

        Log.i(TAG,Method + "End...");
    }

}
