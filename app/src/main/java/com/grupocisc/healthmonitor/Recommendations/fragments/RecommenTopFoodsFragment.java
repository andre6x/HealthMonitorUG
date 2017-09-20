package com.grupocisc.healthmonitor.Recommendations.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.grupocisc.healthmonitor.R;
import com.grupocisc.healthmonitor.Recommendations.adapters.TopFoodsAdapter;
import com.grupocisc.healthmonitor.entities.ItopTen;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecommenTopFoodsFragment extends Fragment {

    private String TAG = "opFoodsFragment";
    private RecyclerView recyclerView;
    private TopFoodsAdapter adapter;

    private ItopTen.TopTen mTopTen;
    private Call<ItopTen.TopTen> call_0;

    private String CountryCode = "";
    //INCLUDE
    private LinearLayout linear_loading;
    private ProgressBar progress;
    private Button retry;


    View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.recommen_top_food_fragment, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        linear_loading = (LinearLayout) view.findViewById(R.id.linear_loading);
        progress = (ProgressBar) view.findViewById(R.id.progress);
        retry = (Button) view.findViewById(R.id.retry);

        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                restartLoading();
            }
        });

        restartLoading();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG,"onResume");

    }

    private void showLayout()
    {
        if(getActivity() != null)
            linear_loading.setVisibility(View.GONE);
    }

    private void showLoading()
    {    if(getActivity() != null) {
        linear_loading.setVisibility(View.VISIBLE);
        progress.setVisibility(View.VISIBLE);
        retry.setVisibility(View.GONE);
    }
    }

    private void showRetry()
    {
        if(getActivity() != null) {
            linear_loading.setVisibility(View.VISIBLE);
            progress.setVisibility(View.GONE);
            retry.setVisibility(View.VISIBLE);
        }
    }


    private void restartLoading() {
        showLoading();
        restartLoadingTopTen("true");
    }

    private void restartLoadingTopTen( String valor){
        ItopTen topTen = HealthMonitorApplicattion.getApplication().getRestAdapter().create(ItopTen.class);
        call_0 = topTen.getTopTen(valor, CountryCode);
        call_0.enqueue(new Callback<ItopTen.TopTen>() {
            @Override
            public void onResponse(Call<ItopTen.TopTen> call, Response<ItopTen.TopTen> response) {
                if (response.isSuccessful()) {
                    Log.e(TAG, "Respuesta exitosa");
                    mTopTen = null;
                    mTopTen = response.body();

                    Postexecute();
                } else {
                    showRetry();
                    Log.e(TAG, "Error en la petición");
                }
            }

            @Override
            public void onFailure(Call<ItopTen.TopTen> call, Throwable t) {
                showRetry();
                t.printStackTrace();
            }
        });
    }

    private void Postexecute() {
        if(mTopTen != null){
            if(mTopTen.getCode() == 0){
                if(mTopTen.getRows().size() > 0){

                    showLayout();
                    String cdn = mTopTen.getSummary().getCdn();

                    adapter = new TopFoodsAdapter(getActivity(),mTopTen.getRows(), cdn);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


                }else
                    showRetry();
            }else
                showRetry();
        }else {
            showRetry();

        }
    }


    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(call_0!=null && !call_0.isCanceled()) {
            call_0.cancel();
        }
    }


}
