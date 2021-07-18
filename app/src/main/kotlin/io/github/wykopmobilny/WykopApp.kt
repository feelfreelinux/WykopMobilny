package io.github.wykopmobilny

import android.app.Activity
import android.os.Bundle
import android.webkit.CookieManager
import android.widget.Toast
import com.jakewharton.threetenabp.AndroidThreeTen
import dagger.Lazy
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import io.github.wykopmobilny.api.ApiSignInterceptor
import io.github.wykopmobilny.di.DaggerAppComponent
import io.github.wykopmobilny.domain.di.DomainComponent
import io.github.wykopmobilny.domain.login.ConnectConfig
import io.github.wykopmobilny.domain.login.di.LoginScope
import io.github.wykopmobilny.domain.navigation.InteropRequest
import io.github.wykopmobilny.domain.navigation.android.DaggerFrameworkComponent
import io.github.wykopmobilny.domain.settings.di.SettingsScope
import io.github.wykopmobilny.domain.styles.di.StylesScope
import io.github.wykopmobilny.storage.android.DaggerStoragesComponent
import io.github.wykopmobilny.storage.api.SettingsPreferencesApi
import io.github.wykopmobilny.styles.StylesDependencies
import io.github.wykopmobilny.ui.base.AppDispatchers
import io.github.wykopmobilny.ui.base.AppScopes
import io.github.wykopmobilny.ui.login.LoginDependencies
import io.github.wykopmobilny.ui.modules.blacklist.BlacklistActivity
import io.github.wykopmobilny.ui.modules.search.SuggestionDatabase
import io.github.wykopmobilny.ui.settings.SettingsDependencies
import io.github.wykopmobilny.utils.ApplicationInjector
import io.github.wykopmobilny.utils.usermanager.SimpleUserManagerApi
import io.github.wykopmobilny.utils.usermanager.UserCredentials
import io.github.wykopmobilny.utils.usermanager.UserManagerApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Cache
import okhttp3.OkHttpClient
import javax.inject.Inject
import kotlin.reflect.KClass

open class WykopApp : DaggerApplication(), ApplicationInjector, CoroutineScope {

    companion object {

        const val WYKOP_API_URL = "https://a2.wykop.pl"
    }

    @Inject
    lateinit var userManagerApi: Lazy<UserManagerApi>

    @Inject
    lateinit var settingsPreferencesApi: Lazy<SettingsPreferencesApi>

    override val coroutineContext = Job() + Dispatchers.Default

    private val okHttpClient = OkHttpClient()

    override fun onCreate() {
        super.onCreate()
        AppScopes.applicationScope = this
        AppDispatchers.Default = Dispatchers.Default
        AppDispatchers.Main = Dispatchers.Main
        AppDispatchers.IO = Dispatchers.IO
        AndroidThreeTen.init(this)
        doInterop()
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> =
        DaggerAppComponent.factory().create(
            instance = this,
            okHttpClient = okHttpClient,
            wykop = wykopApi,
            patrons = patrons,
            scraper = scraper,
            storages = storages,
            settingsInterop = domainComponent.settingsApiInterop(),
        )

    protected open val domainComponent: DomainComponent by lazy {
        daggerDomain().create(
            connectConfig = ConnectConfig(connectUrl = "https://a2.wykop.pl/login/connect/appkey/${BuildConfig.APP_KEY}"),
            storages = storages,
            scraper = scraper,
            wykop = wykopApi,
            framework = framework,
        )
    }

    protected open val storages by lazy {
        DaggerStoragesComponent.factory().create(
            context = this,
        )
    }

    protected open val scraper by lazy {
        daggerScraper().create(
            okHttpClient = okHttpClient,
            baseUrl = "https://wykop.pl",
            cookieProvider = { webPage -> CookieManager.getInstance().getCookie(webPage) },
        )
    }

    protected open val patrons by lazy {
        daggerPatrons().create(
            okHttpClient = okHttpClient.newBuilder()
                .cache(Cache(cacheDir.resolve("okhttp/patrons"), maxSize = 5 * 1024 * 1024L))
                .build(),
            baseUrl = "https://raw.githubusercontent.com/",
        )
    }

    protected open val wykopApi by lazy {
        daggerWykop().create(
            okHttpClient = okHttpClient,
            baseUrl = WYKOP_API_URL,
            appKey = BuildConfig.APP_KEY,
            cacheDir = cacheDir.resolve("okhttp/wykop"),
            signingInterceptor = ApiSignInterceptor(
                object : SimpleUserManagerApi {

                    override fun getUserCredentials(): UserCredentials? = userManagerApi.get().getUserCredentials()
                },
            ),
        )
    }

    protected open val framework by lazy {
        DaggerFrameworkComponent.factory().create(
            application = this,
        )
    }

    private val scopes = mutableMapOf<KClass<out Any>, Any>()

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> getDependency(clazz: KClass<T>): T =
        when (clazz) {
            LoginDependencies::class -> scopes.getOrPut(LoginScope::class) { domainComponent.login() } as T
            StylesDependencies::class -> scopes.getOrPut(StylesScope::class) { domainComponent.styles() } as T
            SettingsDependencies::class -> scopes.getOrPut(SettingsScope::class) { domainComponent.settings() } as T
            else -> error("Unknown dependencies for type $clazz")
        }

    private fun doInterop() {
        launch {
            var currentActivity: Activity? = null
            registerActivityLifecycleCallbacks(
                object : ActivityLifecycleCallbacks {
                    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) = Unit

                    override fun onActivityStarted(activity: Activity) = Unit

                    override fun onActivityResumed(activity: Activity) {
                        currentActivity = activity
                    }

                    override fun onActivityPaused(activity: Activity) {
                        currentActivity = null
                    }

                    override fun onActivityStopped(activity: Activity) = Unit

                    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) = Unit

                    override fun onActivityDestroyed(activity: Activity) = Unit
                },
            )
            domainComponent.navigation().request.collect {
                val context = currentActivity ?: return@collect
                when (it) {
                    InteropRequest.BlackListScreen -> context.startActivity(BlacklistActivity.createIntent(context))
                    InteropRequest.ClearSuggestionDatabase -> {
                        SuggestionDatabase(context).clearDb()
                        withContext(Dispatchers.Main) {
                            Toast.makeText(context, "Wyczyszczono historię wyszukiwarki", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }
    }
}
