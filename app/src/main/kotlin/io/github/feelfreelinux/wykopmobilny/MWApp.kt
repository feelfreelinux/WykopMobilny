package io.github.feelfreelinux.wykopmobilny

import android.app.Application
import android.app.NotificationManager
import android.content.Context
import com.github.salomonbrys.kodein.*
import io.github.feelfreelinux.wykopmobilny.utils.api.ApiPreferences
import io.github.feelfreelinux.wykopmobilny.api.WykopApi
import io.github.feelfreelinux.wykopmobilny.api.WykopApiManager
import io.github.feelfreelinux.wykopmobilny.ui.notifications.WykopNotificationManager

class MWApp : Application(), KodeinAware {
    override fun onCreate() {
        super.onCreate()
    }

    override val kodein by Kodein.lazy {
        bind() from singleton { ApiPreferences(this@MWApp) }
        bind() from singleton { WykopApiManager(instance()) as WykopApi }
        bind() from singleton { WykopNotificationManager(getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager) }
    }
}
