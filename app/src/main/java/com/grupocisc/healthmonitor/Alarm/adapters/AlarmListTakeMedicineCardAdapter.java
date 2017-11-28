package com.grupocisc.healthmonitor.Alarm.adapters;

import android.animation.Animator;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.grupocisc.healthmonitor.HealthMonitorApplicattion;
import com.grupocisc.healthmonitor.Medicines.activities.MedicineTakeActivity;
import com.grupocisc.healthmonitor.R;
import com.grupocisc.healthmonitor.Services.AlarmReceiver;
import com.grupocisc.healthmonitor.Utils.RecyclerViewAnimator;
import com.grupocisc.healthmonitor.Utils.Utils;
import com.grupocisc.healthmonitor.entities.EAlarmDetails;
import com.grupocisc.healthmonitor.entities.EAlarmTakeMedicine;

import java.sql.SQLException;
import java.util.List;

import static android.content.Context.ALARM_SERVICE;
import static com.grupocisc.healthmonitor.R.id.layoutContent;
import static com.grupocisc.healthmonitor.R.id.layoutMain;

/**
 * Created by mpolo on 08/08/2017.
 */

public class AlarmListTakeMedicineCardAdapter extends RecyclerView.Adapter <AlarmListTakeMedicineCardAdapter.ViewHolder> {
    private static final String TAG = "[AlarmListRTCAdapter]";
    private RecyclerViewAnimator mAnimator;
    private boolean  isAnimRebound;
    public Context context;
    private ViewHolder.ClickListener clickListener;
    private LayoutInflater mLayoutInflater;
    private List<EAlarmDetails> lstAlarmList;

