package io.github.feelfreelinux.wykopmobilny.presenters
import io.github.feelfreelinux.wykopmobilny.objects.Entry
import io.github.feelfreelinux.wykopmobilny.utils.WykopApi
import io.github.feelfreelinux.wykopmobilny.utils.setupSubscribeIOAndroid


class EntryDetailsPresenter(val entryId: Int, val view: View, val wam: WykopApi) {

    fun loadData() =
            wam.getEntryIndex(entryId).setupSubscribeIOAndroid().subscribe{(result, ee) -> view.setAdapterEntry(result!!)}
    interface View {
        fun setAdapterEntry(entry : Entry)
    }
}