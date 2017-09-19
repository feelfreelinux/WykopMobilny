package io.github.feelfreelinux.wykopmobilny.di.modules

import android.app.NotificationManager
import android.content.Context
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import io.github.feelfreelinux.wykopmobilny.api.ApiSignInterceptor
import io.github.feelfreelinux.wykopmobilny.ui.notifications.WykopNotificationManager
import io.github.feelfreelinux.wykopmobilny.ui.notifications.WykopNotificationManagerApi
import io.github.feelfreelinux.wykopmobilny.utils.api.CredentialsPreferences
import io.github.feelfreelinux.wykopmobilny.utils.api.CredentialsPreferencesApi
import io.github.feelfreelinux.wykopmobilny.utils.rx.SubscriptionHelper
import io.github.feelfreelinux.wykopmobilny.utils.rx.SubscriptionHelperApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
class NetworkModule(private val baseUrl : String) {
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val httpLogging = HttpLoggingInterceptor()
        httpLogging.level = HttpLoggingInterceptor.Level.BASIC
        return OkHttpClient.Builder()
                .addInterceptor(ApiSignInterceptor())
                .addInterceptor(httpLogging)
                .build()
    }

    @Provides
    @Singleton
    fun provideMoshi() =
            Moshi.Builder().build()

    @Provides
    @Singleton
    fun provideCredentialsPreferences(context: Context) : CredentialsPreferencesApi = CredentialsPreferences(context)

    @Provides
    @Singleton
    fun provideNotificationManager(context: Context) : NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    @Provides
    @Singleton
    fun provideWykopNotificationManager(mgr: NotificationManager) : WykopNotificationManagerApi = WykopNotificationManager(mgr)

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient, moshi: Moshi) : Retrofit {
        return Retrofit.Builder()
                .client(client)
                .baseUrl(baseUrl)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
    }

    @Provides
    @Singleton
    fun provideSubscriptionHandler() : SubscriptionHelperApi = SubscriptionHelper()
}