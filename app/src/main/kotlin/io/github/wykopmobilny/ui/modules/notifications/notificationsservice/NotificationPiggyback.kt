package io.github.wykopmobilny.ui.modules.notifications.notificationsservice

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import dagger.android.AndroidInjection
import io.github.wykopmobilny.storage.api.SettingsPreferencesApi
import javax.inject.Inject

/***
 * `NotificationPiggyback` captures notifications from official wykop.pl application,
 * and runs singleshot notification job instead.
 */
class NotificationPiggyback : NotificationListenerService() {

    @Inject
    lateinit var settingsPreferencesApi: SettingsPreferencesApi

    override fun onCreate() {
        AndroidInjection.inject(this)
        super.onCreate()
    }
    override fun onNotificationPosted(sbn: StatusBarNotification) {
        if (settingsPreferencesApi.piggyBackPushNotifications) {
            if (sbn.packageName == "pl.wykop.droid" || sbn.packageName == "com.tylerr147.notisend") {
                cancelNotification(sbn.key)
                WykopNotificationsJob.scheduleOnce()
            }
        }
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification) = Unit
}
