package io.github.feelfreelinux.wykopmobilny.ui.modules.links.hits

import io.github.feelfreelinux.wykopmobilny.api.hits.HitsApi
import io.github.feelfreelinux.wykopmobilny.base.BasePresenter
import io.github.feelfreelinux.wykopmobilny.base.Schedulers
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Link
import io.github.feelfreelinux.wykopmobilny.ui.fragments.links.LinkActionListener
import io.github.feelfreelinux.wykopmobilny.ui.fragments.links.LinksInteractor
import io.reactivex.Single

class HitsPresenter(val schedulers: Schedulers, val hitsApi: HitsApi, val linksInteractor : LinksInteractor) : BasePresenter<HitsView>(), LinkActionListener {
    companion object {
        val HITS_POPULAR = "popular"
        val HITS_DAY = "day"
        val HITS_WEEK = "week"
        val HITS_MONTH = "month"
        val HITS_YEAR = "year"
    }
    var currentScreen = "popular"
    var yearSelection = 0
    var monthSelection = 0

    fun loadData() {
        compositeObservable.add(
                when(currentScreen) {
                    HITS_POPULAR -> hitsApi.popular()
                    HITS_DAY -> hitsApi.currentDay()
                    HITS_WEEK -> hitsApi.currentWeek()
                    HITS_MONTH -> hitsApi.byMonth(yearSelection, monthSelection)
                    else -> hitsApi.byYear(yearSelection)
                }
                        .subscribeOn(schedulers.backgroundThread())
                        .observeOn(schedulers.mainThread())
                        .subscribe({ view?.addItems(it, true) }, { view?.showErrorDialog(it) })
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