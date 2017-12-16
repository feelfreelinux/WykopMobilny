package io.github.feelfreelinux.wykopmobilny.ui.modules.search.links

import io.github.feelfreelinux.wykopmobilny.api.search.SearchApi
import io.github.feelfreelinux.wykopmobilny.base.BasePresenter
import io.github.feelfreelinux.wykopmobilny.ui.modules.search.entry.EntrySearchView
import io.github.feelfreelinux.wykopmobilny.utils.rx.SubscriptionHelperApi

class LinkSearchPresenter(val subscriptionHelperApi: SubscriptionHelperApi, val searchApi: SearchApi) : BasePresenter<LinkSearchView>() {
    var page = 1
    fun searchLinks(q : String, shouldRefresh : Boolean) {
        if (shouldRefresh) page = 1
        subscriptionHelperApi.subscribe(searchApi.searchLinks(page, q),
                {
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
                { view?.showErrorDialog(it) }, this)
    }
}