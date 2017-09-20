package com.grupocisc.healthmonitor.Asthma.fragments;


import android.app.ProgressDialog;
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
import android.widget.TextView;

import com.grupocisc.healthmonitor.Asthma.activities.PickFlowRegistry;
import com.grupocisc.healthmonitor.Asthma.adapters.CausesGridAdapter;
import com.grupocisc.healthmonitor.HealthMonitorApplicattion;
import com.grupocisc.healthmonitor.R;
import com.grupocisc.healthmonitor.Utils.Utils;
import com.grupocisc.healthmonitor.entities.ArrayListCauses;
import com.grupocisc.healthmonitor.entities.ArrayListSymptom;
import com.grupocisc.healthmonitor.entities.IV2Causes;
import com.grupocisc.healthmonitor.entities.IV2SavePickFlow;
import com.grupocisc.healthmonitor.entities.ObjPickFlow;
import com.grupocisc.healthmonitor.entities.ObjtSavePickFlow;
import com.grupocisc.healthmonitor.entities.ArrayParamsFavoritesPick;
import com.grupocisc.healthmonitor.entities.rowsV2Causes;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CausesGridFragment extends Fragment implements CausesGridAdapter.ViewHolder.ClickListener {


    private static  final String TAG = "CausesGridFragment";
    private boolean isUpdate = false;
    //INCLUDE
    private LinearLayout linear_loading , lyt_loading;
    private ProgressBar progress;
    private Button retry;

    private TextView title_cab, txt_siguiente;

    private RecyclerView recyclerView;
    private CausesGridAdapter adapter;
    private CardView card_siguiente , card_atras;

    private View view;
    private ArrayList<ArrayParamsFavoritesPick> details;
    private Call<IV2SavePickFlow.SavePickFlow> call_1;
    private IV2SavePickFlow.SavePickFlow mSaveFav;
    private Call<IV2Causes.Obj> call_0;
    private IV2Causes.Obj mCauses;

    private List<rowsV2Causes> rowCausesSelect ;
    private boolean startSettings = false ;

    private String  title = "Seleccione los desencadenantes";
    private String enviadoServerOk = "true";
    private String operacionI = "I";

    private String Email= "";
    public ProgressDialog Dialog;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      // Inflate the layout for this fragment
       view = inflater.inflate(R.layout.pick_symtom_grid_fragment, container, false);

        title_cab      = (TextView) view.findViewById(R.id.title_cab);
        title_cab.setText(title);
        txt_siguiente = (TextView) view.findViewById(R.id.txt_siguiente);
        txt_siguiente.setText(getResources().getString(R.string.guardar) );

        Dialog = new ProgressDialog(getActivity());

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

        if (Utils.getEmailFromPreference(getActivity()) != null)
            Email = Utils.getEmailFromPreference(getActivity());

        eventButtons();
        restartLoading();
        return view;

    }

    private void showLoadingDialog(){
        Dialog.setMessage("Espere un Momento..");
        Dialog.setCancelable(false);
        Dialog.show();
    }

    private void showLayoutDialog(){
        if (Dialog != null)
            Dialog.dismiss();
    }

    public void eventButtons(){
        card_atras = (CardView) view.findViewById(R.id.card_atras);
        card_atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //call previous fargment
                ((PickFlowRegistry)getActivity()).callFragmentSymptomCategory();
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
                            int categoryId = mCauses.getRows().get(selectPosition).getIdRegisterDB() ;
                            Log.e("adapter--", "categoryid:--" + categoryId + "--:" + mCauses.getRows().get(selectPosition).getDescripcion());
                            favCategory.add(new ArrayParamsFavoritesPick(categoryId , 1));
                        }
                        setFavortiesZeroOne(favCategory);
                        restartLoadingSaveDataPickFlow();

                    } else {
                        snackBar(v, getString(R.string.text_mensaje_select));
                    }
                }
            }
        });

    }
    //ontener toda la lista y 1 para marcar como favorito y 0 para desmarcarlo
    public void setFavortiesZeroOne(List<ArrayParamsFavoritesPick> favCategory){
        Log.e(TAG, "setFavortiesZeroOne" );
        details = new ArrayList<ArrayParamsFavoritesPick>() ;
        for(int w=0 ; w < mCauses.getRows().size() ; w++) {
            Log.e(TAG, "setFavortiesZeroOne 2" );
            boolean isFavorite = false;
            int idCategory = mCauses.getRows().get(w).getIdRegisterDB();
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
            details.add(dt);
        }
    }

    private void restartLoading() {
        showLoading();
        restartLoadingCauses();
    }

    private void restartLoadingCauses(){
        IV2Causes posiciones = HealthMonitorApplicattion.getApplication().getRestCISCAdapterV2().create(IV2Causes.class);
        call_0 = posiciones.getCategoryFrom( new ObjPickFlow( PickFlowRegistry.FlujoMax ) );
        call_0.enqueue(new Callback<IV2Causes.Obj>() {
            @Override
            public void onResponse(Call<IV2Causes.Obj> call, Response<IV2Causes.Obj> response) {
                if (response.isSuccessful()) {
                    Log.e(TAG, "Respuesta exitosa");
                    mCauses = null;
                    mCauses = response.body();
                    Postexecute();

                } else {
                    showRetry();
                    Log.e(TAG, "Error en la petición");
                }
            }

            @Override
            public void onFailure(Call<IV2Causes.Obj> call, Throwable t) {
                Log.e(TAG, "Error en la petición onFailure");
                showRetry();
                t.printStackTrace();
            }
        });
    }


    public void Postexecute(){

        if(mCauses != null){
            if(mCauses.getIdCodResult() == 0){
                if(mCauses.getRows() != null){
                    if(mCauses.getRows().size() > 0){

                        //setear grdiview recicler
                        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2); //de cuanto va hacer el gridview
                        recyclerView.setItemAnimator(new DefaultItemAnimator());

                        recyclerView.setLayoutManager(gridLayoutManager);

                        adapter = new CausesGridAdapter(getActivity(),this, recyclerView, mCauses.getRows() , true );
                        recyclerView.setAdapter(adapter);

                        //setFavoritesPositionsAdapter(mCauses.getRows() , rowCategorySelect);
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

    private void restartLoadingSaveDataPickFlow(){
        if (details.size() > 0 || details != null ) {

            //datos de la actividad
            String email           = Utils.getEmailFromPreference(getActivity());
            float flujoMax = PickFlowRegistry.FlujoMax ;
            String  fe = PickFlowRegistry.fecha ;
            String  ho = PickFlowRegistry.hora ;
            Log.e(TAG, "fecha1:" + fe + ho);
            String fecha=fe.substring(6,10)+"/"+fe.substring(3,5)+"/"+fe.substring(0,2)+" "+ho;
            Log.e(TAG, "fecha2:" + fecha);
            String  observacion = PickFlowRegistry.observacion ;
            ArrayList<ArrayParamsFavoritesPick>  arregloSymptom   = PickFlowRegistry.arregloSymptom ;

            for(  ArrayParamsFavoritesPick  arrayParamsFavoritesPick:  details){
                Log.e(TAG, "LISTA simtomas:"+ arrayParamsFavoritesPick.getKey() + "-"+  arrayParamsFavoritesPick.getValue());
            }

            //setear objeto
            ObjtSavePickFlow objSavePick =new ObjtSavePickFlow(email, flujoMax , fecha, observacion, details, arregloSymptom);

            restarLoadingSavePick(objSavePick);

        }
    }

    public void restarLoadingSavePick(final ObjtSavePickFlow objSavePick){
        try{
            showLoadingDialog();

            IV2SavePickFlow saveFav = HealthMonitorApplicattion.getApplication().getRestCISCAdapterV2().create(IV2SavePickFlow.class);
            call_1 = saveFav.setSaveSavePickFlow( objSavePick );
            call_1.enqueue(new Callback<IV2SavePickFlow.SavePickFlow>() {
                @Override
                public void onResponse(Call<IV2SavePickFlow.SavePickFlow> call, Response<IV2SavePickFlow.SavePickFlow> response) {
                    if (response.isSuccessful()) {
                        Log.e(TAG, "Respuesta exitosa");
                        mSaveFav = null;
                        mSaveFav = response.body();

                        postExecutionSavePickFlow(objSavePick);
                    } else {
                        showLayoutDialog();
                        Utils.generateToast(getActivity(), getResources().getString(R.string.text_error_metodo));
                        Log.e(TAG, "Error en la petición");
                    }
                }
                @Override
                public void onFailure(Call<IV2SavePickFlow.SavePickFlow> call, Throwable t) {
                    showLayoutDialog();
                    Utils.generateToast(getActivity(), getResources().getString(R.string.text_error_metodo));
                    t.printStackTrace();
                    Log.e(TAG, "Error en la petición onFailure");
                }
            });
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void postExecutionSavePickFlow(ObjtSavePickFlow objSavePick){
        showLayoutDialog();
        Log.e(TAG, " post execute");
        if(mSaveFav != null){
            Log.e(TAG, " post execute != null");
            if(mSaveFav.getIdCodResult() == 0){
                Log.e(TAG, " post execute code 0 ok");
                guardarBDAndroid(objSavePick);
            }else{
                Utils.generateToast(getActivity(), mSaveFav.getResultDescription() );
            }
        }else{
            Utils.generateToast(getActivity(), getResources().getString(R.string.text_error_metodo));
        }
    }

    public void guardarBDAndroid(ObjtSavePickFlow objSavePick){
        //if (isUpdate) {
          //  updateAsthmaFromDatabase(IdAsthma, flujo_ma, fecha, hora, observacion);
        //} else {
        String fecha= objSavePick.getDate().substring(8,10)+ "/"+ objSavePick.getDate().substring(5,7)+"/" +objSavePick.getDate().substring(0,4);
        String hora =  objSavePick.getDate().substring(11,16);
        saveDataAsthmaDB(mSaveFav.getIdRegisterDB() , fecha, hora, objSavePick.getMeasureUnits() , objSavePick.getObservations(), operacionI);
        //}
        //finalizar matar la actividad
        ((PickFlowRegistry)getActivity()).finilizar();
    }

    public boolean saveDataAsthmaDB(int idBdServer, String fecha, String hora, float flujo_maximo, String observacion, String operacionI) {
        try {
            //setear datos al objeto y guardar y BD
            Utils.DbsaveAsthmaFromDatabase(
                    idBdServer,
                    fecha,
                    hora,
                    flujo_maximo,
                    observacion,
                    enviadoServerOk,
                    operacionI,
                    HealthMonitorApplicattion.getApplication().getAsthmaDao());
            Utils.generateToast(getActivity(), getResources().getString(R.string.txt_guardado));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    //ACTUALIZAR DATOS EN LA TABLA BDSQLITE
    public void updateAsthmaFromDatabase(int id, int flujo_maximo, String fecha, String hora, String observacion) {
        try {
            //setear datos al objeto y guardar y BD
            Utils.UpdateAsthmaFromDatabase(id,
                    flujo_maximo,
                    fecha,
                    hora,
                    observacion,
                    HealthMonitorApplicattion.getApplication().getAsthmaDao());

            Utils.generateToast(getActivity(), getResources().getString(R.string.txt_actulizado));

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        if(call_0!=null && !call_0.isCanceled()) {
            call_0.cancel();
        }
        if(call_1!=null && !call_1.isCanceled()) {
            call_1.cancel();
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
