package io.github.wykopmobilny.ui.modules.mainnavigation

import dagger.Module
import dagger.Provides
import io.github.wykopmobilny.api.notifications.NotificationsApi
import io.github.wykopmobilny.api.scraper.ScraperApi
import io.github.wykopmobilny.base.Schedulers
import io.github.wykopmobilny.ui.modules.NewNavigator
import io.github.wykopmobilny.ui.modules.NewNavigatorApi
import io.github.wykopmobilny.utils.shortcuts.ShortcutsDispatcher
import io.github.wykopmobilny.utils.usermanager.UserManagerApi
import io.github.wykopmobilny.utils.linkhandler.WykopLinkHandler
import io.github.wykopmobilny.utils.linkhandler.WykopLinkHandlerApi

@Module
class MainNavigationModule {

    @Provides
    fun provideMainNavigationPresenter(
        schedulers: Schedulers,
        notificationsApi: NotificationsApi,
        userManagerApi: UserManagerApi,
        scraperApi: ScraperApi
    ) = MainNavigationPresenter(
        schedulers,
        notificationsApi,
        userManagerApi,
        scraperApi
    )

    @Provides
    fun provideNavigator(activity: MainNavigationActivity): NewNavigatorApi = NewNavigator(activity)

    @Provides
    fun provideLinkHandler(activity: MainNavigationActivity, navigator: NewNavigatorApi): WykopLinkHandlerApi =
        WykopLinkHandler(activity, navigator)

    @Provides
    fun provideShortcutsDispatcher() = ShortcutsDispatcher()
}
