package io.github.feelfreelinux.wykopmobilny.api.links

import io.github.feelfreelinux.wykopmobilny.api.UserTokenRefresher
import io.github.feelfreelinux.wykopmobilny.api.WykopImageFile
import io.github.feelfreelinux.wykopmobilny.api.errorhandler.ErrorHandlerTransformer
import io.github.feelfreelinux.wykopmobilny.api.filters.OWMContentFilter
import io.github.feelfreelinux.wykopmobilny.api.patrons.PatronsApi
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Downvoter
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Link
import io.github.feelfreelinux.wykopmobilny.models.dataclass.LinkComment
import io.github.feelfreelinux.wykopmobilny.models.dataclass.LinkVoteResponsePublishModel
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Related
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Upvoter
import io.github.feelfreelinux.wykopmobilny.models.mapper.apiv2.DownvoterMapper
import io.github.feelfreelinux.wykopmobilny.models.mapper.apiv2.LinkCommentMapper
import io.github.feelfreelinux.wykopmobilny.models.mapper.apiv2.LinkMapper
import io.github.feelfreelinux.wykopmobilny.models.mapper.apiv2.RelatedMapper
import io.github.feelfreelinux.wykopmobilny.models.mapper.apiv2.UpvoterMapper
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.DigResponse
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.DownvoterResponse
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.LinkCommentResponse
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.LinkResponse
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.LinkVoteResponse
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.RelatedResponse
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.UpvoterResponse
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.VoteResponse
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject
import retrofit2.Retrofit
import toRequestBody

