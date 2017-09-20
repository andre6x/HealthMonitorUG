package com.grupocisc.healthmonitor.FitData.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.data.Value;
import com.grupocisc.healthmonitor.R;
import com.grupocisc.healthmonitor.Utils.RecyclerViewAnimator;
import com.grupocisc.healthmonitor.entities.IPressure;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.grupocisc.healthmonitor.R.id.txt_fecha;

public class FitListAdapter extends RecyclerView.Adapter<FitListAdapter.MyViewHolder> {
    private static final String TAG = "FitListAdapter";

    private RecyclerViewAnimator mAnimator;
    private boolean  isAnimRebound;
    private MyViewHolder.ClickListener clickListener;
    public Context context;
    //private List<IPressure> rows;
    private LayoutInflater mLayoutInflater;
    ArrayList<DataPoint> mData = new ArrayList<DataPoint>();

    private SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("MMM d, h:mm a");
    //public FitListAdapter(Context c , List<IPressure> rowsPressure , MyViewHolder.ClickListener clickListener,
    //                       RecyclerView recyclerView, boolean isAnimRebound) {
    public FitListAdapter(Context c , ArrayList<DataPoint> rowsPoint , MyViewHolder.ClickListener clickListener,
                RecyclerView recyclerView, boolean isAnimRebound) {

        this.context = c;
        this.mData = rowsPoint;
        this.isAnimRebound = isAnimRebound;
        mAnimator = new RecyclerViewAnimator(recyclerView);
        this.clickListener = clickListener;
        mLayoutInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, int position) {


        StringBuilder dataValue = new StringBuilder();
        for (Field field : mData.get(position).getDataType().getFields())
        {
            Value val = mData.get(position).getValue(field);

            dataValue.append("Nombre: " + field.getName() + "  Valor: " + val.toString());

        }

        //mTextView1.setText("Data Type: "+ mDataPointList.get(position).getDataType().getName());
        //mTextView2 = (TextView)convertView.findViewById(R.id.text2);
        //.setText(dataValue.toString());
        //mTextView3 = (TextView)convertView.findViewById(R.id.text3);
        //mTextView3.setText("Hora Inicio: " + mSimpleDateFormat.format(mDataPointList.get(position).getStartTime(TimeUnit.MILLISECONDS)) +
        //        "\nHora Fin: " + mSimpleDateFormat.format(mDataPointList.get(position).getEndTime(TimeUnit.MILLISECONDS)));

        myViewHolder.txt_nombre.setText(dataValue.toString());
        myViewHolder.txt_fecha.setText("Hora Inicio: " + mSimpleDateFormat.format(mData.get(position).getStartTime(TimeUnit.MILLISECONDS)));
        myViewHolder.txt_fecha_1.setText("Hora Fin: " + mSimpleDateFormat.format(mData.get(position).getEndTime(TimeUnit.MILLISECONDS)));


        //Log.e(TAG,"Mostrar datos");
     /*   myViewHolder.txt_pressure.setText("" + rows.get(position).getMaxPressure() );
        myViewHolder.txt_pressure2.setText( "/" + rows.get(position).getMinPressure() + " mm/Hg");
        myViewHolder.txt_fecha.setText(rows.get(position).getFecha() + " " );
        myViewHolder.txt_hora.setText(" " + rows.get(position).getHora());
        myViewHolder.txt_observacion.setText(rows.get(position).getObservacion());

        myViewHolder.txt_concentracion.setText(  ""+ rows.get(position).getPulso()  + " PPM");
        myViewHolder.txt_medido.setText(  rows.get(position).getMedido()  );
*/


        /*set Animation Rebound*/
        setAnimReboundonBindViewHolder(myViewHolder.itemView, position);
    }

    @Override
    public int getItemCount() {
        return mData.size();

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v;
        v = mLayoutInflater.inflate(R.layout.fit_card_adapter, viewGroup, false);
        MyViewHolder mvh = new MyViewHolder(v, clickListener);
        /*set Animation Rebound*/
        setAnimReboundonCreateViewHolder(v);
        return mvh ;
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
    /*
        private CardView main_card;
        private TextView txt_pressure;
        private TextView txt_pressure2;
        private TextView txt_fecha;
        private TextView txt_hora;
        private ImageView img_avatar;
        private ImageView img_cloud;
        private TextView txt_observacion;
        private TextView txt_concentracion;
        private TextView txt_medido;
        private ClickListener listener;

        public MyViewHolder(View v , ClickListener listener) {
            super(v);
            img_avatar = (ImageView) v.findViewById(R.id.img_avatar);
            main_card = (CardView) v.findViewById(R.id.main_card);
            txt_pressure = (TextView) v.findViewById(R.id.txt_pressure);
            txt_pressure2 = (TextView) v.findViewById(R.id.txt_pressure2);
            txt_fecha = (TextView) v.findViewById(R.id.txt_fecha);
            txt_hora  = (TextView) v.findViewById(R.id.txt_hora);
            txt_observacion = (TextView) v.findViewById(R.id.txt_observacion);
            img_cloud = (ImageView) v.findViewById(R.id.img_cloud);
            txt_concentracion = (TextView) v.findViewById(R.id.txt_concentracion);
            txt_medido = (TextView) v.findViewById(R.id.txt_medido);

            this.listener = listener;
            v.setOnClickListener(this);
            v.setOnLongClickListener(this);
        }
        */

        private CardView main_card;
        private TextView txt_fecha_1;
        private TextView txt_hora_1;
        private TextView txt_fecha;
        private TextView txt_hora;
        private TextView txt_nombre;
        private TextView txt_valor;
        private ClickListener listener;

        public MyViewHolder(View v , ClickListener listener) {
            super(v);
            main_card = (CardView) v.findViewById(R.id.main_card);
            txt_fecha = (TextView) v.findViewById(R.id.txt_fecha);
            txt_fecha_1 = (TextView) v.findViewById(R.id.txt_fecha1);
            txt_hora = (TextView) v.findViewById(R.id.txt_hora);
            txt_hora_1  = (TextView) v.findViewById(R.id.txt_hora1);
            txt_nombre = (TextView) v.findViewById(R.id.txt_nombre);
            txt_valor = (TextView) v.findViewById(R.id.txt_valor);

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
    //public void updateData(List<IPressure> rowsPressure) {
    public void updateData(ArrayList<DataPoint> rowsPoints) {
        Log.e(TAG,"updateData");
        mData.clear();
        mData.addAll(rowsPoints);
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

}
