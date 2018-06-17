package io.github.feelfreelinux.wykopmobilny.ui.modules.search.entry

import io.github.feelfreelinux.wykopmobilny.api.entries.EntriesApi
import io.github.feelfreelinux.wykopmobilny.api.search.SearchApi
import io.github.feelfreelinux.wykopmobilny.base.BasePresenter
import io.github.feelfreelinux.wykopmobilny.base.Schedulers
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Entry
import io.github.feelfreelinux.wykopmobilny.ui.fragments.entries.EntriesInteractor
import io.github.feelfreelinux.wykopmobilny.ui.fragments.entries.EntryActionListener
import io.github.feelfreelinux.wykopmobilny.utils.printout
import io.reactivex.Single

class EntrySearchPresenter(val schedulers: Schedulers, val searchApi: SearchApi, val entriesApi: EntriesApi, val entriesInteractor: EntriesInteractor) : BasePresenter<EntrySearchView>(), EntryActionListener {
    var page = 1
    fun searchEntries(q : String, shouldRefresh : Boolean) {
        if (shouldRefresh) page = 1
        compositeObservable.add(
                searchApi.searchEntries(page, q)
                        .subscribeOn(schedulers.backgroundThread())
                        .observeOn(schedulers.mainThread())
                        .subscribe({
                            view?.showSearchEmptyView = (page == 1 && it.isEmpty())
                            if (it.isNotEmpty()) {
                                page++
                                view?.addItems(it, shouldRefresh)
                            } else {
                                view?.addItems(it, (page == 1))
                                view?.disableLoading()
                            }
                        }, { view?.showErrorDialog(it) })
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
                entriesApi.getEntryVoters(entry.id)
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