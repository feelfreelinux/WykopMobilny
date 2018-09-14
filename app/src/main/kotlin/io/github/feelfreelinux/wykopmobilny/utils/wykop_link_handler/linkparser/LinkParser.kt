package io.github.feelfreelinux.wykopmobilny.utils.wykop_link_handler.linkparser

class LinkParser {
    companion object {
        fun getLinkId(url: String): Int? {
            if (url.contains("/link/")) {
                return url.substringAfter("/link/").substringBefore("/").toIntOrNull()
            }
            return null
        }

        fun getLinkCommentId(url: String): Int? {
            if (url.contains("/#comment-")) {
                return url.substringAfter("/#comment-").substringBefore("/").toIntOrNull()
            }
            return null
        }
    }
}