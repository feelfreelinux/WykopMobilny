package io.github.feelfreelinux.wykopmobilny.ui.widgets.link.linkitem

import io.github.feelfreelinux.wykopmobilny.api.links.LinksApi
import io.github.feelfreelinux.wykopmobilny.base.Schedulers
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi
import javax.inject.Inject

class LinkItemPresenterFactory @Inject constructor(
        val schedulers: Schedulers,
        val linksApi: LinksApi,
        val userManagerApi: UserManagerApi) {
    fun create() : LinkItemPresenter {
        return LinkItemPresenter(schedulers, linksApi, userManagerApi)
    }
}