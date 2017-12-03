package io.github.feelfreelinux.wykopmobilny.ui.modules.notifications

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import java.util.concurrent.atomic.AtomicInteger

const val NOTIFICATION_TAG = "io.github.com.feelfreelinux.wykopmobilny"



interface WykopNotificationManagerApi {
    fun getNewId() : Int
    fun updateNotification(id : Int, notification : Notification)
    fun cancelNotification(id : Int)
}

class WykopNotificationManager(private val notificationManager : NotificationManager) : WykopNotificationManagerApi {
    private val ids = AtomicInteger(0)

    companion object {
        val NOTIFICATION_CHANNEL_ID = "wykopmobilny-notification"
    }

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            notificationManager.createNotificationChannel(
                    NotificationChannel(NOTIFICATION_CHANNEL_ID,
                            NOTIFICATION_CHANNEL_ID,
                            NotificationManager.IMPORTANCE_HIGH))
    }

    override fun getNewId() : Int = ids.incrementAndGet()

    override fun updateNotification(id : Int, notification : Notification) {
        notificationManager.notify(NOTIFICATION_TAG, id, notification)
    }

    override fun cancelNotification(id : Int) = notificationManager.cancel(NOTIFICATION_TAG, id)
}