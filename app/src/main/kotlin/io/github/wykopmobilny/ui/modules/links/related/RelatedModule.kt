package io.github.wykopmobilny.ui.modules.links.related

import dagger.Module
import dagger.Provides
import io.github.wykopmobilny.api.links.LinksApi
import io.github.wykopmobilny.base.Schedulers
import io.github.wykopmobilny.ui.modules.NewNavigator
import io.github.wykopmobilny.ui.modules.NewNavigatorApi
import io.github.wykopmobilny.utils.linkhandler.WykopLinkHandler
import io.github.wykopmobilny.utils.linkhandler.WykopLinkHandlerApi

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
