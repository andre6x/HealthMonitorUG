package com.grupocisc.healthmonitor.Services;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.grupocisc.healthmonitor.HealthMonitorApplicattion;
import com.grupocisc.healthmonitor.Home.activities.MainActivity;
import com.grupocisc.healthmonitor.R;
import com.grupocisc.healthmonitor.Utils.ServiceChecker;
import com.grupocisc.healthmonitor.Utils.Utils;
import com.grupocisc.healthmonitor.entities.EAlarmDetails;
import com.grupocisc.healthmonitor.entities.EAlarmTakeMedicine;
import com.grupocisc.healthmonitor.entities.IRegisteredMedicines;
import com.grupocisc.healthmonitor.entities.IV2ConsulControlMedication;
import com.grupocisc.healthmonitor.entities.IV2ConsulMedicationAlarm;
import com.grupocisc.healthmonitor.entities.IConsulMyDoctors;
import com.grupocisc.healthmonitor.entities.IV2ConsultCholesterol;
import com.grupocisc.healthmonitor.entities.IV2ConsultGlucosa;
import com.grupocisc.healthmonitor.entities.IV2ConsultHbaCetona;
import com.grupocisc.healthmonitor.entities.IV2ConsultInsulin;
import com.grupocisc.healthmonitor.entities.IV2ConsultMood;
import com.grupocisc.healthmonitor.entities.IV2ConsultPickFlow;
import com.grupocisc.healthmonitor.entities.IV2ConsultPulsePressure;
import com.grupocisc.healthmonitor.entities.IV2ConsultWeight;
import com.grupocisc.healthmonitor.entities.ObjUser;
import com.grupocisc.healthmonitor.entities.ObjUserdate;

import java.sql.SQLException;

import okhttp3.internal.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Un {@link IntentService} que simula un proceso en primer plano
 * <p>
 */
public class ProgressIntentService extends IntentService {
    private static final String TAG = ProgressIntentService.class.getSimpleName();
    private Call<IV2ConsultGlucosa.Obj> call_3;
    private IV2ConsultGlucosa.Obj mDataGlucosa;
    private Call<IV2ConsultPickFlow.Obj> call_4;
    private IV2ConsultPickFlow.Obj mDataPick;
    private Call<IV2ConsultWeight.Obj> call_5;
    private IV2ConsultWeight.Obj mDataWeight;
    private Call<IV2ConsultInsulin.Obj> call_6;
    private IV2ConsultInsulin.Obj mDataInsulin;
    private Call<IV2ConsultCholesterol.Obj> call_7;
    private IV2ConsultCholesterol.Obj mDataCholesterol;
    private Call<IV2ConsultHbaCetona.Obj> call_8;
    private IV2ConsultHbaCetona.Obj mDataHbaCetona;
    private Call<IV2ConsultMood.Obj> call_9;
    private IV2ConsultMood.Obj mDataMood;
    private Call<IV2ConsultPulsePressure.Obj> call_10;
    private IV2ConsultPulsePressure.Obj mDataPulsePressure;
    private Call<IConsulMyDoctors.Obj> call_11;
    private IConsulMyDoctors.Obj mDataMyDoctors;
	
	private Call<IV2ConsulMedicationAlarm.Obj> call_12;
    private IV2ConsulMedicationAlarm.Obj mDataMedAlm;
    private Call<IV2ConsulControlMedication.Obj> call_13;
    private IV2ConsulControlMedication.Obj mDataCtrlMed;
    public Boolean run;
    public String Email = "";
    private static final String sendServerOK = "true";
    private static final String operacionI = "I";

