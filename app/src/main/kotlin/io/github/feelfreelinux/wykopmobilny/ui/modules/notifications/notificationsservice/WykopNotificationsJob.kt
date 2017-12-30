package io.github.feelfreelinux.wykopmobilny.ui.modules.notifications.notificationsservice

import android.app.Notification
import android.app.PendingIntent
import android.content.Intent
import android.net.Uri
import android.support.v4.app.NotificationCompat
import com.evernote.android.job.Job
import com.evernote.android.job.JobRequest
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.WykopApp
import io.github.feelfreelinux.wykopmobilny.ui.modules.mainnavigation.MainNavigationActivity
import io.github.feelfreelinux.wykopmobilny.ui.modules.notifications.WykopNotificationManager
import io.github.feelfreelinux.wykopmobilny.ui.modules.notifications.WykopNotificationManagerApi
import io.github.feelfreelinux.wykopmobilny.utils.SettingsPreferencesApi
import io.github.feelfreelinux.wykopmobilny.utils.wykop_link_handler.WykopLinkHandlerApi
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class WykopNotificationsJob : Job(), WykopNotificationsJobView {
    @Inject lateinit var presenter: WykopNotificationsJobPresenter
    @Inject lateinit var settingsApi: SettingsPreferencesApi
    @Inject lateinit var wykopLinkHandler: WykopLinkHandlerApi
    @Inject lateinit var notificationManager: WykopNotificationManagerApi

    private fun buildNotification(body: String, intent: PendingIntent): Notification {
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
            var build: JobRequest? = null
            if (settingsPreferencesApi.showNotifications) {
                build = JobRequest.Builder(TAG)
                        .setPeriodic(TimeUnit.MINUTES.toMillis(15.toLong()), TimeUnit.MINUTES.toMillis(5))
                        .setRequiredNetworkType(JobRequest.NetworkType.CONNECTED)
                        .setUpdateCurrent(true)
                        .build()
                build.schedule()
            } else {
                build?.cancelAndEdit()
            }
        }
    }

    override fun onRunJob(params: Params): Result {
        WykopApp.uiInjector.inject(this)
        if (settingsApi.showNotifications) {
            presenter.apply {
                subscribe(this@WykopNotificationsJob)
                checkNotifications()
            }
        }
        return Result.SUCCESS
    }

    override fun showNotification(notification: io.github.feelfreelinux.wykopmobilny.models.dataclass.Notification) {
        if (settingsApi.showNotifications) {
            val intent = wykopLinkHandler.getLinkIntent(context, notification.url!!)
            intent?.action = System.currentTimeMillis().toString()
            val pendingIntent = if (intent != null) {
                intent.flags = (Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

            } else {
                val notificationIntent = Intent(Intent.ACTION_VIEW, Uri.parse(notification.url))
                PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT)
            }
            notificationManager.updateNotification(WykopNotificationsJob.NOTIFICATION_ID, buildNotification(notification.body, pendingIntent))

        }
    }
    override fun showNotificationsCount(count: Int) {
        if (settingsApi.showNotifications) {
            // Create intent
            val intent = MainNavigationActivity.getIntent(context, MainNavigationActivity.TARGET_NOTIFICATIONS)
            intent.action = System.currentTimeMillis().toString()
            intent.flags = (Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

            // Show Notification
            notificationManager.updateNotification(NOTIFICATION_ID,
                    buildNotification(context.getString(R.string.notificationsCountMessage, count), pendingIntent))

        }
    }

    override fun cancelNotification() {
        notificationManager.cancelNotification(NOTIFICATION_ID)
    }

    override fun showErrorDialog(e: Throwable) {} // @TODO idk how to handle it

}