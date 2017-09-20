package com.grupocisc.healthmonitor.Feeding.fragments;

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

import com.grupocisc.healthmonitor.Feeding.adapters.FeedingListDietAdapterU;
import com.grupocisc.healthmonitor.HealthMonitorApplicattion;
import com.grupocisc.healthmonitor.R;
import com.grupocisc.healthmonitor.Utils.Utils;
import com.grupocisc.healthmonitor.entities.IDietR;
import com.grupocisc.healthmonitor.entities.IDietU;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by Walter on 01/03/2017.
 */

public class FeedingListDietFragmentU extends Fragment {

    private Call<ArrayList<IDietU.Diet>> listCall;
    private ArrayList<IDietU.Diet> diet;
    private Call<ArrayList<IDietR.DietR>> listCallR;
    private ArrayList<IDietR.DietR> dietR;
    private RecyclerView recyclerView;
    private LinearLayout linear_loading;
    private ProgressBar progress;
    private Button retry;
    private View contenView;
    private float calories;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contenView = inflater.inflate(R.layout.feeding_list_diet_fragment, container, false);
        linear_loading = (LinearLayout) contenView.findViewById(R.id.linear_loading);
        progress = (ProgressBar) contenView.findViewById(R.id.progress);
        calories = getActivity().getIntent().getFloatExtra("calories", 0.0f);
        retry = (Button) contenView.findViewById(R.id.retry);
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                restartLoadingDietsRec();
                restartLoadingDiets();

            }
        });

        restartLoadingDietsRec();
        restartLoadingDiets();


        return contenView;
    }

    public void restartLoadingDiets() {
        showLoading();
        IDietU diets = HealthMonitorApplicattion.getApplication().getmRestCISCAdapter().create(IDietU.class);
        listCall = diets.getListDiet();
        listCall.enqueue(new Callback<ArrayList<IDietU.Diet>>() {
            @Override
            public void onResponse(Call<ArrayList<IDietU.Diet>> call, Response<ArrayList<IDietU.Diet>> response) {
                if (response.isSuccessful()) {
                    diet = null;
                    diet = response.body();
                    postExecute();
                } else {
                    showRetry();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<IDietU.Diet>> call, Throwable t) {
                showRetry();
                t.printStackTrace();
            }
        });
    }

    public void restartLoadingDietsRec() {
        showLoading();
        String email = Utils.getEmailFromPreference(getActivity());
        IDietR dietsR = HealthMonitorApplicattion.getApplication().getmRestCISCAdapterAnimo().create(IDietR.class);
        listCallR = dietsR.getDietR(email);
        listCallR.enqueue(new Callback<ArrayList<IDietR.DietR>>() {
            @Override
            public void onResponse(Call<ArrayList<IDietR.DietR>> call, Response<ArrayList<IDietR.DietR>> response) {
                if (response.isSuccessful()) {
                    dietR = null;
                    dietR = response.body();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<IDietR.DietR>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }


    public void postExecute() {
        if (diet != null) {
            if (diet.size() > 0) {
                ArrayList<IDietU.Diet> listDiet = new ArrayList<>();
                int count = 0;
                for (int i = 0; i < diet.size(); i++) {
                    if (diet.get(i).getCalorias() == calories) {
                        listDiet.add(count, diet.get(i));
                        count += 1;
                    }
                }
                if (listDiet.size() > 0) {
                    ArrayList<IDietU.Diet> diets = postExecuteRec();
                    if (diets.size() > 0) {
                        diets.addAll(listDiet);
                    } else {
                        diets = listDiet;
                    }

                    recyclerView = (RecyclerView) contenView.findViewById(R.id.dummyfrag_scrollableview);
                    GridLayoutManager lLayout = new GridLayoutManager(getActivity(), 2);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(lLayout);
                    FeedingListDietAdapterU adapter = new FeedingListDietAdapterU(getActivity(), diets);
                    recyclerView.setAdapter(adapter);
                    showLayout();
                } else {
                    showRetry();
                }
            } else
                showRetry();
        } else
            showRetry();
    }

    public ArrayList<IDietU.Diet> postExecuteRec() {
        ArrayList<IDietU.Diet> listDietU = new ArrayList<>();
        if (dietR != null) {
            if (dietR.size() > 0) {
                int count = 0;
                for (int i = 0; i < dietR.size(); i++) {
                    if (!"".equals(dietR.get(i).getIdDieta())) {
                        IDietU.Diet diet1 = new IDietU.Diet();
                        if (dietR.get(i).getIdDieta() != null) {
                            diet1.setIdDieta(Integer.valueOf(dietR.get(i).getIdDieta()));
                        } else {
                            diet1.setIdDieta(0);
                        }
                        if (dietR.get(i).getNombre() != null) {
                            diet1.setNombre(dietR.get(i).getNombre());
                        } else {
                            diet1.setNombre("Dieta");
                        }
                        if (dietR.get(i).getDescripcion() != null) {
                            diet1.setDescripcion(dietR.get(i).getDescripcion());
                        } else {
                            diet1.setDescripcion("Dieta");
                        }
                        if (dietR.get(i).getCalorias() != null) {
                            diet1.setCalorias(Float.valueOf(dietR.get(i).getCalorias()));
                        } else {
                            diet1.setCalorias(0.0f);
                        }
                        if (dietR.get(i).getGrasas() != null) {
                            diet1.setGrasas(Float.valueOf(dietR.get(i).getGrasas()));
                        } else {
                            diet1.setGrasas(0.0f);
                        }
                        if (dietR.get(i).getCarbohidratos() != null) {
                            diet1.setCarbohidratos(Float.valueOf(dietR.get(i).getCarbohidratos()));
                        } else {
                            diet1.setCarbohidratos(0.0f);
                        }
                        if (dietR.get(i).getProteinas() != null) {
                            diet1.setProteinas(Float.valueOf(dietR.get(i).getProteinas()));
                        } else {
                            diet1.setProteinas(0.0f);
                        }
                        if (dietR.get(i).getObservaciones() != null) {
                            diet1.setObservaciones(dietR.get(i).getObservaciones());
                        } else {
                            diet1.setObservaciones("Dieta");
                        }

                        diet1.setKey("R");
                        diet1.setObservaciones(dietR.get(i).getObservaciones());
                        diet1.setUrl(dietR.get(i).getUrl());
                        listDietU.add(diet1);
                        count += 1;
                    }
                }
            }
        }
        return listDietU;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            if (listCall != null || !listCall.isCanceled()) {
                listCall.cancel();
            }
        } catch (NullPointerException ex) {
            ex.printStackTrace();
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
}
