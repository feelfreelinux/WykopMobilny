package io.github.wykopmobilny.ui.fragments.entrycomments

import io.github.wykopmobilny.models.dataclass.Author
import io.github.wykopmobilny.models.dataclass.EntryComment

interface EntryCommentViewListener {
    fun addReply(author: Author)
    fun quoteComment(comment: EntryComment)
}
