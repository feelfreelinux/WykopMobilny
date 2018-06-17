package io.github.feelfreelinux.wykopmobilny.ui.modules.search.links

import io.github.feelfreelinux.wykopmobilny.api.search.SearchApi
import io.github.feelfreelinux.wykopmobilny.base.BasePresenter
import io.github.feelfreelinux.wykopmobilny.base.Schedulers
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Link
import io.github.feelfreelinux.wykopmobilny.ui.fragments.links.LinkActionListener
import io.github.feelfreelinux.wykopmobilny.ui.fragments.links.LinksInteractor
import io.reactivex.Single

class LinkSearchPresenter(val schedulers: Schedulers, val searchApi: SearchApi, val linksInteractor: LinksInteractor) : BasePresenter<LinkSearchView>(), LinkActionListener {
    var page = 1
    fun searchLinks(q : String, shouldRefresh : Boolean) {
        if (shouldRefresh) page = 1
        compositeObservable.add(
                searchApi.searchLinks(page, q)
                        .subscribeOn(schedulers.backgroundThread())
                        .observeOn(schedulers.mainThread())
                        .subscribe({
                            view?.showSearchEmptyView = (page == 1 && it.isEmpty())
                            if (it.isNotEmpty()) {
                                page++
                                view?.addItems(it, shouldRefresh)
                            } else {
                                view?.addItems(it, (page == 1))
                                view?.disableLoading()
                            }
                        }, { view?.showErrorDialog(it) })
        )
    }

    override fun dig(link: Link) {
        linksInteractor.dig(link).processLinkSingle(link)
    }

    override fun removeVote(link: Link) {
        linksInteractor.voteRemove(link).processLinkSingle(link)
    }

    fun Single<Link>.processLinkSingle(link: Link) {
        compositeObservable.add(
                this
                        .subscribeOn(schedulers.backgroundThread())
                        .observeOn(schedulers.mainThread())
                        .subscribe({ view?.updateLink(it) },
                                {
                                    view?.showErrorDialog(it)
                                    view?.updateLink(link)
                                })
        )
    }
}