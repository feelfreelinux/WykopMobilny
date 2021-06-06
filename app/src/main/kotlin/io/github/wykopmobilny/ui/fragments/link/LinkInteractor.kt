package io.github.wykopmobilny.ui.fragments.link

import io.github.wykopmobilny.api.links.LinksApi
import io.github.wykopmobilny.models.dataclass.Link
import io.reactivex.Single
import javax.inject.Inject

class LinkInteractor @Inject constructor(val linksApi: LinksApi) {

    companion object {
        const val BURY_REASON_DUPLICATE = 1
        const val BURY_REASON_SPAM = 2
        const val BURY_REASON_FAKE_INFO = 3
        const val BURY_REASON_WRONG_CONTENT = 4
        const val BURY_REASON_UNSUITABLE_CONTENT = 5
    }

    fun digLink(link: Link): Single<Link> =
        linksApi.voteUp(link.id)
            .map {
                link.voteCount = it.diggs
                link.buryCount = it.buries
                link.userVote = "dig"
                link
            }

    fun buryLink(link: Link, reason: Int): Single<Link> =
        linksApi.voteDown(link.id, reason)
            .map {
                link.voteCount = it.diggs
                link.buryCount = it.buries
                link.userVote = "bury"
                link
            }

    fun removeVote(link: Link): Single<Link> =
        linksApi.voteRemove(link.id)
            .map {
                link.voteCount = it.diggs
                link.buryCount = it.buries
                link.userVote = null
                link
            }

    fun markFavorite(link: Link): Single<Link> =
        linksApi.markFavorite(link.id)
            .map {
                link.userFavorite = !link.userFavorite
                link
            }
}
