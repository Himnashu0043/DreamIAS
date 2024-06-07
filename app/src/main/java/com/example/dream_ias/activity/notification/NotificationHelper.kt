package com.example.dream_ias.activity.notification

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.AudioAttributes
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.dream_ias.MainActivity
import com.example.dream_ias.R
import java.util.Random

class NotificationHelper {

    fun createChannels(context: Context) {
        for (channel in ChannelType.values()) {
            createNotificationChannel(context, channel)
        }
    }

    private fun createNotificationChannel(context: Context, channelType: ChannelType) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val soundUri = Uri.parse("android.resource://${context.packageName}/${R.raw.telephone_ringtone_new}")
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val audioAttributes = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build()
            val channel = NotificationChannel(
                channelType.channelId,
                channelType.channelDescription,
                channelType.importance
            ).apply {
                description = channelType.channelDescription
                setSound(channelType.soundUri, audioAttributes)
            }
            notificationManager.createNotificationChannel(channel)
        }
    }

    @SuppressLint("MissingPermission", "DiscouragedApi", "WrongConstant")
    fun showNotification(
        context: Context,
        intent: Intent,
        title: String,
        description: String,
        type: ChannelType = ChannelType.DEFAULT_CHANNEL
    ) {

//        val soundResourceId =
//            context.resources.getIdentifier("telephone_ringtone_new", "raw", context.packageName)

        val paddingIntentRedirect = if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O) {
            PendingIntent.getActivity(
                context, getUniqueId(), intent, PendingIntent.FLAG_IMMUTABLE
            )
        } else {
            val stackBuilder = TaskStackBuilder.create(context)
                .addParentStack(MainActivity::class.java)
                .addNextIntent(intent)
            stackBuilder
                .getPendingIntent(getUniqueId(), PendingIntent.FLAG_IMMUTABLE)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val builder =
                Notification.Builder(context, type.channelId)
                    .setSmallIcon(R.drawable.splash_logo)
                    .setLargeIcon(
                        BitmapFactory.decodeResource(
                            context.resources,
                            R.drawable.splash_logo
                        )
                    )
                    .setContentTitle(title)
                    .setContentText(description)
                    .setPriority(type.notificationPriority)
                    .setCategory(type.category)
                    .setContentIntent(paddingIntentRedirect)
                    .setDefaults(0)
                    .setAutoCancel(true) // Dismiss the notification when clicked
                    .setSound(type.soundUri)
            with(NotificationManagerCompat.from(context)) {
                notify(notificationId, builder.build())
            }
        } else {
            val builder = NotificationCompat.Builder(
                context,
                type.channelId
            )
                .setSmallIcon(R.drawable.splash_logo)
                .setLargeIcon(
                    BitmapFactory.decodeResource(
                        context.resources,
                        R.drawable.splash_logo
                    )
                )
                .setContentTitle(title)
                .setContentText(description)
                .setPriority(type.notificationPriority)
                .setDefaults(0)
                .setContentIntent(paddingIntentRedirect)
                .setCategory(type.category)
                .setAutoCancel(true) // Dismiss the notification when clicked
                .setSound(type.soundUri)
            with(NotificationManagerCompat.from(context)) {
                notify(notificationId, builder.build())
            }
        }

    }

    private val notificationId get() = Random().nextInt()
    private fun getUniqueId() = ((System.currentTimeMillis() % 10000).toInt())

}