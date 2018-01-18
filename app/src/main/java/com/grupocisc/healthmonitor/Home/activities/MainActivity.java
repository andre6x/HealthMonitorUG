package com.grupocisc.healthmonitor.Home.activities;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import com.github.mikephil.charting.utils.FileUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.grupocisc.healthmonitor.Advertising.PublicidadWebViewActivity;
import com.grupocisc.healthmonitor.Asthma.activities.PickFlowActivity;
import com.grupocisc.healthmonitor.Complementary.activities.ComplActivity;
import com.grupocisc.healthmonitor.Disease.activities.DiseaseActivity;
import com.grupocisc.healthmonitor.Doctor.activities.DoctorActivity;
import com.grupocisc.healthmonitor.Feeding.activities.FeedingActivity;
import com.grupocisc.healthmonitor.FitData.activities.FitActivity;
import com.grupocisc.healthmonitor.Glucose.activities.GlucoseActivity;
import com.grupocisc.healthmonitor.HealthMonitorApplicattion;
import com.grupocisc.healthmonitor.Home.fragments.HomeFragment;
import com.grupocisc.healthmonitor.Home.fragments.NavigationDrawerFragment;
import com.grupocisc.healthmonitor.Insulin.activities.InsulinActivity;
import com.grupocisc.healthmonitor.Medicines.activities.MedicinesActivity;
import com.grupocisc.healthmonitor.NotificationsMedical.activities.NotificationsMedicalActivity;
import com.grupocisc.healthmonitor.Pressure.activities.PressureActivity;
import com.grupocisc.healthmonitor.Profile.ProfileDataActivity;
import com.grupocisc.healthmonitor.Pulse.activities.PulseActivity;
import com.grupocisc.healthmonitor.R;
import com.grupocisc.healthmonitor.Recommendations.activities.RecommendationsActivity;
import com.grupocisc.healthmonitor.Report.activities.ReportActivity;
import com.grupocisc.healthmonitor.Routines.activities.RoutinesActivity;
import com.grupocisc.healthmonitor.Services.AssistantService;
import com.grupocisc.healthmonitor.Services.BarometerService;
import com.grupocisc.healthmonitor.Services.ProgressIntentService;
import com.grupocisc.healthmonitor.Services.SendDataMyService;
import com.grupocisc.healthmonitor.Settings.activities.AboutActivity;
import com.grupocisc.healthmonitor.Settings.activities.TutorialActivityV2;
import com.grupocisc.healthmonitor.SocialNetworks.activities.SocialActivity;
import com.grupocisc.healthmonitor.State.activities.StateActivity;
import com.grupocisc.healthmonitor.Utils.NotificationHelper;
import com.grupocisc.healthmonitor.Utils.ServiceChecker;
import com.grupocisc.healthmonitor.Utils.SharedPreferencesManager;
import com.grupocisc.healthmonitor.Utils.Utils;
import com.grupocisc.healthmonitor.Weight.activities.WeightActivity;
import com.grupocisc.healthmonitor.entities.IDoctorVinculado;
import com.grupocisc.healthmonitor.entities.IPushNotification;
import com.grupocisc.healthmonitor.gcmClasses.QuickstartPreferences;
import com.grupocisc.healthmonitor.gcmClasses.RegistrationIntentService;
import com.grupocisc.healthmonitor.login.activities.LoginActivity;
import com.grupocisc.healthmonitor.login.activities.LoginBackPassword;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Calendar;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;


/**
 * Created by GrupoLink on 08/04/2015.
 */
