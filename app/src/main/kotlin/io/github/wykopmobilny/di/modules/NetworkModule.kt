package io.github.wykopmobilny.di.modules

import android.app.NotificationManager
import android.content.Context
import dagger.Module
import dagger.Provides
import io.github.wykopmobilny.api.UserTokenRefresher
import io.github.wykopmobilny.api.user.LoginApi
import io.github.wykopmobilny.base.WykopSchedulers
import io.github.wykopmobilny.ui.modules.Navigator
import io.github.wykopmobilny.ui.modules.NavigatorApi
import io.github.wykopmobilny.utils.ClipboardHelper
import io.github.wykopmobilny.utils.ClipboardHelperApi
import io.github.wykopmobilny.utils.usermanager.UserManager
import io.github.wykopmobilny.utils.usermanager.UserManagerApi

@Module
class NetworkModule {

    @Provides
    fun provideWykopSchedulers(): io.github.wykopmobilny.base.Schedulers = WykopSchedulers()

    @Provides
    fun UserManager.provideUserManagerApi(): UserManagerApi = this

    @Provides
    fun provideNotificationManager(context: Context): NotificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    @Provides
    fun provideUserTokenRefresher(userApi: LoginApi, userManagerApi: UserManagerApi) =
        UserTokenRefresher(userApi, userManagerApi)

    @Provides
    fun provideNavigatorApi(): NavigatorApi = Navigator()

    @Provides
    fun provideClipboardHelper(context: Context): ClipboardHelperApi = ClipboardHelper(context)
}
