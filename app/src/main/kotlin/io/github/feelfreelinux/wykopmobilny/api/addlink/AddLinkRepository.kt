package io.github.feelfreelinux.wykopmobilny.api.addlink

import io.github.feelfreelinux.wykopmobilny.api.UserTokenRefresher
import io.github.feelfreelinux.wykopmobilny.api.errorhandler.ErrorHandler
import io.github.feelfreelinux.wykopmobilny.api.errorhandler.ErrorHandlerTransformer
import io.github.feelfreelinux.wykopmobilny.api.filters.OWMContentFilter
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Link
import io.github.feelfreelinux.wykopmobilny.models.mapper.apiv2.LinkMapper
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.AddLinkPreviewImage
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.LinkResponse
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.VoteResponse
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.responses.NewLinkResponse
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.responses.TagLinksResponse
import io.github.feelfreelinux.wykopmobilny.utils.preferences.BlacklistPreferencesApi
import io.github.feelfreelinux.wykopmobilny.utils.preferences.LinksPreferencesApi
import io.github.feelfreelinux.wykopmobilny.utils.preferences.SettingsPreferencesApi
import io.reactivex.Single
import retrofit2.Retrofit

class AddLinkRepository(val retrofit: Retrofit, val userTokenRefresher: UserTokenRefresher, val owmContentFilter: OWMContentFilter) : AddlinkApi {
    private val addlinkApi by lazy { retrofit.create(AddlinkRetrofitApi::class.java) }

    override fun getDraft(url: String): Single<NewLinkResponse> =
            addlinkApi.getDraft(url)
                    .retryWhen(userTokenRefresher)
                    .flatMap(ErrorHandler<NewLinkResponse>())

    override fun getImages(key: String): Single<List<AddLinkPreviewImage>> =
            addlinkApi.getImages(key)
                    .retryWhen(userTokenRefresher)
                    .compose<List<AddLinkPreviewImage>>(ErrorHandlerTransformer())

    override fun publishLink(key: String, title: String, description: String, tags: String, photo: String, url: String, plus18: Boolean): Single<Link> =
            addlinkApi.publishLink(key, title, description, tags, if (photo.isNotEmpty()) photo else null, url, if (plus18) 1 else 0)
                    .retryWhen(userTokenRefresher)
                    .compose<LinkResponse>(ErrorHandlerTransformer())
                    .map { LinkMapper.map(it, owmContentFilter) }
}