public class MainActivity extends AppCompatActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private String TAG = "MainActivity";
    private Toolbar mToolbar;
    public static Intent nextIntent;
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private Fragment mFragment;

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private BroadcastReceiver mRegistrationBroadcastReceiver;

    //private BroadcastReceiver _networkStateReceiver; //V3

    private String token = "";

    private Call<IDoctorVinculado.DoctorVinculado> call_1; //CAMBIO
    private IDoctorVinculado.DoctorVinculado mLoginUser; //CAMBIO															   
    private IPushNotification.InsertNotification insertNotification;

    private String StatusName = "";
    private int IdStatus = 0;

    private static final String enviadoServer = "false";
    private static final String operacionI = "I";

    private ImageView iv_est_1;
    private ImageView iv_est_2;
    private ImageView iv_est_3;
    private ImageView iv_est_4;
    private ImageView iv_est_5;



    public static final String PREF_USER_FIRST_TIME = "user_first_time";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        //        Transparent Status Bar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        setProgressBarIndeterminateVisibility(false);
        nextIntent = null;

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        //mToolbar.setNavigationIcon(R.drawable.back);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(""); //titulo tollbar
        // Remove default title text

        mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerLayout.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS); /* add this line */
        // Set up the drawer.
        mNavigationDrawerFragment.setUp(R.id.navigation_drawer, drawerLayout, mToolbar);
        mNavigationDrawerFragment.Close();

        mFragment = new HomeFragment();

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container, mFragment).commit();

        String email = Utils.getEmailFromPreference(this) == null ? "" : Utils.getEmailFromPreference(this);
        notificaciones(email);

        iv_est_1 = (ImageView) findViewById(R.id.img_est_1);
        iv_est_2 = (ImageView) findViewById(R.id.img_est_2);
        iv_est_3 = (ImageView) findViewById(R.id.img_est_3);
        iv_est_4 = (ImageView) findViewById(R.id.img_est_4);
        iv_est_5 = (ImageView) findViewById(R.id.img_est_5);


//        File db =HealthMonitorApplicattion.getApplication().getDatabasePath("healthmonitorDB");
//
//        if (db.exists()) {
//            File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
//
//            exportDB(db.getAbsolutePath());
//        }



        Imagenes();

        iniciarServicio();

        //v3
        if(!ServiceChecker.Current.isServiceRunning(this,ProgressIntentService.class)){

            if(Utils.getEmailFromPreference(this)!=null){
                InitAssistantService(this,TAG);
                InitBarometerReaderService(this,TAG);
            }
        }
        else {
            Log.i(TAG,"ProgressIntent is still running ");
        }

        showHashKey(this);
    }

