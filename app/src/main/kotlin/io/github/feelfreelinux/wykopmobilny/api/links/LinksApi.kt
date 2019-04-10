package io.github.feelfreelinux.wykopmobilny.api.links

import io.github.feelfreelinux.wykopmobilny.api.WykopImageFile
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Downvoter
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Link
import io.github.feelfreelinux.wykopmobilny.models.dataclass.LinkComment
import io.github.feelfreelinux.wykopmobilny.models.dataclass.LinkVoteResponsePublishModel
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Related
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Upvoter
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.DigResponse
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.LinkVoteResponse
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.VoteResponse
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject

interface LinksApi {

    val burySubject: PublishSubject<LinkVoteResponsePublishModel>
    val digSubject: PublishSubject<LinkVoteResponsePublishModel>
    val voteRemoveSubject: PublishSubject<LinkVoteResponsePublishModel>

    fun getPromoted(page: Int): Single<List<Link>>
    fun getUpcoming(page: Int, sortBy: String): Single<List<Link>>

    fun getObserved(page: Int): Single<List<Link>>
    fun getLinkComments(linkId: Int, sortBy: String): Single<List<LinkComment>>
    fun getLink(linkId: Int): Single<Link>

    fun commentVoteUp(linkId: Int): Single<LinkVoteResponse>
    fun commentVoteDown(linkId: Int): Single<LinkVoteResponse>
    fun relatedVoteUp(relatedId: Int): Single<VoteResponse>
    fun relatedVoteDown(relatedId: Int): Single<VoteResponse>
    fun commentVoteCancel(linkId: Int): Single<LinkVoteResponse>
    fun commentDelete(commentId: Int): Single<LinkComment>
    fun commentAdd(
        body: String,
        plus18: Boolean,
        inputStream: WykopImageFile,
        linkId: Int, linkComment: Int
    ): Single<LinkComment>
    fun relatedAdd(
            title: String,
            url: String,
            plus18: Boolean,
            linkId: Int
    ): Single<Related>

    fun commentAdd(
        body: String, embed: String?,
        plus18: Boolean,
        linkId: Int, linkComment: Int
    ): Single<LinkComment>

    fun commentAdd(
        body: String,
        plus18: Boolean,
        inputStream: WykopImageFile,
        linkId: Int
    ): Single<LinkComment>

    fun commentAdd(
        body: String, embed: String?,
        plus18: Boolean,
        linkId: Int
    ): Single<LinkComment>

    fun commentEdit(body: String, linkId: Int): Single<LinkComment>
    fun voteUp(linkId: Int, notifyPublisher: Boolean = true): Single<DigResponse>
    fun voteDown(linkId: Int, reason: Int, notifyPublisher: Boolean = true): Single<DigResponse>
    fun voteRemove(linkId: Int, notifyPublisher: Boolean = true): Single<DigResponse>
    fun getUpvoters(linkId: Int): Single<List<Upvoter>>
    fun getDownvoters(linkId: Int): Single<List<Downvoter>>
    fun markFavorite(linkId: Int): Single<Boolean>
    fun getRelated(linkId: Int): Single<List<Related>>
}