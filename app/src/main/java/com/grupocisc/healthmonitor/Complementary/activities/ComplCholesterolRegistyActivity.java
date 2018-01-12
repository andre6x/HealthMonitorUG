package com.grupocisc.healthmonitor.Complementary.activities;

import android.animation.Animator;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.grupocisc.healthmonitor.HealthMonitorApplicattion;
import com.grupocisc.healthmonitor.Insulin.TextValidator;
import com.grupocisc.healthmonitor.R;
import com.grupocisc.healthmonitor.Utils.NotificationHelper;
import com.grupocisc.healthmonitor.Utils.Utils;
import com.grupocisc.healthmonitor.entities.IColesterol;
import com.grupocisc.healthmonitor.entities.IRegCrtPacient;
import com.satsuware.usefulviews.LabelledSpinner;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.sql.SQLException;
import java.util.Calendar;

import retrofit2.Call;

public class ComplCholesterolRegistyActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener
        , LabelledSpinner.OnItemChosenListener {

    private String TAG = "cholesteRegistyActivity";
    private int year, month, day, hour, minute;
    private LinearLayout lyt_fecha, lyt_hora;
    private TextView txt_fecha, txt_hora;
    private EditText txt_colesterol, txt_colesldl, txt_coleshdl, txt_observation,txt_triglycerides;
    private FloatingActionButton fab;
    private String selectTextSpinner;
    private Toolbar toolbar;
    public ProgressDialog Dialog;
    //Envio de datos a la BD
    private static final String enviadoServer = "false";
    //Actualización de Datos
    private boolean isUpdate = false;
    private LinearLayout layoutMain;
    private RelativeLayout layoutButtons;
    private RelativeLayout layoutContent;
    private CardView cardReg;
    private int idAuxPresure = 0;
    private IColesterol row;
    private String operacionU = "U";
    private String operacionI = "I";

    public ComplCholesterolRegistyActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getIntent()!=null)
        {
            NotificationHelper.Current.cancelNotificationFromActivity(this,getIntent().getExtras());
        }

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

        setContentView(R.layout.compl_cholesterol_registy_activity);

        Dialog = new ProgressDialog(this);
        layoutMain = (LinearLayout) findViewById(R.id.layoutMain);
        layoutButtons = (RelativeLayout) findViewById(R.id.layoutButtons);
        layoutContent = (RelativeLayout) findViewById(R.id.layoutContent);
        cardReg       = (CardView) findViewById(R.id.cardReg);

        txt_coleshdl = (EditText) findViewById(R.id.txt_coleshdl);
        txt_colesldl = (EditText) findViewById(R.id.txt_colesldl);
        txt_colesterol = (EditText) findViewById(R.id.txt_colesterol);
        txt_triglycerides = (EditText) findViewById(R.id.txt_triglycerides);
        txt_observation = (EditText) findViewById(R.id.txt_observation);

        lyt_fecha = (LinearLayout) findViewById(R.id.lyt_fecha);
        txt_fecha = (TextView) findViewById(R.id.txt_fecha);
        lyt_hora = (LinearLayout) findViewById(R.id.lyt_hora);
        txt_hora = (TextView) findViewById(R.id.txt_hora);
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
                validarDataColesterol(view);

            }
        });
        setToolbar();
        inicializarFechaHora();
        setearMaterialBetterSpinner();

        //obtener objeto de la actividad anterior
        if(savedInstanceState != null){
            row = (IColesterol) savedInstanceState.getSerializable("car");
            actualizarData();
        }
        else {
            if (getIntent() != null && getIntent().getExtras() != null && getIntent().getExtras().getSerializable("car") != null) {
                row = (IColesterol) getIntent().getExtras().getSerializable("car");
                actualizarData();
            }
        }



        txt_triglycerides.addTextChangedListener(new TextValidator(txt_triglycerides) {
            @Override
            public void validate(EditText editText, String text) {
                if (!text.isEmpty()) {
                    if (Float.parseFloat(text) > 245)
                        txt_colesterol.setError("El valor de su Colesterol no puede ser mayor a 245 mg/dl. Por favor Verifique");
                    if (Float.parseFloat(text) < 95)
                        txt_colesterol.setError("El valor de su Colesterol no puede ser menor a 95 mg/dl.  Por favor Verifique");
                }
            }
        });

    }

    public void actualizarData(){
        isUpdate = true;
        idAuxPresure = row.getId();
        Log.e(TAG, "ACTULIZAR DATA:"+ row.getId() + row.getObservacion());
        txt_fecha.setText(""+row.getFecha());
        txt_hora.setText(""+row.getHora());
        txt_colesterol.setText(""+row.getColesterol());
        txt_triglycerides.setText(""+row.getTriglycerides());
        txt_coleshdl.setText(""+row.getHdl());
        txt_colesldl.setText(""+row.getLdl());
        txt_observation.setText(""+row.getObservacion());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();

        return super.onOptionsItemSelected(item);
    }

    public void setToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false); //desactivar title
        toolbar.setNavigationIcon(R.mipmap.back); //buton back tollbar
        //getSupportActionBar().setTitle("Glucosa"); //titulo tollbar
        toolbar.setTitleTextColor(getResources().getColor(R.color.white)); //color tollbar title

    }

    public void setearMaterialBetterSpinner() {

        LabelledSpinner labelledSpinner = (LabelledSpinner) findViewById(R.id.spinner_planets);
        labelledSpinner.setColor(R.color.colorPrimaryDark);
        labelledSpinner.setItemsArray(R.array.planets_array);
        labelledSpinner.setDefaultErrorEnabled(true);
        labelledSpinner.setDefaultErrorText("Este es un campo obligatorio.");  // Displayed when first item remains selected
        labelledSpinner.setOnItemChosenListener(this);
        //SETEAR LA POSICION DEL SPINNER QUE APARESCA SELECIONADO
        labelledSpinner.setSelection(11, true);

    }

    private void inicializarFechaHora() {

        //obtener fechay hroa de Calendar
        Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH) + 1;
        day = c.get(Calendar.DAY_OF_MONTH);
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);
        //setear fecha
        String mes = month < 10 ? "0" + month : "" + month;
        String dia = day < 10 ? "0" + day : "" + day;
        String date = "" + dia + "/" + mes + "/" + year;
        txt_fecha.setText(date);
        //setear hora
        String hourString = hour < 10 ? "0" + hour : "" + hour;
        String minuteString = minute < 10 ? "0" + minute : "" + minute;
        String time = "" + hourString + ":" + minuteString;
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
        dpd.setMaxDate(now);
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
        tpd.enableSeconds(false);
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
        String time = "" + hourString + ":" + minuteString;
        txt_hora.setText(time);
    }

    //se ejecuta el selecionar el spinner
    @Override
    public void onItemChosen(View labelledSpinner, AdapterView<?> adapterView, View itemView, int position, long id) {
        String selectedText = adapterView.getItemAtPosition(position).toString();
        switch (labelledSpinner.getId()) {
            case R.id.spinner_planets:
                //Toast.makeText(this, "Selected: " + selectedText, Toast.LENGTH_SHORT).show();
                selectTextSpinner = selectedText;
                break;
            // If you have multiple LabelledSpinners, you can add more cases here
        }
    }

    //se ejecuta el selecionar el spinner
    @Override
    public void onNothingChosen(View labelledSpinner, AdapterView<?> adapterView) {
        // Do something here
    }

    //***********************************************************************************************************************
    // Nuevos Metodos.

    public void callBackPressedActivity(){
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ){
            //pager.setVisibility( View.INVISIBLE );
            //TransitionManager.beginDelayedTransition(mRoot, new Slide());
        }
        super.onBackPressed();
    }


    public void onBackPressed() {
        callBackPressedActivity();
        //super.onBackPressed();
    }

    //VALIDACION DE DATOS
    public void validarDataColesterol(View view) {

        String fecha = txt_fecha.getText().toString();
        String hora = txt_hora.getText().toString();
        String observacion = txt_observation.getText().toString();

        if(!txt_colesterol.getText().toString().isEmpty() && Utils.isNumeric(txt_colesterol.getText().toString())&&
                !txt_coleshdl.getText().toString().isEmpty() && Utils.isNumeric(txt_coleshdl.getText().toString()) &&
                !txt_colesldl.getText().toString().isEmpty() && Utils.isNumeric(txt_colesldl.getText().toString())){

            int colest = Integer.parseInt(txt_colesterol.getText().toString());//Float.parseFloat(txt_concentration.getText().toString())
            int trigl =  Integer.parseInt(txt_triglycerides.getText().toString());
            int colesthdl = Integer.parseInt(txt_coleshdl.getText().toString());
            int colestldl = Integer.parseInt(txt_colesldl.getText().toString());

            //if(Valida.rangoColesterol(colest)){
            if(true){

                if(isUpdate){
                    //SI AUN NO AH SIDO ENVIADO AL SERVIDOR INSERTA
                    String  operacionIU = "";
                    if(row.getIdBdServer() > 0){
                        operacionIU = operacionU;
                    }else{
                        operacionIU = operacionI;
                    }
                    updateDataColesterolDB(idAuxPresure ,colest,trigl, colesthdl ,colestldl, fecha, hora, observacion, operacionIU);
                }else {
                    saveDataColesterolDB(colest,trigl, colesthdl ,colestldl, fecha, hora, observacion);
                }
                nextAction();//ANIMATION
            } else {
                Snackbar.make(view, "\n" + "Su Colesterol está mal ingresado: "+"\n"+getString(R.string.ingrese), Snackbar.LENGTH_LONG)
                        .setAction("Ok", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                            }
                        })
                        .setActionTextColor(this.getResources().getColor(R.color.greenMenu))
                        .show();
            }
        }else{
            Snackbar.make(view, "\n" + "Revise sus datos : "+"\n"+getString(R.string.ingrese), Snackbar.LENGTH_LONG)
                    .setAction("Ok", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        }
                    })
                    .setActionTextColor(this.getResources().getColor(R.color.greenMenu))
                    .show();
        }

    }


    //GUARDOS DATOS EN LA TABLA BDSQLITE
    public  void saveDataColesterolDB(int colest,int trigl, int colesthdl ,int colestldl,
                                      String fecha, String hora, String observacion){
        try {
            //setear datos al objeto y guardar y BD
            Utils.DbsaveColesterolFromDatabase(-1,
                    colest,
                    trigl,
                    colesthdl,
                    colestldl,
                    fecha,
                    hora,
                    observacion,
                    enviadoServer,
                    operacionI,
                    HealthMonitorApplicattion.getApplication().getColesterolDao());

            Utils.generateToast(this, getResources().getString(R.string.txt_guardado));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //ACTUALIZAR DATOS EN LA TABLA BDSQLITE
    public  void updateDataColesterolDB(int idAuxPresure, int colest, int trigl,int colestldl,
                                        int colesthdl, String fecha, String hora, String observacion, String operacion){
        try {
            //setear datos al objeto y guardar y BD
            Utils.UpdateColesterolFromDatabase(idAuxPresure,
                    colest,
                    trigl,
                    colesthdl,
                    colestldl,
                    fecha,
                    hora,
                    observacion,
                    enviadoServer,
                    operacion,
                    HealthMonitorApplicattion.getApplication().getColesterolDao());

            Utils.generateToast(this, getResources().getString(R.string.txt_actulizado));
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
