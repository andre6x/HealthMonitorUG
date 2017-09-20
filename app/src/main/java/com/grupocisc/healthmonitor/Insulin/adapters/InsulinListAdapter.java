package com.grupocisc.healthmonitor.Insulin.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.grupocisc.healthmonitor.Insulin.fragments.InsulinListFragment;
import com.grupocisc.healthmonitor.R;
import com.grupocisc.healthmonitor.Utils.RecyclerViewAnimator;
import com.grupocisc.healthmonitor.entities.EInsulin;
import com.grupocisc.healthmonitor.entities.ICunsulParamet;

import java.util.List;

public class InsulinListAdapter extends RecyclerView.Adapter<InsulinListAdapter.ViewHolder> {
    private static final String TAG = "[InsulinListAdapter]";
    private ViewHolder.ClickListener clickListener;
    private RecyclerViewAnimator mAnimator;
    private boolean  isAnimRebound;
    //public InsulinListFragment context;
    public Context context;
    private List<ICunsulParamet.Objeto> rows;
    private List <EInsulin> lstEInsulin ;
    private LayoutInflater mLayoutInflater;

    public InsulinListAdapter(Context c , List<EInsulin> lstEInsulin , ViewHolder.ClickListener clickListener ,
                              RecyclerView recyclerView , boolean isAnimRebound) {
        this.context = c ;
        this.lstEInsulin = lstEInsulin;

        this.clickListener = clickListener;
        this.mAnimator = new RecyclerViewAnimator(recyclerView);
        this.isAnimRebound = isAnimRebound;

        mLayoutInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public void onBindViewHolder(ViewHolder myViewHolder, final int position) {
        String Method ="[onBindViewHolder]";
        Log.i(TAG, Method +  "Init..."  );
        Log.i(TAG, Method +  this.lstEInsulin.get(position).toString()  );

        myViewHolder.txt_dosis.setText(this.lstEInsulin.get(position).getInsulina() + "\tu.");
        myViewHolder.txt_fecha.setText( this.lstEInsulin.get(position).getFecha() + " " + this.lstEInsulin.get(position).getHora()    );
        myViewHolder.txt_observacion.setText(this.lstEInsulin.get(position).getObservacion());

        if (this.lstEInsulin.get(position).getEnviadoServer().equals("false"))
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
        iSize = this.lstEInsulin.size();
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
        View v = mLayoutInflater.inflate(R.layout.insulin_card_adapter, viewGroup, false);
        InsulinListAdapter.ViewHolder vh = new InsulinListAdapter.ViewHolder(v, clickListener);
        setAnimReboundonCreateViewHolder(v);
        Log.i(TAG, Method +  "End..."  );
        return vh;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        private CardView main_card;
        private TextView txt_dosis;
        private TextView txt_fecha;
        private TextView txt_observacion;
        private ClickListener listener;
        private ImageView img_cloud;

        public ViewHolder(View v , ClickListener listener) {
            super(v);
            String Method ="[ViewHolder]";
            Log.i(TAG, Method + "Init..." );
            main_card = (CardView) v.findViewById(R.id.main_card);
            txt_dosis = (TextView) v.findViewById(R.id.txt_dosis);
            txt_fecha = (TextView) v.findViewById(R.id.txt_fecha);
            txt_observacion = (TextView) v.findViewById(R.id.txt_observacion);

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

    //actulizar informacion del adapter
    public void updateDatos(List<ICunsulParamet.Objeto> rowsEInsulin) {
        String Method ="[updateDatos]";
        Log.i(TAG, Method +  "Init..."  );
        rows.clear();
        rows.addAll(rowsEInsulin);
        notifyDataSetChanged();
        Log.i(TAG, Method +  "Init..."  );

    }

    public void updateData(List<EInsulin> lstEInsulin) {
        String Method ="[updateData]";
        Log.i(TAG, Method +  "Init..."  );
        this.lstEInsulin.clear();
        this.lstEInsulin.addAll(lstEInsulin);
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
