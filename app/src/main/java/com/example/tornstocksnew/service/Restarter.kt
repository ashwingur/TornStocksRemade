package com.example.tornstocksnew.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast

class Restarter : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Toast.makeText(context, "Broadcast received", Toast.LENGTH_SHORT).show()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context!!.startForegroundService(Intent(context, TriggerCheckerService::class.java))
            Toast.makeText(context, "Starting foreground service", Toast.LENGTH_SHORT).show()
        } else {
            context!!.startService(Intent(context, TriggerCheckerService::class.java))
        }
    }
}