package io.github.feelfreelinux.wykopmobilny.ui.modules.links.hits

import io.github.feelfreelinux.wykopmobilny.api.hits.HitsApi
import io.github.feelfreelinux.wykopmobilny.base.BasePresenter
import io.github.feelfreelinux.wykopmobilny.base.Schedulers

class HitsPresenter(val schedulers: Schedulers, val hitsApi: HitsApi) : BasePresenter<HitsView>() {
    companion object {
        val HITS_POPULAR = "popular"
        val HITS_DAY = "day"
        val HITS_WEEK = "week"
        val HITS_MONTH = "month"
        val HITS_YEAR = "year"
    }
    var currentScreen = "popular"
    var yearSelection = 2005
    var monthSelection = 12

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
                        .subscribe({ view?.showHits(it) }, { view?.showErrorDialog(it) })
        )
    }
}