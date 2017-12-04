package com.grupocisc.healthmonitor.FitData.fragments;

import android.content.Intent;
import android.content.IntentSender;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.Bucket;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.data.Value;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.result.DataReadResult;
import com.grupocisc.healthmonitor.FitData.activities.FitActivity;
import com.grupocisc.healthmonitor.FitData.adapters.FitListAdapterAnd;
import com.grupocisc.healthmonitor.HealthMonitorApplicattion;
import com.grupocisc.healthmonitor.R;
import com.grupocisc.healthmonitor.Utils.Utils;
import com.grupocisc.healthmonitor.entities.IV2Fit;
import com.grupocisc.healthmonitor.entities.rowV2Fit;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.google.android.gms.fitness.data.DataType.TYPE_CALORIES_EXPENDED;

public class FitListFragmentAnd extends Fragment implements FitListAdapterAnd.MyViewHolder.ClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private String TAG = "FitListFragmentAnd";
    private SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("MMM d, h:mm a");
    GoogleApiClient mClient;
    private TextView mResultsText;
    private static final int REQUEST_OAUTH = 1;
    View contenView;
    //private ListView mListView;
    private RecyclerView recyclerView;
    private FitListAdapterAnd adapter;
    //private ListAdapter mListAdapter;
    private boolean isDataAggregated = true;
    ArrayList<DataType> mAggregateDataTypeList = new ArrayList<DataType>();
    private int bucketSize = 0;
    private Call<IV2Fit.Fit> call_fit;
    private IV2Fit.Fit mFit;
    private SimpleDateFormat mSimpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
    Button boton;

    private static final String AUTH_PENDING = "auth_state_pending";
    private boolean authInProgress = false;
    public SweetAlertDialog pDialog;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        contenView = inflater.inflate(R.layout.historydata_layout, container, false);

        pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        recyclerView = (RecyclerView) contenView.findViewById(R.id.recycler_view);

       /* if (savedInstanceState != null) {
            authInProgress = savedInstanceState.getBoolean(AUTH_PENDING);
        }*/

        mClient = new GoogleApiClient.Builder(getActivity())
                .addApi(Fitness.HISTORY_API)
                .addScope(new Scope(Scopes.FITNESS_ACTIVITY_READ))
                .addScope(new Scope(Scopes.FITNESS_BODY_READ))
                .addScope(new Scope(Scopes.FITNESS_LOCATION_READ))
                .addScope(new Scope(Scopes.FITNESS_NUTRITION_READ))
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        mResultsText = (TextView) contenView.findViewById(R.id.results);

        boton = (Button) contenView.findViewById(R.id.consultar);
        boton.setVisibility(View.VISIBLE);
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boton.setVisibility(View.GONE);
                try {
                    if (mClient.isConnected()) {
                        if (FitActivity.mDataPointList.size() > 0) {
                            FitActivity.mDataPointList.clear();
                        }
                        if (mAggregateDataTypeList.size() > 0) {
                            mAggregateDataTypeList.clear();
                        }
                        //isDataAggregated = mAggregateCheckBox.isChecked();
                        showLoadingDialog();
                        new ReadFromHistoryTask().execute();
                    } else {
                        ((FitActivity) getActivity()).setUpTabs();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });


        return contenView;
    }

    /*
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(AUTH_PENDING, authInProgress);
    }
*/
    private void showLoadingDialog() {
        pDialog.getProgressHelper().setBarColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
        pDialog.setTitleText("Espere un Momento..");
        pDialog.setCancelable(false);
        pDialog.show();

    }

    private void showLayoutDialog() {
        if (pDialog != null)
            pDialog.dismiss();
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e(TAG, "STAR");
        mClient.connect();
        elpepa();

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG, "RESUMEE");
        //elpepa();
    }


    private void elpepa() {
        Log.e(TAG, "Inicio del Metodo");
        try {
            if (mClient.isConnected()) {

                if (FitActivity.mDataPointList.size() > 0) {
                    FitActivity.mDataPointList.clear();
                }
                if (mAggregateDataTypeList.size() > 0) {
                    mAggregateDataTypeList.clear();
                }
                showLoadingDialog();

                new ReadFromHistoryTask().execute();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e(TAG, "onStop");
        if (mClient.isConnected()) {
            mClient.disconnect();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_OAUTH && resultCode == RESULT_OK) {
            if (!mClient.isConnecting() && !mClient.isConnected()) {
                mClient.connect();
                elpepa();
            }
        }
/*
        if( requestCode == REQUEST_OAUTH ) {
            authInProgress = false;
            if( resultCode == RESULT_OK ) {
                if( !mClient.isConnecting() && !mClient.isConnected() ) {
                    mClient.connect();
                    Log.e( "GoogleFit", "onActivityResult conect" );

                }
            } else if( resultCode == RESULT_CANCELED ) {
                Log.e( "GoogleFit", "RESULT_CANCELED" );
            }
        } else {
            Log.e("GoogleFit", "requestCode NOT request_oauth");
        }
        */
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(getActivity(), REQUEST_OAUTH);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
      /* if( !authInProgress ) {
            try {
                Log.e( "GoogleFit", "authInProgres if" );
                authInProgress = true;
                connectionResult.startResolutionForResult( getActivity(), REQUEST_OAUTH );
            } catch(IntentSender.SendIntentException e ) {

            }
        } else {
            Log.e( "GoogleFit", "authInProgress" );
        } */

    }

    public void setLista() {
        try {
            showLayoutDialog();
            //validacion si se a iniciado el adapter
            if (adapter != null) {
                //actuliza la data del apdater
                Log.e(TAG, "adapter != null");
                adapter.updateData(FitActivity.mDataPointList);
                sendWSCholesterol(FitActivity.mDataPointList);

            } else {//es nulo
                //crea la lista adapter
                Log.e(TAG, "adapter  null");
                adapter = new FitListAdapterAnd(getActivity(), FitActivity.mDataPointList, this, recyclerView, true);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                sendWSCholesterol(FitActivity.mDataPointList);
            }
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    @Override
    public void onItemClicked(View v, int position) {
    }

    @Override
    public boolean onItemLongClicked(View v, int position) {
        return false;
    }


    public class ReadFromHistoryTask extends AsyncTask<Void, Void, Void> {
        protected Void doInBackground(Void... params) {


            //long endTime = mEndDateCalendar.getTimeInMillis();
            //long startTime = mStartDateCalendar.getTimeInMillis();
            Calendar cal = Calendar.getInstance();
            Date now = new Date();
            cal.setTime(now);
            long endTime = cal.getTimeInMillis();
            cal.add(Calendar.WEEK_OF_YEAR, -1);
            long startTime = cal.getTimeInMillis();

            DataReadResult dataReadResult = null;
            if (isDataAggregated) {
                //


                if (true) {
                    DataReadRequest readRequest = new DataReadRequest.Builder()
                            //.aggregate(mSelectedDataType, mAggregateDataTypeList.get(0))
                            //.aggregate(mSelectedDataType,   TYPE_CALORIES_EXPENDED)
                            .aggregate(TYPE_CALORIES_EXPENDED, TYPE_CALORIES_EXPENDED)
                            .bucketByTime(1, TimeUnit.DAYS)
                            .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
                            .build();
                    dataReadResult = Fitness.HistoryApi.readData(mClient, readRequest).await();
                    Log.e(TAG, "Hola Adentro");
                } else {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mResultsText.setText("Datos no soportados");
                            setLista();
                            //adapter.notifyDataSetChanged();
                        }
                    });
                }
            } /*else {
                DataReadRequest readRequest = new DataReadRequest.Builder().read(mSelectedDataType)
                        .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS).build();
                dataReadResult = Fitness.HistoryApi.readData(mClient, readRequest).await();
            }*/
            if (isDataAggregated) {
                //if (mAggregateDataTypeList.size() > 0 && dataReadResult != null) {
                if (dataReadResult != null) {
                    readBucketValues(dataReadResult);
                }
            } /*else {
                DataSet dataSet = dataReadResult.getDataSet(mSelectedDataType);
                readDataSetValues(dataSet, false);
            }*/
            return null;
        }
    }

    public void readBucketValues(DataReadResult dataReadResult) {

        bucketSize = 0;
        for (Bucket bucket : dataReadResult.getBuckets()) {

            List<DataSet> dataSets = bucket.getDataSets();

            for (DataSet dataSet : dataSets) {

                if (dataSet.getDataPoints().size() > 0) {
                    bucketSize++;
                    readDataSetValues(dataSet, true);
                }
            }
        }
        updateUIThread(true);
    }

    public void readDataSetValues(DataSet dataSet, boolean isBucketData) {

        for (DataPoint mDataPoint : dataSet.getDataPoints()) {

            FitActivity.mDataPointList.add(mDataPoint);
        }

        if (!isBucketData) {

            updateUIThread(isBucketData);
        }
    }

    public void updateUIThread(final boolean isBucketData) {
        showLayoutDialog();
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isBucketData) {
                    if (bucketSize > 0) {
                        mResultsText.setText("Total de " + bucketSize + " Dato(s) encontrado(s)");
                    } else {
                        mResultsText.setText("No se han encontrado datos");
                    }
                } else {
                    if (FitActivity.mDataPointList.size() > 0) {
                        mResultsText.setText("Total " + FitActivity.mDataPointList.size() + " Punto(s) de dato(s) encontrado(s)");

                        setLista();
                    } else {
                        mResultsText.setText("No hay datos históricos encontrados");
                    }
                }
                setLista();
                //adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onConnected(Bundle bundle) {

    }


    @Override
    public void onConnectionSuspended(int i) {

    }

    public void sendWSCholesterol(ArrayList<DataPoint> rowsPoint) {
        try {
            int x = 0;
            for (DataPoint dt : rowsPoint) {
                restartLoadingEnviarData(dt, x);
                Log.e(TAG, "Fit - Entra a enviar");
                x++;
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }


    private void restartLoadingEnviarData(final DataPoint dt, int posicion) {
        int value = 0;
        String Email = Utils.getEmailFromPreference(getActivity());
        String fechaI = mSimpleDateFormat2.format(dt.getStartTime(TimeUnit.MILLISECONDS));
        String fechaF = mSimpleDateFormat2.format(dt.getStartTime(TimeUnit.MILLISECONDS));
        String activity = "Calorias";
        StringBuilder dataValue = new StringBuilder();
        for (Field field : FitActivity.mDataPointList.get(posicion).getDataType().getFields()) {
            Value val = FitActivity.mDataPointList.get(posicion).getValue(field);

            //dataValue.append("Nombre: Calorias" + nombre + "  Valor: " + val.toString());
            String valor = val.toString();
            float f = Float.parseFloat(valor);
            value = (int) f;

        }

        Log.e(TAG, "Fit - Email " + Email + "Fit - Fecha Inicio " + fechaI + "Fit - Fecha Fin " + fechaF);

        try {
            IV2Fit CrtPacientFit = HealthMonitorApplicattion.getApplication().getRetrofitAdapter().create(IV2Fit.class);
            call_fit = CrtPacientFit.setSendFitFrom(new rowV2Fit(Email, fechaI, fechaF, activity, value));
            call_fit.enqueue(new Callback<IV2Fit.Fit>() {

                @Override
                public void onResponse(Call<IV2Fit.Fit> call, Response<IV2Fit.Fit> response) {
                    if (response.isSuccessful()) {
                        mFit = null;
                        mFit = response.body();

                        Log.e(TAG, "Fit -  response true");
                        postRetrofit();
                    } else {
                        String msj = "Fit - Error en la petición response false";
                        Log.e(TAG, msj);
                    }
                }

                @Override
                public void onFailure(Call<IV2Fit.Fit> call, Throwable t) {
                    String msj = "Fit - Error en la petición: onFailure";
                    Log.e(TAG, msj);
                    t.printStackTrace();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void postRetrofit() {
        if (mFit != null) {
            if (mFit.getIdCodResult() == 0) {
                Log.e(TAG, "enviado ok IdCodResult ==  0");
            } else {
                Log.e(TAG, "IdCodResult !=  0");
            }
        } else {
            Log.e(TAG, "mFit is null");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (call_fit != null && !call_fit.isCanceled()) {
            call_fit.cancel();
        }
    }


}