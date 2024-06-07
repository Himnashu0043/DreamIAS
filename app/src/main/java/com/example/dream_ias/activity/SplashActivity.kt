package com.example.dream_ias.activity

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.core.content.edit
import com.example.dream_ias.activity.login.LoginActivity
import com.example.dream_ias.activity.testInfo.VideoTipsActivity
import com.example.dream_ias.databinding.ActivitySplashBinding
import com.example.dream_ias.util.App
import com.example.dream_ias.util.CommonUtil
import com.example.dream_ias.util.Constants.PREF_NAME
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging

class SplashActivity : AppCompatActivity() {
    lateinit var binding: ActivitySplashBinding
    private var isTutorial: Boolean = true
    private var deviceToken: String? = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        CommonUtil.clearLightStatusBar(this)
        CommonUtil.themeSet(this, window)
        isTutorial = App.app.prefManager.isTutorial

        //startActivity(Intent(this, VideoTipsActivity::class.java))
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("TAG", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            deviceToken = task.result
            Log.d(ContentValues.TAG, "DeviceToken==>>: ${deviceToken} ")
            getSharedPreferences(PREF_NAME, MODE_PRIVATE).edit {
                App.app.prefManager.deviceToken = deviceToken!!
                apply()
            }
            App.app.prefManager.deviceToken = deviceToken!!
        })
        Handler(Looper.getMainLooper()).postDelayed({
            if (isTutorial) {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            } else {
                startActivity(Intent(this, TourActivity::class.java))
                finish()
            }

        }, 2000)
    }


}