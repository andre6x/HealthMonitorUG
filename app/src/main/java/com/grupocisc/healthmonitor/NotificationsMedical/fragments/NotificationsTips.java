package com.grupocisc.healthmonitor.NotificationsMedical.fragments;

import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.grupocisc.healthmonitor.HealthMonitorApplicattion;
import com.grupocisc.healthmonitor.NotificationsMedical.adapters.TipsMedicalAdapter;
import com.grupocisc.healthmonitor.R;
import com.grupocisc.healthmonitor.entities.IPushNotification;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Gems on 29/01/2017.
 */

public class NotificationsTips extends Fragment {

    private  String TAG = "NotificationsTips";
	private List<IPushNotification.TipsMensajes> rowsTips;
    private RecyclerView recyclerView;
    private TipsMedicalAdapter adapter;
    protected SwipeRefreshLayout mSwipeRefreshLayout;
    Call<List<IPushNotification.TipsMensajes>> call_1;

    //INCLUDE
    private LinearLayout linear_loading , lyt_loading;
    private ProgressBar progress;
    private Button retry;

    View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState){
        view = inflater.inflate(R.layout.notifications_tips_fragment , viewGroup, false);
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

        return view;
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

    private void showRetry() {
        if (getActivity() != null){
            lyt_loading.setVisibility(View.VISIBLE);
            linear_loading.setVisibility(View.VISIBLE);
            progress.setVisibility(View.GONE);
            retry.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onResume(){
        super.onResume();
        Log.e(TAG,"onResume");

        restarLoading();
    }

    public void restarLoading(){
        showLoading();
        try{
            int id_enfermedad = 1; // diabetes
            //Pregunta si existen datos
            IPushNotification notifiMensajes = HealthMonitorApplicattion.getApplication().getRetrofitAdapter().create(IPushNotification.class);
            call_1 = notifiMensajes.getTips(id_enfermedad );
            call_1.enqueue(new Callback<List<IPushNotification.TipsMensajes>>() {
                @Override
                public void onResponse(Call<List<IPushNotification.TipsMensajes>> call, Response<List<IPushNotification.TipsMensajes>> response) {
                    if(response.isSuccessful()) {
                        Log.e(TAG, "Tips Respuesta exitosa");
                        rowsTips = response.body();
                        if (!rowsTips.isEmpty()) {
                            //setear el adaptador con los datos
                            callsetAdapter();
                        }
                    }
                    else
                    {    showRetry();
                        Log.e(TAG, "Tips Error en la petición");
                    }
                }

                @Override
                public void onFailure(Call<List<IPushNotification.TipsMensajes>> call, Throwable t) {
                    showRetry();
                    t.printStackTrace();
                }
            });

        }catch(Exception e)
        {
            Log.e(TAG, "Error" + e.toString());
        }
    }

    public void callsetAdapter(){
        showLayout();
        if(adapter != null)
        {
            Log.e(TAG,"adapter!=null");
            adapter.updateData(rowsTips);
        }else
        {
            //Crea la lista
            Log.e(TAG, "adapter null");
            adapter = new TipsMedicalAdapter(this, rowsTips);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
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
