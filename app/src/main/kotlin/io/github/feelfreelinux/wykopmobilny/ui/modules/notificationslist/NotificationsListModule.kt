package io.github.feelfreelinux.wykopmobilny.ui.modules.notificationslist

import dagger.Module
import dagger.Provides
import io.github.feelfreelinux.wykopmobilny.ui.modules.NewNavigator
import io.github.feelfreelinux.wykopmobilny.ui.modules.NewNavigatorApi
import io.github.feelfreelinux.wykopmobilny.utils.wykop_link_handler.WykopLinkHandler
import io.github.feelfreelinux.wykopmobilny.utils.wykop_link_handler.WykopLinkHandlerApi

@Module
class NotificationsListModule {
    @Provides
    fun provideNavigatorApi(activity: NotificationsListActivity) : NewNavigatorApi = NewNavigator(activity)

    @Provides
    fun provideLinkHandlerApi(activity: NotificationsListActivity, newNavigatorApi: NewNavigatorApi) : WykopLinkHandlerApi = WykopLinkHandler(activity, newNavigatorApi)
}