package io.github.wykopmobilny

import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import io.github.wykopmobilny.di.DaggerTestAppComponent
import io.github.wykopmobilny.fakes.FakeCookieProvider
import io.github.wykopmobilny.storage.android.DaggerStoragesComponent
import okhttp3.OkHttpClient
import javax.inject.Inject

class TestApp : WykopApp() {

    private val okHttpClient = OkHttpClient()

    @Inject
    lateinit var cookieProvider: FakeCookieProvider

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> =
        DaggerTestAppComponent.factory()
            .create(
                instance = this,
                okHttpClient = okHttpClient,
                wykop = daggerWykop().create(
                    okHttpClient = okHttpClient,
                    baseUrl = "http://localhost:8000",
                    appKey = "fixture-app-key",
                    signingInterceptor = { it.proceed(it.request()) },
                    cacheDir = cacheDir.resolve("tests/okhttp-cache"),
                ),
                patrons = daggerPatrons().create(
                    okHttpClient = okHttpClient,
                    baseUrl = "http://localhost:8000",
                ),
                scraper = daggerScraper().create(
                    okHttpClient = okHttpClient,
                    baseUrl = "http://localhost:8000",
                    cookieProvider = { cookieProvider.cookieForSite(it) },
                ),
                storages = DaggerStoragesComponent.factory().create(this),
            )

    companion object {
        lateinit var instance: TestApp
    }
}
