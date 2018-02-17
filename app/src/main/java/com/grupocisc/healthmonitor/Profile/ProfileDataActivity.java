package com.grupocisc.healthmonitor.Profile;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.grupocisc.healthmonitor.HealthMonitorApplicattion;
import com.grupocisc.healthmonitor.Home.activities.MainActivity;
import com.grupocisc.healthmonitor.R;
import com.grupocisc.healthmonitor.Services.AssistantService;
import com.grupocisc.healthmonitor.Services.BarometerService;
import com.grupocisc.healthmonitor.Utils.NotificationHelper;
import com.grupocisc.healthmonitor.Utils.SharedPreferencesManager;
import com.grupocisc.healthmonitor.Utils.Utils;
import com.grupocisc.healthmonitor.entities.IProfileData;
import com.grupocisc.healthmonitor.entities.ProfileData;
import com.grupocisc.healthmonitor.entities.UpdateProfileResult;
import com.grupocisc.healthmonitor.login.activities.LoginBackPassword;
import com.squareup.picasso.Picasso;

import java.sql.SQLException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProfileDataActivity extends AppCompatActivity {

    static final String TAG = "ProfileDataActivity";
    public ProgressDialog Dialog;

    @BindView(R.id.et_name) EditText txt_name;
    @BindView(R.id.et_last_name) EditText txt_last_name;
    @BindView(R.id.txt_email) EditText txt_email;
    @BindView(R.id.txt_sexo) Spinner txt_sexo;
    @BindView(R.id.spinnerDiabetes) Spinner spinnerDiabetes;
    @BindView(R.id.txt_fecha) TextView txt_fecha;
    @BindView(R.id.txt_altura) EditText txt_altura;
    @BindView(R.id.txt_peso) TextView txt_peso;
    @BindView(R.id.chkAsma) CheckBox chk_tipo_asma;
    @BindView(R.id.txt_estcivil) Spinner txt_estcivil;
    @BindView(R.id.txt_telefono) EditText txt_telefono;
    @BindView(R.id.txt_pais) TextView txt_pais;
    @BindView(R.id.editionBtn) ImageView editionButton;
    @BindView(R.id.card_change_pass) CardView card_change_pass;
    @BindView(R.id.card_update_data) CardView card_update_data;

    private static final int DATE_DIALOG_ID = 1;
    private int _year;
    private int month;
    private int day;
    private String currentDate;

    boolean _isEnabled=false;
    public String name = "";
    public String _lastName = "";
    public String Email = "";
    public String year = "";
    public String Peso = "";
    public String TipoDiabetesP = "";
    public String TipoAsma = "";
    public String Altura = "";
    public String Sexo = "";
    public String EstCivil = "";
    public String Telefono = "";
    public String Pais = "";
    private String enviaSexo = "";
    private ImageView user_avatar;

    Call<UpdateProfileResult> _updateRequest;
    UpdateProfileResult updateProfileResult;

    //String  tdiabetes="";
    int idTipoDiabetes=0;
    Integer hasAsthma;
    //Spinner spinnerDiabetes;
    /*String[] diabetesType = new String[]{
            "Tipo 1",
            "Tipo 2",
            "Gestacional",
            "Sin diabetes"
    };*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_datata_activity);

        ButterKnife.bind(this);

        Dialog = new ProgressDialog(this);

        Utils.SetStyleToolbarLogo(this);
        //setea el color de la imagen
        ImageView perfil_user = (ImageView) findViewById(R.id.img_icon);
        perfil_user.setColorFilter(perfil_user.getContext().getResources().getColor(R.color.btn_login), PorterDuff.Mode.SRC_ATOP);

        txt_sexo.setEnabled(_isEnabled);
        txt_estcivil.setEnabled(_isEnabled);
        spinnerDiabetes.setEnabled(_isEnabled);


        //OBTENER DATA DE PREFERENCIA
        if (Utils.getEmailFromPreference(this) != null)
            Email = Utils.getEmailFromPreference(this);
        if (Utils.getNombreFromPreference(this) != null)
            name = Utils.getNombreFromPreference(this);
        if (Utils.getApellidoFromPreference(this) != null)
            _lastName = Utils.getApellidoFromPreference(this);
        if (Utils.getAnioFromPreference(this) != null)
            year = Utils.getAnioFromPreference(this);
        if (Utils.getPesoFromPreference(this) != null)
            Peso = Utils.getPesoFromPreference(this);
        if (Utils.getTipoDiabetesFromPreference(this) != null)
            TipoDiabetesP = Utils.getTipoDiabetesFromPreference(this);
        if (Utils.getTipoAsmaFromPreference(this) != null)
            TipoAsma = Utils.getTipoAsmaFromPreference(this);
        if (Utils.getAlturaFromPreference(this) != null)
            Altura = Utils.getAlturaFromPreference(this);
        if (Utils.getSexoFromPreference(this) != null)
            Sexo = Utils.getSexoFromPreference(this);
        if (Utils.getEstCivilFromPreference(this) != null)
            EstCivil = Utils.getEstCivilFromPreference(this);
        if (Utils.getPaisFromPreference(this) != null)
            Pais = Utils.getPaisFromPreference(this);
        if (Utils.getTelefonoFromPreference(this) != null)
            Telefono = Utils.getTelefonoFromPreference(this);

        if (Utils.getPictureUriFromPreference(this) != null && !Utils.getPictureUriFromPreference(this).equals("")) {
            Picasso.with(this).load(Utils.getPictureUriFromPreference(this)).into(user_avatar);
        }

        if (Utils.getEmailFromPreference(this) != null) {
            //setear texto del layout
            String dateValues = year.substring(2, 3);
            if (dateValues.equals("/")) {
                txt_fecha.setText(year);
            } else {
                //String date = year.substring(8, 10) + "/" + year.substring(5, 7) + "/" + year.substring(0, 4);
                String date = year;
                _year = Integer.parseInt(year.substring(0, 4));
                month = Integer.parseInt(year.substring(5, 7));
                day = Integer.parseInt(year.substring(8, 10));
                txt_fecha.setText(date);
            }

            String sex = Sexo;
            if (sex.equals("M")) {
                Sexo = "MASCULINO";
                txt_sexo.setSelection(0);
            } else {
                Sexo = "FEMENINO";
                txt_sexo.setSelection(1);
            }
            txt_name.setText(name);
            txt_last_name.setText(_lastName);
            txt_email.setText(Email);
            txt_altura.setText(Altura);
            txt_peso.setText(Peso + " kg");
            chk_tipo_asma.setChecked(false);

            if(TipoAsma.equals("1") || TipoAsma.equals("2"))
                chk_tipo_asma.setChecked(true);
            else
                chk_tipo_asma.setChecked(false);

            if(EstCivil.equals("Soltero")){
                txt_estcivil.setSelection(0);
            }else if(EstCivil.equals("Casado")){
                txt_estcivil.setSelection(1);
            }else if(EstCivil.equals("Viudo")){
                txt_estcivil.setSelection(2);
            }else if(EstCivil.equals("Divorciado")){
                txt_estcivil.setSelection(3);
            }else if(EstCivil.equals("En Unión de Hechos")){
                txt_estcivil.setSelection(4);
            }
            txt_telefono.setText(Telefono);
            txt_pais.setText(Pais);
        }

        //setSpinner();

        if(TipoDiabetesP.equals("11")){
            spinnerDiabetes.setSelection(0);
        }else if(TipoDiabetesP.equals("12")){
            spinnerDiabetes.setSelection(1);
        }else if(TipoDiabetesP.equals("13")){
            spinnerDiabetes.setSelection(2);
        }else if(TipoDiabetesP.equals("14")){
            spinnerDiabetes.setSelection(3);
        }

        txt_fecha.setOnClickListener(view -> {
            if(_isEnabled){
                showDialog(DATE_DIALOG_ID);
            }
        });
    }

    //se ejecuta al seleccionar el icon back del toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            this.finish();
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.card_change_pass)
    public void callChangePass() {
        Intent i = new Intent(ProfileDataActivity.this, LoginBackPassword.class);
        startActivity(i);
    }

    @OnClick(R.id.card_update_data)
    public void actualizaDatos(){
        String msg = "";
        if(this.txt_email.getText().toString().trim().isEmpty()){
            msg = "Debe proporcionar una dirección email como cuenta de usuario";
            Utils.generarSweetAlertDialogError(ProfileDataActivity.this, "HealthMonitorUG", msg);
        }else if(this.txt_altura.getText().toString().trim().isEmpty()){
            msg = "Debe proporcionar su dato de altura";
            Utils.generarSweetAlertDialogError(ProfileDataActivity.this, "HealthMonitorUG", msg);
        }else if(this.txt_telefono.getText().toString().trim().isEmpty()){
            msg = "Debe proporcionar un número de teléfono o celular";
            Utils.generarSweetAlertDialogError(ProfileDataActivity.this, "HealthMonitorUG", msg);
        }else{
            getIdSexo();
            getIdTipoDiabetes();
            getHasAsthma();
            getCivilState();

            IProfileData _data = HealthMonitorApplicattion.getApplication().getRetrofitAdapter().create(IProfileData.class);
            ProfileData profileData = new ProfileData(txt_email.getText().toString(),txt_name.getText().toString(),txt_last_name.getText().toString(),txt_fecha.getText().toString().replace('/','-'),enviaSexo,EstCivil,txt_telefono.getText().toString(),25,1,Float.parseFloat(txt_altura.getText().toString()), idTipoDiabetes, hasAsthma);
            _updateRequest = _data.updateProfileData(profileData);
            _updateRequest.enqueue(new Callback<UpdateProfileResult>() {
                @Override
                public void onResponse(Call<UpdateProfileResult> call, Response<UpdateProfileResult> response) {
                    if(response.isSuccessful()){
                        updateProfileResult =null;
                        updateProfileResult = response.body();
                        postEnviaData();
                    }else {
                        showLayoutDialog();
                        Utils.generarAlerta(ProfileDataActivity.this, getString(R.string.txt_atencion), getString(R.string.text_error_metodo));
                        Log.e(TAG, "Error en la petición actualizacion de datos");
                    }
                }

                @Override
                public void onFailure(Call<UpdateProfileResult> call, Throwable t) {
                    showLayoutDialog();
                    Utils.generarAlerta(ProfileDataActivity.this, getString(R.string.txt_atencion), getString(R.string.text_error_metodo) + " o revise su conexión a internet");
                    t.printStackTrace();
                    Log.e(TAG, "Error en la petición onFailure");
                }
            });

            //Variables para enviarla al sevidor
            /*
            Email --> txt_email.getText().toString().trim()
            Sexo --> enviaSexo
            Altura --> txt_altura.getText().toString().trim()
            Tipo de diabetes --> idTipoDiabetes
            Asma --> tieneAsma
            Estado civil --> txt_estcivil.getSelectedItem().toString().trim()
            Telefono --> txt_telefono.getText().toString().trim()
            */
            //Utils.generarAlerta(ProfileDataActivity.this, "HealthMonitorUG", "Datos actualizados");
        }
    }

    private void showLayoutDialog() {
        if (Dialog != null)
            Dialog.dismiss();
    }

    private void getIdTipoDiabetes(){
        if(this.spinnerDiabetes.getSelectedItemPosition() == 0) {
            idTipoDiabetes = 11;
        }else if(this.spinnerDiabetes.getSelectedItemPosition() == 1) {
            idTipoDiabetes = 12;
        }
        else if(this.spinnerDiabetes.getSelectedItemPosition() == 2) {
            idTipoDiabetes = 13;
        }
        else { //Sin diabetes
            idTipoDiabetes = 14;
        }
    }

    void getCivilState(){
        if(txt_estcivil.getSelectedItemPosition()==0){
            EstCivil = "SOLTERO";
        }
        else if(txt_estcivil.getSelectedItemPosition()==1){
            EstCivil = "CASADO";
        }
        else if(txt_estcivil.getSelectedItemPosition()==2){
            EstCivil = "VIUDO";
        }
        else if(txt_estcivil.getSelectedItemPosition()==3){
            EstCivil = "DIVORCIADO";
        }
        else {
            EstCivil = "En Unión de Hechos";
        }
    }

    private void getIdSexo(){
        if(this.txt_sexo.getSelectedItemPosition()==0){
            enviaSexo ="M";
        }else{
            enviaSexo = "F";
        }
    }

    private void getHasAsthma(){
        if(this.chk_tipo_asma.isChecked()){
            hasAsthma = 2;
        }else{
            hasAsthma = null;
        }
    }

    //@SuppressLint("ResourceAsColor")

    @OnClick(R.id.editionBtn)
    public void enableFields(){
        _isEnabled = !_isEnabled;
        txt_name.setEnabled(_isEnabled);
        txt_last_name.setEnabled(_isEnabled);
        //txt_email.setEnabled(_isEnabled);
        txt_altura.setEnabled(_isEnabled);
        txt_telefono.setEnabled(_isEnabled);
        txt_estcivil.setEnabled(_isEnabled);
        spinnerDiabetes.setEnabled(_isEnabled);
        chk_tipo_asma.setEnabled(_isEnabled);

        if(_isEnabled){
            //txt_email.setBackgroundResource(R.color.silver_fondo);
            txt_name.setBackgroundResource(R.color.silver_fondo);
            txt_last_name.setBackgroundResource(R.color.silver_fondo);
            txt_altura.setBackgroundResource(R.color.silver_fondo);
            txt_telefono.setBackgroundResource(R.color.silver_fondo);
        }
        else {
            //txt_email.setBackgroundResource(R.color.transparent);
            txt_name.setBackgroundResource(R.color.transparent);
            txt_last_name.setBackgroundResource(R.color.transparent);
            txt_altura.setBackgroundResource(R.color.transparent);
            txt_telefono.setBackgroundResource(R.color.transparent);
        }
    }

