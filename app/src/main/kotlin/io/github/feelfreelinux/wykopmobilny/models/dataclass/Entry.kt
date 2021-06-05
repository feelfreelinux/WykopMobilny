package io.github.feelfreelinux.wykopmobilny.models.dataclass

import io.github.feelfreelinux.wykopmobilny.utils.toPrettyDate

class Entry(
    val id: Int,
    val author: Author,
    var body: String,
    val fullDate: String,
    var isVoted: Boolean,
    var isFavorite: Boolean,
    var survey: Survey?,
    var embed: Embed?,
    var voteCount: Int,
    val commentsCount: Int,
    val comments: MutableList<EntryComment>,
    val app: String?,
    val violationUrl: String,
    var isNsfw: Boolean = false,
    var isBlocked: Boolean = false,
    var collapsed: Boolean = true,
    val isCommentingPossible: Boolean
) {
    override fun equals(other: Any?): Boolean {
        return if (other !is Entry) false
        else (other.hashCode() == hashCode())
    }

    override fun hashCode(): Int {
        return (id + author.group + body.length).hashCode()
    }

    val url: String
        get() = "https://www.wykop.pl/wpis/$id"

    val date: String
        get() = this.fullDate.toPrettyDate()
}
