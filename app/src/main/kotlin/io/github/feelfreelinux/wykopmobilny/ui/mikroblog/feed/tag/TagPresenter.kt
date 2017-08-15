package io.github.feelfreelinux.wykopmobilny.ui.mikroblog.feed.tag

import io.github.feelfreelinux.wykopmobilny.base.Presenter
import io.github.feelfreelinux.wykopmobilny.utils.WykopApi

class TagPresenter(val apiManager : WykopApi, val tag : String) : Presenter<TagContract.View>(), TagContract.Presenter {
    override fun loadData(page : Int) {
        apiManager.getTagEntries(page, tag) {
            it.fold(
                    { view?.addDataToAdapter(it.items.asList(), page == 1) },
                    { view?.showErrorDialog(it) })
        }
    }
}