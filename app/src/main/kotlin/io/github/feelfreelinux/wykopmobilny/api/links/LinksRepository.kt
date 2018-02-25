package io.github.feelfreelinux.wykopmobilny.api.links

import io.github.feelfreelinux.wykopmobilny.api.UserTokenRefresher
import io.github.feelfreelinux.wykopmobilny.api.entries.TypedInputStream
import io.github.feelfreelinux.wykopmobilny.api.errorhandler.ErrorHandlerTransformer
import io.github.feelfreelinux.wykopmobilny.api.getRequestBody
import io.github.feelfreelinux.wykopmobilny.models.dataclass.*
import io.github.feelfreelinux.wykopmobilny.models.mapper.apiv2.*
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.*
import io.github.feelfreelinux.wykopmobilny.utils.LinksPreferencesApi
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Retrofit
import java.lang.reflect.Type

class LinksRepository(val retrofit: Retrofit, val userTokenRefresher: UserTokenRefresher, val linksPreferencesApi: LinksPreferencesApi) : LinksApi {
    private val linksApi by lazy { retrofit.create(LinksRetrofitApi::class.java) }
    override val voteRemoveSubject = PublishSubject.create<LinkVoteResponsePublishModel>()
    override val digSubject = PublishSubject.create<LinkVoteResponsePublishModel>()
    override val burySubject = PublishSubject.create<LinkVoteResponsePublishModel>()

    override fun getPromoted(page : Int): Single<List<Link>> =
            linksApi.getPromoted(page)
                    .retryWhen(userTokenRefresher)
                    .compose<List<LinkResponse>>(ErrorHandlerTransformer())
                    .map { it.map { LinkMapper.map(it, linksPreferencesApi) } }

    override fun getUpcoming(page : Int, sortBy: String): Single<List<Link>> =
            linksApi.getUpcoming(page, sortBy)
                    .retryWhen(userTokenRefresher)
                    .compose<List<LinkResponse>>(ErrorHandlerTransformer())
                    .map { it.map { LinkMapper.map(it, linksPreferencesApi) } }

    override fun getObserved(page : Int): Single<List<Link>> =
            linksApi.getObserved(page)
                    .retryWhen(userTokenRefresher)
                    .compose<List<LinkResponse>>(ErrorHandlerTransformer())
                    .map { it.map { LinkMapper.map(it, linksPreferencesApi) } }

    override fun getLinkComments(linkId: Int, sortBy: String) =
            linksApi.getLinkComments(linkId, sortBy)
                    .retryWhen(userTokenRefresher)
                    .compose<List<LinkCommentResponse>>(ErrorHandlerTransformer())
                    .map { it.map { LinkCommentMapper.map(it) } }
                    .map {
                        val list = it
                        it.forEach {
                            val commentId = it.id
                            if (commentId == it.parentId) it.childCommentCount = list.filter { commentId == it.parentId }.size - 1
                        }
                        it
                    }


    override fun getLink(linkId: Int): Single<Link> =
            linksApi.getLink(linkId)
                    .retryWhen(userTokenRefresher)
                    .compose<LinkResponse>(ErrorHandlerTransformer())
                    .map { LinkMapper.map(it, linksPreferencesApi) }

    override fun commentVoteUp(linkId: Int): Single<LinkVoteResponse> =
            linksApi.commentVoteUp(linkId)
                    .retryWhen(userTokenRefresher)
                    .compose<LinkVoteResponse>(ErrorHandlerTransformer())


    override fun commentVoteDown(linkId: Int): Single<LinkVoteResponse> =
            linksApi.commentVoteDown(linkId)
                    .retryWhen(userTokenRefresher)
                    .compose<LinkVoteResponse>(ErrorHandlerTransformer())

    override fun relatedVoteUp(relatedId: Int): Single<VoteResponse> =
            linksApi.relatedVoteUp(relatedId)
                    .retryWhen(userTokenRefresher)
                    .compose<VoteResponse>(ErrorHandlerTransformer())


    override fun relatedVoteDown(relatedId: Int): Single<VoteResponse> =
            linksApi.relatedVoteDown(relatedId)
                    .retryWhen(userTokenRefresher)
                    .compose<VoteResponse>(ErrorHandlerTransformer())

    override fun commentVoteCancel(linkId: Int): Single<LinkVoteResponse> =
            linksApi.commentVoteCancel(linkId)
                    .retryWhen(userTokenRefresher)
                    .compose<LinkVoteResponse>(ErrorHandlerTransformer())

    override fun voteUp(linkId: Int, notifyPublisher : Boolean): Single<DigResponse> =
            linksApi.voteUp(linkId)
                    .retryWhen(userTokenRefresher)
                    .compose<DigResponse>(ErrorHandlerTransformer())
                    .doOnSuccess {
                        if (notifyPublisher) {
                            digSubject.onNext(LinkVoteResponsePublishModel(linkId, it))
                        }
                    }

