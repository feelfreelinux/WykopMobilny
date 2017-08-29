package io.github.feelfreelinux.wykopmobilny.ui.notifications

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import java.util.concurrent.atomic.AtomicInteger

const val NOTIFICATION_TAG = "io.github.com.feelfreelinux.wykopmobilny"
const val UPLOADING_NOTIFICATION_CHANNEL_ID = "wykopmobilny-uploading"

class WykopNotificationManager(private val notificationManager : NotificationManager) {
    private val ids = AtomicInteger(0)

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            notificationManager.createNotificationChannel(
                    NotificationChannel(UPLOADING_NOTIFICATION_CHANNEL_ID,
                            UPLOADING_NOTIFICATION_CHANNEL_ID,
                            NotificationManager.IMPORTANCE_DEFAULT))
    }

    fun getNewId() : Int = ids.incrementAndGet()

    fun updateNotification(id : Int, notification : Notification) {
        notificationManager.notify(NOTIFICATION_TAG, id, notification)
    }

    fun cancelNotification(id : Int) = notificationManager.cancel(NOTIFICATION_TAG, id)
}