//    void ShowDatePickerDialog(){
//        new DatePickerDialog(this).show();
//    }

    @Override
    protected android.app.Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this, myDateSetListener, _year, month, day);
        }
        return null;
    }

    private void updateDisplay() {

        //currentDate = year.substring(0, 4)+ "-" + year.substring(5, 7)+ "-" +year.substring(8, 10);
        currentDate = new StringBuilder().append(_year).append("-")
                .append(month + 1).append("-").append(day).toString();

        Log.i("DATE", currentDate);
    }

    DatePickerDialog.OnDateSetListener myDateSetListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker datePicker, int i, int j, int k) {

            _year = i;
            month = j;
            day = k;
            updateDisplay();
            txt_fecha.setText(currentDate);
        }
    };

    public void postEnviaData() {
        showLayoutDialog();
        if (updateProfileResult != null) {
            if(updateProfileResult.getRespuesta()){

                new SweetAlertDialog(MainActivity.getInstance().getApplicationContext(),SweetAlertDialog.SUCCESS_TYPE)
                        .setContentText("Los datos se han actualizado correctamente, debe volver a iniciar sesión")
                        .setConfirmText("OK")
                        .show();
                //Toast.makeText(this,"",Toast.LENGTH_SHORT).show();
                generateCerrar();

            }
            else {
                Utils.generarAlerta(ProfileDataActivity.this, getString(R.string.txt_atencion), getString(R.string.text_error_metodo));
            }
        } else {
            Utils.generarAlerta(ProfileDataActivity.this, getString(R.string.txt_atencion), getString(R.string.text_error_metodo));
        }
    }

    public void generateCerrar() {

        //eliminar preferencias
        DeletePreferencesCallMainActivity();
        AssistantService.stopService(getApplicationContext());
        NotificationHelper.Current.cancelAllNotifications(getApplicationContext());
        BarometerService.Current.stopService(getApplicationContext());

    }
    private void DeletePreferencesCallMainActivity() {
        SharedPreferencesManager.setValor(this, Utils.PREFERENCIA_USER, null, Utils.KEY_EMAIL);
        SharedPreferencesManager.setValor(this, Utils.PREFERENCIA_USER, null, Utils.KEY_NOMBRE);
        SharedPreferencesManager.setValor(this, Utils.PREFERENCIA_USER, null, Utils.KEY_APELLIDO);
        SharedPreferencesManager.setValor(this, Utils.PREFERENCIA_USER, null, Utils.KEY_PICTURE_URI);
        SharedPreferencesManager.setValor(this, Utils.PREFERENCIA_USER, null, Utils.KEY_ANIO);
        SharedPreferencesManager.setValor(this, Utils.PREFERENCIA_USER, null, Utils.KEY_PESO);
        SharedPreferencesManager.setValor(this, Utils.PREFERENCIA_USER, null, Utils.KEY_ALTURA);
        SharedPreferencesManager.setValor(this, Utils.PREFERENCIA_USER, null, Utils.KEY_SEXO);
        SharedPreferencesManager.setValor(this, Utils.PREFERENCIA_USER, null, Utils.KEY_ESTCIVIL);
        SharedPreferencesManager.setValor(this, Utils.PREFERENCIA_USER, null, Utils.KEY_TELEFONO);
        SharedPreferencesManager.setValor(this, Utils.PREFERENCIA_USER, null, Utils.KEY_PAIS);

        eliminarDatosDB();

        //volver a crear el main
        Intent intent = new Intent(this, MainActivity.class);  // envia al main
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP); //para borrar pila de actividades
        startActivity(intent);
    }

    public void eliminarDatosDB() {
        try {
            //GLUCOSA
            Utils.DeleterowsIGlucose(HealthMonitorApplicattion.getApplication().getGlucoseDao());
            //PICK FLOW ASMA
            Utils.DeleterowsIAsthma(HealthMonitorApplicattion.getApplication().getAsthmaDao());
            //INSULINA
            Utils.DeleterowsEInsulin(HealthMonitorApplicattion.getApplication().getInsulinDao());
            //PESO
            Utils.DeleterowsIWeight(HealthMonitorApplicattion.getApplication().getWeightDao());
            //PULSO-PRESION
            Utils.DeleteIPulseDB(HealthMonitorApplicattion.getApplication().getPulseDao());
            //ESTADO ANIMO
            Utils.DeleterowsIState(HealthMonitorApplicattion.getApplication().getStateDao());
            //COLESTEROL complementario
            Utils.DeleterowsIColesterol(HealthMonitorApplicattion.getApplication().getColesterolDao());
            //HB1 CETONAS complementario
            Utils.DeleterowsIHba1c(HealthMonitorApplicattion.getApplication().getHba1cDao());
            //mis doctores
            Utils.DeleterowsAllDoctors(HealthMonitorApplicattion.getApplication().getDoctorDao());
            //Medicinas y Alarmas
            Utils.DeleterowsAllMedicinesUser(HealthMonitorApplicattion.getApplication().getMedicineUserDao());
            Utils.DeleterowsAllMedicinesControl(HealthMonitorApplicattion.getApplication().getRegisteredMedicinesDao());
            Utils.DeleterowsAllMedicinesAlarm(HealthMonitorApplicattion.getApplication().getEAlarmDetailsDao());
            Utils.DeleterowsAllMedicinesTake(HealthMonitorApplicattion.getApplication().getEAlarmTakeMedicineDao());

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}