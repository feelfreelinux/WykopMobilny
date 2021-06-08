package io.github.wykopmobilny

import android.webkit.CookieManager
import com.evernote.android.job.JobManager
import com.jakewharton.threetenabp.AndroidThreeTen
import dagger.Lazy
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import io.github.wykopmobilny.api.ApiSignInterceptor
import io.github.wykopmobilny.di.DaggerAppComponent
import io.github.wykopmobilny.phuckdagger.daggerPatrons
import io.github.wykopmobilny.phuckdagger.daggerScraper
import io.github.wykopmobilny.phuckdagger.daggerWykop
import io.github.wykopmobilny.storage.android.DaggerStoragesComponent
import io.github.wykopmobilny.storage.api.SettingsPreferencesApi
import io.github.wykopmobilny.ui.modules.notifications.notificationsservice.WykopNotificationJobCreator
import io.github.wykopmobilny.utils.usermanager.SimpleUserManagerApi
import io.github.wykopmobilny.utils.usermanager.UserCredentials
import io.github.wykopmobilny.utils.usermanager.UserManagerApi
import okhttp3.Cache
import okhttp3.OkHttpClient
import javax.inject.Inject

open class WykopApp : DaggerApplication() {

    companion object {

        const val WYKOP_API_URL = "https://a2.wykop.pl"
    }

    @Inject
    lateinit var jobCreator: Lazy<WykopNotificationJobCreator>

    @Inject
    lateinit var userManagerApi: Lazy<UserManagerApi>

    @Inject
    lateinit var settingsPreferencesApi: Lazy<SettingsPreferencesApi>

    private val okHttpClient = OkHttpClient()

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
        JobManager.create(this).addJobCreator(jobCreator.get())
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> =
        DaggerAppComponent.factory().create(
            instance = this,
            okHttpClient = okHttpClient,
            wykop = createWykop(),
            patrons = createPatrons(),
            scraper = createScraper(),
            storages = storages(),
        )

    private fun storages() =
        DaggerStoragesComponent.factory().create(
            context = this,
        )

    private fun createScraper() =
        daggerScraper().create(
            okHttpClient = okHttpClient,
            baseUrl = "https://wykop.pl",
            cookieProvider = { webPage -> CookieManager.getInstance().getCookie(webPage) },
        )

    private fun createPatrons() =
        daggerPatrons().create(
            okHttpClient = okHttpClient.newBuilder()
                .cache(Cache(cacheDir.resolve("okhttp/patrons"), maxSize = 5 * 1024 * 1024L))
                .build(),
            baseUrl = "https://raw.githubusercontent.com/",
        )

    private fun createWykop() =
        daggerWykop().create(
            okHttpClient = okHttpClient,
            baseUrl = WYKOP_API_URL,
            appKey = APP_KEY,
            cacheDir = cacheDir.resolve("okhttp/wykop"),
            signingInterceptor = ApiSignInterceptor(
                object : SimpleUserManagerApi {

                    override fun isUserAuthorized(): Boolean = userManagerApi.get().isUserAuthorized()

                    override fun getUserCredentials(): UserCredentials? = userManagerApi.get().getUserCredentials()
                },
            ),
        )
}
