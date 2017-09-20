package com.grupocisc.healthmonitor.Feeding.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.grupocisc.healthmonitor.R;

/**
 * Created by Walter on 02/03/2017.
 */

public class FeedingLessonAdapterU extends RecyclerView.Adapter<FeedingLessonAdapterU.ViewHolder> {

    private Context context;
    private String[] rows;
    private int count;
    private LayoutInflater mLayoutInflater;

    public FeedingLessonAdapterU(Context context, String[] rows, int count) {
        this.context = context;
        this.rows = rows;
        this.count = count;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        v = mLayoutInflater.inflate(R.layout.feeding_lesson_adapter, parent, false);
        ViewHolder mvh = new ViewHolder(v);
        return mvh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final String[] alimento = rows[position].split(":");
        if (alimento.length > 1) {
            holder.txtTituloView.setText(alimento[0].trim());
            holder.txtnameView.setText(alimento[1].trim());
            holder.main_card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    menssageTextVisible(alimento[0].trim(), alimento[1].trim());
                }
            });
        }
    }

    public void menssageTextVisible(String title, String message) {
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle(title);
        alert.setMessage(message);
        alert.setCancelable(false);
        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface alert, int id) {

            }
        });
        alert.show();
    }

    @Override
    public int getItemCount() {
        return rows.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView txtTituloView;
        private TextView txtnameView;
        private ImageView imageView;
        private CardView main_card;

        public ViewHolder(View v) {
            super(v);
            main_card = (CardView) v.findViewById(R.id.main_card);
            imageView = (ImageView) v.findViewById(R.id.img_lock);
            txtnameView = (TextView) v.findViewById(R.id.txt_name);
            txtTituloView = (TextView) v.findViewById(R.id.txt_titulo);
        }

        @Override
        public void onClick(View v) {
        }
    }

}
