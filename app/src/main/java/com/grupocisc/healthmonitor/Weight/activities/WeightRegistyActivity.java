package com.grupocisc.healthmonitor.Weight.activities;


import android.animation.Animator;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
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
import com.grupocisc.healthmonitor.Pulse.activities.PulseRegistyActivity;
import com.grupocisc.healthmonitor.R;
import com.grupocisc.healthmonitor.Utils.SharedPreferencesManager;
import com.grupocisc.healthmonitor.Utils.Utils;
import com.grupocisc.healthmonitor.entities.IRegCrtPacient;
import com.grupocisc.healthmonitor.entities.IWeight;
import com.satsuware.usefulviews.LabelledSpinner;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
import com.grupocisc.healthmonitor.Insulin.TextValidator;

//import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;


import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeightRegistyActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener
        ,TimePickerDialog.OnTimeSetListener
        ,LabelledSpinner.OnItemChosenListener {

    private String TAG = "WeightRegistyActivity";
    private int year, month, day, hour, minute,second;
    private LinearLayout lyt_fecha ,lyt_hora;
    private TextView txt_fecha, txt_hora ;
    private EditText  txt_peso , txt_observation,txt_tmb,txt_dmo,txt_agua,txt_grasa,txt_masamuscular;//se cambia concentracion por peso
    private FloatingActionButton fab ;
    private Toolbar toolbar;
    private Call<IRegCrtPacient.RegCrtPacient> call_1;
    private IRegCrtPacient.RegCrtPacient mSaveCrtPacient;
    private boolean isUpdate = false;
    private int idAuxWeight = 0;
    private IWeight row;
    private static final String enviadoServer = "false";
    private static final String operacionU = "U";
    private static final String operacionI = "I";
    private LinearLayout layoutMain;
    private RelativeLayout layoutButtons;
    private RelativeLayout layoutContent;
    private  CardView cardReg;

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
        setContentView(R.layout.weight_registy_activity);
        txt_peso = (EditText) findViewById(R.id.txt_peso);//se cambia concentracion por peso
        txt_observation = (EditText) findViewById(R.id.txt_observation);
        txt_tmb = (EditText) findViewById(R.id.txt_tmb);
        txt_dmo = (EditText) findViewById(R.id.txt_dmo);
        txt_agua = (EditText) findViewById(R.id.txt_agua);
        txt_grasa = (EditText) findViewById(R.id.txt_grasa);
        txt_masamuscular = (EditText) findViewById(R.id.txt_masamuscular);
        lyt_fecha = (LinearLayout) findViewById(R.id.lyt_fecha);
        txt_fecha = (TextView) findViewById(R.id.txt_fecha);
        lyt_hora = (LinearLayout) findViewById(R.id.lyt_hora);
        txt_hora = (TextView) findViewById(R.id.txt_hora);


        layoutMain = (LinearLayout) findViewById(R.id.layoutMain);
        layoutButtons = (RelativeLayout) findViewById(R.id.layoutButtons);
        layoutContent = (RelativeLayout) findViewById(R.id.layoutContent);
        cardReg       = (CardView) findViewById(R.id.cardReg);

        setToolbar();
        inicializarFechaHora();

        //obtener objeto de la actividad anterior
        if(savedInstanceState != null){
            row = (IWeight) savedInstanceState.getSerializable("car");
            actualizarData();
        }
        else {
            if (getIntent() != null && getIntent().getExtras() != null && getIntent().getExtras().getSerializable("car") != null) {
                row = (IWeight) getIntent().getExtras().getSerializable("car");
                actualizarData();
            }
        }

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
                saveDataWeightDB(view);
            }
        });


        txt_peso.addTextChangedListener(new TextValidator(txt_peso) {
            @Override
            public void validate(EditText editText, String text) {
                if (!text.isEmpty() && Utils.isNumeric(text))

                {
                    if (Float.parseFloat(text) > 200)
                        txt_peso.setError("El valor de su peso no puede ser mayor a 200 KG Por favor Verifique");
                    if (Float.parseFloat(text) < 35)
                        txt_peso.setError("El valor de su peso no puede ser menor a 35 KG.  Por favor Verifique");
                }
            }
        });

        txt_tmb.addTextChangedListener(new TextValidator(txt_tmb) {
            @Override
            public void validate(EditText editText, String text) {
                if (!text.isEmpty() && Utils.isNumeric(text))

                {
                    if (Float.parseFloat(text) > 1900)
                        txt_tmb.setError("El valor de su TMB no puede ser mayor a 1900  Por favor Verifique");
                    if (Float.parseFloat(text) < 1400)
                        txt_tmb.setError("El valor de su TMB no puede ser menor a 1400 .  Por favor Verifique");
                }
            }
        });

        txt_dmo.addTextChangedListener(new TextValidator(txt_dmo) {
            @Override
            public void validate(EditText editText, String text) {
                if (!text.isEmpty() && Utils.isNumeric(text))

                {
                    if (Float.parseFloat(text) > 3)
                        txt_dmo.setError("El valor de su DMO no puede ser mayor a 3  Por favor Verifique");
                    if (Float.parseFloat(text) < -3)
                        txt_dmo.setError("El valor de su DMO no puede ser menor a -3 .  Por favor Verifique");
                }
            }
        });

        txt_agua.addTextChangedListener(new TextValidator(txt_agua) {
            @Override
            public void validate(EditText editText, String text) {
                if (!text.isEmpty() && Utils.isNumeric(text))

                {
                    if (Float.parseFloat(text) > 100)
                        txt_agua.setError("El porcentaje de agua no puede ser mayor a 100  Por favor Verifique");
                    if (Float.parseFloat(text) < 1)
                        txt_agua.setError("El porcentaje de agua no puede ser menor a 1 .  Por favor Verifique");
                }
            }
        });

        txt_grasa.addTextChangedListener(new TextValidator(txt_grasa) {
            @Override
            public void validate(EditText editText, String text) {
                if (!text.isEmpty() && Utils.isNumeric(text))

                {
                    if (Float.parseFloat(text) > 100)
                        txt_grasa.setError("El porcentaje de grasa no puede ser mayor a 100  Por favor Verifique");
                    if (Float.parseFloat(text) < 1)
                        txt_grasa.setError("El porcentaje de grasa no puede ser menor a 1 .  Por favor Verifique");
                }
            }
        });

        txt_masamuscular.addTextChangedListener(new TextValidator(txt_masamuscular) {
            @Override
            public void validate(EditText editText, String text) {
                if (!text.isEmpty() && Utils.isNumeric(text))

                {
                    if (Float.parseFloat(text) > 100)
                        txt_masamuscular.setError("El valor de su masa muscular no puede ser mayor a 100  Por favor Verifique");
                    if (Float.parseFloat(text) < 1)
                        txt_masamuscular.setError("El valor de su masa muscular no puede ser menor a 1 .  Por favor Verifique");
                }
            }
        });

    }

    public void actualizarData(){
        isUpdate = true;
        idAuxWeight = row.getId();
        Log.e(TAG, "ACTULIZAR DATA:"+ row.getId() + row.getObservacion());
        txt_fecha.setText(""+row.getFecha());
        txt_hora.setText(""+row.getHora());
        txt_peso.setText(""+row.getPeso());
        txt_observation.setText(""+row.getObservacion());
        txt_masamuscular.setText(""+row.getMasamuscular());
        txt_tmb.setText(""+row.getTmb());
        txt_dmo.setText(""+row.getDmo());
        txt_agua.setText(""+row.getPorcentajeAgua());
        txt_grasa.setText(""+row.getPorcentajeGrasa());

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

    public void setToolbar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false); //desactivar title
        toolbar.setNavigationIcon(R.mipmap.back); //buton back tollbar
        toolbar.setTitleTextColor(getResources().getColor(R.color.white)); //color tollbar title

    }

    private void inicializarFechaHora(){

        //obtener fechay hroa de Calendar
        Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);
        second = c.get(Calendar.SECOND);
        //setear fecha
        String mes= ""+((month+1)<10? "0"+(month+1) : ""+(month+1));
        String dia = day <10 ? "0"+day : ""+day;
        String date = ""+dia+"/"+mes+"/"+year;
        txt_fecha.setText(date);

        //setear hora
        String hourString = hour < 10 ? "0"+hour : ""+hour;
        String minuteString = minute < 10 ? "0"+minute : ""+minute;
        String secondString = second < 10 ? "0"+second : ""+second;
        String time = ""+hourString+":"+minuteString+":"+secondString;
        txt_hora.setText(time);

    }

    //llamar calendario
    public void callCalendar(){
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

    public void callCalendarTime(){
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
        if(dpd != null)
            dpd.setOnDateSetListener(this);
        TimePickerDialog tpd = (TimePickerDialog) getFragmentManager().findFragmentByTag("Timepickerdialog");
        if(tpd != null)
            tpd.setOnTimeSetListener(this);
    }

    //se ejecuta al dar ACEPTAR en el dialogo
    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String mes= ""+((monthOfYear+1)<10? "0"+(monthOfYear+1) : ""+(monthOfYear+1));
        String dia = dayOfMonth <10 ? "0"+dayOfMonth : ""+dayOfMonth;
        String date = ""+dia+"/"+mes+"/"+year;
        txt_fecha.setText(date);
    }

    //se ejecuta al dar CANCELAR en el dialogo
    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        String hourString = hourOfDay < 10 ? "0"+hourOfDay : ""+hourOfDay;
        String minuteString = minute < 10 ? "0"+minute : ""+minute;
        String secondString = second < 10 ? "0"+second : ""+second;
        String time = ""+hourString+":"+minuteString+":"+secondString;
        txt_hora.setText(time);
    }


    //se ejecuta el selecionar el spinner
    @Override
    public void onItemChosen(View labelledSpinner, AdapterView<?> adapterView, View itemView, int position, long id) {
        String selectedText = adapterView.getItemAtPosition(position).toString();
        switch (labelledSpinner.getId()) {
            case R.id.spinner_planets:
                Toast.makeText(this, "Selected: " + selectedText, Toast.LENGTH_SHORT).show();
                //selectTextSpinner = selectedText;
                break;
            // If you have multiple LabelledSpinners, you can add more cases here
        }
    }
    //se ejecuta el selecionar el spinner
    @Override
    public void onNothingChosen(View labelledSpinner, AdapterView<?> adapterView) {
        // Do something here
    }

    //GUARDOS DATOS EN LA TABLA BD
    public  void saveDataWeightDB(View view){

        String peso =  txt_peso.getText().toString() ;//se cambia concentracion por peso
        String fecha =  txt_fecha.getText().toString() ;
        String hora = txt_hora.getText().toString();
        String observacion = txt_observation.getText().toString() ;
        String tmb = txt_tmb.getText().toString() ;
        String dmo = txt_dmo.getText().toString() ;
        String agua = txt_agua.getText().toString() ;
        String grasa = txt_grasa.getText().toString() ;
        String masamuscular = txt_masamuscular.getText().toString() ;
        //validar campos llenos
        if(peso.length()>0) {
            if (!peso.isEmpty() && Utils.isNumeric(peso)){
            if ((Float.parseFloat(txt_peso.getText().toString()) >= 35 && Float.parseFloat(txt_peso.getText().toString()) <= 200 && Utils.isNumeric(peso) )
                    || (observacion.equals(""))
                    ) {

                 if (validaTmb(tmb)==true&&validaDmo(dmo)==true && validaAgua(agua)==true&&validaGrasa(grasa)==true&&validamasa(masamuscular)==true) {
                     try {
                         //setear datos al objeto y guardar y BD
                         GuardaPeso();
                     } catch (Exception e) {
                         e.printStackTrace();
                     }
                     finish();
                 }else{
                     Snackbar.make(view, "\n" + getString(R.string.ingrese), Snackbar.LENGTH_LONG)
                             .setAction("Ok", new View.OnClickListener() {
                                 @Override
                                 public void onClick(View v) {
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
                            }
                        })
                        .setActionTextColor(this.getResources().getColor(R.color.greenMenu))
                        .show();
            }
        }else{
                Snackbar.make(view, "\n" + getString(R.string.ingrese), Snackbar.LENGTH_LONG)
                        .setAction("Ok", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                            }
                        })
                        .setActionTextColor(this.getResources().getColor(R.color.greenMenu))
                        .show();
            }
        }else{
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

    public void GuardaPeso(){
        saveDataWeightDataBase();
    }

    //GUARDOS DATOS EN LA TABLA BD
    public  void saveDataWeightDataBase(){
        float tmb, porcentajeAgua, porcentajeGrasa, dmo, masaMuscular;
        String observacion;
        float peso = Float.parseFloat(txt_peso.getText().toString());
        if (txt_tmb.getText().toString().equals("")) {
            tmb = 0;
        } else {
            tmb = Float.parseFloat(txt_tmb.getText().toString());
        }
        if (txt_agua.getText().toString().equals("")) {
            porcentajeAgua = 0;
        } else {
            porcentajeAgua = Float.parseFloat(txt_agua.getText().toString());
        }
        if (txt_grasa.getText().toString().equals("")) {
            porcentajeGrasa = 0;
        } else {
            porcentajeGrasa = Float.parseFloat(txt_grasa.getText().toString());
        }
        if (txt_dmo.getText().toString().equals("")) {
            dmo = 0;
        } else {
            dmo = Float.parseFloat(txt_dmo.getText().toString());
        }
        if (txt_masamuscular.getText().toString().equals("")) {
            masaMuscular = 0;
        } else {
            masaMuscular = Float.parseFloat(txt_masamuscular.getText().toString());
        }
        if (txt_observation.getText().toString().equals("")) {
            observacion = "";
        } else {
            observacion = txt_observation.getText().toString();
        }
        String fecha = txt_fecha.getText().toString() ;
        String hora =  txt_hora.getText().toString();

        if(isUpdate){
            //SI AUN NO AH SIDO ENVIADO AL SERVIDOR INSERTA
            String  operacionIU = "";
            if(row.getIdBdServer() > 0){
                operacionIU = operacionU;
            }else{
                operacionIU = operacionI;
            }
            updateDataPesoDB(peso, masaMuscular,tmb, dmo, porcentajeAgua, porcentajeGrasa, fecha, hora, observacion, operacionIU);
        }else {
            saveDataPesoDB(peso, masaMuscular,tmb, dmo, porcentajeAgua, porcentajeGrasa, fecha, hora, observacion);
        }
        nextAction();//ANIMATION

    }


    //GUARDOS DATOS EN LA TABLA BDSQLITE
    public  void saveDataPesoDB(float peso, float masaMuscular,float tmb, float dmo, float porcentajeAgua,
                                float porcentajeGrasa, String fecha, String hora, String observacion){
        try {
            //setear datos al objeto y guardar y BD
            Utils.DbsaveWeightFromDatabase(-1,
                    peso,
                    masaMuscular,
                    tmb,
                    dmo,
                    porcentajeAgua,
                    porcentajeGrasa,
                    fecha,
                    hora,
                    observacion,
                    enviadoServer,
                    operacionI,
                    HealthMonitorApplicattion.getApplication().getWeightDao());
            Utils.generateToast(this, getResources().getString(R.string.txt_guardado));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //ACTUALIZAR DATOS EN LA TABLA BDSQLITE
    public  void updateDataPesoDB(float peso, float masaMuscular,float tmb, float dmo, float porcentajeAgua,
                                  float porcentajeGrasa, String fecha, String hora, String observacion, String operacion){
        try {
            //setear datos al objeto y guardar y BD
            Utils.UpdateWeightFromDatabase(idAuxWeight,
                                            peso,
                                            masaMuscular,
                                            tmb,
                                            dmo,
                                            porcentajeAgua,
                                            porcentajeGrasa,
                                            fecha,
                                            hora,
                                            observacion,
                                            enviadoServer,
                                            operacion,
                                            HealthMonitorApplicattion.getApplication().getWeightDao());

            Utils.generateToast(this, getResources().getString(R.string.txt_actulizado));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



        private boolean validaTmb(String tmb) {
        if (tmb.equals("") )
        {
           return true;
        }else
         if(!tmb.isEmpty() &&Utils.isNumeric(tmb)){
             if((Float.parseFloat(tmb.toString()) >= 1400) && (Float.parseFloat(tmb.toString()) <= 1900) ){
                 return true;
             }else
            return false;
        }else
            return false;

        }

    private boolean validaDmo(String dmo) {
        if (dmo.equals("") )
        {
            return true;
        }else
        if(!dmo.isEmpty() &&Utils.isNumeric(dmo)){
            if((Float.parseFloat(dmo.toString()) >= -3) && (Float.parseFloat(dmo.toString()) <= 3) ){
                return true;
            }else
                return false;
        }else
            return false;

    }

    private boolean validaGrasa(String grasa) {
        if (grasa.equals("") )
        {
            return true;
        }else
        if(!grasa.isEmpty() &&Utils.isNumeric(grasa)){
            if((Float.parseFloat(grasa.toString()) >= 1) && (Float.parseFloat(grasa.toString()) <= 100) ){
                return true;
            }else
                return false;
        }else
            return false;

    }

    private boolean validaAgua(String agua) {
        if (agua.equals("") )
        {
            return true;
        }else
        if(!agua.isEmpty() &&Utils.isNumeric(agua)){
            if((Float.parseFloat(agua.toString()) >= 1) && (Float.parseFloat(agua.toString()) <= 100) ){
                return true;
            }else
                return false;
        }else
            return false;

    }
    private boolean validamasa(String masa) {
        if (masa.equals("") )
        {
            return true;
        }else
        if(!masa.isEmpty() &&Utils.isNumeric(masa)){
            if((Float.parseFloat(masa.toString()) >= 1) && (Float.parseFloat(masa.toString()) <= 100) ){
                return true;
            }else
                return false;
        }else
            return false;

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

