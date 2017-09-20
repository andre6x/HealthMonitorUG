package com.grupocisc.healthmonitor.Feeding.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.grupocisc.healthmonitor.R;
import com.grupocisc.healthmonitor.entities.rowsFeedingLesson;

import java.util.List;

/**
 * Created by Walter on 13/02/2017.
 */

public class FeedingLessonAdapter extends RecyclerView.Adapter<FeedingLessonAdapter.ViewHolder>{

    private Context context;
    private List<rowsFeedingLesson> rows;
    private String cdn;
    private int count;
    private LayoutInflater mLayoutInflater;

    public FeedingLessonAdapter(Context context, List<rowsFeedingLesson> rows, String cdn, int count) {
        this.context = context;
        this.rows = rows;
        this.cdn = cdn;
        this.count = count;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.txtCountView.setText(convertTwoDigit(position));
        if (rows.get(position).getDescription() != null)
            holder.txtnameView.setText(rows.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return rows.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        v = mLayoutInflater.inflate(R.layout.feeding_lesson_adapter, parent, false);
        ViewHolder mvh = new ViewHolder(v);
        return mvh;
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

    public void updateData(List<rowsFeedingLesson> rowsLesson) {
        rows.clear();
        rows.addAll(rowsLesson);
        notifyDataSetChanged();
    }
}
