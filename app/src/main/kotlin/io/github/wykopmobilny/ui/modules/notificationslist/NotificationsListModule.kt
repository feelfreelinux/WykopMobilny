package io.github.wykopmobilny.ui.modules.notificationslist

import dagger.Module
import dagger.Provides
import io.github.wykopmobilny.ui.modules.NewNavigator
import io.github.wykopmobilny.ui.modules.NewNavigatorApi
import io.github.wykopmobilny.utils.linkhandler.WykopLinkHandler
import io.github.wykopmobilny.utils.linkhandler.WykopLinkHandlerApi

@Module
class NotificationsListModule {
    @Provides
    fun provideNavigatorApi(activity: NotificationsListActivity): NewNavigatorApi = NewNavigator(activity)

    @Provides
    fun provideLinkHandlerApi(activity: NotificationsListActivity, newNavigatorApi: NewNavigatorApi): WykopLinkHandlerApi =
        WykopLinkHandler(activity, newNavigatorApi)
}
