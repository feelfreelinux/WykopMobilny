package io.github.wykopmobilny.utils.linkhandler.linkparser

object LinkParser {

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
