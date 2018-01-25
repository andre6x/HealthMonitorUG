package com.grupocisc.healthmonitor.Barometro;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.grupocisc.healthmonitor.HealthMonitorApplicattion;
import com.grupocisc.healthmonitor.R;
import com.grupocisc.healthmonitor.entities.IOpenWeatherMap;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BarometroActivity extends AppCompatActivity implements LocationListener {
    private String TAG = "BarometroActivity";
    private SensorManager sensorManager = null;
    private Sensor sensor = null;

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

    IOpenWeatherMap.OpenWeatherMap data;
    LocationManager locationManager;


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

        if(isOnline()){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG,"Faltan permisos");
                    ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
                    return;
                }
                else {
                    if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                        buildAlertMessage();
                    }
                    else {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
                    }
                }
            } else {

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            }
        }
        else {
            Toast.makeText(this, "No se encuentra conectado a internet",Toast.LENGTH_SHORT).show();
        }
    }

    void buildAlertMessage(){
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

//    @OnClick(R.id.btnMeasure)
    void PressureMeasurement(double lat, double lon){
        IOpenWeatherMap openWeatherMap = HealthMonitorApplicattion.getApplication().getMeasurePressureAdapter().create(IOpenWeatherMap.class);
        Call<IOpenWeatherMap.OpenWeatherMap> invokeMethod = openWeatherMap.getData(lat,lon,"29a0fabd112d649e3c512dd4856e7870");
        invokeMethod.enqueue(new Callback<IOpenWeatherMap.OpenWeatherMap>() {
            @Override
            public void onResponse(Call<IOpenWeatherMap.OpenWeatherMap> call, Response<IOpenWeatherMap.OpenWeatherMap> response) {
                if(response.isSuccessful()){
                    data = response.body();
                    setData(data);
                }
                else {
                    Log.e(TAG,"No se pudo obtener la información");
                }
            }

            @Override
            public void onFailure(Call<IOpenWeatherMap.OpenWeatherMap> call, Throwable t) {
                Log.e(TAG,"No se pudo obtener la información se invocó desde onFailure");
                t.printStackTrace();
            }
        });

    }

    void  setData(IOpenWeatherMap.OpenWeatherMap data){
        tvCityName.setText(data.getName().toUpperCase());
        tvLastForecastDate.setText(" "+convertMillisToStringDate(System.currentTimeMillis()));
        tvForecast.setText(data.getWeather().get(0).getMain().toUpperCase());
        tvPressure.setText(" "+data.getMain().getPressure()+" hpa");
        tvHumidity.setText(" "+data.getMain().getHumidity()+"%");
        tvTemperature.setText(getKelvinToCelsius(data.getMain().getTemp())+" °C");

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
        Log.i(TAG,"Haciendo petición a servicio OpenWeatherMap");
        PressureMeasurement(location.getLatitude(),location.getLongitude());
        Log.i(TAG,"Removiendo actualizaciones de GPS");
        locationManager.removeUpdates(this);
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
    }

    @Override
    protected void onResume() {
        super.onResume();
        locationManager.removeUpdates(this);
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
    }
}