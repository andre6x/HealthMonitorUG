package com.grupocisc.healthmonitor.Medicines.adapters;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.grupocisc.healthmonitor.HealthMonitorApplicattion;
import com.grupocisc.healthmonitor.Medicines.activities.MedicineRegistry;
import com.grupocisc.healthmonitor.Medicines.activities.MedicinesRegisteredActivity;
import com.grupocisc.healthmonitor.Medicines.fragments.MedicinesRegisteredListFragment;
import com.grupocisc.healthmonitor.R;
import com.grupocisc.healthmonitor.Utils.RecyclerViewAnimator;
import com.grupocisc.healthmonitor.Utils.Utils;
import com.grupocisc.healthmonitor.entities.EMedicine;
import com.grupocisc.healthmonitor.entities.IConsulMedicines;
import com.grupocisc.healthmonitor.entities.IRegisteredMedicines;

import java.util.ArrayList;
import java.util.List;

/*
*
*  REGISTRO DE MEDICAMENTOS
*
* */

public class MedicinesRegisteredListAdapter extends RecyclerView.Adapter<MedicinesRegisteredListAdapter.ViewHolder>  {
    private static final String TAG = "[MedicinesRListAdapter]";

    //private List<IRegisteredMedicines> lstIRegisteredMedicines;
    private List<IRegisteredMedicines> lstIRegisteredMedicines;
    private MedicinesRegisteredListAdapter.ViewHolder.ClickListener clickListener;
    private RecyclerViewAnimator mAnimator;
    private boolean  isAnimRebound;
    private LayoutInflater mLayoutInflater;
    public Context context;

    //constructor
    public MedicinesRegisteredListAdapter(Context c , List<IRegisteredMedicines> lstIRegisteredMedicines , ViewHolder.ClickListener clickListener ,
                                          RecyclerView recyclerView , boolean isAnimRebound) {
        String Method ="[MedicinesRegisteredListAdapter]";
        Log.i(TAG, Method + "Init..." );
        this.context = c ;
        this.lstIRegisteredMedicines = lstIRegisteredMedicines;
        this.clickListener = clickListener;
        this.mAnimator = new RecyclerViewAnimator(recyclerView);
        this.isAnimRebound = isAnimRebound;
        mLayoutInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Log.i(TAG, Method + "End..." );
    }

    @Override
    public void onBindViewHolder(final ViewHolder myViewHolder, final int position) {
        String Method ="[onBindViewHolder]";
        Log.i(TAG, Method + "Init..." );
        if (lstIRegisteredMedicines.get(position) != null){
            Log.i(TAG, Method + "lstIRegisteredMedicines.get("+position+")" + lstIRegisteredMedicines.get(position).toString() );
        }
        myViewHolder.setRegisteredMedicines(this.lstIRegisteredMedicines.get(position));

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


        myViewHolder.buttonViewOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                PopupMenu popup = new PopupMenu(context,myViewHolder.buttonViewOption);
                popup.inflate(R.menu.menu_alarm);

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.mnu_edit:{
                                int idMenu = lstIRegisteredMedicines.get(position).getId() ;
                                Intent intent = new Intent(context, MedicineRegistry.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("cardMedCtrlUpd", lstIRegisteredMedicines.get(position) ) ;
                                intent.putExtras(bundle);
                                // TRANSITIONS
//                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                                    View card = v.findViewById(R.id.main_cardMed );
//                                    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),
//                                            Pair.create(card, "element1"));
//                                    getActivity().startActivity(intent, options.toBundle());
//                                } else {
//                                    getActivity().startActivity(intent);
//                                }
                                context.startActivity(intent);
                                break;
                            }
                            case R.id.mnu_delete:{
                                try {
                                    int rows = Utils.deleteRegisteredMedicinesAndAlarmToDBLocal(
                                              lstIRegisteredMedicines.get(position).getId()
                                            , lstIRegisteredMedicines.get(position).getEmail()
                                            , HealthMonitorApplicattion.getApplication().getRegisteredMedicinesDao()
                                            , HealthMonitorApplicattion.getApplication().getEAlarmDetailsDao()
                                    );
                                    if ( rows > 0 ){

                                        lstIRegisteredMedicines.remove(myViewHolder.getRegisteredMedicines());
                                        List<IRegisteredMedicines> l = new ArrayList<IRegisteredMedicines>();
                                        for (IRegisteredMedicines r :lstIRegisteredMedicines) {
                                            l.add(r);
                                        }
                                        updateData(l);
                                        Utils.restartAlarmService(context);
                                    }
                                }catch (Exception e){e.printStackTrace();}

                                break;
                            }
                        }
                        return false;
                    }
                });
                popup.show();
            }
        });

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
        MedicinesRegisteredListAdapter.ViewHolder vh = new MedicinesRegisteredListAdapter.ViewHolder(v, clickListener);
        setAnimReboundonCreateViewHolder(v);
        Log.i(TAG, Method +  "End..."  );
        return vh;
    }




    public static class ViewHolder extends RecyclerView.ViewHolder   {

        private CardView main_card;
        private TextView txt_dosis;
        private TextView txt_nombre;
        private TextView txt_medicineType;
        private TextView txt_vecesDia;
        private TextView txt_fecha;
        private TextView txt_obs;
        private ClickListener listener;
        private ImageView img_cloud;
        private TextView buttonViewOption;

        private IRegisteredMedicines registeredMedicines;

        public ViewHolder(View v, ClickListener clickListener ) {
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
            buttonViewOption = (TextView) v.findViewById(R.id.textViewOption);

            img_cloud = (ImageView) v.findViewById(R.id.img_cloud);
            this.listener = listener;
            //v.setOnClickListener(this);
            //v.setOnLongClickListener(this);

            Log.i(TAG, Method + "End..." );
        }

        public IRegisteredMedicines getRegisteredMedicines() {
            return registeredMedicines;
        }

        public void setRegisteredMedicines(IRegisteredMedicines registeredMedicines) {
            this.registeredMedicines = registeredMedicines;
        }


//        @Override
//        public void onClick(View v) {
//            if (listener != null) {
//                listener.onItemClicked(v,getLayoutPosition());
//            }
//        }
//        @Override
//        public boolean onLongClick(View v) {
//            if (listener != null) {
//                return listener.onItemLongClicked(v,getLayoutPosition());
//            }
//            return false;
//        }


        public interface ClickListener {
            void onItemClicked(View v, int position);
            boolean onItemLongClicked(View v, int position);
            //boolean onMenuItemClick(View v, int position, MenuItem item);
        }

    }

    public void updateData(List<IRegisteredMedicines> lstIRegisteredMedicines ) {
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
