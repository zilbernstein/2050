package ru.digipeople.locotech.core.helper

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import ru.digipeople.locotech.core.R
import ru.digipeople.locotech.core.ui.activity.auth.AuthActivity
import ru.digipeople.utils.android.AndroidUtils
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Класс для создания/показа уведомлений от пушей
 */
@Singleton
class NotificationHelper @Inject constructor(private val context: Context) {

    private val notificationManager = NotificationManagerCompat.from(context)
    private val channelId = context.getString(R.string.notification_helper_default_channel_id)

    fun subscribeToNotificationsAsync() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        if (notificationManager.getNotificationChannel(channelId) != null) {
            // Channel already exists
            return
        }

        val channel = NotificationChannel(
                channelId,
                context.getString(R.string.notification_helper_default_channel_name),
                NotificationManager.IMPORTANCE_DEFAULT)
        channel.description = context.getString(R.string.notification_helper_default_channel_description)
/*
        * Звук уведомления
        */
        val soundUri = AndroidUtils.uriFromResource(R.raw.gudok, context)
        val audioAttributes = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_UNKNOWN)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build()
        channel.setSound(soundUri, audioAttributes)

        notificationManager.createNotificationChannel(channel)
    }
    /*
     * Отображение уведомления
     */
    fun showNotification(title: String, message: String) {
        val notification = createNotification(context.getString(R.string.notification_helper_default_channel_id), title, message)

        val intent = AuthActivity.getCallingIntent(context)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK

        val pIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        notification.setContentIntent(pIntent)

        notificationManager.notify(NotificationId.next, notification.build())
    }
    /*
     * Создание уведомления
     */
    private fun createNotification(channel: String, title: String, message: String): NotificationCompat.Builder {
        return NotificationCompat.Builder(context, channel).apply {
            setSmallIcon(R.drawable.ic_logo)
            setContentTitle(title)
            setContentText(message)
            setAutoCancel(true)
            setSound(AndroidUtils.uriFromResource(R.raw.gudok, context));
        }
    }
    /*
         * Очистка
         */
    fun clearAll() {
        NotificationManagerCompat.from(context).cancelAll()
    }

    private object NotificationId {
        private val counter = AtomicInteger(0)
        val next: Int
            get() = counter.incrementAndGet()
    }
}