package io.github.feelfreelinux.wykopmobilny.ui.mikroblog.entry

import io.github.feelfreelinux.wykopmobilny.api.entries.EntriesApi
import io.github.feelfreelinux.wykopmobilny.base.BasePresenter
import io.github.feelfreelinux.wykopmobilny.api.enqueue

class EntryPresenter(private val entriesApi: EntriesApi) : BasePresenter<EntryView>() {
    var entryId = 0
    fun loadData() {
        entriesApi.getEntryIndex(entryId).enqueue(
                { view?.showEntry(it.body()!!) },
                { view?.showErrorDialog(it) }
        )
    }

}