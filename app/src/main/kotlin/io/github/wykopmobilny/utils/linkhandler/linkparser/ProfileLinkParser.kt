package io.github.wykopmobilny.utils.linkhandler.linkparser

object ProfileLinkParser {

    fun getProfile(url: String): String {
        val data = url.substringAfter("/ludzie/")
        val possibleNickMatch = data.substringBefore("/")
        val possibleMatchers = arrayOf(
            "wpisy", "komentarze", "plusowane-wpisy",
            "dodane", "opublikowane", "komentarze", "powiązane", "wykopane",
            "obserwujący", "obserwowani", "osiagniecia"
        )
        return if (possibleMatchers.contains(possibleNickMatch)) {
            data.substringAfter(possibleNickMatch + "/").substringBefore("/")
        } else possibleNickMatch
    }
}
