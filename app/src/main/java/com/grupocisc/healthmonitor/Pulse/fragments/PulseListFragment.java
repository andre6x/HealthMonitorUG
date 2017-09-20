
package com.grupocisc.healthmonitor.Pulse.fragments;

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

import com.grupocisc.healthmonitor.HealthMonitorApplicattion;
import com.grupocisc.healthmonitor.Pulse.activities.PulseActivity;
import com.grupocisc.healthmonitor.Pulse.activities.PulseRegistyActivity;
import com.grupocisc.healthmonitor.Pulse.adapters.PulseListAdapter;
import com.grupocisc.healthmonitor.R;
import com.grupocisc.healthmonitor.Utils.Utils;
import com.grupocisc.healthmonitor.entities.ICunsulParamet;
import com.grupocisc.healthmonitor.entities.IPulse;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PulseListFragment extends Fragment implements PulseListAdapter.MyViewHolder.ClickListener{

    private String TAG = "PulseListFragment";
    private static List<ICunsulParamet.Objeto> rowsIPulse;
    private RecyclerView recyclerView;
    private PulseListAdapter adapter;
    public ProgressDialog Dialog;
    private Call<List<ICunsulParamet.Objeto>> call_1;
    private int year1, month1, day1, year2, month2, day2;
    private String Alerta = "";
    private static List<IPulse> rowsPulse;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Dialog = new ProgressDialog(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View contenView = inflater.inflate(R.layout.pulse_registre_fragment, container, false);
        recyclerView = (RecyclerView) contenView.findViewById(R.id.recycler_view);
        return contenView;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG,"onResume");
        selectDataPulseDB();
    }//obtener datos de la tabla BD
    public  void selectDataPulseDB(){
        Log.e(TAG,"selectDataPulseDB");
        try {
            //validar si en la tabla ahi datos mayor a 0
            if (Utils.GetPulseFromDatabase(HealthMonitorApplicattion.getApplication().getPulseDao() ).size() > 0 ){
                //asignamos datos de la tabla a la lista de objeto
                rowsPulse = Utils.GetPulseFromDatabase(HealthMonitorApplicattion.getApplication().getPulseDao() );
                //obtenemos el tamaño de la listaDEobjetos , para recorrerlo
                //y presenta los datos de la tabla bd en el LOG
                int tamaño = rowsPulse.size();
                for(int i = 0 ; i < tamaño ; i++){
                    Log.e(TAG,"Pulse:" + rowsPulse.get(i).getEnviadoServer() +"-" + rowsPulse.get(i).getConcentracion()+"/"+ rowsPulse.get(i)+" mm/Hg"+" -fecha:"+rowsPulse.get(i).getFecha() );//se cambia concentracion por peso
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

        //selectDataPulseDB();


    /*
    //obtener datos de la tabla BD
    public  void selectDataPulseDB(){
        Calendar now = Calendar.getInstance();
        int month = now.get(Calendar.MONTH)+1;
        String mes = month < 10 ? "0"+month : ""+month;
        Log.e(TAG,"selectDataPulseDB");
        try {
            //validar si en la tabla ahi datos mayor a 0
            if (Utils.GetIPulseFromDatabase(HealthMonitorApplicattion.getApplication().getPulseDao(),mes ).size() > 0 ){
                //asignamos datos de la tabla a la lista de objeto
                rowsIPulse = Utils.GetIPulseFromDatabase(HealthMonitorApplicattion.getApplication().getPulseDao(),mes );
                //obtenemos el tamaño de la listaDEobjetos , para recorrerlo
                //y presenta los datos de la tabla bd en el LOG
               int tamaño = rowsIPulse.size();
                for(int i = 0 ; i < tamaño ; i++){
                    Log.e(TAG,"Concentracion:" + rowsIPulse.get(i).getConcentracion() +" -fecha:"+rowsIPulse.get(i).getFecha() );
                }

                //setear el adaptador con los datos
                callsetAdapter();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
*/
    public  void callsetAdapter(){
        //validacion si se a iniciado el adapter
        if (adapter != null){
            //actuliza la data del apdater
            Log.e(TAG,"adapter != null");
            adapter.updateData(rowsPulse);

        }else {//es nulo
            //crea la lista adapter
            Log.e(TAG,"adapter  null");
            adapter = new PulseListAdapter(getActivity(), rowsPulse, this, recyclerView, true);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }
    }

    @Override
    public void onItemClicked(View view, int position) {
        int idMenu = rowsPulse.get(position).getId() ;
        Log.w(TAG, String.valueOf(idMenu));
        Intent intent = new Intent(getActivity(), PulseRegistyActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("car", rowsPulse.get(position) ) ;
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



   /*
        for(int i=0;i<mObjeto.size();i++){
            //
            ICunsulParamet.Objeto a = mObjeto.get(i);
            IPulse Pulse = new IPulse()  ;

             Pulse.setConcentracion(a.getValor()+"") ;
             Pulse.setFecha(a.getFecha());
             Pulse.setMedido(a.getMedio());
            Pulse.setObservacion(a.getObservacion());

           // rowsIPulse.add(Pulse);
            Log.e(TAG,Pulse.getConcentracion());
            Log.e(TAG,Pulse.getFecha()+"");
            Log.e(TAG,Pulse.getMedido()+"");
            Log.e(TAG,Pulse.getObservacion()+"");
        }


            if (mObjeto.getCodigoSalida() == 0 ) {
                //Utils.generarAlerta(LoginActivity.this, getString(R.string.txt_atencion), mLoginUser.getRespuesta() );
                //oka
                SavePreferencesCallMainActivity();
            }else {
                Utils.generarAlerta(LoginActivity.this, getString(R.string.txt_atencion), mLoginUser.getMensajeSalida() );
            }
        }else {
            Utils.generarAlerta(LoginActivity.this, getString(R.string.txt_atencion), getString(R.string.text_error_metodo));
        }
        */

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (this.isVisible()) {
            if (isVisibleToUser) {
                PulseActivity.principal.setVisibility(View.VISIBLE);
                PulseActivity.menu1.setVisibility(View.VISIBLE);
                PulseActivity.menu2.setVisibility(View.VISIBLE);

            } else {
                PulseActivity.principal.setVisibility(View.GONE);
                PulseActivity.menu1.setVisibility(View.GONE);
                PulseActivity.menu2.setVisibility(View.GONE);

            }
        }
    }
    @Override
    public boolean onItemLongClicked(View v, int position) {
        return false;
    }
}
