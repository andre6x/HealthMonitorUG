package com.grupocisc.healthmonitor.Medicines.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.grupocisc.healthmonitor.R;
import com.grupocisc.healthmonitor.Utils.RecyclerViewAnimator;
import com.grupocisc.healthmonitor.entities.EAlarmTakeMedicine;
import com.grupocisc.healthmonitor.entities.IRegisteredMedicines;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by Gema on 10/01/2017.
 * CONTROL DE MEDICAMENTOS LISTA
 */

public class MedicineTakeListAdapter extends RecyclerView.Adapter<MedicineTakeListAdapter.ViewHolder>  {
    private static final String TAG = "[MedTakeListAdapter]";

    private List<EAlarmTakeMedicine> lstAlarmTakeMedicine;
    private MedicineTakeListAdapter.ViewHolder.ClickListener clickListener;
    private RecyclerViewAnimator mAnimator;
    private boolean  isAnimRebound;
    private LayoutInflater mLayoutInflater;
    public Context context;

    public MedicineTakeListAdapter(Context c , List<EAlarmTakeMedicine> lstAlarmTakeMedicine , ViewHolder.ClickListener clickListener ,
                                   RecyclerView recyclerView , boolean isAnimRebound) {
        this.context = c ;
        this.lstAlarmTakeMedicine = lstAlarmTakeMedicine;

        this.clickListener = clickListener;
        this.mAnimator = new RecyclerViewAnimator(recyclerView);
        this.isAnimRebound = isAnimRebound;

        mLayoutInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public void onBindViewHolder(ViewHolder myViewHolder, final int position) {
        String Method ="[onBindViewHolder]";
        Log.i(TAG, Method + "Init..." );
        if (lstAlarmTakeMedicine.get(position) != null){
            Log.i(TAG, Method + "lstAlarmTakeMedicine.get("+position+")" + lstAlarmTakeMedicine.get(position).toString() );
        }

        Log.i(TAG, "onBindViewHolder: getAlarmTakeMedicineDateBASE  = " + this.lstAlarmTakeMedicine.get(position).getAlarmTakeMedicineDate());
        myViewHolder.setAlarmTakeMedicine(this.lstAlarmTakeMedicine.get(position));

        String fechaConsumo =   this.lstAlarmTakeMedicine.get(position).getAlarmTakeMedicineDate() .substring(8,10)  + "/" +
                this.lstAlarmTakeMedicine.get(position).getAlarmTakeMedicineDate().substring(5,7)  + "/" +
                this.lstAlarmTakeMedicine.get(position).getAlarmTakeMedicineDate().substring(0,4)  ;
        String horaConsumo = this.lstAlarmTakeMedicine.get(position).getAlarmTakeMedicineDate().substring(11,19)  ;
        String horaAlarma = this.lstAlarmTakeMedicine.get(position).getAlarmDetailHour()  ;

        Log.i(TAG, "onBindViewHolder: fechaConsumo = " + fechaConsumo);
        myViewHolder.txt_nombre.setText( this.lstAlarmTakeMedicine.get(position).getMedicineDescription() );
        myViewHolder.txt_fecha.setText( "Fecha Consumo: " + fechaConsumo );
        myViewHolder.txt_descripcion.setText( "Hora Alarma: " + horaAlarma + " Hora Cosumo: " + horaConsumo );

        if (this.lstAlarmTakeMedicine.get(position).getSentWs().equals("N"))
            myViewHolder.img_cloud.setImageResource(R.mipmap.cloud_off);
        else
            myViewHolder.img_cloud.setImageResource(R.mipmap.cloud_on);

        /*set Animation Rebound*/
        setAnimReboundonBindViewHolder(myViewHolder.itemView, position);
        Log.i(TAG, Method + "End..." );
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
        iSize = this.lstAlarmTakeMedicine.size();
        Log.i(TAG, Method +  "iSize = " + iSize  );
        Log.i(TAG, Method +  "End..."  );
        return  iSize;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        String Method ="[onCreateViewHolder]";
        Log.i(TAG, Method +  "Init..."  );
        View v = mLayoutInflater.inflate(R.layout.medicine_take_card_adapter, viewGroup, false);
        MedicineTakeListAdapter.ViewHolder vh = new MedicineTakeListAdapter.ViewHolder(v, clickListener);
        setAnimReboundonCreateViewHolder(v);
        Log.i(TAG, Method +  "End..."  );
        return vh;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        private CardView main_card;
        private TextView txt_nombre;
        private TextView txt_descripcion;
        private TextView txt_fecha;
        private EAlarmTakeMedicine alarmTakeMedicine;

        private ClickListener listener;
        private ImageView img_cloud;

        public ViewHolder(View v , ClickListener listener) {
            super(v);
            String Method ="[ViewHolder]";
            Log.i(TAG, Method + "Init..." );
            main_card = (CardView) v.findViewById(R.id.main_cardMedicineTake);
            txt_nombre = (TextView) v.findViewById(R.id.txt_nombre);
            txt_fecha= (TextView) v.findViewById(R.id.txt_fecha);
            txt_descripcion= (TextView) v.findViewById(R.id.txt_descripcion);

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

        public EAlarmTakeMedicine getAlarmTakeMedicine() {
            return alarmTakeMedicine;
        }

        public void setAlarmTakeMedicine(EAlarmTakeMedicine alarmTakeMedicine) {
            this.alarmTakeMedicine = alarmTakeMedicine;
        }

        public interface ClickListener {
            void onItemClicked(View v, int position);
            boolean onItemLongClicked(View v, int position);
        }


    }

    /**
     * @param lstAlarmTakeMedicine
     */
    public void updateData(List<EAlarmTakeMedicine> lstAlarmTakeMedicine   ) {
        String Method ="[updateData]";
        Log.i(TAG, Method + "Init..." );
        this.lstAlarmTakeMedicine.clear();
        this.lstAlarmTakeMedicine.addAll(lstAlarmTakeMedicine);
        notifyDataSetChanged();
        Log.i(TAG, Method + "End..." );
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
