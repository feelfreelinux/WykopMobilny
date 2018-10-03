package io.github.feelfreelinux.wykopmobilny.ui.modules.addlink

import dagger.Module
import dagger.Provides
import io.github.feelfreelinux.wykopmobilny.ui.modules.NewNavigator
import io.github.feelfreelinux.wykopmobilny.ui.modules.NewNavigatorApi
import io.github.feelfreelinux.wykopmobilny.utils.wykop_link_handler.WykopLinkHandler
import io.github.feelfreelinux.wykopmobilny.utils.wykop_link_handler.WykopLinkHandlerApi

@Module
class AddlinkModule {

    @Provides
    fun provideNavigator(activity: AddlinkActivity): NewNavigatorApi =
        NewNavigator(activity)

    @Provides
    fun provideLinkHandler(activity: AddlinkActivity, navigator: NewNavigatorApi): WykopLinkHandlerApi =
        WykopLinkHandler(activity, navigator)

}