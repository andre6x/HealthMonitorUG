package com.grupocisc.healthmonitor.login.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.grupocisc.healthmonitor.HealthMonitorApplicattion;
import com.grupocisc.healthmonitor.Home.activities.MainActivity;
import com.grupocisc.healthmonitor.R;
import com.grupocisc.healthmonitor.Utils.SharedPreferencesManager;
import com.grupocisc.healthmonitor.Utils.Utils;
import com.grupocisc.healthmonitor.entities.ISendBackAccount;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginAccountBackActivity extends AppCompatActivity {

    private String title = "RECUPERA TU CUENTA";
    private CardView card_recuperar;
    private EditText edt_email;
    public ProgressDialog Dialog;
    private String user_email;
    String TAG = "LoginAccountBackActivit";
    private Call<ISendBackAccount.SendBackAccount> call_1;
    private ISendBackAccount.SendBackAccount mLoginUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_account_back_activity);
        Utils.SetStyleToolbarTitle(this, title);
        Dialog = new ProgressDialog(this);
        edt_email = (EditText)findViewById(R.id.edt_email);
        card_recuperar = (CardView) findViewById(R.id.card_recuperar);
        card_recuperar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ls_mail = edt_email.getText().toString();
                if(camposLlenos()) {
                    if (isEmailValid(ls_mail)== true){
                        if(Utils.isOnline(LoginAccountBackActivity.this)) {
                            restarLoading();
                        }else{
                            Utils.generarAlerta(LoginAccountBackActivity.this, getString(R.string.txt_atencion),getString(R.string.sin_conexion));
                        }
                    }

                }
            }
        });
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
            generarAlerta("El campo EMAIL es obligatorio");
            return false;
        }
        return true;
    }

    private void generarAlerta(String mensaje){
        Utils.generarAlerta(this, getString(R.string.txt_error), mensaje);
    }

    public void restarLoading(){
        showLoadingDialog();
        restartLoadingEnviarData();
    }

    private void restartLoadingEnviarData( ){
        //obtener todos los datos del activity
         user_email    = edt_email.getText().toString() ;
        Log.e(TAG, "LoginAccountBackActivity :: restartLoadingEnviarData: "+user_email);

        //enviar webservice
        //APUNTANDO AA METODO CISC
        ISendBackAccount send = HealthMonitorApplicattion.getApplication().getRetrofitAdapter().create(ISendBackAccount.class);
        call_1 = send.EnvioEmail(user_email);
        call_1.enqueue(new Callback<ISendBackAccount.SendBackAccount>() {
            @Override
            public void onResponse(Call<ISendBackAccount.SendBackAccount> call, Response<ISendBackAccount.SendBackAccount> response) {
                if (response.isSuccessful()) {
                    Log.e(TAG, "Respuesta exitosa onResponse");
                    mLoginUser = null;
                    mLoginUser = response.body();

                    postExecutionLogin();
                } else {
                    showLayoutDialog();
                    //Utils.generarAlerta(LoginAccountBackActivity.this, getString(R.string.txt_atencion), getString(R.string.text_error_metodo));
                    generarAlertaOk("RECUPERA TU CUENTA",   mLoginUser.getRespuesta());
                    Log.e(TAG, "Error en la petición");
                }
            }

            @Override
            public void onFailure(Call<ISendBackAccount.SendBackAccount> call, Throwable t) {
                showLayoutDialog();
                Utils.generarAlerta(LoginAccountBackActivity.this, getString(R.string.txt_atencion), getString(R.string.text_error_metodo) + " o revise su conexión a internet");
                t.printStackTrace();
            }
        });
    }

    public void postExecutionLogin(){
        showLayoutDialog();

        if (mLoginUser != null) {
            if (mLoginUser.getCodigo() == 0 ) {
                //Utils.generarAlerta(LoginAccountBackActivity.this, getString(R.string.txt_atencion), mLoginUser.getRespuesta() );
                //oka
                Log.e(TAG,"LoginAccountBackActivity :: postExecutionLogin; "+ mLoginUser.getRespuesta());
                //Utils.generarAlerta(LoginAccountBackActivity.this, getString(R.string.txt_ok), getString(R.string.back_pass));
                generarAlertaOk("RECUPERA TU CUENTA",  getString(R.string.back_pass));

            }else {
                //Utils.generarAlerta(LoginAccountBackActivity.this, getString(R.string.txt_atencion), mLoginUser.getRespuesta() );
                generarAlertaOk("RECUPERA TU CUENTA",   mLoginUser.getRespuesta());
            }
        }else {
            generarAlertaOk("RECUPERA TU CUENTA",   mLoginUser.getRespuesta());
            // Utils.generarAlerta(LoginAccountBackActivity.this, getString(R.string.txt_atencion), getString(R.string.text_error_metodo));
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

    private void showLayoutDialog(){
        if (Dialog != null)
            Dialog.dismiss();
    }

    private void showLoadingDialog(){
        Dialog.setMessage("Espere un Momento..");
        Dialog.setCancelable(false);
        Dialog.show();
    }

    private void SavePreferencesCallMainActivity() {
        SharedPreferencesManager.setValor(this, Utils.PREFERENCIA_USER,"SOLICITADO"  , Utils.KEY_RECUPERA_CTA);
        SharedPreferencesManager.setValor(this, Utils.PREFERENCIA_USER, user_email , Utils.KEY_EMAIL_TEMP);

        //volver a crear el main
        Intent intent = new Intent(this, MainActivity.class);  // envia al main
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP); //para borrar pila de actividades
        startActivity(intent);
    }
    public void generarAlertaOk(String Title, String msm) {
        if (!(LoginAccountBackActivity.this).isFinishing()) {
            try {
                //show dialog
                AlertDialog.Builder alert = new AlertDialog.Builder(LoginAccountBackActivity.this);
                alert.setTitle("" + Title);
                alert.setMessage("" + msm);
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                        SavePreferencesCallMainActivity();
                       // finish();
                    }
                });
                alert.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
