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
import com.example.tornstocksnew.models.Stock
import com.example.tornstocksnew.models.TRIGGER_TYPE
import com.example.tornstocksnew.models.Trigger
import com.example.tornstocksnew.repositories.Repository
import com.example.tornstocksnew.ui.activities.MainActivity
import com.example.tornstocksnew.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.math.absoluteValue

@AndroidEntryPoint
class TriggerCheckerService : LifecycleService() {

    private val TAG = "TriggerCheckerService"

    private lateinit var mainHandler: Handler
    private val DELAY = 60000L
    private var reqCodeCounter = 3
    private var testCounter = 0
    private val test: MutableLiveData<Int> = MutableLiveData(0)
    private lateinit var triggers: LiveData<List<Trigger>>
    private val stocks: MutableLiveData<List<Stock>> = MutableLiveData()

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

    private fun observeStocks() {
        stocks.observe(this, {
            Log.d(
                TAG,
                "observeStocks: Counter is $testCounter, triggers value: ${triggers.value}, stocks value: ${stocks.value}"
            )
            if (triggers.value != null && stocks.value !== null) {
                for (trigger in triggers.value!!) {
                    for (stock in stocks.value!!) {
                        if (trigger.stock_id == stock.stock_id) {
                            Log.d(TAG, "observeStocks: Checking stock ${trigger.acronym}")
                            checkIfTriggerHit(stock, trigger)
                            break
                        }
                    }
                }
            }

        })
    }

    private fun checkIfTriggerHit(stock: Stock, trigger: Trigger) {
        when (trigger.trigger_type) {
            TRIGGER_TYPE.DEFAULT -> {
                if (trigger.trigger_price >= trigger.stock_price && stock.current_price >= trigger.trigger_price) {
                    // Stock is now above trigger price
                    showNotification(
                        this,
                        "Default trigger hit",
                        "%s is now above %.2f".format(trigger.acronym, trigger.trigger_price),
                        Intent(this, MainActivity::class.java),
                        reqCodeCounter++
                    )
                    Log.d(TAG, "checkIfTriggerHit: Hit above trigger for ${trigger.name}")
                    if (trigger.single_use) {
                        GlobalScope.launch {
                            withContext(Dispatchers.IO) {
                                repository.deleteTrigger(trigger)
                            }
                        }
                    }
                } else if (trigger.trigger_price < trigger.stock_price && stock.current_price <= trigger.trigger_price) {
                    // Stock is now below the trigger price
                    showNotification(
                        this,
                        "Default trigger hit",
                        "%s is now below %.2f".format(trigger.acronym, trigger.trigger_price),
                        Intent(this, MainActivity::class.java),
                        reqCodeCounter++
                    )
                    Log.d(TAG, "checkIfTriggerHit: Hit below trigger for ${trigger.name}")
                    if (trigger.single_use) {
                        GlobalScope.launch {
                            withContext(Dispatchers.IO) {
                                repository.deleteTrigger(trigger)
                            }
                        }
                    }
                }
            }
            TRIGGER_TYPE.PERCENTAGE -> {
                val triggerPrice = (1 + trigger.trigger_percentage/100) * trigger.stock_price
                if (stock.current_price >= triggerPrice && trigger.trigger_percentage >= 0) {
                    // Stock is now above trigger price
                    showNotification(
                        this,
                        "Percentage trigger hit",
                        "%s is now %.2f%% above %.2f".format(trigger.acronym, trigger.trigger_percentage, trigger.stock_price),
                        Intent(this, MainActivity::class.java),
                        reqCodeCounter++
                    )
                    Log.d(TAG, "checkIfTriggerHit: Hit above trigger for ${trigger.name}")
                    if (trigger.single_use) {
                        GlobalScope.launch {
                            withContext(Dispatchers.IO) {
                                repository.deleteTrigger(trigger)
                            }
                        }
                    }
                } else if (stock.current_price < triggerPrice && trigger.trigger_percentage < 0){
                    // Stock is now below the trigger price
                    showNotification(
                        this,
                        "Default trigger hit",
                        "%s is now %.2f%% below %.2f".format(trigger.acronym, trigger.trigger_percentage.absoluteValue, trigger.stock_price),
                        Intent(this, MainActivity::class.java),
                        reqCodeCounter++
                    )
                    Log.d(TAG, "checkIfTriggerHit: Hit below trigger for ${trigger.name}")
                    if (trigger.single_use) {
                        GlobalScope.launch {
                            withContext(Dispatchers.IO) {
                                repository.deleteTrigger(trigger)
                            }
                        }
                    }
                }
            }
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
        repository.loadApiKey()
        startTask()
        return START_STICKY
    }

    private fun startTask() {
        mainHandler = Handler(Looper.getMainLooper())
        Log.d(TAG, "startTask: YAY starting task")
        mainHandler.post(object : Runnable {
            override fun run() {
                Log.d(TAG, "run: YAY")
                GlobalScope.launch {
                    withContext(Dispatchers.IO) {
                        stocks.postValue(Constants.API_KEY?.let { repository.getStocks(it).stocks.values.toMutableList() })
                    }
                }
                mainHandler.postDelayed(this, DELAY)
            }
        })
        triggers.observe(this, {})
        observeStocks()
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
            .setSmallIcon(R.mipmap.ic_app_icon_round)
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
        sendBroadcast(broadcastIntent)
    }

}