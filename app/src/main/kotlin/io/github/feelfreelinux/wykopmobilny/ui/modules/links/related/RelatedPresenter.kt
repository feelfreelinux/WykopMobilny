package io.github.feelfreelinux.wykopmobilny.ui.modules.links.related

import io.github.feelfreelinux.wykopmobilny.api.links.LinksApi
import io.github.feelfreelinux.wykopmobilny.base.BasePresenter
import io.github.feelfreelinux.wykopmobilny.base.Schedulers

class RelatedPresenter(
    val schedulers: Schedulers,
    val linksApi: LinksApi
) : BasePresenter<RelatedView>() {

    var linkId = -1

    fun getRelated() {
        compositeObservable.add(
            linksApi.getRelated(linkId)
                .subscribeOn(schedulers.backgroundThread())
                .observeOn(schedulers.mainThread())
                .subscribe({ view?.showRelated(it) }, { view?.showErrorDialog(it) })
        )
    }
}