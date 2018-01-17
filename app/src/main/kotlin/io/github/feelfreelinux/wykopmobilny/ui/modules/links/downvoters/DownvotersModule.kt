package io.github.feelfreelinux.wykopmobilny.ui.modules.links.downvoters

import dagger.Module
import dagger.Provides
import io.github.feelfreelinux.wykopmobilny.api.links.LinksApi
import io.github.feelfreelinux.wykopmobilny.base.Schedulers
import io.github.feelfreelinux.wykopmobilny.ui.modules.NewNavigator
import io.github.feelfreelinux.wykopmobilny.ui.modules.NewNavigatorApi
import io.github.feelfreelinux.wykopmobilny.utils.wykop_link_handler.WykopLinkHandler
import io.github.feelfreelinux.wykopmobilny.utils.wykop_link_handler.WykopLinkHandlerApi

@Module
class DownvotersModule {
    @Provides
    fun provideDownvotersPresenter(schedulers: Schedulers, linksApi: LinksApi) = DownvotersPresenter(schedulers, linksApi)

    @Provides
    fun provideNavigatorApi(activity: DownvotersActivity) : NewNavigatorApi = NewNavigator(activity)

    @Provides
    fun provideWykopLinkHandler(activity: DownvotersActivity, navigatorApi: NewNavigatorApi) : WykopLinkHandlerApi
            = WykopLinkHandler(activity, navigatorApi)
}