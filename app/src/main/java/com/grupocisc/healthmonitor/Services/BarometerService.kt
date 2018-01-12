package com.grupocisc.healthmonitor.Services

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.IBinder
import android.util.Log
import com.grupocisc.healthmonitor.R
import com.grupocisc.healthmonitor.Utils.NotificationHelper
import com.grupocisc.healthmonitor.Utils.SensorChecker
import com.grupocisc.healthmonitor.Utils.ServiceChecker
import com.grupocisc.healthmonitor.Utils.Utils
import kotlin.math.log

/**
 * Created by alex on 1/8/18.
 */
class BarometerService: Service(),SensorEventListener {
    val tag: String = "BarometerService"
    private var sensorManager: SensorManager?=null
    private var sensor: Sensor? = null
    private var scheduler: AlarmManager? = null

    lateinit var ctx: Context

    override fun onSensorChanged(event: SensorEvent?) {
        val attitude = event?.values?.firstOrNull()

        if(android.os.Build.VERSION.SDK_INT>=android.os.Build.VERSION_CODES.O){
            Log.i(tag,"La presión del aire es: $attitude mindelay ${event?.sensor?.minDelay} maxdelay ${event?.sensor?.maxDelay} modo de reporte ${event?.sensor?.reportingMode} range ${event?.sensor?.maximumRange}")
        }
        else{
            Log.i(tag,"La presión del aire es: $attitude mindelay ${event?.sensor?.minDelay} range ${event?.sensor?.maximumRange}")
        }

        if(attitude!!>= 2000){
            NotificationHelper.showNotification(applicationContext, 1009, R.mipmap.icon_inhalator, "111", "Medición de presión del aire", "El sensor detectó una presión de ${attitude!!}")
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, intent: Int) {

    }

    override fun onBind(intent: Intent?): IBinder {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        if (!ServiceChecker.isServiceRunning(applicationContext, BarometerService::class.java)) {
            Log.i(tag, "Iniciando el servicio de lectura de presión")
            val restartServiceIntent = Intent(applicationContext, this.javaClass)
            //restartServiceIntent.`package` = packageName
            startService(restartServiceIntent)
        } else {
            Log.i(tag, "El servicio de lectura de presión ya está en ejecución")
        }
        super.onTaskRemoved(rootIntent)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        onTaskRemoved(intent)
        if(Utils.getEmailFromPreference(applicationContext)!=null)
        {
            if(SensorChecker.isSupported(applicationContext,Sensor.TYPE_PRESSURE))
            {
                sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
                sensor = sensorManager?.getDefaultSensor(Sensor.TYPE_PRESSURE)
                sensorManager?.registerListener(this, sensor, changeMilliToMicro(1000)) // la propiedad de event.sensor.maxdelay muestra cual es el delay máximo que soporta el sensor
            }else{
                Log.i(tag, "El dispositivo no soporta el sensor de barómetro")
            }
        }
        else{
            Log.i(tag, "User didn't log in")
        }

        return START_STICKY
    }

    fun changeMilliToMicro(milli:Int):Int{
        Log.i(tag,"$milli = ${milli * 1000} micro s")
        return  milli * 1000
    }

    override fun onDestroy() {
        super.onDestroy()
        sensorManager?.unregisterListener(this)
        if(SensorChecker.isSupported(applicationContext,Sensor.TYPE_PRESSURE)){
            runService()
        }
    }

    fun runService() {
        try{
            if (Utils.getEmailFromPreference(applicationContext) != null) {
                if (SensorChecker.Current.isSupported(applicationContext, Sensor.TYPE_PRESSURE)) {
                    scheduler = applicationContext.getSystemService (Context.ALARM_SERVICE) as AlarmManager

                    val barometerService = Intent(applicationContext, BarometerService::class.java)
                    val pendingIntent:PendingIntent = PendingIntent . getService (applicationContext, 0, barometerService, PendingIntent.FLAG_UPDATE_CURRENT)

                    scheduler?.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), AlarmManager.INTERVAL_FIFTEEN_MINUTES, pendingIntent);
                } else {
                    Log.i(tag, "El dispositivo no soporta el sensor de barómetro")
                }
            }
        }
        catch (ex:Exception){
            Log.e(tag,ex.message)
        }
    }
}