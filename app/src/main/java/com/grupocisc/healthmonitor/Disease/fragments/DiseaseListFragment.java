package com.grupocisc.healthmonitor.Disease.fragments;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.grupocisc.healthmonitor.Disease.adapters.DiseaseListAdapter;
import com.grupocisc.healthmonitor.HealthMonitorApplicattion;
import com.grupocisc.healthmonitor.R;
import com.grupocisc.healthmonitor.Utils.Utils;
import com.grupocisc.healthmonitor.entities.IDiseasePac;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Gema on 10/01/2017.
 */

public class DiseaseListFragment extends Fragment {

    private  String TAG = "DiseaseListFragment";
    private static List<IDiseasePac.Disease> rowsIDisease;
    private RecyclerView recyclerView;
    private DiseaseListAdapter adapter;
    private Call<List<IDiseasePac.Disease>> call_1;
    private int year1, month1, day1, year2, month2, day2;
    public ProgressDialog Dialog;

    private LinearLayout linear_loading , lyt_loading;
    private ProgressBar progress;
    private Button retry;

    String user_email = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Dialog = new ProgressDialog(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.disease_register_fragment , viewGroup, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        lyt_loading    = (LinearLayout) view.findViewById(R.id.lyt_loading);
        linear_loading = (LinearLayout) view.findViewById(R.id.linear_loading);
        progress       = (ProgressBar)  view.findViewById(R.id.progress);
        retry          = (Button)       view.findViewById(R.id.retry);
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                restarLoading();
            }
        });

        user_email = Utils.getEmailFromPreference(getActivity());

        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
        restarLoading();
    }

    /*
    public void selectDataInsulinDB(){
        //obtener datos de la tabla
        try{
            //Pregunta si existen datos
            if (Utils.GetDiseaseFromDatabase(HealthMonitorApplicattion.getApplication().getDiseaseDao()).size() > 0){
                //Si existen, asignamos los datos de la tabla a la lista de objetos
                rowsIDisease = Utils.GetDiseaseFromDatabase(HealthMonitorApplicattion.getApplication().getDiseaseDao());
                int cont = 0 , ListSize = rowsIDisease.size();
                do{
                    Log.e(TAG,"Dosis:" + rowsIDisease.get(cont).getDisease()+" -fecha:"+rowsIDisease.get(cont).getFecha() );
                    cont++;
                }while (cont < ListSize);
                //setear el adaptador con los datos
                callsetAdapter();
            }
        }catch(Exception e)
        {
            Log.e(TAG, "Error" + e.toString());
        }
    }
    */

    public void postExecutionLogin(){
        showLayout();
        if (rowsIDisease != null && rowsIDisease.size() > 0) {
            callsetAdapter();
        }else{
            showRetry();
        }
    }

    public void callsetAdapter(){
        if(adapter != null)
        {
            Log.e(TAG,"adapter!=null");
            adapter.updateData(rowsIDisease);
        }else
        {
            //Crea la lista
            Log.e(TAG, "adapter null");
            adapter = new DiseaseListAdapter(getActivity(), rowsIDisease);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }
    }

    public void restarLoading(){
        showLoading();
        restartLoadingEnviarData();
    }

    private void restartLoadingEnviarData( ) {
        //enviar webservice
        IDiseasePac DiseasePac = HealthMonitorApplicattion.getApplication().getRestCISCAdapterV2IP().create(IDiseasePac.class);
        call_1 = DiseasePac.DiseasePac(user_email);
        call_1.enqueue(new Callback<List<IDiseasePac.Disease>>() {
            @Override
            public void onResponse(Call<List<IDiseasePac.Disease>> call, Response<List<IDiseasePac.Disease>> response) {
                if (response.isSuccessful()) {
                    rowsIDisease = null;
                    rowsIDisease = response.body();
                    Collections.sort(rowsIDisease);
                    postExecutionLogin();
                } else {
                    showRetry();
                }
            }
            @Override
            public void onFailure(Call<List<IDiseasePac.Disease>> call, Throwable t) {
                showRetry();
                t.printStackTrace();

            }
        });

    }

    private void showLayout( ) {
        if (getActivity() != null) {
            lyt_loading.setVisibility(View.GONE);
            linear_loading.setVisibility(View.GONE);
        }
    }

    private void showLoading() {
        if (getActivity() != null) {
            lyt_loading.setVisibility(View.VISIBLE);
            linear_loading.setVisibility(View.VISIBLE);
            progress.setVisibility(View.VISIBLE);
            retry.setVisibility(View.GONE);
        }
    }

    private void showRetry( ) {
        if (getActivity() != null){
            lyt_loading.setVisibility(View.VISIBLE);
            linear_loading.setVisibility(View.VISIBLE);
            progress.setVisibility(View.GONE);
            retry.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(call_1!=null && !call_1.isCanceled()) {
            call_1.cancel();
        }
    }


}
