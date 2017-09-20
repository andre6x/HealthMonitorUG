package com.grupocisc.healthmonitor.Complementary.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.grupocisc.healthmonitor.entities.IHba1c;




import java.util.List;

public class ComplHba1cListAdapter extends RecyclerView.Adapter<ComplHba1cListAdapter.MyViewHolder> {
    private static final String TAG = "ComplHba1cListAdapter";
    private RecyclerViewAnimator mAnimator;
    private boolean  isAnimRebound;
    private MyViewHolder.ClickListener clickListener;
    public Context context;
    private List<IHba1c> rows;
    private LayoutInflater mLayoutInflater;

    public ComplHba1cListAdapter(Context c , List<IHba1c> rowsHba1c ,  MyViewHolder.ClickListener clickListener,
                                       RecyclerView recyclerView, boolean isAnimRebound) {
        this.context = c;
        this.rows = rowsHba1c;
        this.isAnimRebound = isAnimRebound;
        mAnimator = new RecyclerViewAnimator(recyclerView);
        this.clickListener = clickListener;
        mLayoutInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, int position) {

        //Log.e(TAG,"Mostrar datos");

        myViewHolder.txt_concentration.setText(""+ rows.get(position).getConcentracion()+"%");
        myViewHolder.txt_cetona.setText(""+rows.get(position).getCetonas());

        //myViewHolder.txt_hdl.setText(""+ rows.get(position).getHdl());
        //myViewHolder.txt_ldl.setText(""+ rows.get(position).getLdl());
        myViewHolder.txt_fecha.setText(rows.get(position).getFecha() + " " );
        myViewHolder.txt_hora.setText(" " + rows.get(position).getHora());
        myViewHolder.txt_observacion.setText(rows.get(position).getObservacion());

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
    public ComplHba1cListAdapter.MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v;
        v = mLayoutInflater.inflate(R.layout.compl_hba1c_card_adapter, viewGroup, false);
        ComplHba1cListAdapter.MyViewHolder mvh = new ComplHba1cListAdapter.MyViewHolder(v, clickListener);
        /*set Animation Rebound*/
        setAnimReboundonCreateViewHolder(v);
        return mvh ;
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

        private CardView main_card;
        private TextView txt_concentration;
        private TextView txt_hora;
        private ImageView img_avatar;
        private TextView txt_cetona;
        private ImageView img_cloud;
        private TextView txt_observacion;
        private ClickListener listener;
        private TextView txt_fecha;

        public MyViewHolder(View v , ClickListener listener) {
            super(v);
            img_avatar = (ImageView) v.findViewById(R.id.img_avatar);
            main_card = (CardView) v.findViewById(R.id.main_card);
            txt_concentration = (TextView) v.findViewById(R.id.txt_concentration);
            txt_cetona=(TextView) v.findViewById(R.id.txt_cetona);
            //txt_hdl = (TextView) v.findViewById(R.id.txt_coleshdl);
            //txt_ldl = (TextView) v.findViewById(R.id.txt_colesldl);
            txt_hora  = (TextView) v.findViewById(R.id.txt_hora);
            txt_fecha  = (TextView) v.findViewById(R.id.txt_fecha);
            txt_observacion = (TextView) v.findViewById(R.id.txt_observacion);
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
    public void updateData(List<IHba1c> rowsHba1c) {
        Log.e(TAG,"updateData");
        rows.clear();
        rows.addAll(rowsHba1c);
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
