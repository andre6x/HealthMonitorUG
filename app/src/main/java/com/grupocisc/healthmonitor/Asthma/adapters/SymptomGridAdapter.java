package com.grupocisc.healthmonitor.Asthma.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.grupocisc.healthmonitor.Utils.RecyclerViewAnimator;
import com.grupocisc.healthmonitor.entities.IV2ConsultHbaCetona;
import com.grupocisc.healthmonitor.entities.IV2Symptom;
import com.grupocisc.healthmonitor.entities.rowsV2Symptom;

import java.util.List;

import com.grupocisc.healthmonitor.R;

//e
public class SymptomGridAdapter extends  SelectableAdapter<SymptomGridAdapter.ViewHolder> {
    private static final String TAG = "SymptomGridAdapter";

    public Context context;
    private List<IV2Symptom.rows> rows;
    private String cdn;
    private boolean  isAnimRebound;
    private RecyclerViewAnimator mAnimator;

    private ViewHolder.ClickListener clickListener;


    private SparseBooleanArray selectedItems = new SparseBooleanArray();

    public SymptomGridAdapter(Context context , ViewHolder.ClickListener clickListener , RecyclerView recyclerView, List<IV2Symptom.rows> rowsSymptom , boolean isAnimRebound) {
        this.context = context;
        this.rows = rowsSymptom;
        this.isAnimRebound = isAnimRebound;
        mAnimator = new RecyclerViewAnimator(recyclerView);
        this.cdn = cdn;
        this.clickListener = clickListener;
    }

    @Override
    public void onBindViewHolder(ViewHolder myViewHolder, final int position) {

        myViewHolder.txt_title.setText(  rows.get(position).getDescripcion() );

        if(isSelected(position) ){
            myViewHolder.main_card.setCardBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
            myViewHolder.txt_title.setTextColor(ContextCompat.getColor(context, R.color.white));
        }else{
            myViewHolder.main_card.setCardBackgroundColor(ContextCompat.getColor(context, R.color.white));
            myViewHolder.txt_title.setTextColor(ContextCompat.getColor(context, R.color.black));
        }

        /*set Animation Rebound*/
        setAnimReboundonBindViewHolder(myViewHolder.itemView, position);

    }

    @Override
    public int getItemCount() {
         return rows.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater li = LayoutInflater.from(viewGroup.getContext());
        View v = li.inflate(R.layout.symtom_cate_grid_adapter, viewGroup, false);
        /*set Animation Rebound*/
        setAnimReboundonCreateViewHolder(v);
        return new ViewHolder(v, clickListener);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

        private CardView main_card;
        private TextView txt_title;
        private ClickListener listener;

        public ViewHolder(View itemView , ClickListener listener) {
            super(itemView);
            main_card = (CardView) itemView.findViewById(R.id.main_card);
            txt_title = (TextView) itemView.findViewById(R.id.txt_title);

            this.listener = listener;

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (listener != null) {
                listener.onItemClicked(getLayoutPosition());
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (listener != null) {
                return listener.onItemLongClicked(getLayoutPosition());
            }

            return false;
        }

        public interface ClickListener {
            void onItemClicked(int position);
            boolean onItemLongClicked(int position);
        }
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
