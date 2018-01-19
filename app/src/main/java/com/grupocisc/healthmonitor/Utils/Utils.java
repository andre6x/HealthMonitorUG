package com.grupocisc.healthmonitor.Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.grupocisc.healthmonitor.HealthMonitorApplicattion;
import com.grupocisc.healthmonitor.Home.activities.MainActivity;
import com.grupocisc.healthmonitor.R;
import com.grupocisc.healthmonitor.Services.AlarmGetAllMedicineService;
import com.grupocisc.healthmonitor.entities.EAlarmDetails;
import com.grupocisc.healthmonitor.entities.EAlarmReminderTime;
import com.grupocisc.healthmonitor.entities.EAlarmReminderType;
import com.grupocisc.healthmonitor.entities.EAlarmTakeMedicine;
import com.grupocisc.healthmonitor.entities.EInsulin;
import com.grupocisc.healthmonitor.entities.EMedicineType;
import com.grupocisc.healthmonitor.entities.IAsthma;
import com.grupocisc.healthmonitor.entities.IColesterol;
import com.grupocisc.healthmonitor.entities.EMedicine;
import com.grupocisc.healthmonitor.entities.EMedicineUser;
import com.grupocisc.healthmonitor.entities.IDisease;
import com.grupocisc.healthmonitor.entities.IDoctor;
import com.grupocisc.healthmonitor.entities.IFeeding;
import com.grupocisc.healthmonitor.entities.IGlucose;
import com.grupocisc.healthmonitor.entities.IHba1c;
import com.grupocisc.healthmonitor.entities.IKetone;
import com.grupocisc.healthmonitor.entities.IMedicines;
import com.grupocisc.healthmonitor.entities.INotifcationsMedical;
import com.grupocisc.healthmonitor.entities.IPressure;
import com.grupocisc.healthmonitor.entities.IPulse;
import com.grupocisc.healthmonitor.entities.IRegisteredMedicines;
import com.grupocisc.healthmonitor.entities.IRoutineCheckLesson;
import com.grupocisc.healthmonitor.entities.IState;
import com.grupocisc.healthmonitor.entities.ITrigliceros;
import com.grupocisc.healthmonitor.entities.IWeight;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.nostra13.universalimageloader.core.ImageLoader.TAG;






/**
 * Created by aonate on 02/01/2017.
 */
public class Utils {

    private static final ImageLoader sImageLoader;
    public static final String CHANNEL = "APP";
    public static String KEY_SUSCRIBER = "isSuscriber";
    public static String KEY_MSISDN = "numero";
    public static String PREFERENCIA_INICIAL = "codigo_inicial";
    public static String PREFERENCIA_COUNTRYCODE = "Country_Code";
    public static String KEY_COUNTRYCODE = "isCountry_Code";
    //inicio preferencias usuario
    public static String PREFERENCIA_USER = "data_user";
    public static String KEY_PICTURE_URI     = "picture_uri";
    public static String KEY_EMAIL     = "email";
    public static String KEY_NOMBRE    = "nombre";
    public static String KEY_APELLIDO  = "apellido";
    public static String KEY_ANIO      = "anio";
    public static String KEY_PESO      = "peso";
    public static String KEY_ALTURA    = "altura";
    public static String KEY_SEXO      = "sexo";
    public static String KEY_ESTCIVIL  = "estcivil";//CAMBIO
    public static String KEY_PAIS      ="pais";//CAMBIO
    public static String KEY_TELEFONO   ="telefono";//CAMBIO
    public static String KEY_ASMA   ="asma";//CAMBIO
    public static String KEY_TIPO_DIABETES   ="tipo_diabetes";//CAMBIO
    public static String KEY_RECUPERA_CTA   ="recupera_cta";//CAMBIO
    public static String KEY_EMAIL_TEMP ="mail_temporal";//CAMBIO
    public static String KEY_AVISO_TEMP ="aviso_temporal";//CAMBIO


    //INI PREFERENCIAS DOCTOR
    //public static String KEY_ID_DR = "IdDr";//CAMBIO
    //public static String KEY_EMAIL_DR     = "email_dr";
    //public static String KEY_NOMBRE_DR    = "nombre_dr";
	//public static String KEY_ULTIMOPESO = "MARIUXI";
    //public static String KEY_APELLIDO_DR  = "apellido_dr";
	//public static String KEY_TELEFONO_DR  = "telefono_dr";//CAMBIO
	// public static String KEY_ESPECIALIDAD_DR = "especialidad_dr";//CAMBIO
    //FIN PREFERENCIAS DOCTOR
    //fin preferencias usuario
    private static String PREFERENCIA_PUSH = "Push_Code";
    private static String KEY_PUSH = "isPush_Code";

    private static String PREFERENCIA_PUSH_NUM = "Push_num_Code";
    private static String KEY_PUSH_NUM = "isPush_num_Code";

	public  static String PESOIDEAL ="peso_ideal";//mariuxi peso ideal						 
    public  static String DIFERENCIAPESO ="diferencia";
	
    public static ProgressDialog DialogCountry;

    private static String iDate;
    private static Date jsonDate;

    private static SimpleDateFormat dateJson = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
    private static SimpleDateFormat dateJson_2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static SimpleDateFormat dateOutput = new SimpleDateFormat("HH:mm");
    private static SimpleDateFormat dateOutputL = new SimpleDateFormat("dd 'de' MMMM, HH:mm");
    private static SimpleDateFormat dateOutputL_HomeLocal = new SimpleDateFormat("dd 'de' MMMM 'del' yyyy - HH:mm");
    private static SimpleDateFormat dateOutputL_HomeLocal_2 = new SimpleDateFormat("dd 'de' MMMM 'del' yyyy");

    public static final String OPERATION_INSERT = "I" ;
    public static final String OPERATION_UPDATE = "U" ;
    public static final String OPERATION_DELETE = "D" ;

    private static final String PREFERENCES_FILE = "materialsample_settings";

    static {
        sImageLoader = ImageLoader.getInstance();
    }


    public static String parseDateNotZone(String date) {
        try {
            jsonDate = dateJson_2.parse(date);
            iDate = dateOutputL_HomeLocal_2.format(jsonDate);
            return iDate;
        } catch (ParseException e) {
            e.printStackTrace();
        }


        return null;
    }


    /**
     * Image cache
     *
     * @param imageUri
     * @param imageView
     */
    public static void loadImage(final String imageUri, final ImageView imageView) {
        if (imageUri != null) {
            ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
            sImageLoader.displayImage(imageUri, imageView, animateFirstListener);
        }
    }
    //fin de cache


    public static Drawable tintMyDrawable(Drawable drawable, int color) {
        drawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(drawable, color);
        DrawableCompat.setTintMode(drawable, PorterDuff.Mode.SRC_IN);
        return drawable;
    }

    public static void saveSharedSetting(Context ctx, String settingName, String settingValue) {
        SharedPreferences sharedPref = ctx.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(settingName, settingValue);
        editor.apply();
    }


    //inicio toolbar
    public static void SetStyleToolbarHome(Activity act) {

        Toolbar toolbar = (Toolbar) act.findViewById(R.id.toolbar);
        ((AppCompatActivity) act).setSupportActionBar(toolbar);
        // Remove default title text
        ((AppCompatActivity) act).getSupportActionBar().setDisplayShowTitleEnabled(false);

        ImageView imgLogoToolBar = (ImageView) toolbar.findViewById(R.id.imgLogoToolBar);
        imgLogoToolBar.setVisibility(View.VISIBLE);

        TextView txtTitleToolbar = (TextView) toolbar.findViewById(R.id.txtTitleToolbar);
        txtTitleToolbar.setVisibility(View.GONE);

        /*ImageView imgSearchToolbar = (ImageView) toolbar.findViewById(R.id.imgSearchToolbar);
        imgSearchToolbar.setVisibility(View.VISIBLE);
        Buscar(imgSearchToolbar, act);*/
    }
    public static void SetStyleToolbarHome2(Activity act, String title) {

        Toolbar toolbar = (Toolbar) act.findViewById(R.id.toolbar);
        ((AppCompatActivity) act).setSupportActionBar(toolbar);
        // Remove default title text
        ((AppCompatActivity) act).getSupportActionBar().setDisplayShowTitleEnabled(false);

        ImageView imgLogoToolBar = (ImageView) toolbar.findViewById(R.id.imgLogoToolBar);
        imgLogoToolBar.setVisibility(View.GONE);

        TextView txtTitleToolbar = (TextView) toolbar.findViewById(R.id.txtTitleToolbar);
        txtTitleToolbar.setVisibility(View.VISIBLE);
        txtTitleToolbar.setText(title);

        /*ImageView imgSearchToolbar = (ImageView) toolbar.findViewById(R.id.imgSearchToolbar);
        imgSearchToolbar.setVisibility(View.VISIBLE);
        Buscar(imgSearchToolbar, act);*/
    }
    public static void SetStyleToolbarLogo(Activity act) {

        Toolbar toolbar = (Toolbar) act.findViewById(R.id.toolbar);
        ((AppCompatActivity) act).setSupportActionBar(toolbar);
        // Remove default title text
        ((AppCompatActivity) act).getSupportActionBar().setDisplayShowTitleEnabled(false);
        // BUTTON BACK
        toolbar.setNavigationIcon(R.mipmap.back);
        //((AppCompatActivity) act).getSupportActionBar().setDisplayUseLogoEnabled(true);

        ImageView imgLogoToolBar = (ImageView) toolbar.findViewById(R.id.imgLogoToolBar);
        imgLogoToolBar.setVisibility(View.VISIBLE);

        TextView txtTitleToolbar = (TextView) toolbar.findViewById(R.id.txtTitleToolbar);
        txtTitleToolbar.setVisibility(View.GONE);

        /*ImageView imgSearchToolbar = (ImageView) toolbar.findViewById(R.id.imgSearchToolbar);
        imgSearchToolbar.setVisibility(View.GONE);*/

    }
    //fragmnt
    public static void SetStyleToolbarTitle(Activity act, String title) {

        final float scale = act.getResources().getDisplayMetrics().density;
        int p = (int) (11 * scale + 0.5f);

        LinearLayout layout = new LinearLayout(act);
        layout.setOrientation(LinearLayout.HORIZONTAL);

        layout.setVerticalGravity(Gravity.CENTER);
        layout.setHorizontalGravity(Gravity.CENTER);
        layout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
        params.setMargins(0, 0, p, 0);
        ImageView img = new ImageView(act);
        img.setImageResource(R.mipmap.back);

        TextView txt = new TextView(act);
        txt.setTextSize(17f);
        txt.setTextColor(Color.WHITE);
        if (title != null)
            txt.setText(title);
        txt.setLayoutParams(params);
        txt.setGravity(Gravity.CENTER);

        img.setVisibility(View.INVISIBLE);

        layout.addView(txt);
        layout.addView(img);

        Toolbar toolbar = (Toolbar) act.findViewById(R.id.toolbar);
        if (toolbar != null) {

            toolbar.addView(layout);

            ((AppCompatActivity) act).setSupportActionBar(toolbar);
            // Remove default title text
            ((AppCompatActivity) act).getSupportActionBar().setDisplayShowTitleEnabled(false);
            // BUTTON BACK
            toolbar.setNavigationIcon(R.mipmap.back);
            //((AppCompatActivity) act).getSupportActionBar().setDisplayUseLogoEnabled(true);

            ImageView imgLogoToolBar = (ImageView) toolbar.findViewById(R.id.imgLogoToolBar);
            imgLogoToolBar.setVisibility(View.GONE);

            TextView txtTitleToolbar = (TextView) toolbar.findViewById(R.id.txtTitleToolbar);
            txtTitleToolbar.setVisibility(View.GONE);//VISIBLE
            txtTitleToolbar.setText(title);

            /*ImageView imgSearchToolbar = (ImageView) toolbar.findViewById(R.id.imgSearchToolbar);
            imgSearchToolbar.setVisibility(View.GONE);*/
        }
    }
    public static void SetStyleToolbarTitleShare(Activity act, String title, String share) {


        final float scale = act.getResources().getDisplayMetrics().density;
        int p_ = (int) (3 * scale + 0.5f);
        int p = (int) (11 * scale + 0.5f);

        LinearLayout layout = new LinearLayout(act);
        layout.setOrientation(LinearLayout.HORIZONTAL);

        layout.setVerticalGravity(Gravity.CENTER);
        layout.setHorizontalGravity(Gravity.CENTER);
        layout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, p, 0);
        params2.setMargins(0, 0, p_, 0);
        ImageView img = new ImageView(act);
        img.setImageResource(R.mipmap.ic_action_share);
        img.setLayoutParams(params2);

        if (share != null)
            if (share.length() > 0)
                shareImage(act, img, share);

        TextView txt = new TextView(act);
        txt.setTextSize(15f);
        txt.setTextColor(Color.WHITE);
        txt.setEllipsize(TextUtils.TruncateAt.END);
        txt.setMaxLines(2);

        if (title != null)
            txt.setText(title);
        txt.setLayoutParams(params);
        txt.setGravity(Gravity.CENTER);
        layout.addView(txt);
        layout.addView(img);


        Toolbar toolbar = (Toolbar) act.findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.addView(layout);
            ((AppCompatActivity) act).setSupportActionBar(toolbar);
            // Remove default title text
            ((AppCompatActivity) act).getSupportActionBar().setDisplayShowTitleEnabled(false);
            // BUTTON BACK
            toolbar.setNavigationIcon(R.mipmap.back);
            //((AppCompatActivity) act).getSupportActionBar().setDisplayUseLogoEnabled(true);

            ImageView imgLogoToolBar = (ImageView) toolbar.findViewById(R.id.imgLogoToolBar);
            imgLogoToolBar.setVisibility(View.GONE);

            TextView txtTitleToolbar = (TextView) toolbar.findViewById(R.id.txtTitleToolbar);
            txtTitleToolbar.setVisibility(View.GONE);
            txtTitleToolbar.setText(title);

            /*ImageView imgSearchToolbar = (ImageView) toolbar.findViewById(R.id.imgSearchToolbar);
            imgSearchToolbar.setVisibility(View.GONE);
            imgSearchToolbar.setImageResource(R.mipmap.ic_action_share);

            if (share != null)
                if (share.length() > 0)
                    shareImage(act, imgSearchToolbar, share);*/
        }
    }
    public static void SetStyleToolbarTitle2(Activity act, String title) {


        Toolbar toolbar = (Toolbar) act.findViewById(R.id.toolbar);
        if (toolbar != null) {


            ((AppCompatActivity) act).setSupportActionBar(toolbar);
            // Remove default title text
            ((AppCompatActivity) act).getSupportActionBar().setDisplayShowTitleEnabled(false);
            // BUTTON BACK
            toolbar.setNavigationIcon(R.mipmap.back);
            //((AppCompatActivity) act).getSupportActionBar().setDisplayUseLogoEnabled(true);

            ImageView imgLogoToolBar = (ImageView) toolbar.findViewById(R.id.imgLogoToolBar);
            imgLogoToolBar.setVisibility(View.GONE);

            TextView txtTitleToolbar = (TextView) toolbar.findViewById(R.id.txtTitleToolbar);
            txtTitleToolbar.setVisibility(View.VISIBLE);
            txtTitleToolbar.setText(title);

            /*ImageView imgSearchToolbar = (ImageView) toolbar.findViewById(R.id.imgSearchToolbar);
            imgSearchToolbar.setVisibility(View.GONE);*/
        }
    }
    public static void SetStyleToolbarSearch(Activity act) {

        Toolbar toolbar = (Toolbar) act.findViewById(R.id.toolbar);
        if (toolbar != null) {

            ((AppCompatActivity) act).setSupportActionBar(toolbar);
            // Remove default title text
            ((AppCompatActivity) act).getSupportActionBar().setDisplayShowTitleEnabled(false);
            // BUTTON BACK
            toolbar.setNavigationIcon(R.mipmap.back);
            //((AppCompatActivity) act).getSupportActionBar().setDisplayUseLogoEnabled(true);

            ImageView imgLogoToolBar = (ImageView) toolbar.findViewById(R.id.imgLogoToolBar);
            imgLogoToolBar.setVisibility(View.GONE);

            TextView txtTitleToolbar = (TextView) toolbar.findViewById(R.id.txtTitleToolbar);
            txtTitleToolbar.setVisibility(View.GONE);


            /*ImageView imgSearchToolbar = (ImageView) toolbar.findViewById(R.id.imgSearchToolbar);
            imgSearchToolbar.setVisibility(View.GONE);
            imgSearchToolbar.setImageResource(R.mipmap.ic_action_share);*/
        }

    }
