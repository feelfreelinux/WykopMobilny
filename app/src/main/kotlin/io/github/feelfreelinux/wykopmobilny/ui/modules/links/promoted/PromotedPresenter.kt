package io.github.feelfreelinux.wykopmobilny.ui.modules.links.promoted

import io.github.feelfreelinux.wykopmobilny.api.links.LinksApi
import io.github.feelfreelinux.wykopmobilny.base.BasePresenter
import io.github.feelfreelinux.wykopmobilny.base.Schedulers

class PromotedPresenter(val schedulers: Schedulers, private val linksApi: LinksApi) : BasePresenter<PromotedView>() {
    var page = 1
    fun getPromotedLinks(shouldRefresh : Boolean) {
        if (shouldRefresh) page = 1
        compositeObservable.add(
                linksApi.getPromoted(page)
                        .subscribeOn(schedulers.backgroundThread())
                        .observeOn(schedulers.mainThread())
                        .subscribe(
                                {
                                    if (it.isNotEmpty()) {
                                        page++
                                        view?.addDataToAdapter(it, shouldRefresh)
                                    } else view?.disableLoading()
                                },
                                { view?.showErrorDialog(it) }
                        )
        )
    }
}