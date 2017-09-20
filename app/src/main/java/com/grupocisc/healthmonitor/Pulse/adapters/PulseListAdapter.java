package com.grupocisc.healthmonitor.Pulse.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.grupocisc.healthmonitor.Pulse.fragments.PulseListFragment;
import com.grupocisc.healthmonitor.R;
import com.grupocisc.healthmonitor.Utils.RecyclerViewAnimator;
import com.grupocisc.healthmonitor.entities.ICunsulParamet;
import com.grupocisc.healthmonitor.entities.IPaises;
import com.grupocisc.healthmonitor.entities.IPulse;

import java.util.List;

/**
 * Created by Raymond on 12/01/2017.
 */

public class PulseListAdapter extends RecyclerView.Adapter<PulseListAdapter.MyViewHolder> {
    private static final String TAG = "PulseListAdapter";

    private RecyclerViewAnimator mAnimator;
    private boolean isAnimRebound;
    private MyViewHolder.ClickListener clickListener;
    public Context context;
    private List<IPulse> rows;
    private LayoutInflater mLayoutInflater;

    public PulseListAdapter(Context c, List<IPulse> rowsPulse, MyViewHolder.ClickListener clickListener,
                            RecyclerView recyclerView, boolean isAnimRebound) {
        this.context = c;
        this.rows = rowsPulse;
        this.isAnimRebound = isAnimRebound;
        mAnimator = new RecyclerViewAnimator(recyclerView);
        this.clickListener = clickListener;
        mLayoutInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, int position) {

       Log.e(TAG,"Mostrar datos");
        myViewHolder.txt_pressure.setText("" + rows.get(position).getMaxPressure() );
        myViewHolder.txt_pressure2.setText( "/" + rows.get(position).getMinPressure() + " mm/Hg");
       myViewHolder.txt_concentracion.setText("" + rows.get(position).getConcentracion() + " PPM");
       myViewHolder.txt_fecha.setText(rows.get(position).getFecha() +" " + rows.get(position).getHora());
       myViewHolder.txt_medido.setText(rows.get(position).getMedido());
       myViewHolder.txt_observacion.setText(rows.get(position).getObservacion());
      if (rows.get(position).getEnviadoServer().equals("false"))
            myViewHolder.img_cloud.setImageResource(R.mipmap.cloud_off);
        else
            myViewHolder.img_cloud.setImageResource(R.mipmap.cloud_on);

        /*set Animation Rebound*/
        setAnimReboundonBindViewHolder(myViewHolder.itemView, position);
    }

    @Override
    public int getItemCount() {
        return rows.size();

    }

    @Override
    public PulseListAdapter.MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v;
        v = mLayoutInflater.inflate(R.layout.pulse_card_adapter, viewGroup, false);
        PulseListAdapter.MyViewHolder mvh = new PulseListAdapter.MyViewHolder(v, clickListener);
        /*set Animation Rebound*/
        setAnimReboundonCreateViewHolder(v);
        return mvh;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        private CardView main_card;
        private TextView txt_concentracion;
        private TextView txt_pressure;
        private TextView txt_pressure2;
        private TextView txt_fecha;
        private TextView txt_medido;
        private ImageView img_avatar;
        private ImageView img_cloud;
        private TextView txt_observacion;
        private ClickListener listener;

        public MyViewHolder(View v, ClickListener listener) {
            super(v);
            img_avatar = (ImageView) v.findViewById(R.id.img_avatar);
            main_card = (CardView) v.findViewById(R.id.main_card);
            txt_concentracion = (TextView) v.findViewById(R.id.txt_concentracion);
            txt_pressure = (TextView) v.findViewById(R.id.txt_pressure);
            txt_pressure2 = (TextView) v.findViewById(R.id.txt_pressure2);
            txt_fecha = (TextView) v.findViewById(R.id.txt_fecha);
            txt_medido = (TextView) v.findViewById(R.id.txt_medido);
            txt_observacion = (TextView) v.findViewById(R.id.txt_observacion);
            img_cloud = (ImageView) v.findViewById(R.id.img_cloud);

            this.listener = listener;
            v.setOnClickListener(this);
            v.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (listener != null) {
                listener.onItemClicked(v, getLayoutPosition());
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (listener != null) {
                return listener.onItemLongClicked(v, getLayoutPosition());
            }
            return false;
        }


        public interface ClickListener {
            void onItemClicked(View v, int position);

            boolean onItemLongClicked(View v, int position);
        }
    }


    //actulizar informacion del adapter
    public void updateData(List<IPulse> rowsPulse) {
        Log.e(TAG, "updateData");
        rows.clear();
        rows.addAll(rowsPulse);
        notifyDataSetChanged();
    }

    //inicio animation rebound isAnimRebound=crear se anima , actulizar= no se anima
    public void setAnimReboundonBindViewHolder(View itemView, int position) {
        /** Item's entrance animations during scroll are performed here.*/
        if (isAnimRebound)
            mAnimator.onBindViewHolder(itemView, position);
    }

    public void setAnimReboundonCreateViewHolder(View v) {
        /**First item's entrance animations. */
        if (isAnimRebound)
            mAnimator.onCreateViewHolder(v);
    }

}
//fin animation rebound}
