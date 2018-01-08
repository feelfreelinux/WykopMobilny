package io.github.feelfreelinux.wykopmobilny.ui.modules.links.linkdetails

import io.github.feelfreelinux.wykopmobilny.api.links.LinksApi
import io.github.feelfreelinux.wykopmobilny.base.BasePresenter
import io.github.feelfreelinux.wykopmobilny.base.Schedulers

class LinkDetailsPresenter(val schedulers: Schedulers, val linksApi: LinksApi) : BasePresenter<LinkDetailsView>() {
    var sortBy = "best"
    var linkId = -1
    fun loadComments() {
        compositeObservable.add(
                linksApi.getLinkComments(linkId, sortBy)
                        .subscribeOn(schedulers.backgroundThread())
                        .observeOn(schedulers.mainThread())
                        .subscribe({ view?.showLinkComments(it) }, { view?.showErrorDialog(it) })
        )
    }
}