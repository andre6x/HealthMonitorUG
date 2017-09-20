package com.grupocisc.healthmonitor.Feeding.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.grupocisc.healthmonitor.Feeding.activities.FeedingLessonActivityU;
import com.grupocisc.healthmonitor.R;
import com.grupocisc.healthmonitor.Utils.Utils;
import com.grupocisc.healthmonitor.entities.IDietU;

import java.util.ArrayList;

/**
 * Created by Walter on 01/03/2017.
 */

public class FeedingListDietAdapterU extends RecyclerView.Adapter<FeedingListDietAdapterU.ViewHolder> {

    public Context context;
    private ArrayList<IDietU.Diet> rows;

    public FeedingListDietAdapterU(Context context, ArrayList<IDietU.Diet> rows) {
        this.context = context;
        this.rows = rows;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        String titulo = "Dieta";
        if (rows.get(position).getNombre() != null) {
            titulo = rows.get(position).getNombre();
        }

        if ("R".equals(rows.get(position).getKey())) {
            holder.ratingBar.setVisibility(View.VISIBLE);
            holder.msgDieta.setText("Dieta Recomendada");
        } else {
            holder.ratingBar.setVisibility(View.GONE);
            holder.msgDieta.setText("Dieta");
        }
        holder.txt_title.setText(titulo);
        String url_img = "";
        if (!"".equals(rows.get(position).getUrl()))
            url_img = rows.get(position).getUrl();
        Utils.loadImage(url_img, holder.img_cover);

        final String finalUrl_img = url_img;
        final String tituloDieta = titulo;

        holder.main_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent next = new Intent(context, FeedingLessonActivityU.class);
                Bundle bundle = new Bundle();
                bundle.putInt("idDieta", rows.get(position).getIdDieta());
                bundle.putString("Name", "" + tituloDieta);
                bundle.putString("Description", "" + rows.get(position).getDescripcion());
                bundle.putString("urlImage", finalUrl_img);
                next.putExtras(bundle);
                context.startActivity(next);

            }
        });

    }

    @Override
    public int getItemCount() {
        return rows.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater li = LayoutInflater.from(parent.getContext());
        View v = li.inflate(R.layout.feeding_list_diet_adapter, parent, false);
        return new ViewHolder(v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private CardView main_card;
        private TextView txt_title;
        private ImageView img_cover;
        private RatingBar ratingBar;
        private TextView msgDieta;

        public ViewHolder(View v) {
            super(v);
            main_card = (CardView) v.findViewById(R.id.main_card);
            txt_title = (TextView) v.findViewById(R.id.txt_title);
            img_cover = (ImageView) v.findViewById(R.id.img_cover);
            ratingBar = (RatingBar) v.findViewById(R.id.ratingBar);
            msgDieta = (TextView) v.findViewById(R.id.msgDieta);
        }
    }
}
