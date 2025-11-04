package com.example.myapplication

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DailyWorkReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Log.e("TAG", "onReceive: ", )
        val requestCode = intent.getIntExtra(SplashActivity.ALARM_REQUEST_CODE, -1)
        val workData = workDataOf(SplashActivity.ALARM_REQUEST_CODE to requestCode)

        val workRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
            .setInputData(workData)
            .build()
        WorkManager.getInstance(context).enqueue(workRequest)
    }
}