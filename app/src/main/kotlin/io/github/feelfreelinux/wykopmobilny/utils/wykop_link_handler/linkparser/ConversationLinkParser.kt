package io.github.feelfreelinux.wykopmobilny.utils.wykop_link_handler.linkparser

class ConversationLinkParser {
    companion object {
        fun getConversationUser(url : String) : String {
            if (url.contains("/wiadomosc-prywatna/konwersacja/")) {
                return url.substringAfter("/wiadomosc-prywatna/konwersacja/").substringBefore("/")
            }
            return ""
        }
    }
}