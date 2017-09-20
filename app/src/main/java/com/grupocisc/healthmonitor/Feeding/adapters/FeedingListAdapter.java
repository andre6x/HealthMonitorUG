package com.grupocisc.healthmonitor.Feeding.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.grupocisc.healthmonitor.Feeding.fragments.FeedingsListFragments;
import com.grupocisc.healthmonitor.R;
import com.grupocisc.healthmonitor.entities.IGetFeeding;

import java.util.ArrayList;

/**
 * Created by Walter on 02/02/2017.
 */

public class FeedingListAdapter extends RecyclerView.Adapter<FeedingListAdapter.ViewHolder> {
    public FeedingsListFragments context;
    public ArrayList<IGetFeeding.RegistreFeeding> iFeedings;


    public FeedingListAdapter(FeedingsListFragments context, ArrayList<IGetFeeding.RegistreFeeding> iFeedings) {
        this.context = context;
        this.iFeedings = iFeedings;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.txt_alimento.setText(iFeedings.get(position).getDescripcion());
        holder.txt_fecha.setText("Calorías: " + String.valueOf(iFeedings.get(position).getCalorias()));
        holder.txt_categoria.setText("Grasas: " + String.valueOf(iFeedings.get(position).getGrasas()));
    }

    @Override
    public int getItemCount() {
        return iFeedings.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater li = LayoutInflater.from(parent.getContext());
        View v = li.inflate(R.layout.feeding_card_adapter, parent, false);
        return new ViewHolder(v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CardView main_card;
        private TextView txt_alimento;
        private TextView txt_fecha;
        private TextView txt_categoria;

        public ViewHolder(View v) {
            super(v);

            main_card = (CardView) v.findViewById(R.id.main_card);
            txt_alimento = (TextView) v.findViewById(R.id.txt_alimento);
            txt_fecha = (TextView) v.findViewById(R.id.txt_fecha);
            txt_categoria = (TextView) v.findViewById(R.id.txt_categoria);
        }
    }

    @Override
    public void onViewDetachedFromWindow(ViewHolder viewHolder) {
        super.onViewDetachedFromWindow(viewHolder);
        viewHolder.itemView.clearAnimation();
    }

    public void updateData(ArrayList<IGetFeeding.RegistreFeeding> rowsIFeeding) {
        iFeedings.clear();
        iFeedings.addAll(rowsIFeeding);
        notifyDataSetChanged();
    }

}
