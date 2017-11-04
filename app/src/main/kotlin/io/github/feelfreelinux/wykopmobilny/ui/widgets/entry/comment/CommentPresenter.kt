package io.github.feelfreelinux.wykopmobilny.ui.widgets.entry.comment

import io.github.feelfreelinux.wykopmobilny.api.entries.EntriesApi
import io.github.feelfreelinux.wykopmobilny.base.BasePresenter
import io.github.feelfreelinux.wykopmobilny.utils.rx.SubscriptionHelperApi

class CommentPresenter(val subscriptionHelperApi: SubscriptionHelperApi, val entriesApi: EntriesApi) : BasePresenter<CommentView>() {
    fun deleteComment(commentId : Int) {
        subscriptionHelperApi.subscribe(entriesApi.deleteEntryComment(commentId),
                { view?.markCommentAsRemoved() }, { view?.showErrorDialog(it) }, this)
    }

    override fun unsubscribe() {
        super.unsubscribe()
        subscriptionHelperApi.dispose(this)
    }
}