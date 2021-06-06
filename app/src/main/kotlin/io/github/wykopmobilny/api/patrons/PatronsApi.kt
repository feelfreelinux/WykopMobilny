package io.github.wykopmobilny.api.patrons

import io.github.wykopmobilny.models.dataclass.AndroidPatronBadge
import io.github.wykopmobilny.models.dataclass.Author
import io.github.wykopmobilny.patrons.api.Patron
import io.reactivex.Single

interface PatronsApi {
    fun getPatrons(): Single<List<Patron>>
    fun <T : Any> ensurePatrons(d: T): Single<T>
    var patrons: List<Patron>
}

fun PatronsApi.getBadgeFor(nick: String) =
    try {
        patrons.firstOrNull { it.username == nick }?.badge?.let { badge ->
            AndroidPatronBadge(
                badge.hexColor,
                badge.text,
            )
        }
    } catch (e: Throwable) {
        null
    }

fun PatronsApi.getBadgeFor(author: Author) = getBadgeFor(author.nick)
