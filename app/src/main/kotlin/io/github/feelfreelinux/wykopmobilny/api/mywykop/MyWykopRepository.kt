package io.github.feelfreelinux.wykopmobilny.api.mywykop

import io.github.feelfreelinux.wykopmobilny.api.UserTokenRefresher
import io.github.feelfreelinux.wykopmobilny.api.errorhandler.ErrorHandlerTransformer
import io.github.feelfreelinux.wykopmobilny.api.filters.OWMContentFilter
import io.github.feelfreelinux.wykopmobilny.models.dataclass.EntryLink
import io.github.feelfreelinux.wykopmobilny.models.mapper.apiv2.EntryLinkMapper
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.EntryLinkResponse
import io.github.feelfreelinux.wykopmobilny.utils.preferences.BlacklistPreferencesApi
import io.github.feelfreelinux.wykopmobilny.utils.preferences.LinksPreferencesApi
import io.github.feelfreelinux.wykopmobilny.utils.preferences.SettingsPreferencesApi
import io.reactivex.Single
import retrofit2.Retrofit

class MyWykopRepository(val retrofit: Retrofit, val userTokenRefresher: UserTokenRefresher, val owmContentFilter: OWMContentFilter) : MyWykopApi {
    private val mywykopApi by lazy { retrofit.create(MyWykopRetrofitApi::class.java) }

    override fun getIndex(page : Int): Single<List<EntryLink>> =
            mywykopApi.getIndex(page)
                    .retryWhen(userTokenRefresher)
                    .compose<List<EntryLinkResponse>>(ErrorHandlerTransformer())
                    .map { it.map { EntryLinkMapper.map(it, owmContentFilter) } }

    override fun byUsers(page : Int): Single<List<EntryLink>> =
            mywykopApi.byUsers(page)
                    .retryWhen(userTokenRefresher)
                    .compose<List<EntryLinkResponse>>(ErrorHandlerTransformer())
                    .map { it.map { EntryLinkMapper.map(it, owmContentFilter) } }

    override fun byTags(page : Int): Single<List<EntryLink>> =
            mywykopApi.byTags(page)
                    .retryWhen(userTokenRefresher)
                    .compose<List<EntryLinkResponse>>(ErrorHandlerTransformer())
                    .map { it.map { EntryLinkMapper.map(it, owmContentFilter) } }
}