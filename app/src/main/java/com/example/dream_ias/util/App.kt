package com.example.dream_ias.util

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.dream_ias.activity.notification.NotificationHelper
import java.io.File

class App : Application() {

    lateinit var prefManager: PrefManager
    var token: String = ""

    override fun onCreate() {
        super.onCreate()
        app = this
        prefManager = PrefManager.get(this)
        token = prefManager.accessToken
        NotificationHelper().createChannels(this)
        val dexOutputDir: File = codeCacheDir
        dexOutputDir.setReadOnly()
        ///Dark Mode off
       // AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        //dark mode off
    }

    companion object {
        lateinit var app: App
    }
}