package com.grupocisc.healthmonitor.NotificationsMedical.fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
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

import com.grupocisc.healthmonitor.HealthMonitorApplicattion;
import com.grupocisc.healthmonitor.NotificationsMedical.adapters.NotificationsMedicalListAdapter;
import com.grupocisc.healthmonitor.R;
import com.grupocisc.healthmonitor.Utils.SharedPreferencesManager;
import com.grupocisc.healthmonitor.Utils.Utils;
import com.grupocisc.healthmonitor.entities.IPushNotification;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Gema on 10/01/2017.
 */

public class NotificationsMedical extends Fragment {

    private String email;
    private static List<IPushNotification.NotifiMensajes> rowsINotifiMedi;
    private RecyclerView recyclerView;
    private NotificationsMedicalListAdapter adapter;
    Call<List<IPushNotification.NotifiMensajes>> call_1;

    String token;

    private LinearLayout linear_loading , lyt_loading;
    private ProgressBar progress;
    private Button retry;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        email = Utils.getEmailFromPreference(this.getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.medicine_register_fragment , viewGroup, false);

        token = SharedPreferencesManager.getValorEsperado(this.getActivity(), getString(R.string.preferencias_inicio), "gcmToken");

        lyt_loading    = (LinearLayout) view.findViewById(R.id.lyt_loading);
        linear_loading = (LinearLayout) view.findViewById(R.id.linear_loading);
        progress       = (ProgressBar)  view.findViewById(R.id.progress);
        retry          = (Button)       view.findViewById(R.id.retry);
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectDataNotifiDB(token);
            }
        });

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        selectDataNotifiDB(token);

        return view;
    }

    @Override
    public void onResume(){
        super.onResume();



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

    public void selectDataNotifiDB(String token){
        try {
            showLoading();
            IPushNotification notifiMensajes = HealthMonitorApplicattion.getApplication().getRestCISCAdapterV2IP().create(IPushNotification.class);
            call_1 = notifiMensajes.getPushNotification_3(token, email);
            call_1.enqueue(new Callback<List<IPushNotification.NotifiMensajes>>() {
                @Override
                public void onResponse(Call<List<IPushNotification.NotifiMensajes>> call, Response<List<IPushNotification.NotifiMensajes>> response) {
                    if(response.isSuccessful()) {
                        rowsINotifiMedi = response.body();
                        showLayout();
                        if (rowsINotifiMedi  != null && rowsINotifiMedi.size() > 0) {
                            Collections.reverse(rowsINotifiMedi);
                            callsetAdapter();
                        }else{
                            menssageTextVisible();
                            showRetry();
                        }
                    }else{
                        showRetry();
                    }
                }

                @Override
                public void onFailure(Call<List<IPushNotification.NotifiMensajes>> call, Throwable t) {
                    showRetry();
                    t.printStackTrace();
                }
            });
            }catch(Exception e)
            {
                e.printStackTrace();
            }


    }

    public  void menssageTextVisible(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this.getActivity());
        alert.setTitle("" + getString(R.string.txt_atencion));
        alert.setMessage("" + getString(R.string.txt_mensaje_noti));
        alert.setCancelable(false);
        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface alert, int id) {

            }
        });
        alert.show();
    }
    public void callsetAdapter(){
        if(adapter != null) {
            adapter.updateData(rowsINotifiMedi);
        }else {
            //Crea la lista
            adapter = new NotificationsMedicalListAdapter(this, rowsINotifiMedi);
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
