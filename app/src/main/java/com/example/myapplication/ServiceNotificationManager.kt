package com.example.myapplication

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
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
        const val REQUEST_CODE_APP_LOCKER_SERVICE = 112

    }

    fun createNotificationOffline(title: String, message: String) {
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
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(getResultPendingIntent())
            .setAutoCancel(true)
            .setShowWhen(false)
            .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
            .build()

        val notificationId = System.currentTimeMillis().toInt()
        notificationManager.notify(notificationId, notification)
    }

    private fun getResultPendingIntent(): PendingIntent {
        val resultIntent = Intent(context, SplashActivity::class.java)
        resultIntent.putExtra("from_notification", true)
        val resultPendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getActivity(context, REQUEST_CODE_APP_LOCKER_SERVICE, resultIntent, PendingIntent.FLAG_MUTABLE)
        } else {
            PendingIntent.getActivity(
                context, REQUEST_CODE_APP_LOCKER_SERVICE, resultIntent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
        }
        return resultPendingIntent
    }

    @SuppressLint("RemoteViewLayout")
    private fun createNotificationCustom(): Notification {
        val contentView = RemoteViews(context.packageName, R.layout.notification_applock)
        contentView.setTextViewText(R.id.title, "getDescriptionText()")
        return NotificationCompat.Builder(context, CHANNEL_ID_APP_LOCKER_SERVICE)
            .setSmallIcon(R.drawable.ic_launcher_background)
//            .setContentTitle(descriptionText)
            .setCustomContentView(contentView)
            .setPriority(NotificationCompat.PRIORITY_MIN)
            .setContentIntent(getResultPendingIntent())
            .setAutoCancel(false)
            .setShowWhen(false)
            .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
            .setOngoing(true)
            .build()
    }

    /*    private fun getDescriptionText(): String {
        val currentLanguage = context.getCurrentLanguageCode()
        val locale = Locale(currentLanguage)
        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)
        val localizedContext = context.createConfigurationContext(config)

        return when (appPreferences.currentIcon) {
            KeyIconAppValue.KEY_ICON_MESSAGES -> localizedContext.getString(
                R.string.camouflage_icon_app_notification_message, localizedContext.getString(R.string.alias_messages)
            )

            KeyIconAppValue.KEY_ICON_CALENDAR -> localizedContext.getString(
                R.string.camouflage_icon_app_notification_message, localizedContext.getString(R.string.alias_calendar)
            )

            KeyIconAppValue.KEY_ICON_CONTACTS -> localizedContext.getString(
                R.string.camouflage_icon_app_notification_message, localizedContext.getString(R.string.alias_contacts)
            )

            KeyIconAppValue.KEY_ICON_BROWSER -> localizedContext.getString(
                R.string.camouflage_icon_app_notification_message, localizedContext.getString(R.string.alias_browser)
            )

            KeyIconAppValue.KEY_ICON_MUSIC -> localizedContext.getString(
                R.string.camouflage_icon_app_notification_message, localizedContext.getString(R.string.alias_music)
            )

            KeyIconAppValue.KEY_ICON_WEATHER -> localizedContext.getString(
                R.string.camouflage_icon_app_notification_message, localizedContext.getString(R.string.alias_weather)
            )

            KeyIconAppValue.KEY_ICON_CLOCK -> localizedContext.getString(
                R.string.camouflage_icon_app_notification_message, localizedContext.getString(R.string.alias_alarm)
            )

            KeyIconAppValue.KEY_ICON_CALCULATE -> localizedContext.getString(
                R.string.camouflage_icon_app_notification_message, localizedContext.getString(R.string.alias_calculate)
            )

            else -> localizedContext.getString(R.string.notification_protecting_your_privacy)
        }
    }*/

}