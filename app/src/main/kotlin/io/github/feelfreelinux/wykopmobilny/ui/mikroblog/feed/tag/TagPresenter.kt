package io.github.feelfreelinux.wykopmobilny.ui.mikroblog.feed.tag

import io.github.feelfreelinux.wykopmobilny.api.WykopApi
import io.github.feelfreelinux.wykopmobilny.base.BasePresenter
import io.github.feelfreelinux.wykopmobilny.ui.mikroblog.feed.BaseFeedPresenter

class TagPresenter(val apiManager : WykopApi, val tag : String) : BasePresenter<TagView>(), BaseFeedPresenter {
    override fun loadData(page : Int) {
        apiManager.getTagEntries(page, tag) {
            it.fold(
                    { view?.addDataToAdapter(it.items.asList(), page == 1) },
                    { view?.showErrorDialog(it) })
        }
    }
}