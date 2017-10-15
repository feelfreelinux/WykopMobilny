package io.github.feelfreelinux.wykopmobilny.ui.modules.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class WykopNotificationsBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        WykopNotificationsJob.shedule()
    }
}