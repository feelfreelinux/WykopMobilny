package io.github.feelfreelinux.wykopmobilny.ui.modules.links.upcoming

import io.github.feelfreelinux.wykopmobilny.api.links.LinksApi
import io.github.feelfreelinux.wykopmobilny.base.BasePresenter
import io.github.feelfreelinux.wykopmobilny.base.Schedulers

class UpcomingPresenter(val schedulers: Schedulers, private val linksApi: LinksApi) : BasePresenter<UpcomingView>() {
    companion object {
        val SORTBY_COMMENTS = "comments"
        val SORTBY_VOTES = "votes"
        val SORTBY_DATE = "date"
        val SORTBY_ACTIVE = "active"
    }
    var page = 1
    var sortBy = "comments"
    fun getUpcomingLinks(shouldRefresh: Boolean) {
        if (shouldRefresh) page = 1
        compositeObservable.add(
                linksApi.getUpcoming(page, sortBy)
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
