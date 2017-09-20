package com.grupocisc.healthmonitor.State.fragments;



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

import com.grupocisc.healthmonitor.Pressure.activities.PressureRegistyActivity;
import com.grupocisc.healthmonitor.State.activities.StateActivity;
import com.grupocisc.healthmonitor.State.activities.StateRegistyActivity;
import com.grupocisc.healthmonitor.State.adapters.StateListAdapter;
import com.grupocisc.healthmonitor.HealthMonitorApplicattion;
import com.grupocisc.healthmonitor.R;
import com.grupocisc.healthmonitor.Utils.Utils;
import com.grupocisc.healthmonitor.entities.IConsulState;
import com.grupocisc.healthmonitor.entities.IState;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StateListFragment extends Fragment implements StateListAdapter.MyViewHolder.ClickListener {

    private String TAG = "StateList";
    private static List<IState> rowsIState;
    private RecyclerView recyclerView;
    private StateListAdapter adapter;
   // public ProgressDialog Dialog;
   // private Call<List<IConsulState.Objeto>> call_1;
    private int year1, month1, day1, year2, month2, day2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Dialog = new ProgressDialog(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View contenView = inflater.inflate(R.layout.state_registre_fragment, container, false);
        recyclerView = (RecyclerView) contenView.findViewById(R.id.recycler_view);
        return contenView;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG,"onResume");
        selectDataStateDB();
        //if(Utils.isOnline(getActivity())) {
        //  restarLoading();
        //}
        //selectDataStateDB();
    }

    //obtener datos de la tabla BD
   public  void selectDataStateDB(){
        Log.e(TAG,"selectDataStateDB");
        try {
            //validar si en la tabla ahi datos mayor a 0
            if (Utils.GetStateFromDatabase(HealthMonitorApplicattion.getApplication().getStateDao() ).size() > 0 ){
                //asignamos datos de la tabla a la lista de objeto
                rowsIState = Utils.GetStateFromDatabase(HealthMonitorApplicattion.getApplication().getStateDao() );
                //obtenemos el tamaño de la listaDEobjetos , para recorrerlo
                //y presenta los datos de la tabla bd en el LOG
                int tamaño = rowsIState.size();
                for(int i = 0 ; i < tamaño ; i++){
                    Log.e(TAG,"fecha: "+rowsIState.get(i).getFecha() );
                }

                //setear el adaptador con los datos
                callsetAdapter();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public  void callsetAdapter(){
        //validacion si se a iniciado el adapter
        if (adapter != null){
            //actuliza la data del apdater
          //  Log.e(TAG,"adapter != null");
            adapter.updateData(rowsIState);

        }else {//es nulo
            //crea la lista adapter
            Log.e(TAG,"adapter  null");
            adapter = new StateListAdapter(getActivity(), rowsIState, this, recyclerView, true);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (this.isVisible()) {
            if (isVisibleToUser) {
                StateActivity.fab.setVisibility(View.VISIBLE);
            } else {
                StateActivity.fab.setVisibility(View.GONE);
            }
        }
    }

  /*  public void restarLoading(){
        showLoadingDialog();
        restartLoadingEnviarData();
    }*/

  /*  private void restartLoadingEnviarData( ){
        //obtener todos los datos del activity
        //obtener fechay hroa de Calendar
        Calendar c = Calendar.getInstance();
        year1 = c.get(Calendar.YEAR);
        month1 = c.get(Calendar.MONTH);
        String mes1 = "" + ((month1 + 1) < 10 ? "0" + (month1 + 1) : "" + (month1 + 1));
        String dia1 = "01";

        //setear fecha_desde
        String fechaDesde = "" + year1  + "/" + mes1 + "/" + dia1;

        //setear fecha_hasta

        year2 = c.get(Calendar.YEAR);
        month2 = c.get(Calendar.MONTH);
        day2 = c.getActualMaximum(Calendar.DAY_OF_MONTH);
        String mes2 = "" + ((month2 + 1) < 10 ? "0" + (month2 + 1) : "" + (month2 + 1));
        String dia2 = "" + day2;
        String fechaHasta = "" + year1  + "/" + mes2 + "/" + dia2;

        String email    = Utils.getEmailFromPreference(getActivity()) ;

        //enviar webservice
        //APUNTANDO AA METODO CISC  mRestCISCAdapter
        IConsulState ConsulState = HealthMonitorApplicattion.getApplication().getmRestCISCAdapterAnimo().create(IConsulState.class);
        call_1 = ConsulState.ConsulState(email,fechaDesde,fechaHasta );
        call_1.enqueue(new Callback<List<IConsulState.Objeto>>() {
            @Override
            public void onResponse(Call<List<IConsulState.Objeto>> call, Response<List<IConsulState.Objeto>> response) {
                if (response.isSuccess()) {
                    Log.e(TAG, "Respuesta exitosa");
                    rowsIState = null;
                    rowsIState = response.body();
                    postExecutionLogin();
                } else {
                    showLayoutDialog();
                    Utils.generarAlerta(getActivity(), getString(R.string.txt_atencion), getString(R.string.text_error_metodo));
                  //  Log.e(TAG, "Error en la petición");
                }
            }

            @Override
            public void onFailure(Call<List<IConsulState.Objeto>> call, Throwable t) {
                showLayoutDialog();
                Utils.generarAlerta(getActivity(), getString(R.string.txt_atencion), getString(R.string.text_error_metodo ) + " o revise su conexión a internet");
                t.printStackTrace();
            //    Log.e(TAG, "mmmmmmmmmmmm");
            }
        });
    }*/

    /*private void showLoadingDialog(){
        Dialog.setMessage("Espere un Momento..");
        Dialog.setCancelable(false);
        Dialog.show();
    }*/

   /* public void postExecutionLogin(){
        showLayoutDialog();

        if (rowsIState != null) {
            Log.e(TAG, "ooooook" + rowsIState.size());
            if (rowsIState.size() > 0)
            { callsetAdapter();}

        }*/

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
    //}

   /* private void showLayoutDialog(){
        if (Dialog != null)
            Dialog.dismiss();
    }*/

    @Override
    public void onItemClicked(View v, int position) {
        int idMenu = rowsIState.get(position).getId() ;
        Intent intent = new Intent(getActivity(), StateRegistyActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("car", rowsIState.get(position) ) ;
        intent.putExtras(bundle);

        // TRANSITIONS
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            View card = v.findViewById(R.id.main_card1);


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
