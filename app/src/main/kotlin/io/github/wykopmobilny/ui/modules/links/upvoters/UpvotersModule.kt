package io.github.wykopmobilny.ui.modules.links.upvoters

import dagger.Module
import dagger.Provides
import io.github.wykopmobilny.api.links.LinksApi
import io.github.wykopmobilny.base.Schedulers
import io.github.wykopmobilny.ui.modules.NewNavigator
import io.github.wykopmobilny.ui.modules.NewNavigatorApi
import io.github.wykopmobilny.utils.linkhandler.WykopLinkHandler
import io.github.wykopmobilny.utils.linkhandler.WykopLinkHandlerApi

@Module
class UpvotersModule {
    @Provides
    fun provideUpvotersPresenter(schedulers: Schedulers, linksApi: LinksApi) =
        UpvotersPresenter(schedulers, linksApi)

    @Provides
    fun provideNavigatorApi(activity: UpvotersActivity): NewNavigatorApi =
        NewNavigator(activity)

    @Provides
    fun provideWykopLinkHandler(activity: UpvotersActivity, navigatorApi: NewNavigatorApi): WykopLinkHandlerApi =
        WykopLinkHandler(activity, navigatorApi)
}
