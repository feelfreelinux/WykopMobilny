package io.github.feelfreelinux.wykopmobilny.ui.fragments.link

import io.github.feelfreelinux.wykopmobilny.api.links.LinksApi
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Link
import io.reactivex.Single
import javax.inject.Inject


class LinkInteractor @Inject constructor(val linksApi: LinksApi) {
    companion object {
        val BURY_REASON_DUPLICATE = 1
        val BURY_REASON_SPAM = 2
        val BURY_REASON_FAKE_INFO = 3
        val BURY_REASON_WRONG_CONTENT = 4
        val BURY_REASON_UNSUITABLE_CONTENT = 5
    }

    fun digLink(link : Link): Single<Link> {
        return linksApi.voteUp(link.id)
                .map {
                    link.voteCount = it.diggs
                    link.buryCount = it.buries
                    link.userVote = "dig"
                    link
                }
    }

    fun buryLink(link : Link, reason : Int): Single<Link> {
        return linksApi.voteDown(link.id, reason)
                .map {
                    link.voteCount = it.diggs
                    link.buryCount = it.buries
                    link.userVote = "bury"
                    link
                }
    }
    fun removeVote(link : Link): Single<Link> {
        return linksApi.voteRemove(link.id)
                .map {
                    link.voteCount = it.diggs
                    link.buryCount = it.buries
                    link.userVote = null
                    link
                }
    }
    fun markFavorite(link : Link): Single<Link> {
        return linksApi.markFavorite(link.id)
                .map {
                    link.userFavorite = it
                    link
                }
    }
}