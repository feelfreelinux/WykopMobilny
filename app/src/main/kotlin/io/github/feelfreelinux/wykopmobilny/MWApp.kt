package io.github.feelfreelinux.wykopmobilny

import android.app.Application
import com.github.salomonbrys.kodein.*
import io.github.feelfreelinux.wykopmobilny.presenters.WykopWebViewClient
import io.github.feelfreelinux.wykopmobilny.utils.ApiPreferences
import io.github.feelfreelinux.wykopmobilny.utils.WykopApiManager

class MWApp : Application(), KodeinAware {
    override fun onCreate() {
        super.onCreate()
    }

    override val kodein by Kodein.lazy {
        bind() from singleton { ApiPreferences(this@MWApp) }
        bind() from singleton { WykopApiManager(this@MWApp, instance()) }
        bind() from singleton { WykopWebViewClient(instance()) }
    }
}
