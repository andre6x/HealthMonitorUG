package com.grupocisc.healthmonitor.Disease.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.grupocisc.healthmonitor.Disease.adapters.DiseaseListAdapterCons;
import com.grupocisc.healthmonitor.HealthMonitorApplicattion;
import com.grupocisc.healthmonitor.R;
import com.grupocisc.healthmonitor.Utils.Utils;
import com.grupocisc.healthmonitor.entities.IConsulEnfermedad;
import com.grupocisc.healthmonitor.entities.IDisease;
import com.grupocisc.healthmonitor.entities.IRegistreDisease;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;

import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.grupocisc.healthmonitor.Utils.Utils.UcallCalendar;
import static com.grupocisc.healthmonitor.Utils.Utils.UcallCalendarTime;

/**
 * Created by Gema on 10/01/2017.
 */

public class DiseaseRegistry extends AppCompatActivity implements DatePickerDialog.OnDateSetListener
        , TimePickerDialog.OnTimeSetListener {

    String TAG = "DiseaseRegistry";
    private LinearLayout linear_loading , lyt_loading;
    private ProgressBar progress;
    private Button retry;

    private RecyclerView recyclerView;
    private DiseaseListAdapterCons adapter;

    private int year, month, day, hour, minute;
    private LinearLayout lyt_fecha, lyt_hora;
    private TextView txt_fecha, txt_hora;
    private EditText txt_observacion, txt_dosis , txt_disease1;
    private FloatingActionButton fab;
    private String selectTextSpinner;
    private Toolbar toolbar;
    private CardView card_vincula;
    private static List<IDisease> rowsIDisease;

    private EditText inputSearch;
    public ProgressDialog Dialog;
    private int idEnf;
    private String NombreEnf;
    private String DescripEnf;
    private Call<IRegistreDisease.RegistreDisease> call_1;
    private IRegistreDisease.RegistreDisease mSaveDisease;
    private Call<List<IConsulEnfermedad.Enfermedad>> call_2;
    private static List<IConsulEnfermedad.Enfermedad> rowsIConsulEnfermedad;

    String email = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.disease_registry_activity);
        ButterKnife.bind(this);
        setToolbar();

        Dialog = new ProgressDialog(DiseaseRegistry.this);
        txt_fecha = (TextView) findViewById(R.id.txt_fecha);
        lyt_fecha = (LinearLayout) findViewById(R.id.lyt_fecha);
        txt_observacion = (EditText) findViewById(R.id.txt_observacion);
        card_vincula = (CardView) findViewById(R.id.card_vincular);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        lyt_loading    = (LinearLayout) findViewById(R.id.lyt_loading);
        linear_loading = (LinearLayout) findViewById(R.id.linear_loading);
        progress       = (ProgressBar)  findViewById(R.id.progress);
        retry          = (Button)       findViewById(R.id.retry);
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                restartLoading(2);
            }
        });

        inputSearch = (EditText) findViewById(R.id.edt_buscar);
        /* Activando el filtro de busqueda */
        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub
                Log.e(TAG, "execute inputSearch"+ inputSearch.getText().toString() );
                String query = inputSearch.getText().toString();
                filterNameDoctor(query);
            }
            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,int arg3) {
                // TODO Auto-generated method stub
            }
            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });

        email = Utils.getEmailFromPreference(this);

        inicializarFechaHora();
        restartLoading(2);

        lyt_fecha.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                 callCalendar();
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SaveDisease(view);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        DatePickerDialog dpd = (DatePickerDialog) getFragmentManager().findFragmentByTag("Datepickerdialog");
        if (dpd != null)
            dpd.setOnDateSetListener(this);
        TimePickerDialog tpd = (TimePickerDialog) getFragmentManager().findFragmentByTag("Timepickerdialog");
        if (tpd != null)
            tpd.setOnTimeSetListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    public void setToolbar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false); //desactivar title
        toolbar.setNavigationIcon(R.mipmap.back); //buton back tollbar
        //getSupportActionBar().setTitle("Glucosa"); //titulo tollbar
        toolbar.setTitleTextColor(getResources().getColor(R.color.white)); //color tollbar title
    }

    private void inicializarFechaHora() {
        //obtener fechay hroa de Calendar
        Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month =  c.get(Calendar.MONTH)+1;
        day = c.get(Calendar.DAY_OF_MONTH);
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);
        String mes = month < 10 ? "0"+month : ""+month;
        String dia =  day < 10 ? "0"+day : ""+day;
        //setear fecha
        String date = ""+dia+"/"+mes+"/"+year;
        txt_fecha.setText(date);
    }

    //Se ejecuta al dar ACeptar al dialogo
    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String mes = "" + ((monthOfYear + 1) < 10 ? "0" + (monthOfYear + 1) : "" + (monthOfYear + 1));
        String dia = dayOfMonth < 10 ? "0" + dayOfMonth : "" + dayOfMonth;
        String date = "" + dia + "/" + mes + "/" + year;

        //String date = ""+dayOfMonth+"/"+(++monthOfYear)+"/"+year;
        txt_fecha.setText(date);
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        String hourString = hourOfDay < 10 ? "0" + hourOfDay : "" + hourOfDay;
        String minuteString = minute < 10 ? "0" + minute : "" + minute;
        //String secondString = second < 10 ? "0"+second : ""+second;
        String time = "" + hourString + ":" + minuteString;
        txt_hora.setText(time);
    }

    /* CALENDARIO */
    public void callCalendar() {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        UcallCalendar(dpd).show(getFragmentManager(), "Datepickerdialog");
    }

    /* TIME */
    public void callCalendarTime(){
        Calendar now = Calendar.getInstance();
        TimePickerDialog tpd = TimePickerDialog.newInstance(
                this,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                true /*mode24Hours.isChecked()*/
        );
        UcallCalendarTime(tpd).show(getFragmentManager(), "Timepickerdialog");
    }

    private void showLoadingDialog(){
        if (this != null) {
            Dialog.setMessage("Espere un Momento..");
            Dialog.setCancelable(false);
            Dialog.show();
        }
    }

    private void showLayoutDialog(){
        if (this != null) {
            if (Dialog != null)
                Dialog.dismiss();
        }
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

    private void restartLoading(int index) {
        if(index == 2) { //CONSULAR
            showLoading();
            restartLoadingconsultaEnf();
        }else if(index == 1){//GUARDAR
            showLoadingDialog();
            restartLoadingEnviarData();
        }
    }
    private void restartLoadingconsultaEnf() {
        Log.e(TAG, "METODO consultaEnf ");
        IConsulEnfermedad CunsulParamet = HealthMonitorApplicattion.getApplication().getRetrofitAdapter().create(IConsulEnfermedad.class);
        call_2 = CunsulParamet.CunsulParamet();
        call_2.enqueue(new Callback<List<IConsulEnfermedad.Enfermedad>>() {
            @Override
            public void onResponse(Call<List<IConsulEnfermedad.Enfermedad>> call, Response<List<IConsulEnfermedad.Enfermedad>> response) {
                if (response.isSuccessful()) {
                    Log.e(TAG, "Respuesta exitosa");
                    rowsIConsulEnfermedad = null;
                    rowsIConsulEnfermedad = response.body();
                    postExecutionLogin();
                } else {
                    showRetry();
                    Log.e(TAG, "Error en la petición");
                }
            }

            @Override
            public void onFailure(Call<List<IConsulEnfermedad.Enfermedad>> call, Throwable t) {
                showRetry();
                t.printStackTrace();
                Log.e(TAG, "Error en la petición onFailure ");
            }
        });
    }

    public void postExecutionLogin() {
        showLayout();
        if (rowsIConsulEnfermedad != null && rowsIConsulEnfermedad.size() > 0) {
                adapter = new DiseaseListAdapterCons(this , rowsIConsulEnfermedad );
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager( this ));
        }else{
            showRetry();
        }
    }

    public void filterNameDoctor(String query){
        if(adapter != null){
            adapter.filterUpdate(query);
        }
    }

    private void restartLoadingEnviarData() {
        //enviar webservice
        IRegistreDisease RegistreDisease = HealthMonitorApplicattion.getApplication().getRetrofitAdapter().create(IRegistreDisease.class);
        String fecha = txt_fecha.getText().toString().substring(6,10)+"/"+txt_fecha.getText().toString().substring(3,5)+"/"+txt_fecha.getText().toString().substring(0,2);
        String observacion = txt_observacion.getText().toString();
        Log.e(TAG, "fercha "+ fecha);

        call_1 = RegistreDisease.RegDisease(email,fecha,observacion,"", idEnf);
        call_1.enqueue(new Callback<IRegistreDisease.RegistreDisease>() {
            @Override
            public void onResponse(Call<IRegistreDisease.RegistreDisease> call, Response<IRegistreDisease.RegistreDisease> response) {
                if (response.isSuccessful()) {
                    Log.e(TAG, "Respuesta exitosa call_1");
                    mSaveDisease = null;
                    mSaveDisease = response.body();
                    postExecutionEnviarData();
                } else {
                    showLayoutDialog();
                    Utils.generarAlerta(DiseaseRegistry.this, getString(R.string.txt_atencion), getString(R.string.text_error_metodo));
                    Log.e(TAG, "Error en la petición call_1");
                }
            }

            @Override
            public void onFailure(Call<IRegistreDisease.RegistreDisease> call, Throwable t) {
                showLayoutDialog();
                Utils.generarAlerta(DiseaseRegistry.this, getString(R.string.txt_atencion), getString(R.string.text_error_metodo));
                t.printStackTrace();
                Log.e(TAG, "Error en la petición");
            }
        });
    }

    public void postExecutionEnviarData() {
        showLayoutDialog();
        if (mSaveDisease != null) {
            if (mSaveDisease.getCodigo() == 0) {
               finish();
            } else {
                Utils.generarAlerta(DiseaseRegistry.this, getString(R.string.txt_atencion), mSaveDisease.getRespuesta());
            }
        } else {
            Utils.generarAlerta(DiseaseRegistry.this, getString(R.string.txt_atencion), getString(R.string.text_error_metodo));
        }
    }


    public void SaveDisease(View v )
    {
        Log.e(TAG,"vincular");
        if(adapter != null){
            int positionSelect = adapter.getRow_index();
            if(positionSelect != -1) {
                idEnf = adapter.getIdEnf();
                NombreEnf=  adapter.getNombreEnf();
                DescripEnf = adapter.getDescripEnf();
                generateAlertDialog("Registro de Patología", "Se registrará la patología " + NombreEnf + "-" +DescripEnf);

                Log.e(TAG,"datos select id: "+idEnf+"name "  + NombreEnf+"DescripEnf " + DescripEnf);
            }else {
                snack(v, "Por favor, Seleccione Patología");
            }
        }else {
            snack(v, "Por favor, Seleccione Patología");
        }
    }

    public void generateAlertDialog(String Title, String message) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("" + Title);
        alert.setMessage("" + message);
        alert.setCancelable(false);
        alert.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface alert, int id) {
                restartLoading(1); //descomentar cuando funcione el metodo
            }
        });
        alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
            }
        });
        alert.show();
    }

    public void snack(View view, String mensaje){
        Snackbar.make(view, "\n" + mensaje, Snackbar.LENGTH_LONG)
                .setAction("Ok", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                })
                .setActionTextColor(this.getResources().getColor(R.color.greenMenu))
                .show();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if(call_1!=null && !call_1.isCanceled()) {
            call_1.cancel();
        }
        if(call_2!=null && !call_2.isCanceled()) {
            call_2.cancel();
        }
        Log.e(TAG,"onDestroy.........");
    }

}
