package io.github.feelfreelinux.wykopmobilny.ui.mikroblog.entry

import io.github.feelfreelinux.wykopmobilny.api.WykopApi
import io.github.feelfreelinux.wykopmobilny.base.BasePresenter

class EntryPresenter(private val apiManager : WykopApi, private val entryId : Int) : BasePresenter<EntryView>() {
    fun loadData() {
        apiManager.getEntryIndex(entryId) {
            it.fold(
                    { view?.showEntry(it) },
                    { view?.showErrorDialog(it) })
        }
    }

}