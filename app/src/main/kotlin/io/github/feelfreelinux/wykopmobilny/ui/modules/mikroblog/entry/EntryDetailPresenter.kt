package io.github.feelfreelinux.wykopmobilny.ui.modules.mikroblog.entry

import io.github.feelfreelinux.wykopmobilny.api.entries.EntriesApi
import io.github.feelfreelinux.wykopmobilny.api.entries.TypedInputStream
import io.github.feelfreelinux.wykopmobilny.base.BasePresenter
import io.github.feelfreelinux.wykopmobilny.base.Schedulers

class EntryDetailPresenter(val schedulers: Schedulers, private val entriesApi: EntriesApi) : BasePresenter<EntryDetailView>() {
    var entryId = 0
    fun loadData() {
        view?.hideInputToolbar()
        compositeObservable.add(
                entriesApi.getEntry(entryId)
                        .subscribeOn(schedulers.backgroundThread())
                        .observeOn(schedulers.mainThread())
                        .subscribe(
                                { view?.showEntry(it) },
                                { view?.showErrorDialog(it) }
                        ))

    }

    fun addComment(body : String, photo: TypedInputStream, containsAdultContent : Boolean) {
        compositeObservable.add(
                entriesApi.addEntryComment(body, entryId, photo, containsAdultContent)
                        .subscribeOn(schedulers.backgroundThread())
                        .observeOn(schedulers.mainThread())
                        .subscribe(
                                {
                                    view?.hideInputbarProgress()
                                    view?.resetInputbarState()
                                    loadData() // Refresh view
                                },
                                {
                                    view?.hideInputbarProgress()
                                    view?.showErrorDialog(it)
                                }
                        ))
    }

    fun addComment(body : String, photo : String?, containsAdultContent : Boolean) {
        compositeObservable.add(
                entriesApi.addEntryComment(body, entryId, photo, containsAdultContent)
                        .subscribeOn(schedulers.backgroundThread())
                        .observeOn(schedulers.mainThread())
                        .subscribe(
                                {
                                    view?.hideInputbarProgress()
                                    view?.resetInputbarState()
                                    loadData() // Refresh view
                                },
                                {
                                    view?.hideInputbarProgress()
                                    view?.showErrorDialog(it)
                                }
                        ))
    }
}