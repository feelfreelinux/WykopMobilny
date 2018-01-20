package io.github.feelfreelinux.wykopmobilny.ui.widgets.link.comment

import io.github.feelfreelinux.wykopmobilny.api.links.LinksApi
import io.github.feelfreelinux.wykopmobilny.base.Schedulers
import io.github.feelfreelinux.wykopmobilny.ui.modules.NewNavigatorApi
import javax.inject.Inject

class LinkCommentPresenterFactory @Inject constructor(
        val schedulers: Schedulers,
        val newNavigatorApi: NewNavigatorApi,
        val linksApi : LinksApi) {
    fun create() : LinkCommentPresenter {
        return LinkCommentPresenter(schedulers, newNavigatorApi, linksApi)
    }
}