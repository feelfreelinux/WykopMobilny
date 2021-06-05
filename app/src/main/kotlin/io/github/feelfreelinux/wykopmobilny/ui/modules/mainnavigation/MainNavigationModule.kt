package io.github.feelfreelinux.wykopmobilny.ui.modules.mainnavigation

import dagger.Module
import dagger.Provides
import io.github.feelfreelinux.wykopmobilny.api.notifications.NotificationsApi
import io.github.feelfreelinux.wykopmobilny.api.scraper.ScraperApi
import io.github.feelfreelinux.wykopmobilny.base.Schedulers
import io.github.feelfreelinux.wykopmobilny.ui.modules.NewNavigator
import io.github.feelfreelinux.wykopmobilny.ui.modules.NewNavigatorApi
import io.github.feelfreelinux.wykopmobilny.utils.shortcuts.ShortcutsDispatcher
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi
import io.github.feelfreelinux.wykopmobilny.utils.wykop_link_handler.WykopLinkHandler
import io.github.feelfreelinux.wykopmobilny.utils.wykop_link_handler.WykopLinkHandlerApi

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
