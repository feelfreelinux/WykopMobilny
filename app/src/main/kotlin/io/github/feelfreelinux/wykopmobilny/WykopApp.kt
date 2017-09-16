package io.github.feelfreelinux.wykopmobilny

import android.app.Application
import android.content.Context
import com.squareup.leakcanary.LeakCanary
import com.squareup.leakcanary.RefWatcher
import io.github.feelfreelinux.wykopmobilny.di.AppModule
import io.github.feelfreelinux.wykopmobilny.di.components.DaggerInjector
import io.github.feelfreelinux.wykopmobilny.di.components.Injector
import io.github.feelfreelinux.wykopmobilny.di.modules.NetworkModule
import io.github.feelfreelinux.wykopmobilny.di.modules.PresentersModule
import io.github.feelfreelinux.wykopmobilny.di.modules.RepositoryModule
import io.github.feelfreelinux.wykopmobilny.di.modules.ViewPresentersModule


class WykopApp : Application() {
    companion object {
        lateinit var uiInjector: Injector
        val WYKOP_API_URL = "https://a.wykop.pl"
    }

    private var refWatcher: RefWatcher? = null

    fun getRefWatcher(context: Context): RefWatcher {
        val application = context.applicationContext as WykopApp
        return application.refWatcher!!
    }
    override fun onCreate() {
        super.onCreate()
        refWatcher = LeakCanary.install(this)


        uiInjector = DaggerInjector.builder()
                .appModule(AppModule(this))
                .networkModule(NetworkModule(WYKOP_API_URL))
                .repositoryModule(RepositoryModule())
                .presentersModule(PresentersModule())
                .viewPresentersModule(ViewPresentersModule())
                .build()
    }
}
