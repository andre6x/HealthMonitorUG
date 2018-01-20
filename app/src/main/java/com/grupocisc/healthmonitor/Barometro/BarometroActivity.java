package com.grupocisc.healthmonitor.Barometro;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toolbar;

import com.grupocisc.healthmonitor.Utils.NotificationHelper;
import com.grupocisc.healthmonitor.R;
import com.grupocisc.healthmonitor.Utils.SensorChecker;
import com.grupocisc.healthmonitor.Utils.Utils;

public class BarometroActivity extends AppCompatActivity implements SensorEventListener {
    private android.support.v7.widget.Toolbar toolbar;
    private TextView txtPresion;
    private String TAG = "BarometroActivity";
    private SensorManager sensorManager = null;
    private Sensor sensor = null;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barometro);

        toolbar = (android.support.v7.widget.Toolbar)findViewById(R.id.toolbar);

        //Utils.SetStyleToolbarLogo(this);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayShowTitleEnabled(false); //desactivar title
        toolbar.setNavigationIcon(R.mipmap.back); //buton back tollbar
        getSupportActionBar().setTitle("Barómetro"); //titulo tollbar
        toolbar.setTitleTextColor(getResources().getColor(R.color.white)); //color tollbar title

        txtPresion = (TextView) findViewById(R.id.tvPressure);
    }

    public void presion(View view){
        if(Utils.getEmailFromPreference(this)!=null)
        {
            if(SensorChecker.Current.isSupported(this,Sensor.TYPE_PRESSURE))
            {
                sensorManager = (SensorManager) getSystemService(this.SENSOR_SERVICE);
                sensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
                sensorManager.registerListener(this, sensor, changeMilliToMicro(1000)); // la propiedad de event.sensor.maxdelay muestra cual es el delay máximo que soporta el sensor
            }else{
                Log.i(TAG, "El dispositivo no soporta el sensor de barómetro");
            }
        }
        else{
            Log.i(TAG, "User didn't log in");
        }
    }

    private int changeMilliToMicro(int milli){
        Log.i(TAG,"$milli = ${milli * 1000} micro s");
        return  milli * 1000;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        //float attitude = sensorEvent.values[0];
        txtPresion.setText(String.valueOf(sensorEvent.values[0]));

        if(android.os.Build.VERSION.SDK_INT>=android.os.Build.VERSION_CODES.O){
            Log.i(TAG,"La presión del aire es: $attitude mindelay ${event?.sensor?.minDelay} maxdelay ${event?.sensor?.maxDelay} modo de reporte ${event?.sensor?.reportingMode} range ${event?.sensor?.maximumRange}");
        }
        else{
            Log.i(TAG,"La presión del aire es: $attitude mindelay ${event?.sensor?.minDelay} range ${event?.sensor?.maximumRange}");
        }

        //if(attitude>= 2000){
        if(Float.parseFloat(txtPresion.getText().toString().trim())>= 2000){
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