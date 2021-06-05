package io.github.feelfreelinux.wykopmobilny.api.addlink

import io.github.feelfreelinux.wykopmobilny.api.UserTokenRefresher
import io.github.feelfreelinux.wykopmobilny.api.errorhandler.ErrorHandler
import io.github.feelfreelinux.wykopmobilny.api.errorhandler.ErrorHandlerTransformer
import io.github.feelfreelinux.wykopmobilny.api.filters.OWMContentFilter
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Link
import io.github.feelfreelinux.wykopmobilny.models.mapper.apiv2.LinkMapper
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.AddLinkPreviewImage
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.responses.NewLinkResponse
import io.reactivex.Single
import retrofit2.Retrofit

class AddLinkRepository(
    private val retrofit: Retrofit,
    private val userTokenRefresher: UserTokenRefresher,
    private val owmContentFilter: OWMContentFilter
) : AddLinkApi {

    private val addlinkApi by lazy { retrofit.create(AddLinkRetrofitApi::class.java) }

    override fun getDraft(url: String): Single<NewLinkResponse> =
        addlinkApi.getDraft(url)
            .retryWhen(userTokenRefresher)
            .flatMap(ErrorHandler<NewLinkResponse>())

    override fun getImages(key: String): Single<List<AddLinkPreviewImage>> =
        addlinkApi.getImages(key)
            .retryWhen(userTokenRefresher)
            .compose(ErrorHandlerTransformer())

    override fun publishLink(
        key: String,
        title: String,
        description: String,
        tags: String,
        photo: String,
        url: String,
        plus18: Boolean
    ): Single<Link> =
        addlinkApi.publishLink(key, title, description, tags, if (photo.isNotEmpty()) photo else null, url, if (plus18) 1 else 0)
            .retryWhen(userTokenRefresher)
            .compose(ErrorHandlerTransformer())
            .map { LinkMapper.map(it, owmContentFilter) }
}
