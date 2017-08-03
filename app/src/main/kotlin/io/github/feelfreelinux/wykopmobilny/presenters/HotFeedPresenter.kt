package io.github.feelfreelinux.wykopmobilny.presenters

import io.github.feelfreelinux.wykopmobilny.objects.Entry
import io.github.feelfreelinux.wykopmobilny.utils.ApiResponseCallback
import io.github.feelfreelinux.wykopmobilny.utils.WykopApiManager

class HotFeedPresenter(wam: WykopApiManager, callbacks: FeedViewCallbacks) : FeedPresenter(wam, callbacks) {
    var period = "24"
    var resultCallback : ApiResponseCallback = {
        result ->
        run {
            entryList.addAll(result as Array<Entry>)
            dataLoadedListener.invoke()
        }
    }

    override fun loadData(page : Int) {
        // For refresh actions, etc we should empty whole list
        if (page == 1) entryList.clear()

        when (period) {
            "24", "12", "6" ->
                wam.getMikroblogHot(page, "12", resultCallback)
            "newest" ->
                wam.getMikroblogIndex(page, resultCallback)
        }
    }
}