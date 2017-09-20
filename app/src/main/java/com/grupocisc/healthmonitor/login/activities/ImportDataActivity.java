package com.grupocisc.healthmonitor.login.activities;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.grupocisc.healthmonitor.HealthMonitorApplicattion;
import com.grupocisc.healthmonitor.Home.activities.MainActivity;
import com.grupocisc.healthmonitor.R;
import com.grupocisc.healthmonitor.Services.ProgressIntentService;
import com.grupocisc.healthmonitor.Utils.SharedPreferencesManager;
import com.grupocisc.healthmonitor.Utils.Utils;
import com.grupocisc.healthmonitor.entities.IConfirmBackPass;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImportDataActivity extends AppCompatActivity {
    private String title = "RECUPERA TU CUENTA";
    private EditText edt_email,edt_pass1,edt_pass2 ;
    String TAG = "LoginBackPassword";
    RecyclerView recyclerView;
    CardView card_next;
    CardView card_omitir;
    public ProgressDialog Dialog;
    public String Email = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.import_data_activity);
//        Utils.SetStyleToolbarLogo(this);
        Dialog = new ProgressDialog(this);
        card_next = (CardView) findViewById(R.id.card_next);
        card_omitir = (CardView) findViewById(R.id.card_omitir);


        //OBTENER DATA DE PREFERENCIA
        if (Utils.getEmailFromPreference(this) != null) {
            Email = Utils.getEmailFromPreference(this);
        }
        Log.e(TAG,"Email:"+ Email);

        card_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ImportDataActivity.this, ProgressIntentService.class);
                //intent.setAction(Constants.ACTION_RUN_ISERVICE);
                startService(intent);
                gotoMain();

            }
        });
        card_omitir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoMain();

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        gotoMain();
    }

    public void gotoMain(){
        //volver a crear el main
        Intent intent = new Intent(this, MainActivity.class);  // envia al main
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP); //para borrar pila de actividades
        startActivity(intent);
    }




}
