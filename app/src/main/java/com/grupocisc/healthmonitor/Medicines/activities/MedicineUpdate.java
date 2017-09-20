package com.grupocisc.healthmonitor.Medicines.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.grupocisc.healthmonitor.HealthMonitorApplicattion;
import com.grupocisc.healthmonitor.Insulin.TextValidator;
import com.grupocisc.healthmonitor.R;
import com.grupocisc.healthmonitor.Utils.Utils;
import com.grupocisc.healthmonitor.entities.IRegCrtMedicamentos;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.grupocisc.healthmonitor.Utils.Utils.isOnline;
import static com.grupocisc.healthmonitor.Utils.Utils.UcallCalendar;
import static com.grupocisc.healthmonitor.Utils.Utils.UcallCalendarTime;
import static com.grupocisc.healthmonitor.Utils.Utils.isNumeric;

public class MedicineUpdate extends AppCompatActivity implements DatePickerDialog.OnDateSetListener
        , TimePickerDialog.OnTimeSetListener {

    int idMed = 0;

    @Bind(R.id.NombreUpdate) TextView nombre;
    @Bind(R.id.IdMed) TextView idMedicamento;
    @Bind(R.id.updt_dosis) EditText txt_dosis ;
    @Bind(R.id.updt_vecesDia) EditText txt_veces ;
    @Bind(R.id.updt_fecha) TextView txt_fecha;
    @Bind(R.id.lyt_fecha) LinearLayout lyt_fecha;
    @Bind(R.id.updt_hora)TextView txt_hora;
    @Bind(R.id.lyt_hora) LinearLayout lyt_hora;
    @Bind(R.id.updt_obs) EditText txt_observacion;
    @Bind(R.id.btnSave) Button actualizar ;
    @Bind(R.id.btnClose) Button salir ;

    private Toolbar toolbar;
    public ProgressDialog Dialog;

    IRegCrtMedicamentos.ActCtrlMedicamento mSaveCrtRMedicines;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.medicine_update_activity);
        ButterKnife.bind(this);

        txt_dosis.addTextChangedListener(new TextValidator(txt_dosis) {
            @Override
            public void validate(EditText editText, String text) {
                if(!text.isEmpty() && Utils.isNumeric(text))
                {
                    if( Integer.parseInt(text) == 0 ){
                        txt_dosis.setError( "No puede ser 0" );
                    }
                }
            }
        });

        txt_veces.addTextChangedListener(new TextValidator(txt_veces) {
            @Override
            public void validate(EditText editText, String text) {
                if(!text.isEmpty() && Utils.isNumeric(text))
                {
                    if( Float.parseFloat(text) == 0 ){
                        txt_veces.setError( "No puede ser 0" );
                    }
                }
            }
        });
        Dialog = new ProgressDialog(getBaseContext());


        /*Para el Update de datos*/
        Bundle datos = this.getIntent().getExtras();
        if (datos != null) {
            txt_dosis.setText(datos.getInt("Dosis")+"");
            txt_veces.setText(datos.getInt("Veces")+"");
            txt_fecha.setText(datos.getString("fecha"));
            txt_hora.setText(datos.getString("hora"));
            txt_observacion.setText(datos.getString("Obs"));
            nombre.setText(datos.getString("Nombre"));
            idMedicamento.setText(datos.getInt("Id")+"");
            idMed = Integer.parseInt(idMedicamento.getText().toString());

            Log.e("Update","Intents");
            Log.e("Update",idMed + "");
        }

        lyt_fecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callCalendar();
            }
        });

        lyt_hora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callCalendarTime();
            }
        });

        actualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(idMed != 0)
                    if(isOnline(MedicineUpdate.this))
                        saveDataMedicinDB(view);
                    else
                        Toast.makeText(MedicineUpdate.this ,"Compruebe conexión a Internet", Toast.LENGTH_SHORT).show();

            }
        });
        salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        setToolbar();
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

    public void setToolbar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false); //desactivar title
        toolbar.setNavigationIcon(R.mipmap.back); //buton back tollbar
        toolbar.setTitleTextColor(getResources().getColor(R.color.white)); //color tollbar title

    }


    //Se ejecuta al dar ACeptar al dialogo
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
        String time = "" + hourString + ":" + minuteString;
        txt_hora.setText(time);
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
        Calendar now = Calendar.getInstance();
        TimePickerDialog tpd = TimePickerDialog.newInstance(
                this,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                true /*mode24Hours.isChecked()*/
        );
        UcallCalendarTime(tpd).show(getFragmentManager(), "Timepickerdialog");
    }

    //GUARDOS DATOS EN LA TABLA BD
    public void saveDataMedicinDB(View view) {
        final int dosis = isNumeric(txt_dosis.getText().toString()) == true? Integer .parseInt(txt_dosis.getText().toString()) : 0;
        final String fecha = txt_fecha.getText().toString().substring(6, 10) + "/" + txt_fecha.getText().toString().substring(3, 5) + "/" + txt_fecha.getText().toString().substring(0, 2) + " " + txt_hora.getText().toString(); ;
        final int veces = isNumeric(txt_veces.getText().toString())== true ? Integer.parseInt(txt_veces.getText().toString()) : 0;
        final String obs = txt_observacion.getText().toString();
        if (dosis > 0 && veces > 0) {
           /*Manda a guardar el registro al confirmar*/
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("¿Desea actualizar el registro?")
                    .setTitle("Guardar")
                    .setPositiveButton("ACEPTAR",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    IRegCrtMedicamentos regCtrlMed = HealthMonitorApplicattion.getApplication().getmRestCISCAdapterP().create(IRegCrtMedicamentos.class);
                                    Call<IRegCrtMedicamentos.ActCtrlMedicamento> medicacionCall = regCtrlMed.ACTUALIZA_CTRL_MED_CALL(idMed, dosis,veces, fecha, obs);
                                    medicacionCall.enqueue(new Callback<IRegCrtMedicamentos.ActCtrlMedicamento>() {
                                        @Override
                                        public void onResponse(Call<IRegCrtMedicamentos.ActCtrlMedicamento> call, Response<IRegCrtMedicamentos.ActCtrlMedicamento> response) {
                                            if (response.isSuccessful()) {
                                                mSaveCrtRMedicines = response.body();
                                                postExecutionEnviarData();

                                            } else {
                                                //showLayoutDialog();
                                                Utils.generarAlerta(getBaseContext(), getString(R.string.txt_atencion), getString(R.string.text_error_metodo));
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<IRegCrtMedicamentos.ActCtrlMedicamento> call, Throwable t) {
                                            Utils.generarAlerta(getBaseContext(), getString(R.string.txt_atencion), getString(R.string.text_error_metodo));

                                        }
                                    });
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
    }
    public void postExecutionEnviarData(){
        showLayoutDialog();
        if (mSaveCrtRMedicines != null) {
            if (mSaveCrtRMedicines.getCodigo() == 0 ) {
                Utils.generarAlerta(this, getString(R.string.txt_atencion), mSaveCrtRMedicines.getRespuesta() );
                finish();
            }else {
                Utils.generarAlerta(this, getString(R.string.txt_atencion), mSaveCrtRMedicines.getRespuesta() );
            }
        }else {
            Utils.generarAlerta(this, getString(R.string.txt_atencion), getString(R.string.text_error_metodo));
        }
    }
    private void showLayoutDialog(){
        if (Dialog != null)
            Dialog.dismiss();
    }

}
