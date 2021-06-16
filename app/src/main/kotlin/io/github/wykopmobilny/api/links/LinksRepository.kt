package io.github.wykopmobilny.api.links

import io.github.wykopmobilny.api.UserTokenRefresher
import io.github.wykopmobilny.api.WykopImageFile
import io.github.wykopmobilny.api.endpoints.LinksRetrofitApi
import io.github.wykopmobilny.api.errorhandler.ErrorHandlerTransformer
import io.github.wykopmobilny.api.filters.OWMContentFilter
import io.github.wykopmobilny.api.patrons.PatronsApi
import io.github.wykopmobilny.models.dataclass.LinkVoteResponsePublishModel
import io.github.wykopmobilny.models.mapper.apiv2.DownvoterMapper
import io.github.wykopmobilny.models.mapper.apiv2.LinkCommentMapper
import io.github.wykopmobilny.models.mapper.apiv2.LinkMapper
import io.github.wykopmobilny.models.mapper.apiv2.RelatedMapper
import io.github.wykopmobilny.models.mapper.apiv2.UpvoterMapper
import io.reactivex.subjects.PublishSubject
import kotlinx.coroutines.rx2.rxSingle
import toRequestBody
import javax.inject.Inject

class LinksRepository @Inject constructor(
    private val linksApi: LinksRetrofitApi,
    private val userTokenRefresher: UserTokenRefresher,
    private val owmContentFilter: OWMContentFilter,
    private val patronsApi: PatronsApi,
) : LinksApi {

    override val voteRemoveSubject = PublishSubject.create<LinkVoteResponsePublishModel>()
    override val digSubject = PublishSubject.create<LinkVoteResponsePublishModel>()
    override val burySubject = PublishSubject.create<LinkVoteResponsePublishModel>()

    override fun getPromoted(page: Int) =
        rxSingle { linksApi.getPromoted(page) }
            .retryWhen(userTokenRefresher)
            .flatMap { patronsApi.ensurePatrons(it) }
            .compose(ErrorHandlerTransformer())
            .map { it.map { response -> LinkMapper.map(response, owmContentFilter) } }

    override fun getUpcoming(page: Int, sortBy: String) =
        rxSingle { linksApi.getUpcoming(page, sortBy) }
            .retryWhen(userTokenRefresher)
            .flatMap { patronsApi.ensurePatrons(it) }
            .compose(ErrorHandlerTransformer())
            .map { it.map { response -> LinkMapper.map(response, owmContentFilter) } }

    override fun getObserved(page: Int) =
        rxSingle { linksApi.getObserved(page) }
            .retryWhen(userTokenRefresher)
            .flatMap { patronsApi.ensurePatrons(it) }
            .compose(ErrorHandlerTransformer())
            .map { it.map { response -> LinkMapper.map(response, owmContentFilter) } }

    override fun getLinkComments(linkId: Int, sortBy: String) =
        rxSingle { linksApi.getLinkComments(linkId, sortBy) }
            .retryWhen(userTokenRefresher)
            .flatMap { patronsApi.ensurePatrons(it) }
            .compose(ErrorHandlerTransformer())
            .map { it.map { response -> LinkCommentMapper.map(response, owmContentFilter) } }
            .map { list ->
                list.forEach { comment ->
                    if (comment.id == comment.parentId) {
                        comment.childCommentCount = list.filter { item -> comment.id == item.parentId }.size - 1
                    }
                }
                list
            }

    override fun getLink(linkId: Int) =
        rxSingle { linksApi.getLink(linkId) }
            .retryWhen(userTokenRefresher)
            .flatMap { patronsApi.ensurePatrons(it) }
            .compose(ErrorHandlerTransformer())
            .map { LinkMapper.map(it, owmContentFilter) }

    override fun commentVoteUp(linkId: Int) =
        rxSingle { linksApi.commentVoteUp(linkId) }
            .flatMap { patronsApi.ensurePatrons(it) }
            .retryWhen(userTokenRefresher)
            .compose(ErrorHandlerTransformer())

    override fun commentVoteDown(linkId: Int) =
        rxSingle { linksApi.commentVoteDown(linkId) }
            .flatMap { patronsApi.ensurePatrons(it) }
            .retryWhen(userTokenRefresher)
            .compose(ErrorHandlerTransformer())

    override fun relatedVoteUp(relatedId: Int) =
        rxSingle { linksApi.relatedVoteUp(relatedId) }
            .retryWhen(userTokenRefresher)
            .compose(ErrorHandlerTransformer())

    override fun relatedVoteDown(relatedId: Int) =
        rxSingle { linksApi.relatedVoteDown(relatedId) }
            .flatMap { patronsApi.ensurePatrons(it) }
            .retryWhen(userTokenRefresher)
            .compose(ErrorHandlerTransformer())

    override fun commentVoteCancel(linkId: Int) =
        rxSingle { linksApi.commentVoteCancel(linkId) }
            .flatMap { patronsApi.ensurePatrons(it) }
            .retryWhen(userTokenRefresher)
            .compose(ErrorHandlerTransformer())

    override fun voteUp(linkId: Int, notifyPublisher: Boolean) =
        rxSingle { linksApi.voteUp(linkId) }
            .flatMap { patronsApi.ensurePatrons(it) }
            .retryWhen(userTokenRefresher)
            .compose(ErrorHandlerTransformer())
            .doOnSuccess {
                if (notifyPublisher) {
                    digSubject.onNext(LinkVoteResponsePublishModel(linkId, it))
                }
            }

    override fun voteDown(
        linkId: Int,
        reason: Int,
        notifyPublisher: Boolean
    ) =
        rxSingle { linksApi.voteDown(linkId, reason) }
            .retryWhen(userTokenRefresher)
            .flatMap { patronsApi.ensurePatrons(it) }
            .compose(ErrorHandlerTransformer())
            .doOnSuccess {
                if (notifyPublisher) {
                    burySubject.onNext(LinkVoteResponsePublishModel(linkId, it))
                }
            }

    override fun voteRemove(linkId: Int, notifyPublisher: Boolean) =
        rxSingle { linksApi.voteRemove(linkId) }
            .retryWhen(userTokenRefresher)
            .flatMap { patronsApi.ensurePatrons(it) }
            .compose(ErrorHandlerTransformer())
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
    ) =
        rxSingle { linksApi.addComment(body, linkId, embed, plus18) }
            .retryWhen(userTokenRefresher)
            .compose(ErrorHandlerTransformer())
            .map { LinkCommentMapper.map(it, owmContentFilter) }

    override fun relatedAdd(
        title: String,
        url: String,
        plus18: Boolean,
        linkId: Int
    ) =
        rxSingle { linksApi.addRelated(title, linkId, url, plus18) }
            .retryWhen(userTokenRefresher)
            .compose(ErrorHandlerTransformer())
            .map { RelatedMapper.map(it) }

    override fun commentAdd(
        body: String,
        plus18: Boolean,
        inputStream: WykopImageFile,
        linkId: Int
    ) =
        rxSingle { linksApi.addComment(body.toRequestBody(), plus18.toRequestBody(), linkId, inputStream.getFileMultipart()) }
            .retryWhen(userTokenRefresher)
            .compose(ErrorHandlerTransformer())
            .map { LinkCommentMapper.map(it, owmContentFilter) }

    override fun commentAdd(
        body: String,
        embed: String?,
        plus18: Boolean,
        linkId: Int,
        linkComment: Int
    ) =
        rxSingle { linksApi.addComment(body, linkId, linkComment, embed, plus18) }
            .retryWhen(userTokenRefresher)
            .compose(ErrorHandlerTransformer())
            .map { LinkCommentMapper.map(it, owmContentFilter) }

    override fun commentAdd(
        body: String,
        plus18: Boolean,
        inputStream: WykopImageFile,
        linkId: Int,
        linkComment: Int
    ) =
        rxSingle { linksApi.addComment(body.toRequestBody(), plus18.toRequestBody(), linkId, linkComment, inputStream.getFileMultipart()) }
            .retryWhen(userTokenRefresher)
            .compose(ErrorHandlerTransformer())
            .map { LinkCommentMapper.map(it, owmContentFilter) }

    override fun commentEdit(body: String, linkId: Int) =
        rxSingle { linksApi.editComment(body, linkId) }
            .retryWhen(userTokenRefresher)
            .compose(ErrorHandlerTransformer())
            .map { LinkCommentMapper.map(it, owmContentFilter) }

    override fun commentDelete(commentId: Int) =
        rxSingle { linksApi.deleteComment(commentId) }
            .retryWhen(userTokenRefresher)
            .compose(ErrorHandlerTransformer())
            .map { LinkCommentMapper.map(it, owmContentFilter) }

    override fun getDownvoters(linkId: Int) =
        rxSingle { linksApi.getDownvoters(linkId) }
            .retryWhen(userTokenRefresher)
            .compose(ErrorHandlerTransformer())
            .map { it.map { response -> DownvoterMapper.map(response) } }

    override fun getUpvoters(linkId: Int) =
        rxSingle { linksApi.getUpvoters(linkId) }
            .retryWhen(userTokenRefresher)
            .compose(ErrorHandlerTransformer())
            .map { it.map { response -> UpvoterMapper.map(response) } }

    override fun getRelated(linkId: Int) =
        rxSingle { linksApi.getRelated(linkId) }
            .retryWhen(userTokenRefresher)
            .compose(ErrorHandlerTransformer())
            .map { it.map { response -> RelatedMapper.map(response) } }

    override fun markFavorite(linkId: Int) =
        rxSingle { linksApi.markFavorite(linkId) }
            .retryWhen(userTokenRefresher)
            .compose(ErrorHandlerTransformer())
            .map { it.first() }
}
