package com.grupocisc.healthmonitor.Glucose.adapters;

import android.content.Context;
import android.graphics.Color;
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
import com.grupocisc.healthmonitor.entities.IGlucose;




import java.util.List;

public class GlucoseListAdapter extends RecyclerView.Adapter<GlucoseListAdapter.MyViewHolder> {
    private static final String TAG = "GlucoseListAdapter";
    private RecyclerViewAnimator mAnimator;
    private boolean  isAnimRebound;
    private MyViewHolder.ClickListener clickListener;
    public Context context;
    private List<IGlucose> rows;
    private LayoutInflater mLayoutInflater;

    public GlucoseListAdapter(Context c , List<IGlucose> rowsGlucose ,  MyViewHolder.ClickListener clickListener,
                                       RecyclerView recyclerView, boolean isAnimRebound) {
        this.context = c;
        this.rows = rowsGlucose;
        this.isAnimRebound = isAnimRebound;
        mAnimator = new RecyclerViewAnimator(recyclerView);
        this.clickListener = clickListener;
        mLayoutInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, int position) {

        //Log.e(TAG,"Mostrar datos");

        myViewHolder.txt_fecha.setText(rows.get(position).getFecha() + " " );
        myViewHolder.txt_hora.setText(" " + rows.get(position).getHora());
        myViewHolder.txt_con.setText(rows.get(position).getConcentracion()+"  mg/dL");
        myViewHolder.txt_observacion.setText(rows.get(position).getObservacion());

        if(rows.get(position).getEnviadoServer().equals("false") )
            myViewHolder.img_cloud.setImageResource(R.mipmap.cloud_off);
        else
            myViewHolder.img_cloud.setImageResource(R.mipmap.cloud_on);

        int glucoseValue = rows.get(position).getConcentracion();

        if(glucoseValue<=100){
            changeCardViewBackgroundColor(myViewHolder,R.color.teal);
        }

        else if(glucoseValue>=101 && glucoseValue<=126){
            changeCardViewBackgroundColor(myViewHolder,R.color.yellow_expandle);
        }

        else if(glucoseValue>126){
            changeCardViewBackgroundColor(myViewHolder,R.color.red);
        }

        /*set Animation Rebound*/
        setAnimReboundonBindViewHolder(myViewHolder.itemView, position);
    }

    void changeCardViewBackgroundColor(MyViewHolder viewHolder, int colorId){
        switch (colorId){
            case R.color.teal:
                viewHolder.main_card.setCardBackgroundColor(viewHolder.main_card.getResources().getColor(colorId));
                viewHolder.txt_fecha.setTextColor(viewHolder.main_card.getResources().getColor(R.color.teal_Light_primary_color));
                viewHolder.txt_hora.setTextColor(viewHolder.main_card.getResources().getColor(R.color.teal_Light_primary_color));
                viewHolder.txt_con.setTextColor(viewHolder.main_card.getResources().getColor(R.color.white));
                break;
            case R.color.yellow_expandle:
                viewHolder.main_card.setCardBackgroundColor(viewHolder.main_card.getResources().getColor(colorId));
                break;
            case  R.color.red:
                viewHolder.main_card.setCardBackgroundColor(viewHolder.main_card.getResources().getColor(colorId));
                viewHolder.txt_con.setTextColor(viewHolder.main_card.getResources().getColor(R.color.red_light_primary_color));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return rows.size();

    }

    @Override
    public GlucoseListAdapter.MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v;
        v = mLayoutInflater.inflate(R.layout.glucose_card_adapter, viewGroup, false);
        GlucoseListAdapter.MyViewHolder mvh = new GlucoseListAdapter.MyViewHolder(v, clickListener);
        /*set Animation Rebound*/
        setAnimReboundonCreateViewHolder(v);
        return mvh ;
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

        private CardView main_card;
        private TextView txt_con;
        private TextView txt_hora;
        private ImageView img_avatar;
        private ImageView img_cloud;
        private TextView txt_observacion;
        private ClickListener listener;
        private TextView txt_fecha;

        public MyViewHolder(View v , ClickListener listener) {
            super(v);

            img_avatar = (ImageView) v.findViewById(R.id.img_avatar);
            main_card = (CardView) v.findViewById(R.id.main_card);
            txt_con = (TextView) v.findViewById(R.id.txt_concentracion);
            txt_hora  = (TextView) v.findViewById(R.id.txt_hora);
            txt_fecha  = (TextView) v.findViewById(R.id.txt_fecha);
            txt_observacion = (TextView) v.findViewById(R.id.txt_observacion);
            img_cloud = (ImageView) v.findViewById(R.id.img_cloud);

//            Double glucoseValue = Double.parseDouble(txt_con.getText().toString());
//
//            if(glucoseValue>=70 && glucoseValue<=100){
//                changeCardViewBackgroundColor(main_card,R.color.green);
//            }
//
//            else if(glucoseValue>=101 && glucoseValue<=126){
//                changeCardViewBackgroundColor(main_card,R.color.yellow);
//            }
//
//            else if(glucoseValue>126){
//                changeCardViewBackgroundColor(main_card,R.color.red);
//            }

            this.listener = listener;
            v.setOnClickListener(this);
            v.setOnLongClickListener(this);
        }

//        void changeCardViewBackgroundColor(CardView card, int colorId){
//            card.setCardBackgroundColor(card.getResources().getColor(colorId));
//        }

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
    public void updateData(List<IGlucose> rowsGlucose) {
        Log.e(TAG,"updateData");
        rows.clear();
        rows.addAll(rowsGlucose);
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
