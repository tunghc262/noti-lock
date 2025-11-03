package com.example.myapplication

import android.app.Application
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import dagger.hilt.android.HiltAndroidApp
import jakarta.inject.Inject


@HiltAndroidApp
class App : Application(), DefaultLifecycleObserver{

    companion object {
        var isAppStarted = false
    }

    @Inject
    lateinit var serviceNotificationManager: ServiceNotificationManager

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        isAppStarted = true
    }

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
        isAppStarted = false
    }

}