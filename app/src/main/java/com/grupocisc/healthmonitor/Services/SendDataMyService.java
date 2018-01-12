package com.grupocisc.healthmonitor.Services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;


import com.grupocisc.healthmonitor.HealthMonitorApplicattion;
import com.grupocisc.healthmonitor.Utils.Utils;
import com.grupocisc.healthmonitor.entities.EAlarmDetails;
import com.grupocisc.healthmonitor.entities.EAlarmTakeMedicine;
import com.grupocisc.healthmonitor.entities.IState;
import com.grupocisc.healthmonitor.entities.IV2Cholesterol;
import com.grupocisc.healthmonitor.entities.IColesterol;
import com.grupocisc.healthmonitor.entities.IConsulHba1c;
import com.grupocisc.healthmonitor.entities.IGlucose;
import com.grupocisc.healthmonitor.entities.IHba1c;
import com.grupocisc.healthmonitor.entities.IPulse;
import com.grupocisc.healthmonitor.entities.IRecomGlucose;
import com.grupocisc.healthmonitor.entities.EInsulin;
import com.grupocisc.healthmonitor.entities.EMedicineUser;
import com.grupocisc.healthmonitor.entities.IConsulMedicines;

import com.grupocisc.healthmonitor.entities.IRegWeight;
import com.grupocisc.healthmonitor.entities.ISendPulsePresion;
import com.grupocisc.healthmonitor.entities.IV2Insulina;
import com.grupocisc.healthmonitor.entities.IV2RegisterAlarmMedication;
import com.grupocisc.healthmonitor.entities.IV2RegisterAlarmTakeMedicine;
import com.grupocisc.healthmonitor.entities.IV2RegisterMedication;
import com.grupocisc.healthmonitor.entities.IWeight;
import com.grupocisc.healthmonitor.entities.IRegisteredMedicines;
import com.grupocisc.healthmonitor.entities.IV2RegistreState;
import com.grupocisc.healthmonitor.entities.rowV2Cholesterol;
import com.grupocisc.healthmonitor.entities.rowV2CholesterolUpdate;
import com.grupocisc.healthmonitor.entities.rowGlucosa;
import com.grupocisc.healthmonitor.entities.rowGlucosaUpdate;
import com.grupocisc.healthmonitor.entities.rowV2Hba1;
import com.grupocisc.healthmonitor.entities.rowInsulin;
import com.grupocisc.healthmonitor.entities.rowInsulinUpdate;
import com.grupocisc.healthmonitor.entities.rowPeso;
import com.grupocisc.healthmonitor.entities.rowPesoUpdate;
import com.grupocisc.healthmonitor.entities.rowPulsePresion;
import com.grupocisc.healthmonitor.entities.rowPulsePresionUpdate;
import com.grupocisc.healthmonitor.entities.rowV2Hba1Update;
import com.grupocisc.healthmonitor.entities.rowV2State;
import com.grupocisc.healthmonitor.entities.rowV2StateUpdate;

import java.sql.SQLException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/***************************************************************************
 ** ENVIAR TODOS LOS REGISTROS DE LA BASEDATOS SQLITE
 ** A LA BASEDATOS DEL SERVIDOR MEDIANTE WEBSERVICE
 **************************************************************************/

public class SendDataMyService extends Service {

    private static final String TAG = SendDataMyService.class.getSimpleName();
    TimerTask timerTask;
    public String Email = "";

    private Call<IV2RegistreState.RegistroState> call_2;
    private IV2RegistreState.RegistroState mRegistroState;

    private Call<ISendPulsePresion.SendPulsePresion> call_3;
    private ISendPulsePresion.SendPulsePresion mSendPulsePresion;

    private Call<IRegWeight.RegWeight> call_4;
    private IRegWeight.RegWeight mRegWeight;

    private  Call<IV2Cholesterol.Cholesterol>call_5;
    private  IV2Cholesterol.Cholesterol mCholesterol;

    private Call<IConsulHba1c.Hba>call_6;
    private IConsulHba1c.Hba mHba1c;

    private IV2RegistreState.RegistroState mSaveAnPaciente;

    private Call<IV2Insulina.Insulina> call_7;
    private IV2Insulina.Insulina mInsulina;


    private   Call<IRecomGlucose.RecomGlucose>glucose;
    private IRecomGlucose.RecomGlucose mRegGlucosa;

    private Call<IConsulMedicines.RegMedicacion> medicacionCall;
    private IConsulMedicines.RegMedicacion mSaveCrtRMedicines;

    private Call<IV2RegisterMedication.MedicationRegister> medUserCtrlCall;
    private IV2RegisterMedication.MedicationRegister mSaveUserCtrl;
    private Call<IV2RegisterMedication.MedicationUpdate> medUserCtrlUpdCall;
    private IV2RegisterMedication.MedicationUpdate mSaveUserUpdCtrl;

    //private boolean sendDataMedUsrCtrlWs = false;

    private Call<IV2RegisterAlarmMedication.registerAlarmMedication> medAlarmCall;
    private IV2RegisterAlarmMedication.registerAlarmMedication mSaveMedAlarm;

    private Call<IV2RegisterAlarmTakeMedicine.registerAlarmTakeMedicine> medAlarmTakeCall;
    private IV2RegisterAlarmTakeMedicine.registerAlarmTakeMedicine mSaveAlarmTake;

    private static final String sendServer = "false";
    private static final String sendServerOK = "true";
    private static final String operacionI = "I";
    private static final String operacionU = "U";

