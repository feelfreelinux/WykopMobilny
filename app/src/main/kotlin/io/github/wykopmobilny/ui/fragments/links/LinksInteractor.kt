package io.github.wykopmobilny.ui.fragments.links

import io.github.wykopmobilny.api.links.LinksApi
import io.github.wykopmobilny.models.dataclass.Link
import io.reactivex.Single
import javax.inject.Inject

class LinksInteractor @Inject constructor(val linksApi: LinksApi) {

    fun dig(link: Link): Single<Link> =
        linksApi.voteUp(link.id, true)
            .map {
                link.voteCount = it.diggs
                link.userVote = "dig"
                link
            }

    fun voteRemove(link: Link): Single<Link> =
        linksApi.voteRemove(link.id, true)
            .map {
                link.voteCount = it.diggs
                link.userVote = ""
                link
            }
}
