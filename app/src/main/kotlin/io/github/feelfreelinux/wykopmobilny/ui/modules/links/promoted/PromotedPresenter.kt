package io.github.feelfreelinux.wykopmobilny.ui.modules.links.promoted

import io.github.feelfreelinux.wykopmobilny.api.links.LinksApi
import io.github.feelfreelinux.wykopmobilny.base.BasePresenter
import io.github.feelfreelinux.wykopmobilny.base.Schedulers
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Entry
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Link
import io.github.feelfreelinux.wykopmobilny.ui.fragments.links.LinkActionListener
import io.github.feelfreelinux.wykopmobilny.ui.fragments.links.LinksInteractor
import io.reactivex.Single

class PromotedPresenter(val schedulers: Schedulers, private val linksApi: LinksApi, val linksInteractor: LinksInteractor) : BasePresenter<PromotedView>(), LinkActionListener {
    override fun dig(link: Link) {
        linksInteractor.dig(link).processLinkSingle(link)
    }

    override fun removeVote(link: Link) {
        linksInteractor.voteRemove(link).processLinkSingle(link)
    }

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
                                        view?.addItems(it, shouldRefresh)
                                    } else view?.disableLoading()
                                },
                                { view?.showErrorDialog(it) }
                        )
        )
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