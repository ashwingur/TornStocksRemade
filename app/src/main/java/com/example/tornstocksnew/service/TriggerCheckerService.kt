package com.example.tornstocksnew.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tornstocksnew.R
import com.example.tornstocksnew.models.Trigger
import com.example.tornstocksnew.repositories.Repository
import com.example.tornstocksnew.ui.activities.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@AndroidEntryPoint
class TriggerCheckerService : LifecycleService() {

    private val TAG = "TriggerCheckerService"

    private lateinit var mainHandler: Handler
    private var reqCodeCounter = 3
    private val test: MutableLiveData<Int> = MutableLiveData(0)
    private lateinit var triggers: LiveData<List<Trigger>>

    @Inject
    lateinit var repository: Repository

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startMyOwnForeground()
        } else {
            startForeground(1, Notification())
        }
    }



    @RequiresApi(Build.VERSION_CODES.O)
    private fun startMyOwnForeground() {
        val NOTIFICATION_CHANNEL_ID = "com.example.tornstocksnew.service"
        val channelName = "Background Service"
        val chan = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            channelName,
            NotificationManager.IMPORTANCE_MIN
        )
        chan.lightColor = Color.BLUE
        chan.lockscreenVisibility = Notification.VISIBILITY_PUBLIC

        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(chan)

        val notificationBuilder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
        val notification = notificationBuilder
            .setOngoing(true)
            .setContentTitle("Stocks are being monitored")
            .setPriority(NotificationManager.IMPORTANCE_MIN)
            .setCategory(Notification.CATEGORY_SERVICE)
            .build()
        Log.d(TAG, "startMyOwnForeground: Created")
        startForeground(2, notification)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        Log.d(TAG, "onStartCommand: Started")
        triggers = repository.getAllTriggers()
        startTask()
        return START_STICKY
    }

    private fun startTask() {
        mainHandler = Handler(Looper.getMainLooper())
        Log.d(TAG, "startTask: YAY starting task")
        mainHandler.post(object : Runnable {
            override fun run() {
                Log.d(TAG, "run: YAY")
                mainHandler.postDelayed(this, 10000)
            }
        })

        triggers.observe(this, {
            Toast.makeText(this@TriggerCheckerService, "triggers= ${triggers.value}", Toast.LENGTH_SHORT).show()
        })
    }

    private fun showNotification(
        context: Context,
        title: String,
        message: String,
        intent: Intent,
        reqCode: Int
    ) {
        val pendingIntent =
            PendingIntent.getActivity(context, reqCode, intent, PendingIntent.FLAG_ONE_SHOT)
        val CHANNEL_ID = "Trigger Alert"
        val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .setContentIntent(pendingIntent);
        notificationBuilder.setVibrate(
            longArrayOf(
                0,
                500,
                200,
                500,
                200,
                1000
            )
        ) // Vibrate pattern [delay, vibrate, delay, vibrate...]
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = "Stock Trigger Alert"
            val channel = NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(reqCode, notificationBuilder.build())
    }

    override fun onDestroy() {
        super.onDestroy()
        mainHandler.removeCallbacksAndMessages(null)
        val broadcastIntent = Intent()
        broadcastIntent.setAction("restartService")
        broadcastIntent.setClass(this, Restarter::class.java)
        Toast.makeText(this, "Sending broadcast", Toast.LENGTH_SHORT).show()
        sendBroadcast(broadcastIntent)
    }

//    override fun onBind(p0: Intent?): IBinder? {
//        return null
//    }
}