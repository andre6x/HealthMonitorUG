package com.grupocisc.healthmonitor.Medicines.activities;

import android.animation.Animator;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.inputmethod.InputMethodManager;
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
import com.grupocisc.healthmonitor.Alarm.adapters.AlarmListReminderTimesCardAdapter;
import com.grupocisc.healthmonitor.HealthMonitorApplicattion;
import com.grupocisc.healthmonitor.Medicines.adapters.MedicinesRListAdapter;
import com.grupocisc.healthmonitor.R;
import com.grupocisc.healthmonitor.Utils.Utils;
import com.grupocisc.healthmonitor.entities.EAlarmDetails;
import com.grupocisc.healthmonitor.entities.EMedicine;
import com.grupocisc.healthmonitor.entities.IConsulMedicines;
import com.grupocisc.healthmonitor.entities.OnItemClick;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
import java.sql.SQLDataException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import static com.grupocisc.healthmonitor.Utils.Utils.UcallCalendar;
import static com.grupocisc.healthmonitor.Utils.Utils.UcallCalendarTime;
import static com.grupocisc.healthmonitor.Utils.Utils.generarAlerta;
import static com.grupocisc.healthmonitor.Utils.Utils.inicializarFecha;
import static com.grupocisc.healthmonitor.Utils.Utils.isNumeric;


/**
 * Created by Gema on 29/01/2017.
 */

/*
*
*  REGISTRO DE MEDICAMENTOS
*
* */

public class MedicinesRegisteredActivity extends AppCompatActivity implements OnItemClick, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private static final String TAG = "[MedicinesRegActivity]";
    final Context context = this;
    private static final String sentServer = "N";
    private String operationDB = "I";
    private boolean isUpdate = false;

    String email = "";
    @BindView(R.id.fabMedicines)
    FloatingActionButton getFab;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.txt_nombreMed)
    TextView nombre;
    @BindView(R.id.txt_descripcion)
    TextView Tdescripcion;
    @BindView(R.id.txt_presentacion)
    TextView Tpresentacion;
    @BindView(R.id.txt_via)
    TextView Tvia;
    @BindView(R.id.txt_id)
    TextView Tid;
    @BindView(R.id.spinner_MedicineTypes)
    Spinner spinner_MedicineTypes;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.lyt_recycler)
    LinearLayout layout;
    //@BindView(R.id.card_medicines) CardView cardView;

    @BindView(R.id.layoutMainMedReg)
    LinearLayout layoutMain;
    @BindView(R.id.layoutButtonsMedReg)
    RelativeLayout layoutButtons;
    @BindView(R.id.layoutContentMedReg)
    RelativeLayout layoutContent;
    @BindView(R.id.cardMedReg)
    CardView cardReg;

    @BindView(R.id.cardMedCtrl)
    CardView cardMedCtrl;
    @BindView(R.id.fabMedCtrl)
    FloatingActionButton getFabMetCtrl;
    @BindView(R.id.txt_MedNombreMedCtrl)
    TextView txt_MedNombreMedCtrl;
    @BindView(R.id.txt_dosisMedCtrl)
    TextView txt_dosisMedCtrl;
    //@BindView(R.id.txt_vecesDiaMedCtrl)     TextView txt_vecesDiaMedCtrl;
    @BindView(R.id.txt_startDateMedCtrl)
    TextView txt_startDateMedCtrl;
    @BindView(R.id.txt_startHourMedCtrl)
    TextView txt_startHourMedCtrl;
    @BindView(R.id.txt_observacionMedCtrl)
    TextView txt_observacionMedCtrl;
    @BindView(R.id.lyt_startDateMedCtrl)
    LinearLayout lyt_startDateMedCtrl;
    @BindView(R.id.lyt_startHourMedCtrl)
    LinearLayout lyt_startHourMedCtrl;

    EditText inputSearch;
    public ProgressDialog Dialog;
    IConsulMedicines.RegMedicacion mSaveCrtRMedicines;
    Call<IConsulMedicines.RegMedicacion> medicacionCall;

    Call<List<IConsulMedicines.medicamentosAll>> listCall;
    private static List<IConsulMedicines.medicamentosAll> listMedM;
    private List<EMedicine> lstMedicine = new ArrayList<>();
    ;
    MedicinesRListAdapter rListAdapter;

    private String[] MedicineTypes;
    private int positionSpinner;

    // Alarms_INI
    private EAlarmDetails alarmDetails;

    private Map<Object, Object> mapDescValue;
    private List<Object> lstAllWeekDays;
    private List<Object> lstMedicineTypes;
    private List<Object> lstReminderTypes;
    private List<Object> lstReminderTimesF;
    private List<Object> lstReminderTimesI;
    private Date fechaI;
    private Date fechaF;
    private List<WeekDays> lstWeekDaysSelected = new ArrayList<>();
    private static List<WeekDays> lstWeekDays;

    private AlarmListReminderTimesCardAdapter adapterAlarm;
    private int positionSRTp;
    private int positionSRTm;
    private String[] ReminderTypes;
    private String[] ReminderTimesFrequencies;
    private String[] ReminderTimesIntervals;

    @BindView(R.id.spinner_ReminderTypes)
    Spinner spinner_ReminderTypes;
    @BindView(R.id.spinner_ReminderTimes)
    Spinner spinner_ReminderTimes;
    @BindView(R.id.lyt_reminder_times)
    LinearLayout lyt_reminder_times;
    @BindView(R.id.rv_reminder_times)
    RecyclerView rv_reminder_times;

    @BindView(R.id.rgrpDuration)
    RadioGroup rgrpDuration;
    @BindView(R.id.rbtContinuous)
    RadioButton rbtContinuous;
    @BindView(R.id.rbtNumberOfDays)
    RadioButton rbtNumberOfDays;

    @BindView(R.id.rgrpDays)
    RadioGroup rgrpDays;
    @BindView(R.id.rbtEveryDay)
    RadioButton rbtEveryDay;
    @BindView(R.id.rbtSpecificDaysOfWeek)
    RadioButton rbtSpecificDaysOfWeek;
    //@BindView(R.id.txt_NumberOfDay)      TextView txt_NumberOfDays;


    // Alarms_END

    @Override
    public void onCreate(Bundle savedInstanceState) {
        String Method = "[onCreate]";
        Log.i(TAG, Method + "Init...");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.medicines_registered_activity);
        ButterKnife.bind(this);
        inicializarRecursos();
        email = Utils.getEmailFromPreference(this);
        Dialog = new ProgressDialog(getBaseContext());

        if (savedInstanceState != null) {
            Log.i(TAG, Method + "savedInstanceState != null");
            alarmDetails = (EAlarmDetails) savedInstanceState.getSerializable("car");
            if (alarmDetails == null) {

            } else {
                if (getIntent() != null && getIntent().getExtras() != null && getIntent().getExtras().getSerializable("car") != null) {
                    alarmDetails = (EAlarmDetails) getIntent().getExtras().getSerializable("car");
                } else {
                    operationDB = "I";
                    Log.i(TAG, Method + "etxt_fecha.setText...");

                }
            }

        }

        //MedicineTypes =  getResources().getStringArray( R.array.array_MedicineTypeDesc);
        //ArrayAdapter<String> adapterRTp = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item  ,MedicineTypes);
        //this.spinner_MedicineTypes.setAdapter(adapterRTp);
        fillSpinner(spinner_MedicineTypes, "MedicineTypes");
        this.spinner_MedicineTypes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Object item = parent.getItemAtPosition(position);
                positionSpinner = position;
                int medicineType = Integer.parseInt((String) getSelectedValue(spinner_MedicineTypes, "MedicineTypes"));
                try {
                    callAdapter(medicineType);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //setSpinner(position);
                //Log.i(TAG, "onItemSelected: " + item.toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        setToolbar();

        getFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (nombre.length() > 0 && Tid.length() > 0) {
                    //saveDataRegisteredMedicineDBLocal(view,email,Integer.parseInt(Tid.getText().toString()) );
                    callActivityControlMedicine();

                } else {
                    Toast.makeText(getBaseContext(), "Seleccione medicamento", Toast.LENGTH_SHORT).show();
                }
            }
        });


        inputSearch = (EditText) findViewById(R.id.edt_buscar);
        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.i(TAG, "execute Search = " + inputSearch.getText().toString());
                String query = inputSearch.getText().toString();
                filterMedicines(query);
            }

            @Override
            public void afterTextChanged(Editable s) {
                //
            }
        });

        this.txt_startDateMedCtrl.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                //SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
                SimpleDateFormat formato = new SimpleDateFormat(getResources().getString(R.string.txt_DateFormat103));
                Calendar c = Calendar.getInstance();
                Date fecha = c.getTime();
                Date fecha2 = null;
                try {
                    fecha2 = formato.parse(txt_startDateMedCtrl.getText().toString());
                } catch (ParseException e) {
                    Log.e(TAG, "Error al convertir fecha " + e.getMessage());
                    e.printStackTrace();
                }

                if (fecha2.compareTo(fecha) != 0 && fecha2.compareTo(fecha) > 0) {
                    txt_startDateMedCtrl.setError("Fecha no válida.");
                    txt_startDateMedCtrl.setText(inicializarFecha());
                }

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        this.lyt_startDateMedCtrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callCalendar();
            }
        });

        this.txt_startDateMedCtrl.setText(inicializarFecha());


        this.lyt_startHourMedCtrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callCalendarTime();
            }
        });

        txt_startHourMedCtrl.setText("08:00");

        getFabMetCtrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (nombre.length() > 0 && Tid.length() > 0) {
                    String strDosis = txt_dosisMedCtrl.getText().toString();
                    if (strDosis.isEmpty()) {
                        Toast.makeText(getBaseContext(), "Ingrese valor de dosis", Toast.LENGTH_SHORT).show();
                    } else {
                        int intDosis = Integer.parseInt(strDosis);
                        if (intDosis > 0) {
                            saveDataDBLocal(view);
                        } else {
                            Toast.makeText(getBaseContext(), "Dosis no puede ser 0.", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(getBaseContext(), "Error al guardar.", Toast.LENGTH_SHORT).show();
                }
            }
        });


        // Alarms_Ini
        //fillSpinner(spinner_ReminderTypes,0,0);
        fillSpinner(this.spinner_ReminderTypes, "ReminderTypes");
        this.spinner_ReminderTypes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Object item = parent.getItemAtPosition(position);
                positionSRTp = position;
                //setSpinner(position);
                //fillSpinner(spinner_ReminderTimes,1,position);
                switch (position) {
                    case 0: {
                        fillSpinner(spinner_ReminderTimes, "ReminderTimesF");
                        break;
                    }
                    case 1: {
                        fillSpinner(spinner_ReminderTimes, "ReminderTimesI");
                        break;
                    }
                }

                //Log.i(TAG, "onItemSelected: " + item.toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        this.spinner_ReminderTimes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Object item = parent.getItemAtPosition(position);
                positionSRTm = position;
                setListAlarm(position);
                //Log.i(TAG, "onItemSelected: " + item.toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        setWeekDays();

        this.rbtContinuous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt = "Número de días:";
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
                String txt = "Días específicos:";
                rbtSpecificDaysOfWeek.setText(txt);
            }
        });

        this.rbtSpecificDaysOfWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fn_showAlertDialogNameOfDays();
            }
        });

//        this.rgrpDuration.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
//                RadioButton rb;
//                switch (checkedId){
//                    case   R.id.rbtContinuous:
////                        rb = (RadioButton) findViewById(checkedId);
////                        Toast.makeText(getBaseContext(),rb.getText(),Toast.LENGTH_SHORT).show();
//                        rbtNumberOfDays.setText("Número de días");
//                        break;
//                    case R.id.rbtNumberOfDays:
//                        //rb = (RadioButton) findViewById(checkedId);
//                        //Toast.makeText(getBaseContext(),rb.getText(),Toast.LENGTH_SHORT).show();
//                        fn_showAlertDialogNumberOfDays();
//                        break;
//                }
//            }
//        });

