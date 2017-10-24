package io.github.feelfreelinux.wykopmobilny.utils.wykop_link_handler.linkparser

class TagLinkParser {
    companion object {
        fun getTag(url : String): String {
            val data = url.substringAfter("/tag/")
            return when(getTagType(url)) {
                "znaleziska", "wpisy" -> data.substringAfter("/").substringBefore("/")
                else -> data.substringBefore("/")
            }
        }

        fun getTagType(url : String): String {
            val subUrl = url.substringAfter("/tag/")
            return subUrl.substringBefore("/")
        }
    }

}