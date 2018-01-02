package com.grupocisc.healthmonitor.Utils

import android.app.ActivityManager
import android.content.Context

/**
 * Created by alex on 1/2/18.
 */
class ServiceChecker {
    companion object Current {
        fun <T> isServiceRunning(ctx:Context, serviceClass:Class<T>):Boolean
        {
            val manager = ctx.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            manager.getRunningServices(Integer.MAX_VALUE).forEach { service ->
                if (serviceClass.name == service.service.className) {
                    return true
                }
            }
            return false
        }
    }
}