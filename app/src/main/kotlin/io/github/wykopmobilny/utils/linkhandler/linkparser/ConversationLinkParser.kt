package io.github.wykopmobilny.utils.linkhandler.linkparser

class ConversationLinkParser {
    companion object {
        fun getConversationUser(url: String): String {
            if (url.contains("/wiadomosc-prywatna/konwersacja/")) {
                return url.substringAfter("/wiadomosc-prywatna/konwersacja/").substringBefore("/")
            }
            return ""
        }
    }
}
