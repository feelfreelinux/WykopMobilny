package io.github.feelfreelinux.wykopmobilny.api.links

import io.github.feelfreelinux.wykopmobilny.models.dataclass.Link
import io.reactivex.Single

interface LinksApi {
    fun getPromoted(page : Int) : Single<List<Link>>
}