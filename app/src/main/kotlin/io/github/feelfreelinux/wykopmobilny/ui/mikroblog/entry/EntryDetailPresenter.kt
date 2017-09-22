package io.github.feelfreelinux.wykopmobilny.ui.mikroblog.entry

import io.github.feelfreelinux.wykopmobilny.api.entries.EntriesApi
import io.github.feelfreelinux.wykopmobilny.base.BasePresenter
import io.github.feelfreelinux.wykopmobilny.utils.rx.SubscriptionHelperApi

class EntryDetailPresenter(val subscriptionHelper: SubscriptionHelperApi, private val entriesApi: EntriesApi) : BasePresenter<EntryDetailView>() {
    var entryId = 0
    fun loadData() {
        subscriptions.add(
                subscriptionHelper.subscribeOnSchedulers(
                        entriesApi.getEntryIndex(entryId))
                        .subscribe(
                                { view?.showEntry(it) },
                                { view?.showErrorDialog(it) }
                        )
        )
    }

}