package com.grupocisc.healthmonitor.Barometro;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.grupocisc.healthmonitor.HealthMonitorApplicattion;
import com.grupocisc.healthmonitor.R;
import com.grupocisc.healthmonitor.entities.IOpenWeatherMap;
import com.transitionseverywhere.ChangeText;
import com.transitionseverywhere.Fade;
import com.transitionseverywhere.Rotate;
import com.transitionseverywhere.TransitionSet;
import com.transitionseverywhere.extra.Scale;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;

public class BarometroActivity extends AppCompatActivity implements LocationListener {
    private String TAG = "BarometroActivity";
    private SensorManager sensorManager = null;
    private Sensor sensor = null;
    PressureAsyncTask pressureTask;

    @BindView(R.id.tvCityName)
    TextView tvCityName;
    @BindView(R.id.tvLastForecastDate)
    TextView tvLastForecastDate;
    @BindView(R.id.tvForecast)
    TextView tvForecast;
    @BindView(R.id.btnMeasure)
    FloatingActionButton btnMeasure;
    @BindView(R.id.tvPressure)
    TextView tvPressure;
    @BindView(R.id.tvHumidity)
    TextView tvHumidity;
    @BindView(R.id.tvTemperature)
    TextView tvTemperature;
    @BindView(R.id.toolbar)
    android.support.v7.widget.Toolbar toolbar;

    @BindView(R.id.img_icon)
    ImageView imgIcon;

    @BindView(R.id.transitions_container)
    ViewGroup transitionsContainer;

    @BindView(R.id.progress)
    ProgressBar progress;

    IOpenWeatherMap.OpenWeatherMap data;
    LocationManager locationManager;

