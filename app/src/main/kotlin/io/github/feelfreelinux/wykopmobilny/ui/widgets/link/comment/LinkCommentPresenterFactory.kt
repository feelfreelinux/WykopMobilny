package io.github.feelfreelinux.wykopmobilny.ui.widgets.link.comment

import io.github.feelfreelinux.wykopmobilny.api.links.LinksApi
import io.github.feelfreelinux.wykopmobilny.base.Schedulers
import io.github.feelfreelinux.wykopmobilny.ui.modules.NewNavigatorApi
import io.github.feelfreelinux.wykopmobilny.utils.wykop_link_handler.WykopLinkHandlerApi
import javax.inject.Inject

class LinkCommentPresenterFactory @Inject constructor(
        val schedulers: Schedulers,
        val newNavigatorApi: NewNavigatorApi,
        val wykopLinkHandlerApi: WykopLinkHandlerApi,
        val linksApi : LinksApi) {
    fun create() : LinkCommentPresenter {
        return LinkCommentPresenter(schedulers, newNavigatorApi, wykopLinkHandlerApi, linksApi)
    }
}