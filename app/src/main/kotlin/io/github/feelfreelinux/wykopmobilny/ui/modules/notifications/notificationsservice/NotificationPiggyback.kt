package io.github.feelfreelinux.wykopmobilny.ui.modules.notifications.notificationsservice

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import io.github.feelfreelinux.wykopmobilny.utils.preferences.SettingsPreferences

/***
 * `NotificationPiggyback` captures notifications from official wykop.pl application,
 * and runs singleshot notification job instead.
 */
class NotificationPiggyback : NotificationListenerService() {

    val settingsPreferencesApi by lazy { SettingsPreferences(applicationContext) }

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        if (settingsPreferencesApi.piggyBackPushNotifications) {
            val packageName = sbn.packageName
            if (packageName.isNotEmpty() && packageName == "pl.wykop.droid" || packageName == "com.tylerr147.notisend") {
                cancelNotification(sbn.key)
                WykopNotificationsJob.scheduleOnce()
            }
        }
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification) = Unit
}
