package io.github.feelfreelinux.wykopmobilny.ui.mikroblog.feed.hot

import io.github.feelfreelinux.wykopmobilny.api.ApiResultCallback
import io.github.feelfreelinux.wykopmobilny.base.Presenter
import io.github.feelfreelinux.wykopmobilny.api.Entry
import io.github.feelfreelinux.wykopmobilny.api.WykopApi

class HotPresenter(val apiManager : WykopApi) : Presenter<HotContract.View>(), HotContract.Presenter {
    override var period = "24"

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