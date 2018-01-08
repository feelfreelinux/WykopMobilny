package io.github.feelfreelinux.wykopmobilny.ui.modules.mikroblog.feed.hot

import io.github.feelfreelinux.wykopmobilny.api.entries.EntriesApi
import io.github.feelfreelinux.wykopmobilny.base.BasePresenter
import io.github.feelfreelinux.wykopmobilny.base.Schedulers
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Entry


class HotPresenter(private val schedulers: Schedulers, private val entriesApi: EntriesApi) : BasePresenter<HotView>() {
    var page = 1
    var period = "24"

    fun loadData(shouldRefresh : Boolean) {
        if (shouldRefresh) page = 1
        val success : (List<Entry>) -> Unit  = {
            if (it.isNotEmpty()) {
                page++
                view?.addDataToAdapter(it, shouldRefresh)
            }
        }
        val failure : (Throwable) -> Unit  = { view?.showErrorDialog(it) }

        compositeObservable.add(
        when (period) {
            "24", "12", "6" -> {
                entriesApi.getHot(page, period).subscribeOn(schedulers.backgroundThread())
                        .observeOn(schedulers.mainThread())
                        .subscribe(success, failure)
            }
            else -> // Newest
                entriesApi.getStream(page).subscribeOn(schedulers.backgroundThread())
                        .observeOn(schedulers.mainThread())
                        .subscribe(success, failure)
        }
        )
    }
}