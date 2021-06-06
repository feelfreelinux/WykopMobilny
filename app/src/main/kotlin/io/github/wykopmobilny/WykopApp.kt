package io.github.wykopmobilny

import android.webkit.CookieManager
import com.devbrackets.android.exomedia.ExoMedia
import com.evernote.android.job.JobManager
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.jakewharton.threetenabp.AndroidThreeTen
import dagger.Lazy
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import io.github.wykopmobilny.api.ApiSignInterceptor
import io.github.wykopmobilny.blacklist.remote.DaggerScraperComponent
import io.github.wykopmobilny.di.DaggerAppComponent
import io.github.wykopmobilny.patrons.remote.DaggerPatronsComponent
import io.github.wykopmobilny.storage.android.DaggerStoragesComponent
import io.github.wykopmobilny.storage.api.SettingsPreferencesApi
import io.github.wykopmobilny.ui.modules.notifications.notificationsservice.WykopNotificationJobCreator
import io.github.wykopmobilny.utils.usermanager.SimpleUserManagerApi
import io.github.wykopmobilny.utils.usermanager.UserCredentials
import io.github.wykopmobilny.utils.usermanager.UserManagerApi
import io.github.wykopmobilny.wykop.remote.DaggerWykopComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.Protocol
import java.util.concurrent.TimeUnit
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
        ExoMedia.setDataSourceFactoryProvider { userAgent, listener ->
            DefaultHttpDataSource.Factory()
                .setUserAgent(userAgent)
                .setTransferListener(listener)
        }
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
        DaggerScraperComponent.factory().create(
            okHttpClient = okHttpClient,
            baseUrl = "https://wykop.pl",
            cookieProvider = { webPage -> CookieManager.getInstance().getCookie(webPage) },
        )

    private fun createPatrons() =
        DaggerPatronsComponent.factory().create(
            okHttpClient = okHttpClient.newBuilder()
                .cache(Cache(cacheDir.resolve("okhttp/patrons"), maxSize = 5 * 1024 * 1024L))
                .build(),
            baseUrl = "https://raw.githubusercontent.com/",
        )

    private fun createWykop() =
        DaggerWykopComponent.factory().create(
            okHttpClient = okHttpClient.newBuilder()
                .addInterceptor(
                    ApiSignInterceptor(
                        object : SimpleUserManagerApi {

                            override fun isUserAuthorized(): Boolean = userManagerApi.get().isUserAuthorized()

                            override fun getUserCredentials(): UserCredentials? = userManagerApi.get().getUserCredentials()
                        }
                    )
                )
                .cache(Cache(cacheDir.resolve("okhttp/wykop"), maxSize = 10 * 1024 * 1024L))
                .protocols(listOf(Protocol.HTTP_1_1))
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build(),
            apiUrl = WYKOP_API_URL,
        )
}
