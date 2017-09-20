package com.grupocisc.healthmonitor.Medicines.activities;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.grupocisc.healthmonitor.Alarm.adapters.AlarmListTakeMedicineCardAdapter;
import com.grupocisc.healthmonitor.R;
import com.grupocisc.healthmonitor.Utils.Utils;
import com.grupocisc.healthmonitor.entities.EAlarmDetails;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mpolo on 08/10/2017.
 */

public class MedicineTakeActivity extends AppCompatActivity implements AlarmListTakeMedicineCardAdapter.ViewHolder.ClickListener {
    private static final String TAG = "[MedicineTakeActivity]";
    private EAlarmDetails alarmDetails;
    private LinearLayout llLinearLayout;
    private RecyclerView rvRecyclerView;
    private String user_email;
    private List<EAlarmDetails> lstAlarmDetails = new ArrayList<>();
    private AlarmListTakeMedicineCardAdapter adapter;
    static boolean isActive = false;
    private boolean updateList = false;
    String objAlarm="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String Method ="[onCreate]";
        Log.i(TAG, Method + "Init..."  );
        super.onCreate(savedInstanceState);
        setContentView(R.layout.medicine_take_activity);
        user_email = Utils.getEmailFromPreference(this);
        rvRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        if(savedInstanceState != null){
            //alarmDetails = (EAlarmDetails) savedInstanceState.getSerializable("alarmDetails");
            objAlarm =   savedInstanceState.getString("objAlarmString");
            String strAlarm [] = objAlarm.split(",");
            alarmDetails = new EAlarmDetails();
            alarmDetails.setAlarmDetailId( Integer.parseInt(strAlarm[0].split("=")[1]) );
            alarmDetails.setRegisteredMedicinesId( Integer.parseInt(strAlarm[1].split("=")[1]) );
            alarmDetails.setAlarmDetailHour( strAlarm[2].split("=")[1] );
            alarmDetails.setAlarmDetailStatus(  strAlarm[3].split("=")[1] );
            alarmDetails.setAlarmDetailCreateDate( strAlarm[4].split("=")[1] );
            alarmDetails.setMedicineCode( Integer.parseInt(strAlarm[5].split("=")[1]) );
            alarmDetails.setMedicineDescription( strAlarm[6].split("=")[1] );
            alarmDetails.setEmail(  strAlarm[7].split("=")[1]  );
            alarmDetails.setIdUsuario  ( strAlarm[8].split("=")[1] );
            alarmDetails.setSentWs(  strAlarm[9].split("=")[1] );
            alarmDetails.setOperationDb  ( strAlarm[10].split("=")[1] );
            alarmDetails.setIdServerDb( Integer.parseInt(strAlarm[11].split("=")[1]) );
            Log.i(TAG, Method + "savedInstanceState EAlarmDetails = " + alarmDetails.toString()  );
        }else{
            if (getIntent() != null && getIntent().getExtras() != null && getIntent().getExtras().getString ("objAlarmString") != null) {
                //if (getIntent() != null && getIntent().getExtras() != null && getIntent().getExtras().getSerializable("alarmDetails") != null) {
                //alarmDetails = (EAlarmDetails) getIntent().getExtras().getSerializable("alarmDetails");
                objAlarm =   getIntent().getExtras().getString("objAlarmString");
                String strAlarm [] = objAlarm.split(",");
                alarmDetails = new EAlarmDetails();
                alarmDetails.setAlarmDetailId( Integer.parseInt(strAlarm[0].split("=")[1].replace("[","")) );
                alarmDetails.setRegisteredMedicinesId( Integer.parseInt(strAlarm[1].split("=")[1]) );
                alarmDetails.setAlarmDetailHour( strAlarm[2].split("=")[1] );
                alarmDetails.setAlarmDetailStatus(  strAlarm[3].split("=")[1] );
                alarmDetails.setAlarmDetailCreateDate( strAlarm[4].split("=")[1] );
                alarmDetails.setMedicineCode( Integer.parseInt(strAlarm[5].split("=")[1]) );
                alarmDetails.setMedicineDescription( strAlarm[6].split("=")[1] );
                alarmDetails.setEmail(  strAlarm[7].split("=")[1]  );
                alarmDetails.setIdUsuario  ( strAlarm[8].split("=")[1] );
                alarmDetails.setSentWs(  strAlarm[9].split("=")[1] );
                alarmDetails.setOperationDb  ( strAlarm[10].split("=")[1] );
                alarmDetails.setIdServerDb( Integer.parseInt(strAlarm[11].split("=")[1].replace("]","")) );

                Log.i(TAG, Method + "getIntent() EAlarmDetails = " + alarmDetails.toString()  );
            }
        }
        if (alarmDetails != null){
            Log.i(TAG, Method + "alarmDetails != null");
            addAlarmList(alarmDetails);
        }
        Log.i(TAG, Method + "End..."  );
    }

    @Override
    public void onResume() {
        String Method ="[onResume]";
        Log.i(TAG, Method +  "Init..."  );
        super.onResume();
        if (updateList){
            callSetAdapter();
        }
        Log.i(TAG, Method +  "End..."  );
    }

    private void addAlarmList(EAlarmDetails alarmDetails){
        String Method ="[addAlarmList]";
        Log.i(TAG, Method + "Init..."  );

        if (getLstAlarmDetails() == null){
            setLstAlarmDetails(new ArrayList<EAlarmDetails>());
        }
        getLstAlarmDetails().add(alarmDetails);
        callSetAdapter();
        Log.i(TAG, Method + "End..."  );
    }

    public void callSetAdapter(){
        String Method ="[callSetAdapter]";
        Log.i(TAG, Method + "Init..."  );

        if (adapter != null){
            adapter.updateData(getLstAlarmDetails());
        }else{
            adapter = new AlarmListTakeMedicineCardAdapter(this, getLstAlarmDetails(),this,rvRecyclerView,true);
            rvRecyclerView.setAdapter(adapter);
            rvRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        }
        Log.i(TAG, Method + "End..."  );
    }

    @Override
    public void onItemClicked(View v, int position) {

    }

    @Override
    public boolean onItemLongClicked(View v, int position) {
        return false;
    }


    public void nextAction() {
        String Method ="[nextAction]";
        Log.i(TAG, Method + "Init..."  );
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //finalizar matar la actividad
                try {
                    finish();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        }, 150);
        Log.i(TAG, Method + "End..."  );
    }

    public boolean isUpdateList() {
        return updateList;
    }

    public void setUpdateList(boolean updateList) {
        this.updateList = updateList;
    }


    public List<EAlarmDetails> getLstAlarmDetails() {
        return lstAlarmDetails;
    }

    public void setLstAlarmDetails(List<EAlarmDetails> lstAlarmDetails) {
        this.lstAlarmDetails = lstAlarmDetails;
    }
}
