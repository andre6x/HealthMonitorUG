package com.grupocisc.healthmonitor.Routines.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.grupocisc.healthmonitor.HealthMonitorApplicattion;
import com.grupocisc.healthmonitor.R;
import com.grupocisc.healthmonitor.Routines.adapters.RutinesGridAdapter;
import com.grupocisc.healthmonitor.Utils.Utils;
import com.grupocisc.healthmonitor.entities.IRutinasR;
import com.grupocisc.healthmonitor.entities.IRutinasU;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RoutinesGridFragment extends Fragment {

    private Call<ArrayList<Integer>> call_2;
    private ArrayList<Integer> arrayListRutina;
    private Call<ArrayList<String>> call_2R;
    private ArrayList<String> arrayListRutinaR;
    private RecyclerView recyclerView;

    private LinearLayout linear_loading;
    private ProgressBar progress;
    private Button retry;
    private View contenView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contenView = inflater.inflate(R.layout.routines_grid_fragment, container, false);
        linear_loading = (LinearLayout) contenView.findViewById(R.id.linear_loading);
        progress = (ProgressBar) contenView.findViewById(R.id.progress);
        retry = (Button) contenView.findViewById(R.id.retry);

        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                restartLoadingCourses2R();
                restartLoadingCourses2();
            }
        });

        restartLoadingCourses2R();
        restartLoadingCourses2();

        return contenView;
    }

    private void restartLoadingCourses2() {
        showLoading();
        int id_enfermedad = 1; // diabetes
        IRutinasU iRutinasU = HealthMonitorApplicattion.getApplication().getRetrofitAdapter().create(IRutinasU.class);
        call_2 = iRutinasU.getIdRutinas(id_enfermedad);
        call_2.enqueue(new Callback<ArrayList<Integer>>() {
            @Override
            public void onResponse(Call<ArrayList<Integer>> call, Response<ArrayList<Integer>> response) {
                if (response.isSuccessful()) {
                    arrayListRutina = response.body();
                    postExecute2();
                } else {
                    showRetry();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Integer>> call, Throwable t) {
                showRetry();
                t.printStackTrace();
            }
        });
    }

    private void restartLoadingCourses2R() {
        String email = Utils.getEmailFromPreference(getActivity());
        IRutinasR iRutinasR = HealthMonitorApplicattion.getApplication().getRetrofitAdapter().create(IRutinasR.class);
        call_2R = iRutinasR.getIdRutinas(email);
        call_2R.enqueue(new Callback<ArrayList<String>>() {
            @Override
            public void onResponse(Call<ArrayList<String>> call, Response<ArrayList<String>> response) {
                if (response.isSuccessful()) {
                    arrayListRutinaR = null;
                    arrayListRutinaR = response.body();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<String>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public ArrayList<IRutinasR.RutinasR> postExecuteRec() {
        ArrayList<IRutinasR.RutinasR> rutinasRs = new ArrayList<>();
        if (arrayListRutinaR != null) {
            if (arrayListRutinaR.size() > 0) {
                for (int i = 0; i < arrayListRutinaR.size(); i++) {
                    if (!"0".equals(arrayListRutinaR.get(0))) {
                        IRutinasR.RutinasR rutinasR = new IRutinasR.RutinasR();
                        rutinasR.setKey("R");
                        rutinasR.setValue(Integer.valueOf(arrayListRutinaR.get(i)));
                        rutinasRs.add(rutinasR);
                    }
                }
            }
        }
        return rutinasRs;
    }

    public void postExecute2() {
        if (arrayListRutina != null) {
            if (arrayListRutina.size() > 0) {
                ArrayList<IRutinasR.RutinasR> listRoutines = new ArrayList<>();
                for (int i = 0; i < arrayListRutina.size(); i++) {
                    IRutinasR.RutinasR rutinasR = new IRutinasR.RutinasR();
                    rutinasR.setKey("N");
                    rutinasR.setValue(arrayListRutina.get(i));
                    listRoutines.add(rutinasR);
                }
                ArrayList<IRutinasR.RutinasR> listRoutinesR = postExecuteRec();
                if (listRoutinesR.size() > 0) {
                    listRoutinesR.addAll(listRoutines);
                } else {
                    listRoutinesR = listRoutines;
                }
                recyclerView = (RecyclerView) contenView.findViewById(R.id.dummyfrag_scrollableview);
                GridLayoutManager lLayout = new GridLayoutManager(getActivity(), 2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(lLayout);
                RutinesGridAdapter adapter = new RutinesGridAdapter(getActivity(), listRoutinesR);
                recyclerView.setAdapter(adapter);
                showLayout();
            } else {
                showRetry();
            }
        } else {
            showRetry();
        }
    }

    private void showLayout() {
        if (getActivity() != null)
            linear_loading.setVisibility(View.GONE);
    }

    private void showLoading() {
        if (getActivity() != null) {
            linear_loading.setVisibility(View.VISIBLE);
            progress.setVisibility(View.VISIBLE);
            retry.setVisibility(View.GONE);
        }
    }

    private void showRetry() {
        if (getActivity() != null) {
            linear_loading.setVisibility(View.VISIBLE);
            progress.setVisibility(View.GONE);
            retry.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (call_2 != null && !call_2.isCanceled()) {
            call_2.cancel();
        }
    }

}
