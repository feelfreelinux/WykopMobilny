package io.github.feelfreelinux.wykopmobilny.api.links

import io.github.feelfreelinux.wykopmobilny.api.UserTokenRefresher
import io.github.feelfreelinux.wykopmobilny.api.entries.TypedInputStream
import io.github.feelfreelinux.wykopmobilny.api.errorhandler.ErrorHandlerTransformer
import io.github.feelfreelinux.wykopmobilny.api.getRequestBody
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Link
import io.github.feelfreelinux.wykopmobilny.models.dataclass.LinkComment
import io.github.feelfreelinux.wykopmobilny.models.mapper.apiv2.LinkCommentMapper
import io.github.feelfreelinux.wykopmobilny.models.mapper.apiv2.LinkMapper
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.DigResponse
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.LinkCommentResponse
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.LinkResponse
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.LinkVoteResponse
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Retrofit
import java.lang.reflect.Type

class LinksRepository(val retrofit: Retrofit, val userTokenRefresher: UserTokenRefresher) : LinksApi {
    private val linksApi by lazy { retrofit.create(LinksRetrofitApi::class.java) }

    override fun getPromoted(page : Int): Single<List<Link>> =
            linksApi.getPromoted(page)
                    .retryWhen(userTokenRefresher)
                    .compose<List<LinkResponse>>(ErrorHandlerTransformer())
                    .map { it.map { LinkMapper.map(it) } }

    override fun getLinkComments(linkId: Int, sortBy: String) =
            linksApi.getLinkComments(linkId, sortBy)
                    .retryWhen(userTokenRefresher)
                    .compose<List<LinkCommentResponse>>(ErrorHandlerTransformer())
                    .map { it.map { LinkCommentMapper.map(it) } }

    override fun getLink(linkId: Int): Single<Link> =
            linksApi.getLink(linkId)
                    .retryWhen(userTokenRefresher)
                    .compose<LinkResponse>(ErrorHandlerTransformer())
                    .map { LinkMapper.map(it) }

    override fun commentVoteUp(linkId: Int): Single<LinkVoteResponse> =
            linksApi.commentVoteUp(linkId)
                    .retryWhen(userTokenRefresher)
                    .compose<LinkVoteResponse>(ErrorHandlerTransformer())


    override fun commentVoteDown(linkId: Int): Single<LinkVoteResponse> =
            linksApi.commentVoteDown(linkId)
                    .retryWhen(userTokenRefresher)
                    .compose<LinkVoteResponse>(ErrorHandlerTransformer())

    override fun commentVoteCancel(linkId: Int): Single<LinkVoteResponse> =
            linksApi.commentVoteCancel(linkId)
                    .retryWhen(userTokenRefresher)
                    .compose<LinkVoteResponse>(ErrorHandlerTransformer())

    override fun voteUp(linkId: Int): Single<DigResponse> =
            linksApi.voteUp(linkId)
                    .retryWhen(userTokenRefresher)
                    .compose<DigResponse>(ErrorHandlerTransformer())

    override fun voteDown(linkId: Int, reason : Int): Single<DigResponse> =
            linksApi.voteDown(linkId, reason)
                    .retryWhen(userTokenRefresher)
                    .compose<DigResponse>(ErrorHandlerTransformer())

    override fun voteRemove(linkId: Int) : Single<DigResponse> =
            linksApi.voteRemove(linkId)
                    .retryWhen(userTokenRefresher)
                    .compose<DigResponse>(ErrorHandlerTransformer())

    override fun commentAdd(body: String, embed: String?, linkId: Int): Single<LinkComment> =
            linksApi.addComment(body, linkId, embed)
                    .retryWhen(userTokenRefresher)
                    .compose<LinkCommentResponse>(ErrorHandlerTransformer())
                    .map { LinkCommentMapper.map(it) }

    override fun commentAdd(body: String, inputStream: TypedInputStream, linkId: Int): Single<LinkComment> =
            linksApi.addComment(body.toRequestBody(), linkId, inputStream.getFileMultipart())
                    .retryWhen(userTokenRefresher)
                    .compose<LinkCommentResponse>(ErrorHandlerTransformer())
                    .map { LinkCommentMapper.map(it) }

    override fun commentAdd(body: String, embed: String?, linkId: Int, linkComment: Int): Single<LinkComment> =
        linksApi.addComment(body, linkId, linkComment, embed)
                        .retryWhen(userTokenRefresher)
                        .compose<LinkCommentResponse>(ErrorHandlerTransformer())
                        .map { LinkCommentMapper.map(it) }

    override fun commentAdd(body: String, inputStream: TypedInputStream, linkId: Int, linkComment: Int): Single<LinkComment> =
            linksApi.addComment(body.toRequestBody(), linkId, linkComment, inputStream.getFileMultipart())
                    .retryWhen(userTokenRefresher)
                    .compose<LinkCommentResponse>(ErrorHandlerTransformer())
                    .map { LinkCommentMapper.map(it) }

    override fun commentDelete(commentId: Int): Single<LinkComment> =
            linksApi.deleteComment(commentId)
                    .retryWhen(userTokenRefresher)
                    .compose<LinkCommentResponse>(ErrorHandlerTransformer())
                    .map { LinkCommentMapper.map(it) }

    private fun TypedInputStream.getFileMultipart() =
            MultipartBody.Part.createFormData("embed", fileName, inputStream.getRequestBody(mimeType))!!

    private fun String.toRequestBody() = RequestBody.create(MultipartBody.FORM, this)!!

}

