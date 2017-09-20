package com.grupocisc.healthmonitor.Insulin.activities;

import android.animation.Animator;
import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.grupocisc.healthmonitor.HealthMonitorApplicattion;
import com.grupocisc.healthmonitor.Insulin.TextValidator;
import com.grupocisc.healthmonitor.R;
import com.grupocisc.healthmonitor.Utils.Utils;
import com.grupocisc.healthmonitor.entities.EInsulin;
import com.grupocisc.healthmonitor.entities.IRegCrtPacient;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.w3c.dom.Text;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.sql.SQLException;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;

import static com.grupocisc.healthmonitor.Utils.Utils.OPERATION_INSERT;
import static com.grupocisc.healthmonitor.Utils.Utils.UcallCalendar;
import static com.grupocisc.healthmonitor.Utils.Utils.UcallCalendarTime;
import static com.grupocisc.healthmonitor.Utils.Utils.inicializarFecha;
import static com.grupocisc.healthmonitor.Utils.Utils.inicializarHora;
import static com.grupocisc.healthmonitor.Utils.Utils.isOnline;

/**
 * Created by Gema on 10/01/2017.
 */

public class InsulinRegistry extends AppCompatActivity implements DatePickerDialog.OnDateSetListener
        , TimePickerDialog.OnTimeSetListener {

    private static final String TAG = "[InsulinRegistry]";
    private static final String enviadoServer = "false";

    private Float fMinValueInsulin = Float.parseFloat("0") ;
    private Float fMaxValueInsulin = Float.parseFloat("0") ;
    private EInsulin eInsulin;

    @Bind(R.id.txt_Title) TextView txt_Title;
    @Bind(R.id.txt_dosis) EditText etxt_dosis;
    @Bind(R.id.txt_fecha) TextView etxt_fecha;
    @Bind(R.id.txt_hora)  TextView etxt_hora;
    @Bind(R.id.txt_observacion) EditText etxt_observacion;
    @Bind(R.id.lyt_fecha) LinearLayout getLyt_fecha;
    @Bind(R.id.lyt_hora)  LinearLayout getLyt_hora;
    @Bind(R.id.fab)       FloatingActionButton get_fab;
    @Bind(R.id.toolbar) Toolbar toolbar;

    @Bind(R.id.layoutMainInsulin) LinearLayout layoutMain;
    @Bind(R.id.layoutButtonsInsulin)  RelativeLayout layoutButtons;
    @Bind(R.id.layoutContentInsulin)  RelativeLayout layoutContent;
    @Bind(R.id.cardInsulin)  CardView cardReg;

    public ProgressDialog Dialog;
    private Call<IRegCrtPacient.RegCrtPacient> call_1;
    private IRegCrtPacient.RegCrtPacient mSaveCrtPacient;
    private String mes;

    private int idInsulinDBLocal;
    private String operationDB;
    private int idBdServer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        String Method ="[onCreate]";
        Log.i(TAG, Method +  "Init..."  );

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

        fMinValueInsulin = Float.parseFloat(getResources().getString(R.string.num_MinValueInsulin));
        fMaxValueInsulin = Float.parseFloat(getResources().getString(R.string.num_MaxValueInsulin));

        Log.i(TAG, Method +  "super.onCreate..."  );
        super.onCreate(savedInstanceState);

        Log.i(TAG, Method +  "setContentView..."  );
        setContentView(R.layout.insulin_registry_activity);
        ButterKnife.bind(this);
        Log.i(TAG, Method +  "ProgressDialog..."  );
        Dialog = new ProgressDialog(this);

        //obtener objeto de la actividad anterior
        if(savedInstanceState != null){
            Log.i(TAG, Method +  "savedInstanceState != null" );
            eInsulin = (EInsulin) savedInstanceState.getSerializable("car");
             updateData(eInsulin);
        }else{
            if (getIntent() != null && getIntent().getExtras() != null && getIntent().getExtras().getSerializable("car") != null) {
                eInsulin = (EInsulin) getIntent().getExtras().getSerializable("car");
                updateData(eInsulin);

            }else{
                operationDB="I";
                idBdServer=0;
                txt_Title.setText(getResources().getString(R.string.lbl_InsulinAdd));
                eInsulin = null ;
                Log.i(TAG, Method +  "etxt_fecha.setText..."  );
                etxt_fecha.setText(inicializarFecha());

                Log.i(TAG, Method +  "etxt_hora.setText..."  );
                etxt_hora.setText(inicializarHora());

            }
        }


        etxt_fecha.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                //SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
                SimpleDateFormat formato = new SimpleDateFormat( getResources().getString(R.string.txt_DateFormat103) );
                Calendar c = Calendar.getInstance();
                Date fecha = c.getTime();
                Date fecha2 = null;
                try {
                    fecha2 = formato.parse(etxt_fecha.getText().toString());
                } catch (ParseException e) {
                    Log.e(TAG, "Error al convertir fecha " + e.getMessage() );
                    e.printStackTrace();
                }

                if(  fecha2.compareTo(fecha) != 0 &&   fecha2.compareTo(fecha) > 0)
                {
                    etxt_fecha.setError( "Fecha no válida." );
                    etxt_fecha.setText(inicializarFecha());
                }

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etxt_dosis.addTextChangedListener(new TextValidator(etxt_dosis) {
            @Override
            public void validate(EditText editText, String text) {
                if(!text.isEmpty() && Utils.isNumeric(text))
                {
                    if( Float.parseFloat(text) > fMaxValueInsulin ){
                        etxt_dosis.setError( getResources().getString(R.string.msg_DoseValueMoreThan) + fMaxValueInsulin  + " units." );//etxt_dosis.setError( "No puede ser mayor a 100 unidades." );
                        etxt_dosis.setText("");
                    }
                    if( Float.parseFloat(text) < fMinValueInsulin ){
                        etxt_dosis.setError( getResources().getString(R.string.msg_DoseValueMoreThan) + fMinValueInsulin  + " unit." );//etxt_dosis.setError( "No puede ser menor a 1 unidad." );
                        etxt_dosis.setText("");
                    }

                }
            }
        });

        getLyt_fecha.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                callCalendar();
            }
        });

        getLyt_hora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callCalendarTime();
            }
        });

        get_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( !  etxt_dosis.getText().toString().isEmpty() ) {

                    if( Float.parseFloat(etxt_dosis.getText().toString()) >= 1 && Float.parseFloat(etxt_dosis.getText().toString()) <= 100  ){
                        saveData(view);
                    }
                    else
                        Toast.makeText( InsulinRegistry.this , getResources().getString(R.string.msg_DoseValueIncorrect)   , Toast.LENGTH_SHORT  ).show();
                }
                else
                    Toast.makeText( InsulinRegistry.this ,  getResources().getString(R.string.msg_DoseValueEmpty) , Toast.LENGTH_SHORT  ).show();

            }
        });

        setToolbar();
        Log.i(TAG, Method +  "End..."  );
    }

    private void updateData(EInsulin eInsulin ){
        operationDB="U";
        txt_Title.setText(getResources().getString(R.string.lbl_InsulinEdit));
        idInsulinDBLocal = eInsulin.getId() ;
        idBdServer = eInsulin.getIdBdServer();
        etxt_dosis.setText( eInsulin.getInsulina()+ "");
        etxt_fecha.setText( eInsulin.getFecha() );
        etxt_hora.setText( eInsulin.getHora().substring(0,5) );
        etxt_observacion.setText( eInsulin.getObservacion() );
    }

    private void saveData(View view ){
        if(true){
            if (saveDataInsulinDBLocal(view)) {
                nextAction();
            }
        }else
            Utils.generarAlerta(getBaseContext() , getString(R.string.txt_atencion), getString(R.string.sin_conexion));

    }

    @Override
    public void onResume() {
        String Method ="[onResume]";
        Log.i(TAG, Method +  "Init..."  );
        super.onResume();
        DatePickerDialog dpd = (DatePickerDialog) getFragmentManager().findFragmentByTag("Datepickerdialog");
        if (dpd != null)
            dpd.setOnDateSetListener(this);
        TimePickerDialog tpd = (TimePickerDialog) getFragmentManager().findFragmentByTag("Timepickerdialog");
        if (tpd != null)
            tpd.setOnTimeSetListener(this);
        Log.i(TAG, Method +  "End..."  );
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String Method ="[onOptionsItemSelected]";
        Log.i(TAG, Method +  "Init..."  );
        if(item.getItemId()==android.R.id.home)
            nextAction();//finish();
        Log.i(TAG, Method +  "End..."  );
        return super.onOptionsItemSelected(item);
    }

    public void setToolbar(){
        String Method ="[setToolbar]";
        Log.i(TAG, Method + "Init..." );
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false); //desactivar title
        toolbar.setNavigationIcon(R.mipmap.back); //buton back tollbar
        toolbar.setTitleTextColor(getResources().getColor(R.color.white)); //color tollbar title
        Log.i(TAG, Method + "End..." );
    }
    //Fijar fecha
    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String Method ="[onDateSet]";
        Log.i(TAG, Method + "Init..." );
        mes = "" + ((monthOfYear + 1) < 10 ? "0" + (monthOfYear + 1) : "" + (monthOfYear + 1));
        String dia = dayOfMonth < 10 ? "0" + dayOfMonth : "" + dayOfMonth;
        String date = "" + dia + "/" + mes + "/" + year;
        etxt_fecha.setText(date);
        Log.i(TAG, Method + "End..." );
    }
    //Fijar hora
    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        String Method ="[onTimeSet]";
        Log.i(TAG, Method + "Init..." );
        String hourString = hourOfDay < 10 ? "0" + hourOfDay : "" + hourOfDay;
        String minuteString = minute < 10 ? "0" + minute : "" + minute;
        String time = "" + hourString + ":" + minuteString;
        etxt_hora.setText(time);
        Log.i(TAG, Method + "End..." );
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
        dpd.setMaxDate(now);
        UcallCalendar(dpd).show(getFragmentManager(), "Datepickerdialog");
    }
    /* TIME */
    public void callCalendarTime(){
        String Method ="[callCalendarTime]";
        Log.i(TAG, Method +  "Init..."  );
        Calendar now = Calendar.getInstance();
        TimePickerDialog tpd = TimePickerDialog.newInstance(
                this,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                true /*mode24Hours.isChecked()*/
        );
        UcallCalendarTime(tpd).show(getFragmentManager(), "Timepickerdialog");
        Log.i(TAG, Method +  "End..."  );
    }

    private boolean fnValidateFieldsAndData(View view){
        String Method ="[fnValidateFieldsAndData]";
        Log.i(TAG, Method +  "Init..."  );
        //if (_dosis.getText().toString().length() > 0  ) {
        if (   etxt_dosis.getText().toString().isEmpty()  ) {

            Snackbar.make(view, "\n" + getString(R.string.ingrese), Snackbar.LENGTH_LONG)
                    .setAction("Ok", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    })
                    .setActionTextColor(this.getResources().getColor(R.color.greenMenu))
                    .show();
        }
        Log.i(TAG, Method +  "End..."  );
        return  true   ;
    }

    //Save Data in Local DataBase
    private  boolean saveDataInsulinDBLocal(View view){
        String Method ="[saveDataInsulinDBLocal]";
        Log.i(TAG, Method +  "Init..."  );
        boolean saveDataInsulinDBLocal=false;
        if (fnValidateFieldsAndData(view)){

            //obtener todos los datos del activity
            String email = Utils.getEmailFromPreference(this);
            int insulina = Integer.parseInt(etxt_dosis.getText().toString()) ;

            String observacion = etxt_observacion.getText().toString();
            String fecha = etxt_fecha.getText().toString();//.substring(6, 10) + "/" + etxt_fecha.getText().toString().substring(3, 5) + "/" + etxt_fecha.getText().toString().substring(0, 2) + " " + etxt_hora.getText().toString() + ":00"; ;
            String hora = etxt_hora.getText().toString() + ":00";

            try {
                switch (operationDB){
                    case Utils.OPERATION_INSERT:
                        Log.i(TAG, Method +  "Init calling Utils.saveInsulinToDataBaseLocal..."  );
                        //setear datos al objeto y guardar y BD
                        Utils.saveInsulinToDataBaseLocal ( -1
                                                            ,insulina
                                                            ,fecha
                                                            ,hora
                                                            ,observacion
                                                            ,enviadoServer
                                                            ,operationDB
                                                            ,HealthMonitorApplicattion.getApplication().getInsulinDao()  );
                        saveDataInsulinDBLocal = true;
                        Log.i(TAG, Method +  "End calling Utils.saveInsulinToDataBaseLocal..."  );
                        break;
                    case Utils.OPERATION_UPDATE:

                        Log.i(TAG, Method +  "Init calling Utils.updateInsulinToDataBaseLocal..."  );
                        Utils.updateInsulinToDataBaseLocal (idInsulinDBLocal
                                                            ,insulina
                                                            ,fecha
                                                            ,hora
                                                            ,observacion
                                                            ,enviadoServer
                                                            ,operationDB
                                                            ,idBdServer
                                                            ,HealthMonitorApplicattion.getApplication().getInsulinDao()  );
                        saveDataInsulinDBLocal = true;
                        Log.i(TAG, Method +  "End calling Utils.updateInsulinToDataBaseLocal..."  );

                        break;
                    case Utils.OPERATION_DELETE:
                        Log.i(TAG, Method +  "Init calling Utils.deleteInsulinToDataBaseLocal..."  );
                        Utils.deleteInsulinToDataBaseLocal ( idInsulinDBLocal
                                                                ,HealthMonitorApplicattion.getApplication().getInsulinDao()  );
                        saveDataInsulinDBLocal = true;
                        Log.i(TAG, Method +  "End calling Utils.deleteInsulinToDataBaseLocal..."  );
                        break;
                }

            } catch (SQLException e) {
                saveDataInsulinDBLocal=false;
                e.printStackTrace();
                Log.e(TAG, Method +  "Error calling Utils.saveInsulinToDataBaseLocal..."  );
            }
        }

        Log.i(TAG, Method +  "End..."  );
        return  saveDataInsulinDBLocal;

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
