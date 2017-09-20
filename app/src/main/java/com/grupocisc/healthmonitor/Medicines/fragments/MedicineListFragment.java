package com.grupocisc.healthmonitor.Medicines.fragments;

import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.util.Pair;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.grupocisc.healthmonitor.HealthMonitorApplicattion;
import com.grupocisc.healthmonitor.Medicines.activities.MedicineRegistry;
import com.grupocisc.healthmonitor.Medicines.activities.MedicinesActivity;
import com.grupocisc.healthmonitor.Medicines.activities.MedicinesRegisteredActivity;
import com.grupocisc.healthmonitor.Medicines.adapters.MedicineListAdapter;
import com.grupocisc.healthmonitor.R;
import com.grupocisc.healthmonitor.Utils.Utils;
import com.grupocisc.healthmonitor.entities.IRegCrtMedicamentos;
import com.grupocisc.healthmonitor.entities.IRegisteredMedicines;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Gema on 10/01/2017.
 * CONTROL DE MEDICAMENTOS
 */

public class MedicineListFragment extends Fragment implements MedicineListAdapter.ViewHolder.ClickListener {

    private static final String TAG = "[MedListFragment]";
    private String user_email;
    private RecyclerView recyclerView;
    private MedicineListAdapter adapter;
    public ProgressDialog Dialog;
    private List<IRegCrtMedicamentos.ConsulCtrlMedList> rowsIMedicines;
    private List<IRegisteredMedicines> lstIRegisteredMedicines;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        String Method ="[onCreate]";
        Log.i(TAG, Method +  "Init..."  );
        super.onCreate(savedInstanceState);
        user_email = Utils.getEmailFromPreference(this.getActivity());
        Dialog = new ProgressDialog(getActivity());
        Log.i(TAG, Method + "End..."  );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState){
        String Method ="[onCreate]";
        Log.i(TAG, Method +  "Init..."  );
        View view = inflater.inflate(R.layout.medicine_register_fragment , viewGroup, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        Log.i(TAG, Method +  "End..."  );
        return view;
    }

    @Override
    public void onResume() {
        String Method ="[onResume]";
        Log.i(TAG, Method + "Init..."  );
        super.onResume();
        //if(isOnline(getActivity()))
            restartLoading();
        Log.i(TAG, Method + "End..."  );
    }

    public void restartLoading(){
        String Method ="[restartLoading]";
        Log.i(TAG, Method + "Init..."  );
        //showLoadingDialog();
        //selectDataMedicines();
        selectDataRegisteredMedicinesDBLocal();
        Log.i(TAG, Method + "End..."  );
    }

    private void selectDataRegisteredMedicinesDBLocal(){
        String Method ="[selectDataRegisteredMedicinesDBLocal]";
        Log.i(TAG, Method + "Init..." );

        try {
            //lstIRegisteredMedicines = Utils.GetControlRegisteredMedicineUserDBLocal(user_email, "", HealthMonitorApplicattion.getApplication().getRegisteredMedicinesDao()   );
        }catch (Exception e){
            //showLayoutDialog();
            //Utils.generarAlerta(getActivity(), getString(R.string.txt_atencion), getString(R.string.text_error_metodo));
            e.printStackTrace();
        }

        if (lstIRegisteredMedicines != null ){
            postExecutionQueryDBLocal();
        }else{
            //Utils.generarAlerta(getActivity(), getString(R.string.txt_atencion), getString(R.string.text_error_metodo));
        }
        Log.i(TAG, Method + "End..." );
    }

    public void postExecutionQueryDBLocal(){
        String Method ="[postExecutionQueryDBLocal]";
        Log.i(TAG, Method + "Init..." );
        //showLayoutDialog();
        if (lstIRegisteredMedicines != null)
            if (lstIRegisteredMedicines.size() > 0 ){
                Collections.sort( lstIRegisteredMedicines );
                callSetAdapter();
            }
        Log.i(TAG, Method + "End..." );
    }

    
    /*
    *
    *
    * Mostrar la lista de Medicamentos en CONTROL
    * */

    public void callSetAdapter(){
        String Method ="[callSetAdapter]";
        Log.i(TAG, Method + "Init..."  );
        if(adapter != null) {
            adapter.updateData(lstIRegisteredMedicines);
        }else{
            //Crea la lista
            adapter = new MedicineListAdapter(getActivity() , lstIRegisteredMedicines , this,  recyclerView  , true); //adapter = new MedicineListAdapter(this, lstIRegisteredMedicines);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }
        Log.i(TAG, Method + "End..."  );
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        String Method ="[setUserVisibleHint]";
        Log.i(TAG, Method +  "Init..."  );
        super.setUserVisibleHint(isVisibleToUser);
        if (this.isVisible()) {
            if (isVisibleToUser) {
//                MedicinesActivity.fabControl.setVisibility(View.VISIBLE);
//                MedicinesActivity.fabRegistro.setVisibility(View.VISIBLE);
            } else {
//                MedicinesActivity.fabControl.setVisibility(View.GONE);
//                MedicinesActivity.fabRegistro.setVisibility(View.GONE);
            }
        }
        Log.i(TAG, Method +  "Init..."  );
    }

    @Override
    public void onItemClicked(View view, int position) {
        int idMenu = this.lstIRegisteredMedicines.get(position).getId() ;
        //Intent intent = new Intent(getActivity(), MedicineRegistry.class);
        Intent intent = new Intent(getActivity(), MedicinesRegisteredActivity.class);
        Bundle bundle = new Bundle();
        Log.i(TAG, "onItemClicked: " + this.lstIRegisteredMedicines.get(position).toString() );
        bundle.putSerializable("cardMed", this.lstIRegisteredMedicines.get(position) ) ;
        intent.putExtras(bundle);
        // TRANSITIONS
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            View card = view.findViewById(R.id.main_cardMed );
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),
                    Pair.create(card, "element1"));
            getActivity().startActivity(intent, options.toBundle());
        } else {
            getActivity().startActivity(intent);
        }
    }

    @Override
    public boolean onItemLongClicked(View v, int position) {
        return false;
    }

}






