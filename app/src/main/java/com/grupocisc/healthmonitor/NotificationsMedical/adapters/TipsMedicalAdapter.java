package com.grupocisc.healthmonitor.NotificationsMedical.adapters;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.grupocisc.healthmonitor.Medicines.fragments.MedicinesRegisteredListFragment;
import com.grupocisc.healthmonitor.NotificationsMedical.fragments.NotificationsTips;
import com.grupocisc.healthmonitor.R;
import com.grupocisc.healthmonitor.entities.IPushNotification;
import com.grupocisc.healthmonitor.entities.IRegisteredMedicines;

import java.util.List;

public class TipsMedicalAdapter extends RecyclerView.Adapter<TipsMedicalAdapter.ViewHolder>  {
    private static final String TAG = "TipsListAdapter";

    public NotificationsTips context;
    private List<IPushNotification.TipsMensajes> rows;
    private String mensaje;
    //constructor
    public TipsMedicalAdapter(NotificationsTips context , List<IPushNotification.TipsMensajes> rowsIRMedicines) {
        this.context = context;
        this.rows = rowsIRMedicines;
    }

    @Override
    public void onBindViewHolder(ViewHolder myViewHolder, final int position) {
        myViewHolder.txt_mensaje.setText( rows.get(position).getMensaje().length()>30?
                rows.get(position).getMensaje().substring(0,30) + "..." :  rows.get(position).getMensaje() );
        myViewHolder.txt_url.setText(  rows.get(position).getUrl() );

        myViewHolder.main_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
        /*Manda a eliminar el registro consumiendo ws*/
                AlertDialog.Builder builder = new AlertDialog.Builder(context.getActivity());
                builder.setMessage(rows.get(position).getMensaje())
                        .setTitle("Tip")
                        .setPositiveButton("ACEPTAR",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                builder.create();
                builder.show();
            }
        });

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
        View v = li.inflate(R.layout.notifi_tips_card_adapter, viewGroup, false);
        return new ViewHolder(v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private CardView main_card;
        private TextView txt_mensaje;
        private TextView txt_url;

        public ViewHolder(View v) {
            super(v);

            main_card = (CardView) v.findViewById(R.id.main_cardMedicines);
            txt_mensaje = (TextView) v.findViewById(R.id.txt_mensaje);
            txt_url = (TextView) v.findViewById(R.id.txt_url);
        }
    }

    //actulizar informacion del adapter
    public void updateData(List<IPushNotification.TipsMensajes> rowsTips) {
        Log.e(TAG,"updateData");
        rows.clear();
        rows.addAll(rowsTips);
        notifyDataSetChanged();
    }
}
