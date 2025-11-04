package com.example.myapplication

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Calendar

class SplashActivity : AppCompatActivity() {

    companion object {
        const val REQUEST_CODE_NOTIFICATION_LOCK = 0
        const val REQUEST_CODE_NOTIFICATION_NORMAL = 1
        const val ALARM_REQUEST_CODE = "ALARM_REQUEST_CODE"
    }

    private lateinit var alarmManager: AlarmManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager

        val fromNotification = intent?.getBooleanExtra("from_notification", false) == true

        if (fromNotification && App.isAppStarted){
            overridePendingTransition(0, 0)
            finish()
            return
        }

        scheduleDailyWork()
        lifecycleScope.launch {
            delay(5000)
            if (fromNotification){
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            }else{
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            }
        }
    }

    private fun scheduleDailyWork() {
        cancelDailyWork()

        val isShowRemindUsingAppNotification = true

        if (!isShowRemindUsingAppNotification) {
            return
        }

        val intentLock = Intent(this, DailyWorkReceiver::class.java).apply {
            putExtra(ALARM_REQUEST_CODE, REQUEST_CODE_NOTIFICATION_LOCK)
        }

        val pendingIntentLock = PendingIntent.getBroadcast(
            this,
            REQUEST_CODE_NOTIFICATION_LOCK,
            intentLock,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val intentNormal = Intent(this, DailyWorkReceiver::class.java).apply {
            putExtra(ALARM_REQUEST_CODE, REQUEST_CODE_NOTIFICATION_NORMAL)
        }

        val pendingIntentNormal = PendingIntent.getBroadcast(
            this,
            REQUEST_CODE_NOTIFICATION_NORMAL,
            intentNormal,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, 17)
            set(Calendar.MINUTE, 15)
            set(Calendar.SECOND, 0)
            if (timeInMillis < System.currentTimeMillis()) {
                add(Calendar.DAY_OF_YEAR, 1)
            }
        }

        val calendar2 = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, 17)
            set(Calendar.MINUTE, 17)
            set(Calendar.SECOND, 0)
            if (timeInMillis < System.currentTimeMillis()) {
                add(Calendar.DAY_OF_YEAR, 1)
            }
        }

        val dayInterval = 1 * AlarmManager.INTERVAL_DAY

        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            dayInterval,
            pendingIntentLock
        )

        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar2.timeInMillis,
            dayInterval,
            pendingIntentNormal
        )
    }

    private fun cancelDailyWork() {
        val intentLock = Intent(this, DailyWorkReceiver::class.java).apply {
            putExtra(ALARM_REQUEST_CODE, REQUEST_CODE_NOTIFICATION_LOCK)
        }
        val pendingIntentLock = PendingIntent.getBroadcast(
            this,
            REQUEST_CODE_NOTIFICATION_LOCK,
            intentLock,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        if (pendingIntentLock != null) {
            alarmManager.cancel(pendingIntentLock)
        }

        val intentNormal = Intent(this, DailyWorkReceiver::class.java).apply {
            putExtra(ALARM_REQUEST_CODE, REQUEST_CODE_NOTIFICATION_NORMAL)
        }
        val pendingIntentNormal = PendingIntent.getBroadcast(
            this,
            REQUEST_CODE_NOTIFICATION_NORMAL,
            intentNormal,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        if (pendingIntentNormal != null) {
            alarmManager.cancel(pendingIntentNormal)
        }
    }
}
