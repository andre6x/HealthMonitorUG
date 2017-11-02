package com.grupocisc.healthmonitor.Complementary.activities;

import android.animation.Animator;
import android.app.AlertDialog;
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
import com.grupocisc.healthmonitor.R;
import com.grupocisc.healthmonitor.Utils.Utils;
import com.grupocisc.healthmonitor.entities.IColesterol;
import com.grupocisc.healthmonitor.entities.IHba1c;
import com.grupocisc.healthmonitor.entities.IRegCrtPacient;
import com.satsuware.usefulviews.LabelledSpinner;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.sql.SQLException;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ComplHba1cRegistyActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener,
                                                                          TimePickerDialog.OnTimeSetListener
                                                                         ,LabelledSpinner.OnItemChosenListener {

    private String TAG = "hba1cRegistyActivity";
    private int year, month, day, hour, minute;
    private LinearLayout lyt_fecha ,lyt_hora;
    private TextView txt_fecha, txt_hora ;
    private EditText  txt_concentration ,txt_concentrations, txt_observation,txt_centonas;
    private int idAuxKetone = 0;
    private FloatingActionButton fab ;
    private String selectTextSpinner;
    private Toolbar toolbar;
    private Call<IRegCrtPacient.RegCrtPacient> call_1;
    public ProgressDialog Dialog;
    private IRegCrtPacient.RegCrtPacient mSaveCrtPacient;
    //Actualización de Datos
    private boolean isUpdate = false;
    //Envio de datos a la BD
    private static final String enviadoServer = "false";
    private int idAuxHba1c = 0;
    private LinearLayout layoutMain;
    private RelativeLayout layoutButtons;
    private RelativeLayout layoutContent;
    private CardView cardReg;
    private String operacionI = "I";
    private String operacionU = "U";
    private IHba1c row;
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

        setContentView(R.layout.compl_hba1c_registy_activity);

        Dialog = new ProgressDialog(this);
        //copiar
        layoutMain = (LinearLayout) findViewById(R.id.layoutMain);
        layoutButtons = (RelativeLayout) findViewById(R.id.layoutButtons);
        layoutContent = (RelativeLayout) findViewById(R.id.layoutContent);
        cardReg       = (CardView) findViewById(R.id.cardReg);
        //hasta aqui
        txt_concentration = (EditText) findViewById(R.id.txt_concentration);
        txt_centonas = (EditText) findViewById(R.id.txt_centonas);
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
                 validarDataHba1c(view);
                //validarDataKetone(view);
            }
        });
        setToolbar();
        inicializarFechaHora();
        setearMaterialBetterSpinner();

       // selectDataGlucosaDB();
        //obtener objeto de la actividad anterior
        if(savedInstanceState != null){
            row = (IHba1c) savedInstanceState.getSerializable("car");
            actualizarData();
        }
        else {
            if (getIntent() != null && getIntent().getExtras() != null && getIntent().getExtras().getSerializable("car") != null) {
                row = (IHba1c) getIntent().getExtras().getSerializable("car");
                actualizarData();
            }
        }

    }

    public void actualizarData(){
        isUpdate = true;
        idAuxHba1c = row.getId();
        Log.e(TAG, "ACTULIZAR DATA:"+ row.getId() + row.getObservacion());
        txt_fecha.setText(""+row.getFecha());
        txt_hora.setText(""+row.getHora());
        txt_concentration.setText(""+row.getConcentracion());
        txt_centonas.setText(""+row.getCetonas() );
        txt_observation.setText(""+row.getObservacion());
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

    public void setearMaterialBetterSpinner(){

        LabelledSpinner labelledSpinner = (LabelledSpinner) findViewById(R.id.spinner_planets);
        labelledSpinner.setColor(R.color.colorPrimaryDark);
        labelledSpinner.setItemsArray(R.array.planets_array);
        labelledSpinner.setDefaultErrorEnabled(true);
        labelledSpinner.setDefaultErrorText("Este es un campo obligatorio.");  // Displayed when first item remains selected
        labelledSpinner.setOnItemChosenListener(this);
        //SETEAR LA POSICION DEL SPINNER QUE APARESCA SELECIONADO
        labelledSpinner.setSelection(11 , true);

    }

    private void inicializarFechaHora(){

        //obtener fechay hroa de Calendar
        Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH)+1;
        day = c.get(Calendar.DAY_OF_MONTH);
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);
        //setear fecha
        String mes = month < 10 ? "0"+month : ""+month;
        String dia =  day < 10 ? "0"+day : ""+day;
        String date = ""+dia+"/"+mes+"/"+year;
        txt_fecha.setText(date);
        //setear hora
        String hourString = hour < 10 ? "0"+hour : ""+hour;
        String minuteString = minute < 10 ? "0"+minute : ""+minute;
        String time = ""+hourString+":"+minuteString;
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
        if(dpd != null)
            dpd.setOnDateSetListener(this);
        TimePickerDialog tpd = (TimePickerDialog) getFragmentManager().findFragmentByTag("Timepickerdialog");
        if(tpd != null)
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
        String hourString = hourOfDay < 10 ? "0"+hourOfDay : ""+hourOfDay;
        String minuteString = minute < 10 ? "0"+minute : ""+minute;
        String secondString = second < 10 ? "0"+second : ""+second;
        String time = ""+hourString+":"+minuteString;
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

    //Nuevos metodos para ORMLITE
    //efecto
    public void callBackPressedActivity(){
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ){
            //pager.setVisibility( View.INVISIBLE );
            //TransitionManager.beginDelayedTransition(mRoot, new Slide());
        }
        super.onBackPressed();
    }
    //boton atras
    public void onBackPressed() {
        callBackPressedActivity();
        //super.onBackPressed();
    }

    public void validarDataHba1c(View view) {

        String fecha = txt_fecha.getText().toString();
        String hora = txt_hora.getText().toString();
        String observacion = txt_observation.getText().toString();


        if(!txt_concentration.getText().toString().isEmpty() && Utils.isNumeric(txt_concentration.getText().toString())){

            float concent = Float.parseFloat(txt_concentration.getText().toString());//Float.parseFloat(txt_concentration.getText().toString())
            float cetonas = Float.parseFloat(txt_centonas.getText().toString());


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
                   updateDataHba1cDB(idAuxHba1c ,concent,cetonas, fecha, hora, observacion, operacionIU);
                }else {
                   saveDataHba1cDB(concent,cetonas, fecha, hora, observacion, operacionI);

                }
                nextAction();//ANIMATION
            } else {
               /* Snackbar.make(view, "\n" + "Su Colesterol está mal ingresado: "+"\n"+getString(R.string.ingrese), Snackbar.LENGTH_LONG)
                        .setAction("Ok", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                            }
                        })
                        .setActionTextColor(this.getResources().getColor(R.color.greenMenu))
                        .show();*/
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
        public  void saveDataHba1cDB(float concent,float cetonas, String fecha, String hora,
                                     String observacion, String operacionI){
            try {
                //setear datos al objeto y guardar y BD
                Utils.DbsaveHba1cFromDatabase(-1,
                        concent,
                        cetonas,
                        fecha,
                        hora,
                        observacion,
                        enviadoServer,
                        operacionI,
                        HealthMonitorApplicattion.getApplication().getHba1cDao());
                        Utils.generateToast(this, "Se ha guardado con extio!");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    //ACTUALIZAR DATOS EN LA TABLA BDSQLITE
    public  void updateDataHba1cDB(int idAuxHba1c, float concent, float cetonas,String fecha,
                                   String hora, String observacion, String operacionU){
        try {
            //setear datos al objeto y guardar y BD
            Utils.UpdateHba1cFromDatabase(idAuxHba1c,
                    concent,
                    cetonas,
                    fecha,
                    hora,
                    observacion,
                    enviadoServer,
                    operacionU,
                    HealthMonitorApplicattion.getApplication().getHba1cDao());
                    Utils.generateToast(this, getResources().getString(R.string.txt_actulizado));
            //Utils.generateToast(this, concent + fecha + hora + observacion + enviadoServer + getResources().getString(R.string.txt_actulizado));

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
