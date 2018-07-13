package io.github.feelfreelinux.wykopmobilny.utils.wykop_link_handler.linkparser

class ProfileLinkParser {
    companion object {
        fun getProfile(url : String): String {
            val data = url.substringAfter("/ludzie/")
            val possibleNickMatch = data.substringBefore("/")
            val possibleMatchers = arrayOf(
                    "wpisy", "komentarze", "plusowane-wpisy",
                    "dodane", "opublikowane", "komentarze", "powiązane", "wykopane",
                    "obserwujący", "obserwowani", "osiagniecia")
            return if (possibleMatchers.contains(possibleNickMatch)) {
                data.substringAfter(possibleNickMatch + "/").substringBefore("/")
            } else possibleNickMatch
        }
    }

}