package io.github.feelfreelinux.wykopmobilny.ui.modules.mikroblog.entry

import io.github.feelfreelinux.wykopmobilny.api.entries.EntriesApi
import io.github.feelfreelinux.wykopmobilny.api.entries.TypedInputStream
import io.github.feelfreelinux.wykopmobilny.base.BasePresenter
import io.github.feelfreelinux.wykopmobilny.base.Schedulers
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Entry
import io.github.feelfreelinux.wykopmobilny.models.dataclass.EntryComment
import io.github.feelfreelinux.wykopmobilny.ui.fragments.entries.EntriesInteractor
import io.github.feelfreelinux.wykopmobilny.ui.fragments.entries.EntryActionListener
import io.github.feelfreelinux.wykopmobilny.ui.fragments.entrycomments.EntryCommentActionListener
import io.github.feelfreelinux.wykopmobilny.ui.fragments.entrycomments.EntryCommentInteractor
import io.reactivex.Single

class EntryDetailPresenter(val schedulers: Schedulers, private val entriesApi: EntriesApi, private val entriesInteractor : EntriesInteractor, private val entryCommentInteractor: EntryCommentInteractor) : BasePresenter<EntryDetailView>(), EntryActionListener, EntryCommentActionListener {
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

    fun Single<EntryComment>.processEntryCommentSingle(comment : EntryComment) {
        compositeObservable.add(
                this
                        .subscribeOn(schedulers.backgroundThread())
                        .observeOn(schedulers.mainThread())
                        .subscribe({ view?.updateComment(it) },
                                {
                                    view?.showErrorDialog(it)
                                    view?.updateComment(comment)
                                })
        )
    }

    override fun voteComment(comment: EntryComment) {
        entryCommentInteractor.voteComment(comment).processEntryCommentSingle(comment)
    }

    override fun unvoteComment(comment: EntryComment) {
        entryCommentInteractor.unvoteComment(comment).processEntryCommentSingle(comment)
    }

    override fun deleteComment(comment: EntryComment) {
        entryCommentInteractor.deleteComment(comment).processEntryCommentSingle(comment)
    }

    override fun getVoters(comment: EntryComment) {
        view?.openVotersMenu()
        compositeObservable.add(
                entriesApi.getEntryCommentVoters(comment.id)
                        .subscribeOn(schedulers.backgroundThread())
                        .observeOn(schedulers.mainThread())
                        .subscribe({
                            view?.showVoters(it)
                        }, {
                            view?.showErrorDialog(it)
                        })
        )
    }

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