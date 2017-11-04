package io.github.feelfreelinux.wykopmobilny.ui.modules.notifications.notificationsservice

import android.app.Notification
import android.app.PendingIntent
import android.content.Intent
import android.support.v4.app.NotificationCompat
import com.evernote.android.job.Job
import com.evernote.android.job.JobRequest
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.WykopApp
import io.github.feelfreelinux.wykopmobilny.ui.modules.mainnavigation.NavigationActivity
import io.github.feelfreelinux.wykopmobilny.ui.modules.notifications.WykopNotificationManager
import io.github.feelfreelinux.wykopmobilny.ui.modules.notifications.WykopNotificationManagerApi
import io.github.feelfreelinux.wykopmobilny.utils.SettingsPreferencesApi
import io.github.feelfreelinux.wykopmobilny.utils.wykop_link_handler.WykopLinkHandlerApi
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class WykopNotificationsJob : Job(), WykopNotificationsJobView {
    @Inject lateinit var presenter : WykopNotificationsJobPresenter
    @Inject lateinit var wykopLinkHandler : WykopLinkHandlerApi
    @Inject lateinit var notificationManager : WykopNotificationManagerApi

    private fun buildNotification(body : String, intent : PendingIntent): Notification {
        return NotificationCompat.Builder(context, WykopNotificationManager.NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_wykopmobilny)
                .setContentIntent(intent)
                .setContentTitle(context.getString(R.string.app_name))
                .setAutoCancel(true)
                .setOnlyAlertOnce(true)
                .setContentText(body).build()

    }

    companion object {
        val TAG = "wykop_notifications_job"
        val NOTIFICATION_ID = 15

        fun schedule(settingsPreferencesApi: SettingsPreferencesApi) {
            JobRequest.Builder(TAG)
                    .setPeriodic(TimeUnit.MINUTES.toMillis(settingsPreferencesApi.notificationsSchedulerDelay!!.toLong()), TimeUnit.MINUTES.toMillis(5))
                    .setRequiredNetworkType(JobRequest.NetworkType.CONNECTED)
                    .setUpdateCurrent(true)
                    .build()
                    .schedule()
        }
    }

    override fun onRunJob(params: Params): Result {
        WykopApp.uiInjector.inject(this)

        presenter.apply {
            subscribe(this@WykopNotificationsJob)
            checkNotifications()
        }
        return Result.SUCCESS
    }

    override fun showNotification(notification: io.github.feelfreelinux.wykopmobilny.models.dataclass.Notification) {
        val intent = wykopLinkHandler.getLinkIntent(context, notification.url)
        intent?.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
        notificationManager.updateNotification(NOTIFICATION_ID, buildNotification(notification.body, pendingIntent))
    }

    override fun showNotificationsCount(count: Int) {
        // Create intent
        val intent = NavigationActivity.getIntent(context, NavigationActivity.TARGET_NOTIFICATIONS)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)

        // Show Notification
        notificationManager.updateNotification(NOTIFICATION_ID,
                buildNotification(context.getString(R.string.notificationsCountMessage, count), pendingIntent))

    }

    override fun showErrorDialog(e: Throwable) {} // @TODO idk how to handle it

}