package io.github.wykopmobilny.ui.modules.links.downvoters

import dagger.Module
import dagger.Provides
import io.github.wykopmobilny.api.links.LinksApi
import io.github.wykopmobilny.base.Schedulers
import io.github.wykopmobilny.ui.modules.NewNavigator
import io.github.wykopmobilny.ui.modules.NewNavigatorApi
import io.github.wykopmobilny.utils.linkhandler.WykopLinkHandler
import io.github.wykopmobilny.utils.linkhandler.WykopLinkHandlerApi

@Module
class DownvotersModule {
    @Provides
    fun provideDownvotersPresenter(schedulers: Schedulers, linksApi: LinksApi) = DownvotersPresenter(schedulers, linksApi)

    @Provides
    fun provideNavigatorApi(activity: DownvotersActivity): NewNavigatorApi = NewNavigator(activity)

    @Provides
    fun provideWykopLinkHandler(activity: DownvotersActivity, navigatorApi: NewNavigatorApi): WykopLinkHandlerApi =
        WykopLinkHandler(activity, navigatorApi)
}
