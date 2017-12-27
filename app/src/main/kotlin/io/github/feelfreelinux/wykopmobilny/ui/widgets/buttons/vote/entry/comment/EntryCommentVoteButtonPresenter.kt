package io.github.feelfreelinux.wykopmobilny.ui.widgets.buttons.vote.entry.comment

import io.github.feelfreelinux.wykopmobilny.api.entries.EntriesApi
import io.github.feelfreelinux.wykopmobilny.base.BasePresenter
import io.github.feelfreelinux.wykopmobilny.ui.widgets.buttons.vote.base.BaseVoteButtonPresenter
import io.github.feelfreelinux.wykopmobilny.utils.rx.SubscriptionHelperApi

class EntryCommentVoteButtonPresenter(private val subscriptionHandler : SubscriptionHelperApi, private val entriesApi : EntriesApi) : BasePresenter<EntryCommentVoteButtonView>(), BaseVoteButtonPresenter  {
    var commentId = 0

    override fun unvote() {
        view?.isButtonEnabled = false
        subscriptionHandler.subscribe(entriesApi.unvoteComment(commentId),
                    {
                        view?.isButtonEnabled = true
                        view?.voteCount = it.voteCount
                        view?.isButtonSelected = false
                    }, { view?.showErrorDialog(it) }, this)
    }

    override fun vote() {
        view?.isButtonEnabled = false
        subscriptionHandler.subscribe(entriesApi.voteComment(commentId),
                {
                    view?.isButtonEnabled = true
                    view?.voteCount = it.voteCount
                    view?.isButtonSelected = true
                }, { view?.showErrorDialog(it) }, this)
    }

    override fun unsubscribe() {
        super.unsubscribe()
        subscriptionHandler.dispose(this)
    }
}