package com.example.dream_ias.activity.firebaseNotification

import android.annotation.SuppressLint
import android.app.*
import android.app.ActivityManager.RunningAppProcessInfo
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.AudioAttributes
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.dream_ias.R
import com.example.dream_ias.activity.HomeActivity
import com.example.dream_ias.util.App
import com.example.dream_ias.util.PushKeys
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import java.util.*


class MyFireBaseMessagingServices : FirebaseMessagingService() {
    var tvTitleName: String = ""
    var tvBodyName: String = ""
    override fun onNewToken(p0: String) {
        Log.e("Device Token  ==>>", p0)
        App.app.prefManager.deviceToken = p0

    }

    fun isAppIsInBackground(context: Context): Boolean {
        var isInBackground = true
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val runningProcesses = am.runningAppProcesses
        for (processInfo in runningProcesses) {
            if (processInfo.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                for (activeProcess in processInfo.pkgList) {
                    if (activeProcess == context.getPackageName()) {
                        isInBackground = false
                    }
                }
            }
        }
        return isInBackground
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(
            TAG,
            "onMessageReceived: notification object ${Gson().toJson(remoteMessage.notification)}"
        )
        var intent = Intent()
        for (key in remoteMessage.data.keys) {
            println("-----reee key : $key || value : ${remoteMessage.data[key]}")
        }
        tvTitleName = remoteMessage.data[PushKeys.title.name] ?: ""
        tvBodyName = remoteMessage.data[PushKeys.body.name] ?: ""
        createNotificationChannelDefault(this)

        /*val pendingINtet = PendingIntent.getService(
            this,
            1,
            Intent(this, HomeActivity::class.java),
            PendingIntent.FLAG_MUTABLE
        )*/
        val paddingIntentRedirect =
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O) {
                PendingIntent.getActivity(
                    this, getUniqueId(), intent, PendingIntent.FLAG_IMMUTABLE
                )
            } else {
                val stackBuilder = TaskStackBuilder.create(this)
                    .addParentStack(HomeActivity::class.java).addNextIntent(intent)
                stackBuilder.getPendingIntent(
                    getUniqueId(), PendingIntent.FLAG_IMMUTABLE
                )
            }
        showNotificationDefault(paddingIntentRedirect, tvTitleName, tvBodyName)
    }


    @SuppressLint("MissingPermission", "DiscouragedApi")
    private fun showNotificationDefault(
        paddingIntentRedirect: PendingIntent, title: String, text: String
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val builder =
                Notification.Builder(
                    this,
                    getString(R.string.default_notification_channel_id)
                )
                    .setSmallIcon(R.drawable.app_logo).setLargeIcon(
                        BitmapFactory.decodeResource(
                            resources, R.drawable.app_logo
                        )
                    ).setContentTitle(title).setContentText(text)
                    .setPriority(Notification.PRIORITY_MAX).setCategory(Notification.CATEGORY_CALL)
                    .setContentIntent(paddingIntentRedirect).setDefaults(0)
                    .setAutoCancel(true) // Dismiss the notification when clicked
            with(NotificationManagerCompat.from(this)) {
                notify(notificationId, builder.build())
            }
        } else {
            val builder = NotificationCompat.Builder(
                this, getString(R.string.default_notification_channel_id)
            ).setSmallIcon(R.drawable.app_logo).setLargeIcon(
                BitmapFactory.decodeResource(
                    resources, R.drawable.app_logo
                )
            ).setContentTitle(title).setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_MAX).setDefaults(0)
                .setContentIntent(paddingIntentRedirect)
                .setCategory(NotificationCompat.CATEGORY_CALL)
                .setAutoCancel(true) // Dismiss the notification when clicked
            with(NotificationManagerCompat.from(this)) {
                notify(notificationId, builder.build())
            }
        }

    }

    companion object {

        private fun getUniqueId() = ((System.currentTimeMillis() % 10000).toInt())
        private val TAG = MyFireBaseMessagingServices::class.java.simpleName
        fun createNotificationChannelDefault(context: Context) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val notificationManager =
                    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                val name = "All Notification Channel"
                val descriptionText = "Channel for All Notifications"
                val importance = NotificationManager.IMPORTANCE_HIGH
//                val soundUri =
//                    Uri.parse("android.resource://${context.packageName}/${R.raw.telephone_ringtone_new}")

                val audioAttributes = AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION).build()
                val channel = NotificationChannel(
                    context.getString(R.string.default_notification_channel_id),
                    name,
                    importance
                ).apply {
                    description = descriptionText
//                    setSound(soundUri, audioAttributes)
                }
                notificationManager.createNotificationChannel(channel)
            }
        }


        private val notificationId get() = Random().nextInt()
    }
}


