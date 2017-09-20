package com.grupocisc.healthmonitor;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.crashlytics.android.Crashlytics;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.places.internal.ScannerException;
import com.grupocisc.healthmonitor.database.Database;
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
import com.grupocisc.healthmonitor.entities.IFeeding;
import com.grupocisc.healthmonitor.entities.IDoctor;
import com.grupocisc.healthmonitor.entities.IDiet;


import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.twitter.sdk.android.core.GuestSession;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.TwitterSession;

import java.sql.SQLException;

import io.fabric.sdk.android.Fabric;
import retrofit2.converter.gson.GsonConverterFactory;
//import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;


public class HealthMonitorApplicattion extends Application {

    private static HealthMonitorApplicattion instance;
    private Retrofit mRestAdapter;
    private Retrofit mRestCISCAdapter;
	private Retrofit mRestCISCAdapterp;								   
    private Retrofit mRestPushAdapter;
    private Retrofit mRestCISCAdapterP;
    private Retrofit getmRestCISCReconAdapter;
    private Retrofit mRestCISCAdapterAnimo;
	private Retrofit mRestPushAdapterRecpre;
    private Retrofit mRestCISCAdapterV2;
    private Retrofit mRestCISCAdapterV2IP;
	
    private Database databaseHelper = null;
    //Notifications DATABASE
    private Dao<IGlucose, Integer> Glucose = null;
    private Dao<INotifcationsMedical, Integer> NotifcationsMedical = null;
    private Dao<IWeight, Integer> Weight = null;
    private Dao<IPressure, Integer> Pressure = null;
    private Dao<IState, Integer> State = null;
    private Dao<IDoctor, Integer> Doctor = null;
    private Dao<EInsulin, Integer> Insulin = null;
    private Dao<IMedicines, Integer> Medicines = null;
    private Dao<EMedicine, Integer> Medicine = null;
    private Dao<EMedicineUser, Integer> MedicineUser = null;
    private Dao<IPulse, Integer> Pulse = null;
    private Dao<IRegisteredMedicines, Integer> RegisteredMedicines= null;
    private Dao<IRoutineCheckLesson, Integer> checkLesson = null;
    private Dao<IDisease, Integer> Disease= null;
    private Dao<IFeeding, Integer> feedings = null;
    private Dao<IDiet, Integer> iDiets = null;
    private Dao<IAsthma, Integer> Asthma=null;

    private Dao<IColesterol, Integer> Colesterol = null;
    private Dao<ITrigliceros, Integer> Trigliceros = null;
    private Dao<IHba1c, Integer> Hba1c = null;
    private Dao<IKetone, Integer> Ketone = null;
    private Dao<EMedicineType, Integer> EMedicineType = null;
    private Dao<EAlarmReminderType, Integer> EAlarmReminderType = null;
    private Dao<EAlarmReminderTime, Integer> EAlarmReminderTime = null;
    private Dao<EAlarmDetails, Integer> EAlarmDetails = null;
    private Dao<EAlarmTakeMedicine, Integer> EAlarmTakeMedicine = null;
    private static HealthMonitorApplicattion mInstance;

    private static final String TWITTER_KEY = BuildConfig.CONSUMER_KEY;
    private static final String TWITTER_SECRET = BuildConfig.CONSUMER_SECRET;
    public GuestSession guestAppSession = null;

    @Override
    public void onCreate() {
        super.onCreate();

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);


        Fabric.with(this, new Crashlytics());

        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        TwitterConfig configg = new TwitterConfig.Builder(this).twitterAuthConfig(authConfig).build();
        Twitter.initialize(configg);





        databaseHelper = new Database(this);

        mRestAdapter = new Retrofit.Builder()
                .baseUrl(getString(R.string.base_path))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mRestCISCAdapter = new Retrofit.Builder()
                .baseUrl(getString(R.string.base_path_cisc))
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        mRestCISCAdapterP = new Retrofit.Builder()
                .baseUrl(getString(R.string.base_path_procesos))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        getmRestCISCReconAdapter = new Retrofit.Builder()
                .baseUrl(getString(R.string.base_path_recomendacion))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mRestCISCAdapterAnimo = new Retrofit.Builder()
                .baseUrl(getString(R.string.base_path_cisc_animo))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mRestPushAdapterRecpre = new Retrofit.Builder()
                .baseUrl(getString(R.string.base_path_ciscrepp))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        //inicio nuevos metodos v2
        mRestCISCAdapterV2 = new Retrofit.Builder()
                .baseUrl(getString(R.string.base_path_v2))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mRestCISCAdapterV2IP  = new Retrofit.Builder()
                .baseUrl(getString(R.string.base_path_v2_ip))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        //fin nuevos metodos v2



        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisc(true).build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(options).build();
        ImageLoader.getInstance().init(config);


