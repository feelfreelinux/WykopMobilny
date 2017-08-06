package io.github.feelfreelinux.wykopmobilny.presenters

import io.github.feelfreelinux.wykopmobilny.objects.Entry
import io.github.feelfreelinux.wykopmobilny.utils.ApiResponseCallback
import io.github.feelfreelinux.wykopmobilny.utils.WykopApiManager

class HotFeedPresenter(view: FeedPresenter.View, wam: WykopApiManager) : FeedPresenter(view, wam) {
    var period = "24"


    override fun loadData(page : Int) {
        val resultCallback : ApiResponseCallback = {
            result ->
            // For refresh actions, etc we should empty whole list
            view.addDataToAdapter((result as Array<Entry>).asList(), page == 1)
        }

        when (period) {
            "24", "12", "6" ->
                wam.getMikroblogHot(page, "12", resultCallback)
            "newest" ->
                wam.getMikroblogIndex(page, resultCallback)
        }
    }
}