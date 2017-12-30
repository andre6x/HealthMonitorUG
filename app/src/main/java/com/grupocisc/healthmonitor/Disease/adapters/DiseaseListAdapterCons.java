package com.grupocisc.healthmonitor.Disease.adapters;

import android.content.Context;
import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.grupocisc.healthmonitor.R;
import com.grupocisc.healthmonitor.entities.IConsulEnfermedad;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gema on 10/01/2017.
 */

public class DiseaseListAdapterCons extends RecyclerView.Adapter<DiseaseListAdapterCons.ViewHolder>  {
    private static final String TAG = "InsulinListAdapter";
    private Context context;
    private LayoutInflater inflater;
    private int row_index = -1;
    private int idEnf;
    private String nombreEnf = "";
    private String DescripEnf = "";
    private List<IConsulEnfermedad.Enfermedad> filteredList;
    List<IConsulEnfermedad.Enfermedad> rows = new ArrayList<>();



    public DiseaseListAdapterCons(Context context , List<IConsulEnfermedad.Enfermedad> list) {
        this.context = context;
        // this.list = list;//la lista total
        this.filteredList = list; //la lista a filtrar
        this.rows.addAll(list);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Log.e(TAG, "list.size():"+this.rows.size());

    }

    public int getRow_index() {return row_index;}

    public void setRow_index(int row_index) {  this.row_index = row_index;  }

    public String getNombreEnf() {
        return nombreEnf;
    }

    public void setNombreEnf(String nombreEnf) {
        this.nombreEnf = nombreEnf;
    }

    public String getDescripEnf() {
        return DescripEnf;
    }

    public void setDescripEnf(String descripEnf) {
        DescripEnf = descripEnf;
    }

    public int getIdEnf() {
        return idEnf;
    }

    public void setIdEnf(int idEnf) {
        this.idEnf = idEnf;
    }

    @Override
    public void onBindViewHolder(DiseaseListAdapterCons.ViewHolder viewHolder, final int position) {
        //   String name = filteredList.get(position);
        String name = filteredList.get(position).getNombre();

        viewHolder.txt_title.setText(name);
        //setea el color de la imagen
        viewHolder.img_cover.setColorFilter(viewHolder.img_cover.getContext().getResources().getColor(R.color.btn_login), PorterDuff.Mode.SRC_ATOP);

        final int posicion = position;
        viewHolder.linear_layout_c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRow_index(posicion);
                setIdEnf( filteredList.get(position).getIdEnfermedad() );
                setNombreEnf(filteredList.get(position).getNombre());
                setDescripEnf(filteredList.get(position).getDescripcion());
                notifyDataSetChanged(); //actuliza el adapter
            }
        });

        if(row_index==position){
            viewHolder.linear_layout_c.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
            //holder.tv1.setTextColor(Color.parseColor("#ffffff"));
        }
        else
        {
            viewHolder.linear_layout_c.setBackgroundColor(ContextCompat.getColor(context, R.color.silver_expandle));
            //holder.tv1.setTextColor(Color.parseColor("#000000"));
        }


    }

    @Override
    public void onViewDetachedFromWindow(DiseaseListAdapterCons.ViewHolder viewHolder) {
        super.onViewDetachedFromWindow(viewHolder);
        viewHolder.itemView.clearAnimation();
    }

    @Override
    public void onViewAttachedToWindow(DiseaseListAdapterCons.ViewHolder viewHolder) {
        super.onViewAttachedToWindow(viewHolder);
        // animateCircularReveal(viewHolder.itemView);
    }


    @Override
    public int getItemCount() {
        return filteredList.size();
    }



    @Override
    public DiseaseListAdapterCons.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater li = LayoutInflater.from(viewGroup.getContext());
        View v = li.inflate(R.layout.custom_enfermedad, viewGroup, false);
        return new DiseaseListAdapterCons.ViewHolder(v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txt_title;
        private ImageView img_cover;
        private CardView card;
        private LinearLayout linear_layout_c;

        public ViewHolder(View v) {
            super(v);
            txt_title = (TextView) v.findViewById(R.id.txt_title);
            img_cover = (ImageView) v.findViewById(R.id.img_cover);
            linear_layout_c = (LinearLayout) v.findViewById(R.id.linear_layout_c);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int requestCode = getAdapterPosition();
                }
            });


        }
    }
    //filtrar lista
    public void filterUpdate(String query) {
        Log.e(TAG, "rows.size():"+rows.size());
        if (TextUtils.isEmpty(query)) {
            filteredList.clear();
            filteredList.addAll(rows);
        }
        else {
            Log.e(TAG, "filteredList.size():"+filteredList.size());
            filteredList.clear();

            for(int i = 0; i < rows.size() ;i++ ){
                String nombres = rows.get(i).getNombre();
                if(nombres!=null && !nombres.isEmpty()){
                    if (nombres.toLowerCase().contains(query.toLowerCase())) {
                        filteredList.add( rows.get(i) ); //añade ala lista la posicion del objeto
                        Log.e(TAG, "filteredList.add():");
                    }
                }
            }
        }
        setRow_index(-1);//para no selecionar ningun elemnto de la lista
        notifyDataSetChanged(); //actuliza el adapter
    }

}
