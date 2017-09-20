package com.grupocisc.healthmonitor.Disease.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.grupocisc.healthmonitor.R;
import com.grupocisc.healthmonitor.entities.IDiseasePac;

import java.util.List;

/**
 * Created by Gema on 10/01/2017.
 */

public class DiseaseListAdapter extends RecyclerView.Adapter<DiseaseListAdapter.ViewHolder>  {
    private static final String TAG = "InsulinListAdapter";

    public Context context;
    private List<IDiseasePac.Disease> rows;

    //constructor
    public DiseaseListAdapter(Context context , List<IDiseasePac.Disease> rowsIEnfermedad) {
        this.context = context;
        this.rows = rowsIEnfermedad;
    }

    @Override
    public void onBindViewHolder(ViewHolder myViewHolder, int position) {
        myViewHolder.txt_patologia1.setText(  ""+ rows.get(position).getNombre()  );
        myViewHolder.txt_fecha.setText(  rows.get(position).getFechaAparicion() +" "); //+  rows.get(position).getHora() );
        myViewHolder.txt_observacion.setText(  rows.get(position).getObservacion()  );
    }

    @Override
    public void onViewDetachedFromWindow(ViewHolder viewHolder) {
        super.onViewDetachedFromWindow(viewHolder);
        viewHolder.itemView.clearAnimation();
    }

    @Override
    public void onViewAttachedToWindow(ViewHolder viewHolder) {
        super.onViewAttachedToWindow(viewHolder);
    }


    @Override
    public int getItemCount() {
        return rows.size();

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater li = LayoutInflater.from(viewGroup.getContext());
        View v = li.inflate(R.layout.disease_card_adapter, viewGroup, false);
        return new ViewHolder(v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private CardView main_card;
        private TextView txt_fecha;
        private TextView txt_observacion;
        private TextView txt_patologia1;


        public ViewHolder(View v) {
            super(v);

            main_card = (CardView) v.findViewById(R.id.main_card);
            txt_fecha = (TextView) v.findViewById(R.id.txt_fecha);
            txt_observacion = (TextView) v.findViewById(R.id.txt_observacion);
            txt_patologia1 = (TextView) v.findViewById(R.id.txt_patologia);

        }

    }

    //actulizar informacion del adapter
    public void updateData(List<IDiseasePac.Disease> rowsEDisease) {
        Log.e(TAG,"updateData");
        rows.clear();
        rows.addAll(rowsEDisease);
        notifyDataSetChanged();
    }

}
