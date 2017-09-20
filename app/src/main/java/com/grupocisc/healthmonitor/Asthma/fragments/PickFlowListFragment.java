package com.grupocisc.healthmonitor.Asthma.fragments;

/**
 * Created by Mariuxi on 13/01/2017.
 */

/**
 * Created by Mariuxi on 12/01/2017.
 */

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

import com.grupocisc.healthmonitor.Asthma.activities.PickFlowActivity;
import com.grupocisc.healthmonitor.Asthma.activities.PickFlowRegistry;
import com.grupocisc.healthmonitor.Asthma.adapters.PickFlowListAdapter;
import com.grupocisc.healthmonitor.HealthMonitorApplicattion;
import com.grupocisc.healthmonitor.R;
import com.grupocisc.healthmonitor.Utils.Utils;
import com.grupocisc.healthmonitor.entities.IAsthma;
import com.grupocisc.healthmonitor.entities.IPressure;

import java.sql.SQLException;
import java.util.List;

public class PickFlowListFragment extends Fragment implements PickFlowListAdapter.MyViewHolder.ClickListener {

    private String TAG = "PickFlowListFragment";
    private RecyclerView recyclerView;
    private PickFlowListAdapter adapter;
    private static List<IAsthma> rowsAsthma;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View contenView = inflater.inflate(R.layout.asthma_registre_fragment, container, false);
        recyclerView = (RecyclerView) contenView.findViewById(R.id.recycler_view);
        return contenView;
    }
    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG,"onResume");
        selectDataAsthmaDB();
    }

    //obtener datos de la tabla BD
    public  void selectDataAsthmaDB(){
        Log.e(TAG,"selectDataAsthmaDB");
        try {
            //validar si en la tabla ahi datos mayor a 0
            if (Utils.GetAsthmaFromDatabase(HealthMonitorApplicattion.getApplication().getAsthmaDao() ).size() > 0 ){
                //asignamos datos de la tabla a la lista de objeto
                rowsAsthma = Utils.GetAsthmaFromDatabase(HealthMonitorApplicattion.getApplication().getAsthmaDao() );
                //obtenemos el tamaño de la listaDEobjetos , para recorrerlo
                //y presenta los datos de la tabla bd en el LOG
                int tamaño = rowsAsthma.size();
                for(int i = 0 ; i < tamaño ; i++){
                    Log.e(TAG,"Pressure:" + rowsAsthma.get(i).getEnviadoServer() +"-" + rowsAsthma.get(i).getFlujoMaximo()+"/"+ rowsAsthma.get(i)+" I/min"+" -fecha:"+rowsAsthma.get(i).getFecha() );//se cambia concentracion por peso
                }
                if(tamaño > 0) {
                    //setear el adaptador con los datos
                    callsetAdapter();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public  void callsetAdapter(){
        //validacion si se a iniciado el adapter
        if (adapter != null){
            //actuliza la data del apdater
            Log.e(TAG,"adapter != null");
            adapter.updateData(rowsAsthma);

        }else  {//es nulo
            //crea la lista adapter
            Log.e(TAG,"adapter  null");
            adapter = new PickFlowListAdapter(getActivity(), rowsAsthma, this, recyclerView, true);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }
    }

    //ocultar flotingaction button
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (this.isVisible()) {
            if (isVisibleToUser) {

                PickFlowActivity.fab.setVisibility(View.VISIBLE);
            } else {
                PickFlowActivity.fab.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onItemClicked(View view, int position) {
        /*
        int idMenu = rowsAsthma.get(position).getId() ;
        Intent intent = new Intent(getActivity(), PickFlowRegistry.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("car", rowsAsthma.get(position) ) ;
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
        */

    }

    @Override
    public boolean onItemLongClicked(View v, int position) {

        return false;
    }

}
