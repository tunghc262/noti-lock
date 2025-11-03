package com.example.myapplication

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class NotificationWorker(
    private val context: Context,
    workerParams: WorkerParameters,
) : CoroutineWorker(context, workerParams) {

    private val notificationLock =
        listOf(
            NotificationData(
                id = INDEX_TAKE_PHOTO,
                title = context.getString(R.string.title_noti_lock_1),
                message = context.getString(R.string.message_noti_lock_1)
            ),
            NotificationData(
                id = INDEX_TAKE_PHOTO,
                title = context.getString(R.string.title_noti_lock_2),
                message = context.getString(R.string.message_noti_lock_2)
            ),
            NotificationData(
                id = INDEX_TAKE_PHOTO,
                title = context.getString(R.string.title_noti_lock_3),
                message = context.getString(R.string.message_noti_lock_3)
            ),
        )

    private val notificationNormal =
        listOf(
            NotificationData(
                id = INDEX_TAKE_PHOTO,
                title = context.getString(R.string.title_noti_normal_1),
                message = context.getString(R.string.message_noti_normal_1)
            ),
            NotificationData(
                id = INDEX_TAKE_PHOTO,
                title = context.getString(R.string.title_noti_normal_2),
                message = context.getString(R.string.message_noti_normal_2)
            ),
            NotificationData(
                id = INDEX_TAKE_PHOTO,
                title = context.getString(R.string.title_noti_normal_3),
                message = context.getString(R.string.message_noti_normal_3)
            ),
        )

    override suspend fun doWork(): Result {
        Log.e("TAG", "onReceive: ")
        if (isSendNotification()) {
            checkAppStateAndNotify()
        }
        return Result.success()
    }

    private fun checkAppStateAndNotify() {
        val serviceNotificationManager = (context as App).serviceNotificationManager
        serviceNotificationManager.createNotificationOffline(
            "notification.title",
            "notification.message"
        )
    }
    private fun isSendNotification(): Boolean {
//        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//            context.isNotificationPermissionsGranted()
//        } else {
//            true
//        }
        return true
    }


    companion object {
        private const val INDEX_TAKE_PHOTO = 0
        private const val INDEX_DETECT_INTRUDERS = 1
        private const val INDEX_APP_SUGGEST_NEW_INSTALL = 2
        private const val INDEX_THEME = 3
        private const val INDEX_CAMOUFLAGE = 4
        private const val INDEX_PREVENT_UNINSTALL = 5
        private const val TITLE_LOCK = "TITLE_LOCK"
        private const val MESSAGE_LOCK = "MESSAGE_LOCK"
        private const val TITLE_NORMAL = "TITLE_NORMAL"
        private const val MESSAGE_NORMAL = "MESSAGE_NORMAL"

    }

}