class LinksRepository(
        val retrofit: Retrofit,
        val userTokenRefresher: UserTokenRefresher,
        val owmContentFilter: OWMContentFilter,
        val patronsApi: PatronsApi
) : LinksApi {

    private val linksApi by lazy { retrofit.create(LinksRetrofitApi::class.java) }

    override val voteRemoveSubject = PublishSubject.create<LinkVoteResponsePublishModel>()
    override val digSubject = PublishSubject.create<LinkVoteResponsePublishModel>()
    override val burySubject = PublishSubject.create<LinkVoteResponsePublishModel>()

    override fun getPromoted(page: Int): Single<List<Link>> =
            linksApi.getPromoted(page)
                    .retryWhen(userTokenRefresher)
                    .flatMap { patronsApi.ensurePatrons(it) }
                    .compose<List<LinkResponse>>(ErrorHandlerTransformer())
                    .map { it.map { response -> LinkMapper.map(response, owmContentFilter) } }

    override fun getUpcoming(page: Int, sortBy: String): Single<List<Link>> =
            linksApi.getUpcoming(page, sortBy)
                    .retryWhen(userTokenRefresher)
                    .flatMap { patronsApi.ensurePatrons(it) }
                    .compose<List<LinkResponse>>(ErrorHandlerTransformer())
                    .map { it.map { response -> LinkMapper.map(response, owmContentFilter) } }

    override fun getObserved(page: Int): Single<List<Link>> =
            linksApi.getObserved(page)
                    .retryWhen(userTokenRefresher)
                    .flatMap { patronsApi.ensurePatrons(it) }
                    .compose<List<LinkResponse>>(ErrorHandlerTransformer())
                    .map { it.map { response -> LinkMapper.map(response, owmContentFilter) } }

    override fun getLinkComments(linkId: Int, sortBy: String) =
            linksApi.getLinkComments(linkId, sortBy)
                    .retryWhen(userTokenRefresher)
                    .flatMap { patronsApi.ensurePatrons(it) }
                    .compose<List<LinkCommentResponse>>(ErrorHandlerTransformer())
                    .map { it.map { response -> LinkCommentMapper.map(response, owmContentFilter) } }
                    .map { list ->
                        list.forEach { comment ->
                            if (comment.id == comment.parentId) {
                                comment.childCommentCount = list.filter { item -> comment.id == item.parentId }.size - 1
                            }
                        }
                        list
                    }

    override fun getLink(linkId: Int): Single<Link> =
            linksApi.getLink(linkId)
                    .retryWhen(userTokenRefresher)
                    .flatMap { patronsApi.ensurePatrons(it) }
                    .compose<LinkResponse>(ErrorHandlerTransformer())
                    .map { LinkMapper.map(it, owmContentFilter) }

    override fun commentVoteUp(linkId: Int): Single<LinkVoteResponse> =
            linksApi.commentVoteUp(linkId)
                    .flatMap { patronsApi.ensurePatrons(it) }
                    .retryWhen(userTokenRefresher)
                    .compose<LinkVoteResponse>(ErrorHandlerTransformer())


    override fun commentVoteDown(linkId: Int): Single<LinkVoteResponse> =
            linksApi.commentVoteDown(linkId)
                    .flatMap { patronsApi.ensurePatrons(it) }
                    .retryWhen(userTokenRefresher)
                    .compose<LinkVoteResponse>(ErrorHandlerTransformer())

    override fun relatedVoteUp(relatedId: Int): Single<VoteResponse> =
            linksApi.relatedVoteUp(relatedId)
                    .retryWhen(userTokenRefresher)
                    .compose<VoteResponse>(ErrorHandlerTransformer())


    override fun relatedVoteDown(relatedId: Int): Single<VoteResponse> =
            linksApi.relatedVoteDown(relatedId)
                    .flatMap { patronsApi.ensurePatrons(it) }
                    .retryWhen(userTokenRefresher)
                    .compose<VoteResponse>(ErrorHandlerTransformer())

    override fun commentVoteCancel(linkId: Int): Single<LinkVoteResponse> =
            linksApi.commentVoteCancel(linkId)
                    .flatMap { patronsApi.ensurePatrons(it) }
                    .retryWhen(userTokenRefresher)
                    .compose<LinkVoteResponse>(ErrorHandlerTransformer())

    override fun voteUp(linkId: Int, notifyPublisher: Boolean): Single<DigResponse> =
            linksApi.voteUp(linkId)
                    .flatMap { patronsApi.ensurePatrons(it) }
                    .retryWhen(userTokenRefresher)
                    .compose<DigResponse>(ErrorHandlerTransformer())
                    .doOnSuccess {
                        if (notifyPublisher) {
                            digSubject.onNext(LinkVoteResponsePublishModel(linkId, it))
                        }
                    }

    override fun voteDown(
            linkId: Int,
            reason: Int,
            notifyPublisher: Boolean
    ): Single<DigResponse> =
            linksApi.voteDown(linkId, reason)
                    .retryWhen(userTokenRefresher)
                    .flatMap { patronsApi.ensurePatrons(it) }
                    .compose<DigResponse>(ErrorHandlerTransformer())
                    .doOnSuccess {
                        if (notifyPublisher) {
                            burySubject.onNext(LinkVoteResponsePublishModel(linkId, it))
                        }
                    }

    override fun voteRemove(linkId: Int, notifyPublisher: Boolean): Single<DigResponse> =
            linksApi.voteRemove(linkId)
                    .retryWhen(userTokenRefresher)
                    .flatMap { patronsApi.ensurePatrons(it) }
                    .compose<DigResponse>(ErrorHandlerTransformer())
                    .doOnSuccess {
                        if (notifyPublisher) {
                            voteRemoveSubject.onNext(LinkVoteResponsePublishModel(linkId, it))
                        }
                    }

    override fun commentAdd(
            body: String,
            embed: String?,
            plus18: Boolean,
            linkId: Int
    ): Single<LinkComment> =
            linksApi.addComment(body, linkId, embed, plus18)
                    .retryWhen(userTokenRefresher)
                    .compose<LinkCommentResponse>(ErrorHandlerTransformer())
                    .map { LinkCommentMapper.map(it, owmContentFilter) }

    override fun relatedAdd(
            title: String,
            url: String,
            plus18: Boolean,
            linkId: Int
    ): Single<Related> =
            linksApi.addRelated(title,  linkId, url, plus18)
                    .retryWhen(userTokenRefresher)
                    .compose<RelatedResponse>(ErrorHandlerTransformer())
                    .map { RelatedMapper.map(it) }

    override fun commentAdd(
            body: String,
            plus18: Boolean,
            inputStream: WykopImageFile,
            linkId: Int
    ): Single<LinkComment> =
            linksApi.addComment(body.toRequestBody(), plus18.toRequestBody(), linkId, inputStream.getFileMultipart())
                    .retryWhen(userTokenRefresher)
                    .compose<LinkCommentResponse>(ErrorHandlerTransformer())
                    .map { LinkCommentMapper.map(it, owmContentFilter) }

    override fun commentAdd(
            body: String,
            embed: String?,
            plus18: Boolean,
            linkId: Int,
            linkComment: Int
    ): Single<LinkComment> =
            linksApi.addComment(body, linkId, linkComment, embed, plus18)
                    .retryWhen(userTokenRefresher)
                    .compose<LinkCommentResponse>(ErrorHandlerTransformer())
                    .map { LinkCommentMapper.map(it, owmContentFilter) }

    override fun commentAdd(
            body: String,
            plus18: Boolean,
            inputStream: WykopImageFile,
            linkId: Int,
            linkComment: Int
    ): Single<LinkComment> =
            linksApi.addComment(body.toRequestBody(), plus18.toRequestBody(), linkId, linkComment, inputStream.getFileMultipart())
                    .retryWhen(userTokenRefresher)
                    .compose<LinkCommentResponse>(ErrorHandlerTransformer())
                    .map { LinkCommentMapper.map(it, owmContentFilter) }

    override fun commentEdit(body: String, linkId: Int): Single<LinkComment> {
        return linksApi.editComment(body, linkId)
                .retryWhen(userTokenRefresher)
                .compose<LinkCommentResponse>(ErrorHandlerTransformer())
                .map { LinkCommentMapper.map(it, owmContentFilter) }
    }

    override fun commentDelete(commentId: Int): Single<LinkComment> =
            linksApi.deleteComment(commentId)
                    .retryWhen(userTokenRefresher)
                    .compose<LinkCommentResponse>(ErrorHandlerTransformer())
                    .map { LinkCommentMapper.map(it, owmContentFilter) }

    override fun getDownvoters(linkId: Int): Single<List<Downvoter>> =
            linksApi.getDownvoters(linkId)
                    .retryWhen(userTokenRefresher)
                    .compose<List<DownvoterResponse>>(ErrorHandlerTransformer())
                    .map { it.map { response -> DownvoterMapper.map(response) } }

    override fun getUpvoters(linkId: Int): Single<List<Upvoter>> =
            linksApi.getUpvoters(linkId)
                    .retryWhen(userTokenRefresher)
                    .compose<List<UpvoterResponse>>(ErrorHandlerTransformer())
                    .map { it.map { response -> UpvoterMapper.map(response) } }

    override fun getRelated(linkId: Int): Single<List<Related>> =
            linksApi.getRelated(linkId)
                    .retryWhen(userTokenRefresher)
                    .compose<List<RelatedResponse>>(ErrorHandlerTransformer())
                    .map { it.map { response -> RelatedMapper.map(response) } }

    override fun markFavorite(linkId: Int): Single<Boolean> =
            linksApi.markFavorite(linkId)
                    .retryWhen(userTokenRefresher)
                    .compose<List<Boolean>>(ErrorHandlerTransformer())
                    .map { it.first() }
}

