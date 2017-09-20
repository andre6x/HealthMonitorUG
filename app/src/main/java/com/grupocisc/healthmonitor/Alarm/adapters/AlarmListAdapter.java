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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.SimpleFormatter;

/**
 * Created by developer on 21/07/2017.
 */

public class AlarmListAdapter extends RecyclerView.Adapter<AlarmListAdapter.ViewHolder>{
    private static final String TAG = "[AlarmListAdapter]";
    private ViewHolder.ClickListener clickListener;
    private RecyclerViewAnimator mAnimator;
    private boolean  isAnimRebound;
    //public InsulinListFragment context;
    public Context context;
    private List<EAlarmDetails> lstEAlarmDetails;
    private LayoutInflater mLayoutInflater;




    public AlarmListAdapter(Context c , List<EAlarmDetails> lstEAlarmDetails , ViewHolder.ClickListener clickListener,
                            RecyclerView recyclerView , boolean isAnimRebound , String date , String hourI , String hourF ){
        this.context = c ;
        this.lstEAlarmDetails = lstEAlarmDetails;
        this.clickListener = clickListener;
        this.mAnimator = new RecyclerViewAnimator(recyclerView);
        this.isAnimRebound = isAnimRebound;
        mLayoutInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


    }

    @Override
    public void onBindViewHolder(ViewHolder myViewHolder, final int position) {
        String Method ="[onBindViewHolder]";
        Log.i(TAG, Method +  "Init..."  );
        String date_ddMMyyyyHHmm="01/01/1900 00:00";

        myViewHolder.txt_medicine_desc .setText( this.lstEAlarmDetails.get(position).getMedicineDescription()+"");
        myViewHolder.txt_alarm_hour .setText( this.lstEAlarmDetails.get(position).getAlarmDetailHour() +"");

        if (this.lstEAlarmDetails.get(position).getSentWs().equals("N"))
            myViewHolder.img_cloud.setImageResource(R.mipmap.cloud_off);
        else
            myViewHolder.img_cloud.setImageResource(R.mipmap.cloud_on);

        /*set Animation Rebound*/
        setAnimReboundonBindViewHolder(myViewHolder.itemView, position);
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
        iSize = this.lstEAlarmDetails.size();
        Log.i(TAG, Method +  "iSize = " + iSize  );
        Log.i(TAG, Method +  "End..."  );
        return  iSize;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        String Method ="[onCreateViewHolder]";
        Log.i(TAG, Method +  "Init..."  );
        //LayoutInflater li = LayoutInflater.from(viewGroup.getContext());
        //View v = li.inflate(R.layout.insulin_card_adapter, viewGroup, false);
        //return new ViewHolder(v);
        View v = mLayoutInflater.inflate(R.layout.alarm_card_adapter, viewGroup, false);
        AlarmListAdapter.ViewHolder vh = new AlarmListAdapter.ViewHolder(v, clickListener);
        setAnimReboundonCreateViewHolder(v);
        Log.i(TAG, Method +  "End..."  );
        return vh;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private CardView main_card;
        private TextView txt_medicine_desc;
        private TextView txt_alarm_hour;
        private ClickListener listener;
        private ImageView img_avatar;
        private ImageView img_cloud;

        public ViewHolder(View v , ClickListener listener) {
            super(v);
            String Method ="[ViewHolder]";
            Log.i(TAG, Method + "Init..." );
            main_card = (CardView) v.findViewById(R.id.main_card);
            txt_medicine_desc = (TextView) v.findViewById(R.id.txt_medicine_desc);
            txt_alarm_hour = (TextView) v.findViewById(R.id.txt_alarm_hour);

            img_avatar = (ImageView) v.findViewById(R.id.img_avatar);
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

        public interface ClickListener {
            void onItemClicked(View v, int position);
            boolean onItemLongClicked(View v, int position);
        }
    }

    public void updateData(List<EAlarmDetails> lstEAlarmDetails) {
        String Method ="[updateData]";
        Log.i(TAG, Method +  "Init..."  );
        this.lstEAlarmDetails.clear();
        this.lstEAlarmDetails.addAll( lstEAlarmDetails );
        notifyDataSetChanged();
        Log.i(TAG, Method +  "Init..."  );

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


}
