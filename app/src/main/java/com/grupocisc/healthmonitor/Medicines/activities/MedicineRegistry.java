package com.grupocisc.healthmonitor.Medicines.activities;

import android.animation.Animator;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.grupocisc.healthmonitor.entities.OnItemClick;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import com.grupocisc.healthmonitor.Alarm.adapters.AlarmListReminderTimesCardAdapter;
import com.grupocisc.healthmonitor.HealthMonitorApplicattion;
import com.grupocisc.healthmonitor.Insulin.TextValidator;
import com.grupocisc.healthmonitor.R;
import com.grupocisc.healthmonitor.Utils.Utils;
import com.grupocisc.healthmonitor.entities.EAlarmDetails;
import com.grupocisc.healthmonitor.entities.EMedicine;
import com.grupocisc.healthmonitor.entities.IConsulMedicines;
import com.grupocisc.healthmonitor.entities.IRegCrtMedicamentos;
import com.grupocisc.healthmonitor.entities.IRegisteredMedicines;
import com.grupocisc.healthmonitor.entities.OnItemClick;
import com.satsuware.usefulviews.LabelledSpinner;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.grupocisc.healthmonitor.Utils.Utils.UcallCalendar;
import static com.grupocisc.healthmonitor.Utils.Utils.UcallCalendarTime;
import static com.grupocisc.healthmonitor.Utils.Utils.getEmailFromPreference;
import static com.grupocisc.healthmonitor.Utils.Utils.inicializarFecha;
import static com.grupocisc.healthmonitor.Utils.Utils.isNumeric;

/**
 * Created by Gema on 10/01/2017.
 * CONTROL DE MEDICAMENTOS
 */

public class MedicineRegistry extends AppCompatActivity implements  DatePickerDialog.OnDateSetListener , TimePickerDialog.OnTimeSetListener{
    private static final String TAG = "[MedicineRegistry]";
    private static final String sentServer = "N";
    final Context context = this;
    String email = "";

    public ProgressDialog Dialog;


    // Alarms_INI
    private EAlarmDetails alarmDetails;
    private List<EAlarmDetails> lstAlarmDetails;

    private Map<Object,Object> mapDescValue;
    private List<Object> lstAllWeekDays;
    private List<Object> lstMedicineTypes;
    private List<Object> lstReminderTypes;
    private List<Object> lstReminderTimesF;
    private List<Object> lstReminderTimesI;
    private Date fechaI;
    private Date fechaF;
    private List <WeekDays> lstWeekDaysSelected = new ArrayList<>( );
    private static List <WeekDays> lstWeekDays;

    private AlarmListReminderTimesCardAdapter adapterAlarm;
    private int positionSRTp;
    private int positionSRTm;
    private String [] ReminderTypes ;
    private String [] ReminderTimesFrequencies;
    private String [] ReminderTimesIntervals;


    @BindView(R.id.layoutMainMedCtrlUpd)            LinearLayout layoutMain ;
    @BindView(R.id.toolbar)                         Toolbar toolbar;
    @BindView(R.id.layoutContentMedCtrlUpd)         RelativeLayout layoutContent ;
    @BindView(R.id.cardMedCtrlUpd)                 CardView cardMedCtrlUpd;

    @BindView(R.id.fabMedCtrlUpd)                  FloatingActionButton fabMedCtrlUpd;
    @BindView(R.id.llv_MedCtrlUpd)                  LinearLayout llv_MedCtrlUpd ;
    @BindView(R.id.txt_MedNombreMedCtrlUpd)        TextView txt_MedNombreMedCtrlUpd;
    @BindView(R.id.txt_dosisMedCtrlUpd)            TextView txt_dosisMedCtrlUpd;

    @BindView(R.id.lyt_startDateMedCtrlUpd)        LinearLayout lyt_startDateMedCtrlUpd;
    @BindView(R.id.txt_startDateMedCtrlUpd)        TextView txt_startDateMedCtrlUpd;
    @BindView(R.id.lyt_startHourMedCtrlUpd)        LinearLayout lyt_startHourMedCtrlUpd;
    @BindView(R.id.txt_startHourMedCtrlUpd)        TextView txt_startHourMedCtrlUpd;

    @BindView(R.id.txt_Title)                       TextView txt_Title;
    @BindView(R.id.txt_observacionMedCtrlUpd)      TextView txt_observacionMedCtrlUpd;


    @BindView(R.id.lyt_ReminderTypes)               LinearLayout lyt_ReminderTypes;
    @BindView(R.id.rgrpReminderTypes)        RadioGroup rgrpReminderTypes;
    @BindView(R.id.rbtFrecuency)       RadioButton rbtFrecuency;
    @BindView(R.id.rbtInterval)     RadioButton rbtInterval;
    //@BindView(R.id.spinner_ReminderTypes)           Spinner spinner_ReminderTypes;
    @BindView(R.id.spinner_ReminderTimes)           Spinner spinner_ReminderTimes;

    @BindView(R.id.lyt_reminder_times ) LinearLayout lyt_reminder_times;
    @BindView(R.id.rv_reminder_times)   RecyclerView rv_reminder_times;

    @BindView(R.id.lyt_scheldule )      LinearLayout lyt_scheldule;
    @BindView(R.id.lyt_duration )       LinearLayout lyt_duration;
    @BindView(R.id.rgrpDuration)        RadioGroup rgrpDuration;
    @BindView(R.id.rbtContinuous)       RadioButton rbtContinuous;
    @BindView(R.id.rbtNumberOfDays)     RadioButton rbtNumberOfDays;
    @BindView(R.id.lyt_Days)            LinearLayout lyt_Days;
    @BindView(R.id.rgrpDays)            RadioGroup rgrpDays;
    @BindView(R.id.rbtEveryDay)         RadioButton rbtEveryDay;
    @BindView(R.id.rbtSpecificDaysOfWeek)     RadioButton rbtSpecificDaysOfWeek;


    @BindView(R.id.layoutButtonsMedUpd)          RelativeLayout layoutButtons ;


    //@BindView(R.id.txt_NumberOfDay)      TextView txt_NumberOfDays;

     private IRegisteredMedicines iRegisteredMedicines;
    private String Tid;
    private int idRegDBLocal=0;
    private int idServerDb=0;
    private int idMedicamento=0;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        String Method ="[onCreate]";
        Log.i(TAG, Method + "Init..."  );

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


        super.onCreate(savedInstanceState);
        setContentView(R.layout.medicine_registry_activity);
        ButterKnife.bind(this);
        Dialog = new ProgressDialog(this);
        email = Utils.getEmailFromPreference(this);
        inicializarRecursos();
        setWeekDays();
        loadSpinner();

