package com.grupocisc.healthmonitor.Medicines.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.grupocisc.healthmonitor.Medicines.activities.MedicinesRegisteredActivity;
import com.grupocisc.healthmonitor.R;
import com.grupocisc.healthmonitor.entities.EMedicine;
import com.grupocisc.healthmonitor.entities.IConsulMedicines;
import com.grupocisc.healthmonitor.entities.OnItemClick;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gems on 08/03/2017.
 */

public class MedicinesRListAdapter extends RecyclerView.Adapter<MedicinesRListAdapter.ViewHolder> {
    private static final String TAG = "MedicinesRListAdapter";
    private Context context;
    private LayoutInflater inflater;
    private int row_index = -1;
    private int idMed;
    private String nombreMed="";
    private String descripcionMed="";
    private String viaMed="";
    private String presentacionMed="";
    private List<IConsulMedicines.medicamentosAll> filteredList2;
    private List<EMedicine> filteredList;
    List<IConsulMedicines.medicamentosAll> rows2 = new ArrayList<>();
    List<EMedicine> rows = new ArrayList<>();
    private OnItemClick mCallback;

    public MedicinesRListAdapter(Context context, List<EMedicine> allList, OnItemClick listener)
    //public MedicinesRListAdapter(Context context, List<IConsulMedicines.medicamentosAll> allList, OnItemClick listener)
    //public MedicinesRListAdapter(Context context, List<IRegisteredMedicines> allList)
    {
        this.context = context;
        this.filteredList = allList;
        this.rows.addAll(allList);
        this.mCallback = listener;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getRow_index(){ return row_index; }

    public void setRow_index(int row_index) {  this.row_index = row_index;  }

    public int getIdMed() { return idMed; }

    public void setIdMed(int idMed) { this.idMed = idMed; }

    public String getNombreMed() { return nombreMed; }

    public void setNombreMed(String nombreMed) { this.nombreMed = nombreMed; }

    public String getDescripcionMed() { return descripcionMed; }

    public void setDescripcionMed(String descripcionMed) { this.descripcionMed = descripcionMed; }

    public String getViaMed() { return viaMed; }

    public void setViaMed(String viaMed) { this.viaMed = viaMed; }

    public String getPresentacionMed() { return presentacionMed; }

    public void setPresentacionMed(String presentacionMed) { this.presentacionMed = presentacionMed; }

    @Override
    public void onBindViewHolder(MedicinesRListAdapter.ViewHolder viewHolder,final int position) {
        String name = filteredList.get(position).getNombre();
        viewHolder.txt_title.setText(name);

        final int posicion = position;
        viewHolder.linear_layout_c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRow_index(posicion);
                setIdMed( filteredList.get(position).getIdMedicamento());
                setNombreMed(filteredList.get(position).getNombre());
                setDescripcionMed(filteredList.get(position).getDescripcion());
                setPresentacionMed(filteredList.get(position).getPresentacion());
                setViaMed(filteredList.get(position).getVia());
                mCallback.onClick(getIdMed()+";"+getNombreMed()+";"+getPresentacionMed()+";"+getDescripcionMed()+";"+getViaMed());

                notifyDataSetChanged(); //actuliza el adapter
            }
        });

        if(row_index==position){
            viewHolder.linear_layout_c.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
            //holder.tv1.setTextColor(Color.parseColor("#ffffff"));
        }
        else
        {
            viewHolder.linear_layout_c.setBackgroundColor(ContextCompat.getColor(context, R.color.silver_expandle));
            //holder.tv1.setTextColor(Color.parseColor("#000000"));
        }
    }


    @Override
    public void onViewDetachedFromWindow(MedicinesRListAdapter.ViewHolder viewHolder) {
        super.onViewDetachedFromWindow(viewHolder);
        viewHolder.itemView.clearAnimation();
    }

    @Override
    public void onViewAttachedToWindow(MedicinesRListAdapter.ViewHolder viewHolder) {
        super.onViewAttachedToWindow(viewHolder);
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    @Override
    public MedicinesRListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater li = LayoutInflater.from(viewGroup.getContext());
        View v = li.inflate(R.layout.medicines_list_item, viewGroup, false);
        return new MedicinesRListAdapter.ViewHolder(v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txt_title;
        private LinearLayout linear_layout_c;

        public ViewHolder(View v) {
            super(v);
            txt_title = (TextView) v.findViewById(R.id.txt_title);
            linear_layout_c = (LinearLayout) v.findViewById(R.id.linear_layout_c);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int requestCode = getAdapterPosition();
                }
            });
        }
    }

    //filtrar lista
    public void filterUpdate(String query) {
        Log.e(TAG, "rows.size():"+rows.size());
        if (TextUtils.isEmpty(query)) {
            filteredList.clear();
            filteredList.addAll(rows);
        }
        else {
            Log.e(TAG, "filteredList.size():"+filteredList.size());
            filteredList.clear();

            for(int i = 0; i < rows.size() ;i++ ){
                String nombres = rows.get(i).getNombre();
                if (nombres.toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add( rows.get(i) ); //añade ala lista la posicion del objeto
                    Log.e(TAG, "filteredList.add():");
                }
            }
        }
        setRow_index(-1);//para no selecionar ningun elemnto de la lista
        this.notifyDataSetChanged(); //actuliza el adapter
    }


}
