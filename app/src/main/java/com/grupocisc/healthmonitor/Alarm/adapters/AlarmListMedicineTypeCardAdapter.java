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
import com.grupocisc.healthmonitor.entities.EMedicineType;

import java.util.List;

/**
 * Created by mpolo on 07/30/2017.
 */

public class AlarmListMedicineTypeCardAdapter extends RecyclerView.Adapter<AlarmListMedicineTypeCardAdapter.ViewHolder>{
    private static final String TAG = "[AlarmListMTCAdapter]";
    private RecyclerViewAnimator mAnimator;
    private AlarmListMedicineTypeCardAdapter.ViewHolder.ClickListener clickListener;
    private boolean  isAnimRebound;
    public Context context;
    private LayoutInflater mLayoutInflater;
    //private List<String> lstAlarmList;
    private List<EMedicineType> lstEMedicineType ;


    public AlarmListMedicineTypeCardAdapter(Context c , List<EMedicineType> lstEMedicineType ,ViewHolder.ClickListener clickListener
                                           ,RecyclerView recyclerView , boolean isAnimRebound){
        this.context = c ;
        this.lstEMedicineType = lstEMedicineType;
        this.clickListener = clickListener;
        this.mAnimator = new RecyclerViewAnimator(recyclerView);
        this.isAnimRebound = isAnimRebound;

        mLayoutInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public void onBindViewHolder(ViewHolder myViewHolder, final int position) {
        String Method ="[onBindViewHolder]";
        Log.i(TAG, Method +  "Init..."  );

        myViewHolder.txt_medicine_type_code  .setText(String.valueOf( this.lstEMedicineType.get(position).getMedicineTypeCode()  ));
        myViewHolder.txt_medicine_type_desc.setText(this.lstEMedicineType.get(position).getMedicineTypeDescription() );

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
        iSize = this.lstEMedicineType.size();
        Log.i(TAG, Method +  "iSize = " + iSize  );
        Log.i(TAG, Method +  "End..."  );
        return  iSize;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        String Method ="[onCreateViewHolder]";
        Log.i(TAG, Method +  "Init..."  );
        View v = mLayoutInflater.inflate(R.layout.alarm_medicine_type_card_adapter, viewGroup, false);
        AlarmListMedicineTypeCardAdapter.ViewHolder vh = new AlarmListMedicineTypeCardAdapter.ViewHolder(v, clickListener);
        setAnimReboundonCreateViewHolder(v);
        Log.i(TAG, Method +  "End..."  );
        return vh;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        private CardView main_card;
        private TextView txt_medicine_type_code;
        private TextView txt_medicine_type_desc;
        private ImageView img_avatar;
        private ClickListener listener;


        public ViewHolder(View v , ClickListener listener) {
            super(v);
            String Method ="[ViewHolder]";
            Log.i(TAG, Method + "Init..." );
            main_card = (CardView) v.findViewById(R.id.main_card);
            txt_medicine_type_code = (TextView) v.findViewById(R.id.txt_medicine_type_code);
            txt_medicine_type_desc = (TextView) v.findViewById(R.id.txt_medicine_type_desc);

            img_avatar = (ImageView) v.findViewById(R.id.img_avatar);
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

    public void updateData(List<EMedicineType> lstEMedicineType) {
        String Method ="[updateData]";
        Log.i(TAG, Method +  "Init..."  );
        this.lstEMedicineType.clear();
        this.lstEMedicineType.addAll(lstEMedicineType);
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
