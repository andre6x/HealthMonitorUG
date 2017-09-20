package com.grupocisc.healthmonitor.Routines.adapters;

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

import com.grupocisc.healthmonitor.R;
import com.grupocisc.healthmonitor.Routines.activities.RoutinesLessonActivity;
import com.grupocisc.healthmonitor.Utils.Utils;
import com.grupocisc.healthmonitor.entities.IRutinasR;

import java.util.ArrayList;
import java.util.Random;

public class RutinesGridAdapter extends RecyclerView.Adapter<RutinesGridAdapter.ViewHolder> {

    public Context context;
    private ArrayList<IRutinasR.RutinasR> rowsIdRutinas;
    private int[] srcImageRoutine = new int[]{R.drawable.rutina_diabetes1,
                                                R.drawable.rutina_diabetes2,
                                                R.drawable.rutina_diabetes3,
                                                R.drawable.rutina_diabetes4};

    public RutinesGridAdapter(Context context, ArrayList<IRutinasR.RutinasR> rowsIdRutinas) {
        this.context = context;
        this.rowsIdRutinas = rowsIdRutinas;
    }

    @Override
    public void onBindViewHolder(ViewHolder myViewHolder, final int position) {
        String url_img = "";
        myViewHolder.txt_title.setText("Rutina " + rowsIdRutinas.get(position).getValue());
        if ("R".equals(rowsIdRutinas.get(position).getKey())) {
            myViewHolder.ratingBar.setVisibility(View.VISIBLE);
            myViewHolder.msgRutina.setText("Rutina Recomendada");
        } else {
            myViewHolder.ratingBar.setVisibility(View.GONE);
            myViewHolder.msgRutina.setText("Rutina");
        }

        int idx = new Random().nextInt(srcImageRoutine.length);
        url_img = ("drawable://" + srcImageRoutine[idx]);
        Utils.loadImage(url_img, myViewHolder.img_cover);
        final String finalUrl_img = url_img;
        myViewHolder.main_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent next = new Intent(context, RoutinesLessonActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("idRutina", rowsIdRutinas.get(position).getValue());
                bundle.putString("urlImage", finalUrl_img);
                next.putExtras(bundle);
                context.startActivity(next);
            }
        });
    }

    @Override
    public int getItemCount() {
        return rowsIdRutinas.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater li = LayoutInflater.from(viewGroup.getContext());
        View v = li.inflate(R.layout.rutine_grid_card_adapter, viewGroup, false);

        return new ViewHolder(v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private CardView main_card;
        private TextView txt_title;
        private ImageView img_cover;
        private RatingBar ratingBar;
        private TextView msgRutina;

        public ViewHolder(View v) {
            super(v);
            main_card = (CardView) v.findViewById(R.id.main_card);
            txt_title = (TextView) v.findViewById(R.id.txt_title);
            img_cover = (ImageView) v.findViewById(R.id.img_cover);
            ratingBar = (RatingBar) v.findViewById(R.id.ratingBar);
            msgRutina = (TextView) v.findViewById(R.id.msgRutina);
        }
    }

}
