package io.github.feelfreelinux.wykopmobilny.ui.fragments.entries

import io.github.feelfreelinux.wykopmobilny.api.entries.EntriesApi
import io.github.feelfreelinux.wykopmobilny.base.BasePresenter
import io.github.feelfreelinux.wykopmobilny.base.Schedulers
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Entry

open class EntriesFragmentPresenter(val schedulers : Schedulers, val entriesApi: EntriesApi) : BasePresenter<EntriesFragmentView>(), EntryActionListener {
    override fun voteEntry(entry: Entry) {
        compositeObservable.add(
                entriesApi.voteEntry(entry.id, true)
                        .subscribeOn(schedulers.backgroundThread())
                        .observeOn(schedulers.mainThread())
                        .subscribe({
                            entry.voteCount = it.voteCount
                            entry.isVoted = true
                            view?.updateEntry(entry)
                        }, {
                            view?.showErrorDialog(it)
                            view?.updateEntry(entry)
                        })
        )
    }

    override fun unvoteEntry(entry: Entry) {
        compositeObservable.add(
                entriesApi.unvoteEntry(entry.id, true)
                        .subscribeOn(schedulers.backgroundThread())
                        .observeOn(schedulers.mainThread())
                        .subscribe({
                            entry.voteCount = it.voteCount
                            entry.isVoted = false
                            view?.updateEntry(entry)
                        }, {
                            view?.showErrorDialog(it)
                            view?.updateEntry(entry)
                        })
        )
    }

    override fun markFavorite(entry: Entry) {
        compositeObservable.add(
                entriesApi.markFavorite(entry.id)
                        .subscribeOn(schedulers.backgroundThread())
                        .observeOn(schedulers.mainThread())
                        .subscribe({
                            entry.isFavorite = it.userFavorite
                            view?.updateEntry(entry)
                        }, {
                            view?.showErrorDialog(it)
                            view?.updateEntry(entry)
                        })
        )
    }

    override fun deleteEntry(entry: Entry) {
        compositeObservable.add(
                entriesApi.deleteEntry(entry.id)
                        .subscribeOn(schedulers.backgroundThread())
                        .observeOn(schedulers.mainThread())
                        .subscribe({
                            entry.embed = null
                            entry.survey = null
                            entry.body = "[Wpis usunięty]"
                            view?.updateEntry(entry)
                        }, {
                            view?.showErrorDialog(it)
                            view?.updateEntry(entry)
                        })
        )
    }

    override fun voteSurvey(entry: Entry, index : Int) {
        compositeObservable.add(
                entriesApi.voteSurvey(entry.id, index)
                        .subscribeOn(schedulers.backgroundThread())
                        .observeOn(schedulers.mainThread())
                        .subscribe({
                            entry.survey = it
                            view?.updateEntry(entry)
                        }, {
                            view?.showErrorDialog(it)
                            view?.updateEntry(entry)
                        })
        )
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
}