        instance=this;
    }
    public GuestSession getGuestAppSession()   {
        return guestAppSession;
    }

    public void setGuestAppSession(GuestSession guestAppSession) {
        this.guestAppSession = guestAppSession;
    }

    /**
     * Rest Adapter
     * @return
     */


    public Retrofit getRestCISCAdapterV2() {
        return this.mRestCISCAdapterV2;
    }

    public Retrofit getRestCISCAdapterV2IP() {
        return this.mRestCISCAdapterV2IP;
    }


    public Retrofit getRestAdapter() {
        return this.mRestAdapter;
    }

    public Retrofit getmRestCISCAdapter() {
        return this.mRestCISCAdapter;
    }

    public Retrofit getmRestCISCAdapterP() {
        return this.mRestCISCAdapterP;
    }


    public Retrofit getmRestCISCReconAdapter() {
        return this.getmRestCISCReconAdapter;
    }

    public Retrofit getmRestPushAdapter() {
        return this.mRestPushAdapter;
    }
    public Retrofit getmRestCISCAdapterAnimo() {
        return this.mRestCISCAdapterAnimo;
	 } //CAMBIO
	public Retrofit getmRestPushAdapterRecpre() {
        return this.mRestPushAdapterRecpre;
    }
    //INIICO GLUCOSA
    public Dao<IGlucose, Integer> getGlucoseDao() throws SQLException {
        if (Glucose == null) {
            Glucose = databaseHelper.getDao(IGlucose.class);
        }
        return Glucose;
    }
    public  void CleanGlucose() throws SQLException {
        databaseHelper.CleanGlucose();
    }
    //FIN GLUCOSA
//INICO PULSO
    public Dao<IPulse, Integer> getPulseDao() throws SQLException {
        if (Pulse == null) {
            Pulse = databaseHelper.getDao(IPulse.class);
        }
        return Pulse;
    }
    public  void CleanPulse() throws SQLException {
        databaseHelper.CleanPulse();
    }
    //FIN PULSO
    //INICIO NOTIFICATION MEDICAL
    public Dao<INotifcationsMedical, Integer> getNotifcationsMedicalDao() throws SQLException {
        if (NotifcationsMedical == null) {
            NotifcationsMedical = databaseHelper.getDao(INotifcationsMedical.class);
        }
        return NotifcationsMedical;
    }
    public  void CleanNotifcationsMedical() throws SQLException {
        databaseHelper.CleanNotifcationsMedical();
    }
    //FIN NOTIFICATION MEDICAL
    //INICIO MARIUXI
    public Dao<IWeight, Integer> getWeightDao() throws SQLException {
        if (Weight == null) {
            Weight = databaseHelper.getDao(IWeight.class);
        }
        return Weight;
    }

    public Dao<IFeeding, Integer> getIFeedingDao() throws SQLException {
        if (feedings == null) {
            feedings = databaseHelper.getDao(IFeeding.class);
        }
        return feedings;
    }

    public  void CleanWeight() throws SQLException {
        databaseHelper.CleanWeight();
    }
    public Dao<IPressure, Integer> getPressureDao() throws SQLException {
        if (Pressure == null) {
            Pressure = databaseHelper.getDao(IPressure.class);
        }
        return Pressure;
    }
    public  void CleanPressure() throws SQLException {
        databaseHelper.CleanPressure();
    }
    //FIN MARIUXI
    public Dao<IState, Integer> getStateDao() throws SQLException{
        if (State == null ){
            State = databaseHelper.getDao(IState.class);
        }
        return State;
    }

    //ASTHMA
    public Dao<IAsthma, Integer> getAsthmaDao() throws SQLException{
        if(Asthma== null){
            Asthma= databaseHelper.getDao(IAsthma.class);
        }
        return Asthma;
    }

    public Dao<IDoctor, Integer> getDoctorDao() throws SQLException{
        if (Doctor == null ){
            Doctor = databaseHelper.getDao(IDoctor.class);
        }
        return Doctor;
    }

    public  void CleanState() throws SQLException {
        databaseHelper.CleanState();
    }

    public Dao<EInsulin, Integer> getInsulinDao() throws SQLException {
        if (Insulin == null) {
            Insulin = databaseHelper.getDao(EInsulin.class);
        }
        return Insulin;
    }
    public  void CleanInsulin() throws SQLException {
        databaseHelper.CleanInsulin();
    }

    public Dao<IMedicines, Integer> getMedicinesDao() throws SQLException {
        if (Medicines == null) {
            Medicines = databaseHelper.getDao(IMedicines.class);
        }
        return Medicines;
    }

    public Dao<EMedicine, Integer> getMedicineDao() throws SQLException {
        if (Medicine == null) {
            Medicine = databaseHelper.getDao(EMedicine.class);
        }
        return Medicine;
    }

    public Dao<EMedicineUser, Integer> getMedicineUserDao() throws SQLException {
        if (MedicineUser == null) {
            MedicineUser = databaseHelper.getDao(EMedicineUser.class );
        }
        return MedicineUser;
    }

    public void CleanMedicine() throws SQLException{
        databaseHelper.CleanMedicine();
    }

    public void CleanMedicines() throws SQLException {
        databaseHelper.CleanMedicines();
    }

    public Dao<IRegisteredMedicines, Integer> getRegisteredMedicinesDao() throws SQLException {
        if (RegisteredMedicines == null) {
            RegisteredMedicines = databaseHelper.getDao(IRegisteredMedicines.class);
        }
        return RegisteredMedicines ;
    }
    public  void CleanRegisteredMedicines() throws SQLException {
        databaseHelper.CleanRegisteredMedicines();
    }
    //REGOISTRO DE Disease
    public Dao<IDisease, Integer> getDiseaseDao() throws SQLException {
        if (Disease == null) {
            Disease = databaseHelper.getDao(IDisease.class);
        }
        return Disease;
    }

    public  void CleanRegisteredDisease() throws SQLException {
        databaseHelper.CleanRegisteredDisease();
    }

    public Dao<IAsthma, Integer> getAsthma()throws SQLException{
        if(Asthma==null){
            Asthma=databaseHelper.getDao(IAsthma.class);
        }
        return Asthma;
    }
    public void CleanRegisteredAsthma() throws ScannerException, SQLException {
        databaseHelper.CleanAsthma();
    }
