package com.grupocisc.healthmonitor.Pulse.activities;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.os.Vibrator;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;
import com.grupocisc.healthmonitor.HealthMonitorApplicattion;
import com.grupocisc.healthmonitor.Insulin.TextValidator;
import com.grupocisc.healthmonitor.Pulse.Funtion.ImageProcessing;
import com.grupocisc.healthmonitor.R;
import com.grupocisc.healthmonitor.Utils.Utils;
import com.grupocisc.healthmonitor.entities.IRegCrtPacient;
import com.satsuware.usefulviews.LabelledSpinner;

import java.util.Calendar;
import java.util.concurrent.atomic.AtomicBoolean;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Raymond on 03/01/2017.
 */

public class HeartRateMonitor extends AppCompatActivity implements LabelledSpinner.OnItemChosenListener{

    private static final String TAG = "HeartRateMonitor";
    private static final AtomicBoolean processing = new AtomicBoolean(false);

    private static SurfaceView preview = null;
    private static SurfaceHolder previewHolder = null;
    private static Camera camera = null;
    private static View image = null;
    private static TextView text = null;
    private EditText txt_concentration , txt_observation;
    private EditText txt_maxpressure, txt_minpressure;
    private static String beatsPerMinuteValue="";
    private static PowerManager.WakeLock wakeLock = null;
   // private static TextView mTxtVwStopWatch;
    private static int averageIndex = 0;
    private static final int averageArraySize = 4;
    private static final int[] averageArray = new int[averageArraySize];
    private static String strSavedDoctorID="";
    private static Context parentReference = null;
    private int year, month, day, hour, minute;
    private String hora ="", fecha="";
    private String selectTextSpinner;
    private int selectPosicionSpinner;
    public ProgressDialog Dialog;
    public static enum TYPE {
        GREEN, RED
    };
    FloatingActionButton menu1,menu2,menu3 ;
    private static TYPE currentType = TYPE.GREEN;

    public static TYPE getCurrent() {
        return currentType;
    }

    private static int beatsIndex = 0;
    private static final int beatsArraySize = 3;
    private static final int[] beatsArray = new int[beatsArraySize];
    private static double beats = 0;
    private static long startTime = 0;
    private static Vibrator v ;
    ProgressBar mprogressBar;

    private static final String enviadoServer = "false";
    private Call<IRegCrtPacient.RegCrtPacient> call_1;
    private String operacionI = "I";

