package io.github.feelfreelinux.wykopmobilny.ui.fragments.entrycomments

import io.github.feelfreelinux.wykopmobilny.models.dataclass.EntryComment


interface EntryCommentActionListener {
    fun voteComment(comment: EntryComment)
    fun unvoteComment(comment: EntryComment)
    fun deleteComment(comment: EntryComment)
    fun getVoters(comment: EntryComment)
}