    boolean isRotated=false;


//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barometro);

        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.back); //buton back tollbar
        getSupportActionBar().setTitle("Barómetro"); //titulo tollbar
        toolbar.setTitleTextColor(getResources().getColor(R.color.white)); //color tollbar title

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    @OnClick(R.id.btnMeasure)
    void getLocation() {

        UpdateWidget(progress,true);

        if(isOnline()){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG,"Faltan permisos");
                    ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
                    UpdateWidget(progress,false);
                    return;
                }
                else {
                    //LocationProvider low =locationManager.getProvider(locationManager.getBestProvider(createCoarseCriteria(),false));
                    //LocationProvider high = locationManager.getProvider(locationManager.getBestProvider(createFineCriteria(),true));

                    if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                        buildAlertMessage();
                    }
                    else {
                        /*locationManager.requestLocationUpdates(low.getName(), 200, 0, this);
                        locationManager.requestLocationUpdates(high.getName(), 200, 0, this);*/

                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 200, 0, this);
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 200, 0, this);
                    }
                }
            } else {

                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 200, 0, this);
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 200, 0, this);
            }
        }
        else {
            Toast.makeText(this, "No se encuentra conectado a internet",Toast.LENGTH_SHORT).show();
            UpdateWidget(progress,false);
        }
    }

    Criteria createCoarseCriteria() {

        Criteria c = new Criteria();
        c.setAccuracy(Criteria.ACCURACY_COARSE);
        c.setAltitudeRequired(false);
        c.setBearingRequired(false);
        c.setSpeedRequired(false);
        c.setCostAllowed(true);
        c.setPowerRequirement(Criteria.POWER_HIGH);
        return c;

    }

    /** this criteria needs high accuracy, high power, and cost */
    Criteria createFineCriteria() {

        Criteria c = new Criteria();
        c.setAccuracy(Criteria.ACCURACY_FINE);
        c.setAltitudeRequired(false);
        c.setBearingRequired(false);
        c.setSpeedRequired(false);
        c.setCostAllowed(true);
        c.setPowerRequirement(Criteria.POWER_HIGH);
        return c;

    }

    void buildAlertMessage(){
        UpdateWidget(progress,false);
        final AlertDialog.Builder builder= new AlertDialog.Builder(this);

        builder.setMessage("Tu GPS se encuentra desactivado, deseas activarlo?")
                .setCancelable(false)
                .setPositiveButton("Si", (dialog, i) -> startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)))
                .setNegativeButton("No", (dialog, i) -> dialog.cancel());

        final AlertDialog dialog = builder.create();
        dialog.show();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode){
            case 1:
                if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED ){
                    Toast.makeText(this,"Ya tiene permisos",Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(this,"No tiene permisos",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    void  setData(IOpenWeatherMap.OpenWeatherMap data){
        if(data!=null){

            UpdateWidget(tvCityName,true);
            TextChange(tvCityName,data.getName().toUpperCase());
            TextChange(tvLastForecastDate," "+convertMillisToStringDate(System.currentTimeMillis()));
            UpdateWidget(imgIcon,true);

            String foreCast = data.getWeather().get(0).getMain().toUpperCase();

            if(foreCast.equals("CLEAR")){
                TextChange(tvForecast,"DESPEJADO");
                changeIcon(R.drawable.ic_white_balance_sunny_white_48dp);
            }
            else if(foreCast.equals("RAIN")){
                TextChange(tvForecast,"LLUVIOSO");
                changeIcon(R.drawable.ic_weather_rainy_white_48dp);
            }
            else if (foreCast.equals("CLOUDS")){
                TextChange(tvForecast,"NUBLADO");
                changeIcon(R.drawable.ic_weather_cloudy_white_48dp);
            }

            TextChange(tvPressure,String.format(" %.0f hpa",data.getMain().getPressure()));
            TextChange(tvHumidity," "+data.getMain().getHumidity()+"%");
            TextChange(tvTemperature,String.format( " %.0f °C", getKelvinToCelsius(data.getMain().getTemp()) ));
        }
        else {
            UpdateWidget(tvCityName,false);
            UpdateWidget(imgIcon,false);
            TextChange(tvLastForecastDate,"");
            TextChange(tvForecast,"");
            TextChange(tvPressure," 0 hpa");
            TextChange(tvHumidity," 0%");
            TextChange(tvTemperature,"0 °C");
        }
    }

    void changeIcon(int drawable){
        imgIcon.setImageResource(drawable);
    }

    void TextChange(TextView _textView,String text){
        com.transitionseverywhere.TransitionManager.beginDelayedTransition(transitionsContainer,  new ChangeText().setChangeBehavior(ChangeText.CHANGE_BEHAVIOR_OUT_IN));
        _textView.setText(text!=null ? text : "");
    }

    void UpdateWidget(View _view, boolean _isVisible){
        TransitionSet set = new TransitionSet()
                .addTransition(new Scale(0.7f))
                .addTransition(new Fade())
                .setInterpolator(_isVisible ? new LinearOutSlowInInterpolator() :
                        new FastOutLinearInInterpolator());
        com.transitionseverywhere.TransitionManager.beginDelayedTransition(transitionsContainer,set);
        _view.setVisibility(_isVisible ? View.VISIBLE:View.GONE);
    }

    double getKelvinToCelsius(double kelvinValue){
        double celsius = kelvinValue - 273.15;
        return celsius;
    }

    String convertMillisToStringDate(long millis){

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy HH:mm a");
        return  dateFormat.format(calendar.getTime());

    }

//    public void presion(View view){
//        if(Utils.getEmailFromPreference(this)!=null)
//        {
//            if(SensorChecker.Current.isSupported(this,Sensor.TYPE_PRESSURE))
//            {
//                sensorManager = (SensorManager) getSystemService(this.SENSOR_SERVICE);
//                sensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
//                sensorManager.registerListener(this, sensor, changeMilliToMicro(1000)); // la propiedad de event.sensor.maxdelay muestra cual es el delay máximo que soporta el sensor
//            }else{
//                Log.i(TAG, "El dispositivo no soporta el sensor de barómetro");
//            }
//        }
//        else{
//            Log.i(TAG, "User didn't log in");
//        }
//    }

    private int changeMilliToMicro(int milli){
        Log.i(TAG,"$milli = ${milli * 1000} micro s");
        return  milli * 1000;
    }

//    @Override
//    public void onSensorChanged(SensorEvent sensorEvent) {
////        tvPressure.setText(String.valueOf(sensorEvent.values[0]));
////
////        if(android.os.Build.VERSION.SDK_INT>=android.os.Build.VERSION_CODES.O){
////            Log.i(TAG,"La presión del aire es: $attitude mindelay ${event?.sensor?.minDelay} maxdelay ${event?.sensor?.maxDelay} modo de reporte ${event?.sensor?.reportingMode} range ${event?.sensor?.maximumRange}");
////        }
////        else{
////            Log.i(TAG,"La presión del aire es: $attitude mindelay ${event?.sensor?.minDelay} range ${event?.sensor?.maximumRange}");
////        }
////
////        //if(attitude>= 2000){
////        if(Float.parseFloat(tvPressure.getText().toString().trim())>= 2000){
////            NotificationHelper.Current.showNotification(getApplicationContext(), 1009, R.mipmap.icon_inhalator, "111", "Medición de presión del aire", "El sensor detectó una presión de ${attitude!!}");
////        }
//    }
//
//    @Override
//    public void onAccuracyChanged(Sensor sensor, int i) {
//
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            callBackPressedActivity();
        return super.onOptionsItemSelected(item);
    }

    public void callBackPressedActivity(){
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ){
            //pager.setVisibility( View.INVISIBLE );
            //TransitionManager.beginDelayedTransition(mRoot, new Slide());
        }
        super.onBackPressed();
    }

    @Override
    public void onLocationChanged(Location location) {

        Log.i(TAG,"Removiendo actualizaciones de GPS");
        locationManager.removeUpdates(this);

        pressureTask = new PressureAsyncTask();
        if(pressureTask.getStatus() == AsyncTask.Status.PENDING)
        {
            Log.i(TAG,"Haciendo petición a servicio OpenWeatherMap");
            pressureTask.execute(location.getLatitude(),location.getLongitude());
        }
        else if (pressureTask.getStatus()== AsyncTask.Status.RUNNING){
            pressureTask.cancel(true);
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
        if(pressureTask!=null)
            pressureTask.cancel(true);

        UpdateWidget(progress,false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        locationManager.removeUpdates(this);

        if(pressureTask!=null)
            pressureTask.cancel(true);

        UpdateWidget(progress,false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG,"Faltan permisos");
            //ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
            return;
        }
        locationManager.removeUpdates(this);

        if(pressureTask!=null)
            pressureTask.cancel(true);

        UpdateWidget(progress,false);
    }


    class PressureAsyncTask extends AsyncTask<Double,Void,IOpenWeatherMap.OpenWeatherMap>{
        private SweetAlertDialog dialog;

        @Override
        protected IOpenWeatherMap.OpenWeatherMap doInBackground(Double... doubles) {
            IOpenWeatherMap openWeatherMap = HealthMonitorApplicattion.getApplication().getMeasurePressureAdapter().create(IOpenWeatherMap.class);
            Call<IOpenWeatherMap.OpenWeatherMap> invokeMethod = openWeatherMap.getData(doubles[0],doubles[1],"29a0fabd112d649e3c512dd4856e7870");
            try {
                data= invokeMethod.execute().body();
            } catch (IOException e) {
                data = null;
                e.printStackTrace();
            }
            Log.i(TAG,"Obteniendo datos del servicio web");
            return  data;
        }

        @Override
        protected void onPostExecute(IOpenWeatherMap.OpenWeatherMap data) {
           Log.i(TAG,"Seteando los datos en los controles");
           //dialog.dismissWithAnimation();
            UpdateWidget(progress,false);

           if(data!=null)
               setData(data);
           else {
               dialog = new SweetAlertDialog(BarometroActivity.this, SweetAlertDialog.ERROR_TYPE);
               dialog.setTitleText("No se pudieron obtener los datos");
               dialog.setCancelable(true);
               dialog.show();
               setData(null);
           }
        }
    }
}