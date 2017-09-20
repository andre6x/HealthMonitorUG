package com.grupocisc.healthmonitor.Asthma.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
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
import com.grupocisc.healthmonitor.entities.IAsthma;


import java.util.List;

public class PickFlowListAdapter extends RecyclerView.Adapter<PickFlowListAdapter.MyViewHolder> {
    private static final String TAG = "PickFlowListAdapter";

    private RecyclerViewAnimator mAnimator;
    private boolean  isAnimRebound;
    private MyViewHolder.ClickListener clickListener;
    public Context context;
    private List<IAsthma> rows;
    private LayoutInflater mLayoutInflater;

    public PickFlowListAdapter(Context c , List<IAsthma> rowsAsthma , MyViewHolder.ClickListener clickListener,
                               RecyclerView recyclerView, boolean isAnimRebound) {
        this.context = c;
        this.rows = rowsAsthma;
        this.isAnimRebound = isAnimRebound;
        mAnimator = new RecyclerViewAnimator(recyclerView);
        this.clickListener = clickListener;
        mLayoutInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, int position) {

        //Log.e(TAG,"Mostrar datos");
        myViewHolder.txt_flujo_maximo.setText(rows.get(position).getFlujoMaximo()+ " I/min");
        myViewHolder.txt_fecha.setText(rows.get(position).getFecha() + " " );
        myViewHolder.txt_hora.setText(" " + rows.get(position).getHora());
        myViewHolder.txt_observacion.setText(rows.get(position).getObservacion());
        if(rows.get(position).getEnviadoServer().equals("N") )
            myViewHolder.img_cloud.setImageResource(R.mipmap.cloud_off);
        else
            myViewHolder.img_cloud.setImageResource(R.mipmap.cloud_on);

        int valor= (int) rows.get(position).getFlujoMaximo();

        if (valor >= 0 & valor <= 250) {
           myViewHolder.main_card.setCardBackgroundColor(ContextCompat.getColor(context, R.color.asma_red));
        }
        if (valor >= 251 & valor <= 500) {
            myViewHolder.main_card.setCardBackgroundColor(ContextCompat.getColor(context, R.color.asma_yellow));
        }
        if (valor >= 501 & valor <= 800) {
            myViewHolder.main_card.setCardBackgroundColor(ContextCompat.getColor(context, R.color.asma_green));
        }

        myViewHolder.isUpdate=true;


        /*set Animation Rebound*/
        setAnimReboundonBindViewHolder(myViewHolder.itemView, position);
    }

    @Override
    public int getItemCount() {
        return rows.size();

    }

    @Override
    public PickFlowListAdapter.MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v;
        v = mLayoutInflater.inflate(R.layout.asthma_card_adapter, viewGroup, false);
        PickFlowListAdapter.MyViewHolder mvh = new PickFlowListAdapter.MyViewHolder(v, clickListener);
        /*set Animation Rebound*/
        setAnimReboundonCreateViewHolder(v);
        return mvh ;
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

        private CardView main_card;
        private TextView txt_pressure;
        private TextView txt_pressure2;
        private TextView txt_fecha;
        private TextView txt_hora;
        private ImageView img_avatar;
        private ImageView img_cloud;
        private TextView txt_observacion;
        private TextView txt_flujo_maximo;
        private ClickListener listener;
        private boolean isUpdate=true;

        public MyViewHolder(View v , ClickListener listener) {
            super(v);
            img_avatar = (ImageView) v.findViewById(R.id.img_avatar);
            main_card = (CardView) v.findViewById(R.id.main_card);
            txt_fecha = (TextView) v.findViewById(R.id.txt_fecha);
            txt_hora  = (TextView) v.findViewById(R.id.txt_hora);
            txt_observacion = (TextView) v.findViewById(R.id.txt_observacion);
            img_cloud = (ImageView) v.findViewById(R.id.img_cloud);
            txt_flujo_maximo=(TextView)v.findViewById(R.id.txt_flujo_maximo);


            this.listener = listener;
            v.setOnClickListener(this);
            v.setOnLongClickListener(this);
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

    //actulizar informacion del adapter
    public void updateData(List<IAsthma> rowsAsthma) {
        Log.e(TAG,"updateData");
        rows.clear();
        rows.addAll(rowsAsthma);
        notifyDataSetChanged();
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
    //fin animation rebound


}
