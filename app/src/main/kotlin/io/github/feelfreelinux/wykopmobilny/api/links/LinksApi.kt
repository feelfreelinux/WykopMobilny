package io.github.feelfreelinux.wykopmobilny.api.links

import io.github.feelfreelinux.wykopmobilny.models.dataclass.Link
import io.github.feelfreelinux.wykopmobilny.models.dataclass.LinkComment
import io.reactivex.Single

interface LinksApi {
    fun getPromoted(page : Int) : Single<List<Link>>
    fun getLinkComments(linkId: Int, sortBy : String) : Single<List<LinkComment>>
}