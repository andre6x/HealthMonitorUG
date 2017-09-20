package com.grupocisc.healthmonitor.Weight.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.grupocisc.healthmonitor.Utils.RecyclerViewAnimator;
import com.grupocisc.healthmonitor.Weight.fragments.WeightListFragment;
import com.grupocisc.healthmonitor.R;
import com.grupocisc.healthmonitor.entities.IConculPeso;
import com.grupocisc.healthmonitor.entities.IWeight;

import java.util.List;

public class WeightListAdapter extends RecyclerView.Adapter<WeightListAdapter.MyViewHolder> {
    private static final String TAG = "WeightListAdapter";

    private RecyclerViewAnimator mAnimator;
    private boolean  isAnimRebound;
    public Context context;
    private List<IWeight> rows;
    private LayoutInflater mLayoutInflater;
    private MyViewHolder.ClickListener clickListener;

    public WeightListAdapter(Context c , List<IWeight> rowsIWeight , WeightListAdapter.MyViewHolder.ClickListener clickListener,
                             RecyclerView recyclerView, boolean isAnimRebound) {
        this.context = c;
        this.rows = rowsIWeight;
        this.isAnimRebound = isAnimRebound;
        this.clickListener = clickListener;
        mAnimator = new RecyclerViewAnimator(recyclerView);
        mLayoutInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, int position) {
        //txt_fecha.getText().toString().substring(6, 10) + "/" + txt_fecha.getText().toString().substring(3, 5) + "/" + txt_fecha.getText().toString().substring(0, 2) + " " + txt_hora.getText().toString();
        myViewHolder.txt_peso.setText(   "Peso: "+ rows.get(position).getPeso()+"Kg"  );//se cambia concentracion por peso
        myViewHolder.txt_fecha.setText(rows.get(position).getFecha() + " "+ rows.get(position).getHora() );//(   rows.get(position).getFecha().toString().substring(0,4) +"-"+rows.get(position).getFecha().toString().substring(5,7)+"-"+rows.get(position).getFecha().toString().substring(8,10));
        //myViewHolder.txt_medido.setText(  rows.get(position).getMedido()  );
      //  myViewHolder.txt_observacion.setText(  " "+rows.get(position).getObservacion()  );
        myViewHolder.txt_tmb.setText(  "TMB: "+rows.get(position).getTmb() +"%" );
        myViewHolder.txt_dmo.setText(  "DMO: "+rows.get(position).getDmo()  +"%");
        myViewHolder.txt_agua.setText(  "%Agua: "+rows.get(position).getPorcentajeAgua() +"%");
        myViewHolder.txt_grasa.setText(  "%Grasa: "+rows.get(position).getPorcentajeGrasa() +"%");
        myViewHolder.txt_masamuscular.setText( "%Masa M: "+ rows.get(position).getMasamuscular() +"%");
        myViewHolder.txt_observacion.setText( "Observación: "+ rows.get(position).getObservacion()+"");
        if(rows.get(position).getEnviadoServer().equals("false") )
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
    public WeightListAdapter.MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v;
        v = mLayoutInflater.inflate(R.layout.weight_card_adapter, viewGroup, false);
        WeightListAdapter.MyViewHolder mvh = new WeightListAdapter.MyViewHolder(v, clickListener);
        /*set Animation Rebound*/
        setAnimReboundonCreateViewHolder(v);
        return mvh ;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

        private CardView main_card;
        private TextView txt_peso;
        private TextView txt_fecha;
        //private TextView txt_medido;
        private TextView txt_observacion;
        private  TextView txt_tmb;
        private  TextView txt_dmo;
        private  TextView txt_agua;
        private TextView txt_grasa;
        private ImageView img_cloud;
        private TextView txt_masamuscular;
        private ClickListener listener;


        public MyViewHolder(View v , ClickListener listener) {
            super(v);

            main_card = (CardView) v.findViewById(R.id.main_card);
            txt_peso = (TextView) v.findViewById(R.id.txt_peso);
            txt_fecha = (TextView) v.findViewById(R.id.txt_fecha);
            //  txt_medido = (TextView) v.findViewById(R.id.txt_medido);
            //txt_observacion = (TextView) v.findViewById(R.id.txt_observacion);
            txt_tmb = (TextView) v.findViewById(R.id.txt_tmb);
            txt_dmo = (TextView) v.findViewById(R.id.txt_dmo);
            txt_agua = (TextView) v.findViewById(R.id.txt_agua);
            txt_grasa = (TextView) v.findViewById(R.id.txt_grasa);
            txt_masamuscular = (TextView) v.findViewById(R.id.txt_masamuscular);
            txt_observacion=(TextView) v.findViewById(R.id.txt_observacionp);
            img_cloud = (ImageView) v.findViewById(R.id.img_cloud);

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
    public void updateData(List<IWeight> rowsIWeight) {
        Log.e(TAG,"updateData");
        rows.clear();
        rows.addAll(rowsIWeight);
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
