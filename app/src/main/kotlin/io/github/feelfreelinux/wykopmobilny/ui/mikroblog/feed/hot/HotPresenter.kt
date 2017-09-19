package io.github.feelfreelinux.wykopmobilny.ui.mikroblog.feed.hot

import io.github.feelfreelinux.wykopmobilny.api.Entry
import io.github.feelfreelinux.wykopmobilny.api.stream.StreamApi
import io.github.feelfreelinux.wykopmobilny.base.BasePresenter
import io.github.feelfreelinux.wykopmobilny.ui.mikroblog.feed.BaseFeedPresenter
import io.github.feelfreelinux.wykopmobilny.utils.rx.SubscriptionHelperApi
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class HotPresenter(private val subscriptionHelper: SubscriptionHelperApi, private val streamApi: StreamApi) : BasePresenter<HotView>(), BaseFeedPresenter {
    var period = "24"

    override fun loadData(page : Int) {
        val success : (List<Entry>) -> Unit  = { view?.addDataToAdapter(it, page == 1) }
        val failure : (Throwable) -> Unit  = { view?.showErrorDialog(it) }


        when (period) {
            "24", "12", "6" -> {
                subscriptions.add(
                    subscriptionHelper.subscribeOnSchedulers(
                        streamApi.getMirkoblogHot(page, period.toInt())
                    ).subscribe(success, failure)
                )
            }
            "newest" ->
                subscriptions.add(
                        subscriptionHelper.subscribeOnSchedulers(
                                streamApi.getMikroblogIndex(page)
                        ).subscribe(success, failure)
                )
            }
    }
}