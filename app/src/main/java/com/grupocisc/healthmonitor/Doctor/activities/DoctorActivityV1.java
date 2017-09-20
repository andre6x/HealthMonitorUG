package com.grupocisc.healthmonitor.Doctor.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.grupocisc.healthmonitor.Doctor.adapters.doctorsListAdapter;
import com.grupocisc.healthmonitor.HealthMonitorApplicattion;
import com.grupocisc.healthmonitor.Home.activities.MainActivity;
import com.grupocisc.healthmonitor.R;
import com.grupocisc.healthmonitor.Utils.SharedPreferencesManager;
import com.grupocisc.healthmonitor.Utils.Utils;
import com.grupocisc.healthmonitor.entities.IConsulDoctor;
import com.grupocisc.healthmonitor.entities.IRegistreDoctor;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DoctorActivityV1 extends AppCompatActivity {
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    private String respuesta;
    EditText inputSearch;
    private CardView card_vincula; //card_vincular xml
    String TAG = "DoctorActivity";
    public ProgressDialog Dialog;
    private RecyclerView recyclerView;
    doctorsListAdapter adapter;

    private static List<IConsulDoctor.doctor> rowsIDoctor;
    private Call<List<IConsulDoctor.doctor>> call_2;


    private IRegistreDoctor.RegistroDoctor mSaveDoctor;
    private Call<IRegistreDoctor.RegistroDoctor> call_1;

    int idDoctor;
    String nombreDoc = "";
    String apellidoDoc = "";
    String emailDoc = "";
    String telefonoDoc = "";
    String especialidadDoc = "";


    //INCLUDE
    private LinearLayout linear_loading;
    private ProgressBar progress;
    private Button retry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctor_selected_activity);
        ButterKnife.bind(this);
        //  Utils.SetStyleToolbarTitle(this, title);
        //setearMaterialBetterSpinner();
        setToolbar();
        card_vincula = (CardView) findViewById(R.id.card_vincular);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        linear_loading = (LinearLayout) findViewById(R.id.linear_loading);
        progress = (ProgressBar) findViewById(R.id.progress);
        retry = (Button) findViewById(R.id.retry);
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                restartLoading(2);
            }
        });

        Dialog = new ProgressDialog(DoctorActivityV1.this);
        // Arreglo de codigos de proyecto.
        restartLoading(2);

        inputSearch = (EditText) findViewById(R.id.edt_buscar);
        /* Activando el filtro de busqueda */
        inputSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub
                Log.e(TAG, "execute inputSearch" + inputSearch.getText().toString());
                String query = inputSearch.getText().toString();
                //DoctorActivity.this.adapter.getFilter().filter(arg0);
                filterNameDoctor(query);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    private void showLayout() {
        linear_loading.setVisibility(View.GONE);
    }

    private void showLoading() {
        linear_loading.setVisibility(View.VISIBLE);
        progress.setVisibility(View.VISIBLE);
        retry.setVisibility(View.GONE);
    }

    private void showRetry() {
        linear_loading.setVisibility(View.VISIBLE);
        progress.setVisibility(View.GONE);
        retry.setVisibility(View.VISIBLE);
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

    private void generarAlerta(String mensaje) {
        Utils.generarAlerta(this, getString(R.string.txt_error), mensaje);
    }

    public void generateAlertDialog(String Title, String message) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("" + Title);
        alert.setMessage("" + message);
        alert.setCancelable(false);
        alert.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface alert, int id) {

                restartLoading(1); //descomentar cuando funcione el metodo

            }
        });
        alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
            }
        });
        alert.show();
    }


    private void restartLoading(int index) {
        if (index == 2) {
            showLoading();
            restartLoadingconsultadoc();
        } else if (index == 1) {
            showLoadingDialog();
            restartLoadingEnviarData();
        }
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
                //volver a crear el main
                Log.e(TAG, "SetOnclick Toolbar");
                Intent intent = new Intent(DoctorActivityV1.this, MainActivity.class);  // envia al main
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP); //para borrar pila de actividades
                startActivity(intent);
            }
        });
    }

    private void restartLoadingconsultadoc() {
        Log.e(TAG, "METODO consultadoc ");
        IConsulDoctor CunsulParamet = HealthMonitorApplicattion.getApplication().getmRestCISCAdapter().create(IConsulDoctor.class);
        //call_2 = CunsulParamet.CunsulParamet();
        call_2.enqueue(new Callback<List<IConsulDoctor.doctor>>() {
            @Override
            public void onResponse(Call<List<IConsulDoctor.doctor>> call, Response<List<IConsulDoctor.doctor>> response) {
                if (response.isSuccessful()) {
                    Log.e(TAG, "Respuesta exitosa");
                    rowsIDoctor = null;
                    rowsIDoctor = response.body();
                    postExecutionLogin();
                } else {
                    showRetry();
                    //Utils.generarAlerta(DoctorActivity.this, getString(R.string.txt_atencion), getString(R.string.text_error_metodo));
                    Log.e(TAG, "Error en la petición");
                }
            }

            @Override
            public void onFailure(Call<List<IConsulDoctor.doctor>> call, Throwable t) {
                showRetry();
                Utils.generarAlerta(DoctorActivityV1.this, getString(R.string.txt_atencion), getString(R.string.text_error_metodo) + " o revise su conexión a internet");
                t.printStackTrace();
                //    Log.e(TAG, "Error en la petición onFailure ");
            }
        });
    }

    public void postExecutionLogin() {
        if (rowsIDoctor != null) {
            if (rowsIDoctor.size() > 0) {
                showLayout();
                // List<String> contactsDoctors = getContactsDoctors();
                adapter = new doctorsListAdapter(this, rowsIDoctor);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));

                card_vincula.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e(TAG, "vincular");
                        int positionSelect = adapter.getRow_index();
                        if (positionSelect != -1) {
//                            idDoctor = rowsIDoctor.get(positionSelect).getIdDoctor();
                            //                          nombreDoc = rowsIDoctor.get(positionSelect).getNombre();
                            //                        apellidoDoc = rowsIDoctor.get(positionSelect).getApellido();
                            //                      emailDoc = rowsIDoctor.get(positionSelect).getEmail();

                            idDoctor = adapter.getIdDoctor();// rowsIDoctor.get(positionSelect).getIdDoctor();
                            nombreDoc = adapter.getNombreDoc();//rowsIDoctor.get(positionSelect).getNombre();
                            apellidoDoc = adapter.getApellidoDoc();//rowsIDoctor.get(positionSelect).getApellido();
                            emailDoc = adapter.getEmailDoc();//rowsIDoctor.get(positionSelect).getEmail();
                            telefonoDoc = adapter.getTelefonoDoc();
                            especialidadDoc = adapter.getEspecialidadDoc(); //CAMBIO

                            generateAlertDialog(getString(R.string.txt_vincular), "¿Está seguro de vincularse del Doctor " + nombreDoc + " " + apellidoDoc + " ?");

                            Log.e(TAG, "datos select id:" + idDoctor + "; name: " + nombreDoc + "; email: " + emailDoc + " ; Especialidad: " + especialidadDoc);
                        } else
                            generarAlerta("Por favor, Seleccione Doctor a Vincular");

                    }
                });


            } else
                showRetry();
        } else
            showRetry();
    }

    public void filterNameDoctor(String query) {
        if (adapter != null) {
            adapter.filterUpdate(query);
        }
    }


    private void restartLoadingEnviarData() {
        String email = Utils.getEmailFromPreference(this);// debe ir correo de usuario paciente seleccionado
        Log.e(TAG, "email paciente: " + email);
        Log.e(TAG, "id dr: " + idDoctor);
        //enviar webservice
        //APUNTANDO AA METODO CISC
        IRegistreDoctor RegistreDoctor = HealthMonitorApplicattion.getApplication().getmRestCISCAdapter().create(IRegistreDoctor.class);

        //call_1 = RegistreDoctor.RegDoctor(email, idDoctor);
        call_1.enqueue(new Callback<IRegistreDoctor.RegistroDoctor>() {
            @Override
            public void onResponse(Call<IRegistreDoctor.RegistroDoctor> call, Response<IRegistreDoctor.RegistroDoctor> response) {
                if (response.isSuccessful()) {
                    Log.e(TAG, "Respuesta exitosa call_1");
                    mSaveDoctor = null;
                    mSaveDoctor = response.body();
                    postExecutionEnviarData();
                } else {
                    showLayoutDialog();
                    Utils.generarAlerta(DoctorActivityV1.this, getString(R.string.txt_atencion), getString(R.string.text_error_metodo));
                    Log.e(TAG, "Error en la petición call_1");
                }
            }

            @Override
            public void onFailure(Call<IRegistreDoctor.RegistroDoctor> call, Throwable t) {
                showLayoutDialog();
                Utils.generarAlerta(DoctorActivityV1.this, getString(R.string.txt_atencion), getString(R.string.text_error_metodo) + " o revise su conexión a internet");
                t.printStackTrace();
                Log.e(TAG, "Error en la petición");
            }
        });
    }

    public void postExecutionEnviarData() {

    }

    //GUARDOS DATOS EN LA TABLA BD
  /*  public void saveDataDoctorDB() {
        String nombres = nombreDoc;
        String apellidos = apellidoDoc;
        String email = emailDoc;

        try {
            //setear datos al objeto y guardar y BD
            Utils.DbsaveDoctorFromDatabase(
                    nombres,
                    apellidos,
                    email,
                    HealthMonitorApplicattion.getApplication().getDoctorDao());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }*/

    //datos de usuario obtenedis del webservice y guardar en preferencias
    private void SavePreferencesUserDoctor() {

    }

    //metodo se ejecuta despues de guardar en el servidor el emial con su idDoctor
    private void callActivityDoctorRegistre() {
        //saveDataDoctorDB();
        SavePreferencesUserDoctor();
        Intent i = new Intent(DoctorActivityV1.this, DoctorRegistre.class);
        startActivity(i);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (Dialog != null) {
            Dialog.dismiss();
            Dialog = null;
        }
        if (call_1 != null && !call_1.isCanceled()) {
            call_1.cancel();
        }
        if (call_2 != null && !call_2.isCanceled()) {
            call_2.cancel();
        }

    }

}





