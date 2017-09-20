package com.grupocisc.healthmonitor.Pressure.activities;

/**
 * Created by Mariuxi on 13/01/2017.
 */


import android.animation.Animator;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.grupocisc.healthmonitor.HealthMonitorApplicattion;
import com.grupocisc.healthmonitor.Insulin.TextValidator;
import com.grupocisc.healthmonitor.R;
import com.grupocisc.healthmonitor.Utils.Utils;
import com.grupocisc.healthmonitor.Weight.activities.WeightRegistyActivity;
import com.grupocisc.healthmonitor.entities.IPressure;
//import com.satsuware.usefulviews.LabelledSpinner;
import com.grupocisc.healthmonitor.entities.IRegCrtPacient;
import com.grupocisc.healthmonitor.entities.IRecomenPressure;
import com.satsuware.usefulviews.LabelledSpinner;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
//import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;


import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PressureRegistyActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener
                                                                        , TimePickerDialog.OnTimeSetListener
                                                                        ,LabelledSpinner.OnItemChosenListener  {

    private String TAG = "PressureRegistyActivity";
    private int year, month, day, hour, minute,second;
    private LinearLayout lyt_fecha, lyt_hora;
    private TextView txt_fecha, txt_hora;
    private EditText txt_maxpressure, txt_minpressure, txt_observation;//se cambia concentracion por peso
    private FloatingActionButton fab;
    private Toolbar toolbar;
    public ProgressDialog Dialog;
    private Call<IRecomenPressure.Objeto> call_2;

    private static IRecomenPressure.Objeto rowsRecomend;
    private IPressure row;
    private boolean isUpdate = false;
    private int idAuxPresure = 0;
    private static final String enviadoServer = "false";
    private static final String operacionU = "U";
    private static final String operacionI = "I";
    private LinearLayout layoutMain;
    private RelativeLayout layoutButtons;
    private RelativeLayout layoutContent;
    private  CardView cardReg;
    private EditText txt_concentration;
    private String selectTextSpinner;
    private int selectPosicionSpinner;
    private LabelledSpinner labelledSpinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TRANSITIONS
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ){
            TransitionInflater inflater = TransitionInflater.from( this );
            Transition transition = inflater.inflateTransition( R.transition.transitions );
            getWindow().setSharedElementEnterTransition(transition);
            Transition transition1 = getWindow().getSharedElementEnterTransition();
            transition1.addListener(new Transition.TransitionListener() {
                @Override
                public void onTransitionStart(Transition transition) {
                }
                @Override
                public void onTransitionEnd(Transition transition) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        //TransitionManager.beginDelayedTransition(mRoot, new Slide());
                    }
                    //lyt_contenedor.setVisibility( View.VISIBLE );
                    //img_curso.setVisibility( View.VISIBLE );
                }
                @Override
                public void onTransitionCancel(Transition transition) {
                }
                @Override
                public void onTransitionPause(Transition transition) {
                }
                @Override
                public void onTransitionResume(Transition transition) {
                }
            });
        }
        setContentView(R.layout.pressure_registy_activity);
        Dialog = new ProgressDialog(this);
        layoutMain = (LinearLayout) findViewById(R.id.layoutMain);
        layoutButtons = (RelativeLayout) findViewById(R.id.layoutButtons);
        layoutContent = (RelativeLayout) findViewById(R.id.layoutContent);
        cardReg       = (CardView) findViewById(R.id.cardReg);

        txt_concentration = (EditText) findViewById(R.id.txt_concentration);
        txt_maxpressure = (EditText) findViewById(R.id.txt_maxpressure);
        txt_minpressure = (EditText) findViewById(R.id.txt_minpressure);//se cambia concentracion por peso
        txt_observation = (EditText) findViewById(R.id.txt_observation);

        lyt_fecha = (LinearLayout) findViewById(R.id.lyt_fecha);
        txt_fecha = (TextView) findViewById(R.id.txt_fecha);
        lyt_hora = (LinearLayout) findViewById(R.id.lyt_hora);
        txt_hora = (TextView) findViewById(R.id.txt_hora);
        setToolbar();
        inicializarFechaHora();
        setearMaterialBetterSpinner();

        //obtener objeto de la actividad anterior
        if(savedInstanceState != null){
            row = (IPressure) savedInstanceState.getSerializable("car");
            actualizarData();
        }
        else {
            if (getIntent() != null && getIntent().getExtras() != null && getIntent().getExtras().getSerializable("car") != null) {
                row = (IPressure) getIntent().getExtras().getSerializable("car");
                actualizarData();
            }
        }

        //CONCENRTACION
        txt_concentration.addTextChangedListener(new TextValidator(txt_concentration) {
            @Override
            public void validate(EditText editText, String text) {
                if (!text.isEmpty() && Utils.isNumeric(text))

                {
                    if (Float.parseFloat(text) > 195)
                        txt_concentration.setError("El valor de su pulso no puede ser mayor a 195 PPM. Por favor Verifique");
                    if (Float.parseFloat(text) < 39)
                        txt_concentration.setError("El valor de su pulso no puede ser menor a 39 PPM.  Por favor Verifique");
                }
            }
        });


        lyt_fecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                callCalendar();
            }
        });
        lyt_hora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                callCalendarTime();
            }
        });


        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validarDataPressure(view);

            }
        });


        txt_maxpressure.addTextChangedListener(new TextValidator(txt_maxpressure) {
            @Override
            public void validate(EditText editText, String text) {
                if (!text.isEmpty() && Utils.isNumeric(text))

                {
                    if (Float.parseFloat(text) > 230)
                        txt_maxpressure.setError("El valor de su presion sistólica no puede ser mayor a 230 Por favor Verifique");
                    if (Float.parseFloat(text) < 50)
                        txt_maxpressure.setError("El valor su presion sistólica no puede ser menor a 50.  Por favor Verifique");
                }
            }
        });

        txt_minpressure.addTextChangedListener(new TextValidator(txt_minpressure) {
            @Override
            public void validate(EditText editText, String text) {
                if (!text.isEmpty() && Utils.isNumeric(text))

                {
                    if (Float.parseFloat(text) > 135)
                        txt_minpressure.setError("El valor de su presion diastólica no puede ser mayor a 135 Por favor Verifique");
                    if (Float.parseFloat(text) < 35)
                        txt_minpressure.setError("El valor su presion diastólica no puede ser menor a 35.  Por favor Verifique");
                }
            }
        });
    }

    public void setearMaterialBetterSpinner() {

        labelledSpinner = (LabelledSpinner) findViewById(R.id.spinner_planets);
        labelledSpinner.setColor(R.color.colorPrimaryDark);
        labelledSpinner.setItemsArray(R.array.pluse_array);
        labelledSpinner.setDefaultErrorEnabled(true);
        labelledSpinner.setDefaultErrorText("Por favor seleccionar una opción.");  // Displayed when first item remains selected
        labelledSpinner.setOnItemChosenListener(this);
        //SETEAR LA POSICION DEL SPINNER QUE APARESCA SELECIONADO
        labelledSpinner.setSelection(0, true);
    }
    //se ejecuta el selecionar el spinner
    @Override
    public void onItemChosen(View labelledSpinner, AdapterView<?> adapterView, View itemView, int position, long id) {
        String selectedText = adapterView.getItemAtPosition(position).toString();
        switch (labelledSpinner.getId()) {
            case R.id.spinner_planets:
                //Toast.makeText(this, "Selected: " + selectedText, Toast.LENGTH_SHORT).show();
                selectTextSpinner = selectedText;
                selectPosicionSpinner  = position;
                break;
            // If you have multiple LabelledSpinners, you can add more cases here
        }
    }

    //se ejecuta el selecionar el spinner
    @Override
    public void onNothingChosen(View labelledSpinner, AdapterView<?> adapterView) {
        // Do something here
    }

    public void actualizarData(){
        isUpdate = true;
        idAuxPresure = row.getId();
        Log.e(TAG, "ACTULIZAR DATA:"+ row.getId() + row.getObservacion());
        txt_fecha.setText(""+row.getFecha());
        txt_hora.setText(""+row.getHora());
        txt_maxpressure.setText(""+row.getMaxPressure());
        txt_minpressure.setText(""+row.getMinPressure());
        txt_observation.setText(""+row.getObservacion());
        txt_concentration.setText(""+ row.getPulso() );


        String recuSp = row.getMedido();
        String[] arrayMedic = getResources().getStringArray(R.array.pluse_array);
        int posi = 0;
        //selecciona el valor del spinner para presentarlo


        for (int i=0 ; i < arrayMedic.length ; i++ ){
            if (arrayMedic[i].equals(recuSp)) {
                posi = i;
            }
        }
        labelledSpinner.setSelection(posi, true);
    }

    //se ejecuta al seleccionar el icon back del toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home) {
            callBackPressedActivity();
        }
        return super.onOptionsItemSelected(item);
    }

    //se ejecuta al dar click en el boton back del dispositivo
    @Override
    public void onBackPressed() {
        callBackPressedActivity();
        //super.onBackPressed();
    }

    public void callBackPressedActivity(){
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ){
            //pager.setVisibility( View.INVISIBLE );
            //TransitionManager.beginDelayedTransition(mRoot, new Slide());
        }
        super.onBackPressed();
    }

    public void setToolbar() {
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
        month = c.get(Calendar.MONTH) + 1;
        day = c.get(Calendar.DAY_OF_MONTH);
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);
        second = c.get(Calendar.SECOND);
        //setear fecha
        String mes = month < 10 ? "0" + month : "" + month;
        String dia = day < 10 ? "0" + day : "" + day;

        String date = "" + dia + "/" + mes + "/" + year;
        txt_fecha.setText(date);
        //setear hora
        String hourString = hour < 10 ? "0" + hour : "" + hour;
        String minuteString = minute < 10 ? "0" + minute : "" + minute;
        String secondString = second < 10 ? "0"+second : ""+second;
        String time = ""+hourString+":"+minuteString+":"+secondString;
        txt_hora.setText(time);
    }

    //llamar calendario
    public void callCalendar() {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.setMaxDate(now);
        dpd.setThemeDark(false);//tema dark
        dpd.vibrate(true);//vibrar al selecionar
        dpd.dismissOnPause(false);
        dpd.showYearPickerFirst(false);//aparece el año primero
        dpd.setVersion(false ? DatePickerDialog.Version.VERSION_2 : DatePickerDialog.Version.VERSION_1);//false aparece v1 true v2
        if (false) {
            dpd.setAccentColor(Color.parseColor("#9C27B0"));//color customizado
        }
        if (false) {
            dpd.setTitle("DatePicker Title");//titulo cutomizado
        }
        if (false) {// rango de fechas
            Calendar date1 = Calendar.getInstance();
            Calendar date2 = Calendar.getInstance();
            date2.add(Calendar.WEEK_OF_MONTH, -1);
            Calendar date3 = Calendar.getInstance();
            date3.add(Calendar.WEEK_OF_MONTH, 1);
            Calendar[] days = {date1, date2, date3};
            dpd.setHighlightedDays(days);
        }
        if (false) {
            Calendar[] days = new Calendar[13];
            for (int i = -6; i < 7; i++) {
                Calendar day = Calendar.getInstance();
                day.add(Calendar.DAY_OF_MONTH, i * 2);
                days[i + 6] = day;
            }
            dpd.setSelectableDays(days);
        }
        dpd.show(getFragmentManager(), "Datepickerdialog");
    }

    public void callCalendarTime() {
        Calendar now = Calendar.getInstance();
        TimePickerDialog tpd = TimePickerDialog.newInstance(
                this,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                true /*mode24Hours.isChecked()*/
        );
        tpd.setThemeDark(false);//tema dark
        tpd.vibrate(true);
        tpd.dismissOnPause(false);

        tpd.enableSeconds(true);
        tpd.setVersion(false ? TimePickerDialog.Version.VERSION_2 : TimePickerDialog.Version.VERSION_1);
        if (false) {
            tpd.setAccentColor(Color.parseColor("#9C27B0"));
        }
        if (false) {
            tpd.setTitle("TimePicker Title");
        }
        if (false) { //limitSelectableTimes
            tpd.setTimeInterval(3, 5, 10);
        }
        tpd.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                Log.d("TimePicker", "Dialog was cancelled");
            }
        });
        tpd.show(getFragmentManager(), "Timepickerdialog");
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

    //se ejecuta al dar ACEPTAR en el dialogo
    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String mes = "" + ((monthOfYear + 1) < 10 ? "0" + (monthOfYear + 1) : "" + (monthOfYear + 1));
        String dia = dayOfMonth < 10 ? "0" + dayOfMonth : "" + dayOfMonth;
        String date = "" + dia + "/" + mes + "/" + year;
        //String date = ""+dayOfMonth+"/"+(++monthOfYear)+"/"+year;
        txt_fecha.setText(date);
    }

    //se ejecuta al dar CANCELAR en el dialogo
    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        String hourString = hourOfDay < 10 ? "0" + hourOfDay : "" + hourOfDay;
        String minuteString = minute < 10 ? "0" + minute : "" + minute;
        String secondString = second < 10 ? "0" + second : "" + second;
        String time = "" + hourString + ":" + minuteString+":"+secondString;
        txt_hora.setText(time);
    }
    //VALIDACION DE DATOS
    public void validarDataPressure(View view) {
        //se cambia concentracion por peso
        String fecha = txt_fecha.getText().toString();
        String hora = txt_hora.getText().toString();
        String maxpressureS =  txt_maxpressure.getText().toString() ;
        String minpressureS =  txt_minpressure.getText().toString() ;
        String observacion = txt_observation.getText().toString();
        if(!txt_maxpressure.getText().toString().isEmpty() &&Utils.isNumeric(txt_maxpressure.getText().toString())&&
                !txt_minpressure.getText().toString().isEmpty() &&Utils.isNumeric(txt_minpressure.getText().toString())   ){
            float maxpressure = Float.parseFloat(txt_maxpressure.getText().toString());//Float.parseFloat(txt_concentration.getText().toString())
            float minpressure = Float.parseFloat(txt_minpressure.getText().toString());
            if(validaRangos(maxpressure,minpressure)){

                validarDataPulso(view, idAuxPresure ,maxpressureS, minpressureS, fecha, hora, observacion);

            } else {
                Snackbar.make(view, "\n" + "Su P.A esta mal ingresada: "+"\n"+getString(R.string.ingrese), Snackbar.LENGTH_LONG)
                        .setAction("Ok", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                            /*Intent it = new Intent(Settings.ACTION_WIFI_SETTINGS);
                            startActivity(it);*/
                            }
                        })
                        .setActionTextColor(this.getResources().getColor(R.color.greenMenu))
                        .show();
            }
        }else{
            Snackbar.make(view, "\n" + "Su P.A esta mal ingresada: "+"\n"+getString(R.string.ingrese), Snackbar.LENGTH_LONG)
                    .setAction("Ok", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            /*Intent it = new Intent(Settings.ACTION_WIFI_SETTINGS);
                            startActivity(it);*/
                        }
                    })
                    .setActionTextColor(this.getResources().getColor(R.color.greenMenu))
                    .show();
        }

    }

    public void validarDataPulso (View view, int idAuxPresure, String maxpressureS, String minpressureS, String fecha, String hora, String observacion){
        String medido = selectTextSpinner;
        String pulso = txt_concentration.getText().toString();

        if (pulso.length() > 0 && Utils.isNumeric(pulso)) {
            if (pulso.length() > 0 &&  selectPosicionSpinner != 0 && Float.parseFloat(pulso) <= 195 && Float.parseFloat(pulso) >= 39) {
                try {

                    //GUARDAR O ACTUALIZAR
                    if(isUpdate){
                        //SI AUN NO AH SIDO ENVIADO AL SERVIDOR INSERTA
                        String  operacionIU = "";
                        if(row.getIdBdServer() > 0 ){
                            operacionIU = operacionU;
                        }else{
                            operacionIU = operacionI;
                        }
                        updateDataPressureDB(idAuxPresure ,maxpressureS, minpressureS,pulso,medido , fecha, hora, observacion, operacionIU);
                    }else {
                        saveDataPressureDB(maxpressureS, minpressureS,pulso,medido, fecha, hora, observacion);
                    }
                    nextAction();//ANIMATION

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {

                Snackbar.make(view, "\n" + getString(R.string.ingrese), Snackbar.LENGTH_LONG)
                        .setAction("Ok", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                            /*Intent it = new Intent(Settings.ACTION_WIFI_SETTINGS);
                            startActivity(it);*/
                            }
                        })
                        .setActionTextColor(this.getResources().getColor(R.color.greenMenu))
                        .show();
            }
        } else {

            Snackbar.make(view, "\n" + getString(R.string.ingrese), Snackbar.LENGTH_LONG)
                    .setAction("Ok", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                /*Intent it = new Intent(Settings.ACTION_WIFI_SETTINGS);
                                startActivity(it);*/
                        }
                    })
                    .setActionTextColor(this.getResources().getColor(R.color.greenMenu))
                    .show();
        }


    }

    //GUARDOS DATOS EN LA TABLA BDSQLITE
    public  void saveDataPressureDB(String maxpressure, String minpressure,String pulso, String medido, String fecha, String hora, String observacion){
        try {
            //setear datos al objeto y guardar y BD
            Utils.DbsavePressureFromDatabase(maxpressure,
                    minpressure,
                    pulso,
                    medido,
                    fecha,
                    hora,
                    observacion,
                    enviadoServer,
                    operacionI,
                    HealthMonitorApplicattion.getApplication().getPressureDao());

            Utils.generateToast(this, getResources().getString(R.string.txt_guardado));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //ACTUALIZAR DATOS EN LA TABLA BDSQLITE
    public  void updateDataPressureDB(int idAuxPresure, String maxpressure, String minpressure, String pulso, String medido, String fecha, String hora, String observacion,String operacion){
        try {
            //setear datos al objeto y guardar y BD
            Utils.UpdatePressureFromDatabase(idAuxPresure,
                                            maxpressure,
                                            minpressure,
                                            pulso,
                                            medido,
                                            fecha,
                                            hora,
                                            observacion,
                                            enviadoServer,
                                            operacion,
                                            HealthMonitorApplicattion.getApplication().getPressureDao());

            Utils.generateToast(this, getResources().getString(R.string.txt_actulizado));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private boolean validaRangos(float sistolica, float Diastolica ) {
        if (sistolica>= 50 & sistolica <=90 )
        {
            if (Diastolica>= 35 & Diastolica <=60)
            {
                return true;
            }
        }
        if (sistolica>= 91 & sistolica <=100 )
        {
            if (Diastolica>= 61 & Diastolica <=70)
            {
                return true;
            }
        }
        if (sistolica>= 101 & sistolica <=130 )
        {
            if (Diastolica>= 71 & Diastolica <=85)
            {
                return true;
            }
        }
        if (sistolica>= 131 & sistolica <=140 )
        {
            if (Diastolica>= 86 & Diastolica <=90)
            {
                return true;
            }
        }
        if (sistolica>= 141 & sistolica <=160 )
        {
            if (Diastolica>= 91 & Diastolica <=110)
            {
                return true;
            }
        }
        if (sistolica>= 161 & sistolica <=230 )
        {
            if (Diastolica>= 111 & Diastolica <=135)
            {
                return true;
            }
        }
        return false;
    }

////////////////////////////REOCMENDACIONES OJOOOOOO

    private void Recomendacion() {

        String user_email = Utils.getEmailFromPreference(this);
        //enviar webservice
        //APUNTANDO AA METODO CISC
        IRecomenPressure RecomenPressure = HealthMonitorApplicattion.getApplication().getmRestPushAdapterRecpre().create(IRecomenPressure.class);
        call_2 = RecomenPressure.RecomenPressure(user_email);
        Log.e(TAG,"estoy aki ");
        call_2.enqueue(new Callback<IRecomenPressure.Objeto>() {
            @Override
            public void onResponse(Call<IRecomenPressure.Objeto> call, Response<IRecomenPressure.Objeto> response) {
                if (response.isSuccessful()) {
                    Log.e(TAG, "Respuesta exitosa recomendacion");
                    rowsRecomend = null;
                    rowsRecomend = response.body();
                    postExecutionRecom();
                    Log.e(TAG, "Respuesta exitosa recomendacion post ejecucion");
                } else {
                    Utils.generarAlerta(com.grupocisc.healthmonitor.Pressure.activities.PressureRegistyActivity.this, getString(R.string.txt_atencion), getString(R.string.text_error_metodo));
                    Log.e(TAG, " Recomendacion.........Error en la petición");
                }
            }

            @Override
            public void onFailure(Call<IRecomenPressure.Objeto> call, Throwable t) {
                Utils.generarAlerta(com.grupocisc.healthmonitor.Pressure.activities.PressureRegistyActivity.this, getString(R.string.txt_atencion), getString(R.string.text_error_metodo) + " o revise su conexión a internet");
                t.printStackTrace();
                Log.e(TAG, "mmmmmmmmmmmm");
            }
        });
    }
    public void postExecutionRecom() {
        if (rowsRecomend != null) {
            rowsRecomend.getRespuesta();
            generarAlerta("Recomendación", rowsRecomend.getMensaje());
            Log.e(TAG, "ooooook recomendacion}" + rowsRecomend.getMensaje());
            }
    }

    public void generarAlerta(String Title, String msm) {
      //  if (!(com.grupocisc.healthmonitor.Pressure.activities.PressureRegistyActivity.this).isFinishing()) {
            try {
                Log.e(TAG, "ooooook recomendacion alerta" );
                //show dialog
                AlertDialog.Builder alert = new AlertDialog.Builder(com.grupocisc.healthmonitor.Pressure.activities.PressureRegistyActivity.this);
                alert.setTitle("" + Title);
                alert.setMessage("" + msm);
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                        finish();
                    }
                });
                alert.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
       // }
    }

    private void nextAction() {
        circleAnimationOpen();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //finalizar matar la actividad
                finish();
            }
        }, 150);
    }


    private void circleAnimationOpen() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            int x = layoutContent.getRight();
            int y = layoutContent.getBottom();
            int startRadius = 0;
            int endRadius = (int) Math.hypot(layoutMain.getWidth(), layoutMain.getHeight());
            Animator anim = ViewAnimationUtils.createCircularReveal(layoutButtons, x, y, startRadius, endRadius);
            layoutButtons.setVisibility(View.VISIBLE);
            cardReg.setVisibility(View.INVISIBLE);
            anim.start();
        }
    }
    private void circleAnimationClose() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            int x = layoutButtons.getRight();
            int y = layoutButtons.getBottom();
            int startRadius = Math.max(layoutContent.getWidth(), layoutContent.getHeight());
            int endRadius = 0;
            Animator anim = ViewAnimationUtils.createCircularReveal(layoutButtons, x, y, startRadius, endRadius);
            anim.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {
                }
                @Override
                public void onAnimationEnd(Animator animator) {
                    layoutButtons.setVisibility(View.GONE);
                    cardReg.setVisibility(View.VISIBLE);
                }
                @Override
                public void onAnimationCancel(Animator animator) {
                }
                @Override
                public void onAnimationRepeat(Animator animator) {
                }
            });
            anim.start();
        }
    }


}

