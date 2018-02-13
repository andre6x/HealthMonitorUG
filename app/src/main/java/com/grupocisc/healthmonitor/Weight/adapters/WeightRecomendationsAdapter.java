package com.grupocisc.healthmonitor.Weight.adapters;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.grupocisc.healthmonitor.R;
import com.grupocisc.healthmonitor.Utils.RecyclerViewAnimator;
import com.grupocisc.healthmonitor.Weight.fragments.WeightRecomendationsFragment;
import com.grupocisc.healthmonitor.entities.IPushNotification;

import java.util.List;

/**
 * Created by alex on 1/11/18.
 */

public class WeightRecomendationsAdapter extends RecyclerView.Adapter<WeightRecomendationsAdapter.MyViewHolder> {

    private static final String TAG = "WeightRecomAdapter";

    public WeightRecomendationsFragment context;
    private List<IPushNotification.Recommendation> rows;


    public WeightRecomendationsAdapter(WeightRecomendationsFragment ctx, List<IPushNotification.Recommendation> rawRecomendations)
    {
        this.context = ctx;
        this.rows = rawRecomendations;

    }

    @Override




    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater _li =LayoutInflater.from(parent.getContext());
        View v =  _li.inflate(R.layout.notifi_tips_card_adapter, parent, false);
        WeightRecomendationsAdapter.MyViewHolder mvh = new WeightRecomendationsAdapter.MyViewHolder(v);
        /*set Animation Rebound*/
        //setAnimReboundonCreateViewHolder(v);
        return mvh ;
    }

    @Override


    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.content.setText(rows.get(position).content + " " );

        holder.main_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context.getActivity());
                builder.setMessage(rows.get(position).content)
                        .setTitle("")
                        //.setTitle(rows.get(position).title.isEmpty()?"Tip":rows.get(position).title)
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

        //setAnimReboundonBindViewHolder(holder.itemView, position);

    }


    @Override
    public int getItemCount() {
        return rows.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private CardView main_card;
        private TextView content;

        public MyViewHolder(View v) {
            super(v);

            main_card = (CardView) v.findViewById(R.id.main_cardMedicines);
            content = (TextView) v.findViewById(R.id.txt_mensaje);
        }
    }

    //actulizar informacion del adapter
    public void updateData(List<IPushNotification.Recommendation> rowsRecomendations) {
        Log.e(TAG,"updateData");
        rows.clear();
        rows.addAll(rowsRecomendations);
        notifyDataSetChanged();
    }


}
