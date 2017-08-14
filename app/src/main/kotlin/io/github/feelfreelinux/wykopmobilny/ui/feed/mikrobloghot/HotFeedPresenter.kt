package io.github.feelfreelinux.wykopmobilny.ui.feed.mikrobloghot

import io.github.feelfreelinux.wykopmobilny.presenters.FeedPresenter
import io.github.feelfreelinux.wykopmobilny.utils.WykopApi
import io.github.feelfreelinux.wykopmobilny.utils.setupSubscribeIOAndroid

class HotFeedPresenter(view: View, wam: WykopApi) : FeedPresenter(view, wam) {
    var period = "24"


    override fun loadData(page : Int) {

        when (period) {
            "24", "12", "6" -> {
                wam.getMikroblogHot(page, "12").setupSubscribeIOAndroid().subscribe{ (result, ee) -> view.addDataToAdapter(result!!.asList(), page == 1) }
            }
            "newest" ->
                wam.getMikroblogIndex(page).setupSubscribeIOAndroid().subscribe{ (result, ee) -> view.addDataToAdapter(result!!.asList(), page == 1)
                }
    }
}}