package io.github.feelfreelinux.wykopmobilny.ui.modules.loginscreen

import dagger.Module
import dagger.Provides
import io.github.feelfreelinux.wykopmobilny.api.user.LoginApi
import io.github.feelfreelinux.wykopmobilny.base.Schedulers
import io.github.feelfreelinux.wykopmobilny.ui.modules.NewNavigator
import io.github.feelfreelinux.wykopmobilny.ui.modules.NewNavigatorApi
import io.github.feelfreelinux.wykopmobilny.utils.rx.SchedulerProvider
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi
import io.github.feelfreelinux.wykopmobilny.utils.wykop_link_handler.WykopLinkHandler
import io.github.feelfreelinux.wykopmobilny.utils.wykop_link_handler.WykopLinkHandlerApi

@Module
class LoginScreenModule {
    @Provides
    fun providesLoginScreenPresenter(schedulers: SchedulerProvider, userManager: UserManagerApi, loginApi: LoginApi) =
            LoginScreenPresenter(schedulers, userManager, loginApi)

    @Provides
    fun provideNavigator(activity : LoginScreenActivity) : NewNavigatorApi = NewNavigator(activity)

    @Provides
    fun provideLinkHandler(activity : LoginScreenActivity, navigator : NewNavigatorApi) : WykopLinkHandlerApi = WykopLinkHandler(activity, navigator)
}