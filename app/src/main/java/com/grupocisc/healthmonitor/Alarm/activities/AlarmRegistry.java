package com.grupocisc.healthmonitor.Alarm.activities;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.grupocisc.healthmonitor.Alarm.adapters.AlarmListReminderTimesCardAdapter;
import com.grupocisc.healthmonitor.R;
import com.grupocisc.healthmonitor.entities.EAlarmDetails;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.grupocisc.healthmonitor.Utils.Utils.UcallCalendar;
import static com.grupocisc.healthmonitor.Utils.Utils.UcallCalendarTime;
import static com.grupocisc.healthmonitor.Utils.Utils.inicializarFecha;
import static com.grupocisc.healthmonitor.Utils.Utils.inicializarHora;

/**
 * Created by developer on 21/07/2017.
 */

public class AlarmRegistry extends AppCompatActivity implements DatePickerDialog.OnDateSetListener
        , TimePickerDialog.OnTimeSetListener  {

    private static final String TAG = "[AlarmRegistry]";
    private static final String sentServer = "N";
    private int positionSRTp=0;
    private int positionSRTm=0;

    private AlarmListReminderTimesCardAdapter adapterAlarm;
    //private RecyclerView recyclerView;

    private String [] ReminderTypes ;
    private String [] ReminderTimesFrequencies;
    private String [] ReminderTimesIntervals;

    @BindView(R.id.spinner_ReminderTypes) Spinner spinner_ReminderTypes;
    @BindView(R.id.spinner_ReminderTimes ) Spinner spinner_ReminderTimes;

    @BindView(R.id.rgrpDuration)  RadioGroup rgrpDuration;

    @BindView(R.id.lyt_recycler ) LinearLayout lyt_recycler;
    @BindView(R.id.rv_reminder_times) RecyclerView recyclerView;
    @BindView(R.id.lyt_startDate) LinearLayout lyt_startDate;
    @BindView(R.id.txt_startDate) TextView txt_startDate;

    @BindView(R.id.lyt_startHour) LinearLayout lyt_startHour;
    @BindView(R.id.txt_startHour) TextView txt_startHour;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        String Method ="[onCreate]";
        Log.i(TAG, Method +  "Init..."  );
        Log.i(TAG, Method +  "super.onCreate..."  );
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

        setContentView(R.layout.alarm_registry_activity);
        ButterKnife.bind(this);

        ReminderTypes =  getResources().getStringArray( R.array.array_ReminderTypesDesc);
        ReminderTimesFrequencies =  getResources().getStringArray( R.array.array_ReminderTimesFDesc);
        ReminderTimesIntervals =  getResources().getStringArray( R.array.array_ReminderTimesIDesc);
        ArrayAdapter<String> adapterRTp = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item  ,ReminderTypes);
        this.spinner_ReminderTypes.setAdapter(adapterRTp);
        this.spinner_ReminderTypes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Object item = parent.getItemAtPosition(position);
                positionSRTp=position;
                setSpinner(position);
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
                positionSRTm=position;
                setListAlarm(position);
                //Log.i(TAG, "onItemSelected: " + item.toString());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        this.lyt_startDate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                callCalendar();
            }
        });

        txt_startDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                //SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
                SimpleDateFormat formato = new SimpleDateFormat( getResources().getString(R.string.txt_DateFormat103) );
                Calendar c = Calendar.getInstance();
                Date fecha = c.getTime();
                Date fecha2 = null;
                try {
                    fecha2 = formato.parse(txt_startDate.getText().toString());
                } catch (ParseException e) {
                    Log.e(TAG, "Error al convertir fecha " + e.getMessage() );
                    e.printStackTrace();
                }

                if(  fecha2.compareTo(fecha) != 0 &&   fecha2.compareTo(fecha) > 0)
                {
                    txt_startDate.setError( "Fecha no válida." );
                    txt_startDate.setText(inicializarFecha());
                }

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        this.lyt_startHour.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                callCalendarTime();
            }
        });

        this.txt_startHour.setText(inicializarHora());

        this.rgrpDuration.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                RadioButton rb;
                switch (checkedId){
                    case   R.id.rbtContinuous:
                        rb = (RadioButton) findViewById(checkedId);
                        Toast.makeText(getBaseContext(),rb.getText(),Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.rbtNumberOfDays:
                         rb = (RadioButton) findViewById(checkedId);
                        Toast.makeText(getBaseContext(),rb.getText(),Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });



        Log.i(TAG, Method +  "End..." );
    }

    @Override
    public void onResume() {
        String Method ="[onResume]";
        Log.i(TAG, Method +  "Init..."  );
        super.onResume();
        DatePickerDialog dpd = (DatePickerDialog) getFragmentManager().findFragmentByTag("Datepickerdialog");
        if (dpd != null)
            dpd.setOnDateSetListener(this);
//        TimePickerDialog tpd = (TimePickerDialog) getFragmentManager().findFragmentByTag("Timepickerdialog");
//        if (tpd != null)
//            tpd.setOnTimeSetListener(this);
        Log.i(TAG, Method +  "End..."  );
    }

    private void setSpinner(int id){
        switch ( id){
            case 0: {
                ArrayAdapter<String> adapterRTmF = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, ReminderTimesFrequencies);
                this.spinner_ReminderTimes.setAdapter(adapterRTmF);
                break;
            }
            case 1: {
                ArrayAdapter<String> adapterRTmI = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, ReminderTimesIntervals);
                this.spinner_ReminderTimes.setAdapter(adapterRTmI);
                break;
            }
        }
    }

    private String hourAdd(String hora , int hh , int mm , int ss){
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
        return   (h<10 ? "0" + h : h) + ":" +  (m<10 ? "0" + m : m)  + ":" +  (s<10 ? "0" + s : s) + ""  ;
    }

    private void setListAlarm(int id){
        String Method ="[setListAlarm]";

        int HH = Integer.parseInt( this.txt_startHour.getText().toString().substring(0,2) );
        int mm = Integer.parseInt( this.txt_startHour.getText().toString().substring(3,5) );
        int ss = 0 ;
        String time = (HH < 10 ? "0" + HH : HH) + ":" + (mm < 10 ? "0" + mm : mm) + ":" + (ss < 10 ? "0" + ss : ss)      +"";
        int HH24 = 24;
        int periodo = 0;

        Log.i(TAG, Method +  "Init..."  );
        List<EAlarmDetails> lstAlarmList = new ArrayList<>();
        EAlarmDetails alarmDetails;

        if ( positionSRTp == 0){
            Log.i(TAG, Method +  "Intervalos positionSRTm=" + positionSRTm  );
            int intvervalo=1;
            time = txt_startHour.getText().toString()+":00";
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
                lstAlarmList.add(alarmDetails);
                //lstAlarmList.add(time.substring(0,5));
                time = hourAdd(time,HH,mm,ss)  ;
            }
            callSetAdapter(lstAlarmList);

        }else if (positionSRTp == 1 ){
            int intvervalo=1;
            Log.i(TAG, Method +  "Intervalos positionSRTm=" + positionSRTm  );
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
                lstAlarmList.add(alarmDetails);
                //lstAlarmList.add(time.substring(0,5));
                time = hourAdd(time,periodo,0,0)  ;
            }
            try{
                callSetAdapter(lstAlarmList);
            }catch (Exception e){e.printStackTrace();}

        }
        Log.i(TAG, Method +  "End..."  );

    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String Method ="[onDateSet]";
        Log.i(TAG, Method + "Init..." );
        String mes = "" + ((monthOfYear + 1) < 10 ? "0" + (monthOfYear + 1) : "" + (monthOfYear + 1));
        String dia = dayOfMonth < 10 ? "0" + dayOfMonth : "" + dayOfMonth;
        String date = "" + dia + "/" + mes + "/" + year;
        this.txt_startDate.setText(date);
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
        this.txt_startHour.setText(time);

        Log.i(TAG, Method + "End..." );

        this.setListAlarm(positionSRTm);
    }

    private void fn_showAlertDialog() {
        String Method ="[fn_showAlertDialog]";
        Log.i(TAG, Method + "Init..."  );
        new AlertDialog.Builder(this)
                .setTitle("Title of your dialog")
                .setMessage("Text that you want to show.")
                .setCancelable(false)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //do your task
                        dialog.cancel();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //do your task
                        dialog.cancel();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
        Log.i(TAG, Method + "End..."  );
    }

    private void callSetAdapter( List<EAlarmDetails> lst ){
        String Method ="[callSetAdapter]";
        Log.i(TAG, Method + "Init..."  );
        if (adapterAlarm != null){
            adapterAlarm.updateData(lst);
        }else{
            adapterAlarm =  new AlarmListReminderTimesCardAdapter(this , lst,  recyclerView);
            recyclerView.setAdapter(adapterAlarm);
            recyclerView.setLayoutManager( new LinearLayoutManager(this) );
        }
        Log.i(TAG, Method + "End..."  );
    }

    private void callCalendar() {
        String Method ="[callCalendar]";
        Log.i(TAG, Method + "Init..."  );
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.setMaxDate(now);
        UcallCalendar(dpd).show(getFragmentManager(), "Datepickerdialog");
        Log.i(TAG, Method + "End..."  );
    }

    private void callCalendarTime(){
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



}
