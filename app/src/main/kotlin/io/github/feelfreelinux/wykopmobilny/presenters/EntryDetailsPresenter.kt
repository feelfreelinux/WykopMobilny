package io.github.feelfreelinux.wykopmobilny.presenters
import io.github.feelfreelinux.wykopmobilny.objects.Entry
import io.github.feelfreelinux.wykopmobilny.utils.WykopApiManager


class EntryDetailsPresenter(val entryId : Int, val view : EntryDetailsPresenter.View, val wam: WykopApiManager) {

    fun loadData() =
            wam.getEntryIndex(entryId, {
                view.setAdapterEntry(it as Entry)
            })

    interface View {
        fun setAdapterEntry(entry : Entry)
    }
}