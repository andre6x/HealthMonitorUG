package com.grupocisc.healthmonitor.Glucose.fragments;

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
import com.grupocisc.healthmonitor.Complementary.activities.ComplCholesterolRegistyActivity;
import com.grupocisc.healthmonitor.Complementary.adapters.ComplCholesterolListAdapter;
import com.grupocisc.healthmonitor.Glucose.activities.GlucoseRegistyActivity;
import com.grupocisc.healthmonitor.Glucose.adapters.GlucoseListAdapter;
import com.grupocisc.healthmonitor.HealthMonitorApplicattion;
import com.grupocisc.healthmonitor.R;
import com.grupocisc.healthmonitor.Utils.Utils;
import com.grupocisc.healthmonitor.entities.IGlucose;
import java.sql.SQLException;
import java.util.List;

public class GlucoseListFragment extends Fragment implements GlucoseListAdapter.MyViewHolder.ClickListener {

    private String TAG = "GlucoseList";
    private RecyclerView recyclerView;
    private GlucoseListAdapter adapter;
    private static List<IGlucose> rowsGlucose;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View contenView = inflater.inflate(R.layout.glucose_registre_fragment, container, false);
        recyclerView = (RecyclerView) contenView.findViewById(R.id.recycler_view);
        return contenView;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG,"onResume");
        selectDataGlucoseDB();
    }

    //obtener datos de la tabla BD
    public  void selectDataGlucoseDB(){
        Log.e(TAG,"selectDataGlucoseDB");
        try {
            //validar si en la tabla ahi datos mayor a 0
            if (Utils.GetGlucoseFromDatabase(HealthMonitorApplicattion.getApplication().getGlucoseDao() ).size() > 0 ){
                //asignamos datos de la tabla a la lista de objeto
                rowsGlucose = Utils.GetGlucoseFromDatabase(HealthMonitorApplicattion.getApplication().getGlucoseDao() );
                //obtenemos el tamaño de la listaDEobjetos , para recorrerlo
                //y presenta los datos de la tabla bd en el LOG
                int tamaño = rowsGlucose.size();
                for(int i = 0 ; i < tamaño ; i++){
                    Log.e(TAG,"Glucosa:" + rowsGlucose.get(i).getEnviadoServer() +"-" + rowsGlucose.get(i).getConcentracion()+"/"+" -fecha:"+rowsGlucose.get(i).getFecha() );
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
            adapter.updateData(rowsGlucose);

        }else {//es nulo
            //crea la lista adapter
            Log.e(TAG,"adapter  null");
            adapter = new GlucoseListAdapter(getActivity(), rowsGlucose, this, recyclerView, true);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }
    }


    @Override
    public void onItemClicked(View view, int position) {
        int idMenu = rowsGlucose.get(position).getId() ;
        Intent intent = new Intent(getActivity(), GlucoseRegistyActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("car", rowsGlucose.get(position) ) ;
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

    @Override
    public boolean onItemLongClicked(View v, int position) {
        return false;
    }
}
