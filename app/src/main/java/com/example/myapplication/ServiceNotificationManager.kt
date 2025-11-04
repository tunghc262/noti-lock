package com.example.myapplication

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.provider.Settings
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import jakarta.inject.Singleton


@Singleton
class ServiceNotificationManager @Inject constructor(
    @ApplicationContext val context: Context,
) {

    companion object {
        private const val CHANNEL_ID_APP_LOCKER_SERVICE = "10101"
        private const val CHANNEL_ID = "daily_notification_channel"
        const val REQUEST_CODE_APP_BEAUTY_SERVICE = 112

        const val ID_NOTIFICATION_LOCK = 1111
        const val ID_NOTIFICATION_NORMAL = 1112

    }

    fun createNotificationLockOffline(title: String, message: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val isChannel = notificationManager.getNotificationChannel(CHANNEL_ID)
            if (isChannel == null) {
                val nameJobHide = "Daily Notifications"
                val importanceJobHide = NotificationManager.IMPORTANCE_DEFAULT
                val channelJobHide = NotificationChannel(CHANNEL_ID, nameJobHide, importanceJobHide)
                notificationManager.createNotificationChannel(channelJobHide)
            }
        }
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val bitmap: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.ic_launcher_background)
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_app) // Icon nhỏ góc trái (bắt buộc)
            .setLargeIcon(bitmap) // Ảnh thumbnail ở compact view (bên phải)
            .setStyle(
                NotificationCompat.BigPictureStyle()
                    .bigPicture(bitmap) // Ảnh lớn khi expand
                    .setBigContentTitle(title) // Title trong big picture
                    .setSummaryText(message) // Description đầy đủ dưới ảnh
                    .bigLargeIcon(null as Bitmap?) // Ẩn large icon ở expanded view (fix ambiguity)
            )
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(getResultPendingIntent())
            .setAutoCancel(true)
            .setShowWhen(false)
            .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
            .build()

        val notificationId = ID_NOTIFICATION_LOCK
        notificationManager.notify(notificationId, notification)
    }

    fun createNotificationNormalOffline(title: String, message: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val isChannel = notificationManager.getNotificationChannel(CHANNEL_ID)
            if (isChannel == null) {
                val nameJobHide = "Daily Notifications"
                val importanceJobHide = NotificationManager.IMPORTANCE_DEFAULT
                val channelJobHide = NotificationChannel(CHANNEL_ID, nameJobHide, importanceJobHide)
                notificationManager.createNotificationChannel(channelJobHide)
            }
        }
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val bitmap: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.ic_launcher_background)
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_app) // Icon nhỏ góc trái (bắt buộc)
            .setLargeIcon(bitmap) // Ảnh thumbnail ở compact view (bên phải)
            .setStyle(
                NotificationCompat.BigPictureStyle()
                    .bigPicture(bitmap) // Ảnh lớn khi expand
                    .setBigContentTitle(title) // Title trong big picture
                    .setSummaryText(message) // Description đầy đủ dưới ảnh
                    .bigLargeIcon(null as Bitmap?) // Ẩn large icon ở expanded view (fix ambiguity)
            )
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(getResultPendingIntent())
            .setAutoCancel(true)
            .setShowWhen(false)
            .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
            .build()

        val notificationId = ID_NOTIFICATION_NORMAL
        notificationManager.notify(notificationId, notification)
    }

    private fun getResultPendingIntent(): PendingIntent {
        val resultIntent = Intent(context, SplashActivity::class.java)
        resultIntent.putExtra("from_notification", true)
        val resultPendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getActivity(context, REQUEST_CODE_APP_BEAUTY_SERVICE, resultIntent, PendingIntent.FLAG_MUTABLE)
        } else {
            PendingIntent.getActivity(
                context, REQUEST_CODE_APP_BEAUTY_SERVICE, resultIntent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
        }
        return resultPendingIntent
    }
}