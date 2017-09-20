package com.grupocisc.healthmonitor.Home.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import com.grupocisc.healthmonitor.Home.ItemHome;
import com.grupocisc.healthmonitor.R;
import com.grupocisc.healthmonitor.Utils.RecyclerViewAnimator;


/**
 * Created by Andres on 18/01/2017.
 */

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder>  {

    private String TAG = "HomeAdapter";
    private Context context;
    private List<ItemHome> rows ;
    private String cdn;
    private LayoutInflater mLayoutInflater;
    private int roundPixels;
    private float scale;
    private MyViewHolder.ClickListener clickListener;
    private RecyclerViewAnimator mAnimator;
    private boolean  isAnimRebound;

    public HomeAdapter(Context c, List<ItemHome> rows, MyViewHolder.ClickListener clickListener,
                       RecyclerView recyclerView, boolean isAnimRebound) {
        this.context = c;
        this.rows = rows;
        this.isAnimRebound = isAnimRebound;
        this.clickListener = clickListener;
        mAnimator = new RecyclerViewAnimator(recyclerView);
        mLayoutInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public void onBindViewHolder(final HomeAdapter.MyViewHolder myViewHolder, final int position) {

        myViewHolder.txt_cab_title.setText(rows.get(position).getTipo());
        myViewHolder.txt_title.setText(rows.get(position).getTitle());
        myViewHolder.img_icon.setImageResource( rows.get(position).getIconRes());
        myViewHolder.img_header.setImageResource(rows.get(position).getImageRes() );


        myViewHolder.main_card.setCardBackgroundColor(Color.parseColor(rows.get(position).getColor()));


       /*set Animation Rebound*/
        setAnimReboundonBindViewHolder(myViewHolder.itemView, position);
    }

    @Override
    public int getItemCount() {
        return rows.size();
    }


    @Override
    public HomeAdapter.MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v;
        v = mLayoutInflater.inflate(R.layout.home_list_small, viewGroup, false);
        HomeAdapter.MyViewHolder mvh = new HomeAdapter.MyViewHolder(v, clickListener);
        /*set Animation Rebound*/
        setAnimReboundonCreateViewHolder(v);
        return mvh ;

    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
        public CardView main_card;
        public ImageView img_icon;
        public ImageView img_header;
        public TextView txt_cab_title;
        public TextView txt_title;
        private ClickListener listener;

        public MyViewHolder(View itemView , ClickListener listener) {
            super(itemView);
            main_card = (CardView) itemView.findViewById(R.id.main_card);
            img_icon = (ImageView) itemView.findViewById(R.id.img_icon);
            img_header = (ImageView) itemView.findViewById(R.id.img_header);
            txt_cab_title = (TextView) itemView.findViewById(R.id.txt_cab_title);
            txt_title = (TextView) itemView.findViewById(R.id.txt_title);

            this.listener = listener;
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
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
