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
import android.widget.TextView;

import com.grupocisc.healthmonitor.Feeding.activities.FeedingLessonActivity;
import com.grupocisc.healthmonitor.R;
import com.grupocisc.healthmonitor.Utils.Utils;
import com.grupocisc.healthmonitor.entities.rowsDiets;

import java.util.List;

/**
 * Created by Walter on 10/02/2017.
 */

public class FeedingListDietAdapter extends RecyclerView.Adapter<FeedingListDietAdapter.ViewHolder> {

    public Context context;
    private List<rowsDiets> rows;
    private String cdn;

    public FeedingListDietAdapter(Context context, List<rowsDiets> rows, String cdn) {
        this.context = context;
        this.rows = rows;
        this.cdn = cdn;
    }

    @Override
    public void onBindViewHolder(ViewHolder myViewHolder, final int position) {
        myViewHolder.txt_title.setText(rows.get(position).getName());
        String url_img = "";
        if (this.cdn != null)
            url_img = this.cdn;
        if (rows.get(position).getFolder() != null)
            url_img = url_img + "/" + rows.get(position).getFolder();
        if (rows.get(position).getUrl_cdn_small() != null)
            url_img = url_img + "/" + rows.get(position).getUrl_cdn_small();
        Utils.loadImage(url_img, myViewHolder.img_cover);

        final String finalUrl_img = url_img;
        myViewHolder.main_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent next = new Intent(context, FeedingLessonActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("idCourse", "" + rows.get(position).getIdmed_courses());
                bundle.putString("Name", "" + rows.get(position).getName());
                bundle.putString("Description", "" + rows.get(position).getDescription());
                bundle.putString("urlImage", finalUrl_img);
                bundle.putString("cdn", cdn);
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

        public ViewHolder(View v) {
            super(v);
            main_card = (CardView) v.findViewById(R.id.main_card);
            txt_title = (TextView) v.findViewById(R.id.txt_title);
            img_cover = (ImageView) v.findViewById(R.id.img_cover);
        }
    }
}
