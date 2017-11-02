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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.grupocisc.healthmonitor.Doctor.adapters.doctorsListAdapter;
import com.grupocisc.healthmonitor.HealthMonitorApplicattion;
import com.grupocisc.healthmonitor.Home.activities.MainActivity;
import com.grupocisc.healthmonitor.R;
import com.grupocisc.healthmonitor.Utils.Utils;
import com.grupocisc.healthmonitor.entities.IConsulDoctor;
import com.grupocisc.healthmonitor.entities.IConsulDoctorEspeciality;
import com.grupocisc.healthmonitor.entities.IRegistreDoctor;
import com.grupocisc.healthmonitor.entities.ObjDoctorSelect;
import com.grupocisc.healthmonitor.entities.ObjSpeciality;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectedDoctorActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private String respuesta;
    EditText inputSearch;
    private CardView card_vincula; //card_vincular xml
    String TAG = "DoctorActivity";
    public ProgressDialog Dialog;
    private RecyclerView recyclerView;
    doctorsListAdapter adapter;

    private static IConsulDoctorEspeciality.Obj mEspeciality;
    private Call<IConsulDoctorEspeciality.Obj> call_3;

    private static IConsulDoctor.Obj mDoctor;
    private Call<IConsulDoctor.Obj> call_2;


    private IRegistreDoctor.RegistroDoctor mSaveDoctor;
    private Call<IRegistreDoctor.RegistroDoctor> call_1;

    int idDoctor;
    String nombreDoc = "";
    String apellidoDoc = "";
    String emailDoc = "";
    String telefonoDoc = "";
    String especialidadDoc = "";

    Spinner spinnerPais;

    //INCLUDE
    private LinearLayout linear_loading;
    private ProgressBar progress;
    private Button retry;

    String email;
    String sp_SpecialtyId = "";
    int SpecialtyId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctor_selected_activity);
        ButterKnife.bind(this);
        setToolbar();
        Dialog = new ProgressDialog(SelectedDoctorActivity.this);

        card_vincula = (CardView) findViewById(R.id.card_vincular);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        linear_loading = (LinearLayout) findViewById(R.id.linear_loading);
        progress = (ProgressBar) findViewById(R.id.progress);
        retry = (Button) findViewById(R.id.retry);
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (spinnerPais != null) {
                    restartLoading(2);
                } else {
                    restartLoading(3);
                }
            }
        });

        email = Utils.getEmailFromPreference(this);// debe ir correo de usuario paciente seleccionado

        restartLoading(3);


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

            }

            @Override
            public void afterTextChanged(Editable arg0) {
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

    private void restartLoading(int index) {
        if (index == 3) {
            showLoading();
            restartLoadingconsultaEspeciality();
        } else if (index == 2) {
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
                finish();
            }
        });
    }

    private void restartLoadingconsultaEspeciality() {
        Log.e(TAG, "METODO consultadoc ");
        IConsulDoctorEspeciality CunsulParamet = HealthMonitorApplicattion.getApplication().getRestCISCAdapterV2().create(IConsulDoctorEspeciality.class);
        call_3 = CunsulParamet.CunsulParametEspeciality();
        call_3.enqueue(new Callback<IConsulDoctorEspeciality.Obj>() {
            @Override
            public void onResponse(Call<IConsulDoctorEspeciality.Obj> call, Response<IConsulDoctorEspeciality.Obj> response) {
                if (response.isSuccessful()) {
                    Log.e(TAG, "Respuesta exitosa");
                    mEspeciality = null;
                    mEspeciality = response.body();
                    postExecutionEspeciality();
                } else {
                    showRetry();
                    Log.e(TAG, "Error en la petición");
                }
            }

            @Override
            public void onFailure(Call<IConsulDoctorEspeciality.Obj> call, Throwable t) {
                showRetry();
                Utils.generarAlerta(SelectedDoctorActivity.this, getString(R.string.txt_atencion), getString(R.string.text_error_metodo) + " o revise su conexión a internet");
                t.printStackTrace();
                Log.e(TAG, "Error en la petición onFailure ");
            }
        });
    }


    public void postExecutionEspeciality() {
        if (mEspeciality != null) {
            if (mEspeciality.getRows() != null && mEspeciality.getRows().size() > 0) {
                showLayout();
                String[] arrayPaises = getPaises(mEspeciality.getRows());
                setSpinner(arrayPaises);

            } else
                showRetry();
        } else
            showRetry();
    }

    //llenar list string
    private String[] getPaises(List<IConsulDoctorEspeciality.Especiality> rowsPaises) {
        List<String> listPaises = new ArrayList<>();
        for (int i = 0; i < rowsPaises.size(); i++) {
            String nombres = rowsPaises.get(i).getName();
            listPaises.add(nombres);
        }
        String[] array = new String[listPaises.size()];
        int j = 0;
        for (String s : listPaises) {
            array[j++] = s;
        }
        return array;
    }

    public void setSpinner(String[] arrayPaises) {
        spinnerPais = (Spinner) findViewById(R.id.spinnerPais);
        ArrayAdapter<String> spinnerArrayAdapterPais = new ArrayAdapter<String>(this, R.layout.custom_textview_to_spinner, arrayPaises);
        spinnerArrayAdapterPais.setDropDownViewResource(R.layout.custom_textview_to_spinner);
        spinnerPais.setAdapter(spinnerArrayAdapterPais);
        spinnerPais.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                //Toast.makeText(getActivity(), spinnerColorChange.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
                sp_SpecialtyId = spinnerPais.getSelectedItem().toString();
                SpecialtyId = mEspeciality.getRows().get(position).getSpecialtyId();//ID
                Log.e(TAG, "select idPais: " + mEspeciality.getRows().get(position).getSpecialtyId());
                Log.e(TAG, "select idPais: " + SpecialtyId);

                restartLoading(2);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
    }

    private void restartLoadingconsultadoc() {
        Log.e(TAG, "METODO consultadoc ");
        IConsulDoctor CunsulParamet = HealthMonitorApplicattion.getApplication().getRestCISCAdapterV2().create(IConsulDoctor.class);
        call_2 = CunsulParamet.CunsulParamet(new ObjSpeciality(SpecialtyId));
        call_2.enqueue(new Callback<IConsulDoctor.Obj>() {
            @Override
            public void onResponse(Call<IConsulDoctor.Obj> call, Response<IConsulDoctor.Obj> response) {
                if (response.isSuccessful()) {
                    Log.e(TAG, "Respuesta exitosa");
                    mDoctor = null;
                    mDoctor = response.body();
                    postExecutionLogin();
                } else {
                    showRetry();
                    Log.e(TAG, "Error en la petición");
                }
            }

            @Override
            public void onFailure(Call<IConsulDoctor.Obj> call, Throwable t) {
                showRetry();
                Utils.generarAlerta(SelectedDoctorActivity.this, getString(R.string.txt_atencion), getString(R.string.text_error_metodo) + " o revise su conexión a internet");
                t.printStackTrace();
                //    Log.e(TAG, "Error en la petición onFailure ");
            }
        });
    }

    public void postExecutionLogin() {
        showLayout();
        if (mDoctor != null) {
            if (mDoctor.getIdCodResult() == 0) {
                if (mDoctor.getRows() != null && mDoctor.getRows().size() > 0) {
                    showLayout();
                    // List<String> contactsDoctors = getContactsDoctors();
                    adapter = new doctorsListAdapter(this, mDoctor.getRows());
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(this));

                    card_vincula.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.e(TAG, "vincular");
                            int positionSelect = adapter.getRow_index();
                            if (positionSelect != -1) {
                                idDoctor = adapter.getIdDoctor();// rowsIDoctor.get(positionSelect).getIdDoctor();
                                nombreDoc = adapter.getNombreDoc();//rowsIDoctor.get(positionSelect).getNombre();
                                apellidoDoc = adapter.getApellidoDoc();//rowsIDoctor.get(positionSelect).getApellido();
                                emailDoc = adapter.getEmailDoc();//rowsIDoctor.get(positionSelect).getEmail();
                                telefonoDoc = adapter.getTelefonoDoc();
                                especialidadDoc = adapter.getEspecialidadDoc(); //CAMBIO

                                generateAlertDialog(getString(R.string.txt_vincular), "¿Está seguro de vincularse del Doctor " + nombreDoc + " " + apellidoDoc + " ?");

                                Log.e(TAG, "datos select id:" + idDoctor + "; name: " + nombreDoc + "; email: " + emailDoc + " ; Especialidad: " + especialidadDoc);
                            } else
                                Utils.generarAlerta(SelectedDoctorActivity.this, "Alerta", "Por favor, Seleccione Doctor a Vincular");

                        }
                    });
                } else
                    showRetry();

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

    private void restartLoadingEnviarData() {

        Log.e(TAG, "email paciente: " + email);
        Log.e(TAG, "id dr: " + idDoctor);
        IRegistreDoctor RegistreDoctor = HealthMonitorApplicattion.getApplication().getRestCISCAdapterV2().create(IRegistreDoctor.class);
        call_1 = RegistreDoctor.RegDoctor(new ObjDoctorSelect(email, idDoctor));
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
                    Utils.generarAlerta(SelectedDoctorActivity.this, getString(R.string.txt_atencion), getString(R.string.text_error_metodo));
                    Log.e(TAG, "Error en la petición call_1");
                }
            }

            @Override
            public void onFailure(Call<IRegistreDoctor.RegistroDoctor> call, Throwable t) {
                showLayoutDialog();
                Utils.generarAlerta(SelectedDoctorActivity.this, getString(R.string.txt_atencion), getString(R.string.text_error_metodo) + " o revise su conexión a internet");
                t.printStackTrace();
                Log.e(TAG, "Error en la petición");
            }
        });
    }

    public void postExecutionEnviarData() {
        showLayoutDialog();
        if (mSaveDoctor != null) {
            Log.e(TAG, "mSaveDoctor != null ::  " + mSaveDoctor.getIdCodResult());
            if (mSaveDoctor.getIdCodResult() == 0) {
                callActivityDoctorRegistre();
            } else {
                Utils.generarAlerta(SelectedDoctorActivity.this, getString(R.string.txt_atencion), mSaveDoctor.getResultDescription());
            }
        } else {
            Utils.generarAlerta(SelectedDoctorActivity.this, getString(R.string.txt_atencion), getString(R.string.text_error_metodo));
        }
    }

    //metodo se ejecuta despues de guardar en el servidor el emial con su idDoctor
    private void callActivityDoctorRegistre() {
        saveDataDoctorDB();
        finish();
    }

    //GUARDOS DATOS EN LA TABLA BD
    public void saveDataDoctorDB() {
        int idDoc = idDoctor;
        String nombres = nombreDoc;
        String apellidos = apellidoDoc;
        String email = emailDoc;
        String phone = telefonoDoc;
        String especialidad = especialidadDoc;

        try {
            //setear datos al objeto y guardar y BD
            Utils.DbsaveDoctorFromDatabase(idDoc,
                    nombres,
                    apellidos,
                    email,
                    phone,
                    especialidad,
                    HealthMonitorApplicattion.getApplication().getDoctorDao());
        } catch (Exception e) {
            e.printStackTrace();
        }
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





