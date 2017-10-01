package io.github.feelfreelinux.wykopmobilny.ui.mikroblog.feed.hot

import io.github.feelfreelinux.wykopmobilny.models.dataclass.Entry
import io.github.feelfreelinux.wykopmobilny.api.stream.StreamApi
import io.github.feelfreelinux.wykopmobilny.base.BasePresenter
import io.github.feelfreelinux.wykopmobilny.ui.mikroblog.feed.BaseFeedPresenter
import io.github.feelfreelinux.wykopmobilny.utils.rx.SubscriptionHelperApi

class HotPresenter(private val subscriptionHelper: SubscriptionHelperApi, private val streamApi: StreamApi) : BasePresenter<HotView>(), BaseFeedPresenter {
    var period = "24"

    override fun loadData(page : Int) {
        val success : (List<Entry>) -> Unit  = { view?.addDataToAdapter(it, page == 1) }
        val failure : (Throwable) -> Unit  = { view?.showErrorDialog(it) }


        when (period) {
            "24", "12", "6" -> {
                subscriptionHelper.subscribe(
                        streamApi.getMirkoblogHot(page, period.toInt()), success, failure, this)
            }
            "newest" ->
                subscriptionHelper.subscribe(
                        streamApi.getMikroblogIndex(page), success, failure, this)
        }
    }

    override fun unsubscribe() {
        super.unsubscribe()
        subscriptionHelper.dispose(this)
    }
}