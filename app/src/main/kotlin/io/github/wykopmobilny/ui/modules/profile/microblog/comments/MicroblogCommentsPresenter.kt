package io.github.wykopmobilny.ui.modules.profile.microblog.comments

import io.github.wykopmobilny.api.entries.EntriesApi
import io.github.wykopmobilny.api.profile.ProfileApi
import io.github.wykopmobilny.base.BasePresenter
import io.github.wykopmobilny.base.Schedulers
import io.github.wykopmobilny.models.dataclass.EntryComment
import io.github.wykopmobilny.ui.fragments.entrycomments.EntryCommentActionListener
import io.github.wykopmobilny.ui.fragments.entrycomments.EntryCommentInteractor
import io.github.wykopmobilny.utils.intoComposite
import io.reactivex.Single

class MicroblogCommentsPresenter(
    val schedulers: Schedulers,
    val profileApi: ProfileApi,
    val entriesApi: EntriesApi,
    private val entryCommentInteractor: EntryCommentInteractor
) : BasePresenter<MicroblogCommentsView>(), EntryCommentActionListener {

    var page = 1
    lateinit var username: String

    fun loadData(shouldRefresh: Boolean) {
        if (shouldRefresh) page = 1
        profileApi.getEntriesComments(username, page)
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
            .intoComposite(compositeObservable)
    }

    override fun voteComment(comment: EntryComment) =
        entryCommentInteractor.voteComment(comment).processEntryCommentSingle(comment)

    override fun unvoteComment(comment: EntryComment) =
        entryCommentInteractor.unvoteComment(comment).processEntryCommentSingle(comment)

    override fun deleteComment(comment: EntryComment) =
        entryCommentInteractor.deleteComment(comment).processEntryCommentSingle(comment)

    override fun getVoters(comment: EntryComment) {
        view?.openVotersMenu()
        entriesApi.getEntryCommentVoters(comment.id)
            .subscribeOn(schedulers.backgroundThread())
            .observeOn(schedulers.mainThread())
            .subscribe(
                {
                    view?.showVoters(it)
                },
                {
                    view?.showErrorDialog(it)
                }
            )
            .intoComposite(compositeObservable)
    }

    private fun Single<EntryComment>.processEntryCommentSingle(comment: EntryComment) {
        this
            .subscribeOn(schedulers.backgroundThread())
            .observeOn(schedulers.mainThread())
            .subscribe(
                { view?.updateComment(it) },
                {
                    view?.showErrorDialog(it)
                    view?.updateComment(comment)
                }
            )
            .intoComposite(compositeObservable)
    }
}
