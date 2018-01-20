package com.grupocisc.healthmonitor.Insulin.fragments;

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

import com.grupocisc.healthmonitor.Insulin.activities.InsulinActivity;
import com.grupocisc.healthmonitor.Insulin.activities.InsulinRegistry;
import com.grupocisc.healthmonitor.Insulin.adapters.InsulinListAdapter;
import com.grupocisc.healthmonitor.HealthMonitorApplicattion;
import com.grupocisc.healthmonitor.R;
import com.grupocisc.healthmonitor.Utils.Utils;
import com.grupocisc.healthmonitor.entities.EInsulin;
import com.grupocisc.healthmonitor.entities.ICunsulParamet;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.sql.SQLException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.grupocisc.healthmonitor.Utils.Utils.isOnline;
import static com.grupocisc.healthmonitor.Utils.Utils.isOnlineNet;

/**
 * Created by Gema on 10/01/2017.
 */

public class InsulinListFragment extends Fragment implements InsulinListAdapter.ViewHolder.ClickListener  {

    private static final String TAG = "[InsulinListFragment]";
    private String user_email;
    private static List<ICunsulParamet.Objeto> rowsEInsulin;
    private static List<EInsulin> lstEInsulin;

    private RecyclerView recyclerView;
    private InsulinListAdapter adapter;
    public ProgressDialog Dialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        String Method ="[onCreate]";
        Log.i(TAG, Method +  "Init..."  );
        super.onCreate(savedInstanceState);
        user_email = Utils.getEmailFromPreference(this.getContext());
        Dialog = new ProgressDialog(getActivity());
        Log.i(TAG, Method +  "End..."  );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState){
        String Method ="[onCreateView]";
        Log.i(TAG, Method +  "Init..."  );
        View view = inflater.inflate(R.layout.insulin_register_fragment , viewGroup, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        Log.i(TAG, Method +  "End..."  );
        return view;
    }

    @Override
    public void onResume(){
        String Method ="[onResume]";
        Log.i(TAG, Method +  "Init..."  );
        super.onResume();

        if( true )//if(isOnline(getActivity()))
            restarLoading();
        Log.i(TAG, Method +  "End..."  );
    }

    public void restarLoading(){
        String Method ="[restarLoading]";
        Log.i(TAG, Method +  "Init..."  );
        //showLoadingDialog();
        //selectDataInsulinDB(user_email, "insulina");
        selectDataInsulinDBLocal(user_email, "insulina");
        Log.i(TAG, Method +  "End..."  );
    }

    public void selectDataInsulinDBLocal(String email, String parametro)  {
        String Method ="[selectDataInsulinDBLocal]";;

        try {
            lstEInsulin = Utils.GetInsulinFromDatabase( HealthMonitorApplicattion.getApplication().getInsulinDao() );
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (lstEInsulin != null){
            postExecutionQueryDBLocal();
        }
        else {
            showLayoutDialog();
            Utils.generarAlerta(getActivity(), getString(R.string.txt_atencion), getString(R.string.text_error_metodo));

        }

        Log.i(TAG, Method +  "End..."  );
    }

    public void selectDataInsulinDB(String email, String parametro){
        Calendar c = Calendar.getInstance();
        String mes = (c.get(Calendar.MONTH) + 1) < 10 ? "0" + (c.get(Calendar.MONTH) + 1) : "" + (c.get(Calendar.MONTH) + 1) ;
        String dia = c.get(Calendar.DAY_OF_MONTH) < 10 ? "0" + (c.get(Calendar.DAY_OF_MONTH) + 1) : "" + (c.get(Calendar.DAY_OF_MONTH) + 1) ;
        String anio = "" + c.get(Calendar.YEAR);
        String hora = "" + c.get(Calendar.HOUR_OF_DAY);
        String minuto =  "" + c.get(Calendar.MINUTE);
        String fechaIni = "1900/01/01 00:00:00"; //anio +"/" + mes +"/" + "01";
        String fechaFin = anio +"/" + mes +"/" + dia + " " + hora +":"+minuto+":00" ;

        //obtener datos de la tabla
        try{
            //Pregunta si existen datos
            ICunsulParamet CunsulParamet = HealthMonitorApplicattion.getApplication().getRetrofitAdapter().create(ICunsulParamet.class);
            Call<List<ICunsulParamet.Objeto>> call_1 = CunsulParamet.CunsulParamet(email, parametro, fechaIni , fechaFin);
            call_1.enqueue(new Callback<List<ICunsulParamet.Objeto>>() {
                @Override
                public void onResponse(Call<List<ICunsulParamet.Objeto>> call, Response<List<ICunsulParamet.Objeto>> response) {
                    if (response.isSuccessful()) {
                        rowsEInsulin = null;
                        rowsEInsulin = response.body();
                        postExecutionLogin();
                    } else {
                        showLayoutDialog();
                        Utils.generarAlerta(getActivity(), getString(R.string.txt_atencion), getString(R.string.text_error_metodo));
                    }
                }

                @Override
                public void onFailure(Call<List<ICunsulParamet.Objeto>> call, Throwable t) {
                    showLayoutDialog();
                    Utils.generarAlerta(getActivity(), getString(R.string.txt_atencion), getString(R.string.text_error_metodo ) + " o revise su conexión a internet");
                    t.printStackTrace();
                }
            });

        }catch(Exception e)
        {
            e.printStackTrace();
        }

    }

    public void postExecutionQueryDBLocal() {
        String Method ="[postExecutionQueryDBLocal]";
        Log.i(TAG, Method + "Init..."  );
        showLayoutDialog();
        if (lstEInsulin != null)
            if (lstEInsulin.size() > 0)
                callSetAdapter();
        Log.i(TAG, Method + "Init..."  );
    }

    public void postExecutionLogin() {
        showLayoutDialog();

        if (rowsEInsulin != null) {
            if (rowsEInsulin.size() > 0) {
                callsetAdapter();
            }

        }
    }

    private void showLoadingDialog(){
        String Method ="[showLoadingDialog]";
        Log.i(TAG, Method +  "Init..."  );
        //Dialog.setMessage("Espere un Momento..");
        Dialog.setMessage(getResources().getString(R.string.msg_PleaseWait));
        Dialog.setCancelable(false);
        Dialog.show();
        Log.i(TAG, Method +  "End..."  );
    }

    private void showLayoutDialog(){
        String Method ="[showLayoutDialog]";
        Log.i(TAG, Method +  "Init..."  );
        if (Dialog != null)
            Dialog.dismiss();
        Log.i(TAG, Method +  "End..."  );
    }

    public void callsetAdapter(){
        if(adapter != null) {
            adapter.updateDatos(rowsEInsulin);
        }else {
            //Crea la lista
            //adapter = new InsulinListAdapter(this, rowsEInsulin);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }
    }

    public void callSetAdapter(){
        String Method ="[callSetAdapter]";
        Log.i(TAG, Method + "Init..."  );
        if(adapter != null) {
            adapter.updateData(lstEInsulin);
        }else {
            //Crea la lista
            adapter = new InsulinListAdapter(getActivity(), lstEInsulin , this,  recyclerView  , true);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }
        Log.i(TAG, Method + "Init..."  );
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        String Method ="[setUserVisibleHint]";
        Log.i(TAG, Method +  "Init..."  );
        super.setUserVisibleHint(isVisibleToUser);
        if (this.isVisible()) {
            if (isVisibleToUser) {
                InsulinActivity.fab.setVisibility(View.VISIBLE);
            } else {
                InsulinActivity.fab.setVisibility(View.GONE);
            }
        }
        Log.i(TAG, Method +  "Init..."  );
    }

    @Override
    public void onItemClicked(View view, int position) {
        int idMenu = lstEInsulin.get(position).getId() ;
        Log.i(TAG, "onItemClicked: Insuline id=" + lstEInsulin.get(position).getId());
        Log.i(TAG, "onItemClicked: Insuline Units=" + lstEInsulin.get(position).getInsulina());
        Log.i(TAG, "onItemClicked: Insuline Fecha=" + lstEInsulin.get(position).getFecha());
        Log.i(TAG, "onItemClicked: Insuline Obs=" + lstEInsulin.get(position).getObservacion() );
        Intent intent = new Intent(getActivity(), InsulinRegistry.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("car", lstEInsulin.get(position) ) ;
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
