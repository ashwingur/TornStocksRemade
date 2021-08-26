package com.example.tornstocksnew.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.Toast

class Restarter : BroadcastReceiver() {

    private val TAG = "Debugg"
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d(TAG, "onReceive: Received")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context!!.startForegroundService(Intent(context, TriggerCheckerService::class.java))
            Log.d(TAG, "onReceive: Restarting foreground service, context is $context")
        } else {
            context!!.startService(Intent(context, TriggerCheckerService::class.java))
        }
    }
}