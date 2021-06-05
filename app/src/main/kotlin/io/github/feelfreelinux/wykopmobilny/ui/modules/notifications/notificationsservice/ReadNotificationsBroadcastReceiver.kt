package io.github.feelfreelinux.wykopmobilny.ui.modules.notifications.notificationsservice

import android.content.Context
import android.content.Intent
import dagger.android.DaggerBroadcastReceiver
import io.github.feelfreelinux.wykopmobilny.api.notifications.NotificationsApi
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ReadNotificationsBroadcastReceiver : DaggerBroadcastReceiver() {

    @Inject lateinit var notificationsApi: NotificationsApi

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        notificationsApi.readNotifications().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({}, {})
    }
}
