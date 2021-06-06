package io.github.wykopmobilny

import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import io.github.wykopmobilny.blacklist.remote.DaggerScraperComponent
import io.github.wykopmobilny.di.DaggerTestAppComponent
import io.github.wykopmobilny.fakes.FakeCookieProvider
import io.github.wykopmobilny.patrons.remote.DaggerPatronsComponent
import io.github.wykopmobilny.storage.android.DaggerStoragesComponent
import io.github.wykopmobilny.wykop.remote.DaggerWykopComponent
import okhttp3.OkHttpClient
import javax.inject.Inject

class TestApp : WykopApp() {

    private val okHttpClient = OkHttpClient()

    @Inject
    lateinit var cookieProvider: FakeCookieProvider

    override fun onCreate() {
        super.onCreate()
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> =
        DaggerTestAppComponent.factory()
            .create(
                instance = this,
                okHttpClient = okHttpClient,
                wykop = DaggerWykopComponent.factory().create(
                    okHttpClient = okHttpClient,
                    apiUrl = "http://localhost:8000",
                ),
                patrons = DaggerPatronsComponent.factory().create(
                    okHttpClient = okHttpClient,
                    baseUrl = "http://localhost:8000",
                ),
                scraper = DaggerScraperComponent.factory().create(
                    okHttpClient = okHttpClient,
                    baseUrl = "http://localhost:8000",
                    cookieProvider = { cookieProvider.cookieForSite(it) },
                ),
                storages = DaggerStoragesComponent.factory().create(this),
            )
}
