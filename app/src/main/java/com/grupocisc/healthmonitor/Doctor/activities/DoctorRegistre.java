package com.grupocisc.healthmonitor.Doctor.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.grupocisc.healthmonitor.HealthMonitorApplicattion;
import com.grupocisc.healthmonitor.Home.activities.MainActivity;
import com.grupocisc.healthmonitor.R;
import com.grupocisc.healthmonitor.Utils.SharedPreferencesManager;
import com.grupocisc.healthmonitor.Utils.Utils;
import com.grupocisc.healthmonitor.entities.IDesvinculaDr;
import com.grupocisc.healthmonitor.entities.IDoctor;
import com.grupocisc.healthmonitor.entities.IDoctorVinculado;
import com.grupocisc.healthmonitor.entities.ObjDoctorSelect;

import java.sql.SQLException;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DoctorRegistre extends AppCompatActivity {
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    private CardView cv_desvincula;
    String TAG = "DoctorRegistre";
    public ProgressDialog Dialog;

    private IDesvinculaDr.DesvinculaDoctor mDesvDoctor;
    private Call<IDesvinculaDr.DesvinculaDoctor> call_3;

    String email;

    TextView txt_name;
    TextView txt_last_name;
    TextView txt_email;
    TextView txt_telefono;
    TextView txt_specialty; //cambio

    public String Nombre = "";
    public String Apellido = "";
    public String Email = "";
    public String Telefono = "";
    public String Especialidad = "";//cambio
    int idAuxDoctor, idAuxDoctorBDServer;
    private IDoctor row;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TRANSITIONS
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            TransitionInflater inflater = TransitionInflater.from(this);
            Transition transition = inflater.inflateTransition(R.transition.transitions);
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

        setContentView(R.layout.doctor_registre);
        ButterKnife.bind(this);
        Dialog = new ProgressDialog(this);
        txt_name = (TextView) findViewById(R.id.txt_name);
        txt_last_name = (TextView) findViewById(R.id.txt_last_name);
        txt_email = (TextView) findViewById(R.id.txt_email);
        txt_telefono = (TextView) findViewById(R.id.txt_telefono);
        txt_specialty = (TextView) findViewById(R.id.txt_specialty);//CAMBIO

        //OBTENER DATA DE PREFERENCIA
        //setear texto del layout
        txt_name.setText(Nombre);
        txt_last_name.setText(Apellido);
        txt_email.setText(Email);
        txt_telefono.setText(Telefono);
        txt_specialty.setText(Especialidad);

        cv_desvincula = (CardView) findViewById(R.id.card_desvinvula);
        cv_desvincula.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "cv_desvincula");
                generateAlertDialog(getString(R.string.txt_desvincular), "¿Está seguro de desvincularse del Doctor " + Nombre + " " + Apellido + " ?");


            }
        });
        email = Utils.getEmailFromPreference(this);

        //obtener objeto de la actividad anterior
        if (savedInstanceState != null) {
            row = (IDoctor) savedInstanceState.getSerializable("car");
            actualizarData();
        } else {
            if (getIntent() != null && getIntent().getExtras() != null && getIntent().getExtras().getSerializable("car") != null) {
                row = (IDoctor) getIntent().getExtras().getSerializable("car");
                actualizarData();
            }
        }

        setToolbar();
    }


    public void setToolbar() {
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayShowTitleEnabled(false); //desactivar title
        toolbar.setNavigationIcon(R.mipmap.back); //buton back tollbar
        getSupportActionBar().setTitle(""); //titulo tollbar
        toolbar.setTitleTextColor(getResources().getColor(R.color.white)); //color tollbar title

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)

            finish();
        return super.onOptionsItemSelected(item);
    }

    public void actualizarData() {
        idAuxDoctor = row.getId();
        idAuxDoctorBDServer = row.getDoctorId();
        Log.e(TAG, "ACTULIZAR DATA:" + row.getId());

        txt_name.setText(row.getNombres());
        txt_last_name.setText(row.getApellidos());
        txt_email.setText(row.getMail());
        txt_telefono.setText(row.getPhone());
        txt_specialty.setText(row.getEspecialidad());
    }

    public void generateAlertDialog(String Title, String message) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("" + Title);
        alert.setMessage("" + message);
        alert.setCancelable(false);
        alert.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface alert, int id) {
                DesvinculaDoctor();
            }
        });
        alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
            }
        });
        alert.show();
    }

    private void showLoadingDialog() {
        Dialog.setMessage("Espere un Momento..");
        Dialog.setCancelable(false);
        Dialog.show();
    }

    private void showLayoutDialog() {
        if (Dialog != null)
            Dialog.dismiss();
    }

    private void DesvinculaDoctor() {
        showLoadingDialog();

        Log.e(TAG, "email paciente: " + email);
        IDesvinculaDr DesvDoctor = HealthMonitorApplicattion.getApplication().getRestCISCAdapterV2().create(IDesvinculaDr.class);
        call_3 = DesvDoctor.DesvDoctor(new ObjDoctorSelect(email, idAuxDoctorBDServer));
        call_3.enqueue(new Callback<IDesvinculaDr.DesvinculaDoctor>() {
            @Override
            public void onResponse(Call<IDesvinculaDr.DesvinculaDoctor> call, Response<IDesvinculaDr.DesvinculaDoctor> response) {
                if (response.isSuccessful()) {
                    Log.e(TAG, "Respuesta exitosa call_3");
                    mDesvDoctor = null;
                    mDesvDoctor = response.body();
                    postEnviaData();
                } else {
                    showLayoutDialog();
                    Utils.generarAlerta(DoctorRegistre.this, getString(R.string.txt_atencion), getString(R.string.text_error_metodo));
                    Log.e(TAG, "Error en la petición call_3");
                }
            }

            @Override
            public void onFailure(Call<IDesvinculaDr.DesvinculaDoctor> call, Throwable t) {
                showLayoutDialog();
                Utils.generarAlerta(DoctorRegistre.this, getString(R.string.txt_atencion), getString(R.string.text_error_metodo) + " o revise su conexión a internet");
                t.printStackTrace();
                Log.e(TAG, "Error en la petición onFailure");
            }
        });
    }

    public void postEnviaData() {
        showLayoutDialog();
        if (mDesvDoctor != null) {
            Log.e(TAG, "mDesvDoctor != null ::  " + mDesvDoctor.getIdCodResult());
            if (mDesvDoctor.getIdCodResult() == 0) {
                DeletePreferencesCallMainActivity();
            } else {
                Utils.generarAlerta(DoctorRegistre.this, getString(R.string.txt_atencion), mDesvDoctor.getResultDescription());
            }
        } else {
            Utils.generarAlerta(DoctorRegistre.this, getString(R.string.txt_atencion), getString(R.string.text_error_metodo));
        }
    }

    //eliminar preferencias de usuario
    private void DeletePreferencesCallMainActivity() {
        try {
            Utils.DeleterowDoctor(HealthMonitorApplicattion.getApplication().getDoctorDao(), idAuxDoctor);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //finalizar actividad
        finish();
    }

}
