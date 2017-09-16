package io.github.feelfreelinux.wykopmobilny.ui.elements.vote_button.entry.comment

import io.github.feelfreelinux.wykopmobilny.api.entries.EntriesApi
import io.github.feelfreelinux.wykopmobilny.base.BasePresenter
import io.github.feelfreelinux.wykopmobilny.ui.elements.vote_button.base.BaseVoteButtonPresenter
import io.github.feelfreelinux.wykopmobilny.api.enqueue

class EntryCommentVoteButtonPresenter(private val entriesApi : EntriesApi) : BasePresenter<EntryCommentVoteButtonView>(), BaseVoteButtonPresenter  {
    var entryId = 0
    var commentId = 0

    override fun unvote() {
        entriesApi.unvoteComment(entryId, commentId).enqueue(
                {
                    val voteCount = it.body()!!.vote
                    view?.voteCount = voteCount
                    view?.isButtonSelected = false
                },
                { view?.showErrorDialog(it) }
        )
    }

    override fun vote() {
        entriesApi.voteComment(entryId, commentId).enqueue(
                {
                    val voteCount = it.body()!!.vote
                    view?.voteCount = voteCount
                    view?.isButtonSelected = true
                },
                { view?.showErrorDialog(it) }
        )
    }
}