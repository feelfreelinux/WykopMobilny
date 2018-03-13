package io.github.feelfreelinux.wykopmobilny.utils.wykop_link_handler.linkparser

class EntryLinkParser {
    companion object {
        fun getEntryId(url : String) : Int? {
            if (url.contains("/wpis/")) {
                return url.substringAfter("/wpis/").substringBefore("/").toIntOrNull()
            }
            return null
        }

        fun getEntryCommentId(url : String) : Int? {
            if (url.contains("/#comment-")) {
                return url.substringAfter("/#comment-").substringBefore("/").toIntOrNull()
            }
            return null
        }
    }
}