package io.github.wykopmobilny.utils.linkhandler.linkparser

object EntryLinkParser {

    fun getEntryId(url: String): Int? {
        if (url.contains("/wpis/")) {
            return url.substringAfter("/wpis/").substringBefore("/").toIntOrNull()
        }
        return null
    }

    fun getEntryCommentId(url: String): Int? {
        if (url.contains("/#comment-")) {
            return url.substringAfter("/#comment-").substringBefore("/").toIntOrNull()
        }
        return null
    }
}
