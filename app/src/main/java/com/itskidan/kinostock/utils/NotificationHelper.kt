package com.itskidan.kinostock.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.itskidan.core_api.entity.Film
import com.itskidan.kinostock.R
import com.itskidan.kinostock.view.MainActivity


class NotificationHelper(private val context: Context) {

    private val channelId = Constants.NOTIFICATION_CHANNEL_ID
    private val channelName = context.getString(R.string.channel_name)
    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    init {
        createNotificationChannel()
    }

    fun showFilmNotification(film: Film) {
        // create values for notification
        val titleNotification = context.getString(R.string.title_notification)
        val messageNotification =
            "${context.getString(R.string.message_notification)}: ${film.title}"
        // create intent with film for PendingIntent
        val intent = Intent(context, MainActivity::class.java)
        intent.putExtra(Constants.NOTIFICATION_FILM, film)
        intent.putExtra(Constants.NOTIFICATION_FRAGMENT, Constants.DETAIL_FRAGMENT)
        //create flag
        val flag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            PendingIntent.FLAG_IMMUTABLE
        } else{
            0
        }
        // create pending Intent
        val pendingIntent = PendingIntent.getActivity(
            context,
            Constants.NOTIFICATION_PENDING_INTENT_REQUEST_CODE,
            intent,
            flag
        )
        // create notification for all versions
        val notification = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationCompat.Builder(context, channelId)
                .setContentTitle(titleNotification)
                .setContentText(messageNotification)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .build()
        } else {
            Notification.Builder(context)
                .setContentTitle(titleNotification)
                .setContentText(messageNotification)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setAutoCancel(true)
                .setPriority(Notification.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .build()
        }

        notificationManager.notify(1, notification)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Default notification channel"
            }
            notificationManager.createNotificationChannel(channel)
        }
    }
}