package com.grupocisc.healthmonitor.Complementary.fragments;

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


import com.grupocisc.healthmonitor.Complementary.activities.ComplHba1cRegistyActivity;
import com.grupocisc.healthmonitor.Complementary.adapters.ComplHba1cListAdapter;
import com.grupocisc.healthmonitor.HealthMonitorApplicattion;
import com.grupocisc.healthmonitor.R;
import com.grupocisc.healthmonitor.Utils.Utils;

import com.grupocisc.healthmonitor.entities.ICunsulParamet;
import com.grupocisc.healthmonitor.entities.IHba1c;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ComplHba1cListFragment extends Fragment implements ComplHba1cListAdapter.MyViewHolder.ClickListener {

    private String TAG = "Complementary";
    private static List<ICunsulParamet.Objeto> rowsIComplementary;
    private RecyclerView recyclerView;
    private ComplHba1cListAdapter adapter;
    public ProgressDialog Dialog;
    private Call<List<ICunsulParamet.Objeto>> call_1;
    private String Alerta = "";
    private static List<IHba1c> rowsHba1c;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Dialog = new ProgressDialog(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View contenView = inflater.inflate(R.layout.compl_hba1c_registre_fragment, container, false);
        recyclerView = (RecyclerView) contenView.findViewById(R.id.recycler_view);
        return contenView;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG,"onResume");
        selectDataHba1cDB();
    }
    //obtener datos de la tabla BD
    public  void selectDataHba1cDB(){
        Log.e(TAG,"selectDataHba1cDB");
        try {
            //validar si en la tabla ahi datos mayor a 0
            if (Utils.GetHba1cFromDatabase(HealthMonitorApplicattion.getApplication().getHba1cDao() ).size() > 0 ){
                //asignamos datos de la tabla a la lista de objeto
                rowsHba1c = Utils.GetHba1cFromDatabase(HealthMonitorApplicattion.getApplication().getHba1cDao() );
                //obtenemos el tamaño de la listaDEobjetos , para recorrerlo
                //y presenta los datos de la tabla bd en el LOG
                int tamaño = rowsHba1c.size();
                for(int i = 0 ; i < tamaño ; i++){
                    Log.e(TAG,"Hba1c:" + rowsHba1c.get(i).getEnviadoServer() +"-" + rowsHba1c.get(i).getConcentracion()+"/"+ rowsHba1c.get(i).getFecha());
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



    public void callsetAdapter() {
        //validacion si se a iniciado el adapter
        if (adapter != null) {
            //actuliza la data del apdater
            adapter.updateData(rowsHba1c);

        } else {//es nulo
            //crea la lista adapter
            Log.e(TAG,"adapter  null");
            adapter = new ComplHba1cListAdapter(getActivity(), rowsHba1c, this, recyclerView, true);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }
    }


  /*  public void restarLoading() {
        showLoadingDialog();
        restartLoadingEnviarData();
    }

    private void restartLoadingEnviarData() {
        Calendar c = Calendar.getInstance();
        String mes = (c.get(Calendar.MONTH) + 1) < 10 ? "0" + (c.get(Calendar.MONTH) + 1) : "" + (c.get(Calendar.MONTH) + 1);
        String dia = c.get(Calendar.DAY_OF_MONTH) < 10 ? "0" + (c.get(Calendar.DAY_OF_MONTH) + 1) : "" + (c.get(Calendar.DAY_OF_MONTH) + 1);
        String anio = "" + c.get(Calendar.YEAR);
        String hora = "" + c.get(Calendar.HOUR_OF_DAY);
        String minuto = "" + c.get(Calendar.MINUTE);

        String fechaIni = "1900/01/01 00:00:00"; //anio +"/" + mes +"/" + "01";
        String fechaFin = anio + "/" + mes + "/" + dia + " " + hora + ":" + minuto + ":59";

        String user_email = Utils.getEmailFromPreference(getActivity());
        String parametro = "HBA1C";

        if (Utils.isOnline(getActivity())) {
            //enviar webservice
            //APUNTANDO AA METODO CISC
            ICunsulParamet CunsulParamet = HealthMonitorApplicattion.getApplication().getmRestCISCAdapter().create(ICunsulParamet.class);
            call_1 = CunsulParamet.CunsulParamet(user_email, parametro, fechaIni, fechaFin);
            call_1.enqueue(new Callback<List<ICunsulParamet.Objeto>>() {
                @Override
                public void onResponse(Call<List<ICunsulParamet.Objeto>> call, Response<List<ICunsulParamet.Objeto>> response) {
                    Alerta = "";
                    if (response.isSuccess()) {
                        rowsIComplementary = null;
                        rowsIComplementary = response.body();
                        //  Collections.sort(rowsIComplementary);
                        postExecutionLogin();
                    } else {
                        showLayoutDialog();
                        Utils.generarAlerta(getActivity(), getString(R.string.txt_atencion), getString(R.string.text_error_metodo));
                        Alerta = "A";
                    }
                }

                @Override
                public void onFailure(Call<List<ICunsulParamet.Objeto>> call, Throwable t) {
                    showLayoutDialog();
                    if (Alerta == "") {

                        Utils.generarAlerta(getActivity(), getString(R.string.txt_atencion), getString(R.string.text_error_metodo) + " o revise su conexión a internet");
                        t.printStackTrace();
                    }
                }
            });
        }
    }

    public void postExecutionLogin() {
        showLayoutDialog();

        if (rowsIComplementary != null) {
            if (rowsIComplementary.size() > 0) {
                callsetAdapter();
            }

        }

    }

    private void showLoadingDialog() {
        Dialog.setMessage("Espere un Momento..");
        Dialog.setCancelable(false);
        Dialog.show();
    }

    private void showLayoutDialog() {
        if (Dialog != null)
            Dialog.dismiss();
    }*/



    @Override
    public void onItemClicked(View view, int position) {
        int idMenu = rowsHba1c.get(position).getId() ;
        Intent intent = new Intent(getActivity(), ComplHba1cRegistyActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("car", rowsHba1c.get(position) ) ;
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