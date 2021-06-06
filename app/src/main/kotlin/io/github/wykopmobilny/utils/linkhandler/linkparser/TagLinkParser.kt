package io.github.wykopmobilny.utils.linkhandler.linkparser

object TagLinkParser {

    fun getTag(url: String): String {
        val data = url.substringAfter("/tag/")
        return when (getTagType(url)) {
            "znaleziska", "wpisy" -> data.substringAfter("/").substringBefore("/")
            else -> data.substringBefore("/")
        }
    }

    private fun getTagType(url: String): String {
        val subUrl = url.substringAfter("/tag/")
        return subUrl.substringBefore("/")
    }
}
