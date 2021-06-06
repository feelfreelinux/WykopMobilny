package io.github.wykopmobilny.ui.modules.links.hits

import io.github.wykopmobilny.api.hits.HitsApi
import io.github.wykopmobilny.base.BasePresenter
import io.github.wykopmobilny.base.Schedulers
import io.github.wykopmobilny.models.dataclass.Link
import io.github.wykopmobilny.ui.fragments.links.LinkActionListener
import io.github.wykopmobilny.ui.fragments.links.LinksInteractor
import io.github.wykopmobilny.utils.intoComposite
import io.reactivex.Single

class HitsPresenter(
    val schedulers: Schedulers,
    val linksInteractor: LinksInteractor,
    private val hitsApi: HitsApi
) : BasePresenter<HitsView>(), LinkActionListener {

    companion object {
        const val HITS_POPULAR = "popular"
        const val HITS_DAY = "day"
        const val HITS_WEEK = "week"
        const val HITS_MONTH = "month"
        const val HITS_YEAR = "year"
    }

    var currentScreen = "popular"
    var yearSelection = 0
    var monthSelection = 0

    fun loadData() {
        when (currentScreen) {
            HITS_POPULAR -> hitsApi.popular()
            HITS_DAY -> hitsApi.currentDay()
            HITS_WEEK -> hitsApi.currentWeek()
            HITS_MONTH -> hitsApi.byMonth(yearSelection, monthSelection)
            else -> hitsApi.byYear(yearSelection)
        }
            .subscribeOn(schedulers.backgroundThread())
            .observeOn(schedulers.mainThread())
            .subscribe(
                {
                    view?.addItems(it, true)
                    view?.disableLoading()
                },
                { view?.showErrorDialog(it) }
            )
            .intoComposite(compositeObservable)
    }

    override fun dig(link: Link) = linksInteractor.dig(link).processLinkSingle(link)

    override fun removeVote(link: Link) = linksInteractor.voteRemove(link).processLinkSingle(link)

    private fun Single<Link>.processLinkSingle(link: Link) {
        this.subscribeOn(schedulers.backgroundThread())
            .observeOn(schedulers.mainThread())
            .subscribe(
                { view?.updateLink(it) },
                {
                    view?.showErrorDialog(it)
                    view?.updateLink(link)
                }
            )
            .intoComposite(compositeObservable)
    }
}
