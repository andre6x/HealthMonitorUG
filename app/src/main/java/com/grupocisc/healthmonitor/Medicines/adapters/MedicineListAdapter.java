package com.grupocisc.healthmonitor.Medicines.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.grupocisc.healthmonitor.R;
import com.grupocisc.healthmonitor.Utils.RecyclerViewAnimator;
import com.grupocisc.healthmonitor.entities.IRegisteredMedicines;


import java.util.List;

/**
 * Created by Gema on 10/01/2017.
 * CONTROL DE MEDICAMENTOS LISTA
 */

public class MedicineListAdapter extends RecyclerView.Adapter<MedicineListAdapter.ViewHolder>  {
    private static final String TAG = "[MedListAdapter]";

    //public MedicineListFragment context;
    //private List<IRegCrtMedicamentos.ConsulCtrlMedList> rows;
    private List<IRegisteredMedicines> lstIRegisteredMedicines;
    private MedicineListAdapter.ViewHolder.ClickListener clickListener;
    private RecyclerViewAnimator mAnimator;
    private boolean  isAnimRebound;
    private LayoutInflater mLayoutInflater;
    public Context context;

    public MedicineListAdapter(Context c , List<IRegisteredMedicines> lstIRegisteredMedicines , ViewHolder.ClickListener clickListener ,
                               RecyclerView recyclerView , boolean isAnimRebound) {
        this.context = c ;
        this.lstIRegisteredMedicines = lstIRegisteredMedicines;

        this.clickListener = clickListener;
        this.mAnimator = new RecyclerViewAnimator(recyclerView);
        this.isAnimRebound = isAnimRebound;

        mLayoutInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public void onBindViewHolder(ViewHolder myViewHolder, final int position) {
        String Method ="[onBindViewHolder]";
        Log.i(TAG, Method + "Init..." );
        if (lstIRegisteredMedicines.get(position) != null){
            Log.i(TAG, Method + "lstIRegisteredMedicines.get("+position+")" + lstIRegisteredMedicines.get(position).toString() );
        }

        Log.i(TAG, "onBindViewHolder: fechaConsumoBase  = " + this.lstIRegisteredMedicines.get(position).getFechaInicio());

        String fechaConsumo =   this.lstIRegisteredMedicines.get(position).getFechaInicio() .substring(8,10)  + "/" +
                this.lstIRegisteredMedicines.get(position).getFechaInicio().substring(5,7)  + "/" +
                this.lstIRegisteredMedicines.get(position).getFechaInicio().substring(0,4)  + " " +
                this.lstIRegisteredMedicines.get(position).getFechaInicio().substring(11,16)  ;

        Log.i(TAG, "onBindViewHolder: fechaConsumo = " + fechaConsumo);
        myViewHolder.txt_dosis.setText( "Dosis: " + this.lstIRegisteredMedicines.get(position).getDosis()    );
        myViewHolder.txt_nombre.setText( this.lstIRegisteredMedicines.get(position).getNombre() );
        myViewHolder.txt_vecesDia.setText( this.lstIRegisteredMedicines.get(position).getReminderTimeDescription() + "\t\t"+  fechaConsumo   );
        //myViewHolder.txt_vecesDia.setText( "Veces en el día: " + this.lstIRegisteredMedicines.get(position).getVeces_dia() + "\t\t"+  fechaConsumo   );
        myViewHolder.txt_obs.setText(this.lstIRegisteredMedicines.get(position).getConsumo_medicina()  );

        myViewHolder.txt_medicineType.setText("Tipo: " + this.lstIRegisteredMedicines.get(position).getMedicineTypeDescription() );


        if (this.lstIRegisteredMedicines.get(position).getSentWs().equals("N"))
            myViewHolder.img_cloud.setImageResource(R.mipmap.cloud_off);
        else
            myViewHolder.img_cloud.setImageResource(R.mipmap.cloud_on);


//        myViewHolder.main_card.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(context  , MedicineUpdateNoSeUsa.class);
//                intent.putExtra("IdMed", lstIRegisteredMedicines.get(position).getId_medicacion()   );
//                intent.putExtra("Nombre", lstIRegisteredMedicines.get(position).getNombre());
//                intent.putExtra("Dosis", lstIRegisteredMedicines.get(position).getDosis());
//                intent.putExtra("Veces", lstIRegisteredMedicines.get(position).getVeces_dia());
//                try {
//                    // el que parsea
//                    //SimpleDateFormat parseador = new SimpleDateFormat("yyyy-MM-dd");
//                    SimpleDateFormat parseador = new SimpleDateFormat( HealthMonitorApplicattion.getApplication().getResources().getString(R.string.txt_DateFormat101)  );
//                    // el que formatea
//                    //SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
//                    SimpleDateFormat formateador = new SimpleDateFormat( HealthMonitorApplicattion.getApplication().getResources().getString(R.string.txt_DateFormat103) );
//                    //Date date = parseador.parse(rows.get(position).getFecha().substring(0,10));
//                    Log.i(TAG,"" + parseador);
//                    Log.i(TAG,"" + HealthMonitorApplicattion.getApplication().getResources().getString(R.string.txt_DateFormat101) );
//                    Log.i(TAG,"" + formateador);
//                    Log.i(TAG,"" + HealthMonitorApplicattion.getApplication().getResources().getString(R.string.txt_DateFormat103) );
//
//                    Date date = parseador.parse(lstIRegisteredMedicines.get(position).getFecha_consumo().substring(0,10));
//                    intent.putExtra("fecha",formateador.format(date)); //  rows.get(position).getFecha().substring(0,10));
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
////                intent.putExtra("hora", rows.get(position).getFecha().substring(11,16));
////                intent.putExtra("Obs", rows.get(position).getDescripcion());
//                intent.putExtra("hora", lstIRegisteredMedicines.get(position).getFecha_consumo().substring(11,16));
//                intent.putExtra("Obs", lstIRegisteredMedicines.get(position).getConsumo_medicina()  );
//                context.startActivity(intent);
//            }
//        });
        /*set Animation Rebound*/
        setAnimReboundonBindViewHolder(myViewHolder.itemView, position);
        Log.i(TAG, Method + "End..." );
    }

    @Override
    public void onViewDetachedFromWindow(ViewHolder viewHolder) {
        String Method ="[onViewDetachedFromWindow]";
        Log.i(TAG, Method +  "Init..."  );
        super.onViewDetachedFromWindow(viewHolder);
        viewHolder.itemView.clearAnimation();
        Log.i(TAG, Method +  "End..."  );
    }

    @Override
    public void onViewAttachedToWindow(ViewHolder viewHolder) {
        String Method ="[onViewAttachedToWindow]";
        Log.i(TAG, Method +  "Init..."  );
        super.onViewAttachedToWindow(viewHolder);
        Log.i(TAG, Method +  "End..."  );
    }

    @Override
    public int getItemCount() {
        String Method ="[getItemCount]";
        Log.i(TAG, Method +  "Init..."  );
        int iSize=0;
        iSize = this.lstIRegisteredMedicines.size();
        Log.i(TAG, Method +  "iSize = " + iSize  );
        Log.i(TAG, Method +  "End..."  );
        return  iSize;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        String Method ="[onCreateViewHolder]";
        Log.i(TAG, Method +  "Init..."  );
        View v = mLayoutInflater.inflate(R.layout.medicine_card_adapter, viewGroup, false);
        MedicineListAdapter.ViewHolder vh = new MedicineListAdapter.ViewHolder(v, clickListener);
        setAnimReboundonCreateViewHolder(v);
        Log.i(TAG, Method +  "End..."  );
        return vh;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        private CardView main_card;
        private TextView txt_dosis;
        private TextView txt_nombre;
        private TextView txt_medicineType;
        private TextView txt_vecesDia;
        private TextView txt_fecha;
        private TextView txt_obs;
        private ClickListener listener;
        private ImageView img_cloud;
        private Menu menu ;

        public ViewHolder(View v , ClickListener listener) {
            super(v);
            String Method ="[ViewHolder]";
            Log.i(TAG, Method + "Init..." );
            main_card = (CardView) v.findViewById(R.id.main_card);
            txt_dosis = (TextView) v.findViewById(R.id.txt_dosis);
            txt_nombre = (TextView) v.findViewById(R.id.txt_nombre);
            txt_medicineType = (TextView) v.findViewById(R.id.txt_medicineType);
            txt_vecesDia = (TextView) v.findViewById(R.id.txt_vecesDia);
            txt_fecha    = (TextView)  v.findViewById(R.id.txt_fecha);
            txt_obs= (TextView) v.findViewById(R.id.txt_obs);

            img_cloud = (ImageView) v.findViewById(R.id.img_cloud);
            this.listener = listener;
            v.setOnClickListener(this);
            v.setOnLongClickListener(this);
            Log.i(TAG, Method + "End..." );
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

    /**
     * @param rowsMedicines
     */
    //actulizar informacion del adapter
//    public void updateDatos(List<IRegCrtMedicamentos.ConsulCtrlMedList> rowsMedicines) {
//        Log.e(TAG,"updateDatos");
//        rows.clear();
//        rows.addAll(rowsMedicines);
//        notifyDataSetChanged();
//    }

    /**
     * @param lstIRegisteredMedicines
     */
    public void updateData(List<IRegisteredMedicines> lstIRegisteredMedicines   ) {
        String Method ="[updateData]";
        Log.i(TAG, Method + "Init..." );
        this.lstIRegisteredMedicines.clear();
        this.lstIRegisteredMedicines.addAll(lstIRegisteredMedicines);
        notifyDataSetChanged();
        Log.i(TAG, Method + "End..." );
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


}
