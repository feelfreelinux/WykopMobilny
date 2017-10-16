package io.github.feelfreelinux.wykopmobilny.di.modules

import android.app.NotificationManager
import android.content.Context
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import io.github.feelfreelinux.wykopmobilny.api.ApiSignInterceptor
import io.github.feelfreelinux.wykopmobilny.api.WykopRequestBodyConverterFactory
import io.github.feelfreelinux.wykopmobilny.api.user.UserApi
import io.github.feelfreelinux.wykopmobilny.ui.modules.notifications.WykopNotificationManager
import io.github.feelfreelinux.wykopmobilny.ui.modules.notifications.WykopNotificationManagerApi
import io.github.feelfreelinux.wykopmobilny.utils.api.CredentialsPreferences
import io.github.feelfreelinux.wykopmobilny.utils.api.CredentialsPreferencesApi
import io.github.feelfreelinux.wykopmobilny.utils.rx.SubscriptionHelper
import io.github.feelfreelinux.wykopmobilny.utils.rx.SubscriptionHelperApi
import io.github.feelfreelinux.wykopmobilny.api.UserTokenRefresher
import io.github.feelfreelinux.wykopmobilny.utils.ClipboardHelper
import io.github.feelfreelinux.wykopmobilny.utils.ClipboardHelperApi
import io.github.feelfreelinux.wykopmobilny.utils.SettingsPreferences
import io.github.feelfreelinux.wykopmobilny.utils.SettingsPreferencesApi
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManager
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi
import io.github.feelfreelinux.wykopmobilny.utils.wykop_link_handler.WykopLinkHandler
import io.github.feelfreelinux.wykopmobilny.utils.wykop_link_handler.WykopLinkHandlerApi
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import javax.inject.Singleton

@Module
class NetworkModule(private val baseUrl : String) {
    @Provides
    @Singleton
    fun provideOkHttpClient(userManagerApi: UserManagerApi): OkHttpClient {
        val httpLogging = HttpLoggingInterceptor()
        httpLogging.level = HttpLoggingInterceptor.Level.BASIC
        return OkHttpClient.Builder()
                .addInterceptor(ApiSignInterceptor(userManagerApi))
                .addInterceptor(httpLogging)
                .build()
    }

    @Provides
    @Singleton
    fun provideMoshi() =
            Moshi.Builder().build()

    @Provides
    fun provideCredentialsPreferences(context: Context) : CredentialsPreferencesApi = CredentialsPreferences(context)

    @Provides
    fun provideSettingsPreferences(context: Context) : SettingsPreferencesApi = SettingsPreferences(context)

    @Provides
    fun provideUserManagerApi(credentialsPreferencesApi: CredentialsPreferencesApi) : UserManagerApi = UserManager(credentialsPreferencesApi)

    @Provides
    fun provideNotificationManager(context: Context) : NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    @Provides
    @Singleton
    fun provideWykopNotificationManager(mgr: NotificationManager) : WykopNotificationManagerApi = WykopNotificationManager(mgr)

    @Provides
    fun provideWykopLinkHandlerApi(context : Context) : WykopLinkHandlerApi = WykopLinkHandler(context)

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient, moshi: Moshi) : Retrofit {
        return Retrofit.Builder()
                .client(client)
                .baseUrl(baseUrl)
                .addConverterFactory(WykopRequestBodyConverterFactory(moshi))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
    }

    @Provides
    fun provideUserTokenRefresher(userApi : UserApi, userManagerApi: UserManagerApi) = UserTokenRefresher(userApi, userManagerApi)

    @Provides
    fun provideSubscriptionHandler(userTokenRefresher: UserTokenRefresher) : SubscriptionHelperApi = SubscriptionHelper(AndroidSchedulers.mainThread(), Schedulers.io(), userTokenRefresher)

    @Provides
    fun provideClipboardHelper(context: Context) : ClipboardHelperApi = ClipboardHelper(context)
}