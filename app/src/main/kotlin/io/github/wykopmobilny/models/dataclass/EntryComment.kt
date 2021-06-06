package io.github.wykopmobilny.models.dataclass

import io.github.wykopmobilny.utils.toPrettyDate

class EntryComment(
    val id: Int,
    var entryId: Int,
    val author: Author,
    var body: String,
    val fullDate: String,
    var isVoted: Boolean,
    var embed: Embed?,
    var voteCount: Int,
    val app: String?,
    val violationUrl: String,
    var isNsfw: Boolean = false,
    var isBlocked: Boolean = false
) {
    override fun equals(other: Any?): Boolean {
        return if (other !is EntryComment) false
        else (other.id == id)
    }

    override fun hashCode(): Int {
        return id
    }

    val url: String
        get() = "https://www.wykop.pl/wpis/$entryId/#comment-$id"

    val date: String
        get() = this.fullDate.toPrettyDate()
}
