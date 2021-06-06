package io.github.wykopmobilny.ui.modules.links.linkdetails

import dagger.Module
import dagger.Provides
import io.github.wykopmobilny.api.links.LinksApi
import io.github.wykopmobilny.base.Schedulers
import io.github.wykopmobilny.ui.fragments.link.LinkInteractor
import io.github.wykopmobilny.ui.fragments.linkcomments.LinkCommentInteractor
import io.github.wykopmobilny.ui.modules.NewNavigator
import io.github.wykopmobilny.ui.modules.NewNavigatorApi
import io.github.wykopmobilny.utils.linkhandler.WykopLinkHandler
import io.github.wykopmobilny.utils.linkhandler.WykopLinkHandlerApi

@Module
class LinkDetailsModule {

    @Provides
    fun provideLinkDetailsPresenter(
        schedulers: Schedulers,
        linksApi: LinksApi,
        linkCommentInteractor: LinkCommentInteractor,
        linkInteractor: LinkInteractor
    ) = LinkDetailsPresenter(schedulers, linksApi, linkCommentInteractor, linkInteractor)

    @Provides
    fun provideNavigatorApi(activity: LinkDetailsActivity): NewNavigatorApi = NewNavigator(activity)

    @Provides
    fun provideWykopLinkHandler(activity: LinkDetailsActivity, navigatorApi: NewNavigatorApi): WykopLinkHandlerApi =
        WykopLinkHandler(activity, navigatorApi)
}
