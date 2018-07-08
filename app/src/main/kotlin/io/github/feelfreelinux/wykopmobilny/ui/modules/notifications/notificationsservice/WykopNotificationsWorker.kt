/*package io.github.feelfreelinux.wykopmobilny.ui.modules.notifications.notificationsservice

import androidx.work.Worker
import io.github.feelfreelinux.wykopmobilny.WykopApp
import io.github.feelfreelinux.wykopmobilny.api.ApiSignInterceptor
import io.github.feelfreelinux.wykopmobilny.api.UserTokenRefresher
import io.github.feelfreelinux.wykopmobilny.api.notifications.NotificationsRepository
import io.github.feelfreelinux.wykopmobilny.api.user.LoginRepository
import io.github.feelfreelinux.wykopmobilny.base.WykopSchedulers
import io.github.feelfreelinux.wykopmobilny.utils.api.CredentialsPreferences
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManager
import okhttp3.OkHttpClient
import okhttp3.Protocol
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.jackson.JacksonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Dagger's AndroidInjector doesn't support Worker for now.
 * Will replace this depedencies mess after it gets implemented.
 */
class WykopNotificationsWorker : Worker() {
    val userManager by lazy {
        UserManager(CredentialsPreferences(applicationContext))
    }

    val okHttpClient by lazy { OkHttpClient.Builder()
        .addInterceptor(ApiSignInterceptor(userManager))
        .protocols(listOf(Protocol.HTTP_1_1))
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build() }

    val retrofit by lazy { Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(WykopApp.WYKOP_API_URL)
            .addConverterFactory(JacksonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build() }

    val notificationsRepository by lazy {
        NotificationsRepository(retrofit, UserTokenRefresher(LoginRepository(retrofit, CredentialsPreferences(applicationContext)), userManager))
    }
    val presenter = WykopNotificationsJobPresenter(WykopSchedulers(), notificationsRepository, userManager)

    override fun doWork(): Result {

    }
}*/