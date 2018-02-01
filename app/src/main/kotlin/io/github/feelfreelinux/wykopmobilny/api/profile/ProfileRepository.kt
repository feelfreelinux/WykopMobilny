package io.github.feelfreelinux.wykopmobilny.api.profile

import io.github.feelfreelinux.wykopmobilny.api.UserTokenRefresher
import io.github.feelfreelinux.wykopmobilny.api.errorhandler.ErrorHandlerTransformer
import io.github.feelfreelinux.wykopmobilny.api.notifications.NotificationsRetrofitApi
import io.github.feelfreelinux.wykopmobilny.models.dataclass.EntryLink
import io.github.feelfreelinux.wykopmobilny.models.mapper.apiv2.EntryLinkMapper
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.EntryLinkResponse
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.ProfileResponse
import io.github.feelfreelinux.wykopmobilny.utils.LinksPreferencesApi
import io.reactivex.Single
import retrofit2.Retrofit

class ProfileRepository(val retrofit: Retrofit, val userTokenRefresher: UserTokenRefresher, val linksPreferencesApi : LinksPreferencesApi) : ProfileApi {
    private val profileApi by lazy { retrofit.create(ProfileRetrofitApi::class.java) }

    override fun getIndex(username : String): Single<ProfileResponse> =
            profileApi.getIndex(username)
                    .retryWhen(userTokenRefresher)
                    .compose<ProfileResponse>(ErrorHandlerTransformer())
}