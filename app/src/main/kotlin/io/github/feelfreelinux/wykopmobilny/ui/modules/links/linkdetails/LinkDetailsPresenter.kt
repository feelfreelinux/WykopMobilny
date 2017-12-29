package io.github.feelfreelinux.wykopmobilny.ui.modules.links.linkdetails

import io.github.feelfreelinux.wykopmobilny.api.links.LinksApi
import io.github.feelfreelinux.wykopmobilny.base.BasePresenter
import io.github.feelfreelinux.wykopmobilny.utils.rx.SubscriptionHelperApi

class LinkDetailsPresenter(val subscriptionHelperApi: SubscriptionHelperApi, val linksApi: LinksApi) : BasePresenter<LinkDetailsView>() {
    var sortBy = "best"
    var linkId = -1
    fun loadComments() {
        subscriptionHelperApi.subscribe(linksApi.getLinkComments(linkId, sortBy),
                { view?.showLinkComments(it) }, { view?.showErrorDialog(it) }, this)
    }
}