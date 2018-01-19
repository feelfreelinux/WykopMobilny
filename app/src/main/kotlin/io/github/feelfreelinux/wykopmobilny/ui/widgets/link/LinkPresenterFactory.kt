package io.github.feelfreelinux.wykopmobilny.ui.widgets.link

import io.github.feelfreelinux.wykopmobilny.api.links.LinksApi
import io.github.feelfreelinux.wykopmobilny.base.Schedulers
import io.github.feelfreelinux.wykopmobilny.ui.modules.NewNavigatorApi
import io.github.feelfreelinux.wykopmobilny.utils.wykop_link_handler.WykopLinkHandlerApi
import javax.inject.Inject

class LinkPresenterFactory @Inject constructor(
        val schedulers: Schedulers,
        val navigatorApi: NewNavigatorApi,
        val linkHandlerApi: WykopLinkHandlerApi,
        val linksApi: LinksApi) {
    fun create() : LinkPresenter {
        return LinkPresenter(schedulers, navigatorApi, linkHandlerApi, linksApi)
    }
}