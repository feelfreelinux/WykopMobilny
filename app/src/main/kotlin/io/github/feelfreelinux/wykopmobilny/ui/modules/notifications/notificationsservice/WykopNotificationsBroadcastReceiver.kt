package io.github.feelfreelinux.wykopmobilny.ui.modules.notifications.notificationsservice

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import io.github.feelfreelinux.wykopmobilny.WykopApp
import io.github.feelfreelinux.wykopmobilny.utils.SettingsPreferencesApi
import javax.inject.Inject

class WykopNotificationsBroadcastReceiver : BroadcastReceiver() {
    @Inject lateinit var settingsApi : SettingsPreferencesApi

    override fun onReceive(context: Context, intent: Intent) {
        WykopApp.uiInjector.inject(this)

        intent.action?.apply {
            if (this == "android.intent.action.BOOT_COMPLETED") {
                WykopNotificationsJob.schedule(settingsApi)
            }
        }
    }
}