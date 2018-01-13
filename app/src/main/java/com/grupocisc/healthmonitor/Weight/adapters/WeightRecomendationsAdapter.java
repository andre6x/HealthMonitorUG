package com.grupocisc.healthmonitor.Weight.adapters;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.grupocisc.healthmonitor.R;
import com.grupocisc.healthmonitor.Utils.RecyclerViewAnimator;
import com.grupocisc.healthmonitor.entities.IPushNotification;

import java.util.List;

/**
 * Created by alex on 1/11/18.
 */

public class WeightRecomendationsAdapter extends RecyclerView.Adapter<WeightRecomendationsAdapter.MyViewHolder> {

    private static final String TAG = "WeightRecomAdapter";
    private RecyclerViewAnimator mAnimator;
    private boolean  isAnimRebound;
    private MyViewHolder.ClickListener clickListener;
    public Context context;
    private List<IPushNotification.Recommendation> rows;
    private LayoutInflater mLayoutInflater;


    public WeightRecomendationsAdapter(Context ctx, List<IPushNotification.Recommendation> rawRecomendations, WeightRecomendationsAdapter.MyViewHolder.ClickListener clickListener, RecyclerView recyclerView, boolean isAnimRebound)
    {
        this.context = ctx;
        this.rows = rawRecomendations;
        this.isAnimRebound = isAnimRebound;
        mAnimator = new RecyclerViewAnimator(recyclerView);
        this.clickListener = clickListener;
        mLayoutInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        v = mLayoutInflater.inflate(R.layout.notification_card_adapter, parent, false);
        WeightRecomendationsAdapter.MyViewHolder mvh = new WeightRecomendationsAdapter.MyViewHolder(v, clickListener);
        /*set Animation Rebound*/
        setAnimReboundonCreateViewHolder(v);
        return mvh ;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.content.setText(rows.get(position).getContent() + " " );

        setAnimReboundonBindViewHolder(holder.itemView, position);

    }

    @Override
    public int getItemCount() {
        return rows.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

        private CardView main_card;
        private TextView content;
        private WeightRecomendationsAdapter.MyViewHolder.ClickListener listener;

        public MyViewHolder(View v , WeightRecomendationsAdapter.MyViewHolder.ClickListener listener) {
            super(v);

            main_card = (CardView) v.findViewById(R.id.main_card);
            content = (TextView) v.findViewById(R.id.tvContent);

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
    public void updateData(List<IPushNotification.Recommendation> rowsRecomendations) {
        Log.e(TAG,"updateData");
        rows.clear();
        rows.addAll(rowsRecomendations);
        notifyDataSetChanged();
    }

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
