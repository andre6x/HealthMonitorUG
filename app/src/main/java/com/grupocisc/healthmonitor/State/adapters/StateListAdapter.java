package com.grupocisc.healthmonitor.State.adapters;

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
import com.grupocisc.healthmonitor.entities.IState;

import java.util.List;

public class StateListAdapter extends RecyclerView.Adapter<StateListAdapter.MyViewHolder> {

    private static final String TAG = "StateListAdapter";
    private RecyclerViewAnimator mAnimatorr;
    private boolean  isAnimRebound;
    private MyViewHolder.ClickListener clickListener;
    public Context context;
    private List<IState> rows;
    private LayoutInflater mLayoutInflater;
    //public StateListFragment context;


    public StateListAdapter(Context c , List<IState> rowsIState, MyViewHolder.ClickListener clickListener,
                            RecyclerView recyclerView, boolean isAnimRebound) {
        this.context = c;
        this.rows = rowsIState;
        this.isAnimRebound = isAnimRebound;
        mAnimatorr = new RecyclerViewAnimator(recyclerView);
        this.clickListener=clickListener;

        mLayoutInflater=(LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // this.context = context;
        //Log.e(TAG,"StateListAdapter");
    }


    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, int position) {
        myViewHolder.txt_fecha.setText(rows.get(position).getFecha()+" " );
        myViewHolder.txt_observacion.setText(rows.get(position).getObservacion());
        myViewHolder.txt_hora.setText(rows.get(position).getHora());

        if(rows.get(position).getEnviadoServer().equals("false") )
          myViewHolder.img_cloud.setImageResource(R.mipmap.cloud_off);
        else
          myViewHolder.img_cloud.setImageResource(R.mipmap.cloud_on);


        //myViewHolder.txt_estado.setText( ""+ rows.get(position).getidEstadoAnimo()); //CAMBIO
//CAMBIO
        switch (rows.get(position).getIdStatus()) {

            case 1:
                myViewHolder.txt_imagen.setImageResource(R.drawable.estado_increible_con);
                myViewHolder.txt_estado.setTextColor(ContextCompat.getColor(context,R.color.status_orange));
                myViewHolder.txt_estado.setText( "Increíble");
                break;
            case 2:
                myViewHolder.txt_imagen.setImageResource(R.drawable.estado_feliz_con);
                myViewHolder.txt_estado.setTextColor(ContextCompat.getColor(context, R.color.status_green));
                myViewHolder.txt_estado.setText( "Feliz");
                break;
            case 3:
                myViewHolder.txt_imagen.setImageResource(R.drawable.estado_serio_con);
                myViewHolder.txt_estado.setTextColor(ContextCompat.getColor(context, R.color.status_purple));
                myViewHolder.txt_estado.setText( "Normal");
                break;
            case 4:
                myViewHolder.txt_imagen.setImageResource(R.drawable.estado_triste_con);
                myViewHolder.txt_estado.setTextColor(ContextCompat.getColor(context, R.color.status_blue));
                myViewHolder.txt_estado.setText( "Mal");
                break;
            case 5:
                myViewHolder.txt_imagen.setImageResource(R.drawable.estado_horrible_con);
                myViewHolder.txt_estado.setTextColor(ContextCompat.getColor(context, R.color.status_silver));
                myViewHolder.txt_estado.setText( "Horrible");
                break;
        }




        setAnimReboundonBindViewHolder(myViewHolder.itemView, position);
    }

    @Override
    public int getItemCount() {
        return rows.size();
    }


    @Override
    public StateListAdapter.MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View v;
        v= mLayoutInflater.inflate(R.layout.state_card_adapter, viewGroup, false);
        StateListAdapter.MyViewHolder svh= new StateListAdapter.MyViewHolder(v,clickListener);

        setAnimReboundonCreateViewHolder(v);
        return svh;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

        private CardView main_card;
        private TextView txt_fecha;
        private TextView txt_estado;
        private TextView txt_observacion;
        private ImageView txt_imagen;
        private ClickListener  listener;
        private ImageView img_cloud;
        private TextView txt_hora;

        public MyViewHolder(View v, ClickListener listener) {
            super(v);

            main_card = (CardView) v.findViewById(R.id.main_card1);
            txt_fecha = (TextView) v.findViewById(R.id.txt_fecha);
            txt_estado = (TextView) v.findViewById(R.id.txt_estado);
            txt_imagen = (ImageView) v.findViewById(R.id.txt_imagen);
            img_cloud = (ImageView) v.findViewById(R.id.img_cloud);
            txt_observacion=(TextView)v.findViewById(R.id.txt_observacion);
            txt_hora=(TextView) v.findViewById(R.id.txt_hora);

            this.listener = listener;
            v.setOnClickListener(this);
            v.setOnLongClickListener(this);
            //Log.e(TAG,"ViewHolder extends RecyclerView.ViewHolder");
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

   /* @Override
    public void onViewDetachedFromWindow(ViewHolder viewHolder) {
        super.onViewDetachedFromWindow(viewHolder);
        viewHolder.itemView.clearAnimation();
        //Log.e(TAG,"onViewDetachedFromWindow");
    }

    @Override
    public void onViewAttachedToWindow(ViewHolder viewHolder) {
        super.onViewAttachedToWindow(viewHolder);

    }

    @Override
    public int getItemCount() {
        Log.e(TAG,"getItemCount"); return rows.size();
    }

    @Override
    public StateListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater li = LayoutInflater.from(viewGroup.getContext());
        View v = li.inflate(R.layout.state_card_adapter, viewGroup, false);
       // Log.e(TAG,"onCreateViewHolder");
        return new StateListAdapter.ViewHolder(v);
    }

    /*public class ViewHolder extends RecyclerView.ViewHolder {

        private CardView main_card;
        private TextView txt_fecha;
        private TextView txt_estado;
        private TextView txt_observacion;
        private ImageView txt_imagen;

        public ViewHolder(View v) {
            super(v);
            main_card = (CardView) v.findViewById(R.id.main_card);
            txt_fecha = (TextView) v.findViewById(R.id.txt_fecha);
            txt_estado = (TextView) v.findViewById(R.id.txt_estado);
            txt_observacion = (TextView) v.findViewById(R.id.txt_observacion);
            txt_imagen = (ImageView) v.findViewById(R.id.txt_imagen);

           // Log.e(TAG,"ViewHolder extends RecyclerView.ViewHolder");
        }
    }*/

    //actualizar informacion del adapter

    public void updateData(List<IState>rowsIState) {

        Log.e(TAG,"updateData");
        rows.clear();
        rows.addAll(rowsIState);
        notifyDataSetChanged();
    }


    //inicio animation rebound isAnimRebound=crear se anima , actulizar= no se anima
    public void setAnimReboundonBindViewHolder(View itemView ,int position){
        /** Item's entrance animations during scroll are performed here.*/
        if(isAnimRebound)
            mAnimatorr.onBindViewHolder(itemView, position);
    }
    public void setAnimReboundonCreateViewHolder(View v){
        /**First item's entrance animations. */
        if(isAnimRebound)
            mAnimatorr.onCreateViewHolder(v);
    }
    //fin animation rebound

}
