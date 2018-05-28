package io.github.feelfreelinux.wykopmobilny.ui.modules.favorite.links

import io.github.feelfreelinux.wykopmobilny.api.links.LinksApi
import io.github.feelfreelinux.wykopmobilny.base.BasePresenter
import io.github.feelfreelinux.wykopmobilny.base.Schedulers
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Link
import io.github.feelfreelinux.wykopmobilny.ui.fragments.links.LinkActionListener
import io.github.feelfreelinux.wykopmobilny.ui.fragments.links.LinksInteractor
import io.reactivex.Single

class LinksFavoritePresenter(val schedulers: Schedulers, val linksApi: LinksApi, val linksInteractor: LinksInteractor) : BasePresenter<LinksFavoriteView>(), LinkActionListener {
    var page = 1
    fun loadData(shouldRefresh : Boolean) {
        if (shouldRefresh) page = 1
        compositeObservable.add(
                linksApi.getObserved(page)
                        .subscribeOn(schedulers.backgroundThread())
                        .observeOn(schedulers.mainThread())
                        .subscribe(
                                {
                                    if (it.isNotEmpty()) {
                                        page++
                                        view?.addItems(it, shouldRefresh)
                                    } else view?.disableLoading()
                                },
                                { view?.showErrorDialog(it) }
                        )
        )
    }

    override fun dig(link: Link) {
        linksInteractor.dig(link).processLinkSingle(link)
    }

    override fun removeVote(link: Link) {
        linksInteractor.voteRemove(link).processLinkSingle(link)
    }

    fun Single<Link>.processLinkSingle(link : Link) {
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