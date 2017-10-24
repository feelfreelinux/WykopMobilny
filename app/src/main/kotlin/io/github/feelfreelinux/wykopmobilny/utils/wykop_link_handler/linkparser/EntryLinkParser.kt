package io.github.feelfreelinux.wykopmobilny.utils.wykop_link_handler.linkparser

class EntryLinkParser {
    companion object {
        fun getEntryId(url : String) : Int? {
            if (url.contains("/wpis/")) {
                return url.substringAfter("/wpis/").substringBefore("/").toInt()
            }
            return null
        }

        fun getEntryCommentId(url : String) : Int? {
            if (url.contains("/#comment-")) {
                return url.substringAfter("/#comment-").substringBefore("/").toInt()
            }
            return null
        }
    }
}