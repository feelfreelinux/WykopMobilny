package io.github.feelfreelinux.wykopmobilny.api.hits

import io.github.feelfreelinux.wykopmobilny.api.UserTokenRefresher
import io.github.feelfreelinux.wykopmobilny.api.errorhandler.ErrorHandlerTransformer
import io.github.feelfreelinux.wykopmobilny.api.filters.OWMContentFilter
import io.github.feelfreelinux.wykopmobilny.api.patrons.PatronsApi
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Link
import io.github.feelfreelinux.wykopmobilny.models.mapper.apiv2.LinkMapper
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.LinkResponse
import io.reactivex.Single
import retrofit2.Retrofit

class HitsRepository(
    val retrofit: Retrofit,
    val userTokenRefresher: UserTokenRefresher,
    val owmContentFilter: OWMContentFilter,
    val patronsApi: PatronsApi
) : HitsApi {

    private val hitsApi by lazy { retrofit.create(HitsRetrofitApi::class.java) }

    override fun byMonth(year: Int, month: Int): Single<List<Link>> =
        hitsApi.byMonth(year, month)
            .retryWhen(userTokenRefresher)
            .compose<List<LinkResponse>>(ErrorHandlerTransformer())
            .map { it.map { response -> LinkMapper.map(response, owmContentFilter) } }


    override fun currentDay(): Single<List<Link>> =
        hitsApi.currentDay()
            .retryWhen(userTokenRefresher)
            .compose<List<LinkResponse>>(ErrorHandlerTransformer())
            .map { it.map { response -> LinkMapper.map(response, owmContentFilter) } }


    override fun byYear(year: Int): Single<List<Link>> =
        hitsApi.byYear(year)
            .retryWhen(userTokenRefresher)
            .compose<List<LinkResponse>>(ErrorHandlerTransformer())
            .map { it.map { response -> LinkMapper.map(response, owmContentFilter) } }


    override fun currentWeek(): Single<List<Link>> =
        hitsApi.currentWeek()
            .retryWhen(userTokenRefresher)
            .compose<List<LinkResponse>>(ErrorHandlerTransformer())
            .map { it.map { response -> LinkMapper.map(response, owmContentFilter) } }


    override fun popular(): Single<List<Link>> =
        hitsApi.popular()
            .retryWhen(userTokenRefresher)
            .compose<List<LinkResponse>>(ErrorHandlerTransformer())
            .map { it.map { response -> LinkMapper.map(response, owmContentFilter) } }
}
