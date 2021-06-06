package io.github.wykopmobilny.ui.modules.loginscreen

import dagger.Module
import dagger.Provides
import io.github.wykopmobilny.api.scraper.ScraperApi
import io.github.wykopmobilny.api.user.LoginApi
import io.github.wykopmobilny.base.Schedulers
import io.github.wykopmobilny.ui.modules.NewNavigator
import io.github.wykopmobilny.ui.modules.NewNavigatorApi
import io.github.wykopmobilny.utils.usermanager.UserManagerApi
import io.github.wykopmobilny.utils.linkhandler.WykopLinkHandler
import io.github.wykopmobilny.utils.linkhandler.WykopLinkHandlerApi

@Module
class LoginScreenModule {
    @Provides
    fun providesLoginScreenPresenter(schedulers: Schedulers, userManager: UserManagerApi, loginApi: LoginApi, scraperApi: ScraperApi) =
        LoginScreenPresenter(schedulers, userManager, scraperApi, loginApi)

    @Provides
    fun provideNavigator(activity: LoginScreenActivity): NewNavigatorApi = NewNavigator(activity)

    @Provides
    fun provideLinkHandler(activity: LoginScreenActivity, navigator: NewNavigatorApi): WykopLinkHandlerApi =
        WykopLinkHandler(activity, navigator)
}
