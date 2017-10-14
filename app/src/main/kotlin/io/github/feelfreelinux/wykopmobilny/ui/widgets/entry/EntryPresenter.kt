package io.github.feelfreelinux.wykopmobilny.ui.widgets.entry

import io.github.feelfreelinux.wykopmobilny.api.entries.EntriesApi
import io.github.feelfreelinux.wykopmobilny.base.BasePresenter
import io.github.feelfreelinux.wykopmobilny.utils.rx.SubscriptionHelperApi

class EntryPresenter(val subscriptionHelperApi: SubscriptionHelperApi, val entriesApi: EntriesApi) : BasePresenter<EntryView>() {
    fun deleteEntry(entryId : Int) {
        subscriptionHelperApi.subscribe(entriesApi.deleteEntry(entryId),
                { view?.markEntryAsRemoved() }, { view?.showErrorDialog(it) }, this)
    }

    override fun unsubscribe() {
        super.unsubscribe()
        subscriptionHelperApi.dispose(this)
    }
}