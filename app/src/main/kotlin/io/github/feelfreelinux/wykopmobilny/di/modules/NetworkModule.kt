package io.github.feelfreelinux.wykopmobilny.di.modules

import android.app.NotificationManager
import android.content.Context
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import io.github.feelfreelinux.wykopmobilny.WykopApp
import io.github.feelfreelinux.wykopmobilny.api.ApiSignInterceptor
import io.github.feelfreelinux.wykopmobilny.api.UserTokenRefresher
import io.github.feelfreelinux.wykopmobilny.api.user.LoginApi
import io.github.feelfreelinux.wykopmobilny.base.WykopSchedulers
import io.github.feelfreelinux.wykopmobilny.ui.modules.Navigator
import io.github.feelfreelinux.wykopmobilny.ui.modules.NavigatorApi
import io.github.feelfreelinux.wykopmobilny.ui.modules.notifications.WykopNotificationManager
import io.github.feelfreelinux.wykopmobilny.ui.modules.notifications.WykopNotificationManagerApi
import io.github.feelfreelinux.wykopmobilny.utils.*
import io.github.feelfreelinux.wykopmobilny.utils.api.CredentialsPreferences
import io.github.feelfreelinux.wykopmobilny.utils.api.CredentialsPreferencesApi
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManager
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.jackson.JacksonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import io.github.feelfreelinux.wykopmobilny.api.CacheControlInterceptor
import okhttp3.Cache

@Module
class NetworkModule {
    @Provides
    fun provideWykopSchedulers() : io.github.feelfreelinux.wykopmobilny.base.Schedulers = WykopSchedulers()

    @Provides
    @Singleton
    fun provideOkHttpClient(userManagerApi: UserManagerApi, cache : Cache, context: Context): OkHttpClient {
        val httpLogging = HttpLoggingInterceptor()
        httpLogging.level = HttpLoggingInterceptor.Level.BASIC
        return OkHttpClient.Builder()
                //.addNetworkInterceptor(CacheControlInterceptor(context))
                .addInterceptor(ApiSignInterceptor(userManagerApi))
                .addInterceptor(httpLogging)
                //.cache(cache)
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build()
    }

    @Provides
    @Singleton
    fun provideMoshi() =
            Moshi.Builder().build()

    @Provides
    @Singleton
    fun provideHttpCache(application: WykopApp): Cache {
        val cacheSize = 10 * 1024 * 1024
        return Cache(application.cacheDir, cacheSize.toLong())
    }

    @Provides
    fun provideCredentialsPreferences(context: Context) : CredentialsPreferencesApi = CredentialsPreferences(context)

    @Provides
    fun provideSettingsPreferences(context: Context) : SettingsPreferencesApi = SettingsPreferences(context)

    @Provides
    fun provideLinksPreferencesApi(context: Context) : LinksPreferencesApi = LinksPreferences(context)

    @Provides
    fun provideUserManagerApi(credentialsPreferencesApi: CredentialsPreferencesApi) : UserManagerApi = UserManager(credentialsPreferencesApi)

    @Provides
    fun provideNotificationManager(context: Context) : NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    @Provides
    @Singleton
    fun provideWykopNotificationManager(mgr: NotificationManager) : WykopNotificationManagerApi = WykopNotificationManager(mgr)

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient, moshi: Moshi) : Retrofit {
        return Retrofit.Builder()
                .client(client)
                .baseUrl(WykopApp.WYKOP_API_URL)
                .addConverterFactory(JacksonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
    }

    @Provides
    fun provideUserTokenRefresher(userApi : LoginApi, userManagerApi: UserManagerApi) = UserTokenRefresher(userApi, userManagerApi)

    @Provides
    fun provideNavigatorApi() : NavigatorApi = Navigator()

    @Provides
    fun provideClipboardHelper(context: Context) : ClipboardHelperApi = ClipboardHelper(context)

    //fun provideEntryPresenterFactory() = EntryPresenterFactory()

}