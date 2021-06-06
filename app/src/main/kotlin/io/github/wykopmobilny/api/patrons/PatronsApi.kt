package io.github.wykopmobilny.api.patrons

import io.github.wykopmobilny.models.dataclass.AndroidPatronBadge
import io.github.wykopmobilny.models.dataclass.Author
import io.github.wykopmobilny.patrons.api.Patron
import io.reactivex.Single

interface PatronsApi {
    fun <T : Any> ensurePatrons(continuation: T): Single<T>
    val patrons: List<Patron>
}

fun PatronsApi.getBadgeFor(nick: String) =
    patrons.firstOrNull { it.username == nick }?.badge?.let { badge ->
        AndroidPatronBadge(
            badge.hexColor,
            badge.text,
        )
    }

fun PatronsApi.getBadgeFor(author: Author) = getBadgeFor(author.nick)
