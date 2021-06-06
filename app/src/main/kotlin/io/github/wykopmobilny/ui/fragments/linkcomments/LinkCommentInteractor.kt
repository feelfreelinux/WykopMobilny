package io.github.wykopmobilny.ui.fragments.linkcomments

import io.github.wykopmobilny.api.links.LinksApi
import io.github.wykopmobilny.models.dataclass.LinkComment
import io.reactivex.Single
import javax.inject.Inject

class LinkCommentInteractor @Inject constructor(val linksApi: LinksApi) {

    fun commentVoteUp(link: LinkComment): Single<LinkComment> =
        linksApi.commentVoteUp(link.id)
            .map {
                link.voteCount = it.voteCount
                link.voteCountPlus = it.voteCountPlus
                link.voteCountMinus = it.voteCountMinus
                link.userVote = 1
                link
            }

    fun commentVoteDown(link: LinkComment): Single<LinkComment> =
        linksApi.commentVoteDown(link.id)
            .map {
                link.voteCount = it.voteCount
                link.voteCountPlus = it.voteCountPlus
                link.voteCountMinus = it.voteCountMinus
                link.userVote = -1
                link
            }

    fun commentVoteCancel(link: LinkComment): Single<LinkComment> =
        linksApi.commentVoteCancel(link.id)
            .map {
                link.voteCount = it.voteCount
                link.voteCountPlus = it.voteCountPlus
                link.voteCountMinus = it.voteCountMinus
                link.userVote = 0
                link
            }

    fun removeComment(link: LinkComment): Single<LinkComment> =
        linksApi.commentDelete(link.id)
            .map {
                link.embed = null
                link.body = "[Komentarz usunięty]"
                link
            }
}
