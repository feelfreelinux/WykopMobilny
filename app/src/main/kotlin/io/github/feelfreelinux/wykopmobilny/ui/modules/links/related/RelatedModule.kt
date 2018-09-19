package io.github.feelfreelinux.wykopmobilny.ui.modules.links.related

import dagger.Module
import dagger.Provides
import io.github.feelfreelinux.wykopmobilny.api.links.LinksApi
import io.github.feelfreelinux.wykopmobilny.base.Schedulers
import io.github.feelfreelinux.wykopmobilny.ui.modules.NewNavigator
import io.github.feelfreelinux.wykopmobilny.ui.modules.NewNavigatorApi
import io.github.feelfreelinux.wykopmobilny.utils.wykop_link_handler.WykopLinkHandler
import io.github.feelfreelinux.wykopmobilny.utils.wykop_link_handler.WykopLinkHandlerApi

@Module
class RelatedModule {
    @Provides
    fun provideRelatedPresenter(schedulers: Schedulers, linksApi: LinksApi) =
        RelatedPresenter(schedulers, linksApi)

    @Provides
    fun provideNavigatorApi(activity: RelatedActivity): NewNavigatorApi =
        NewNavigator(activity)

    @Provides
    fun provideWykopLinkHandler(activity: RelatedActivity, navigatorApi: NewNavigatorApi): WykopLinkHandlerApi =
        WykopLinkHandler(activity, navigatorApi)
}