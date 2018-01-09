package com.grupocisc.healthmonitor.Utils

import android.content.Context
import android.hardware.SensorManager

/**
 * Created by alex on 1/7/18.
 */
class SensorChecker {
    companion object Current{
        fun isSupported(ctx:Context,sensorType:Int):Boolean{
            val sensorManager = ctx.getSystemService(Context.SENSOR_SERVICE) as SensorManager
            val sensor = sensorManager.getDefaultSensor(sensorType)
            return sensor!=null
        }
    }
}