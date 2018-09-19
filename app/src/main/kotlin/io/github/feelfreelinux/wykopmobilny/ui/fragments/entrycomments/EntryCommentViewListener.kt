package io.github.feelfreelinux.wykopmobilny.ui.fragments.entrycomments

import io.github.feelfreelinux.wykopmobilny.models.dataclass.Author
import io.github.feelfreelinux.wykopmobilny.models.dataclass.EntryComment

interface EntryCommentViewListener {
    fun addReply(author: Author)
    fun quoteComment(comment: EntryComment)
}