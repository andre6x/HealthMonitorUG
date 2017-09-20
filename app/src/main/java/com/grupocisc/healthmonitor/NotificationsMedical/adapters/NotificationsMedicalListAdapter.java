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

import com.grupocisc.healthmonitor.NotificationsMedical.fragments.NotificationsMedical;
import com.grupocisc.healthmonitor.R;
import com.grupocisc.healthmonitor.entities.IPushNotification;

import java.util.List;

/**
 * Created by Gema on 10/01/2017.
 */

public class NotificationsMedicalListAdapter extends RecyclerView.Adapter<NotificationsMedicalListAdapter.ViewHolder>  {
    private static final String TAG = "NotifiMediListAdapter";

    public NotificationsMedical context;
    private List<IPushNotification.NotifiMensajes> rows;

    //constructor
    public NotificationsMedicalListAdapter(NotificationsMedical context , List<IPushNotification.NotifiMensajes> rowsNotifi) {
        this.context = context;
        this.rows = rowsNotifi;
    }

    @Override
    public void onBindViewHolder(ViewHolder myViewHolder, final int position) {
        myViewHolder.txt_mensaje.setText(  ""+ rows.get(position).getMensajes() );
        myViewHolder.txt_fecha.setText(""+ rows.get(position).getNombreDoctor());


        myViewHolder.main_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context.getActivity());
                builder.setMessage(rows.get(position).getMensajes())
                        .setTitle("Notificación")
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
        View v = li.inflate(R.layout.notifi_medical_card_adapter, viewGroup, false);
        return new ViewHolder(v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private CardView main_card;
        private TextView txt_mensaje;
        private TextView txt_fecha;

        public ViewHolder(View v) {
            super(v);

            main_card = (CardView) v.findViewById(R.id.main_card);
            txt_mensaje = (TextView) v.findViewById(R.id.txt_mensaje);
            txt_fecha = (TextView) v.findViewById(R.id.txt_fecha);
        }

    }

    //actulizar informacion del adapter
    public void updateData(List<IPushNotification.NotifiMensajes> rowsNotifi) {
        rows.clear();
        rows.addAll(rowsNotifi);
        notifyDataSetChanged();
    }

}
