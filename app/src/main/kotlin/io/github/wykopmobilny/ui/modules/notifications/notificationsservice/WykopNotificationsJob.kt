package io.github.wykopmobilny.ui.modules.notifications.notificationsservice

import android.app.Notification
import android.app.PendingIntent
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import androidx.core.app.NotificationCompat
import com.evernote.android.job.Job
import com.evernote.android.job.JobManager
import com.evernote.android.job.JobRequest
import io.github.wykopmobilny.R
import io.github.wykopmobilny.ui.modules.notifications.WykopNotificationManager
import io.github.wykopmobilny.ui.modules.notifications.WykopNotificationManagerApi
import io.github.wykopmobilny.ui.modules.notificationslist.NotificationsListActivity
import io.github.wykopmobilny.utils.linkhandler.WykopLinkHandler
import io.github.wykopmobilny.utils.preferences.SettingsPreferencesApi
import java.util.concurrent.TimeUnit

class WykopNotificationsJob(
    val presenter: WykopNotificationsJobPresenter,
    val settingsApi: SettingsPreferencesApi,
    private val notificationManager: WykopNotificationManagerApi
) : Job(), WykopNotificationsJobView {

    companion object {
        const val TAG = "wykop_notifications_job"
        const val NOTIFICATION_ID = 15

        fun schedule(settingsPreferencesApi: SettingsPreferencesApi) {
            var build: JobRequest? = null
            if (settingsPreferencesApi.showNotifications) {
                build = JobRequest.Builder(TAG)
                    .setPeriodic(TimeUnit.MINUTES.toMillis(15.toLong()), TimeUnit.MINUTES.toMillis(5))
                    .setRequiredNetworkType(JobRequest.NetworkType.CONNECTED)
                    .setRequirementsEnforced(true)
                    .setUpdateCurrent(true)
                    .build()
                build.schedule()
            } else {
                build?.cancelAndEdit()
            }
        }

        fun cancel() {
            JobManager.instance().cancelAllForTag(TAG)
        }

        fun scheduleOnce() {
            val build: JobRequest = JobRequest.Builder(TAG)
                .startNow()
                .build()
            build.schedule()
        }
    }

    private fun buildNotification(body: String, intent: PendingIntent): Notification {
        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        return NotificationCompat.Builder(context, WykopNotificationManager.NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_wykopmobilny)
            .setContentIntent(intent)
            .setContentTitle(context.getString(R.string.app_name))
            .setSound(alarmSound)
            .setAutoCancel(true)
            .setOnlyAlertOnce(true)
            .setDeleteIntent(PendingIntent.getBroadcast(context, 0, Intent(context, ReadNotificationsBroadcastReceiver::class.java), 0))
            .setContentText(body).build()
    }

    override fun onRunJob(params: Params): Result {
        if (settingsApi.showNotifications || settingsApi.piggyBackPushNotifications) {
            presenter.apply {
                subscribe(this@WykopNotificationsJob)
                checkNotifications()
            }
        }
        return Result.SUCCESS
    }

    override fun showNotification(notification: io.github.wykopmobilny.models.dataclass.Notification) {
        if (settingsApi.showNotifications || settingsApi.piggyBackPushNotifications) {
            val intent = WykopLinkHandler.getLinkIntent(notification.url!!, context)
            intent?.action = System.currentTimeMillis().toString()
            val pendingIntent = if (intent != null) {
                intent.flags = (Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
            } else {
                val notificationIntent = Intent(Intent.ACTION_VIEW, Uri.parse(notification.url))
                PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT)
            }
            notificationManager.updateNotification(
                NOTIFICATION_ID,
                buildNotification(notification.body, pendingIntent),
            )
        }
    }

    override fun showNotificationsCount(count: Int) {
        if (settingsApi.showNotifications || settingsApi.piggyBackPushNotifications) {
            // Create intent
            val intent = NotificationsListActivity.createIntent(context)
            intent.action = System.currentTimeMillis().toString()
            intent.flags = (Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

            // Show Notification
            notificationManager.updateNotification(
                NOTIFICATION_ID,
                buildNotification(context.getString(R.string.notificationsCountMessage, count), pendingIntent)
            )
        }
    }

    override fun cancelNotification() = notificationManager.cancelNotification(NOTIFICATION_ID)

    override fun showErrorDialog(e: Throwable) = Unit // @TODO idk how to handle it
}
