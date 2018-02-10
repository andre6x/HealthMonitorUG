package com.grupocisc.healthmonitor.Asthma.adapters;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.grupocisc.healthmonitor.Asthma.fragments.PickFlowRecommendationsFragment;
import com.grupocisc.healthmonitor.R;
import com.grupocisc.healthmonitor.Utils.RecyclerViewAnimator;
import com.grupocisc.healthmonitor.entities.IPushNotification;

import java.util.List;

/**
 * Created by alex on 1/11/18.
 */

public class PickFlowRecommendationsAdapter extends RecyclerView.Adapter<PickFlowRecommendationsAdapter.MyViewHolder> {

    private static final String TAG = "PickFlowRecomAdapter";
    //private RecyclerViewAnimator mAnimator;
    //private boolean  isAnimRebound;
    //private MyViewHolder.ClickListener clickListener;
    public PickFlowRecommendationsFragment context;
    private List<IPushNotification.Recommendation> rows;
    //private LayoutInflater mLayoutInflater;


    public PickFlowRecommendationsAdapter(PickFlowRecommendationsFragment ctx, List<IPushNotification.Recommendation> rawRecomendations)
    {
        this.context = ctx;
        this.rows = rawRecomendations;
        //this.isAnimRebound = isAnimRebound;
        //mAnimator = new RecyclerViewAnimator(recyclerView);
        //this.clickListener = clickListener;
        //mLayoutInflater = (LayoutInflater) ctx.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater _li =LayoutInflater.from(parent.getContext());
        View v =  _li.inflate(R.layout.notifi_tips_card_adapter, parent, false);
        PickFlowRecommendationsAdapter.MyViewHolder mvh = new PickFlowRecommendationsAdapter.MyViewHolder(v);
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

    //public void setAnimReboundonBindViewHolder(View itemView ,int position){
        /** Item's entrance animations during scroll are performed here.*/
      //  if(isAnimRebound)
        //    mAnimator.onBindViewHolder(itemView, position);
    //}
    //public void setAnimReboundonCreateViewHolder(View v){
        /**First item's entrance animations. */
      //  if(isAnimRebound)
        //    mAnimator.onCreateViewHolder(v);
    //}
}