        this.txt_startDateMedCtrlUpd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                //SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
                SimpleDateFormat formato = new SimpleDateFormat( getResources().getString(R.string.txt_DateFormat103) );
                Calendar c = Calendar.getInstance();
                Date fecha = c.getTime();
                Date fecha2 = null;
                try {
                    fecha2 = formato.parse(txt_startDateMedCtrlUpd.getText().toString());
                } catch (ParseException e) {
                    Log.e(TAG, "Error al convertir fecha " + e.getMessage() );
                    e.printStackTrace();
                }
                if(  fecha2.compareTo(fecha) != 0 &&   fecha2.compareTo(fecha) > 0){
                    txt_startDateMedCtrlUpd.setError( "Fecha no válida." );
                    txt_startDateMedCtrlUpd.setText(inicializarFecha());
                }
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        this.lyt_startDateMedCtrlUpd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callCalendar();
            }
        });

        this.txt_startDateMedCtrlUpd.setText(inicializarFecha());

        this.lyt_startHourMedCtrlUpd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callCalendarTime();
            }
        });

        txt_startHourMedCtrlUpd.setText("08:00");

//        this.spinner_ReminderTypes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                //Object item = parent.getItemAtPosition(position);
//                positionSRTp=position;
//                //setSpinner(position);
//                //fillSpinner(spinner_ReminderTimes,1,position);
//                switch (position){
//                    case 0:{fillSpinner(spinner_ReminderTimes,"ReminderTimesF");break;}
//                    case 1:{fillSpinner(spinner_ReminderTimes,"ReminderTimesI");break;}
//                }
//            }
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//            }
//        });
        this.rbtFrecuency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                positionSRTp=0;
                fillSpinner(spinner_ReminderTimes,"ReminderTimesF");
            }
        });
        this.rbtInterval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                positionSRTp=1;
                fillSpinner(spinner_ReminderTimes,"ReminderTimesI");
            }
        });

        this.spinner_ReminderTimes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                positionSRTm=position;
                Log.i(TAG, "[onItemSelected] positionSRTm="+positionSRTm +" position="+position );
                setListAlarm(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        this.rbtContinuous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt="Número de días:";
                rbtNumberOfDays.setText(txt);
            }
        });
        this.rbtNumberOfDays.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fn_showAlertDialogNumberOfDays();
            }
        });

        this.rbtEveryDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt="Días específicos:";
                rbtSpecificDaysOfWeek.setText(txt);
                lstWeekDaysSelected.clear();
            }
        });

        this.rbtSpecificDaysOfWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fn_showAlertDialogNameOfDays();
            }
        });

        fabMedCtrlUpd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( idMedicamento>0  ) {
                    String strDosis=txt_dosisMedCtrlUpd  .getText().toString();
                    if( strDosis.isEmpty() ) {
                        Toast.makeText(getBaseContext(), "Ingrese valor de dosis" , Toast.LENGTH_SHORT).show();
                    }else{
                        int intDosis = Integer.parseInt(strDosis);
                        if (intDosis > 0 ){
                            saveDataDBLocal(view);
                        }else{
                            Toast.makeText(getBaseContext(), "Dosis no puede ser 0." , Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(getBaseContext(), "Error al guardar." , Toast.LENGTH_SHORT).show();
                }
            }
        });

        //obtener objeto de la actividad anterior
        if(savedInstanceState != null){
            Log.i(TAG, Method +  "savedInstanceState != null" );
            iRegisteredMedicines = (IRegisteredMedicines) savedInstanceState.getSerializable("cardMedCtrlUpd");
            if (iRegisteredMedicines == null)
                Log.i(TAG, Method +  "iRegisteredMedicines is null" );
            else
                Log.i(TAG, Method +  "iRegisteredMedicines is not null" );
            updateData(iRegisteredMedicines);
        }else {
            if (getIntent() != null && getIntent().getExtras() != null && getIntent().getExtras().getSerializable("cardMedCtrlUpd") != null) {
                Log.i(TAG, Method +  "getIntent() != null" );
                iRegisteredMedicines = (IRegisteredMedicines) getIntent().getExtras().getSerializable("cardMedCtrlUpd");
                if (iRegisteredMedicines == null)
                    Log.i(TAG, Method +  "iRegisteredMedicines is null" );
                else
                    Log.i(TAG, Method +  "iRegisteredMedicines is not null" );
                updateData(iRegisteredMedicines);
            }else{
                iRegisteredMedicines = null ;
            }
        }


        Log.i(TAG, Method + "End..."  );
    }

    @Override
    public void onResume(){
        String Method ="[onResume]";
        Log.i(TAG, Method + "Init..."  );
        super.onResume();
        DatePickerDialog dpd = (DatePickerDialog) getFragmentManager().findFragmentByTag("Datepickerdialog");
        if (dpd != null)
            dpd.setOnDateSetListener(this);
        TimePickerDialog tpd = (TimePickerDialog) getFragmentManager().findFragmentByTag("Timepickerdialog");
        if (tpd != null)
            tpd.setOnTimeSetListener(this);

        //updateData(iRegisteredMedicines);

        Log.i(TAG, Method + "End..."  );
    }

    private void loadSpinner(){
        //fillSpinner(spinner_ReminderTypes,"ReminderTypes");
        fillSpinner(spinner_ReminderTimes,"ReminderTimesF");
    }

    private void restartAlarmService(){
        String Method = "[restartAlarmService]";
        Log.i(TAG, Method + "Init..."  );
        Utils.restartAlarmService(this);
        Log.i(TAG, Method + "End..."  );
    }

    private void updateData(IRegisteredMedicines iRegisteredMedicines ){
        String Method ="[updateData]";
        Log.i(TAG, Method + "Init..." );
        Log.i(TAG, Method + "iRegisteredMedicines " + iRegisteredMedicines.toString() );
        idRegDBLocal = iRegisteredMedicines.getId();
        idServerDb = iRegisteredMedicines.getIdServerDb();
        idMedicamento = iRegisteredMedicines.getId_medicacion()   ;
        Tid = String.valueOf(iRegisteredMedicines.getId_medicacion()   );
        txt_dosisMedCtrlUpd.setText(iRegisteredMedicines.getDosis()+"");
        txt_observacionMedCtrlUpd.setText(iRegisteredMedicines.getConsumo_medicina());
        String date_ddMMyyyy = "01/01/1900";
        String date_HHmm = "00:00";

        date_ddMMyyyy = iRegisteredMedicines.getFechaInicio().substring(8,10) + "/" +
                iRegisteredMedicines.getFechaInicio().substring(5,7) + "/" +
                iRegisteredMedicines.getFechaInicio().substring(0,4) ;
        date_HHmm = iRegisteredMedicines.getFechaInicio().substring(11,16) ;

        Log.i(TAG, Method + "date_ddMMyyyy=" + date_ddMMyyyy );
        Log.i(TAG, Method + "date_HHmm=" + date_HHmm );
        txt_startDateMedCtrlUpd.setText( date_ddMMyyyy ) ;
        txt_startHourMedCtrlUpd.setText( date_HHmm );
        try {
            fechaI = new SimpleDateFormat("dd/MM/yyyy").parse(date_ddMMyyyy);
            if (iRegisteredMedicines.getFechaFin() != null){
                fechaF = new SimpleDateFormat("dd/MM/yyyy").parse(
                        iRegisteredMedicines.getFechaFin().substring(8,10) + "/" +
                                iRegisteredMedicines.getFechaFin().substring(5,7) + "/" +
                                iRegisteredMedicines.getFechaFin().substring(0,4)
                );
            }else{fechaF=null;}
        }catch (Exception e){e.printStackTrace();}
        Log.i(TAG, Method + "fechaI=" + iRegisteredMedicines.getFechaInicio() );
        Log.i(TAG, Method + "fechaF=" + iRegisteredMedicines.getFechaFin() );

        if (fechaF != null){
            Log.i(TAG, Method + "fechaI=" + fechaI );
            Log.i(TAG, Method + "fechaF=" + fechaF );
            long daysDiff = getDateDiff(fechaI,fechaF, TimeUnit.DAYS);
            final String textRbt="Número de días: ";
            rbtNumberOfDays.setText( textRbt + daysDiff);
            rbtNumberOfDays.setChecked(true);
        }

        final String textRbt="Días específicos: ";
        String Days="ALL";
        Days = getDaySelectedFromDataBase ( iRegisteredMedicines.getDiasMedicacion() );
        if ( ! Days.equals("ALL")){
            rbtSpecificDaysOfWeek.setText( textRbt + Days);
            rbtSpecificDaysOfWeek.setChecked(true);
        }

        /* -------------------------------------------------------- */

        int idxReminderTypes = getSelectedValueIndex("ReminderTypes",iRegisteredMedicines.getReminderTypeCode());
        Log.i(TAG, Method + "spinner_ReminderTypes.setSelection(idxReminderTypes) = " + idxReminderTypes );
        //this.spinner_ReminderTypes.setSelection(idxReminderTypes,true);

        String strKey="ReminderTimesF";
        positionSRTp=idxReminderTypes;
        switch (idxReminderTypes){
            case 0:{strKey="ReminderTimesF" ; this.rbtFrecuency.setChecked(true); this.rbtInterval.setChecked(false); break;}
            case 1:{strKey="ReminderTimesI" ; this.rbtFrecuency.setChecked(false); this.rbtInterval.setChecked(true); break;}
        }
        fillSpinner(spinner_ReminderTimes,strKey);
        int idxReminderTimes = getSelectedValueIndex(strKey,iRegisteredMedicines.getReminderTimeCode()+"");
        Log.i(TAG, Method + "spinner_ReminderTimes.setSelection(idxReminderTimes) = " + idxReminderTimes );
        //this.spinner_ReminderTimes.setSelection(idxReminderTimes,true);
        this.spinner_ReminderTimes.setSelection(idxReminderTimes);
        this.spinner_ReminderTimes.refreshDrawableState();
        Log.i(TAG, Method + "spinner_ReminderTimes.setSelection(idxReminderTimes) = ............" + idxReminderTimes );
        try{
            lstAlarmDetails = Utils.getEAlarmDetailsFromDataBaseLocal(HealthMonitorApplicattion.getApplication().getEAlarmDetailsDao(),iRegisteredMedicines.getId(),0,"","","","" );
        }catch (Exception e){e.printStackTrace();}

        callSetAdapter(lstAlarmDetails);



        Log.i(TAG, Method + "End..."  );
    }

    private boolean saveDataDBLocal(View view){

        boolean saveMedicieUser = false;
        boolean saveMedicineUserControl=false;

        if (Tid.length() > 0) {
            /*Manda a guardar el registro al confirmar*/
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(getResources().getString(R.string.msg_SaveRegisterQuestion))
                    .setTitle( getResources().getString(R.string.txt_Save)   )
                    .setPositiveButton( getResources().getString(R.string.txt_Accept)    ,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (saveDataMedicineControlDBLocal()){
                                        restartAlarmService();
                                        postExecutionSaveData();
                                    }else {

                                    }
                                }
                            })
                    .setNegativeButton("CANCELAR",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    return;
                                }
                            });

            builder.create();
            builder.show();
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
        return  false;
    }

    private  boolean saveDataMedicineControlDBLocal( ){
        final String Method ="[saveDataMedicineControlDBLocal]";
        Log.i(TAG, Method + "Init..." );
        boolean saveDataMedicineControlDBLocal = false;
        int medicineNumDays=0;
        int yyyy = Integer.parseInt(this.txt_startDateMedCtrlUpd.getText().toString().substring(6, 10)) ;
        int MM = Integer.parseInt(this.txt_startDateMedCtrlUpd.getText().toString().substring(3, 5)) -1  ; // 0=Jan,1=Feb...,11=Dec
        int dd = Integer.parseInt(this.txt_startDateMedCtrlUpd.getText().toString().substring(0, 2)) ;
        String [] tmp = rbtNumberOfDays.getText().toString().split(":");

        final int dosis = isNumeric(  this.txt_dosisMedCtrlUpd.getText().toString()) == true ? Integer.parseInt(this.txt_dosisMedCtrlUpd.getText().toString()) : 0;
        final String fechaInicio = this.txt_startDateMedCtrlUpd.getText().toString().substring(6, 10) + "/" + this.txt_startDateMedCtrlUpd.getText().toString().substring(3, 5) + "/" + this.txt_startDateMedCtrlUpd.getText().toString().substring(0, 2) + " " + this.txt_startHourMedCtrlUpd.getText().toString() + ":00";

        final String obsevaciones= this.txt_observacionMedCtrlUpd.getText().toString();
        final int idMedicine =  idMedicamento ;
        //final String fechaRegistro = Utils.getDate("yyyy/MM/dd HH:mm:ss");
        final boolean existsMedicine = true ;//fnExistsMediceRegistred(idMedicine);
        //validar campos llenos

        //final String reminderTypeCode = getSelectedValue ( this.spinner_ReminderTypes, "ReminderTypes").toString() ; // ObtenerValor de Spinner Frecuencia.
        final String reminderTypeCode = this.rbtFrecuency.isChecked() ? "F" : "I";   // ObtenerValor de Spinner Frecuencia.
        String optionSpinnerKey = reminderTypeCode.equals("F")?"ReminderTimesF":"ReminderTimesI";
        Log.i(TAG, Method + "optionSpinnerKey = " + optionSpinnerKey);
        final int reminderTimeCode = Integer.parseInt( getSelectedValue ( this.spinner_ReminderTimes, optionSpinnerKey).toString()) ; //Obtener valor de 2do Spinner
        final String diasMedicacion = getSelectedDayValue() ; // Obtener String de días, 0=ALL - 135=LMV - 25=MV

        if (tmp.length > 1 ){
            medicineNumDays =  Integer.parseInt( tmp[1].trim() ) ;
            Log.i(TAG, Method + "medicineNumDays = " + medicineNumDays);
        }
        Date tmpFinalDate = new Date( yyyy,MM,dd );
        if (medicineNumDays != 0 ){
            Log.i(TAG, Method + "new Date(" + yyyy +","+ MM +","+ dd + ") = " + new Date( yyyy,MM,dd ) );
            tmpFinalDate = getDateAdd( new Date( yyyy,MM,dd  ) , medicineNumDays ,TimeUnit.DAYS );
            Log.i(TAG, Method + "tmpFinalDate = " + tmpFinalDate );
            Log.i(TAG, Method + "tmpFinalDate.getYear() = " + tmpFinalDate.getYear() );
            Log.i(TAG, Method + "tmpFinalDate.getMonth() = " + tmpFinalDate.getMonth() );
            Log.i(TAG, Method + "tmpFinalDate.getDate() = " + tmpFinalDate.getDate() );
        }


        String finalYYYY=tmpFinalDate.getYear()+"";
        String finalMM = (tmpFinalDate.getMonth()+1) < 10 ? "0" + (tmpFinalDate.getMonth()+1) : (tmpFinalDate.getMonth()+1)+"";
        String finaldd = tmpFinalDate.getDate() < 10 ? "0" + tmpFinalDate.getDate() : tmpFinalDate.getDate()+"";
        String finalDate = finalYYYY + "/" + finalMM + "/" + finaldd + " " + this.txt_startHourMedCtrlUpd.getText().toString() + ":00";

        final String fechaFin = medicineNumDays != 0 ? finalDate : null ;

        //Log.i(TAG, Method + "txt_startDateMedCtrlUpd.text = " + this.txt_startDateMedCtrlUpd.getText());
        //Log.i(TAG, Method + "txt_startHourMedCtrlUpd.text = " + this.txt_startHourMedCtrlUpd.getText());

        Log.i(TAG, Method + "existsMedicine = " + existsMedicine);

        Log.i(TAG, Method + "idMedicine = " + idMedicine);
        Log.i(TAG, Method + "dosis = " + dosis);
        Log.i(TAG, Method + "fechaInicio = " + fechaInicio);
        Log.i(TAG, Method + "fechaFin = " + fechaFin);
        Log.i(TAG, Method + "reminderTypeCode = " + reminderTypeCode);
        Log.i(TAG, Method + "reminderTimeCode = " + reminderTimeCode);
        Log.i(TAG, Method + "diasMedicacion = " + diasMedicacion);
        Log.i(TAG, Method + "obsevaciones = " + obsevaciones);
        Log.i(TAG, Method + "email = " + email);
        try {
            int registeredMedicinesId=0;
            if ( !existsMedicine ){
                int iRows=0;

                try {
                    iRows = Utils.saveRegisteredMedicineUserToDataBaseLocal(idMedicine,Utils.getDate("yyyy/MM/dd HH:mm:ss"),email
                            ,idServerDb,HealthMonitorApplicattion.getApplication().getMedicineUserDao()
                    );
                }catch (Exception e){
                    //generarAlerta(MedicinesRegisteredActivity.this, getString(R.string.txt_atencion), getString(R.string.text_error_metodo));
                    Log.e(TAG, Method + "Error saving Medicine User Register");
                }
                if (iRows > 0 ){
                    registeredMedicinesId = Utils.updateRegisteredMedicinesToDataBaseLocal( idRegDBLocal, idMedicine,dosis, fechaInicio, fechaFin , reminderTypeCode,  reminderTimeCode, diasMedicacion , obsevaciones,email, idServerDb,
                            HealthMonitorApplicattion.getApplication().getRegisteredMedicinesDao());
                    if ( registeredMedicinesId  > 0 ){
                        if(deleteAlarmsDetails(idRegDBLocal)){
                            if (saveAlarmsDetails(adapterAlarm.getItemCount(),idRegDBLocal)){
                                saveDataMedicineControlDBLocal=true;
                            }
                        }
                    }
                }
            }else{
                registeredMedicinesId = Utils.updateRegisteredMedicinesToDataBaseLocal( idRegDBLocal, idMedicine,dosis, fechaInicio, fechaFin , reminderTypeCode,  reminderTimeCode, diasMedicacion , obsevaciones,email,idServerDb,
                        HealthMonitorApplicattion.getApplication().getRegisteredMedicinesDao());
                if ( registeredMedicinesId  > 0 ){
                    if(deleteAlarmsDetails(idRegDBLocal)){
                        if (saveAlarmsDetails(adapterAlarm.getItemCount(),idRegDBLocal)){
                            saveDataMedicineControlDBLocal=true;
                        }
                    }
                }
            }

        }catch (Exception e){
            saveDataMedicineControlDBLocal=false;
            Log.e(TAG, Method + "Error saving Medicine User Control Register. " + e.getMessage() );
        }

        return  saveDataMedicineControlDBLocal;
    }

    private boolean deleteAlarmsDetails( int regMedId ){
        boolean deleteAlarmsDetails = false;
        try {
            if (Utils.deleteAlarmDetailsToDataBaseLocal( HealthMonitorApplicattion.getApplication().getEAlarmDetailsDao(),regMedId ) > 0){
                deleteAlarmsDetails=true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return deleteAlarmsDetails;
    }

    private boolean saveAlarmsDetails( int numReg , int regMedId  ){
        boolean saveAlarmsDetails = false;
        String alarmDetailHour;
        for (int i=0; i < numReg ; i++){
            alarmDetailHour=adapterAlarm.getAlarmHour(i)+":00";
            try {
                if (Utils.saveAlarmDetailsToDataBaseLocal( HealthMonitorApplicattion.getApplication().getEAlarmDetailsDao(),regMedId,alarmDetailHour,"",email,0 ) > 0){
                    saveAlarmsDetails=true;
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }

        return saveAlarmsDetails;
    }

    private  void postExecutionSaveData(){
        //generarAlerta(this, getString(R.string.txt_atencion), "Registro ingresado con éxito" );
        nextAction();
        //finish();
    }

    /* CALENDAR */
    public void callCalendar() {
        String Method ="[callCalendarDate]";
        Log.i(TAG, Method + "Init..." );
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.setMaxDate(now);
        UcallCalendar(dpd).show(getFragmentManager(), "Datepickerdialog");
        Log.i(TAG, Method + "End..." );
    }

    /* TIME */
    public void callCalendarTime(){
        String Method ="[callCalendarTime]";
        Log.i(TAG, Method + "Init..." );
        Calendar now = Calendar.getInstance();
        TimePickerDialog tpd = TimePickerDialog.newInstance(
                this,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                true /*mode24Hours.isChecked()*/
        );
        UcallCalendarTime(tpd).show(getFragmentManager(), "Timepickerdialog");
        Log.i(TAG, Method + "End..." );
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String Method ="[onOptionsItemSelected]";
        Log.i(TAG, Method + "Init...");
        if(item.getItemId()==android.R.id.home)
            nextAction();//finish();
        Log.i(TAG, Method + "End...");
        return super.onOptionsItemSelected(item);
    }

    public void setToolbar(){
        String Method ="[setToolbar]";
        Log.i(TAG, Method + "Init..." );
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false); //desactivar title
        toolbar.setNavigationIcon(R.mipmap.back); //buton back tollbar
        toolbar.setTitleTextColor(getResources().getColor(R.color.white)); //color tollbar title
        Log.i(TAG, Method + "End..." );
    }

    private void postExecuteSaveData(){
        String Method ="[postExecuteSaveData]";
        Log.i(TAG, Method + "Init..." );
        //showLayoutDialog();
        //Utils.generarAlerta(this, getString(R.string.txt_atencion), "Insert Success." );
        nextAction();// finish();
        Log.i(TAG, Method + "End..." );
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int x = layoutContent.getRight();
            int y = layoutContent.getBottom();
            int startRadius = 0;
            int endRadius = (int) Math.hypot(layoutMain.getWidth(), layoutMain.getHeight());
            Animator anim = ViewAnimationUtils.createCircularReveal(layoutButtons, x, y, startRadius, endRadius);
            layoutButtons.setVisibility(View.VISIBLE);
            cardMedCtrlUpd.setVisibility(View.INVISIBLE);
            anim.start();
        }
    }

    private void circleAnimationClose() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
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
                    cardMedCtrlUpd.setVisibility(View.VISIBLE);
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

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String Method ="[onDateSet]";
        Log.i(TAG, Method + "Init..." );
        String mes = "" + ((monthOfYear + 1) < 10 ? "0" + (monthOfYear + 1) : "" + (monthOfYear + 1));
        String dia = dayOfMonth < 10 ? "0" + dayOfMonth : "" + dayOfMonth;
        String date = "" + dia + "/" + mes + "/" + year;
        this.txt_startDateMedCtrlUpd.setText(date);
        Log.i(TAG, Method + "End..." );
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        String Method ="[onTimeSet]";
        Log.i(TAG, Method + "Init..." );
        String hourString = hourOfDay < 10 ? "0" + hourOfDay : "" + hourOfDay;
        String minuteString = minute < 10 ? "0" + minute : "" + minute;
        String time = "" + hourString + ":" + minuteString;
        this.txt_startHourMedCtrlUpd.setText(time);
        if (this.positionSRTp == 0 )
            this.setListAlarm(positionSRTm);
        Log.i(TAG, Method + "End..." );
    }

    private void inicializarRecursos(){
        Log.i(TAG, "inicializarRecursos: Init...");
        mapDescValue = new LinkedHashMap<Object,Object>();
        lstAllWeekDays = new ArrayList<Object>();
        lstMedicineTypes = new ArrayList<Object>();
        lstReminderTypes = new ArrayList<Object>();
        lstReminderTimesF = new ArrayList<Object>();
        lstReminderTimesI = new ArrayList<Object>();

        ValueDesc valueDesc;
        String [] tmpCode = new String[0];
        String [] tmpDesc = new String[0];
        //
        tmpCode = getResources().getStringArray(R.array.array_WeekDaysCode);
        tmpDesc = getResources().getStringArray(R.array.array_WeekDaysDesc);
        Log.i(TAG, "inicializarRecursos: tmpCode.length = " + tmpCode.length);
        for (int i=0; i < tmpCode.length  ; i ++){
            valueDesc = new ValueDesc(tmpCode[i],tmpDesc[i]);
            lstAllWeekDays.add(valueDesc);
        }
        mapDescValue.put("WeekDays",lstAllWeekDays);

        tmpCode = getResources().getStringArray(R.array.array_MedicineTypesCode);
        tmpDesc = getResources().getStringArray(R.array.array_MedicineTypesDesc);
        Log.i(TAG, "inicializarRecursos: tmpCode.length = " + tmpCode.length);
        for (int i=0; i < tmpCode.length  ; i ++){
            //Log.i(TAG, "inicializarRecursos.ReminderTypes: Code="+tmpCode[i] + " Desc=" + tmpDesc[i] );
            valueDesc = new ValueDesc(tmpCode[i],tmpDesc[i]);
            lstMedicineTypes.add(valueDesc);
        }
        mapDescValue.put("MedicineTypes",lstMedicineTypes);

        tmpCode = getResources().getStringArray(R.array.array_ReminderTypesCode);
        tmpDesc = getResources().getStringArray(R.array.array_ReminderTypesDesc);
        for (int i=0; i < tmpCode.length  ; i ++){
            valueDesc = new ValueDesc(tmpCode[i],tmpDesc[i]);
            lstReminderTypes.add(valueDesc);
        }
        mapDescValue.put("ReminderTypes",lstReminderTypes);

        tmpCode = getResources().getStringArray(R.array.array_ReminderTimesFCode);
        tmpDesc = getResources().getStringArray(R.array.array_ReminderTimesFDesc);
        for (int i=0; i < tmpCode.length  ; i ++){
            valueDesc = new ValueDesc(tmpCode[i],tmpDesc[i]);
            lstReminderTimesF.add(valueDesc);
        }
        mapDescValue.put("ReminderTimesF",lstReminderTimesF);

        tmpCode = getResources().getStringArray(R.array.array_ReminderTimesICode);
        tmpDesc = getResources().getStringArray(R.array.array_ReminderTimesIDesc);
        for (int i=0; i < tmpCode.length  ; i ++){
            valueDesc = new ValueDesc(tmpCode[i],tmpDesc[i]);
            lstReminderTimesI.add(valueDesc);
        }
        mapDescValue.put("ReminderTimesI",lstReminderTimesI);
        List <ValueDesc> lst;
        Log.i(TAG, "inicializarRecursos: Mapa = " + mapDescValue.size() );
        for (Map.Entry<Object, Object> entry : mapDescValue.entrySet()) {
            System.out.println("clave=" + entry.getKey() + ", valor=" + entry.getValue());
            lst = (List) entry.getValue();
            for (ValueDesc vd : lst) {
                Log.i(TAG, "inicializarRecursos.Map.key=" + entry.getKey() + " Code="+vd.getValue() + " Desc=" + vd.getDescription() );
            }
        }
        Log.i(TAG, "inicializarRecursos: End...");
    }

    private void fillSpinner(Spinner spinner , String key ){
        String Method="fillSpinner";
        Log.i(TAG, Method + " Init...");
        Log.i(TAG, Method + " key = " +  key);
        ArrayAdapter<String> adapterRTp;
        List <ValueDesc> lst = null ;
        List <String> lstDesc = new ArrayList<>() ;
        for (Map.Entry<Object, Object> entry : mapDescValue.entrySet()) {
            //System.out.println("clave=" + entry.getKey() + ", valor=" + entry.getValue());
            if ( key.equals(entry.getKey().toString()) ) {
                lst = (List) entry.getValue();
                for (ValueDesc vd : lst) {
                    //Log.i(TAG, "fillSpinner.Map.key=" + entry.getKey() + " Code="+vd.getValue() + " Desc=" + vd.getDescription() );
                    lstDesc.add( (String) vd.getDescription() );
                }
            }
        }
        adapterRTp = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item,lstDesc) ;
        spinner.setAdapter(adapterRTp);
        Log.i(TAG, Method + " End...");
    }

    private Object getSelectedValue(Spinner spinner , String key) {
        String Method = "[getSelectedValue]";
        Log.i(TAG, Method + " Init...");
        Object obj = spinner.getSelectedItem();
        List <ValueDesc> lst = null ;
        List <String> lstDesc = new ArrayList<>() ;
        for (Map.Entry<Object, Object> entry : mapDescValue.entrySet()) {
            System.out.println("clave=" + entry.getKey() + ", valor=" + entry.getValue());
            if ( key.equals(entry.getKey().toString()) ) {
                lst = (List) entry.getValue();
                int idx = spinner.getSelectedItemPosition();
                obj = lst.get(idx).getValue();
            }
        }
        Log.i(TAG, Method + " with key="+key + " get value=" + obj);
        Log.i(TAG, Method + " End...");
        return  obj ;
    }

    private int getSelectedValueIndex(String key, String value){
        String Method = "[getSelectedValueIndex]";
        Log.i(TAG, Method + " Init...");
        Log.i(TAG, Method + " Key=" + key + ", value="+value );
        List <ValueDesc> lst = null ;
        int idx=-1;
        for (Map.Entry<Object, Object> entry : mapDescValue.entrySet()) {
            //System.out.println("key=" + entry.getKey() + ", valor=" + entry.getValue());
            if ( key.equals(entry.getKey().toString()) ) {
                System.out.println("key=" + entry.getKey() + ", valor=" + entry.getValue());
                lst = (List) entry.getValue();
                for (int i=0 ; i < lst.size() ; i++){
                    System.out.println("key=" + entry.getKey() + ", map.value=" + entry.getValue() + ", value=" +value );
                    if ( lst.get(i).getValue().equals(value) ){
                        System.out.println("FOUND key=" + entry.getKey() + ", map.value=" + entry.getValue() + ", value=" +value );
                        idx=i;break;
                    }
                }
            }
        }
        Log.i(TAG, Method + " Key="+key + " with value=" + value + " index = " + idx );
        Log.i(TAG, Method + " End...");
        return idx;
    }

    private void setListAlarm(int id){
        String Method = "[setListAlarm]";
        Log.i(TAG, Method + " Init...");
        int HH = Integer.parseInt( this.txt_startHourMedCtrlUpd.getText().toString().substring(0,2) );
        int mm = Integer.parseInt( this.txt_startHourMedCtrlUpd.getText().toString().substring(3,5) );
        int ss = 0 ;
        String time = (HH < 10 ? "0" + HH : HH) + ":" + (mm < 10 ? "0" + mm : mm) + ":" + (ss < 10 ? "0" + ss : ss) +"";
        int HH24 = 24;
        int periodo = 0;

        Log.i(TAG, Method +  "Init..."  );
        List<EAlarmDetails> lstAlarmList = new ArrayList<>();
        EAlarmDetails alarmDetails;
        if ( positionSRTp == 0){
            Log.i(TAG, Method +  "Frecuencia - positionSRTm=" + positionSRTm  );
            int intvervalo=1;
            time = txt_startHourMedCtrlUpd.getText().toString()+":00";
            switch (positionSRTm){
                case 0: {HH=0 ; mm = 0 ; break;}
                case 1: {HH=15 ; mm = 0 ; break;}
                case 2: {HH=7 ; mm = 30 ; break;}
                case 3: {HH=5 ; mm = 0 ; break;}
                case 4: {HH=3 ; mm = 45 ; break;}
                case 5: {HH=3 ; mm = 0 ; break;}
                case 6: {HH=2 ; mm = 30 ; break;}
                case 7: {HH=2 ; mm = 8 ; break;}
            }
            for ( int i=0 ; i <= positionSRTm ; i++ ){
                Log.i(TAG, Method +  "Intervals time = " + time  );
                alarmDetails = new EAlarmDetails();
                alarmDetails.setAlarmDetailHour(time.substring(0,5));
                lstAlarmList.add(alarmDetails );
                //lstAlarmList.add(time.substring(0,5));
                time = hourAdd(time,HH,mm,ss)  ;
            }
            callSetAdapter(lstAlarmList);

        }else if (positionSRTp == 1 ){
            int intvervalo=1;
            Log.i(TAG, Method +  "Intervalos - positionSRTm=" + positionSRTm  );
            switch (positionSRTm){
                case 0: {intvervalo = 2 ; break;}
                case 1: {intvervalo = 3 ; break;}
                case 2: {intvervalo = 4 ; break;}
                case 3: {intvervalo = 6 ; break;}
                case 4: {intvervalo = 8 ; break;}
                case 5: {intvervalo = 12 ; break;}
                case 6: {intvervalo = 24 ; break;}
            }
            periodo = HH24 / intvervalo ;
            Log.i(TAG, Method +  "Intervalos periodo=" + periodo  );

            if (periodo==1) intvervalo=24;
            for ( int i=0 ; i < intvervalo ; i++ ){
                Log.i(TAG, Method +  "Intervalos time=" + time  );
                alarmDetails = new EAlarmDetails();
                alarmDetails.setAlarmDetailHour(time.substring(0,5));
                lstAlarmList.add(alarmDetails );
                //lstAlarmList.add(time.substring(0,5));
                time = hourAdd(time,periodo,0,0)  ;
            }
            try{
                callSetAdapter(lstAlarmList);
            }catch (Exception e){e.printStackTrace();}

        }
        Log.i(TAG, Method +  "End..."  );
    }

    private String hourAdd(String hora , int hh , int mm , int ss){
        String Method = "[hourAdd]";
        Log.i(TAG, Method + " Init...");
        String hourAdd="";
        String [] time = hora.split(":");
        int h = Integer.parseInt( time[0] );
        int m = Integer.parseInt( time[1] );
        int s = Integer.parseInt( time[2] );

        s += ss ;
        m += mm ;
        h += hh ;
        if ( s > 59 ){
            s = s - 60;
            m += 1;
        }
        if ( m > 59 ){
            m = m - 60;
            h += 1;
        }
        if ( h > 23 ){
            h = h - 24;
        }

        hourAdd = (h<10 ? "0" + h : h) + ":" +  (m<10 ? "0" + m : m)  + ":" +  (s<10 ? "0" + s : s) + ""  ;
        Log.i(TAG, Method + " hourAdd="+hourAdd);
        Log.i(TAG, Method + " End...");

        return   hourAdd ;// (h<10 ? "0" + h : h) + ":" +  (m<10 ? "0" + m : m)  + ":" +  (s<10 ? "0" + s : s) + ""  ;
    }

    private void callSetAdapter( List<EAlarmDetails> lst ){
        String Method ="[callSetAdapter]";
        Log.i(TAG, Method + "Init..."  );
        if (adapterAlarm != null){
            adapterAlarm.updateData(lst);
        }else{
            adapterAlarm = new AlarmListReminderTimesCardAdapter(this , lst,  rv_reminder_times);
            rv_reminder_times.setAdapter(adapterAlarm);
            rv_reminder_times.setLayoutManager( new LinearLayoutManager(this) );
        }
        Log.i(TAG, Method + "End..."  );
    }

    private boolean [] fn_itemsCheckArray(){
        String Method = "[fn_itemsCheckArray]";
        Log.i(TAG, Method + " Init...");
        String tmp [] = rbtSpecificDaysOfWeek.getText().toString().split(":");
        boolean [] itemsCheck = {false, false, false, false, false, false, false};
        if (tmp.length==1){
            Log.i(TAG, Method + " tmp.length==1");
            Log.i(TAG, Method + " End...");
            return  itemsCheck;
        }
        else{
            String Days = getSelectedDayValue();
            Log.i(TAG, Method + " Days "  + Days );
            if ("0".equals(Days)){
                return  itemsCheck;
            }
            for (int i=0;i<Days.length();i++){
                Log.i(TAG, Method + " Days.substring(i,i+1)..."  + Days.substring(i,i+1));
                int dayIdx =  Integer.parseInt( Days.substring(i,i+1) ) -1;
                itemsCheck[dayIdx] = true;
            }
        }
        Log.i(TAG, Method + " End...");
        return itemsCheck;
    }

    private ArrayList<?> fn_itemsCheckArrayList(){
        String Method = "[fn_itemsCheckArrayList]";
        Log.i(TAG, Method + " Init...");
        ArrayList seletedItems=new ArrayList();
        String tmp [] = rbtSpecificDaysOfWeek.getText().toString().split(":");
        Log.i(TAG, Method + " End...");
        if (tmp.length==1){
            Log.i(TAG, Method + " tmp.length==1");
            //for (int i=0;i<7;i++){
            //    seletedItems.add(i);
            //}
        }else{
            String Days = getSelectedDayValue();
            Log.i(TAG, Method + " Days "  + Days );
            for (int i=0;i<Days.length();i++){
                Log.i(TAG, Method + " Days.substring(i,i+1)..."  + Days.substring(i,i+1));
                int dayIdx =  Integer.parseInt( Days.substring(i,i+1) ) -1;
                seletedItems.add(dayIdx);
            }

        }
        Log.i(TAG, Method + " End...");
        return seletedItems;
    }

    private void fn_showAlertDialogNameOfDays() {
        final String textRbt="Días específicos: ";
        String Method ="[fn_showAlertDialogNameOfDays]";
        Log.i(TAG, Method + "Init..."  );
        //final CharSequence[] items = {"Lunes","Martes","Miércoles","Jueves","Viernes","Sábado ","Domingo"};
        final CharSequence[] items = getResources().getStringArray(R.array.array_WeekDaysDesc) ;//{"Lunes","Martes","Miércoles","Jueves","Viernes","Sábado ","Domingo"};
        boolean [] itemsCheck = fn_itemsCheckArray();
        // arraylist to keep the selected items
        final ArrayList seletedItems=fn_itemsCheckArrayList();
        //seletedItems.add(2); seletedItems.add(3); seletedItems.add(5);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Seleccione los días de la semana");
        builder.setMultiChoiceItems(items, itemsCheck, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int indexSelected, boolean isChecked) {
                if (isChecked) {
                    // If the user checked the item, add it to the selected items
                    seletedItems.add(indexSelected);
                } else if (seletedItems.contains(indexSelected)) {
                    // Else, if the item is already in the array, remove it
                    seletedItems.remove(Integer.valueOf(indexSelected));
                }
            }
        });
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                //  Your code when user clicked on OK
                //  You can write the code  to save the selected item here
                String Days = "ALL";
                Collections.sort(seletedItems);
                Days = getDaySelected(seletedItems);
                if (Days.equals("ALL")) {
                    rbtEveryDay.setChecked(true);
                    rbtSpecificDaysOfWeek.setText(textRbt);
                }
                else {
                    rbtSpecificDaysOfWeek.setText(textRbt + Days);
                }

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                //  Your code when user clicked on Cancel
                String [] tmp = rbtSpecificDaysOfWeek.getText().toString().split(":");
                if (tmp.length==1){
                    rbtEveryDay.setChecked(true);
                }
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
        Log.i(TAG, Method + "End..." );
    }

    private void setWeekDays(){
        String Method = "[setWeekDays]";
        Log.i(TAG, Method + " Init...");

        final String[] items = getResources().getStringArray(R.array.array_WeekDaysDesc) ;//{"Lunes","Martes","Miércoles","Jueves","Viernes","Sábado ","Domingo"};
        WeekDays wk ;
        lstWeekDays = new ArrayList<>();
        for (int i=0 ; i< items.length  ; i++ ){
            wk = new WeekDays( (i+1) ,items[i]);
            Log.i(TAG, "wk.toString(): " + wk.toString() );
            this.lstWeekDays.add(wk);
        }
        Log.i(TAG, Method + " End...");
    }

    private String getDaySelectedFromDataBase(String Days){
        String Method="[getDaySelectedFromDataBase]";
        Log.i(TAG, Method + "Init..." );
        Log.i(TAG, Method + "Days=" + Days );
        String weekDays="ALL";

        if ( ! Days.equals("0")){
            weekDays="";
            for(int i=0 ; i < Days.length() ; i++){
                for (WeekDays wk : lstWeekDays){
                    int numberDayDb = Integer.parseInt(Days.substring(i,i+1) ) ;
                    if ( numberDayDb  ==  wk.getNumberDay() ){
                        lstWeekDaysSelected.add(wk);
                        weekDays = weekDays + wk.getNameDay() + "," ;
                    }
                }
            }
            weekDays = weekDays.substring(0, weekDays.length() -1 );
        }
        Log.i(TAG, Method + "End..." );
        return weekDays;
    }
    private String getDaySelected(ArrayList items){
        String Method="[getDaySelected]";
        Log.i(TAG, Method + "Init..." );
        String weekDays="";
        ArrayList itemsTmp = new ArrayList();
        itemsTmp = items;

        lstWeekDaysSelected.clear();
        if (items.size()==7){
            WeekDays wk = new WeekDays(0,"ALL");
            lstWeekDaysSelected.add(wk);
            weekDays="ALL,";
        }else{
            //items.sort((Comparator) itemsTmp);
//            for (int i=0; i<7;i++){
//                for (int j=0; j < itemsTmp.size();j++){
//                    if (i == (int)itemsTmp.get(j) ){items.add(i);}
//                }
//            }
            Log.i(TAG, Method + "lstWeekDays.size(): " + lstWeekDays.size() );
            Log.i(TAG, Method + "lstWeekDays.toString(): " + lstWeekDays.toString() );
            Log.i(TAG, Method + "items.size(): " + items.size() );
            for (int i=0 ; i < items.size();i++){
                Log.i(TAG, Method + "items.get(i).toString(): " +   items.get(i).toString() );
                Log.i(TAG, Method + "Integer.parseInt(items.get(i).toString()) + 1: " +   (Integer.parseInt(items.get(i).toString()) + 1)  );
                for (WeekDays wk : lstWeekDays) {
                    int numberDay = Integer.parseInt(items.get(i).toString())+1 ;
                    if ( numberDay ==  wk.getNumberDay() ){
                        lstWeekDaysSelected.add(wk);
                        weekDays = weekDays + wk.getNameDay() + "," ;
                    }
                }
            }
        }

        Log.i(TAG, Method + "lstWeekDaysSelected.size(): " + lstWeekDaysSelected.size() );
        Log.i(TAG, Method + "weekDays: " + weekDays );
        if (lstWeekDaysSelected.size() == 0) weekDays="ALL,";
        weekDays = weekDays.substring(0, weekDays.length() -1 );
        Log.i(TAG, Method + "weekDays: " + weekDays );
        Log.i(TAG, Method + "End..." );
        return weekDays;
    }

    private String getSelectedDayValue(){
        String Method="[getSelectedDayValue]";
        Log.i(TAG, Method + "Init..." );
        Log.i(TAG, Method + "lstWeekDaysSelected.size():" +lstWeekDaysSelected.size() );
        String weekDays = "" ;
        String key = "WeekDays" ;
        if (lstWeekDaysSelected.size()==7 || lstWeekDaysSelected.size()==0 ){
            weekDays="0";
        }else{
            for (WeekDays wk : lstWeekDays) {
                for (int i=0 ; i < lstWeekDaysSelected.size() ; i++){
                    Log.i(TAG, Method + "lstWeekDaysSelected.get(" + i + ").getNumberDay() = " + lstWeekDaysSelected.get(i).getNumberDay() + " | wk.getNumberDay()=" +wk.getNumberDay() );
                    if ( lstWeekDaysSelected.get(i).getNumberDay() == wk.getNumberDay() ){
                        weekDays += wk.getNumberDay() + "" ;
                        Log.i(TAG, Method + "weekDays=" + weekDays );
                    }
                }
            }
        }
        Log.i(TAG, Method + "getSelectedDayValue()="+weekDays );
        Log.i(TAG, Method + "End..." );
        return weekDays;
    }

    private void fn_showAlertDialogNumberOfDays( ) {
        String Method ="[fn_showAlertDialogNumberOfDays]";
        Log.i(TAG, Method + "Init..."  );

        final String textRbt="Número de días: ";
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.custom_dialog_edit_texbox, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);
        alertDialogBuilder.setView(promptsView);
        final EditText userInput = (EditText) promptsView
                .findViewById(R.id.editTextDialogUserInput);

        long daysDiff = 0;
        try {
            fechaI = new Date(2017,07,1);
            fechaF = new Date(2017,07,30);
            daysDiff = getDateDiff(fechaI,fechaF, TimeUnit.DAYS);
            String [] tmp = rbtNumberOfDays.getText().toString().split(":");
            daysDiff = 0;
            if (tmp.length > 1 ){
                daysDiff =  Integer.parseInt( tmp[1].trim() ) ;
                Log.i(TAG, Method + "daysDiff = " + daysDiff);
            }
        }catch (Exception e){daysDiff = 0;e.printStackTrace();}

        userInput.setText( String.valueOf( daysDiff) );

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // get user input and set it to result edit text
                                Log.i(TAG,  "[onClick] userInput.getText() = " + userInput.getText().toString().trim());
                                if (! "0".equals(userInput.getText().toString().trim())){
                                    rbtNumberOfDays.setText( textRbt + userInput.getText() );
                                }else{
                                    rbtNumberOfDays.setText(textRbt);
                                    rbtContinuous.setChecked(true);
                                }
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {

                                String [] tmp = rbtNumberOfDays.getText().toString().split(":");
                                if (tmp.length==1){
                                    rbtContinuous.setChecked(true);
                                }else if (tmp.length==2){
                                    if ("0".equals(tmp[1].trim())){
                                        rbtNumberOfDays.setText(textRbt);
                                        rbtContinuous.setChecked(true);
                                    }
                                }
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();
        Log.i(TAG, Method + "End..."  );
    }

    /**
     * Get a diff between two dates
     * @param date1 the oldest date
     * @param date2 the newest date
     * @param timeUnit the unit in which you want the diff
     * @return the diff value, in the provided unit
     */
    private long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        String Method="[getDateDiff]";
        Log.i(TAG, Method + "Init...");
        long diffInMillies = date2.getTime() - date1.getTime();
        Log.i(TAG, Method + "getDateDiff=" + timeUnit.convert(diffInMillies,TimeUnit.MILLISECONDS) );
        Log.i(TAG, Method + "End...");
        return timeUnit.convert(diffInMillies,TimeUnit.MILLISECONDS);
    }

    private Date getDateAdd(Date date1, int days,TimeUnit timeUnit){
        String Method="[getDateAdd]";
        Log.i(TAG, Method + "Init...");
        long addInMillies = date1.getTime() + timeUnit.DAYS.toMillis(days);
        Log.i(TAG, Method + "getDateAdd=" + new Date(addInMillies) );
        Log.i(TAG, Method + "End...");
        return new Date(addInMillies);
    }

    private Map<TimeUnit,Long> computeDiff(Date date1, Date date2) {
        String Method="[computeDiff]";
        Log.i(TAG, Method + "Init...");
        long diffInMillies = date2.getTime() - date1.getTime();
        List<TimeUnit> units = new ArrayList<TimeUnit>(EnumSet.allOf(TimeUnit.class));
        Collections.reverse(units);
        Map<TimeUnit,Long> result = new LinkedHashMap<TimeUnit,Long>();
        long milliesRest = diffInMillies;
        for ( TimeUnit unit : units ) {
            long diff = unit.convert(milliesRest,TimeUnit.MILLISECONDS);
            long diffInMilliesForUnit = unit.toMillis(diff);
            milliesRest = milliesRest - diffInMilliesForUnit;
            result.put(unit,diff);
        }
        Log.i(TAG, Method + "End...");
        return result;
    }

    class WeekDays{
        private int numberDay;
        private String nameDay;

        public int getNumberDay() {
            return numberDay;
        }

        public void setNumberDay(int numberDay) {
            this.numberDay = numberDay;
        }

        public String getNameDay() {
            return nameDay;
        }

        public void setNameDay(String nameDay) {
            this.nameDay = nameDay;
        }

        public WeekDays (int numberDay, String nameDay){
            this.numberDay = numberDay ;
            this.nameDay = nameDay ;
        }
        @Override
        public String toString(){
            return "[" +
                    " numberDay=" + this.getNumberDay() +
                    ",nameDay=" + this.getNameDay() +
                    " ]";
        }
    }

    class ValueDesc{
        private Object value;
        private Object description;

        public Object getValue() {
            return value;
        }

        public ValueDesc(){
        }

        public ValueDesc(Object value, Object description){
            this.value = value;
            this.description = description;
        }

        public void setValue(Object value) {
            this.value = value;
        }

        public Object getDescription() {
            return description;
        }

        public void setDescription(Object description) {
            this.description = description;
        }
        @Override
        public String toString(){
            return "[" +
                    "value="+ this.getValue() +
                    ",description=" +  this.getDescription() +
                    "]";
        }
    }

    class strComparator implements Comparator<String>{
        @Override
        public int compare(String a, String b){
            return a.compareTo(b);
        }

    }



}
