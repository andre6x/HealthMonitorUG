package com.grupocisc.healthmonitor.Glucose.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.grupocisc.healthmonitor.Glucose.activities.GlucoseActivity;
import com.grupocisc.healthmonitor.Glucose.activities.GlucoseRegistyActivity;
import com.grupocisc.healthmonitor.Glucose.adapters.GlucoseListAdapter;
import com.grupocisc.healthmonitor.Glucose.adapters.GlucoseRecomendationsAdapter;
import com.grupocisc.healthmonitor.HealthMonitorApplicattion;
import com.grupocisc.healthmonitor.R;
import com.grupocisc.healthmonitor.Utils.Utils;
import com.grupocisc.healthmonitor.entities.IGlucose;
import com.grupocisc.healthmonitor.entities.IPushNotification;

import java.sql.SQLException;
import java.util.List;


public class GlucoseRecomendationsFragment extends Fragment implements GlucoseRecomendationsAdapter.MyViewHolder.ClickListener {

    private String TAG = "GlucoseRecoFragment";
    private RecyclerView recyclerView;
    private GlucoseRecomendationsAdapter adapter;
    private static List<IPushNotification.Recomendation> rowsRecomendations;

    //private OnFragmentInteractionListener mListener;

    public GlucoseRecomendationsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View contentView = inflater.inflate(R.layout.fragment_glucose_recomendations, container, false);
        recyclerView = (RecyclerView) contentView.findViewById(R.id.recycler_view);

        return contentView;
    }

    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }

    @Override
    public void onItemClicked(View view, int position) {
        int idMenu = rowsRecomendations.get(position).getId() ;
        Intent intent = new Intent(getActivity(), GlucoseRegistyActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("car", rowsRecomendations.get(position) ) ;
        intent.putExtras(bundle);

        // TRANSITIONS
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            View card = view.findViewById(R.id.main_card);


            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),
                    Pair.create(card, "element1"));

            getActivity().startActivity(intent, options.toBundle());
        } else {
            getActivity().startActivity(intent);
        }

    }


//    public  void selectRecomendationsDB(){
//        Log.e(TAG,"selectRecoGluDB");
//        try {
//            //validar si en la tabla ahi datos mayor a 0
//            if (Utils.GetGlucoseFromDatabase(HealthMonitorApplicattion.getApplication().getGlucoseDao() ).size() > 0 ){
//                //asignamos datos de la tabla a la lista de objeto
//                rowsRecomendations = Utils.GetGlucoseFromDatabase(HealthMonitorApplicattion.getApplication().getGlucoseDao() );
//                //obtenemos el tamaño de la listaDEobjetos , para recorrerlo
//                //y presenta los datos de la tabla bd en el LOG
//                int tamaño = rowsRecomendations.size();
//                for(int i = 0 ; i < tamaño ; i++){
//                    Log.e(TAG,"id:" + rowsRecomendations.get(i).getId() +"-" + rowsRecomendations.get(i).getContent());
//                }
//                if(tamaño > 0) {
//                    //setear el adaptador con los datos
//                    callsetAdapter();
//                }
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }

    public void callsetAdapter(){
        //validacion si se a iniciado el adapter
        if (adapter != null){
            //actuliza la data del apdater
            Log.e(TAG,"adapter != null");
            adapter.updateData(rowsRecomendations);

        }else {//es nulo
            //crea la lista adapter
            Log.e(TAG,"adapter  null");
            adapter = new GlucoseRecomendationsAdapter(getActivity(), rowsRecomendations, this, recyclerView, true);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG,"onResume");
        //selectDataGlucoseDB();
    }

    @Override
    public boolean onItemLongClicked(View v, int position) {
        return false;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (this.isVisible()) {
            if (isVisibleToUser) {
                GlucoseActivity.fab.setVisibility(View.GONE);
            } else {
                GlucoseActivity.fab.setVisibility(View.VISIBLE);
            }
        }
    }

    //    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }
}
