package com.grupocisc.healthmonitor.Services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.grupocisc.healthmonitor.Utils.ServiceChecker

/**
 * Created by alex on 1/3/18.
 */
class RestartServiceReceiver: BroadcastReceiver() {
    private val tag:String="RestartServiceReceiver"

    override fun onReceive(ctx: Context?, intent: Intent?) {
        if(!ServiceChecker.Current.isServiceRunning(ctx!!,AssistantService::class.java)){
            Log.i(tag,"Reiniciando servicio")
            ctx?.startService(Intent(ctx.applicationContext,AssistantService::class.java))
        }
        else
            Log.i(tag,"El servicio no necnsita ser reiniciado")
    }
}