//    void exportDB(String dbPath) {
//        try {
//            File dbFile = new File(dbPath);
//            FileInputStream fis = new FileInputStream(dbFile);
//
//            File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
//
//            String outFileName = dir.getAbsolutePath();
//            OutputStream output = new FileOutputStream(outFileName+"/healthmonitorDb.db3");
//            byte[] buffer = new byte[1024];
//            int length;
//            while ((length = fis.read(buffer)) > 0) {
//                output.write(buffer, 0, length);
//            }
//            output.flush();
//            output.close();
//            fis.close();
//        } catch (Exception e) {
//            Log.e("MainAC",e.getMessage());
//        }
//    }

    //v3
    public static void InitAssistantService(Context ctx,String TAG){
        if(!ServiceChecker.Current.isServiceRunning(ctx,AssistantService.class)){
            Intent assistantService = new Intent(ctx, AssistantService.class); //serv de tipo Intent
            ctx.startService(assistantService); //ctx de tipo Context
            Log.i(TAG, "Assistant service started");
        } else {
            Log.i(TAG, "Assistant service is already running");
        }
    }

    public static void InitBarometerReaderService(Context ctx,String TAG){
        if(!ServiceChecker.Current.isServiceRunning(ctx,BarometerService.class)){
            Intent barometerService = new Intent(ctx, BarometerService.class); //serv de tipo Intent
            ctx.startService(barometerService); //ctx de tipo Context
            Log.i(TAG, "Barometer service started");
        } else {
            Log.i(TAG, "Barometer service is already running");
        }
    }

    //v3
    public void saveDataStateDB(String fecha, String hora, int IdStatus, String StatusName, String observacion) {
        try {
            if(Utils.getEmailFromPreference(getApplicationContext())!=null){
                //setear datos al objeto y guardar y BD
                Utils.DbsaveStateFromDatabase(-1,
                        IdStatus,
                        StatusName,
                        fecha,
                        hora,
                        observacion,
                        enviadoServer,
                        operacionI,
                        HealthMonitorApplicattion.getApplication().getStateDao());

                Utils.generateToast(this, "Su estado de ánimo se ha guardado con éxito");

            }
            else {
                generarAlertaNoRegistrado("No iniciado sesión");

            }

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    unselectStates();
                }
            }, 500);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //v3
    static final class CustomDate{

        private static String _stringDate;

        public static String getStringDate(){
            return _stringDate;
        }

        private static String _stringHour;
        public static String getStringHour(){
            return _stringHour;
        }

        static void init(){
            //obtener fechay hroa de Calendar
            Calendar c = Calendar.getInstance();
            Integer year = c.get(Calendar.YEAR);
            Integer month = c.get(Calendar.MONTH)+1;
            Integer day = c.get(Calendar.DAY_OF_MONTH);
            Integer hour = c.get(Calendar.HOUR_OF_DAY);
            Integer minute = c.get(Calendar.MINUTE);
            Integer second = c.get(Calendar.SECOND);
            //setear fecha

            //String mes = (month + 1) < 10 ? "0" + (month + 1) : "" + (month);
            String mes = month < 10 ? "0" + month : "" + month;
            String dia = day < 10 ? "0" + day : "" + day;
            //String date = ""+day+"/"+(++month)+"/"+year;
            _stringDate = "" + dia + "/" + mes + "/" + year;

            //setear hora
            String hourString = hour < 10 ? "0" + hour : "" + hour;
            String minuteString = minute < 10 ? "0" + minute : "" + minute;
            //String secondString = second < 10 ? "0"+second : ""+second;
            _stringHour = "" + hourString + ":" + minuteString;

        }
    }

    //v3
    public void Imagenes() {
        iv_est_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setImage_estatus_1();
            }
        });

        iv_est_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setImage_estatus_2();

            }
        });

        iv_est_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setImage_estatus_3();

            }
        });

        iv_est_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setImage_estatus_4();

            }
        });

        iv_est_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setImage_estatus_5();

            }
        });
    }

    public void setImage_estatus_1() {
        iv_est_1.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.estado_increible_con));
        iv_est_2.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.estado_feliz_sin));
        iv_est_3.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.estado_serio_sin));
        iv_est_4.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.estado_triste_sin));
        iv_est_5.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.estado_horrible_sin));
        StatusName = getResources().getString(R.string.txt_sta_increible);
        StatusName= String.format(String.valueOf(getResources().getColor(R.color.status_orange)));

        IdStatus = 1;

        CustomDate.init();

        saveDataStateDB(CustomDate.getStringDate(),CustomDate.getStringHour(),IdStatus,StatusName,"");

    }

    public void setImage_estatus_2() {
        iv_est_1.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.estado_increible_sin));
        iv_est_2.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.estado_feliz_con));
        iv_est_3.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.estado_serio_sin));
        iv_est_4.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.estado_triste_sin));
        iv_est_5.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.estado_horrible_sin));
        StatusName = getResources().getString(R.string.txt_sta_bien);
        IdStatus = 2;

        CustomDate.init();

        saveDataStateDB(CustomDate.getStringDate(),CustomDate.getStringHour(),IdStatus,StatusName,"");

    }

    public void setImage_estatus_3() {
        iv_est_1.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.estado_increible_sin));
        iv_est_2.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.estado_feliz_sin));
        iv_est_3.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.estado_serio_con));
        iv_est_4.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.estado_triste_sin));
        iv_est_5.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.estado_horrible_sin));
        StatusName = getResources().getString(R.string.txt_sta_normal);
        IdStatus = 3;

        CustomDate.init();

        saveDataStateDB(CustomDate.getStringDate(),CustomDate.getStringHour(),IdStatus,StatusName,"");

    }

    public void setImage_estatus_4() {
        iv_est_1.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.estado_increible_sin));
        iv_est_2.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.estado_feliz_sin));
        iv_est_3.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.estado_serio_sin));
        iv_est_4.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.estado_triste_con));
        iv_est_5.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.estado_horrible_sin));
        StatusName = getResources().getString(R.string.txt_sta_mal);
        IdStatus = 4;

        CustomDate.init();

        saveDataStateDB(CustomDate.getStringDate(),CustomDate.getStringHour(),IdStatus,StatusName,"");
    }

    public void setImage_estatus_5() {
        iv_est_1.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.estado_increible_sin));
        iv_est_2.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.estado_feliz_sin));
        iv_est_3.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.estado_serio_sin));
        iv_est_4.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.estado_triste_sin));
        iv_est_5.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.estado_horrible_con));
        StatusName = getResources().getString(R.string.txt_sta_horrible);
        IdStatus = 5;

        CustomDate.init();

        saveDataStateDB(CustomDate.getStringDate(),CustomDate.getStringHour(),IdStatus,StatusName,"");
    }

    //v3
    void unselectStates(){
        iv_est_1.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.estado_increible_sin));
        iv_est_2.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.estado_feliz_sin));
        iv_est_3.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.estado_serio_sin));
        iv_est_4.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.estado_triste_sin));
        iv_est_5.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.estado_horrible_sin));
        StatusName="";
        IdStatus =0;
        //StatusName = getResources().getString(R.string.txt_sta_increible);
        //StatusName= String.format(String.valueOf(getResources().getColor(R.color.status_orange)));
    }

    //inico servicio enviar data webservice
    public void iniciarServicio() {
        if (!ServiceChecker.Current.isServiceRunning(this,SendDataMyService.class)) { //método que determina si el servicio ya está corriendo o no
            Intent serv = new Intent(this, SendDataMyService.class); //serv de tipo Intent
            this.startService(serv); //ctx de tipo Context
            Log.e(TAG, "Send Data WS Service started");
        } else {
            Log.e(TAG, "Send Data WS Service already running");
        }
    }


    //fin servicio enviar data webservice

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {

        if ((Utils.getEmailFromPreference(this) != null)
                || position == 0 || position == -1 || position == 18
                || position == 17 || position == 20 || position == 21
                || position == -2 || position == 15 || position == 16
                || position == 22 || position == 24 || position == 25) {
            displayView(position);
        } else {
            notificaciones("");
            generarAlertaNoRegistrado();
        }

    }

    public void displayView(int position) {

        int idMenu = position;
        Log.e("Posicion", "--posicionfragment0:" + idMenu);

        Intent i = null;
        switch (idMenu) {
            case -2: // REGISTRO USUARIO
                //SI esta registrado
                if (Utils.getEmailFromPreference(this) != null) {
                    if (Utils.getAvisoTempFromPreference(this) != null) { //CAMBIO
                        String mmm = Utils.getAvisoTempFromPreference(this);
                        String mail_temp = Utils.getEmailFromPreference(MainActivity.this);
                        String user_email = Utils.getEmailFromPreference(MainActivity.this);
                        if (user_email.equals(mail_temp)) {
                            i = new Intent(MainActivity.this, LoginBackPassword.class);
                        } else {
                            i = new Intent(MainActivity.this, ProfileDataActivity.class);
                        }
                    } else {
                        i = new Intent(MainActivity.this, ProfileDataActivity.class);
                    }
                }
                break;
            case -1: // REGISTRO USUARIO
                //NO esta registrado
                i = new Intent(MainActivity.this, LoginActivity.class);
                break;
            case 1: // CONTROL DE GLUCOSA
                i = new Intent(this, GlucoseActivity.class);
                break;
            case 2: // CONTROL DE PULSO
                i = new Intent(this, PulseActivity.class);
                break;
            case 3: //CONTROL PRESIÓN
                i = new Intent(this, PressureActivity.class);
                break;
            case 4: // CONTROL DE PESO
                i = new Intent(this, WeightActivity.class);
                break;
            case 5: // CONTROL DE INSULINA
                i = new Intent(this, InsulinActivity.class);
                break;
            case 6: // CONTROL ESTADO DE ANIMO
                i = new Intent(this, StateActivity.class);
                break;
            case 7: // CONTROL DE ENFERMEDAD
                i = new Intent(this, DiseaseActivity.class);
                break;
            case 8: //RUTINAS EJERCICIOS
                i = new Intent(this, RoutinesActivity.class);
                break;
            case 9:  //ALIMENTACIÓN
                i = new Intent(this, FeedingActivity.class);
                break;
            case 10: // NOTIFICACIONES MEDICAS
                i = new Intent(this, NotificationsMedicalActivity.class);
                break;
            case 11:  //RECOMENDACIONES
                i = new Intent(this, RecommendationsActivity.class);
                break;
            case 12:  // INFORMES
                i = new Intent(this, ReportActivity.class);
                break;
            case 13:  // MEDICINAS
                i = new Intent(this, MedicinesActivity.class);
                break;
            case 14:  // REGISTROS COMPLEMENTARIOS
                i = new Intent(this, ComplActivity.class);
                break;
            case 15:  // VINCULACION DOCTORES
                i = new Intent(this, DoctorActivity.class);
                break;
            case 16:  // PUBLICIDAD
                i = new Intent(this, PublicidadWebViewActivity.class);
                break;
            case 17:  // REDES SOCIALES
                i = new Intent(this, SocialActivity.class);
                break;
            case 18:  // COMPARTIR
                Utils.shareApp(this, getResources().getString(R.string.link_text) + " " + getResources().getString(R.string.linkGoogleplay));
                break;
            case 19:  // CONFIGURACIONES

                break;
            case 20:  // ACERCA DE
                i = new Intent(this, AboutActivity.class);
                break;
            case 21:  // TUTORIAL
                i = new Intent(this, TutorialActivityV2.class);
                break;
            case 22:  // INCIAR SESION
                i = new Intent(MainActivity.this, LoginActivity.class);
                break;
            case 23:  // CERRAR SESION
                generateAlertDialogCerrar(getString(R.string.txt_atencion), getString(R.string.text_logout));
                break;
            case 26:  // ASMA
                i = new Intent(MainActivity.this, PickFlowActivity.class);
                break;
            case 27:  // GOOGLE FIT
                i = new Intent(MainActivity.this, FitActivity.class);
                break;
            default:
                break;

        }
        if (i != null) {
            startActivity(i);
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (mFragment instanceof HomeFragment) {
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        } else {
            mFragment = new HomeFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.container, mFragment).commit();

            //mNavigationDrawerFragment.setSelectionPaint(0);
        }
    }

    private void notificaciones(String Usermail) {
        if (Utils.isOnline(this)) {
            token = SharedPreferencesManager.getValorEsperado(this, getString(R.string.preferencias_inicio), "gcmToken");
            if (token == null || token.equalsIgnoreCase("")) {
                Log.e(TAG, "token is null");
                registrarGCM();
            }
            if (Usermail.length() != 0) {
                Log.e(TAG, "token=" + token);
                String email = Utils.getEmailFromPreference(this);
                enviartoken(email, SharedPreferencesManager.getValorEsperado(this, getString(R.string.preferencias_inicio), "gcmToken"));
            }
        }


        // REDIRECCIONANDO
        //evento servicio notificacion
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            Log.e("bundle", "no nulo");
            Log.e("idCurso", "->" + bundle.getInt("idSeccion"));
            try {
                redireccionar_evento(bundle.getInt("idSeccion"));
            } catch (Exception e) {
                Log.e("error", "redireccionando: " + e.toString());
            }
        } else {
            Log.e("bundle", "es nulo");
        }
    }

    private void enviartoken(String userMail, String gcmToken) {
        IPushNotification notifi = HealthMonitorApplicattion.getApplication().getRetrofitAdapter().create(IPushNotification.class);
        final Call<IPushNotification.InsertNotification> insertNotificationCall = notifi.INSERT_NOTIFICATION_CALL(userMail, gcmToken);
        insertNotificationCall.enqueue(new Callback<IPushNotification.InsertNotification>() {
            @Override
            public void onResponse(Call<IPushNotification.InsertNotification> call, Response<IPushNotification.InsertNotification> response) {
                if (response.isSuccessful()) {
                    Log.e("Main: ", "enviando token 3");
                    insertNotification = response.body();
                    if (insertNotification != null) {
                        if (insertNotification.getCodigo() == 0) {
                            Log.e(TAG, "Exitooooooooooo");
                        } else {
                            Log.e(TAG, "No saved");
                        }
                    } else
                        Log.e(TAG, "Fail!!!");
                }
            }

            @Override
            public void onFailure(Call<IPushNotification.InsertNotification> call, Throwable t) {
                Log.e("Failure", t.toString());

            }
        });


    }

    public void redireccionar_evento(int idSeccion) {
        Intent i = null;
        switch (idSeccion) {
            case 0:
                Log.e(TAG, "No definido: " + idSeccion);
                break;
            case 1:
                Log.e(TAG, "Evento Notificaciones Medicas :" + idSeccion);
                i = new Intent(this, NotificationsMedicalActivity.class);
                break;
            default:
                break;
        }
        if (i != null) {
            i.setFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
            startActivity(i);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

//        if(_networkStateReceiver!=null)
//            unregisterReceiver(_networkStateReceiver);
    }


    /**
     * Método para realizar el registro del Google CLoud Messaging
     */
    private void registrarGCM() {
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //mRegistrationProgressBar.setVisibility(ProgressBar.GONE);
                SharedPreferences sharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(context);
                boolean sentToken = sharedPreferences
                        .getBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false);
                if (sentToken) {
                    Log.d(TAG, getString(R.string.gcm_send_message));
                } else {
                    Log.d(TAG, getString(R.string.token_error_message));
                }
            }
        };

        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }
    }

    /**
     * Método que valida si se tiene instalado y con la versión requerida el Google Play Service
     * ya que sin ello, la nueva forma de implementación del GCM no funcionaría
     * TODO: Descomentar cuando se obtengan el APIKey y el archivo json requeridos para implementar...
     * TODO...el GCM en la aplicación
     */
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    public void generarAlertaNoRegistrado() {
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("" + getString(R.string.txt_atencion))
                .setContentText("" + getString(R.string.error_login))
                .setConfirmText("OK")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();

                        notificaciones("");//CAMBIO
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);  // envia al login
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    }
                })
                .show();
    }

    public void generarAlertaNoRegistrado(String message) {
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("" + getString(R.string.txt_atencion))
                .setContentText(message)
                .setConfirmText("OK")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();

                        notificaciones("");//CAMBIO
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);  // envia al login
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    }
                })
                .show();
    }

    private void generateAlertDialog(String Title, String message) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("" + Title);
        alert.setMessage("" + message);
        alert.setCancelable(false);
        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface alert, int id) {
                //finalizar aplicacion si no existe conexion a internet
                finish();
            }
        });
        alert.show();
    }

    public void generateAlertDialogCerrar(String Title, String message) {

        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("" + Title)
                .setContentText("" + message)
                .setConfirmText("Confirmar")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                        //eliminar preferencias
                        DeletePreferencesCallMainActivity();
                        AssistantService.stopService(getApplicationContext());
                        NotificationHelper.Current.cancelAllNotifications(getApplicationContext());
                        BarometerService.Current.stopService(getApplicationContext());
                    }
                })
                .setCancelText("Cancelar")
                .showCancelButton(true)
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.cancel();
                    }
                })
                .show();
    }

    //eliminar preferencias de usuario
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


    public void showHashKey(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo("com.grupocisc.healthmonitor",
                    PackageManager.GET_SIGNATURES);
            for (android.content.pm.Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());

                String sign = Base64.encodeToString(md.digest(), Base64.DEFAULT);
                Log.e("KeyHash:", sign);
                //  Toast.makeText(getApplicationContext(),sign,     Toast.LENGTH_LONG).show();
            }
            Log.d("KeyHash:", "****------------***");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
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
