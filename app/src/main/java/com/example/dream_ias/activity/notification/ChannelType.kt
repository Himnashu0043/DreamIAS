package com.example.dream_ias.activity.notification

import android.app.Notification
import android.app.NotificationManager
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat

enum class ChannelType(
    val channelId: String,
    val channelName: String,
    val channelDescription: String,
    val importance: Int = NotificationManager.IMPORTANCE_DEFAULT,
    val soundUri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION),
    val notificationPriority: Int = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) Notification.PRIORITY_DEFAULT else NotificationCompat.PRIORITY_DEFAULT,
    val category: String = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) Notification.CATEGORY_MESSAGE else NotificationCompat.CATEGORY_MESSAGE
) {
    DEFAULT_CHANNEL(
        "id_default_channel",
        "Default channel",
        "THis channel is for default notifications"
    ),
    LIVE_STREAMING("id_live_streaming", "Live Streaming", "This channel is for Live Streaming"),
    NEW_CONTESTS("id_new_contests", "New Contests", "This channel is for new Contests"),
    PRIZE_WINNING("id_prize_winning", "Prize Winning", "This channel is for Prize Winning"),
    SUBSCRIPTION_REMINDER(
        "id_subscription_reminder",
        "Subscription reminder",
        "This channel is for Subscription Reminder"
    )
}