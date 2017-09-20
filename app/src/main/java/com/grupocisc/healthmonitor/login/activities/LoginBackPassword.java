package com.grupocisc.healthmonitor.login.activities;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.grupocisc.healthmonitor.HealthMonitorApplicattion;
import com.grupocisc.healthmonitor.Home.activities.MainActivity;
import com.grupocisc.healthmonitor.R;
import com.grupocisc.healthmonitor.Utils.SharedPreferencesManager;
import com.grupocisc.healthmonitor.Utils.Utils;
import com.grupocisc.healthmonitor.entities.IConfirmBackPass;
import com.grupocisc.healthmonitor.entities.ObjNewPass;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginBackPassword extends AppCompatActivity {
    private String title = "RECUPERA TU CUENTA";
    private EditText edt_email, edt_pass1, edt_pass2;
    String TAG = "LoginBackPassword";
    RecyclerView recyclerView;
    CardView card_login;
    public SweetAlertDialog pDialog;
    public String Email = "";

    private Call<IConfirmBackPass.RegistroNuevaPass> call_1;
    private IConfirmBackPass.RegistroNuevaPass mLoginUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_new_password);
        Utils.SetStyleToolbarLogo(this);
        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        card_login = (CardView) findViewById(R.id.card_login);
        edt_email = (EditText) findViewById(R.id.email);
        edt_pass1 = (EditText) findViewById(R.id.pass_1);
        edt_pass2 = (EditText) findViewById(R.id.pass_2);


        //OBTENER DATA DE PREFERENCIA
        if (Utils.getEmailFromPreference(this) != null) {
            Email = Utils.getEmailFromPreference(this);
        }

        //edt_email.setText(Email); //setear editText
        int tamanomail = edt_email.length();
        Log.e(TAG, "tamanomail: " + tamanomail);
        //edt_email.setSelection(tamanomail);
        //SharedPreferencesManager.setValor(LoginBackPassword.this, Utils.PREFERENCIA_USER, "NO_CAMBIO_PASSWORD", Utils.KEY_AVISO_TEMP);

        card_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String ls_pass = edt_pass1.getText().toString();
                if (camposLlenos()) {
                    if (isPasswordValid(ls_pass) == true) {
                        if (Utils.isOnline(LoginBackPassword.this)) {
                            restarLoading();
                        } else {
                            Utils.generarSweetAlertDialogError(LoginBackPassword.this, getString(R.string.txt_atencion), getString(R.string.sin_conexion));
                        }
                    } else {
                        generarAlerta("Password debe tener mínimo 6 caracteres");

                    }
                }
            }
        });

    }

    public void restarLoading() {
        showLoadingDialog();
        restartLoadingEnviarData();
    }

    private void restartLoadingEnviarData() {
        //obtener todos los datos del activity
        //String email = edt_email.getText().toString();
        String pass = edt_pass1.getText().toString();
        Log.e(TAG, "LoginBackPassword :: restartLoadingEnviarData: " + Email);
        Log.e(TAG, "LoginBackPassword :: restartLoadingEnviarData: " + pass);
        //enviar webservice
        //APUNTANDO AA METODO CISC
        IConfirmBackPass login = HealthMonitorApplicattion.getApplication().getRestCISCAdapterV2().create(IConfirmBackPass.class);
        call_1 = login.RegPass(new ObjNewPass(Email, pass));
        call_1.enqueue(new Callback<IConfirmBackPass.RegistroNuevaPass>() {
            @Override
            public void onResponse(Call<IConfirmBackPass.RegistroNuevaPass> call, Response<IConfirmBackPass.RegistroNuevaPass> response) {
                if (response.isSuccessful()) {
                    Log.e(TAG, "Respuesta exitosa :: LoginBackPassword :: onResponse");
                    mLoginUser = null;
                    mLoginUser = response.body();

                    postExecutionLogin();
                } else {
                    showLayoutDialog();
                    Utils.generarSweetAlertDialogError(LoginBackPassword.this, getString(R.string.txt_atencion), getString(R.string.text_error_metodo));
                    Log.e(TAG, "Error en la petición");
                }
            }

            @Override
            public void onFailure(Call<IConfirmBackPass.RegistroNuevaPass> call, Throwable t) {
                showLayoutDialog();
                Utils.generarSweetAlertDialogError(LoginBackPassword.this, getString(R.string.txt_atencion), getString(R.string.text_error_metodo) + " o revise su conexión a internet");
                t.printStackTrace();
            }
        });
    }

    //se ejecuta al seleccionar el icon back del toolbar
   /* @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            this.finish();
        return super.onOptionsItemSelected(item);
    }*/

    private void showLoadingDialog() {
        pDialog.getProgressHelper().setBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        pDialog.setTitleText("Espere un Momento..");
        pDialog.setCancelable(false);
        pDialog.show();

    }

    private void showLayoutDialog() {
        if (pDialog != null)
            pDialog.dismiss();
    }

    public void postExecutionLogin() {
        showLayoutDialog();

        if (mLoginUser != null) {
            if (mLoginUser.getIdCodResult() == 0) {
                //oka
                Log.e(TAG, "postExecutionLogin; LoginBackPassword :: " + mLoginUser.getIdCodResult() );
                generateAlertDialogChangePass( getString(R.string.txt_atencion), mLoginUser.getResultDescription() );


                /*SharedPreferencesManager.setValor(LoginBackPassword.this, Utils.PREFERENCIA_USER, null, Utils.KEY_RECUPERA_CTA);
                SharedPreferencesManager.setValor(LoginBackPassword.this, Utils.PREFERENCIA_USER, null, Utils.KEY_EMAIL_TEMP);
                SharedPreferencesManager.setValor(LoginBackPassword.this, Utils.PREFERENCIA_USER, null, Utils.KEY_AVISO_TEMP);
                Intent intent = new Intent(LoginBackPassword.this, MainActivity.class);  // envia al main
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP); //para borrar pila de actividades
                startActivity(intent);*/


            } else {
                Utils.generarSweetAlertDialogError(LoginBackPassword.this, getString(R.string.txt_atencion), mLoginUser.getResultDescription());
            }
        } else {
            Utils.generarSweetAlertDialogError(LoginBackPassword.this, getString(R.string.txt_atencion), getString(R.string.text_error_metodo));
        }
    }

    public void generateAlertDialogChangePass(String Title, String message) {
        new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("" + Title)
                .setContentText("" + message)
                .setConfirmText("Confirmar")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();

                        finish();
                    }
                })
                .show();
    }

    private boolean camposLlenos() {
        /*if (edt_email.getText().toString() == null || edt_email.getText().toString().length() == 0) {
            generarAlerta("El campo EMAIL es obligatorio");
            return false;
        } else*/
        if (edt_pass1.getText().toString() == null || edt_pass1.getText().toString().length() == 0 ||
                edt_pass2.getText().toString() == null || edt_pass2.getText().toString().length() == 0) {
            generarAlerta("El campo CONTRASEÑA es obligatorio");
            return false;
        } else if (!edt_pass1.getText().toString().equals(edt_pass2.getText().toString())) {
            generarAlerta("La CONTRASEÑA no coincide");
            return false;
        }
        return true;
    }

    private void generarAlerta(String mensaje) {
        Utils.generarSweetAlertDialogError(this, getString(R.string.txt_error), mensaje);
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
        if (ls_pass.length() > 5) {
            isValid = true;
        }
        return isValid;
    }


}