//fin de toolbar

    public static void shareImage(final Activity act, ImageView img, final String text) {

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(Intent.EXTRA_TEXT, text);
                act.startActivity(Intent.createChooser(sharingIntent, act.getResources().getString(R.string.txt_compartir)));
            }
        });
    }
    private static void Buscar(View view, final Activity act) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(act, SearchActivity.class);
                act.startActivity(intent);
                act.overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_top);*/

            }
        });
    }

    public static void shareApp(final Context context, final String text) {


        Bitmap bitmap= BitmapFactory.decodeResource(context.getResources(),R.mipmap.splash_image);
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+"/HMShare.jpg";
        OutputStream out = null;
        File file=new File(path);
        try {
            out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        path=file.getPath();
        Uri bmpUri = Uri.parse("file://"+path);
        Intent shareIntent = new Intent();
        shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
        shareIntent.putExtra(Intent.EXTRA_TEXT, text);
        shareIntent.setType("image/jpg");
        context.startActivity(Intent.createChooser(shareIntent,"Share with"));


        /*
        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject/Title");
        intent.putExtra(android.content.Intent.EXTRA_TEXT, text);
        context.startActivity(Intent.createChooser(intent, context.getResources().getString(R.string.txt_compartir)));
        */
    }

    public static void generarSweetAlertDialogError(Activity act, String Title, String msm) {
        if (!((Activity) act).isFinishing()) {
            try {
                new SweetAlertDialog(act, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText(Title)
                        .setContentText(msm)
                        .show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void generarSweetAlertDialogWarning(Activity act, String Title, String msm) {
        if (!((Activity) act).isFinishing()) {
            try {
                new SweetAlertDialog(act, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText(Title)
                        .setContentText(msm)
                        .show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //alerta
    public static void generarAlerta(Activity act, String Title, String msm) {
        if (!((Activity) act).isFinishing()) {
            try {
                //show dialog
                AlertDialog.Builder alert = new AlertDialog.Builder(act);
                alert.setTitle("" + Title);
                alert.setMessage("" + msm);
                alert.setPositiveButton("OK", null);
                alert.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Método para crear un AlertDialog en la aplicación
     *
     * @param act
     * @param Title
     * @param msm
     */
    public static void generarAlerta(Context act, String Title, String msm) {
        AlertDialog.Builder alert = new AlertDialog.Builder(act);
        alert.setTitle("" + Title);
        alert.setMessage("" + msm);
        alert.setPositiveButton("OK", null);
        alert.show();
    }

    public static void generateToast(Context ctx , String message)
    {
        Toast t = Toast.makeText(ctx, message , Toast.LENGTH_LONG);
        t.setGravity(Gravity.BOTTOM, 50, 50);
        t.show();
    }

    /**
     * Método para crear un AlertDialog en la aplicación
     *
     * @param act
     * @param Title
     * @param msm
     */
    public static void generarAlertaMain(final Activity act,
                                               String Title,
                                               String msm,
                                               final boolean isFinish) {

        try {

            AlertDialog.Builder alert = new AlertDialog.Builder(act);
            alert.setTitle("" + Title);
            alert.setMessage("" + msm);
            alert.setCancelable(false);
            //alert.setPositiveButton("OK 2", null);
            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface alert, int id) {
                    Intent intent = new Intent(act, MainActivity.class);  // envia al main
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP); //para borrar pila de actividades
                    act.startActivity(intent);
                    if (isFinish) {
                        act.finish();
                    }
                }
            });
            alert.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Check internet Connection
     *
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
        }
        return false;
    }

    public static boolean isOnline(Activity act) {
        ConnectivityManager cm = (ConnectivityManager) act.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


    public static int isSuscrito(Context ctx) {
        return SharedPreferencesManager.getValorEsperadoInt(ctx, PREFERENCIA_INICIAL, KEY_SUSCRIBER);
    }

    //fin de alertas

    //preferencias
    /**
     * @param ctx
     * @return codigo de pais que se tiene en la preferencia
     */

    public static String getCountryCode(Context ctx) {
        return SharedPreferencesManager.getValorEsperado(ctx, PREFERENCIA_COUNTRYCODE, KEY_COUNTRYCODE);
    }

    /**
     * @param ctx
     * @return numero que se tiene en la preferencia
     */
    public static String getMsisdn(Context ctx) {
        return SharedPreferencesManager.getValorEsperado(ctx, PREFERENCIA_INICIAL, KEY_MSISDN);
    }

    /**
     * @param ctx
     * @return numero que se tiene en la preferencia
     */
    public static String getSendPush(Context ctx) {
        return SharedPreferencesManager.getValorEsperado(ctx, PREFERENCIA_PUSH, KEY_PUSH);
    }

    /**
     * @param ctx
     * @return numero que se tiene en la preferencia
     */
    public static String getSendPushMsisdn(Context ctx) {
        return SharedPreferencesManager.getValorEsperado(ctx, PREFERENCIA_PUSH_NUM, KEY_PUSH_NUM);
    }


    /*return EMAIL que se tiene en la preferencia*/
    public static String getEmailFromPreference(Context ctx){
        return SharedPreferencesManager.getValorEsperado(ctx, PREFERENCIA_USER, KEY_EMAIL);
    }

    /*return EMAIL TEMPORAL que se tiene en la preferencia*/ //CAMBIO
   /* public static String getEmailTempFromPreference(Context ctx){
        return SharedPreferencesManager.getValorEsperado(ctx, PREFERENCIA_USER, KEY_EMAIL_TEMP);
    }*/

    /*return PICTURE que se tiene en la preferencia*/
    public static String getPictureUriFromPreference(Context ctx){
        return SharedPreferencesManager.getValorEsperado(ctx, PREFERENCIA_USER, KEY_PICTURE_URI);
    }

    /*return AVISO TEMPORAL que se tiene en la preferencia*/ //CAMBIO
    public static String getAvisoTempFromPreference(Context ctx){
        return SharedPreferencesManager.getValorEsperado(ctx, PREFERENCIA_USER, KEY_AVISO_TEMP);
    }																 
    /*return NOMBRE que se tiene en la preferencia*/
    public static String getNombreFromPreference(Context ctx){
        return SharedPreferencesManager.getValorEsperado(ctx, PREFERENCIA_USER, KEY_NOMBRE);
    }

    /*return APELLIDO que se tiene en la preferencia*/
    public static String getApellidoFromPreference(Context ctx){
        return SharedPreferencesManager.getValorEsperado(ctx, PREFERENCIA_USER, KEY_APELLIDO);
    }

    /*return ANIO que se tiene en la preferencia*/
    public static String getAnioFromPreference(Context ctx){
        return SharedPreferencesManager.getValorEsperado(ctx, PREFERENCIA_USER, KEY_ANIO);
    }

    /*return PESO que se tiene en la preferencia*/
    public static String getPesoFromPreference(Context ctx){
        return SharedPreferencesManager.getValorEsperado(ctx, PREFERENCIA_USER, KEY_PESO);
    }

    public static String getTipoDiabetesFromPreference(Context ctx){
        return SharedPreferencesManager.getValorEsperado(ctx, PREFERENCIA_USER, KEY_TIPO_DIABETES);
    }

    public static String getTipoAsmaFromPreference(Context ctx){
        return SharedPreferencesManager.getValorEsperado(ctx, PREFERENCIA_USER, KEY_ASMA);
    }


    /*return ALTURA que se tiene en la preferencia*/
    public static String getAlturaFromPreference(Context ctx){
        return SharedPreferencesManager.getValorEsperado(ctx, PREFERENCIA_USER, KEY_ALTURA);
    }

    /*return SEXO que se tiene en la preferencia*/
    public static String getSexoFromPreference(Context ctx){
        return SharedPreferencesManager.getValorEsperado(ctx, PREFERENCIA_USER, KEY_SEXO);
    }

    /*return RECUPERA CUENTA que se tiene en la preferencia*/ // CAMBIO
    public static String getRctaFromPreference(Context ctx){
        return SharedPreferencesManager.getValorEsperado(ctx, PREFERENCIA_USER, KEY_RECUPERA_CTA);
    }

    /*return ESTADO_CIVIL que se tiene en la preferencia*/
    public static String getEstCivilFromPreference(Context ctx){
        return SharedPreferencesManager.getValorEsperado(ctx, PREFERENCIA_USER, KEY_ESTCIVIL);
    }

    /*return PAIS que se tiene en la preferencia*/
    public static String getPaisFromPreference(Context ctx){
        return SharedPreferencesManager.getValorEsperado(ctx, PREFERENCIA_USER, KEY_PAIS);
    }

    /*return TELEFONO que se tiene en la preferencia*/
    public static String getTelefonoFromPreference(Context ctx){
        return SharedPreferencesManager.getValorEsperado(ctx, PREFERENCIA_USER, KEY_TELEFONO);
    }

    public static String getAsmaFromPreference(Context ctx){
        return SharedPreferencesManager.getValorEsperado(ctx, PREFERENCIA_USER, KEY_ASMA);
    }



//fin de preferencias

    public static String getDate(String format){
        String Method ="[getDate]";
        Log.i("[Utils]", Method + "Init..." );
        Calendar c = Calendar.getInstance();
        String dd = c.get(Calendar.DAY_OF_MONTH) < 10? "0"+c.get(Calendar.DAY_OF_MONTH) : c.get(Calendar.DAY_OF_MONTH)+"" ;
        String mm = (c.get(Calendar.MONTH)+1) < 10? "0"+ (c.get(Calendar.MONTH)+1) : (c.get(Calendar.MONTH)+1) + "";
        String yyyy = c.get(Calendar.YEAR) + "";
        String HH =  c.get(Calendar.HOUR_OF_DAY) < 10 ? "0" + c.get(Calendar.HOUR_OF_DAY) : c.get(Calendar.HOUR_OF_DAY)  + "";
        String MM = c.get(Calendar.MINUTE) < 10 ? "0" + c.get(Calendar.MINUTE) : c.get(Calendar.MINUTE)  + "";
        String ss = c.get(Calendar.SECOND) < 10 ? "0" + c.get(Calendar.SECOND) : c.get(Calendar.SECOND)  + "";
        String sss = c.get(Calendar.MILLISECOND) < 10 ? "00" + c.get(Calendar.MILLISECOND) : ( c.get(Calendar.MILLISECOND) < 100 ? "0" : c.get(Calendar.MILLISECOND) )   + "";
        String time = HH + ":" + MM + ":" + ss + "." + sss ;
        String getDate = yyyy + "/" + mm +"/" + dd + " " + time;
        Log.i("[Utils]", Method + "Format received = " + format );

        switch (format){
            case "ddMMyyyy":{ getDate =dd + mm + yyyy ;
                break;
            }
            case "dd/MM/yyyy":{ getDate = dd + "/" + mm + "/" + yyyy ;
                break;
            }
            case "dd/MM/yyyy HH:mm:ss":{ getDate = dd + "/" + mm + "/" + yyyy + " " + HH + ":" + MM + ":" + ss;
                break;
            }
            case "dd/MM/yyyy HH:mm:ss.sss":{ getDate = dd + "/" + mm + "/" + yyyy + " " + HH + ":" + MM + ":" + ss + "." + sss;
                break;
            }
            case "yyyy/MM/dd HH:mm:ss":{ getDate = yyyy + "/" + mm + "/" + dd + " " + HH + ":" + MM + ":" + ss;
                break;
            }
            case "yyyy/MM/dd HH:mm:ss.sss":{ getDate = yyyy + "/" + mm + "/" + dd + " " + HH + ":" + MM + ":" + ss + "." + sss;
                break;
            }
            case "yyyyMMdd HH:mm:ss":{ getDate = yyyy + mm + dd + " " + HH + ":" + MM + ":" + ss;
                break;
            }
            case "yyyyMMdd HH:mm:ss.sss":{ getDate = yyyy + mm + dd + " " + HH + ":" + MM + ":" + ss + "." + sss;
                break;
            }
            case "yyyy/MM/dd":{ getDate = yyyy + "/" + mm + "/" + dd ;
                break;
            }
            case "HH:mm:ss":{ getDate = HH + ":" + MM + ":" + ss;
                break;
            }
            case "HH:mm:ss.sss":{ getDate = HH + ":" + MM + ":" + ss + "." + sss;
                break;
            }
            case "HH:mm":{ getDate = HH + ":" + MM ;
                break;
            }
            default:getDate = yyyy + "/" + mm +"/" + dd + " " + HH + ":" + MM + ":" + ss + "." + sss;
                break;
        }
        Log.i("[Utils]", Method + "Date format to return is: " + getDate );
        Log.i("[Utils]", Method + "End..." );
        return getDate;
    }


    //INICIO NOTIFCACIONES MEDICAS
    //GUARDAR
    public static void
    DbsaveNotificationsMedicalFromDatabase(String message,
                                           String tipo,
                                           String fecha,
                                           String hora,
                                           Dao<INotifcationsMedical, Integer> NotifcationsMedicalDao)
            throws java.sql.SQLException {
        try {
            INotifcationsMedical notifi = new INotifcationsMedical();
            notifi.setMensaje(message);
            notifi.setTipo(tipo);
            notifi.setFecha( getDateYYYYMMDD( fecha )  );
            notifi.setHora(hora);
            NotifcationsMedicalDao.create(notifi);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*OBTEBER REGISTROS DE LA TABLA BD*/
    public static List<INotifcationsMedical> GetNotifcationsMedicalFromDatabase(Dao<INotifcationsMedical, Integer> NotifcationsMedicalDao) throws SQLException, java.sql.SQLException {
        List<INotifcationsMedical> notifi = null;
        String query = "SELECT id, mensaje, tipo " +
                ", " + getDateStr("fecha","") +
                ", hora " +
                " FROM NotifcationsMedicalTable order by id DESC"; //LIMIT
        GenericRawResults<INotifcationsMedical> rawResults = NotifcationsMedicalDao.queryRaw(query, NotifcationsMedicalDao.getRawRowMapper());
        notifi = rawResults.getResults();
        return notifi;
    }

//FIN FE NOTIFICACIONES

    /*INICIO DATA BASE PULSE*/
    public static void DbsavePulseFromDatabase(int IdBdServer,
                                               int concentracion,
                                               String maxpressure,
                                               String minpressure,
                                               String medido,
                                               String fecha,
                                               String hora,
                                               String observacion,
                                               String enviadoServer,
                                               String operacion,
                                               Dao<IPulse, Integer> PulseDao) throws java.sql.SQLException {
        try {
            IPulse Pulse = new IPulse();
            Pulse.setIdBdServer(IdBdServer);
            Pulse.setConcentracion(concentracion);
            Pulse.setMaxPressure(maxpressure);
            Pulse.setMinPressure(minpressure);
            Pulse.setMedido(medido);
            Pulse.setFecha(getDateYYYYMMDD(fecha));
            Pulse.setHora(hora);
            Pulse.setObservacion(observacion);
            Pulse.setEnviadoServer(enviadoServer);
            Pulse.setOperacion(operacion);
            PulseDao.create(Pulse);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void UpdatePulseFromDatabase(int idPulse,
                                               int concentracion,
                                               String  maxpressure,
                                               String minpressure,
                                               String fecha,
                                               String hora,
                                               String medido,
                                               String observacion,
                                               String enviadoServer,
                                               String operacion,
                                               Dao<IPulse, Integer> PulseDao) throws SQLException, java.sql.SQLException {
        try {
            UpdateBuilder<IPulse, Integer> updateBuilder = PulseDao.updateBuilder();
            // set the criteria like you would a QueryBuilder
            updateBuilder.where().eq("id", idPulse);
            // update the value of your field(s)
            updateBuilder.updateColumnValue("concentracion"  /* column */,  concentracion  /* value */);
            updateBuilder.updateColumnValue("maxPressure"    /* column */,  maxpressure    /* value */);
            updateBuilder.updateColumnValue("minPressure"    /* column */,  minpressure    /* value */);
            updateBuilder.updateColumnValue("fecha"          /* column */, getDateYYYYMMDD(fecha)         /* value */);
            updateBuilder.updateColumnValue("hora"           /* column */,  hora           /* value */);
            updateBuilder.updateColumnValue("medido"         /* column */,  medido         /* value */);
            updateBuilder.updateColumnValue("observacion"    /* column */,  observacion    /* value */);
            updateBuilder.updateColumnValue("enviadoServer"  /* column */,  enviadoServer  /* value */);
            updateBuilder.updateColumnValue("Operacion"      /* column */,  operacion      /* value */);
            updateBuilder.update();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    /*ACTUALIZAR REGISTRO DE LA TABLA BD ENVIADO AL SERVIDOR OK*/
    public static void sendOkUpdatePulseFromDatabase(int idPulse,
                                                     String enviadoServer,
                                                     int IdRegisterDB,
                                                     Dao<IPulse, Integer> PulseDao) throws SQLException, java.sql.SQLException {
        try {
            UpdateBuilder<IPulse, Integer> updateBuilder = PulseDao.updateBuilder();
            // set the criteria like you would a QueryBuilder
            updateBuilder.where().eq("id", idPulse);
            // update the value of your field(s)
            updateBuilder.updateColumnValue("idBdServer", IdRegisterDB);
            updateBuilder.updateColumnValue("enviadoServer"/* column */, enviadoServer /* value */);
            updateBuilder.update();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*ACTUALIZAR REGISTRO DE LA TABLA BD ENVIADO AL SERVIDOR OK*/
    public static void sendOkUpdatePulseFromDatabase2(int idPulse,
                                                     String enviadoServer,
                                                     Dao<IPulse, Integer> PulseDao) throws SQLException, java.sql.SQLException {
        try {
            UpdateBuilder<IPulse, Integer> updateBuilder = PulseDao.updateBuilder();
            // set the criteria like you would a QueryBuilder
            updateBuilder.where().eq("id", idPulse);
            // update the value of your field(s)
            updateBuilder.updateColumnValue("enviadoServer"/* column */, enviadoServer /* value */);
            updateBuilder.update();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    //ELIMINAR REGISTROS DE TABLA
    public static void DeleteIPulseDB(Dao<IPulse, Integer> rowDao) throws java.sql.SQLException {
        DeleteBuilder<IPulse, Integer> deleteBuilder = rowDao.deleteBuilder();
        deleteBuilder.delete();
    }

    /*OBTEBER REGISTROS DE LA TABLA BD QUE NO SE AN ENVIADO AL SERVIDOR*/
    public static List<IPulse> GetPulseNotSendFromDatabase(Dao<IPulse, Integer> PulseDao, String sendServer) throws SQLException, java.sql.SQLException {
        List<IPulse> pulse = null;
        String query = "SELECT  id , idBdServer , concentracion , maxPressure , minPressure , medido " +
                ", fecha " +
                ", hora , observacion , enviadoServer , operacion " +
                " FROM PulseTable WHERE  enviadoServer = '"+ sendServer +"' ";
        GenericRawResults<IPulse> rawResults = PulseDao.queryRaw(query, PulseDao.getRawRowMapper());
        pulse = rawResults.getResults();
        return pulse;
    }

    //Obtiene los registros para verificar si no se han ingreado datos recientemente V3
    public static List<IPulse> GetPulseFromDataBase(Dao<IPulse,Integer> _pulseDao) throws SQLException, java.sql.SQLException
    {
        List<IPulse> _pulseCollection = null;
        String query="SELECT fecha FROM PulseTable";

        GenericRawResults<IPulse> result = _pulseDao.queryRaw(query,_pulseDao.getRawRowMapper());
        _pulseCollection = result.getResults();
        return  _pulseCollection;
    }

    /*OBTEBER REGISTROS DE LA TABLA BD*/
    public static List<IPulse> GetPulseFromDatabase(Dao<IPulse, Integer> PulseDao) throws SQLException, java.sql.SQLException {
        List<IPulse> pulse = null;
        String query = "SELECT  id , idBdServer , concentracion , maxPressure , minPressure , medido " +
                " , " + getDateStr("fecha","") +
                ", hora , observacion , enviadoServer , operacion " +
                " FROM PulseTable order by fecha DESC , hora DESC";
        GenericRawResults<IPulse> rawResults = PulseDao.queryRaw(query, PulseDao.getRawRowMapper());
        pulse = rawResults.getResults();
        return pulse;
    }
    /*OBTEBER REGISTROS DE LA TABLA BD*/
       public static List<IPulse> GetPulseFromDatabaseMinimumDate(Dao<IPulse, Integer> PulseDao, int NumNotfi) throws SQLException, java.sql.SQLException {
        List<IPulse> pulse = null;
        //String query = "SELECT  MIN(id) id FROM NotificationsTable" ;
        String query = " SELECT id , idBdServer , concentracion , maxPressure , minPressure , medido " +
                " , " + getDateStr("fecha","") +
                " , hora , observacion , enviadoServer , operacion " +
                " from   PulseTable  where  id   not in  ( SELECT    id  FROM PulseTable order by fecha DESC , hora DESC LIMIT " + NumNotfi + ")";
        GenericRawResults<IPulse> rawResults = PulseDao.queryRaw(query, PulseDao.getRawRowMapper());
        pulse = rawResults.getResults();
        return pulse;
    }
    //OBTENER DATOS POR RANGO DE FECHAS
    public static List<IPulse> GetPulseRangoFechas(Dao<IPulse, Integer> PulseDao, String fini, String ffin) throws SQLException , java.sql.SQLException
    {
        List<IPulse> pulse = null;
        String query = " SELECT id , idBdServer , concentracion , maxPressure , minPressure , medido " +
                " , " + getDateStr("fecha","") +
                " , hora , observacion , enviadoServer , operacion " +
                " from PulseTable where fecha>= "+ getDateYYYYMMDD( fini ) + " && fecha<= "+ getDateYYYYMMDD( ffin )+ " order by fecha DESC , hora DESC";
        GenericRawResults<IPulse> rawResults = PulseDao.queryRaw(query, PulseDao.getRawRowMapper());
        pulse = rawResults.getResults();
        return pulse ;
    }
    public static List<IPulse> GetIPulseFromDatabase(Dao<IPulse, Integer> IPulseDao, String mes) throws SQLException, java.sql.SQLException {
        List<IPulse> Pulse = null;
        String query = " SELECT id , idBdServer , concentracion  , maxPressure , minPressure , medido " +
                " , " + getDateStr("fecha","") +
                " , hora , observacion , enviadoServer , operacion " +
                " FROM PulseTable where substr(Fecha,6,2) = '"+mes+"' order by fecha DESC , hora DESC"; //LIMIT 5
        GenericRawResults<IPulse> rawResults = IPulseDao.queryRaw(query, IPulseDao.getRawRowMapper());
        Pulse = rawResults.getResults();
        return Pulse;
    }
    /*OBTEBER REGISTROS DE LA TABLA BD*/
    public static List<IPulse> GetIPulseDateFromDatabase(Dao<IPulse, Integer> IPulseDao, String fecha_desde, String fecha_hasta,String estado ) throws SQLException, java.sql.SQLException {
        List<IPulse> Pulse = null;
        String query = " SELECT id , idBdServer , concentracion , maxPressure , minPressure , medido " +
                " , " + getDateStr("fecha","") +
                " , hora , observacion , enviadoServer , operacion " +
                " FROM PulseTable where Fecha  >= '"+ getDateYYYYMMDD( fecha_desde ) +"' and Fecha  <= '"+ getDateYYYYMMDD( fecha_hasta ) +"'  order by fecha DESC , hora DESC" ;
               /* "and case when  '"+estado+"' = 'Presion mas Baja' then CAST((maxpressure) AS INTEGER)  <=60 " +
                "when  '"+estado+"' = 'Presion mas Alta' then CAST((maxpressure) AS INTEGER) >60 " +
                "else  '"+estado+"' = 'Todos' end order by id DESC"; */
        GenericRawResults<IPulse> rawResults = IPulseDao.queryRaw(query, IPulseDao.getRawRowMapper());
        Pulse = rawResults.getResults();
        return Pulse;

    }


/*FIN DATA BASE PULSE*/

//---------------------------------------------------------------------------------------------------------//
     /*INICIO DATA BASE PESO*/

    /**
     * GUARDA DATA EN TABLA
     *
     * @param WeightDao
     * @throws java.sql.SQLException
     */
    public static void DbsaveWeightFromDatabase(int idBdServer,
                                                float peso,
                                                float masamuscular,
                                                float tmb,
                                                float dmo,
                                                float porcentajeAgua,
                                                float porcentajeGrasa,
                                                String fecha,
                                                String hora,
                                                String observacion,
                                                String enviadoServer,
                                                String operacion,
                                                Dao<IWeight, Integer> WeightDao) throws java.sql.SQLException {
        try {
            IWeight weight = new IWeight();
            weight.setIdBdServer(idBdServer);
            weight.setPeso(peso);
            weight.setMasamuscular(masamuscular);
            weight.setTmb(tmb);
            weight.setDmo(dmo);
            weight.setPorcentajeAgua(porcentajeAgua);
            weight.setPorcentajeGrasa(porcentajeGrasa);
            weight.setFecha( getDateYYYYMMDD(  fecha) );
            weight.setHora(hora);
            weight.setObservacion(observacion);
            weight.setEnviadoServer(enviadoServer);
            weight.setOperacion(operacion);
            WeightDao.create(weight);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*ACTUALIZAR REGISTROS DE LA TABLA BD*/
    public static void UpdateWeightFromDatabase(int idPeso,
                                                float peso,
                                                float masamuscular,
                                                float tmb,
                                                float dmo,
                                                float porcentajeAgua,
                                                float porcentajeGrasa,
                                                String fecha,
                                                String hora,
                                                String observacion,
                                                String enviadoServer,
                                                String operacion,
                                                Dao<IWeight, Integer> WeightDao) throws SQLException, java.sql.SQLException {
        try {
            UpdateBuilder<IWeight, Integer> updateBuilder = WeightDao.updateBuilder();
            // set the criteria like you would a QueryBuilder
            updateBuilder.where().eq("id", idPeso);
            // update the value of your field(s)
            updateBuilder.updateColumnValue("peso"            /* column */, peso   /* value */);
            updateBuilder.updateColumnValue("masamuscular"    /* column */, masamuscular   /* value */);
            updateBuilder.updateColumnValue("tmb"             /* column */, tmb   /* value */);
            updateBuilder.updateColumnValue("dmo"             /* column */, dmo   /* value */);
            updateBuilder.updateColumnValue("porcentajeAgua"  /* column */, porcentajeAgua   /* value */);
            updateBuilder.updateColumnValue("porcentajeGrasa"  /* column */, porcentajeGrasa   /* value */);
            updateBuilder.updateColumnValue("fecha"        /* column */, getDateYYYYMMDD( fecha )        /* value */);
            updateBuilder.updateColumnValue("hora"         /* column */, hora          /* value */);
            updateBuilder.updateColumnValue("observacion"  /* column */, observacion   /* value */);
            updateBuilder.updateColumnValue("enviadoServer"/* column */, enviadoServer /* value */);
            updateBuilder.updateColumnValue("operacion"    /* column */, operacion /* value */);
            updateBuilder.update();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    /*ACTUALIZAR REGISTRO DE LA TABLA BD ENVIADO AL SERVIDOR OK*/
    public static void sendOkUpdateWeightFromDatabase(int idWeight,
                                                     String enviadoServer,
                                                     int IdRegisterDB,
                                                     Dao<IWeight, Integer> WeightDao) throws SQLException, java.sql.SQLException {
        try {
            UpdateBuilder<IWeight, Integer> updateBuilder = WeightDao.updateBuilder();
            // set the criteria like you would a QueryBuilder
            updateBuilder.where().eq("id", idWeight);
            // update the value of your field(s)
            updateBuilder.updateColumnValue("idBdServer", IdRegisterDB);
            updateBuilder.updateColumnValue("enviadoServer"/* column */, enviadoServer /* value */);
            updateBuilder.update();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    /*ACTUALIZAR REGISTRO DE LA TABLA BD ENVIADO AL SERVIDOR OK*/
    public static void sendOkUpdateWeightFromDatabase2(int idWeight,
                                                      String enviadoServer,
                                                      Dao<IWeight, Integer> WeightDao) throws SQLException, java.sql.SQLException {
        try {
            UpdateBuilder<IWeight, Integer> updateBuilder = WeightDao.updateBuilder();
            // set the criteria like you would a QueryBuilder
            updateBuilder.where().eq("id", idWeight);
            // update the value of your field(s)
            updateBuilder.updateColumnValue("enviadoServer"/* column */, enviadoServer /* value */);
            updateBuilder.update();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*ELIMINAR*/
    public static void DeleterowsIWeight(Dao<IWeight, Integer> rowDao) throws java.sql.SQLException {
        DeleteBuilder<IWeight, Integer> deleteBuilder = rowDao.deleteBuilder();
        deleteBuilder.delete();
    }

    /*OBTEBER REGISTROS DE LA TABLA BD*/
    public static List<IWeight> GetWeightFromDatabase(Dao<IWeight, Integer> WeightDao) throws SQLException, java.sql.SQLException {
        List<IWeight> weight = null;
        String query = " SELECT id, idBdServer , peso, masamuscular, tmb, dmo, porcentajeAgua, porcentajeGrasa " +
                ", " + getDateStr("fecha","") +
                ", hora, observacion, enviadoServer , operacion " +
                " FROM WeightTable order by fecha DESC , hora DESC"; //LIMIT 5
        GenericRawResults<IWeight> rawResults = WeightDao.queryRaw(query, WeightDao.getRawRowMapper());
        weight = rawResults.getResults();
        return weight;
    }

    public static List<IWeight> GetWeightFromDatabaseMinimumDate(Dao<IWeight, Integer> WeightDao, int NumNotfi) throws SQLException, java.sql.SQLException {
        List<IWeight> weight = null;
        //String query = "SELECT  MIN(id) id FROM NotificationsTable" ;
        String query = " SELECT id, idBdServer , peso, masamuscular, tmb, dmo, porcentajeAgua, porcentajeGrasa " +
                ", " + getDateStr("fecha","") +
                ", hora, observacion, enviadoServer , operacion " +
                " from   WeightTable  where  id   not in  ( SELECT    id  FROM WheightTable order by fecha DESC , hora DESC LIMIT " + NumNotfi + ")";
        GenericRawResults<IWeight> rawResults = WeightDao.queryRaw(query, WeightDao.getRawRowMapper());
        weight = rawResults.getResults();
        return weight;
    }
    //OBTENER DATOS POR RANGO DE FECHAS
    public static List<IWeight> GetWeightRangoFechas(Dao<IWeight, Integer> WeightDao, String fecha_desde, String fecha_hasta) throws SQLException , java.sql.SQLException
    {
        List<IWeight> weight = null;
        String query = " SELECT id, idBdServer , peso, masamuscular, tmb, dmo, porcentajeAgua, porcentajeGrasa " +
                ", " + getDateStr("fecha","") +
                ", hora, observacion, enviadoServer , operacion " +
                " FROM WeightTable where Fecha  >= '"+ getDateYYYYMMDD( fecha_desde ) +"' and Fecha  <= '"+ getDateYYYYMMDD( fecha_hasta ) +"'  order by fecha DESC , hora DESC" ;
        GenericRawResults<IWeight> rawResults = WeightDao.queryRaw(query, WeightDao.getRawRowMapper());
        weight = rawResults.getResults();
        return weight;
    }
    //grafico de barras

    public static List<IWeight> GetIWeightFromDatabase(Dao<IWeight, Integer> IWeightDao, String mes) throws SQLException, java.sql.SQLException {
        List<IWeight> Weight = null;
        String query = " SELECT id, idBdServer , peso, masamuscular, tmb, dmo, porcentajeAgua, porcentajeGrasa " +
                ", " + getDateStr("fecha","") +
                ", hora, observacion, enviadoServer , operacion " +
                " FROM WeightTable where substr(Fecha,6,2) = '"+mes+"' order by fecha DESC , hora DESC"; //LIMIT 5
        GenericRawResults<IWeight> rawResults = IWeightDao.queryRaw(query, IWeightDao.getRawRowMapper());
        Weight = rawResults.getResults();
        return Weight;
    }
    /*OBTEBER REGISTROS DE LA TABLA BD*/
    public static List<IWeight> GetIWeightDateFromDatabase(Dao<IWeight, Integer> IWeightDao, String fecha_desde, String fecha_hasta,String estado ) throws SQLException, java.sql.SQLException {
        List<IWeight> Weight = null;
        String query = " SELECT id, idBdServer , peso, masamuscular, tmb, dmo, porcentajeAgua, porcentajeGrasa " +
                ", " + getDateStr("fecha","") +
                ", hora, observacion, enviadoServer , operacion " +
                " FROM WeightTable where Fecha  >= '"+ getDateYYYYMMDD( fecha_desde ) +"' and Fecha  <= '"+ getDateYYYYMMDD( fecha_hasta ) +"' and " +
                "case when  '"+estado+"' = 'Pesos mas Bajos' then CAST((peso) AS INTEGER)  <=60 " +
                "when  '"+estado+"' = 'Pesos mas Altos' then CAST((peso) AS INTEGER) >60 " +
                "else  '"+estado+"' = 'Todos' end order by id DESC"; //LIMIT 5
        GenericRawResults<IWeight> rawResults = IWeightDao.queryRaw(query, IWeightDao.getRawRowMapper());
        Weight = rawResults.getResults();
        return Weight;
    }


    /*OBTEBER REGISTROS DE LA TABLA BD QUE NO SE AN ENVIADO AL SERVIDOR*/
    public static List<IWeight> GetWeightNotSendFromDatabase(Dao<IWeight, Integer> WeigthDao, String sendServer) throws SQLException, java.sql.SQLException {
        List<IWeight> Weight = null;
        String query = " SELECT id, idBdServer , peso, masamuscular, tmb, dmo, porcentajeAgua, porcentajeGrasa " +
                //", " + getDateStr("fecha","") +
                ", fecha " +
                ", hora, observacion, enviadoServer , operacion " +
                " FROM WeightTable WHERE  enviadoServer = '"+ sendServer +"' ";
        GenericRawResults<IWeight> rawResults = WeigthDao.queryRaw(query, WeigthDao.getRawRowMapper());
        Weight= rawResults.getResults();
        return Weight;
    }
  /*  public static List<IWeight> GetIWeightDateDesHastFromDatabase(Dao<IWeight, Integer> IWeightDao, String fecha_desde, String fecha_hasta) throws SQLException, java.sql.SQLException {
        List<IWeight> Weight = null;
        String query = "SELECT * FROM WeightTable where  case  when length(fecha) <= 9 " +
                "then substr(fecha ,6,4) ||'0'||substr(fecha ,4,1) ||substr(fecha ,1,2)" +
                "else substr(fecha ,7,4) ||substr(fecha ,4,2) ||substr(fecha ,1,2)" +
                "end )AS INTEGER >= '"+fecha_desde+"' and case  when length(fecha) <= 9" +
                "then substr(fecha ,6,4) ||'0'||substr(fecha ,4,1) ||substr(fecha ,1,2)" +
                "else substr(fecha ,7,4) ||substr(fecha ,4,2) ||substr(fecha ,1,2)" +
                "end )AS INTEGER <= '"+fecha_hasta+"' order by id DESC"; //LIMIT 5
        GenericRawResults<IWeight> rawResults = IWeightDao.queryRaw(query, IWeightDao.getRawRowMapper());
        Weight = rawResults.getResults();
        return Weight;
    }*/

   /*FIN DATA BASE PESO*/

//---------------------------------------------------------------------------------------------------------//

	 /*INICIO DATA BASE PRESION ARTERIAL*/

    /**
     * GUARDA DATA EN TABLA
     *
     * @param PressureDao
     * @throws java.sql.SQLException
     */
    public static void DbsavePressureFromDatabase(String maxpressure,
                                                  String minpressure,
                                                  String pulso,
                                                  String medido,
                                                  String fecha,
                                                  String hora,
                                                  String observacion,
                                                  String enviadoServer,
                                                  String operacion,
                                                  Dao<IPressure, Integer> PressureDao) throws java.sql.SQLException {
        try {
            IPressure pressure = new IPressure();
            pressure.setMaxPressure(maxpressure);
            pressure.setMinPressure(minpressure);
            pressure.setPulso(pulso);
            pressure.setMedido(medido);
            pressure.setFecha( getDateYYYYMMDD( fecha ) );
            pressure.setHora(hora);
            pressure.setObservacion(observacion);
            pressure.setEnviadoServer(enviadoServer);
            pressure.setOperacion(operacion);
            PressureDao.create(pressure);

        } catch (SQLException e) {


            e.printStackTrace();
        }
    }

    /*ACTUALIZAR REGISTROS DE LA TABLA BD*/
    public static void UpdatePressureFromDatabase(int idPresure,
                                                  String maxpressure,
                                                  String minpressure,
                                                  String pulso,
                                                  String medido,
                                                  String fecha,
                                                  String hora,
                                                  String observacion,
                                                  String enviadoServer,
                                                  String operacion,
                                                  Dao<IPressure, Integer> PressureDao) throws SQLException, java.sql.SQLException {
        try {
            UpdateBuilder<IPressure, Integer> updateBuilder = PressureDao.updateBuilder();
            // set the criteria like you would a QueryBuilder
            updateBuilder.where().eq("id", idPresure);
            // update the value of your field(s)
            updateBuilder.updateColumnValue("maxpressure"  /* column */, maxpressure   /* value */);
            updateBuilder.updateColumnValue("minpressure"  /* column */, minpressure   /* value */);
            updateBuilder.updateColumnValue("pulso"        /* column */, pulso         /* value */);
            updateBuilder.updateColumnValue("medido"       /* column */, medido        /* value */);
            updateBuilder.updateColumnValue("fecha"        /* column */, getDateYYYYMMDD( fecha )        /* value */);
            updateBuilder.updateColumnValue("hora"         /* column */, hora          /* value */);
            updateBuilder.updateColumnValue("observacion"  /* column */, observacion   /* value */);
            updateBuilder.updateColumnValue("enviadoServer"/* column */, enviadoServer /* value */);
            updateBuilder.updateColumnValue("operacion"    /* column */, operacion     /* value */);
            updateBuilder.update();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*ACTUALIZAR REGISTRO DE LA TABLA BD ENVIADO AL SERVIDOR OK*/
    public static void sendOkUpdatePressureFromDatabase(int idPresure,
                                                        String enviadoServer,
                                                        Dao<IPressure, Integer> PressureDao) throws SQLException, java.sql.SQLException {
        try {
            UpdateBuilder<IPressure, Integer> updateBuilder = PressureDao.updateBuilder();
            // set the criteria like you would a QueryBuilder
            updateBuilder.where().eq("id", idPresure);
            // update the value of your field(s)
            updateBuilder.updateColumnValue("enviadoServer"/* column */, enviadoServer /* value */);
            updateBuilder.update();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*OBTEBER REGISTROS DE LA TABLA BD QUE NO SE AN ENVIADO AL SERVIDOR*/
    public static List<IPressure> GetPressureNotSendFromDatabase(Dao<IPressure, Integer> PressureDao, String sendServer) throws SQLException, java.sql.SQLException {
        List<IPressure> pressure = null;
        String query = " SELECT id ,maxpressure ,minpressure pulso ,medido " +
                //", " + getDateStr("fecha","") +
                ", fecha " +
                ", hora ,observacion ,enviadoServer ,idBdServer ,operacion " +
                " FROM PressureTable WHERE  enviadoServer = '"+ sendServer +"' ";
        GenericRawResults<IPressure> rawResults = PressureDao.queryRaw(query, PressureDao.getRawRowMapper());
        pressure = rawResults.getResults();
        return pressure;
    }

    /*OBTEBER REGISTROS DE LA TABLA BD*/
    public static List<IPressure> GetPressureFromDatabase(Dao<IPressure, Integer> PressureDao) throws SQLException, java.sql.SQLException {
        List<IPressure> pressure = null;
        String query = "SELECT id ,maxpressure ,minpressure pulso ,medido " +
                ", " + getDateStr("fecha","") +
                ", hora ,observacion ,enviadoServer ,idBdServer ,operacion " +
                " FROM PressureTable order by fecha DESC , hora DESC";
        GenericRawResults<IPressure> rawResults = PressureDao.queryRaw(query, PressureDao.getRawRowMapper());
        pressure = rawResults.getResults();
        return pressure;
    }
    public static List<IPressure> GetPressureFromDatabaseMinimumDate(Dao<IPressure, Integer> PressureDao, int NumNotfi) throws SQLException, java.sql.SQLException {
        List<IPressure> pressure = null;
        //String query = "SELECT  MIN(id) id FROM NotificationsTable" ;
        String query = "SELECT id ,maxpressure ,minpressure pulso ,medido " +
                ", " + getDateStr("fecha","") +
                ", hora ,observacion ,enviadoServer ,idBdServer ,operacion " +
                " from   PressureTable  where  id   not in  ( SELECT    id  FROM PressureTable order by fecha DESC , hora DESC LIMIT " + NumNotfi + ")";
        GenericRawResults<IPressure> rawResults = PressureDao.queryRaw(query, PressureDao.getRawRowMapper());
        pressure = rawResults.getResults();
        return pressure;
    }
    //OBTENER DATOS POR RANGO DE FECHAS
    public static List<IPressure> GetPressureRangoFechas(Dao<IPressure, Integer> PresureDao, String fini, String ffin) throws SQLException , java.sql.SQLException
    {
        List<IPressure> pressure = null;
        String query = "SELECT id ,maxpressure ,minpressure pulso ,medido " +
                ", " + getDateStr("fecha","") +
                ", hora ,observacion ,enviadoServer ,idBdServer ,operacion " +
                " from PressureTable where fecha>= "+ getDateYYYYMMDD( fini ) + " && fecha<= "+ getDateYYYYMMDD(  ffin ) + " order by fecha DESC , hora DESC";
        GenericRawResults<IPressure> rawResults = PresureDao.queryRaw(query, PresureDao.getRawRowMapper());
        pressure = rawResults.getResults();
        return pressure ;
    }
    public static List<IPressure> GetIPressureFromDatabase(Dao<IPressure, Integer> IPressureDao, String mes) throws SQLException, java.sql.SQLException {
        List<IPressure> Pressure = null;
        String query =  "SELECT id ,maxpressure ,minpressure pulso ,medido " +
                ", " + getDateStr("fecha","") +
                ", hora ,observacion ,enviadoServer ,idBdServer ,operacion " +
                " FROM PressureTable where substr(Fecha,6,2) = '"+mes+"' order by fecha DESC , hora DESC"; //LIMIT 5
        GenericRawResults<IPressure> rawResults = IPressureDao.queryRaw(query, IPressureDao.getRawRowMapper());
        Pressure = rawResults.getResults();
        return Pressure;
    }
    /*OBTEBER REGISTROS DE LA TABLA BD*/
    public static List<IPressure> GetIPressureDateFromDatabase(Dao<IPressure, Integer> IPressureDao, String fecha_desde, String fecha_hasta,String estado ) throws SQLException, java.sql.SQLException {
        List<IPressure> Pressure = null;
        String query =  " SELECT id ,maxpressure ,minpressure pulso ,medido " +
                ", " + getDateStr("fecha","") +
                ", hora ,observacion ,enviadoServer ,idBdServer ,operacion " +
                " FROM PressureTable where Fecha  >= '"+ getDateYYYYMMDD( fecha_desde ) +"' and Fecha  <= '"+ getDateYYYYMMDD( fecha_hasta ) +"'  order by fecha DESC , hora DESC" ;
               /* "and case when  '"+estado+"' = 'Presion mas Baja' then CAST((maxpressure) AS INTEGER)  <=60 " +
                "when  '"+estado+"' = 'Presion mas Alta' then CAST((maxpressure) AS INTEGER) >60 " +
                "else  '"+estado+"' = 'Todos' end order by id DESC"; */
        GenericRawResults<IPressure> rawResults = IPressureDao.queryRaw(query, IPressureDao.getRawRowMapper());
        Pressure = rawResults.getResults();
        return Pressure;
    }
    /*FIN DATA BASE PRESION ARTERIAL*/
    /*FIN Mariuxi Sanchez*/


//---------------------------------------------------------------------------------------------------------//
    /*INICIO DATA BASE Asthma*/
    /**
     * GUARDA DATA EN TABLA
     *
     * @param AsthmaDao
     * @throws java.sql.SQLException
     */
    public static void DbsaveAsthmaFromDatabase(  int idBdServer,
                                                  String fecha,
                                                  String hora,
                                                  float flujo_maximo,
                                                  String observacion,
                                                  String enviadoServer,
                                                  String operacion,
                                                  Dao<IAsthma, Integer> AsthmaDao ) throws java.sql.SQLException {
        try {
            IAsthma asthma = new IAsthma();
            asthma.setIdBdServer(idBdServer);
            asthma.setFlujoMaximo(flujo_maximo);
            asthma.setFecha( getDateYYYYMMDD( fecha ));
            asthma.setHora(hora);
            asthma.setObservacion(observacion);
            asthma.setEnviadoServer(enviadoServer);
            asthma.setOperationDb(operacion);
            AsthmaDao.create(asthma);
        } catch (SQLException e) {


            e.printStackTrace();
        }
    }

    /*ACTUALIZAR REGISTROS DE LA TABLA BD*/
    public static void UpdateAsthmaFromDatabase(   int  id,
                                                   int  flujo_maximo,
                                                  String fecha,
                                                  String hora,
                                                  String obsevacion,
                                                  Dao<IAsthma, Integer> AsthmaDao) throws SQLException, java.sql.SQLException {
        try {
            UpdateBuilder<IAsthma, Integer> updateBuilder = AsthmaDao.updateBuilder();
            // set the criteria like you would a QueryBuilder
            updateBuilder.where().eq("id", id);
            // update the value of your field(s)
            updateBuilder.updateColumnValue("flujo_maximo"  /* column */, flujo_maximo   /* value */);
            updateBuilder.updateColumnValue("fecha"        /* column */, getDateYYYYMMDD( fecha )        /* value */);
            updateBuilder.updateColumnValue("hora"         /* column */, hora          /* value */);
            updateBuilder.updateColumnValue("observacion"  /* column */, obsevacion    /* value */);
            updateBuilder.updateColumnValue("enviadoServer"/* column */, "N"/* value */);
            updateBuilder.updateColumnValue("operationDb"    /* column */, "U"    /* value */);
            updateBuilder.update();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    /*ACTUALIZAR REGISTRO DE LA TABLA BD ENVIADO AL SERVIDOR OK*/
    public static void sendOkUpdateAsthmaFromDatabase(int idAsthma,
                                                        String enviadoServer,
                                                        Dao<IAsthma, Integer> AsthmaDao) throws SQLException, java.sql.SQLException {
        try {
            UpdateBuilder<IAsthma, Integer> updateBuilder = AsthmaDao.updateBuilder();
            // set the criteria like you would a QueryBuilder
            updateBuilder.where().eq("id", idAsthma);
            // update the value of your field(s)
            updateBuilder.updateColumnValue("enviadoServer"/* column */, enviadoServer /* value */);
            updateBuilder.update();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*ELIMINAR REGISTROS*/
    public static void DeleterowsIAsthma(Dao<IAsthma, Integer> rowDao) throws  java.sql.SQLException {
        DeleteBuilder<IAsthma, Integer> deleteBuilder = rowDao.deleteBuilder();
        deleteBuilder.delete();
    }

    /*OBTEBER REGISTROS DE LA TABLA BD QUE NO SE AN ENVIADO AL SERVIDOR*/
    public static List<IAsthma> GetAsthmaNotSendFromDatabase(Dao<IAsthma, Integer> AsthmaDao, String sendServer) throws SQLException, java.sql.SQLException {
        List<IAsthma> asthma = null;
        String query = " SELECT id , idBdServer , flujoMaximo " +
                ", fecha " +
                ", hora , observacion , enviadoServer  , operationDb " +
                " FROM AsthmaTable WHERE  enviadoServer = '"+ sendServer +"' ";
        GenericRawResults<IAsthma> rawResults = AsthmaDao.queryRaw(query, AsthmaDao.getRawRowMapper());
        asthma = rawResults.getResults();
        return asthma;
    }

    /*OBTEBER REGISTROS DE LA TABLA BD*/
    public static List<IAsthma> GetAsthmaFromDatabase(Dao<IAsthma, Integer> AsthmaDao) throws SQLException, java.sql.SQLException {
        List<IAsthma> asthma = null;
        String query = " SELECT id , idBdServer , flujoMaximo " +
                ", " + getDateStr("fecha","") +
                ", hora , observacion , enviadoServer  , operationDb " +
                " FROM AsthmaTable order by fecha DESC , hora DESC";
        GenericRawResults<IAsthma> rawResults = AsthmaDao.queryRaw(query, AsthmaDao.getRawRowMapper());
        asthma = rawResults.getResults();
        return asthma;
    }
    public static List<IAsthma> GetAsthmaFromDatabaseMinimumDate(Dao<IAsthma, Integer> AsthmaDao, int NumNotfi) throws SQLException, java.sql.SQLException {
        List<IAsthma> asthma = null;
        //String query = "SELECT  MIN(id) id FROM NotificationsTable" ;
        String query =" SELECT id , idBdServer , flujoMaximo" +
                ", " + getDateStr("fecha","") +
                ", hora , observacion , enviadoServer  , operationDb " +
                " from   AsthmaTable  where  id   not in  ( SELECT    id  FROM AsthmaTable order by fecha DESC , hora DESC LIMIT " + NumNotfi + ")";
        GenericRawResults<IAsthma> rawResults = AsthmaDao.queryRaw(query, AsthmaDao.getRawRowMapper());
        asthma = rawResults.getResults();
        return asthma;
    }
    //OBTENER DATOS POR RANGO DE FECHAS
    public static List<IAsthma> GetAsthmaRangoFechas(Dao<IAsthma, Integer> AsthmaDao, String fini, String ffin) throws SQLException , java.sql.SQLException
    {
        List<IAsthma> asthma = null;
        String query = " SELECT id , idBdServer , flujoMaximo" +
                ", " + getDateStr("fecha","") +
                ", hora , observacion , enviadoServer  , operationDb " +
                " from AsthmaTable where fecha >= "+ getDateYYYYMMDD( fini ) + " && fecha <= "+ getDateYYYYMMDD( ffin ) + " order by fecha DESC , hora DESC";
        GenericRawResults<IAsthma> rawResults = AsthmaDao.queryRaw(query, AsthmaDao.getRawRowMapper());
        asthma = rawResults.getResults();
        return asthma ;
    }
    public static List<IAsthma> GetAsthmaFromDatabase(Dao<IAsthma, Integer> AsthmaDao, String mes) throws SQLException, java.sql.SQLException {
        List<IAsthma> asthma = null;
        String query = " SELECT id , idBdServer , flujoMaximo" +
                ", " + getDateStr("fecha","") +
                ", hora , observacion , enviadoServer  , operationDb " +
                " FROM AsthmaTable where substr(Fecha,6,2) = '"+mes+"' order by fecha DESC , hora DESC"; //LIMIT 5
        GenericRawResults<IAsthma> rawResults = AsthmaDao.queryRaw(query, AsthmaDao.getRawRowMapper());
        asthma = rawResults.getResults();
        return asthma;
    }
    /*OBTEBER REGISTROS DE LA TABLA BD*/
    public static List<IAsthma> GetAsthmaDateFromDatabase(Dao<IAsthma, Integer> AsthmaDao, String fecha_desde, String fecha_hasta) throws SQLException, java.sql.SQLException {
        List<IAsthma> asthma = null;
        String query = " SELECT id , idBdServer , flujoMaximo" +
                ", " + getDateStr("fecha","") +
                ", hora , observacion , enviadoServer  , operationDb " +
                " FROM AsthmaTable where Fecha  >= '"+ getDateYYYYMMDD( fecha_desde ) +"' and Fecha  <= '"+ getDateYYYYMMDD( fecha_hasta ) +"'  order by fecha DESC , hora DESC" ;

        GenericRawResults<IAsthma> rawResults = AsthmaDao.queryRaw(query, AsthmaDao.getRawRowMapper());
        asthma = rawResults.getResults();
        return asthma;
    }
    //FIN ASTHMA
//---------------------------------------------------------------------------------------------------------//
    /*INICIO STATUS DATA BASE STATE*/

    /**
     * GUARDA DATA EN TABLA
     *
     * @param StateDao
     * @throws java.sql.SQLException
     */
    public static void DbsaveStateFromDatabase(int IdBdServer,
                                               int IdStatus,
                                               String nombreStatus,
                                               String fecha,
                                               String hora,
                                               String observacion,
                                               String enviadoServer,
                                               String operationDb,
                                               Dao<IState, Integer> StateDao) throws java.sql.SQLException {
        try {
            IState state = new IState();
            state.setIdBdServer(IdBdServer);
            state.setIdStatus(IdStatus);
            state.setStatusName(nombreStatus);
            state.setFecha( getDateYYYYMMDD( fecha ) );
            state.setHora(hora);
            state.setObservacion(observacion);
            state.setEnviadoServer(enviadoServer);
            state.setOperationDb(operationDb);
            StateDao.create(state);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

/*ACTUALIZAR REGISTROS DE LA TABLA BD*/

public  static  void UpdateStateFromDatabase(int idstate,
                                             int IdStatus,
                                             String StatusName,
                                             String fecha,
                                             String hora,
                                             String observacion,
                                             String enviadoServer,
                                             String operationDb,
                                             Dao<IState, Integer> StateDao)throws SQLException, java.sql.SQLException{

    try {
        UpdateBuilder<IState, Integer> updateBuilder = StateDao.updateBuilder();
        // set the criteria like you would a QueryBuilder
        updateBuilder.where().eq("id", idstate);
        // update the value of your field(s)
        updateBuilder.updateColumnValue("fecha"        /* column */, getDateYYYYMMDD( fecha )  /* value */);
        updateBuilder.updateColumnValue("hora"         /* column */, hora   /* value */);
        updateBuilder.updateColumnValue("IdStatus"     /* column */,IdStatus        /* value */);
        updateBuilder.updateColumnValue("StatusName"     /* column */,StatusName        /* value */);
        updateBuilder.updateColumnValue("observacion"  /* column */, observacion   /* value */);
        updateBuilder.updateColumnValue("enviadoServer"/* column */, enviadoServer /* value */);
        updateBuilder.updateColumnValue("operationDb" /* column */, operationDb /* value */);;
        updateBuilder.update();
    }catch (Exception e) {
        e.printStackTrace();
    }
}

    /*ELIMINAR*/
    public static void DeleterowsIState(Dao<IState, Integer> rowDao) throws java.sql.SQLException {
        DeleteBuilder<IState, Integer> deleteBuilder = rowDao.deleteBuilder();
        deleteBuilder.delete();
    }

    /*ACTUALIZAR REGISTRO DE LA TABLA BD ENVIADO AL SERVIDOR OK*/
    public static void sendOkUpdateStateFromDatabase(int idstate,
                                                     int    IdRegisterDB       ,
                                                     String enviadoServer,
                                                     Dao<IState, Integer> StateDao) throws SQLException, java.sql.SQLException {
        try {
            UpdateBuilder<IState, Integer> updateBuilder = StateDao.updateBuilder();
            // set the criteria like you would a QueryBuilder
            updateBuilder.where().eq("id", idstate);
            // update the value of your field(s)
            updateBuilder.updateColumnValue("idBdServer"/* column */, IdRegisterDB /* value */);
            updateBuilder.updateColumnValue("enviadoServer"/* column */, enviadoServer /* value */);
            updateBuilder.update();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*ACTUALIZAR REGISTRO DE LA TABLA BD ENVIADO AL SERVIDOR OK*/
    public static void sendOkUpdateStateFromDatabase2(int idstate,
                                                     String enviadoServer,
                                                     Dao<IState, Integer> StateDao) throws SQLException, java.sql.SQLException {
        try {
            UpdateBuilder<IState, Integer> updateBuilder = StateDao.updateBuilder();
            // set the criteria like you would a QueryBuilder
            updateBuilder.where().eq("id", idstate);
            // update the value of your field(s)
            updateBuilder.updateColumnValue("enviadoServer"/* column */, enviadoServer /* value */);
            updateBuilder.update();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*OBTEBER REGISTROS DE LA TABLA BD QUE NO SE AN ENVIADO AL SERVIDOR*/
    public static List<IState> GetStateNotSendFromDatabase(Dao<IState, Integer> StateDao, String sendServer) throws SQLException, java.sql.SQLException {
        List<IState> state = null;
        String query = " SELECT id , idBdServer , IdStatus , StatusName " +
                //", " + getDateStr("fecha","") +
                ", fecha " +
                ", hora , observacion , enviadoServer  , operationDb  " +
                " FROM StateTable WHERE  enviadoServer = '"+ sendServer +"' ";
        GenericRawResults<IState> rawResults = StateDao.queryRaw(query, StateDao.getRawRowMapper());
        state = rawResults.getResults();
        return state;
    }


    /*OBTEBER REGISTROS DE LA TABLA BD*/

    public static List<IState> GetStateFromDatabase(Dao<IState, Integer> StateDao) throws java.sql.SQLException {
        Log.e(TAG,"GetStateFromDatabase");
        List<IState> state = null;
        String query = " SELECT id , idBdServer , IdStatus , StatusName " +
                ", " + getDateStr("fecha","") +
                ", hora , observacion , enviadoServer  , operationDb  " +
                " FROM StateTable order by  fecha DESC, hora DESC";
        //String query = "SELECT * FROM StateTable order by id DESC"; //LIMIT 5
        GenericRawResults<IState> rawResults = StateDao.queryRaw(query, StateDao.getRawRowMapper());
        state = rawResults.getResults();
        return state;
    }
    /*OBTEBER REGISTROS DE LA TABLA BD*/
    public static List<IState> GetIStateDateFromDatabase(Dao<IState, Integer> IStateDao, String fecha_desde, String fecha_hasta) throws  java.sql.SQLException {
        List<IState> State = null;
        //  String query = "SELECT * FROM StateTable where Fecha  >= '"+fecha_desde+"' and Fecha  <= '"+fecha_hasta+"' order by id DESC"; //LIMIT 5

        String query = " SELECT id , idBdServer , IdStatus , StatusName " +
                ", " + getDateStr("fecha","") +
                ", hora , observacion , enviadoServer  , operationDb  " +
                " FROM StateTable where Fecha  >= '"+ getDateYYYYMMDD( fecha_desde ) +"' and Fecha  <= '"+ getDateYYYYMMDD( fecha_hasta ) +"' order by id, Fecha DESC, hora DESC " ;
              /*  "' and " +
                "case when  '"+estado+"' = 'Increíble (1)' then CAST((IdStatus) AS INTEGER)  = 1 " +
                "when  '"+estado+"' = 'Bien (2)' then CAST((IdStatus) AS INTEGER) = 2 " +
                "when  '"+estado+"' = 'Normal (3)' then CAST((IdStatus) AS INTEGER) = 3 " +
                "when  '"+estado+"' = 'Mal (4)' then CAST((IdStatus) AS INTEGER) = 4 " +
                "when  '"+estado+"' = 'Horrible (5)' then CAST((IdStatus) AS INTEGER) = 5 " +
                "else  '"+estado+"' = 'Todos' end order by fecha DESC, hora DESC"; //LIMIT 5*/

       Log.e(TAG,"Estado QUERY: " + query );

        GenericRawResults<IState> rawResults = IStateDao.queryRaw(query, IStateDao.getRawRowMapper());
        State = rawResults.getResults();
        return State;
    }

    /*OBTEBER REGISTROS DE LA TABLA BD*/
    public static List<IState> GetIStateFromDatabase(Dao<IState, Integer> IStateDao, String mes) throws SQLException, java.sql.SQLException {
        List<IState> State = null;
        String query = " SELECT id , idBdServer , IdStatus , StatusName " +
                ", " + getDateStr("fecha","") +
                ", hora , observacion , enviadoServer  , operationDb  " +
                " FROM StateTable where substr(Fecha,6,2) = '"+mes+"' order by Fecha DESC,hora DESC "; //LIMIT 5

        Log.i(TAG,"QUERY= " +query);

        GenericRawResults<IState> rawResults = IStateDao.queryRaw(query, IStateDao.getRawRowMapper());

        State = rawResults.getResults();
        Log.i(TAG,"State= " +State.size());
        return State;
    }

    public static List<IState> GetStateFromDatabaseMinimumDate(Dao<IState, Integer> StateDao, int NumNotfi) throws SQLException, java.sql.SQLException {
        List<IState> state = null;
        //String query = "SELECT  MIN(id) id FROM NotificationsTable" ;
        String query = " SELECT id , idBdServer , IdStatus , StatusName " +
                ", " + getDateStr("fecha","") +
                ", hora , observacion , enviadoServer  , operationDb  " +
                " from   StateTable  where  id   not in  ( SELECT    id  FROM StateTable order by fecha DESC  ,hora DESC  LIMIT" + NumNotfi + ")";
        GenericRawResults<IState> rawResults = StateDao.queryRaw(query, StateDao.getRawRowMapper());
        state = rawResults.getResults();
        return state;
    }

    /*ELIMINAR LA FECHA MINIMA*/
    public static void DeleterowsfechaMinimaState(Dao<IState, Integer> rowDao, int idMinimo) throws SQLException, Exception {
        DeleteBuilder<IState, Integer> deleteBuilder = rowDao.deleteBuilder();
        deleteBuilder.where().eq("id", idMinimo);
        deleteBuilder.delete();
    }


    //OBTENER DATOS POR RANGO DE FECHAS

    public static List<IState> GetStateRangoFechas(Dao<IState, Integer> StateDao, String fini, String ffin) throws  java.sql.SQLException
    {
        String Method="[GetStateRangoFechas]";
        Log.i(TAG,Method + "Init..");
        List<IState> state = null;
        try {

        String query = " SELECT id , idBdServer , IdStatus , StatusName " +
                ", " + getDateStr("fecha","") +
                ", hora , observacion , enviadoServer  , operationDb  " +
                " FROM  StateTable WHERE fecha >= '"+ getDateYYYYMMDD( fini ) + "' AND  fecha <= '"+ getDateYYYYMMDD( ffin ) + "' ORDER BY fecha ASC ";
        Log.i(TAG,Method + "Query =" + query);
        Log.i(TAG,Method+"Enviando Query...");
        GenericRawResults<IState> rawResults = StateDao.queryRaw(query, StateDao.getRawRowMapper());
            Log.i(TAG,Method+"Executing Query...");
            state = rawResults.getResults();
            Log.i(TAG,Method+"Init getting List of State...");
        }catch (SQLException e){
            state=null;
            Log.e(TAG,Method+ "Error al consultar datos." +"\n" + e.getMessage());
        }
            Log.i(TAG,Method+"End...");
            return state;
    }
	/*FIN STATUS DATA BASE STATE*/


//---------------------------------------------------------------------------------------------------------//




    /**********************************************************************************************************/
    /*INSULINA*/

    /*ACTUALIZAR REGISTRO DE LA TABLA BD ENVIADO AL SERVIDOR OK*/
    public static void sendOkUpdateInsulinFromDatabase(int    idInsulinDBLocal ,
                                                       int    IdRegisterDB       ,
                                                       String enviadoServer 	,
                                                       Dao<EInsulin, Integer> InsulinDao )throws java.sql.SQLException{
        try {
            UpdateBuilder<EInsulin, Integer> updateBuilder = InsulinDao.updateBuilder();
            // set the criteria like you would a QueryBuilder
            updateBuilder.where().eq("id", idInsulinDBLocal);
            // update the value of your field(s)
            updateBuilder.updateColumnValue("idBdServer", IdRegisterDB);
            updateBuilder.updateColumnValue("enviadoServer"/* column */, enviadoServer /* value */);
            updateBuilder.update();
        }catch (Exception e) {
            e.printStackTrace();
            Log.e("[Utils]", "INSULIN" +  "Error actualizando registro SQLITE.... ");
        }
    }

    /*ACTUALIZAR REGISTRO DE LA TABLA BD ENVIADO AL SERVIDOR OK*/
    public static void sendOkUpdateInsulinFromDatabase2(int    idInsulinDBLocal ,
                                                       String enviadoServer 	,
                                                       Dao<EInsulin, Integer> InsulinDao )throws java.sql.SQLException{
        try {
            UpdateBuilder<EInsulin, Integer> updateBuilder = InsulinDao.updateBuilder();
            // set the criteria like you would a QueryBuilder
            updateBuilder.where().eq("id", idInsulinDBLocal);
            // update the value of your field(s)
            updateBuilder.updateColumnValue("enviadoServer"/* column */, enviadoServer /* value */);
            updateBuilder.update();
        }catch (Exception e) {
            e.printStackTrace();
            Log.e("[Utils]", "INSULIN" +  "Error actualizando registro SQLITE.... ");
        }
    }

    public static List<EInsulin> GetInsulinNotSendFromDatabase(Dao<EInsulin, Integer> InsulinDao, String sentServer) throws java.sql.SQLException {
        List<EInsulin> lstEInsulin = null;
        String query = "SELECT  id , idBdServer , insulina , observacion " +
                //", " + getDateStr("fecha","") +
                ", fecha " +
                ", hora , enviadoServer , operationDb " +
                " FROM InsulinTable WHERE enviadoServer = '"+ sentServer +"' ";
        try {
            GenericRawResults<EInsulin> rawResults = InsulinDao.queryRaw(query, InsulinDao.getRawRowMapper());
            lstEInsulin = rawResults.getResults();
        }catch (Exception e){
            lstEInsulin=null;
        }
        return lstEInsulin;
    }

    /**
     * GUARDA DATA EN TABLA
     *
     * @param InsulinDao
     * @throws java.sql.SQLException
     */

    public static int saveInsulinToDataBaseLocal( int idBdServer,
                                                  int  insulina         ,
                                                  String fecha            ,
                                                  String hora            ,
                                                  String observacion      ,
                                                  String enviadoServer,
                                                  String operacion,
                                                  Dao<EInsulin, Integer> InsulinDao
                                                )throws java.sql.SQLException{
        String Method ="[saveInsulinToDataBaseLocal]";
        Log.i(TAG, Method + "Init..."  );
        int iRows=0;
        try {
            EInsulin eInsulin = new EInsulin();
            eInsulin.setIdBdServer(idBdServer );
            eInsulin.setInsulina         (insulina              ) ;
            eInsulin.setObservacion      (observacion           ) ;
            eInsulin.setFecha            ( getDateYYYYMMDD( fecha )                ) ;
            eInsulin.setHora            (hora                 ); ;
            eInsulin.setEnviadoServer(enviadoServer);
            eInsulin.setOperationDb(operacion);

            iRows = InsulinDao.create(eInsulin);
            Log.i(TAG, Method + iRows + " row(s) affected." );
            Log.i(TAG, Method + " --> " + eInsulin );
            Log.i(TAG, Method + "End..."  );

        }catch (SQLException e){
            Log.e("Error creando registro"," .... ");
            e.printStackTrace();
        }
        return  iRows;
    }

    public static int updateInsulinToDataBaseLocal( int    idInsulinDBLocal ,
                                                    float  insulina         ,
                                                    String fecha            ,
                                                    String hora             ,
                                                    String observacion      ,
                                                    String enviadoServer,
                                                    String operacion,
                                                    int    idBdServer,
                                                    Dao<EInsulin, Integer> InsulinDao
                                                 )throws java.sql.SQLException{
        String Method ="[updateInsulinToDataBaseLocal]";
        Log.i(TAG, Method + "Init..."  );
        int iRows=0;
        try {

            UpdateBuilder<EInsulin, Integer> updateBuilder = InsulinDao.updateBuilder();
            // set the criteria like you would a QueryBuilder
            updateBuilder.where().eq("id", idInsulinDBLocal);
            // update the value of your field(s)
            updateBuilder.updateColumnValue("insulina", insulina );
            updateBuilder.updateColumnValue("observacion", observacion );
            updateBuilder.updateColumnValue("fecha", getDateYYYYMMDD( fecha ) );
            updateBuilder.updateColumnValue("hora", hora );
            updateBuilder.updateColumnValue("enviadoServer", enviadoServer );
            updateBuilder.updateColumnValue("operationDb", idBdServer>0?"U":"I"  );
            // Execute Update
            iRows = updateBuilder.update()  ;
            Log.i(TAG, Method + iRows + " row(s) affected." );
            Log.i(TAG, Method + "End..."  );

        }catch (SQLException e){
            Log.e("Utils","Error actualizando registro .... ");
            e.printStackTrace();
        }
        return  iRows;
    }

    public static int deleteInsulinToDataBaseLocal( int idInsulinDBLocal , Dao<EInsulin, Integer> InsulinDao )throws java.sql.SQLException{
        String Method ="[deleteInsulinToDataBaseLocal]";
        Log.i(TAG, Method + "Init..."  );
        int iRows=0;
        try {

            EInsulin eInsulin = new EInsulin();
            eInsulin.setId               (idInsulinDBLocal      ) ;
            eInsulin.setEnviadoServer    ("N");
            eInsulin.setOperationDb      ("D");
            //eInsulin.setIdServerDb        (0);
            iRows = InsulinDao.update(eInsulin);
            Log.i(TAG, Method + iRows + " row(s) affected." );
            Log.i(TAG, Method + "End..."  );

        }catch (SQLException e){
            Log.e("Utils","Error eliminando lógicamente registro .... ");
            e.printStackTrace();
        }
        return  iRows;
    }

    /*ELIMINAR TABLA*/
    public static void DeleterowsEInsulin(Dao<EInsulin, Integer> rowDao) throws  java.sql.SQLException {
        DeleteBuilder<EInsulin, Integer> deleteBuilder = rowDao.deleteBuilder();
        deleteBuilder.delete();
    }


    public static void DbsaveInsulinFromDatabase(String dosis,
                                                 String fecha,
															 
                                                 String observacion,
                                                 Dao<EInsulin, Integer> InsulinDao) throws java.sql.SQLException{

        try {
            String Method ="[DbsaveInsulinToDBLocal]";
            Log.i(TAG, Method +  "Init..."  );
            //Log.i(Database.class.getName(), "onCreate");
            //Log.i(TAG, Method + "PathDb=" + db.getPath() );
            EInsulin insulin = new EInsulin();
            //insulin.setDosis(dosis);
            insulin.setFecha( getDateYYYYMMDD( fecha) );
								  
            insulin.setObservacion(observacion);
           int iRows = InsulinDao.create(insulin);

            Log.i(TAG, Method + "iRows=" + iRows );

            Log.i(TAG, Method +  "End..."  );

        }catch (SQLException e){
            Log.e("Error creando registro"," .... ");
            e.printStackTrace();
        }
    }

    /* OBTENER REGISTROS DE LA BASE */
    public static List<EInsulin> GetInsulinFromDatabase(Dao<EInsulin, Integer> InsulinDao) throws SQLException, java.sql.SQLException{
        List<EInsulin> insulins = null;

        String query= "SELECT  id , idBdServer , insulina , observacion" +
                ", " + getDateStr("fecha","") +
                ", hora , enviadoServer , operationDb " +
                " FROM InsulinTable  order by fecha DESC";
        GenericRawResults<EInsulin> eInsulins = InsulinDao.queryRaw(query, InsulinDao.getRawRowMapper());
        insulins = eInsulins.getResults();

        return insulins;
    }
    //OBTENER DATOS POR RANGO DE FECHAS
    public static List<EInsulin> GetInsulinRangoFechas(Dao<EInsulin, Integer> InsulinDao, String fini, String ffin) throws SQLException , java.sql.SQLException
    {
        String Method ="[GetInsulinRangoFechas]";
        Log.i(TAG, Method +  "Init..."  );
        List<EInsulin> lstEInsulin = null;
        try {
            String query = "SELECT  id , idBdServer , insulina , observacion" +
                    ", " + getDateStr("fecha","") +
                    ", hora , enviadoServer , operationDb " +
                    " FROM InsulinTable WHERE fecha || ' ' || hora >= '" + getDateYYYYMMDD( fini ) + "' AND fecha || ' ' || hora <= '" + getDateYYYYMMDD( ffin ) + "' ORDER BY fecha DESC ";
            Log.i(TAG, Method + "Query =" + query  );
            Log.i(TAG, Method + "Sending query..."  );
            GenericRawResults<EInsulin> rawResults = InsulinDao.queryRaw(query, InsulinDao.getRawRowMapper());
            Log.i(TAG, Method + "Executing query..."  );
            lstEInsulin = rawResults.getResults();
            Log.i(TAG, Method + "Init getting List of Insulin..."  );

        }catch (SQLException e){
            lstEInsulin = null;
        Log.e(TAG, Method + "Error al consultar datos. " +"\n"+ e.getMessage());
        e.printStackTrace();
    }
        Log.i(TAG, Method +  "End..."  );
        return lstEInsulin;
    }
/*FIN DATA BASE INSULIN*/
//---------------------------------------------------------------------------------------------------------//

    /*INICIO DATA BASE MEDICINAS*/
    public static void DbsaveMedicinesFromDatabase(String medicine,
                                                   String dosis,
                                                   String fecha,
                                                   String hora,
                                                   String observacion,
                                                   Dao<IMedicines, Integer> IMedicineDao) throws java.sql.SQLException{

        try {
            IMedicines medi = new IMedicines();
            medi.setMedicina(medicine);
            medi.setDosis(dosis);
            medi.setFecha( getDateYYYYMMDD( fecha ) );
            medi.setHora(hora);
            medi.setObservacion(observacion);
            IMedicineDao.create(medi);
        }catch (SQLException e){
            Log.e("Error creando registro"," .... ");
            e.printStackTrace();
        }
    }



    /* OBTENER REGISTROS DE LA BASE */
    public static List<IMedicines> GetMedicinesFromDatabase(Dao<IMedicines, Integer> InsulinDao) throws SQLException, java.sql.SQLException{
        List<IMedicines> medicines = null;
        String query= "SELECT id, medicina , dosis" +
                ", " + getDateStr("fecha","") +
                ", hora , observacion " +
                " FROM IMedicineTable order by id DESC";
        GenericRawResults<IMedicines> emedicines = InsulinDao.queryRaw(query, InsulinDao.getRawRowMapper());
        medicines = emedicines.getResults();
        return medicines;
    }
    //OBTENER DATOS POR RANGO DE FECHAS
    public static List<IMedicines> GetMedicineRangoFechas(Dao<IMedicines, Integer> MedicineDao, String fini, String ffin) throws SQLException , java.sql.SQLException {
        List<IMedicines> medicine = null;
        String query = "SELECT id, medicina , dosis" +
                ", " + getDateStr("fecha","") +
                ", hora , observacion " +
                " from IMedicineTable where fecha >= " + getDateYYYYMMDD( fini ) + " && fecha <= " + getDateYYYYMMDD( ffin ) + " Order by fecha";
        GenericRawResults<IMedicines> rawResults = MedicineDao.queryRaw(query, MedicineDao.getRawRowMapper());
        medicine = rawResults.getResults();
        return medicine;
    }
    /*OBTEBER REGISTROS DE LA TABLA GRÁFICOS */
    public static List<EInsulin> GetEInsulinDateFromDatabase(Dao<EInsulin, Integer> InsulinDao, String fecha_desde, String fecha_hasta, String estado) throws SQLException, java.sql.SQLException {
        List<EInsulin> insulins = null;
        String query = "SELECT id, medicina , dosis" +
                ", " + getDateStr("fecha","") +
                ", hora , observacion " +
                " FROM InsulinTable where Fecha  >= '" + getDateYYYYMMDD( fecha_desde ) + "' and Fecha  <= '" + getDateYYYYMMDD( fecha_hasta ) + "' and " +
                "case when  '" + estado + "' = 'Dosis mas Bajas' then CAST((dosis) AS INTEGER)  <=60 " +
                "when  '" + estado + "' = 'Dosis mas Altas' then CAST((dosis) AS INTEGER) >60 " +
                "else  '" + estado + "' = 'Todos' end order by id DESC"; //LIMIT 5
        GenericRawResults<EInsulin> rawResults = InsulinDao.queryRaw(query, InsulinDao.getRawRowMapper());
        insulins = rawResults.getResults();
        return insulins;
    }
    /*FIN DATA BASE MEDICINAS*/
//---------------------------------------------------------------------------------------------------------//

    /*INICIO DATA BASE MEDICAMENTOS REGISTRADOS*/


    /**
     * GUARDA DATA EN TABLA MEDICAMENTOS REGISTRADOS
     *
     * @param RegisteredMedicinesDao
     * @throws java.sql.SQLException
     */
    public static void DbsaveRegisteredMedicineFromDatabase( String fecha,
                                                             String nombre,
                                                             String descripcion,
                                                             String presentacion,
                                                             String via,
                                                             Dao<IRegisteredMedicines, Integer> RegisteredMedicinesDao) throws java.sql.SQLException {
        try {
            IRegisteredMedicines registeredMedicines = new IRegisteredMedicines();
            registeredMedicines.setFecha( getDateYYYYMMDD( fecha ) );
            registeredMedicines.setNombre(nombre);
            registeredMedicines.setDescripcion(descripcion);
            registeredMedicines.setPresentacion(presentacion);
            registeredMedicines.setVia(via);
            RegisteredMedicinesDao.create(registeredMedicines);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void truncateMedicineDataBaseLocal(Dao<EMedicine,Integer> MedicineDao

    )throws java.sql.SQLException {
        String Method ="[truncateMedicineDataBaseLocal]";
        Log.i(TAG, Method + "Init..."  );
        int iRows=0;
        try {
            iRows = MedicineDao.executeRawNoArgs("TRUNCATE TABLE MedicineTable");
            //TableUtils.clearTable( HealthMonitorApplicattion.getApplication().getMedicineDao() , EMedicine.class      );

        }catch (java.sql.SQLException e){}

    }

    public static int saveMediceToDataBaseLocal(
            int idMedicamento ,
            String Nombre,
            String Descripcion ,
            String PrincipioActivo ,
            String Indicaciones ,
            String Recomendaciones,
            String Via,
            String Presentacion,
            String Estado ,
            String Laboratorio ,
            int    medicineType,
            Dao<EMedicine,Integer> MedicineDao

    )throws java.sql.SQLException {
        String Method ="[saveMediceToDataBaseLocal]";
        Log.i(TAG, Method + "Init..."  );
        int iRows = 0;
        try {
            EMedicine eMedicine = new EMedicine();
            eMedicine.setIdMedicamento( idMedicamento );
            eMedicine.setNombre( Nombre );
            eMedicine.setDescripcion( Descripcion )  ;
            eMedicine.setPrincipioActivo( PrincipioActivo );
            eMedicine.setIndicaciones( Indicaciones );
            eMedicine.setRecomendaciones( Recomendaciones );
            eMedicine.setVia( Via );
            eMedicine.setPresentacion( Presentacion  );
            eMedicine.setEstado( Estado );
            eMedicine.setLaboratorio( Laboratorio);
            eMedicine.setMedicineTypeCode(medicineType);
            iRows = MedicineDao.create(eMedicine);
            Log.i(TAG, Method + iRows + " row(s) affected." );
        }catch (SQLException e) {
            e.printStackTrace();
        }
        Log.i(TAG, Method + "End..."  );
        return  iRows;


    }

    public static float GetTotalMedicineDBLocal( Dao<EMedicine,Integer> MedicineDao )throws java.sql.SQLException {
        List<EMedicine> lstEMedicine = null;
        String Method ="[GetMedicineDBLocal]";
        float fRows = 0 ;
        Log.i(TAG, Method + "Init..."  );
        try {
            String query="SELECT count(1) AS NumReg FROM MedicineTable ";
            Log.i(TAG, Method + "query=" + query  );
            Log.i(TAG, Method + "Init executing query..."  );
            //GenericRawResults<EMedicine> rawResults = MedicineDao.queryRaw(query, MedicineDao.getRawRowMapper());
            fRows = MedicineDao.queryRawValue(query);
            Log.i(TAG, Method + "End executing query..."  );
            Log.i(TAG, Method + "fRows=" +fRows );

        }catch (java.sql.SQLException e ){
            e.printStackTrace();
        }

        Log.i(TAG, Method + "End..."  );
        return fRows ;
    }


    public static List <EMedicine> GetMedicineDBLocal( Dao<EMedicine,Integer> MedicineDao )throws java.sql.SQLException {
        List<EMedicine> lstEMedicine = null;
        String Method ="[GetMedicineDBLocal]";
        Log.i(TAG, Method + "Init..."  );
        try {
            String query="SELECT * FROM MedicineTable ORDER BY nombre";
            Log.i(TAG, Method + "query=" +query  );
            Log.i(TAG, Method + "Executing query..."  );
            GenericRawResults<EMedicine> rawResults = MedicineDao.queryRaw(query, MedicineDao.getRawRowMapper());
            Log.i(TAG, Method + "Init getting List of Medicine..."  );
            lstEMedicine = rawResults.getResults();
            Log.i(TAG, Method + "End getting List of Medicine..."  );
        }catch (java.sql.SQLException e ){
            e.printStackTrace();
        }

        Log.i(TAG, Method + "End..."  );
        return lstEMedicine ;
    }

    public static List <EMedicine> GetMedicineByTypeDBLocal( Dao<EMedicine,Integer> MedicineDao , int medicineType )throws java.sql.SQLException {
        List<EMedicine> lstEMedicine = null;
        String Method ="[GetMedicineDBLocal]";
        Log.i(TAG, Method + "Init..."  );
        try {
            String query="SELECT * FROM MedicineTable ";
            if (medicineType != 0)
                query = query + " WHERE medicineTypeCode="+medicineType ;
            query = query + " ORDER BY nombre";

            Log.i(TAG, Method + "query=" +query  );
            Log.i(TAG, Method + "Executing query..."  );
            GenericRawResults<EMedicine> rawResults = MedicineDao.queryRaw(query, MedicineDao.getRawRowMapper());
            Log.i(TAG, Method + "Init getting List of Medicine..."  );
            lstEMedicine = rawResults.getResults();
            Log.i(TAG, Method + "End getting List of Medicine..."  );
        }catch (java.sql.SQLException e ){
            e.printStackTrace();
        }

        Log.i(TAG, Method + "End..."  );
        return lstEMedicine ;
    }


    public static List< IRegisteredMedicines > GetControlRegisteredMedicineUserDBLocal(String UserEmail ,
                                                                    String UserId ,
                                                                    Dao<IRegisteredMedicines,Integer> IRegisteredMedicinesDao )throws java.sql.SQLException {
        List<IRegisteredMedicines> lstIRegisteredMedicines = null;
        String Method ="[GetControlRegisteredMedicineUserDBLocal]";
        Log.i(TAG, Method + "Init..."  );
        try {
            String query= " SELECT " +
                    " rm.consumo_medicina , rm.descripcion  , m.via  , rm.email , rm.fecha , rm.fechaInicio , rm.fechaFin , rtp.reminderTypeCode, rtp.reminderTypeDescription, rtm.reminderTimeCode, rtm.reminderTimeDescription, rm.diasMedicacion , mt.medicineTypeCode , mt.medicineTypeDescription, m.presentacion , rm.idUsuario , m.nombre , rm.id_medicacion , rm.id , rm.veces_dia , rm.dosis ," +
                    " rm.idUsuario , rm.sentWs , rm.idServerDb " +
                    " FROM MedicineTable m " +
                    " JOIN MedicineUserTable mu ON m.idMedicamento=mu.idMedicacion " +
                    " JOIN RMedicinesTable rm ON mu.idMedicacion = rm.id_medicacion " +
                    " JOIN MedicineType mt ON m.medicineTypeCode = mt.medicineTypeCode " +
                    " JOIN AlarmReminderType rtp ON rm.reminderTypeCode = rtp.reminderTypeCode " +
                    " JOIN AlarmReminderTime rtm ON rtp.reminderTypeCode=rtm.reminderTypeCode AND rm.reminderTimeCode = rtm.reminderTimeCode " +
                    " WHERE rm.registeredMedicinesStatus = 'A' " ;
            if (UserEmail.isEmpty())
               query=query + " AND mu.idUsuario = '"+ UserId +"'"  ;
            else
                query=query + " AND mu.email = '"+ UserEmail +"'"  ;
            query=query + " ORDER BY m.nombre";

            Log.i(TAG, Method + "Executing query=" + query );
            GenericRawResults<IRegisteredMedicines> rawResults = IRegisteredMedicinesDao.queryRaw(query, IRegisteredMedicinesDao.getRawRowMapper());
            Log.i(TAG, Method + "Executed query..."  );
            Log.i(TAG, Method + "Init getting List of Control Registered Medicine..."  );
            lstIRegisteredMedicines = rawResults.getResults();
            Log.i(TAG, Method + "End getting List of Control Registered Medicine..."  );
        }catch (java.sql.SQLException e ){
            e.printStackTrace();
        }
        Log.i(TAG, Method + "End..."  );
        return lstIRegisteredMedicines ;
    }

    public static List< IRegisteredMedicines > GetControlRegisteredMedicineUserByDateRangeDBLocal(
            String UserEmail ,
            String UserId ,
            int    idMedicine,
            String DateI ,
            String DateF ,
            Dao<IRegisteredMedicines,Integer> IRegisteredMedicinesDao )throws java.sql.SQLException {
        List<IRegisteredMedicines> lstIRegisteredMedicines = null;
        String Method ="[GetControlRegisteredMedicineUserByDateRangeDBLocal]";
        Log.i(TAG, Method + "Init..."  );
        try {
            String query= " SELECT " +
                    " rm.consumo_medicina , rm.descripcion  , m.via  , rm.email , rm.fecha , atm.alarmTakeMedicineDate AS fecha_consumo , m.presentacion , rm.idUsuario , m.nombre , rm.id_medicacion , rm.id , rm.veces_dia , rm.dosis " +
                    " FROM MedicineTable m " +
                    " JOIN MedicineUserTable mu ON m.idMedicamento=mu.idMedicacion " +
                    " JOIN RMedicinesTable rm ON mu.idMedicacion = rm.id_medicacion " +
                    " JOIN EAlarmDetails ad ON rm.id = ad.registeredMedicinesId " +
                    " JOIN EAlarmTakeMedicine atm ON ad.alarmDetailId = atm.alarmDetailId " ;
            if (UserEmail.isEmpty())
                query=query + " WHERE mu.idUsuario = '"+ UserId +"'"  ;
            else
                query=query + " WHERE mu.email = '"+ UserEmail +"'"  ;
            query=query + " AND m.idMedicamento = " + idMedicine  ;
            query=query + " AND atm.alarmTakeMedicineDate >= '" + DateI + "'"  ;
            query=query + " AND atm.alarmTakeMedicineDate <= '" + DateF + "'"  ;

            query=query + " ORDER BY atm.alarmTakeMedicineDate ASC";

            Log.i(TAG, Method + "Executing query=" + query );
            GenericRawResults<IRegisteredMedicines> rawResults = IRegisteredMedicinesDao.queryRaw(query, IRegisteredMedicinesDao.getRawRowMapper());
            Log.i(TAG, Method + "Executed query..."  );
            Log.i(TAG, Method + "Init getting List of Control Registered Medicine with Medicine Code = " + idMedicine  );
            lstIRegisteredMedicines = rawResults.getResults();
            Log.i(TAG, Method + "End getting List of Control Registered Medicine with Medicine Code = " + idMedicine  );
        }catch (java.sql.SQLException e ){
            e.printStackTrace();
        }
        Log.i(TAG, Method + "End..."  );
        return lstIRegisteredMedicines ;
    }



    /*
    * @return List<EMedicine>
    *
    * @Description
    *  Retorna los medicamentos que sólo va a usar el usuario.
    *
    * */
    public static List<EMedicine> GetMedicineUserDBLocal(String UserEmail ,
                                                         String UserId ,
                                                         Dao<EMedicine,Integer> MedicineDao )throws java.sql.SQLException {
        List<EMedicine> lstEMedicine = null;
        String Method ="[GetMedicineUserDBLocal]";
        Log.i(TAG, Method + "Init..."  );
        try {
            String  query=" SELECT m.id,m.idMedicamento,m.nombre,m.descripcion,m.principioActivo," ;
                    query=query +"m.indicaciones ,m.recomendaciones ,m.via ,m.presentacion ,m.estado,";
                    query=query +"m.laboratorio ,m.email,m.idUsuario,";
                    query=query +"mu.sentWs,mu.operationDb,mu.idServerDb ";
                    query=query +" FROM MedicineTable m JOIN MedicineUserTable mu ON m.idMedicamento=mu.idMedicacion " ;
            if (UserEmail.isEmpty())
                query=query + " WHERE mu.idUsuario = '"+ UserId +"'"  ;
            else
                query=query + " WHERE mu.email = '"+ UserEmail +"'"  ;
            query=query + " ORDER BY nombre" ;

            Log.i(TAG, Method + "Executing query=" + query );
            GenericRawResults<EMedicine> rawResults = MedicineDao.queryRaw(query, MedicineDao.getRawRowMapper());
            Log.i(TAG, Method + "Executed query..."  );
            Log.i(TAG, Method + "Init getting List of Medicine..."  );
            lstEMedicine = rawResults.getResults();
            Log.i(TAG, Method + "End getting List of Medicine..."  );
        }catch (java.sql.SQLException e ){
            e.printStackTrace();
        }
        Log.i(TAG, Method + "End..."  );
        return lstEMedicine ;
    }

    public static int saveRegisteredMedicineUserToDataBaseLocal(int idMedicacion, String fechaRegistro, String email, int idServerDb,
            Dao<EMedicineUser, Integer> MedicineUserDao

    )throws java.sql.SQLException {
        String Method ="[saveRegisteredMedicineUserToDabaBaseLocal]";
        Log.i(TAG, Method + "Init..."  );
        int iRows = 0;

        try {
            EMedicineUser eMedicineUser = new EMedicineUser();
            eMedicineUser.setIdMedicacion(idMedicacion);
            eMedicineUser.setFechaRegistro(fechaRegistro);
            eMedicineUser.setMedicineUserStatus("A");
            eMedicineUser.setEmail(email);
            eMedicineUser.setOperationDb( idServerDb==0 ? "I" : "U" );
            eMedicineUser.setSentWs(idServerDb==0 ? "N" : "S");
            eMedicineUser.setIdServerDb(idServerDb);
            iRows = MedicineUserDao.create(eMedicineUser);
            Log.i(TAG, Method + iRows + " row(s) affected." );
        }catch (SQLException e) {
            e.printStackTrace();
        }
        Log.i(TAG, Method + "End..."  );
        return  iRows;
    }

//    public static int updateRegisteredMedicinesToDataBaseLocal(
//            int id_registro
//            ,int id_medicacion
//            ,float dosis
//            ,int veces_dia
//            ,String consumo_medicina
//            ,String fecha_consumo
//            ,Dao<IRegisteredMedicines, Integer> RegisteredMedicinesDao) throws java.sql.SQLException {
//        String Method ="[updateRegisteredMedicinesToDataBaseLocal]";
//        Log.i(TAG, Method + "Init..."  );
//        int iRows = 0;
//        try {
//
//            UpdateBuilder<IRegisteredMedicines, Integer> updateBuilder = RegisteredMedicinesDao.updateBuilder();
//            // set the criteria like you would a QueryBuilder
//            updateBuilder.where().eq("id", id_registro);
//            // update the value of your field(s)
//            updateBuilder.updateColumnValue("sentWs", "N" );
//            updateBuilder.updateColumnValue("operationDb", "U" );
//            updateBuilder.updateColumnValue("dosis", dosis );
//            updateBuilder.updateColumnValue("veces_dia", veces_dia );
//            updateBuilder.updateColumnValue("consumo_medicina", consumo_medicina );
//            updateBuilder.updateColumnValue("fecha_consumo", fecha_consumo );
//            // Execute Update
//            iRows = updateBuilder.update()  ;
//            Log.i(TAG, Method + iRows + " row(s) affected." );
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        Log.i(TAG, Method + "End..."  );
//        return  iRows;
//    }

    public static List<IRegisteredMedicines> GetMedicineUserControlNotSendFromDatabase(Dao<IRegisteredMedicines, Integer> IRegisteredMedicinesDao, String sentServer) throws java.sql.SQLException {
        String Method="[GetMedicineUserControlNotSendFromDatabase]";
        Log.i(TAG, Method + "Init..."  );
        List<IRegisteredMedicines> lstIRegisteredMedicines = null;
        String sentWs="N";
        if (sentServer.equals("true")) sentWs="S";
        String query = "SELECT * FROM RMedicinesTable WHERE sentWs = '"+ sentWs +"'";
        query = query + " ORDER BY id ASC ";
        Log.i(TAG, Method + "Query = " + query  );
        Log.i(TAG, Method + "Sending query..."  );
        try {
            GenericRawResults<IRegisteredMedicines> rawResults = IRegisteredMedicinesDao.queryRaw(query, IRegisteredMedicinesDao.getRawRowMapper());
            Log.i(TAG, Method + "Executing query..."  );
            lstIRegisteredMedicines = rawResults.getResults();
            Log.i(TAG, Method + "Init getting List of Medicine User Control..."  );
        }catch (Exception e){
            lstIRegisteredMedicines=null;
            Log.e(TAG, Method + "Error al consultar datos. " +"\n"+ e.getMessage());
        }
        Log.i(TAG, Method + "End..."  );
        return lstIRegisteredMedicines;
    }

    public static int sendOkUpdateMedicineUserControlFromDatabase(
            int    idMedicineUserControlDBLocal ,
            int    idServerDb                   ,
            String sentServer 			        ,
            Dao<IRegisteredMedicines, Integer> IRegisteredMedicinesDao
    )throws java.sql.SQLException{
        String Method ="[sendOkUpdateMedicineUserControlFromDatabase]";
        String sentWs = "S";
        Log.i("[Utils]", Method + "Init..."  );
        if (sentServer.equals("false")) sentWs="N" ;
        int iRows=0;
        try {
            UpdateBuilder<IRegisteredMedicines, Integer> updateBuilder = IRegisteredMedicinesDao.updateBuilder();
            // set the criteria like you would a QueryBuilder
            updateBuilder.where().eq("id", idMedicineUserControlDBLocal);
            // update the value of your field(s)
            updateBuilder.updateColumnValue("sentWs", sentWs );
            updateBuilder.updateColumnValue("idServerDb", idServerDb );
            // Execute Update
            iRows = updateBuilder.update()  ;
            Log.i("[Utils]", Method + iRows + " row(s) affected." );
        }catch (SQLException e){
            Log.e("[Utils]", Method +  "Error actualizando registro .... ");
            e.printStackTrace();
        }
        Log.i("[Utils]", Method + "End..."  );
        return  iRows;
    }





    public static int sendOkUpdateMedicineUserRegisteredFromDatabase(
            int    idMedicineUserDBLocal ,
            int    idServerDb       ,
            String sentServer 			,
            Dao<EMedicineUser, Integer> EMedicineUserDao
    )throws java.sql.SQLException{
        String Method ="[sendOkUpdateMedicineUserRegisteredFromDatabase]";
        String sentWs = "S";
        Log.i("[Utils]", Method + "Init..."  );
        if (sentServer.equals("false")) sentWs="N" ;
        int iRows=0;
        try {
            UpdateBuilder<EMedicineUser, Integer> updateBuilder = EMedicineUserDao.updateBuilder();
            // set the criteria like you would a QueryBuilder
            updateBuilder.where().eq("id", idMedicineUserDBLocal);
            // update the value of your field(s)
            updateBuilder.updateColumnValue("sentWs", sentWs );
            updateBuilder.updateColumnValue("idServerDb", idServerDb );
            // Execute Update
            iRows = updateBuilder.update()  ;
            Log.i("[Utils]", Method + iRows + " row(s) affected." );
        }catch (SQLException e){
            Log.e("[Utils]", Method +  "Error actualizando registro .... ");
            e.printStackTrace();
        }
        Log.i("[Utils]", Method + "End..."  );
        return  iRows;
    }

    public static List<EMedicineUser> GetMedicineUserRegisteredNotSendFromDatabase(Dao<EMedicineUser, Integer> EMedicineUserDao, String sentServer) throws java.sql.SQLException {
        String Method="[GetMedicineUserRegisteredNotSendFromDatabase]";
        Log.i(TAG, Method + "Init..."  );
        List<EMedicineUser> lstEMedicineUser = null;
        String sentWs="N";
        if (sentServer.equals("true")) sentWs="S";
        String query = "SELECT * FROM MedicineUserTable WHERE sentWs = '"+ sentWs +"'";
        query = query + " ORDER BY id ASC ";
        Log.i(TAG, Method + "Query = " + query  );
        Log.i(TAG, Method + "Sending query..."  );
        try {
            GenericRawResults<EMedicineUser> rawResults = EMedicineUserDao.queryRaw(query, EMedicineUserDao.getRawRowMapper());
            Log.i(TAG, Method + "Executing query..."  );
            lstEMedicineUser = rawResults.getResults();
            Log.i(TAG, Method + "Init getting List of Medicine User Registered..."  );
        }catch (Exception e){
            lstEMedicineUser=null;
            Log.e(TAG, Method + "Error al consultar datos. " +"\n"+ e.getMessage());
        }
        Log.i(TAG, Method + "End..."  );
        return lstEMedicineUser;
    }

    public static float GetTotalMedicineUserRegisteredByIdFromDBLocal( int idMedicamento ,Dao<EMedicineUser,Integer> EMedicineUserDao )throws java.sql.SQLException {
        List<EMedicine> lstEMedicine = null;
        String Method ="[GetMedicineDBLocal]";
        float fRows = 0 ;
        Log.i(TAG, Method + "Init..."  );
        try {
            String query="SELECT count(1) AS NumReg FROM MedicineUserTable mu ";
            query = query + " WHERE mu.idMedicacion = " + idMedicamento ;
            Log.i(TAG, Method + "query=" + query  );
            Log.i(TAG, Method + "Init executing query..."  );
            //GenericRawResults<EMedicine> rawResults = MedicineDao.queryRaw(query, MedicineDao.getRawRowMapper());
            fRows = EMedicineUserDao.queryRawValue(query);
            Log.i(TAG, Method + "End executing query..."  );
            Log.i(TAG, Method + "fRows=" +fRows );

        }catch (java.sql.SQLException e ){
            e.printStackTrace();
        }

        Log.i(TAG, Method + "End..."  );
        return fRows ;
    }

    public static int saveRegisteredMedicinesToDataBaseLocal(
            int id_medicacion
            ,int dosis
            ,String fechaInicio
            ,String fechaFin
            ,String reminderTypeCode
            ,int reminderTimeCode
            ,String diasMedicacion
            ,String consumo_medicina
            ,String email
            ,int idServerDb
            ,Dao<IRegisteredMedicines, Integer> RegisteredMedicinesDao) throws java.sql.SQLException {
        String Method ="[saveRegisteredMedicinesToDataBaseLocal]";
        Log.i(TAG, Method + "Init..."  );
        int iRows = 0;
        try {
            IRegisteredMedicines iRegisteredMedicines = new IRegisteredMedicines();
            iRegisteredMedicines.setId_medicacion(id_medicacion);
            iRegisteredMedicines.setDosis( dosis );
            iRegisteredMedicines.setFechaInicio(fechaInicio);
            iRegisteredMedicines.setFechaFin(fechaFin);
            iRegisteredMedicines.setReminderTypeCode(reminderTypeCode);
            iRegisteredMedicines.setReminderTimeCode(reminderTimeCode);
            iRegisteredMedicines.setVeces_dia(0);
            iRegisteredMedicines.setDiasMedicacion(diasMedicacion);
            iRegisteredMedicines.setConsumo_medicina(consumo_medicina);

            iRegisteredMedicines.setRegisteredMedicinesStatus("A");
            iRegisteredMedicines.setOperationDb ( idServerDb == 0 ? "I" : "U"  );
            // if (idServerDb == 0){iRegisteredMedicines.setOperationDb("I");}else{iRegisteredMedicines.setOperationDb("U");}
            iRegisteredMedicines.setSentWs(idServerDb == 0 ? "N" : "S");
            iRegisteredMedicines.setIdServerDb(idServerDb);
            iRegisteredMedicines.setEmail(email);
            iRows = RegisteredMedicinesDao.create(iRegisteredMedicines);
            Log.i(TAG, Method + iRows + " row(s) affected." );
            iRows = iRegisteredMedicines.getId();
            Log.i(TAG, Method + " IRegisteredMedicines.id="+iRows  );
        } catch (SQLException e) {
        e.printStackTrace();
        }
        Log.i(TAG, Method + "End..."  );
        return  iRows;
    }

    public static int updateRegisteredMedicinesToDataBaseLocal(
             int id
            ,int id_medicacion
            ,int dosis
            ,String fechaInicio
            ,String fechaFin
            ,String reminderTypeCode
            ,int reminderTimeCode
            ,String diasMedicacion
            ,String consumo_medicina
            ,String email
             , int idServerDb
            ,Dao<IRegisteredMedicines, Integer> RegisteredMedicinesDao) throws java.sql.SQLException {
        String Method ="[saveRegisteredMedicinesToDataBaseLocal]";
        Log.i(TAG, Method + "Init..."  );
        int iRows = 0;
        try {
            UpdateBuilder<IRegisteredMedicines,Integer> updateBuilder = RegisteredMedicinesDao.updateBuilder() ;
            updateBuilder.where().eq("id",id);
            updateBuilder.updateColumnValue("id_medicacion",id_medicacion);
            updateBuilder.updateColumnValue("dosis",dosis);
            updateBuilder.updateColumnValue("fechaInicio",fechaInicio);
            updateBuilder.updateColumnValue("fechaFin",fechaFin);
            updateBuilder.updateColumnValue("reminderTypeCode",reminderTypeCode);
            updateBuilder.updateColumnValue("reminderTimeCode",reminderTimeCode);
            updateBuilder.updateColumnValue("diasMedicacion",diasMedicacion);
            updateBuilder.updateColumnValue("consumo_medicina",consumo_medicina);
            updateBuilder.updateColumnValue("email",email);

            updateBuilder.updateColumnValue("idServerDb",idServerDb);
            updateBuilder.updateColumnValue("sentWs","N");
            updateBuilder.updateColumnValue("operationDb", idServerDb==0?"I":"U"  );

            iRows = updateBuilder.update();
            Log.i(TAG, Method + iRows + " row(s) affected." );

            Log.i(TAG, Method + " IRegisteredMedicines.id="+iRows  );
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Log.i(TAG, Method + "End..."  );
        return  iRows;
    }


    public static int deleteRegisteredMedicinesAndAlarmToDBLocal(
            int registeredMedicinesId
            ,String email
            ,Dao<IRegisteredMedicines, Integer> RegisteredMedicinesDao
            ,Dao<EAlarmDetails, Integer> EAlarmDetailsDao
    ) throws java.sql.SQLException {
        String Method ="[deleteRegisteredMedicinesAndAlarmToDBLocal]";
        Log.i(TAG, Method + "Init..."  );
        int iRows = 0;
        try {

            UpdateBuilder<IRegisteredMedicines, Integer> updateBuilder = RegisteredMedicinesDao.updateBuilder();
            // set the criteria like you would a QueryBuilder
            updateBuilder.where().eq("id", registeredMedicinesId);
            // update the value of your field(s)
            updateBuilder.updateColumnValue("registeredMedicinesStatus", "E" );
            // Execute Update
            iRows = updateBuilder.update()  ;
            Log.i(TAG, Method + iRows + " row(s) affected." );
            if (iRows > 0){
                UpdateBuilder<EAlarmDetails, Integer> updateBuilder2 = EAlarmDetailsDao.updateBuilder();
                // set the criteria like you would a QueryBuilder
                updateBuilder2.where().eq("registeredMedicinesId", registeredMedicinesId);
                // update the value of your field(s)
                updateBuilder2.updateColumnValue("alarmDetailStatus", "E" );
                // Execute Update
                iRows = updateBuilder2.update()  ;
                Log.i(TAG, Method + iRows + " row(s) affected." );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Log.i(TAG, Method + "End..."  );
        return iRows;
    }

    /*OBTEBER REGISTROS DE LA TABLA BD*/
    public static List<IRegisteredMedicines> GetRMedicinesFromDatabase(Dao<IRegisteredMedicines, Integer> RegisteredMedicinesDao) throws SQLException, java.sql.SQLException {
        List<IRegisteredMedicines> medicines = null;
        String query = "SELECT * FROM RMedicinesTable order by id DESC"; //LIMIT 5
        GenericRawResults<IRegisteredMedicines> rawResults = RegisteredMedicinesDao.queryRaw(query, RegisteredMedicinesDao.getRawRowMapper());
        medicines = rawResults.getResults();
        return medicines;
    }

    //OBTENER DATOS POR RANGO DE FECHAS
    public static List<IRegisteredMedicines> GetRMedicineRangoFechas(Dao<IRegisteredMedicines, Integer> RegisteredMedicinesDao, String fini, String ffin) throws SQLException , java.sql.SQLException {
        List<IRegisteredMedicines> medicine = null;
        String query = "select * from RMedicinesTable where fecha>= " + fini + " && fecha<= " + ffin + " Order by fecha";
        GenericRawResults<IRegisteredMedicines> rawResults = RegisteredMedicinesDao.queryRaw(query, RegisteredMedicinesDao.getRawRowMapper());
        medicine = rawResults.getResults();
        return medicine;
    }
    //FIN MEDICAMENTOS REGISTRADOS


//---------------------------------------------------------------------------------------------------------//

    /*INICIO DATA BASE EFERMEDAD*/
    /**
     * GUARDA DATA EN TABLA EFERMEDAD
     *
     * @param IDiseaseDao
     * @throws java.sql.SQLException
     */


    public static void DbsaveDiseaseFromDatabase(String disease,
                                                 String fecha,
                                                 String hora,
                                                 String observacion,
                                                 Dao<IDisease, Integer> IDiseaseDao) throws java.sql.SQLException{

        try {
            IDisease Disease = new IDisease();
            Disease.setDisease(disease);
            Disease.setFecha( getDateYYYYMMDD( fecha) );
            Disease.setHora(hora);
            Disease.setObservacion(observacion);
            IDiseaseDao.create(Disease);
        }catch (SQLException e){
            Log.e("Error creando registro"," .... ");
            e.printStackTrace();
        }
    }

    /* OBTENER REGISTROS DE LA BASE */
    public static List<IDisease> GetDiseaseFromDatabase(Dao<IDisease, Integer> DiseaseDao) throws SQLException, java.sql.SQLException{
        List<IDisease> Disease = null;
        String query= "SELECT id , disease" +
                "," + getDateStr("fecha","") +
                ", hora , observacion " +
                " FROM DiseaseTable order by id DESC";
        GenericRawResults<IDisease> eDisease = DiseaseDao.queryRaw(query, DiseaseDao.getRawRowMapper());
        Disease = eDisease.getResults();
        return Disease;
    }
    //OBTENER DATOS POR RANGO DE FECHAS
    public static List<IDisease> GetDiseaseRangoFechas(Dao<IDisease, Integer> DiseaseDao, String fini, String ffin) throws SQLException , java.sql.SQLException {
        List<IDisease> Disease = null;
        String query = "SELECT id , disease" +
                "," + getDateStr("fecha","") +
                ", hora , observacion " +
                " from DiseaseTable where fecha>= " + getDateYYYYMMDD( fini ) + " && fecha<= " + getDateYYYYMMDD( ffin ) + " Order by fecha";
        GenericRawResults<IDisease> rawResults =DiseaseDao.queryRaw(query, DiseaseDao.getRawRowMapper());
        Disease = rawResults.getResults();
        return Disease;

    }

     /*FIN DATA BASE EFERMEDAD*/

//---------------------------------------------------------------------------------------------------------//



        /*INICIO STATUS DATA BASE DOCTOR*/
    /**
     * GUARDA DATA EN TABLA DOCTOR
     *
     * @param DoctorDao
     * @throws java.sql.SQLException
     */
    public static void DbsaveDoctorFromDatabase(int DoctorId,
                                                String nombres,
                                                String apellidos,
                                                String email,
                                                String phone,
                                                String especialidad,
                                                Dao<IDoctor, Integer> DoctorDao) throws java.sql.SQLException {
        try {
            IDoctor doctor = new IDoctor();
            doctor.setDoctorId(DoctorId);
            doctor.setNombres(nombres);
            doctor.setApellidos(apellidos);
            doctor.setMail(email);
            doctor.setPhone(phone);
            doctor.setEspecialidad(especialidad);

            DoctorDao.create(doctor);


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*OBTEBER REGISTROS DE LA TABLA BD*/

    public static List<IDoctor> GetDoctorFromDatabase(Dao<IDoctor, Integer> DoctorDao) throws SQLException, java.sql.SQLException {
        List<IDoctor> doctor = null;
        String query = "SELECT * FROM DoctorTable order by id DESC"; //LIMIT 5


        GenericRawResults<IDoctor> rawResults = DoctorDao.queryRaw(query, DoctorDao.getRawRowMapper());
        doctor = rawResults.getResults();
        return doctor;
    }

    /*ELIMINAR LA FECHA MINIMA*/
    public static void DeleterowDoctor(Dao<IDoctor, Integer> rowDao, int id) throws SQLException, Exception {
        DeleteBuilder<IDoctor, Integer> deleteBuilder = rowDao.deleteBuilder();
        deleteBuilder.where().eq("id", id);
        deleteBuilder.delete();
    }

    public static void DeleterowsAllDoctors(Dao<IDoctor, Integer> rowDao) throws java.sql.SQLException {
        DeleteBuilder<IDoctor, Integer> deleteBuilder = rowDao.deleteBuilder();
        deleteBuilder.delete();
    }

    //---------------------------------------------------------------------------------------------------------//

    /*FIN STATUS DATA BASE DOCTOR*/
/* CALENDAR */



    public static DatePickerDialog UcallCalendar(DatePickerDialog dpd)
    {
        dpd.setThemeDark(false);//tema dark
        dpd.vibrate(true);//vibrar al selecionar
        dpd.dismissOnPause(false);
        dpd.showYearPickerFirst(false);//aparece el año primero
        dpd.setVersion(false ? DatePickerDialog.Version.VERSION_2 : DatePickerDialog.Version.VERSION_1);//false aparece v1 true v2

        if (false) {


            dpd.setAccentColor(Color.parseColor("#9C27B0"));//color customizado


        }


        if (false) {

            dpd.setTitle("DatePicker Title");//titulo cutomizado
        }


        if (false) {// rango de fechas
            Calendar date1 = Calendar.getInstance();
            Calendar date2 = Calendar.getInstance();
            date2.add(Calendar.WEEK_OF_MONTH, -1);
            Calendar date3 = Calendar.getInstance();
            date3.add(Calendar.WEEK_OF_MONTH, 1);
            Calendar[] days = {date1, date2, date3};
            dpd.setHighlightedDays(days);
        }


        if (false) {
            Calendar[] days = new Calendar[13];
            for (int i = -6; i < 7; i++) {
                Calendar day = Calendar.getInstance();
                day.add(Calendar.DAY_OF_MONTH, i * 2);
                days[i + 6] = day;
            }
            dpd.setSelectableDays(days);
        }




        return dpd;
    }

    /* TIME */
    public static TimePickerDialog UcallCalendarTime(TimePickerDialog timePickerDialog)
    {
        timePickerDialog.setThemeDark(false);//tema dark
        timePickerDialog.vibrate(true);
        timePickerDialog.dismissOnPause(false);
        timePickerDialog.enableSeconds(false);
        timePickerDialog.setVersion(false ? TimePickerDialog.Version.VERSION_2 : TimePickerDialog.Version.VERSION_1);
        if (false) {
            timePickerDialog.setAccentColor(Color.parseColor("#9C27B0"));
        }
        if (false) {
            timePickerDialog.setTitle("TimePicker Title");
        }
        if (false) { //limitSelectableTimes
            timePickerDialog.setTimeInterval(3, 5, 10);
        }
        timePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                Log.d("TimePicker", "Dialogo cancelado");
            }
        });
        return timePickerDialog;
    }

    //INICIALIZAR FECHA
    public static String inicializarFecha() {
        //obtener fecha y hora de Calendario
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        //setear fecha
        String mes =  (month+1)< 10 ? "0"+(month+1) : ""+(month+1);
        String dia =  day < 10 ? "0"+day : ""+day;
        String date = ""+dia+"/"+mes+"/"+year;

        return date;
    }

    //INICIALIZAR HORA

    public static String inicializarHora() {
        //obtener hora de Calendario
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        //setear hora
        String hourString = hour < 10 ? "0" + hour : "" + hour;
        String minuteString = minute < 10 ? "0" + minute : "" + minute;
        String time = "" + hourString + ":" + minuteString;

        return time;
    }

    //Rutinas

    public static List<IRoutineCheckLesson> GetRoutineFromDatabase(Dao<IRoutineCheckLesson, Integer> RoutineDao) throws SQLException, java.sql.SQLException {
        List<IRoutineCheckLesson> routine = null;
        String query = "SELECT * FROM rowsCheckLessonTable order by id DESC";
        GenericRawResults<IRoutineCheckLesson> rawResults = RoutineDao.queryRaw(query, RoutineDao.getRawRowMapper());
        routine = rawResults.getResults();
        return routine;
    }

    public static void DbsaveRoutineFromDatabase(String idCourse,
                                                 int idLesson,
                                                 int check,
                                                 String date_row,
                                                 Dao<IRoutineCheckLesson, Integer> RoutineDao) throws java.sql.SQLException {
        try {
            IRoutineCheckLesson routine = new IRoutineCheckLesson();
            routine.setIdmed_courses(Integer.parseInt(idCourse));
            routine.setIdmed_lesson(idLesson);
            routine.setCheck(check);
            routine.setDate_row(date_row);
            RoutineDao.create(routine);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    /***************************/

    //----------------INFORMES-------------------------------------------- //

//INSULINA
    public static List<EInsulin> selectDataInsulinDB(){
        List<EInsulin> rowsEInsulin = null;
        //obtener datos de la tabla
        try{

            //Pregunta si existen datos
            if (Utils.GetInsulinFromDatabaseInf(HealthMonitorApplicattion.getApplication().getInsulinDao()).size() > 0){
                //Si existen, asignamos los datos de la tabla a la lista de objetos
                rowsEInsulin = Utils.GetInsulinFromDatabaseInf(HealthMonitorApplicattion.getApplication().getInsulinDao());

            }
        }catch(Exception e)
        {
            Log.e("Database", "Error" + e.toString());
        }
        return rowsEInsulin;
    }

																					
										   
			

																																   
																																	  
			 
						   
		 
													  
		 
							
	 

    //PULSO
    public static List<IPulse> selectDataPulseDB(){
        List<IPulse> rowsIPulse = null;
        try{

            if (Utils.GetPulseFromDatabaseInf(HealthMonitorApplicattion.getApplication().getPulseDao()).size() > 0){
                rowsIPulse = Utils.GetPulseFromDatabaseInf(HealthMonitorApplicattion.getApplication().getPulseDao());
            }
        }catch(Exception e)
        {
            Log.e("Database", "Error" + e.toString());
        }
        return rowsIPulse;
    }

																				
									   
			

																															   
																																
			 
						   
		 
													  
		 
						  
	 

    //PRESION
    public static List<IPressure> selectDataPressureDB(){
        List<IPressure> rowsIPressure = null;
        try{

            if (Utils.GetPressureFromDatabaseInf(HealthMonitorApplicattion.getApplication().getPressureDao()).size() > 0){
                rowsIPressure = Utils.GetPressureFromDatabaseInf(HealthMonitorApplicattion.getApplication().getPressureDao());
            }
        }catch(Exception e)
        {
            Log.e("Database", "Error" + e.toString());
        }
        return rowsIPressure;
    }

																					  
											 
			

																																	 
																																		 
			 
						   
		 
													  
		 
							 
	 

    //MEDICAMENTOS
    public static List<IMedicines> selectDataMedicineDB(){
        List<IMedicines> rowsIMedicines = null;
        try{

            if (Utils.GetMedicinesFromDatabaseInf(HealthMonitorApplicattion.getApplication().getMedicinesDao()).size() > 0){
                rowsIMedicines = Utils.GetMedicinesFromDatabaseInf(HealthMonitorApplicattion.getApplication().getMedicinesDao());
            }
        }catch(Exception e)
        {
            Log.e("Database", "Error" + e.toString());
        }
        return rowsIMedicines;
    }

																					   
											   
			

																																	  
																																		   
			 
						   
		 
													  
		 
							  
	 

    //ESTADO
    public static List<IState> selectDataStateDB(){
        List<IState> rowsIState = null;
        try{

            if (Utils.GetStateFromDatabaseInf(HealthMonitorApplicattion.getApplication().getStateDao()).size() > 0){
                rowsIState = Utils.GetStateFromDatabaseInf(HealthMonitorApplicattion.getApplication().getStateDao());
            }
        }catch(Exception e)
        {
            Log.e("Database", "Error" + e.toString());
        }
        return rowsIState;
    }

																				
									   
			

																															   
																																
			 
						   
		 
													  
		 
						  
	 

    //PESO
    public static List<IWeight> selectDataWeightDB(){
        List<IWeight> rowsIWeight = null;
        try{

            if (Utils.GetWeightFromDatabaseInf(HealthMonitorApplicattion.getApplication().getWeightDao()).size() > 0){
                rowsIWeight = Utils.GetWeightFromDatabaseInf(HealthMonitorApplicattion.getApplication().getWeightDao());
            }
        }catch(Exception e)
        {
            Log.e("Database", "Error" + e.toString());
        }
        return rowsIWeight;
    }

																				  
										 
			

																																 
																																   
			 
						   
		 
													  
		 
						   
	 
    //GLUCOSA

    public static List<IGlucose> selectDataGlucoseDB(){
        List<IGlucose> rowsIGlucose = null;
        //obtener datos de la tabla
        try{

            //Pregunta si existen datos
            if (Utils.GetGlucoseFromDatabase(HealthMonitorApplicattion.getApplication().getGlucoseDao()).size() > 0){
                //Si existen, asignamos los datos de la tabla a la lista de objetos
                rowsIGlucose = Utils.GetGlucoseFromDatabase(HealthMonitorApplicattion.getApplication().getGlucoseDao());
            }
        }catch(Exception e)
        {
            Log.e("Database", "Error" + e.toString());
        }
        return rowsIGlucose;
    }

																					
										   
			

																																  
																																	 
			 
						   
													


							
	 
    //QUERYS

    /*OBTEBER REGISTROS DE LA TABLA BD*/
    public static List<IGlucose> GetGlucoseFromDatabaseInf(Dao<IGlucose, Integer> GlucoseDao) throws SQLException, java.sql.SQLException {
        String query = "SELECT id , idBdServer , concentracion " +
                ", " + getDateStr("fecha","") +
                ", hora , observacion , enviadoServer , operacion " +
                " FROM GlucoseTable order by id DESC"; //LIMIT
        GenericRawResults<IGlucose> rawResults = GlucoseDao.queryRaw(query, GlucoseDao.getRawRowMapper());
        return  rawResults.getResults();
    }

    public static List<EInsulin> GetInsulinFromDatabaseInf(Dao<EInsulin, Integer> InsulinDao) throws SQLException, java.sql.SQLException {
        String query = "SELECT id , idBdServer , insulina , observacion" +
                ", " + getDateStr("fecha","") +
                ", hora , enviadoServer , operationDb " +
                " FROM InsulinTable order by id DESC";
        GenericRawResults<EInsulin> eInsulins = InsulinDao.queryRaw(query, InsulinDao.getRawRowMapper());
        Log.e("Consulta"," Insulina ");
        return eInsulins.getResults();
    }

    public static List<IMedicines> GetMedicinesFromDatabaseInf(Dao<IMedicines, Integer> MedicinesDao) throws SQLException, java.sql.SQLException {
        String query = "SELECT id , medicina , dosis " +
                ", " + getDateStr("fecha","") +
                ", hora , observacion  " +
                " FROM IMedicineTable order by id DESC";
        GenericRawResults<IMedicines> emedicines = MedicinesDao.queryRaw(query, MedicinesDao.getRawRowMapper());
        return emedicines.getResults();
    }

    public static List<IPulse> GetPulseFromDatabaseInf(Dao<IPulse, Integer> PulseDao) throws SQLException, java.sql.SQLException {
        String query = "SELECT  id , idBdServer , concentracion , maxPressure , minPressure " +
                ", " + getDateStr("fecha","") +
                ", hora , medido , observacion , enviadoServ        er , operacion " +
                " FROM PulseTable order by id DESC"; //LIMIT
        GenericRawResults<IPulse> rawResults = PulseDao.queryRaw(query, PulseDao.getRawRowMapper());
        return rawResults.getResults();
    }

    public static List<IPressure> GetPressureFromDatabaseInf(Dao<IPressure, Integer> PressureDao) throws SQLException, java.sql.SQLException {
        List<IPressure> pressure = null;
        String query = "SELECT id , maxpressure , minpressure , pulso , medido" +
                ", " + getDateStr("fecha","") +
                ", hora , observacion , enviadoServer  , idBdServer  , operacion   " +
                " FROM PressureTable order by id DESC"; //LIMIT 5
        GenericRawResults<IPressure> rawResults = PressureDao.queryRaw(query, PressureDao.getRawRowMapper());
        pressure = rawResults.getResults();
        return pressure;
    }

    public static List<IState> GetStateFromDatabaseInf(Dao<IState, Integer> StateDao) throws SQLException, java.sql.SQLException {
        String query = " SELECT id , idBdServer , IdStatus , StatusName" +
                ", " + getDateStr("fecha","") +
                ", hora , observacion , enviadoServer  , operationDb  " +
                " FROM StateTable order by id DESC"; //LIMIT 5
        GenericRawResults<IState> rawResults = StateDao.queryRaw(query, StateDao.getRawRowMapper());
        return rawResults.getResults();
    }

    public static List<IWeight> GetWeightFromDatabaseInf(Dao<IWeight, Integer> WeightDao) throws SQLException, java.sql.SQLException {
        String query = " SELECT id, idBdServer , peso, masamuscular, tmb, dmo, porcentajeAgua, porcentajeGrasa " +
                ", " + getDateStr("fecha","") +
                ", hora, observacion, enviadoServer , operacion " +
                " FROM WeightTable order by id DESC"; //LIMIT 5
        GenericRawResults<IWeight> rawResults = WeightDao.queryRaw(query, WeightDao.getRawRowMapper());
        return rawResults.getResults();
    }

												
																																								   
																															 
																										  
									   
	 

																																								   
																																  
																										  
									   
	 

																																										 
																															   
																											  
									   
	 

																																						   
																											  
																									
									   
	 

																																									  
																																
																										   
									   
	 

																																						   
																															
																									
									   
	 

																																							   
																															
																									   
									   
	 

    //***************//


    /*OBTEBER REGISTROS DE LA TABLA BD*/
    public static List<IGlucose> GetIGlucoseDateFromDatabase(Dao<IGlucose, Integer> IGlucoseDao, String fecha_desde, String fecha_hasta,String estado ) throws SQLException, java.sql.SQLException {
        List<IGlucose> Glucose = null;
        String query = "SELECT  id , idBdServer , concentracion " +
                ", " + getDateStr("fecha","") +
                ", hora , observacion , enviadoServer , operacion " +
                " FROM GlucoseTable where Fecha  >= '"+ getDateYYYYMMDD( fecha_desde ) +"' and Fecha  <= '"+ getDateYYYYMMDD( fecha_hasta ) +"' " ;
                //"and case when  '"+estado+"' = 'Pulsos mas Bajos' then CAST((concentracion) AS INTEGER)  <=60 " +
                //"when  '"+estado+"' = 'Pulsos mas Altos' then CAST((concentracion) AS INTEGER) >60 " +
                //"else  '"+estado+"' = 'Todos' end order by id DESC"; //LIMIT 5
        System.out.println("Query=" + query);
        GenericRawResults<IGlucose> rawResults = IGlucoseDao.queryRaw(query, IGlucoseDao.getRawRowMapper());
        Glucose = rawResults.getResults();
        return Glucose;
    }

    public static void DbsaveFeedingFromDatabase(String alimento,
                                                 String fecha,
                                                 String hora,
                                                 String categoria,
                                                 String porcion,
                                                 int carbohidrato,
                                                 int proteina,
                                                 int grasa,
                                                 int caloria,
                                                 Dao<IFeeding, Integer> FeedingDao) throws java.sql.SQLException {
        try {
            IFeeding feeding = new IFeeding();
            feeding.setAlimento(alimento);
            feeding.setFecha( getDateYYYYMMDD(fecha) );
            feeding.setHora(hora);
            feeding.setCategoria(categoria);
            feeding.setPorcion(porcion);
            feeding.setCarbohidrato(carbohidrato);
            feeding.setProteina(proteina);
            feeding.setGrasa(grasa);
            feeding.setCaloria(caloria);
            FeedingDao.create(feeding);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*ACTUALIZAR REGISTRO DE LA TABLA BD ENVIADO AL SERVIDOR OK*/
    public static void sendOkUpdateGlucosaFromDatabase(int idGlucosa,
                                                       String enviadoServer,
                                                       int IdRegisterDB,
                                                       Dao<IGlucose, Integer> GlucoseDao) throws SQLException, java.sql.SQLException {
        try {
            UpdateBuilder<IGlucose, Integer> updateBuilder = GlucoseDao.updateBuilder();
            // set the criteria like you would a QueryBuilder
            updateBuilder.where().eq("id", idGlucosa);
            // update the value of your field(s)
            updateBuilder.updateColumnValue("idBdServer", IdRegisterDB);
            updateBuilder.updateColumnValue("enviadoServer"/* column */, enviadoServer /* value */);
            updateBuilder.update();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*ACTUALIZAR REGISTRO DE LA TABLA BD ENVIADO AL SERVIDOR OK*/
    public static void sendOkUpdateGlucosaFromDatabase2(int idGlucosa,
                                                       String enviadoServer,
                                                       Dao<IGlucose, Integer> GlucoseDao) throws SQLException, java.sql.SQLException {
        try {
            UpdateBuilder<IGlucose, Integer> updateBuilder = GlucoseDao.updateBuilder();
            // set the criteria like you would a QueryBuilder
            updateBuilder.where().eq("id", idGlucosa);
            // update the value of your field(s)
            updateBuilder.updateColumnValue("enviadoServer"/* column */, enviadoServer /* value */);
            updateBuilder.update();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static List<IFeeding> GetFeedingFromDatabase(Dao<IFeeding, Integer> FeedingDao) throws SQLException, java.sql.SQLException {
        List<IFeeding> feeding = null;
        String query = "SELECT id , alimento " +
                ", " + getDateStr("fecha","") +
                ", hora , categoria , porcion , carbohidrato , proteina , grasa , caloria " +
                " FROM FeedingTable order by id DESC"; //LIMIT 5
        GenericRawResults<IFeeding> rawResults = FeedingDao.queryRaw(query, FeedingDao.getRawRowMapper());
        feeding = rawResults.getResults();
        return feeding;
    }

    //AGREGADO PARA MEDICAMENTOS
    /*OBTENER NOMBRES DE MEDICAMENTOS INGRESADOS*/
    public static List<IRegisteredMedicines> GetListaMedRegistrados(Dao<IRegisteredMedicines, Integer> IMedRegistradosDao) throws java.sql.SQLException
    {
        String query="Select nombre from RMedicinesTable order by nombre";
        GenericRawResults<IRegisteredMedicines> rawResults = IMedRegistradosDao.queryRaw(query,IMedRegistradosDao.getRawRowMapper());
        return rawResults.getResults();
    }
    /*OBTEBER REGISTROS DE LA TABLA GRÁFICOS */
    public static List<IMedicines> GetMedicinesDateFromDatabase(Dao<IMedicines, Integer> IMedicinesDao, String fecha_desde, String fecha_hasta, String estado) throws SQLException, java.sql.SQLException {
        List<IMedicines> medicines = null;
        String query = "SELECT * FROM IMedicineTable where Fecha  >= '" + fecha_desde + "' and Fecha  <= '" + fecha_hasta + "' and " +
                "case when  '" + estado + "' = 'Dosis mas Bajas' then CAST((dosis) AS INTEGER)  <=60 " +
                "when  '" + estado + "' = 'Dosis mas Altas' then CAST((dosis) AS INTEGER) >60 " +
                "else  '" + estado + "' = 'Todos' end order by id DESC"; //LIMIT 5
        GenericRawResults<IMedicines> rawResults = IMedicinesDao.queryRaw(query, IMedicinesDao.getRawRowMapper());
        medicines = rawResults.getResults();
        return medicines;
    }
    /* OBTENER REGISTROS DE LA BASE POR MES - GRÁFICOS */
    public static List<IMedicines> GetMedicinesFromDatabase(Dao<IMedicines, Integer> MedicineDao, String mes) throws SQLException, java.sql.SQLException{
        List<IMedicines> medicines = null;
        String query = "SELECT * FROM IMedicineTable where substr(Fecha,4,2) = '"+mes+"' order by id DESC"; //LIMIT 5
        GenericRawResults<IMedicines> rawResults = MedicineDao.queryRaw(query, MedicineDao.getRawRowMapper());
        medicines = rawResults.getResults();
        return medicines;
    }
//////////////////////////
     /* Borrar REGISTRO DE LA BASE */
public static int DeleteByIdInsulin(Dao<EInsulin, Integer> InsulinDao, int id) throws SQLException, java.sql.SQLException {
    String query = "Delete FROM InsulinTable where Id = "+ id ;
    GenericRawResults rawResults = InsulinDao.queryRaw(query);
    return rawResults.getNumberColumns();
}

    public static boolean isNumeric(String cadena){
        try {
            Float.parseFloat(cadena);
            return true;
        } catch (NumberFormatException nfe){
            return false;
        }
    }
	
	    //Verifica Conexión a internet
    public static Boolean isOnlineNet() {
        try {
            Process p = Runtime.getRuntime().exec("ping -c 1 www.google.es");
            int val           = p.waitFor();
            boolean reachable = (val == 0);
            return reachable;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }
//*********************************************************************************************************************
// Actualizar registros de Colesterol en BD

    public static void DbsaveColesterolFromDatabase( int IdBdServer,
                                                    int col,
													int triglycerides,
                                                    int colhdl,
                                                    int colldl,
                                                    String fecha,
                                                    String hora,
                                                    String observacion,
                                                    String enviadoServer,
                                                    String operacion,
                                                    Dao<IColesterol, Integer> ColesterolDao) throws java.sql.SQLException {
        try {
            IColesterol colesterol = new IColesterol();
            colesterol.setIdBdServer(IdBdServer);
            colesterol.setColesterol(col);
            colesterol.setTriglycerides(triglycerides);
            colesterol.setHdl(colhdl);
            colesterol.setLdl(colldl);
            colesterol.setFecha( getDateYYYYMMDD( fecha ) ) ;
            colesterol.setHora(hora);
            colesterol.setObservacion(observacion);
            colesterol.setEnviadoServer(enviadoServer);
            colesterol.setOperacion(operacion);
            ColesterolDao.create(colesterol);

        } catch (SQLException e) {


            e.printStackTrace();
        }
    }


    public static void UpdateColesterolFromDatabase(int idCol,
                                                    int col,
                                                    int colhdl,
                                                    int colldl,
                                                    int triglycerides,
                                                    String fecha,
                                                    String hora,
                                                    String observacion,
                                                    String enviadoServer,
                                                    String operacion,
                                                    Dao<IColesterol, Integer> ColesterolDao) throws SQLException, java.sql.SQLException {
        try {
            UpdateBuilder<IColesterol, Integer> updateBuilder = ColesterolDao.updateBuilder();
            // set the criteria like you would a QueryBuilder
            updateBuilder.where().eq("id", idCol);
            // update the value of your field(s)
            updateBuilder.updateColumnValue("colesterol"  /* column */, col   /* value */);
            updateBuilder.updateColumnValue("triglycerides"  /* column */, triglycerides   /* value */);
            updateBuilder.updateColumnValue("hdl"  /* column */, colhdl   /* value */);
            updateBuilder.updateColumnValue("ldl"  /* column */, colldl   /* value */);
            updateBuilder.updateColumnValue("fecha"        /* column */, getDateYYYYMMDD( fecha )        /* value */);
            updateBuilder.updateColumnValue("hora"         /* column */, hora          /* value */);
            updateBuilder.updateColumnValue("observacion"  /* column */, observacion   /* value */);
            updateBuilder.updateColumnValue("enviadoServer"/* column */, enviadoServer /* value */);
            updateBuilder.updateColumnValue("operacion"/* column */, operacion /* value */);
            updateBuilder.update();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*ELIMINAR*/
    public static void DeleterowsIColesterol(Dao<IColesterol, Integer> rowDao) throws java.sql.SQLException {
        DeleteBuilder<IColesterol, Integer> deleteBuilder = rowDao.deleteBuilder();
        deleteBuilder.delete();
    }

    /*ACTUALIZAR REGISTRO DE LA TABLA BD ENVIADO AL SERVIDOR OK*/
    public static void sendOkUpdateColesterolFromDatabase(int idColest,
                                                          String enviadoServer,
                                                          Dao<IColesterol, Integer> ColesterolDao) throws SQLException, java.sql.SQLException {
        try {
            UpdateBuilder<IColesterol, Integer> updateBuilder = ColesterolDao.updateBuilder();
            // set the criteria like you would a QueryBuilder
            updateBuilder.where().eq("id", idColest);
            // update the value of your field(s)
            updateBuilder.updateColumnValue("enviadoServer"/* column */, enviadoServer /* value */);
            updateBuilder.update();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*OBTEBER REGISTROS DE LA TABLA BD QUE NO SE AN ENVIADO AL SERVIDOR*/
    public static List<IColesterol> GetColesterolNotSendFromDatabase(Dao<IColesterol, Integer> ColesterolDao, String sendServer) throws SQLException, java.sql.SQLException {
        List<IColesterol> colesterol = null;
        String query = "SELECT id , idBdServer , colesterol , hdl , ldl , triglycerides " +
                //", " + getDateStr("fecha","") +
                ", fecha " +
                ", hora , observacion , enviadoServer , operacion " +
                " FROM ColesterolTable WHERE  enviadoServer = '"+ sendServer +"' ";
        GenericRawResults<IColesterol> rawResults = ColesterolDao.queryRaw(query, ColesterolDao.getRawRowMapper());
        colesterol = rawResults.getResults();
        return colesterol;
    }

    /*OBTEBER REGISTROS DE LA TABLA BD*/
    public static List<IColesterol> GetColesterolFromDatabase(Dao<IColesterol, Integer> ColesterolDao) throws SQLException, java.sql.SQLException {
        List<IColesterol> colesterol = null;
        String query = "SELECT id , idBdServer , colesterol , hdl , ldl , triglycerides " +
                ", " + getDateStr("fecha","") +
                ", hora , observacion , enviadoServer , operacion " +
                " FROM ColesterolTable order by fecha DESC , hora DESC";
        GenericRawResults<IColesterol> rawResults = ColesterolDao.queryRaw(query, ColesterolDao.getRawRowMapper());
        colesterol = rawResults.getResults();
        return colesterol;
    }
    public static List<IColesterol> GetColesterolFromDatabaseMinimumDate(Dao<IColesterol, Integer> ColesterolDao, int NumNotfi) throws SQLException, java.sql.SQLException {
        List<IColesterol> colesterol = null;
        //String query = "SELECT  MIN(id) id FROM NotificationsTable" ;
        String query = "SELECT id , idBdServer , colesterol , hdl , ldl , triglycerides " +
                ", " + getDateStr("fecha","") +
                ", hora , observacion , enviadoServer , operacion " +
                " from   ColesterolTable  where  id   not in  ( SELECT    id  FROM ColesterolTable order by fecha DESC , hora DESC LIMIT " + NumNotfi + ")";
        GenericRawResults<IColesterol> rawResults = ColesterolDao.queryRaw(query, ColesterolDao.getRawRowMapper());
        colesterol = rawResults.getResults();
        return colesterol;
    }
    //OBTENER DATOS POR RANGO DE FECHAS
    public static List<IColesterol> GetColesterolRangoFechas(Dao<IColesterol, Integer> ColesterolDao, String fini, String ffin) throws SQLException , java.sql.SQLException
    {
        List<IColesterol> colesterol = null;
        String query = "SELECT id , idBdServer , colesterol , hdl , ldl , triglycerides " +
                ", " + getDateStr("fecha","") +
                ", hora , observacion , enviadoServer , operacion " +
                " from ColesterolTable where fecha>= "+ fini+ " && fecha<= "+ffin + " order by fecha DESC , hora DESC";
        GenericRawResults<IColesterol> rawResults = ColesterolDao.queryRaw(query, ColesterolDao.getRawRowMapper());
        colesterol = rawResults.getResults();
        return colesterol ;
    }
    public static List<IColesterol> GetIColesterolFromDatabase(Dao<IColesterol, Integer> ColesterolDao, String mes) throws SQLException, java.sql.SQLException {
        List<IColesterol> Colesterol = null;
        String query = "SELECT id , idBdServer , colesterol , hdl , ldl , triglycerides " +
                ", " + getDateStr("fecha","") +
                ", hora , observacion , enviadoServer , operacion " +
                " FROM ColesterolTable where substr(Fecha,4,2) = '"+mes+"' order by fecha DESC , hora DESC"; //LIMIT 5
        GenericRawResults<IColesterol> rawResults = ColesterolDao.queryRaw(query, ColesterolDao.getRawRowMapper());
        Colesterol = rawResults.getResults();
        return Colesterol;
    }
    /*OBTEBER REGISTROS DE LA TABLA BD*/
    public static List<IColesterol> GetIColesterolDateFromDatabase(Dao<IColesterol, Integer> ColesterolDao, String fecha_desde, String fecha_hasta,String estado ) throws SQLException, java.sql.SQLException {
        List<IColesterol> Colesterol = null;
        String query = "SELECT id , idBdServer , colesterol , hdl , ldl , triglycerides " +
                ", " + getDateStr("fecha","") +
                ", hora , observacion , enviadoServer , operacion " +
                " FROM ColesterolTable where Fecha  >= '"+fecha_desde+"' and Fecha  <= '"+fecha_hasta+"'  order by fecha DESC , hora DESC" ;
               /* "and case when  '"+estado+"' = 'Presion mas Baja' then CAST((maxpressure) AS INTEGER)  <=60 " +
                "when  '"+estado+"' = 'Presion mas Alta' then CAST((maxpressure) AS INTEGER) >60 " +
                "else  '"+estado+"' = 'Todos' end order by id DESC"; */
        GenericRawResults<IColesterol> rawResults = ColesterolDao.queryRaw(query, ColesterolDao.getRawRowMapper());
        Colesterol = rawResults.getResults();
        return Colesterol;
    }

    /*ACTUALIZAR REGISTRO DE LA TABLA BD ENVIADO AL SERVIDOR OK*/
    public static void sendOkUpdateCholesterolFromDatabase(int idCholesterol,
                                                           int IdRegisterDB,
                                                           String enviadoServer,
                                                           Dao<IColesterol, Integer>ColesterolDao ) throws SQLException, java.sql.SQLException {
        try {
            UpdateBuilder<IColesterol, Integer> updateBuilder = ColesterolDao.updateBuilder();
            // set the criteria like you would a QueryBuilder
            updateBuilder.where().eq("id", idCholesterol);
            // update the value of your field(s)
            updateBuilder.updateColumnValue("idBdServer"/* column */, IdRegisterDB /* value */);
            updateBuilder.updateColumnValue("enviadoServer"/* column */, enviadoServer /* value */);
            updateBuilder.update();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*ACTUALIZAR REGISTRO DE LA TABLA BD ENVIADO AL SERVIDOR OK*/
    public static void sendOkUpdateCholesterolFromDatabase2(int idCholesterol,
                                                           String enviadoServer,
                                                           Dao<IColesterol, Integer>ColesterolDao ) throws SQLException, java.sql.SQLException {
        try {
            UpdateBuilder<IColesterol, Integer> updateBuilder = ColesterolDao.updateBuilder();
            // set the criteria like you would a QueryBuilder
            updateBuilder.where().eq("id", idCholesterol);
            // update the value of your field(s)
            updateBuilder.updateColumnValue("enviadoServer"/* column */, enviadoServer /* value */);
            updateBuilder.update();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

//**********************************************************************************************************************
//Actuliza Registros de Trigliceros

    public static void DbsaveTriglicerosFromDatabase(int trig,
                                                     String fecha,
                                                     String hora,
                                                     String observacion,
                                                     String enviadoServer,
                                                     String operacion,
                                                     Dao<ITrigliceros, Integer> TriglicerosDao) throws java.sql.SQLException {
        try {
            ITrigliceros trigliceros = new ITrigliceros();
            trigliceros.setConcentration(trig);
            trigliceros.setFecha( getDateYYYYMMDD(fecha) );
            trigliceros.setHora(hora);
            trigliceros.setObservacion(observacion);
            trigliceros.setEnviadoServer(enviadoServer);
            trigliceros.setObservacion(operacion);
            TriglicerosDao.create(trigliceros);

        } catch (SQLException e) {


            e.printStackTrace();
        }
    }


    public static void UpdateTriglicerosFromDatabase(int idTri,
                                                     int trig,
                                                     String fecha,
                                                     String hora,
                                                     String observacion,
                                                     String enviadoServer,
                                                     String operacion,
                                                     Dao<ITrigliceros, Integer> TriglicerosDao) throws SQLException, java.sql.SQLException {
        try {
            UpdateBuilder<ITrigliceros, Integer> updateBuilder = TriglicerosDao.updateBuilder();
            // set the criteria like you would a QueryBuilder
            updateBuilder.where().eq("id", idTri);
            // update the value of your field(s)
            updateBuilder.updateColumnValue("concentration"  /* column */, trig   /* value */);
            updateBuilder.updateColumnValue("fecha"        /* column */, getDateYYYYMMDD( fecha )       /* value */);
            updateBuilder.updateColumnValue("hora"         /* column */, hora          /* value */);
            updateBuilder.updateColumnValue("observacion"  /* column */, observacion   /* value */);
            updateBuilder.updateColumnValue("enviadoServer"/* column */, enviadoServer /* value */);
            updateBuilder.updateColumnValue("operacion"/* column */, operacion /* value */);
            updateBuilder.update();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*ACTUALIZAR REGISTRO DE LA TABLA BD ENVIADO AL SERVIDOR OK*/
    public static void sendOkUpdateTriglicerosFromDatabase(int idTrig,
                                                           String enviadoServer,
                                                           Dao<ITrigliceros, Integer> TriglicerosDao) throws SQLException, java.sql.SQLException {
        try {
            UpdateBuilder<ITrigliceros, Integer> updateBuilder = TriglicerosDao.updateBuilder();
            // set the criteria like you would a QueryBuilder
            updateBuilder.where().eq("id", idTrig);
            // update the value of your field(s)
            updateBuilder.updateColumnValue("enviadoServer"/* column */, enviadoServer /* value */);
            updateBuilder.update();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*OBTEBER REGISTROS DE LA TABLA BD QUE NO SE AN ENVIADO AL SERVIDOR*/
    public static List<ITrigliceros> GetTriglicerosNotSendFromDatabase(Dao<ITrigliceros, Integer> TriglicerosDao, String sendServer) throws SQLException, java.sql.SQLException {
        List<ITrigliceros> trigliceros = null;
        String query = "SELECT  id , concentration " +
                ", " + getDateStr("fecha","") +
                ", hora , observacion , enviadoServer , idBdServer , operacion " +
                " FROM TriglicerosTable WHERE  enviadoServer = '"+ sendServer +"' ";
        GenericRawResults<ITrigliceros> rawResults = TriglicerosDao.queryRaw(query, TriglicerosDao.getRawRowMapper());
        trigliceros = rawResults.getResults();
        return trigliceros;
    }

    /*OBTEBER REGISTROS DE LA TABLA BD*/
    public static List<ITrigliceros> GetTriglicerosFromDatabase(Dao<ITrigliceros, Integer> TriglicerosDao) throws SQLException, java.sql.SQLException {
        List<ITrigliceros> trigliceros = null;
        String query = "SELECT  id , concentration " +
                ", " + getDateStr("fecha","") +
                ", hora , observacion , enviadoServer , idBdServer , operacion " +
                " FROM TriglicerosTable order by fecha DESC , hora DESC";
        GenericRawResults<ITrigliceros> rawResults = TriglicerosDao.queryRaw(query, TriglicerosDao.getRawRowMapper());
        trigliceros = rawResults.getResults();
        return trigliceros;
    }
    public static List<ITrigliceros> GetTriglicerosFromDatabaseMinimumDate(Dao<ITrigliceros, Integer> TriglicerosDao, int NumNotfi) throws SQLException, java.sql.SQLException {
        List<ITrigliceros> trigliceros = null;
        //String query = "SELECT  MIN(id) id FROM NotificationsTable" ;
        String query = "SELECT  id , concentration " +
                ", " + getDateStr("fecha","") +
                ", hora , observacion , enviadoServer , idBdServer , operacion " +
                " from   TriglicerosTable  where  id   not in  ( SELECT    id  FROM TriglicerosTable order by fecha DESC , hora DESC LIMIT " + NumNotfi + ")";
        GenericRawResults<ITrigliceros> rawResults = TriglicerosDao.queryRaw(query, TriglicerosDao.getRawRowMapper());
        trigliceros = rawResults.getResults();
        return trigliceros;
    }
    //OBTENER DATOS POR RANGO DE FECHAS
    public static List<ITrigliceros> GetTriglicerosRangoFechas(Dao<ITrigliceros, Integer> TriglicerosDao, String fini, String ffin) throws SQLException , java.sql.SQLException
    {
        List<ITrigliceros> trigliceros = null;
        String query = "SELECT  id , concentration " +
                ", " + getDateStr("fecha","") +
                ", hora , observacion , enviadoServer , idBdServer , operacion " +
                " from TriglicerosTable where fecha>= "+ fini+ " && fecha<= "+ffin + " order by fecha DESC , hora DESC";
        GenericRawResults<ITrigliceros> rawResults = TriglicerosDao.queryRaw(query, TriglicerosDao.getRawRowMapper());
        trigliceros = rawResults.getResults();
        return trigliceros ;
    }
    public static List<ITrigliceros> GetTriglicerosFromDatabase(Dao<ITrigliceros, Integer> TriglicerosDao, String mes) throws SQLException, java.sql.SQLException {
        List<ITrigliceros> trigliceros = null;
        String query = "SELECT  id , concentration " +
                ", " + getDateStr("fecha","") +
                ", hora , observacion , enviadoServer , idBdServer , operacion " +
                " FROM TriglicerosTable where substr(Fecha,4,2) = '"+mes+"' order by fecha DESC , hora DESC"; //LIMIT 5
        GenericRawResults<ITrigliceros> rawResults = TriglicerosDao.queryRaw(query, TriglicerosDao.getRawRowMapper());
        trigliceros = rawResults.getResults();
        return trigliceros;
    }
    /*OBTEBER REGISTROS DE LA TABLA BD*/
    public static List<ITrigliceros> GetTriglicerosDateFromDatabase(Dao<ITrigliceros, Integer> TriglicerosDao, String fecha_desde, String fecha_hasta,String estado ) throws SQLException, java.sql.SQLException {
        List<ITrigliceros> trigliceros = null;
        String query = "SELECT  id , concentration " +
                ", " + getDateStr("fecha","") +
                ", hora , observacion , enviadoServer , idBdServer , operacion " +
                " FROM TriglicerosTable where Fecha  >= '"+ getDateYYYYMMDD( fecha_desde ) +"' and Fecha  <= '"+ getDateYYYYMMDD( fecha_hasta ) +"'  order by fecha DESC , hora DESC" ;
               /* "and case when  '"+estado+"' = 'Presion mas Baja' then CAST((maxpressure) AS INTEGER)  <=60 " +
                "when  '"+estado+"' = 'Presion mas Alta' then CAST((maxpressure) AS INTEGER) >60 " +
                "else  '"+estado+"' = 'Todos' end order by id DESC"; */
        GenericRawResults<ITrigliceros> rawResults = TriglicerosDao.queryRaw(query, TriglicerosDao.getRawRowMapper());
        trigliceros = rawResults.getResults();
        return trigliceros;
    }
//*********************************************************************************************************************
// Actualizar registros de Hba1c en BD

    public static void DbsaveHba1cFromDatabase( int IdBdServer,
                                                float con,
												float cetonas,
                                                String fecha,
                                                String hora,
                                                String observacion,
                                                String enviadoServer,
                                                String operacion,
                                                Dao<IHba1c, Integer> Hba1cDao) throws java.sql.SQLException {
        try {
            IHba1c hba1c = new IHba1c();
            hba1c.setIdBdServer(IdBdServer);
            hba1c.setConcentracion(con);
            hba1c.setCetonas(cetonas);
            hba1c.setFecha( getDateYYYYMMDD(fecha) );
            hba1c.setHora(hora);
            hba1c.setObservacion(observacion);
            hba1c.setEnviadoServer(enviadoServer);
            hba1c.setOperacion(operacion);
            Hba1cDao.create(hba1c);

        } catch (SQLException e) {


            e.printStackTrace();
        }
    }


    public static void UpdateHba1cFromDatabase(     int idHba1c,
                                                    float con,
                                                    float cetonas,
                                                    String fecha,
                                                    String hora,
                                                    String observacion,
                                                    String enviadoServer,
                                                    String operacion,
                                                    Dao<IHba1c, Integer> Hba1cDao) throws SQLException, java.sql.SQLException {
       //Log.e(TAG, con + " " + fecha + " " + hora + " " + observacion + " " + enviadoServer );
        try {
            UpdateBuilder<IHba1c, Integer> updateBuilder = Hba1cDao.updateBuilder();
            // set the criteria like you would a QueryBuilder
            updateBuilder.where().eq("id", idHba1c);
            // update the value of your field(s)
            updateBuilder.updateColumnValue("concentracion"  /* column */, con   /* value */);
            updateBuilder.updateColumnValue("cetonas"  /* column */, cetonas   /* value */);
            updateBuilder.updateColumnValue("fecha"        /* column */, getDateYYYYMMDD(fecha )        /* value */);
            updateBuilder.updateColumnValue("hora"         /* column */, hora          /* value */);
            updateBuilder.updateColumnValue("observacion"  /* column */, observacion   /* value */);
            updateBuilder.updateColumnValue("enviadoServer"/* column */, enviadoServer /* value */);
            updateBuilder.updateColumnValue("Operacion"/* column */,       operacion /* value */);
            updateBuilder.update();
            Log.e(TAG, con + " " + fecha + " " + hora + " " + observacion + " " + enviadoServer );
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void DeleterowsIHba1c(Dao<IHba1c, Integer> rowDao) throws java.sql.SQLException {
        DeleteBuilder<IHba1c, Integer> deleteBuilder = rowDao.deleteBuilder();
        deleteBuilder.delete();
    }

    /*ACTUALIZAR REGISTRO DE LA TABLA BD ENVIADO AL SERVIDOR OK*/
    public static void sendOkUpdateHba1cFromDatabase(int idHba1c,
                                                     int IdRegisterDB,
                                                      String enviadoServer,
                                                      Dao<IHba1c, Integer> Hba1cDao) throws SQLException, java.sql.SQLException {
        try {
            UpdateBuilder<IHba1c, Integer> updateBuilder = Hba1cDao.updateBuilder();
            // set the criteria like you would a QueryBuilder
            updateBuilder.where().eq("id", idHba1c);
            // update the value of your field(s)
            updateBuilder.updateColumnValue("idBdServer"/* column */, IdRegisterDB /* value */);
            updateBuilder.updateColumnValue("enviadoServer"/* column */, enviadoServer /* value */);
            updateBuilder.update();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*ACTUALIZAR REGISTRO DE LA TABLA BD ENVIADO AL SERVIDOR OK*/
    public static void sendOkUpdateHba1cFromDatabase2(int idHba1c,
                                                     String enviadoServer,
                                                     Dao<IHba1c, Integer> Hba1cDao) throws SQLException, java.sql.SQLException {
        try {
            UpdateBuilder<IHba1c, Integer> updateBuilder = Hba1cDao.updateBuilder();
            // set the criteria like you would a QueryBuilder
            updateBuilder.where().eq("id", idHba1c);
            // update the value of your field(s)
            updateBuilder.updateColumnValue("enviadoServer"/* column */, enviadoServer /* value */);
            updateBuilder.update();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*OBTEBER REGISTROS DE LA TABLA BD QUE NO SE AN ENVIADO AL SERVIDOR*/
    public static List<IHba1c> GetHba1cNotSendFromDatabase(Dao<IHba1c, Integer> Hba1cDao, String sendServer) throws SQLException, java.sql.SQLException {
        List<IHba1c> hba1c = null;
        String query = "SELECT id , idBdServer , concentracion , Cetonas " +
                //", " + getDateStr("fecha","") +
                ", fecha " +
                ", hora , observacion , enviadoServer , operacion  " +
                " FROM Hba1cTable WHERE  enviadoServer = '"+ sendServer +"' ";
        GenericRawResults<IHba1c> rawResults = Hba1cDao.queryRaw(query, Hba1cDao.getRawRowMapper());
        hba1c = rawResults.getResults();
        return hba1c;
    }

    /*OBTEBER REGISTROS DE LA TABLA BD*/
    public static List<IHba1c> GetHba1cFromDatabase(Dao<IHba1c, Integer> Hba1cDao) throws SQLException, java.sql.SQLException {
        List<IHba1c> hba1c = null;
        String query = "SELECT id , idBdServer , concentracion , Cetonas " +
                ", " + getDateStr("fecha","") +
                ", hora , observacion , enviadoServer , operacion  " +
                " FROM Hba1cTable order by fecha DESC , hora DESC";
        GenericRawResults<IHba1c> rawResults = Hba1cDao.queryRaw(query, Hba1cDao.getRawRowMapper());
        hba1c = rawResults.getResults();
        return hba1c;
    }
    public static List<IHba1c> GetHba1cFromDatabaseMinimumDate(Dao<IHba1c, Integer> Hba1cDao, int NumNotfi) throws SQLException, java.sql.SQLException {
        List<IHba1c> hba1c = null;
        //String query = "SELECT  MIN(id) id FROM NotificationsTable" ;
        String query = "SELECT id , idBdServer , concentracion , Cetonas " +
                ", " + getDateStr("fecha","") +
                ", hora , observacion , enviadoServer , operacion  " +
                " Hba1cTable  where  id   not in  ( SELECT    id  FROM Hba1cTable order by fecha DESC , hora DESC LIMIT " + NumNotfi + ")";
        GenericRawResults<IHba1c> rawResults = Hba1cDao.queryRaw(query, Hba1cDao.getRawRowMapper());
        hba1c = rawResults.getResults();
        return hba1c;
    }
    //OBTENER DATOS POR RANGO DE FECHAS
    public static List<IHba1c> GetHba1cRangoFechas(Dao<IHba1c, Integer> Hba1cDao, String fini, String ffin) throws SQLException , java.sql.SQLException
    {
        List<IHba1c> hba1c = null;
        String query = "SELECT id , idBdServer , concentracion , Cetonas " +
                ", " + getDateStr("fecha","") +
                ", hora , observacion , enviadoServer , operacion  " +
                " from Hba1cTable where fecha>= "+ getDateYYYYMMDD( fini ) + " && fecha<= "+ getDateYYYYMMDD( ffin ) + " order by fecha DESC , hora DESC";
        GenericRawResults<IHba1c> rawResults = Hba1cDao.queryRaw(query, Hba1cDao.getRawRowMapper());
        hba1c = rawResults.getResults();
        return hba1c ;
    }
    public static List<IHba1c> GetIHba1cFromDatabase(Dao<IHba1c, Integer> Hba1cDao, String mes) throws SQLException, java.sql.SQLException {
        List<IHba1c> Hba1c = null;
        String query = "SELECT id , idBdServer , concentracion , Cetonas " +
                ", " + getDateStr("fecha","") +
                ", hora , observacion , enviadoServer , operacion  " +
                " FROM Hba1cTable where substr(Fecha,4,2) = '"+mes+"' order by fecha DESC , hora DESC"; //LIMIT 5
        GenericRawResults<IHba1c> rawResults = Hba1cDao.queryRaw(query, Hba1cDao.getRawRowMapper());
        Hba1c = rawResults.getResults();
        return Hba1c;
    }
    /*OBTEBER REGISTROS DE LA TABLA BD Hba1c*/
    public static List<IHba1c> GetIHba1cDateFromDatabase(Dao<IHba1c, Integer> IHba1cDao, String fecha_desde, String fecha_hasta,String estado ) throws SQLException, java.sql.SQLException {
        List<IHba1c> Hba1c = null;
        String query = "SELECT id , idBdServer , concentracion , Cetonas " +
                ", " + getDateStr("fecha","") +
                ", hora , observacion , enviadoServer , operacion  " +
                " FROM Hba1cTable where Fecha  >= '"+ getDateYYYYMMDD( fecha_desde ) +"' and Fecha  <= '"+ getDateYYYYMMDD( fecha_hasta ) +"'  order by fecha DESC , hora DESC" ;
               /* "and case when  '"+estado+"' = 'Presion mas Baja' then CAST((maxpressure) AS INTEGER)  <=60 " +
                "when  '"+estado+"' = 'Presion mas Alta' then CAST((maxpressure) AS INTEGER) >60 " +
                "else  '"+estado+"' = 'Todos' end order by id DESC"; */
        GenericRawResults<IHba1c> rawResults = IHba1cDao.queryRaw(query, IHba1cDao.getRawRowMapper());
        Hba1c = rawResults.getResults();
        return Hba1c;
    }

    //*********************************************************************************************************************
// Actualizar registros de Ketone en BD

    public static void DbsaveKetoneFromDatabase(int con,
                                                float cetonas,
                                               String fecha,
                                               String hora,
                                               String observacion,
                                               String enviadoServer,
                                               String operacion,
                                               Dao<IKetone, Integer> KetoneDao) throws java.sql.SQLException {
        try {
            IKetone ketone = new IKetone();
            ketone.setConcentracion(con);
            ketone.setFecha( getDateYYYYMMDD( fecha) );
            ketone.setHora(hora);
            ketone.setObservacion(observacion);
            ketone.setEnviadoServer(enviadoServer);
            ketone.setOperacion(operacion);
            KetoneDao.create(ketone);


        } catch (SQLException e) {


            e.printStackTrace();
        }
    }


    public static void UpdateKetoneFromDatabase(int idKetone,
                                               int con,
                                               String fecha,
                                               String hora,
                                               String observacion,
                                               String enviadoServer,
                                               String operacion,
                                               Dao<IKetone, Integer> KetoneDao) throws SQLException, java.sql.SQLException {
        try {
            UpdateBuilder<IKetone, Integer> updateBuilder = KetoneDao.updateBuilder();
            // set the criteria like you would a QueryBuilder
            updateBuilder.where().eq("id", idKetone);
            // update the value of your field(s)
            updateBuilder.updateColumnValue("concentracion"  /* column */, con   /* value */);
            updateBuilder.updateColumnValue("fecha"        /* column */, getDateYYYYMMDD( fecha )        /* value */);
            updateBuilder.updateColumnValue("hora"         /* column */, hora          /* value */);
            updateBuilder.updateColumnValue("observacion"  /* column */, observacion   /* value */);
            updateBuilder.updateColumnValue("enviadoServer"/* column */, enviadoServer /* value */);
            updateBuilder.updateColumnValue("operacion"/* column */, operacion /* value */);
            updateBuilder.update();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*ACTUALIZAR REGISTRO DE LA TABLA BD ENVIADO AL SERVIDOR OK*/
    public static void sendOkUpdateKetoneFromDatabase(int idKetone,
                                                     String enviadoServer,
                                                     Dao<IKetone, Integer> KetoneDao) throws SQLException, java.sql.SQLException {
        try {
            UpdateBuilder<IKetone, Integer> updateBuilder = KetoneDao.updateBuilder();
            // set the criteria like you would a QueryBuilder
            updateBuilder.where().eq("id", idKetone);
            // update the value of your field(s)
            updateBuilder.updateColumnValue("enviadoServer"/* column */, enviadoServer /* value */);
            updateBuilder.update();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*OBTEBER REGISTROS DE LA TABLA BD QUE NO SE AN ENVIADO AL SERVIDOR*/
    public static List<IKetone> GetKetoneNotSendFromDatabase(Dao<IKetone, Integer> KetoneDao, String sendServer) throws SQLException, java.sql.SQLException {
        List<IKetone> ketone = null;
        String query = "SELECT id , concentracion " +
                // ", " + getDateStr("fecha","") +
                ", fecha " + 
                ", hora , observacion , enviadoServer , idBdServer , operacion  " +
                " FROM KetonesTable WHERE  enviadoServer = '"+ sendServer +"' ";
        GenericRawResults<IKetone> rawResults = KetoneDao.queryRaw(query, KetoneDao.getRawRowMapper());
        ketone = rawResults.getResults();
        return ketone;
    }

    /*OBTEBER REGISTROS DE LA TABLA BD*/
    public static List<IKetone> GetKetoneFromDatabase(Dao<IKetone, Integer> KetoneDao) throws SQLException, java.sql.SQLException {
        List<IKetone> ketone = null;
        String query = "SELECT id , concentracion " +
                ", " + getDateStr("fecha","") +
                ", hora , observacion , enviadoServer , idBdServer , operacion  " +
                " FROM KetonesTable order by fecha DESC , hora DESC";
        GenericRawResults<IKetone> rawResults = KetoneDao.queryRaw(query, KetoneDao.getRawRowMapper());
        ketone = rawResults.getResults();
        return ketone;
    }
    public static List<IKetone> GetKetoneFromDatabaseMinimumDate(Dao<IKetone, Integer> KetoneDao, int NumNotfi) throws SQLException, java.sql.SQLException {
        List<IKetone> ketone = null;
        //String query = "SELECT  MIN(id) id FROM NotificationsTable" ;
        String query = "SELECT id , concentracion " +
                ", " + getDateStr("fecha","") +
                ", hora , observacion , enviadoServer , idBdServer , operacion  " +
                " from   KetonesTable  where  id   not in  ( SELECT    id  FROM KetonesTable order by fecha DESC , hora DESC LIMIT " + NumNotfi + ")";
        GenericRawResults<IKetone> rawResults = KetoneDao.queryRaw(query, KetoneDao.getRawRowMapper());
        ketone = rawResults.getResults();
        return ketone;
    }
    //OBTENER DATOS POR RANGO DE FECHAS

    public static List<IKetone> GetKetoneRangoFechas(Dao<IKetone, Integer> KetoneDao, String fini, String ffin) throws SQLException , java.sql.SQLException
    {
        List<IKetone> ketone = null;
        String query = "SELECT id , concentracion " +
                ", " + getDateStr("fecha","") +
                ", hora , observacion , enviadoServer , idBdServer , operacion  " +
                " from KetonesTable where fecha>= "+ getDateYYYYMMDD( fini ) + " && fecha<= "+ getDateYYYYMMDD( ffin ) + " order by fecha DESC , hora DESC";
        GenericRawResults<IKetone> rawResults = KetoneDao.queryRaw(query, KetoneDao.getRawRowMapper());
        ketone= rawResults.getResults();
        return ketone ;
    }
    public static List<IKetone> GetIKetoneFromDatabase(Dao<IKetone, Integer> KetoneDao, String mes) throws SQLException, java.sql.SQLException {
        List<IKetone> ketone = null;
        String query = "SELECT * FROM KetonesTable where substr(Fecha,4,2) = '"+mes+"' order by fecha DESC , hora DESC"; //LIMIT 5
        GenericRawResults<IKetone> rawResults = KetoneDao.queryRaw(query, KetoneDao.getRawRowMapper());
        ketone = rawResults.getResults();
        return ketone;
    }
    /*OBTEBER REGISTROS DE LA TABLA BD Ketone*/
    public static List<IKetone> GetIKetoneDateFromDatabase(Dao<IKetone, Integer> IKetoneDao, String fecha_desde, String fecha_hasta,String estado ) throws SQLException, java.sql.SQLException {
        List<IKetone> Ketone = null;
        String query = "SELECT id , concentracion " +
                ", " + getDateStr("fecha","") +
                ", hora , observacion , enviadoServer , idBdServer , operacion  " +
                " FROM KetonesTable where Fecha  >= '"+ getDateYYYYMMDD( fecha_desde ) +"' and Fecha  <= '"+ getDateYYYYMMDD( fecha_hasta ) +"'  order by fecha DESC , hora DESC" ;
               /* "and case when  '"+estado+"' = 'Presion mas Baja' then CAST((maxpressure) AS INTEGER)  <=60 " +
                "when  '"+estado+"' = 'Presion mas Alta' then CAST((maxpressure) AS INTEGER) >60 " +
                "else  '"+estado+"' = 'Todos' end order by id DESC"; */
        GenericRawResults<IKetone> rawResults = IKetoneDao.queryRaw(query, IKetoneDao.getRawRowMapper());
        Ketone = rawResults.getResults();
        return Ketone;
    }

        /*INICIO DATA BASE GLUCOSE*/

    public static void DbsaveGlucoseFromDatabase(int IdBdServer,
                                                 int concentracion,
                                                 String fecha,
                                                 String hora,
                                                 String observacion,
                                                 String enviadoServer,
                                                 String operacion,
                                                 Dao<IGlucose, Integer> GlucoseDao) throws java.sql.SQLException {
        try {
            IGlucose glucose = new IGlucose();
            glucose.setIdBdServer(IdBdServer);
            glucose.setConcentracion(concentracion);
            glucose.setFecha( getDateYYYYMMDD( fecha) ) ;
            glucose.setHora(hora);
            glucose.setObservacion(observacion);
            glucose.setEnviadoServer(enviadoServer);
            glucose.setOperacion(operacion);
            GlucoseDao.create(glucose);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void UpdateGlucoseFromDatabase(int idGlu,
                                                    int con,
                                                    String fecha,
                                                    String hora,
                                                    String observacion,
                                                    String enviadoServer,
                                                    String operacion,
                                                    Dao<IGlucose, Integer> GlucoseDao) throws SQLException, java.sql.SQLException {
        try {
            UpdateBuilder<IGlucose, Integer> updateBuilder = GlucoseDao.updateBuilder();
            // set the criteria like you would a QueryBuilder
            updateBuilder.where().eq("id", idGlu);
            // update the value of your field(s)
            updateBuilder.updateColumnValue("concentracion"  /* column */, con   /* value */);
            updateBuilder.updateColumnValue("fecha"        /* column */, getDateYYYYMMDD( fecha )        /* value */);
            updateBuilder.updateColumnValue("hora"         /* column */, hora          /* value */);
            updateBuilder.updateColumnValue("observacion"  /* column */, observacion   /* value */);
            updateBuilder.updateColumnValue("enviadoServer"/* column */, enviadoServer /* value */);
            updateBuilder.updateColumnValue("operacion"/* column */, operacion /* value */);
            updateBuilder.update();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*ELIMINAR REGISTROS*/
    public static void DeleterowsIGlucose(Dao<IGlucose, Integer> rowDao) throws  java.sql.SQLException {
        DeleteBuilder<IGlucose, Integer> deleteBuilder = rowDao.deleteBuilder();
        deleteBuilder.delete();
    }

    /*OBTEBER REGISTROS DE LA TABLA BD*/
    /*
    public static List<IGlucose> GetGlucoseFromDatabase(Dao<IGlucose, Integer> GlucoseDao) throws SQLException, java.sql.SQLException {
        List<IGlucose> glucose = null;
        String query = "SELECT * FROM GlucoseTable order by id DESC"; //LIMIT
        GenericRawResults<IGlucose> rawResults = GlucoseDao.queryRaw(query, GlucoseDao.getRawRowMapper());
        glucose = rawResults.getResults();
        return glucose;
    }
*/public static List<IGlucose> GetIGlucoseFromDatabase(Dao<IGlucose, Integer> IGlucoseDao, String mes) throws SQLException, java.sql.SQLException {
        List<IGlucose> Glucose = null;
        String query = "SELECT id , idBdServer , concentracion " +
                ", " + getDateStr("fecha","") +
                ", hora , observacion , enviadoServer , operacion " +
                " FROM GlucoseTable where substr(Fecha,6,2) = '"+mes+"' order by fecha DESC , hora DESC"; //LIMIT 5
        GenericRawResults<IGlucose> rawResults = IGlucoseDao.queryRaw(query, IGlucoseDao.getRawRowMapper());
        Glucose = rawResults.getResults();
        return Glucose;
    }
    public static List<IGlucose> GetGlucoseFromDatabase(Dao<IGlucose, Integer> IGlucoseDao) throws SQLException, java.sql.SQLException {
        List<IGlucose> Glucose = null;
        String query = "SELECT id , idBdServer , concentracion " +
                ", " + getDateStr("fecha","") +
                ", hora , observacion , enviadoServer , operacion " +
                " FROM GlucoseTable  order by fecha DESC , hora DESC"; //LIMIT 5
        GenericRawResults<IGlucose> rawResults = IGlucoseDao.queryRaw(query, IGlucoseDao.getRawRowMapper());
        Glucose = rawResults.getResults();
        return Glucose;
    }
    public static List<IGlucose> GetGlucoseNotFromDatabase(Dao<IGlucose, Integer> IGlucoseDao, String sendServer) throws SQLException, java.sql.SQLException {
        List<IGlucose> Glucose = null;
        String query = "SELECT id , idBdServer , concentracion " +
                //", " + getDateStr("fecha","") +
                ", fecha " +
                ", hora , observacion , enviadoServer , operacion " +
                " FROM GlucoseTable where enviadoServer = '"+ sendServer +"' "; //LIMIT 5
        GenericRawResults<IGlucose> rawResults = IGlucoseDao.queryRaw(query, IGlucoseDao.getRawRowMapper());
        Glucose = rawResults.getResults();
        return Glucose;
    }
    public static List<IGlucose> GetGlucoseFromDatabaseMes(Dao<IGlucose, Integer> IGlucoseDao, String mes) throws SQLException, java.sql.SQLException {
        List<IGlucose> Glucose = null;
        String query = "SELECT id , idBdServer , concentracion " +
                ", " + getDateStr("fecha","") +
                ", hora , observacion , enviadoServer , operacion " +
                " FROM GlucoseTable where substr(Fecha,6,2) = '"+mes+"' order by id DESC"; //LIMIT 5
        GenericRawResults<IGlucose> rawResults = IGlucoseDao.queryRaw(query, IGlucoseDao.getRawRowMapper());
        Glucose = rawResults.getResults();
        return Glucose;
    }
    /*OBTEBER REGISTROS DE LA TABLA BD*/

    public static List<IGlucose> GetGlucoseFromDatabaseMinimumDate(Dao<IGlucose, Integer> GlucoseDao, int NumNotfi) throws SQLException, java.sql.SQLException {
        List<IGlucose> glucose = null;
        //String query = "SELECT  MIN(id) id FROM NotificationsTable" ;
        String query = "SELECT id , idBdServer , concentracion " +
                ", " + getDateStr("fecha","") +
                ", hora , observacion , enviadoServer , operacion " +
                " from   GlucoseTable  where  id   not in  ( SELECT    id  FROM GlucoseTable order by id  DESC LIMIT " + NumNotfi + ")";
        GenericRawResults<IGlucose> rawResults = GlucoseDao.queryRaw(query, GlucoseDao.getRawRowMapper());
        glucose = rawResults.getResults();
        return glucose;
    }

    /*ELIMINAR LA FECHA MINIMA*/
    public static void DeleterowsfechaMinima(Dao<IGlucose, Integer> rowDao, int idMinimo) throws SQLException, Exception {
        DeleteBuilder<IGlucose, Integer> deleteBuilder = rowDao.deleteBuilder();
        deleteBuilder.where().eq("id", idMinimo);
        deleteBuilder.delete();
    }
    //OBTENER DATOS POR RANGO DE FECHAS
    public static List<IGlucose> GetGlucoseRangoFechas(Dao<IGlucose, Integer> GlucoseDao, String fini, String ffin) throws SQLException , java.sql.SQLException
    {
        List<IGlucose> glucose = null;
        String query = "SELECT id , idBdServer , concentracion " +
                ", " + getDateStr("fecha","") +
                ", hora , observacion , enviadoServer , operacion " +
                " from GlucoseTable where fecha >= "+ getDateYYYYMMDD( fini ) + " && fecha <= "+ getDateYYYYMMDD( ffin ) + " Order by fecha";
        GenericRawResults<IGlucose> rawResults = GlucoseDao.queryRaw(query, GlucoseDao.getRawRowMapper());
        glucose = rawResults.getResults();
        return glucose;
    }

    /*OBTEBER REGISTROS DE LA TABLA BD*/
    public static List<IGlucose> GetIGlucoseDaoFromDatabase(Dao<IGlucose, Integer> GlucoseDao, String mes) throws SQLException, java.sql.SQLException {
        List<IGlucose> Glucose = null;
        String query = "SELECT id , idBdServer , concentracion " +
                ", " + getDateStr("fecha","") +
                ", hora , observacion , enviadoServer , operacion " +
                " FROM GlucoseTable where substr(Fecha,4,2) = '"+mes+"' order by id DESC"; //LIMIT 5
        GenericRawResults<IGlucose> rawResults = GlucoseDao.queryRaw(query, GlucoseDao.getRawRowMapper());
        Glucose = rawResults.getResults();
        return Glucose;
    }
    /*OBTEBER REGISTROS DE LA TABLA BD*/
    public static List<IGlucose> GetIGlucose1DateFromDatabase(Dao<IGlucose, Integer> IGlucoseDao, String fecha_desde, String fecha_hasta,String estado ) throws SQLException, java.sql.SQLException {
        List<IGlucose> Glucose = null;
        String query = "SELECT id , idBdServer , concentracion " +
                ", " + getDateStr("fecha","") +
                ", hora , observacion , enviadoServer , operacion " +
                " FROM GlucoseTable where Fecha  >= '"+ getDateYYYYMMDD( fecha_desde ) +"' and Fecha  <= '"+ getDateYYYYMMDD( fecha_hasta ) +"'  order by fecha DESC , hora DESC" ;
               /* "and case when  '"+estado+"' = 'Presion mas Baja' then CAST((maxpressure) AS INTEGER)  <=60 " +
                "when  '"+estado+"' = 'Presion mas Alta' then CAST((maxpressure) AS INTEGER) >60 " +
                "else  '"+estado+"' = 'Todos' end order by id DESC"; */
        GenericRawResults<IGlucose> rawResults = IGlucoseDao.queryRaw(query, IGlucoseDao.getRawRowMapper());
        Glucose = rawResults.getResults();
        return Glucose;
    }

 /*FIN DATA BASE GLUCOSE*/


 /* INI ALARMAS*/

    public static int saveMedicineTypeToDataBaseLocal(
        Dao<EMedicineType, Integer> ObjDao,
        int medicineTypeCode ,
        String medicineTypeDescription
    )throws java.sql.SQLException{
        String Method ="[saveMedicineTypeToDataBaseLocal]";
        Log.i(TAG, Method + "Init..."  );
        int iRows=0;
        try {
            EMedicineType obj = new EMedicineType() ;
            obj.setMedicineTypeCode(medicineTypeCode);
            obj.setMedicineTypeDescription(medicineTypeDescription);
            obj.setMedicineTypeStatus("A");
            iRows = ObjDao.create(obj);
            Log.i(TAG, Method + iRows + " row(s) affected." );
            Log.i(TAG, Method + "End..."  );
        }catch (SQLException e){
            Log.e("Error creando registro"," .... ");
            e.printStackTrace();
        }
        return  iRows;
    }

    public static int updateMedicineTypeToDataBaseLocal(
            Dao<EMedicineType, Integer> ObjDao,
            int medicineTypeCode ,
            String medicineTypeDescription
    )throws java.sql.SQLException{
        String Method ="[updateMedicineTypeToDataBaseLocal]";
        Log.i(TAG, Method + "Init..."  );
        int iRows=0;
        try {
            UpdateBuilder<EMedicineType, Integer> updateBuilder = ObjDao.updateBuilder();
            // set the criteria like you would a QueryBuilder
            updateBuilder.where().eq("medicineTypeCode", medicineTypeCode);
            // update the value of your field(s)
            updateBuilder.updateColumnValue("medicineTypeDescription", medicineTypeDescription );
            // Execute Update
            iRows = updateBuilder.update()  ;
            Log.i(TAG, Method + iRows + " row(s) affected." );
            Log.i(TAG, Method + "End..."  );

        }catch (SQLException e){
            Log.e("Utils","Error actualizando registro .... ");
            e.printStackTrace();
        }
        return  iRows;
    }

    public static List<EMedicineType> getMedicineTypeFromDataBaseLocal(Dao<EMedicineType, Integer> ObjDao) throws SQLException, java.sql.SQLException{
        List<EMedicineType> lstObj = null;
        String query= "SELECT * FROM MedicineType WHERE medicineTypeStatus='A'" ;
        try {
            GenericRawResults<EMedicineType> obj = ObjDao.queryRaw(query, ObjDao.getRawRowMapper());
            lstObj = obj.getResults();
        }catch (SQLException e){
            Log.e("Utils","Error al obtener registros.... ");
            e.printStackTrace();
        }
        return lstObj;
    }
    //EAlarmReminderType
    public static int saveAlarmReminderTypeToDataBaseLocal(
            Dao<EAlarmReminderType, Integer> ObjDao,
            String reminderTypeCode ,
            String reminderTypeDescription
    )throws java.sql.SQLException{
        String Method ="[saveAlarmTypeToDataBaseLocal]";
        Log.i(TAG, Method + "Init..."  );
        int iRows=0;
        try {
            EAlarmReminderType obj = new EAlarmReminderType() ;
            obj.setReminderTypeCode(reminderTypeCode);
            obj.setReminderTypeDescription(reminderTypeDescription);
            obj.setReminderTypeStatus("A");
            iRows = ObjDao.create(obj);
            Log.i(TAG, Method + iRows + " row(s) affected." );
            Log.i(TAG, Method + "End..."  );
        }catch (SQLException e){
            Log.e("Error creando registro"," .... ");
            e.printStackTrace();
        }
        return  iRows;
    }

    public static List<EAlarmReminderType> getAlarmReminderTypeFromDataBaseLocal(Dao<EAlarmReminderType, Integer> ObjDao) throws SQLException, java.sql.SQLException{
        List<EAlarmReminderType> lstObj = null;
        String query= "SELECT * FROM AlarmReminderType WHERE reminderTypeStatus='A'" ;
        try {
            GenericRawResults<EAlarmReminderType> obj = ObjDao.queryRaw(query, ObjDao.getRawRowMapper());
            lstObj = obj.getResults();
        }catch (SQLException e){
            Log.e("Utils","Error al obtener registros.... ");
            e.printStackTrace();
        }
        return lstObj;
    }

    //AlarmReminderTime

    public static int saveAlarmReminderTimeToDataBaseLocal(
            Dao<EAlarmReminderTime, Integer> ObjDao,
            String reminderTypeCode ,
            int reminderTimeCode ,
            String reminderTimeDescription
    )throws java.sql.SQLException{
        String Method ="[saveAlarmReminderTimeToDataBaseLocal]";
        Log.i(TAG, Method + "Init..."  );
        int iRows=0;
        try {
            EAlarmReminderTime obj = new EAlarmReminderTime() ;
            obj.setReminderTypeCode(reminderTypeCode);
            obj.setReminderTimeCode (reminderTimeCode);
            obj.setReminderTimeDescription (reminderTimeDescription);
            obj.setReminderTimeStatus("A");
            iRows = ObjDao.create(obj);
            Log.i(TAG, Method + iRows + " row(s) affected." );
            Log.i(TAG, Method + "End..."  );
        }catch (SQLException e){
            Log.e("Error creando registro"," .... ");
            e.printStackTrace();
        }
        return  iRows;
    }

    public static List<EAlarmReminderTime> getAlarmReminderTimeFromDataBaseLocal(
            Dao<EAlarmReminderTime, Integer> ObjDao ,
            String reminderTypeCode ,
            int reminderTimeCode
    ) throws SQLException, java.sql.SQLException{
        List<EAlarmReminderTime> lstObj = null;
        if (reminderTypeCode==null) reminderTypeCode="";
        String query= "SELECT * FROM AlarmReminderTime WHERE reminderTimeStatus='A'" ;
        if (! reminderTypeCode.isEmpty()){
            query = query + " AND reminderTypeCode = '" +  reminderTypeCode +"'";
            if (reminderTimeCode != 0)
                query = query + " AND reminderTimeCode = " +  reminderTimeCode ;
        }
        try {
            GenericRawResults<EAlarmReminderTime> obj = ObjDao.queryRaw(query, ObjDao.getRawRowMapper());
            lstObj = obj.getResults();
        }catch (SQLException e){
            Log.e("Utils","Error al obtener registros.... ");
            e.printStackTrace();
        }
        return lstObj;
    }

    public static int saveAlarmDetailsToDataBaseLocal(
            Dao<EAlarmDetails, Integer> ObjDao,
            int registeredMedicinesId ,
            String alarmDetailHour ,
            String alarmDetailCreateDate,
            String email,
            int idServerDb
    )throws java.sql.SQLException{
        String Method ="[saveAlarmReminderTimeToDataBaseLocal]";
        Log.i(TAG, Method + "Init..."  );
        int iRows=0;
        try {
            EAlarmDetails obj = new EAlarmDetails() ;
            obj.setRegisteredMedicinesId(registeredMedicinesId);
            obj.setAlarmDetailHour (alarmDetailHour);
            alarmDetailCreateDate = alarmDetailCreateDate==null?"":alarmDetailCreateDate;
            obj.setAlarmDetailCreateDate( alarmDetailCreateDate.isEmpty() ? getDate("yyyy/MM/dd HH:mm:ss") : alarmDetailCreateDate );
            obj.setAlarmDetailStatus("A");

            obj.setEmail(email);
            obj.setOperationDb("I");
            obj.setSentWs( idServerDb==0?"N":"S" );
            obj.setIdServerDb(idServerDb);
            iRows = ObjDao.create(obj);
            Log.i(TAG, Method + iRows + " row(s) affected." );
            iRows = obj.getAlarmDetailId();
            Log.i(TAG, Method + " AlarmDetailId="+iRows );
        }catch (SQLException e){
            Log.e("Error creando registro"," .... ");
            e.printStackTrace();
        }
        Log.i(TAG, Method + "End..."  );
        return  iRows;
    }

    public static int deleteAlarmDetailsToDataBaseLocal(
            Dao<EAlarmDetails, Integer> ObjDao,
            int registeredMedicinesId
    )throws java.sql.SQLException{
        String Method ="[deleteAlarmDetailsToDataBaseLocal]";
        Log.i(TAG, Method + "Init..."  );
        int iRows=0;
        try {
            UpdateBuilder<EAlarmDetails, Integer> updateBuilder = ObjDao.updateBuilder();
            updateBuilder.where().eq("registeredMedicinesId",registeredMedicinesId).and().eq("alarmDetailStatus","A");
            updateBuilder.updateColumnValue("alarmDetailStatus","I");
            iRows = updateBuilder.update();
            Log.i(TAG, Method + iRows + " row(s) affected." );

        }catch (SQLException e){
            Log.e("Error creando registro"," .... ");
            e.printStackTrace();
        }
        Log.i(TAG, Method + "End..."  );
        return  iRows;
    }



    public static List<EAlarmDetails> getEAlarmDetailsFromDataBaseLocal(
            Dao<EAlarmDetails, Integer> ObjDao ,
            int registeredMedicinesId,
            int alarmDetailId,
            String alarmStartDate,
            String alarmEndDate,
            String alarmStartHour,
            String alarmEndHour
    ) throws SQLException, java.sql.SQLException{
        String Method="[getEAlarmDetailsFromDataBaseLocal]";
        List<EAlarmDetails> lstObj = new ArrayList<>();
        if (alarmStartDate == null) alarmStartDate="";
        if (alarmEndDate == null) alarmEndDate=alarmStartDate;
        if (alarmStartHour == null) alarmStartHour="00:00:00";
        if (alarmEndHour == null) alarmEndHour="23:59:59";
        String query= "SELECT ad.alarmDetailId , rm.id as registeredMedicinesId, substr(rm.fechaInicio,1,10) alarmDetailDate, ad.alarmDetailHour, " +
                "ad.alarmDetailStatus, ad.alarmDetailCreateDate, m.idMedicamento as medicineCode , m.nombre as medicineDescription , " +
                "ad.email , ad.idUsuario, ad.sentWs, ad.operationDb, ad.idServerDb " ;
               query=query + " FROM EAlarmDetails ad " ;
               query=query + " JOIN RMedicinesTable rm ON ad.registeredMedicinesId = rm.id " ;
               query=query + " JOIN MedicineTable m ON rm.id_medicacion = m.idMedicamento " ;
               query=query + " WHERE ad.alarmDetailStatus='A' AND registeredMedicinesStatus='A' " ;

               query=query + " AND ( rm.diasMedicacion = '0' OR rm.diasMedicacion LIKE '%" + Calendar.getInstance().getTime().getDay() + "%' )" ;
        if (registeredMedicinesId != 0)
            query = query + " AND ad.registeredMedicinesId = " + registeredMedicinesId ;
        if ( alarmDetailId != 0 )
            query = query + " AND ad.alarmDetailId = " + alarmDetailId ;
        if ( !(alarmStartDate.isEmpty()) ){
            //query = query + " AND DATETIME('" + alarmStartDate + "') BETWEEN "; // Se debe usar formato yyyyMMdd or yyyy-MM-dd , no acpeta segundos.
            //query = query + " DATETIME( replace( substr(rm.fechaInicio,1,10),'/','-') ) AND DATETIME( replace( substr(coalesce(rm.fechaFin,rm.fechaInicio),1,10),'/','-') ) ";
            query=query + " AND DATETIME(replace(substr(rm.fechaInicio,1,10),'/','-')) >= DATETIME('" + alarmStartDate + "')" ;
            query=query + " AND DATETIME('" + alarmEndDate + "') <= DATETIME(replace(substr(coalesce(rm.fechaFin,rm.fechaInicio),1,10),'/','-')) " ;
        }
        query = query + " ORDER BY ad.alarmDetailHour ASC "  ;

        Log.i(TAG, Method + "Query =" + query  );
        try {
            GenericRawResults<EAlarmDetails> obj = ObjDao.queryRaw(query, ObjDao.getRawRowMapper());
            lstObj = obj.getResults();
            Log.i(TAG, Method + lstObj.size() + " row(s) affected." );
        }catch (SQLException e){
            Log.e("Utils","Error al obtener registros.... ");
            e.printStackTrace();
        }
        return lstObj;
    }

    public static int saveAlarmTakeMedicineToDataBaseLocal(
            Dao<EAlarmTakeMedicine, Integer> ObjDao,
            int registeredMedicinesId,
            int alarmDetailId ,
            String MedicineTakeDate,
            String email
            ,String operationDB
            ,String sentServer

//            ,int registeredMedicinesIdServerDB,
//            int alarmDetailIdServerDB

    )throws java.sql.SQLException{
        String Method ="[saveAlarmReminderTimeToDataBaseLocal]";
        Log.i(TAG, Method + "Init..."  );
        int iRows=0;
        try {
            EAlarmTakeMedicine obj = new EAlarmTakeMedicine() ;
            obj.setRegisteredMedicinesId(registeredMedicinesId);
            obj.setAlarmDetailId(alarmDetailId);
            obj.setAlarmTakeMedicineDate(MedicineTakeDate);
            obj.setAlarmTakeMedicineStatus("A");
            obj.setEmail(email);
            obj.setOperationDb(operationDB);
            obj.setSentWs(sentServer);

//            if (registeredMedicinesIdServerDB != 0 ){
//                obj.setRegisteredMedicinesIdServerDB(registeredMedicinesIdServerDB);
//                if (alarmDetailIdServerDB != 0 )
//                    obj.setAlarmDetailIdServerDB(alarmDetailIdServerDB);
//            }

            obj.setIdServerDb(0);
            iRows = ObjDao.create(obj);
            Log.i(TAG, Method + iRows + " row(s) affected." );
            iRows = obj.getAlarmTakeMedicineId();
            Log.i(TAG, Method + " AlarmTakeMedicineId="+iRows );
        }catch (SQLException e){
            Log.e("Error creando registro"," .... ");
            e.printStackTrace();
        }
        Log.i(TAG, Method + "End..."  );
        return  iRows;
    }


    public static List< EAlarmTakeMedicine > GetMedicineTakeByUserDBLocal(String UserEmail ,
                                                                          String UserId ,
                                                                          Dao<EAlarmTakeMedicine,Integer> EAlarmTakeMedicineDao )throws java.sql.SQLException {
        List<EAlarmTakeMedicine> lstEAlarmTakeMedicine = null;
        String Method ="[GetMedicineTakeByUserDBLocal]";
        Log.i(TAG, Method + "Init..."  );
        try {
            String query= " SELECT " +
                    " atm.alarmTakeMedicineId, atm.alarmDetailId, atm.alarmTakeMedicineDate, ad.alarmDetailHour, me.idMedicamento AS medicineCode , me.nombre AS medicineDescription , " +
                    " atm.email , atm.idUsuario , atm.sentWs , atm.operationDb , atm.idServerDb " +
                    " FROM EAlarmTakeMedicine atm " +
                    " JOIN EAlarmDetails ad ON atm.alarmDetailId = ad.alarmDetailId " +
                    " JOIN RMedicinesTable rm ON ad.registeredMedicinesId = rm.id " +
                    " JOIN MedicineTable me ON rm.id_medicacion = me.idMedicamento " +
                    //" JOIN MedicineUserTable mu ON me.idMedicamento = mu.idMedicacion " +

                    //" JOIN MedicineType mt ON me.medicineTypeCode = mt.medicineTypeCode " +
                    //" JOIN AlarmReminderType rtp ON rm.reminderTypeCode = rtp.reminderTypeCode " +
                    //" JOIN AlarmReminderTime rtm ON rtp.reminderTypeCode=rtm.reminderTypeCode AND rm.reminderTimeCode = rtm.reminderTimeCode " +
                    " WHERE atm.alarmTakeMedicineStatus = 'A' " +
                    " AND  ad.alarmDetailStatus in ('A','I') " +
                    " AND  rm.registeredMedicinesStatus = 'A' " ;
            if (UserEmail.isEmpty())
                query=query + " AND rm.idUsuario = '"+ UserId +"'" ;
            else{query=query + " AND rm.email = '"+ UserEmail +"'" ;}

            query=query + " ORDER BY alarmTakeMedicineDate DESC , atm.alarmTakeMedicineId ASC ";

            Log.i(TAG, Method + "Executing query=" + query );
            GenericRawResults<EAlarmTakeMedicine> rawResults = EAlarmTakeMedicineDao.queryRaw(query, EAlarmTakeMedicineDao.getRawRowMapper());
            Log.i(TAG, Method + "Executed query..."  );
            Log.i(TAG, Method + "Init getting List of Control Registered Medicine..."  );
            lstEAlarmTakeMedicine = rawResults.getResults();
            Log.i(TAG, Method + "End getting List of Control Registered Medicine..."  );
        }catch (java.sql.SQLException e ){
            e.printStackTrace();
        }
        Log.i(TAG, Method + "End..."  );
        return lstEAlarmTakeMedicine ;
    }


    public static void restartAlarmService(Context context){
        try {
            String Method = "[restartAlarmService]";
            Log.i("Utils", Method + "Init..."  );
            context.stopService(new Intent(context, AlarmGetAllMedicineService.class));
            context.startService(new Intent(context, AlarmGetAllMedicineService.class));
            Log.i("Utils", Method + "Init..."  );
        }catch (Exception e){
            e.printStackTrace();
        }
    }


	    public static List<EAlarmDetails> GetRegisterAlarmMedicationByIdNotSendFromDatabase(Dao<EAlarmDetails, Integer> EAlarmDetailsDao,
                                                                                        int registerMedicationId,
                                                                                        String sentServer) throws java.sql.SQLException {
        String Method="[GetRegisterAlarmMedicationByIdNotSendFromDatabase]";
        Log.i(TAG, Method + "Init..."  );
        List<EAlarmDetails> lstAlarmDetails = null;
        String sentWs="N";
        if (sentServer.equals("true")) sentWs="S";
        String query = "SELECT * FROM EAlarmDetails WHERE sentWs = '"+ sentWs +"'";
        query = query + " AND registeredMedicinesId = " + registerMedicationId ;
        query = query + " ORDER BY alarmDetailId ASC ";
        Log.i(TAG, Method + "Query = " + query  );
        Log.i(TAG, Method + "Sending query..."  );
        try {
            GenericRawResults<EAlarmDetails> rawResults = EAlarmDetailsDao.queryRaw(query, EAlarmDetailsDao.getRawRowMapper());
            Log.i(TAG, Method + "Executing query..."  );
            lstAlarmDetails = rawResults.getResults();
        }catch (Exception e){
            lstAlarmDetails=null;
            Log.e(TAG, Method + "Error al consultar datos. " +"\n"+ e.getMessage());
        }
        Log.i(TAG, Method + "End..."  );
        return lstAlarmDetails;
    }



    public static List<EAlarmDetails> GetRegisterAlarmMedicationNotSendFromDatabase(Dao<EAlarmDetails, Integer> EAlarmDetailsDao, String sentServer) throws java.sql.SQLException {
        String Method="[GetMedicineUserControlNotSendFromDatabase]";
        Log.i(TAG, Method + "Init..."  );
        List<EAlarmDetails> lstAlarmDetails = null;
        String sentWs="N";
        if (sentServer.equals("true")) sentWs="S";
        String query = "SELECT * FROM RMedicinesTable WHERE sentWs = '"+ sentWs +"'";
        query = query + " ORDER BY id ASC ";
        Log.i(TAG, Method + "Query = " + query  );
        Log.i(TAG, Method + "Sending query..."  );
        try {
            GenericRawResults<EAlarmDetails> rawResults = EAlarmDetailsDao.queryRaw(query, EAlarmDetailsDao.getRawRowMapper());
            Log.i(TAG, Method + "Executing query..."  );
            lstAlarmDetails = rawResults.getResults();
            Log.i(TAG, Method + "Init getting List of Medicine User Control..."  );
        }catch (Exception e){
            lstAlarmDetails=null;
            Log.e(TAG, Method + "Error al consultar datos. " +"\n"+ e.getMessage());
        }
        Log.i(TAG, Method + "End..."  );
        return lstAlarmDetails;
    }


    public static int sendOkUpdateMedicineAlarmFromDatabase(
            int    alarmDetailId ,
            int    idServerDb                   ,
            String sentServer 			        ,
            Dao<EAlarmDetails, Integer> EAlarmDetailsDao
            //Dao<EAlarmTakeMedicine, Integer> EAlarmTakeMedicineDao
    )throws java.sql.SQLException{
        String Method ="[sendOkUpdateMedicineAlarmFromDatabase]";
        String sentWs = "S";
        Log.i("[Utils]", Method + "Init..."  );
        if (sentServer.equals("false")) sentWs="N" ;
        int iRows=0;
        try {
            UpdateBuilder<EAlarmDetails, Integer> updateBuilder = EAlarmDetailsDao.updateBuilder();
            // set the criteria like you would a QueryBuilder
            updateBuilder.where().eq("alarmDetailId", alarmDetailId);
            // update the value of your field(s)
            updateBuilder.updateColumnValue("sentWs", sentWs );
            updateBuilder.updateColumnValue("idServerDb", idServerDb );
            // Execute Update
            iRows = updateBuilder.update()  ;
            Log.i("[Utils]", Method + iRows + " row(s) affected." );
//            if (iRows>0){
//                UpdateBuilder<EAlarmTakeMedicine , Integer> updateBuilder2 = EAlarmTakeMedicineDao.updateBuilder();
//                // set the criteria like you would a QueryBuilder
//                updateBuilder2.where().eq("registeredMedicinesId", registeredMedicinesId);
//                // update the value of your field(s)
//                updateBuilder2.updateColumnValue("registeredMedicinesIdServerDB", registeredMedicinesIdServerDB );
//                //updateBuilder2.updateColumnValue("registeredMedicinesIdServerDB", RegisteredMedicinesIdServerDB );
//                // Execute Update
//                iRows = updateBuilder2.update()  ;
//            }


        }catch (SQLException e){
            Log.e("[Utils]", Method +  "Error actualizando registro .... ");
            e.printStackTrace();
        }
        Log.i("[Utils]", Method + "End..."  );
        return  iRows;
    }

    public static List<EAlarmTakeMedicine> GetRegisterAlarmTakeMedicineNotSendFromDatabase(Dao<EAlarmTakeMedicine, Integer> EAlarmTakeMedicineDao,

                                                                                        String sentServer) throws java.sql.SQLException {
        String Method="[GetRegisterAlarmTakeMedicineNotSendFromDatabase]";
        Log.i(TAG, Method + "Init..."  );
        List<EAlarmTakeMedicine> lstAlarmTakeMedicine = null;
        String sentWs="N";
        if (sentServer.equals("true")) sentWs="S";
        String query = "SELECT atm.alarmTakeMedicineId, adm.idServerDb AS alarmDetailId, mt.idServerDb AS registeredMedicinesId, " +
                " adm.alarmDetailHour AS alarmDetailHour , atm.alarmTakeMedicineDate, atm.alarmTakeMedicineStatus, atm.medicineCode, atm.medicineDescription, " +
                " atm.email, atm.idUsuario, atm.sentWs, atm.operationDb, atm.idServerDb " +
                " FROM EAlarmTakeMedicine atm " +
                " JOIN EAlarmDetails adm ON atm.alarmDetailId = adm.alarmDetailId " +
                " JOIN RMedicinesTable mt ON adm.registeredMedicinesId = mt.id " +
                " WHERE adm.idServerDb <> 0 AND mt.idServerDb <> 0 AND atm.sentWs = '"+ sentWs +"' " +
//                " UNION " +
//                "SELECT atm.alarmTakeMedicineId, ad.idServerDb as alarmDetailId, m.idServerDb as registeredMedicinesId, " +
//                "atm.alarmDetailHour, atm.alarmTakeMedicineDate, atm.alarmTakeMedicineStatus " +
//                "FROM EAlarmTakeMedicine atm  " +
//                "JOIN EAlarmDetails ad ON atm.alarmDetailId = ad.alarmDetailId " +
//                "JOIN RMedicinesTable m ON ad.registeredMedicinesId = m.id " +
//                "WHERE ad.idServerDb != 0 AND m.idServerDb != 0 AND atm.sentWs = '"+ sentWs +"'" +
                "";

        Log.i(TAG, Method + "Query = " + query  );
        Log.i(TAG, Method + "Sending query..."  );
        try {
            GenericRawResults<EAlarmTakeMedicine> rawResults = EAlarmTakeMedicineDao.queryRaw(query, EAlarmTakeMedicineDao.getRawRowMapper());
            Log.i(TAG, Method + "Executing query..."  );
            lstAlarmTakeMedicine = rawResults.getResults();
        }catch (Exception e){
            lstAlarmTakeMedicine=null;
            Log.e(TAG, Method + "Error al consultar datos. " +"\n"+ e.getMessage());
        }
        Log.i(TAG, Method + "End..."  );
        return lstAlarmTakeMedicine;
    }

    public static int sendOkUpdateAlarmTakeMedicineFromDatabase(
            int    alarmTakeMedicineId ,
            int    idServerDb ,
            String sentServer ,
            Dao<EAlarmTakeMedicine, Integer> EAlarmTakeMedicineDao
    )throws java.sql.SQLException{
        String Method ="[sendOkUpdateAlarmTakeMedicineFromDatabase]";
        String sentWs="N";
        if (sentServer.equals("true")) sentWs="S";
        Log.i("[Utils]", Method + "Init..."  );
        int iRows=0;
        try {
            UpdateBuilder<EAlarmTakeMedicine, Integer> updateBuilder = EAlarmTakeMedicineDao.updateBuilder();
            // set the criteria like you would a QueryBuilder
            updateBuilder.where().eq("alarmTakeMedicineId", alarmTakeMedicineId);
            // update the value of your field(s)
            updateBuilder.updateColumnValue("idServerDb", idServerDb );
            updateBuilder.updateColumnValue("sentWs" , sentWs);

            // Execute Update
            iRows = updateBuilder.update()  ;
            Log.i("[Utils]", Method + iRows + " row(s) affected." );
        }catch (SQLException e){
            Log.e("[Utils]", Method +  "Error actualizando registro .... ");
            e.printStackTrace();
        }
        Log.i("[Utils]", Method + "End..."  );
        return  iRows;
    }



    public static int sendOkUpdateAlarmTakeMedicineFromDatabase_1(
            int    alarmDetailId ,

            int    RegisteredMedicinesIdServerDB ,

            Dao<EAlarmTakeMedicine, Integer> EAlarmTakeMedicineDao
    )throws java.sql.SQLException{
        String Method ="[sendOkUpdateAlarmTakeMedicineFromDatabase_1]";
        Log.i("[Utils]", Method + "Init..."  );
        int iRows=0;
        try {
            UpdateBuilder<EAlarmTakeMedicine, Integer> updateBuilder = EAlarmTakeMedicineDao.updateBuilder();
            // set the criteria like you would a QueryBuilder

            updateBuilder.where().eq("alarmDetailId", alarmDetailId);
            // update the value of your field(s)
            updateBuilder.updateColumnValue("registeredMedicinesIdServerDB", RegisteredMedicinesIdServerDB );

            // Execute Update
            iRows = updateBuilder.update()  ;
            Log.i("[Utils]", Method + iRows + " row(s) affected." );
        }catch (SQLException e){
            Log.e("[Utils]", Method +  "Error actualizando registro .... ");
            e.printStackTrace();
        }
        Log.i("[Utils]", Method + "End..."  );
        return  iRows;
    }

    public static void DeleterowsAllMedicinesUser(Dao<EMedicineUser, Integer> rowDao) throws java.sql.SQLException {
        DeleteBuilder<EMedicineUser, Integer> deleteBuilder = rowDao.deleteBuilder();
        deleteBuilder.delete();
    }

    public static void DeleterowsAllMedicinesControl(Dao<IRegisteredMedicines, Integer> rowDao) throws java.sql.SQLException {
        DeleteBuilder<IRegisteredMedicines, Integer> deleteBuilder = rowDao.deleteBuilder();
        deleteBuilder.delete();
    }
    public static void DeleterowsAllMedicinesAlarm(Dao<EAlarmDetails, Integer> rowDao) throws java.sql.SQLException {
        DeleteBuilder<EAlarmDetails, Integer> deleteBuilder = rowDao.deleteBuilder();
        deleteBuilder.delete();
    }
    public static void DeleterowsAllMedicinesTake(Dao<EAlarmTakeMedicine , Integer> rowDao) throws java.sql.SQLException {
        DeleteBuilder<EAlarmTakeMedicine, Integer> deleteBuilder = rowDao.deleteBuilder();
        deleteBuilder.delete();
    }




 /* FIN ALARMAS*/



    // OBTENER REGISTROS DE INSULINA PARA PDF
    public static List<EInsulin>GetInsulinFromDatabasePDF(Dao<EInsulin, Integer> InsulinDao, String FI,String FF) throws SQLException, java.sql.SQLException {

        List<EInsulin> Insulin = null;
        String query = "SELECT id , idBdServer , insulina , observacion" +
                ", " + getDateStr("fecha","") +
                ", hora , enviadoServer , operationDb " +
                " FROM InsulinTable WHERE fecha || ' ' || hora >= '" + getDateYYYYMMDD( FI ) + "' AND fecha || ' ' || hora <= '" + getDateYYYYMMDD( FF ) + "'  order by id DESC"; //LIMIT 5
        GenericRawResults<EInsulin> rawResults = InsulinDao.queryRaw(query, InsulinDao.getRawRowMapper());
        Insulin = rawResults.getResults();
        return Insulin;
    }
    //nuevo metodo de consulta glucosa para el pdf
    public static List<IGlucose>GetGlucoseFromDatabasePDF(Dao<IGlucose, Integer> IGlucoseDao, String FI, String FF) throws SQLException, java.sql.SQLException {
        List<IGlucose> Glucose = null;
        Log.e(TAG,"entra al utils");
        String query = // "SELECT fecha, concentracion, observacion" +
                "SELECT id , idBdServer , concentracion " +
                ", " + getDateStr("fecha","") +
                ", hora , observacion , enviadoServer , operacion " +
                " FROM GlucoseTable WHERE fecha >= '" + getDateYYYYMMDD( FI ) + "' AND fecha <= '" + getDateYYYYMMDD( FF ) + "' order by id DESC"; //LIMIT 5
        GenericRawResults<IGlucose> rawResults = IGlucoseDao.queryRaw(query, IGlucoseDao.getRawRowMapper());
        Glucose = rawResults.getResults();
        return Glucose;

    }
    // CONSULTA PULSO-PRESION PARA EL PDF
    public static List<IPulse> GetPulseFromDatabasePDF(Dao<IPulse, Integer> PulseDao , String FI, String FF) throws SQLException, java.sql.SQLException {
        List<IPulse> pulse = null;
        String query = // "SELECT fecha, concentracion, maxPressure, minPressure, medido, observacion" +
                "SELECT  id , idBdServer , concentracion , maxPressure , minPressure " +
                ", " + getDateStr("fecha","") +
                ", hora , medido , observacion , enviadoServer , operacion " +
                " FROM PulseTable WHERE fecha >= '" + getDateYYYYMMDD( FI ) + "' AND fecha <= '" + getDateYYYYMMDD( FF ) + "' order by fecha DESC , hora DESC";
        GenericRawResults<IPulse> rawResults = PulseDao.queryRaw(query, PulseDao.getRawRowMapper());
        pulse = rawResults.getResults();
        return pulse;
    }


// OBTENER REGISTROS DE ASMA PARA PDF

    public static List<IAsthma> GetAsthmaFromDatabasePDF(Dao<IAsthma, Integer> AsthmaDao, String FI, String FF) throws SQLException, java.sql.SQLException {
        List<IAsthma> asthma = null;
        String query = " SELECT id , idBdServer , flujoMaximo" +
                ", " + getDateStr("fecha","") +
                ", hora , observacion , enviadoServer  , operationDb " +
                " FROM AsthmaTable WHERE fecha >= '" + getDateYYYYMMDD( FI ) + "' AND fecha <= '" + getDateYYYYMMDD( FF ) + "' order by fecha DESC , hora DESC";
        GenericRawResults<IAsthma> rawResults = AsthmaDao.queryRaw(query, AsthmaDao.getRawRowMapper());
        asthma = rawResults.getResults();
        return asthma;
    }

    //CONSULTA DE PESO PARA PDF
    public static List<IWeight> GetWeightFromDatabasePDF(Dao<IWeight, Integer> WeightDao, String FI,String FF) throws SQLException, java.sql.SQLException {
        List<IWeight> weight = null;
        String query = //"SELECT fecha, peso, masamuscular, tmb, dmo, porcentajeAgua, porcentajeGrasa, observacion" +
                " SELECT id, idBdServer , peso, masamuscular, tmb, dmo, porcentajeAgua, porcentajeGrasa " +
                ", " + getDateStr("fecha","") +
                ", hora, observacion, enviadoServer , operacion " +
                " FROM WeightTable WHERE fecha >= '" + getDateYYYYMMDD( FI ) + "' AND fecha <= '" + getDateYYYYMMDD( FF ) + "' order by fecha DESC , hora DESC"; //LIMIT 5
        GenericRawResults<IWeight> rawResults = WeightDao.queryRaw(query, WeightDao.getRawRowMapper());
        weight = rawResults.getResults();
        return weight;
    }

    private static String getDateYYYYMMDD(String date){
        //25/01/2017 20:00:00
        String getDateYYYYMMDD="";
        String YYYY="";
        String MM="";
        String DD="";
        String HHmmss="";

        if (date.length()==10){
            YYYY = date.substring(6,10);
            MM = date.substring(3,5);
            DD = date.substring(0,2);
            getDateYYYYMMDD=YYYY + "/" + MM + "/" + DD ;
        }else if (date.length() > 10){
            YYYY = date.substring(6,10);
            MM = date.substring(3,5);
            DD = date.substring(0,2);
            HHmmss = date.substring(11);
            getDateYYYYMMDD=YYYY + "/" + MM + "/" + DD + " " + HHmmss ;
        } else{
            YYYY = date.substring(6,10);
            MM = date.substring(3,5);
            DD = date.substring(0,2);
            getDateYYYYMMDD=YYYY + "/" + MM + "/" + DD  ;
        }
        System.out.println("--------------------------"+ getDateYYYYMMDD);
        return getDateYYYYMMDD;
    }

    private static String getDateStr(String fieldDate, String fieldHour){
        //2017/09/23
        String getDateStr = "" ;
        getDateStr = " substr(" + fieldDate + ",9,2) || '/' || " +
                     " substr(" + fieldDate + ",6,2) || '/' || " +
                     " substr(" + fieldDate + ",1,4) " ;
        if (! fieldHour.isEmpty())
            getDateStr=getDateStr + " || ' ' || " + fieldHour ;
        getDateStr=getDateStr +  " AS " + fieldDate ;

        return getDateStr;
    }

//    public static float getRegisteredMedicinesIdFromDataBaseLocal(
//            Dao<IRegisteredMedicines, Integer> RegisteredMedicinesDao,
//            int idServerDb) throws java.sql.SQLException {
//        String Method ="[getRegisteredMedicinesIdFromDataBaseLocal]";
//        Log.i(TAG, Method + "Init..."  );
//        float fRows = 0;
//        try {
//            String query="SELECT id AS registeredMedicinesId FROM RMedicinesTable " +
//                    "WHERE idServerDb="+idServerDb;
//            Log.i(TAG, Method + "query=" + query  );
//            Log.i(TAG, Method + "Init executing query..."  );
//            fRows = RegisteredMedicinesDao.queryRawValue(query);
//            Log.i(TAG, Method + "End executing query..."  );
//            Log.i(TAG, Method + "fRows=" +fRows );
//
//            Log.i(TAG, Method + " IRegisteredMedicines.id="+fRows  );
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        Log.i(TAG, Method + "End..."  );
//        return  fRows;
//    }
//
//    public static float getAlarmDetailIdFromDataBaseLocal(
//            Dao<EAlarmDetails, Integer> EAlarmDetailsDao,
//            int idServerDb) throws java.sql.SQLException {
//        String Method ="[getAlarmDetailIdFromDataBaseLocal]";
//        Log.i(TAG, Method + "Init..."  );
//        float fRows = 0;
//        try {
//            String query="SELECT id AS registeredMedicinesId FROM EAlarmDetails " +
//                    "WHERE idServerDb="+idServerDb;
//            Log.i(TAG, Method + "query=" + query  );
//            Log.i(TAG, Method + "Init executing query..."  );
//            fRows = EAlarmDetailsDao.queryRawValue(query);
//            Log.i(TAG, Method + "End executing query..."  );
//            Log.i(TAG, Method + "fRows=" +fRows );
//
//            Log.i(TAG, Method + " IRegisteredMedicines.id="+fRows  );
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        Log.i(TAG, Method + "End..."  );
//        return  fRows;
//    }

    public static EAlarmDetails getAlarmDetailIdFromDataBaseLocal(
            Dao<EAlarmDetails, Integer> EAlarmDetailsDao,
            int idServerDb) throws java.sql.SQLException {
        String Method ="[getAlarmDetailIdFromDataBaseLocal]";
        Log.i(TAG, Method + "Init..."  );
        EAlarmDetails eAlarmDetails = null;
        try {
            String query="SELECT * FROM EAlarmDetails WHERE idServerDb=" + idServerDb ;
            Log.i(TAG, Method + "query=" + query  );
            Log.i(TAG, Method + "Init executing query..."  );
            GenericRawResults<EAlarmDetails> rawResults = EAlarmDetailsDao.queryRaw(query, EAlarmDetailsDao.getRawRowMapper());
            Log.i(TAG, Method + "End executing query..."  );
            List<EAlarmDetails> lstAlarmDetails = rawResults.getResults();
            Log.i(TAG, Method + "lstAlarmDetails.size()=" +lstAlarmDetails.size() );
            eAlarmDetails = null;
            if (lstAlarmDetails != null)
                if(lstAlarmDetails.size()>0)
                    eAlarmDetails = lstAlarmDetails.get(0);
        } catch (SQLException e) {
            eAlarmDetails = null;
            e.printStackTrace();
        }
        Log.i(TAG, Method + "End..."  );
        return eAlarmDetails;
    }

    public static <T> T getLastRecordWithDate(Dao<T,Integer> data, String tableName) throws java.sql.SQLException {
        String query ="SELECT fecha FROM "+tableName+" ORDER By date(fecha) DESC LIMIT 1";
        GenericRawResults<T> rawResults = data.queryRaw(query, data.getRawRowMapper());

        return rawResults.getFirstResult();
    }

    public static <T> T getLastRecord(Dao<T,Integer> data,String tableName) throws java.sql.SQLException {
        String query = "SELECT * FROM "+tableName+" ORDER By date(fecha) DESC LIMIT 1";
        GenericRawResults<T> rawResults = data.queryRaw(query,data.getRawRowMapper());
        return rawResults.getFirstResult();
    }
}