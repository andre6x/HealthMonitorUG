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
import android.os.Build
import android.os.IBinder
import android.support.annotation.RequiresApi
import android.util.Log
import com.grupocisc.healthmonitor.R
import com.grupocisc.healthmonitor.Utils.NotificationHelper
import com.grupocisc.healthmonitor.Utils.SensorChecker
import com.grupocisc.healthmonitor.Utils.ServiceChecker
import com.grupocisc.healthmonitor.Utils.Utils

/**
 * Created by alex on 1/8/18.
 */
class BarometerService: Service(),SensorEventListener {
    val tag: String = "BarometerService"
    var sensorManager: SensorManager?=null
    var sensor: Sensor? = null
    var scheduler: AlarmManager? = null

    lateinit var ctx: Context

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onSensorChanged(event: SensorEvent?) {
        val attitude = event?.values?.firstOrNull()

        Log.i(tag,"La presión es: $attitude")
        //NotificationHelper.showNotification(applicationContext, 1009, R.mipmap.icon_inhalator, "111", "Medición de presión atmosférica", "El sensor detectó una elevación de ${attitude!!}")
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
            restartServiceIntent.`package` = packageName
            startService(restartServiceIntent)
        } else {
            Log.i(tag, "El servicio de lectura de presión ya está en ejecución")
        }
        super.onTaskRemoved(rootIntent)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        onTaskRemoved(intent)
        //runService()
        //ctx = application.applicationContext
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensor = sensorManager?.getDefaultSensor(Sensor.TYPE_PRESSURE)
        sensorManager?.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        sensorManager?.unregisterListener(this)
    }

    fun runService() {
        if (Utils.getEmailFromPreference(applicationContext) != null) {
            if (SensorChecker.Current.isSupported(applicationContext, Sensor.TYPE_PRESSURE)) {
                scheduler = applicationContext.getSystemService (Context.ALARM_SERVICE) as AlarmManager

                val barometerService = Intent(applicationContext, BarometerService::class.java)
                val pendingIntent:PendingIntent = PendingIntent . getService (applicationContext, 0, barometerService, PendingIntent.FLAG_UPDATE_CURRENT)

                scheduler?.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), AlarmManager.INTERVAL_HALF_HOUR, pendingIntent);
            } else {
                Log.i(tag, "El dispositivo no soporta el sensor de barómetro");
            }

        }
    }
}