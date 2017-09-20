package com.grupocisc.healthmonitor.Alarm.fragments;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.grupocisc.healthmonitor.Alarm.activities.AlarmActivity;
import com.grupocisc.healthmonitor.Alarm.activities.AlarmRegistry;
import com.grupocisc.healthmonitor.Alarm.adapters.AlarmListMedicineTypeCardAdapter;
import com.grupocisc.healthmonitor.HealthMonitorApplicattion;
import com.grupocisc.healthmonitor.R;
import com.grupocisc.healthmonitor.Utils.Utils;
import com.grupocisc.healthmonitor.entities.EMedicineType;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.sql.SQLException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mpolo on 07/31/2017.
 */

public class AlarmListMedicineTypeCardFragment extends Fragment implements AlarmListMedicineTypeCardAdapter.ViewHolder.ClickListener {
    private static final String TAG = "[AlarmListMTFragment]";
    private String user_email;
    private RecyclerView recyclerView;
    private AlarmListMedicineTypeCardAdapter adapter;
    public ProgressDialog Dialog;
    private static List<EMedicineType> lstEMedicineType = new ArrayList<EMedicineType>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        String Method = "[onCreate]";
        Log.i(TAG, Method + "Init...");
        super.onCreate(savedInstanceState);
        user_email = Utils.getEmailFromPreference(this.getContext());
        Dialog = new ProgressDialog(getActivity());
        Log.i(TAG, Method + "End...");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        String Method = "[onCreateView]";
        Log.i(TAG, Method + "Init...");
        View view = inflater.inflate(R.layout.alarm_medicine_type_card_fragment, viewGroup, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        Log.i(TAG, Method + "End...");
        return view;
    }

    @Override
    public void onResume() {
        String Method = "[onResume]";
        Log.i(TAG, Method + "Init...");
        super.onResume();

        if (true)//if(isOnline(getActivity()))
            restarLoading();
        Log.i(TAG, Method + "End...");
    }

    public void restarLoading() {
        String Method = "[restartLoading]";
        Log.i(TAG, Method + "Init...");
        //showLoadingDialog();
        //selectDataInsulinDB(user_email, "insulina");
        selectDataInsulinDBLocal(user_email, "insulina");
        Log.i(TAG, Method + "End...");
    }

    public void selectDataInsulinDBLocal(String email, String parametro) {
        String Method = "[selectDataInsulinDBLocal]";
        Log.i(TAG, Method + "Init...");
        Calendar c = Calendar.getInstance();
        String mes = (c.get(Calendar.MONTH) + 1) < 10 ? "0" + (c.get(Calendar.MONTH) + 1) : "" + (c.get(Calendar.MONTH) + 1);
        String dia = c.get(Calendar.DAY_OF_MONTH) < 10 ? "0" + (c.get(Calendar.DAY_OF_MONTH) + 1) : "" + (c.get(Calendar.DAY_OF_MONTH) + 1);
        String anio = "" + c.get(Calendar.YEAR);
        String hora = "" + c.get(Calendar.HOUR_OF_DAY);
        String minuto = "" + c.get(Calendar.MINUTE);

        String fechaIni = "1900/01/01 00:00:00"; //anio +"/" + mes +"/" + "01";
        String fechaFin = anio + "/" + mes + "/" + dia + " " + hora + ":" + minuto + ":00";

        //if (lstEMedicineType == null)
            lstEMedicineType = new ArrayList<EMedicineType>();


        EMedicineType eMedicineType ;
        eMedicineType = new EMedicineType();
        eMedicineType.setMedicineTypeCode(1);
        eMedicineType.setMedicineTypeDescription("DIABETES");
        eMedicineType.setMedicineTypeStatus("A");
        this.lstEMedicineType.add(eMedicineType);
        eMedicineType = new EMedicineType();
        eMedicineType.setMedicineTypeCode(2);
        eMedicineType.setMedicineTypeDescription("ASMA");
        eMedicineType.setMedicineTypeStatus("A");
        this.lstEMedicineType.add(eMedicineType);
        eMedicineType = new EMedicineType();
        eMedicineType.setMedicineTypeCode(3);
        eMedicineType.setMedicineTypeDescription("GENERAL");
        eMedicineType.setMedicineTypeStatus("A");
        this.lstEMedicineType.add(eMedicineType);

//        try {
//            this.lstEMedicineType = null;
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }

        if (this.lstEMedicineType != null) {
            postExecutionQueryDBLocal();
        } else {
            showLayoutDialog();
            Utils.generarAlerta(getActivity(), getString(R.string.txt_atencion), getString(R.string.text_error_metodo));

        }

        Log.i(TAG, Method + "End...");
    }

    public void postExecutionQueryDBLocal() {
        String Method = "[postExecutionQueryDBLocal]";
        Log.i(TAG, Method + "Init...");
        showLayoutDialog();
        if (lstEMedicineType != null)
            if (lstEMedicineType.size() > 0)
                callSetAdapter();
    }

    private void showLayoutDialog(){
        String Method ="[showLayoutDialog]";
        Log.i(TAG, Method +  "Init..."  );
        if (Dialog != null)
            Dialog.dismiss();
        Log.i(TAG, Method +  "End..."  );
    }

    public void callSetAdapter(){
        String Method ="[callSetAdapter]";
        Log.i(TAG, Method + "Init..."  );
        if(adapter != null) {
            adapter.updateData(lstEMedicineType);
        }else {
            //Crea la lista
            adapter = new AlarmListMedicineTypeCardAdapter (getActivity(), lstEMedicineType , this,  recyclerView  , true);
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
//            if (isVisibleToUser) {
//                InsulinActivity  .fab.setVisibility(View.VISIBLE);
//            } else {
//                InsulinActivity.fab.setVisibility(View.GONE);
//            }
        }
        Log.i(TAG, Method +  "Init..."  );
    }

    public void onItemClicked(View view, int position) {
        int idMenu = lstEMedicineType.get(position).getMedicineTypeCode() ;
        Log.i(TAG, "onItemClicked: EMedicineType Code=" + lstEMedicineType.get(position).getMedicineTypeCode());
        Log.i(TAG, "onItemClicked: EMedicineType Description=" + lstEMedicineType.get(position).getMedicineTypeDescription()  );

//        Intent intent = new Intent(getActivity(), AlarmActivity.class);
//        Bundle bundle = new Bundle();
//        bundle.putSerializable("car", lstEMedicineType.get(position) ) ;
//        intent.putExtras(bundle);
//
//        // TRANSITIONS
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            View card = view.findViewById(R.id.main_card);
//            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),
//                    Pair.create(card, "element1"));
//            getActivity().startActivity(intent, options.toBundle());
//        } else {
//            getActivity().startActivity(intent);
//        }
    }

    @Override
    public boolean onItemLongClicked(View v, int position) {
        return false;
    }



}