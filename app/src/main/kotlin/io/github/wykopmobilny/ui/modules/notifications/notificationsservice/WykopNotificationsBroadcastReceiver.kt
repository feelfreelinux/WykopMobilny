package io.github.wykopmobilny.ui.modules.notifications.notificationsservice

import android.content.Context
import android.content.Intent
import dagger.android.DaggerBroadcastReceiver
import io.github.wykopmobilny.storage.api.SettingsPreferencesApi
import javax.inject.Inject

class WykopNotificationsBroadcastReceiver : DaggerBroadcastReceiver() {

    @Inject
    lateinit var settingsApi: SettingsPreferencesApi

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        intent.action?.apply {
            if (this == "android.intent.action.BOOT_COMPLETED") {
                WykopNotificationsJob.schedule(settingsApi)
            }
        }
    }
}