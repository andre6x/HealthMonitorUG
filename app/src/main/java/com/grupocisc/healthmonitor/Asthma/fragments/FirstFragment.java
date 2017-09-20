package com.grupocisc.healthmonitor.Asthma.fragments;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.grupocisc.healthmonitor.Asthma.activities.PickFlowRegistry;
import com.grupocisc.healthmonitor.Insulin.TextValidator;
import com.grupocisc.healthmonitor.R;
import com.grupocisc.healthmonitor.Utils.Utils;
import com.grupocisc.healthmonitor.entities.ISaveUser;
import com.grupocisc.healthmonitor.login.activities.LoginAccountActivity;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;

import retrofit2.Call;

import static com.grupocisc.healthmonitor.Utils.Utils.isNumeric;


public class FirstFragment extends Fragment  implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    String TAG = "FirstFragment";
    View view;
    private int year, month, day, hour, minute,second;
    private LinearLayout lyt_fecha, lyt_hora;
    private TextView txt_fecha, txt_hora;
    private EditText txt_flujo_maximo;
    private EditText txt_observation;
    public ProgressDialog Dialog;
    private  CardView card_next_guardar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.asthma_first_fragment, container, false);

        Dialog = new ProgressDialog(getActivity());
        txt_observation = (EditText) view.findViewById(R.id.txt_observation);
        lyt_fecha = (LinearLayout) view.findViewById(R.id.lyt_fecha);
        lyt_hora = (LinearLayout) view.findViewById(R.id.lyt_hora);
        txt_fecha = (TextView) view.findViewById(R.id.txt_fecha);
        txt_hora = (TextView) view.findViewById(R.id.txt_hora);
        txt_flujo_maximo=(EditText) view.findViewById(R.id.txt_flujo_maximo);
        card_next_guardar = (CardView) view.findViewById(R.id.card_siguiente);


        inicializarFechaHora();

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

        txt_flujo_maximo.addTextChangedListener(new com.grupocisc.healthmonitor.Complementary.funtion.TextValidator(txt_flujo_maximo) {
            @Override
            public void validate(EditText editText, String text) {
                if (!text.isEmpty() && Utils.isNumeric(text))
                {
                    if (Float.parseFloat(text) > 800 &&  Float.parseFloat(text) < 0 ) {
                        txt_flujo_maximo.setError("Por favor Ingrese, un valor entre 0 y 800 I/min. ");
                    }
                }
            }

        });

        card_next_guardar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                guardarDataAsthma(v);
            }
        });

        return view;
    }

    private void inicializarFechaHora() {

        //obtener fechay hroa de Calendar
        Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH) + 1;
        day = c.get(Calendar.DAY_OF_MONTH);
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);
        //second = c.get(Calendar.SECOND);
        String mes = month < 10 ? "0" + month : "" + month;
        String dia = day < 10 ? "0" + day : "" + day;
        //setear fecha
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
        dpd.show(getActivity().getFragmentManager(), "Datepickerdialog");
    }

    public void callCalendarTime() {
        Calendar now = Calendar.getInstance();
        TimePickerDialog tpd = TimePickerDialog.newInstance(this,
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
        tpd.show(getActivity().getFragmentManager(), "Timepickerdialog");
    }

    @Override
    public void onResume() {
        super.onResume();
        DatePickerDialog dpd = (DatePickerDialog) getActivity().getFragmentManager().findFragmentByTag("Datepickerdialog");
        if (dpd != null)
            dpd.setOnDateSetListener(this);
        TimePickerDialog tpd = (TimePickerDialog) getActivity().getFragmentManager().findFragmentByTag("Timepickerdialog");
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

    private boolean validaRangos(float valor ) {

        if (valor >= 0 & valor <= 250) {
            return true;
        }
        if (valor >= 251 & valor <= 500) {
            return true;
        }
        if (valor >= 501 & valor <= 800) {
            return true;
        }
        return false;

    }

    public void guardarDataAsthma(View v) {
        String fecha = txt_fecha.getText().toString();
        String hora =  txt_hora.getText().toString();
        String flujo_maximo= txt_flujo_maximo.getText().toString();
        String observacion = txt_observation.getText().toString();

        if(!flujo_maximo.isEmpty() && Utils.isNumeric(flujo_maximo) )
        {   if (Float.parseFloat(flujo_maximo) > 800) {
                snackBar(v, "El valor no puede ser mayor a 800 I/min. Por favor Verifique");
             }else{
                //int flujo_ma = Integer.parseInt(flujo_maximo);
                PickFlowRegistry.FlujoMax = Float.parseFloat(flujo_maximo);
                PickFlowRegistry.fecha = fecha;
                PickFlowRegistry.hora = hora;
                PickFlowRegistry.observacion = observacion;
                nextAction();
             }
        } else {
            snackBar(v, "Ingresar valor");

        }
    }

    public void snackBar(View view , String mensaje){
        Snackbar.make(view, "\n" + mensaje, Snackbar.LENGTH_LONG)
                .setAction("Ok", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                })
                .setActionTextColor(this.getResources().getColor(R.color.greenMenu))
                .show();

    }

    public void nextAction(){
        ((PickFlowRegistry)getActivity()).callFragmentSymptomCategory();
    }

}
