package com.grupocisc.healthmonitor.Utils

import android.content.Context
import android.os.PowerManager

/**
 * Created by alex on 1/3/18.
 */
class WakeLocker
{    companion object Current {
        private var wakeLock: PowerManager.WakeLock? = null

        fun acquire(context: Context) {
            if (wakeLock != null) wakeLock!!.release()

            val pm = context.getSystemService(Context.POWER_SERVICE) as PowerManager
            wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK or
                    PowerManager.ACQUIRE_CAUSES_WAKEUP or
                    PowerManager.ON_AFTER_RELEASE, "WakeLock")
            wakeLock!!.acquire()
        }

        fun release() {
            if (wakeLock != null) wakeLock!!.release()
            wakeLock = null
        }
    }
}