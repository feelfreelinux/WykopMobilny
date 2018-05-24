package io.github.feelfreelinux.wykopmobilny.ui.modules.links.linkdetails

import dagger.Module
import dagger.Provides
import io.github.feelfreelinux.wykopmobilny.api.links.LinksApi
import io.github.feelfreelinux.wykopmobilny.base.Schedulers
import io.github.feelfreelinux.wykopmobilny.ui.fragments.link.LinkInteractor
import io.github.feelfreelinux.wykopmobilny.ui.fragments.linkcomments.LinkCommentInteractor
import io.github.feelfreelinux.wykopmobilny.ui.modules.NewNavigator
import io.github.feelfreelinux.wykopmobilny.ui.modules.NewNavigatorApi
import io.github.feelfreelinux.wykopmobilny.utils.wykop_link_handler.WykopLinkHandler
import io.github.feelfreelinux.wykopmobilny.utils.wykop_link_handler.WykopLinkHandlerApi

@Module
class LinkDetailsModule {
    @Provides
    fun provideLinkDetailsPresenter(schedulers: Schedulers, linksApi: LinksApi, linkCommentInteractor: LinkCommentInteractor, linkInteractor: LinkInteractor) =
            LinkDetailsPresenter(schedulers, linksApi, linkCommentInteractor, linkInteractor)

    @Provides
    fun provideNavigatorApi(activity: LinkDetailsActivity) : NewNavigatorApi = NewNavigator(activity)

    @Provides
    fun provideWykopLinkHandler(activity: LinkDetailsActivity, navigatorApi: NewNavigatorApi) : WykopLinkHandlerApi
            = WykopLinkHandler(activity, navigatorApi)
}