package io.github.wykopmobilny.api.addlink

import io.github.wykopmobilny.api.UserTokenRefresher
import io.github.wykopmobilny.api.endpoints.AddLinkRetrofitApi
import io.github.wykopmobilny.api.errorhandler.ErrorHandler
import io.github.wykopmobilny.api.errorhandler.ErrorHandlerTransformer
import io.github.wykopmobilny.api.filters.OWMContentFilter
import io.github.wykopmobilny.api.responses.NewLinkResponse
import io.github.wykopmobilny.models.dataclass.Link
import io.github.wykopmobilny.models.mapper.apiv2.LinkMapper
import io.reactivex.Single
import kotlinx.coroutines.rx2.rxSingle
import javax.inject.Inject

class AddLinkRepository @Inject constructor(
    private val addlinkApi: AddLinkRetrofitApi,
    private val userTokenRefresher: UserTokenRefresher,
    private val owmContentFilter: OWMContentFilter
) : AddLinkApi {

    override fun getDraft(url: String) =
        rxSingle { addlinkApi.getDraft(url) }
            .retryWhen(userTokenRefresher)
            .flatMap(ErrorHandler<NewLinkResponse>())

    override fun getImages(key: String) =
        rxSingle { addlinkApi.getImages(key) }
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
        rxSingle {
            addlinkApi.publishLink(
                key = key,
                title = title,
                description = description,
                tags = tags,
                photo = photo.takeIf { it.isNotEmpty() },
                url = url,
                plus18 = if (plus18) 1 else 0,
            )
        }
            .retryWhen(userTokenRefresher)
            .compose(ErrorHandlerTransformer())
            .map { LinkMapper.map(it, owmContentFilter) }
}