    override fun voteDown(linkId: Int, reason : Int, notifyPublisher: Boolean): Single<DigResponse> =
            linksApi.voteDown(linkId, reason)
                    .retryWhen(userTokenRefresher)
                    .compose<DigResponse>(ErrorHandlerTransformer())
                    .doOnSuccess {
                        if (notifyPublisher) {
                            burySubject.onNext(LinkVoteResponsePublishModel(linkId, it))
                        }
                    }

    override fun voteRemove(linkId: Int, notifyPublisher: Boolean) : Single<DigResponse> =
            linksApi.voteRemove(linkId)
                    .retryWhen(userTokenRefresher)
                    .compose<DigResponse>(ErrorHandlerTransformer())
                    .doOnSuccess {
                        if (notifyPublisher) {
                            voteRemoveSubject.onNext(LinkVoteResponsePublishModel(linkId, it))
                        }
                    }

    override fun commentAdd(body: String, embed: String?, plus18 : Boolean, linkId: Int): Single<LinkComment> =
            linksApi.addComment(body, linkId, embed, plus18)
                    .retryWhen(userTokenRefresher)
                    .compose<LinkCommentResponse>(ErrorHandlerTransformer())
                    .map { LinkCommentMapper.map(it) }

    override fun commentAdd(body: String, plus18 : Boolean, inputStream: TypedInputStream, linkId: Int): Single<LinkComment> =
            linksApi.addComment(body.toRequestBody(), plus18.toRequestBody(), linkId, inputStream.getFileMultipart())
                    .retryWhen(userTokenRefresher)
                    .compose<LinkCommentResponse>(ErrorHandlerTransformer())
                    .map { LinkCommentMapper.map(it) }

    override fun commentAdd(body: String, embed: String?, plus18 : Boolean, linkId: Int, linkComment: Int): Single<LinkComment> =
        linksApi.addComment(body, linkId, linkComment, embed, plus18)
                        .retryWhen(userTokenRefresher)
                        .compose<LinkCommentResponse>(ErrorHandlerTransformer())
                        .map { LinkCommentMapper.map(it) }

    override fun commentAdd(body: String, plus18 : Boolean, inputStream: TypedInputStream, linkId: Int, linkComment: Int): Single<LinkComment> =
            linksApi.addComment(body.toRequestBody(), plus18.toRequestBody(), linkId, linkComment, inputStream.getFileMultipart())
                    .retryWhen(userTokenRefresher)
                    .compose<LinkCommentResponse>(ErrorHandlerTransformer())
                    .map { LinkCommentMapper.map(it) }
    override fun commentEdit(body: String, linkId: Int): Single<LinkComment> =
            linksApi.editComment(body, linkId)
                    .retryWhen(userTokenRefresher)
                    .compose<LinkCommentResponse>(ErrorHandlerTransformer())
                    .map { LinkCommentMapper.map(it) }

    override fun commentDelete(commentId: Int): Single<LinkComment> =
            linksApi.deleteComment(commentId)
                    .retryWhen(userTokenRefresher)
                    .compose<LinkCommentResponse>(ErrorHandlerTransformer())
                    .map { LinkCommentMapper.map(it) }

    override fun getDownvoters(linkId: Int): Single<List<Downvoter>> =
            linksApi.getDownvoters(linkId)
                    .retryWhen(userTokenRefresher)
                    .compose<List<DownvoterResponse>>(ErrorHandlerTransformer())
                    .map { it.map { DownvoterMapper.map(it) } }

    override fun getUpvoters(linkId: Int): Single<List<Upvoter>> =
            linksApi.getUpvoters(linkId)
                    .retryWhen(userTokenRefresher)
                    .compose<List<UpvoterResponse>>(ErrorHandlerTransformer())
                    .map { it.map { UpvoterMapper.map(it) } }

    override fun getRelated(linkId: Int): Single<List<Related>> =
            linksApi.getRelated(linkId)
                    .retryWhen(userTokenRefresher)
                    .compose<List<RelatedResponse>>(ErrorHandlerTransformer())
                    .map { it.map { RelatedMapper.map(it) } }

    override fun markFavorite(linkId: Int): Single<Boolean> =
            linksApi.markFavorite(linkId)
                    .retryWhen(userTokenRefresher)
                    .compose<List<Boolean>>(ErrorHandlerTransformer())
                    .map { it.first() }

    private fun TypedInputStream.getFileMultipart() =
            MultipartBody.Part.createFormData("embed", fileName, inputStream.getRequestBody(mimeType))!!
    private fun Boolean.toRequestBody() = RequestBody.create(MultipartBody.FORM, if (this) "1" else "")!!
    private fun String.toRequestBody() = RequestBody.create(MultipartBody.FORM, this)!!

}

