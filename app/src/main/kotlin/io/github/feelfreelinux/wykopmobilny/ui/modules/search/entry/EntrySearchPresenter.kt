package io.github.feelfreelinux.wykopmobilny.ui.modules.search.entry

import io.github.feelfreelinux.wykopmobilny.api.search.SearchApi
import io.github.feelfreelinux.wykopmobilny.base.BasePresenter
import io.github.feelfreelinux.wykopmobilny.base.Schedulers

class EntrySearchPresenter(val schedulers: Schedulers, val searchApi: SearchApi) : BasePresenter<EntrySearchView>() {
    var page = 1
    fun searchEntries(q : String, shouldRefresh : Boolean) {
        if (shouldRefresh) page = 1
        compositeObservable.add(
                searchApi.searchEntries(page, q)
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