    public ProgressIntentService() {
        super("ProgressIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.e(TAG, "Servicio onHandleIntent...");
        if (intent != null) {
            //final String action = intent.getAction();
            //if (Constants.ACTION_RUN_ISERVICE.equals(action)) {
                   handleActionRun();

            //}
        }
    }

    /**
     * Maneja la acción de ejecución del servicio
     */
    private void handleActionRun() {
        try {
            //OBTENER DATA DE PREFERENCIA
            if (Utils.getEmailFromPreference(this) != null)
                Email = Utils.getEmailFromPreference(this);
            //ELIMINAR DATOS BD
            eliminarDatosDB();

            Drawable drawable=getApplicationInfo().loadIcon(getPackageManager());
            Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
            // Se construye la notificación
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                    //.setSmallIcon(android.R.drawable.stat_sys_download_done)
                    .setSmallIcon(R.mipmap.ic_notification)
                    .setLargeIcon(bitmap)
                    .setContentTitle("Restaurando datos")
                    .setContentText("Procesando...");
            if (Build.VERSION.SDK_INT >= 23) {
                builder.setColor(ContextCompat.getColor(this, R.color.colorPrimary));
            } else {
                builder.setColor(this.getResources().getColor(R.color.colorPrimary));
            }


            importDataGlucosa();
            importDataPulsoPresion();
            importDataPeso();
            importDataEstadoAnimo();
            importDataInsulina();
            importDataColesterol();
            importDataHbaCetona();
            importDataPickFlow();
            importDataDoctors();
            importDataMedicineAlarm();
            //importDataMedicineTakeAlarm();

            // Bucle de simulación
            for (int i = 1; i <= 30; i++) {
                Log.e(TAG, i + ""); // Logueo
                // Poner en primer plano
                builder.setProgress(30, i, false);
                startForeground(1, builder.build());
                // Retardo de 1 segundo en la iteración
                Thread.sleep(1000);
            }


            // Quitar de primer plano
            stopForeground(true);

            //v3
            MainActivity.InitAssistantService(this,TAG);
            //MainActivity.InitBarometerReaderService(this,TAG);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }



    ///**********************************INNICIO GLUCOSA**************************//
    public void importDataGlucosa(){
        IV2ConsultGlucosa Iglucosa = HealthMonitorApplicattion.getApplication().getRetrofitAdapter().create(IV2ConsultGlucosa.class);
        call_3 = Iglucosa.getConsultGlucosa(new ObjUserdate(Email, null, null ));
        call_3.enqueue(new Callback<IV2ConsultGlucosa.Obj>() {
            @Override
            public void onResponse(Call<IV2ConsultGlucosa.Obj> call, Response<IV2ConsultGlucosa.Obj> response) {
                if (response.isSuccessful() ) {
                    Log.e(TAG, "GLUCOSA CONSULTAR Respuesta exitosa: response true  ");
                    mDataGlucosa = null;
                    mDataGlucosa = response.body();
                    postExecutionSaveDataGlucosa();

                } else {
                    String msj = "GLUCOSA CONSULTAR Error en la petición: response false";
                    bitacoraErrorGlucosa(msj);
                    Log.e(TAG, msj);
                }
            }

            @Override
            public void onFailure(Call<IV2ConsultGlucosa.Obj> call, Throwable t) {
                String msj = "GLUCOSA CONSULTAR Error en la petición: onFailure";
                bitacoraErrorGlucosa(msj);
                t.printStackTrace();
            }
        });
    }

    public void postExecutionSaveDataGlucosa(){
        Log.e(TAG, "GLUCOSA postExecution Data");
        String msj1 = "Error: Object is null";
        String msj2 = "Error: codigo != 0";
        String msj3 = "Error: lista  Rows  is null or size is 0";
        if (mDataGlucosa != null) {
            if (mDataGlucosa.getIdCodResult() == 0 ) {
                if(mDataGlucosa.getRows() != null && mDataGlucosa.getRows().size() > 0){
                    Log.e(TAG, "GLUCOSA obtener data server oK ");

                    //enviar datos a guardar
                    for (IV2ConsultGlucosa.rows  rows :  mDataGlucosa.getRows() ){
                        saveDataGlucosaDB(rows);
                    }

                }else{
                    bitacoraErrorGlucosa(msj3 );
                }
            }else
                bitacoraErrorGlucosa(msj2 );
        }else
            bitacoraErrorGlucosa(msj1);
    }

    //GUARDOS DATOS EN LA TABLA BDSQLITE
    public  void saveDataGlucosaDB(IV2ConsultGlucosa.rows  rows){
        try {
            String fecha= rows.getDate().substring(8,10)+ "/"+ rows.getDate().substring(5,7)+"/" +rows.getDate().substring(0,4);
            String hora =  rows.getDate().substring(11,16);
            //setear datos al objeto y guardar y BD
            Utils.DbsaveGlucoseFromDatabase(rows.getIdRegisterDB() ,
                    rows.getMeasureUnit(),
                    fecha,//cortar fecha
                    hora,//cortar hora
                    rows.getObservations(),
                    sendServerOK,
                    operacionI,
                    HealthMonitorApplicattion.getApplication().getGlucoseDao());

            //Utils.generateToast(this, getResources().getString(R.string.txt_guardado));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //BITACORA
    public void bitacoraErrorGlucosa(String msj){
        String tipo = "GLUCOSA";
        Log.e(TAG, tipo +" "+ msj );
        //getApplicationContext()
        //LLAMAR METODO WS NUEVO DE BITACORA ERRORES
    }
    ///**********************************FIN GLUCOSA*****************************//

    ///**********************************INNICIO PULSOPRESION**************************//
    public void importDataPulsoPresion(){
        IV2ConsultPulsePressure pulse = HealthMonitorApplicattion.getApplication().getRetrofitAdapter().create(IV2ConsultPulsePressure.class);
        call_10 = pulse.getConsultPulsePressure(new ObjUserdate(Email, null, null ));
        call_10.enqueue(new Callback<IV2ConsultPulsePressure.Obj>() {
            @Override
            public void onResponse(Call<IV2ConsultPulsePressure.Obj> call, Response<IV2ConsultPulsePressure.Obj> response) {
                if (response.isSuccessful() ) {
                    Log.e(TAG, "PULSOPRESION CONSULTAR Respuesta exitosa: response true  ");
                    mDataPulsePressure = null;
                    mDataPulsePressure = response.body();
                    postExecutionSaveDataPulsoPresion();

                } else {
                    String msj = "PULSOPRESION CONSULTAR Error en la petición: response false";
                    bitacoraErrorPulsoPresion(msj);
                    Log.e(TAG, msj);
                }
            }

            @Override
            public void onFailure(Call<IV2ConsultPulsePressure.Obj> call, Throwable t) {
                String msj = "PULSOPRESION CONSULTAR Error en la petición: onFailure";
                bitacoraErrorPulsoPresion(msj);
                t.printStackTrace();
            }
        });
    }

    public void postExecutionSaveDataPulsoPresion(){
        Log.e(TAG, "PULSOPRESION postExecution Data");
        String msj1 = "Error: Object is null";
        String msj2 = "Error: codigo != 0";
        String msj3 = "Error: lista  Rows  is null or size is 0";
        if (mDataPulsePressure != null) {
            if (mDataPulsePressure.getIdCodResult() == 0 ) {
                if(mDataPulsePressure.getRows() != null && mDataPulsePressure.getRows().size() > 0){
                    Log.e(TAG, "PULSOPRESION obtener data server oK ");

                    //enviar datos a guardar
                    for (IV2ConsultPulsePressure.rows  rows :  mDataPulsePressure.getRows() ){
                        saveDataPulsoPresionDB(rows);
                    }

                }else{
                    bitacoraErrorPulsoPresion(msj3 );
                }
            }else
                bitacoraErrorPulsoPresion(msj2 );
        }else
            bitacoraErrorPulsoPresion(msj1);
    }

    //GUARDOS DATOS EN LA TABLA BDSQLITE
    public  void saveDataPulsoPresionDB(IV2ConsultPulsePressure.rows  rows){
        try {
            String fecha= rows.getDate().substring(8,10)+ "/"+ rows.getDate().substring(5,7)+"/" +rows.getDate().substring(0,4);
            String hora =  rows.getDate().substring(11,16);
            //setear datos al objeto y guardar y BD
            Utils.DbsavePulseFromDatabase(rows.getIdRegisterDB() ,
                                            rows.getPulse(),
                                            Float.toString(rows.getPressureSistolica() ) ,
                                            Float.toString(rows.getPressureDiastolica() ),
                                            rows.getActivity(),
                                            fecha,
                                            hora,
                                            rows.getObservations(),
                                            sendServerOK,
                                            operacionI,
                                            HealthMonitorApplicattion.getApplication().getPulseDao());

            //Utils.generateToast(this, getResources().getString(R.string.txt_guardado));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //BITACORA
    public void bitacoraErrorPulsoPresion(String msj){
        String tipo = "PULSOPRESION";
        Log.e(TAG, tipo +" "+ msj );
        //getApplicationContext()
        //LLAMAR METODO WS NUEVO DE BITACORA ERRORES
    }
    ///**********************************FIN PULSOPRESION*****************************//

    ///**********************************INNICIO PESO**************************//
    public void importDataPeso(){
        IV2ConsultWeight Iglucosa = HealthMonitorApplicattion.getApplication().getRetrofitAdapter().create(IV2ConsultWeight.class);
        call_5 = Iglucosa.getConsultWeight(new ObjUserdate(Email, null, null ));
        call_5.enqueue(new Callback<IV2ConsultWeight.Obj>() {
            @Override
            public void onResponse(Call<IV2ConsultWeight.Obj> call, Response<IV2ConsultWeight.Obj> response) {
                if (response.isSuccessful() ) {
                    Log.e(TAG, "PESO CONSULTAR Respuesta exitosa: response true  ");
                    mDataWeight = null;
                    mDataWeight = response.body();
                    postExecutionSaveDataPeso();

                } else {
                    String msj = "PESO CONSULTAR Error en la petición: response false";
                    bitacoraErrorPeso(msj);
                    Log.e(TAG, msj);
                }
            }

            @Override
            public void onFailure(Call<IV2ConsultWeight.Obj> call, Throwable t) {
                String msj = "PESO CONSULTAR Error en la petición: onFailure";
                bitacoraErrorPeso(msj);
                t.printStackTrace();
            }
        });
    }

    public void postExecutionSaveDataPeso(){
        Log.e(TAG, "PESO postExecution Data");
        String msj1 = "Error: Object is null";
        String msj2 = "Error: codigo != 0";
        String msj3 = "Error: lista  Rows  is null or size is 0";
        if (mDataWeight != null) {
            if (mDataWeight.getIdCodResult() == 0 ) {
                if(mDataWeight.getRows() != null && mDataWeight.getRows().size() > 0){
                    Log.e(TAG, "PESO obtener data server oK ");

                    //enviar datos a guardar
                    for (IV2ConsultWeight.rows  rows :  mDataWeight.getRows() ){
                        saveDataPesoDB(rows);
                    }

                }else{
                    bitacoraErrorPeso(msj3 );
                }
            }else
                bitacoraErrorPeso(msj2 );
        }else
            bitacoraErrorPeso(msj1);
    }

    //GUARDOS DATOS EN LA TABLA BDSQLITE
    public  void saveDataPesoDB(IV2ConsultWeight.rows  rows){
        try {
            String fecha= rows.getDate().substring(8,10)+ "/"+ rows.getDate().substring(5,7)+"/" +rows.getDate().substring(0,4);
            String hora =  rows.getDate().substring(11,16);
            //setear datos al objeto y guardar y BD
            Utils.DbsaveWeightFromDatabase(rows.getIdRegisterDB() ,
                    rows.getWeight(),
                    rows.getMuscleMass(),
                    rows.getTmb(),
                    rows.getDmo(),
                    rows.getWaterPercentage(),
                    rows.getGreasePercentage(),
                    fecha,
                    hora,
                    rows.getObservations(),
                    sendServerOK,
                    operacionI,
                    HealthMonitorApplicattion.getApplication().getWeightDao());

            //Utils.generateToast(this, getResources().getString(R.string.txt_guardado));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //BITACORA
    public void bitacoraErrorPeso(String msj){
        String tipo = "PESO";
        Log.e(TAG, tipo +" "+ msj );
        //getApplicationContext()
        //LLAMAR METODO WS NUEVO DE BITACORA ERRORES
    }
    ///**********************************FIN PESO*****************************//

    ///**********************************INNICIO INSULINA**************************//
    public void importDataInsulina(){
        IV2ConsultInsulin insulin = HealthMonitorApplicattion.getApplication().getRetrofitAdapter().create(IV2ConsultInsulin.class);
        call_6 = insulin.getConsultInsulin(new ObjUserdate(Email, null, null ));
        call_6.enqueue(new Callback<IV2ConsultInsulin.Obj>() {
            @Override
            public void onResponse(Call<IV2ConsultInsulin.Obj> call, Response<IV2ConsultInsulin.Obj> response) {
                if (response.isSuccessful() ) {
                    Log.e(TAG, "PESO CONSULTAR Respuesta exitosa: response true  ");
                    mDataInsulin = null;
                    mDataInsulin = response.body();
                    postExecutionSaveDataInsulina();

                } else {
                    String msj = "PESO CONSULTAR Error en la petición: response false";
                    bitacoraErrorInsulina(msj);
                    Log.e(TAG, msj);
                }
            }

            @Override
            public void onFailure(Call<IV2ConsultInsulin.Obj> call, Throwable t) {
                String msj = "INSULINA CONSULTAR Error en la petición: onFailure";
                bitacoraErrorInsulina(msj);
                t.printStackTrace();
            }
        });
    }

    public void postExecutionSaveDataInsulina(){
        Log.e(TAG, "INSULINA postExecution Data");
        String msj1 = "Error: Object is null";
        String msj2 = "Error: codigo != 0";
        String msj3 = "Error: lista  Rows  is null or size is 0";
        if (mDataInsulin != null) {
            if (mDataInsulin.getIdCodResult() == 0 ) {
                if(mDataInsulin.getRows() != null && mDataInsulin.getRows().size() > 0){
                    Log.e(TAG, "INSULINA obtener data server oK ");

                    //enviar datos a guardar
                    for (IV2ConsultInsulin.rows  rows :  mDataInsulin.getRows() ){
                        saveDataInsulinaDB(rows);
                    }

                }else{
                    bitacoraErrorInsulina(msj3 );
                }
            }else
                bitacoraErrorInsulina(msj2 );
        }else
            bitacoraErrorInsulina(msj1);
    }

    //GUARDOS DATOS EN LA TABLA BDSQLITE
    public  void saveDataInsulinaDB(IV2ConsultInsulin.rows  rows){
        try {
            //Log.e(TAG,"******************************INSULINA==== rows"+ rows.getDate() );
            String fecha= rows.getDate().substring(8,10)+ "/"+ rows.getDate().substring(5,7)+"/" +rows.getDate().substring(0,4);
            String hora =  rows.getDate().substring(11,19);
            //String fechaHora =  fecha +" "+hora;
            Log.e(TAG,"******************************INSULINA==== fecha="+ fecha + " hora= " + hora );
            //setear datos al objeto y guardar y BD
            Utils.saveInsulinToDataBaseLocal ( rows.getIdRegisterDB(),
                    rows.getMeasureUnit(),
                    fecha,
                    hora ,
                    rows.getObservations(),
                    sendServerOK,
                    operacionI,
                    HealthMonitorApplicattion.getApplication().getInsulinDao()  );

            //Utils.generateToast(this, getResources().getString(R.string.txt_guardado));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //BITACORA
    public void bitacoraErrorInsulina(String msj){
        String tipo = "INSULINA";
        Log.e(TAG, tipo +" "+ msj );
        //getApplicationContext()
        //LLAMAR METODO WS NUEVO DE BITACORA ERRORES
    }
    ///**********************************FIN INSULINA*****************************//

    ///**********************************INNICIO COLESTEROL**************************//
    public void importDataColesterol(){
        IV2ConsultCholesterol colest = HealthMonitorApplicattion.getApplication().getRetrofitAdapter().create(IV2ConsultCholesterol.class);
        call_7 = colest.getConsultCholesterol(new ObjUserdate(Email, null, null ));
        call_7.enqueue(new Callback<IV2ConsultCholesterol.Obj>() {
            @Override
            public void onResponse(Call<IV2ConsultCholesterol.Obj> call, Response<IV2ConsultCholesterol.Obj> response) {
                if (response.isSuccessful() ) {
                    Log.e(TAG, "COLESTEROL CONSULTAR Respuesta exitosa: response true  ");
                    mDataCholesterol = null;
                    mDataCholesterol = response.body();
                    postExecutionSaveDataColesterol();

                } else {
                    String msj = "COLESTEROL CONSULTAR Error en la petición: response false";
                    bitacoraErrorColesterol(msj);
                    Log.e(TAG, msj);
                }
            }

            @Override
            public void onFailure(Call<IV2ConsultCholesterol.Obj> call, Throwable t) {
                String msj = "COLESTEROL CONSULTAR Error en la petición: onFailure";
                bitacoraErrorColesterol(msj);
                t.printStackTrace();
            }
        });
    }

    public void postExecutionSaveDataColesterol(){
        Log.e(TAG, "COLESTEROL postExecution Data");
        String msj1 = "Error: Object is null";
        String msj2 = "Error: codigo != 0";
        String msj3 = "Error: lista  Rows  is null or size is 0";
        if (mDataCholesterol != null) {
            if (mDataCholesterol.getIdCodResult() == 0 ) {
                if(mDataCholesterol.getRows() != null && mDataCholesterol.getRows().size() > 0){
                    Log.e(TAG, "COLESTEROL obtener data server oK ");

                    //enviar datos a guardar
                    for (IV2ConsultCholesterol.rows  rows :  mDataCholesterol.getRows() ){
                        saveDataColesterolDB(rows);
                    }

                }else{
                    bitacoraErrorColesterol(msj3 );
                }
            }else
                bitacoraErrorColesterol(msj2 );
        }else
            bitacoraErrorColesterol(msj1);
    }

    //GUARDOS DATOS EN LA TABLA BDSQLITE
    public  void saveDataColesterolDB(IV2ConsultCholesterol.rows  rows){
        try {
            String fecha= rows.getDate().substring(8,10)+ "/"+ rows.getDate().substring(5,7)+"/" +rows.getDate().substring(0,4);
            String hora =  rows.getDate().substring(11,16);
            //setear datos al objeto y guardar y BD
            Utils.DbsaveColesterolFromDatabase( rows.getIdRegisterDB(),
                                                rows.getCholesterol(),
                                                rows.getTriglycerides(),
                                                rows.getHdl(),
                                                rows.getLdl(),
                                                fecha,
                                                hora,
                                                rows.getObservations(),
                                                sendServerOK,
                                                operacionI,
                                                HealthMonitorApplicattion.getApplication().getColesterolDao());

            //Utils.generateToast(this, getResources().getString(R.string.txt_guardado));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //BITACORA
    public void bitacoraErrorColesterol(String msj){
        String tipo = "COLESTEROL";
        Log.e(TAG, tipo +" "+ msj );
        //getApplicationContext()
        //LLAMAR METODO WS NUEVO DE BITACORA ERRORES
    }
    ///**********************************FIN COLESTEROL*****************************//

    ///**********************************INNICIO HBACETONA**************************//
    public void importDataHbaCetona(){
        IV2ConsultHbaCetona hba = HealthMonitorApplicattion.getApplication().getRetrofitAdapter().create(IV2ConsultHbaCetona.class);
        call_8 = hba.getConsultGlucosa(new ObjUserdate(Email, null, null ));
        call_8.enqueue(new Callback<IV2ConsultHbaCetona.Obj>() {
            @Override
            public void onResponse(Call<IV2ConsultHbaCetona.Obj> call, Response<IV2ConsultHbaCetona.Obj> response) {
                if (response.isSuccessful() ) {
                    Log.e(TAG, "HBACETONA CONSULTAR Respuesta exitosa: response true  ");
                    mDataHbaCetona = null;
                    mDataHbaCetona = response.body();
                    postExecutionSaveDataHbaCetona();

                } else {
                    String msj = "HBACETONA CONSULTAR Error en la petición: response false";
                    bitacoraErrorHbaCetona(msj);
                    Log.e(TAG, msj);
                }
            }

            @Override
            public void onFailure(Call<IV2ConsultHbaCetona.Obj> call, Throwable t) {
                String msj = "HBACETONA CONSULTAR Error en la petición: onFailure";
                bitacoraErrorHbaCetona(msj);
                t.printStackTrace();
            }
        });
    }

    public void postExecutionSaveDataHbaCetona(){
        Log.e(TAG, "HBACETONA postExecution Data");
        String msj1 = "Error: Object is null";
        String msj2 = "Error: codigo != 0";
        String msj3 = "Error: lista  Rows  is null or size is 0";
        if (mDataHbaCetona != null) {
            if (mDataHbaCetona.getIdCodResult() == 0 ) {
                if(mDataHbaCetona.getRows() != null && mDataHbaCetona.getRows().size() > 0){
                    Log.e(TAG, "HBACETONA obtener data server oK ");

                    //enviar datos a guardar
                    for (IV2ConsultHbaCetona.rows  rows :  mDataHbaCetona.getRows() ){
                        saveDataHbaCetonaDB(rows);
                    }

                }else{
                    bitacoraErrorHbaCetona(msj3 );
                }
            }else
                bitacoraErrorHbaCetona(msj2 );
        }else
            bitacoraErrorHbaCetona(msj1);
    }

    //GUARDOS DATOS EN LA TABLA BDSQLITE
    public  void saveDataHbaCetonaDB(IV2ConsultHbaCetona.rows  rows){
        try {
            String fecha= rows.getDate().substring(8,10)+ "/"+ rows.getDate().substring(5,7)+"/" +rows.getDate().substring(0,4);
            String hora =  rows.getDate().substring(11,16);
            //setear datos al objeto y guardar y BD
            Utils.DbsaveHba1cFromDatabase( rows.getIdRegisterDB(),
                                        rows.getHba1c(),
                                        rows.getCetonas(),
                                        fecha,
                                        hora,
                                        rows.getObservations(),
                                        sendServerOK,
                                        operacionI,
                                        HealthMonitorApplicattion.getApplication().getHba1cDao());

            //Utils.generateToast(this, getResources().getString(R.string.txt_guardado));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //BITACORA
    public void bitacoraErrorHbaCetona(String msj){
        String tipo = "HBACETONA";
        Log.e(TAG, tipo +" "+ msj );
        //getApplicationContext()
        //LLAMAR METODO WS NUEVO DE BITACORA ERRORES
    }
    ///**********************************FIN HBACETONA*****************************//

    ///**********************************INNICIO ESTADOANIMO**************************//
    public void importDataEstadoAnimo(){
        IV2ConsultMood estadoAnim = HealthMonitorApplicattion.getApplication().getRetrofitAdapter().create(IV2ConsultMood.class);
        call_9 = estadoAnim.getConsultMood(new ObjUserdate(Email, null, null ));
        call_9.enqueue(new Callback<IV2ConsultMood.Obj>() {
            @Override
            public void onResponse(Call<IV2ConsultMood.Obj> call, Response<IV2ConsultMood.Obj> response) {
                if (response.isSuccessful() ) {
                    Log.e(TAG, "ESTADOANIMO CONSULTAR Respuesta exitosa: response true  ");
                    mDataMood = null;
                    mDataMood = response.body();
                    postExecutionSaveDataEstadoAnimo();

                } else {
                    String msj = "ESTADOANIMO CONSULTAR Error en la petición: response false";
                    bitacoraErrorEstadoAnimo(msj);
                    Log.e(TAG, msj);
                }
            }

            @Override
            public void onFailure(Call<IV2ConsultMood.Obj> call, Throwable t) {
                String msj = "ESTADOANIMO CONSULTAR Error en la petición: onFailure";
                bitacoraErrorEstadoAnimo(msj);
                t.printStackTrace();
            }
        });
    }

    public void postExecutionSaveDataEstadoAnimo(){
        Log.e(TAG, "ESTADOANIMO postExecution Data");
        String msj1 = "Error: Object is null";
        String msj2 = "Error: codigo != 0";
        String msj3 = "Error: lista  Rows  is null or size is 0";
        if (mDataMood != null) {
            if (mDataMood.getIdCodResult() == 0 ) {
                if(mDataMood.getRows() != null && mDataMood.getRows().size() > 0){
                    Log.e(TAG, "ESTADOANIMO obtener data server oK ");

                    //enviar datos a guardar
                    for (IV2ConsultMood.rows  rows :  mDataMood.getRows() ){
                        saveDataEstadoAnimoDB(rows);
                    }

                }else{
                    bitacoraErrorEstadoAnimo(msj3 );
                }
            }else
                bitacoraErrorEstadoAnimo(msj2 );
        }else
            bitacoraErrorEstadoAnimo(msj1);
    }

    //GUARDOS DATOS EN LA TABLA BDSQLITE
    public  void saveDataEstadoAnimoDB(IV2ConsultMood.rows  rows){
        try {
            String StatusName = "";
            String fecha= rows.getDate().substring(8,10)+ "/"+ rows.getDate().substring(5,7)+"/" +rows.getDate().substring(0,4);
            String hora =  rows.getDate().substring(11,16);
            //1 al 5 sacar nombre
            if(rows.getMood() == 1){
                StatusName = getResources().getString(R.string.txt_sta_increible);
            }else if(rows.getMood() == 2){
                StatusName = getResources().getString(R.string.txt_sta_bien);
            }else if(rows.getMood() == 3){
                StatusName = getResources().getString(R.string.txt_sta_normal);
            }else if(rows.getMood() == 4){
                StatusName = getResources().getString(R.string.txt_sta_mal);
            }else if(rows.getMood() == 5){
                StatusName = getResources().getString(R.string.txt_sta_horrible);
            }
            //setear datos al objeto y guardar y BD
            Utils.DbsaveStateFromDatabase( rows.getMoodId(),
                                            rows.getMood() ,
                                            StatusName,
                                            fecha,
                                            hora,
                                            rows.getObservation(),
                                            sendServerOK,
                                            operacionI,
                                            HealthMonitorApplicattion.getApplication().getStateDao());

            //Utils.generateToast(this, getResources().getString(R.string.txt_guardado));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //BITACORA
    public void bitacoraErrorEstadoAnimo(String msj){
        String tipo = "ESTADOANIMO";
        Log.e(TAG, tipo +" "+ msj );
        //getApplicationContext()
        //LLAMAR METODO WS NUEVO DE BITACORA ERRORES
    }
    ///**********************************FIN ESTADOANIMO*****************************//


    ///***********************************INNICIO PICKFLOW***********************//
    public void importDataPickFlow(){
        IV2ConsultPickFlow Iglucosa = HealthMonitorApplicattion.getApplication().getRetrofitAdapter().create(IV2ConsultPickFlow.class);
        call_4 = Iglucosa.getConsultPickFlow(new ObjUserdate(Email, null, null ));
        call_4.enqueue(new Callback<IV2ConsultPickFlow.Obj>() {
            @Override
            public void onResponse(Call<IV2ConsultPickFlow.Obj> call, Response<IV2ConsultPickFlow.Obj> response) {
                if (response.isSuccessful() ) {
                    Log.e(TAG, "PICKFLOW CONSULTAR Respuesta exitosa: response true  ");
                    mDataPick = null;
                    mDataPick = response.body();
                    postExecutionSaveDataPickFlow();

                } else {
                    String msj = "PICKFLOW CONSULTAR Error en la petición: response false";
                    bitacoraErrorPickFlow(msj);
                    Log.e(TAG, msj);
                }
            }

            @Override
            public void onFailure(Call<IV2ConsultPickFlow.Obj> call, Throwable t) {
                String msj = "GLUCOSA CONSULTAR Error en la petición: onFailure";
                bitacoraErrorGlucosa(msj);
                t.printStackTrace();
            }
        });
    }

    public void postExecutionSaveDataPickFlow(){
        Log.e(TAG, "PICKFLOW postExecution Data");
        String msj1 = "Error: Object is null";
        String msj2 = "Error: codigo != 0";
        String msj3 = "Error: lista  Rows  is null or size is 0";
        if (mDataPick != null) {
            if (mDataPick.getIdCodResult() == 0 ) {
                if(mDataPick.getRows() != null && mDataPick.getRows().size() > 0){
                    Log.e(TAG, "PICKFLOW obtener data server oK ");

                    //enviar datos a guardar
                    for (IV2ConsultPickFlow.rows  rows :  mDataPick.getRows() ){
                        saveDataPickFlowDB(rows);
                    }

                }else{
                    bitacoraErrorGlucosa(msj3 );
                }
            }else
                bitacoraErrorGlucosa(msj2 );
        }else
            bitacoraErrorGlucosa(msj1);
    }

    //GUARDOS DATOS EN LA TABLA BDSQLITE
    public  void saveDataPickFlowDB(IV2ConsultPickFlow.rows rows){
        try {
            String fecha= rows.getDate().substring(8,10)+ "/"+ rows.getDate().substring(5,7)+"/" +rows.getDate().substring(0,4);
            String hora =  rows.getDate().substring(11,16);
            //setear datos al objeto y guardar y BD
            Utils.DbsaveAsthmaFromDatabase( rows.getIdRegisterDB(),
                                            fecha,
                                            hora,
                                            rows.getMeasureUnit(),
                                            rows.getObservations(),
                                            sendServerOK,
                                            operacionI,
                                            HealthMonitorApplicattion.getApplication().getAsthmaDao());

            //Utils.generateToast(this, getResources().getString(R.string.txt_guardado));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //BITACORA
    public void bitacoraErrorPickFlow(String msj){
        String tipo = "PICKFLOW";
        Log.e(TAG, tipo +" "+ msj );
        //getApplicationContext()
        //LLAMAR METODO WS NUEVO DE BITACORA ERRORES
    }
    ///**********************************FIN PICKFLOW******************************//

    ///***********************************INNICIO DOCTORES***********************//
    public void importDataDoctors(){
        IConsulMyDoctors Iglucosa = HealthMonitorApplicattion.getApplication().getRetrofitAdapter().create(IConsulMyDoctors.class);
        call_11 = Iglucosa.getConsultMyDoctors(new ObjUser(Email));
        call_11.enqueue(new Callback<IConsulMyDoctors.Obj>() {
            @Override
            public void onResponse(Call<IConsulMyDoctors.Obj> call, Response<IConsulMyDoctors.Obj> response) {
                if (response.isSuccessful() ) {
                    Log.e(TAG, "PICKFLOW CONSULTAR Respuesta exitosa: response true  ");
                    mDataMyDoctors = null;
                    mDataMyDoctors = response.body();
                    postExecutionSaveDataDoctors();

                } else {
                    String msj = "PICKFLOW CONSULTAR Error en la petición: response false";
                    bitacoraErrorDoctors(msj);
                    Log.e(TAG, msj);
                }
            }

            @Override
            public void onFailure(Call<IConsulMyDoctors.Obj> call, Throwable t) {
                String msj = "GLUCOSA CONSULTAR Error en la petición: onFailure";
                bitacoraErrorGlucosa(msj);
                t.printStackTrace();
            }
        });
    }

    public void postExecutionSaveDataDoctors(){
        Log.e(TAG, "PICKFLOW postExecution Data");
        String msj1 = "Error: Object is null";
        String msj2 = "Error: codigo != 0";
        String msj3 = "Error: lista  Rows  is null or size is 0";
        if (mDataMyDoctors != null) {
            if (mDataMyDoctors.getIdCodResult() == 0 ) {
                if(mDataMyDoctors.getRows() != null && mDataMyDoctors.getRows().size() > 0){
                    Log.e(TAG, "PICKFLOW obtener data server oK ");

                    //enviar datos a guardar
                    for (IConsulMyDoctors.rows  rows :  mDataMyDoctors.getRows() ){
                        saveDataDoctorsDB(rows);
                    }

                }else{
                    bitacoraErrorGlucosa(msj3 );
                }
            }else
                bitacoraErrorGlucosa(msj2 );
        }else
            bitacoraErrorGlucosa(msj1);
    }

    //GUARDOS DATOS EN LA TABLA BDSQLITE
    public  void saveDataDoctorsDB(IConsulMyDoctors.rows rows){
        try {
            //setear datos al objeto y guardar y BD
            Utils.DbsaveDoctorFromDatabase( rows.getDoctorId(),
                    rows.getName(),
                    rows.getLastName(),
                    rows.getEmail(),
                    rows.getCellPhone(),
                    rows.getSpecialty(),
                    HealthMonitorApplicattion.getApplication().getDoctorDao());

            //Utils.generateToast(this, getResources().getString(R.string.txt_guardado));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //BITACORA
    public void bitacoraErrorDoctors(String msj){
        String tipo = "PICKFLOW";
        Log.e(TAG, tipo +" "+ msj );
        //getApplicationContext()
        //LLAMAR METODO WS NUEVO DE BITACORA ERRORES
    }
    ///**********************************FIN DOCTORES******************************//


    ///***********************************INICIO MEDICACION_ALARMA***********************//
    public void importDataMedicineAlarm(){
        IV2ConsulMedicationAlarm IConsul = HealthMonitorApplicattion.getApplication().getRetrofitAdapter().create(IV2ConsulMedicationAlarm.class);
        call_12 = IConsul.queryMedicationAlarmLog (new IV2ConsulMedicationAlarm.ObjQueryMedicationAlarmLog(Email)  );

        call_12.enqueue(new Callback<IV2ConsulMedicationAlarm.Obj>() {
            @Override
            public void onResponse(Call<IV2ConsulMedicationAlarm.Obj> call, Response<IV2ConsulMedicationAlarm.Obj> response) {
                if (response.isSuccessful() ) {
                    Log.e(TAG, "MEDICINE ALARM Respuesta exitosa: response true  ");
                    mDataMedAlm = null;
                    mDataMedAlm = response.body();
                    postExecutionSaveDataMedicineAlarm();

                } else {
                    String msj = "MEDICINE ALARM Error en la petición: response false";
                    bitacoraErrorMedicineAlarm(msj);
                    Log.e(TAG, msj);
                }
            }

            @Override
            public void onFailure(Call<IV2ConsulMedicationAlarm.Obj> call, Throwable t) {
                String msj = "MEDICINE ALARM Error en la petición: onFailure";
                bitacoraErrorMedicineAlarm(msj);
                t.printStackTrace();
            }
        });
    }

    public void postExecutionSaveDataMedicineAlarm(){
        Log.e(TAG, "MEDICINE ALARM postExecution Data");
        String msj1 = "Error: Object is null";
        String msj2 = "Error: codigo != 0";
        String msj3 = "Error: lista  Rows  is null or size is 0";
        if (mDataMedAlm != null) {
            if (mDataMedAlm.getIdCodResult() == 0 ) {
                if(mDataMedAlm.getRows() != null && mDataMedAlm.getRows().size() > 0){
                    Log.e(TAG, "MEDICINE ALARM obtener data server oK ");
                    //enviar datos a guardar
                    for (IV2ConsulMedicationAlarm.Rows  rows :  mDataMedAlm.getRows() ){
                        saveDataMedicineAlarmDB(rows);
                    }
                }else{
                    bitacoraErrorMedicineAlarm(msj3 );
                }
            }else
                bitacoraErrorMedicineAlarm(msj2 );
        }else
            bitacoraErrorMedicineAlarm(msj1);

        importDataMedicineTakeAlarm();
    }

    //GUARDOS DATOS EN LA TABLA BDSQLITE
    public  void saveDataMedicineAlarmDB(IV2ConsulMedicationAlarm.Rows rows){
        try {
            int registeredMedicinesId = 0 ;
            int regMed=0;
            regMed = Utils.saveRegisteredMedicineUserToDataBaseLocal( rows.getMedicineID() , rows.getStartDate().substring(0,19).replace("-","/"),Email, rows.getIdRegisterDB(), HealthMonitorApplicattion.getApplication().getMedicineUserDao()  );
            if (regMed > 0){
                registeredMedicinesId = Utils.saveRegisteredMedicinesToDataBaseLocal( rows.getMedicineID(),
                        rows.getDoseMedicine() , rows.getStartDate().substring(0,19).replace("-","/"),
                        rows.getEndDate() == null ? null :  rows.getEndDate().substring(0,19).replace("-","/")
                        ,  rows.getFrequencyType(), rows.getTimes() , rows.getFrequencyDescription()  , rows.getObservations()  ,Email, rows.getIdRegisterDB(),
                        HealthMonitorApplicattion.getApplication().getRegisteredMedicinesDao());
                if (registeredMedicinesId>0){
                    for (IV2ConsulMedicationAlarm.Alarms alarm: rows.getAlarmas()) {
                        Utils.saveAlarmDetailsToDataBaseLocal( HealthMonitorApplicattion.getApplication().getEAlarmDetailsDao(),registeredMedicinesId,alarm.getHourAlarm(),Email,"",alarm.getIdRegisterDB() );
                    }
                }

            }



//            String fecha= rows.getDate().substring(8,10)+ "/"+ rows.getDate().substring(5,7)+"/" +rows.getDate().substring(0,4);
//            String hora =  rows.getDate().substring(11,16);
//            //setear datos al objeto y guardar y BD
//            Utils.DbsaveAsthmaFromDatabase( rows.getIdRegisterDB(),
//                    fecha,
//                    hora,
//                    rows.getFluxMax(),
//                    rows.getObservations(),
//                    sendServerOK,
//                    operacionI,
//                    HealthMonitorApplicattion.getApplication().getAsthmaDao());

            //Utils.generateToast(this, getResources().getString(R.string.txt_guardado));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //BITACORA
    public void bitacoraErrorMedicineAlarm(String msj){
        String tipo = "MedicineAlarm";
        Log.e(TAG, tipo +" "+ msj );
        //getApplicationContext()
        //LLAMAR METODO WS NUEVO DE BITACORA ERRORES
    }
    ///**********************************FIN MEDICACION_ALARMA******************************//

    ///***********************************INICIO MEDICINE_TAKE_ALARM***********************//
    public void importDataMedicineTakeAlarm(){
        IV2ConsulControlMedication IConsul = HealthMonitorApplicattion.getApplication().getRetrofitAdapter().create(IV2ConsulControlMedication.class);
        call_13 = IConsul.queryControlMedication (new IV2ConsulControlMedication.ObjQueryControlMedication(Email) );

        call_13.enqueue(new Callback<IV2ConsulControlMedication.Obj>() {
            @Override
            public void onResponse(Call<IV2ConsulControlMedication.Obj> call, Response<IV2ConsulControlMedication.Obj> response) {
                if (response.isSuccessful() ) {
                    Log.e(TAG, "CONTROL MEDICINE Respuesta exitosa: response true  ");
                    mDataCtrlMed = null;
                    mDataCtrlMed = response.body();
                    postExecutionSaveDataMedicineTakeAlarm();

                } else {
                    String msj = "CONTROL MEDICINE Error en la petición: response false";
                    bitacoraErrorMedicineTakeAlarm (msj);
                    Log.e(TAG, msj);
                }
            }

            @Override
            public void onFailure(Call<IV2ConsulControlMedication.Obj> call, Throwable t) {
                String msj = "MEDICINE ALARM Error en la petición: onFailure";
                bitacoraErrorMedicineTakeAlarm(msj);
                t.printStackTrace();
            }
        });
    }

    public void postExecutionSaveDataMedicineTakeAlarm(){
        Log.e(TAG, "CONTROL MEDICINE postExecution Data");
        String msj1 = "Error: Object is null";
        String msj2 = "Error: codigo != 0";
        String msj3 = "Error: lista  Rows  is null or size is 0";
        if (mDataCtrlMed != null) {
            if (mDataCtrlMed.getIdCodResult() == 0 ) {
                if(mDataCtrlMed.getRows() != null && mDataCtrlMed.getRows().size() > 0){
                    Log.e(TAG, "CONTROL MEDICINE obtener data server oK ");
                    //enviar datos a guardar
                    for (IV2ConsulControlMedication.Rows  rows :  mDataCtrlMed.getRows() ){
                        saveDataMedicineTakeAlarmDB(rows);
                    }
                }else{
                    bitacoraErrorMedicineTakeAlarm(msj3 );
                }
            }else
                bitacoraErrorMedicineTakeAlarm(msj2 );
        }else
            bitacoraErrorMedicineTakeAlarm(msj1);
    }

    //GUARDOS DATOS EN LA TABLA BDSQLITE
    public  void saveDataMedicineTakeAlarmDB(IV2ConsulControlMedication.Rows rows){
        try {
            Log.e(TAG, "CONTROL MEDICINE saveDataMedicineTakeAlarmDB" + rows.toString());
//            String sRegisteredMedicinesId= String.valueOf(Utils.getRegisteredMedicinesIdFromDataBaseLocal(HealthMonitorApplicattion.getApplication().getRegisteredMedicinesDao(),rows.getIdRegisterDB() ));
//            String sAlarmDetailId = String.valueOf(Utils.getAlarmDetailIdFromDataBaseLocal(HealthMonitorApplicattion.getApplication().getEAlarmDetailsDao(),rows.getIdAlarmMedication() ));
//            int registeredMedicinesId = Integer.parseInt(sRegisteredMedicinesId);
//            int alarmDetailId = Integer.parseInt(sAlarmDetailId) ;
            EAlarmDetails eAlarmDetails = Utils.getAlarmDetailIdFromDataBaseLocal(HealthMonitorApplicattion.getApplication().getEAlarmDetailsDao(),rows.getIdAlarmMedication() );
            Log.e(TAG, "CONTROL MEDICINE saveDataMedicineTakeAlarmDB eAlarmDetails " + eAlarmDetails.toString());
            int registeredMedicinesId = 0 ;
            int alarmDetailId = 0;
            int regMed=0;
            if (eAlarmDetails != null){
                registeredMedicinesId = eAlarmDetails.getRegisteredMedicinesId();
                alarmDetailId = eAlarmDetails.getAlarmDetailId();
                String fecha=rows.getDate().substring(0,19).replace("-","/");
                regMed = Utils.saveAlarmTakeMedicineToDataBaseLocal( HealthMonitorApplicattion.getApplication().getEAlarmTakeMedicineDao(),
                        registeredMedicinesId,alarmDetailId,fecha,Email,"U","S"
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //BITACORA
    public void bitacoraErrorMedicineTakeAlarm(String msj){
        String tipo = "MedicineTakeAlarm";
        Log.e(TAG, tipo +" "+ msj );
        //getApplicationContext()
        //LLAMAR METODO WS NUEVO DE BITACORA ERRORES
    }
    ///**********************************FIN TAKE_MEDICINE******************************//

    @Override
    public void onDestroy() {
        Toast.makeText(this, "Servicio Finalizado...", Toast.LENGTH_SHORT).show();

        // Emisión para avisar que se terminó el servicio
        //Intent localIntent = new Intent(Constants.ACTION_PROGRESS_EXIT);
        //LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);

        Log.e(TAG, "Servicio destruido...");
    }

    //********************************ELIMAR DATOS BD SQLITE ANDROID*******************
    public void eliminarDatosDB(){
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
