package com.grupocisc.healthmonitor.Alarm.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.grupocisc.healthmonitor.R;
import com.grupocisc.healthmonitor.Utils.RecyclerViewAnimator;
import com.grupocisc.healthmonitor.entities.EAlarmDetails;

import java.util.List;

/**
 * Created by mpolo on 07/26/2017.
 */

public class AlarmListReminderTimesCardAdapter extends RecyclerView.Adapter <AlarmListReminderTimesCardAdapter.ViewHolder>  {
    private static final String TAG = "[AlarmListRTCAdapter]";
    private RecyclerViewAnimator mAnimator;
    private boolean  isAnimRebound;
    public Context context;
    private LayoutInflater mLayoutInflater;
    private AlarmListReminderTimesCardAdapter.ViewHolder.ClickListener clickListener;
    //private List<String> lstAlarmList;
    private List<EAlarmDetails> lstAlarmDetails;

//    public AlarmListReminderTimesCardAdapter(Context c , List<String> lstAlarmList
//            //,ViewHolder.ClickListener clickListener
//            ,RecyclerView recyclerView ){
//        this.context = c ;
//        this.lstAlarmList = lstAlarmList;
//        //this.clickListener = clickListener;
//        this.mAnimator = new RecyclerViewAnimator(recyclerView);
//        mLayoutInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//
//    }

    public AlarmListReminderTimesCardAdapter(Context c , List<EAlarmDetails> lstAlarmDetails
                                             //,ViewHolder.ClickListener clickListener
            ,RecyclerView recyclerView ){
        this.context = c ;
        this.lstAlarmDetails = lstAlarmDetails;
        //this.clickListener = clickListener;
        this.mAnimator = new RecyclerViewAnimator(recyclerView);
        mLayoutInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

//    public String getAlarmHour(int position){
//        return lstAlarmList.get(position).toString()  ;
//    }
     public String getAlarmHour(int position){

         return lstAlarmDetails.get(position).getAlarmDetailHour()  ;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        String Method ="[onBindViewHolder]";
        Log.i(TAG, Method +  "Init..."  );

        //viewHolder.txt_hora.setText(this.lstAlarmList.get(i).toString());
        viewHolder.txt_hora.setText(this.lstAlarmDetails.get(i).getAlarmDetailHour());
        viewHolder.setAlarmDetails(this.lstAlarmDetails.get(i));
        Log.i(TAG, Method +  "End..."  );
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
        //iSize = this.lstAlarmList.size();
        iSize = this.lstAlarmDetails.size();
        Log.i(TAG, Method +  "iSize = " + iSize  );
        Log.i(TAG, Method +  "End..."  );
        return  iSize;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        String Method ="[onCreateViewHolder]";
        Log.i(TAG, Method +  "Init..."  );
        View v = mLayoutInflater.inflate(R.layout.alarm_reminder_times_card_adapter, viewGroup, false);
        AlarmListReminderTimesCardAdapter.ViewHolder vh = new AlarmListReminderTimesCardAdapter.ViewHolder(v,clickListener);
        setAnimReboundonCreateViewHolder(v);
        Log.i(TAG, Method +  "End..."  );
        return vh;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        private CardView main_card;
        private TextView txt_hora;
        private EAlarmDetails alarmDetails;
        private ClickListener listener;
        private ImageView img_cloud;

        public ViewHolder(View v , ClickListener listener) {
            super(v);
            String Method ="[ViewHolder]";
            Log.i(TAG, Method + "Init..." );
            main_card = (CardView) v.findViewById(R.id.main_card);
            txt_hora = (TextView) v.findViewById(R.id.txt_hora);

            img_cloud = (ImageView) v.findViewById(R.id.img_cloud);
            this.listener = listener;
            v.setOnClickListener(this);
            v.setOnLongClickListener(this);
            Log.i(TAG, Method + "End..." );
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

        public EAlarmDetails getAlarmDetails() {
            return alarmDetails;
        }

        public void setAlarmDetails(EAlarmDetails alarmDetails) {
            this.alarmDetails = alarmDetails;
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

//    public void updateData(List<String> lst) {
//        String Method ="[updateData]";
//        Log.i(TAG, Method +  "Init..."  );
//        this.lstAlarmList.clear();
//        this.lstAlarmList.addAll(lst);
//        notifyDataSetChanged();
//        Log.i(TAG, Method +  "Init..."  );
//    }

    public void updateData(List<EAlarmDetails> lst) {
        String Method ="[updateData2]";
        Log.i(TAG, Method +  "Init..."  );
        this.lstAlarmDetails.clear();
        this.lstAlarmDetails.addAll(lst);
        notifyDataSetChanged();
        Log.i(TAG, Method +  "Init..."  );
    }


}