    public SendDataMyService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "Servicio onCreate...");
        callSendDataWS();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "Servicio onStartCommand...");
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void callSendDataWS() {
        Log.e(TAG, "Servicio iniciado...");
        Timer timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {

                Log.e(TAG, "timerTask intro");
                dataPreference();
                Pulso();
                Glucosa();
                WeightData();
                insulinData();
                CholesterolData();
                hba1c();
                stateData();//Pendiente lo va a hacer remotamente
                //medicineUserRegistered(); POLO
                medicineUserControl(); //POLO
                alarmTakeMedicine();//POLO

            }
        };
        timer.scheduleAtFixedRate(timerTask, 0, 60000);//60000 = 1 mint
    }

    public void dataPreference() {
        //OBTENER DATA DE PREFERENCIA
        if (Utils.getEmailFromPreference(this) != null)
            Email = Utils.getEmailFromPreference(this);
    }

    /*************************************** INICIO PULSO ******************************************/
    public void Pulso(){
        if(isOnline())
            selectDataPulseDB();
    }

    //obtener datos de la tabla BD
    public  void selectDataPulseDB(){
        List<IPulse> rowsPulse;
        Log.e(TAG,"selectDataPulseDB");
        try {
            //validar si en la tabla ahi datos mayor a 0
            if (Utils.GetPulseNotSendFromDatabase(HealthMonitorApplicattion.getApplication().getPulseDao(),sendServer ).size() > 0 ){
                //asignamos datos de la tabla a la lista de objeto
                rowsPulse = Utils.GetPulseNotSendFromDatabase(HealthMonitorApplicattion.getApplication().getPulseDao(),sendServer  );
                //obtenemos el tamaño de la listaDEobjetos , para recorrerlo
                //y presenta los datos de la tabla bd en el LOG
                int tamaño = rowsPulse.size();
                for(int i = 0 ; i < tamaño ; i++){
                    Log.e(TAG,"**Pulse:" + rowsPulse.get(i).getEnviadoServer() +"-" + rowsPulse.get(i).getConcentracion());//se cambia concentracion por peso
                }
                sendWSPulse(rowsPulse);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //envia la lista al llamado ws
    public void  sendWSPulse(List<IPulse> rowsPulse){
        try{
            for (IPulse  puls: rowsPulse){
                restartLoadingEnviarData(puls);
            }
        }catch (Exception e){
            System.out.println(e);
        }
    }

    private void restartLoadingEnviarData(final IPulse pulse ){
        final int idPulse ;
        String fecha ="";
        Log.e(TAG, "PULSO-PRESION  restartLoadingEnviarData:" + pulse.getOperacion() +"-ID-"+pulse.getId());
        idPulse    = pulse.getId();
        float presionSistolica = Float.parseFloat(pulse.getMinPressure());
        float presionDistolica = Float.parseFloat(pulse.getMaxPressure());
        int pulso            = pulse.getConcentracion();
        String observacion     = pulse.getObservacion();
        String medido          = pulse.getMedido();
        //fecha    = pulse.getFecha().substring(6,10)+"/"+pulse.getFecha().substring(3,5)+"/"+pulse.getFecha().substring(0,2) + " "+pulse.getHora();
        fecha    = pulse.getFecha().replace("/","-") + " " + pulse.getHora();
        Log.e(TAG, fecha);

        //INSERTAR
        if(pulse.getOperacion().equals(operacionI)) {
            ISendPulsePresion Ipulse = HealthMonitorApplicattion.getApplication().getRetrofitAdapter().create(ISendPulsePresion.class);
            call_3 = Ipulse.setSendPulsePresionFrom(new rowPulsePresion(Email, presionSistolica,presionDistolica,medido,pulso,fecha, observacion ));
            call_3.enqueue(new Callback<ISendPulsePresion.SendPulsePresion>() {
                @Override
                public void onResponse(Call<ISendPulsePresion.SendPulsePresion> call, Response<ISendPulsePresion.SendPulsePresion> response) {
                    if (response.isSuccessful() ) {
                        Log.e(TAG, "PULSO-PRESION INSERTAR Respuesta exitosa: response true  ");
                        mSendPulsePresion = null;
                        mSendPulsePresion = response.body();
                        postExecutionEnviarDataP(idPulse);
                    } else {
                        String msj = "PULSO-PRESION INSERTAR Error en la petición: response false";
                        bitacoraErrorPulse(msj);
                    }
                }
                @Override
                public void onFailure(Call<ISendPulsePresion.SendPulsePresion> call, Throwable t) {
                    String msj = "PULSO-PRESION INSERTAR Error en la petición: onFailure";
                    bitacoraErrorPulse(msj);
                    t.printStackTrace();
                }
            });
        }else if(pulse.getOperacion().equals(operacionU)) {
            //ACTUALIZAR
            ISendPulsePresion Ipulse = HealthMonitorApplicattion.getApplication().getRetrofitAdapter().create(ISendPulsePresion.class);
            call_3 = Ipulse.setSendPulsePresionUpdateFrom(new rowPulsePresionUpdate(pulse.getIdBdServer(),presionSistolica,presionDistolica,medido,pulso,fecha, observacion ));
            call_3.enqueue(new Callback<ISendPulsePresion.SendPulsePresion>() {
                @Override
                public void onResponse(Call<ISendPulsePresion.SendPulsePresion> call, Response<ISendPulsePresion.SendPulsePresion> response) {
                    if (response.isSuccessful()) {
                        Log.e(TAG, "PULSO-PRESION ACTUALIZAR Respuesta exitosa: response true  ");
                        mSendPulsePresion = null;
                        mSendPulsePresion = response.body();
                        postExecutionEnviarDataPUpdate(idPulse);
                    } else {
                        String msj = "PULSO-PRESION ACTUALIZAR Error en la petición: response false";
                        bitacoraErrorPulse(msj);
                        Log.e(TAG, msj);
                    }
                }
                @Override
                public void onFailure(Call<ISendPulsePresion.SendPulsePresion> call, Throwable t) {
                    String msj = "PULSO-PRESION ACTUALIZAR Error en la petición: onFailure";
                    bitacoraErrorPulse(msj);
                    t.printStackTrace();
                }
            });
        }

    }

    public void postExecutionEnviarDataP(int idPulse){
        Log.e(TAG, "PULSO-PRESION postExecutionEnviar Data");
        String msj1 = "Error: Object is null";
        String msj2 = "Error: codigo != 0";
        if (mSendPulsePresion != null) {
            if (mSendPulsePresion.getIdCodResult() == 0 ) {
                updateDataPulseDB(idPulse, mSendPulsePresion.getIdRegisterDB());
                Log.e(TAG, "PULSO-PRESION send server oK ");
            }else
                bitacoraErrorPulse(msj2+ " "+mSendPulsePresion.getResultDescription() );
        }else
            bitacoraErrorPulse(msj1);
    }

    public void postExecutionEnviarDataPUpdate(int idPulse){
        Log.e(TAG, "PULSO-PRESION postExecutionEnviar Data");
        String msj1 = "Error: Object is null";
        String msj2 = "Error: codigo != 0";
        if (mSendPulsePresion != null) {
            if (mSendPulsePresion.getIdCodResult() == 0 ) {
                updateDataPulseDB2(idPulse );
                Log.e(TAG, "PULSO-PRESION send server oK ");
            }else
                bitacoraErrorPulse(msj2+ " "+mSendPulsePresion.getResultDescription() );
        }else
            bitacoraErrorPulse(msj1);
    }

    //ACTUALIZAR DATOS EN LA TABLA BDSQLITE ENVIADO OK TRUE
    public  void updateDataPulseDB(int idAuxPulse, int IdRegisterDB){
        try {
            Log.e(TAG, "PULSO-PRESION updateDataPulseDB");
            //setear datos al objeto y guardar y BD
            Utils.sendOkUpdatePulseFromDatabase(idAuxPulse,
                    sendServerOK,
                    IdRegisterDB,
                    HealthMonitorApplicattion.getApplication().getPulseDao());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateDataPulseDB2(int idAuxPulse){
        try {
            Log.e(TAG, "PULSO-PRESION updateDataPulseDB");
            //setear datos al objeto y guardar y BD
            Utils.sendOkUpdatePulseFromDatabase2(idAuxPulse,
                    sendServerOK,
                    HealthMonitorApplicattion.getApplication().getPulseDao());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void bitacoraErrorPulse(String msj){
        String inicio = "Pulse";
        Log.e(TAG, inicio +" "+ msj );
        //getApplicationContext()
        //LLAMAR METODO WS NUEVO DE BITACORA ERRORES
    }
    /*************************************** FIN PULSO ******************************************/


    /*************************************** INICIO  GLUCOSA ******************************************/
    public void Glucosa(){
        if(isOnline())
            selectDataGlucosaDB();
    }

    //obtener datos de la tabla BD
    public  void selectDataGlucosaDB(){
        List<IGlucose> rowsGlucosa;
        Log.e(TAG,"** selectDataGLUCOSADB");
        try {
            //validar si en la tabla ahi datos mayor a 0
            if (Utils.GetGlucoseNotFromDatabase(HealthMonitorApplicattion.getApplication().getGlucoseDao(),sendServer ).size() > 0 ){
                //asignamos datos de la tabla a la lista de objeto
                rowsGlucosa = Utils.GetGlucoseNotFromDatabase(HealthMonitorApplicattion.getApplication().getGlucoseDao(),sendServer  );
                //obtenemos el tamaño de la listaDEobjetos , para recorrerlo
                //y presenta los datos de la tabla bd en el LOG
                int tamaño = rowsGlucosa.size();
                for(int i = 0 ; i < tamaño ; i++){
                    Log.e(TAG,"GLUCOSA:" + rowsGlucosa.get(i).getConcentracion() );//se cambia concentracion por peso
                }
                sendWSGlucosa(rowsGlucosa);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //envia la lista al llamado ws
    public void  sendWSGlucosa(List<IGlucose> rowsGlucosa){
        try{
            for (IGlucose  gluc: rowsGlucosa){
                restartLoadingEnviarData(gluc);
            }
        }catch (Exception e){
            System.out.println(e);
        }
    }

    private void restartLoadingEnviarData(final IGlucose glucosa ){
        Log.e(TAG, "GLUCOSA  restartLoadingEnviarData:" + glucosa.getConcentracion() );
        final int idGlucosa      = glucosa.getId();
        int measureUnits = glucosa.getConcentracion();
        //String fecha           = glucosa.getFecha().substring(6,10)+"/"+glucosa.getFecha().substring(3,5)+"/"+glucosa.getFecha().substring(0,2) + " "+ glucosa.getHora();
        String fecha           = glucosa.getFecha().replace("/","-") + " " + glucosa.getHora();
        String observacion= glucosa.getObservacion();
        Log.e(TAG, fecha);

        try {
            Log.e(TAG, idGlucosa+" "+  measureUnits  +"-"+ fecha +"-"+ observacion );
            //INSERTAR
            if(glucosa.getOperacion().equals(operacionI)) {
                IRecomGlucose IGlucosa = HealthMonitorApplicattion.getApplication().getRetrofitAdapter().create(IRecomGlucose.class);
                glucose = IGlucosa.setSendregisterGlucosaFrom(new rowGlucosa(Email, measureUnits,fecha, observacion ));
                glucose.enqueue(new Callback<IRecomGlucose.RecomGlucose>() {
                    @Override
                    public void onResponse(Call<IRecomGlucose.RecomGlucose> call, Response<IRecomGlucose.RecomGlucose> response) {
                        if (response.isSuccessful() ) {
                            Log.e(TAG, "GLUCOSA INSERTAR Respuesta exitosa: response true  ");
                            mRegGlucosa = null;
                            mRegGlucosa = response.body();
                            postExecutionEnviarDataGluco(idGlucosa);
                        } else {
                            String msj1 = "GLUCOSA INSERTAR Error en la petición: response false";
                            bitacoraErrorPulse(msj1);
                        }
                    }

                    @Override
                    public void onFailure(Call<IRecomGlucose.RecomGlucose> call, Throwable t) {
                        String msj1 = "GLUCOSA INSERTAR Error en la petición: onFailure";
                        bitacoraErrorGlucosa(msj1);
                        t.printStackTrace();
                    }
                });
            }else if(glucosa.getOperacion().equals(operacionU)) {
                //ACTUALIZAR
                IRecomGlucose IGlucosa = HealthMonitorApplicattion.getApplication().getRetrofitAdapter().create(IRecomGlucose.class);
                glucose = IGlucosa.setSendregisterGlucosaUpdateFrom(new rowGlucosaUpdate(glucosa.getIdBdServer(), measureUnits,fecha, observacion ));
                glucose.enqueue(new Callback<IRecomGlucose.RecomGlucose>() {
                    @Override
                    public void onResponse(Call<IRecomGlucose.RecomGlucose> call, Response<IRecomGlucose.RecomGlucose> response) {
                        if (response.isSuccessful()) {
                            Log.e(TAG, "GLUCOSA ACTUALIZAR Respuesta exitosa: response true  ");
                            mRegGlucosa = null;
                            mRegGlucosa = response.body();
                            postExecutionEnviarDataGlucoUpdate(idGlucosa);
                        } else {
                            String msj1 = "GLUCOSA ACTUALIZAR Error en la petición: response false";
                            bitacoraErrorGlucosa(msj1);
                            Log.e(TAG, msj1);
                        }
                    }

                    @Override
                    public void onFailure(Call<IRecomGlucose.RecomGlucose> call, Throwable t) {
                        String msj1 = "Glucosa ACTUALIZAR Error en la petición: onFailure";
                        bitacoraErrorPulse(msj1);
                        t.printStackTrace();
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void postExecutionEnviarDataGluco(int idGlucosa){
        Log.e(TAG, "GLUCOSA postExecutionEnviar Data");
        String msj1 = "Error: Object is null";
        String msj2 = "Error: codigo != 0";
        if (mRegGlucosa != null) {
            if (mRegGlucosa.getIdCodResult() == 0 ) {
                Log.e(TAG, "GLUCOSA send server oK "+ idGlucosa +" idbd: "+ mRegGlucosa.getIdRegisterDB());
                updateDataGlucosaDB(idGlucosa, mRegGlucosa.getIdRegisterDB());

            }else
                bitacoraErrorPulse(msj2+ " "+mRegGlucosa.getResultDescription() );
        }else
            bitacoraErrorPulse(msj1);
    }

    public void postExecutionEnviarDataGlucoUpdate(int idGlucosa){
        Log.e(TAG, "GLUCOSA postExecutionEnviar Data");
        String msj1 = "Error: Object is null";
        String msj2 = "Error: codigo != 0";
        if (mRegGlucosa != null) {
            if (mRegGlucosa.getIdCodResult() == 0 ) {
                updateDataGlucosaDB2(idGlucosa);
                Log.e(TAG, "GLUCOSA send server oK ");
            }else
                bitacoraErrorPulse(msj2+ " "+mRegGlucosa.getResultDescription() );
        }else
            bitacoraErrorPulse(msj1);
    }

    //ACTUALIZAR DATOS EN LA TABLA BDSQLITE ENVIADO OK TRUE
    public  void updateDataGlucosaDB(int idAuxPulse, int IdRegisterDB){
        try {
            Log.e(TAG, "GLUCOSA updateDataPulseDB");
            //setear datos al objeto y guardar y BD
            Utils.sendOkUpdateGlucosaFromDatabase(idAuxPulse,
                                                  sendServerOK,
                                                  IdRegisterDB,
                                                  HealthMonitorApplicattion.getApplication().getGlucoseDao());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //ACTUALIZAR DATOS EN LA TABLA BDSQLITE ENVIADO OK TRUE
    public  void updateDataGlucosaDB2(int idAuxPulse){
        try {
            Log.e(TAG, "GLUCOSA updateDataPulseDB 2");
            //setear datos al objeto y guardar y BD
            Utils.sendOkUpdateGlucosaFromDatabase2(idAuxPulse,
                                                    sendServerOK,
                                                    HealthMonitorApplicattion.getApplication().getGlucoseDao());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void bitacoraErrorGlucosa(String msj){
        String inicio = "GLUCOSA";
        Log.e(TAG, inicio +" "+ msj );
        //getApplicationContext()
        //LLAMAR METODO WS NUEVO DE BITACORA ERRORES
    }
    /*************************************** FIN  GLUCOSA ******************************************/

    /*************************************** INICIO PESO ******************************************/

    public void WeightData() {
        if (isOnline()) {
            selectDataWeightDB();
        }
    }
    public void selectDataWeightDB() {
        List<IWeight> rowsWeight;
        Log.e(TAG, "selectDataPESODB");
        try {
            //validar si en la tabla ahi datos mayor a 0
            if (Utils.GetWeightNotSendFromDatabase(HealthMonitorApplicattion.getApplication().getWeightDao(), sendServer).size() > 0) {
                //asignamos datos de la tabla a la lista de objeto
                rowsWeight = Utils.GetWeightNotSendFromDatabase(HealthMonitorApplicattion.getApplication().getWeightDao(), sendServer);
                //obtenemos el tamaño de la listaDEobjetos , para recorrerlo
                //y presenta los datos de la tabla bd en el LOG
                int tamaño = rowsWeight.size();
                for (int i = 0; i < tamaño; i++) {
                    Log.e(TAG, "PESO:" + rowsWeight.get(i).getPeso() + "/" + rowsWeight.get(i) + " mm/Hg" + " -fecha:" + rowsWeight.get(i).getFecha());//se cambia concentracion por peso
                }
                sendWSWeight(rowsWeight);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //envia la lista al llamado ws
    public void sendWSWeight(List<IWeight> rowsWeight) {
        try {
            for (IWeight wei : rowsWeight) {
                saveDataPesoDB(wei);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    //GUARDOS DATOS EN LA TABLA BDSQLITE
    public void saveDataPesoDB(final IWeight weight) {
        final int idServer = weight.getIdBdServer();
        final int idweight = weight.getId();
        final float peso = Float.parseFloat(String.valueOf(weight.getPeso()));
        final float imc = 0;
        final float masaMuscular = Float.parseFloat(String.valueOf(weight.getMasamuscular()));
        final float tmb = Float.parseFloat(String.valueOf(weight.getTmb()));
        final float dmo = Float.parseFloat(String.valueOf(weight.getDmo()));
        final float porcentajeAgua = Float.parseFloat(String.valueOf(weight.getPorcentajeGrasa()));
        final float porcentajeGrasa = Float.parseFloat(String.valueOf(weight.getPorcentajeAgua()));
        //String fecha = weight.getFecha().substring(6, 10) + "/" + weight.getFecha().substring(3, 5) + "/" + weight.getFecha().substring(0, 2) + " " + weight.getHora();
        String fecha = weight.getFecha().replace("/","-") + " " + weight.getHora();
        String observacion = weight.getObservacion();
        Log.e(TAG, fecha);
        try {
            Log.e(TAG,"PESO"+ Email +"-"+ peso +"-"+ tmb +"-"+ porcentajeAgua +"-"+ porcentajeGrasa+"-"+ dmo+"-"+ masaMuscular +"-"+ fecha +"-"+ observacion );

            if (weight.getOperacion().equals(operacionI)) {
                IRegWeight IWeight = HealthMonitorApplicattion.getApplication().getRetrofitAdapter().create(IRegWeight.class);
                call_4 = IWeight.setSendregisterWeightFrom(new rowPeso(Email, peso, imc, tmb, porcentajeAgua, porcentajeGrasa, dmo, masaMuscular, fecha, observacion));
                call_4.enqueue(new Callback<IRegWeight.RegWeight>() {
                    @Override
                    public void onResponse(Call<IRegWeight.RegWeight> call, Response<IRegWeight.RegWeight> response) {
                        if (response.isSuccessful()) {
                            Log.e(TAG, "PESO INSERTAR Respuesta exitosa: response true");
                            mRegWeight = null;
                            mRegWeight = response.body();
                            postExecutionEnviarDataPeso(idweight);
                        } else {
                            String msj = "PESO INSERTAR Error en la petición: response false";
                            bitacoraErrorPulse(msj);
                            Log.e(TAG, msj);
                        }
                    }
                    @Override
                    public void onFailure(Call<IRegWeight.RegWeight> call, Throwable t) {
                        String msj = "PESO INSERTAR Error en la petición: onFailure";
                        bitacoraErrorPulse(msj);
                        t.printStackTrace();
                    }
                });
            } else if (weight.getOperacion().equals(operacionU)) {
                //ACTUALIZAR
                IRegWeight IWeight =  HealthMonitorApplicattion.getApplication().getRetrofitAdapter().create(IRegWeight.class);
                call_4 = IWeight.setSendWeightUpdateFrom(new rowPesoUpdate(weight.getIdBdServer() , peso, imc, tmb, porcentajeAgua, porcentajeGrasa, dmo, masaMuscular, fecha, observacion));
                call_4.enqueue(new Callback<IRegWeight.RegWeight>() {
                    @Override
                    public void onResponse(Call<IRegWeight.RegWeight> call, Response<IRegWeight.RegWeight> response) {
                        if (response.isSuccessful()) {
                            Log.e(TAG, "PESO ACTUALIZAR Respuesta exitosa: response true  ");
                            mRegWeight = null;
                            mRegWeight = response.body();
                            postExecutionEnviarDataPesoUpdate(idweight);
                        } else {
                            String msj = "PESO ACTUALIZAR Error en la petición: response false";
                            bitacoraErrorPulse(msj);
                            Log.e(TAG, msj);
                        }
                    }
                    @Override
                    public void onFailure(Call<IRegWeight.RegWeight> call, Throwable t) {
                        String msj = "PESO ACTUALIZAR Error en la petición: onFailure";
                        bitacoraErrorPulse(msj);
                        t.printStackTrace();
                    }
                });
            }
        } catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    public void postExecutionEnviarDataPeso(int idweight){
        Log.e(TAG, "PESO postExecutionEnviar Data");
        String msj = "Error: Object is null";
        String msj1 = "Error: codigo != 0";
        if (mRegWeight != null) {
            if (mRegWeight.getIdCodResult() == 0 ) {
                Log.e(TAG, "PESO send server oK ");
                updateDataWeightDB(idweight, mRegWeight.getIdRegisterDB());
            }else
                bitacoraErrorWeight(msj1+ " "+mRegWeight.getResultDescription() );
        }else
            bitacoraErrorWeight(msj);
    }

    public void postExecutionEnviarDataPesoUpdate(int idweight){
        Log.e(TAG, "PESO postExecutionEnviar Data");
        String msj = "Error: Object is null";
        String msj1 = "Error: codigo != 0";
        if (mRegWeight != null) {
            if (mRegWeight.getIdCodResult() == 0 ) {
                Log.e(TAG, "PESO send server oK ");
                updateDataWeightDB2(idweight);
            }else
                bitacoraErrorWeight(msj1+ " "+mRegWeight.getResultDescription() );
        }else
            bitacoraErrorWeight(msj);
    }

    //ACTUALIZAR DATOS EN LA TABLA BDSQLITE ENVIADO OK TRUE
    public  void updateDataWeightDB(int idAuxWeight, int IdRegisterDB){
        try {
            Log.e(TAG, "PESO updateDataPulseDB");
            //setear datos al objeto y guardar y BD
            Utils.sendOkUpdateWeightFromDatabase(idAuxWeight,
                                                sendServerOK,
                                                IdRegisterDB,
                                                HealthMonitorApplicattion.getApplication().getWeightDao());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public  void updateDataWeightDB2(int idAuxWeight){
        try {
            Log.e(TAG, "PESO updateDataPulseDB 2");
            //setear datos al objeto y guardar y BD
            Utils.sendOkUpdateWeightFromDatabase2(idAuxWeight,
                                                    sendServerOK,
                                                    HealthMonitorApplicattion.getApplication().getWeightDao());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void bitacoraErrorWeight(String msj){
        String inicio = "PESO";
        Log.e(TAG, inicio +" "+ msj );
        //getApplicationContext()
        //LLAMAR METODO WS NUEVO DE BITACORA ERRORES
    }

    /*************************************** FIN PESO ******************************************/

    /*************************************** INICIO INSULINA ******************************************/

    public void insulinData() {
        if (isOnline())
            selectDataInsulinDB();
    }

    private void selectDataInsulinDB() {
        String Method = "[selectDataInsulinDB]";
        List<EInsulin> lstEInsulin;
        Log.e(TAG, Method + "Init...");
        try {
            lstEInsulin = Utils.GetInsulinNotSendFromDatabase(HealthMonitorApplicattion.getApplication().getInsulinDao(), sendServer);
            if (lstEInsulin != null)
                if (lstEInsulin.size() > 0) {
                for (EInsulin eInsulin : lstEInsulin) {
                    Log.i(TAG, Method + " EInsulin = " + eInsulin.toString());
                }
                sendWsInsulin(lstEInsulin);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void sendWsInsulin(List<EInsulin> lstEInsulin) {
        try {
            for (EInsulin eInsulin : lstEInsulin) {
                restartLoadingSendDataInsulin(eInsulin);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private void restartLoadingSendDataInsulin(final EInsulin eInsulin) {
        final int idInsulin = eInsulin.getId();
        int insulina = eInsulin.getInsulina();// Float.parseFloat(etxt_dosis.getText().toString()) ;
        String observacion = eInsulin.getObservacion();
        //String fechaHora = eInsulin.getFecha().substring(6,10) + "-" + eInsulin.getFecha().substring(3,5) + "-" + eInsulin.getFecha().substring(0,2)  + " " + eInsulin.getHora();
        String fechaHora = eInsulin.getFecha().replace("/","-")  + " " + eInsulin.getHora();
        Log.e(TAG,"INSULINA DATA: "+ fechaHora +"--"+ eInsulin.toString() );
        try
        {
            if(eInsulin.getOperationDb().equals(operacionI)) {
                IV2Insulina CrtPacient = HealthMonitorApplicattion.getApplication().getRetrofitAdapter().create(IV2Insulina.class);
                call_7 = CrtPacient.setSendInsulinFrom(new rowInsulin(Email, idInsulin, fechaHora, observacion ));
                call_7.enqueue(new Callback<IV2Insulina.Insulina>() {
                    @Override
                    public void onResponse(Call<IV2Insulina.Insulina> call, retrofit2.Response<IV2Insulina.Insulina> response) {
                        String Method = "[Insulin]";
                        if (response.isSuccessful()) {
                            Log.e(TAG, Method + "Respuesta exitosa...");
                            mInsulina = null;
                            mInsulina = response.body();
                            postExecutionSendDataInsulin(idInsulin);
                        } else {
                            String msj = "Error en la petición: response false";
                            Log.e(TAG, Method + msj );
                            bitacoraErrorInsulina(msj);
                        }
                    }

                    @Override
                    public void onFailure(Call<IV2Insulina.Insulina> call, Throwable t) {
                        String msj1 = "PULSO-PRESION INSERTAR Error en la petición: onFailure";
                        bitacoraErrorInsulina(msj1);
                        t.printStackTrace();
                    }
                });
            }else if(eInsulin.getOperationDb().equals(operacionU)){
                //ACTUALIZAR
                IV2Insulina CrtPacient = HealthMonitorApplicattion.getApplication().getRetrofitAdapter().create(IV2Insulina.class);
                call_7 = CrtPacient.setSendInsulinUpdateFrom(new rowInsulinUpdate(eInsulin.getIdBdServer(), insulina, fechaHora, observacion));
                call_7.enqueue(new Callback<IV2Insulina.Insulina>() {
                    @Override
                    public void onResponse(Call<IV2Insulina.Insulina> call, retrofit2.Response<IV2Insulina.Insulina> response) {
                        String Method = "INSULINA ";
                        if (response.isSuccessful()) {
                            Log.e(TAG, Method + "actualizar Respuesta exitosa...");
                            mInsulina = null;
                            mInsulina = response.body();
                            postExecutionSendDataInsulinUpdate(idInsulin);
                        } else {
                            String msj = "Error en la petición: response false";
                            Log.e(TAG," actualizar"+ msj);
                            bitacoraErrorInsulina(msj);

                        }
                    }
                    @Override
                    public void onFailure(Call<IV2Insulina.Insulina> call, Throwable t) {
                        t.printStackTrace();
                        Log.e(TAG, "INSULINA Error en la petición: response onFailure" );
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void postExecutionSendDataInsulin(int idInsulin) {
        String msj1 = "Error: Object is null";
        String msj2 = "Error: codigo != 0";
        if (mInsulina != null) {
            if (mInsulina.getIdCodResult() == 0) {
                updateDataInsulinDB(idInsulin, mInsulina.getIdRegisterDB()) ;
                Log.i(TAG, "Send Server OK Insulin");
            } else
                bitacoraErrorInsulina(msj2 + "[ErrorCode=" + mInsulina.getIdCodResult() + "][ErrorDescription=" + mInsulina.getResultDescription()+ "]");
        } else
            bitacoraErrorInsulina(msj1);
    }

    private void postExecutionSendDataInsulinUpdate(int idInsulin) {
        String msj1 = "Error: Object is null";
        String msj2 = "Error: codigo != 0";
        if (mInsulina != null) {
            if (mInsulina.getIdCodResult() == 0) {
                updateDataInsulinDB2(idInsulin) ;
                Log.e(TAG, "INSULINA Send Server OK Insulin");
            } else
                bitacoraErrorInsulina(msj2 + "[ErrorCode=" + mInsulina.getIdCodResult() + "][ErrorDescription=" + mInsulina.getResultDescription()+ "]");
        } else
            bitacoraErrorInsulina(msj1);
    }

    private void updateDataInsulinDB(int intidInsulin,int idBdServer ) {
        try {
            //setear datos al objeto y guardar y BD
            Utils.sendOkUpdateInsulinFromDatabase(intidInsulin,
                                                idBdServer,
                                                sendServerOK,
                                                HealthMonitorApplicattion.getApplication().getInsulinDao());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateDataInsulinDB2(int intidInsulin) {
        try {
            //setear datos al objeto y guardar y BD
            Utils.sendOkUpdateInsulinFromDatabase2(intidInsulin,
                                                    sendServerOK,
                                                    HealthMonitorApplicattion.getApplication().getInsulinDao());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void bitacoraErrorInsulina(String msj){
        String inicio = "INSULINA";
        Log.e(TAG, inicio +" "+ msj );
        //getApplicationContext()
        //LLAMAR METODO WS NUEVO DE BITACORA ERRORES
    }


    /*************************************** FIN INSULINA ******************************************/


    /*************************************** INICIO ESTADO A. ******************************************/
    public void stateData() {
        if (isOnline())
            selectDataStateDB();
    }

    public void selectDataStateDB() {
        List<IState> rowsIState;
        Log.e(TAG, "selectDataEstadoAnimoDB");
        try {
            //validar si en la tabla ahi datos mayor a 0
            if (Utils.GetStateNotSendFromDatabase(HealthMonitorApplicattion.getApplication().getStateDao(), sendServer).size() > 0) {
                //asignamos datos de la tabla a la lista de objeto
                rowsIState = Utils.GetStateNotSendFromDatabase(HealthMonitorApplicattion.getApplication().getStateDao(), sendServer);
                //obtenemos el tamaño de la listaDEobjetos , para recorrerlo
                //y presenta los datos de la tabla bd en el LOG
                int tamaño = rowsIState.size();
                for (int i = 0; i < tamaño; i++) {
                    Log.e(TAG, "EstadoAnimo" + "fecha:" + rowsIState.get(i).getFecha());
                }

                sendWSState(rowsIState);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void sendWSState(List<IState> rowsState) {

        try {
            for (IState state : rowsState) {
                restartLoadingEnviarDataState(state);
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private void restartLoadingEnviarDataState(IState state) {
        Log.e(TAG, "EstadoAnimo");
        final int idState = state.getId();
        int id_estado_animo = state.getIdStatus();
        //String fecha = state.getFecha().substring(6, 10) + "/" + state.getFecha().substring(3, 5) + "/" + state.getFecha().substring(0, 2) + " " + state.getHora();
        String fecha = state.getFecha().replace("/","-") + " " + state.getHora();
        String observacion = state.getObservacion();

        Log.e(TAG, "EstadoAnimo:"+id_estado_animo +"*fecha:"+ fecha +"*observacion:"+ observacion);

        if(state.getOperationDb().equals(operacionI)) {
            IV2RegistreState ConsulState = HealthMonitorApplicattion.getApplication().getRetrofitAdapter().create(IV2RegistreState.class);
            call_2 = ConsulState.setSendStateFrom(new rowV2State(Email, id_estado_animo, fecha, observacion));
            call_2.enqueue(new Callback<IV2RegistreState.RegistroState>() {
                @Override
                public void onResponse(Call<IV2RegistreState.RegistroState> call, Response<IV2RegistreState.RegistroState> response) {
                    if (response.isSuccessful()) {
                        Log.e(TAG, "EstadoAnimo Respuesta exitosa: response true");
                        mRegistroState = null;
                        mRegistroState = response.body();
                        ExecutionEnviarData(idState);
                    } else {
                        String ms = "Error en la peticion: response false";
                        bitacoraErrorState(ms);
                        Log.e(TAG, ms);
                    }
                }

                @Override
                public void onFailure(Call<IV2RegistreState.RegistroState> call, Throwable t) {
                    String ms = "EstadoAnimo Error en la peticion:  onFailure";
                    bitacoraErrorState(ms);
                    Log.e(TAG, ms);
                    t.printStackTrace();
                }
            });
        }else if(state.getOperationDb().equals(operacionU)) {
            IV2RegistreState ConsulState = HealthMonitorApplicattion.getApplication().getRetrofitAdapter().create(IV2RegistreState.class);
            call_2 = ConsulState.setSendStateUpdateFrom(new rowV2StateUpdate(state.getIdBdServer(), id_estado_animo, fecha, observacion));
            call_2.enqueue(new Callback<IV2RegistreState.RegistroState>() {
                @Override
                public void onResponse(Call<IV2RegistreState.RegistroState> call, Response<IV2RegistreState.RegistroState> response) {
                    if (response.isSuccessful()) {
                        Log.e(TAG, "EstadoAnimo Respuesta exitosa: response true");
                        mRegistroState = null;
                        mRegistroState = response.body();
                        ExecutionEnviarData(idState);
                    } else {
                        String ms = "Error en la peticion: response false";
                        Log.e(TAG, "EstadoAnimo" + ms);
                        bitacoraErrorState(ms);

                    }
                }

                @Override
                public void onFailure(Call<IV2RegistreState.RegistroState> call, Throwable t) {
                    String ms = "Error en la peticion:  onFailure";
                    Log.e(TAG, "EstadoAnimo" + ms);
                    bitacoraErrorState(ms);
                    t.printStackTrace();
                }
            });


        }

    }

    public void ExecutionEnviarData(int idState) {
        String msj1 = "Error: Object is null";
        String msj2 = "Error: codigo != 0";
        if (mRegistroState != null) {
            if (mRegistroState.getIdCodResult() == 0) {
                updateDataStateDB(idState, mRegistroState.getIdRegisterDB());
                Log.e(TAG, "EstadoAnimo send server ok Estado de Animo");
            } else
                bitacoraErrorState(msj2);
        } else
            bitacoraErrorState(msj1);
    }

    public void ExecutionEnviarDataUpdate(int idState) {
        String msj1 = "Error: Object is null";
        String msj2 = "Error: codigo != 0";
        if (mRegistroState != null) {
            if (mRegistroState.getIdCodResult() == 0) {
                updateDataStateDB2(idState);
                Log.e(TAG, "EstadoAnimo send server ok Estado de Animo");
            } else
                bitacoraErrorState(msj2);
        } else
            bitacoraErrorState(msj1);
    }

    public void updateDataStateDB(int idAuxState, int IdRegisterDB) {
        try {
            //setear datos al objeto y guardar y BD
            Utils.sendOkUpdateStateFromDatabase(idAuxState,
                                                IdRegisterDB,
                                                sendServerOK,
                                                HealthMonitorApplicattion.getApplication().getStateDao());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateDataStateDB2(int idAuxState) {
        try {
            //setear datos al objeto y guardar y BD
            Utils.sendOkUpdateStateFromDatabase2(idAuxState,
                                                sendServerOK,
                                                HealthMonitorApplicattion.getApplication().getStateDao());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void bitacoraErrorState(String msj) {
        String inicio = "EstadoAnimo";
        Log.e(TAG, inicio + " " + msj);
        //getApplicationContext()
        //LLAMAR METODO WS NUEVO DE BITACORA ERRORES
    }

    /*************************************** FIN ESTADO A. ******************************************/

    /*************************************** INICIO COLESTEROL KETONE ******************************************/
    public void CholesterolData() {
        if (isOnline())
            selectDataCholesterolDB();
    }

    public void selectDataCholesterolDB() {
        List<IColesterol> rowsChol;
        try {

            if (Utils.GetColesterolNotSendFromDatabase(HealthMonitorApplicattion.getApplication().getColesterolDao(), sendServer).size() > 0) {
                rowsChol = Utils.GetColesterolNotSendFromDatabase(HealthMonitorApplicattion.getApplication().getColesterolDao(), sendServer);
                int tamaño = rowsChol.size();
                sendWSCholesterol(rowsChol);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void sendWSCholesterol(List<IColesterol> rowsChol) {
        try {
            for (IColesterol chol : rowsChol) {
                restartLoadingEnviarData(chol);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private void restartLoadingEnviarData(final IColesterol cholesterol) {
        final int idCholesterol = cholesterol.getId();
        float colesterol = cholesterol.getColesterol();
        float trigliceridos = cholesterol.getTriglycerides();
        float hdl = cholesterol.getHdl();
        float ldl = cholesterol.getLdl();
        String observacion = cholesterol.getObservacion();
        String fecha = cholesterol.getFecha().replace("/","-") + " " + cholesterol.getHora();
        //String fecha = cholesterol.getFecha().substring(6, 10) + "/" + cholesterol.getFecha().substring(3, 5) + "/" + cholesterol.getFecha().substring(0, 2) + " " + cholesterol.getHora();


        try{
            if(cholesterol.getOperacion().equals(operacionI)) {

                IV2Cholesterol CrtPacientCholesterol = HealthMonitorApplicattion.getApplication().getRetrofitAdapter().create(IV2Cholesterol.class);
                call_5 = CrtPacientCholesterol.setSendCholesterolFrom(new rowV2Cholesterol(Email,  colesterol,  trigliceridos, hdl, ldl,  fecha, observacion ));
                call_5.enqueue(new Callback<IV2Cholesterol.Cholesterol>() {
                    @Override
                    public void onResponse(Call<IV2Cholesterol.Cholesterol> call, Response<IV2Cholesterol.Cholesterol> response) {
                        if (response.isSuccessful()) {
                            mCholesterol = null;
                            mCholesterol = response.body();
                            postExecutionEnviarDataCho(idCholesterol);
                        } else {
                            String msj = "Error en la petición: response false";
                            bitacoraErrorCholesterol(msj);
                        }
                    }
                    @Override
                    public void onFailure(Call<IV2Cholesterol.Cholesterol> call, Throwable t) {
                        String msj = "Adrian - Error en la petición: onFailure";
                        bitacoraErrorCholesterol(msj);
                        t.printStackTrace();
                    }
                });
            }else if (cholesterol.getOperacion().equals(operacionU)){
                //ACTUALIZAR


                IV2Cholesterol CrtPacientCholesterol = HealthMonitorApplicattion.getApplication().getRetrofitAdapter().create(IV2Cholesterol.class);
                call_5=CrtPacientCholesterol.setSendCholesterolupdateFrom(new rowV2CholesterolUpdate(cholesterol.getIdBdServer(),  colesterol, trigliceridos, hdl, ldl,  fecha, observacion));
                call_5.enqueue(new Callback<IV2Cholesterol.Cholesterol>() {
                    @Override



                    public void onResponse(Call<IV2Cholesterol.Cholesterol> call, Response<IV2Cholesterol.Cholesterol> response) {
                        if (response.isSuccessful()) {
                            mCholesterol = null;
                            mCholesterol = response.body();
                            postExecutionEnviarDataChoreUpdate(idCholesterol);
                        } else {
                            String msj = "Error en la petición: response false - Actualizar";
                            bitacoraErrorCholesterol(msj);
                        }
                    }

                    @Override
                    public void onFailure(Call<IV2Cholesterol.Cholesterol> call, Throwable t) {
                        String msj = "Error en la petición: onFailure - Actualizar";
                        bitacoraErrorCholesterol(msj);
                        t.printStackTrace();
                    }
                });

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void postExecutionEnviarDataCho(int idCholesterol) {
        String msj1 = "Error: Object is null";
        String msj2 = "Error: codigo != 0";
        if (mCholesterol != null) {
            if (mCholesterol.getIdCodResult() == 0) {
                updateDataCholesterolDB(idCholesterol,mCholesterol.getIdRegisterDB());
                Log.e(TAG, "Send server ok Cholesterol ");
            } else
                bitacoraErrorCholesterol(msj2+" "+mCholesterol.getResultDescription());
        } else
            bitacoraErrorCholesterol(msj1);
    }

    public void postExecutionEnviarDataChoreUpdate(int idCholesterol) {
        String msj1 = "Error: Object is null";
        String msj2 = "Error: codigo != 0";
        if (mCholesterol != null) {
            if (mCholesterol.getIdCodResult() == 0) {
                updateDataCholesterolDB2(idCholesterol);
                Log.e(TAG, "Fiallos - send server ok Cholesterol ");
            } else
                bitacoraErrorCholesterol(msj2+" "+mCholesterol.getResultDescription());
        } else
            bitacoraErrorCholesterol(msj1);
    }

    //ACTUALIZAR DATOS EN LA TABLA BDSQLITE ENVIADO OK TRUE
    public void updateDataCholesterolDB(int idAuxChol, int IdRegisterDB ) {
        try {
            //setear datos al objeto y guardar y BD
            Utils.sendOkUpdateCholesterolFromDatabase(idAuxChol,
                                                    IdRegisterDB,
                                                    sendServerOK,
                                                    HealthMonitorApplicattion.getApplication().getColesterolDao());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //ACTUALIZAR DATOS EN LA TABLA BDSQLITE ENVIADO OK TRUE
    public void updateDataCholesterolDB2(int idAuxChol ) {
        try {
            //setear datos al objeto y guardar y BD
            Utils.sendOkUpdateCholesterolFromDatabase2(idAuxChol,
                                                        sendServerOK,
                                                        HealthMonitorApplicattion.getApplication().getColesterolDao());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void bitacoraErrorCholesterol(String msj) {
        String inicio = "COLESTEROL";
        Log.e(TAG, inicio + " " + msj);
        //getApplicationContext()
        //LLAMAR METODO WS NUEVO DE BITACORA ERRORES
    }

    /*************************************** FIN COLESTEROL KETONE ******************************************/


    /*************************************** INICIO HBA1C *****************************************/
    public void hba1c() {
        if (isOnline())
            selectDataHba1cDB();
    }

    //obtener datos de la tabla BD
    public void selectDataHba1cDB() {
        List<IHba1c> rowsHba1c;
        Log.e(TAG, "Hba1c - SelectDataHba1cDB");
        try {

            //validar si en la tabla ahi datos mayor a 0
            if (Utils.GetHba1cNotSendFromDatabase(HealthMonitorApplicattion.getApplication().getHba1cDao(), sendServer).size() > 0) {
                Log.e(TAG, "Hba1c - Entra al modificar Hba1c");
                //asignamos datos de la tabla a la lista de objeto
                rowsHba1c = Utils.GetHba1cNotSendFromDatabase(HealthMonitorApplicattion.getApplication().getHba1cDao(), sendServer);
                //obtenemos el tamaño de la listaDEobjetos , para recorrerlo
                //y presenta los datos de la tabla bd en el LOG
                int tamaño = rowsHba1c.size();
                for (int i = 0; i < tamaño; i++) {
                    Log.e(TAG, "Hba1c:" + rowsHba1c.get(i).getConcentracion() + "/" + rowsHba1c.get(i).getCetonas() + " mm/Hg" + " -fecha:" + rowsHba1c.get(i).getFecha());//se cambia concentracion por peso
                }
                sendWSHba1c(rowsHba1c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //envia la lista al llamado ws
    public void sendWSHba1c(List<IHba1c> rowsHba1c) {
        try {
            for (IHba1c hba1 : rowsHba1c) {
                restartLoadingEnviarData(hba1);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private void restartLoadingEnviarData(final IHba1c hba1) {
        Log.e(TAG, "Hba1c - restartLoadingEnviarData");
        //obtener todos los datos del activity
        final int idHba1c = hba1.getId();
        float hba1c = hba1.getConcentracion();
        float cetona = hba1.getCetonas() ;
        String observacion = hba1.getObservacion();
        String fecha = hba1.getFecha().replace("/","-") + " " + hba1.getHora();
        //String fecha = hba1.getFecha().substring(6, 10) + "/" + hba1.getFecha().substring(3, 5) + "/" + hba1.getFecha().substring(0, 2) + " " + hba1.getHora();
        Log.e(TAG, fecha);

        try {
            if (hba1.getOperacion().equals(operacionI)) {
                IConsulHba1c CrtPacientHba1c = HealthMonitorApplicattion.getApplication().getRetrofitAdapter().create(IConsulHba1c.class);
                call_6 = CrtPacientHba1c.setSendregisterHba1cFrom(new rowV2Hba1(Email, hba1c, cetona, fecha, observacion));
                call_6.enqueue(new Callback<IConsulHba1c.Hba>() {
                    @Override
                    public void onResponse(Call<IConsulHba1c.Hba> call, Response<IConsulHba1c.Hba> response) {
                        if (response.isSuccessful()) {
                            Log.e(TAG, "Hba1c - Respuesta exitosa: response true  - Insertar");
                            mHba1c = null;
                            mHba1c = response.body();
                            postExecutionEnviarDataH(idHba1c);
                        } else {
                            String msj = "Hba1c - Error en la petición: response false - Insertar";
                            bitacoraErrorHba1c(msj);
                            Log.e(TAG, msj);
                        }
                    }
                    @Override
                    public void onFailure(Call<IConsulHba1c.Hba> call, Throwable t) {
                        String msj = "Hba1c - Error en la petición: onFailure - Insertar";
                        bitacoraErrorHba1c(msj);
                        t.printStackTrace();
                    }
                });
            }else if(hba1.getOperacion().equals(operacionU)){
                IConsulHba1c CrtPacientHba1c = HealthMonitorApplicattion.getApplication().getRetrofitAdapter().create(IConsulHba1c.class);
                call_6 = CrtPacientHba1c.setSendregisterHba1cUpdateFrom(new rowV2Hba1Update(hba1.getIdBdServer(), hba1c, cetona, fecha, observacion));
                call_6.enqueue(new Callback<IConsulHba1c.Hba>() {
                    @Override
                    public void onResponse(Call<IConsulHba1c.Hba> call, Response<IConsulHba1c.Hba> response) {
                        if (response.isSuccessful()) {
                            Log.e(TAG, "Hba1c - Respuesta exitosa: response true - Actalizar");
                            mHba1c = null;
                            mHba1c = response.body();
                            postExecutionEnviarDataHUpdate(idHba1c);
                        } else {
                            String msj = "Hba1c - Error en la petición: response false - Actalizar";
                            bitacoraErrorHba1c(msj);
                            Log.e(TAG, msj);
                        }
                    }
                    @Override
                    public void onFailure(Call<IConsulHba1c.Hba> call, Throwable t) {
                        String msj = "Hba1c - Error en la petición: onFailure - Actalizar";
                        bitacoraErrorHba1c(msj);
                        t.printStackTrace();
                    }
                });
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void postExecutionEnviarDataH(int idHba1c) {
        Log.e(TAG, "Hba1c - PostExecutionEnviarDataH - Insertar");
        String msj1 = "Error: Object is null";
        String msj2 = "Error: codigo != 0";
        if (mHba1c != null) {
            if (mHba1c.getIdCodResult()==0) {
                updateDataHba1cDB(idHba1c, mHba1c.getIdRegisterDB());
                Log.e(TAG, "Hba1c - Send server ok Hba1c - Insertar");
            } else
                bitacoraErrorHba1c(msj2);
        } else
            bitacoraErrorHba1c(msj1);
    }

    public void postExecutionEnviarDataHUpdate(int idHba1c) {
        Log.e(TAG, "Hba1c - PostExecutionEnviarDataH - Actalizar");
        String msj1 = "Error: Object is null";
        String msj2 = "Error: codigo != 0";
        if (mHba1c != null) {
            if (mHba1c.getIdCodResult()==0) {
                updateDataHba1cDB2(idHba1c);
                Log.e(TAG, "Hba1c - Send server ok Hba1c - Actualizar");
            } else
                bitacoraErrorHba1c(msj2);
        } else
            bitacoraErrorHba1c(msj1);
    }

    //ACTUALIZAR DATOS EN LA TABLA BDSQLITE ENVIADO OK TRUE
    public void updateDataHba1cDB(int idAuxHba1c, int IdRegisterDB) {
        try {
            //setear datos al objeto y guardar y BD
            Utils.sendOkUpdateHba1cFromDatabase(idAuxHba1c,
                                                IdRegisterDB,
                                                sendServerOK,
                                                HealthMonitorApplicattion.getApplication().getHba1cDao());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //ACTUALIZAR DATOS EN LA TABLA BDSQLITE ENVIADO OK TRUE
    public void updateDataHba1cDB2(int idAuxHba1c) {
        try {
            //setear datos al objeto y guardar y BD
            Utils.sendOkUpdateHba1cFromDatabase2(idAuxHba1c,
                                                sendServerOK,
                                                HealthMonitorApplicattion.getApplication().getHba1cDao());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void bitacoraErrorHba1c(String msj) {
        String inicio = "Hba1c";
        Log.e(TAG, inicio + " " + msj);
        //getApplicationContext()
        //LLAMAR METODO WS NUEVO DE BITACORA ERRORES
    }

    /*************************************** FIN HBA1C ******************************************/



    /*************************************** INICIO MEDICINA REGISTRADA******************************************/

    private void medicineUserRegistered() {
        if (isOnline())
            selectDataMedicineUserRegisteredDB();
    }

    private void selectDataMedicineUserRegisteredDB() {
        String Method = "[selectDataMedicineUserRegisteredDB]";
        List<EMedicineUser> lstEMedicineUser;
        Log.i(TAG, Method + "Init...");
        try {
            Log.i(TAG, Method + "Init getting List of Medicine User Registered to send server...");
            lstEMedicineUser = Utils.GetMedicineUserRegisteredNotSendFromDatabase(HealthMonitorApplicattion.getApplication().getMedicineUserDao(), sendServer);
            Log.i(TAG, Method + "End getting List of Medicine User Registered to send server...");
            if (lstEMedicineUser != null)
                Log.i(TAG, Method + "Registers Obtained = " + lstEMedicineUser.size());
            if (lstEMedicineUser.size() > 0) {
                for (EMedicineUser eMedicineUser : lstEMedicineUser) {
                    Log.i(TAG, Method + " EMedicineUser = " + eMedicineUser.toString());
                }
                sendWsMedicineUserRegistered(lstEMedicineUser);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Log.i(TAG, Method + "End...");
    }

    private void sendWsMedicineUserRegistered(List<EMedicineUser> lstEMedicineUser) {
        String Method = "[sendWsMedicineUserRegistered]";
        Log.i(TAG, Method + "Init...");
        try {
            for (EMedicineUser eMedicineUser : lstEMedicineUser) {
                Log.i(TAG, Method + "Calling restartLoadingSendDataWsMedicineUserRegistered...");
                restartLoadingSendDataWsMedicineUserRegistered(eMedicineUser);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        Log.i(TAG, Method + "End...");
    }

    private void restartLoadingSendDataWsMedicineUserRegistered(final EMedicineUser eMedicineUser) {
        String Method = "[restartLoadingSendDataWsMedicineUserRegistered]";
        Log.i(TAG, Method + "Init...");
        String userMail = eMedicineUser.getEmail(); // Utils.getEmailFromPreference(this);

        IConsulMedicines regMed = HealthMonitorApplicattion.getApplication().getRetrofitAdapter().create(IConsulMedicines.class);
        medicacionCall = regMed.RegMedicacion(userMail, eMedicineUser.getIdMedicacion(), eMedicineUser.getFechaRegistro());
        medicacionCall.enqueue(new Callback<IConsulMedicines.RegMedicacion>() {
            @Override
            public void onResponse(Call<IConsulMedicines.RegMedicacion> call, retrofit2.Response<IConsulMedicines.RegMedicacion> response) {
                String Method = "[restartLoadingSendDataWsMedicineUserRegistered][onResponse]";
                if (response.isSuccessful()) {
                    Log.i(TAG, Method + "Respuesta exitosa...");
                    mSaveCrtRMedicines = null;
                    mSaveCrtRMedicines = response.body();
                    Log.i(TAG, Method + "Calling postExecutionSendDataMedicineUserRegistered...");
                    postExecutionSendDataMedicineUserRegistered(eMedicineUser);
                } else {
                    String msj = "Error en la petición: response false";
                    Log.i(TAG, Method + "Calling logErrorMedicineUserRegistered...");
                    logErrorMedicineUserRegistered(msj);
                    Log.e(TAG, msj);
                }
            }

            @Override
            public void onFailure(Call<IConsulMedicines.RegMedicacion> call, Throwable t) {
                t.printStackTrace();
            }
        });
        Log.i(TAG, Method + "End...");
    }

    private void postExecutionSendDataMedicineUserRegistered(EMedicineUser eMedicineUser) {
        String Method = "[postExecutionSendDataMedicineUserRegistered]";
        Log.i(TAG, Method + "Init...");
        String msj1 = "Error: Object is null";
        String msj2 = "Error: codigo != 0";
        if (mSaveCrtRMedicines != null) {
            if (mSaveCrtRMedicines.getCodigo() == 0) {
                eMedicineUser.setIdServerDb(mSaveCrtRMedicines.getCodigo());
                updateDataMedicineUserRegisteredDB(eMedicineUser);
                Log.i(TAG, Method + "Send Server OK Medicine User Registered");
            } else
                logErrorMedicineUserRegistered(msj2 + "[ErrorCode=" + mSaveCrtRMedicines.getCodigo() + "][ErrorDescription=" + mSaveCrtRMedicines.getRespuesta() + "]");
        } else
            logErrorMedicineUserRegistered(msj1);
        Log.i(TAG, Method + "End...");

    }

    private void updateDataMedicineUserRegisteredDB(EMedicineUser eMedicineUser) {
        try {
            //setear datos al objeto y guardar y BD
            Utils.sendOkUpdateMedicineUserRegisteredFromDatabase(eMedicineUser.getId(), eMedicineUser.getIdServerDb(), sendServerOK,
                    HealthMonitorApplicattion.getApplication().getMedicineUserDao());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void logErrorMedicineUserRegistered(String msj) {
        String Method = "[logErrorMedicineUserRegistered]";
        Log.i(TAG, Method + "Init...");
        Log.i(TAG, Method + "msj=" + msj);
        //getApplicationContext()
        //LLAMAR METODO WS NUEVO DE BITACORA ERRORES
        Log.i(TAG, Method + "End...");
    }

    /*************************************** FIN MEDICINA REGISTRADA******************************************/

    /*************************************** INICIO MEDICINA CONTROL******************************************/

    private void medicineUserControl() {
        if (isOnline())
            selectDataMedicineUserControlDB();
    }

    private void selectDataMedicineUserControlDB() {
        String Method = "[selectDataMedicineUserControlDB]";
        List<IRegisteredMedicines> lstIRegisteredMedicines;
        Log.i(TAG, Method + "Init...");
        try {
            Log.i(TAG, Method + "Init getting List of Medicine User Control to send server...");
            lstIRegisteredMedicines = Utils.GetMedicineUserControlNotSendFromDatabase(HealthMonitorApplicattion.getApplication().getRegisteredMedicinesDao(), sendServer);
            Log.i(TAG, Method + "End getting List of Medicine User Control to send server...");
            if (lstIRegisteredMedicines != null)
                Log.i(TAG, Method + "Registers Obtained = " + lstIRegisteredMedicines.size());
            if (lstIRegisteredMedicines.size() > 0) {
                for (IRegisteredMedicines iRegisteredMedicines : lstIRegisteredMedicines) {
                    Log.i(TAG, Method + " IRegisteredMedicines = " + iRegisteredMedicines.toString());
                }
                sendWsMedicineUserControl(lstIRegisteredMedicines);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Log.i(TAG, Method + "End...");
    }

    private void sendWsMedicineUserControl(List<IRegisteredMedicines> lstIRegisteredMedicines) {
        String Method = "[sendWsMedicineUserControl]";
        Log.i(TAG, Method + "Init...");
        try {
            for (IRegisteredMedicines iRegisteredMedicines : lstIRegisteredMedicines) {
                Log.i(TAG, Method + "Calling restartLoadingSendDataWsMedicineUserControl...");
                restartLoadingSendDataWsMedicineUserControl(iRegisteredMedicines) ;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        Log.i(TAG, Method + "End...");

    }

    private void restartLoadingSendDataWsMedicineUserControl(final IRegisteredMedicines iRegisteredMedicines) {
        String Method = "[restartLoadingSendDataWsMedicineUserControl]";
        Log.i(TAG, Method + "Init...");

        IV2RegisterMedication regCtrlMed = HealthMonitorApplicattion.getApplication().getRetrofitAdapter().create(IV2RegisterMedication.class);
        if (iRegisteredMedicines.getOperationDb().equals(operacionI)){

            medUserCtrlCall = regCtrlMed.RegisterMedication (new IV2RegisterMedication.ObjRegisterMedication(
                            iRegisteredMedicines.getEmail(),
                            iRegisteredMedicines.getId_medicacion(),
                            iRegisteredMedicines.getDosis(),
                            iRegisteredMedicines.getReminderTypeCode(),
                            iRegisteredMedicines.getReminderTimeCode(),
                            iRegisteredMedicines.getDiasMedicacion(),
                            iRegisteredMedicines.getFechaInicio(),
                            iRegisteredMedicines.getFechaFin(),
                            iRegisteredMedicines.getConsumo_medicina()
                    )
            );

            medUserCtrlCall.enqueue(new Callback<IV2RegisterMedication.MedicationRegister>() {
                @Override
                public void onResponse(Call<IV2RegisterMedication.MedicationRegister> call, Response<IV2RegisterMedication.MedicationRegister> response) {
                    String Method = "[restartLoadingSendDataWsMedicineUserControl_Insert][onResponse]";
                    if (response.isSuccessful()) {
                        Log.i(TAG, Method + "Respuesta exitosa...");
                        mSaveUserCtrl = null;
                        mSaveUserCtrl = response.body();
                        Log.i(TAG, Method + "Calling postExecutionSendDataMedicineUserRegistered...");
                        postExecutionSendDataMedicineUserControl(iRegisteredMedicines);
                    } else {
                        String msj = "Error en la petición: response false";
                        Log.i(TAG, Method + "Calling logErrorMedicineUserRegistered...");
                        logErrorMedicineUserControl(msj);
                        Log.e(TAG, msj);
                    }
                }
                @Override
                public void onFailure(Call<IV2RegisterMedication.MedicationRegister> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        }
        else  if (iRegisteredMedicines.getOperationDb().equals(operacionU)){
            medUserCtrlUpdCall = regCtrlMed.UpdateMedication (new IV2RegisterMedication.ObjUpadateMedication  (
                            iRegisteredMedicines.getEmail(),
                            iRegisteredMedicines.getIdServerDb(),
                            iRegisteredMedicines.getId_medicacion(),
                            iRegisteredMedicines.getDosis(),
                            iRegisteredMedicines.getReminderTypeCode(),
                            iRegisteredMedicines.getReminderTimeCode(),
                            iRegisteredMedicines.getDiasMedicacion(),

                            iRegisteredMedicines.getFechaInicio(),
                            iRegisteredMedicines.getFechaFin(),
                            iRegisteredMedicines.getConsumo_medicina()
                    )
            );
//            Log.i(TAG, "restartLoadingSendDataWsMedicineUserControl:identifier " + iRegisteredMedicines.getEmail()            );
//            Log.i(TAG, "restartLoadingSendDataWsMedicineUserControl:medicationID " + iRegisteredMedicines.getIdServerDb()       );
//            Log.i(TAG, "restartLoadingSendDataWsMedicineUserControl:medicineID " + iRegisteredMedicines.getId_medicacion()    );
//            Log.i(TAG, "restartLoadingSendDataWsMedicineUserControl:doseMedicine " + iRegisteredMedicines.getDosis()            );
//            Log.i(TAG, "restartLoadingSendDataWsMedicineUserControl:frequencyType " + iRegisteredMedicines.getReminderTimeCode() );
//            Log.i(TAG, "restartLoadingSendDataWsMedicineUserControl:times " + iRegisteredMedicines.getDiasMedicacion()   );
//            Log.i(TAG, "restartLoadingSendDataWsMedicineUserControl:frequencyDescription " + iRegisteredMedicines.getReminderTypeCode() );
//            Log.i(TAG, "restartLoadingSendDataWsMedicineUserControl:startDate " + iRegisteredMedicines.getFechaInicio()      );
//            Log.i(TAG, "restartLoadingSendDataWsMedicineUserControl:endDate " + iRegisteredMedicines.getFechaFin()         );
//            Log.i(TAG, "restartLoadingSendDataWsMedicineUserControl:observations " + iRegisteredMedicines.getConsumo_medicina() );

            medUserCtrlUpdCall.enqueue(new Callback<IV2RegisterMedication.MedicationUpdate>() {
                @Override
                public void onResponse(Call<IV2RegisterMedication.MedicationUpdate> call, Response<IV2RegisterMedication.MedicationUpdate> response) {
                    String Method = "[restartLoadingSendDataWsMedicineUserControl_Update][onResponse]";
                    if (response.isSuccessful()) {
                        Log.i(TAG, Method + "Respuesta exitosa...");
                        mSaveUserUpdCtrl = null;
                        mSaveUserUpdCtrl = response.body();
                        Log.i(TAG, Method + "Calling postExecutionSendDataMedicineUserRegistered...");
                        postExecutionSendDataMedicineUserControl(iRegisteredMedicines);
                    } else {
                        String msj = "Error en la petición: response false";
                        Log.i(TAG, Method + "Calling logErrorMedicineUserRegistered...");
                        logErrorMedicineUserControl(msj);
                        Log.e(TAG, msj);
                    }
                }
                @Override
                public void onFailure(Call<IV2RegisterMedication.MedicationUpdate> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        }
        Log.i(TAG, Method + "End...");
    }

    private void postExecutionSendDataMedicineUserControl(IRegisteredMedicines iRegisteredMedicines) {
        String Method = "[postExecutionSendDataMedicineUserControl]";
        Log.i(TAG, Method + "Init...");
        String msj1 = "Error: Object is null";
        String msj2 = "Error: codigo != 0";
        if (iRegisteredMedicines.getOperationDb().equals(operacionI)){
            if (mSaveUserCtrl != null) {
                if (mSaveUserCtrl.getIdCodResult() == 0) {
                    iRegisteredMedicines.setIdServerDb(mSaveUserCtrl.getIdRegisterDB());
                    Log.i(TAG, Method + "medicineAlarm(iRegisteredMedicines.getId()) = " + iRegisteredMedicines.getId() + ", mSaveUserCtrl.getIdRegisterDB() " + mSaveUserCtrl.getIdRegisterDB());
                    medicineAlarm(iRegisteredMedicines.getId(), iRegisteredMedicines.getIdServerDb());
                    Log.i(TAG, Method + "Send Server OK Medicine User Control");
                    updateDataMedicineUserControlDB(iRegisteredMedicines);
                } else
                    logErrorMedicineUserControl(msj2 + "[ErrorCode=" + mSaveUserCtrl.getIdCodResult() + "][ErrorDescription=" + mSaveUserCtrl.getResultDescription() + "]");
            } else
                logErrorMedicineUserControl(msj1);
        }else if (iRegisteredMedicines.getOperationDb().equals(operacionU)){
                if (mSaveUserUpdCtrl != null) {
                    if (mSaveUserUpdCtrl.getIdCodResult() == 0) {
                        //iRegisteredMedicines.setIdServerDb(mSaveUserUpdCtrl.getIdRegisterDB());
                        Log.i(TAG, Method + "medicineAlarm(iRegisteredMedicines.getId()) = " + iRegisteredMedicines.getId() );
                        medicineAlarm(iRegisteredMedicines.getId(), iRegisteredMedicines.getIdServerDb());
                        Log.i(TAG, Method + "Send Server OK Medicine User Control");
                        updateDataMedicineUserControlDB(iRegisteredMedicines);
                    } else
                        logErrorMedicineUserControl(msj2 + "[ErrorCode=" + mSaveUserUpdCtrl.getIdCodResult() + "][ErrorDescription=" + mSaveUserUpdCtrl.getResultDescription() + "]");
                } else
                    logErrorMedicineUserControl(msj1);
            }

        Log.i(TAG, Method + "End...");

    }

    private void updateDataMedicineUserControlDB(IRegisteredMedicines iRegisteredMedicines) {
        String Method = "[updateDataMedicineUserControlDB]";
        Log.i(TAG, Method + "Init...");
        try {
            //setear datos al objeto y guardar y BD
            Utils.sendOkUpdateMedicineUserControlFromDatabase(iRegisteredMedicines.getId(), iRegisteredMedicines.getIdServerDb() , sendServerOK,
                    HealthMonitorApplicattion.getApplication().getRegisteredMedicinesDao());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Log.i(TAG, Method + "End...");
    }

    public void logErrorMedicineUserControl(String msj) {
        String Method = "[logErrorMedicineUserControl]";
        Log.i(TAG, Method + "Init...");
        Log.i(TAG, Method + "msj=" + msj);
        //getApplicationContext()
        //LLAMAR METODO WS NUEVO DE BITACORA ERRORES
        Log.i(TAG, Method + "End...");
    }


    /*************************************** FIN MEDICINA CONTROL******************************************/

    /*************************************** INICIO ALARMA******************************************/

    private void medicineAlarm(int registerMedicationId ,int registeredMedicinesIdServerDB) {
        if (isOnline())
            selectDataMedicineAlarmDB(registerMedicationId,registeredMedicinesIdServerDB);
    }

    private void selectDataMedicineAlarmDB(int registerMedicationId,int registeredMedicinesIdServerDB) {
        String Method = "[selectDataMedicineAlarmDB]";
        List<EAlarmDetails> listAlarmDetails;
        Log.i(TAG, Method + "Init...");
        try {
            Log.i(TAG, Method + "Init getting List of EAlarmDetails to send server with registerMedicationId = " + registerMedicationId );
            listAlarmDetails = Utils.GetRegisterAlarmMedicationByIdNotSendFromDatabase(HealthMonitorApplicattion.getApplication().getEAlarmDetailsDao(), registerMedicationId,  sendServer);
            Log.i(TAG, Method + "End getting List of EAlarmDetails to send server...");
            if (listAlarmDetails != null)
                Log.i(TAG, Method + "Registers Obtained = " + listAlarmDetails.size());
            if (listAlarmDetails.size() > 0) {
                for (EAlarmDetails alarmDetails : listAlarmDetails) {
                    Log.i(TAG, Method + " alarmDetails = " + alarmDetails.toString());
                }
                sendWsMedicineAlarm(listAlarmDetails,registeredMedicinesIdServerDB);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Log.i(TAG, Method + "End...");
    }

    private void sendWsMedicineAlarm(List<EAlarmDetails> listAlarmDetails,int registeredMedicinesIdServerDB) {
        String Method = "[sendWsMedicineAlarm]";
        Log.i(TAG, Method + "Init...");
        try {
            for (EAlarmDetails alarmDetails : listAlarmDetails) {
                Log.i(TAG, Method + "Calling restartLoadingSendDataWsMedicineAlarm...");
                restartLoadingSendDataWsMedicineAlarm(alarmDetails,registeredMedicinesIdServerDB);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        Log.i(TAG, Method + "End...");

    }

    private void restartLoadingSendDataWsMedicineAlarm(final EAlarmDetails alarmDetails,final int registeredMedicinesIdServerDB) {
        String Method = "[restartLoadingSendDataWsMedicineAlarm]";
        Log.i(TAG, Method + "Init...");
        // getRestCISCAdapterV2
        IV2RegisterAlarmMedication regCtrlMed = HealthMonitorApplicattion.getApplication().getRetrofitAdapter().create(IV2RegisterAlarmMedication.class);
        Log.i(TAG, Method + "H=" + alarmDetails.getAlarmDetailHour().substring(0,5) );
        Log.i(TAG, Method + "F=" + alarmDetails.getAlarmDetailCreateDate()  .substring(0,10) );
        medAlarmCall = regCtrlMed.registerAlarmMedication (new IV2RegisterAlarmMedication.ObjRegisterAlarmMedication (
                alarmDetails.getEmail(),registeredMedicinesIdServerDB
                ,alarmDetails.getAlarmDetailHour().substring(0,5)
                ,alarmDetails.getAlarmDetailCreateDate().substring(0,10)   ,"")
        );

        medAlarmCall.enqueue(new Callback<IV2RegisterAlarmMedication.registerAlarmMedication>() {
            @Override
            public void onResponse(Call<IV2RegisterAlarmMedication.registerAlarmMedication> call, Response<IV2RegisterAlarmMedication.registerAlarmMedication> response) {
                String Method = "[restartLoadingSendDataWsMedicineAlarm][onResponse]";
                if (response.isSuccessful()) {
                    Log.i(TAG, Method + "Respuesta exitosa...");
                    mSaveMedAlarm = null;
                    mSaveMedAlarm = response.body();
                    Log.i(TAG, Method + "Calling postExecutionSendDataMedicineAlarm...");
                    postExecutionSendDataMedicineAlarm(alarmDetails);
                } else {
                    String msj = "Error en la petición: response false";
                    Log.i(TAG, Method + "Calling logErrorMedicineAlarm...");
                    logErrorMedicineAlarm(msj);
                    Log.e(TAG, msj);
                }
            }
            @Override
            public void onFailure(Call<IV2RegisterAlarmMedication.registerAlarmMedication> call, Throwable t) {
                t.printStackTrace();
            }
        });
        Log.i(TAG, Method + "End...");
    }

    private void postExecutionSendDataMedicineAlarm(EAlarmDetails alarmDetails) {
        String Method = "[postExecutionSendDataMedicineAlarm]";
        Log.i(TAG, Method + "Init...");
        String msj1 = "Error: Object is null";
        String msj2 = "Error: codigo != 0";
        if (mSaveMedAlarm != null) {
            if (mSaveMedAlarm.getIdCodResult() == 0) {
                alarmDetails.setIdServerDb(mSaveMedAlarm.getIdRegisterDB());
                Log.i(TAG, Method + "Send Server OK EAlarmDetails");
                updateDataMedicineAlarmDB(alarmDetails);
            } else
                logErrorMedicineAlarm(msj2 + "[ErrorCode=" + mSaveMedAlarm.getIdCodResult() + "][ErrorDescription=" + mSaveMedAlarm.getResultDescription() + "]");
        } else
            logErrorMedicineAlarm(msj1);
        Log.i(TAG, Method + "End...");

    }

    private void updateDataMedicineAlarmDB(EAlarmDetails alarmDetails) {
        String Method = "[updateDataMedicineAlarmDB]";
        Log.i(TAG, Method + "Init...");
        try {
            //setear datos al objeto y guardar y BD
            Utils.sendOkUpdateMedicineAlarmFromDatabase(
                    alarmDetails.getAlarmDetailId() ,
                    alarmDetails.getIdServerDb(),
                    sendServerOK,
                    HealthMonitorApplicattion.getApplication().getEAlarmDetailsDao()
                    );
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Log.i(TAG, Method + "End...");
    }

    public void logErrorMedicineAlarm(String msj) {
        String Method = "[logErrorMedicineAlarm]";
        Log.i(TAG, Method + "Init...");
        Log.i(TAG, Method + "msj=" + msj);
        //getApplicationContext()
        //LLAMAR METODO WS NUEVO DE BITACORA ERRORES
        Log.i(TAG, Method + "End...");
    }

    /*************************************** FIN ALARMA******************************************/

    /*************************************** INICIO TOMAR MEDICINA******************************************/
    private void alarmTakeMedicine() {
        if (isOnline())
            selectDataAlarmTakeMedicineDB();
    }

    private void selectDataAlarmTakeMedicineDB() {
        String Method = "[selectDataAlarmTakeMedicineDB]";
        List<EAlarmTakeMedicine> listAlarmTakeMedicine;
        Log.i(TAG, Method + "Init...");
        try {
            Log.i(TAG, Method + "Init getting List of alarmTakeMedicine to send server " );
            listAlarmTakeMedicine = Utils.GetRegisterAlarmTakeMedicineNotSendFromDatabase(HealthMonitorApplicattion.getApplication().getEAlarmTakeMedicineDao()  , sendServer);
            Log.i(TAG, Method + "End getting List of alarmTakeMedicine to send server...");
            if (listAlarmTakeMedicine != null)
                Log.i(TAG, Method + "Registers Obtained = " + listAlarmTakeMedicine.size());
            if (listAlarmTakeMedicine.size() > 0) {
                for (EAlarmTakeMedicine alarmTakeMedicine : listAlarmTakeMedicine) {
                    Log.i(TAG, Method + " alarmTakeMedicine = " + alarmTakeMedicine.toString());
                }
                sendWsAlarmTakeMedicine(listAlarmTakeMedicine);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Log.i(TAG, Method + "End...");
    }

    private void sendWsAlarmTakeMedicine(List<EAlarmTakeMedicine> listAlarmTakeMedicine) {
        String Method = "[sendWsAlarmTakeMedicine]";
        Log.i(TAG, Method + "Init...");
        try {
            for (EAlarmTakeMedicine alarmTakeMedicine : listAlarmTakeMedicine) {
                Log.i(TAG, Method + "Calling restartLoadingSendDataWsAlarmTakeMedicine...");
                restartLoadingSendDataWsAlarmTakeMedicine(alarmTakeMedicine);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        Log.i(TAG, Method + "End...");
    }

    private void restartLoadingSendDataWsAlarmTakeMedicine(final EAlarmTakeMedicine alarmTakeMedicine) {
        String Method = "[restartLoadingSendDataWsAlarmTakeMedicine]";
        Log.i(TAG, Method + "Init...");
        IV2RegisterAlarmTakeMedicine regAlarmTakeMedicine = HealthMonitorApplicattion.getApplication().getRetrofitAdapter().create(IV2RegisterAlarmTakeMedicine.class);

        medAlarmTakeCall = regAlarmTakeMedicine.registerAlarmTakeMedicine (new IV2RegisterAlarmTakeMedicine.ObjRegisterAlarmTakeMedicine(
                alarmTakeMedicine.getAlarmDetailId()
                ,alarmTakeMedicine.getRegisteredMedicinesId()
                ,"S"
                ,alarmTakeMedicine.getAlarmTakeMedicineDate()
                ,""
                  )
        );

        medAlarmTakeCall.enqueue(new Callback<IV2RegisterAlarmTakeMedicine.registerAlarmTakeMedicine>() {
            @Override
            public void onResponse(Call<IV2RegisterAlarmTakeMedicine.registerAlarmTakeMedicine> call, Response<IV2RegisterAlarmTakeMedicine.registerAlarmTakeMedicine> response) {
                String Method = "[restartLoadingSendDataWsAlarmTakeMedicine][onResponse]";
                if (response.isSuccessful()) {
                    Log.i(TAG, Method + "Respuesta exitosa...");
                    mSaveAlarmTake = null;
                    mSaveAlarmTake = response.body();
                    Log.i(TAG, Method + "Calling postExecutionSendDataAlarmTakeMedicine...");
                    postExecutionSendDataAlarmTakeMedicine(alarmTakeMedicine);
                } else {
                    String msj = "Error en la petición: response false";
                    Log.i(TAG, Method + "Calling logErrorMedicineAlarm...");
                    logErrorAlarmTakeMedicine(msj);
                    Log.e(TAG, msj);
                }
            }
            @Override
            public void onFailure(Call<IV2RegisterAlarmTakeMedicine.registerAlarmTakeMedicine> call, Throwable t) {
                t.printStackTrace();
            }
        });
        Log.i(TAG, Method + "End...");
    }

    private void postExecutionSendDataAlarmTakeMedicine(EAlarmTakeMedicine alarmTakeMedicine) {
        String Method = "[postExecutionSendDataAlarmTakeMedicine]";
        Log.i(TAG, Method + "Init...");
        String msj1 = "Error: Object is null";
        String msj2 = "Error: codigo != 0";
        if (mSaveAlarmTake != null){
            if (mSaveAlarmTake.getIdCodResult() == 0) {
                alarmTakeMedicine.setIdServerDb (mSaveAlarmTake.getIdRegisterDB());
                Log.i(TAG, Method + "Send Server OK alarmTakeMedicine");
                updateDataAlarmTakeMedicineDB(alarmTakeMedicine);
            } else
                logErrorAlarmTakeMedicine(msj2 + "[ErrorCode=" + mSaveAlarmTake.getIdCodResult() + "][ErrorDescription=" + mSaveAlarmTake.getResultDescription() + "]");
        } else
            logErrorAlarmTakeMedicine(msj1);
        Log.i(TAG, Method + "End...");
    }

    private void updateDataAlarmTakeMedicineDB(EAlarmTakeMedicine alarmTakeMedicine) {
        String Method = "[updateDataAlarmTakeMedicineDB]";
        Log.i(TAG, Method + "Init...");
        try {
            //setear datos al objeto y guardar y BD
            Utils.sendOkUpdateAlarmTakeMedicineFromDatabase(alarmTakeMedicine.getAlarmTakeMedicineId() , alarmTakeMedicine.getIdServerDb(), sendServerOK,
                    HealthMonitorApplicattion.getApplication().getEAlarmTakeMedicineDao());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Log.i(TAG, Method + "End...");
    }

    public void logErrorAlarmTakeMedicine(String msj) {
        String Method = "[logErrorAlarmTakeMedicine]";
        Log.i(TAG, Method + "Init...");
        Log.i(TAG, Method + "msj=" + msj);
        //getApplicationContext()
        //LLAMAR METODO WS NUEVO DE BITACORA ERRORES
        Log.i(TAG, Method + "End...");
    }

    /*************************************** FIN TOMAR MEDICINA******************************************/
    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    @Override
    public void onDestroy() {
        timerTask.cancel();
        Log.e(TAG, "Servicio destruido...");
    }
}
