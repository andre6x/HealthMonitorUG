package com.grupocisc.healthmonitor.Insulin.fragments;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.grupocisc.healthmonitor.Insulin.adapters.InsulinRecommendationsAdapter;
import com.grupocisc.healthmonitor.R;
import com.grupocisc.healthmonitor.entities.IPushNotification;
import com.grupocisc.healthmonitor.Utils.Utils;
import com.grupocisc.healthmonitor.HealthMonitorApplicattion;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class InsulinRecommendationsFragment extends Fragment  {

    private String TAG = "InsulinRecoFragment";
    private RecyclerView recyclerView;
    private InsulinRecommendationsAdapter adapter;
    private static List<IPushNotification.Recommendation> rowsRecommendations;
    private IPushNotification.RecommendationRequest recomendacionRequest;
    Call<IPushNotification.RecommendationRequest> call_1;

    //private OnFragmentInteractionListener mListener;

    public InsulinRecommendationsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View contentView = inflater.inflate(R.layout.fragment_recomendations, container, false);
        recyclerView = (RecyclerView) contentView.findViewById(R.id.recycler_view);
        rowsRecommendations = new ArrayList<>();
        IPushNotification.Recommendation r1 = new IPushNotification.Recommendation();
        r1.content = "Su nivel de insulina está dentro de los parámetros establecidos";
        //r1.title = "Filtrado de contenido";

        IPushNotification.Recommendation r2 = new IPushNotification.Recommendation();
        r2.content = "Debe evitar comidas con mucha azucar";
        //r2.title = "Filtrado de contenido";


        rowsRecommendations.add(r1);
        rowsRecommendations.add(r2);
        selectRecomendationsDB();
        return contentView;
    }

    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }

    public  void selectRecomendationsDB(){

        try {
            //validar si en la tabla ahi datos mayor a 0



            String usuario = "";
            if(Utils.getAsmaFromPreference(getActivity())!=null) {
                usuario = Utils.getEmailFromPreference(getActivity());
            }



                IPushNotification notifiMensajes = HealthMonitorApplicattion.getApplication().getRetrofitAdapter().create(IPushNotification.class);

                call_1 = notifiMensajes.getRecommendations(new IPushNotification.ParamRequest(usuario,4));
                call_1.enqueue(new Callback<IPushNotification.RecommendationRequest>() {
                    @Override
                    public void onResponse(Call<IPushNotification.RecommendationRequest> call, Response<IPushNotification.RecommendationRequest> response) {
                        if(response.isSuccessful()) {
                            Log.e(TAG, "Tips Respuesta exitosa");
                            recomendacionRequest = response.body();
                            if (recomendacionRequest != null) {
                                if(recomendacionRequest.rows!=null){
                                    if(recomendacionRequest.rows.size() > 0 ){
                                        int idx=0;
                                        for (IPushNotification.rows registro : recomendacionRequest.rows) {
                                            idx++;
                                            IPushNotification.Recommendation rowIngresa  = new IPushNotification.Recommendation();
                                            rowIngresa.id = idx;
                                            rowIngresa.content = registro.recommendations;
                                            rowsRecommendations.add(rowIngresa);
                                        }
                                        //showLayout();
                                        //setear el adaptador con los datos
                                        int size = rowsRecommendations.size();
                                        for(int i = 0 ; i < size ; i++){
                                            Log.e(TAG,"id:" + rowsRecommendations.get(i).id +"-" + rowsRecommendations.get(i).content);
                                        }
                                        if(size > 0) {
                                            //setear el adaptador con los datos
                                            callsetAdapter();
                                        }
                                    }
                                }

                            }else{
                                //showRetry();
                            }
                        }
                        else
                        {    //showRetry();
                            Log.e(TAG, "Tips Error en la petición");
                        }
                    }

                    @Override
                    public void onFailure(Call<IPushNotification.RecommendationRequest> call, Throwable t) {
                        //showRetry();
                    }
                });

                if(rowsRecommendations.size()>0){
                    callsetAdapter(); // setea los datos quemados
                }



        } catch (Exception e) {
            Log.e(TAG, "Error" + e.toString());
        }
    }




    public void callsetAdapter(){
        //validacion si se a iniciado el adapter
        if (adapter != null){
            //actuliza la data del apdater
            Log.e(TAG,"adapter != null");
            adapter.updateData(rowsRecommendations);

        }else {//es nulo
            //crea la lista adapter
            Log.e(TAG,"adapter  null");
            adapter = new InsulinRecommendationsAdapter(this, rowsRecommendations);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG,"onResume");
        //selectDataInsulinDB();
    }



    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (this.isVisible()) {
            if (isVisibleToUser) {
                InsulinActivity.fab.setVisibility(View.GONE);
            } else {
                InsulinActivity.fab.setVisibility(View.VISIBLE);
            }
        }
    }

    //    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }
}
