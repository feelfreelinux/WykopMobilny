package io.github.feelfreelinux.wykopmobilny.ui.widgets.entry.comment

import com.google.auto.factory.AutoFactory
import com.google.auto.factory.Provided
import io.github.feelfreelinux.wykopmobilny.api.entries.EntriesApi
import io.github.feelfreelinux.wykopmobilny.base.BasePresenter
import io.github.feelfreelinux.wykopmobilny.base.Schedulers
import io.github.feelfreelinux.wykopmobilny.models.dataclass.EntryComment
import io.github.feelfreelinux.wykopmobilny.ui.modules.NewNavigatorApi
import io.github.feelfreelinux.wykopmobilny.utils.ClipboardHelperApi
import io.github.feelfreelinux.wykopmobilny.utils.textview.removeHtml
import io.github.feelfreelinux.wykopmobilny.utils.wykop_link_handler.WykopLinkHandlerApi

@AutoFactory
class CommentPresenter(@Provided val schedulers: Schedulers,
                     @Provided val entriesApi: EntriesApi,
                     @Provided val clipboardHelperApi: ClipboardHelperApi,
                     @Provided val navigatorApi: NewNavigatorApi,
                     @Provided val linkHandler: WykopLinkHandlerApi) : BasePresenter<CommentView>() {
    var commentId = -1
    fun deleteComment() {
        compositeObservable.add(
                entriesApi.deleteEntryComment(commentId)
                        .subscribeOn(schedulers.backgroundThread())
                        .observeOn(schedulers.mainThread())
                        .subscribe({ view?.markCommentAsRemoved() }, { view?.showErrorDialog(it) })
        )
    }

    fun voteComment() {
        compositeObservable.add(
                entriesApi.voteComment(commentId)
                        .subscribeOn(schedulers.backgroundThread())
                        .observeOn(schedulers.mainThread())
                        .subscribe({ view?.markCommentVoted(it.voteCount) }, { view?.showErrorDialog(it) })
        )
    }

    fun unvoteComment() {
        compositeObservable.add(
                entriesApi.unvoteComment(commentId)
                        .subscribeOn(schedulers.backgroundThread())
                        .observeOn(schedulers.mainThread())
                        .subscribe({ view?.markCommentUnvoted(it.voteCount) }, { view?.showErrorDialog(it) })
        )
    }

    fun copyContent(comment : EntryComment) {
        clipboardHelperApi.copyTextToClipboard(comment.body.removeHtml(), "entry-comment-text-copied")
    }

    fun editEntryComment(comment : EntryComment) {
        navigatorApi.openEditEntryCommentActivity(comment.body, comment.entryId, comment.id)
    }

    fun handleLink(url : String) {
        linkHandler.handleUrl(url, false)
    }

    fun reportContent() {
        navigatorApi.openReportEntryCommentScreen(commentId)
    }

    fun getVoters() {
        compositeObservable.add(
                entriesApi.getEntryCommentVoters(commentId)
                        .subscribeOn(schedulers.backgroundThread())
                        .observeOn(schedulers.mainThread())
                        .subscribe(
                                { view?.showVoters(it) },
                                { view?.showErrorDialog(it) }
                        )
        )
    }
}