//        this.rgrpDays.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
//                RadioButton rb;
//                switch (checkedId){
//                    case   R.id.rbtEveryDay:
////                        rb = (RadioButton) findViewById(checkedId);
////                        Toast.makeText(getBaseContext(),rb.getText(),Toast.LENGTH_SHORT).show();
//                        rbtSpecificDaysOfWeek.setText("Días específicos");
//                        break;
//                    case R.id.rbtSpecificDaysOfWeek:
//                        fn_showAlertDialogNameOfDays();
//                        break;
//                }
//            }
//        });


        // Alamrs_End


        Log.i(TAG, Method + "Init Calling callAdapter()...");
        try {
            //callAdapter();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // callAdapterLocal();
        Log.i(TAG, Method + "End Calling callAdapter()...");
        Log.i(TAG, Method + "End...");
    }

    @Override
    public void onResume() {
        String Method = "[onResume]";
        Log.i(TAG, Method + "Init...");
        super.onResume();
        DatePickerDialog dpd = (DatePickerDialog) getFragmentManager().findFragmentByTag("Datepickerdialog");
        if (dpd != null)
            dpd.setOnDateSetListener(this);
        TimePickerDialog tpd = (TimePickerDialog) getFragmentManager().findFragmentByTag("Timepickerdialog");
        if (tpd != null)
            tpd.setOnTimeSetListener(this);
        Log.i(TAG, Method + "End...");
    }

    private void updateData(EAlarmDetails alarmDetails) {
        operationDB = "U";

        String date_ddMMyyyy = "01/01/1900";
        String date_HHmm = "00:00";


    }


    public void filterMedicines(String query) {
        String Method = "[filterMedicines]";
        Log.i(TAG, Method + "Init...");
        if (rListAdapter != null) {
            Log.i(TAG, Method + "rListAdapter != null");
            rListAdapter.filterUpdate(query);
        }
        Log.i(TAG, Method + "End...");
    }

    private void saveMedicineAllDataBaseLocal(List<IConsulMedicines.medicamentosAll> lstMedicine) {
        if (lstMedicine.size() > 0) {
            try {
                HealthMonitorApplicattion.getApplication().CleanMedicine();
            } catch (SQLException e) {
                Log.e(TAG, "Error HealthMonitorApplicattion.getApplication().CleanMedicine()");
            }

            //String mail = "marcopolomp3@gmail.com" ; //Utils.getEmailFromPreference(HealthMonitorApplicattion.getApplication());
            //String idUsuario = "marcopolomp3";
            for (int i = 0; i < lstMedicine.size() - 1; i++) {
                try {
                    Utils.saveMediceToDataBaseLocal(
                            lstMedicine.get(i).getIdMedicamento()
                            , lstMedicine.get(i).getNombre()
                            , lstMedicine.get(i).getDescripcion()
                            , lstMedicine.get(i).getPrincipioActivo()
                            , lstMedicine.get(i).getIndicaciones()
                            , lstMedicine.get(i).getRecomendaciones()
                            , lstMedicine.get(i).getVia()
                            , lstMedicine.get(i).getPresentacion()
                            , lstMedicine.get(i).getEstado()
                            , lstMedicine.get(i).getLaboratorio()
                            , lstMedicine.get(i).getMedicineType()
                            , HealthMonitorApplicattion.getApplication().getMedicineDao()

                    );
                } catch (SQLDataException e) {

                } catch (SQLException e) {
                    e.printStackTrace();
                }


            }
        }
    }

    private void callAdapterLocal(int medicineType) {
        String Method = "[callAdapterLocal]";
        Log.i(TAG, Method + "Init...");
        try {
            Log.i(TAG, Method + "Init Calling Utils.GetMedicineByTypeDBLocal");
            lstMedicine = Utils.GetMedicineByTypeDBLocal(HealthMonitorApplicattion.getApplication().getMedicineDao(), medicineType);
            Collections.sort(lstMedicine);
            this.postExecutionQuery();
        } catch (SQLException e) {
            Log.e(TAG, Method + "Error Calling Utils.GetMedicineByTypeDBLocal");
        }
        Log.i(TAG, Method + "End...");
    }

    private void callAdapter(int medicineType) throws SQLException {
        String Method = "[callAdapter]";
        Log.i(TAG, Method + "Init...");
        float fRows = 0;
        IConsulMedicines iConsulMedicines = HealthMonitorApplicattion.getApplication().getmRestCISCAdapter().create(IConsulMedicines.class);
        try {
            fRows = Utils.GetTotalMedicineDBLocal(HealthMonitorApplicattion.getApplication().getMedicineDao());
        } catch (Exception e) {

        }

        boolean isEmpty = true;

//        if (lstMedicine != null ){
//            Log.i(TAG, Method + "lstMedicine is not null ... "  );
//            if (lstMedicine.size()  > 0 ){
//                isEmpty=false;
//                Log.i(TAG, Method + "Table MedicineTable is NOT empty, NOT calling WS ... "  );
//            }
//        }

        if (fRows > 0) {
            isEmpty = false;
            Log.i(TAG, Method + "Table MedicineTable contains " + fRows + " register(s).");
        }else{
            llenarMedicinaDummy(100);
            llenarMedicineType();
            llenarReminderType();
            llenarReminderTime();
            isEmpty = false;

        }

        if ( ! isEmpty) {
            try {
                lstMedicine = Utils.GetMedicineByTypeDBLocal(HealthMonitorApplicattion.getApplication().getMedicineDao(), medicineType);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.i(TAG, Method + "lstMedicine.size() = " + lstMedicine.size());
            Log.i(TAG, Method + "Sorting table MedicineTable...");
            Collections.sort(lstMedicine);
            Log.i(TAG, Method + "Executing postExecutionQuery()...");
            postExecutionQuery();
            //
//            //Obtiene el listado de medicamentos
//            listCall = iConsulMedicines.MEDICAMENTOS_ALL_CALL();
//            listCall.enqueue(new Callback<List<IConsulMedicines.medicamentosAll>>() {
//                @Override
//                public void onResponse(Call<List<IConsulMedicines.medicamentosAll>> call, Response<List<IConsulMedicines.medicamentosAll>> response) {
//                    if(response.isSuccess()) {
//                        String Method ="[onResponse]";
//                        Log.i(TAG, Method + "Execuete WS success... "  );
//                        listMedM = null;
//                        listMedM = response.body();
//                        Collections.sort(listMedM);
//                        saveMedicineAllDataBaseLocal(listMedM);
//                        fillListMediceWithDataWs(listMedM);
//                        //postExecutionLogin();
//                        postExecutionQuery();
//                        Log.e(".....","Onsuccess MedReg");
//                    }
//                }
//                @Override
//                public void onFailure(Call<List<IConsulMedicines.medicamentosAll>> call, Throwable t) {
//                    Log.e(".....","Failure MedReg");
//                }
//            });
//
//


            //
        } else {
            Log.i(TAG, Method + "Table MedicineTable is empty, calling WS ... ");

            //Obtiene el listado de medicamentos
            /*
            listCall = iConsulMedicines.MEDICAMENTOS_ALL_CALL();
            listCall.enqueue(new Callback<List<IConsulMedicines.medicamentosAll>>() {
                @Override
                public void onResponse(Call<List<IConsulMedicines.medicamentosAll>> call, Response<List<IConsulMedicines.medicamentosAll>> response) {
                    if(response.isSuccess()) {
                        String Method ="[onResponse]";
                        Log.i(TAG, Method + "Execuete WS success... "  );
                        listMedM = null;
                        listMedM = response.body();
                        Collections.sort(listMedM);
                        saveMedicineAllDataBaseLocal(listMedM);
                        fillListMediceWithDataWs(listMedM);
                        //postExecutionLogin();
                        postExecutionQuery();
                        Log.e(".....","Onsuccess MedReg");
                    }
                }
                @Override
                public void onFailure(Call<List<IConsulMedicines.medicamentosAll>> call, Throwable t) {
                    Log.e(".....","Failure MedReg");
                }
            });
*/


        }


        Log.i(TAG, Method + "End...");
    }

    private void fillListMediceWithDataWs(List<IConsulMedicines.medicamentosAll> lstMed) {
        String Method = "[fillListMediceWithDataWs]";

        Log.i(TAG, Method + "Init...");
        if (!lstMedicine.isEmpty())
            lstMedicine.clear();
        ;
        for (IConsulMedicines.medicamentosAll medicina : lstMed) {
            EMedicine eMedicine = new EMedicine();
            eMedicine.setIdMedicamento(medicina.getIdMedicamento());
            eMedicine.setNombre(medicina.getNombre());
            eMedicine.setDescripcion(medicina.getDescripcion());
            eMedicine.setPrincipioActivo(medicina.getPrincipioActivo());
            eMedicine.setIndicaciones(medicina.getIndicaciones());
            eMedicine.setRecomendaciones(medicina.getRecomendaciones());
            eMedicine.setVia(medicina.getVia());
            eMedicine.setPresentacion(medicina.getPresentacion());
            eMedicine.setEstado(medicina.getEstado());
            eMedicine.setLaboratorio(medicina.getLaboratorio());
            lstMedicine.add(eMedicine);
        }
        Log.i(TAG, Method + "End...");

    }

    public void postExecutionQuery() {
        String Method = "[postExecutionQuery]";
        Log.i(TAG, Method + "Init...");
        if (lstMedicine != null) {
            if (lstMedicine.size() > 0) {
                rListAdapter = new MedicinesRListAdapter(this, lstMedicine, this);
                recyclerView.setAdapter(rListAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
            }
        }
        Log.i(TAG, Method + "End...");
    }

    public void postExecutionLogin() {
        String Method = "[postExecutionLogin]";
        Log.i(TAG, Method + "Init...");
        if (listMedM != null) {
            if (listMedM.size() > 0) {
//                rListAdapter = new MedicinesRListAdapter(this , listMedM, this);
//                recyclerView.setAdapter(rListAdapter);
//                recyclerView.setLayoutManager(new LinearLayoutManager( this ));
            }
        }
        Log.i(TAG, Method + "End...");
    }

    @Override
    public void onClick(String value) {
        if (value.length() != 0) {
            View view = this.getCurrentFocus(); //added
            if (view != null) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);//added
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);//added
            }
            //getIdMed()+";"+getNombreMed()+";"+getPresentacionMed()+";"+getDescripcionMed()+";"+getViaMed()
            String[] separated = value.split(";");
            Tid.setText(separated[0]);
            nombre.setText(separated[1]);
            Tpresentacion.setText(separated[2]);
            Tdescripcion.setText(separated[3]);
            Tvia.setText(separated[4]);

        }
    }

    public void setToolbar() {
        String Method = "[setToolbar]";
        Log.i(TAG, Method + "Init...");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false); //desactivar title
        toolbar.setNavigationIcon(R.mipmap.back); //buton back tollbar
        toolbar.setTitleTextColor(getResources().getColor(R.color.white)); //color tollbar title

        Log.i(TAG, Method + "End...");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String Method = "[onOptionsItemSelected]";
        Log.i(TAG, Method + "Init...");

        Log.i(TAG, Method + "this.cardReg.isActivated()" + this.cardReg.isActivated());
        Log.i(TAG, Method + "this.cardReg.isEnabled()" + this.cardReg.isEnabled());
        Log.i(TAG, Method + "this.cardReg.isShown()" + this.cardReg.isShown());

        Log.i(TAG, Method + "this.cardMedCtrl.isActivated()" + this.cardMedCtrl.isActivated());
        Log.i(TAG, Method + "this.cardMedCtrl.isEnabled()" + this.cardMedCtrl.isEnabled());
        Log.i(TAG, Method + "this.cardMedCtrl.isShown()" + this.cardMedCtrl.isShown());

        if (item.getItemId() == android.R.id.home && this.cardReg.isShown())
            finish();
        else if (item.getItemId() == android.R.id.home && this.cardMedCtrl.isShown()) {
            this.cardReg.setVisibility(View.VISIBLE);
            this.cardMedCtrl.setVisibility(View.GONE);
        }
        Log.i(TAG, Method + "End...");
        return super.onOptionsItemSelected(item);
    }

    private void saveDataRegisteredMedicineDBLocal(View view, final String userMail, final int id) {
        String Method = "[saveDataRegisteredMedicineDBLocal]";
        Log.i(TAG, Method + "Init...");

        Calendar c = Calendar.getInstance();
        String dd = c.get(Calendar.DAY_OF_MONTH) < 10 ? "0" + c.get(Calendar.DAY_OF_MONTH) : c.get(Calendar.DAY_OF_MONTH) + "";
        String mm = (c.get(Calendar.MONTH) + 1) < 10 ? "0" + (c.get(Calendar.MONTH) + 1) : (c.get(Calendar.MONTH) + 1) + "";
        String aa = c.get(Calendar.YEAR) + "";
        String HH = c.get(Calendar.HOUR_OF_DAY) < 10 ? "0" + c.get(Calendar.HOUR_OF_DAY) : c.get(Calendar.HOUR_OF_DAY) + "";
        String MM = c.get(Calendar.MINUTE) < 10 ? "0" + c.get(Calendar.MINUTE) : c.get(Calendar.MINUTE) + "";
        String ss = c.get(Calendar.SECOND) < 10 ? "0" + c.get(Calendar.SECOND) : c.get(Calendar.SECOND) + "";
        String time = HH + ":" + MM + ":" + ss;
        final String fechaRegistro = aa + "/" + mm + "/" + dd + " " + time;

        //validar campos llenos
        if (nombre.length() > 0) {
            /*Manda a guardar el registro al confirmar*/
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(getResources().getString(R.string.msg_SaveRegisterQuestion))
                    .setTitle(getResources().getString(R.string.txt_Save))
                    .setPositiveButton(getResources().getString(R.string.txt_Accept),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    int iRows = 0;
                                    try {
                                        iRows = Utils.saveRegisteredMedicineUserToDataBaseLocal(id, fechaRegistro, userMail, 0
                                                , HealthMonitorApplicattion.getApplication().getMedicineUserDao()
                                        );
                                        //generarAlerta(MedicinesRegisteredActivity.this, getString(R.string.txt_atencion), "GUARDA OK");
                                        //finish();
                                        postExecutionSaveData();
                                    } catch (Exception e) {

                                        //showLayoutDialog();
                                        generarAlerta(MedicinesRegisteredActivity.this, getString(R.string.txt_atencion), getString(R.string.text_error_metodo));
                                        Log.e(TAG, "Error en la petición");
                                    }
                                    Log.e("Builder:", "");
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


        Log.i(TAG, Method + "End...");
    }

    //GUARDOS DATOS EN LA TABLA BD
    public void saveDataRegisteredMedicinesDB(View view, final String userMail, final int id) {
        String Method = "[saveDataRegisteredMedicinesDB]";
        Log.i(TAG, Method + "Init...");
        //Fecha
        Calendar c = Calendar.getInstance();
        String dd = c.get(Calendar.DAY_OF_MONTH) < 10 ? "0" + c.get(Calendar.DAY_OF_MONTH) : c.get(Calendar.DAY_OF_MONTH) + "";
        String mm = (c.get(Calendar.MONTH) + 1) < 10 ? "0" + (c.get(Calendar.MONTH) + 1) : (c.get(Calendar.MONTH) + 1) + "";
        String aa = c.get(Calendar.YEAR) + "";
        String time = c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE) + ":" + c.get(Calendar.SECOND);
        final String fechaRegistro = aa + "/" + mm + "/" + dd + " " + time;

        //validar campos llenos
        if (nombre.length() > 0) {
            /*Manda a guardar el registro al confirmar*/
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("¿Desea guardar el registro?")
                    .setTitle("Guardar")
                    .setPositiveButton("ACEPTAR",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    IConsulMedicines regMed = HealthMonitorApplicattion.getApplication().getmRestCISCAdapterP().create(IConsulMedicines.class);
                                    medicacionCall = regMed.RegMedicacion(userMail, id, fechaRegistro);
                                    medicacionCall.enqueue(new Callback<IConsulMedicines.RegMedicacion>() {
                                        @Override
                                        public void onResponse(Call<IConsulMedicines.RegMedicacion> call, Response<IConsulMedicines.RegMedicacion> response) {
                                            if (response.isSuccessful()) {
                                                Log.i(TAG, "Respuesta exitosa onSave");
                                                mSaveCrtRMedicines = response.body();
                                                postExecutionEnviarData();

                                            } else {
                                                //showLayoutDialog();
                                                generarAlerta(MedicinesRegisteredActivity.this, getString(R.string.txt_atencion), getString(R.string.text_error_metodo));
                                                Log.i(TAG, "Error en la petición");
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<IConsulMedicines.RegMedicacion> call, Throwable t) {
                                            Log.i(TAG, "Error onFailure");
                                        }
                                    });
                                    Log.i(TAG, "Builder:");
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

        Log.i(TAG, Method + "End...");
    }

//    private void showLoadingDialog() {
//        String Method ="[showLoadingDialog]";
//        Log.i(TAG, Method + "Init..."  );
//        Dialog.setMessage("Espere un Momento..");
//        Dialog.setCancelable(false);
//        Dialog.show();
//        Log.i(TAG, Method + "End..."  );
//    }
//    //
//    private void showLayoutDialog(){
//        String Method ="[showLayoutDialog]";
//        Log.i(TAG, Method + "Init..."  );
//        if (Dialog != null)
//            Dialog.dismiss();
//        Log.i(TAG, Method + "End..."  );
//    }

    public void postExecutionEnviarData() {
        String Method = "[postExecutionEnviarData]";
        Log.i(TAG, Method + "Init...");
        if (mSaveCrtRMedicines != null) {
            if (mSaveCrtRMedicines.getCodigo() == 0) {
                generarAlerta(this, getString(R.string.txt_atencion), mSaveCrtRMedicines.getRespuesta());
                finish();
            } else {
                generarAlerta(this, getString(R.string.txt_atencion), mSaveCrtRMedicines.getRespuesta());
            }
        } else {
            generarAlerta(this, getString(R.string.txt_atencion), getString(R.string.text_error_metodo));
        }
        Log.i(TAG, Method + "End...");
    }

    private void restartAlarmService() {
        String Method = "[restartAlarmService]";
        Log.i(TAG, Method + "Init...");
        Utils.restartAlarmService(this);
        Log.i(TAG, Method + "End...");
    }

    private void postExecutionSaveData() {
        //generarAlerta(this, getString(R.string.txt_atencion), "Registro ingresado con éxito" );
        nextAction();
        //finish();
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

    private void callActivityControlMedicine() {
        String Method = "[callActivityControlMedicine]";
//        Log.i(TAG, Method + "Init..."  );
//        Intent intent = new Intent(this, MedicineRegistry.class);
//        startActivity(intent);

        this.txt_MedNombreMedCtrl.setText(this.nombre.getText());

        this.cardReg.setVisibility(View.GONE);
        this.cardMedCtrl.setVisibility(View.VISIBLE);
        Log.i(TAG, Method + "End...");
    }

    //Se ejecuta al dar ACeptar al dialogo
    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String Method = "[onDateSet]";
        Log.i(TAG, Method + "Init...");
        String mes = "" + ((monthOfYear + 1) < 10 ? "0" + (monthOfYear + 1) : "" + (monthOfYear + 1));
        String dia = dayOfMonth < 10 ? "0" + dayOfMonth : "" + dayOfMonth;
        String date = "" + dia + "/" + mes + "/" + year;
        this.txt_startDateMedCtrl.setText(date);
        Log.i(TAG, Method + "End...");
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        String Method = "[onTimeSet]";
        Log.i(TAG, Method + "Init...");
        String hourString = hourOfDay < 10 ? "0" + hourOfDay : "" + hourOfDay;
        String minuteString = minute < 10 ? "0" + minute : "" + minute;
        String time = "" + hourString + ":" + minuteString;
        this.txt_startHourMedCtrl.setText(time);
        if (this.positionSRTp == 0)
            this.setListAlarm(positionSRTm);
        Log.i(TAG, Method + "End...");
    }

    /* CALENDAR */
    public void callCalendar() {
        String Method = "[callCalendarDate]";
        Log.i(TAG, Method + "Init...");
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.setMaxDate(now);
        UcallCalendar(dpd).show(getFragmentManager(), "Datepickerdialog");
        Log.i(TAG, Method + "End...");
    }

    /* TIME */
    public void callCalendarTime() {
        String Method = "[callCalendarTime]";
        Log.i(TAG, Method + "Init...");
        Calendar now = Calendar.getInstance();
        TimePickerDialog tpd = TimePickerDialog.newInstance(
                this,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                true /*mode24Hours.isChecked()*/
        );
        UcallCalendarTime(tpd).show(getFragmentManager(), "Timepickerdialog");
        Log.i(TAG, Method + "End...");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (this.cardReg.isShown())
                finish();
            else if (this.cardMedCtrl.isShown()) {
                this.cardReg.setVisibility(View.VISIBLE);
                this.cardMedCtrl.setVisibility(View.GONE);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private boolean saveDataDBLocal(View view) {

        boolean saveMedicieUser = false;
        boolean saveMedicineUserControl = false;

        if (nombre.length() > 0) {
            /*Manda a guardar el registro al confirmar*/
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(getResources().getString(R.string.msg_SaveRegisterQuestion))
                    .setTitle(getResources().getString(R.string.txt_Save))
                    .setPositiveButton(getResources().getString(R.string.txt_Accept),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (saveDataMedicineControlDBLocal()) {
                                        restartAlarmService();
                                        postExecutionSaveData();
                                    } else {

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


        return false;
    }


    private boolean saveDataMedicineControlDBLocal() {
        final String Method = "[saveDataMedicineControlDBLocal]";
        Log.i(TAG, Method + "Init...");
        boolean saveDataMedicineControlDBLocal = false;
        int medicineNumDays = 0;
        int yyyy = Integer.parseInt(this.txt_startDateMedCtrl.getText().toString().substring(6, 10));
        int MM = Integer.parseInt(this.txt_startDateMedCtrl.getText().toString().substring(3, 5)) - 1; // 0=Jan,1=Feb...,11=Dec
        int dd = Integer.parseInt(this.txt_startDateMedCtrl.getText().toString().substring(0, 2));
        String[] tmp = rbtNumberOfDays.getText().toString().split(":");

        final int dosis = isNumeric(this.txt_dosisMedCtrl.getText().toString()) == true ? Integer.parseInt(this.txt_dosisMedCtrl.getText().toString()) : 0;
        final String fechaInicio = this.txt_startDateMedCtrl.getText().toString().substring(6, 10) + "/" + this.txt_startDateMedCtrl.getText().toString().substring(3, 5) + "/" + this.txt_startDateMedCtrl.getText().toString().substring(0, 2) + " " + this.txt_startHourMedCtrl.getText().toString() + ":00";

        final String obsevaciones = this.txt_observacionMedCtrl.getText().toString();
        final int idMedicine = Integer.parseInt(Tid.getText().toString());
        //final String fechaRegistro = Utils.getDate("yyyy/MM/dd HH:mm:ss");
        final boolean existsMedicine = fnExistsMediceRegistred(idMedicine);
        //validar campos llenos

        final String reminderTypeCode = getSelectedValue(this.spinner_ReminderTypes, "ReminderTypes").toString(); // ObtenerValor de Spinner Frecuencia.
        String optionSpinnerKey = reminderTypeCode.equals("F") ? "ReminderTimesF" : "ReminderTimesI";
        Log.i(TAG, Method + "optionSpinnerKey = " + optionSpinnerKey);
        final int reminderTimeCode = Integer.parseInt(getSelectedValue(this.spinner_ReminderTimes, optionSpinnerKey).toString()); //Obtener valor de 2do Spinner
        final String diasMedicacion = getSelectedDayValue(); // Obtener String de días, 0=ALL - 135=LMV - 25=MV

        if (tmp.length > 1) {
            medicineNumDays = Integer.parseInt(tmp[1].trim());
            Log.i(TAG, Method + "medicineNumDays = " + medicineNumDays);
        }
        Date tmpFinalDate = new Date(yyyy, MM, dd);
        if (medicineNumDays != 0) {
            Log.i(TAG, Method + "new Date(" + yyyy + "," + MM + "," + dd + ") = " + new Date(yyyy, MM, dd));
            tmpFinalDate = getDateAdd(new Date(yyyy, MM, dd), medicineNumDays, TimeUnit.DAYS);
            Log.i(TAG, Method + "tmpFinalDate = " + tmpFinalDate);
            Log.i(TAG, Method + "tmpFinalDate.getYear() = " + tmpFinalDate.getYear());
            Log.i(TAG, Method + "tmpFinalDate.getMonth() = " + (tmpFinalDate.getMonth() + 1));
            Log.i(TAG, Method + "tmpFinalDate.getDate() = " + tmpFinalDate.getDate());
        }


        String finalYYYY = tmpFinalDate.getYear() + "";
        String finalMM = (tmpFinalDate.getMonth() + 1) < 10 ? "0" + (tmpFinalDate.getMonth() + 1) : (tmpFinalDate.getMonth() + 1) + "";
        String finaldd = tmpFinalDate.getDate() < 10 ? "0" + tmpFinalDate.getDate() : tmpFinalDate.getDate() + "";
        String finalDate = finalYYYY + "/" + finalMM + "/" + finaldd + " " + this.txt_startHourMedCtrl.getText().toString() + ":00";

        final String fechaFin = medicineNumDays != 0 ? finalDate : null;

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
            int registeredMedicinesId = 0;
            if (!existsMedicine) {
                int iRows = 0;

                try {
                    iRows = Utils.saveRegisteredMedicineUserToDataBaseLocal(idMedicine, Utils.getDate("yyyy/MM/dd HH:mm:ss"), email
                            , 0, HealthMonitorApplicattion.getApplication().getMedicineUserDao()
                    );
                } catch (Exception e) {
                    //generarAlerta(MedicinesRegisteredActivity.this, getString(R.string.txt_atencion), getString(R.string.text_error_metodo));
                    Log.e(TAG, Method + "Error saving Medicine User Register");
                }
                if (iRows > 0) {

                    registeredMedicinesId = Utils.saveRegisteredMedicinesToDataBaseLocal(idMedicine, dosis, fechaInicio, fechaFin, reminderTypeCode, reminderTimeCode, diasMedicacion, obsevaciones,
                            email, 0,
                            HealthMonitorApplicattion.getApplication().getRegisteredMedicinesDao());
                    if (registeredMedicinesId > 0) {
                        if (saveAlarmsDetails(adapterAlarm.getItemCount(), registeredMedicinesId)) {
                            saveDataMedicineControlDBLocal = true;
                        }
                    }
                }
            } else {
                registeredMedicinesId = Utils.saveRegisteredMedicinesToDataBaseLocal(idMedicine, dosis, fechaInicio, fechaFin, reminderTypeCode, reminderTimeCode, diasMedicacion, obsevaciones,
                        email, 0,
                        HealthMonitorApplicattion.getApplication().getRegisteredMedicinesDao());
                if (registeredMedicinesId > 0) {
                    if (saveAlarmsDetails(adapterAlarm.getItemCount(), registeredMedicinesId)) {
                        saveDataMedicineControlDBLocal = true;
                    }
                }
            }

        } catch (Exception e) {
            saveDataMedicineControlDBLocal = false;
            Log.e(TAG, Method + "Error saving Medicine User Control Register. " + e.getMessage());
        }

        return saveDataMedicineControlDBLocal;
    }

    private boolean saveAlarmsDetails(int numReg, int regMedId) {
        boolean saveAlarmsDetails = false;
        String alarmDetailHour;
        for (int i = 0; i < numReg; i++) {
            alarmDetailHour = adapterAlarm.getAlarmHour(i) + ":00";
            try {
                if (Utils.saveAlarmDetailsToDataBaseLocal(HealthMonitorApplicattion.getApplication().getEAlarmDetailsDao(), regMedId, alarmDetailHour, "", email, 0) > 0) {
                    saveAlarmsDetails = true;
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }

        return saveAlarmsDetails;
    }

    private boolean fnExistsMediceRegistred(int idMedicine) {

        float fRows = 0;
        IConsulMedicines iConsulMedicines = HealthMonitorApplicattion.getApplication().getmRestCISCAdapter().create(IConsulMedicines.class);
        try {
            fRows = Utils.GetTotalMedicineUserRegisteredByIdFromDBLocal(idMedicine, HealthMonitorApplicattion.getApplication().getMedicineUserDao());
        } catch (Exception e) {

        }

        if (fRows > 0)
            return true;
        else
            return false;
    }

//    private String getDate(String format){
//        String Method ="[getDate]";
//        Log.i(TAG, Method + "Init..." );
//        Calendar c = Calendar.getInstance();
//        String dd = c.get(Calendar.DAY_OF_MONTH) < 10? "0"+c.get(Calendar.DAY_OF_MONTH) : c.get(Calendar.DAY_OF_MONTH)+"" ;
//        String mm = (c.get(Calendar.MONTH)+1) < 10? "0"+ (c.get(Calendar.MONTH)+1) : (c.get(Calendar.MONTH)+1) + "";
//        String yyyy = c.get(Calendar.YEAR) + "";
//        String HH =  c.get(Calendar.HOUR_OF_DAY) < 10 ? "0" + c.get(Calendar.HOUR_OF_DAY) : c.get(Calendar.HOUR_OF_DAY)  + "";
//        String MM = c.get(Calendar.MINUTE) < 10 ? "0" + c.get(Calendar.MINUTE) : c.get(Calendar.MINUTE)  + "";
//        String ss = c.get(Calendar.SECOND) < 10 ? "0" + c.get(Calendar.SECOND) : c.get(Calendar.SECOND)  + "";
//        String time = HH + ":" + MM + ":" + ss;
//        String getDate = yyyy + "/" + mm +"/" + dd + " " + time;
//        Log.i(TAG, Method + "Format received = " + format );
//
//        switch (format){
//            case "ddMMyyyy":{ getDate =dd + mm + yyyy ;
//                            break;
//            }
//            case "dd/MM/yyyy":{ getDate = dd + "/" + mm + "/" + yyyy ;
//                break;
//            }
//            case "yyyy/MM/dd HH:mm:ss":{ getDate = yyyy + "/" + mm + "/" + dd + " "  + HH + ":" + MM + ":" + ss;
//                break;
//            }
//            case "HH:mm:ss":{ getDate = HH + ":" + MM ;
//                break;
//            }
//            default:getDate = yyyy + "/" + mm +"/" + dd + " " + time;
//                break;
//        }
//        Log.i(TAG, Method + "Date format to return is: " + getDate );
//        Log.i(TAG, Method + "End..." );
//        return getDate;
//    }

    private void inicializarRecursos() {
        Log.i(TAG, "inicializarRecursos: Init...");
        mapDescValue = new LinkedHashMap<Object, Object>();
        lstAllWeekDays = new ArrayList<Object>();
        lstMedicineTypes = new ArrayList<Object>();
        lstReminderTypes = new ArrayList<Object>();
        lstReminderTimesF = new ArrayList<Object>();
        lstReminderTimesI = new ArrayList<Object>();

        ValueDesc valueDesc;
        String[] tmpCode = new String[0];
        String[] tmpDesc = new String[0];
        //
        tmpCode = getResources().getStringArray(R.array.array_WeekDaysCode);
        tmpDesc = getResources().getStringArray(R.array.array_WeekDaysDesc);
        Log.i(TAG, "inicializarRecursos: tmpCode.length = " + tmpCode.length);
        for (int i = 0; i < tmpCode.length; i++) {
            valueDesc = new ValueDesc(tmpCode[i], tmpDesc[i]);
            lstAllWeekDays.add(valueDesc);
        }
        mapDescValue.put("WeekDays", lstAllWeekDays);

        tmpCode = getResources().getStringArray(R.array.array_MedicineTypesCode);
        tmpDesc = getResources().getStringArray(R.array.array_MedicineTypesDesc);
        Log.i(TAG, "inicializarRecursos: tmpCode.length = " + tmpCode.length);
        for (int i = 0; i < tmpCode.length; i++) {
            //Log.i(TAG, "inicializarRecursos.ReminderTypes: Code="+tmpCode[i] + " Desc=" + tmpDesc[i] );
            valueDesc = new ValueDesc(tmpCode[i], tmpDesc[i]);
            lstMedicineTypes.add(valueDesc);
        }
        mapDescValue.put("MedicineTypes", lstMedicineTypes);

        tmpCode = getResources().getStringArray(R.array.array_ReminderTypesCode);
        tmpDesc = getResources().getStringArray(R.array.array_ReminderTypesDesc);
        for (int i = 0; i < tmpCode.length; i++) {
            valueDesc = new ValueDesc(tmpCode[i], tmpDesc[i]);
            lstReminderTypes.add(valueDesc);
        }
        mapDescValue.put("ReminderTypes", lstReminderTypes);

        tmpCode = getResources().getStringArray(R.array.array_ReminderTimesFCode);
        tmpDesc = getResources().getStringArray(R.array.array_ReminderTimesFDesc);
        for (int i = 0; i < tmpCode.length; i++) {
            valueDesc = new ValueDesc(tmpCode[i], tmpDesc[i]);
            lstReminderTimesF.add(valueDesc);
        }
        mapDescValue.put("ReminderTimesF", lstReminderTimesF);

        tmpCode = getResources().getStringArray(R.array.array_ReminderTimesICode);
        tmpDesc = getResources().getStringArray(R.array.array_ReminderTimesIDesc);
        for (int i = 0; i < tmpCode.length; i++) {
            valueDesc = new ValueDesc(tmpCode[i], tmpDesc[i]);
            lstReminderTimesI.add(valueDesc);
        }
        mapDescValue.put("ReminderTimesI", lstReminderTimesI);
        List<ValueDesc> lst;
        Log.i(TAG, "inicializarRecursos: Mapa = " + mapDescValue.size());
        for (Map.Entry<Object, Object> entry : mapDescValue.entrySet()) {
            System.out.println("clave=" + entry.getKey() + ", valor=" + entry.getValue());
            lst = (List) entry.getValue();
            for (ValueDesc vd : lst) {
                Log.i(TAG, "inicializarRecursos.Map.key=" + entry.getKey() + " Code=" + vd.getValue() + " Desc=" + vd.getDescription());
            }
        }
        Log.i(TAG, "inicializarRecursos: End...");
    }

    private void fillSpinner(Spinner spinner, String key) {
        String Method = "fillSpinner";
        Log.i(TAG, Method + " Init...");
        Log.i(TAG, Method + " key = " + key);
        ArrayAdapter<String> adapterRTp;
        List<ValueDesc> lst = null;
        List<String> lstDesc = new ArrayList<>();
        for (Map.Entry<Object, Object> entry : mapDescValue.entrySet()) {
            //System.out.println("clave=" + entry.getKey() + ", valor=" + entry.getValue());
            if (key.equals(entry.getKey().toString())) {
                lst = (List) entry.getValue();
                for (ValueDesc vd : lst) {
                    //Log.i(TAG, "fillSpinner.Map.key=" + entry.getKey() + " Code="+vd.getValue() + " Desc=" + vd.getDescription() );
                    lstDesc.add((String) vd.getDescription());
                }
            }
        }
        adapterRTp = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, lstDesc);
        spinner.setAdapter(adapterRTp);
        Log.i(TAG, Method + " End...");
    }

    private Object getSelectedValue(Spinner spinner, String key) {
        String Method = "[getSelectedValue]";
        Log.i(TAG, Method + " Init...");
        Object obj = spinner.getSelectedItem();
        List<ValueDesc> lst = null;
        List<String> lstDesc = new ArrayList<>();
        for (Map.Entry<Object, Object> entry : mapDescValue.entrySet()) {
            System.out.println("clave=" + entry.getKey() + ", valor=" + entry.getValue());
            if (key.equals(entry.getKey().toString())) {
                lst = (List) entry.getValue();
                int idx = spinner.getSelectedItemPosition();
                obj = lst.get(idx).getValue();
            }
        }
        Log.i(TAG, Method + " with key=" + key + " get value=" + obj);
        Log.i(TAG, Method + " End...");
        return obj;
    }

    private void fillSpinner(Spinner spinner, int option, int positionSpinner) {
        String Method = "[fillSpinner]";
        Log.i(TAG, Method + " Init...");
        ArrayAdapter<String> adapterRTp;
        String[] SpinnerText = new String[0];

        if (option == 0) {
            SpinnerText = getResources().getStringArray(R.array.array_ReminderTypesDesc);
        } else if (option == 1) {
            //ReminderTimesFrequencies =  getResources().getStringArray( R.array.ReminderTimesF_array);
            //ReminderTimesIntervals =  getResources().getStringArray( R.array.ReminderTimesI_array );
            switch (positionSpinner) {
                case 0: {
                    SpinnerText = getResources().getStringArray(R.array.array_ReminderTimesFDesc);
                    break;
                }
                case 1: {
                    SpinnerText = getResources().getStringArray(R.array.array_ReminderTimesIDesc);
                    break;
                }
            }

        }

        //ArrayAdapter<String> adapterRTp = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item  ,ReminderTypes);
        adapterRTp = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, SpinnerText);
        spinner.setAdapter(adapterRTp);
        Log.i(TAG, Method + " End...");
    }

    private void setListAlarm(int id) {
        String Method = "[setListAlarm]";
        Log.i(TAG, Method + " Init...");
        int HH = Integer.parseInt(this.txt_startHourMedCtrl.getText().toString().substring(0, 2));
        int mm = Integer.parseInt(this.txt_startHourMedCtrl.getText().toString().substring(3, 5));
        int ss = 0;
        String time = (HH < 10 ? "0" + HH : HH) + ":" + (mm < 10 ? "0" + mm : mm) + ":" + (ss < 10 ? "0" + ss : ss) + "";
        int HH24 = 24;
        int periodo = 0;

        Log.i(TAG, Method + "Init...");
        //List<String> lstAlarmList = new ArrayList<String>();
        List<EAlarmDetails> lstAlarmDetails = new ArrayList<>();
        EAlarmDetails alarmDetails;

        if (positionSRTp == 0) {
            Log.i(TAG, Method + "Intervalos positionSRTm=" + positionSRTm);
            int intvervalo = 1;
            time = txt_startHourMedCtrl.getText().toString() + ":00";
            switch (positionSRTm) {
                case 0: {
                    HH = 0;
                    mm = 0;
                    break;
                }
                case 1: {
                    HH = 15;
                    mm = 0;
                    break;
                }
                case 2: {
                    HH = 7;
                    mm = 30;
                    break;
                }
                case 3: {
                    HH = 5;
                    mm = 0;
                    break;
                }
                case 4: {
                    HH = 3;
                    mm = 45;
                    break;
                }
                case 5: {
                    HH = 3;
                    mm = 0;
                    break;
                }
                case 6: {
                    HH = 2;
                    mm = 30;
                    break;
                }
                case 7: {
                    HH = 2;
                    mm = 8;
                    break;
                }
            }
            for (int i = 0; i <= positionSRTm; i++) {
                Log.i(TAG, Method + "Intervals time = " + time);
                alarmDetails = new EAlarmDetails();
                alarmDetails.setAlarmDetailHour(time.substring(0, 5));
                lstAlarmDetails.add(alarmDetails);
                //lstAlarmList.add(time.substring(0,5));
                time = hourAdd(time, HH, mm, ss);
            }
            callSetAdapter(lstAlarmDetails);

        } else if (positionSRTp == 1) {
            int intvervalo = 1;
            Log.i(TAG, Method + "Intervalos positionSRTm=" + positionSRTm);
            switch (positionSRTm) {
                case 0: {
                    intvervalo = 2;
                    break;
                }
                case 1: {
                    intvervalo = 3;
                    break;
                }
                case 2: {
                    intvervalo = 4;
                    break;
                }
                case 3: {
                    intvervalo = 6;
                    break;
                }
                case 4: {
                    intvervalo = 8;
                    break;
                }
                case 5: {
                    intvervalo = 12;
                    break;
                }
                case 6: {
                    intvervalo = 24;
                    break;
                }
            }
            periodo = HH24 / intvervalo;
            Log.i(TAG, Method + "Intervalos periodo=" + periodo);

            if (periodo == 1) intvervalo = 24;
            for (int i = 0; i < intvervalo; i++) {
                Log.i(TAG, Method + "Intervalos time=" + time);

                alarmDetails = new EAlarmDetails();
                alarmDetails.setAlarmDetailHour(time.substring(0, 5));
                lstAlarmDetails.add(alarmDetails);

                //lstAlarmList.add(time.substring(0,5));
                time = hourAdd(time, periodo, 0, 0);
            }
            try {
                callSetAdapter(lstAlarmDetails);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        Log.i(TAG, Method + "End...");
    }

    private String hourAdd(String hora, int hh, int mm, int ss) {
        String Method = "[hourAdd]";
        Log.i(TAG, Method + " Init...");
        String hourAdd = "";
        String[] time = hora.split(":");
        int h = Integer.parseInt(time[0]);
        int m = Integer.parseInt(time[1]);
        int s = Integer.parseInt(time[2]);

        s += ss;
        m += mm;
        h += hh;
        if (s > 59) {
            s = s - 60;
            m += 1;
        }
        if (m > 59) {
            m = m - 60;
            h += 1;
        }
        if (h > 23) {
            h = h - 24;
        }

        hourAdd = (h < 10 ? "0" + h : h) + ":" + (m < 10 ? "0" + m : m) + ":" + (s < 10 ? "0" + s : s) + "";
        Log.i(TAG, Method + " hourAdd=" + hourAdd);
        Log.i(TAG, Method + " End...");

        return hourAdd;// (h<10 ? "0" + h : h) + ":" +  (m<10 ? "0" + m : m)  + ":" +  (s<10 ? "0" + s : s) + ""  ;
    }

    private void callSetAdapter(List<EAlarmDetails> lst) {
        String Method = "[callSetAdapter]";
        Log.i(TAG, Method + "Init...");
//        if (adapterAlarm != null){
//            adapterAlarm.updateData(lst);
//        }else{
//            adapterAlarm = new AlarmListReminderTimesCardAdapter(this , lst,  rv_reminder_times);
//            rv_reminder_times.setAdapter(adapterAlarm);
//            rv_reminder_times.setLayoutManager( new LinearLayoutManager(this) );
//        }
        if (adapterAlarm != null) {
            adapterAlarm.updateData(lst);
        } else {
            adapterAlarm = new AlarmListReminderTimesCardAdapter(this, lst, rv_reminder_times);
            rv_reminder_times.setAdapter(adapterAlarm);
            rv_reminder_times.setLayoutManager(new LinearLayoutManager(this));
        }
        Log.i(TAG, Method + "End...");
    }

    private void fn_showAlertDialogNameOfDays() {
        final String textRbt = "Días específicos: ";
        String Method = "[fn_showAlertDialogNameOfDays]";
        Log.i(TAG, Method + "Init...");
        //final CharSequence[] items = {"Lunes","Martes","Miércoles","Jueves","Viernes","Sábado ","Domingo"};
        final CharSequence[] items = getResources().getStringArray(R.array.array_WeekDaysDesc);//{"Lunes","Martes","Miércoles","Jueves","Viernes","Sábado ","Domingo"};
        boolean[] itemsCheck = {false, false, true, true, false, true, false};
        // arraylist to keep the selected items
        final ArrayList seletedItems = new ArrayList();
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Seleccione los días de la semana")
                .setMultiChoiceItems(items, null, new DialogInterface.OnMultiChoiceClickListener() {
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
                }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //  Your code when user clicked on OK
                        //  You can write the code  to save the selected item here
                        String Days = "ALL";
                        Days = getDaySelected(seletedItems);
                        if (Days.equals("ALL")) {
                            rbtEveryDay.setChecked(true);
                            rbtSpecificDaysOfWeek.setText(textRbt);
                        } else {
                            rbtSpecificDaysOfWeek.setText(textRbt + Days);

                        }

                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //  Your code when user clicked on Cancel
                    }
                }).create();
        dialog.show();
        Log.i(TAG, Method + "End...");
    }

    private void setWeekDays() {
        String Method = "[setWeekDays]";
        Log.i(TAG, Method + " Init...");

        final String[] items = getResources().getStringArray(R.array.array_WeekDaysDesc);//{"Lunes","Martes","Miércoles","Jueves","Viernes","Sábado ","Domingo"};
        WeekDays wk;
        lstWeekDays = new ArrayList<>();
        for (int i = 0; i < items.length; i++) {
            wk = new WeekDays((i + 1), items[i]);
            Log.i(TAG, "wk.toString(): " + wk.toString());
            this.lstWeekDays.add(wk);
        }
        Log.i(TAG, Method + " End...");
    }

    private String getDaySelected(ArrayList items) {
        String Method = "[getDaySelected]";
        Log.i(TAG, Method + "Init...");
        String weekDays = "";
        lstWeekDaysSelected.clear();
        if (items.size() == 7) {
            WeekDays wk = new WeekDays(0, "ALL");
            lstWeekDaysSelected.add(wk);
            weekDays = "ALL,";
        } else {
            Log.i(TAG, Method + "lstWeekDays.size(): " + lstWeekDays.size());
            Log.i(TAG, Method + "lstWeekDays.toString(): " + lstWeekDays.toString());
            Log.i(TAG, Method + "items.size(): " + items.size());
            for (int i = 0; i < items.size(); i++) {
                Log.i(TAG, Method + "items.get(i).toString(): " + items.get(i).toString());
                Log.i(TAG, Method + "Integer.parseInt(items.get(i).toString()) + 1: " + (Integer.parseInt(items.get(i).toString()) + 1));
                for (WeekDays wk : lstWeekDays) {
                    if (Integer.parseInt(items.get(i).toString()) + 1 == wk.getNumberDay()) {
                        lstWeekDaysSelected.add(wk);
                        weekDays = weekDays + wk.getNameDay() + ",";
                    }
                }
            }
        }
        Log.i(TAG, Method + "lstWeekDaysSelected.size(): " + lstWeekDaysSelected.size());
        Log.i(TAG, Method + "weekDays: " + weekDays);
        weekDays = weekDays.substring(0, weekDays.length() - 1);
        Log.i(TAG, Method + "weekDays: " + weekDays);
        Log.i(TAG, Method + "End...");
        return weekDays;
    }

    private String getSelectedDayValue() {
        String Method = "[getSelectedDayValue]";
        Log.i(TAG, Method + "Init...");
        Log.i(TAG, Method + "lstWeekDaysSelected.size():" + lstWeekDaysSelected.size());
        String weekDays = "";
        String key = "WeekDays";
        if (lstWeekDaysSelected.size() == 7 || lstWeekDaysSelected.size() == 0) {
            weekDays = "0";
        } else {
            for (WeekDays wk : lstWeekDays) {
                for (int i = 0; i < lstWeekDaysSelected.size(); i++) {
                    Log.i(TAG, Method + "lstWeekDaysSelected.get(" + i + ").getNumberDay() = " + lstWeekDaysSelected.get(i).getNumberDay() + " | wk.getNumberDay()=" + wk.getNumberDay());
                    if (lstWeekDaysSelected.get(i).getNumberDay() == wk.getNumberDay()) {
                        weekDays += wk.getNumberDay() + "";
                        Log.i(TAG, Method + "weekDays=" + weekDays);
                    }
                }
            }
        }
        Log.i(TAG, Method + "getSelectedDayValue()=" + weekDays);
        Log.i(TAG, Method + "End...");
        return weekDays;
    }

    private void fn_showAlertDialogNumberOfDays() {
        String Method = "[fn_showAlertDialogNumberOfDays]";
        Log.i(TAG, Method + "Init...");

        final String textRbt = "Número de días: ";
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.custom_dialog_edit_texbox, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);
        alertDialogBuilder.setView(promptsView);
        final EditText userInput = (EditText) promptsView
                .findViewById(R.id.editTextDialogUserInput);

        long daysDiff = 0;
        try {
            fechaI = new Date(2017, 07, 2);
            fechaF = new Date(2017, 07, 31);
            daysDiff = getDateDiff(fechaI, fechaF, TimeUnit.DAYS);
        } catch (Exception e) {
            daysDiff = 0;
            e.printStackTrace();
        }

        userInput.setText(String.valueOf(daysDiff));

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // get user input and set it to result
                                // edit text
                                //result.setText(userInput.getText());
                                //txt_NumberOfDays.setText(userInput.getText());
                                rbtNumberOfDays.setText(textRbt + userInput.getText());
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //txt_NumberOfDays.setText("");
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();
        Log.i(TAG, Method + "End...");
    }

    /**
     * Get a diff between two dates
     *
     * @param date1    the oldest date
     * @param date2    the newest date
     * @param timeUnit the unit in which you want the diff
     * @return the diff value, in the provided unit
     */
    private long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        String Method = "[getDateDiff]";
        Log.i(TAG, Method + "Init...");
        long diffInMillies = date2.getTime() - date1.getTime();
        Log.i(TAG, Method + "getDateDiff=" + timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS));
        Log.i(TAG, Method + "End...");
        return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }

    private Date getDateAdd(Date date1, int days, TimeUnit timeUnit) {
        String Method = "[getDateAdd]";
        Log.i(TAG, Method + "Init...");
        long addInMillies = date1.getTime() + timeUnit.DAYS.toMillis(days);
        Log.i(TAG, Method + "getDateAdd=" + new Date(addInMillies));
        Log.i(TAG, Method + "End...");
        return new Date(addInMillies);
    }

    private Map<TimeUnit, Long> computeDiff(Date date1, Date date2) {
        String Method = "[computeDiff]";
        Log.i(TAG, Method + "Init...");
        long diffInMillies = date2.getTime() - date1.getTime();
        List<TimeUnit> units = new ArrayList<TimeUnit>(EnumSet.allOf(TimeUnit.class));
        Collections.reverse(units);
        Map<TimeUnit, Long> result = new LinkedHashMap<TimeUnit, Long>();
        long milliesRest = diffInMillies;
        for (TimeUnit unit : units) {
            long diff = unit.convert(milliesRest, TimeUnit.MILLISECONDS);
            long diffInMilliesForUnit = unit.toMillis(diff);
            milliesRest = milliesRest - diffInMilliesForUnit;
            result.put(unit, diff);
        }
        Log.i(TAG, Method + "End...");
        return result;
    }

    class WeekDays {
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

        public WeekDays(int numberDay, String nameDay) {
            this.numberDay = numberDay;
            this.nameDay = nameDay;
        }

        @Override
        public String toString() {
            return "[" +
                    " numberDay=" + this.getNumberDay() +
                    ",nameDay=" + this.getNameDay() +
                    " ]";
        }
    }

    class ValueDesc {
        private Object value;
        private Object description;

        public Object getValue() {
            return value;
        }

        public ValueDesc() {

        }

        public ValueDesc(Object value, Object description) {
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
        public String toString() {
            return "[" +
                    "value=" + this.getValue() +
                    ",description=" + this.getDescription() +
                    "]";

        }

    }

    private void llenarMedicineType() {
        String Method = "[llenarMedicineType]";
        Log.i(TAG, Method + "Init...");
        try {
            Utils.saveMedicineTypeToDataBaseLocal(HealthMonitorApplicattion.getApplication().getEMedicineTypeDao(), 1, "DIABETES");
            Utils.saveMedicineTypeToDataBaseLocal(HealthMonitorApplicattion.getApplication().getEMedicineTypeDao(), 2, "ASMA");
            Utils.saveMedicineTypeToDataBaseLocal(HealthMonitorApplicattion.getApplication().getEMedicineTypeDao(), 3, "GENERAL");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Log.i(TAG, Method + "End...");
    }

    private void llenarReminderType() {
        String Method = "[llenarReminderType]";
        Log.i(TAG, Method + "Init...");
        try {
            Utils.saveAlarmReminderTypeToDataBaseLocal(HealthMonitorApplicattion.getApplication().getEAlarmReminderTypeDao(), "F", "Frecuencia");
            Utils.saveAlarmReminderTypeToDataBaseLocal(HealthMonitorApplicattion.getApplication().getEAlarmReminderTypeDao(), "I", "Intervalos");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Log.i(TAG, Method + "End...");
    }

    private void llenarReminderTime() {
        String Method = "[llenarReminderTime]";
        Log.i(TAG, Method + "Init...");
        try {
            Utils.saveAlarmReminderTimeToDataBaseLocal(HealthMonitorApplicattion.getApplication().getEAlarmReminderTimeDao(), "F", 1, "Una vez al día");
            Utils.saveAlarmReminderTimeToDataBaseLocal(HealthMonitorApplicattion.getApplication().getEAlarmReminderTimeDao(), "F", 2, "Dos veces al día");
            Utils.saveAlarmReminderTimeToDataBaseLocal(HealthMonitorApplicattion.getApplication().getEAlarmReminderTimeDao(), "F", 3, "3 veces al día");
            Utils.saveAlarmReminderTimeToDataBaseLocal(HealthMonitorApplicattion.getApplication().getEAlarmReminderTimeDao(), "F", 4, "4 veces al día");
            Utils.saveAlarmReminderTimeToDataBaseLocal(HealthMonitorApplicattion.getApplication().getEAlarmReminderTimeDao(), "F", 5, "5 veces al día");
            Utils.saveAlarmReminderTimeToDataBaseLocal(HealthMonitorApplicattion.getApplication().getEAlarmReminderTimeDao(), "F", 6, "6 veces al día");
            Utils.saveAlarmReminderTimeToDataBaseLocal(HealthMonitorApplicattion.getApplication().getEAlarmReminderTimeDao(), "F", 7, "7 veces al día");
            Utils.saveAlarmReminderTimeToDataBaseLocal(HealthMonitorApplicattion.getApplication().getEAlarmReminderTimeDao(), "F", 8, "8 veces al día");
            Utils.saveAlarmReminderTimeToDataBaseLocal(HealthMonitorApplicattion.getApplication().getEAlarmReminderTimeDao(), "F", 9, "9 veces al día");
            Utils.saveAlarmReminderTimeToDataBaseLocal(HealthMonitorApplicattion.getApplication().getEAlarmReminderTimeDao(), "F", 10, "10 veces al día");
            Utils.saveAlarmReminderTimeToDataBaseLocal(HealthMonitorApplicattion.getApplication().getEAlarmReminderTimeDao(), "F", 11, "11 veces al día");
            Utils.saveAlarmReminderTimeToDataBaseLocal(HealthMonitorApplicattion.getApplication().getEAlarmReminderTimeDao(), "F", 12, "12 veces al día");

            Utils.saveAlarmReminderTimeToDataBaseLocal(HealthMonitorApplicattion.getApplication().getEAlarmReminderTimeDao(), "I", 1, "Cada 12 horas");
            Utils.saveAlarmReminderTimeToDataBaseLocal(HealthMonitorApplicattion.getApplication().getEAlarmReminderTimeDao(), "I", 2, "Cada 8 horas");
            Utils.saveAlarmReminderTimeToDataBaseLocal(HealthMonitorApplicattion.getApplication().getEAlarmReminderTimeDao(), "I", 3, "Cada 6 horas");
            Utils.saveAlarmReminderTimeToDataBaseLocal(HealthMonitorApplicattion.getApplication().getEAlarmReminderTimeDao(), "I", 4, "Cada 4 horas");
            Utils.saveAlarmReminderTimeToDataBaseLocal(HealthMonitorApplicattion.getApplication().getEAlarmReminderTimeDao(), "I", 5, "Cada 3 horas");
            Utils.saveAlarmReminderTimeToDataBaseLocal(HealthMonitorApplicattion.getApplication().getEAlarmReminderTimeDao(), "I", 6, "Cada 2 horas");
            Utils.saveAlarmReminderTimeToDataBaseLocal(HealthMonitorApplicattion.getApplication().getEAlarmReminderTimeDao(), "I", 7, "Cada hora");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Log.i(TAG, Method + "End...");
    }

    private void llenarMedicinaDummy(int numReg) {
        String Method = "[llenarMedicinaDummy]";
//        EMedicine emed ;
//        int min = 1;
//        int max = 3;
//        new Random();
//        //lstMedicine.add(emed);
//        for(int i=1; i <= numReg  ; i++){
//            try {
//                emed =  new EMedicine();
//                emed.setId(i);
//                emed.setIdMedicamento(i);
//                emed.setEstado("A");
//
//                emed.setDescripcion("DESCR_" + i);
//                emed.setIndicaciones("INDICA_" + i);
//                emed.setLaboratorio("LAB_" + i);
//                emed.setNombre("NOMBRE_" + i);
//                emed.setPresentacion("PRESE_" + i);
//                emed.setVia("VIA");
//                emed.setMedicineTypeCode( ( new Random().nextInt(max - min + 1) + min  ) );
//
//                Log.i(TAG, Method + "EMedicine = "+ emed.toString() );
//
//                Utils.saveMediceToDataBaseLocal(
//                        emed.getIdMedicamento()
//                        ,emed.getNombre()
//                        ,emed.getDescripcion()
//                        ,emed.getPrincipioActivo()
//                        ,emed.getIndicaciones()
//                        ,emed.getRecomendaciones()
//                        ,emed.getVia()
//                        ,emed.getPresentacion()
//                        ,emed.getEstado()
//                        ,emed.getLaboratorio()
//                        ,emed.getMedicineTypeCode()
//                        ,HealthMonitorApplicattion.getApplication().getMedicineDao()
//
//                );
//            }catch (SQLDataException e){
//
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//
//        }
        try {
            Utils.saveMediceToDataBaseLocal(1, "Magaldrato con simeticona", "300mg", "", "ANTIACIDOS", "", "ORAL", "Líquido oral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(2, "Ranitidina", "150 mg", "", "DROGAS PARA EL TRATAMIENTO DE ULCERAS PEPTICAS", "", "ORAL", "Sólido oral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(3, "Omeprazol", "20 mg", "", "DROGAS PARA EL TRATAMIENTO DE LA ÚLCERA PÉPTICA", "", "ORAL", "Sólido oral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(4, "Omeprazol", "10 mg/5ml", "", "DROGAS PARA EL TRATAMIENTO DE LA ÚLCERA PÉPTICA", "", "ORAL", "Sólido oral (polvo)", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(5, "Omeprazol", "40 mg", "", "DROGAS PARA EL TRATAMIENTO DE LA ÚLCERA PÉPTICA", "", "INYECTTABLE", "Sólido parenteral", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(6, "Atropina", "1 mg/ml", "", "BELLADONA Y DERIVADOS, MONODROGAS", "", "INYECTABLE", "Líquido parenteral", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(7, "Butilescopolamina (N-butilbromuro de hioscina)", "20 mg/ml", "", "BELLADONA Y DERIVADOS, MONODROGAS", "", "INYECTABLE", "Líquido parenteral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(8, "Metoclopramida", "10 mg", "", "PROPULSIVOS", "", "ORAL", "Sólido oral", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(9, "Metoclopramida", "5 mg/ml", "", "PROPULSIVOS", "", "INYECTABLE", "Líquido parenteral", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(10, "Ondansetrón", "4 mg ", "", "ANTIEMÉTICOS Y ANTINAUSEOSOS", "", "ORAL", "Sólido oral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(11, "Ondansetrón", "8 mg", "", "ANTIEMÉTICOS Y ANTINAUSEOSOS", "", "ORAL", "Sólido oral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(12, "Ondansetrón", "2 mg/ml", "", "ANTIEMÉTICOS Y ANTINAUSEOSOS", "", "INECTABLE", "Líquido parentera", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(13, "Lactulosa", "65 %", "", "LAXANTES", "", "ORAL", "Solución oral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(14, "Carbohidratos (Dextrosa en agua)", "", "", "LAXANTES", "", "ORAL", "Sólido oral (granulado)", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(15, "Glicerol", "0.92 g ", "", "LAXANTES", "", "RECTAL", "Sólido rectal", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(16, "Glicerol", "3 g", "", "LAXANTES", "", "RECTAL", "Sólido rectal", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(17, "Nistatina", "100.000 UI/ml", "", "ANTIINFECCIOSOS INTESTINALES", "", "ORAL", "Líquido oral", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(18, "Ondansetrón", "4 mg ", "", "ANTIEMÉTICOS Y ANTINAUSEOSOS", "", "ORAL", "Sólido oral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(19, "Ondansetrón", "8 mg", "", "ANTIEMÉTICOS Y ANTINAUSEOSOS", "", "ORAL", "Sólido oral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(20, "Ondansetrón", "2 mg/ml", "", "ANTIEMÉTICOS Y ANTINAUSEOSOS", "", "INYECTABLE", "Líquido parenteral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(21, "Ranitidina", "25 mg/ml", "", "DROGAS PARA EL TRATAMIENTO DE LA ÚLCERA PÉPTICA", "", "ORAL", "Líquido parenteral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(22, "Loperamida", "2 mg", "", "ANTIPROPULSIVOS", "", "ORAL", "Sólido oral", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(23, "Multivitaminas con minerales*: ", "500 mg", "", "AGENTES ANTIINFLAMA", "", "ORAL", "Sólido oral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(24, "Insulina humana (acción rápida)", "100 UI/ml", "", "INSULINA Y ANÁLOGOS", "", "INYECTABLE", "Líquido parenteral", "I", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(25, "Insulina humana NPH (acción intermedia)", "100 UI/ml", "", "INSULINA Y ANÁLOGOS", "", "INYECTABLE", "Líquido parenteral", "I", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(26, "Glibenclamida", "5 mg", "", "DROGAS HIPOGLUCEMIANTES, EXC", "", "ORAL", "Sólido oral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(27, "Metformina + glibenclamida", "(250 mg - 500 mg)+(1.25 mg - 5 mg)", "", "DROGAS HIPOGLUCEMIANTES, EXC", "", "ORAL", "Sólido oral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(28, "Metformina", "500 mg ", "", "DROGAS HIPOGLUCEMIANTES, EXC", "", "ORAL", "Sólido oral", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(29, "Metformina", "1000 mg", "", "DROGAS HIPOGLUCEMIANTES, EXC", "", "ORAL", "Sólido oral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(30, "Sales de rehidratación oral: Glucosa", "13.5 g/L - 20 g/L", "", "ELECTROLITOS CON CARBOHIDRATOS", "", "ORAL", "Sólido oral (plovo)", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(31, "Sales de rehidratación ora: Cloruro de sodio", "2.6 g/L - 3.5 g/L", "", "ELECTROLITOS CON CARBOHIDRATOS", "", "ORAL", "Sólido oral (plovo)", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(32, "Sales de rehidratación ora: Cloruro de potasio", "1.5 g/L", "", "ELECTROLITOS CON CARBOHIDRATOS", "", "ORAL", "Sólido oral (plovo)", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(33, "Sales de rehidratación ora: Citrato trisódico dihi", "2.9 g/L", "", "ELECTROLITOS CON CARBOHIDRATOS", "", "ORAL", "Sólido oral (plovo)", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(34, "Multivitaminas con minerales*: Tiamina (Vitamina B", "0.5 - 1.2 mg/5 ml", "", "MULTIVITAMÍNICOS, COMBINACIONES", "", "ORAL", "Líquido oral", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(35, "Multivitaminas con minerales*: Nicotinamida (Vitam", "6 - 16 mg/5 ml", "", "MULTIVITAMÍNICOS, COMBINACIONES", "", "ORAL", "Líquido oral", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(36, "Multivitaminas con minerales*: Piridoxina (Vitamin", "0.5 - 1 mg/5 ml", "", "MULTIVITAMÍNICOS, COMBINACIONES", "", "ORAL", "Líquido oral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(37, "Multivitaminas con minerales*: Cianicobalamina (Vi", "1 - 2 mcg/5 ml", "", "MULTIVITAMÍNICOS, COMBINACIONES", "", "ORAL", "Líquido oral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(38, "Multivitaminas con minerales*: Ácido ascórbico (Vi", "15 - 50 mg/5 ml", "", "MULTIVITAMÍNICOS, COMBINACIONES", "", "ORAL", "Líquido oral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(39, "Multivitaminas con minerales*:Zinc", "3 - 8 mg/5 ml", "", "MULTIVITAMÍNICOS, COMBINACIONES", "", "ORAL", "Líquido oral", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(40, "Retinol (Vitamina A)", "50.000 UI", "", "VITAMINA A Y D, INCL, COMBINACIONES DE LAS DOS", "", "ORAL", "Sólido oral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(41, "Tiamina (Vitamina B1)", "50 mg", "", "VITAMINA B1, SOLA Y EN COMBINACIÓN CON VITAMINAS B", "", "ORAL", "Sólido oral", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(42, "Tiamina (Vitamina B1)", "50 mg/ml", "", "VITAMINA B1, SOLA Y EN COMBINACIÓN CON VITAMINAS B", "", "INYECTABLE", "Liquido parenteral", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(43, "Complejo B:Tiamina B1", "4 - 6 mg", "", "VITAMINA B1, SOLA Y EN COMBINACIÓN CON VITAMINAS B", "", "ORAL", "Sólido oral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(44, "Complejo B:Piridoxina B6", "1 - 5 mg", "", "VITAMINA B1, SOLA Y EN COMBINACIÓN CON VITAMINAS B", "", "ORAL", "Sólido oral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(45, "Complejo B:Cianocobalamina B12", "1 - 5 mcg", "", "VITAMINA B1, SOLA Y EN COMBINACIÓN CON VITAMINAS B", "", "ORAL", "Sólido oral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(46, "Complejo B:Tiamina B1", "100 mg", "", "VITAMINA B1, SOLA Y EN COMBINACIÓN CON VITAMINAS B", "", "INYECTABLE", "Líquido parenteral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(47, "Complejo B:Piridoxina B6", "100 mg", "", "VITAMINA B1, SOLA Y EN COMBINACIÓN CON VITAMINAS B", "", "INYECTABLE", "Líquido parenteral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(48, "Complejo B:Cianocobalamina B12", "1 mg", "", "VITAMINA B1, SOLA Y EN COMBINACIÓN CON VITAMINAS B", "", "INYECTABLE", "Líquido parenteral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(49, "Complejo B:Tiamina B1", "200 mg", "", "VITAMINA B1, SOLA Y EN COMBINACIÓN CON VITAMINAS B", "", "INYECTABLE", "Polvo parenteral", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(50, "Complejo B:Piridoxina B6", "50 mg", "", "VITAMINA B1, SOLA Y EN COMBINACIÓN CON VITAMINAS B", "", "INYECTABLE", "Polvo parenteral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(51, "Complejo B:Cianocobalamina B12", "0.030 mg", "", "VITAMINA B1, SOLA Y EN COMBINACIÓN CON VITAMINAS B", "", "INYECTABLE", "Polvo parenteral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(52, "Ácido ascórbico (Vitamina C)", "100 mg/ml ", "", "ÁCIDO ASCÓRBICO (VITAMINA C), INCL. COMBINACIONES", "", "INYECTABLE", "Líquido parenteral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(53, "Piridoxina (Vitamina B6)", "100 mg", "", "OTROS PREPARADOS DE VITAMINAS, MONODROGAS", "", "ORAL", "Sólido oral", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(54, "Piridoxina (Vitamina B6)", "150 mg/ml", "", "OTROS PREPARADOS DE VITAMINAS, MONODROGAS", "", "INYECTABLE", "Líquido parenteral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(55, "Combinaciones de vitaminas", "", "", "OTROS PRODUCTOS CON VIT", "", "INYECTABLE", "Sólido parenteral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(56, "Calcio gluconato", "10 %", "", "OTROS PRODUCTOS CON VITAMINAS, COMBINACIONES", "", "INYECTABLE", "Líquido parenteral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(57, "Sulfato de zinc", "2mg/ml  ", "", "OTROS SUPLEMENTOS MINERALES", "", "ORAL", "Líquido oral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(58, "Sulfato de zinc", "5mg/ml", "", "OTROS SUPLEMENTOS MINERALES", "", "ORAL", "Líquido oral", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(59, "Clopidogrel", "75 mg ", "", "AGENTES ANTITROMBÓTICOS", "", "ORAL", "Sólido oral", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(60, "Clopidogrel", "300 mg", "", "AGENTES ANTITROMBÓTICOS", "", "ORAL", "Sólido oral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(61, "Ácido acetil salicílico", "100 mg", "", "AGENTES ANTITROMBÓTICOS", "", "ORAL", "Sólido oral", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(62, "Tirofibán", "0.25 mg/ml", "", "AGENTES ANTITROMBÓTICOS", "", "INYECTABLE", "Líquido parenteral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(63, "Estreptoquinasa", "1´500.000 UI", "", "AGENTES ANTITROMBÓTICOS", "", "INYECTABLE", "Sólido parenteral", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(64, "Ácido tranexámico", "100 mg/ml", "", "ANTIFIBRINOLÍTICOS", "", "INYECTABLE", "Líquido parenteral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(65, "Ácido tranexámico", "250 mg ", "", "ANTIFIBRINOLÍTICOS", "", "ORAL", "Sólido oral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(66, "Ácido tranexámico", "500 mg", "", "ANTIFIBRINOLÍTICOS", "", "ORAL", "Sólido oral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(67, "Warfarina", "5 mg", "", "AGENTES ANTITROMBÓTICOS", "", "ORAL", "Sólido oral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(68, "Enoxaparina", "2000 UI ", "", "AGENTES ANTITROMBÓTICOS", "", "INYECTABLE", "Líquido parenteral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(69, "Enoxaparina", "10.000 UI", "", "AGENTES ANTITROMBÓTICOS", "", "INYECTABLE", "Líquido parenteral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(70, "Enoxaparina", "20 mg ", "", "AGENTES ANTITROMBÓTICOS", "", "INYECTABLE", "Líquido parenteral", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(71, "Enoxaparina", "100 mg", "", "AGENTES ANTITROMBÓTICOS", "", "INYECTABLE", "Líquido parenteral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(72, "Heparina ( no fraccionada )", "5000 UI/ml", "", "AGENTES ANTITROMBÓTICOS", "", "INYECTABLE", "Líquido parenteral", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(73, "Clopidogrel", "75 mg ", "", "AGENTES ANTITROMBÓTICOS", "", "ORAL", "Sólido oral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(74, "Clopidogrel", "300 mg", "", "AGENTES ANTITROMBÓTICOS", "", "ORAL", "Sólido oral", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(75, "Ácido acetil salicílico", "100 mg", "", "AGENTES ANTITROMBÓTICOS", "", "ORAL", "Sólido oral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(76, "Tirofibán", "0.25 mg/ml", "", "AGENTES ANTITROMBÓTICOS", "", "INYECTABLE", "Líquido parenteral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(77, "Estreptoquinasa", "1´500.000 UI", "", "AGENTES ANTITROMBÓTICOS", "", "INYECTABLE", "Sólido parenteral", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(78, "Ácido tranexámico", "100 mg/ml", "", "ANTIFIBRINOLÍTICOS", "", "INYECTABLE", "Líquido parenteral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(79, "Ácido tranexámico", "250 mg", "", "ANTIFIBRINOLÍTICOS", "", "INYECTABLE", "Sólido oral", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(80, "Ácido tranexámico", "500 mg", "", "ANTIFIBRINOLÍTICOS", "", "INYECTABLE", "Sólido oral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(81, "Fitomenadiona", "10 mg/ml", "", "VITAMINA K Y OTROS HEMOSTÁTICOS", "", "INYECTABLE", "Líquido parenteral", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(82, "Factor VIII", "250 UI", "", "VITAMINA K Y OTROS HEMOSTÁTICOS", "", "INYECTABLE", "Sólido parenteral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(83, "Factor VIII", "1500 UI", "", "VITAMINA K Y OTROS HEMOSTÁTICOS", "", "INYECTABLE", "Sólido parenteral", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(84, "Factor IX", "600 UI ", "", "VITAMINA K Y OTROS HEMOSTÁTICOS", "", "INYECTABLE", "Sólido parenteral", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(85, "Factor IX", "1200 UI", "", "VITAMINA K Y OTROS HEMOSTÁTICOS", "", "INYECTABLE", "Sólido parenteral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(86, "Ferroso sulfato", "50mg ", "", "PREPARADOS CON HIERRO", "", "ORAL", "Sólido oral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(87, "Ferroso sulfato", "100 mg", "", "PREPARADOS CON HIERRO", "", "ORAL", "Sólido oral", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(88, "Ferroso sulfato", "25 mg/ml", "", "PREPARADOS CON HIERRO", "", "ORAL", "Líquido oral", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(89, "Ferroso sulfato", "25 - 50 mg/5ml", "", "PREPARADOS CON HIERRO", "", "ORAL", "Líquido oral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(90, "Hierro sacaratado, oxido de", "100 mg", "", "PREPARADOS CON HIERRO", "", "INYECTABLE", "Líquido parenteral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(91, "Multivitaminas y oligoelementos: Hierro", "12,5 mg", "", "PREPARADOS CON HIERRO", "", "ORAL", "Sólido oral (Polvo)", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(92, "Multivitaminas y oligoelementos:Zinc ", "5 mg", "", "PREPARADOS CON HIERRO", "", "ORAL", "Sólido oral (Polvo)", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(93, "Multivitaminas y oligoelementos:Vitamina A", "300 mcg", "", "PREPARADOS CON HIERRO", "", "ORAL", "Sólido oral (Polvo)", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(94, "Multivitaminas y oligoelementos:Ácido fólico", "160 mcg", "", "PREPARADOS CON HIERRO", "", "ORAL", "Sólido oral (Polvo)", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(95, "Multivitaminas y oligoelementos:Ácido ascórbico", "30 mg", "", "PREPARADOS CON HIERRO", "", "ORAL", "Sólido oral (Polvo)", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(96, "Ácido fólico", "1 mg", "", "VITAMINA B12 Y ÁCIDO FÓLICO", "", "ORAL", "Sólido oral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(97, "Eritropoyetina", "2000 UI ", "", "OTROS PREPARADOS ANTIANÉMICOS", "", "INYECTABLE", "Liquido/ o solido parenteral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(98, "Aminoácidos", "10000 UI", "", "OTROS PREPARADOS ANTIANÉMICOS", "", "INYECTABLE", "Liquido/ o solido parenteral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(99, "Agentes gelatinas", "3.5 % ", "", "SANGRE Y PRODUCTOS RELACION", "", "INYECTABLE", "Líquido parenteral", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(100, "Agentes gelatinas", "5.5 %", "", "SANGRE Y PRODUCTOS RELACION", "", "INYECTABLE", "Líquido parenteral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(101, "Aminoácidos", "5 %", "", "SOLUCIONES I.V.", "", "INYECTABLE", "Líquido parenteral", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(102, "Aminoácidos", "15 %", "", "SOLUCIONES I.V.", "", "INYECTABLE", "Líquido parenteral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(103, "Emulsiones grasas (lípidos)", "10 %", "", "SOLUCIONES I.V.", "", "INYECTABLE", "Líquido parenteral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(104, "Emulsiones grasas (lípidos)", "20 %", "", "SOLUCIONES I.V.", "", "INYECTABLE", "Líquido parenteral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(105, "Carbohidratos (Dextrosa en agua)", "10 % ", "", "SOLUCIONES I.V.", "", "INYECTABLE", "Líquido parenteral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(106, "Carbohidratos (Dextrosa en agua)", "50 %", "", "SOLUCIONES I.V.", "", "INYECTABLE", "Líquido parenteral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(107, "Carbohidratos (Dextrosa en agua)", "5 %", "", "SOLUCIONES I.V.", "", "INYECTABLE", "Líquido parenteral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(108, "Electrolitos con carbohidratos (Dextrosa en soluci", "5 %", "", "SOLUCIONES I.V.", "", "INYECTABLE", "Líquido parenteral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(109, "Electrolitos con carbohidratos (Dextrosa en soluci", "0.9 %", "", "SOLUCIONES I.V.", "", "INYECTABLE", "Líquido parenteral", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(110, "Emulsiones grasas (lípidos)", "10 %", "", "SOLUCIONES I.V.", "", "INYECTABLE", "Líquido parenteral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(111, "Emulsiones grasas (lípidos)", "20 %", "", "SOLUCIONES I.V.", "", "INYECTABLE", "Líquido parenteral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(112, "Carbohidratos (Dextrosa en agua)", "10 % ", "", "SOLUCIONES I.V.", "", "INYECTABLE", "Líquido parenteral", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(113, "Carbohidratos (Dextrosa en agua)", "50 %", "", "SOLUCIONES I.V.", "", "INYECTABLE", "Líquido parenteral", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(114, "Carbohidratos (Dextrosa en agua)", "5 %", "", "SOLUCIONES I.V.", "", "INYECTABLE", "Líquido parenteral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(115, "Electrolitos con carbohidratos (Dextrosa en soluci", "5 %", "", "SOLUCIONES I.V.", "", "INYECTABLE", "Líquido parenteral", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(116, "Electrolitos con carbohidratos (Dextrosa en soluci", "0.9 %", "", "SOLUCIONES I.V.", "", "INYECTABLE", "Líquido parenteral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(117, "Manitol", "20%", "", "SOLUCIONES I.V.", "", "INYECTABLE", "Líquido parenteral", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(118, "Cloruro de sodio", "0.9 %", "", "SOLUCIONES DE IRRIGACIÓN", "", "INYECTABLE", "Líquido parenteral", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(119, "Combinaciones (Lactato de Ringer)", "", "", "SOLUCIONES DE IRRIGACIÓN", "", "INYECTABLE", "Líquido parenteral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(120, "Cloruro de potasio", "2 mEq/ml (20%)", "", "ADITIVOS PARA SOLUCIONES I.V.", "", "INYECTABLE", "Líquido parenteral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(121, "Bicarbonato de sodio ", "1 mEq/ml (8.4%)", "", "ADITIVOS PARA SOLUCIONES I.V.", "", "INYECTABLE", "Líquido parenteral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(122, "Cloruro de sodio", "3.4 mEq/ml (20%)", "", "ADITIVOS PARA SOLUCIONES I.V.", "", "INYECTABLE", "Líquido parenteral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(123, "Sulfato de magnesio", "20%", "", "ADITIVOS PARA SOLUCIONES I.V.", "", "INYECTABLE", "Líquido parenteral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(124, "Oligoelementos", "", "", "ADITIVOS PARA SOLUCIONES I.V.", "", "INYECTABLE", "Líquido parenteral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(125, "Digoxina", "62.5 mcg ", "", "GLUCÓSIDOS CARDÍACOS", "", "ORAL", "Sólido oral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(126, "Digoxina", "250 mcg", "", "GLUCÓSIDOS CARDÍACOS", "", "ORAL", "Sólido oral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(127, "Digoxina", "50 mcg/ml", "", "GLUCÓSIDOS CARDÍACOS", "", "ORAL", "Solución oral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(128, "Digoxina", "0.25 mg/ml", "", "GLUCÓSIDOS CARDÍACOS", "", "INYECTABLE", "Líquido parenteral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(129, "Amiodarona", "200 mg", "", "ANTIARRÍTMICOS DE CLASE I Y III", "", "ORAL", "Sólido oral", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(130, "Amiodarona", "50 mg/ml", "", "ANTIARRÍTMICOS DE CLASE I Y III", "", "INYECTABLE", "Líquido parenteral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(131, "Norepinefrina", "1 mg/ml", "", "ESTIMULANTES CARDÍACOS EXCL. GLUCÓSIDOS CARDÍACOS", "", "INYECTABLE", "Líquido parenteral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(132, "Dopamina", "40 mg/ml", "", "ESTIMULANTES CARDÍACOS EXCL. GLUCÓSIDOS CARDÍACOS", "", "INYECTABLE", "Líquido parenteral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(133, "Dopamina", "50 mg/ml", "", "ESTIMULANTES CARDÍACOS EXCL. GLUCÓSIDOS CARDÍACOS", "", "INYECTABLE", "Líquido parenteral", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(134, "Dobutamina", "50 mg/ml", "", "ESTIMULANTES CARDÍACOS EXCL. GLUCÓSIDOS CARDÍACOS", "", "INYECTABLE", "Líquido parenteral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(135, "Epinefrina (adrenalina)", "1 mg/ml", "", "ESTIMULANTES CARDÍACOS EXCL. GLUCÓSIDOS CARDÍACOS", "", "INYECTABLE", "Líquido parenteral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(136, "Efedrina", "60 mg/ml", "", "ESTIMULANTES CARDÍACOS EXCL. GLUCÓSIDOS CARDÍACOS", "", "INYECTABLE", "Líquido parenteral", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(137, "Trinitrato de glicerilo (Nitroglicerina) ", "5 mg/ml", "", "VASODILATADORES USADOS EN ENFERMEDADES CARDÍACAS", "", "INYECTABLE", "Líquido parenteral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(138, "Dinitrato de isosorbida", "5 mg", "", "VASODILATADORES USADOS EN ENFERMEDADES CARDÍACAS", "", "INYECTABLE", "Sólido oral sublingual", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(139, "Mononitrato de isosorbida", "20 mg", "", "VASODILATADORES USADOS EN ENFERMEDADES CARDÍACAS", "", "ORAL", "Sólido oral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(140, "Adenosina", "3 mg/ml", "", "OTROS PREPARADOS PARA EL CORAZÓN", "", "INYECTABLE", "Líquido parenteral", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(141, "Metildopa ( levógira )", "250 mg ", "", "AGENTES ANTIADRENÉRGICOS DE ACCIÓN CENTRAL", "", "ORAL", "Sólido oral", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(142, "Metildopa ( levógira )", "500 mg", "", "AGENTES ANTIADRENÉRGICOS DE ACCIÓN CENTRAL", "", "ORAL", "Sólido oral", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(143, "Doxazosina", "2 mg ", "", "AGENTES ANTIADRENÉRGICOS DE ACCIÓN CENTRAL", "", "ORAL", "Sólido oral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(144, "Doxazosina", "4 mg", "", "AGENTES ANTIADRENÉRGICOS DE ACCIÓN CENTRAL", "", "ORAL", "Sólido oral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(145, "Hidralazina", "50 mg", "", "AGENTES QUE ACTÚAN SOBRE EL MÚSCULO LISO ARTERIOLA", "", "ORAL", "Sólido oral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(146, "Hidralazina", "20 mg/ml", "", "AGENTES QUE ACTÚAN SOBRE EL MÚSCULO LISO ARTERIOLA", "", "INYECTABLE", "Líquido parenteral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(147, "Nitroprusiato sódico", "50 mg", "", "AGENTES QUE ACTÚAN SOBRE EL MÚSCULO LISO ARTERIOLA", "", "INYECTABLE", "Sólido parenteral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(148, "Clortalidona", "25 mg ", "", "DIURÉTICOS DE TECHO BAJO, EXCL. TIAZIDAS", "", "ORAL", "Sólido oral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(149, "Clortalidona", "50 mg", "", "DIURÉTICOS DE TECHO BAJO, EXCL. TIAZIDAS}", "", "ORAL", "Sólido oral", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(150, "Furosemida", "40 mg", "", "DIURÉTICOS DE TECHO ALTO", "", "ORAL", "Sólido oral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(151, "Furosemida", "10 mg/ml", "", "DIURÉTICOS DE TECHO ALTO", "", "INYECTABLE", "Líquido parenteral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(152, "Espironolactona", "25 mg ", "", "AGENTES AHORRADORES DE POTASIO", "", "ORAL", "Sólido oral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(153, "Espironolactona", "100 mg", "", "AGENTES AHORRADORES DE POTASIO", "", "ORAL", "Sólido oral", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(154, "Lidocaína", "2 %", "", "AGENTES DE USO TÓPICO PARA EL TRATAMIENTO DE HEMOR", "", "RECTAL", "Semisólido rectal", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(155, "Polidocanol", "3 %", "", "TERAPIA ANTIVARICOSA", "", "INYECTABLE", "Líquido parenteral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(156, "Atenolol", "50 mg ", "", "AGENTES BETABLOQUEANTES", "", "ORAL", "Sólido oral", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(157, "Atenolol", "100 mg", "", "AGENTES BETABLOQUEANTES", "", "ORAL", "Sólido oral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(158, "Carvedilol", "6.25 mg ", "", "AGENTES BETABLOQUEANTES", "", "ORAL", "Sólido oral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(159, "Carvedilol", "25 mg", "", "AGENTES BETABLOQUEANTES", "", "ORAL", "Sólido oral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(160, "Amlodipina", "5 mg ", "", "BLOQUEANTES SELECTIVOS DE CANALES DE CALCIO CON EF", "", "ORAL", "Sólido oral", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(161, "Amlodipina", "10 mg", "", "BLOQUEANTES SELECTIVOS DE CANALES DE CALCIO CON EF", "", "ORAL", "Sólido oral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(162, "Diltiazem", "60 mg", "", "BLOQUEANTES SELECTIVOS DE CANALES DE CALCIO", "", "ORAL", "Sólido oral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(163, "Diltiazem", "90 mg ", "", "BLOQUEANTES SELECTIVOS DE CANALES DE CALCIO", "", "ORAL", "Sólido oral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(164, "Diltiazem", "120 mg", "", "BLOQUEANTES SELECTIVOS DE CANALES DE CALCIO", "", "ORAL", "Sólido oral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(165, "Diltiazem", "25 mg", "", "BLOQUEANTES SELECTIVOS DE CANALES DE CALCIO", "", "INYECTABLE", "Sólido parenteral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(166, "Enalapril", "5mg ", "", "INHIBIDORES DE LA ECA, MONODROGAS", "", "ORAL", "Sólido oral", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(167, "Enalapril", "20 mg", "", "INHIBIDORES DE LA ECA, MONODROGAS", "", "ORAL", "Sólido oral", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(168, "Losartán", "50 mg ", "", "ANTAGONISTAS DE ANGIOTENSINA II, MONODROGAS", "", "ORAL", "Sólido oral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(169, "Losartán", "100 mg", "", "ANTAGONISTAS DE ANGIOTENSINA II, MONODROGAS", "", "ORAL", "Sólido oral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(170, "Simvastatina", "20 mg ", "", "REDUCTORES DEL COLESTEROL Y LOS TRIGLICÉRIDOS", "", "ORAL", "Sólido oral", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(171, "Simvastatina", "40 mg", "", "REDUCTORES DEL COLESTEROL Y LOS TRIGLICÉRIDOS", "", "ORAL", "Sólido oral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(172, "Gemfibrozilo", "600 mg", "", "REDUCTORES DEL COLESTEROL Y LOS TRIGLICÉRIDOS", "", "ORAL", "Sólido oral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(173, "Clotrimazol", "1 %", "", "ANTIFÚNGICOS PARA USO TÓPICO", "", "TOPICO", "Semisólido cutáneo", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(174, "Clotrimazol", "1 %", "", "ANTIFÚNGICOS PARA USO TÓPICO", "", "TOPICO", "Líquido cutáneo", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(175, "Terbinafina", "1 %", "", "ANTIFÚNGICOS PARA USO TÓPICO", "", "TOPICO", "Semisólido cutáneo", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(176, "Griseofulvina", "125 mg ", "", "ANTIFÚNGICOS PARA USO SISTÉMICO", "", "ORAL", "Sólido oral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(177, "Griseofulvina", "500 mg", "", "ANTIFÚNGICOS PARA USO SISTÉMICO", "", "ORAL", "Sólido oral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(178, "Terbinafina", "250 mg", "", "ANTIFÚNGICOS PARA USO SISTÉMICO", "", "ORAL", "Sólido oral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(179, "Productos con zinc", "", "", "Emolientes y protectores", "", "TOPICO", "Semisólido cutáneo", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(180, "Urea", "5 % ", "", "Emolientes y protectores", "", "TOPICO", "Semisólido cutáneo", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(181, "Urea", "10 %", "", "Emolientes y protectores", "", "TOPICO", "Semisólido cutáneo", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(182, "Lidocaína", "2 % ", "", "ANTIPRURIGINOSOS, INCL. ANTIHISTAMÍNÍCOS, ANESTÉSI", "", "TOPICO", "Semisólido cutáneo", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(183, "Lidocaína", "5 %", "", "ANTIPRURIGINOSOS, INCL. ANTIHISTAMÍNÍCOS, ANESTÉSI", "", "TOPICO", "Semisólido cutáneo", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(184, "Lidocaína", "10 %", "", "ANTIPRURIGINOSOS, INCL. ANTIHISTAMÍNÍCOS, ANESTÉSI", "", "TOPICO", "Líquido cutáneo", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(185, "Alquitrán de hulla", "5 %", "", "ANTIPSORIÁSICOS PARA USO TÓPICO", "", "TOPICO", "Líquido cutáneo", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(186, "Calcipotriol + Betametasona dipropionato", "(50 mcg + 0.5 mg)/g", "", "ANTIPSORIÁSICOS PARA USO TÓPICO", "", "TOPICO", "Semisólido cutáneo", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(187, "Ácido fusídico", "2 %", "", "ANTIBIÓTICOS PARA USO TÓPICO", "", "TOPICO", "Semisólido cutáneo", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(188, "Sulfadiazina de plata", "1 % ", "", "QUIMIOTERÁPICOS PARA USO TÓPICO", "", "TOPICO", "Semisólido cutáneo", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(189, "Hidrocortisona", "0.5 % ", "", "CORTICOSTEROIDES, MONODROGAS", "", "TOPICO", "Semisólido cutáneo/líquido cutáneo", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(190, "Hidrocortisona", "1 %", "", "CORTICOSTEROIDES, MONODROGAS", "", "TOPICO", "Semisólido cutáneo/líquido cutáneo", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(191, "Betametasona", "0.05 % ", "", "CORTICOSTEROIDES, MONODROGAS", "", "TOPICO", "Semisólido cutáneo/líquido cutáneo", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(192, "Betametasona", "0.1 %", "", "CORTICOSTEROIDES, MONODROGAS", "", "TOPICO", "Semisólido cutáneo/líquido cutáneo", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(193, "Adapaleno", "0.1 %", "", "PRODUCTOS ANTI ACNÉ PARA USO TOPICO", "", "TOPICO", "Semisólido cutáneo", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(194, "Peróxido de Benzoílo", "5 %", "", "PRODUCTOS ANTI ACNÉ PARA USO TOPICO", "", "TOPICO", "Semisólido cutáneo o líquido cutáneo", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(195, "Peróxido de Benzoílo", "10 %", "", "PRODUCTOS ANTI ACNÉ PARA USO TOPICO", "", "TOPICO", "Semisólido cutáneo o líquido cutáneo", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(196, "Metronidazol", "500 mg ", "", "ANTIINFECCIOSOS Y ANTISÉPTICOS EXCL. COMBINACIONES", "", "TOPICO", "Sólido vaginal", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(197, "Metronidazol", "1000 mg x", "", "ANTIINFECCIOSOS Y ANTISÉPTICOS EXCL. COMBINACIONES", "", "TOPICO", "Sólido vaginal", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(198, "Clotrimazol", "1 % y 2 %", "", "ANTIINFECCIOSOS Y ANTISÉPTICOS EXCL. COMBINACIONES", "", "TOPICO", "Semisólido vaginal", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(199, "Clotrimazol", "100 mg ", "", "ANTIINFECCIOSOS Y ANTISÉPTICOS EXCL. COMBINACIONES", "", "TOPICO", "Sólido vaginal", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(200, "Clotrimazol", "500 mg", "", "ANTIINFECCIOSOS Y ANTISÉPTICOS EXCL. COMBINACIONES", "", "TOPICO", "Sólido vaginal", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(201, "Metilergometrina ( o G02AB03 Ergometrina )", "0.125 mg", "", "OCITÓCICOS", "", "ORAL", "Sólido oral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(202, "Metilergometrina ( o G02AB03 Ergometrina )", "0.2 mg/ml", "", "OCITÓCICOS", "", "INYECTABLE", "Líquido parenteral", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(203, "Misoprostol", "200 mcg", "", "OCITÓCICOS", "", "ORAL", "Sólido oral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(204, "Nifedipina", "10 mg", "", "OTROS PRODUCTOS GINECOLÓGICOS", "", "ORAL", "Sólido oral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(205, "Estradiol valerato + Noretisterona enantato", "5 mg + 50 mg)/ml", "", "ANTICONCEPTIVOS HORMONALES PARA USO SISTÉMICO", "", "INYECTABLE", "Líquido parenteral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(206, "Levonorgestrel + Etinilestradiol", "150 mcg + 30 mcg", "", "ANTICONCEPTIVOS HORMONALES PARA USO SISTÉMICO", "", "ORAL", "Sólido oral", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(207, "Levonorgestrel", "0.030 mg", "", "ANTICONCEPTIVOS HORMONALES PARA USO SISTÉMICO", "", "ORAL", "Sólido oral", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(208, "Levonorgestrel", "0,75 mg o 1,5 mg", "", "ANTICONCEPTIVOS HORMONALES PARA USO SISTÉMICO", "", "ORAL", "Sólido oral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(209, "Levonorgestrel", "150 mg (2 varillas de 75 mg)", "", "ANTICONCEPTIVOS HORMONALES PARA USO SISTÉMICO", "", "DERMICO", "Implante subdér", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(210, "Testosterona", "250 mg/ml", "", "ANDRÓGENOS", "", "INYECTABLE", "Líquido parenteral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(211, "Estradiol", "1 mg", "", "ESTRÓGENOS", "", "ORAL", "Sólido oral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(212, "Estriol", "1 mg/g (0.1%)", "", "ESTRÓGENOS", "", "VAGINAL", "Semisólido vaginal", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(213, "Progesterona", "100 mg", "", "PROGESTÁGENOS", "", "ORAL", "Sólido oral", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(214, "Clomifeno", "50 mg", "", "GONADOTROFINAS Y OTROS ESTIMULANTES DE LA OVULACIÓ", "", "ORAL", "Sólido oral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(215, "Tamsulosina", "0.4 mg", "", "DROGAS USADAS EN LA HIPERTROFIA PROSTÁTICA BENIGNA", "", "ORAL", "Sólido oral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(216, "Desmopresina", "15 mcg/ml", "", "HORMONAS DEL LÓBULO POSTERIOR DE LA HIPÓFISIS", "", "INYECTABLE", "Líquido parenteral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(217, "Desmopresina", "10 mcg /0.1ml", "", "HORMONAS DEL LÓBULO POSTERIOR DE LA HIPÓFISIS", "", "NASAL", "Líquido para inhalación", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(218, "Oxitocina", "10 UI/ml", "", "HORMONAS DEL LÓBULO POSTERIOR DE LA HIPÓFISIS", "", "INYECTABLE", "Líquido parenteral", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(219, "Betametasona", "4 mg/ml", "", "CORTICOSTEROIDES PARA USO SISTÉMICO, MONODROGAS", "", "INYECTABLE", "Líquido parenteral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(220, "Dexametasona", "4 mg ", "", "CORTICOSTEROIDES PARA USO SISTÉMICO, MONODROGAS", "", "ORAL", "Sólido oral", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(221, "Dexametasona", "8 mg", "", "CORTICOSTEROIDES PARA USO SISTÉMICO, MONODROGAS", "", "ORAL", "Sólido oral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(222, "Dexametasona", "4 mg/ml", "", "CORTICOSTEROIDES PARA USO SISTÉMICO, MONODROGAS", "", "INYECTABLE", "Líquido parenteral", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(223, "Metilprednisolona, acetato", "40 mg/ml ", "", "CORTICOSTEROIDES PARA USO SISTÉMICO, MONODROGAS", "", "INYECTABLE", "Líquido parenteral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(224, "Metilprednisolona, acetato", "80 mg/m", "", "CORTICOSTEROIDES PARA USO SISTÉMICO, MONODROGAS", "", "INYECTABLE", "Líquido parenteral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(225, "Metilprednisolona, succinato", "125 mg ", "", "CORTICOSTEROIDES PARA USO SISTÉMICO, MONODROGASCOR", "", "INYECTABLE", "Sólido parenteral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(226, "Metilprednisolona, succinato", "500 mg", "", "CORTICOSTEROIDES PARA USO SISTÉMICO, MONODROGAS", "", "INYECTABLE", "Sólido parenteral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(227, "Prednisolona", "5 mg ", "", "CORTICOSTEROIDES PARA USO SISTÉMICO, MONODROGASCOR", "", "ORAL", "Sólido oral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(228, "Prednisolona", "20 mg", "", "CORTICOSTEROIDES PARA USO SISTÉMICO, MONODROGASCOR", "", "ORAL", "Sólido oral", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(229, "Prednisona", "5 mg ", "", "CORTICOSTEROIDES PARA USO SISTÉMICO, MONODROGASCOR", "", "ORAL", "Sólido oral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(230, "Prednisona", "20 mg", "", "CORTICOSTEROIDES PARA USO SISTÉMICO, MONODROGASCOR", "", "ORAL", "Sólido oral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(231, "Hidrocortisona, succinato sódico", "100 mg ", "", "CORTICOSTEROIDES PARA USO SISTÉMICO, MONODROGASCOR", "", "INYECTABLE", "Sólido parenteral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(232, "Hidrocortisona, succinato sódico", "500 mg", "", "CORTICOSTEROIDES PARA USO SISTÉMICO, MONODROGASCOR", "", "INYECTABLE", "Sólido parenteral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(233, "Levotiroxina sódica", "0.05 mg - 0.2 mg", "", "PREPARADOS DE HORMONA TIROIDEA", "", "ORAL", "Sólido oral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(234, "Tiamazol (Metimazol)", "5 mg", "", "PREPARADOS ANTITIROIDEOS", "", "ORAL", "Sólido oral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(235, "Doxiciclina", "100 mg", "", "TETRACICLINAS", "", "ORAL", "Sólido oral", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(236, "Ampicilina", "500 mg ", "", "ANTIBACTERIANOS BETALACTÁMICOS, PENICILINAS", "", "INYECTABLE", "Sólido parenteral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(237, "Ampicilina", "1000 mg", "", "ANTIBACTERIANOS BETALACTÁMICOS, PENICILINAS", "", "INYECTABLE", "Sólido parenteral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(238, "Amoxicilina", "500 mg", "", "ANTIBACTERIANOS BETALACTÁMICOS, PENICILINAS", "", "ORAL", "Sólido oral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(239, "Amoxicilina", "100 mg/ml", "", "ANTIBACTERIANOS BETALACTÁMICOS, PENICILINAS", "", "ORAL", "Sólido oral (polvo)", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(240, "Amoxicilina", "250 mg/5ml", "", "ANTIBACTERIANOS BETALACTÁMICOS, PENICILINAS", "", "ORAL", "Sólido oral (polvo)", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(241, "Bencilpenicilina (Penicilina G Cristalina)", "1´000.000 UI - 5´000.000 U", "", "ANTIBACTERIANOS BETALACTÁMICOS, PENICILINAS", "", "INYECTABLE", "Sólido parenteral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(242, "Bencilpenicilina benzatínica (Penicilina G benzatí", "600.000 UI - 2´400.000 U", "", "ANTIBACTERIANOS BETALACTÁMICOS, PENICILINAS", "", "INYECTABLE", "Sólido parenteral", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(243, "Dicloxacilina", "500 mg", "", "ANTIBACTERIANOS BETALACTÁMICOS, PENICILINAS", "", "ORAL", "Sólido oral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(244, "Dicloxacilina", "125mg/5ml ", "", "ANTIBACTERIANOS BETALACTÁMICOS, PENICILINAS", "", "ORAL", "Sólido oral (polvo)", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(245, "Dicloxacilina", "250 mg/5ml", "", "ANTIBACTERIANOS BETALACTÁMICOS, PENICILINAS", "", "ORAL", "Sólido oral (polvo)", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(246, "Oxacilina", "1000 mg", "", "ANTIBACTERIANOS BETALACTÁMICOS, PENICILINAS", "", "INYECTABLE", "Sólido parenteral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(247, "Ampicilina + Sulbactam", "1000 mg + 500 mg", "", "ANTIBACTERIANOS BETALACTÁMICOS, PENICILINAS", "", "INYECTABLE", "Sólido parenteral", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(248, "Amoxicilina + Ácido clavulánico", "500 mg + 125 mg", "", "ANTIBACTERIANOS BETALACTÁMICOS, PENICILINAS", "", "ORAL", "Sólido oral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(249, "Amoxicilina + Ácido clavulánico", "(125 mg + 31.25 mg)/5m", "", "ANTIBACTERIANOS BETALACTÁMICOS, PENICILINAS", "", "ORAL", "Sólido oral (polvo)", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(250, "Amoxicilina + Ácido clavulánico", "(250 mg + 62.5 mg)/5m", "", "ANTIBACTERIANOS BETALACTÁMICOS, PENICILINAS", "", "ORAL", "Sólido oral (polvo)", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(251, "Amoxicilina + Ácido clavulánico", "1000 mg + 200 mg", "", "ANTIBACTERIANOS BETALACTÁMICOS, PENICILINAS", "", "INYECTABLE", "Sólido parenteral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(252, "Cefalexina", "500 mg", "", "OTROS ANTIBACTERIANOS BETALACTÁMICOS", "", "ORAL", "Sólido oral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(253, "Cefalexina", "250 mg/5ml", "", "OTROS ANTIBACTERIANOS BETALACTÁMICOS", "", "ORAL", "Sólido oral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(254, "Cefazolina", "1000 mg", "", "OTROS ANTIBACTERIANOS BETALACTÁMICOS", "", "INYECTABLE", "Sólido parenteral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(255, "Eritromicina", "250 mg ", "", "MACRÓLIDOS, LINCOSAMIDAS Y ESTREPTOGRAMINAS", "", "ORAL", "Sólido oral", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(256, "Eritromicina", "500 mg", "", "MACRÓLIDOS, LINCOSAMIDAS Y ESTREPTOGRAMINAS", "", "ORAL", "Sólido oral", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(257, "Eritromicina", "200 mg/5m", "", "MACRÓLIDOS, LINCOSAMIDAS Y ESTREPTOGRAMINAS", "", "ORAL", "Sólido oral (polvo)", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(258, "Eritromicina", "400 mg/5ml", "", "MACRÓLIDOS, LINCOSAMIDAS Y ESTREPTOGRAMINAS", "", "ORAL", "Sólido oral (polvo)", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(259, "Claritromicina", "500 mg", "", "MACRÓLIDOS, LINCOSAMIDAS Y ESTREPTOGRAMINAS", "", "ORAL", "Sólido oral", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(260, "Claritromicina", "125 mg/5ml", "", "MACRÓLIDOS, LINCOSAMIDAS Y ESTREPTOGRAMINAS", "", "ORAL", "Sólido oral (polvo)", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(261, "Claritromicina", "250mg/5ml", "", "MACRÓLIDOS, LINCOSAMIDAS Y ESTREPTOGRAMINAS", "", "ORAL", "Sólido oral (polvo)", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(262, "Claritromicina", "500 mg", "", "MACRÓLIDOS, LINCOSAMIDAS Y ESTREPTOGRAMINAS", "", "INYECTABLE", "Sólido parenteral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(263, "Azitromicina", "250 mg ", "", "MACRÓLIDOS, LINCOSAMIDAS Y ESTREPTOGRAMINAS", "", "ORAL", "Sólido oral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(264, "Azitromicina", "500 mg", "", "MACRÓLIDOS, LINCOSAMIDAS Y ESTREPTOGRAMINAS", "", "ORAL", "Sólido oral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(265, "Azitromicina", "200 mg/5ml", "", "MACRÓLIDOS, LINCOSAMIDAS Y ESTREPTOGRAMINAS", "", "ORAL", "Sólido oral (polvo)", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(266, "Clindamicina", "300 mg", "", "MACRÓLIDOS, LINCOSAMIDAS Y ESTREPTOGRAMINAS", "", "ORAL", "Sólido oral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(267, "Clindamicina", "150 mg/ml", "", "MACRÓLIDOS, LINCOSAMIDAS Y ESTREPTOGRAMINAS", "", "INYECTABLE", "Líquido parenteral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(268, "Estreptomicina", "1000 mg", "", "AMINOGLUCÓSIDOS ANTIBACTERIANOS", "", "INYECTABLE", "Sólido parenteral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(269, "Gentamicina", "10 mg/ml ", "", "AMINOGLUCÓSIDOS ANTIBACTERIANOS", "", "INYECTABLE", "Líquido parenteral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(270, "Gentamicina", "140 mg/ml", "", "AMINOGLUCÓSIDOS ANTIBACTERIANOS", "", "INYECTABLE", "Líquido parenteral", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(271, "Amikacina", "50 mg/ml ", "", "AMINOGLUCÓSIDOS ANTIBACTERIANOS", "", "INYECTABLE", "Líquido parenteral", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(272, "Amikacina", "250 mg/m", "", "AMINOGLUCÓSIDOS ANTIBACTERIANOS", "", "INYECTABLE", "Líquido parenteral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(273, "Ciprofloxacina", "500 mg", "", "QUINOLONAS ANTIBACTERIANAS", "", "ORAL", "Sólido oral", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(274, "Ciprofloxacina", "2 mg/ml", "", "QUINOLONAS ANTIBACTERIANAS", "", "INYECTABLE", "Líquido parenteral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(275, "Ciprofloxacina", "10 mg/ml", "", "QUINOLONAS ANTIBACTERIANAS", "", "INYECTABLE", "Líquido parenteral", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(276, "Ciprofloxacina", "20 mg/ml", "", "QUINOLONAS ANTIBACTERIANAS", "", "INYECTABLE", "Líquido parenteral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(277, "Levofloxacina", "250 mg ", "", "QUINOLONAS ANTIBACTERIANAS", "", "ORAL", "Sólido oral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(278, "Levofloxacina", "500 mg", "", "QUINOLONAS ANTIBACTERIANAS", "", "INYECTABLE", "Líquido parenteral", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(279, "Levofloxacina", "500 mg/100ml", "", "QUINOLONAS ANTIBACTERIANAS", "", "INYECTABLE", "Líquido parenteral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(280, "Vancomicina", "500 mg", "", "OTROS ANTIBACTERIANOS", "", "INYECTABLE", "Sólido parenteral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(281, "Vancomicina", "1000 mg", "", "OTROS ANTIBACTERIANOS", "", "INYECTABLE", "Sólido parenteral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(282, "Nitrofurantoína", "100 mg", "", "OTROS ANTIBACTERIANOS", "", "ORAL", "Sólido oral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(283, "Nitrofurantoína", "25 mg/5ml", "", "OTROS ANTIBACTERIANOS", "", "ORAL", "Líquido oral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(284, "Metronidazol", "5 mg/ml", "", "OTROS ANTIBACTERIANOS", "", "INYECTABLE", "Líquido parenteral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(285, "Linezolid", "600 mg", "", "OTROS ANTIBACTERIANOS", "", "ORAL", "Sólido oral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(286, "Linezolid", "2 mg/ml", "", "OTROS ANTIBACTERIANOS", "", "INYECTABLE", "Líquido parenteral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(287, "Linezolid", "40 mg/ml", "", "OTROS ANTIBACTERIANOS", "", "INYECTABLE", "Líquido parenteral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(288, "Amfotericina B", "50 mg", "", "ANTIMICÓTICOS PARA USO SISTÉMICO", "", "INYECTABLE", "Sólido parenteral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(289, "Fluconazol", "150 mg", "", "ANTIMICÓTICOS PARA USO SISTÉMICO", "", "ORAL", "Sólido oral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(290, "Fluconazol", "2 mg/ml", "", "ANTIMICÓTICOS PARA USO SISTÉMICO", "", "INYECTABLE", "Líquido parenteral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(291, "Itraconazol", "100 mg", "", "ANTIMICÓTICOS PARA USO SISTÉMICO", "", "ORAL", "Sólido oral", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(292, "Itraconazol", "10mg/ml", "", "ANTIMICÓTICOS PARA USO SISTÉMICO", "", "ORAL", "Líquido oral", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(293, "Voriconazol", "200 mg", "", "ANTIMICÓTICOS PARA USO SISTÉMICO", "", "INYECTABLE", "Sólido parenteral", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(294, "Ácido aminosalicílico (Ácido paraaminosalicílico)", "800 mg", "", "DROGAS PARA EL TRATAMIENTO DE LA TUBERCULOSIS", "", "ORAL", "Sólido oral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(295, "Cicloserina", "250 mg", "", "DROGAS PARA EL TRATAMIENTO DE LA TUBERCULOSIS", "", "ORAL", "Sólido oral", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(296, "Rifampicina", "300 mg", "", "DROGAS PARA EL TRATAMIENTO DE LA TUBERCULOSIS", "", "ORAL", "Sólido oral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(297, "Rifampicina", "100 mg/ 5ml", "", "DROGAS PARA EL TRATAMIENTO DE LA TUBERCULOSIS", "", "ORAL", "Líquido oral", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(298, "Capreomicina", "1000 mg", "", "DROGAS PARA EL TRATAMIENTO DE LA TUBERCULOSIS", "", "INYECTABLE", "Sólido parenteral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(300, "Isoniazida", "100 mg ", "", "DROGAS PARA EL TRATAMIENTO DE LA TUBERCULOSIS", "", "ORAL", "Sólido oral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(301, "Isoniazida", "300 mg", "", "DROGAS PARA EL TRATAMIENTO DE LA TUBERCULOSIS", "", "ORAL", "Sólido oral", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(302, "Etionamida", "250 mg ", "", "DROGAS PARA EL TRATAMIENTO DE LA TUBERCULOSIS ", "", "ORAL", "Sólido oral", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(303, "Etionamida", "500 mg", "", "DROGAS PARA EL TRATAMIENTO DE LA TUBERCULOSIS ", "", "ORAL", "Sólido oral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(304, "Pirazinamida", "500 mg", "", "DROGAS PARA EL TRATAMIENTO DE LA TUBERCULOSIS ", "", "ORAL", "Sólido oral", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(305, "Etambutol", "400 mg", "", "DROGAS PARA EL TRATAMIENTO DE LA TUBERCULOSIS ", "", "ORAL", "Sólido oral", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(306, "Rifampicina + Isoniazida", "300 mg + 150 mg", "", "DROGAS PARA EL TRATAMIENTO DE LA TUBERCULOSIS ", "", "ORAL", "Sólido oral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(307, "Rifampicina + Isoniazida", "150 mg + 75 mg", "", "DROGAS PARA EL TRATAMIENTO DE LA TUBERCULOSIS ", "", "ORAL", "Sólido oral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(308, "Rifampicina + Isoniazida", "150 mg + 150 mg", "", "DROGAS PARA EL TRATAMIENTO DE LA TUBERCULOSIS ", "", "ORAL", "Sólido oral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(309, "Etambutol + Isoniazida", "400 mg + 150 mg", "", "DROGAS PARA EL TRATAMIENTO DE LA TUBERCULOSIS ", "", "ORAL", "Sólido oral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(310, "Rifampicina + Pirazinamida + Isoniazida", "120 mg + 300 mg+60mg", "", "DROGAS PARA EL TRATAMIENTO DE LA TUBERCULOSIS ", "", "ORAL", "Sólido oral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(311, "Rifampicina + Pirazinamida + Isoniazida", "150 mg + 400 mg+75mg", "", "DROGAS PARA EL TRATAMIENTO DE LA TUBERCULOSIS ", "", "ORAL", "Sólido oral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(312, "Rifampicina + Pirazinamida + Etambutol + Isoniazid", "165 mg + 400 mg + 275 mg + 75 mg", "", "DROGAS PARA EL TRATAMIENTO DE LA TUBERCULOSIS ", "", "ORAL", "Sólido oral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(313, "Clofazimina", "100 mg", "", "DROGAS PARA EL TRATAMIENTO DE LA LEPRA", "", "ORAL", "Sólido oral", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(314, "Dapsona", "100 mg", "", "DROGAS PARA EL TRATAMIENTO DE LA LEPRA", "", "ORAL", "Sólido oral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(315, "Aciclovir", "200 mg/5ml", "", "ANTIVIRALES DE ACCIÓN DIRECTA", "", "ORAL", "Líquido oral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(316, "Aciclovir", "200 mg ", "", "ANTIVIRALES DE ACCIÓN DIRECTA", "", "ORAL", "Sólido oral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(318, "Aciclovir", "800 mg", "", "ANTIVIRALES DE ACCIÓN DIRECTA", "", "INYECTABLE", "Sólido parenteral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(319, "Aciclovir", "250 mg", "", "ANTIVIRALES DE ACCIÓN DIRECTA", "", "INYECTABLE", "Sólido parenteral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(320, "Valganciclovir", "450 mg", "", "ANTIVIRALES DE ACCIÓN DIRECTA", "", "ORAL", "Sólido oral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(321, "Saquinavir", "200 mg", "", "ANTIVIRALES DE ACCIÓN DIRECTA", "", "ORAL", "Sólido oral", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(322, "Saquinavir", "500 mg", "", "ANTIVIRALES DE ACCIÓN DIRECTA", "", "ORAL", "Sólido oral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(323, "Ritonavir", "100 mg", "", "ANTIVIRALES DE ACCIÓN DIRECTA", "", "ORAL", "Sólido oral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(324, "Atazanavir", "150 mg ", "", "ANTIVIRALES DE ACCIÓN DIRECTA", "", "ORAL", "Sólido oral", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(325, "Atazanavir", "200 mg", "", "ANTIVIRALES DE ACCIÓN DIRECTA", "", "ORAL", "Sólido oral", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(326, "Darunavir", "400 mg ", "", "ANTIVIRALES DE ACCIÓN DIRECTA", "", "ORAL", "Sólido oral", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(327, "Darunavir", "600 mg", "", "ANTIVIRALES DE ACCIÓN DIRECTA", "", "ORAL", "Sólido oral", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(328, "Zidovudina", "100 mg ", "", "ANTIVIRALES DE ACCIÓN DIRECTA", "", "ORAL", "Sólido oral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(329, "Zidovudina", "300 mg", "", "ANTIVIRALES DE ACCIÓN DIRECTA", "", "ORAL", "Sólido oral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(330, "Zidovudina", "50 mg/5ml", "", "ANTIVIRALES DE ACCIÓN DIRECTA", "", "ORAL", "Líquido oral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(331, "Zidovudina", "10 mg/ml", "", "ANTIVIRALES DE ACCIÓN DIRECTA", "", "INYECTABLE", "Líquido parenteral", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(332, "Didanosina", "100 mg", "", "ANTIVIRALES DE ACCIÓN DIRECTA", "", "ORAL", "Sólido oral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(333, "Lamivudina", "150 mg", "", "ANTIVIRALES DE ACCIÓN DIRECTA", "", "ORAL", "Sólido oral", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(334, "Lamivudina", "50 mg/5ml", "", "ANTIVIRALES DE ACCIÓN DIRECTA", "", "ORAL", "Líquido oral", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(335, "Abacavir", "300 mg", "", "ANTIVIRALES DE ACCIÓN DIRECTA", "", "ORAL", "Sólido oral", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(336, "Abacavir", "10 mg/ml", "", "ANTIVIRALES DE ACCIÓN DIRECTA", "", "ORAL", "Líquido oral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(337, "Tenofovir disoproxilo", "300 mg", "", "ANTIVIRALES DE ACCIÓN DIRECTA", "", "ORAL", "Sólido oral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(338, "Emtricitabina", "10 mg/ml", "", "ANTIVIRALES DE ACCIÓN DIRECTA", "", "ORAL", "Sólido oral", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(339, "Emtricitabina", "200 mg", "", "ANTIVIRALES DE ACCIÓN DIRECTA", "", "ORAL", "Líquido oral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(340, "Nevirapina", "200 mg", "", "ANTIVIRALES DE ACCIÓN DIRECTA", "", "ORAL", "Sólido oral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(341, "Nevirapina", "50 mg/ 5ml", "", "ANTIVIRALES DE ACCIÓN DIRECTA", "", "ORAL", "liquido oral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(342, "Efavirenz", "50 mg ", "", "ANTIVIRALES DE ACCIÓN DIRECTA", "", "ORAL", "Sólido oral", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(343, "Efavirenz", "600 mg", "", "ANTIVIRALES DE ACCIÓN DIRECTA", "", "ORAL", "Sólido oral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(344, "Efavirenz", "30 mg/ml", "", "ANTIVIRALES DE ACCIÓN DIRECTA", "", "ORAL", "Líquido oral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(345, "Etravirina", "100 mg", "", "ANTIVIRALES DE ACCIÓN DIRECTA", "", "ORAL", "Sólido oral", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(346, "Etravirina", "200 mg ", "", "ANTIVIRALES DE ACCIÓN DIRECTA", "", "ORAL", "Sólido oral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(347, "Oseltamivir", "75 mg", "", "ANTIVIRALES DE ACCIÓN DIRECTA", "", "ORAL", "Sólido oral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(348, "Zidovudina + Lamivudina", "300 mg + 150 mg", "", "ANTIVIRALES DE ACCIÓN DIRECTA", "", "ORAL", "Sólido oral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(349, "Lamivudina + Abacavir", "300 mg + 600 mg", "", "ANTIVIRALES DE ACCIÓN DIRECTA", "", "ORAL", "Sólido oral", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(350, "Tenofovir + Emtricitabina", "300 mg + 200 mg", "", "ANTIVIRALES DE ACCIÓN DIRECTA", "", "ORAL", "Sólido oral", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(351, "Zidovudina + Lamivudina + Nevirapina", "300 mg + 150 mg + 200mg", "", "ANTIVIRALES DE ACCIÓN DIRECTA", "", "ORAL", "Sólido oral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(352, "Tenofovir + Emtricitabina + Efavirenz", "300 mg + 200 mg+600mg", "", "ANTIVIRALES DE ACCIÓN DIRECTA", "", "ORAL", "Sólido oral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(353, "Lopinavir + Ritonavir", "100mg + 25 mg", "", "ANTIVIRALES DE ACCIÓN DIRECTA", "", "ORAL", "Sólido oral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(354, "Lopinavir + Ritonavir", "200 mg + 50 mg", "", "ANTIVIRALES DE ACCIÓN DIRECTA", "", "ORAL", "Sólido oral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(355, "Lopinavir + Ritonavir", "(80 mg + 20 mg)/ml", "", "ANTIVIRALES DE ACCIÓN DIRECTA", "", "ORAL", "Líquido oral", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(356, "Raltegravir", "400 mg", "", "ANTIVIRALES DE ACCIÓN DIRECTA", "", "ORAL", "Sólido oral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(357, "Diftérica antitoxina", "10.000 UI", "", "", "", "INYECTABLE", "Líquido parenteral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(358, "Tetánica antitoxina", "250 UI", "", "", "", "INYECTABLE", "Líquido parenteral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(359, "Suero antiofídico polivalente", "", "", "SUEROS INMUNES", "", "INYECTABLE", "Líquido parenteral/ sólido parenteral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(360, "Inmunoglobulina humana normal para administración ", "", "", "INMUNOGLOBULINAS", "", "INYECTABLE", "Líquido parenteral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(361, "Inmunoglobulina anti D", "200 mcg - 300 mcg", "", "INMUNOGLOBULINAS", "", "INYECTABLE", "Líquido parenteral", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(362, "Inmunoglobulina antitetánica", "200 UI - 500 UI", "", "INMUNOGLOBULINAS", "", "INYECTABLE", "Líquido parenteral/ Sólido parenteral", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(363, "Inmunoglobulina antirrábica", "300 UI/ml - 1500 UI/ml", "", "INMUNOGLOBULINAS", "", "INYECTABLE", "Líquido parenteral", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(364, "Vacuna antimeningococo (B + C)", "", "", "VACUNAS ANTIBACTERIANAS", "", "INYECTABLE", "Líquido parenteral/ sólido parenteral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(365, "Toxoide diftérico tetánico + Vacuna pertussis (Vac", "", "", "VACUNAS ANTIBACTERIANAS", "", "INYECTABLE", "Líquido parenteral/ Sólido parenteral", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(366, "Vacuna antineumococo (polisacárido y conjugado)", "", "", "VACUNAS ANTIBACTERIANAS", "", "INYECTABLE", "Líquido parenteral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(367, "Toxoide diftérico tetánico (Toxoide diftérico + To", "", "", "VACUNAS ANTIBACTERIANAS", "", "INYECTABLE", "Líquido parenteral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(368, "Toxoide tetánico", "> 40 UI/0.5ml", "", "VACUNAS ANTIBACTERIANAS", "", "INYECTABLE", "Líquido parenteral", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(369, "Vacuna BCG", "", "", "VACUNAS ANTIBACTERIANAS", "", "INYECTABLE", "Sólido parenteral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(370, "Vacuna antihepatitis B", "", "", "VACUNAS ANTIVIRALES", "", "INYECTABLE", "Líquido parenteral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(371, "Dornasa alfa (desoxirribonucleasa) ", "2.5 mg/2.5 ml  (2500 UI)", "", "EXPECTORANTES, EXCL. COMBINACIONES CON SUPRESORES ", "", "RESPIRATORIA", "Líquido para inhalación", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(372, "Espiramicina ", "3´000.000 UI ", "", "EXPECTORANTES, EXCL. COMBINACIONES CON SUPRESORES ", "", "ORAL", "Sólido oral ", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(373, "Sulfadiazina ", "500 mg", "", "SULFONAMIDAS Y TRIMETOPRIMA", "", "ORAL", "Sólido oral", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(374, "Pirimetamina", "25 mg  ", "", "ANTIPALÚDICOS", "", "ORAL", "Sólido oral ", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(375, "Piridostigmina ", "60 mg ", "", "PARASIMPATICOMIMÉTICOS", "", "ORAL", "Sólido oral ", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(376, "Pegfilgrastim ", "10 mg/ml ", "", "CITOKINAS E INMUNOMODULADORES", "", "INYECTABLE", "Líquido parenteral", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(377, "Exemestano ", "25 mg ", "", "ANTAGONISTAS DE HORMONAS Y AGENTES RELACIONADOS", "", "ORAL", "Sólido oral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(378, "Letrozol ", "2.5 mg ", "", "ANTAGONISTAS DE HORMONAS Y AGENTES RELACIONADOS", "", "ORAL", "Sólido oral  ", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(379, "Tamoxifeno ", "10 mg y 20 mg ", "", "ANTAGONISTAS DE HORMONAS Y AGENTES RELACIONADOS", "", "ORAL", "Sólido oral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(380, "Sunitinib", "12.5 mg", "", "OTROS AGENTES ANTINEOPLÁSICOS", "", "ORAL", "Sólido oral", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(381, "Sunitinib", "50 mg", "", "OTROS AGENTES ANTINEOPLÁSICOS", "", "ORAL", "Sólido oral", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(382, "Imatinib", "100 mg", "", "OTROS AGENTES ANTINEOPLÁSICOS", "", "ORAL", "Sólido oral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(383, "Imatinib", "400 mg", "", "OTROS AGENTES ANTINEOPLÁSICOS", "", "ORAL", "Sólido oral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(384, "Bevacizumab", "25 mg/ml ", "", "OTROS AGENTES ANTINEOPLÁSICOS", "", "INYECTABLE", "Líquido parenteral ", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(385, "Trastuzumab ", "440 mg ", "", "OTROS AGENTES ANTINEOPLÁSICOS", "", "INYECTABLE", "Sólido parenteral ", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(386, "Caspofungina ", "50 mg", "", "ANTIMICÓTICOS PARA USO SISTÉMICO", "", "INYECTABLE", "Sólido parenteral ", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(387, "Caspofungina", "70 mg ", "", "ANTIMICÓTICOS PARA USO SISTÉMICO", "", "INYECTABLE", "Sólido parenteral ", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(388, "Vacuna antihepatitis A", "", "", "VACUNAS ANTIVIRALES", "", "INYECTABLE", "Líquido parenteral", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(389, "Vacuna triple viral (Parotiditis-Sarampión- Rubeól", "", "", "VACUNAS ANTIVIRALES", "", "INYECTABLE", "Sólido parenteral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(390, "Vacuna antipoliomielítica", "", "", "VACUNAS ANTIVIRALES", "", "ORAL", "Líquido oral", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(391, "Vacuna antirrábica", "", "", "VACUNAS ANTIVIRALES", "", "INYECTABLE", "Líquido parenteral/ sólido parenteral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(392, "Vacuna antirotavirus", "", "", "VACUNAS ANTIVIRALES", "", "ORAL", "Líquido oral ", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(393, "Zóster, virus vivo atenuado", "", "", "VACUNAS ANTIVIRALES", "", "INYECTABLE", "Sólido parenteral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(394, "Vacuna antiamarílica", "", "", "VACUNAS ANTIVIRALES", "", "INYECTABLE", "Sólido parenteral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(395, "Vacuna pentavalente viral (Difteria -haemophilus i", "", "", "VACUNAS ANTIBACTERIANAS Y ANTIVIRALES COMBINADAS", "", "INYECTABLE", "Líquido parenteral/ sólido parenteral ", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(396, "Etanercept", "25 mg ", "", "AGENTES INMUNOSUPRESORES", "", "INYECTABLE", "Líquido parenteral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(397, "Etanercept", "50 mg", "", "AGENTES INMUNOSUPRESORES", "", "INYECTABLE", "Líquido parenteral", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(398, "Infliximab", "100 mg", "", "AGENTES INMUNOSUPRESORES", "", "INYECTABLE", "Sólido parenteral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(399, "Diclofenaco", "50 mg", "", "PRODUCTOS ANTIINFLAMA", "", "ORAL", "Solido oral", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(400, "Diclofenaco", "25 mg/ml", "", "PRODUCTOS ANTIINFLAMATORIOS Y ANTIRREUMÁTICOS NO E", "", "INYECTABLE", "Líquido parenteral", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(401, "Ketorolaco", "30 mg/ml", "", "PRODUCTOS ANTIINFLAMATORIOS Y ANTIRREUMÁTICOS NO E", "", "INYECTABLE", "Líquido parenteral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(402, "Ibuprofeno", "400 mg", "", "PRODUCTOS ANTIINFLAMATORIOS Y ANTIRREUMÁTICOS NO E", "", "ORAL", "Solido oral", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(403, "Ibuprofeno", "200 mg/5ml", "", "PRODUCTOS ANTIINFLAMATORIOS Y ANTIRREUMÁTICOS NO E", "", "ORAL", "Líquido oral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(404, "Penicilamina", "125 mg ", "", "AGENTES ANTIRREUMÁTICOS ESPECÍFICOS", "", "ORAL", "Sólido oral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(405, "Penicilamina", "250 mg", "", "AGENTES ANTIRREUMÁTICOS ESPECÍFICOS", "", "ORAL", "Sólido oral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(406, "Suxametonio", "20 mg/ml", "", "AGENTES RELAJANTES MUSCULARES DE ACCIÓN PERIFÉRICA", "", "INYECTABLE", "Líquido parenteral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(407, "Toxina botulínica", "100 U ", "", "AGENTES RELAJANTES MUSCULARES DE ACCIÓN PERIFÉRICA", "", "INYECTABLE", "Sólido parenteral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(408, "Toxina botulínica", "500 U", "", "AGENTES RELAJANTES MUSCULARES DE ACCIÓN PERIFÉRICA", "", "INYECTABLE", "Sólido parenteral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(409, "Pancuronio", "2 mg/ml", "", "AGENTES RELAJANTES MUSCULARES DE ACCIÓN PERIFÉRICA", "", "INYECTABLE", "Líquido parenteral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(410, "Rocuronio, Bromuro", "10 mg/ml", "", "AGENTES RELAJANTES MUSCULARES DE ACCIÓN PERIFÉRICA", "", "INYECTABLE", "Líquido parenteral", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(411, "Alopurinol", "100 mg", "", "PREPARADOS ANTIGOTOSOS", "", "ORAL", "Sólido oral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(412, "Alopurinol", "300 mg", "", "PREPARADOS ANTIGOTOSOS", "", "ORAL", "Sólido oral", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(413, "Colchicina", "0.5 mg", "", "PREPARADOS ANTIGOTOSOS", "", "ORAL", "Sólido oral", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(414, "Ácido alendrónico (Alendronato sódico)", "70 mg", "", "DROGAS QUE AFECTAN LA MINERALIZACIÓN", "", "ORAL", "Sólido oral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(415, "Sevoflurano", "1 mg/ml", "", "ANESTÉSICOS GENERALES", "", "RESPIRATORIA", "Líquido para inhalación", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(416, "Tiopental sódico", "1 g", "", "ANESTÉSICOS GENERALES", "", "INYECTABLE", "Sólido parenteral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(417, "Fentanilo", "0.5 mg/10 ml", "", "ANESTÉSICOS GENERALES", "", "INYECTABLE", "Líquido parenteral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(418, "Fentanilo", "2.5 mcg", "", "ANESTÉSICOS GENERALES", "", "TOPICO", "Sólido cutáneo(parche transdérmico)", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(419, "Remifentanilo", "2 mg ", "", "ANESTÉSICOS GENERALES", "", "INYECTABLE", "Sólido parenteral", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(420, "Remifentanilo", "5 mg", "", "ANESTÉSICOS GENERALES", "", "INYECTABLE", "Sólido parenteral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(421, "Propofol", "10 mg/ml ", "", "ANESTÉSICOS GENERALES", "", "INYECTABLE", "Líquido parenteral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(422, "Propofol", "20mg/ml", "", "ANESTÉSICOS GENERALES", "", "INYECTABLE", "Líquido parenteral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(423, "Bupivacaína (sin epinefrina)", "0.5 % ", "", "ANESTÉSICOS LOCALES", "", "INYECTABLE", "Líquido parenteral", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(424, "Bupivacaína (sin epinefrina)", "0.75 %", "", "ANESTÉSICOS LOCALES", "", "INYECTABLE", "Líquido parenteral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(425, "Bupivacaína hiperbárica", "0.5 % ", "", "ANESTÉSICOS LOCALES", "", "INYECTABLE", "Líquido parenteral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(426, "Bupivacaína hiperbárica", "0.75 %", "", "ANESTÉSICOS LOCALES", "", "INYECTABLE", "Líquido parenteral", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(427, "Lidocaína (sin epinefrina)", "2 %", "", "ANESTÉSICOS LOCALES", "", "INYECTABLE", "Líquido parenteral", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(428, "Lidocaína con epinefrina", "2 % + 1:200.000", "", "ANESTÉSICOS LOCALES", "", "INYECTABLE", "Líquido parenteral", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(429, "Morfina", "10 mg/ml ", "", "OPIOIDES", "", "INYECTABLE", "Líquido parenteral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(430, "Morfina", "20 mg/ml", "", "OPIOIDES", "", "INYECTABLE", "Líquido parenteral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(431, "Morfina", "10 mg", "", "OPIOIDES", "", "ORAL", "Sólido oral (Liberación prolongada)", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(432, "Morfina", "30 mg", "", "OPIOIDES", "", "ORAL", "Sólido oral (Liberación prolongada)", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(433, "Morfina", "60 mg", "", "OPIOIDES", "", "ORAL", "Sólido oral (Liberación prolongada)", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(434, "Morfina", "10 mg", "", "OPIOIDES", "", "ORAL", "Sólido oral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(435, "Morfina", "2 mg/ml ", "", "OPIOIDES", "", "ORAL", "Líquido oral", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(436, "Morfina", "20 mg/ml", "", "OPIOIDES", "", "ORAL", "Líquido oral", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(437, "Hidromorfona", "2.5 mg", "", "OPIOIDES", "", "ORAL", "Sólido oral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(438, "Hidromorfona", "5 mg", "", "OPIOIDES", "", "ORAL", "Sólido oral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(439, "Hidromorfona", "2 mg/ml", "", "OPIOIDES", "", "INYECTABLE", "Líquido parenteral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(440, "Oxicodona", "5 mg", "", "OPIOIDES", "", "ORAL", "Sólido oral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(441, "Oxicodona", "10 mg ", "", "OPIOIDES", "", "ORAL", "Sólido oral (liberación pr", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(442, "Oxicodona", "40 mg", "", "OPIOIDES", "", "ORAL", "Sólido oral (liberación pr", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(443, "Codeína", "10 mg ", "", "OPIOIDES", "", "ORAL", "Sólido oral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(444, "Codeína", "30 mg ", "", "OPIOIDES", "", "ORAL", "Sólido oral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(445, "Codeína", "10 mg/5ml", "", "OPIOIDES", "", "ORAL", "Líquido oral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(446, "Buprenorfina", "20 mg", "", "OPIOIDES", "", "TOPICO", "Solido cutaneo (parche transdérmico)", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(447, "Buprenorfina", "0.2 mg", "", "OPIOIDES", "", "ORAL", "Sólido oral (Sublingual)", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(448, "Buprenorfina", "0.3 mg/ml", "", "OPIOIDES", "", "INYECTABLE", "Líquido parenteral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(449, "Tramadol", "50 mg", "", "", "", "ORAL", "solido oral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(450, "Tramadol", "100 mg/ml", "", "", "", "ORAL", "Liquido oral", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(451, "Tramadol", "50 mg/ml", "", "", "", "INYECTABLE", "Líquido parenteral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(452, "Ácido acetil salicílico       ", "500 mg", "", "OTROS ANALGÉSICOS Y ANTIPIRÉTICOS", "", "ORAL", "Sólido oral", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(453, "Paracetamol", "500 mg", "", "OTROS ANALGÉSICOS Y ANTIPIRÉTICOS", "", "ORAL", "Sólido oral", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(454, "Paracetamol", "120 mg/5 ml", "", "OTROS ANALGÉSICOS Y ANTIPIRÉTICOS", "", "ORAL", "Liquido oral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(455, "Paracetamol", "160mg/5 ml", "", "OTROS ANALGÉSICOS Y ANTIPIRÉTICOS", "", "ORAL", "Liquido oral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(456, "Paracetamol", "100 mg/ml", "", "OTROS ANALGÉSICOS Y ANTIPIRÉTICOS", "", "ORAL", "Líquido oral (gotas)", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(457, "Paracetamol", "100 mg ", "", "OTROS ANALGÉSICOS Y ANTIPIRÉTICOS", "", "RECTAL", "Sólido rectal", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(458, "Paracetamol", "300 mg", "", "OTROS ANALGÉSICOS Y ANTIPIRÉTICOS", "", "RECTAL", "Sólido rectal", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(459, "Paracetamol", "10 mg/ml", "", "OTROS ANALGÉSICOS Y ANTIPIRÉTICOS", "", "INYECTABLE", "Líquido parenteral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(460, "Fenobarbital", "100 mg", "", "ANTIEPILÉPTICOS", "", "ORAL", "Sólido oral", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(461, "Fenobarbital", "20 mg/5 ml", "", "ANTIEPILÉPTICOS", "", "ORAL", "Líquido oral", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(462, "Fenobarbital", "60 mg/ml", "", "ANTIEPILÉPTICOS", "", "INYECTABLE", "Líquido parenteral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(463, "Fenitoína", "100 mg", "", "ANTIEPILÉPTICOS", "", "ORAL", "Sólido oral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(464, "Fenitoína", "125 mg/5 ml", "", "ANTIEPILÉPTICOS", "", "ORAL", "Líquido oral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(465, "Fenitoína", "50 mg/ml", "", "ANTIEPILÉPTICOS", "", "INYECTABLE", "Líquido parenteral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(466, "Clonazepam", "0.5 mg ", "", "ANTIEPILÉPTICOS", "", "ORAL", "Sólido oral", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(467, "Clonazepam", "2 mg", "", "ANTIEPILÉPTICOS", "", "ORAL", "Sólido oral", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(468, "Clonazepam", "2.5 mg/ml", "", "ANTIEPILÉPTICOS", "", "ORAL", "Líquido oral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(469, "Carbamazepina", "200 mg", "", "ANTIEPILÉPTICOS", "", "ORAL", "Sólido oral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(470, "Carbamazepina", "400 mg", "", "ANTIEPILÉPTICOS", "", "ORAL", "Sólido oral (Liberación contr", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(471, "Carbamazepina", "100 mg/5ml", "", "ANTIEPILÉPTICOS", "", "ORAL", "Líquido oral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(472, "Ácido valproico (Sal sódica)", "500 mg", "", "ANTIEPILÉPTICOS", "", "ORAL", "Sólido oral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(473, "Ácido valproico (Sal sódica)", "200mg/ml ", "", "ANTIEPILÉPTICOS", "", "ORAL", "Líquido oral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(474, "Ácido valproico (Sal sódica)", "375 mg/ml", "", "ANTIEPILÉPTICOS", "", "ORAL", "Líquido oral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(475, "Ácido valproico (Sal sódica)", "250 mg /5ml", "", "ANTIEPILÉPTICOS", "", "ORAL", "Líquido oral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(476, "Lamotrigina", "25 mg ", "", "ANTIEPILÉPTICOS", "", "ORAL", "Sólido oral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(477, "Lamotrigina", "100 mg", "", "ANTIEPILÉPTICOS", "", "ORAL", "Sólido oral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(478, "Gabapentina", "300 mg", "", "ANTIEPILÉPTICOS", "", "ORAL", "Sólido oral", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(479, "Biperideno", "2 mg", "", "AGENTES ANTICOLINÉRGICOS", "", "ORAL", "Sólido oral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(480, "Biperideno", "4 mg", "", "AGENTES ANTICOLINÉRGICOS", "", "ORAL", "Sólido oral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(481, "Biperideno", "", "", "AGENTES ANTICOLINÉRGICOS", "", "ORAL", "Sólido oral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(482, "Levodopa + Carbidopa", "100 mg + 10 mg", "", "AGENTES DOPAMINÉRGICOS", "", "INYECTABLE", "Líquido parenteral", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(483, "Levodopa + Carbidopa", "250 mg + 25 mg", "", "AGENTES DOPAMINÉRGICOS", "", "ORAL", "Sólido oral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(484, "Cabergolina", "0.5 mg", "", "AGENTES DOPAMINÉRGICOS", "", "ORAL", "Sólido oral", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(485, "Selegilina", "5 mg", "", "AGENTES DOPAMINÉRGICOS", "", "ORAL", "Sólido oral", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(486, "Clorpromazina", "25 mg", "", "ANTIPSICÓTICOS", "", "ORAL", "Sólido oral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(487, "Clorpromazina", "100 mg", "", "ANTIPSICÓTICOS", "", "ORAL", "Sólido oral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(488, "Clorpromazina", "25 mg/2ml", "", "ANTIPSICÓTICOS", "", "INYECTABLE", "Líquido parenteral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(489, "Levomepromazina", "25 mg ", "", "ANTIPSICÓTICOS", "", "ORAL", "Sólido oral", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(490, "Levomepromazina", "100 mg", "", "ANTIPSICÓTICOS", "", "ORAL", "Sólido oral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(491, "Levomepromazina", "40 mg/ml", "", "ANTIPSICÓTICOS", "", "ORAL", "Líquido oral", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(492, "Levomepromazina", "25 mg/ml", "", "ANTIPSICÓTICOS", "", "INYECTTABLE", "Líquido parenteral", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(493, "Haloperidol", "5 mg ", "", "ANTIPSICÓTICOS", "", "ORAL", "Sólido oral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(494, "Haloperidol", "10 mg", "", "ANTIPSICÓTICOS", "", "ORAL", "Sólido oral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(495, "Haloperidol", "2 mg/ml", "", "ANTIPSICÓTICOS", "", "ORAL", "Líquido oral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(496, "Haloperidol", "5 mg/ml", "", "ANTIPSICÓTICOS", "", "INYECTTABLE", "Líquido parentera", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(497, "Haloperidol decanoato", "50 mg/ml", "", "ANTIPSICÓTICOS", "", "INYECTTABLE", "Líquido parentera", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(498, "Quetiapina", "25 mg ", "", "ANTIPSICÓTICOS", "", "ORAL", "Sólido oral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(499, "Quetiapina", "300 mg", "", "ANTIPSICÓTICOS", "", "ORAL", "Sólido oral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(500, "Litio, carbonato", "300 mg", "", "ANTIPSICÓTICOS", "", "ORAL", "Sólido oral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(501, "Risperidona", "1 mg", "", "ANTIPSICÓTICOS", "", "ORAL", "Sólido oral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(502, "Risperidona", "2 mg", "", "ANTIPSICÓTICOS", "", "ORAL", "Sólido oral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(503, "Risperidona", "25 mg ", "", "ANTIPSICÓTICOS", "", "INYECTABLE", "Sólido parenteral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(504, "Risperidona", "37.5 mg", "", "ANTIPSICÓTICOS", "", "INYECTABLE", "Sólido parenteral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(505, "Risperidona", "1 mg/ml", "", "ANTIPSICÓTICOS", "", "ORAL", "Líquido oral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(506, "Diazepam", "5 mg", "", "", "", "ORAL", "Sólido oral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(507, "Diazepam", "10 mg", "", "", "", "ORAL", "Sólido oral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(508, "Diazepam", "2 mg/5ml", "", "", "", "ORAL", "Líquido oral", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(509, "Diazepam", "5 mg/ml", "", "", "", "INYECTABLE", "Líquido parenteral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(510, "Alprazolam", "0.25 mg ", "", "ANSIOLÍTICOS", "", "ORAL", "Sólido oral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(511, "Alprazolam", "0.50 mg", "", "ANSIOLÍTICOS", "", "ORAL", "Sólido oral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(512, "Hidrato de cloral", "100 mg/ml", "", "HIPNÓTICOS Y SEDANTES", "", "ORAL", "Líquido oral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(513, "Midazolam", "1 mg/ml ", "", "HIPNÓTICOS Y SEDANTES", "", "INYECTABLE", "Líquido parenteral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(514, "Midazolam", "5 mg/ml", "", "HIPNÓTICOS Y SEDANTES", "", "INYECTABLE", "Líquido parenteral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(515, "Amitriptilina", "10 mg", "", "ANTIDEPRESIVOS", "", "ORAL", "Sólido oral", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(516, "Amitriptilina", "25 mg", "", "ANTIDEPRESIVOS", "", "ORAL", "Sólido oral", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(517, "Fluoxetina", "20 mg", "", "ANTIDEPRESIVOS", "", "ORAL", "Sólido oral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(518, "Sertralina", "50 mg ", "", "ANTIDEPRESIVOS", "", "ORAL", "Sólido oral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(519, "Sertralina", "100 mg", "", "ANTIDEPRESIVOS", "", "ORAL", "Sólido oral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(520, "Neostigmina", "0.5 mg/m", "", "PARASIMPATICOMIMÉTICOS", "", "ORAL", "Líquido parenteral", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(521, "Disulfiram", "500 mg", "", "DROGAS USADAS EN DESÓRDENES ADICTIVOS", "", "ORAL", "Sólido oral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(522, "Dimenhidrinato", "50 mg", "", "PREPARADOS CONTRA EL VÉRTIGO", "", "ORAL", "Sólido oral", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(523, "Metronidazol", "250 mg ", "", "AGENTES CONTRA LA AMEBIASIS Y OTRAS ENFERMEDADES P", "", "ORAL", "Sólido oral", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(524, "Metronidazol", "500 mg", "", "AGENTES CONTRA LA AMEBIASIS Y OTRAS ENFERMEDADES P", "", "ORAL", "Sólido oral", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(525, "Mometasona", "50 mcg", "", "DESCONGESTIVOS Y OTROS PREPARADOS NASALES PARA USO", "", "NASAL", "Líquido para inhalación nasal", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(526, "Epinefrina (adrenalina) racémica", "22.5 mg/ml (2.25 %)", "", "ADRENÉRGICOS INHALATORIOS", "", "NASAL", "Líquido para nebulización", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(527, "Salbutamol", "5 mg/ml", "", "ADRENÉRGICOS INHALATORIOS", "", "NASAL", "Líquido para nebulización", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(528, "Salbutamol", "0.1 mg/dosis", "", "ADRENÉRGICOS INHALATORIOS", "", "NASAL", "Líquido para inhalación ", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(529, "Beclometasona", "50 mcg/dosis", "", "OTROS AGENTES CONTRA PADECIMIENTOS OBSTRUCTIVOS DE", "", "NASAL", "Líquido para inhalación ", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(530, "Beclometasona", "250 mcg/dosis", "", "OTROS AGENTES CONTRA PADECIMIENTOS OBSTRUCTIVOS DE", "", "NASAL", "Líquido para inhalación ", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(531, "Ipratropio bromuro", "0.02 mg/dosis", "", "OTROS AGENTES CONTRA PADECIMIENTOS OBSTRUCTIVOS DE", "", "NASAL", "Líquido para inhalación", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(532, "Ipratropio bromuro", "0.25 mg/ml", "", "OTROS AGENTES CONTRA PADECIMIENTOS OBSTRUCTIVOS DE", "", "NASAL", "Líquido para nebulización", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(533, "Tiotropio bromuro", "25 mcg (equivalente a 18 mcg", "", "OTROS AGENTES CONTRA PADECIMIENTOS OBSTRUCTIVOS DE", "", "NASAL", "Sólido para inhalación", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(534, "Aminofilina", "25 mg/ml", "", "", "", "", "", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(535, "Acetilcisteína ", "300 mg", "", "", "", "", "", "A", "", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(536, "Difenhidramina", "", "", "", "", "", "", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(537, "Difenhidramina", "", "", "", "", "", "", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(538, "Difenhidramina", "", "", "", "", "", "", "A", "", 1, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(539, "Loratadina", "", "", "", "", "", "", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(540, "Loratadina", "", "", "", "", "", "", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(541, "Fosfolípidos naturales (Surfactante pulmonar)", "200 mg/8ml", "", "", "", "TRAQUEA", "Líquido intratraqueal", "A", "", 2, HealthMonitorApplicattion.getApplication().getMedicineDao());
            Utils.saveMediceToDataBaseLocal(542, "Salbutamol", "100 ml", "Sulfato de Salbutamol", "Utilizado para tratamiento de asma bronquial, obst", "Conservese a temperatura ambiente", "oral", "Jarabe", "A", "Apotex", 3, HealthMonitorApplicattion.getApplication().getMedicineDao());
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


}
