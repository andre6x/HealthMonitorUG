package com.grupocisc.healthmonitor.login.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.grupocisc.healthmonitor.HealthMonitorApplicattion;
import com.grupocisc.healthmonitor.Home.activities.MainActivity;
import com.grupocisc.healthmonitor.R;
import com.grupocisc.healthmonitor.Services.AssistantService;
import com.grupocisc.healthmonitor.Services.BarometerService;
import com.grupocisc.healthmonitor.Utils.ServiceChecker;
import com.grupocisc.healthmonitor.Utils.SharedPreferencesManager;
import com.grupocisc.healthmonitor.Utils.Utils;
import com.grupocisc.healthmonitor.entities.IUserLogin;
import com.grupocisc.healthmonitor.entities.ObjLogin;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    String TAG = "LoginActivity";
    private TextView  txt_contrasena;
    private CardView card_login , card_crear;
    private EditText edt_email,edt_pass ;

    public SweetAlertDialog pDialog;
    private Call<IUserLogin.UserLogin> call_1;
    private IUserLogin.UserLogin mLoginUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        Utils.SetStyleToolbarLogo(this);
        //Dialog = new ProgressDialog(this);

        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);

        card_login = (CardView) findViewById(R.id.card_login);
        card_crear = (CardView) findViewById(R.id.card_crear);
        txt_contrasena = (TextView) findViewById(R.id.txt_contrasena);

        edt_email = (EditText)findViewById(R.id.edt_email);
        edt_pass = (EditText) findViewById(R.id.edt_pass);


        card_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ls_mail = edt_email.getText().toString();
                String ls_pass = edt_pass.getText().toString();
                if(camposLlenos()) {
                        if ( isPasswordValid(ls_pass) == true)  {
                            if(Utils.isOnline(LoginActivity.this)) {
                                restarLoading();
                                //prueba();
                            }else{
                                Utils.generarSweetAlertDialogError(LoginActivity.this, getString(R.string.txt_atencion),getString(R.string.sin_conexion));
                            }
                        }else{
                                generarAlerta("Password debe tener mínimo 6 caracteres");
                        }
                }


            }
        });
        card_crear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callCrearCuentaActivity();
            }
        });
        txt_contrasena.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callRecuperarCuentaActivity();
            }
        });
    }

    private void prueba() {
        SharedPreferencesManager.setValor(this, Utils.PREFERENCIA_USER, "JESEQUITO@HOTMAIL.COM"   , Utils.KEY_EMAIL);

        //enviar a importar data
        Intent intent = new Intent(this, ImportDataActivity.class);  // enviar a IMPORTA DATA y luego al main
        startActivity(intent);
    }


    //se ejecuta al seleccionar el icon back del toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            this.finish();
        return super.onOptionsItemSelected(item);
    }

    private boolean camposLlenos() {
        if (edt_email.getText().toString() == null || edt_email.getText().toString().length() == 0){
            Utils.generarSweetAlertDialogWarning(this, getString(R.string.txt_atencion), "El campo EMAIL es obligatorio");
            return false;
        }
        else if (edt_pass.getText().toString() == null || edt_pass.getText().toString().length() == 0){
            Utils.generarSweetAlertDialogWarning(this, getString(R.string.txt_atencion),"El campo PASSWORD es obligatorio");
            return false;
        }
        return true;
    }
    private void generarAlerta(String mensaje){
        Utils.generarAlerta(this, getString(R.string.txt_error), mensaje);
    }


    public  void callCrearCuentaActivity(){
        Intent i = new Intent(this,LoginFacebookActivity.class);
        startActivity(i);
    }
    public  void callRecuperarCuentaActivity(){
        Intent i = new Intent(this,LoginAccountBackActivity.class);
        startActivity(i);
    }

    /*public void prueba(){
        SavePreferencesCallMainActivity();
    }*/

    private void showLoadingDialog(){
        pDialog.getProgressHelper().setBarColor( ContextCompat.getColor(this, R.color.colorPrimary)  );
        pDialog.setTitleText("Espere un Momento..");
        pDialog.setCancelable(false);
        pDialog.show();

    }

    private void showLayoutDialog(){
        if (pDialog != null)
            pDialog.dismiss();
    }
    //datos de usuario obtenedis del webservice y guardar en preferencias
    private void SavePreferencesCallMainActivity() {
        SharedPreferencesManager.setValor(this, Utils.PREFERENCIA_USER, mLoginUser.getEmail()    , Utils.KEY_EMAIL);
        SharedPreferencesManager.setValor(this, Utils.PREFERENCIA_USER, mLoginUser.getName()  , Utils.KEY_NOMBRE);
        SharedPreferencesManager.setValor(this, Utils.PREFERENCIA_USER, mLoginUser.getLastName() , Utils.KEY_APELLIDO);
        SharedPreferencesManager.setValor(this, Utils.PREFERENCIA_USER, mLoginUser.getBirthDate()     , Utils.KEY_ANIO);
        SharedPreferencesManager.setValor(this, Utils.PREFERENCIA_USER, mLoginUser.getWeight() +""    , Utils.KEY_PESO);
        SharedPreferencesManager.setValor(this, Utils.PREFERENCIA_USER, mLoginUser.getHeight()+""  , Utils.KEY_ALTURA);
        SharedPreferencesManager.setValor(this, Utils.PREFERENCIA_USER, mLoginUser.getGender()    , Utils.KEY_SEXO);
        SharedPreferencesManager.setValor(this, Utils.PREFERENCIA_USER, mLoginUser.getRelationshipStatus()   , Utils.KEY_ESTCIVIL);
        SharedPreferencesManager.setValor(this, Utils.PREFERENCIA_USER, mLoginUser.getDiabetesType() +""    , Utils.KEY_TIPO_DIABETES);
        SharedPreferencesManager.setValor(this, Utils.PREFERENCIA_USER, mLoginUser.getCellPhone()    , Utils.KEY_TELEFONO);
        SharedPreferencesManager.setValor(this, Utils.PREFERENCIA_USER, mLoginUser.getCountry() +""    , Utils.KEY_PAIS);

        //v3
        SharedPreferencesManager.setValor(this,Utils.PREFERENCIA_USER,mLoginUser.getAsma()+"", Utils.KEY_ASMA);

        //enviar a importar data
        Intent intent = new Intent(this, ImportDataActivity.class);  // enviar a IMPORTA DATA y luego al main
        startActivity(intent);
    }

    public void restarLoading(){
        showLoadingDialog();
        restartLoadingEnviarData();
    }

    private void restartLoadingEnviarData( ){
        //obtener todos los datos del activity
        String user_email    = edt_email.getText().toString() ;
        String user_pass     = edt_pass.getText().toString();

        //enviar webservice
        //APUNTANDO AA METODO CISC
        IUserLogin login = HealthMonitorApplicattion.getApplication().getRetrofitAdapter().create(IUserLogin.class);
        Log.i(TAG, "restartLoadingEnviarData: " + user_email +" - "+ user_pass  );
        call_1 = login.LoginUser( new ObjLogin(user_email,user_pass)  );
        call_1.enqueue(new Callback<IUserLogin.UserLogin>() {
            @Override
            public void onResponse(Call<IUserLogin.UserLogin> call, Response<IUserLogin.UserLogin> response) {
                if (response.isSuccessful()) {
                    Log.e(TAG, "Respuesta exitosa");
                    mLoginUser = null;
                    mLoginUser = response.body();

                    postExecutionLogin();
                } else {
                    showLayoutDialog();
                    Utils.generarSweetAlertDialogError(LoginActivity.this, getString(R.string.txt_atencion), getString(R.string.text_error_metodo));

                    Log.e(TAG, "Error en la petición");
                }
            }

            @Override
            public void onFailure(Call<IUserLogin.UserLogin> call, Throwable t) {
                showLayoutDialog();
                Utils.generarSweetAlertDialogError(LoginActivity.this, getString(R.string.txt_atencion), getString(R.string.text_error_metodo) + " o revise su conexión a internet");
                t.printStackTrace();
            }
        });
    }

    public void postExecutionLogin(){
        showLayoutDialog();

        if (mLoginUser != null) {
            if (mLoginUser.getIdCodResult()== 0 ) {
                if(mLoginUser.getEmail() != null){
                    SavePreferencesCallMainActivity();
                }else{
                    Utils.generarSweetAlertDialogError(LoginActivity.this, getString(R.string.txt_atencion), getString(R.string.text_error_metodo_email) );
                }
            }else {
                Utils.generarSweetAlertDialogError(LoginActivity.this, getString(R.string.txt_atencion), mLoginUser.getResultDescription() );
            }
        }else {
            Utils.generarSweetAlertDialogError(LoginActivity.this, getString(R.string.txt_atencion), getString(R.string.text_error_metodo));
        }
    }

    public static boolean isEmailValid(String ls_mail) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = ls_mail;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    private boolean isPasswordValid(String ls_pass) {
        /**Si la cadena supera los 5 caracteres es una contraseña valida*/
        boolean isValid = false;
        if (ls_pass.length() > 5)
        {isValid = true; }
        return isValid;
    }


}