    public AlarmListTakeMedicineCardAdapter(Context c , List<EAlarmDetails> lstAlarmList
                                             , ViewHolder.ClickListener clickListener
            , RecyclerView recyclerView ,boolean isAnimRebound ){
        this.context = c ;
        this.lstAlarmList = lstAlarmList;
        this.clickListener = clickListener;
        this.mAnimator = new RecyclerViewAnimator(recyclerView);
        this.isAnimRebound = isAnimRebound;
        mLayoutInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }



    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {
        String Method ="[onBindViewHolder]";
        Log.i(TAG, Method +  "Init..."  );

        viewHolder.txt_MedicineName.setText(this.lstAlarmList.get(i).getMedicineDescription());
        viewHolder.txt_Hour.setText(this.lstAlarmList.get(i).getAlarmDetailHour());
        viewHolder.setAlarmDetails(this.lstAlarmList.get(i));

        viewHolder.btnTake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callSaveTakeMedicine(viewHolder.getAlarmDetails()) ){
                    cancelAlarm(viewHolder.getAlarmDetails());
                    lstAlarmList.remove(viewHolder.getAlarmDetails());
                    updateData(lstAlarmList);
                    if (lstAlarmList.isEmpty() ){
                        Log.i(TAG, "btnTake.setOnClickListener:" + "Lista vacía.");
                        nextAction();
                    }
                }
            }
        });

        viewHolder.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lstAlarmList.remove(viewHolder.getAlarmDetails());
                cancelAlarm(viewHolder.getAlarmDetails());
                lstAlarmList.remove(viewHolder.getAlarmDetails());
                updateData(lstAlarmList);
                if (lstAlarmList.isEmpty() ){
                    Log.i(TAG, "btnCancel.setOnClickListener:" + "Cancelar.");
                    nextAction();
                }
            }
        });

        Log.i(TAG, Method +  "End..."  );
    }

    private boolean callSaveTakeMedicine(EAlarmDetails alarmDetails){
        String Method ="[callSaveTakeMedicine]";
        boolean callSaveTakeMedicine = false;
        Log.i(TAG, Method + "Init..."  );
        try {
            Log.i(TAG, Method + "Saving Take " + alarmDetails.toString()  );
            if (Utils.saveAlarmTakeMedicineToDataBaseLocal(HealthMonitorApplicattion.getApplication().getEAlarmTakeMedicineDao()
                    ,alarmDetails.getRegisteredMedicinesId(), alarmDetails.getAlarmDetailId(),Utils.getDate("yyyy/MM/dd HH:mm:ss")
                    ,alarmDetails.getEmail() ,"I","N" ) > 0){

                callSaveTakeMedicine=true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Log.i(TAG, Method + "End..."  );
        return callSaveTakeMedicine;
    }

    private void cancelAlarm(EAlarmDetails alarmDetails ){
        String Method ="[cancelAlarm]";
        Log.i(TAG, Method + "Init...");
        try {
            AlarmManager alarm_manager ;
            alarm_manager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
            PendingIntent pending_intent;
            Intent my_intent = new Intent();
            final int _id = alarmDetails.getAlarmDetailId(); //int) System.currentTimeMillis();
            pending_intent = PendingIntent.getBroadcast(context,_id,my_intent,PendingIntent.FLAG_UPDATE_CURRENT);
            alarm_manager.cancel(pending_intent);
        }catch (Exception e){e.printStackTrace();}
        Log.i(TAG, Method + "End...");
    }

    @Override
    public void onViewDetachedFromWindow(ViewHolder viewHolder) {
        String Method ="[onViewDetachedFromWindow]";
        Log.i(TAG, Method +  "Init..."  );
        super.onViewDetachedFromWindow(viewHolder);
        viewHolder.itemView.clearAnimation();
        Log.i(TAG, Method +  "End..."  );
    }

    @Override
    public void onViewAttachedToWindow(ViewHolder viewHolder) {
        String Method ="[onViewAttachedToWindow]";
        Log.i(TAG, Method +  "Init..."  );
        super.onViewAttachedToWindow(viewHolder);
        Log.i(TAG, Method +  "End..."  );
    }

    @Override
    public int getItemCount() {
        String Method ="[getItemCount]";
        Log.i(TAG, Method +  "Init..."  );
        int iSize=0;
        iSize = this.lstAlarmList.size();
        Log.i(TAG, Method +  "iSize = " + iSize  );
        Log.i(TAG, Method +  "End..."  );
        return  iSize;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        String Method ="[onCreateViewHolder]";
        Log.i(TAG, Method +  "Init..."  );
        View v = mLayoutInflater.inflate(R.layout.alarm_take_medicine_card_adapter, viewGroup, false);
        ViewHolder vh = new ViewHolder(v,clickListener);
        setAnimReboundonCreateViewHolder(v);
        Log.i(TAG, Method +  "End..."  );
        return vh;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private CardView main_card;
        private TextView txt_MedicineName;
        private TextView txt_Hour;
        private Button btnTake;
        private Button btnCancel;
        private EAlarmDetails alarmDetails;

        private ClickListener listener;
        private ImageView img_avatar;
        //private ImageView img_cloud;

        public ViewHolder(View v , ClickListener listener) {
            super(v);
            String Method ="[ViewHolder]";
            Log.i(TAG, Method + "Init..." );
            main_card = (CardView) v.findViewById(R.id.main_card);
            txt_MedicineName = (TextView) v.findViewById(R.id.txt_MedicineName);
            txt_Hour = (TextView) v.findViewById(R.id.txt_Hour);
            btnTake = (Button) v.findViewById(R.id.btnTake);
            btnCancel = (Button) v.findViewById(R.id.btnCancel);
            //img_cloud = (ImageView) v.findViewById(R.id.img_cloud);
            img_avatar = (ImageView) v.findViewById(R.id.img_avatar);
            this.listener = listener;
            v.setOnClickListener(this);
            v.setOnLongClickListener(this);
            Log.i(TAG, Method + "End..." );
        }

        public void setAlarmDetails(EAlarmDetails alarmDetails){
            this.alarmDetails = alarmDetails;
        }

        public EAlarmDetails getAlarmDetails(){
            return  this.alarmDetails ;
        }

        @Override
        public void onClick(View v) {
            if (listener != null) {
                listener.onItemClicked(v,getLayoutPosition());
            }
        }
        @Override
        public boolean onLongClick(View v) {
            if (listener != null) {
                return listener.onItemLongClicked(v,getLayoutPosition());
            }
            return false;
        }

        public interface ClickListener {
            void onItemClicked(View v, int position);
            boolean onItemLongClicked(View v, int position);
        }
    }

    //inicio animation rebound isAnimRebound=crear se anima , actulizar= no se anima
    public void setAnimReboundonBindViewHolder(View itemView ,int position){
        /** Item's entrance animations during scroll are performed here.*/
        if(isAnimRebound)
            mAnimator.onBindViewHolder(itemView, position);
    }

    public void setAnimReboundonCreateViewHolder(View v){
        /**First item's entrance animations. */
        if(isAnimRebound)
            mAnimator.onCreateViewHolder(v);
    }

    public void updateData(List<EAlarmDetails> lst) {
        String Method ="[updateData]";
        Log.i(TAG, Method + "Init..." );
        this.lstAlarmList.clear();
        this.lstAlarmList.addAll(lst);
        notifyDataSetChanged();
        Log.i(TAG, Method + "End.." );
    }

    private void nextAction() {
        String Method ="[nextAction]";
        Log.i(TAG, Method + "Init..."  );
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //finalizar matar la actividad
                try {
                    ((MedicineTakeActivity) context).nextAction();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        }, 150);
        Log.i(TAG, Method + "End..."  );
    }




}
