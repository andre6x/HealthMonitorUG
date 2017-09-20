package com.grupocisc.healthmonitor.Medicines.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.grupocisc.healthmonitor.HealthMonitorApplicattion;
import com.grupocisc.healthmonitor.Medicines.activities.MedicineRegistry;
import com.grupocisc.healthmonitor.Medicines.adapters.MedicinesRegisteredListAdapter;
import com.grupocisc.healthmonitor.R;
import com.grupocisc.healthmonitor.Utils.Utils;
import com.grupocisc.healthmonitor.entities.IConsulMedicines;
import com.grupocisc.healthmonitor.entities.IRegisteredMedicines;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Gema on 29/01/2017.
 * REGISTRO DE MEDICAMENTOS
 */

public class MedicinesRegisteredListFragment extends Fragment implements MedicinesRegisteredListAdapter.ViewHolder.ClickListener {

    private static final String TAG = "[RMedicinesLstFragment]";
    private String email;
    private static List<IConsulMedicines.DetMedicamentosReg> rowsIRMedicines;
    private static List<IRegisteredMedicines> lstIRegisteredMedicines;
    private RecyclerView recyclerView;
    private MedicinesRegisteredListAdapter adapter;
    public ProgressDialog Dialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        String Method ="[onCreate]";
        Log.i(TAG, Method + "Init..." );
        super.onCreate(savedInstanceState);
        email = Utils.getEmailFromPreference(this.getContext());
        Dialog = new ProgressDialog(getActivity());
        Log.i(TAG, Method + "End..." );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.medicines_registered_fragment , viewGroup, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_viewRegister);
        return view;
    }

    @Override
    public void onResume(){
        String Method ="[onResume]";
        Log.i(TAG, Method + "Init..."  );
        super.onResume();
        //if (true) //if(isOnline(getActivity()))
            restartLoading();
        Log.i(TAG, Method + "End..."  );
    }

    public void restartLoading(){
        String Method ="[restartLoading]";
        Log.i(TAG, Method + "Init..."  );
        showLoadingDialog();
        //selectDataMedicinesDB(email);
        selectDataMedicinesDBLocal(email);
        Log.i(TAG, Method + "End..." );
    }

    public void selectDataMedicinesDBLocal(String UserMail){
        String Method ="[selectDataMedicinesDBLocal]";
        Log.i(TAG, Method + "Init..."  );
        String idUsuario="";
        idUsuario = Utils.getEmailFromPreference(this.getContext());
        try {
            //lstIRegisteredMedicines = Utils.GetMedicineUserDBLocal(UserMail,idUsuario,HealthMonitorApplicattion.getApplication().getMedicineDao());
            lstIRegisteredMedicines = Utils.GetControlRegisteredMedicineUserDBLocal(UserMail, "", HealthMonitorApplicattion.getApplication().getRegisteredMedicinesDao()   );
        } catch (Exception e) {
            e.printStackTrace();
        }
        if ( lstIRegisteredMedicines != null ){
            postExecutionQueryDBLocal();
        }else{
            showLayoutDialog();
            Utils.generarAlerta(getActivity(), getString(R.string.txt_atencion), getString(R.string.text_error_metodo));
        }
        Log.i(TAG, Method + "End..."  );
    }

    public void postExecutionQueryDBLocal(){
        String Method ="[postExecutionQueryDBLocal]";
        Log.i(TAG, Method + "Init..."  );
        showLayoutDialog();
        if (lstIRegisteredMedicines != null)
            if (lstIRegisteredMedicines.size() > 0)
                callSetAdapter();
        Log.i(TAG, Method + "End..."  );

    }

    public void postExecutionLogin() {
        showLayoutDialog();
        if (rowsIRMedicines != null) {
            if (rowsIRMedicines.size() > 0) {
                //callsetAdapter();
            }
        }
    }

    private void showLoadingDialog(){
        String Method ="[showLoadingDialog]";
        Log.i(TAG, Method + "Init..."  );
        Dialog.setMessage( getResources().getString(R.string.msg_PleaseWait) );//Dialog.setMessage("Espere un Momento..");
        Dialog.setCancelable(false);
        Dialog.show();
        Log.i(TAG, Method + "End..."  );
    }

    private void showLayoutDialog(){
        String Method ="[showLayoutDialog]";
        Log.i(TAG, Method + "Init..."  );
        if (Dialog != null)
            Dialog.dismiss();
        Log.i(TAG, Method + "End..."  );
    }

    public void callSetAdapter(){
        String Method ="[callSetAdapter]";
        Log.i(TAG, Method + "Init..."  );
        if(adapter != null)
            adapter.updateData (lstIRegisteredMedicines);
        else
        {
            //Crea la lista
            adapter = new MedicinesRegisteredListAdapter(getActivity() , lstIRegisteredMedicines , this,  recyclerView  , true);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }
        Log.i(TAG, Method + "End..."  );
    }

    @Override
    public void onItemClicked(View v, int position) {

    }

    @Override
    public boolean onItemLongClicked(View v, int position) {
        return false;
    }

    //@Override
    public boolean onMenuItemClick(View v, int position, MenuItem item) {
        switch (item.getItemId()){
            case R.id.mnu_edit:{
                int idMenu = this.lstIRegisteredMedicines.get(position).getId() ;
                Intent intent = new Intent(getActivity(), MedicineRegistry.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("cardMedCtrlUpd", this.lstIRegisteredMedicines.get(position) ) ;
                intent.putExtras(bundle);
                // TRANSITIONS
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    View card = v.findViewById(R.id.main_cardMed );
                    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),
                            Pair.create(card, "element1"));
                    getActivity().startActivity(intent, options.toBundle());
                } else {
                    getActivity().startActivity(intent);
                }

                break;
            }
            case R.id.mnu_delete:{
                break;
            }
        }
        return  false;}
}
