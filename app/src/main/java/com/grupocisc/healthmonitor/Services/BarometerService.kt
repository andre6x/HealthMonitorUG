package com.grupocisc.healthmonitor.Services

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
import com.grupocisc.healthmonitor.Utils.ServiceChecker

/**
 * Created by alex on 1/8/18.
 */
class BarometerService: Service(),SensorEventListener {
    val tag:String="BarometerService"
    lateinit var sensorManager: SensorManager
    lateinit var sensor:Sensor

    lateinit var ctx:Context

    override fun onSensorChanged(event: SensorEvent?) {
        val attitude = event?.values?.first()
        NotificationHelper.showNotification(ctx,1009,R.mipmap.icon_inhalator,"009","Medición de presión atmosférica","El sensor detectó una elevación de "+attitude)
    }

    override fun onAccuracyChanged(sensor: Sensor?, intent: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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
        ctx = application.applicationContext
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE)
        sensorManager.registerListener(this,sensor,SensorManager.SENSOR_DELAY_NORMAL)

        return START_STICKY
    }
}