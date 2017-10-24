package io.github.feelfreelinux.wykopmobilny.ui.widgets.buttons.vote.entry.comment

import io.github.feelfreelinux.wykopmobilny.api.entries.EntriesApi
import io.github.feelfreelinux.wykopmobilny.base.BasePresenter
import io.github.feelfreelinux.wykopmobilny.ui.widgets.buttons.vote.base.BaseVoteButtonPresenter
import io.github.feelfreelinux.wykopmobilny.utils.printout
import io.github.feelfreelinux.wykopmobilny.utils.rx.SubscriptionHelperApi

class EntryCommentVoteButtonPresenter(private val subscriptionHandler : SubscriptionHelperApi, private val entriesApi : EntriesApi) : BasePresenter<EntryCommentVoteButtonView>(), BaseVoteButtonPresenter  {
    var entryId = 0
    var commentId = 0

    override fun unvote() {
            subscriptionHandler.subscribe(entriesApi.unvoteComment(entryId, commentId),
                    {
                        view?.voteCount = it.vote
                        view?.isButtonSelected = false
                    }, { view?.showErrorDialog(it) }, this)
    }

    override fun vote() {
        subscriptionHandler.subscribe(entriesApi.voteComment(entryId, commentId),
                {
                    view?.voteCount = it.vote
                    view?.isButtonSelected = true
                }, { view?.showErrorDialog(it) }, this)
    }

    override fun unsubscribe() {
        super.unsubscribe()
        subscriptionHandler.dispose(this)
    }
}