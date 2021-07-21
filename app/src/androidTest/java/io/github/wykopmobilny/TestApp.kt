package io.github.wykopmobilny

import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import io.github.wykopmobilny.di.DaggerTestAppComponent
import io.github.wykopmobilny.domain.login.ConnectConfig
import io.github.wykopmobilny.fakes.FakeCookieProvider
import io.github.wykopmobilny.storage.android.DaggerStoragesComponent
import okhttp3.OkHttpClient
import javax.inject.Inject

internal class TestApp : WykopApp() {

    val okHttpClient = OkHttpClient()

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
                wykop = wykopApi,
                patrons = patrons,
                scraper = scraper,
                storages = storages,
                settingsInterop = domainComponent.settingsApiInterop(),
            )

    override val wykopApi by lazy {
        daggerWykop().create(
            okHttpClient = okHttpClient,
            baseUrl = "http://localhost:8000",
            appKey = "fixture-app-key",
            signingInterceptor = { it.proceed(it.request()) },
            cacheDir = cacheDir.resolve("tests/okhttp-cache"),
        )
    }
    override val patrons by lazy {
        daggerPatrons().create(
            okHttpClient = okHttpClient,
            baseUrl = "http://localhost:8000",
        )
    }

    override val scraper by lazy {
        daggerScraper().create(
            okHttpClient = okHttpClient,
            baseUrl = "http://localhost:8000",
            cookieProvider = { cookieProvider.cookieForSite(it) },
        )
    }

    override val storages by lazy { DaggerStoragesComponent.factory().create(this) }

    override val domainComponent by lazy {
        daggerDomain().create(
            appScopes = this,
            connectConfig = ConnectConfig("http://localhost:8000/login"),
            storages = storages,
            scraper = scraper,
            wykop = wykopApi,
            framework = framework,
        )
    }

    companion object {
        lateinit var instance: TestApp
    }
}
