package io.github.feelfreelinux.wykopmobilny.ui.modules.favorite.entry

import io.github.feelfreelinux.wykopmobilny.api.entries.EntriesApi
import io.github.feelfreelinux.wykopmobilny.api.links.LinksApi
import io.github.feelfreelinux.wykopmobilny.base.BasePresenter
import io.github.feelfreelinux.wykopmobilny.base.Schedulers
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Entry
import io.github.feelfreelinux.wykopmobilny.ui.fragments.entries.EntriesInteractor
import io.github.feelfreelinux.wykopmobilny.ui.fragments.entries.EntryActionListener
import io.reactivex.Single

class EntryFavoritePresenter(val schedulers: Schedulers, val entryApi: EntriesApi, val entriesInteractor : EntriesInteractor) : BasePresenter<EntryFavoriteView>(), EntryActionListener {
    var page = 1
    fun loadData(shouldRefresh : Boolean) {
        if (shouldRefresh) page = 1
        compositeObservable.add(
                entryApi.getObserved(page)
                        .subscribeOn(schedulers.backgroundThread())
                        .observeOn(schedulers.mainThread())
                        .subscribe(
                                {
                                    if (it.isNotEmpty()) {
                                        page++
                                        view?.addItems(it, shouldRefresh)
                                    } else view?.disableLoading()
                                },
                                { view?.showErrorDialog(it) }
                        )
        )
    }

    override fun voteEntry(entry: Entry) {
        entriesInteractor.voteEntry(entry).processEntrySingle(entry)
    }

    override fun unvoteEntry(entry: Entry) {
        entriesInteractor.unvoteEntry(entry).processEntrySingle(entry)

    }

    override fun markFavorite(entry: Entry) {
        entriesInteractor.markFavorite(entry).processEntrySingle(entry)

    }

    override fun deleteEntry(entry: Entry) {
        entriesInteractor.deleteEntry(entry).processEntrySingle(entry)

    }

    override fun voteSurvey(entry: Entry, index : Int) {
        entriesInteractor.voteSurvey(entry, index).processEntrySingle(entry)
    }

    override fun getVoters(entry: Entry) {
        view?.openVotersMenu()
        compositeObservable.add(
                entryApi.getEntryVoters(entry.id)
                        .subscribeOn(schedulers.backgroundThread())
                        .observeOn(schedulers.mainThread())
                        .subscribe({
                            view?.showVoters(it)
                        }, {
                            view?.showErrorDialog(it)
                        })
        )
    }

    fun Single<Entry>.processEntrySingle(entry : Entry) {
        compositeObservable.add(
                this
                        .subscribeOn(schedulers.backgroundThread())
                        .observeOn(schedulers.mainThread())
                        .subscribe({ view?.updateEntry(it) },
                                {
                                    view?.showErrorDialog(it)
                                    view?.updateEntry(entry)
                                })
        )
    }
}