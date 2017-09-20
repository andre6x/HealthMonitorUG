package com.grupocisc.healthmonitor.Asthma.fragments;


import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import com.grupocisc.healthmonitor.Asthma.activities.PickFlowRegistry;
import com.grupocisc.healthmonitor.Asthma.adapters.SymptomGridAdapter;
import com.grupocisc.healthmonitor.HealthMonitorApplicattion;
import com.grupocisc.healthmonitor.R;
import com.grupocisc.healthmonitor.entities.ArrayListSymptom;
import com.grupocisc.healthmonitor.entities.IV2Symptom;
import com.grupocisc.healthmonitor.entities.ObjPickFlow;
import com.grupocisc.healthmonitor.entities.ArrayParamsFavoritesPick;
import com.grupocisc.healthmonitor.entities.rowsV2Causes;

import org.json.JSONArray;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SymptomGridFragment extends Fragment implements SymptomGridAdapter.ViewHolder.ClickListener {


    private static  final String TAG = "SymptomGridFragment";

    //INCLUDE
    private LinearLayout linear_loading , lyt_loading;
    private ProgressBar progress;
    private Button retry;
    private RecyclerView recyclerView;
    private SymptomGridAdapter adapter;
    private CardView card_siguiente , card_atras;
    private View view;
    private Call<IV2Symptom.Obj> call_0;
    private IV2Symptom.Obj mSymptom;
    private boolean startSettings = false ;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      // Inflate the layout for this fragment
       view = inflater.inflate(R.layout.pick_symtom_grid_fragment, container, false);

        lyt_loading    = (LinearLayout) view.findViewById(R.id.lyt_loading);
        linear_loading = (LinearLayout) view.findViewById(R.id.linear_loading);
        progress       = (ProgressBar)  view.findViewById(R.id.progress);
        retry          = (Button)       view.findViewById(R.id.retry);
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                restartLoading();
            }
        });
        Bundle extras = this.getArguments();
        if (extras != null)
            startSettings = extras.getBoolean("startSettings");

        eventButtons();
        restartLoading();
       return view;

    }

    public void eventButtons(){
        card_atras = (CardView) view.findViewById(R.id.card_atras);
        card_atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //call previous fargment
                ((PickFlowRegistry)getActivity()).callFragmentFirst();
            }
        });

        card_siguiente = (CardView) view.findViewById(R.id.card_siguiente);
        card_siguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adapter != null) {
                    Log.e("adapter--", String.valueOf(adapter.getSelectedItemCount()));
                    int itemsCount = adapter.getSelectedItemCount();
                    if (adapter.getSelectedItemCount() > 0 || startSettings) {
                        Log.e("adapter--", "" + adapter.getSelectedItems());
                        List<ArrayParamsFavoritesPick> favCategory = new ArrayList<>();
                        for (int i = 0; i < adapter.getSelectedItems().size(); i++) {
                            Log.e("adapter--", "--:" + adapter.getSelectedItems().get(i));
                            int selectPosition = adapter.getSelectedItems().get(i);
                            int categoryId = mSymptom.getRows().get(selectPosition).getIdRegisterDB();
                            Log.e("adapter--", "categoryid:--" + categoryId + "--:" + mSymptom.getRows().get(selectPosition).getDescripcion());
                            favCategory.add(new ArrayParamsFavoritesPick(categoryId , 1));
                        }
                        setFavortiesZeroOne(favCategory);
                        nextAndSaveSymtom();

                    } else {
                        snackBar(v, getString(R.string.text_mensaje_select));
                    }
                }
            }
        });

    }
    //obtener toda la lista y 1 para marcar como favorito y 0 para desmarcarlo
    public void setFavortiesZeroOne(List<ArrayParamsFavoritesPick> favCategory){
        Log.e("adapter--", "setFavortiesZeroOne" );
        PickFlowRegistry.arregloSymptom = new ArrayList<ArrayParamsFavoritesPick>() ;
        for(int w=0 ; w < mSymptom.getRows().size() ; w++) {
            Log.e("adapter--", "setFavortiesZeroOne 2" );
            boolean isFavorite = false;
            int idCategory = mSymptom.getRows().get(w).getIdRegisterDB() ;
            for (int x=0; x < favCategory.size(); x++) {
                if( idCategory ==  favCategory.get(x).getKey() ) {
                    isFavorite= true;
                }
            }
            int value = 0;
            if(isFavorite)
                value = 1 ;//1 para marcar como favorito y 0 para desmarcarlo
            else
                value = 0;//1 para marcar como favorito y 0 para desmarcarlo
            ArrayParamsFavoritesPick dt = new ArrayParamsFavoritesPick( idCategory, value );
            PickFlowRegistry.arregloSymptom.add(dt);
        }
    }

    private void nextAndSaveSymtom(){


        if (PickFlowRegistry.arregloSymptom.size() > 0 || PickFlowRegistry.arregloSymptom != null ) {
            Log.e(TAG, "restar save 2");

            for(  ArrayParamsFavoritesPick  arrayParamsFavoritesPick:  PickFlowRegistry.arregloSymptom){
                Log.e(TAG, "LISTA simtomas:"+ arrayParamsFavoritesPick.getKey() + "-"+  arrayParamsFavoritesPick.getValue());
            }

            ((PickFlowRegistry)getActivity()).callFragmentCausesCategory();
        }
    }

    private void restartLoading() {
        showLoading();
        restartLoadingCategories();
    }

    private void restartLoadingCategories(){

        IV2Symptom posiciones = HealthMonitorApplicattion.getApplication().getRestCISCAdapterV2().create(IV2Symptom.class);
        call_0 = posiciones.getSymptomFrom( new ObjPickFlow ( PickFlowRegistry.FlujoMax ) );
        call_0.enqueue(new Callback<IV2Symptom.Obj>() {
            @Override
            public void onResponse(Call<IV2Symptom.Obj> call, Response<IV2Symptom.Obj> response) {
                if (response.isSuccessful()) {
                    Log.e(TAG, "Respuesta exitosa");
                    mSymptom = null;
                    mSymptom = response.body();
                    Postexecute();
                } else {
                    showRetry();
                    Log.e(TAG, "Error en la petición");
                }
            }

            @Override
            public void onFailure(Call<IV2Symptom.Obj> call, Throwable t) {
                Log.e(TAG, "Error en la petición onFailure");
                showRetry();
                t.printStackTrace();
            }
        });
    }


    public  void Postexecute(){
        if(mSymptom != null){
            if(mSymptom.getIdCodResult() == 0){
                if(mSymptom.getRows() != null){
                    if(mSymptom.getRows().size() > 0){

                        //setear grdiview recicler
                        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2); //de cuanto va hacer el gridview
                        recyclerView.setItemAnimator(new DefaultItemAnimator());

                        recyclerView.setLayoutManager(gridLayoutManager);

                        adapter = new SymptomGridAdapter(getActivity(),this, recyclerView, mSymptom.getRows() , true );
                        recyclerView.setAdapter(adapter);

                        //setFavoritesPositionsAdapter(mSymptom.getRows() , rowCategorySelect);
                        showLayout();

                    }else
                        showRetry();
                }else
                    showRetry();
            }else
                showRetry();
        }else
            showRetry();
    }
    //setear el adaptador los id favoritos por posicion "toggleSelection"
    public void setFavoritesPositionsAdapter(List<rowsV2Causes> rows , List<rowsV2Causes> rowCategorySelect){

        for (int x = 0; x < rows.size(); x++) {
            if (rowCategorySelect != null) {
                if (rowCategorySelect.size() > 0) {
                    for (int y = 0; y < rowCategorySelect.size(); y++) {
                        if (rowCategorySelect.get(y).getIdmed_categories() == rows.get(x).getIdmed_categories() ) {
                            Log.e(TAG,"CATEGORY:"+ rows.get(x).getTitle() );
                            int position = x;
                            toggleSelection(position);
                        }
                    }
                }
            }
        }
    }

    public void snackBar(View view , String mensaje){
        Snackbar.make(view, "\n" + mensaje, Snackbar.LENGTH_LONG)
                .setAction("Ok", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                })
                .setActionTextColor(this.getResources().getColor(R.color.greenMenu))
                .show();
    }


    private void showLayout( ) {
        if (this != null) {
            lyt_loading.setVisibility(View.GONE);
            linear_loading.setVisibility(View.GONE);
        }
    }

    private void showLoading() {
        if (this != null) {
            lyt_loading.setVisibility(View.VISIBLE);
            linear_loading.setVisibility(View.VISIBLE);
            progress.setVisibility(View.VISIBLE);
            retry.setVisibility(View.GONE);
        }
    }

    private void showRetry( ) {
        if (this != null){
            lyt_loading.setVisibility(View.VISIBLE);
            linear_loading.setVisibility(View.VISIBLE);
            progress.setVisibility(View.GONE);
            retry.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(call_0!=null && !call_0.isCanceled()) {
            call_0.cancel();
        }
        Log.e(TAG,"onDestroy.........");
    }


    @Override
    public void onItemClicked(int position) {
        toggleSelection(position);
    }

    @Override
    public boolean onItemLongClicked(int position) {
        return false;
    }


    /*** Toggle the selection state of an item.
     * If the item was the last one in the selection and is unselected, the selection is stopped.
     * Note that the selection must already be started (actionMode must not be null).
     *
     * @param position Position of the item to toggle the selection state
     */
    private void toggleSelection(int position) {
        adapter.toggleSelection(position);
        int count = adapter.getSelectedItemCount();

    }

}
