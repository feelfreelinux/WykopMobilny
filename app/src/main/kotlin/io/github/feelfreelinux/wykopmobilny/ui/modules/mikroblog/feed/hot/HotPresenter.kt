package io.github.feelfreelinux.wykopmobilny.ui.modules.mikroblog.feed.hot

import io.github.feelfreelinux.wykopmobilny.api.entries.EntriesApi
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Entry
import io.github.feelfreelinux.wykopmobilny.base.BasePresenter
import io.github.feelfreelinux.wykopmobilny.utils.rx.SubscriptionHelperApi

class HotPresenter(private val subscriptionHelper: SubscriptionHelperApi, private val entriesApi: EntriesApi) : BasePresenter<HotView>() {
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

        when (period) {
            "24", "12", "6" -> {
                subscriptionHelper.subscribe(
                        entriesApi.getHot(page, period), success, failure, this)
            }
            "newest" ->
                subscriptionHelper.subscribe(
                        entriesApi.getStream(page), success, failure, this)
        }
    }

    override fun unsubscribe() {
        super.unsubscribe()
        subscriptionHelper.dispose(this)
    }
}