package com.grupocisc.healthmonitor.Feeding.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.grupocisc.healthmonitor.Feeding.activities.FeedingActivity;
import com.grupocisc.healthmonitor.Feeding.adapters.FeedingListAdapter;
import com.grupocisc.healthmonitor.HealthMonitorApplicattion;
import com.grupocisc.healthmonitor.R;
import com.grupocisc.healthmonitor.Utils.Utils;
import com.grupocisc.healthmonitor.entities.IGetFeeding;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Walter on 31/01/2017.
 */

public class FeedingsListFragments extends Fragment {
    private RecyclerView recyclerView;
    private FeedingListAdapter adapter;
    private Call<ArrayList<IGetFeeding.RegistreFeeding>> listCall;
    private ArrayList<IGetFeeding.RegistreFeeding> registreFeedings;

    private LinearLayout linear_loading , lyt_loading;
    private ProgressBar progress;
    private Button retry;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.feeding_registry_fragment, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        lyt_loading    = (LinearLayout) view.findViewById(R.id.lyt_loading);
        linear_loading = (LinearLayout) view.findViewById(R.id.linear_loading);
        progress       = (ProgressBar)  view.findViewById(R.id.progress);
        retry          = (Button)       view.findViewById(R.id.retry);
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFeedings();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getFeedings();
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

    public void callsetAdapter() {
        if (registreFeedings != null) {
            if (registreFeedings.size() > 0) {
                adapter = new FeedingListAdapter(this, registreFeedings);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            }
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (this.isVisible()) {
            if (isVisibleToUser) {
                FeedingActivity.fab.setVisibility(View.VISIBLE);
            } else {
                FeedingActivity.fab.setVisibility(View.GONE);
            }
        }
    }

    public void getFeedings() {
        showLoading();
        String email = Utils.getEmailFromPreference(getActivity());
        IGetFeeding iGetFeeding = HealthMonitorApplicattion.getApplication().getRetrofitAdapter().create(IGetFeeding.class);
        listCall = iGetFeeding.getFeeding(email);
        listCall.enqueue(new Callback<ArrayList<IGetFeeding.RegistreFeeding>>() {
            @Override
            public void onResponse(Call<ArrayList<IGetFeeding.RegistreFeeding>> call, Response<ArrayList<IGetFeeding.RegistreFeeding>> response) {
                if (response.isSuccessful()) {
                    registreFeedings = null;
                    registreFeedings = response.body();
                    postRetrofit();
                }else{
                    showRetry();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<IGetFeeding.RegistreFeeding>> call, Throwable t) {
                t.printStackTrace();
                showRetry();
            }
        });
    }

    public void postRetrofit(){
        showLayout();
        if(registreFeedings != null && registreFeedings.size() > 0){
           callsetAdapter();
         }else{
            showRetry();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(listCall!=null && !listCall.isCanceled()) {
            listCall.cancel();
        }
    }
}
