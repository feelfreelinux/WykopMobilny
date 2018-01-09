package io.github.feelfreelinux.wykopmobilny.api.links

import io.github.feelfreelinux.wykopmobilny.api.UserTokenRefresher
import io.github.feelfreelinux.wykopmobilny.api.errorhandler.ErrorHandlerTransformer
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Link
import io.github.feelfreelinux.wykopmobilny.models.mapper.apiv2.LinkCommentMapper
import io.github.feelfreelinux.wykopmobilny.models.mapper.apiv2.LinkMapper
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.LinkCommentResponse
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.LinkResponse
import io.reactivex.Single
import retrofit2.Retrofit

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

}