package io.github.feelfreelinux.wykopmobilny.ui.modules.search.links

import io.github.feelfreelinux.wykopmobilny.api.search.SearchApi
import io.github.feelfreelinux.wykopmobilny.base.BasePresenter
import io.github.feelfreelinux.wykopmobilny.base.Schedulers

class LinkSearchPresenter(val schedulers: Schedulers, val searchApi: SearchApi) : BasePresenter<LinkSearchView>() {
    var page = 1
    fun searchLinks(q : String, shouldRefresh : Boolean) {
        if (shouldRefresh) page = 1
        compositeObservable.add(
                searchApi.searchLinks(page, q)
                        .subscribeOn(schedulers.backgroundThread())
                        .observeOn(schedulers.mainThread())
                        .subscribe({
                            view?.showSearchEmptyView = page == 1 && it.isEmpty()
                            when {
                                it.isNotEmpty() -> {
                                    page++
                                    view?.addDataToAdapter(it, shouldRefresh)
                                }
                                page == 1 -> view?.addDataToAdapter(it, true)
                                else -> view?.disableLoading()
                            }
                        },
                                { view?.showErrorDialog(it) })
        )
    }
}