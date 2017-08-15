package io.github.feelfreelinux.wykopmobilny.ui.mikroblog.entry

import io.github.feelfreelinux.wykopmobilny.base.Presenter
import io.github.feelfreelinux.wykopmobilny.utils.WykopApi

class EntryPresenter(val apiManager : WykopApi, val entryId : Int) : Presenter<EntryContract.View>(), EntryContract.Presenter {
    override fun loadData() {
        apiManager.getEntryIndex(entryId) {
            it.fold(
                    { view?.showEntry(it) },
                    { view?.showErrorDialog(it) })
        }
    }

}