//FIN DE Disease

    //Rutina
    public Dao<IRoutineCheckLesson, Integer> getCheckLesson() throws SQLException {
        if (checkLesson == null) {
            checkLesson = databaseHelper.getDao(IRoutineCheckLesson.class);
        }
        return checkLesson;

    }

    public Dao<IDiet, Integer> getiDiets() throws SQLException {
        if (iDiets == null) {
            iDiets = databaseHelper.getDao(IDiet.class);
        }
        return iDiets;
    }

    //Complementarios
    // colesterol

    public Dao<IColesterol, Integer> getColesterolDao() throws SQLException {
        if (Colesterol == null) {
            Colesterol = databaseHelper.getDao(IColesterol.class);
        }
        return Colesterol;
    }
    public  void CleanColesterol() throws SQLException {
        databaseHelper.CleanColesterol();
    }
    //trigliceridos

    public Dao<ITrigliceros, Integer> getTriglicerosDao() throws SQLException {
        if (Trigliceros == null) {
            Trigliceros = databaseHelper.getDao(ITrigliceros.class);
        }
        return Trigliceros;
    }
    public  void CleanTrigliceros() throws SQLException {
        databaseHelper.CleanTrigliceros();
    }

    // Hba1c

    public Dao<IHba1c, Integer> getHba1cDao() throws SQLException {
        if (Hba1c == null) {
            Hba1c = databaseHelper.getDao(IHba1c.class);
        }
        return Hba1c;
    }
    public  void CleanHba1c() throws SQLException {
        databaseHelper.CleanHba1c();
    }
// Ketone

    public Dao<IKetone, Integer> getKetoneDao() throws SQLException {
        if (Ketone == null) {
            Ketone = databaseHelper.getDao(IKetone.class);
        }
        return Ketone;
    }
    public  void CleanKetone() throws SQLException {
        databaseHelper.CleanKetone();
    }

    //EMedicineType
    public Dao<EMedicineType, Integer> getEMedicineTypeDao() throws SQLException {
        if (EMedicineType == null) {
            EMedicineType = databaseHelper.getDao(EMedicineType.class);
        }
        return EMedicineType;
    }

    public  void CleanEMedicineType() throws SQLException {
        databaseHelper.CleanEMedicineType();
    }

    //EAlarmReminderType
    public Dao<EAlarmReminderType, Integer> getEAlarmReminderTypeDao() throws SQLException {
        if (EAlarmReminderType == null) {
            EAlarmReminderType = databaseHelper.getDao(EAlarmReminderType.class);
        }
        return EAlarmReminderType;
    }

    //EAlarmReminderType
    public Dao<EAlarmReminderTime, Integer> getEAlarmReminderTimeDao() throws SQLException {
        if (EAlarmReminderTime == null) {
            EAlarmReminderTime = databaseHelper.getDao(EAlarmReminderTime.class);
        }
        return EAlarmReminderTime;
    }

    public void CleanEAlarmReminderType() throws SQLException {
        databaseHelper.CleanEAlarmReminderType();
    }

    //EAlarmDetails
    public Dao<EAlarmDetails, Integer> getEAlarmDetailsDao() throws SQLException {
        if (EAlarmDetails == null) {
            EAlarmDetails = databaseHelper.getDao(EAlarmDetails.class);
        }
        return EAlarmDetails;
    }

    public void CleanEAlarmDetails() throws SQLException {
        databaseHelper.CleanEAlarmDetails();
    }

    //EAlarmTakeMedicine
    public Dao<EAlarmTakeMedicine, Integer> getEAlarmTakeMedicineDao() throws SQLException {
        if (EAlarmTakeMedicine == null) {
            EAlarmTakeMedicine = databaseHelper.getDao(EAlarmTakeMedicine.class);
        }
        return EAlarmTakeMedicine;
    }

    public void CleanEAlarmTakeMedicine() throws SQLException {
        databaseHelper.CleanEAlarmTakeMedicine();
    }




    /**
     * On finish app
     */
    @Override
    public void onTerminate() {
        super.onTerminate();
        if (databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }
    }

    public static HealthMonitorApplicattion getApplication() {
        return instance;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }


}
