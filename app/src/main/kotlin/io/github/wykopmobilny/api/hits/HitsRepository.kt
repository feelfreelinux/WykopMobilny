package io.github.wykopmobilny.api.hits

import io.github.wykopmobilny.api.UserTokenRefresher
import io.github.wykopmobilny.api.endpoints.HitsRetrofitApi
import io.github.wykopmobilny.api.errorhandler.ErrorHandlerTransformer
import io.github.wykopmobilny.api.filters.OWMContentFilter
import io.github.wykopmobilny.api.patrons.PatronsApi
import io.github.wykopmobilny.models.dataclass.Link
import io.github.wykopmobilny.models.mapper.apiv2.LinkMapper
import io.reactivex.Single
import javax.inject.Inject

class HitsRepository @Inject constructor(
    private val hitsApi: HitsRetrofitApi,
    private val userTokenRefresher: UserTokenRefresher,
    private val owmContentFilter: OWMContentFilter,
    private val patronsApi: PatronsApi
) : HitsApi {

    override fun byMonth(year: Int, month: Int): Single<List<Link>> =
        hitsApi.byMonth(year, month)
            .retryWhen(userTokenRefresher)
            .flatMap { patronsApi.ensurePatrons(it) }
            .compose(ErrorHandlerTransformer())
            .map { it.map { response -> LinkMapper.map(response, owmContentFilter) } }

    override fun currentDay(): Single<List<Link>> =
        hitsApi.currentDay()
            .retryWhen(userTokenRefresher)
            .flatMap { patronsApi.ensurePatrons(it) }
            .compose(ErrorHandlerTransformer())
            .map { it.map { response -> LinkMapper.map(response, owmContentFilter) } }

    override fun byYear(year: Int): Single<List<Link>> =
        hitsApi.byYear(year)
            .retryWhen(userTokenRefresher)
            .flatMap { patronsApi.ensurePatrons(it) }
            .compose(ErrorHandlerTransformer())
            .map { it.map { response -> LinkMapper.map(response, owmContentFilter) } }

    override fun currentWeek(): Single<List<Link>> =
        hitsApi.currentWeek()
            .retryWhen(userTokenRefresher)
            .flatMap { patronsApi.ensurePatrons(it) }
            .compose(ErrorHandlerTransformer())
            .map { it.map { response -> LinkMapper.map(response, owmContentFilter) } }

    override fun popular(): Single<List<Link>> =
        hitsApi.popular()
            .retryWhen(userTokenRefresher)
            .flatMap { patronsApi.ensurePatrons(it) }
            .compose(ErrorHandlerTransformer())
            .map { it.map { response -> LinkMapper.map(response, owmContentFilter) } }
}
