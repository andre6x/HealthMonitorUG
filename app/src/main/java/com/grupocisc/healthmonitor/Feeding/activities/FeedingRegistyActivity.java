package com.grupocisc.healthmonitor.Feeding.activities;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.grupocisc.healthmonitor.HealthMonitorApplicattion;
import com.grupocisc.healthmonitor.R;
import com.grupocisc.healthmonitor.Utils.Utils;
import com.grupocisc.healthmonitor.entities.IFeeding;
import com.grupocisc.healthmonitor.entities.IRegistreAlimento;
import com.satsuware.usefulviews.LabelledSpinner;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Walter on 31/01/2017.
 */

public class FeedingRegistyActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener,
        LabelledSpinner.OnItemChosenListener {

    private String TAG = "FeedingRegistyActivity";
    private int year, month, day, hour, minute;
    private int code = 0;
    private LinearLayout lyt_fecha, lyt_hora;
    private TextView txt_fecha, txt_hora;
    private EditText txt_alimento, txt_porcion, txt_carbohidrato, txt_proteina, txt_grasa, txt_caloria;
    private FloatingActionButton fab;
    private String selectTextSpinner;
    private Call<IRegistreAlimento.RegistroAlimento> alimentoCall;
    private IRegistreAlimento.RegistroAlimento registroAlimento;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feeding_registry_activity);

        txt_alimento = (EditText) findViewById(R.id.txt_alimento);
        lyt_fecha = (LinearLayout) findViewById(R.id.lyt_fecha);
        txt_fecha = (TextView) findViewById(R.id.txt_fecha);
        lyt_hora = (LinearLayout) findViewById(R.id.lyt_hora);
        txt_hora = (TextView) findViewById(R.id.txt_hora);
        txt_porcion = (EditText) findViewById(R.id.txt_porcion);
        txt_carbohidrato = (EditText) findViewById(R.id.txt_carbohidrato);
        txt_proteina = (EditText) findViewById(R.id.txt_proteina);
        txt_grasa = (EditText) findViewById(R.id.txt_grasa);
        txt_caloria = (EditText) findViewById(R.id.txt_caloria);
        setToolbar();
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

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertAlimento(view);
            }
        });

        setearItemsSpinner();
        selectDataFeedingDB();
    }

    public void callCalendarTime() {
        Calendar now = Calendar.getInstance();
        TimePickerDialog tpd = TimePickerDialog.newInstance(
                this,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                true
        );
        tpd.setThemeDark(false);
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
        if (false) {
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

    private void inicializarFechaHora() {
        Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);

        String mes = "" + ((month + 1) < 10 ? "0" + (month + 1) : "" + (month + 1));
        String dia = day < 10 ? "0" + day : "" + day;
        String date = "" + dia + "/" + mes + "/" + year;

        txt_fecha.setText(date);
        String hourString = hour < 10 ? "0" + hour : "" + hour;
        String minuteString = minute < 10 ? "0" + minute : "" + minute;
        String time = "" + hourString + ":" + minuteString;
        txt_hora.setText(time);
    }

    public void callCalendar() {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.setThemeDark(false);
        dpd.vibrate(true);
        dpd.dismissOnPause(false);
        dpd.showYearPickerFirst(false);
        dpd.setVersion(false ? DatePickerDialog.Version.VERSION_2 : DatePickerDialog.Version.VERSION_1);//false aparece v1 true v2
        if (false) {
            dpd.setAccentColor(Color.parseColor("#9C27B0"));
        }
        if (false) {
            dpd.setTitle("DatePicker Title");
        }
        if (false) {
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

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String mes = "" + ((monthOfYear + 1) < 10 ? "0" + (monthOfYear + 1) : "" + (monthOfYear + 1));
        String dia = dayOfMonth < 10 ? "0" + dayOfMonth : "" + dayOfMonth;
        String date = "" + dia + "/" + mes + "/" + year;
        txt_fecha.setText(date);
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        String hourString = hourOfDay < 10 ? "0" + hourOfDay : "" + hourOfDay;
        String minuteString = minute < 10 ? "0" + minute : "" + minute;
        String secondString = second < 10 ? "0" + second : "" + second;
        String time = "" + hourString + ":" + minuteString;
        txt_hora.setText(time);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    private static List<IFeeding> rowsIFeeding;

    public void selectDataFeedingDB() {
        try {
            if (Utils.GetFeedingFromDatabase(HealthMonitorApplicattion.getApplication().getIFeedingDao()).size() > 0) {
                rowsIFeeding = Utils.GetFeedingFromDatabase(HealthMonitorApplicattion.getApplication().getIFeedingDao());
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onItemChosen(View labelledSpinner, AdapterView<?> adapterView, View itemView, int position, long id) {
        String selectedText = adapterView.getItemAtPosition(position).toString();
        switch (labelledSpinner.getId()) {
            case R.id.spinner_porcion:
                selectTextSpinner = selectedText;
                break;
        }
    }

    @Override
    public void onNothingChosen(View labelledSpinner, AdapterView<?> adapterView) {

    }

    public void setearItemsSpinner() {
        LabelledSpinner labelledSpinner = (LabelledSpinner) findViewById(R.id.spinner_porcion);
        labelledSpinner.setColor(R.color.colorPrimaryDark);
        labelledSpinner.setItemsArray(R.array.feeding_porcion_array);
        labelledSpinner.setDefaultErrorEnabled(true);
        labelledSpinner.setDefaultErrorText("Este es un campo obligatorio.");
        labelledSpinner.setOnItemChosenListener(this);
        labelledSpinner.setSelection(1, true);
    }

    private void insertAlimento(View view) {
        final String OLD_FORMAT = "dd/MM/yyyy";
        final String NEW_FORMAT = "yyyy/MM/dd";

        String oldDateString = txt_fecha.getText().toString();
        String newDateString = oldDateString;

        try {
            SimpleDateFormat sdf = new SimpleDateFormat(OLD_FORMAT);
            Date d = sdf.parse(oldDateString);
            sdf.applyPattern(NEW_FORMAT);
            newDateString = sdf.format(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String email = Utils.getEmailFromPreference(this);
        String alimento = txt_alimento.getText().toString();
        String hora = txt_hora.getText().toString();

        float porcion = 0.0f;
        float carbohidrato = 0.0f;
        float proteina = 0.0f;
        float grasa = 0.0f;
        float caloria = 0.0f;

        if (alimento.length() <= 0) {
            txt_alimento.setError("Ingrese un alimento válido");
            return;
        }

        if ("-1".equals(validateEditText(txt_porcion))) {
            txt_porcion.setError("Ingrese un valor de porción válido");
            return;
        } else {
            porcion = Float.parseFloat(txt_porcion.getText().toString());
        }
        if ("-1".equals(validateEditText(txt_carbohidrato))) {
            txt_carbohidrato.setError("Ingrese un valor de carbohidratos válido");
            return;
        } else {
            carbohidrato = Float.parseFloat(txt_carbohidrato.getText().toString());
        }
        if ("-1".equals(validateEditText(txt_proteina))) {
            txt_proteina.setError("Ingrese un valor de proteína válido");
            return;
        } else {
            proteina = Float.parseFloat(txt_proteina.getText().toString());
        }
        if ("-1".equals(validateEditText(txt_caloria))) {
            txt_caloria.setError("Ingrese un valor de caloría válido");
            return;
        } else {
            caloria = Float.parseFloat(txt_caloria.getText().toString());
        }
        if ("-1".equals(validateEditText(txt_grasa))) {
            txt_grasa.setError("Ingrese un valor de grasa válido");
            return;
        } else {
            grasa = Float.parseFloat(txt_grasa.getText().toString());
        }

        Log.e(TAG, "?email"+ email + "&descripcion" +alimento +"&porcion"+ porcion +"&calorias"+ caloria +"&proteinas"+ proteina +"&grasas"+ grasa +"&carbohidratos"+ carbohidrato +"&fecha"+ newDateString + " " + hora );

        IRegistreAlimento iRegistreAlimento = HealthMonitorApplicattion.getApplication().getRetrofitAdapter().create(IRegistreAlimento.class);
        alimentoCall = iRegistreAlimento.putAlimento(email, alimento, porcion, caloria, proteina, grasa, carbohidrato, newDateString + " " + hora);
        alimentoCall.enqueue(new Callback<IRegistreAlimento.RegistroAlimento>() {
            @Override
            public void onResponse(Call<IRegistreAlimento.RegistroAlimento> call, Response<IRegistreAlimento.RegistroAlimento> response) {
                if (response.isSuccessful()) {
                    registroAlimento = null;
                    registroAlimento = response.body();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<IRegistreAlimento.RegistroAlimento> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public String validateEditText(EditText editText) {
        String valor = "0";
        if (editText.getText().toString().length() > 0) {
            if (".".equals(editText.getText().toString())) {
                valor = "-1";
            } else {
                if (Float.parseFloat(editText.getText().toString()) < 0 || Float.parseFloat(editText.getText().toString()) > 100000) {
                    valor = "-1";
                } else {
                    valor = editText.getText().toString();
                }
            }
        } else {
            valor = "-1";
        }
        return valor;
    }

    public void setToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(R.mipmap.back);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
    }
}