    private LinearLayout layoutMain;
    private RelativeLayout layoutButtons;
    private RelativeLayout layoutContent;
    private CardView cardReg;


    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pulse_main);
        Utils.SetStyleToolbarLogo(this);
        Dialog = new ProgressDialog(HeartRateMonitor.this);
        v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        parentReference=this;
        preview = (SurfaceView) findViewById(R.id.preview);
        previewHolder = preview.getHolder();
        previewHolder.addCallback(surfaceCallback);
        previewHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        image = findViewById(R.id.image);
        text = (TextView) findViewById(R.id.text);
        txt_maxpressure = (EditText) findViewById(R.id.txt_maxpressure);
        txt_minpressure = (EditText) findViewById(R.id.txt_minpressure);
        txt_observation = (EditText) findViewById(R.id.txt_observation);
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, "DoNotDimScreen");
        prepareCountDownTimer();
        mprogressBar = (ProgressBar) findViewById(R.id.circular_progress_bar);
        ObjectAnimator anim = ObjectAnimator.ofInt(mprogressBar, "progress", 0, 100);
        anim.setDuration(34000);
        menu1 = (FloatingActionButton)findViewById(R.id.subFloatingMenu1) ;
        menu2 = (FloatingActionButton)findViewById(R.id.subFloatingMenu2) ;

        layoutMain = (LinearLayout) findViewById(R.id.layoutMain);
        layoutButtons = (RelativeLayout) findViewById(R.id.layoutButtons);
        layoutContent = (RelativeLayout) findViewById(R.id.layoutContent);
        cardReg       = (CardView) findViewById(R.id.cardReg);

        menu1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveDataPulseDB(v);


            }
        });

        menu2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(HeartRateMonitor.this,PulseActivityAuto.class));
                finish();
            }
        });


        anim.setInterpolator(new DecelerateInterpolator());
        anim.start();
        inicializarFechaHora();
        setearMaterialBetterSpinner();

        txt_maxpressure.addTextChangedListener(new TextValidator(txt_maxpressure) {
            @Override
            public void validate(EditText editText, String text) {
                if (!text.isEmpty() && Utils.isNumeric(text))

                {
                    if (Float.parseFloat(text) > 230)
                        txt_maxpressure.setError("El valor de su presion sistólica no puede ser mayor a 230 Por favor Verifique");
                    if (Float.parseFloat(text) < 50)
                        txt_maxpressure.setError("El valor su presion sistólica no puede ser menor a 50.  Por favor Verifique");
                }
            }
        });

        txt_minpressure.addTextChangedListener(new TextValidator(txt_minpressure) {
            @Override
            public void validate(EditText editText, String text) {
                if (!text.isEmpty() && Utils.isNumeric(text))

                {
                    if (Float.parseFloat(text) > 135)
                        txt_minpressure.setError("El valor de su presion diastólica no puede ser mayor a 135 Por favor Verifique");
                    if (Float.parseFloat(text) < 35)
                        txt_minpressure.setError("El valor su presion diastólica no puede ser menor a 35.  Por favor Verifique");
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


    public void setearMaterialBetterSpinner(){

        LabelledSpinner labelledSpinner = (LabelledSpinner) findViewById(R.id.spinner_planets);
        labelledSpinner.setColor(R.color.colorPrimaryDark);
        labelledSpinner.setItemsArray(R.array.pluse_array);
       // labelledSpinner.setDefaultErrorEnabled(true);
        //labelledSpinner.setDefaultErrorText("Por favor seleccionar una opción.");  // Displayed when first item remains selected
        labelledSpinner.setOnItemChosenListener(this);
        //SETEAR LA POSICION DEL SPINNER QUE APARESCA SELECIONADO
        labelledSpinner.setSelection(0 , true);

    }
    //se ejecuta el selecionar el spinner
    @Override
    public void onItemChosen(View labelledSpinner, AdapterView<?> adapterView, View itemView, int position, long id) {
        String selectedText = adapterView.getItemAtPosition(position).toString();
        switch (labelledSpinner.getId()) {
            case R.id.spinner_planets:
                //Toast.makeText(this, "Selected: " + selectedText, Toast.LENGTH_SHORT).show();
                selectTextSpinner = selectedText;
                selectPosicionSpinner= position;
                break;
            // If you have multiple LabelledSpinners, you can add more cases here
        }
    }
    //se ejecuta el selecionar el spinner
    @Override
    public void onNothingChosen(View labelledSpinner, AdapterView<?> adapterView) {
        // Do something here
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onResume() {
        super.onResume();
        wakeLock.acquire();
        camera = Camera.open();
        startTime = System.currentTimeMillis();

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onPause() {
        super.onPause();

        if (camera != null )
        {
        wakeLock.release();

        camera.setPreviewCallback(null);
        camera.stopPreview();
        camera.release();
        text.setText("0");
        camera = null;
        }
    }

    private static Camera.PreviewCallback previewCallback = new Camera.PreviewCallback() {

        @Override
        public void onPreviewFrame(byte[] data, Camera cam) {
            if (data == null) throw new NullPointerException();
            Camera.Size size = cam.getParameters().getPreviewSize();
            if (size == null) throw new NullPointerException();

            if (!processing.compareAndSet(false, true)) return;

            int width = size.width;
            int height = size.height;

            int imgAvg = ImageProcessing.decodeYUV420SPtoRedAvg(data.clone(), height, width);
            // Log.i(TAG, "imgAvg="+imgAvg);
            if (imgAvg == 0 || imgAvg == 255) {
                processing.set(false);
                return;
            }

            int averageArrayAvg = 0;
            int averageArrayCnt = 0;
            for (int i = 0; i < averageArray.length; i++) {
                if (averageArray[i] > 0) {
                    averageArrayAvg += averageArray[i];
                    averageArrayCnt++;
                }
            }

            int rollingAverage = (averageArrayCnt > 0) ? (averageArrayAvg / averageArrayCnt) : 0;
            TYPE newType = currentType;
            if (imgAvg < rollingAverage) {
                newType = TYPE.RED;
                if (newType != currentType) {
                    beats++;
                    // Log.d(TAG, "BEAT!! beats="+beats);
                }
            } else if (imgAvg > rollingAverage) {
                newType = TYPE.GREEN;
            }

            if (averageIndex == averageArraySize) averageIndex = 0;
            averageArray[averageIndex] = imgAvg;
            averageIndex++;

            // Transitioned from one state to another to the same
            if (newType != currentType) {
                currentType = newType;
                image.postInvalidate();
            }

            long endTime = System.currentTimeMillis();
            double totalTimeInSecs = (endTime - startTime) / 1000d;
            if (totalTimeInSecs >= 10) {
                double bps = (beats / totalTimeInSecs);
                int dpm = (int) (bps * 60d);
                if (dpm < 30 || dpm > 180) {
                    startTime = System.currentTimeMillis();
                    beats = 0;
                    processing.set(false);
                    return;
                }

                // Log.d(TAG,
                // "totalTimeInSecs="+totalTimeInSecs+" beats="+beats);

                if (beatsIndex == beatsArraySize) beatsIndex = 0;
                beatsArray[beatsIndex] = dpm;
                beatsIndex++;

                int beatsArrayAvg = 0;
                int beatsArrayCnt = 0;
                for (int i = 0; i < beatsArray.length; i++) {
                    if (beatsArray[i] > 0) {
                        beatsArrayAvg += beatsArray[i];
                        beatsArrayCnt++;
                    }
                }
                int beatsAvg = (beatsArrayAvg / beatsArrayCnt);
                text.setText(String.valueOf(beatsAvg));
                beatsPerMinuteValue= String.valueOf(beatsAvg);
                makePhoneVibrate();
              //  dispatchPubNubEvent(String.valueOf(beatsAvg));
               // showReadingCompleteDialog();
                //startTime = System.currentTimeMillis();
                beats = 0;
                ApagaCamara();

            }
            processing.set(false);
        }
    };



    private static void makePhoneVibrate(){
        v.vibrate(500);
    }

    private static SurfaceHolder.Callback surfaceCallback = new SurfaceHolder.Callback() {

        /**
         * {@inheritDoc}
         */
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            try {
                camera.setPreviewDisplay(previewHolder);
                camera.setPreviewCallback(previewCallback);
            } catch (Throwable t) {
                Log.e("PreviewDemo-surfaceCall", "Exception in setPreviewDisplay()", t);
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            Camera.Parameters parameters = camera.getParameters();
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            Camera.Size size = getSmallestPreviewSize(width, height, parameters);
            if (size != null) {
                parameters.setPreviewSize(size.width, size.height);
                Log.d(TAG, "Using width=" + size.width + " height=" + size.height);
            }
            camera.setParameters(parameters);
            camera.startPreview();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            // Ignore
        }
    };

    private static Camera.Size getSmallestPreviewSize(int width, int height, Camera.Parameters parameters) {
        Camera.Size result = null;

        for (Camera.Size size : parameters.getSupportedPreviewSizes()) {
            if (size.width <= width && size.height <= height) {
                if (result == null) {
                    result = size;
                } else {
                    int resultArea = result.width * result.height;
                    int newArea = size.width * size.height;

                    if (newArea < resultArea) result = size;
                }
            }
        }

        return result;
    }

    private static void prepareCountDownTimer(){
      //  mTxtVwStopWatch.setText("---");
      //  new CountDownTimer(10000, 1000) {

      //      public void onTick(long millisUntilFinished) {
               /// mTxtVwStopWatch.setText("Por favor Espere: " + (millisUntilFinished) / 1000);
        ///    }

        //    public void onFinish() {
        //        mTxtVwStopWatch.setText("Completado!");
        //    }
       //}.start();
    }

    private static void ApagaCamara() {
        Camera.Parameters parameters = camera.getParameters();
        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
        camera.setParameters(parameters);
        wakeLock.release();
        camera.setPreviewCallback(null);
        camera.stopPreview();
        camera.release();
        camera = null;
        //camera.stopPreview();
    }

    private static void PrendeCamara() {
        Camera.Parameters parameters = camera.getParameters();
        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        camera.setParameters(parameters);
        camera.startPreview();
    }

    private void inicializarFechaHora(){

        //obtener fechay hroa de Calendar
        Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month =  c.get(Calendar.MONTH)+1;
        day = c.get(Calendar.DAY_OF_MONTH);
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);
        String mes = month < 10 ? "0"+month : ""+month;
        String dia =  day < 10 ? "0"+day : ""+day;
        //setear fecha

        //setear hora
        String hourString = hour < 10 ? "0"+hour : ""+hour;
        String minuteString = minute < 10 ? "0"+minute : ""+minute;
        String time = ""+hourString+":"+minuteString;

        fecha = ""+dia+"/"+mes+"/"+year;
        hora = time;
    }

    //GUARDOS DATOS EN LA TABLA BD
    public  void saveDataPulseDB(View view){
        String maxpressureS =  txt_maxpressure.getText().toString() ;
        String minpressureS =  txt_minpressure.getText().toString() ;
        String concentracion =  text.getText().toString() ;
        String medido = selectTextSpinner;
        String observacion = txt_observation.getText().toString() ;
        int concent = Integer.parseInt(concentracion);
        //validar campos llenos
        if(concentracion.length() > 0 && selectPosicionSpinner != 0  ) {
            try {
                if(!txt_maxpressure.getText().toString().isEmpty() &&Utils.isNumeric(txt_maxpressure.getText().toString())&&
                        !txt_minpressure.getText().toString().isEmpty() &&Utils.isNumeric(txt_minpressure.getText().toString())   ) {
                    float maxpressure = Float.parseFloat(txt_maxpressure.getText().toString());//Float.parseFloat(txt_concentration.getText().toString())
                    float minpressure = Float.parseFloat(txt_minpressure.getText().toString());
                    if (validaRangos(maxpressure, minpressure)) {
                        //setear datos al objeto y guardar y BD
                        Utils.DbsavePulseFromDatabase(-1,
                                                    concent,
                                                    maxpressureS,
                                                    minpressureS,
                                                    medido,
                                                    fecha,
                                                    hora,
                                                    observacion,
                                                    enviadoServer,
                                                    operacionI,
                                                    HealthMonitorApplicattion.getApplication().getPulseDao());


                        nextAction();
                    }
                }else{
                    Snackbar.make(view, "\n" + "Su P.A esta mal ingresada: "+"\n"+getString(R.string.ingrese), Snackbar.LENGTH_LONG)
                            .setAction("Ok", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                            /*Intent it = new Intent(Settings.ACTION_WIFI_SETTINGS);
                            startActivity(it);*/
                                }
                            })
                            .setActionTextColor(this.getResources().getColor(R.color.greenMenu))
                            .show();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            //finalizar matar la actividad
            finish();
        }else{

            Snackbar.make(view, "\n" +"Por favor seleccionar una opción" , Snackbar.LENGTH_LONG)
                    .setAction("Ok", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            /*Intent it = new Intent(Settings.ACTION_WIFI_SETTINGS);
                            startActivity(it);*/
                        }
                    })
                    .setActionTextColor(this.getResources().getColor(R.color.greenMenu))
                    .show();
        }
    }

    private static void showReadingCompleteDialog(){
        ApagaCamara();
        AlertDialog.Builder builder = new AlertDialog.Builder(parentReference);
        builder.setTitle("Ritmo Cardiaco Prueba");
        builder.setMessage("Su Ritmo Cardiaco Es:  "+beatsPerMinuteValue+" latidos por minuto.")
                .setCancelable(false)
                .setPositiveButton("Salir y Registrar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ( (Activity) parentReference).finish();
                    }
                })
                .setNegativeButton("Volver a Medir ", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        text.setText("---");
                        PrendeCamara();
                        prepareCountDownTimer();
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void nextAction() {
        circleAnimationOpen();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //finalizar matar la actividad
                finish();
            }
        }, 150);
    }

    private void circleAnimationOpen() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            int x = layoutContent.getRight();
            int y = layoutContent.getBottom();
            int startRadius = 0;
            int endRadius = (int) Math.hypot(layoutMain.getWidth(), layoutMain.getHeight());
            Animator anim = ViewAnimationUtils.createCircularReveal(layoutButtons, x, y, startRadius, endRadius);
            layoutButtons.setVisibility(View.VISIBLE);
            cardReg.setVisibility(View.INVISIBLE);
            anim.start();
        }
    }

    private boolean validaRangos(float sistolica, float Diastolica ) {
        if (sistolica>= 50 & sistolica <=90 )
        {
            if (Diastolica>= 35 & Diastolica <=60)
            {
                return true;
            }
        }
        if (sistolica>= 91 & sistolica <=100 )
        {
            if (Diastolica>= 61 & Diastolica <=70)
            {
                return true;
            }
        }
        if (sistolica>= 101 & sistolica <=130 )
        {
            if (Diastolica>= 71 & Diastolica <=85)
            {
                return true;
            }
        }
        if (sistolica>= 131 & sistolica <=140 )
        {
            if (Diastolica>= 86 & Diastolica <=90)
            {
                return true;
            }
        }
        if (sistolica>= 141 & sistolica <=160 )
        {
            if (Diastolica>= 91 & Diastolica <=110)
            {
                return true;
            }
        }
        if (sistolica>= 161 & sistolica <=230 )
        {
            if (Diastolica>= 111 & Diastolica <=135)
            {
                return true;
            }
        }
        return false;
    }

}

