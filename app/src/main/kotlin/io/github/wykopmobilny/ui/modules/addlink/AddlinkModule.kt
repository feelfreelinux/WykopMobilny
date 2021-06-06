package io.github.wykopmobilny.ui.modules.addlink

import dagger.Module
import dagger.Provides
import io.github.wykopmobilny.ui.modules.NewNavigator
import io.github.wykopmobilny.ui.modules.NewNavigatorApi
import io.github.wykopmobilny.utils.linkhandler.WykopLinkHandler
import io.github.wykopmobilny.utils.linkhandler.WykopLinkHandlerApi

@Module
class AddlinkModule {

    @Provides
    fun provideNavigator(activity: AddlinkActivity): NewNavigatorApi =
        NewNavigator(activity)

    @Provides
    fun provideLinkHandler(activity: AddlinkActivity, navigator: NewNavigatorApi): WykopLinkHandlerApi =
        WykopLinkHandler(activity, navigator)
}
