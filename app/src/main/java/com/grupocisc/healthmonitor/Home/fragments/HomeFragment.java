package com.grupocisc.healthmonitor.Home.fragments;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.grupocisc.healthmonitor.Asthma.activities.PickFlowActivity;
import com.grupocisc.healthmonitor.Complementary.activities.ComplActivity;
import com.grupocisc.healthmonitor.Disease.activities.DiseaseActivity;
import com.grupocisc.healthmonitor.Doctor.activities.DoctorActivity;
import com.grupocisc.healthmonitor.FitData.activities.FitActivity;
import com.grupocisc.healthmonitor.Glucose.activities.GlucoseActivity;
import com.grupocisc.healthmonitor.Home.ItemHome;
import com.grupocisc.healthmonitor.Home.activities.MainActivity;
import com.grupocisc.healthmonitor.Home.adapters.HomeAdapter;
import com.grupocisc.healthmonitor.Insulin.activities.InsulinActivity;
import com.grupocisc.healthmonitor.Medicines.activities.MedicinesActivity;
import com.grupocisc.healthmonitor.Pressure.activities.PressureActivity;
import com.grupocisc.healthmonitor.Pulse.activities.PulseActivity;
import com.grupocisc.healthmonitor.R;
import com.grupocisc.healthmonitor.State.activities.StateActivity;
import com.grupocisc.healthmonitor.Utils.Utils;
import com.grupocisc.healthmonitor.Weight.activities.WeightActivity;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by User on 17/04/2015.
 */
public class HomeFragment extends Fragment implements HomeAdapter.MyViewHolder.ClickListener{

    private String TAG = "HomeFragment";
    //INCLUDE
    private LinearLayout linear_loading;
    private ProgressBar progress;
    private Button retry;
    View view;

    private RecyclerView recyclerView;
    private HomeAdapter adapter;
    private List<ItemHome> listDataHome ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.home_layout, container, false);
        callsetAdapter();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private List<ItemHome> getDataItemHome() {
        List<ItemHome> pagerList = new ArrayList<>();

        pagerList.add(new ItemHome("Control de Glucosa",         R.mipmap.pager_gluco       , R.mipmap.home_gluco         ,1  , "#57c7d9", "Diabetes" ));
        pagerList.add(new ItemHome("Flujo Máximo",               R.mipmap.pager_inhaler     , R.mipmap.home_inhaler      ,11 , "#f2b118", "Asma")); //CAMBIO f2b118
        pagerList.add(new ItemHome("Control de Insulina",        R.mipmap.pager_insulina    , R.mipmap.home_insulina      ,5  , "#775ba6", "Diabetes"));
        pagerList.add(new ItemHome("Control de Pulso y Presión", R.mipmap.pager_pulso       , R.mipmap.home_pulso         ,2  , "#bdaf59", "General"));
        pagerList.add(new ItemHome("Control de Peso",            R.mipmap.pager_peso        , R.mipmap.home_peso         ,4  , "#28a697", "General"));
        pagerList.add(new ItemHome("Control del Estado de Ánimo",R.mipmap.pager_estado      , R.mipmap.home_estado        ,6  , "#c2c23a", "General"));
        pagerList.add(new ItemHome("Control de Medicinas",       R.mipmap.pager_medicinas   , R.mipmap.home_medicinas     ,7  , "#fcc10f", "General"));
        pagerList.add(new ItemHome("Control de Enfermedad",      R.mipmap.pager_enfermedades, R.mipmap.home_enfermedades  ,8  , "#4fa6d1", "General"));
        pagerList.add(new ItemHome("Examenes Complementarios",   R.mipmap.pager_prueba       , R.mipmap.home_prueba        ,9  , "#107985", "General"));
        pagerList.add(new ItemHome("Mis Doctores",           R.mipmap.pager_doctor       , R.mipmap.home_doctor    ,10 , "#ebc33f", "General")); //CAMBIO f2b118

        //V3
        pagerList.add(new ItemHome("Google Fit",R.mipmap.fit,R.mipmap.fit,12,"#f2f2f2","Fitness"));
        //pagerList.add(new ItemHome("Peak Flow",                  R.mipmap.pager_presion      , R.mipmap.home_presion     ,?  , "#c0c23a", "Diabetes"));
        return pagerList;
    }

    //setear datos al adapter
    public void callsetAdapter(){
        listDataHome = getDataItemHome();
        //crea la lista adapter
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_list);
        GridLayoutManager lLayout = new GridLayoutManager(getActivity(), 2);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(lLayout);
        adapter = new HomeAdapter(getActivity(), listDataHome,  this, recyclerView, true);
        recyclerView.setAdapter(adapter);
    }
    //inicio metodos ejecutados del adapter click
    @Override
    public void onItemClicked(View v, int position) {
        validationRegistrer(v, position);
    }

    @Override
    public boolean onItemLongClicked(View v, int position) {
        return false;
    }

    public void validationRegistrer(View v, int position){
        if (Utils.getEmailFromPreference(getActivity()) != null) {
            callTransitionActivity(v,position);
        }else {
            //presenta alert de logeo  que se encuentra en el main
            ((MainActivity) getActivity()).generarAlertaNoRegistrado();
        }
    }

    public void callTransitionActivity(View view, int position){
        int idMenu = listDataHome.get(position).getId() ;
        Intent intent = null;
        switch (idMenu) {
            case 1: // CONTROL DE GLUCOSA
                intent = new Intent(getActivity(), GlucoseActivity.class);
                break;
            case 2: // CONTROL DE PULSO
                intent = new Intent(getActivity(), PulseActivity.class);
                break;
            case 3: //CONTROL PRESIÓN
                intent = new Intent(getActivity(), PressureActivity.class);
                break;
            case 4: // CONTROL DE PESO
                intent = new Intent(getActivity(), WeightActivity.class);
                break;
            case 5: // CONTROL DE INSULINA
                intent = new Intent(getActivity(), InsulinActivity.class);
                break;
            case 6: // CONTROL ESTADO DE ANIMO
                intent = new Intent(getActivity(), StateActivity.class);
                break;
            case 7: // CONTROL DE MEDICINAS
                intent = new Intent(getActivity(), MedicinesActivity.class);
                break;
            case 8: // CONTROL DE ENFERMEDAD
                intent = new Intent(getActivity(), DiseaseActivity.class);
                break;
            case 9:  // REGISTROS COMPLEMENTARIOS
                intent = new Intent(getActivity(), ComplActivity.class);
                break;
            case 10: // VINCULACION CON DOCTOR
                intent = new Intent(getActivity(), DoctorActivity.class);
                break;
            case 11: // ASMA
                intent = new Intent(getActivity(), PickFlowActivity.class);
                break;

            case 12: //GOOGLE FIT
                intent = new Intent(getActivity(), FitActivity.class);
                break;

        }

        // TRANSITIONS
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            //View img_cover = view.findViewById(R.id.img_cover);
            View txt_title = view.findViewById(R.id.txt_title);
            View card = view.findViewById(R.id.main_card);

            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),
                    Pair.create(txt_title, "element1"),
                    Pair.create(card, "element2"));

            getActivity().startActivity(intent, options.toBundle());
        } else {
            getActivity().startActivity(intent);
        }
    }
    //fin metodos ejecutados del adapter click

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }




}
