package io.github.feelfreelinux.wykopmobilny.api.patrons

import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.patrons.Patron
import io.reactivex.Single

interface PatronsApi {
    fun getPatrons(): Single<List<Patron>>
    fun <T : Any> ensurePatrons(d: T): Single<T>
    var patrons: List<Patron>
}
