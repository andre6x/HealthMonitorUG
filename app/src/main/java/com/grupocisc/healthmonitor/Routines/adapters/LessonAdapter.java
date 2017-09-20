package com.grupocisc.healthmonitor.Routines.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.grupocisc.healthmonitor.R;
import com.grupocisc.healthmonitor.entities.IRoutinesLessonU;

import java.util.ArrayList;

/**
 * Created by Walter on 23/12/2016.
 */

public class LessonAdapter extends RecyclerView.Adapter<LessonAdapter.ViewHolder> {

    private Context context;
    private ArrayList<IRoutinesLessonU.RoutineExercice> rows;
    private int count;
    private LayoutInflater mLayoutInflater;

    public LessonAdapter(Context c, ArrayList<IRoutinesLessonU.RoutineExercice> rows, int count) {
        this.context = c;
        this.rows = rows;
        this.count = count;
        mLayoutInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void getCount(ViewHolder myViewHolder, int countList) {
        for (int i = 1; i <= rows.size(); i++) {
            if (count >= i) {
                myViewHolder.imageView.setImageResource(R.mipmap.checked);
            }
        }
    }

    @Override
    public int getItemCount() {
        return rows.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v;
        v = mLayoutInflater.inflate(R.layout.routine_lesson_adapter, viewGroup, false);
        ViewHolder mvh = new ViewHolder(v);
        return mvh;
    }

    @Override
    public void onBindViewHolder(ViewHolder myViewHolder, final int position) {
        myViewHolder.txtCountView.setText(convertTwoDigit(position));
        if (rows.get(position).getNombre() != null)
            myViewHolder.txtnameView.setText(rows.get(position).getNombre());

        if (this.count == position + 1 || this.count > position) {
            myViewHolder.imageView.setImageResource(R.mipmap.checked);
        } else {
            if (this.count + 1 == position + 1) {
                myViewHolder.imageView.setImageResource(R.mipmap.left_arrow);
            }
        }

        myViewHolder.main_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView txtnameView;
        private ImageView imageView;
        private TextView txtCountView;
        private CardView main_card;

        public ViewHolder(View v) {
            super(v);
            main_card = (CardView) v.findViewById(R.id.main_card);
            txtCountView = (TextView) v.findViewById(R.id.txt_count);
            imageView = (ImageView) v.findViewById(R.id.img_lock);
            txtnameView = (TextView) v.findViewById(R.id.txt_name);
        }

        @Override
        public void onClick(View v) {

        }
    }

    private String convertTwoDigit(int position) {
        position = position + 1;
        if (position < 10)
            return "" + position;
        else
            return "" + position;
    }

}
