package com.grupocisc.healthmonitor.Recommendations.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.grupocisc.healthmonitor.R;
import com.grupocisc.healthmonitor.Utils.Utils;
import com.grupocisc.healthmonitor.entities.rowsTopTen;

import java.util.List;

public class TopFoodsAdapter extends RecyclerView.Adapter<TopFoodsAdapter.ViewHolder> {
    private static final String TAG = "TopFoodsAdapter";

    public Context context;
    private List<rowsTopTen> rows;
    private String cdn;

    public TopFoodsAdapter(Context context , List<rowsTopTen> rowstopTen , String cdn ) {
        this.context = context;
        this.rows = rowstopTen;
        this.cdn = cdn;
    }

    @Override
    public void onBindViewHolder(ViewHolder myViewHolder, int position) {

        myViewHolder.txt_title.setText(  ""+ rows.get(position).getName()  );

        String url_img = "";
        if(this.cdn != null)
            url_img = this.cdn;
        if(rows.get(position).getFolder() != null)
            url_img = url_img+"/"+rows.get(position).getFolder();
        if (rows.get(position).getUrl_cdn_small() != null)
            url_img = url_img+"/"+rows.get(position).getUrl_cdn_small();
        //System.out.println("**--**--"+url_img);
        Utils.loadImage(url_img, myViewHolder.img_cover );

        /*myViewHolder.main_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, DetalleLessonActivity.class);

                Bundle bundle = new Bundle();
                //bundle.putSerializable("car", rowsRutinas.get(position) ) ;
                bundle.putInt("idLeccion", rowsRutinas.get(position).getIdmed_lesson() );
                bundle.putInt("idCourse", rowsRutinas.get(position).getIdmed_courses() );
                intent.putExtras(bundle);

                context.startActivity(intent);


            }
        });*/

    }


    @Override
    public int getItemCount() {
         return rows.size();

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater li = LayoutInflater.from(viewGroup.getContext());
        View v = li.inflate(R.layout.top_food_card_adapter, viewGroup, false);
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
