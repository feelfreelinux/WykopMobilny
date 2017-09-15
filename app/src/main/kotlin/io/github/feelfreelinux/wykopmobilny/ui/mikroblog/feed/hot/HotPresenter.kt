package io.github.feelfreelinux.wykopmobilny.ui.mikroblog.feed.hot

import io.github.feelfreelinux.wykopmobilny.api.ApiResultCallback
import io.github.feelfreelinux.wykopmobilny.api.Entry
import io.github.feelfreelinux.wykopmobilny.api.WykopApi
import io.github.feelfreelinux.wykopmobilny.base.BasePresenter
import io.github.feelfreelinux.wykopmobilny.ui.mikroblog.feed.BaseFeedPresenter

class HotPresenter(val apiManager : WykopApi) : BasePresenter<HotView>(), BaseFeedPresenter {
    var period = "24"

    override fun loadData(page : Int) {
        val callback : ApiResultCallback<Array<Entry>> = {
            it.fold(
                    { view?.addDataToAdapter(it.asList(), page == 1) },
                    { view?.showErrorDialog(it) }
            )
        }

        when (period) {
            "24", "12", "6" -> {
                apiManager.getMikroblogHot(page, period, callback)
            }
            "newest" ->
                apiManager.getMikroblogIndex(page, callback)
            }
    }
}