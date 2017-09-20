package com.grupocisc.healthmonitor.Complementary.fragments;

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
import com.grupocisc.healthmonitor.Complementary.activities.ComplActivity;
import com.grupocisc.healthmonitor.Complementary.activities.ComplCholesterolRegistyActivity;
import com.grupocisc.healthmonitor.Complementary.adapters.ComplCholesterolListAdapter;
import com.grupocisc.healthmonitor.HealthMonitorApplicattion;
import com.grupocisc.healthmonitor.R;
import com.grupocisc.healthmonitor.Utils.Utils;
import com.grupocisc.healthmonitor.entities.IColesterol;
import java.sql.SQLException;
import java.util.List;

public class ComplCholesterolListFragment extends Fragment implements ComplCholesterolListAdapter.MyViewHolder.ClickListener {

    private String TAG = "ColesterolList";
    private RecyclerView recyclerView;
    private ComplCholesterolListAdapter adapter;
    private static List<IColesterol> rowsColesterol;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View contenView = inflater.inflate(R.layout.compl_cholesterol_registre_fragment, container, false);
        recyclerView = (RecyclerView) contenView.findViewById(R.id.recycler_view);
        return contenView;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG,"onResume");
        selectDataColesterolDB();
    }

    //obtener datos de la tabla BD
    public  void selectDataColesterolDB(){
        Log.e(TAG,"selectDataColesterolDB");
        try {
            //validar si en la tabla ahi datos mayor a 0
            if (Utils.GetColesterolFromDatabase(HealthMonitorApplicattion.getApplication().getColesterolDao() ).size() > 0 ){
                //asignamos datos de la tabla a la lista de objeto
                rowsColesterol = Utils.GetColesterolFromDatabase(HealthMonitorApplicattion.getApplication().getColesterolDao() );
                //obtenemos el tamaño de la listaDEobjetos , para recorrerlo
                //y presenta los datos de la tabla bd en el LOG
                int tamaño = rowsColesterol.size();
                for(int i = 0 ; i < tamaño ; i++){
                    Log.e(TAG,"Colesterol:" + rowsColesterol.get(i).getEnviadoServer() +"-" + rowsColesterol.get(i).getColesterol()+"/"+ rowsColesterol.get(i).getHdl()+"/"+ rowsColesterol.get(i).getLdl()+" -fecha:"+rowsColesterol.get(i).getFecha() );
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
            adapter.updateData(rowsColesterol);

        }else {//es nulo
            //crea la lista adapter
            Log.e(TAG,"adapter  null");
            adapter = new ComplCholesterolListAdapter(getActivity(), rowsColesterol, this, recyclerView, true);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }
    }

    /*
    //ocultar flotingaction button
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (this.isVisible()) {
            if (isVisibleToUser) {
                ComplActivity.fab.setVisibility(View.VISIBLE);
            } else {
                ComplActivity.fab.setVisibility(View.GONE);
            }
        }
    }

*/
    @Override
    public void onItemClicked(View view, int position) {
        int idMenu = rowsColesterol.get(position).getId() ;
        Intent intent = new Intent(getActivity(), ComplCholesterolRegistyActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("car", rowsColesterol.get(position) ) ;
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
