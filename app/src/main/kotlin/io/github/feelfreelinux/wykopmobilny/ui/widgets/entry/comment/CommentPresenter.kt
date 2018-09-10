package io.github.feelfreelinux.wykopmobilny.ui.widgets.entry.comment

import io.github.feelfreelinux.wykopmobilny.api.entries.EntriesApi
import io.github.feelfreelinux.wykopmobilny.base.BasePresenter
import io.github.feelfreelinux.wykopmobilny.base.Schedulers
import io.github.feelfreelinux.wykopmobilny.ui.modules.NewNavigatorApi
import io.github.feelfreelinux.wykopmobilny.utils.wykop_link_handler.WykopLinkHandlerApi

class CommentPresenter(
  val schedulers: Schedulers,
  val entriesApi: EntriesApi,
  val navigatorApi: NewNavigatorApi,
  val linkHandler: WykopLinkHandlerApi) : BasePresenter<CommentView>() {
    var commentId = -1
    fun deleteComment() {
        compositeObservable.add(
                entriesApi.deleteEntryComment(commentId)
                        .subscribeOn(schedulers.backgroundThread())
                        .observeOn(schedulers.mainThread())
                        .subscribe({ view?.markCommentAsRemoved() }, { view?.showErrorDialog(it) })
        )
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