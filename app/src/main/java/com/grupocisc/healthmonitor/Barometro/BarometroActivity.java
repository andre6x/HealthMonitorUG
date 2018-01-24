package com.grupocisc.healthmonitor.Barometro;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toolbar;

import com.grupocisc.healthmonitor.HealthMonitorApplicattion;
import com.grupocisc.healthmonitor.Utils.NotificationHelper;
import com.grupocisc.healthmonitor.R;
import com.grupocisc.healthmonitor.Utils.SensorChecker;
import com.grupocisc.healthmonitor.Utils.Utils;
import com.grupocisc.healthmonitor.entities.IOpenWeatherMap;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class BarometroActivity extends AppCompatActivity implements SensorEventListener {
    //private android.support.v7.widget.Toolbar toolbar;
    //private TextView txtPresion;
    private String TAG = "BarometroActivity";
    private SensorManager sensorManager = null;
    private Sensor sensor = null;

    @BindView(R.id.tvCityName) TextView tvCityName;
    @BindView(R.id.tvLastForecastDate) TextView tvLastForecastDate;
    @BindView(R.id.tvForecast) TextView tvForecast;
    @BindView(R.id.btnMeasure) FloatingActionButton btnMeasure;
    @BindView(R.id.tvPressure) TextView tvPressure;
    @BindView(R.id.tvHumidity) TextView tvHumidity;
    @BindView(R.id.tvTemperature) TextView tvTemperature;
    @BindView(R.id.toolbar) android.support.v7.widget.Toolbar toolbar;

    IOpenWeatherMap.OpenWeatherMap retrivedData;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barometro);

        ButterKnife.bind(this);

        //toolbar = (android.support.v7.widget.Toolbar)findViewById(R.id.toolbar);

        //Utils.SetStyleToolbarLogo(this);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayShowTitleEnabled(false); //desactivar title
        toolbar.setNavigationIcon(R.mipmap.back); //buton back tollbar
        getSupportActionBar().setTitle("Barómetro"); //titulo tollbar
        toolbar.setTitleTextColor(getResources().getColor(R.color.white)); //color tollbar title

        //txtPresion = (TextView) findViewById(R.id.tvPressure);
    }

    @OnClick(R.id.btnMeasure)
    void PressureMeasurement(){
        IOpenWeatherMap openWeatherMap = HealthMonitorApplicattion.getApplication().getMeasurePressureAdapter().create(IOpenWeatherMap.class);
        Call<IOpenWeatherMap.OpenWeatherMap> invokeMethod = openWeatherMap.getData(-2.17,-79.922,"29a0fabd112d649e3c512dd4856e7870");
        invokeMethod.enqueue(new Callback<IOpenWeatherMap.OpenWeatherMap>() {
            @Override
            public void onResponse(Call<IOpenWeatherMap.OpenWeatherMap> call, Response<IOpenWeatherMap.OpenWeatherMap> response) {
                if(response.isSuccessful()){
                    retrivedData = response.body();
                    setData(retrivedData);
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
        tvLastForecastDate.setText(" "+convertMillisToStirngDate(data.getDt()));
        tvForecast.setText(data.getWeather().get(0).getMain().toUpperCase());
        tvPressure.setText(" "+data.getMain().getPressure()+" hpa");
        tvHumidity.setText(" "+data.getMain().getHumidity()+"%");
        tvTemperature.setText(getKelvinToCelsius(data.getMain().getTemp())+" °C");

    }

    double getKelvinToCelsius(double kelvinValue){
        double celsius = kelvinValue - 273.15;
        return celsius;
    }

    String convertMillisToStirngDate(int millis){
        
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss zz");
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

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        //float attitude = sensorEvent.values[0];
        tvPressure.setText(String.valueOf(sensorEvent.values[0]));

        if(android.os.Build.VERSION.SDK_INT>=android.os.Build.VERSION_CODES.O){
            Log.i(TAG,"La presión del aire es: $attitude mindelay ${event?.sensor?.minDelay} maxdelay ${event?.sensor?.maxDelay} modo de reporte ${event?.sensor?.reportingMode} range ${event?.sensor?.maximumRange}");
        }
        else{
            Log.i(TAG,"La presión del aire es: $attitude mindelay ${event?.sensor?.minDelay} range ${event?.sensor?.maximumRange}");
        }

        //if(attitude>= 2000){
        if(Float.parseFloat(tvPressure.getText().toString().trim())>= 2000){
            NotificationHelper.Current.showNotification(getApplicationContext(), 1009, R.mipmap.icon_inhalator, "111", "Medición de presión del aire", "El sensor detectó una presión de ${attitude!!}");
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

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
}